package com.chettergames.checkers;

public class HumanPlayer extends Player 
{
	public HumanPlayer(Game game, int number) 
	{
		super(game, number);
	}

	@Override
	public void prepareForNewGame() {}

	@Override
	public void promptForName() {}

	@Override
	public void myTurn() {}

	@Override
	public void youWon(String otherPlayer) {}

	@Override
	public void youLost(String otherPlayer) {}
}
