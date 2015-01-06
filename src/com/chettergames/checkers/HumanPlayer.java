package com.chettergames.checkers;

import java.awt.Point;

public class HumanPlayer extends Player 
{
	public HumanPlayer(Game game, int number, Piece piece, Board board, CheckersUI ui) 
	{
		super(game, number, piece, board);
		this.ui = ui;
	}

	@Override
	public boolean isReady() 
	{
		return name != null;
	}

	@Override
	public void promptForName() 
	{
		if(name != null) return;
		System.out.println("Prompting: " + number);
		ui.print("Player " + number + ", what is your name? ");
		name = ui.nextLine();
	}

	@Override
	public void myTurn() 
	{
		ui.println(name + ", is your turn.");

		ui.disableActive();
		boolean selected = false;
		int row1 = 0;
		int col1 = 0;
		boolean turnOver = false;

		while(!turnOver)
		{
			Point p = ui.waitForClick();
			int rowp = (int)p.getX();
			int colp = (int)p.getY();

			if(selected)
			{
				if(rowp == row1 && colp == col1)
				{
					selected = false;
					ui.disableActive();
				}else{
					int result = game.move(row1, col1, rowp, colp);
					
					switch(result)
					{
					case Game.MOVE_INVALID:
						ui.println("You can't make that move!");
						break;
					case Game.MOVE_VALID_GO_AGAIN:
						ui.println("You must skip again.");
						break;
					case Game.MOVE_VALID:
						ui.println(name + ", your turn is over.");
						return;
					}
					
					selected = false;
					ui.disableActive();
				}
			}else{
				if(p.getX() >= 0 && p.getX() < 8 &&
						p.getY() >= 0 && p.getY() < 8)
				{
					ui.setActive((int)p.getX(), (int)p.getY());
					selected = true;
					row1 = rowp;
					col1 = colp;
				}
			}
		}

		myTurn();
	}

	@Override
	public void youWon(String otherPlayer) 
	{
		ui.println(name + " has won.");
	}

	@Override
	public void youLost(String otherPlayer) 
	{
		ui.println(name + " has lost.");
	}

	private CheckersUI ui;
}
