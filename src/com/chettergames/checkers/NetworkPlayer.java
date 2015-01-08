package com.chettergames.checkers;

public class NetworkPlayer extends Player
{
	public NetworkPlayer(Game game, int number, Piece piece, Board board) 
	{
		super(game, number, piece, board);
	}

	@Override
	public void promptForName() 
	{
		
	}

	@Override
	public void myTurn() {}

	@Override
	public boolean isReady() {return false;}

	@Override
	public void youWon(String otherPlayer) {}

	@Override
	public void youLost(String otherPlayer) {}
}
