package com.mraof.minestuck.skaianet;

import com.mraof.minestuck.MinestuckConfig;
import com.mraof.minestuck.computer.ComputerReference;
import com.mraof.minestuck.computer.ISburbComputer;
import com.mraof.minestuck.computer.editmode.DeployList;
import com.mraof.minestuck.computer.editmode.EditData;
import com.mraof.minestuck.computer.editmode.ServerEditHandler;
import com.mraof.minestuck.event.ConnectionClosedEvent;
import com.mraof.minestuck.event.ConnectionCreatedEvent;
import com.mraof.minestuck.event.SburbEvent;
import com.mraof.minestuck.player.IdentifierHandler;
import com.mraof.minestuck.player.PlayerIdentifier;
import com.mraof.minestuck.tileentity.ComputerTileEntity;
import com.mraof.minestuck.util.Debug;
import com.mraof.minestuck.world.MSDimensionTypes;
import com.mraof.minestuck.world.lands.LandInfo;
import com.mraof.minestuck.world.lands.LandTypePair;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraft.world.dimension.DimensionType;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.util.Constants;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.annotation.Nullable;
import java.util.*;
import java.util.Map.Entry;

/**
 * This class handles server sided stuff about the sburb connection network.
 * This class also handles the main saving and loading.
 * @author kirderf1
 */
public final class SkaianetHandler
{
	private static final Logger LOGGER = LogManager.getLogger();
	
	public static final String PRIVATE_COMPUTER = "minestuck.private_computer";
	public static final String CLOSED_SERVER = "minestuck.closed_server_message";
	public static final String STOP_RESUME = "minestuck.stop_resume_message";
	public static final String CLOSED = "minestuck.closed_message";
	
	private static SkaianetHandler INSTANCE;
	
	final Map<PlayerIdentifier, ComputerReference> openedServers = new HashMap<>();
	private final Map<PlayerIdentifier, ComputerReference> resumingClients = new HashMap<>();
	private final Map<PlayerIdentifier, ComputerReference> resumingServers = new HashMap<>();
	List<SburbConnection> connections = new ArrayList<>();
	private final List<ComputerReference> movingComputers = new ArrayList<>();
	final SessionHandler sessionHandler = new SessionHandler(this);
	final InfoTracker infoTracker = new InfoTracker(this);
	/**
	 * Changes to this map must also be done to {@link MSDimensionTypes#LANDS#dimToLandAspects}
	 */
	private final Map<ResourceLocation, LandInfo> typeToInfoContainer = new HashMap<>();
	
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
	
