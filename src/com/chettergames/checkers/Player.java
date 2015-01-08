package com.chettergames.checkers;

public abstract class Player 
{
	public Player(Game game, int number, Piece piece, Board board)
	{
		this.game = game;
		this.number = number;
		this.piece = piece;
		this.board = board;
		name = null;
	}
	
	public void prepareForNewGame() 
	{
		new Thread(new Runnable(){
			@Override
			public void run()
			{
				promptForName();
			}
		}).start();
	}
	
	public abstract void promptForName();
	public abstract void myTurn();
	public abstract boolean isReady();
	
	public abstract void youWon(String otherPlayer);
	public abstract void youLost(String otherPlayer);
	public abstract void boardWasReset();
	
	public void moveWasMade(int row1, int col1, int row2, int col2){}
	public void pieceWasKinged(int row, int col){}
	public String getName(){return name;}
	public void setName(String name){this.name = name;}
	public int getNumber(){return number;}
	public Piece getPiece(){return piece;}
	
	protected Game game;
	protected String name;
	protected int number;
	protected Piece piece;
	protected Board board;
}
