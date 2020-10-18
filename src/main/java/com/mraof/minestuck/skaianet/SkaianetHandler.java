package com.mraof.minestuck.skaianet;

import com.mraof.minestuck.MinestuckConfig;
import com.mraof.minestuck.computer.ComputerReference;
import com.mraof.minestuck.computer.ISburbComputer;
import com.mraof.minestuck.event.ConnectionClosedEvent;
import com.mraof.minestuck.event.ConnectionCreatedEvent;
import com.mraof.minestuck.event.SburbEvent;
import com.mraof.minestuck.player.IdentifierHandler;
import com.mraof.minestuck.player.PlayerIdentifier;
import com.mraof.minestuck.tileentity.ComputerTileEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.World;
import net.minecraft.world.dimension.DimensionType;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.util.Constants;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.annotation.Nullable;
import java.util.*;
import java.util.Map.Entry;
import java.util.stream.Stream;

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
	final SessionHandler sessionHandler;
	final InfoTracker infoTracker = new InfoTracker(this);
	
	MinecraftServer mcServer;
	
	private SkaianetHandler()
	{
		sessionHandler = new DefaultSessionHandler(this);
	}
	
	private SkaianetHandler(CompoundNBT nbt)
	{
		SessionHandler sessions;
		if(nbt.contains("session", Constants.NBT.TAG_COMPOUND))
			sessions = new GlobalSessionHandler(this, nbt.getCompound("session"));
		else sessions = new DefaultSessionHandler(this, nbt.getList("sessions", Constants.NBT.TAG_COMPOUND));
		
		sessionHandler = sessions.getActual();
		
		readPlayerComputerList(nbt, openedServers, "serversOpen");
		readPlayerComputerList(nbt, resumingClients, "resumingClients");
		readPlayerComputerList(nbt, resumingServers, "resumingServers");
	}
	
	/**
	 * @param client The client player to search for
	 * @return The active connection with the client as its client player, or null if no such connection was found.
	 */
	public SburbConnection getActiveConnection(PlayerIdentifier client)
	{
		return sessionHandler.getConnectionStream().filter(SburbConnection::isActive).filter(c -> c.getClientIdentifier().equals(client))
				.findAny().orElse(null);
	}
	
	@Nullable
	public PlayerIdentifier getAssociatedPartner(PlayerIdentifier player, boolean isClient)
	{
		Optional<SburbConnection> c = getMainConnection(player, isClient);
		if(isClient)
			return c.filter(SburbConnection::hasServerPlayer)
					.map(SburbConnection::getServerIdentifier).orElse(null);
		else return c.map(SburbConnection::getClientIdentifier).orElse(null);
	}
	
	public Optional<SburbConnection> getMainConnection(PlayerIdentifier player, boolean isClient)
	{
		if(player == null || player.equals(IdentifierHandler.NULL_IDENTIFIER))
			return Optional.empty();
		Stream<SburbConnection> connections = sessionHandler.getConnectionStream().filter(SburbConnection::isMain);
		if(isClient)
			return connections.filter(c -> c.getClientIdentifier().equals(player)).findAny();
		else return connections.filter(c -> c.getServerIdentifier().equals(player)).findAny();
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
	
	public void closeClientConnectionRemotely(PlayerIdentifier player)
	{
		if(resumingClients.containsKey(player))
		{
			ISburbComputer computer = resumingClients.remove(player).getComputer(mcServer);
			if(computer != null)
			{
				computer.putClientBoolean("isResuming", false);
				computer.putClientMessage(STOP_RESUME);
				updateAll();
			}
		} else
		{
			SburbConnection activeConnection = getActiveConnection(player);
			if(activeConnection != null)
				closeConnection(activeConnection);
		}
	}
	
	public void closeClientConnection(ISburbComputer computer)
	{
		PlayerIdentifier owner = computer.getOwner();
		if(resumingClients.containsKey(owner) && resumingClients.get(owner).matches(computer))
		{
			resumingClients.remove(owner);
			computer.putClientBoolean("isResuming", false);
			computer.putClientMessage(STOP_RESUME);
			updateAll();
		} else
		{
			SburbConnection activeConnection = getActiveConnection(owner);
			if(activeConnection != null && activeConnection.isClient(computer))
			{
				closeConnection(activeConnection, computer, activeConnection.getServerComputer().getComputer(mcServer));
			}
		}
	}
	
	public void closeServerConnection(ISburbComputer computer)
	{
		checkAndCloseFromServerList(computer, openedServers);
		checkAndCloseFromServerList(computer, resumingServers);
		
		SburbConnection connection = getServerConnection(computer);
		
		if(connection != null)
		{
			closeConnection(connection, connection.getClientComputer().getComputer(mcServer), computer);
		}
	}
	
	private void checkAndCloseFromServerList(ISburbComputer computer, Map<PlayerIdentifier, ComputerReference> map)
	{
		PlayerIdentifier owner = computer.getOwner();
		if(map.containsKey(owner) && map.get(owner).matches(computer))
		{
			map.remove(owner);
			computer.putServerBoolean("isOpen", false);
			computer.putServerMessage(STOP_RESUME);
			updateAll();
		}
	}
	
	void closeConnection(SburbConnection connection)
	{
		closeConnection(connection, connection.getClientComputer().getComputer(mcServer),
				connection.getServerComputer().getComputer(mcServer));
	}
	
	private void closeConnection(SburbConnection connection, ISburbComputer clientComputer, ISburbComputer serverComputer)
	{
		if(clientComputer != null)
		{
			clientComputer.putClientBoolean("connectedToServer", false);
			clientComputer.putClientMessage(CLOSED);
		}
		if(serverComputer != null)
		{
			serverComputer.clearConnectedClient();
			serverComputer.putServerMessage(CLOSED);
		}
		
		sessionHandler.onConnectionClosed(connection, true);
		
		connection.close();
		
		ConnectionCreatedEvent.ConnectionType type = !connection.isMain() && getMainConnection(connection.getClientIdentifier(), true).isPresent()
				? ConnectionCreatedEvent.ConnectionType.SECONDARY : ConnectionCreatedEvent.ConnectionType.REGULAR;
		MinecraftForge.EVENT_BUS.post(new ConnectionClosedEvent(mcServer, connection, sessionHandler.getPlayerSession(connection.getClientIdentifier()), type));
		
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
			Optional<SburbConnection> conn = getMainConnection(c.getClientIdentifier(), true);
			if(conn.isPresent() && !conn.get().hasServerPlayer() && !getMainConnection(c.getServerIdentifier(), false).isPresent())
			{
				conn.get().setNewServerPlayer(c.getServerIdentifier());
				conn.get().setActive(c.getClientComputer(), c.getServerComputer());
				c = conn.get();
				type = ConnectionCreatedEvent.ConnectionType.RESUME;
				updateLandChain = true;
			} else
			{
				try
				{
					sessionHandler.onConnectionCreated(c);
				} catch(MergeResult.SessionMergeException e)
				{
					LOGGER.warn("SessionHandler denied connection between {} and {}, reason: {}", c.getClientIdentifier().getUsername(), c.getServerIdentifier().getUsername(), e.getMessage());
					ISburbComputer cComp = c.getClientComputer().getComputer(mcServer);
					if(cComp != null)
						cComp.putClientMessage(e.getResult().translationKey());
					map.put(c.getServerIdentifier(), c.getServerComputer());
					return;
				
				}
				SburbHandler.onConnectionCreated(c);
				
				if(conn.isPresent())
				{
					c.copyFrom(conn.get());
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
	
	public void requestInfo(ServerPlayerEntity player, PlayerIdentifier p1)
	{
		checkData();
		infoTracker.requestInfo(player, p1);
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
		
		compound.put("serversOpen", saveComputerMap(openedServers));
		compound.put("resumingClients", saveComputerMap(resumingClients));
		compound.put("resumingServers", saveComputerMap(resumingServers));
		
		return compound;
	}
	
	private ListNBT saveComputerMap(Map<PlayerIdentifier, ComputerReference> map)
	{
		ListNBT list = new ListNBT();
		for(Entry<PlayerIdentifier, ComputerReference> entry : map.entrySet())
		{
			CompoundNBT nbt = new CompoundNBT();
			nbt.put("computer", entry.getValue().write(new CompoundNBT()));
			entry.getKey().saveToNBT(nbt, "player");
			list.add(nbt);
		}
		return list;
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
		
		validateComputerMap(openedServers, false);
		validateComputerMap(resumingClients, true);
		validateComputerMap(resumingServers, false);
		
		sessionHandler.getConnectionStream().forEach(c -> {
			if(c.isActive())
			{
				ISburbComputer cc = c.getClientComputer().getComputer(mcServer), sc = c.getServerComputer().getComputer(mcServer);
				if(cc == null || sc == null || c.getClientComputer().isInNether() || c.getServerComputer().isInNether() || !c.getClientIdentifier().equals(cc.getOwner())
						|| !c.getServerIdentifier().equals(sc.getOwner()) || !cc.getClientBoolean("connectedToServer"))
				{
					LOGGER.warn("[SKAIANET] Invalid computer in connection between {} and {}.", c.getClientIdentifier(), c.getServerIdentifier());
					c.close();
					sessionHandler.onConnectionClosed(c, true);
					
					if(cc != null)
					{
						cc.putClientBoolean("connectedToServer", false);
						cc.putClientMessage(CLOSED);
					} else if(sc != null)
					{
						sc.putServerMessage(CLOSED);
					}
					
					ConnectionCreatedEvent.ConnectionType type = !c.isMain() && getMainConnection(c.getClientIdentifier(), true).isPresent()
							? ConnectionCreatedEvent.ConnectionType.SECONDARY : ConnectionCreatedEvent.ConnectionType.REGULAR;
					MinecraftForge.EVENT_BUS.post(new ConnectionClosedEvent(mcServer, c, sessionHandler.getPlayerSession(c.getClientIdentifier()), type));
				}
			}
		});
		
		infoTracker.checkData();
	}
	
	private void validateComputerMap(Map<PlayerIdentifier, ComputerReference> map, boolean clientPlayerMap)
	{
		Iterator<Entry<PlayerIdentifier, ComputerReference>> i = map.entrySet().iterator();
		while(i.hasNext())
		{
			Entry<PlayerIdentifier, ComputerReference> data = i.next();
			ISburbComputer computer = data.getValue().getComputer(mcServer);
			if(computer == null || data.getValue().isInNether() || !computer.getOwner().equals(data.getKey())
					|| !(clientPlayerMap && computer.getClientBoolean("isResuming")
					|| !clientPlayerMap && computer.getServerBoolean("isOpen")))
			{
				LOGGER.warn("[SKAIANET] Invalid computer in waiting list!");
				i.remove();
			}
		}
	}
	
	public SburbConnection getConnection(PlayerIdentifier client, PlayerIdentifier server)
	{
		return sessionHandler.getConnectionStream().filter(c -> c.getClientIdentifier().equals(client) && c.getServerIdentifier().equals(server))
				.findAny().orElse(null);
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
		return sessionHandler.getConnectionStream().filter(c -> c.isServer(computer)).findAny().orElse(null);
	}
	
	/**
	 * Prepares the sburb connection and data needed for after entry.
	 * Should only be called by the cruxite artifact on trigger before teleportation
	 * @param target the identifier of the player that is entering
	 * @return The dimension type of the new land created, or null if the player can't enter at this time.
	 */
	public DimensionType prepareEntry(PlayerIdentifier target)
	{
		SburbConnection c = getMainConnection(target, true).orElse(null);
		if(c == null)
		{
			c = getActiveConnection(target);
			if(c == null)
			{
				LOGGER.info("Player {} entered without connection. Creating connection... ", target.getUsername());
				c = new SburbConnection(target, this);
				c.setIsMain();
				try
				{
					sessionHandler.onConnectionCreated(c);
					SburbHandler.onFirstItemGiven(c);
				} catch(MergeResult.SessionMergeException e)
				{
					LOGGER.error("Couldn't create a connection for {}: {}. Stopping entry.", target.getUsername(), e.getMessage());
					return null;
				}
			}
			else SburbHandler.giveItems(mcServer, target);
		}
		else if(c.getClientDimension() != null)
			return c.getClientDimension();
		
		SburbHandler.prepareEntry(mcServer, c);
		
		return c.getClientDimension();
	}
	
	/**
	 * Called when entry teleportation has successfully finished.
	 */
	public void onEntry(PlayerIdentifier target)
	{
		Optional<SburbConnection> c = getMainConnection(target, true);
		if(!c.isPresent())
		{
			LOGGER.error("Finished entry without a player connection for {}. This should NOT happen!", target.getUsername());
			return;
		}
		
		SburbHandler.onEntry(mcServer, c.get());
		
		updateAll();
		infoTracker.reloadLandChains();
		
		MinecraftForge.EVENT_BUS.post(new SburbEvent.OnEntry(mcServer, c.get(), sessionHandler.getPlayerSession(target)));
	}
	
	public void movingComputer(ComputerTileEntity oldTE, ComputerTileEntity newTE)
	{
		ComputerReference oldRef = ComputerReference.of(oldTE), newRef = ComputerReference.of(newTE);
		if(!oldTE.owner.equals(newTE.owner))
			throw new IllegalStateException("Moving computers with different owners! ("+oldTE.owner+" and "+newTE.owner+")");
		
		sessionHandler.getConnectionStream().forEach(c -> c.updateComputer(oldTE, newRef));
		
		resumingClients.replace(oldTE.owner, oldRef, newRef);
		resumingServers.replace(oldTE.owner, oldRef, newRef);
		openedServers.replace(oldTE.owner, oldRef, newRef);
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
				INSTANCE = new SkaianetHandler(nbt);
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
		TitleSelectionHook.playersInTitleSelection.clear();
	}
}