package com.chettergames.checkers;

public class Game 
{
	public Game(){}

	public void newHumanVSHuman()
	{
		board = new Board();
		player1 = new HumanPlayer(this, 1);
		player2 = new HumanPlayer(this, 2);
	}

	public static final int NO_WIN = 0;
	public static final int RED_WIN = 1;
	public static final int BLACK_WIN = 2;

	public int checkForWin()
	{
		boolean blackFound=false;
		boolean redFound=false;
		for(int row=0; row<8; row++)
		{
			for(int col=0; col<8; col++)
			{

				int piece = board.getPiece(row, col);
				if(piece==Piece.RED||piece==Piece.RED_KING)
				{
					redFound=true;
				}
				if(piece==Piece.BLACK||piece==Piece.BLACK_KING)
				{
					blackFound=true;
				}
				if(redFound&&blackFound)
				{
					break;
				}
			}
			if(redFound&&blackFound)
			{
				break;
			}
		}
		if(redFound&&blackFound)
		{
			return NO_WIN;
		}else if(redFound&&!blackFound){
			return RED_WIN;
		}else if(!redFound&&blackFound){
			return BLACK_WIN;
		}
		return 0;
	}

	public void postGame()
	{
		new Thread(new Runnable()
		{
			public void run()
			{
				playGame();
			}
		});
	}

	public void playGame()
	{
		boolean playing = true;

		while(playing)
		{

		}
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
