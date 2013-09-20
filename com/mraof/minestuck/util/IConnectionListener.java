package com.mraof.minestuck.util;

public interface IConnectionListener {

	public abstract void onConnected(String server, String client);
	
	public abstract void onServerOpen(String server);
	
	public abstract void onConnectionClosed(String server, String client);
	
	public abstract void newPermaConnection(String client, String server);
	
}
