package com.mraof.minestuck.skaianet;

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
import com.mraof.minestuck.util.Debug;
import com.mraof.minestuck.util.IConnectionListener;
import com.mraof.minestuck.world.storage.MinestuckSaveHandler;

import net.minecraft.server.MinecraftServer;
import net.minecraft.world.World;

public class SburbConnection {

	static TreeMap<String,ComputerData> serversOpen = new TreeMap<String,ComputerData>();
	static List<IConnectionListener> listeners = Collections.synchronizedList(new ArrayList<IConnectionListener>());
	static ArrayList<SburbConnection> connections = new ArrayList<SburbConnection>();
	static HashMap<String,ComputerData>resumingClients = new HashMap<String,ComputerData>();
	static HashMap<String,ComputerData>resumingServers = new HashMap<String,ComputerData>();

	public static ArrayList<String> getServersOpen() {
		return new ArrayList<String>(serversOpen.keySet());
	}
	
	public static boolean isServerOpen(String server){
		return serversOpen.containsKey(server) || resumingServers.containsKey(server);
	}
	
	public static boolean enteredMedium(String client){
		for(SburbConnection c : connections)
			if(c.isMain && c.getClientName().equals(client))
				return c.enteredGame;
		return false;
	}
	
	public static String getAssociatedPartner(String player, boolean isClient){
		for(SburbConnection c : connections)
			if(c.isMain)
				if(isClient && c.getClientName().equals(player))
					return c.getServerName();
				else if(!isClient && c.getServerName().equals(player))
				return c.getClientName();
		return "";
	}
	
	public static SburbConnection getClientConnection(String client){
		for(SburbConnection c : connections)
			if(c.client != null && c.client.owner.equals(client))
				return c;
		return null;
	}
	
	public static boolean isResuming(String player, boolean isClient){
		if(isClient)
			return resumingClients.containsKey(player);
		else
			return resumingServers.containsKey(player);
	}
	
	public static void resumeConnection(String player, int x, int y, int z, int dimensionId, boolean isClient){
		for(SburbConnection c : connections)
			if(c.isMain && (isClient && c.clientName.equals(player) || !isClient && c.serverName.equals(player)))
				if(isClient){
					ComputerData data = resumingServers.get(c.serverName);
					if(data == null)
						resumingClients.put(player, new ComputerData(player, x, y, z, dimensionId));
					else{
						c.server = data;
						resumingServers.remove(c.serverName);
						c.client = new ComputerData(player, x, y, z, dimensionId);
						for(IConnectionListener listener : listeners)
							listener.onConnected(player,c.serverName);
					}
				}else{
					ComputerData data = resumingClients.get(c.clientName);
					if(data == null)
						resumingServers.put(player, new ComputerData(player, x, y, z, dimensionId));
					else{
						c.client = data;
						resumingClients.remove(c.clientName);
						c.server = new ComputerData(player, x, y, z, dimensionId);
						for(IConnectionListener listener : listeners)
							listener.onConnected(c.clientName,player);
						
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
		
		serversOpen.put(player,new ComputerData(player,x,y,z,dimension));
		
		for (IConnectionListener listener : listeners) {
			listener.onServerOpen(player);
		}
	}
	
	public static void connect(String client, int x, int y, int z, int dimension, String server) {
		
		if(serversOpen.containsKey(server)){
			if(getClientConnection(client) != null){
				Debug.print("Connection denied, client got an connection set up already, client:"+client+", connected to:"+getClientConnection(client).server.owner);
				return;
			}
			SburbConnection c = null;
			for(SburbConnection connection : connections)
				if(connection.clientName.equals(client) && connection.serverName.equals(server)){	//If the players got an permanent connection, use that
					c = connection;
					c.server = serversOpen.remove(server);
					c.client = new ComputerData(client,x,y,z,dimension);
					break;
				}
			if(c == null)	//Else, do a new one.
				c = new SburbConnection(new ComputerData(client,x,y,z,dimension),serversOpen.remove(server));
			connections.add(c);
			if(MinestuckSaveHandler.lands.contains((byte) dimension))	//if the client have entered the medium, set enteredGame to true.
				connections.get(connections.size()-1).enteredGame = true;
			for (IConnectionListener listener : listeners)
				listener.onConnected(client,server);
		}
		else Debug.print("Connection denied, the specific server wasn't open, server:"+server);
	}
	
	public static void connectionClosed(String client, String server){
		if(client.isEmpty()){	//If an open server wants to close
			if(resumingServers.containsKey(server))
				resumingServers.remove(server);
			else serversOpen.remove(server);
		}
		else if(server.isEmpty()){	//If a client wants to stop resuming
			resumingClients.remove(client);
		}else
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
	
	public static void enterMedium(String client, int destination){
		SburbConnection c = getClientConnection(client);
		if(c != null){
			c.enteredGame = true;
			c.client.dimension = destination;
		}
	}
	
	public static void addListener(IConnectionListener listener) {
		if(!listeners.contains(listener))
			listeners.add(listener);
	}
	
	public static void removeListener(IConnectionListener listener){
		listeners.remove(listener);
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
				Debug.print(connections.size()+" connection(s) loaded");
				checkConnections();
			}catch(IOException e){
				e.printStackTrace();
			}
		}
	}
	
	public static void checkConnections(){ //Just for safety
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
		int x, y, z;
		int dimension;
		String owner;
		ComputerData(String owner,int x,int y,int z,int dimension){
			this.owner = owner;
			this.x = x;
			this.y = y;
			this.z = z;
			this.dimension = dimension;
		}
		
		private ComputerData() {}
		
		void save(DataOutputStream stream) throws IOException{
			stream.writeInt(x);
			stream.writeInt(y);
			stream.writeInt(z);
			stream.writeInt(dimension);
			stream.write((owner+"\n").getBytes());
		}
		
		static ComputerData load(DataInputStream stream) throws IOException{
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
	
	ComputerData client;
	String clientName = "";
	ComputerData server;
	String serverName = "";
	boolean isMain;
	boolean enteredGame;
	
	SburbConnection(ComputerData client, ComputerData server){
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
