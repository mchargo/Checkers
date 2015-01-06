package com.chettergames.checkers;

public class Game 
{
	public Game(){}

	public void newHumanVSHuman()
	{
		board = new Board();
		this.newBoard();
		CheckersUI ui = new CheckersUI(board);
		player1 = new HumanPlayer(this, 1, new Piece(false, false), board, ui);
		player1.promptForName();
		player2 = new HumanPlayer(this, 2, new Piece(true, false), board, ui);
		player2.promptForName();
	}
	
	public void readyPlayers()
	{
		player1.prepareForNewGame();
		player2.prepareForNewGame();
		
		while(!player1.isReady() || !player2.isReady())
		{
			try
			{
				Thread.sleep(100);
			}catch(Exception e){}
		}
	}

	public boolean checkForWin()
	{
		boolean blackFound=false;
		boolean redFound=false;
		for(int row=0; row<8; row++)
		{
			for(int col=0; col<8; col++)
			{
				Piece piece = board.getPiece(row, col);
				if(piece==null)
				{
					continue;
				}
				if(piece.isRed())
				{
					redFound=true;
				}
				if(!piece.isRed())
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
			return false;
		}else if(redFound){
			return currentPlayer.getPiece().isRed();
		}else if(blackFound){
			return !currentPlayer.getPiece().isRed();
		}
		
		throw new RuntimeException("Illegal state exception");
	}

	public void newBoard()
	{
		for(int row=0; row<3; row++)
		{
			for(int col=0; col<8; col++)
			{
				if(board.isBlack(row, col))
				{
					board.placePiece(row, col, new Piece(true, false));
				}
			}
		}
		for(int row=5; row<8; row++)
		{
			for(int col=0; col<8; col++)
			{
				if(board.isBlack(row, col))
				{
					board.placePiece(row, col, new Piece(false, false));
				}
			}
		}

	}

	public void postGame()
	{
		new Thread(new Runnable()
		{
			public void run()
			{
				playGame();
			}
		}).start();
	}

	public void playGame()
	{
		while(true)
		{
			currentPlayer=player1;
			player1.myTurn();
			if(checkForWin())
			{
				player1.youWon(player2.getName());
				player2.youLost(player1.getName());
				break;
			}
			
			currentPlayer=player2;
			player2.myTurn();
			if(checkForWin())
			{
				player2.youWon(player1.getName());
				player1.youLost(player2.getName());
				break;
			}
		}
	}

	public static final int MOVE_VALID 			= 0;
	public static final int MOVE_INVALID 			= 1;
	public static final int MOVE_VALID_GO_AGAIN 	= 2;

	public int move(int row1, int col1, int row2, int col2)
	{ 
		if(wasMove(row1, col1, row2, col2))
		{
			Piece piece = board.getPiece(row1, col1);
			board.placePiece(row2, col2, board.getPiece(row1, col1));
			board.placePiece(row1, col1, null);
			
			if(piece.isRed() && row2 == 7)
				piece.kingMe();
			else if(!piece.isRed() && row2 == 0)
				piece.kingMe();
			
			return MOVE_VALID;
		}else if(wasJump(row1, col1, row2, col2)){
			
			Piece piece = board.getPiece(row1, col1);
			
			int rowDistance=row2-row1;
			int colDistance=col2-col1;
			int middleRow=rowDistance/2+row1;
			int middleCol=colDistance/2+col1;
			
			board.placePiece(row2, col2, board.getPiece(row1, col1));
			board.placePiece(row1, col1, null);
			board.placePiece(middleRow, middleCol, null);
			
			if(piece.isRed() && row2 == 7)
				piece.kingMe();
			else if(!piece.isRed() && row2 == 0)
				piece.kingMe();
			
			
			if(canDoMoreJumps(row2, col2))
			{
				return MOVE_VALID_GO_AGAIN;
			}else{
				return MOVE_VALID;
			}
		} else {
			return MOVE_INVALID;
		}
	}
	
	public boolean wasMove(int row1, int col1, int row2, int col2)
	{
		if(!(row1>=0&&row1<=7)||!(col1>=0&&col1<=7)||!(row2>=0&&row2<=7)||!(col2>=0&&col2<=7))
		{
			return false;
		}
		if(board.getPiece(row1, col1)==null||!(board.getPiece(row2, col2)==null))
			return false;
		
		boolean currentPieceIsRed=currentPlayer.getPiece().isRed();
		boolean currentPieceIsKing=board.getPiece(row1, col1).isKing();

		int rowDistance=row2-row1;
		int colDistance=col2-col1;
		int rowLength=Math.abs(rowDistance);
		int colLength=Math.abs(colDistance);
		if(rowLength!=1||colLength!=1)
		{
			return false;
		}

		if(!currentPieceIsKing)
		{
			if(currentPieceIsRed)
			{
				if(rowDistance<0)
				{
					return false;
				}
			}else{
				if(rowDistance>0)
				{
					return false;
				}
			}
		}
		if(currentPieceIsRed!=board.getPiece(row1, col1).isRed())
		{
			return false;
		}
		return true; 
	}

	public boolean wasJump(int row1, int col1, int row2, int col2)
	{	
		if(!(row1>=0&&row1<=7)||!(col1>=0&&col1<=7)||!(row2>=0&&row2<=7)||!(col2>=0&&col2<=7))
		{
			return false;
		}
		if(board.getPiece(row1, col1)==null||!(board.getPiece(row2, col2)==null))
			return false;
		
		boolean currentPieceIsRed=currentPlayer.getPiece().isRed();
		boolean currentPieceIsKing=board.getPiece(row1, col1).isKing();

		int rowDistance=row2-row1;
		int colDistance=col2-col1;
		int rowLength=Math.abs(rowDistance);
		int colLength=Math.abs(colDistance);
		if(rowLength!=2||colLength!=2)
		{
			return false;
		}

		if(!currentPieceIsKing)
		{
			if(currentPieceIsRed)
			{
				if(rowDistance<0)
				{
					return false;
				}
			}else{
				if(rowDistance>0)
				{
					return false;
				}
			}
		}

		int middleRow=rowDistance/2+row1;
		int middleCol=colDistance/2+col1;
		Piece middlePiece =board.getPiece(middleRow, middleCol);
		if(middlePiece==null)
		{
			return false;
		}
		if(middlePiece.isRed()==currentPieceIsRed)
		{
			return false;
		}
		if(currentPieceIsRed!=board.getPiece(row1, col1).isRed())
		{
			return false;
		}
		return true; 
	}

	public boolean canDoMoreJumps(int row, int col)
	{
		int possibility1Row=row+2;
		int possibility1Col=col+2;
		int possibility2Row=possibility1Row;
		int possibility2Col=col-2;
		int possibility3Row=row-2;
		int possibility3Col=possibility1Col;
		int possibility4Row=possibility3Row;
		int possibility4Col=possibility2Col;
		if(wasJump(row, col, possibility1Row, possibility1Col))
		{
			return true;
		}
		if(wasJump(row, col, possibility2Row, possibility2Col))
		{
			return true;
		}
		if(wasJump(row, col, possibility3Row, possibility3Col))
		{
			return true;
		}
		if(wasJump(row, col, possibility4Row, possibility4Col))
		{
			return true;
		}
		return false;
	}

	public Board getBoard(){return board;}

	private Player player1;
	private Player player2;
	private Player currentPlayer;
	private Board board;

	public static void main(String[] args) 
	{
		Game game = new Game();
		game.newHumanVSHuman();
		game.readyPlayers();
		game.postGame();
	}
}
