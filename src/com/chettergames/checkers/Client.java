package com.chettergames.checkers;

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
			
			break;
		case WAS_KINGED:
			int rowKinged = builder.pullInt();
			int colKinged = builder.pullInt();
			
			break;
		case PLAYER_MOVE:
			break;
		case YOU_WON:
			break;
		case YOU_LOST:
			break;
		case YOUR_PLAYER:
			break;
		}
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
