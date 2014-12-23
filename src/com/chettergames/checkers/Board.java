package com.chettergames.checkers;

import java.util.Arrays;

public class Board 
{
	public Board()
	{
		board = new int[8][8];
		for(int x = 0;x < 8;x++)
			Arrays.fill(board[x], Piece.NONE);
	}
	
	public int getPiece(int row, int column)
	{
		return board[row][column];
	}
	
	public void placePiece(int row, int column, int piece)
	{
		board[row][column] = piece;
	}
	
	public void printBoard()
	{
		for(int row = 0;row < 8;row++)
		{
			System.out.print("|");
			for(int column = 0;column < 8;column++)
			{
				switch(board[row][column])
				{
				case Piece.NONE:
					System.out.print(" ");
					break;
				case Piece.RED:
					System.out.print("R");
					break;
				case Piece.BLACK:
					System.out.print("B");
					break;
				case Piece.RED_KING:
					System.out.print("r");
					break;
				case Piece.BLACK_KING:
					System.out.print("b");
					break;
				}
				System.out.print("|");
			}
			System.out.println();
		}
	}
	
	public int board[][];
}
