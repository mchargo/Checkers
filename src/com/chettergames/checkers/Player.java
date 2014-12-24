package com.chettergames.checkers;

public abstract class Player 
{
	public Player(Game game, int number, Piece piece)
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
	public Piece getPiece(){return piece;}
	
	protected Game game;
	protected String name;
	protected int number;
	protected Piece piece;
}
