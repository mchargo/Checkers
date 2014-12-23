package com.chettergames.checkers;

public class Game 
{
	public void newHumanVSHuman()
	{
		
	}
	
	public void checkForWin()
	{
		
	}
	public void playGame()
	{
		
	}
	
	public static int MOVE_VALID 			= 0;
	public static int MOVE_INVALID 			= 1;
	public static int MOVE_VALID_GO_AGAIN 	= 2;
	public int move(int row1, int col1, int row2, int col2)
	{ 
		return MOVE_VALID;
	}
	public boolean wasJump(int row1, int col1, int row2, int col2)
	{
		return true; 
	}
	public boolean canDoMoreJumps(int row, int col)
	{
		return false;
	}
	
	public Board getBoard(){return board;}
	
	private Player player1;
	private Player player2;
	private Player currentPlayer;
	private Board board;
	
	private boolean isJumping;
	private int activeRow;
	private int activeColumn;
	
	public static int DIRECTION_UP = 0;
	public static int DIRECTION_DOWN = 0;
	
	public static void main(String[] args) 
	{
		
	}
}
