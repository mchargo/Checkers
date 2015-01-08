package com.chettergames.checkers;

import com.chettergames.net.BufferBuilder;
import com.chettergames.net.NetworkManager;

public class NetworkPlayer extends Player
{
	public NetworkPlayer(Game game, int number, Piece piece, Board board, NetworkManager network) 
	{
		super(game, number, piece, board);
		this.network = network;
		name = null;
	}

	@Override
	public boolean isReady() 
	{
		return name != null;
	}

	@Override
	public void promptForName() 
	{
		network.sendData(new byte[]{REQUEST_NAME});

		BufferBuilder builder = new BufferBuilder(network.blockForMessage(), 0);

		// get the name from the buffer
		if(builder.pullFlag() == RECEIVE_NAME)
		{
			name = builder.pullString();
		} else promptForName();
	}

	@Override
	public void myTurn() 
	{
		network.sendData(new byte[]{REQUEST_MOVE});
		boolean currentTurn = true;
		while(currentTurn)
		{
			BufferBuilder builder = new BufferBuilder(network.blockForMessage(), 0);
			if(builder.pullFlag() == RECEIVE_MOVE)
			{
				int row1 = builder.pullInt();
				int col1 = builder.pullInt();
				int row2 = builder.pullInt();
				int col2 = builder.pullInt();
				int result = game.move(row1, col1, row2, col2);
				switch(result)
				{
				case Game.MOVE_INVALID:
					network.sendData(new byte[]{MOVE_INVALID});
					break;
				case Game.MOVE_VALID_GO_AGAIN:
					network.sendData(new byte[]{MOVE_GO_AGAIN});
					break;
				case Game.MOVE_VALID:
					network.sendData(new byte[]{MOVE_VALID});
					currentTurn = false;
					return;
				}


			} else myTurn();
		}
	}

	@Override
	public void youWon(String otherPlayer) 
	{
		network.sendData(new byte[]{YOU_WON});
		
		byte[] message = network.blockForFlags(new byte[]{LETS_PLAY_AGAIN, DONT_PLAY_AGAIN});
		
		if(message[0] == PLAY_AGAIN)
		{
			// I want to play again
		}else{
			// I dont want to play again
		}
	}

	@Override
	public void youLost(String otherPlayer) 
	{
		network.sendData(new byte[]{YOU_LOST});
		
		byte[] message = network.blockForFlags(new byte[]{LETS_PLAY_AGAIN, DONT_PLAY_AGAIN});
		
		if(message[0] == PLAY_AGAIN)
		{
			// I want to play again
		}else{
			// I dont want to play again
		}
	}
	
	public void moveWasMade(int row1, int col1, int row2, int col2){}
	public void pieceWasKinged(int row, int col){}

	private NetworkManager network;

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
}
