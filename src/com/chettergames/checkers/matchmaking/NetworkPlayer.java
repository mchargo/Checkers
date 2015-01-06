package com.chettergames.checkers.matchmaking;

import com.chettergames.checkers.Board;
import com.chettergames.checkers.Game;
import com.chettergames.checkers.Piece;
import com.chettergames.checkers.Player;
import com.chettergames.net.NetworkManager;

public class NetworkPlayer extends Player
{
	public NetworkPlayer(Game game, int number, Piece piece, Board board, NetworkManager network) 
	{
		super(game, number, piece, board);
		this.network = network;
	}

	@Override
	public void promptForName() {}

	@Override
	public void myTurn() {}

	@Override
	public boolean isReady() {return false;}

	@Override
	public void youWon(String otherPlayer) {}

	@Override
	public void youLost(String otherPlayer) {}
	
	private NetworkManager network;
	
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
}
