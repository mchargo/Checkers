package com.chettergames.checkers.net;

public class BufferBuilder 
{
	public BufferBuilder(int size)
	{
		buffer = new byte[size];
		index = 0;
	}
	
	public BufferBuilder(byte[] buffer, int index)
	{
		this.buffer = buffer;
		this.index = index;
	}
	
	public void pushInt(int i)
	{
		
	}
	
	public byte pullInt()
	{
		return 0;
	}
	
	public void pushFlag(byte flag)
	{
		
	}
	
	public byte pullFlag()
	{
		return 0;
	}
	
	public void pushByteArr(byte[] arr)
	{
		
	}
	
	public byte[] pullByteArr()
	{
		return null;
	}
	
	public void pushString(String string)
	{
		
	}
	
	public String pullString()
	{
		return "";
	}
	
	public void pushStringArr(String arr[])
	{
		
	}
	
	public String[] pullStringArr()
	{
		return null;
	}
	
	private byte[] buffer;
	private int index;
}
