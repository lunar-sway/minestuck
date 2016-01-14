package com.mraof.minestuck.network.skaianet;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;

import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.management.ServerConfigurationManager;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.util.ChatStyle;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.world.World;

import com.mraof.minestuck.MinestuckConfig;
import com.mraof.minestuck.editmode.ServerEditHandler;
import com.mraof.minestuck.network.MinestuckChannelHandler;
import com.mraof.minestuck.network.MinestuckPacket;
import com.mraof.minestuck.network.MinestuckPacket.Type;
import com.mraof.minestuck.tileentity.TileEntityComputer;
import com.mraof.minestuck.util.Debug;
import com.mraof.minestuck.util.MinestuckAchievementHandler;
import com.mraof.minestuck.util.UsernameHandler;
import com.mraof.minestuck.world.MinestuckDimensionHandler;

/**
 * This class handles server sided stuff about the sburb connection network.
 * This class also handles the main saving and loading.
 * @author kirderf1
 */
public class SkaianetHandler {
	
	static Map<String, ComputerData> serversOpen = new TreeMap<String, ComputerData>();
	static Map<String, ComputerData> resumingClients = new HashMap<String, ComputerData>();
	static Map<String, ComputerData> resumingServers = new HashMap<String, ComputerData>();
	static List<SburbConnection> connections = new ArrayList<SburbConnection>();
	static Map<String, String[]> infoToSend = new HashMap<String, String[]>();	//Key: player, value: data to send to player
	static List<ComputerData> movingComputers = new ArrayList<ComputerData>();
	
	public static SburbConnection getClientConnection(String client){
		for(SburbConnection c : connections)
			if(c.isActive && c.client.owner.equals(client))
				return c;
		return null;
	}
	
	public static String getAssociatedPartner(String player, boolean isClient){
		for(SburbConnection c : connections)
			if(c.isMain)
				if(isClient && c.getClientName().equals(player) && !c.getServerName().equals(".null"))
					return c.getServerName();
				else if(!isClient && c.getServerName().equals(player))
				return c.getClientName();
		return "";
	}
	
	public static SburbConnection getMainConnection(String player, boolean isClient)
	{
		for(SburbConnection c : connections)
			if(c.isMain)
				if(isClient ? (c.getClientName().equals(player))
						: c.getServerName().equals(player))
					return c;
		return null;
	}
	
