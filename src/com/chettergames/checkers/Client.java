package com.chettergames.checkers;

import java.awt.Point;

import com.chettergames.net.BufferBuilder;
import com.chettergames.net.NetworkListener;

public class Client extends HumanPlayer implements NetworkListener
{
	public Client(Game game, int number, Piece piece, Board board, CheckersUI ui) 
	{
		super(game, number, piece, board, ui);
	}

	@Override
	public void messageReceived(byte[] buffer) 
	{
		BufferBuilder builder = new BufferBuilder(buffer, 0);
		
		switch(builder.pullFlag())
		{
		case REQUEST_NAME:
			ui.print("What is your name? ");
			name= ui.nextLine();
			break;
		case REQUEST_MOVE:
			
			// get the move from the player
			
			myTurn();
		
			
			break;
		case WAS_KINGED:
			int rowKinged = builder.pullInt();
			int colKinged = builder.pullInt();
			board.getPiece(rowKinged, colKinged).kingMe();
			break;
		case PLAYER_MOVE:
			int row1 = builder.pullInt();
			int col1 = builder.pullInt();
			int row2 = builder.pullInt();
			int col2 = builder.pullInt();
			board.getPiece(row1, col1);
			board.placePiece(row2, col2, piece);
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
			ui.print("You Won! Would you like to play again? (Y/n).");
			boolean playAgain = ui.prompt();
			break;
		case YOU_LOST:
			ui.print("You Lost! Would you like to play again? (Y/n)");
			playAgain = ui.prompt();
			break;
		case YOUR_PLAYER:
			break;
		}
	}
	
	@Override
	public void myTurn() 
	{
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
					int result = game.move(row1, col1, rowp, colp);
					
					switch(result)
					{
					case Game.MOVE_INVALID:
						ui.println("You can't make that move!");
						break;
					case Game.MOVE_VALID_GO_AGAIN:
						ui.println("You must skip again.");
						break;
					case Game.MOVE_VALID:
						ui.println(name + ", your turn is over.");
						return;
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

		myTurn();
	}
	
	private String name;
	private int playerNumber;
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
	public static final byte YOUR_PLAYER	= 10;
	
	public static final byte PLAY_AGAIN		= 11;
	
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
		ui.prompt();
		if(ui.prompt())
		{
			Client client = new Client(null, 0, null, ui.getBoard(), ui);
		}else{
			Game game = new Game();
			game.newHumanVSHuman(ui);
			game.readyPlayers();
			game.postGame();
		}
	}
}
