package com.mraof.minestuck.network.skaianet;

import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
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
import com.mraof.minestuck.editmode.ServerEditHandler;
import com.mraof.minestuck.network.MinestuckPacket;
import com.mraof.minestuck.network.MinestuckPacket.Type;
import com.mraof.minestuck.tileentity.TileEntityComputer;
import com.mraof.minestuck.util.Debug;
import com.mraof.minestuck.util.MinestuckStatsHandler;
import com.mraof.minestuck.util.UsernameHandler;

/**
 * This class handles server sided stuff about the sburb connection network.
 * This class also handles the main saving and loading.
 * @author kirderf1
 */
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
			SessionHandler.onFirstItemGiven(c);
			updatePlayer(c.getClientName());
			updatePlayer(c.getServerName());
			return true;
		}
		return false;
	}
	
	/**
	 * Note that this is when a player logs in to the server.
	 * @param player
	 */
	public static void playerConnected(String player){
		//Debug.print("[SKAIANET] Player connected:"+player);
		String[] s = new String[5];
		s[0] = UsernameHandler.encode(player);
		infoToSend.put(player, s);
		updatePlayer(player);
	}
	
	public static void requestConnection(ComputerData player, String otherPlayer, boolean isClient){
		TileEntityComputer te = getComputer(player);
		if(te == null)
			return;
		if(!isClient){	//Is server
			if(serversOpen.containsKey(player.owner) || resumingServers.containsKey(player.owner))
				return;
			if(otherPlayer.isEmpty()){	//Wants to open
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
			else return;
		} else {	//Is client
			if(getClientConnection(player.owner) != null || resumingClients.containsKey(player.owner))
				return;
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
					te.latestmessage.put(0, "computer.messageResumeStop");
					te.worldObj.markBlockForUpdate(te.xCoord, te.yCoord, te.zCoord);
				}
			} else if(serversOpen.containsKey(player)){
				TileEntityComputer te = getComputer(serversOpen.remove(player));
				if(te != null){
					te.openToClients = false;
					te.latestmessage.put(1, "computer.messageClosedServer");
					te.worldObj.markBlockForUpdate(te.xCoord, te.yCoord, te.zCoord);
				}
			} else if(resumingServers.containsKey(player)){
				TileEntityComputer te = getComputer(resumingServers.remove(player));
				if(te != null){
					te.openToClients = false;
					te.latestmessage.put(1, "computer.messageResumeStop");
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
						cc.latestmessage.put(0, "computer.messageClosed");
						cc.worldObj.markBlockForUpdate(cc.xCoord, cc.yCoord, cc.zCoord);
					}
					if(sc != null){
						sc.clientName = "";
						sc.latestmessage.put(1, "computer.messageClosed");
						sc.worldObj.markBlockForUpdate(sc.xCoord, sc.yCoord, sc.zCoord);
					}
					SessionHandler.onConnectionClosed(c, true);
					ServerEditHandler.onDisconnect(c);
					if(c.isMain)
						c.isActive = false;	//That's everything that is neccesary.
					else connections.remove(c);
				} else if(getAssociatedPartner(player, isClient).equals(otherPlayer)){
					TileEntityComputer te = getComputer(isClient?resumingClients.remove(player):resumingServers.remove(player));
					if(te != null){
						te.latestmessage.put(isClient?0:1, "computer.messageResumeStop");
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
		boolean newConnection = false;	//True if new, false if resuming.
		if(isClient){
			c = getConnection(player.owner, otherPlayer);
			if(c == null){
				c = new SburbConnection();
				connections.add(c);
				newConnection = true;
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
		if(newConnection){
			String s = SessionHandler.onConnectionCreated(c);
			if(s != null) {
				connections.remove(c);
				TileEntityComputer cte = getComputer(c.client);
				if(cte != null)
					cte.latestmessage.put(0, s);
				map.put(c.server.owner, c.server);
				return;
			}
		}
		if(newConnection && !getAssociatedPartner(c.getClientName(), true).isEmpty()) {	//Copy client associated variables
			SburbConnection conn = getConnection(c.getClientName(), getAssociatedPartner(c.getClientName(), true));
			c.enteredGame = conn.enteredGame;
			c.canSplit = conn.canSplit;
			c.centerX = conn.centerX;
			c.centerZ = conn.centerZ;
			c.clientHomeLand = conn.clientHomeLand;
			if(c.inventory != null)
				c.inventory = (NBTTagList) conn.inventory.copy();
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
			Debug.print("[SKAIANET] Player \""+p0+"\" sent a request without being online!");
			return;
		}
		if(Minestuck.privateComputers && !p0.equals(p1) && !MinecraftServer.getServer().getConfigurationManager().isPlayerOpped(p0)){
			if(!Minestuck.privateMessage.isEmpty()){
				ChatMessageComponent chatmessage = new ChatMessageComponent();
				chatmessage.addText("[MINESTUCK] "+Minestuck.privateMessage);
				chatmessage.setColor(EnumChatFormatting.RED);
				player.sendChatToPlayer(chatmessage);
			}
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
	
	public static void saveData(File file){
		checkData();
		
		NBTTagCompound nbt = new NBTTagCompound();
		NBTTagList list = new NBTTagList();
		
		for(Session s : SessionHandler.sessions)
			list.appendTag(s.write());
		
		nbt.setTag("sessions", list);
		
		String[] s = {"serversOpen","resumingClients","resumingServers"};
		Map<String, ComputerData>[] maps = new Map[]{serversOpen, resumingClients, resumingServers};
		for(int i = 0; i < 3; i++) {
			list = new NBTTagList();
			for(ComputerData c:maps[i].values())
				list.appendTag(c.write());
			nbt.setTag(s[i], list);
		}
		
		try {
			CompressedStreamTools.writeCompressed(nbt, new FileOutputStream(file));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static void loadData(File file){
		connections.clear();
		serversOpen.clear();	
		resumingClients.clear();
		resumingServers.clear();
		SessionHandler.sessions.clear();
		if(file.exists()){
			NBTTagCompound nbt = null;
			try{
				nbt = CompressedStreamTools.readCompressed(new FileInputStream(file));
				
			} catch(IOException e){
				e.printStackTrace();
				Debug.print("[SKAIANET] Trying to load using the old format instead.");
				loadOld(file);
			}
			if(nbt != null){
				NBTTagList list = nbt.getTagList("connections");
				for(int i = 0; i < list.tagCount(); i++)
					connections.add(new SburbConnection().read((NBTTagCompound) list.tagAt(i)));
				
				list = nbt.getTagList("sessions");
				for(int i = 0; i < list.tagCount(); i++)
					SessionHandler.sessions.add(new Session().read((NBTTagCompound) list.tagAt(i)));
				
				String[] s = {"serversOpen","resumingClients","resumingServers"};
				Map<String, ComputerData>[] maps = new Map[]{serversOpen, resumingClients, resumingServers};
				for(int e = 0; e < 3; e++) {
					list = (NBTTagList)nbt.getTag(s[e]);
					if(list == null)
						continue;
					for(int i = 0; i < list.tagCount(); i++){
						NBTTagCompound cmp = (NBTTagCompound)list.tagAt(i);
						ComputerData c = new ComputerData();
						c.read(cmp);
						maps[e].put(c.owner, c);
					}
				}
			}
		}
		
		SessionHandler.serverStarted();
	}
	
	static void loadOld(File file) {
		try{
			DataInputStream stream = new DataInputStream(new FileInputStream(file));
			Session s = new Session();
			while(stream.available() > 0){
				SburbConnection c = new SburbConnection();
				c.isActive = stream.readBoolean();
				
				if(c.isActive){
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
				s.connections.add(c);
			}
			SessionHandler.sessions.add(s);
		}catch(IOException e){
			e.printStackTrace();
		}
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
		String playerEnc = UsernameHandler.encode(player);
		for(SburbConnection c : connections)
			if(c.isActive && (c.getClientName().equals(playerEnc) || c.getServerName().equals(playerEnc))) {
				p.triggerAchievement(MinestuckStatsHandler.setupConnection);
				break;
			}
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
		
		List playerList = SessionHandler.getServerList(player);
		list.add(playerList.size());
		list.addAll(playerList);
		
		for(SburbConnection c : connections)
			if(c.getClientName().equals(player) || c.getServerName().equals(player))
				list.add(c);
		
		return list.toArray();
	}
	
	static void checkData() {
		ServerConfigurationManager scm = MinecraftServer.getServer().getConfigurationManager();
		Iterator<String> iter0 = infoToSend.keySet().iterator();
		while(iter0.hasNext())
			if(scm.getPlayerForUsername(iter0.next()) == null){
				Debug.print("[SKAIANET] Player disconnected, removing data.");
				iter0.remove();
			}
		
		Iterator<ComputerData>[] iter1 = new Iterator[]{serversOpen.values().iterator(),resumingClients.values().iterator(),resumingServers.values().iterator()};
		
		for(Iterator<ComputerData> i : iter1)
			while(i.hasNext()) {
				ComputerData data = i.next();
				if(getComputer(data) == null || !getComputer(data).owner.equals(data.owner)) {
					Debug.print("[SKAIANET] Invalid computer in waiting list!");
					i.remove();
				}
			}
		
		Iterator<SburbConnection> iter2 = connections.iterator();
		while(iter2.hasNext()){
			SburbConnection c = iter2.next();
			if(c.isActive){
				TileEntityComputer cc = getComputer(c.client), sc = getComputer(c.server);
				if(cc == null || sc == null){
					Debug.print("[SKAIANET] Invalid computer in connection.");
					if(!c.isMain)
						iter2.remove();
					else c.isActive = false;
					SessionHandler.onConnectionClosed(c, true);
					ServerEditHandler.onDisconnect(c);
					
					if(cc != null){
						cc.serverConnected = false;
						cc.latestmessage.put(0, "computer.messageClosed");
						cc.worldObj.markBlockForUpdate(cc.xCoord, cc.yCoord, cc.zCoord);
					} else if(sc != null){
						sc.clientName = "";
						sc.latestmessage.put(1, "computer.messageClosed");
						sc.worldObj.markBlockForUpdate(sc.xCoord, sc.yCoord, sc.zCoord);
					}
				}
				if(cc != null && c.enteredGame && c.clientHomeLand == 0)
					c.clientHomeLand = c.client.dimension;
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
		if(world == null) {
			DimensionManager.initDimension(data.dimension);
			world = DimensionManager.getWorld(data.dimension);
		}
		if(world == null)
			return null;
		TileEntity te = world.getBlockTileEntity(data.x, data.y, data.z);
		if(te == null || !(te instanceof TileEntityComputer))
			return null;
		else return (TileEntityComputer)te;
	}
	
	public static void enterMedium(EntityPlayerMP player, int dimensionId) {
		String username = UsernameHandler.encode(player.username);
		SburbConnection c = getConnection(username, getAssociatedPartner(username, true));
		if(c == null) {
			c = getClientConnection(username);
			if(c == null) {
				c = new SburbConnection();
				c.isActive = false;
				c.isMain = true;
				c.clientName = username;
				c.serverName = username;
				if(SessionHandler.onConnectionCreated(c) == null) {
					SessionHandler.onFirstItemGiven(c);
					connections.add(c);
				} else if(SessionHandler.singleSession) {
					SessionHandler.singleSession = false;
					SessionHandler.split();
					if(SessionHandler.onConnectionCreated(c) == null) {
						SessionHandler.onFirstItemGiven(c);
						connections.add(c);
					}
				}
			} else giveItems(username);
		}
		c.clientHomeLand = dimensionId;
		SessionHandler.onGameEntered(c);
		
		for(SburbConnection sc : connections) {
			if(sc.isActive){
				if(getComputer(sc.client) == null)
					sc.client.dimension = dimensionId;
				if(getComputer(sc.server) == null)
					sc.server.dimension = dimensionId;
			}
		}
		
		c.enteredGame = true;
		c.centerX = (int)player.posX;
		c.centerZ = (int)player.posZ;
		updateAll();
	}
	
}