	public static boolean giveItems(String player){
		SburbConnection c = getClientConnection(player);
		if(c != null && !c.isMain && getAssociatedPartner(c.getClientName(), true).isEmpty()
				&& getAssociatedPartner(c.getServerName(), false).isEmpty()) {
			c.isMain = true;
			SburbHandler.onFirstItemGiven(c);
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
		if(player.dimension == -1)
			return;
		TileEntityComputer te = getComputer(player);
		if(te == null)
			return;
		if(!isClient){	//Is server
			if(serversOpen.containsKey(player.owner) || resumingServers.containsKey(player.owner))
				return;
			if(otherPlayer.isEmpty()){	//Wants to open
				if(resumingClients.containsKey(getAssociatedPartner(player.owner, false)))
					connectTo(player, false, getAssociatedPartner(player.owner, false), resumingClients);
				else{
					te.getData(1).setBoolean("isOpen", true);
					serversOpen.put(player.owner, player);
				}
			}
			else if(!otherPlayer.isEmpty() && getAssociatedPartner(player.owner, false).equals(otherPlayer)){	//Wants to resume
				if(resumingClients.containsKey(otherPlayer))	//The client is already waiting
					connectTo(player, false, otherPlayer, resumingClients);
				else {	//Client is not currently trying to resume
					te.getData(1).setBoolean("isOpen", true);
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
					te.getData(0).setBoolean("isResuming", true);
					resumingClients.put(player.owner, player);
				}
			}
			else if(serversOpen.containsKey(otherPlayer))	//If the server is open.
				connectTo(player, true, otherPlayer, serversOpen);
		}
		te.getWorld().markBlockForUpdate(te.getPos());
		updateAll();
	}
	
	public static void closeConnection(String player, String otherPlayer, boolean isClient){
		if(otherPlayer.isEmpty()){
			if(isClient)
			{
				if(movingComputers.contains(resumingClients.get(player)))
					return;
				TileEntityComputer te = getComputer(resumingClients.remove(player));
				if(te != null)
				{
					te.getData(0).setBoolean("isResuming", false);
					te.latestmessage.put(0, "computer.messageResumeStop");
					te.getWorld().markBlockForUpdate(te.getPos());
				}
			} else if(serversOpen.containsKey(player))
			{
				if(movingComputers.contains(serversOpen.get(player)))
					return;
				TileEntityComputer te = getComputer(serversOpen.remove(player));
				if(te != null)
				{
					te.getData(1).setBoolean("isOpen", false);
					te.latestmessage.put(1, "computer.messageClosedServer");
					te.getWorld().markBlockForUpdate(te.getPos());
				}
			} else if(resumingServers.containsKey(player))
			{
				if(movingComputers.contains(resumingServers.get(player)))
					return;
				TileEntityComputer te = getComputer(resumingServers.remove(player));
				if(te != null)
				{
					te.getData(1).setBoolean("isOpen", false);
					te.latestmessage.put(1, "computer.messageResumeStop");
					te.getWorld().markBlockForUpdate(te.getPos());
				}
			} else Debug.print("[SKAIANET] Got disconnect request but server is not open! "+player);
		} else {
			SburbConnection c = isClient?getConnection(player, otherPlayer):getConnection(otherPlayer, player);
			if(c != null)
			{
				if(c.isActive)
				{
					if(movingComputers.contains(isClient ? c.client : c.server))
						return;
					TileEntityComputer cc = getComputer(c.client), sc = getComputer(c.server);
					if(cc != null)
					{
						cc.getData(0).setBoolean("connectedToServer", false);
						cc.latestmessage.put(0, "computer.messageClosed");
						cc.getWorld().markBlockForUpdate(cc.getPos());
					}
					if(sc != null)
					{
						sc.getData(1).setString("connectedClient", "");
						sc.latestmessage.put(1, "computer.messageClosed");
						sc.getWorld().markBlockForUpdate(sc.getPos());
					}
					SessionHandler.onConnectionClosed(c, true);
					ServerEditHandler.onDisconnect(c);
					if(c.isMain)
						c.isActive = false;	//That's everything that is neccesary.
					else connections.remove(c);
				} else if(getAssociatedPartner(player, isClient).equals(otherPlayer))
				{
					if(movingComputers.contains(isClient?resumingClients.get(player):resumingServers.get(player)))
						return;
					TileEntityComputer te = getComputer(isClient?resumingClients.remove(player):resumingServers.remove(player));
					if(te != null)
					{
						te.latestmessage.put(isClient?0:1, "computer.messageResumeStop");
						te.getWorld().markBlockForUpdate(te.getPos());
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
				Debug.print("SessionHandler denied, reason:"+s);
				connections.remove(c);
				TileEntityComputer cte = getComputer(c.client);
				if(cte != null)
					cte.latestmessage.put(0, s);
				map.put(c.server.owner, c.server);
				return;
			}
		}
		SburbConnection conn;
		if(newConnection && (conn = getMainConnection(c.getClientName(), true)) != null)	//Copy client associated variables
		{
			if(conn.getServerName().equals(".null"))
			{
				c.isMain = true;
				connections.remove(conn);
				SessionHandler.onConnectionClosed(conn, false);
			}
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
			c2.getWorld().markBlockForUpdate(c2.getPos());
	}
	
	public static void requestInfo(String p0, String p1){
		checkData();
		String[] s = infoToSend.get(p0);
		EntityPlayerMP player = MinecraftServer.getServer().getConfigurationManager().getPlayerByUsername(p0);
		if(s == null || player == null){
			Debug.print("[SKAIANET] Player \"" + p0 + "\" sent a request without being online!");
			return;
		}
		if(MinestuckConfig.privateComputers && !p0.equals(p1) && MinecraftServer.getServer().getConfigurationManager().getOppedPlayers().getEntry(player.getGameProfile()) == null)
		{
			player.addChatComponentMessage(new ChatComponentText("[Minestuck] ").setChatStyle(new ChatStyle().setColor(EnumChatFormatting.RED)).appendSibling(new ChatComponentTranslation("message.privateComputerMessage")));
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
	
	public static void saveData(NBTTagCompound data) {
		checkData();
		
		NBTTagCompound nbt = new NBTTagCompound();
		NBTTagList list = new NBTTagList();
		
		for(Session s : SessionHandler.sessions)
			list.appendTag(s.write());
		
		nbt.setTag("sessions", list);
		
		String[] s = {"serversOpen","resumingClients","resumingServers"};
		@SuppressWarnings("unchecked")
		Map<String, ComputerData>[] maps = new Map[]{serversOpen, resumingClients, resumingServers};
		for(int i = 0; i < 3; i++) {
			list = new NBTTagList();
			for(ComputerData c:maps[i].values())
				list.appendTag(c.write());
			nbt.setTag(s[i], list);
		}
		
		data.setTag("skaianet", nbt);
	}
	
	public static void loadData(NBTTagCompound nbt){
		connections.clear();
		serversOpen.clear();	
		resumingClients.clear();
		resumingServers.clear();
		SessionHandler.sessions.clear();
		SessionHandler.sessionsByName.clear();
		if(nbt != null)
		{
			NBTTagList list = nbt.getTagList("sessions", 10);
			for(int i = 0; i < list.tagCount(); i++)
			{
				Session session = new Session().read(list.getCompoundTagAt(i));
				SessionHandler.sessions.add(session);
				if(session.isCustom())
				{
					if(SessionHandler.sessionsByName.containsKey(session.name))
						Debug.printf("A session with a duplicate name has been loaded! (Session '%s') Either a bug or someone messing with the data file.", session.name);
					SessionHandler.sessionsByName.put(session.name, session);
				}
			}
			
			String[] s = {"serversOpen","resumingClients","resumingServers"};
			List<Map<String, ComputerData>> maps = new ArrayList<Map<String, ComputerData>>();
			maps.add(serversOpen);
			maps.add(resumingClients);
			maps.add(resumingServers);
			for(int e = 0; e < 3; e++) {
				list = nbt.getTagList(s[e], 10);
				if(list == null)
					continue;
				for(int i = 0; i < list.tagCount(); i++){
					NBTTagCompound cmp = list.getCompoundTagAt(i);
					ComputerData c = new ComputerData();
					c.read(cmp);
					maps.get(e).put(c.owner, c);
				}
			}
		}
		
		SessionHandler.serverStarted();
	}
	
	static void updateAll(){
		checkData();
		for(String s : infoToSend.keySet())
			updatePlayer(s);
	}
	
	static void updatePlayer(String player){
		String[] str = infoToSend.get(player);
		EntityPlayerMP playerMP = MinecraftServer.getServer().getConfigurationManager().getPlayerByUsername(player);
		if(str == null || playerMP == null)//If the player disconnected
			return;
		String playerEnc = UsernameHandler.encode(player);
		for(SburbConnection c : connections)
			if(c.isActive && (c.getClientName().equals(playerEnc) || c.getServerName().equals(playerEnc))) {
				playerMP.triggerAchievement(MinestuckAchievementHandler.setupConnection);
				break;
			}
		for(String s : str)
			if(s != null) {
				MinestuckPacket packet = MinestuckPacket.makePacket(Type.SBURB_INFO, generateClientInfo(s));
				MinestuckChannelHandler.sendToPlayer(packet, playerMP);
			}
	}
	
	static Object[] generateClientInfo(String player){
		List<Object> list = new ArrayList<Object>();
		list.add(player);
		
		list.add(resumingClients.containsKey(player));
		list.add(resumingServers.containsKey(player));
		
		List<String> playerList = SessionHandler.getServerList(player);
		list.add(playerList.size());
		list.addAll(playerList);
		
		for(SburbConnection c : connections)
			if(c.getClientName().equals(player) && !c.getServerName().equals(".null") || c.getServerName().equals(player))
				list.add(c);
		
		return list.toArray();
	}
	
	static void checkData() {
		ServerConfigurationManager scm = MinecraftServer.getServer().getConfigurationManager();
		Iterator<String> iter0 = infoToSend.keySet().iterator();
		while(iter0.hasNext())
			if(scm.getPlayerByUsername(iter0.next()) == null){
				//Debug.print("[SKAIANET] Player disconnected, removing data.");
				iter0.remove();
			}
		
		@SuppressWarnings("unchecked")
		Iterator<ComputerData>[] iter1 = new Iterator[]{serversOpen.values().iterator(),resumingClients.values().iterator(),resumingServers.values().iterator()};
		
		for(Iterator<ComputerData> i : iter1)
			while(i.hasNext()) {
				ComputerData data = i.next();
				if(getComputer(data) == null || data.dimension == -1 || !getComputer(data).owner.equals(data.owner)
						|| !(resumingClients.containsValue(data)?getComputer(data).getData(0).getBoolean("isResuming")
								:getComputer(data).getData(1).getBoolean("isOpen"))) {
					//Debug.print("[SKAIANET] Invalid computer in waiting list!");
					i.remove();
				}
			}
		
		Iterator<SburbConnection> iter2 = connections.iterator();
		while(iter2.hasNext())
		{
			SburbConnection c = iter2.next();
			if(c.getClientName().isEmpty() || c.getServerName().isEmpty())
			{
				Debug.print("Found a broken connection with the client \""+c.getClientName()+"\" and server \""+c.getServerName()+". If this message continues to show up, something isn't working as it should.");
				iter2.remove();
				continue;
			}
			if(c.isActive)
			{
				TileEntityComputer cc = getComputer(c.client), sc = getComputer(c.server);
				if(cc == null || sc == null || c.client.dimension == -1 || c.server.dimension == -1 || !cc.owner.equals(c.getClientName())
						|| !sc.owner.equals(c.getServerName()) || !cc.getData(0).getBoolean("connectedToServer") || !sc.getData(1).getString("connectedClient").equals(c.getClientName())){
					//Debug.print("[SKAIANET] Invalid computer in connection.");
					if(!c.isMain)
						iter2.remove();
					else c.isActive = false;
					SessionHandler.onConnectionClosed(c, true);
					ServerEditHandler.onDisconnect(c);
					
					if(cc != null){
						cc.getData(0).setBoolean("connectedToServer", false);
						cc.latestmessage.put(0, "computer.messageClosed");
						cc.getWorld().markBlockForUpdate(cc.getPos());
					} else if(sc != null){
						sc.getData(1).setString("connectedClient", "");
						sc.latestmessage.put(1, "computer.messageClosed");
						sc.getWorld().markBlockForUpdate(sc.getPos());
					}
				}
				if(cc != null && c.enteredGame && c.inventory == null && c.centerX == 0 && c.centerZ == 0) {	//If the center location isn't defined
					c.centerX = cc.getPos().getX();
					c.centerZ = cc.getPos().getZ();
					c.inventory = new NBTTagList();
				}
				if(cc != null && c.enteredGame && !MinestuckDimensionHandler.isLandDimension(c.clientHomeLand))
					c.clientHomeLand = c.client.dimension;
			}
			if(c.enteredGame && !MinestuckDimensionHandler.isLandDimension(c.clientHomeLand))
			{
				EntityPlayerMP player = MinecraftServer.getServer().getConfigurationManager().getPlayerByUsername(UsernameHandler.decode(c.getClientName()));
				if(player != null)
				{
					c.clientHomeLand = player.dimension;
					if(!MinestuckDimensionHandler.isLandDimension(c.clientHomeLand))
					{
						iter2.remove();
						SessionHandler.onConnectionClosed(c, false);
						if(c.isActive) {
							TileEntityComputer cc = getComputer(c.client), sc = getComputer(c.server);
							cc.getData(0).setBoolean("connectedToServer", false);
							cc.latestmessage.put(0, "computer.messageClosed");
							cc.getWorld().markBlockForUpdate(cc.getPos());
							sc.getData(1).setString("connectedClient", "");
							sc.latestmessage.put(1, "computer.messageClosed");
							sc.getWorld().markBlockForUpdate(sc.getPos());
						}
					}
				}
			}
		}
		
		if(MinestuckConfig.privateComputers)
		{
			for(Entry<String,String[]> entry : infoToSend.entrySet())
				for(int i = 0; i < entry.getValue().length; i++)
					if(entry.getValue()[i] != null && !entry.getValue()[i].equals(UsernameHandler.encode(entry.getKey())))
						entry.getValue()[i] = null;
		}
	}
	
	public static SburbConnection getConnection(String client, String server)
	{
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
	public static TileEntityComputer getComputer(ComputerData data)
	{
		if(data == null)
			return null;
		World world = MinecraftServer.getServer().worldServerForDimension(data.dimension);
		if(world == null)
			return null;
		TileEntity te = world.getTileEntity(new BlockPos(data.x, data.y, data.z));
		if(te == null || !(te instanceof TileEntityComputer))
			return null;
		else return (TileEntityComputer)te;
	}
	
	public static int enterMedium(EntityPlayerMP player, int dimensionId)
	{
		String username = UsernameHandler.encode(player.getName());
		SburbConnection c = getMainConnection(username, true);
		if(c == null)
		{
			c = getClientConnection(username);
			if(c == null)
			{
				Debug.print("Player entered without connection. Creating connection with self... "+dimensionId);
				c = new SburbConnection();
				c.isActive = false;
				c.isMain = true;
				c.clientName = username;
				c.serverName = username;
				if(SessionHandler.onConnectionCreated(c) == null)
				{
					SburbHandler.onFirstItemGiven(c);
					connections.add(c);
				}
				else if(SessionHandler.singleSession)
				{
					SessionHandler.singleSession = false;
					SessionHandler.split();
					if(SessionHandler.onConnectionCreated(c) == null)
					{
						SburbHandler.onFirstItemGiven(c);
						connections.add(c);
					}
				}
			}
			else giveItems(username);
		}
		else if(c.enteredGame)
		{
			return c.clientHomeLand;
		}
		
		c.clientHomeLand = dimensionId;
		c.enteredGame = true;
		SburbHandler.onGameEntered(c);
		
		c.centerX = (int)player.posX;
		c.centerZ = (int)player.posZ;
		updateAll();
		return dimensionId;
	}
	
	public static void resetGivenItems()
	{
		for(SburbConnection c : connections)
		{
			for(int i = 0; i < c.givenItemList.length; i++)
				c.givenItemList[i] = false;
			if(ServerEditHandler.getData(c) != null)
			{
				MinestuckPacket packet = MinestuckPacket.makePacket(Type.SERVER_EDIT, c.givenItemList);
				MinestuckChannelHandler.sendToPlayer(packet, ServerEditHandler.getData(c).getEditor());
			}
		}
	}
	
	public static void movingComputer(TileEntityComputer oldTE, TileEntityComputer newTE)
	{
		ComputerData dataOld = ComputerData.createData(oldTE), dataNew = ComputerData.createData(newTE);
		for(SburbConnection c : connections)
		{
			if(dataOld.equals(c.client))
				c.client = dataNew;
			if(dataOld.equals(c.server))
				c.server = dataNew;
		}
		
		resumingClients.replace(dataOld.owner, dataOld, dataNew);
		resumingServers.replace(dataOld.owner, dataOld, dataNew);
		serversOpen.replace(dataOld.owner, dataOld, dataNew);
		
		movingComputers.add(dataNew);
	}
	
	public static void clearMovingList()
	{
		movingComputers.clear();
	}
	
}
