package com.chettergames.checkers;

public class Board 
{
	public Board()
	{
		pieces = new Piece[8][8];
	}
	
	public Piece getPiece(int row, int column)
	{
		return pieces[row][column];
	}
	
	public void placePiece(int row, int column, Piece piece)
	{
		pieces[row][column] = piece;
	}
	
	public boolean isBlack(int row, int col)
	{
		boolean rowIsOdd=row%2==1;
		boolean colIsOdd=col%2==1;
		if(rowIsOdd==colIsOdd)
		{
			return false;
		}
		return true;
	}
	
	public void printBoard()
	{
		for(int row = 0;row < 8;row++)
		{
			System.out.print("|");
			for(int column = 0;column < 8;column++)
			{
				Piece piece = pieces[row][column];
				
				if(piece == null)
					System.out.print(" ");
				else if(piece.isRed())
				{
					if(piece.isKing())
					{
						System.out.print("R");
					}else{
						System.out.print("r");
					}
				}else{
					if(piece.isKing())
					{
						System.out.print("B");
					}else{
						System.out.print("b");
					}
				}
				System.out.print("|");
			}
			System.out.println();
		}
	}
	
	public Piece pieces[][];
}
