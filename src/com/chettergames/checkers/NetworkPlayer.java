package com.chettergames.checkers;

import com.chettergames.net.BufferBuilder;
import com.chettergames.net.NetworkManager;
import com.chettergames.net.Output;

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
		try
		{
			network.sendData(new byte[]{REQUEST_NAME});

			byte[] data = network.blockForFlags(new byte[]{RECEIVE_NAME});
			BufferBuilder builder = new BufferBuilder(data, 0);

			// get the name from the buffer
			name = builder.pullString();
		}catch(Exception e){Output.neterr(e);}
	}

	@Override
	public void myTurn() 
	{
		try
		{
			network.sendData(new byte[]{REQUEST_MOVE});
			boolean currentTurn = true;
			while(currentTurn)
			{
				byte[] data = network.blockForFlags(new byte[]{RECEIVE_MOVE});
				BufferBuilder builder = new BufferBuilder(data, 0);

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
			}
		}catch(Exception e){Output.neterr(e);}
	}

	@Override
	public void youWon(String otherPlayer) 
	{
		try
		{
			network.sendData(new byte[]{YOU_WON});
			byte[] message = network.blockForFlags(new byte[]{LETS_PLAY_AGAIN, DONT_PLAY_AGAIN});

			if(message[0] == LETS_PLAY_AGAIN)
			{
				// I want to play again
			}else{
				// I dont want to play again
			}
		}catch(Exception e){Output.neterr(e);}
	}

	@Override
	public void youLost(String otherPlayer) 
	{
		try
		{
			network.sendData(new byte[]{YOU_LOST});
			byte[] message = network.blockForFlags(new byte[]{LETS_PLAY_AGAIN, DONT_PLAY_AGAIN});

			if(message[0] == LETS_PLAY_AGAIN)
			{
				// I want to play again
			}else{
				// I dont want to play again
			}
		}catch(Exception e){Output.neterr(e);}
	}

	public void moveWasMade(int row1, int col1, int row2, int col2)
	{
		try
		{
			BufferBuilder buffer = new BufferBuilder(1 + 4 + 4 + 4 + 4);
			buffer.pushFlag(PLAYER_MOVE);
			buffer.pushInt(row1);
			buffer.pushInt(col1);
			buffer.pushInt(row2);
			buffer.pushInt(col2);
			network.sendData(buffer.getBuffer());
		}catch(Exception e){Output.neterr(e);}
	}

	public void pieceWasKinged(int row, int col)
	{

	}

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
