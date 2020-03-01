package com.mraof.minestuck.skaianet;

import com.mojang.datafixers.Dynamic;
import com.mraof.minestuck.MinestuckConfig;
import com.mraof.minestuck.advancements.MSCriteriaTriggers;
import com.mraof.minestuck.computer.editmode.DeployList;
import com.mraof.minestuck.computer.editmode.EditData;
import com.mraof.minestuck.computer.editmode.ServerEditHandler;
import com.mraof.minestuck.event.ConnectionClosedEvent;
import com.mraof.minestuck.event.ConnectionCreatedEvent;
import com.mraof.minestuck.network.MSPacketHandler;
import com.mraof.minestuck.network.SkaianetInfoPacket;
import com.mraof.minestuck.player.IdentifierHandler;
import com.mraof.minestuck.player.PlayerIdentifier;
import com.mraof.minestuck.tileentity.ComputerTileEntity;
import com.mraof.minestuck.util.Debug;
import com.mraof.minestuck.util.LazyInstance;
import com.mraof.minestuck.world.MSDimensionTypes;
import com.mraof.minestuck.world.lands.LandInfo;
import com.mraof.minestuck.world.lands.LandTypePair;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.nbt.NBTDynamicOps;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.management.OpEntry;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.GlobalPos;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.Style;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraft.world.dimension.DimensionType;
import net.minecraftforge.common.DimensionManager;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.util.Constants;

import java.util.*;
import java.util.Map.Entry;

/**
 * This class handles server sided stuff about the sburb connection network.
 * This class also handles the main saving and loading.
 * @author kirderf1
 */
public final class SkaianetHandler
{
	public static final String PRIVATE_COMPUTER = "minestuck.private_computer";
	public static final String CLOSED_SERVER = "minestuck.closed_server_message";
	public static final String STOP_RESUME = "minestuck.stop_resume_message";
	public static final String CLOSED = "minestuck.closed_message";
	
	private static SkaianetHandler INSTANCE;
	
	Map<PlayerIdentifier, GlobalPos> serversOpen = new HashMap<>();
	private Map<PlayerIdentifier, GlobalPos> resumingClients = new HashMap<>();
	private Map<PlayerIdentifier, GlobalPos> resumingServers = new HashMap<>();
	List<SburbConnection> connections = new ArrayList<>();
	private Map<PlayerIdentifier, PlayerIdentifier[]> infoToSend = new HashMap<>();	//Key: player, value: data to send to player
	private List<GlobalPos> movingComputers = new ArrayList<>();
	SessionHandler sessionHandler = new SessionHandler(this);
	/**
	 * Changes to this map must also be done to {@link MSDimensionTypes#LANDS#dimToLandAspects}
	 */
	private final Map<ResourceLocation, LandInfo> typeToInfoContainer = new HashMap<>();
	
	/**
	 * Chains of lands to be used by the skybox render
	 */
	private LazyInstance<List<List<Integer>>> landChains = new LazyInstance<>(this::createLandChains);
	
	MinecraftServer mcServer;
	
	private SkaianetHandler()
	{}
	
	/**
	 * @param client The client player to search for
	 * @return The active connection with the client as its client player, or null if no such connection was found.
	 */
	public SburbConnection getActiveConnection(PlayerIdentifier client)
	{
		for(SburbConnection c : connections)
			if(c.isActive() && c.getClientIdentifier().equals(client))
				return c;
		return null;
	}
	
	public PlayerIdentifier getAssociatedPartner(PlayerIdentifier player, boolean isClient)
	{
		for(SburbConnection c : connections)
			if(c.isMain())
				if(isClient && c.getClientIdentifier().equals(player))
					return c.hasServerPlayer() ? c.getServerIdentifier() : null;
				else if(!isClient && c.getServerIdentifier().equals(player))
					return c.getClientIdentifier();
		return null;
	}
	
	public SburbConnection getMainConnection(PlayerIdentifier player, boolean isClient)
	{
		if(player == null || player.equals(IdentifierHandler.NULL_IDENTIFIER))
			return null;
		for(SburbConnection c : connections)
			if(c.isMain())
				if(isClient ? (c.getClientIdentifier().equals(player))
						: c.getServerIdentifier().equals(player))
					return c;
		return null;
	}
	
