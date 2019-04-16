package com.mraof.minestuck.network.skaianet;

import com.mraof.minestuck.MinestuckConfig;
import com.mraof.minestuck.advancements.MinestuckCriteriaTriggers;
import com.mraof.minestuck.editmode.EditData;
import com.mraof.minestuck.editmode.ServerEditHandler;
import com.mraof.minestuck.event.ConnectionClosedEvent;
import com.mraof.minestuck.event.ConnectionCreatedEvent;
import com.mraof.minestuck.network.MinestuckChannelHandler;
import com.mraof.minestuck.network.MinestuckPacket;
import com.mraof.minestuck.network.MinestuckPacket.Type;
import com.mraof.minestuck.tileentity.TileEntityComputer;
import com.mraof.minestuck.tracker.MinestuckPlayerTracker;
import com.mraof.minestuck.util.Debug;
import com.mraof.minestuck.util.IdentifierHandler;
import com.mraof.minestuck.util.IdentifierHandler.PlayerIdentifier;
import com.mraof.minestuck.util.Teleport;
import com.mraof.minestuck.world.MinestuckDimensionHandler;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.server.management.UserListOpsEntry;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.Style;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.FMLCommonHandler;

import java.util.*;
import java.util.Map.Entry;

/**
 * This class handles server sided stuff about the sburb connection network.
 * This class also handles the main saving and loading.
 * @author kirderf1
 */
public class SkaianetHandler {
	
	static Map<PlayerIdentifier, ComputerData> serversOpen = new TreeMap<PlayerIdentifier, ComputerData>();
	private static Map<PlayerIdentifier, ComputerData> resumingClients = new HashMap<PlayerIdentifier, ComputerData>();
	private static Map<PlayerIdentifier, ComputerData> resumingServers = new HashMap<PlayerIdentifier, ComputerData>();
	static List<SburbConnection> connections = new ArrayList<SburbConnection>();
	private static Map<PlayerIdentifier, PlayerIdentifier[]> infoToSend = new HashMap<PlayerIdentifier, PlayerIdentifier[]>();	//Key: player, value: data to send to player
	private static List<ComputerData> movingComputers = new ArrayList<ComputerData>();
	
	/**
	 * Chains of lands to be used by the skybox render
	 */
	private static List<List<Integer>> landChains = new LinkedList<>();
	
	public static SburbConnection getClientConnection(PlayerIdentifier client)
	{
		for(SburbConnection c : connections)
			if(c.isActive && c.client.owner.equals(client))
				return c;
		return null;
	}
	
	public static PlayerIdentifier getAssociatedPartner(PlayerIdentifier player, boolean isClient)
	{
		for(SburbConnection c : connections)
			if(c.isMain)
				if(isClient && c.getClientIdentifier().equals(player))
					return c.getServerIdentifier().equals(IdentifierHandler.nullIdentifier) ? null : c.getServerIdentifier();
				else if(!isClient && c.getServerIdentifier().equals(player))
					return c.getClientIdentifier();
		return null;
	}
	
	public static SburbConnection getMainConnection(PlayerIdentifier player, boolean isClient)
	{
		if(player.equals(IdentifierHandler.nullIdentifier))
			return null;
		for(SburbConnection c : connections)
			if(c.isMain)
				if(isClient ? (c.getClientIdentifier().equals(player))
						: c.getServerIdentifier().equals(player))
					return c;
		return null;
	}
	
	public static boolean giveItems(PlayerIdentifier player)
	{
		SburbConnection c = getClientConnection(player);
		if(c != null && !c.isMain && getAssociatedPartner(c.getClientIdentifier(), true) == null
				&& getAssociatedPartner(c.getServerIdentifier(), false) == null) {
			c.isMain = true;
			SburbHandler.onFirstItemGiven(c);
			updatePlayer(c.getClientIdentifier());
			updatePlayer(c.getServerIdentifier());
			return true;
		}
		return false;
	}
	
