package com.mraof.minestuck.network.skaianet;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;

import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.CompressedStreamTools;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.network.packet.Packet250CustomPayload;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.management.ServerConfigurationManager;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ChatMessageComponent;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.world.World;
import net.minecraftforge.common.DimensionManager;

import com.mraof.minestuck.Minestuck;
import com.mraof.minestuck.network.MinestuckPacket;
import com.mraof.minestuck.network.MinestuckPacket.Type;
import com.mraof.minestuck.tileentity.TileEntityComputer;
import com.mraof.minestuck.util.Debug;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

//@SideOnly(Side.SERVER)	//This crashes the game on execution of ClearMessagePacket?
public class SkaianetHandler {
	
	static Map<String,ComputerData> serversOpen = new TreeMap();
	static Map<String,ComputerData>resumingClients = new HashMap();
	static Map<String,ComputerData>resumingServers = new HashMap();
	static List<SburbConnection> connections = new ArrayList();
	static Map<String, String[]> infoToSend = new HashMap();	//Key: player, value: data to send to player
	
	public static SburbConnection getClientConnection(String client){
		for(SburbConnection c : connections)
			if(c.isActive && c.client.owner.equals(client))
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
	
	public static boolean giveItems(String player){
		SburbConnection c = getClientConnection(player);
		if(c != null && !c.isMain){
			c.isMain = true;
			updatePlayer(c.getClientName());
			updatePlayer(c.getServerName());
			return true;
		}
		return false;
	}
	
	public static void playerConnected(String player){
		Debug.print("[SKAIANET] Player connected:"+player);
		String[] s = new String[5];
		s[0] = player;
		infoToSend.put(player, s);
		updatePlayer(player);
	}
	
	public static void requestConnection(ComputerData player, String otherPlayer, boolean isClient){
		TileEntityComputer te = getComputer(player);
		if(te == null)
			return;
		if(!isClient){	//Is server
			if(otherPlayer.isEmpty() && !serversOpen.containsKey(player.owner)){	//Wants to open
				if(!getAssociatedPartner(player.owner, false).isEmpty() && resumingClients.containsKey(getAssociatedPartner(player.owner, false)))
					connectTo(player, false, getAssociatedPartner(player.owner, false), resumingClients);
				else{
					te.openToClients = true;
					serversOpen.put(player.owner, player);
				}
			}
			else if(!otherPlayer.isEmpty() && getAssociatedPartner(player.owner, false).equals(otherPlayer)){	//Wants to resume
				if(resumingClients.containsKey(otherPlayer))	//The client is already waiting
					connectTo(player, false, otherPlayer, resumingClients);
				else {	//Client is not currently trying to resume
					te.openToClients = true;
					resumingServers.put(player.owner, player);
				}
			}
		} else {	//Is client
			String p = getAssociatedPartner(player.owner, true);
			if(!p.isEmpty() && (otherPlayer.isEmpty() || p.equals(otherPlayer))){	//If trying to connect to the associated partner
				if(resumingServers.containsKey(p)){	//If server is "resuming".
					connectTo(player, true, p, resumingServers);
				} else if(serversOpen.containsKey(p))	//If server is normally open.
					connectTo(player, true, p, serversOpen);
				else {	//If server isn't open
					te.resumingClient = true;
					resumingClients.put(player.owner, player);
				}
			}
			else if(serversOpen.containsKey(otherPlayer))	//If the server is open.
				connectTo(player, true, otherPlayer, serversOpen);
		}
		te.worldObj.markBlockForUpdate(te.xCoord, te.yCoord, te.zCoord);
		updateAll();
	}
	
	public static void closeConnection(String player, String otherPlayer, boolean isClient){
		if(otherPlayer.isEmpty()){
			if(isClient){
				TileEntityComputer te = getComputer(resumingClients.remove(player));
				if(te != null){
					te.resumingClient = false;
					te.latestmessage.put(0, "Stopped resuming");
					te.worldObj.markBlockForUpdate(te.xCoord, te.yCoord, te.zCoord);
				}
			} else if(serversOpen.containsKey(player)){
				TileEntityComputer te = getComputer(serversOpen.remove(player));
				if(te != null){
					te.openToClients = false;
					te.latestmessage.put(1, "Closed server");
					te.worldObj.markBlockForUpdate(te.xCoord, te.yCoord, te.zCoord);
				}
			} else if(resumingServers.containsKey(player)){
				TileEntityComputer te = getComputer(resumingServers.remove(player));
				if(te != null){
					te.openToClients = false;
					te.latestmessage.put(1, "Stopped resuming");
					te.worldObj.markBlockForUpdate(te.xCoord, te.yCoord, te.zCoord);
				}
			} else Debug.print("[SKAIANET] Got disconnect request but server is not open! "+player);
		} else {
			SburbConnection c = isClient?getConnection(player, otherPlayer):getConnection(otherPlayer, player);
			if(c != null){
				if(c.isActive){
					TileEntityComputer cc = getComputer(c.client), sc = getComputer(c.server);
					if(cc != null){
						cc.serverConnected = false;
						cc.latestmessage.put(0, "Connection closed");
						cc.worldObj.markBlockForUpdate(cc.xCoord, cc.yCoord, cc.zCoord);
					}
					if(sc != null){
						sc.clientName = "";
						sc.latestmessage.put(1, "Connection closed");
						sc.worldObj.markBlockForUpdate(sc.xCoord, sc.yCoord, sc.zCoord);
					}
					if(c.isMain){
						c.clientName = c.client.owner;
						c.serverName = c.server.owner;
						c.client = null;
						c.server = null;
						c.isActive = false;
					}
					else connections.remove(c);
				} else if(getAssociatedPartner(player, isClient).equals(otherPlayer)){
					TileEntityComputer te = getComputer(isClient?resumingClients.remove(player):resumingServers.remove(player));
					if(te != null){
						te.latestmessage.put(isClient?0:1, "Stopped resuming");
						te.worldObj.markBlockForUpdate(te.xCoord, te.yCoord, te.zCoord);
					}
				}
			}
		}
		updateAll();
	}
	
	private static void connectTo(ComputerData player, boolean isClient, String otherPlayer, Map<String,ComputerData> map) {
		TileEntityComputer c1 = getComputer(player), c2 = getComputer(map.get(otherPlayer));
		if(c2 == null){
			map.remove(otherPlayer);	//Invalid, should not be in the list
			return;
		}
		if(c1 == null)
			return;
		SburbConnection c;
		if(isClient){
			c = getConnection(player.owner, otherPlayer);
			if(c == null){
				c = new SburbConnection();
				connections.add(c);
			}
			c.client = player;
			c.server = map.remove(otherPlayer);
			c.isActive = true;
		} else {
			c = getConnection(otherPlayer, player.owner);
			if(c == null)
				return;	//A server should only be able to resume
			c.client = map.remove(otherPlayer);
			c.server = player;
			c.isActive = true;
		}
		
		c1.connected(otherPlayer, isClient);
		c2.connected(player.owner, !isClient);
		if(c1 != c2)
			c2.worldObj.markBlockForUpdate(c2.xCoord, c2.yCoord, c2.zCoord);
	}
	
	public static void requestInfo(String p0, String p1){
		checkData();
		String[] s = infoToSend.get(p0);
		EntityPlayerMP player = MinecraftServer.getServer().getConfigurationManager().getPlayerForUsername(p0);
		if(s == null || player == null){
			Debug.print("[SKAIANET] Player sent a request without being online!");
			return;
		}
		if(Minestuck.privateComputers && !p0.equals(p1)){
			ChatMessageComponent chatmessage = new ChatMessageComponent();
			chatmessage.addText("[MINESTUCK] You are not allowed to access other players computers.");
			chatmessage.setColor(EnumChatFormatting.RED);
			player.sendChatToPlayer(chatmessage);
			return;
		}
		int i = 0;
		for(;i < s.length; i++){
			if(s[i] == null)
				break;
			if(s[i].equals(p1)){
				Debug.print("[Skaianet] Player already got the requested data.");
				updatePlayer(p0);	//Update anyway, to fix whatever went wrong
				return;
			}
		}
		if(i == s.length){	//If the array is full, increase size with 5.
			String[] newS = new String[s.length+5];
			System.arraycopy(s, 0, newS, 0, s.length);
			s = newS;
			infoToSend.put(p0, s);
		}
		
		s[i] = p1;
		
		updatePlayer(p0);
	}
	
	public static void saveData(File file0, File file1){
		try{
			checkData();
			
			DataOutputStream stream = new DataOutputStream(new FileOutputStream(file0));
			for(SburbConnection c : connections){
				stream.writeBoolean(c.isActive);
				if(c.isActive){
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
//			for(Session s : Session.sessions){
//				s.save(stream);
//			}
			
			stream.close();
			//Debug.print(connections.size()+" connection"+(connections.size() == 1?"":"s")+" saved");
		} catch(IOException e){
			e.printStackTrace();
		}
		
		NBTTagCompound nbt = new NBTTagCompound();
		
		NBTTagList ls = new NBTTagList("serversOpen");
		for(ComputerData c:serversOpen.values())
			c.write(ls);
		nbt.setTag("serversOpen", ls);
		
		ls = new NBTTagList("resumingClients");
		for(ComputerData c:resumingClients.values())
			c.write(ls);
		nbt.setTag("resumingClients", ls);
		
		ls = new NBTTagList("resumingServers");
		for(ComputerData c:resumingClients.values())
			c.write(ls);
		nbt.setTag("resumingServers", ls);
		
		try {
			CompressedStreamTools.writeCompressed(nbt, new FileOutputStream(file1));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static void loadData(File file0, File file1){
		connections.clear();
		serversOpen.clear();	
		resumingClients.clear();
		resumingServers.clear();
		if(file0.exists()){
			try{
				DataInputStream stream = new DataInputStream(new FileInputStream(file0));
				while(stream.available() > 0){
					
					connections.add(SburbConnection.load(stream));
				}
				
//				Session.sessions.clear();
//				while(stream.available() > 0)
//					Session.sessions.add(Session.load(stream));
				
				stream.close();
				//Debug.print(connections.size()+" connection(s) loaded");
			}catch(IOException e){
				e.printStackTrace();
			}
		}
		
		if(file1.exists()){
			NBTTagCompound nbt = null;
			try{
				nbt = CompressedStreamTools.readCompressed(new FileInputStream(file1));
			}catch(IOException e){
				e.printStackTrace();
			}
			if(nbt != null){
				NBTTagList ls = (NBTTagList)nbt.getTag("serversOpen");
				for(int i = 0; i < ls.tagCount(); i++){
					NBTTagCompound cmp = (NBTTagCompound)ls.tagAt(i);
					ComputerData c = new ComputerData();
					c.read(cmp);
					serversOpen.put(c.owner, c);
				}
				ls = (NBTTagList)nbt.getTag("resumingClients");
				for(int i = 0; i < ls.tagCount(); i++){
					NBTTagCompound cmp = (NBTTagCompound)ls.tagAt(i);
					ComputerData c = new ComputerData();
					c.read(cmp);
					resumingClients.put(c.owner, c);
				}
				ls = (NBTTagList)nbt.getTag("resumingServers");
				for(int i = 0; i < ls.tagCount(); i++){
					NBTTagCompound cmp = (NBTTagCompound)ls.tagAt(i);
					ComputerData c = new ComputerData();
					c.read(cmp);
					resumingServers.put(c.owner, c);
				}
			}
		}
		checkData();
	}
	
	static void updateAll(){
		checkData();
		for(String s : infoToSend.keySet())
			updatePlayer(s);
	}
	
	static void updatePlayer(String player){
		String[] str = infoToSend.get(player);
		EntityPlayerMP p = MinecraftServer.getServer().getConfigurationManager().getPlayerForUsername(player);
		if(str == null || p == null)//If the player disconnected
			return;
		for(String s : str)
			if(s != null){
				Packet250CustomPayload packet = new Packet250CustomPayload();
				packet.channel = "Minestuck";
				packet.data = MinestuckPacket.makePacket(Type.SBURB_INFO, generateClientInfo(s));
				packet.length = packet.data.length;
				p.playerNetServerHandler.sendPacketToPlayer(packet);
			}
	}
	
	static Object[] generateClientInfo(String player){
		List list = new ArrayList();
		list.add(player);
		
		list.add(resumingClients.containsKey(player));
		list.add(resumingServers.containsKey(player));
		
		list.add(serversOpen.size());
		
		for(String s : serversOpen.keySet())
			list.add(s);
		
		for(SburbConnection c : connections)
			if(c.getClientName().equals(player) || c.getServerName().equals(player))
				list.add(c);
		
		return list.toArray();
	}
	
	static void checkData(){
		ServerConfigurationManager scm = MinecraftServer.getServer().getConfigurationManager();
		Iterator<String> iter0 = infoToSend.keySet().iterator();
		while(iter0.hasNext())
			if(scm.getPlayerForUsername(iter0.next()) == null)
				iter0.remove();
		
		Iterator<ComputerData>[] iter1 = new Iterator[]{serversOpen.values().iterator(),resumingClients.values().iterator(),resumingServers.values().iterator()};
		
		for(Iterator<ComputerData> i : iter1)
			while(i.hasNext()){
				ComputerData data = i.next();
				if(getComputer(data) == null || !getComputer(data).owner.equals(data.owner))
					i.remove();
			}
		
		Iterator<SburbConnection> iter2 = connections.iterator();
		while(iter2.hasNext()){
			SburbConnection c = iter2.next();
			if(c.client != null){
				TileEntityComputer cc = getComputer(c.client), sc = getComputer(c.server);
				if(cc == null || sc == null){
					iter2.remove();
					if(cc != null){
						cc.serverConnected = false;
						cc.latestmessage.put(0, "Connection closed");
						cc.worldObj.markBlockForUpdate(cc.xCoord, cc.yCoord, cc.zCoord);
					} else if(sc != null){
						sc.clientName = "";
						sc.latestmessage.put(1, "Connection closed");
						sc.worldObj.markBlockForUpdate(sc.xCoord, sc.yCoord, sc.zCoord);
					}
				}
			}
		}
		
		if(Minestuck.privateComputers){
			for(Entry<String,String[]> entry : infoToSend.entrySet())
				for(int i = 0; i < entry.getValue().length; i++)
					if(entry.getValue()[i] != null && entry.getValue()[i] != entry.getKey())
						entry.getValue()[i] = null;
		}
	}
	
	static SburbConnection getConnection(String client, String server){
		for(SburbConnection c : connections)
			if(c.getClientName().equals(client) && c.getServerName().equals(server))
				return c;
		return null;
	}
	
	/**
	 * Gets the <code>TileEntityComputer</code> at the given position.
	 * @param data A <code>ComputerData</code> representing the computer,
	 * this method does not compare the variable <code>data.owner</code>.
	 * @return The <code>TileEntityComputer</code> at the given position,
	 * or <code>null</code> if there isn't one there.
	 */
	public static TileEntityComputer getComputer(ComputerData data){
		if(data == null)
			return null;
		World world = DimensionManager.getWorld(data.dimension);
		if(world == null)
			return null;
		TileEntity te = world.getBlockTileEntity(data.x, data.y, data.z);
		if(te == null || !(te instanceof TileEntityComputer))
			return null;
		else return (TileEntityComputer)te;
	}
	
	public static void enterMedium(String player, int dimensionId) {
		SburbConnection c = getConnection(player, getAssociatedPartner(player, true));
		if(c != null){
			c.enteredGame = true;
			for(SburbConnection sc : connections){	//TEMP Later make it only change the transferred computers instead
				if(sc.client != null && sc.client.owner.equals(player))
					sc.client.dimension = dimensionId;
				if(sc.server != null && sc.server.owner.equals(player))
					sc.server.dimension = dimensionId;
			}
			updateAll();
		}
	}
	
}
