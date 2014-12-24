package com.chettergames.checkers;
import java.util.*;

public class HumanPlayer extends Player 
{
	public HumanPlayer(Game game, int number, int piece) 
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
	public void myTurn() {}

	@Override
	public void youWon(String otherPlayer) {}

	@Override
	public void youLost(String otherPlayer) {}
}
