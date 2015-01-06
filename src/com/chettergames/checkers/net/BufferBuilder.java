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
		pushInt(arr.length);
		for(int x = 0;x < arr.length;x++)
			buffer[index + x] = arr[x];
		index += arr.length;
	}
	
	public byte[] pullByteArr()
	{
		byte[] arr = new byte[pullInt()];
		
		for(int x = 0;x < arr.length;x++)
			arr[x] = buffer[index + x];
		index += arr.length;
		
		return arr;
	}
	
	public void pushString(String string)
	{
		pushByteArr(string.getBytes());
	}
	
	public String pullString()
	{
		return new String(pullByteArr());
	}
	
	public void pushStringArr(String arr[])
	{
		pushInt(arr.length);
		for(String s : arr) pushString(s);
	}
	
	public String[] pullStringArr()
	{
		String strings[] = new String[pullInt()];
		for(int x = 0;x < strings.length;x++)
			strings[x] = pullString();
		
		return strings;
	}
	
	private byte[] buffer;
	private int index;
}
