package com.chettergames.checkers;

public abstract class Player 
{
	public Player(Game game, int number, int piece)
	{
		this.game = game;
		this.number = number;
		this.piece = piece;
	}
	
	public abstract void prepareForNewGame();
	public abstract void promptForName();
	public abstract void myTurn();
	
	public abstract void youWon(String otherPlayer);
	public abstract void youLost(String otherPlayer);
	
	public String getName(){return name;}
	public void setName(String name){this.name = name;}
	public int getNumber(){return number;}
	public int getPiece(){return piece;}
	
	private Game game;
	private String name;
	private int number;
	private int piece;
}
