package com.chettergames.checkers;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.util.concurrent.CountDownLatch;

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

		panel = new CheckersPanel();
		content.add(panel, BorderLayout.CENTER);
		
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

	private boolean exitOnClose;
	private JTextArea area;
	private JPanel chatPanel;
	private JScrollPane pane;
	private JTextField field;
	private String entered;
	private CountDownLatch latch;
	
	private JFrame frame;
	private CheckersPanel panel;
	
	private class CheckersPanel extends JPanel
	{
		public CheckersPanel()
		{
			super();
		}
		
		@Override
		public void paintComponent(Graphics g)
		{
			Graphics2D g2 = (Graphics2D)g;
			g2.drawRect(0, 0, 599, 599);
		}
	}
}
