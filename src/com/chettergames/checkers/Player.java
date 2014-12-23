package com.chettergames.checkers;

public abstract class Player 
{
	public Player(Game game)
	{
		this.game = game;
	}
	
	public abstract void prepareForNewGame();
	public abstract void promptForName();
	public abstract void myTurn();
	
	public abstract void youWon(String otherPlayer);
	public abstract void youLost(String otherPlayer);
	
	public String getName(){return name;}
	public void setName(String name){this.name = name;}
	
	private Game game;
	private String name;
}
