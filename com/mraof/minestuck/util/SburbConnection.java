package com.mraof.minestuck.util;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
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
	
	public static SburbConnection getClientConnection(String client){
		for(SburbConnection c : connections)
			if(c.client != null && c.client.owner.equals(client))
				return c;
		return null;
	}
	
	public static void openServer(String player, int x, int y, int z,int dimension) {
		
		if(serversOpen.containsKey(player))
			return;
		
		serversOpen.put(player,new ComputerData(player,x,y,z,dimension));
		
		for (Object listener : listeners) {
			((IConnectionListener)listener).onServerOpen(player);
		}
	}
	
	public static void connect(String client, int x, int y, int z, int dimension, String server) {
		
		if(serversOpen.containsKey(server)){
			for(SburbConnection c : connections)
				if(c.client != null && c.client.owner.equals(client)){
					Debug.print("Connection denied, client got an connection set up already, client:"+client);
					return;
				}
			
			connections.add(new SburbConnection(new ComputerData(client,x,y,z,dimension),serversOpen.remove(server)));
			
			for (Object listener : listeners) {
				((IConnectionListener)listener).onConnected(client,server);
			}
		}
		else Debug.print("Connection denied, the specific server wasn't open, server:"+server);
	}
	
	public static void connectionClosed(String client, String server){
		if(client.isEmpty())
			serversOpen.remove(server);
		else
			for(SburbConnection connect : connections)
				if(connect.client != null && connect.client.owner.equals(client)&&connect.server.owner.equals(server)){
					if(connect.isMain){
						connect.clientName = connect.client.owner;
						connect.client = null;
						connect.serverName = connect.server.owner;
						connect.server = null;
					}else connections.remove(connect);
					break;
				}
		for(IConnectionListener listener : listeners)
			listener.onConnectionClosed(client, server);
	}
	
	public static boolean giveItems(String client){
		for(SburbConnection connect : connections)
			if(connect.client != null && connect.client.owner.equals(client) && !connect.isMain && !connect.enteredGame){
				connect.isMain = true;
				for(IConnectionListener listener : listeners)
					listener.newPermaConnection(client, connect.server.owner);
				return true;
			}
		return false;
	}
	
	public static void enterMedium(String client){
		SburbConnection c = getClientConnection(client);
		if(c != null)
			c.enteredGame = true;
	}
	
	public static void addListener(IConnectionListener listener) {
		if(!listeners.contains(listener))
			listeners.add(listener);
	}
	
	public static void removeListener(IConnectionListener listener){
		listeners.remove(listener);
	}
	
	public static void saveData(File file){
		try{
			DataOutputStream stream = new DataOutputStream(new FileOutputStream(file));
			for(SburbConnection c : connections){
				if(c.client != null && c.server != null){
					stream.writeBoolean(true);
					c.client.save(stream);
					c.server.save(stream);
					stream.writeBoolean(c.isMain);
					if(c.isMain)
						stream.writeBoolean(c.enteredGame);
				}
				else{
					stream.write((c.clientName+"\n").getBytes());
					stream.write((c.serverName+"\n").getBytes());
					stream.writeBoolean(c.enteredGame);
				}
			}
			stream.close();
		} catch(IOException e){
			e.printStackTrace();
		}
	}
	
	public static void loadData(File file){
		if(file.exists()){
			try{
				DataInputStream stream = new DataInputStream(new FileInputStream(file));
				connections.clear();
				while(stream.available() > 0){
					boolean connected = stream.readBoolean();
					SburbConnection c = new SburbConnection(null, null);
					
					if(connected){
						c.client = ComputerData.load(stream);
						c.server = ComputerData.load(stream);
						c.isMain = stream.readBoolean();
						if(c.isMain)
							c.enteredGame = stream.readBoolean();
					}
					else{
						c.isMain = true;
						c.clientName = stream.readLine();
						c.serverName = stream.readLine();
						c.enteredGame = stream.readBoolean();
					}
					connections.add(c);
				}
				stream.close();
			}catch(IOException e){
				e.printStackTrace();
			}
		}
	}
	
	public static class ComputerData{
		private int x, y, z;
		private int dimension;
		private String owner;
		private ComputerData(String owner,int x,int y,int z,int dimension){
			this.owner = owner;
			this.x = x;
			this.y = y;
			this.z = z;
			this.dimension = dimension;
		}
		
		private ComputerData() {}
		
		private void save(DataOutputStream stream) throws IOException{
			stream.writeInt(x);
			stream.writeInt(y);
			stream.writeInt(z);
			stream.writeInt(dimension);
			stream.write((owner+"\n").getBytes());
		}
		
		private static ComputerData load(DataInputStream stream) throws IOException{
			ComputerData data = new ComputerData();
			data.x = stream.readInt();
			data.y = stream.readInt();
			data.z = stream.readInt();
			data.dimension = stream.readInt();
			data.owner = stream.readLine(); //How should I read the string here without a deprecated method?
			return data;
		}
		
		public String getOwner(){return owner;}
	}
	
	//Non static stuff
	
	private ComputerData client;
	private String clientName;
	private ComputerData server;
	private String serverName;
	private boolean isMain;
	private boolean enteredGame;
	
	private SburbConnection(ComputerData client, ComputerData server){
		this.client = client;
		this.server = server;
	}
	
	public ComputerData getClient(){return client;}
	public ComputerData getServer(){return server;}
	public boolean givenItems(){return isMain;}
	public boolean enteredGame(){return enteredGame;}
	
}
