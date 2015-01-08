package com.chettergames.checkers;

import java.awt.Point;

import com.chettergames.net.BufferBuilder;
import com.chettergames.net.NetworkListener;
import com.chettergames.net.NetworkManager;

public class Client implements NetworkListener
{
	public Client(CheckersUI ui, NetworkManager network) 
	{
		this.ui = ui;
		this.board = ui.getBoard();
		this.network = network;
	}
	
	public void clearBoard()
	{
		ui.setBoard(new Board());
		board = ui.getBoard();
	}
	
	public void newBoard()
	{
		for(int row=0; row<3; row++)
			for(int col=0; col<8; col++)
				if(board.isBlack(row, col))
					board.placePiece(row, col, new Piece(true, false));
		for(int row=5; row<8; row++)
			for(int col=0; col<8; col++)
				if(board.isBlack(row, col))
					board.placePiece(row, col, new Piece(false, false));
	}

	@Override
	public void messageReceived(byte[] buffer) 
	{
		BufferBuilder builder = new BufferBuilder(buffer, 0);

		switch(builder.pullFlag())
		{
		case EMPTY_BOARD: // no pieces on the board
			clearBoard();
			break;
		case SETUP_BOARD: // the start of the game
			newBoard();
			break;
			
		case REQUEST_NAME: // request the player's name
			ui.print("What is your name? ");
			name= ui.nextLine();
			builder = new BufferBuilder(name.length() + 5);
			builder.pushFlag(RECEIVE_NAME);
			builder.pushString(name);

			network.sendData(builder.getBuffer());
			break;
		case REQUEST_MOVE: // request the move from the player.
			ui.println(name + ", is your turn.");

			ui.disableActive();
			boolean selected = false;
			int row1 = 0;
			int col1 = 0;
			boolean turnOver = false;

			while(!turnOver)
			{
				Point p = ui.waitForClick();
				int rowp = (int)p.getX();
				int colp = (int)p.getY();

				if(selected)
				{
					if(rowp == row1 && colp == col1)
					{
						selected = false;
						ui.disableActive();
					}else{
						builder = new BufferBuilder(1 + 4 + 4 + 4 + 4);
						builder.pushFlag(RECEIVE_MOVE);
						builder.pushInt(row1);
						builder.pushInt(col1);
						builder.pushInt(rowp);
						builder.pushInt(colp);
						
						network.sendData(builder.getBuffer());

						byte[] packet = network.blockForMessage();

						//int result = game.move(row1, col1, rowp, colp);

						switch(packet[0])
						{
						case MOVE_INVALID:
							ui.println("You can't make that move!");
							break;
						case MOVE_GO_AGAIN:
							ui.println("You must skip again.");
							break;
						case MOVE_VALID:
							ui.println(name + ", your turn is over.");
							return;
						default:
							break;
						}

						selected = false;
						ui.disableActive();
					}
				}else{
					if(p.getX() >= 0 && p.getX() < 8 &&
							p.getY() >= 0 && p.getY() < 8)
					{
						ui.setActive((int)p.getX(), (int)p.getY());
						selected = true;
						row1 = rowp;
						col1 = colp;
					}
				}
			}

			break;
		case WAS_KINGED:
			int rowKinged = builder.pullInt();
			int colKinged = builder.pullInt();
			board.getPiece(rowKinged, colKinged).kingMe();
			break;
		case PLAYER_MOVE:
			row1 = builder.pullInt();
			col1 = builder.pullInt();
			int row2 = builder.pullInt();
			int col2 = builder.pullInt();
			board.placePiece(row2, col2, board.getPiece(row1, col1));
			board.placePiece(row1, col1, null);
			int rowDistance=row2-row1;
			int colDistance=col2-col1;
			int middleRow=rowDistance/2+row1;
			int middleCol=colDistance/2+col1;
			if(rowDistance==1&&colDistance==1)
			{
				break;
			}else{
				board.placePiece(middleRow, middleCol, null);
			}
			break;
		case YOU_WON:
			ui.print("You Won! Would you like to play again? ");
			playAgainPrompt();
			break;
		case YOU_LOST:
			ui.print("You Lost! Would you like to play again? ");
			playAgainPrompt();
		case YOUR_PLAYER_NUM:
			break;
		case YOUR_COLOR:
			if(builder.pullString().equalsIgnoreCase("red"))
				ui.println("You are the Dark pieces.");
			else ui.println("You are the Light pieces.");
			break;
		}
	}

	public void playAgainPrompt()
	{
		boolean playAgain = ui.prompt();
		BufferBuilder builder = new BufferBuilder(1);

		if(playAgain)
			builder.pushFlag(LETS_PLAY_AGAIN);
		else builder.pushFlag(DONT_PLAY_AGAIN);

		network.sendData(builder.getBuffer());
	}

	private NetworkManager network;
	private String name;
	private CheckersUI ui;
	private Board board;
	
	// Network Flags

	// From Server to Client
	public static final byte REQUEST_NAME 	= 0;
	public static final byte REQUEST_MOVE 	= 1;

	public static final byte MOVE_VALID 	= 2;
	public static final byte MOVE_INVALID 	= 3;
	public static final byte MOVE_GO_AGAIN 	= 4;

	public static final byte WAS_KINGED 	= 5;
	public static final byte PLAYER_MOVE	= 6;  // the opponent has moved
	public static final byte YOU_WON		= 7;
	public static final byte YOU_LOST		= 8;
	public static final byte YOUR_COLOR		= 9;
	public static final byte YOUR_PLAYER_NUM= 10;

	public static final byte PLAY_AGAIN		= 11;
	public static final byte SETUP_BOARD	= 12;
	public static final byte EMPTY_BOARD	= 13;

	// From Client to Server
	public static final byte RECEIVE_NAME 	= 0;
	public static final byte RECEIVE_MOVE 	= 1;

	public static final byte LETS_PLAY_AGAIN = 2;
	public static final byte DONT_PLAY_AGAIN = 3;

	public static void main(String args[])
	{
		Board board = new Board();
		CheckersUI ui = new CheckersUI(board);
		ui.print("Play on the line? ");
		if(ui.prompt())
		{
			String address = "localhost";
			int port = 10000;

			NetworkManager manager = new NetworkManager(address, port);
			manager.connect();
			new Client(ui, manager);
		}else{
			Game game = new Game();
			game.newHumanVSHuman(ui);
			game.readyPlayers();
			game.postGame();
		}
	}
}
