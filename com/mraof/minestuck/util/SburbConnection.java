package com.mraof.minestuck.util;

import java.util.ArrayList;

public class SburbConnection {

	private static ArrayList serversOpen = new ArrayList();
	private static ArrayList<IConnectionListener> listeners = new ArrayList<IConnectionListener>();

	public static ArrayList getServersOpen() {
		return serversOpen;
	}
	
	public static void openServer(String player) {
		
		for (Object conn : serversOpen) {
			if ((String)conn == player) {
				return;
			}
		}
		
		serversOpen.add(player);
		
		for (Object listener : listeners) {
			((IConnectionListener)listener).onServerOpen(player);
		}
	}
	
	public static void connect(String client, String server) {
		
		if(serversOpen.contains(server)){
			serversOpen.remove(server);
			
			for (Object listener : listeners) {
				((IConnectionListener)listener).onConnected(server,client);
			}
		}
	}
	
	public static void addListener(IConnectionListener listener) {
		listeners.add(listener);
	}
}
