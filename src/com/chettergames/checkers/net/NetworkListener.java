package com.chettergames.checkers.net;

public interface NetworkListener 
{
	void messageReceived(byte[] buffer);
}
