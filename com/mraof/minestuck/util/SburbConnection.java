package com.mraof.minestuck.util;

import java.util.ArrayList;
import java.util.TreeMap;

import net.minecraft.server.MinecraftServer;

public class SburbConnection {

	private static TreeMap<String,ComputerData> serversOpen = new TreeMap<String,ComputerData>();
	private static ArrayList<IConnectionListener> listeners = new ArrayList<IConnectionListener>();
	private static ArrayList<SburbConnection> connections = new ArrayList<SburbConnection>();

	public static ArrayList<String> getServersOpen() {
		return new ArrayList<String>(serversOpen.keySet());
	}
	
	public static void openServer(String player, int x, int y, int z,int dimension) {
		
		if(serversOpen.containsKey(player))
			return;
		
		serversOpen.put(player,new ComputerData(player,x,y,z,dimension,false));
		
		for (Object listener : listeners) {
			((IConnectionListener)listener).onServerOpen(player);
		}
	}
	
	public static void connect(String client, int x, int y, int z, int dimension, String server) {
		
		if(serversOpen.containsKey(server)){
			connections.add(new SburbConnection(new ComputerData(client,x,y,z,dimension,true),serversOpen.remove(server)));
			
			for (Object listener : listeners) {
				((IConnectionListener)listener).onConnected(client,server);
			}
		}
	}
	
	public static void connectionClosed(String client, String server){
		if(client.isEmpty())
			serversOpen.remove(server);
		else
			for(SburbConnection connect : connections)
				if(connect.client.owner.equals(client)&&connect.server.owner.equals(server)){
					connections.remove(connect);
					break;
				}
		for(IConnectionListener listener : listeners)
			listener.onConnectionClosed(client, server);
	}
	
	public static boolean giveItems(String client){
		for(SburbConnection connect : connections)
			if(connect.client.owner.equals(client) && !connect.isMain && !connect.client.enteredGame){
				connect.isMain = true;
				return true;
			}
		return false;
	}
	
	public static void addListener(IConnectionListener listener) {
		if(!listeners.contains(listener))
			listeners.add(listener);
	}
	
	public static void removeListener(IConnectionListener listener){
		listeners.remove(listener);
	}
	
	public static class ComputerData{
		private int x, y, z;
		private int dimension;
		private String owner;
		private boolean isClient;
		private boolean enteredGame;
		private ComputerData(String owner,int x,int y,int z,int dimension,boolean isClient){
			this.owner = owner;
			this.x = x;
			this.y = y;
			this.z = z;
			this.dimension = dimension;
			this.isClient = isClient;
		}
	}
	
	//Non static stuff
	
	private ComputerData client;
	private ComputerData server;
	private boolean isMain;
	
	private SburbConnection(ComputerData client, ComputerData server){
		this.client = client;
		this.server = server;
	}
}