	/**
	 * Note that this is when a player logs in to the server.
	 * @param player
	 */
	public static void playerConnected(EntityPlayer player)
	{
		PlayerIdentifier identifier = IdentifierHandler.encode(player);
		PlayerIdentifier[] s = new PlayerIdentifier[5];
		s[0] = identifier;
		infoToSend.put(identifier, s);
		updatePlayer(identifier);
		MinestuckPacket packet = createLandChainPacket();
		MinestuckChannelHandler.sendToPlayer(packet, player);
	}
	
	public static void requestConnection(ComputerData player, PlayerIdentifier otherPlayer, boolean isClient)
	{
		if(player.dimension == -1)
			return;
		TileEntityComputer te = getComputer(player);
		if(te == null)
			return;
		if(!isClient)	//Is server
		{
			if(serversOpen.containsKey(player.owner) || resumingServers.containsKey(player.owner))
				return;
			if(otherPlayer == null)	//Wants to open
			{
				if(resumingClients.containsKey(getAssociatedPartner(player.owner, false)))
					connectTo(player, false, getAssociatedPartner(player.owner, false), resumingClients);
				else
				{
					te.getData(1).setBoolean("isOpen", true);
					serversOpen.put(player.owner, player);
				}
			}
			else if(otherPlayer != null && getAssociatedPartner(player.owner, false).equals(otherPlayer))	//Wants to resume
			{
				if(resumingClients.containsKey(otherPlayer))	//The client is already waiting
					connectTo(player, false, otherPlayer, resumingClients);
				else	//Client is not currently trying to resume
				{
					te.getData(1).setBoolean("isOpen", true);
					resumingServers.put(player.owner, player);
				}
			}
			else return;
		} else	//Is client
		{
			if(getClientConnection(player.owner) != null || resumingClients.containsKey(player.owner))
				return;
			PlayerIdentifier p = getAssociatedPartner(player.owner, true);
			if(p != null && (otherPlayer == null || p.equals(otherPlayer)))	//If trying to connect to the associated partner
			{
				if(resumingServers.containsKey(p))	//If server is "resuming".
					connectTo(player, true, p, resumingServers);
				else if(serversOpen.containsKey(p))	//If server is normally open.
					connectTo(player, true, p, serversOpen);
				else	//If server isn't open
				{
					te.getData(0).setBoolean("isResuming", true);
					resumingClients.put(player.owner, player);
				}
			}
			else if(serversOpen.containsKey(otherPlayer))	//If the server is open.
				connectTo(player, true, otherPlayer, serversOpen);
		}
		te.markBlockForUpdate();
		updateAll();
	}
	
