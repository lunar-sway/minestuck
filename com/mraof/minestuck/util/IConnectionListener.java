package com.mraof.minestuck.util;

public interface IConnectionListener {

	public abstract void onConnected(String client, String server);
	
	public abstract void onServerOpen(String server);
	
	public abstract void onConnectionClosed(String client, String server);
	
	public abstract void newPermaConnection(String client, String server);
	
}
