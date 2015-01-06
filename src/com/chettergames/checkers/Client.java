package com.chettergames.checkers;

import com.chettergames.net.NetworkListener;

public class Client implements NetworkListener
{
	public Client(CheckersUI ui)
	{
		
	}
	
	@Override
	public void messageReceived(byte[] buffer) 
	{
		
	}
	
	private CheckersUI ui;
	
	public static void main(String args[])
	{
		Board board = new Board();
		CheckersUI ui = new CheckersUI(board);
		
		
	}


}
