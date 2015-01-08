package com.chettergames.checkers;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.concurrent.CountDownLatch;

import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.text.DefaultCaret;

public class CheckersUI implements WindowListener
{
	public CheckersUI(Board board)
	{
		frame = new JFrame("Chetter-Games: Checkers");
		JPanel content = new JPanel();
		content.setLayout(new BorderLayout());

		int rows = 33;
		int columns = 23;

		latch = new CountDownLatch(1);
		chatPanel = new JPanel();
		chatPanel.setPreferredSize(new Dimension(200, 600));
		chatPanel.setLayout(new BorderLayout());
		field = new JTextField(columns);
		field.setFont( new Font("monospaced", Font.PLAIN, 14) );
		// enable auto scrolling
		field.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				entered = field.getText();
				field.setText("");
				latch.countDown();
			}
		});

		area = new JTextArea(rows, columns);
		area.setWrapStyleWord(true);
		area.setLineWrap(true);
		area.setEditable(false);
		area.setFont( new Font("monospaced", Font.PLAIN, 14) );
		DefaultCaret caret = (DefaultCaret)area.getCaret();
		caret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);
		pane = new JScrollPane(area);
		pane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);

		JPanel southLayout = new JPanel();
		southLayout.add(field);
		chatPanel.add(southLayout, BorderLayout.SOUTH);

		JPanel centerLayout = new JPanel();
		centerLayout.add(pane);
		chatPanel.add(centerLayout, BorderLayout.CENTER);

		panel = new CheckersPanel(board);
		panel.loadImages();

		frame.addWindowListener(this);

		content.add(panel, BorderLayout.CENTER);
		content.add(chatPanel, BorderLayout.EAST);

		frame.getContentPane().add(content);

		frame.getContentPane().setPreferredSize(new Dimension(800, 610));
		frame.pack();
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);

		field.requestFocus();
	}
	
	public Board getBoard()
	{
		return panel.board;
	}

	public void setBoard(Board board)
	{
		panel.board = board;
	}
	
	@Override public void windowActivated(WindowEvent e) {}
	@Override public void windowClosed(WindowEvent e){}
	@Override public void windowDeactivated(WindowEvent e){}
	@Override public void windowDeiconified(WindowEvent e) {}
	@Override public void windowIconified(WindowEvent e){}
	@Override public void windowOpened(WindowEvent e){}

	@Override
	public void windowClosing(WindowEvent e) 
	{
		if(exitOnClose) System.exit(0);
	}

	public void print(final String s)
	{
		Runnable run = new Runnable()
		{
			public void run()
			{
				area.append(s);
				area.repaint();
			}
		};

		SwingUtilities.invokeLater(run);
	}

	public void println(){println("");}
	public void println(final String s)
	{
		Runnable run = new Runnable()
		{
			public void run()
			{
				area.append(s + "\n");
				area.repaint();
			}
		};

		SwingUtilities.invokeLater(run);
	}

	public String nextLine()
	{
		try 
		{
			latch.await();
			resetLatch();
			area.append(entered + "\n");
			return entered.trim();
		} catch (InterruptedException e) 
		{
			System.out.println("Error occurred: " + e.toString());
			e.printStackTrace();
		}

		return null;
	}

	public int nextInt()
	{
		while(true)
		{
			try
			{
				return Integer.valueOf(nextLine());
			}catch(Exception e){}
		}
	}

	public long nextLong()
	{
		while(true)
		{
			try
			{
				return Long.valueOf(nextLine());
			}catch(Exception e){}
		}
	}

	public float nextFloat()
	{
		while(true)
		{
			try
			{
				return Float.valueOf(nextLine());
			}catch(Exception e){}
		}
	}

	public double nextDouble()
	{
		while(true)
		{
			try
			{
				return Double.valueOf(nextLine());
			}catch(Exception e){}
		}
	}

	public short nextShort()
	{
		while(true)
		{
			try
			{
				return Short.valueOf(nextLine());
			}catch(Exception e){}
		}
	}

	public void clear()
	{
		if(Thread.currentThread().getId() == 1) // main thread
		{
			area.setText("");
		}else{
			// not main thread
			SwingUtilities.invokeLater(new Runnable()
			{
				public void run()
				{
					area.setText("");
				}
			});
		}
	}

	public boolean prompt()
	{
		return nextLine().toLowerCase().startsWith("y");
	}

	public void resetLatch()
	{
		latch = new CountDownLatch(1);
	}

	public Point waitForClick()
	{
		return panel.getClick();
	}

	public void setActive(int row, int col)
	{
		panel.activeRow = row;
		panel.activeCol = col;
		panel.active = true;
	}

	public Point getActive()
	{
		if(panel.active)
			return new Point(panel.activeRow, panel.activeCol);
		else return null;
	}
	
	public void disableActive()
	{
		panel.active = false;
	}

	private boolean exitOnClose;
	private JTextArea area;
	private JPanel chatPanel;
	private JScrollPane pane;
	private JTextField field;
	private String entered;
	private CountDownLatch latch;

	private JFrame frame;
	private CheckersPanel panel;

	private class CheckersPanel extends JPanel implements MouseListener
	{
		public CheckersPanel(Board board)
		{
			super();
			this.board = board;
			loaded = false;
			squareSize = 600.00f / 8.00f;
			vsync = 60;
			active = false;
			this.startRepaintLoop();
			this.addMouseListener(this);
		}

		@Override
		public void paintComponent(Graphics g)
		{
			if(!loaded) return;
			Graphics2D g2 = (Graphics2D)g;
			g2.drawRect(0, 0, 599, 599);

			for(int row = 0;row < 8;row++)
			{
				for(int col = 0;col < 8;col++)
				{
					int x = (int)((float)col * squareSize);
					int y = (int)((float)row * squareSize);

					if(!board.isBlack(row, col))
						g2.drawImage(lightSquare, x, y, (int)squareSize, (int)squareSize, null);
					else {
						g2.drawImage(darkSquare, x, y, (int)squareSize, (int)squareSize, null);

						Piece piece = board.getPiece(row, col);

						if(piece == null) continue;
						if(piece.isRed())
						{
							g2.drawImage(lightChecker, x, y, (int)squareSize, (int)squareSize, null);
						}else {
							g2.drawImage(darkChecker, x, y, (int)squareSize, (int)squareSize, null);
						}
						
						if(piece.isKing()) 
							g2.drawImage(king, x, y - 2, (int)squareSize, (int)squareSize, null);
					}
					
					if(active && activeRow == row && activeCol == col)
					{
						g2.setColor(Color.yellow);
						g2.drawRect(x, y, (int)squareSize - 1, (int)squareSize - 1);
					}
				}
			}
		}

		public void loadImages()
		{
			if(darkChecker == null)
			{
				File darkCheckerFile = new File("img/dark-checker.png");
				File lightCheckerFile = new File("img/light-checker.png");
				File darkSquareFile = new File("img/dark-square.png");
				File lightSquareFile = new File("img/light-square.png");
				File kingFile = new File("img/king.png");

				try
				{
					darkChecker = ImageIO.read(darkCheckerFile);
					lightChecker = ImageIO.read(lightCheckerFile);
					darkSquare = ImageIO.read(darkSquareFile);
					lightSquare = ImageIO.read(lightSquareFile);
					king = ImageIO.read(kingFile);
				}catch(Exception e)
				{
					System.out.println("Could not load images.");
				}

				darkCheckerFile = null;
				lightCheckerFile = null;
				darkSquareFile = null;
				lightSquareFile = null;
				kingFile = null;
				loaded = true;
			}
		}

		@Override public void mouseClicked(MouseEvent e) {}
		@Override public void mouseReleased(MouseEvent e) {}
		@Override public void mouseEntered(MouseEvent e) {}
		@Override public void mouseExited(MouseEvent e) {}
		@Override 
		public void mousePressed(MouseEvent e) 
		{
			int row = (int)(e.getY() / squareSize);
			int col = (int)(e.getX() / squareSize);

			point = new Point(row, col);

			try
			{
				latch.countDown();
			}catch(Exception ex){}
		}
		
		public void startRepaintLoop()
		{
			if(repaintThread != null ||
					repainting == true) return;
			
			repaintThread = new Thread(new Runnable()
			{
				@Override
				public void run()
				{
					if(repainting) return;
					
					repainting = true;
					
					while(repainting)
					{
						try
						{
							Thread.sleep(1000 / vsync);
							CheckersPanel.this.repaint();
						}catch(Exception e){}
					}
					
				}
			});
			repaintThread.start();
		}
		
		public Point getClick()
		{
			latch = new CountDownLatch(1);

			try
			{
				latch.await();
			}catch(Exception e){}
			latch = null;

			return point;
		}

		private CountDownLatch latch;
		private Point point;

		private BufferedImage darkChecker;
		private BufferedImage lightChecker;
		private BufferedImage darkSquare;
		private BufferedImage lightSquare;
		private BufferedImage king;
		
		private Board board;
		private boolean loaded;
		private float squareSize;

		private boolean active;
		private int activeRow;
		private int activeCol;
		
		private boolean repainting;
		private Thread repaintThread;
		
		private int vsync;
	}
}
