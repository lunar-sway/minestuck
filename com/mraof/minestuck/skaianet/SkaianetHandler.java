package com.mraof.minestuck.skaianet;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraftforge.common.DimensionManager;

import com.mraof.minestuck.tileentity.TileEntityComputer;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@SideOnly(Side.SERVER)
public class SkaianetHandler {
	
	static TreeMap<String,ComputerData> serversOpen = new TreeMap<String,ComputerData>();
	static HashMap<String,ComputerData>resumingClients = new HashMap<String,ComputerData>();
	static HashMap<String,ComputerData>resumingServers = new HashMap<String,ComputerData>();
	static ArrayList<SburbConnection> connections = new ArrayList<SburbConnection>();
	
	/**
	 * Possible return in <code>SkaianetHandler.requestConnection()</code>.
	 */
	public static final int DENIED = -1;
	/**
	 * Possible return in <code>SkaianetHandler.requestConnection()</code>.
	 */
	public static final int WAITING = 0;
	/**
	 * Possible return in <code>SkaianetHandler.requestConnection()</code>.
	 */
	public static final int ACCEPTED = 1;
	
	public static ComputerData createData(TileEntityComputer te){
		return new ComputerData(te.owner, te.xCoord, te.yCoord, te.zCoord, te.worldObj.provider.dimensionId );
	}
	
	public static SburbConnection getClientConnection(String client){
		for(SburbConnection c : connections)
			if(c.client != null && c.client.owner.equals(client))
				return c;
		return null;
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
	
	public static int requestConnection(ComputerData player, String otherPlayer, boolean isClient){
		if(!isClient){	//Is server
			if(otherPlayer.isEmpty() && !serversOpen.containsKey(player.owner)){	//Wants to open
				serversOpen.put(player.owner, player);
				return WAITING;
			}
			else if(getAssociatedPartner(player.owner, false).equals(otherPlayer)){	//Wants to resume
				if(resumingClients.containsKey(otherPlayer)){	//The client is already waiting
					return connectTo(player, false, otherPlayer, resumingClients);
				} else {	//Client is not currently trying to resume
					resumingServers.put(player.owner, player);
					return WAITING;
				}
			}
		} else {	//Is client
			String p = getAssociatedPartner(player.owner, true);
			if(!p.isEmpty() && (otherPlayer.isEmpty() || p.equals(otherPlayer))){	//If trying to connect to the associated partner
				if(resumingServers.containsKey(p)){	//If server is "resuming".
					return connectTo(player, true, p, resumingServers);
				} else if(serversOpen.containsKey(p))	//If server is normally open.
					return connectTo(player, true, p, serversOpen);
				else {	//If server isn't open
					resumingClients.put(player.owner, player);
					return WAITING;
				}
			}
			else if(serversOpen.containsKey(otherPlayer))	//If the server is open.
				return connectTo(player, true, otherPlayer, serversOpen);
		}
		return DENIED;
	}

	private static int connectTo(ComputerData player, boolean isClient, String otherPlayer, Map<String,ComputerData> map) {
		TileEntityComputer c1 = getComputer(player), c2 = getComputer(map.get(otherPlayer));
		if(c2 == null){
			map.remove(otherPlayer);	//Invalid, should not be in the list
			return DENIED;
		}
		if(c1 == null)
			return DENIED;
		SburbConnection c;
		if(isClient){
			c = getConnection(player.owner, otherPlayer);
			if(c == null){
				c = new SburbConnection(null, null);
				connections.add(c);
			}
			c.client = player;
			c.server = map.remove(otherPlayer);
		} else {
			c = getConnection(otherPlayer, player.owner);
			if(c == null)
				return DENIED;	//A server should only be able to resume
			c.client = map.remove(otherPlayer);
			c.server = player;
		}
		
		c1.connected(c, isClient);
		c2.connected(c, !isClient);
		return ACCEPTED;
	}
	
	public static void saveData(File file){
		try{
			DataOutputStream stream = new DataOutputStream(new FileOutputStream(file));
			stream.writeByte(Session.sessions.size());
			for(Session s : Session.sessions){
				s.save(stream);
			}
			
		} catch(IOException e){
			e.printStackTrace();
		}
	}
	
	public static void loadData(File file){
		try{
			DataInputStream stream =  new DataInputStream(new FileInputStream(file));
			byte size = stream.readByte();
			Session.sessions.clear();
			for(int i = 0; i < size; i++)
				Session.sessions.add(Session.load(stream));
		} catch(IOException e){
			e.printStackTrace();
		}
	}
	
	public static void updateAll(){
		
	}
	
	static SburbConnection getConnection(String client, String server){
		for(SburbConnection c : connections)
			if(c.getClientName().equals(client) && c.getServerName().equals(server))
				return c;
		return null;
	}
	
	static TileEntityComputer getComputer(ComputerData data){
		World world = DimensionManager.getWorld(data.dimension);
		if(world == null)
			return null;
		TileEntity te = world.getBlockTileEntity(data.x, data.y, data.z);
		if(te == null || !(te instanceof TileEntityComputer))
			return null;
		else return (TileEntityComputer)te;
	}
	
}
