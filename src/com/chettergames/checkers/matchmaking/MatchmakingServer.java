package com.chettergames.checkers.matchmaking;

import java.net.Socket;

import com.chettergames.checkers.Game;
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
		if(player1==null)
		{
			player1= new NetworkManager(socket);
		}else if(player2==null){
			player2= new NetworkManager(socket);
		}else{
			Game game = new Game();
			game.newNetworkVsNetwork(player1, player2);
			game.readyPlayers();
			game.postGame();
		}
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
