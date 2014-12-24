package com.chettergames.checkers;

public class Piece 
{
	public Piece(boolean isRed, boolean isKing)
	{
		this.isRed = isRed;
		this.isKing = isKing;
	}
	
	public boolean isRed() {
		return isRed;
	}
	public boolean isKing() {
		return isKing;
	}

	private boolean isRed;
	private boolean isKing;
}