	public boolean giveItems(PlayerIdentifier player)
	{
		SburbConnection c = getActiveConnection(player);
		if(c != null && !c.isMain() && getAssociatedPartner(c.getClientIdentifier(), true) == null
				&& getAssociatedPartner(c.getServerIdentifier(), false) == null)
		{
			c.setIsMain();
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
	public void playerConnected(ServerPlayerEntity player)
	{
		PlayerIdentifier identifier = IdentifierHandler.encode(player);
		PlayerIdentifier[] s = new PlayerIdentifier[5];
		s[0] = identifier;
		infoToSend.put(identifier, s);
		updatePlayer(identifier);
		SkaianetInfoPacket packet = createLandChainPacket();
		MSPacketHandler.sendToPlayer(packet, player);
	}
	
	public void requestConnection(PlayerIdentifier player, GlobalPos computer, PlayerIdentifier otherPlayer, boolean isClient)
	{
		if(computer.getDimension() == DimensionType.THE_NETHER)
			return;
		ComputerTileEntity te = getComputer(mcServer, computer);
		if(te == null)
			return;
		if(!isClient)	//Is server
		{
			if(serversOpen.containsKey(player) || resumingServers.containsKey(player))
				return;
			if(otherPlayer == null)	//Wants to open
			{
				if(resumingClients.containsKey(getAssociatedPartner(player, false)))
					connectTo(player, computer, false, getAssociatedPartner(player, false), resumingClients);
				else
				{
					te.getData(1).putBoolean("isOpen", true);
					serversOpen.put(player, computer);
				}
			}
			else if(otherPlayer != null && getAssociatedPartner(player, false).equals(otherPlayer))	//Wants to resume
			{
				if(resumingClients.containsKey(otherPlayer))	//The client is already waiting
					connectTo(player, computer, false, otherPlayer, resumingClients);
				else	//Client is not currently trying to resume
				{
					te.getData(1).putBoolean("isOpen", true);
					resumingServers.put(player, computer);
				}
			}
			else return;
		} else	//Is client
		{
			if(getActiveConnection(player) != null || resumingClients.containsKey(player))
				return;
			PlayerIdentifier p = getAssociatedPartner(player, true);
			if(p != null && (otherPlayer == null || p.equals(otherPlayer)))	//If trying to connect to the associated partner
			{
				if(resumingServers.containsKey(p))	//If server is "resuming".
					connectTo(player, computer, true, p, resumingServers);
				else if(serversOpen.containsKey(p))	//If server is normally open.
					connectTo(player, computer, true, p, serversOpen);
				else	//If server isn't open
				{
					te.getData(0).putBoolean("isResuming", true);
					resumingClients.put(player, computer);
				}
			}
			else if(serversOpen.containsKey(otherPlayer))	//If the server is open.
				connectTo(player, computer, true, otherPlayer, serversOpen);
		}
		te.markBlockForUpdate();
		updateAll();
	}
	
	public void closeConnection(PlayerIdentifier player, PlayerIdentifier otherPlayer, boolean isClient)
	{
		if(otherPlayer == null)
		{
			if(isClient)
			{
				if(movingComputers.contains(resumingClients.get(player)))
					return;
				ComputerTileEntity te = getComputer(mcServer, resumingClients.remove(player));
				if(te != null)
				{
					te.getData(0).putBoolean("isResuming", false);
					te.latestmessage.put(0, STOP_RESUME);
					te.markBlockForUpdate();
				}
			} else if(serversOpen.containsKey(player))
			{
				if(movingComputers.contains(serversOpen.get(player)))
					return;
				ComputerTileEntity te = getComputer(mcServer, serversOpen.remove(player));
				if(te != null)
				{
					te.getData(1).putBoolean("isOpen", false);
					te.latestmessage.put(1, CLOSED_SERVER);
					te.markBlockForUpdate();
				}
			} else if(resumingServers.containsKey(player))
			{
				if(movingComputers.contains(resumingServers.get(player)))
					return;
				ComputerTileEntity te = getComputer(mcServer, resumingServers.remove(player));
				if(te != null)
				{
					te.getData(1).putBoolean("isOpen", false);
					te.latestmessage.put(1, STOP_RESUME);
					te.markBlockForUpdate();
				}
			} else Debug.warn("[SKAIANET] Got disconnect request but server is not open! "+player);
		} else {
			SburbConnection c = isClient?getConnection(player, otherPlayer):getConnection(otherPlayer, player);
			if(c != null)
			{
				if(c.isActive())
				{
					if(movingComputers.contains(isClient ? c.getClientComputer() : c.getServerComputer()))
						return;
					ComputerTileEntity cc = getComputer(mcServer, c.getClientComputer()), sc = getComputer(mcServer, c.getServerComputer());
					if(cc != null)
					{
						cc.getData(0).putBoolean("connectedToServer", false);
						cc.latestmessage.put(0, CLOSED);
						cc.markBlockForUpdate();
					}
					if(sc != null)
					{
						sc.getData(1).putString("connectedClient", "");
						sc.latestmessage.put(1, CLOSED);
						sc.markBlockForUpdate();
					}
					sessionHandler.onConnectionClosed(c, true);
					
					if(c.isMain())
						c.close();
					else connections.remove(c);
					
					ConnectionCreatedEvent.ConnectionType type = !c.isMain() && getMainConnection(c.getClientIdentifier(), true) != null
							? ConnectionCreatedEvent.ConnectionType.SECONDARY : ConnectionCreatedEvent.ConnectionType.REGULAR;
					MinecraftForge.EVENT_BUS.post(new ConnectionClosedEvent(mcServer, c, sessionHandler.getPlayerSession(c.getClientIdentifier()), type));
				} else if(getAssociatedPartner(player, isClient).equals(otherPlayer))
				{
					if(movingComputers.contains(isClient?resumingClients.get(player):resumingServers.get(player)))
						return;
					ComputerTileEntity te = getComputer(mcServer, (isClient?resumingClients:resumingServers).remove(player));
					if(te != null)
					{
						te.latestmessage.put(isClient?0:1, STOP_RESUME);
						te.markBlockForUpdate();
					}
				}
			}
		}
		updateAll();
	}
	
	private void connectTo(PlayerIdentifier player, GlobalPos computer, boolean isClient, PlayerIdentifier otherPlayer, Map<PlayerIdentifier, GlobalPos> map)
	{
		ComputerTileEntity c1 = getComputer(mcServer, computer), c2 = getComputer(mcServer, map.get(otherPlayer));
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
			c = getConnection(player, otherPlayer);
			if(c == null)
			{
				c = new SburbConnection(player, otherPlayer, this);
				connections.add(c);
				newConnection = true;
			}
			
			c.setActive(computer, map.remove(otherPlayer));
		} else
		{
			c = getConnection(otherPlayer, player);
			if(c == null)
				return;	//A server should only be able to resume
			
			c.setActive(map.remove(otherPlayer), computer);
		}
		
		//Get session type for event
		Session s1 = sessionHandler.getPlayerSession(c.getClientIdentifier()), s2 = sessionHandler.getPlayerSession(c.getServerIdentifier());
		ConnectionCreatedEvent.SessionJoinType joinType = s1 == null || s2 == null ? ConnectionCreatedEvent.SessionJoinType.JOIN
				: s1 == s2 ? ConnectionCreatedEvent.SessionJoinType.INTERNAL : ConnectionCreatedEvent.SessionJoinType.MERGE;
		ConnectionCreatedEvent.ConnectionType type = ConnectionCreatedEvent.ConnectionType.REGULAR;
		
		boolean updateLandChain = false;
		if(newConnection)
		{
			SburbConnection conn = getMainConnection(c.getClientIdentifier(), true);
			if(conn != null && !conn.hasServerPlayer() && getMainConnection(c.getServerIdentifier(), false) == null)
			{
				connections.remove(c);
				conn.setNewServerPlayer(c.getServerIdentifier());
				conn.setActive(c.getClientComputer(), c.getServerComputer());
				c = conn;
				type = ConnectionCreatedEvent.ConnectionType.RESUME;
				updateLandChain = true;
			} else
			{
				try
				{
					sessionHandler.onConnectionCreated(c);
				} catch(MergeResult.SessionMergeException e)
				{
					Debug.warnf("SessionHandler denied connection between %s and %s, reason: %s", c.getClientIdentifier().getUsername(), c.getServerIdentifier().getUsername(), e.getMessage());
					connections.remove(c);
					ComputerTileEntity cte = getComputer(mcServer, c.getClientComputer());
					if(cte != null)
						cte.latestmessage.put(0, e.getResult().translationKey());
					map.put(c.getServerIdentifier(), c.getServerComputer());
					return;
				
				}
				SburbHandler.onConnectionCreated(c);
				
				if(conn != null)
				{
					c.copyFrom(conn);
					type = ConnectionCreatedEvent.ConnectionType.SECONDARY;
				}
			}
		} else type = ConnectionCreatedEvent.ConnectionType.RESUME;
		
		c1.connected(otherPlayer, isClient);
		c2.connected(player, !isClient);
		if(c1 != c2)
			c2.markBlockForUpdate();
		
		MinecraftForge.EVENT_BUS.post(new ConnectionCreatedEvent(mcServer, c, sessionHandler.getPlayerSession(c.getClientIdentifier()), type, joinType));
		if(updateLandChain)
			reloadLandChains();
	}
	
	SburbConnection makeConnectionWithLand(LandTypePair landTypes, DimensionType dimensionName, PlayerIdentifier client, PlayerIdentifier server, Session session)
	{
		SburbConnection c = new SburbConnection(client, server, this);
		c.setIsMain();
		c.setLand(landTypes, dimensionName);
		
		session.connections.add(c);
		connections.add(c);
		SburbHandler.onConnectionCreated(c);
		
		return c;
	}
	
	public void requestInfo(ServerPlayerEntity player, PlayerIdentifier p1)
	{
		checkData();
		PlayerIdentifier p0 = IdentifierHandler.encode(player);
		PlayerIdentifier[] s = infoToSend.get(p0);
		if(s == null)
		{
			Debug.error("[SKAIANET] Something went wrong with player \"" + player.getName() + "\"'s skaianet data!");
			return;
		}
		OpEntry opsEntry = player.getServer().getPlayerList().getOppedPlayers().getEntry(player.getGameProfile());
		if(MinestuckConfig.privateComputers.get() && !p0.equals(p1) && !(opsEntry != null && opsEntry.getPermissionLevel() >= 2))
		{
			player.sendMessage(new StringTextComponent("[Minestuck] ").setStyle(new Style().setColor(TextFormatting.RED)).appendSibling(new TranslationTextComponent(PRIVATE_COMPUTER)));
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
	
	private void read(CompoundNBT nbt)
	{
		sessionHandler.read(nbt);
		
		String[] s = {"serversOpen", "resumingClients", "resumingServers"};
		Map<PlayerIdentifier, GlobalPos>[] maps = new Map[]{serversOpen, resumingClients, resumingServers};
		for(int e = 0; e < 3; e++)
		{
			ListNBT list = nbt.getList(s[e], Constants.NBT.TAG_COMPOUND);
			for(int i = 0; i < list.size(); i++)
			{
				CompoundNBT cmp = list.getCompound(i);
				GlobalPos c = GlobalPos.deserialize(new Dynamic<>(NBTDynamicOps.INSTANCE, cmp.get("computer")));
				maps[e].put(IdentifierHandler.load(cmp, "player"), c);
			}
		}
		
		sessionHandler.onLoad();
	}
	
	private CompoundNBT write(CompoundNBT compound)
	{
		//checkData();
		
		sessionHandler.write(compound);
		
		String[] s = {"serversOpen","resumingClients","resumingServers"};
		@SuppressWarnings("unchecked")
		Map<PlayerIdentifier, GlobalPos>[] maps = new Map[]{serversOpen, resumingClients, resumingServers};
		for(int i = 0; i < 3; i++)
		{
			ListNBT list = new ListNBT();
			for(Entry<PlayerIdentifier, GlobalPos> entry : maps[i].entrySet())
			{
				CompoundNBT nbt = new CompoundNBT();
				nbt.put("computer", entry.getValue().serialize(NBTDynamicOps.INSTANCE));
				entry.getKey().saveToNBT(nbt, "player");
				list.add(nbt);
			}
			compound.put(s[i], list);
		}
		
		return compound;
	}
	
	public SkaianetInfoPacket createLandChainPacket()
	{
		return SkaianetInfoPacket.landChains(landChains.get());
	}
	
	private List<List<Integer>> createLandChains()
	{
		List<List<Integer>> landChains = new ArrayList<>();
		
		Set<Integer> checked = new HashSet<>();
		for(SburbConnection c : connections)
		{
			if(c.isMain() && c.hasEntered() && !checked.contains(c.getClientDimension().getId()))
			{
				LinkedList<Integer> chain = new LinkedList<>();
				chain.add(c.getClientDimension().getId());
				checked.add(c.getClientDimension().getId());
				SburbConnection cIter = c;
				while(true)
				{
					cIter = getMainConnection(cIter.getClientIdentifier(), false);
					if(cIter != null && cIter.hasEntered())
					{
						if(!checked.contains(cIter.getClientDimension().getId()))
						{
							chain.addLast(cIter.getClientDimension().getId());
							checked.add(cIter.getClientDimension().getId());
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
					if(cIter != null && cIter.hasEntered() && !checked.contains(cIter.getClientDimension().getId()))
					{
						chain.addFirst(cIter.getClientDimension().getId());
						checked.add(cIter.getClientDimension().getId());
					} else
					{
						break;
					}
				}
				landChains.add(chain);
			}
		}
		
		return landChains;
	}
	
	void reloadLandChains()
	{
		landChains.invalidate();
		SkaianetInfoPacket packet = createLandChainPacket();
		MSPacketHandler.sendToAll(packet);
	}
	
	void updateAll()
	{
		checkData();
		for(PlayerIdentifier i : infoToSend.keySet())
			updatePlayer(i);
	}
	
	private void updatePlayer(PlayerIdentifier player)
	{
		PlayerIdentifier[] iden = infoToSend.get(player);
		ServerPlayerEntity playerMP = player.getPlayer(mcServer);
		if(iden == null || playerMP == null)//If the player disconnected
			return;
		for(SburbConnection c : connections)
			if(c.isActive() && (c.getClientIdentifier().equals(player) || c.getServerIdentifier().equals(player)))
			{
				MSCriteriaTriggers.SBURB_CONNECTION.trigger(playerMP);
				break;
			}
		for(PlayerIdentifier i : iden)
		{
			if(i != null)
			{
				SkaianetInfoPacket packet = generateClientInfoPacket(i);
				MSPacketHandler.sendToPlayer(packet, playerMP);
			}
		}
	}
	
	private SkaianetInfoPacket generateClientInfoPacket(PlayerIdentifier player)
	{
		boolean clientResuming = resumingClients.containsKey(player);
		boolean serverResuming = resumingServers.containsKey(player) || serversOpen.containsKey(player);
		
		Map<Integer, String> serverMap = sessionHandler.getServerList(player);
		
		List<SburbConnection> list = new ArrayList<>();
		for(SburbConnection c : connections)
			if(c.getClientIdentifier().equals(player) || c.getServerIdentifier().equals(player))
				list.add(c);
		
		return SkaianetInfoPacket.update(player.getId(), clientResuming, serverResuming, serverMap, list);
	}
	
	private void checkData()
	{
		if(!MinestuckConfig.skaianetCheck.get())
			return;
		
		Iterator<PlayerIdentifier> iter0 = infoToSend.keySet().iterator();
		while(iter0.hasNext())
			if(iter0.next().getPlayer(mcServer) == null)
			{
				//Debug.print("[SKAIANET] Player disconnected, removing data.");
				iter0.remove();
			}
		
		@SuppressWarnings("unchecked")
		Iterator<Entry<PlayerIdentifier, GlobalPos>>[] iter1 = new Iterator[]{serversOpen.entrySet().iterator(),resumingClients.entrySet().iterator(),resumingServers.entrySet().iterator()};
		
		for(Iterator<Entry<PlayerIdentifier, GlobalPos>> i : iter1)
			while(i.hasNext())
			{
				Entry<PlayerIdentifier, GlobalPos> data = i.next();
				ComputerTileEntity computer = getComputer(mcServer, data.getValue());
				if(computer == null || data.getValue().getDimension() == DimensionType.THE_NETHER || !computer.owner.equals(data.getKey())
						|| !(i == iter1[1] && computer.getData(0).getBoolean("isResuming")
								|| i != iter1[1] && computer.getData(1).getBoolean("isOpen")))
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
			if(c.isActive())
			{
				ComputerTileEntity cc = getComputer(mcServer, c.getClientComputer()), sc = getComputer(mcServer, c.getServerComputer());
				if(cc == null || sc == null || c.getClientComputer().getDimension() == DimensionType.THE_NETHER || c.getServerComputer().getDimension() == DimensionType.THE_NETHER || !c.getClientIdentifier().equals(cc.owner)
						|| !c.getServerIdentifier().equals(sc.owner) || !cc.getData(0).getBoolean("connectedToServer"))
				{
					Debug.warnf("[SKAIANET] Invalid computer in connection between %s and %s.", c.getClientIdentifier(), c.getServerIdentifier());
					if(!c.isMain())
						iter2.remove();
					else c.close();
					sessionHandler.onConnectionClosed(c, true);
					
					if(cc != null)
					{
						cc.getData(0).putBoolean("connectedToServer", false);
						cc.latestmessage.put(0, CLOSED);
						cc.markBlockForUpdate();
					} else if(sc != null)
					{
						sc.latestmessage.put(1, CLOSED);
						sc.markBlockForUpdate();
					}
					
					ConnectionCreatedEvent.ConnectionType type = !c.isMain() && getMainConnection(c.getClientIdentifier(), true) != null
							? ConnectionCreatedEvent.ConnectionType.SECONDARY : ConnectionCreatedEvent.ConnectionType.REGULAR;
					MinecraftForge.EVENT_BUS.post(new ConnectionClosedEvent(mcServer, c, sessionHandler.getPlayerSession(c.getClientIdentifier()), type));
				}
			}
		}
		
		if(MinestuckConfig.privateComputers.get())
		{
			for(Entry<PlayerIdentifier,PlayerIdentifier[]> entry : infoToSend.entrySet())
			{
				ServerPlayerEntity player = entry.getKey().getPlayer(mcServer);
				OpEntry opsEntry = player == null ? null : player.getServer().getPlayerList().getOppedPlayers().getEntry(player.getGameProfile());
				if(opsEntry != null && opsEntry.getPermissionLevel() >= 2)
					continue;
				
				for(int i = 0; i < entry.getValue().length; i++)
					if(entry.getValue()[i] != null && !entry.getValue()[i].equals(entry.getKey()))
						entry.getValue()[i] = null;
			}
		}
	}
	
	public SburbConnection getConnection(PlayerIdentifier client, PlayerIdentifier server)
	{
		for(SburbConnection c : connections)
			if(c.getClientIdentifier().equals(client) && c.getServerIdentifier().equals(server))
				return c;
		return null;
	}
	
	/**
	 * Gets the <code>TileEntityComputer</code> at the given position.
	 * @param server The server which this takes place.
	 * @param location A <code>Location</code> pointing to the computer.
	 * @return The <code>TileEntityComputer</code> at the given position,
	 * or <code>null</code> if there isn't one there.
	 */
	public static ComputerTileEntity getComputer(MinecraftServer server, GlobalPos location)
	{
		if(location == null)
			return null;
		World world = DimensionManager.getWorld(server, location.getDimension(), false, true);	//TODO look over code to limit force loading of dimensions
		if(world == null)
			return null;
		TileEntity te = world.getTileEntity(location.getPos());
		if(!(te instanceof ComputerTileEntity))
			return null;
		else return (ComputerTileEntity)te;
	}
	
	public SburbConnection getServerConnection(ComputerTileEntity computer)
	{
		return connections.stream().filter(c -> c.isServer(computer)).findAny().orElse(null);
	}
	
	/**
	 * Prepares the sburb connection and data needed for after entry.
	 * Should only be called by the cruxite artifact on trigger before teleportation
	 * @param target the identifier of the player that is entering
	 * @return The dimension type of the new land created, or null if the player can't enter at this time.
	 */
	public DimensionType prepareEntry(PlayerIdentifier target)
	{
		SburbConnection c = getMainConnection(target, true);
		if(c == null)
		{
			c = getActiveConnection(target);
			if(c == null)
			{
				Debug.infof("Player %s entered without connection. Creating connection... ", target.getUsername());
				c = new SburbConnection(target, this);
				c.setIsMain();
				try
				{
					sessionHandler.onConnectionCreated(c);
					SburbHandler.onFirstItemGiven(c);
					connections.add(c);
				} catch(MergeResult.SessionMergeException e)
				{
					if(sessionHandler.singleSession)
					{
						Debug.warnf("Failed to create connection: %s. Trying again with global session disabled for this world...", e.getMessage());
						sessionHandler.singleSession = false;
						sessionHandler.split();
						try
						{
							sessionHandler.onConnectionCreated(c);
							SburbHandler.onFirstItemGiven(c);
							connections.add(c);
						} catch(MergeResult.SessionMergeException f)
						{
							sessionHandler.singleSession = true;
							sessionHandler.mergeAll();
							Debug.errorf("Couldn't create a connection for %s: %s. Stopping entry.", target.getUsername(), f.getMessage());
							return null;
						}
					} else
					{
						Debug.errorf("Couldn't create a connection for %s: %s. Stopping entry.", target.getUsername(), e.getMessage());
						return null;
					}
				}
			}
			else giveItems(target);
		}
		else if(c.getClientDimension() != null)
			return c.getClientDimension();
		
		SburbHandler.enterMedium(mcServer, c);
		
		return c.getClientDimension();
	}
	
	void updateLandMaps(SburbConnection connection)
	{
		typeToInfoContainer.put(connection.getLandInfo().getDimensionName(), connection.getLandInfo());
		MSDimensionTypes.LANDS.dimToLandTypes.put(connection.getLandInfo().getDimensionName(), connection.getLandInfo().getLazyLandAspects());
	}
	
	public void onEntry(PlayerIdentifier target)
	{
		SburbConnection c = getMainConnection(target, true);
		if(c == null)
		{
			Debug.errorf("Finished entry without a player connection for %s. This should NOT happen!", target.getUsername());
			return;
		}
		
		SburbHandler.onGameEntered(mcServer, c);
		
		c.centerX = 0;
		c.centerZ = 0;
		c.useCoordinates = false;
		updateAll();
		reloadLandChains();
	}
	
	public void resetGivenItems()
	{
		for(SburbConnection c : connections)
		{
			c.resetGivenItems();
			
			EditData data = ServerEditHandler.getData(mcServer, c);
			if(data != null)
				data.sendGivenItemsToEditor();
		}
		DeployList.onConditionsUpdated(mcServer);
	}
	
	public void movingComputer(ComputerTileEntity oldTE, ComputerTileEntity newTE)
	{
		GlobalPos oldPos = GlobalPos.of(oldTE.getWorld().dimension.getType(), oldTE.getPos()), newPos = GlobalPos.of(newTE.getWorld().dimension.getType(), newTE.getPos());
		if(!oldTE.owner.equals(newTE.owner))
			throw new IllegalStateException("Moving computers with different owners! ("+oldTE.owner+" and "+newTE.owner+")");
		
		for(SburbConnection c : connections)
		{
			c.setActive(c.isClient(oldTE) ? newPos : c.getClientComputer(), c.isServer(oldTE) ? newPos : c.getServerComputer());
		}
		
		if(resumingClients.containsKey(oldTE.owner) && resumingClients.get(oldTE.owner).equals(oldPos))
			resumingClients.put(oldTE.owner, newPos);	//Used to be map.replace until someone had a NoSuchMethodError
		if(resumingServers.containsKey(oldTE.owner) && resumingServers.get(oldTE.owner).equals(oldPos))
			resumingServers.put(oldTE.owner, newPos);
		if(serversOpen.containsKey(oldTE.owner) && serversOpen.get(oldTE.owner).equals(oldPos))
			serversOpen.put(oldTE.owner, newPos);
		
		movingComputers.add(newPos);
	}
	
	public void clearMovingList()
	{
		movingComputers.clear();
	}
	
	public LandInfo landInfoForDimension(DimensionType type)
	{
		return typeToInfoContainer.get(DimensionType.getKey(type));
	}
	
	public static SkaianetHandler get(World world)
	{
		MinecraftServer server = world.getServer();
		if(server == null)
			throw new IllegalArgumentException("Can't get skaianet instance on client side! (Got null server from world)");
		return get(server);
	}
	
	public static SkaianetHandler get(MinecraftServer server)
	{
		Objects.requireNonNull(server);
		if(INSTANCE == null)
			INSTANCE = new SkaianetHandler();
		INSTANCE.mcServer = server;
		return INSTANCE;
	}
	
	/**
	 * Called when reading skaianet persistence data.
	 * Should only be called by minestuck itself (specifically {@link com.mraof.minestuck.MSWorldPersistenceHook}).
	 */
	public static void init(CompoundNBT nbt)
	{
		if(nbt != null)
		{
			INSTANCE = new SkaianetHandler();
			INSTANCE.read(nbt);
		}
	}
	
	public static CompoundNBT write()
	{
		if(INSTANCE == null)
			return null;
		else return INSTANCE.write(new CompoundNBT());
	}
	
	/**
	 * Clears data connected to skaianet. Should only be called on a ServerStopped event by minestuck itself.
	 */
	public static void clear()
	{
		INSTANCE = null;
		MSDimensionTypes.LANDS.dimToLandTypes.clear();
		SburbHandler.titleSelectionMap.clear();
	}
}