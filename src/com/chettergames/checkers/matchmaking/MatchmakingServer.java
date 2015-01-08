package com.chettergames.checkers.matchmaking;

import java.net.Socket;

import com.chettergames.checkers.Game;
import com.chettergames.net.NetworkManager;
import com.chettergames.net.Output;
import com.chettergames.net.Server;
import com.chettergames.net.ServerListener;

public class MatchmakingServer implements ServerListener
{
	public MatchmakingServer()
	{
		server = new Server(PORT);
		server.setServerListener(this);
	}
	
	public void start()
	{
		server.start();
	}
	
	@Override
	public void clientAccepted(Socket socket) 
	{
		Output.net("Handing player...\t");
		if(player1==null)
		{
			Output.netln("Player1 set.");
			player1= new NetworkManager(socket);
			player1.connect();
		}else if(player2==null){
			Output.netln("Player2 set.");
			player2= new NetworkManager(socket);
			player2.connect();
			
			Output.net("Starting new game...\t\t");
			Game game = new Game();
			game.newNetworkVsNetwork(player1, player2);
			game.readyPlayers();
			game.postGame();
			Output.netok();
		}else{
			Output.net("Starting new game...\t\t");
			Game game = new Game();
			Output.netok();
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