	public static void closeConnection(PlayerIdentifier player, PlayerIdentifier otherPlayer, boolean isClient)
	{
		if(otherPlayer == null)
		{
			if(isClient)
			{
				if(movingComputers.contains(resumingClients.get(player)))
					return;
				TileEntityComputer te = getComputer(resumingClients.remove(player));
				if(te != null)
				{
					te.getData(0).setBoolean("isResuming", false);
					te.latestmessage.put(0, "computer.messageResumeStop");
					te.markBlockForUpdate();
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
					te.markBlockForUpdate();
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
					te.markBlockForUpdate();
				}
			} else Debug.warn("[SKAIANET] Got disconnect request but server is not open! "+player);
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
						cc.markBlockForUpdate();
					}
					if(sc != null)
					{
						sc.getData(1).setString("connectedClient", "");
						sc.latestmessage.put(1, "computer.messageClosed");
						sc.markBlockForUpdate();
					}
					SessionHandler.onConnectionClosed(c, true);
					ServerEditHandler.onDisconnect(c);
					if(c.isMain)
						c.isActive = false;	//That's everything that is neccesary.
					else connections.remove(c);
					
					ConnectionCreatedEvent.ConnectionType type = !c.isMain && getMainConnection(c.getClientIdentifier(), true) != null
							? ConnectionCreatedEvent.ConnectionType.SECONDARY : ConnectionCreatedEvent.ConnectionType.REGULAR;
					MinecraftForge.EVENT_BUS.post(new ConnectionClosedEvent(c, SessionHandler.getPlayerSession(c.getClientIdentifier()), type));
				} else if(getAssociatedPartner(player, isClient).equals(otherPlayer))
				{
					if(movingComputers.contains(isClient?resumingClients.get(player):resumingServers.get(player)))
						return;
					TileEntityComputer te = getComputer(isClient?resumingClients.remove(player):resumingServers.remove(player));
					if(te != null)
					{
						te.latestmessage.put(isClient?0:1, "computer.messageResumeStop");
						te.markBlockForUpdate();
					}
				}
			}
		}
		updateAll();
	}
	
	private static void connectTo(ComputerData player, boolean isClient, PlayerIdentifier otherPlayer, Map<PlayerIdentifier, ComputerData> map)
	{
		TileEntityComputer c1 = getComputer(player), c2 = getComputer(map.get(otherPlayer));
		if(c2 == null)
		{
			map.remove(otherPlayer);	//Invalid, should not be in the list
			return;
		}
		if(c1 == null)
			return;
		SburbConnection c;
		boolean newConnection = false;	//True if new, false if resuming.
		if(isClient)
		{
			c = getConnection(player.owner, otherPlayer);
			if(c == null)
			{
				c = new SburbConnection();
				connections.add(c);
				newConnection = true;
			}
			c.client = player;
			c.server = map.remove(otherPlayer);
			c.isActive = true;
		} else
		{
			c = getConnection(otherPlayer, player.owner);
			if(c == null)
				return;	//A server should only be able to resume
			c.client = map.remove(otherPlayer);
			c.server = player;
			c.isActive = true;
		}
		
		//Get session type for event
		Session s1 = SessionHandler.getPlayerSession(c.getClientIdentifier()), s2 = SessionHandler.getPlayerSession(c.getServerIdentifier());
		ConnectionCreatedEvent.SessionJoinType joinType = s1 == null || s2 == null ? ConnectionCreatedEvent.SessionJoinType.JOIN
				: s1 == s2 ? ConnectionCreatedEvent.SessionJoinType.INTERNAL : ConnectionCreatedEvent.SessionJoinType.MERGE;
		ConnectionCreatedEvent.ConnectionType type = ConnectionCreatedEvent.ConnectionType.REGULAR;
		
		boolean updateLandChain = false;
		if(newConnection)
		{
			SburbConnection conn = getMainConnection(c.getClientIdentifier(), true);
			if(conn != null && conn.getServerIdentifier().equals(IdentifierHandler.nullIdentifier) && getMainConnection(c.getServerIdentifier(), false) == null)
			{
				connections.remove(c);
				conn.client = c.client;
				conn.server = c.server;
				conn.serverIdentifier = c.getServerIdentifier();
				conn.isActive = true;
				c = conn;
				type = ConnectionCreatedEvent.ConnectionType.RESUME;
				updateLandChain = true;
			} else
			{
				String s = SessionHandler.onConnectionCreated(c);
				if(s != null)
				{
					Debug.warnf("SessionHandler denied connection between %s and %s, reason: %s", c.getClientIdentifier().getUsername(), c.getServerIdentifier().getUsername(), s);
					connections.remove(c);
					TileEntityComputer cte = getComputer(c.client);
					if(cte != null)
						cte.latestmessage.put(0, s);
					map.put(c.server.owner, c.server);
					return;
				
				}
				SburbHandler.onConnectionCreated(c);
				
				if(conn != null)
				{
					c.enteredGame = conn.enteredGame;
					c.canSplit = conn.canSplit;
					c.centerX = conn.centerX;
					c.centerZ = conn.centerZ;
					c.clientHomeLand = conn.clientHomeLand;
					c.artifactType = conn.artifactType;
					if(c.inventory != null)
						c.inventory = (NBTTagList) conn.inventory.copy();
					type = ConnectionCreatedEvent.ConnectionType.SECONDARY;
				}
			}
		} else type = ConnectionCreatedEvent.ConnectionType.RESUME;
		
		c1.connected(otherPlayer, isClient);
		c2.connected(player.owner, !isClient);
		if(c1 != c2)
			c2.markBlockForUpdate();
		
		MinecraftForge.EVENT_BUS.post(new ConnectionCreatedEvent(c, SessionHandler.getPlayerSession(c.getClientIdentifier()), type, joinType));
		if(updateLandChain)
			sendLandChainUpdate();
	}
	
	public static void requestInfo(EntityPlayer player, PlayerIdentifier p1)
	{
		checkData();
		PlayerIdentifier p0 = IdentifierHandler.encode(player);
		PlayerIdentifier[] s = infoToSend.get(p0);
		if(s == null)
		{
			Debug.error("[SKAIANET] Something went wrong with player \"" + player.getName() + "\"'s skaianet data!");
			return;
		}
		UserListOpsEntry opsEntry = player.getServer().getPlayerList().getOppedPlayers().getEntry(player.getGameProfile());
		if(MinestuckConfig.privateComputers && !p0.equals(p1) && !(opsEntry != null && opsEntry.getPermissionLevel() >= 2))
		{
			player.sendMessage(new TextComponentString("[Minestuck] ").setStyle(new Style().setColor(TextFormatting.RED)).appendSibling(new TextComponentTranslation("message.privateComputerMessage")));
			return;
		}
		int i = 0;
		for(;i < s.length; i++)
		{
			if(s[i] == null)
				break;
			if(s[i].equals(p1))
			{
				Debug.warnf("[Skaianet] Player %s already got the requested data.", player.getName());
				updatePlayer(p0);	//Update anyway, to fix whatever went wrong
				return;
			}
		}
		if(i == s.length)	//If the array is full, increase size with 5.
		{
			PlayerIdentifier[] newS = new PlayerIdentifier[s.length+5];
			System.arraycopy(s, 0, newS, 0, s.length);
			s = newS;
			infoToSend.put(p0, s);
		}
		
		s[i] = p1;
		
		updatePlayer(p0);
	}
	
	public static void saveData(NBTTagCompound data)
	{
		checkData();
		
		NBTTagCompound nbt = new NBTTagCompound();
		NBTTagList list = new NBTTagList();
		
		for(Session s : SessionHandler.sessions)
			list.appendTag(s.write());
		
		nbt.setTag("sessions", list);
		
		String[] s = {"serversOpen","resumingClients","resumingServers"};
		@SuppressWarnings("unchecked")
		Map<PlayerIdentifier, ComputerData>[] maps = new Map[]{serversOpen, resumingClients, resumingServers};
		for(int i = 0; i < 3; i++)
		{
			list = new NBTTagList();
			for(ComputerData c:maps[i].values())
				list.appendTag(c.write());
			nbt.setTag(s[i], list);
		}
		
		data.setTag("skaianet", nbt);
	}
	
	public static void loadData(NBTTagCompound nbt)
	{
		connections.clear();
		serversOpen.clear();	
		resumingClients.clear();
		resumingServers.clear();
		SessionHandler.sessions.clear();
		SessionHandler.sessionsByName.clear();
		SburbHandler.titleSelectionMap.clear();
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
						Debug.warnf("A session with a duplicate name has been loaded! (Session '%s') Either a bug or someone messing with the data file.", session.name);
					SessionHandler.sessionsByName.put(session.name, session);
				}
			}
			
			String[] s = {"serversOpen","resumingClients","resumingServers"};
			Map<PlayerIdentifier, ComputerData>[] maps = new Map[] {serversOpen, resumingClients, resumingServers};
			for(int e = 0; e < 3; e++)
			{
				list = nbt.getTagList(s[e], 10);
				for(int i = 0; i < list.tagCount(); i++)
				{
					NBTTagCompound cmp = list.getCompoundTagAt(i);
					ComputerData c = new ComputerData();
					c.read(cmp);
					maps[e].put(c.owner, c);
				}
			}
		}
		
		SessionHandler.serverStarted();
		
		updateLandChain();
	}
	
	public static MinestuckPacket createLandChainPacket()
	{
		return MinestuckPacket.makePacket(Type.SBURB_INFO, landChains);
	}
	
	static void updateLandChain()
	{
		landChains.clear();
		
		Set<Integer> checked = new HashSet<>();
		for(SburbConnection c : connections)
		{
			if(c.isMain() && c.enteredGame() && !checked.contains(c.clientHomeLand))
			{
				LinkedList<Integer> chain = new LinkedList<>();
				chain.add(c.clientHomeLand);
				checked.add(c.clientHomeLand);
				SburbConnection cIter = c;
				while(true)
				{
					cIter = getMainConnection(cIter.getClientIdentifier(), false);
					if(cIter != null && cIter.enteredGame())
					{
						if(!checked.contains(cIter.clientHomeLand))
						{
							chain.addLast(cIter.clientHomeLand);
							checked.add(cIter.clientHomeLand);
						} else break;
					} else
					{
						chain.addLast(0);
						break;
					}
				}
				cIter = c;
				while(true)
				{
					cIter = getMainConnection(cIter.getServerIdentifier(), true);
					if(cIter != null && cIter.enteredGame() && !checked.contains(cIter.clientHomeLand))
					{
						chain.addFirst(cIter.clientHomeLand);
						checked.add(cIter.clientHomeLand);
					} else
					{
						break;
					}
				}
				landChains.add(chain);
			}
		}
	}
	
	static void sendLandChainUpdate()
	{
		updateLandChain();
		MinestuckPacket packet = createLandChainPacket();
		MinestuckChannelHandler.sendToAllPlayers(packet);
	}
	
	static void updateAll()
	{
		checkData();
		for(PlayerIdentifier i : infoToSend.keySet())
			updatePlayer(i);
	}
	
	private static void updatePlayer(PlayerIdentifier player)
	{
		PlayerIdentifier[] iden = infoToSend.get(player);
		EntityPlayerMP playerMP = player.getPlayer();
		if(iden == null || playerMP == null)//If the player disconnected
			return;
		for(SburbConnection c : connections)
			if(c.isActive && (c.getClientIdentifier().equals(player) || c.getServerIdentifier().equals(player)))
			{
				MinestuckCriteriaTriggers.SBURB_CONNECTION.trigger(playerMP);
				break;
			}
		for(PlayerIdentifier i : iden)
		{
			if(i != null)
			{
				MinestuckPacket packet = MinestuckPacket.makePacket(Type.SBURB_INFO, generateClientInfo(i));
				MinestuckChannelHandler.sendToPlayer(packet, playerMP);
			}
		}
	}
	
	private static Object[] generateClientInfo(PlayerIdentifier player)
	{
		List<Object> list = new ArrayList<Object>();
		list.add(player.getId());
		
		list.add(resumingClients.containsKey(player));
		list.add(resumingServers.containsKey(player) || serversOpen.containsKey(player));
		
		List<Object> playerList = SessionHandler.getServerList(player);
		list.add(playerList.size()/2);
		list.addAll(playerList);
		
		for(SburbConnection c : connections)
			if(c.getClientIdentifier().equals(player) || c.getServerIdentifier().equals(player))
				list.add(c);
		
		return list.toArray();
	}
	
	private static void checkData()
	{
		if(!MinestuckConfig.skaianetCheck)
			return;
		
		Iterator<PlayerIdentifier> iter0 = infoToSend.keySet().iterator();
		while(iter0.hasNext())
			if(iter0.next().getPlayer() == null)
			{
				//Debug.print("[SKAIANET] Player disconnected, removing data.");
				iter0.remove();
			}
		
		@SuppressWarnings("unchecked")
		Iterator<ComputerData>[] iter1 = new Iterator[]{serversOpen.values().iterator(),resumingClients.values().iterator(),resumingServers.values().iterator()};
		
		for(Iterator<ComputerData> i : iter1)
			while(i.hasNext())
			{
				ComputerData data = i.next();
				if(getComputer(data) == null || data.dimension == -1 || !getComputer(data).owner.equals(data.owner)
						|| !(i == iter1[1] && getComputer(data).getData(0).getBoolean("isResuming")
								|| i != iter1[1] && getComputer(data).getData(1).getBoolean("isOpen")))
				{
					Debug.warn("[SKAIANET] Invalid computer in waiting list!");
					i.remove();
				}
			}
		
		Iterator<SburbConnection> iter2 = connections.iterator();
		while(iter2.hasNext())
		{
			SburbConnection c = iter2.next();
			if(c.getClientIdentifier() == null || c.getServerIdentifier() == null)
			{
				Debug.warn("Found a broken connection with the client \""+c.getClientIdentifier()+"\" and server \""+c.getServerIdentifier()+". If this message continues to show up, something isn't working as it should.");
				iter2.remove();
				continue;
			}
			if(c.isActive)
			{
				TileEntityComputer cc = getComputer(c.client), sc = getComputer(c.server);
				if(cc == null || sc == null || c.client.dimension == -1 || c.server.dimension == -1 || !c.getClientIdentifier().equals(cc.owner)
						|| !c.getServerIdentifier().equals(sc.owner) || !cc.getData(0).getBoolean("connectedToServer"))
				{
					Debug.warnf("[SKAIANET] Invalid computer in connection between %s and %s.", c.getClientIdentifier(), c.getServerIdentifier());
					if(!c.isMain)
						iter2.remove();
					else c.isActive = false;
					SessionHandler.onConnectionClosed(c, true);
					ServerEditHandler.onDisconnect(c);
					
					if(cc != null)
					{
						cc.getData(0).setBoolean("connectedToServer", false);
						cc.latestmessage.put(0, "computer.messageClosed");
						cc.markBlockForUpdate();
					} else if(sc != null)
					{
						sc.latestmessage.put(1, "computer.messageClosed");
						sc.markBlockForUpdate();
					}
				}
				if(cc != null && c.enteredGame && !MinestuckDimensionHandler.isLandDimension(c.clientHomeLand))
					c.clientHomeLand = c.client.dimension;
			}
			if(c.enteredGame && !MinestuckDimensionHandler.isLandDimension(c.clientHomeLand))
			{
				EntityPlayerMP player = c.getClientIdentifier().getPlayer();
				if(player != null)
				{
					c.clientHomeLand = player.dimension;
					if(!MinestuckDimensionHandler.isLandDimension(c.clientHomeLand))
					{
						iter2.remove();
						SessionHandler.onConnectionClosed(c, false);
						if(c.isActive)
						{
							TileEntityComputer cc = getComputer(c.client), sc = getComputer(c.server);
							cc.getData(0).setBoolean("connectedToServer", false);
							cc.latestmessage.put(0, "computer.messageClosed");
							cc.markBlockForUpdate();
							sc.getData(1).setString("connectedClient", "");
							sc.latestmessage.put(1, "computer.messageClosed");
							sc.markBlockForUpdate();
						}
					}
				}
			}
		}
		
		if(MinestuckConfig.privateComputers)
		{
			for(Entry<PlayerIdentifier,PlayerIdentifier[]> entry : infoToSend.entrySet())
			{
				EntityPlayerMP player = entry.getKey().getPlayer();
				UserListOpsEntry opsEntry = player == null ? null : player.getServer().getPlayerList().getOppedPlayers().getEntry(player.getGameProfile());
				if(opsEntry != null && opsEntry.getPermissionLevel() >= 2)
					continue;
				
				for(int i = 0; i < entry.getValue().length; i++)
					if(entry.getValue()[i] != null && !entry.getValue()[i].equals(entry.getKey()))
						entry.getValue()[i] = null;
			}
		}
	}
	
	public static SburbConnection getConnection(PlayerIdentifier client, PlayerIdentifier server)
	{
		for(SburbConnection c : connections)
			if(c.getClientIdentifier().equals(client) && c.getServerIdentifier().equals(server))
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
		World world = FMLCommonHandler.instance().getMinecraftServerInstance().getWorld(data.dimension);
		if(world == null)
			return null;
		TileEntity te = world.getTileEntity(new BlockPos(data.x, data.y, data.z));
		if(te == null || !(te instanceof TileEntityComputer))
			return null;
		else return (TileEntityComputer)te;
	}
	
	public static SburbConnection getServerConnection(ComputerData data)
	{
		for(SburbConnection c : connections)
			if(c.isActive && c.server.equals(data))
				return c;
		return null;
	}
	
	public static int enterMedium(EntityPlayerMP player, int dimensionId, Teleport.ITeleporter teleport)
	{
		PlayerIdentifier username = IdentifierHandler.encode(player);
		SburbConnection c = getMainConnection(username, true);
		if(c == null)
		{
			c = getClientConnection(username);
			if(c == null)
			{
				Debug.infof("Player %s entered without connection. Creating connection... ", player.getName());
				c = new SburbConnection();
				c.isActive = false;
				c.isMain = true;
				c.clientIdentifier = username;
				c.serverIdentifier = IdentifierHandler.nullIdentifier;
				String s = SessionHandler.onConnectionCreated(c);
				if(s == null)
				{
					SburbHandler.onFirstItemGiven(c);
					connections.add(c);
				}
				else if(SessionHandler.singleSession)
				{
					Debug.warnf("Failed to create connection: %s. Trying again with global session disabled for this world...", s);
					SessionHandler.singleSession = false;
					SessionHandler.split();
					s = SessionHandler.onConnectionCreated(c);
					if(s == null)
					{
						SburbHandler.onFirstItemGiven(c);
						connections.add(c);
					} else
					{
						Debug.errorf("Couldn't create a connection for %s: %s. Stopping entry.", player.getName(), s);
						return -1;
					}
				} else
				{
					Debug.errorf("Couldn't create a connection for %s: %s. Stopping entry.", player.getName(), s);
					return -1;
				}
			}
			else giveItems(username);
		}
		else if(c.enteredGame)
			return c.clientHomeLand;
		
		int x = (int) player.posX;
		if(player.posX < 0) x--;
		int z = (int) player.posZ;
		if (player.posZ < 0) z--;
		MinestuckDimensionHandler.setSpawn(dimensionId, new BlockPos(x, 128 - MinestuckConfig.artifactRange, z));
		c.clientHomeLand = dimensionId;
		SburbHandler.onLandCreated(c);
		
		if(teleport != null && Teleport.teleportEntity(player, dimensionId, teleport))
		{
			c.enteredGame = true;
			SburbHandler.onGameEntered(c);
			
			c.centerX = 0;
			c.centerZ = 0;
			c.useCoordinates = false;
			updateAll();
			sendLandChainUpdate();
		} else //TODO Look at effects of cancelling entry at this point
			Debug.errorf("Couldn't move %s to their Land. Stopping entry.", player.getName());
		return dimensionId;
	}
	
	public static void resetGivenItems()
	{
		for(SburbConnection c : connections)
		{
			for(int i = 0; i < c.givenItemList.length; i++)
				c.givenItemList[i] = false;
			c.unregisteredItems = new NBTTagList();
			EditData data = ServerEditHandler.getData(c);
			if(data != null)
			{
				MinestuckPacket packet = MinestuckPacket.makePacket(Type.SERVER_EDIT, c.givenItemList);
				MinestuckChannelHandler.sendToPlayer(packet, data.getEditor());
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
		
		if(resumingClients.containsKey(dataOld.owner) && resumingClients.get(dataOld.owner).equals(dataOld))
			resumingClients.put(dataOld.owner, dataNew);	//Used to be map.replace until someone had a NoSuchMethodError
		if(resumingServers.containsKey(dataOld.owner) && resumingServers.get(dataOld.owner).equals(dataOld))
			resumingServers.put(dataOld.owner, dataNew);
		if(serversOpen.containsKey(dataOld.owner) && serversOpen.get(dataOld.owner).equals(dataOld))
			serversOpen.put(dataOld.owner, dataNew);
		
		movingComputers.add(dataNew);
	}
	
	public static void clearMovingList()
	{
		movingComputers.clear();
	}
	
}
