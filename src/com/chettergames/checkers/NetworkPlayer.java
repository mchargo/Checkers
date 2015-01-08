package com.chettergames.checkers;

import com.chettergames.net.NetworkManager;

public class NetworkPlayer extends Player
{
	public NetworkPlayer(Game game, int number, Piece piece, Board board, NetworkManager network) 
	{
		super(game, number, piece, board);
		this.network = network;
	}

	@Override
	public void promptForName() 
	{
		
	}

	@Override
	public void myTurn() 
	{
		
	}

	@Override
	public boolean isReady() {return false;}

	@Override
	public void youWon(String otherPlayer) {}

	@Override
	public void youLost(String otherPlayer) {}
	
	private NetworkManager network;
}