	@Nullable
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
			infoTracker.sendConnectionInfo(c.getClientIdentifier());
			infoTracker.sendConnectionInfo(c.getServerIdentifier());
			return true;
		}
		return false;
	}
	
	public void requestConnection(PlayerIdentifier player, ComputerReference compRef, PlayerIdentifier otherPlayer, boolean connectingAsClient)
	{
		if(compRef.isInNether())
			return;
		ISburbComputer computer = compRef.getComputer(mcServer);
		if(computer == null)
			return;
		
		boolean success;
		if(!connectingAsClient)	//Is server
			success = handleConnectByServer(player, compRef, computer, otherPlayer);
		else success = handleConnectByClient(player, compRef, computer, otherPlayer);
		
		if(success)
		{
			updateAll();
		}
	}
	
	private boolean handleConnectByClient(PlayerIdentifier player, ComputerReference compRef, ISburbComputer computer, PlayerIdentifier otherPlayer)
	{
		if(getActiveConnection(player) != null || resumingClients.containsKey(player))
			return false;
		PlayerIdentifier p = getAssociatedPartner(player, true);
		if(p != null && (otherPlayer == null || p.equals(otherPlayer)))	//If trying to connect to the associated partner
		{
			if(resumingServers.containsKey(p))	//If server is "resuming".
				connectTo(player, compRef, true, p, resumingServers);
			else if(openedServers.containsKey(p))	//If server is normally open.
				connectTo(player, compRef, true, p, openedServers);
			else	//If server isn't open
			{
				computer.putClientBoolean("isResuming", true);
				resumingClients.put(player, compRef);
			}
		}
		else if(openedServers.containsKey(otherPlayer))	//If the server is open.
			connectTo(player, compRef, true, otherPlayer, openedServers);
		else return false;
		
		return true;
	}
	
	private boolean handleConnectByServer(PlayerIdentifier player, ComputerReference compRef, ISburbComputer computer, PlayerIdentifier otherPlayer)
	{
		if(openedServers.containsKey(player) || resumingServers.containsKey(player))
			return false;
		if(otherPlayer == null)	//Wants to open
		{
			if(resumingClients.containsKey(getAssociatedPartner(player, false)))
				connectTo(player, compRef, false, getAssociatedPartner(player, false), resumingClients);
			else
			{
				computer.putServerBoolean("isOpen", true);
				openedServers.put(player, compRef);
			}
		}
		else if(otherPlayer.equals(getAssociatedPartner(player, false)))	//Wants to resume
		{
			if(resumingClients.containsKey(otherPlayer))	//The client is already waiting
				connectTo(player, compRef, false, otherPlayer, resumingClients);
			else	//Client is not currently trying to resume
			{
				computer.putServerBoolean("isOpen", true);
				resumingServers.put(player, compRef);
			}
		}
		else return false;
		
		return true;
	}
	
	public void closeConnection(PlayerIdentifier player, PlayerIdentifier otherPlayer, boolean isClient)
	{
		if(otherPlayer == null)
		{
			if(isClient)
			{
				if(movingComputers.contains(resumingClients.get(player)))
					return;
				ISburbComputer computer = resumingClients.remove(player).getComputer(mcServer);
				if(computer != null)
				{
					computer.putClientBoolean("isResuming", false);
					computer.putClientMessage(STOP_RESUME);
				}
			} else if(openedServers.containsKey(player))
			{
				if(movingComputers.contains(openedServers.get(player)))
					return;
				ISburbComputer computer = openedServers.remove(player).getComputer(mcServer);
				if(computer != null)
				{
					computer.putServerBoolean("isOpen", false);
					computer.putServerMessage(CLOSED_SERVER);
				}
			} else if(resumingServers.containsKey(player))
			{
				if(movingComputers.contains(resumingServers.get(player)))
					return;
				ISburbComputer computer = resumingServers.remove(player).getComputer(mcServer);
				if(computer != null)
				{
					computer.putServerBoolean("isOpen", false);
					computer.putServerMessage(STOP_RESUME);
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
					ISburbComputer cc = c.getClientComputer().getComputer(mcServer), sc = c.getServerComputer().getComputer(mcServer);
					if(cc != null)
					{
						cc.putClientBoolean("connectedToServer", false);
						cc.putClientMessage(CLOSED);
					}
					if(sc != null)
					{
						sc.clearConnectedClient();
						sc.putServerMessage(CLOSED);
					}
					sessionHandler.onConnectionClosed(c, true);
					
					if(c.isMain())
						c.close();
					else connections.remove(c);
					
					ConnectionCreatedEvent.ConnectionType type = !c.isMain() && getMainConnection(c.getClientIdentifier(), true) != null
							? ConnectionCreatedEvent.ConnectionType.SECONDARY : ConnectionCreatedEvent.ConnectionType.REGULAR;
					MinecraftForge.EVENT_BUS.post(new ConnectionClosedEvent(mcServer, c, sessionHandler.getPlayerSession(c.getClientIdentifier()), type));
				} else if(otherPlayer.equals(getAssociatedPartner(player, isClient)))
				{
					if(movingComputers.contains(isClient?resumingClients.get(player):resumingServers.get(player)))
						return;
					ISburbComputer computer = (isClient?resumingClients:resumingServers).remove(player).getComputer(mcServer);
					if(computer != null)
					{
						if(isClient)
							computer.putClientMessage(STOP_RESUME);
						else computer.putServerMessage(STOP_RESUME);
					}
				}
			}
		}
		updateAll();
	}
	
	private void connectTo(PlayerIdentifier player, ComputerReference computer, boolean isClient, PlayerIdentifier otherPlayer, Map<PlayerIdentifier, ComputerReference> map)
	{
		ISburbComputer c1 = computer.getComputer(mcServer), c2 = map.get(otherPlayer).getComputer(mcServer);
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
					ISburbComputer cComp = c.getClientComputer().getComputer(mcServer);
					if(cComp != null)
						cComp.putClientMessage(e.getResult().translationKey());
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
		
		MinecraftForge.EVENT_BUS.post(new ConnectionCreatedEvent(mcServer, c, sessionHandler.getPlayerSession(c.getClientIdentifier()), type, joinType));
		if(updateLandChain)
			infoTracker.reloadLandChains();
	}
	
	SburbConnection makeConnectionWithLand(LandTypePair landTypes, DimensionType dimensionName, PlayerIdentifier client, PlayerIdentifier server, Session session)
	{
		SburbConnection c = new SburbConnection(client, server, this);
		c.setIsMain();
		c.setLand(landTypes, dimensionName);
		c.setHasEntered();
		
		session.connections.add(c);
		connections.add(c);
		SburbHandler.onConnectionCreated(c);
		
		return c;
	}
	
	public void requestInfo(ServerPlayerEntity player, PlayerIdentifier p1)
	{
		checkData();
		infoTracker.requestInfo(player, p1);
	}
	
	private void read(CompoundNBT nbt)
	{
		sessionHandler.read(nbt);
		
		readPlayerComputerList(nbt, openedServers, "serversOpen");
		readPlayerComputerList(nbt, resumingClients, "resumingClients");
		readPlayerComputerList(nbt, resumingServers, "resumingServers");
		
		sessionHandler.onLoad();
	}
	
	private void readPlayerComputerList(CompoundNBT nbt, Map<PlayerIdentifier, ComputerReference> map, String key)
	{
		ListNBT list = nbt.getList(key, Constants.NBT.TAG_COMPOUND);
		for(int i = 0; i < list.size(); i++)
		{
			CompoundNBT cmp = list.getCompound(i);
			map.put(IdentifierHandler.load(cmp, "player"), ComputerReference.read(cmp.getCompound("computer")));
		}
	}
	
	private CompoundNBT write(CompoundNBT compound)
	{
		//checkData();
		
		sessionHandler.write(compound);
		
		String[] s = {"serversOpen","resumingClients","resumingServers"};
		@SuppressWarnings("unchecked")
		Map<PlayerIdentifier, ComputerReference>[] maps = new Map[]{openedServers, resumingClients, resumingServers};
		for(int i = 0; i < 3; i++)
		{
			ListNBT list = new ListNBT();
			for(Entry<PlayerIdentifier, ComputerReference> entry : maps[i].entrySet())
			{
				CompoundNBT nbt = new CompoundNBT();
				nbt.put("computer", entry.getValue().write(new CompoundNBT()));
				entry.getKey().saveToNBT(nbt, "player");
				list.add(nbt);
			}
			compound.put(s[i], list);
		}
		
		return compound;
	}
	
	void updateAll()
	{
		checkData();
		infoTracker.sendInfoToAll();
	}
	
	private void checkData()
	{
		if(!MinestuckConfig.SERVER.skaianetCheck.get())
			return;
		
		@SuppressWarnings("unchecked")	//TODO Use function calls instead of an array
		Iterator<Entry<PlayerIdentifier, ComputerReference>>[] iter1 = new Iterator[]{openedServers.entrySet().iterator(),resumingClients.entrySet().iterator(),resumingServers.entrySet().iterator()};
		
		for(Iterator<Entry<PlayerIdentifier, ComputerReference>> i : iter1)
			while(i.hasNext())
			{
				Entry<PlayerIdentifier, ComputerReference> data = i.next();
				ISburbComputer computer = data.getValue().getComputer(mcServer);
				if(computer == null || data.getValue().isInNether() || !computer.getOwner().equals(data.getKey())
						|| !(i == iter1[1] && computer.getClientBoolean("isResuming")
								|| i != iter1[1] && computer.getServerBoolean("isOpen")))
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
				ISburbComputer cc = c.getClientComputer().getComputer(mcServer), sc = c.getServerComputer().getComputer(mcServer);
				if(cc == null || sc == null || c.getClientComputer().isInNether() || c.getServerComputer().isInNether() || !c.getClientIdentifier().equals(cc.getOwner())
						|| !c.getServerIdentifier().equals(sc.getOwner()) || !cc.getClientBoolean("connectedToServer"))
				{
					Debug.warnf("[SKAIANET] Invalid computer in connection between %s and %s.", c.getClientIdentifier(), c.getServerIdentifier());
					if(!c.isMain())
						iter2.remove();
					else c.close();
					sessionHandler.onConnectionClosed(c, true);
					
					if(cc != null)
					{
						cc.putClientBoolean("connectedToServer", false);
						cc.putClientMessage(CLOSED);
					} else if(sc != null)
					{
						sc.putServerMessage(CLOSED);
					}
					
					ConnectionCreatedEvent.ConnectionType type = !c.isMain() && getMainConnection(c.getClientIdentifier(), true) != null
							? ConnectionCreatedEvent.ConnectionType.SECONDARY : ConnectionCreatedEvent.ConnectionType.REGULAR;
					MinecraftForge.EVENT_BUS.post(new ConnectionClosedEvent(mcServer, c, sessionHandler.getPlayerSession(c.getClientIdentifier()), type));
				}
			}
		}
		
		infoTracker.checkData();
	}
	
	public SburbConnection getConnection(PlayerIdentifier client, PlayerIdentifier server)
	{
		for(SburbConnection c : connections)
			if(c.getClientIdentifier().equals(client) && c.getServerIdentifier().equals(server))
				return c;
		return null;
	}
	
	boolean hasResumingClient(PlayerIdentifier identifier)
	{
		return resumingClients.containsKey(identifier);
	}
	
	boolean hasResumingServer(PlayerIdentifier identifier)
	{
		return resumingServers.containsKey(identifier) || openedServers.containsKey(identifier);
	}
	
	public SburbConnection getServerConnection(ISburbComputer computer)
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
						sessionHandler.splitGlobalSession();
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
		
		SburbHandler.prepareEntry(mcServer, c);
		
		return c.getClientDimension();
	}
	
	void updateLandMaps(SburbConnection connection)
	{
		typeToInfoContainer.put(connection.getLandInfo().getDimensionName(), connection.getLandInfo());
		MSDimensionTypes.LANDS.dimToLandTypes.put(connection.getLandInfo().getDimensionName(), connection.getLandInfo().getLazyLandAspects());
	}
	
	/**
	 * Called when entry teleportation has successfully finished.
	 */
	public void onEntry(PlayerIdentifier target)
	{
		SburbConnection c = getMainConnection(target, true);
		if(c == null)
		{
			Debug.errorf("Finished entry without a player connection for %s. This should NOT happen!", target.getUsername());
			return;
		}
		
		SburbHandler.onEntry(mcServer, c);
		
		updateAll();
		infoTracker.reloadLandChains();
		
		MinecraftForge.EVENT_BUS.post(new SburbEvent.OnEntry(mcServer, c, sessionHandler.getPlayerSession(target)));
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
		ComputerReference oldRef = ComputerReference.of(oldTE), newRef = ComputerReference.of(newTE);
		if(!oldTE.owner.equals(newTE.owner))
			throw new IllegalStateException("Moving computers with different owners! ("+oldTE.owner+" and "+newTE.owner+")");
		
		for(SburbConnection c : connections)
		{
			if(c.isActive())
			{
				boolean isClient = c.isClient(oldTE), isServer = c.isServer(oldTE);
				if(isClient || isServer)	//TODO Change to an "update positions" function in the sburb connection, and add checks to isActive in setActive()
					c.setActive(isClient ? newRef : c.getClientComputer(), isServer ? newRef : c.getServerComputer());
			}
		}
		
		if(resumingClients.containsKey(oldTE.owner) && resumingClients.get(oldTE.owner).equals(oldRef))
			resumingClients.put(oldTE.owner, newRef);	//Used to be map.replace until someone had a NoSuchMethodError
		if(resumingServers.containsKey(oldTE.owner) && resumingServers.get(oldTE.owner).equals(oldRef))
			resumingServers.put(oldTE.owner, newRef);
		if(openedServers.containsKey(oldTE.owner) && openedServers.get(oldTE.owner).equals(oldRef))
			openedServers.put(oldTE.owner, newRef);
		
		movingComputers.add(newRef);
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
			try
			{
				INSTANCE = new SkaianetHandler();
				INSTANCE.read(nbt);
			} catch(Exception e)
			{
				LOGGER.error("Caught unhandled exception while loading Skaianet:", e);
			}
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
		SburbHandler.playersInTitleSelection.clear();
	}
}