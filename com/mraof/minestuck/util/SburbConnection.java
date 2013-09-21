package com.mraof.minestuck.util;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.TreeMap;

import com.mraof.minestuck.Minestuck;
import com.mraof.minestuck.world.storage.MinestuckSaveHandler;

import net.minecraft.server.MinecraftServer;
import net.minecraft.world.World;

public class SburbConnection {

	private static TreeMap<String,ComputerData> serversOpen = new TreeMap<String,ComputerData>();
	private static List<IConnectionListener> listeners = Collections.synchronizedList(new ArrayList<IConnectionListener>());
	private static ArrayList<SburbConnection> connections = new ArrayList<SburbConnection>();
	private static HashMap<String,ComputerData> mainConnections = new HashMap<String,ComputerData>();	//Connections waiting for

	public static ArrayList<String> getServersOpen() {
		return new ArrayList<String>(serversOpen.keySet());
	}
	
	public static SburbConnection getClientConnection(String client){
		for(SburbConnection c : connections)
			if(c.client != null && c.client.owner.equals(client))
				return c;
		return null;
	}
	
	public static boolean isResuming(String player, boolean isClient){
		for(ComputerData data : mainConnections.values())
			if(data.owner.equals(player) && data.isClient == isClient)
				return true;
		return false;
				
	}
	
	public static void resumeConnection(String player, int x, int y, int z, int dimensionId, boolean isClient){
		for(SburbConnection c : connections)
			if(c.isMain && (isClient && c.clientName.equals(player) || !isClient && c.serverName.equals(player)))
				if(isClient){
					ComputerData data = mainConnections.get(c.serverName);
					if(data == null)
						mainConnections.put(player, new ComputerData(player, x, y, z, dimensionId,isClient));
					else{
						c.server = data;
						mainConnections.remove(c.serverName);
						c.client = new ComputerData(player, x, y, z, dimensionId,isClient);
						synchronized(listeners){
							for(IConnectionListener listener : listeners)
								listener.onConnected(player,c.serverName);
						}
					}
				}else{
					ComputerData data = mainConnections.get(c.clientName);
					if(data == null)
						mainConnections.put(player, new ComputerData(player, x, y, z, dimensionId,isClient));
					else{
						c.client = data;
						mainConnections.remove(c.clientName);
						c.server = new ComputerData(player, x, y, z, dimensionId,isClient);
						synchronized(listeners){
							for(IConnectionListener listener : listeners)
								listener.onConnected(c.clientName,player);
						}
					}
				}
	}
	
	public static boolean hasMainClient(String client){ //Returns true if a main connection has a client of the specified name
		for(SburbConnection c : connections)
			if(c.isMain && (c.clientName.equals(client) || c.client != null && c.client.owner.equals(client)))
				return true;
		return false;
	}
	
	public static boolean hasMainServer(String server){
		for(SburbConnection c : connections)
			if(c.isMain && (c.serverName.equals(server) || c.server != null && c.server.owner.equals(server)))
				return true;
		return false;
	}
	
	public static void openServer(String player, int x, int y, int z,int dimension) {
		
		if(serversOpen.containsKey(player))
			return;
		
		serversOpen.put(player,new ComputerData(player,x,y,z,dimension,false));
		
		synchronized(listeners){
			for (IConnectionListener listener : listeners) {
				listener.onServerOpen(player);
			}
		}
	}
	
	public static void connect(String client, int x, int y, int z, int dimension, String server) {
		
		if(serversOpen.containsKey(server)){
			SburbConnection c = getClientConnection(client);
			if(c != null && c.server != null){
				Debug.print("Connection denied, client got an connection set up already, client:"+client+", connected to:"+c.server.owner);
				return;
			}
			
			connections.add(new SburbConnection(new ComputerData(client,x,y,z,dimension,true),serversOpen.remove(server)));
			if(MinestuckSaveHandler.lands.contains((byte) dimension))
				connections.get(connections.size()-1).enteredGame = true;
			synchronized(listeners){
				for (IConnectionListener listener : listeners)
					listener.onConnected(client,server);
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
		synchronized(listeners){
			for(IConnectionListener listener : listeners)
				listener.onConnectionClosed(client, server);
		}
	}
	
	public static boolean giveItems(String client){
		for(SburbConnection connect : connections)
			if(connect.client != null && connect.client.owner.equals(client) && !connect.isMain && !connect.enteredGame){
				connect.isMain = true;
				synchronized(listeners){
					for(IConnectionListener listener : listeners)
						listener.newPermaConnection(client, connect.server.owner);
				}
				return true;
			}
		return false;
	}
	
	public static void enterMedium(String client, int destination){
		SburbConnection c = getClientConnection(client);
		if(c != null){
			c.enteredGame = true;
			c.client.dimension = destination;
		}
	}
	
	public static void addListener(IConnectionListener listener) {
		synchronized(listeners){
			if(!listeners.contains(listener))
				listeners.add(listener);
		}
	}
	
	public static void removeListener(IConnectionListener listener){
		synchronized(listeners){
			listeners.remove(listener);
		}
	}
	
	public static void saveData(File file){
		if(file.exists()){
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
						stream.writeBoolean(false);
						stream.write((c.clientName+"\n").getBytes());
						stream.write((c.serverName+"\n").getBytes());
						stream.writeBoolean(c.enteredGame);
					}
				}
				stream.close();
				Debug.print(connections.size()+" connection(s) saved,"+stream.size());
			} catch(IOException e){
				e.printStackTrace();
			}
		}
	}
	
	public static void loadData(File file){
		if(file.exists()){
			try{
				DataInputStream stream = new DataInputStream(new FileInputStream(file));
				connections.clear();Debug.print(stream.available());
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
				Debug.print(connections.size()+" connection(s) loaded");
				checkConnections();
			}catch(IOException e){
				e.printStackTrace();
			}
		}
	}
	
	public static void checkConnections(){
		for(SburbConnection c : connections){
			if(c.client != null && c.server != null)
				for(World world : MinecraftServer.getServer().worldServers){
					if(world.provider.dimensionId == c.client.dimension && world.getBlockId(c.client.x, c.client.y, c.client.z) != Minestuck.blockComputerOn.blockID ||
							world.provider.dimensionId == c.server.dimension && world.getBlockId(c.server.x, c.server.y, c.server.z) != Minestuck.blockComputerOn.blockID){
						if(c.isMain){
							c.clientName = c.client.owner;
							c.client = null;
							c.serverName = c.server.owner;
							c.server = null;
						} else {
							connections.remove(c);
						}
					}
				}
		}
	}
	
	public static class ComputerData{
		private int x, y, z;
		private int dimension;
		private String owner;
		private boolean isClient;
		private ComputerData(String owner,int x,int y,int z,int dimension,boolean isClient){
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
	private String clientName = "";
	private ComputerData server;
	private String serverName = "";
	private boolean isMain;
	private boolean enteredGame;
	
	private SburbConnection(ComputerData client, ComputerData server){
		this.client = client;
		this.server = server;
	}
	
	public ComputerData getClient(){return client;}
	public ComputerData getServer(){return server;}
	public String getClientName(){
		if(clientName.isEmpty())
			return client.owner;
		else return clientName;
	}
	public String getServerName(){
		if(serverName.isEmpty())
			return server.owner;
		else return serverName;
	}
	public boolean givenItems(){return isMain;}
	public boolean enteredGame(){return enteredGame;}
	
}
