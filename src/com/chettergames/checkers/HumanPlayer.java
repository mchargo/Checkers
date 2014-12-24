package com.chettergames.checkers;

public class HumanPlayer extends Player 
{
	public HumanPlayer(Game game, int number, Piece piece) 
	{
		super(game, number, piece);
	}

	@Override
	public void prepareForNewGame() {}

	@Override
	public void promptForName() 
	{
	
	}
	@Override
	public void myTurn() 
	{
		System.out.println(number + " My Turn");
	}

	@Override
	public void youWon(String otherPlayer) {}

	@Override
	public void youLost(String otherPlayer) {}
}
