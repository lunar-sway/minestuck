package com.mraof.minestuck.util;

import java.util.ArrayList;

public class SburbConnector {

	private static ArrayList<String> serversOpen = new ArrayList<String>();
	private static ArrayList<IConnectionListener> listeners = new ArrayList<IConnectionListener>();

	public static ArrayList getServersOpen() {
		return serversOpen;
	}
	
	public static void addServer(String server) {
		serversOpen.add(server);
	}
	
	public static void addListener(IConnectionListener listener) {
		listeners.add(listener);
	}
	
	public static SburbConnection connect(String client, String server) {
		SburbConnection conn = new SburbConnection(client,server);
		serversOpen.remove(server);
		for (Object listener : listeners) {
			((IConnectionListener)listener).onConnected(conn);
		}
		return conn;
	}
}
