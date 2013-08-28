package com.mraof.minestuck.util;

import java.util.ArrayList;

public class SburbConnection {

	private static ArrayList<SburbConnection> serversOpen = new ArrayList<SburbConnection>();

	public IConnectionListener listener;
	public String server;
	public String client;

	public SburbConnection(IConnectionListener listener) {
		this.listener = listener;
	}

	public static ArrayList getServersOpen() {
		return serversOpen;
	}
	
	public SburbConnection openServer(String player) {
		this.server = player;
		serversOpen.add(this);
		return this;
	}
	
	public SburbConnection connect(String client) {
		
		for (Object conn : serversOpen) {
			if (((SburbConnection)conn).server == this.server) {
				serversOpen.remove(conn);
			}
		}

		this.client = client;
		if (this.listener != null) {
			this.listener.onConnected(this);
		}
		return this;
	}
}
