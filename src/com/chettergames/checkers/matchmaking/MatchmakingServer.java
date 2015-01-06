package com.chettergames.checkers.matchmaking;

import java.net.Socket;

import com.chettergames.net.NetworkManager;
import com.chettergames.net.Server;
import com.chettergames.net.ServerListener;

public class MatchmakingServer implements ServerListener
{
	public MatchmakingServer()
	{
		server = new Server(PORT);
	}
	
	public void start()
	{
		server.start();
	}
	
	@Override
	public void clientAccepted(Socket socket) 
	{
		
	}
	
	private NetworkManager player1;
	private NetworkManager player2;
	
	private Server server;
	
	public static void main(String args[])
	{
		MatchmakingServer server = new MatchmakingServer();
		server.start();
	}
	
	private static final int PORT = 10000;
}
