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
	
	public Optional<SburbConnection> getMainConnection(PlayerIdentifier player, boolean isClient)
	{
		if(player == null || player.equals(IdentifierHandler.NULL_IDENTIFIER))
			return Optional.empty();
		Stream<SburbConnection> connections = sessionHandler.getConnectionStream().filter(SburbConnection::isMain);
		if(isClient)
			return connections.filter(c -> c.getClientIdentifier().equals(player)).findAny();
		else return connections.filter(c -> c.getServerIdentifier().equals(player)).findAny();
	}
	
	public void connectToServer(ISburbComputer computer, PlayerIdentifier server)
	{
		PlayerIdentifier player = computer.getOwner();
		ComputerReference reference = computer.createReference();
		if(reference.isInNether() || isConnectingBlocked(player, true)
				|| !sessionHandler.getServerList(player).containsKey(server.getId()))
			return;
		
		ComputerReference serverReference = openedServers.remove(server);
		
		if(serverReference != null)
		{
			infoTracker.markDirty(server);
			ISburbComputer serverComputer = serverReference.getComputer(mcServer);
			if(serverComputer == null)
			{
				LOGGER.error("Tried to connect to {}, but the waiting computer was not found.",
						server.getUsername());
				checkAndUpdate();
				return;
			}
			
			Optional<SburbConnection> optional = getMainConnection(player, true);
			if(optional.isPresent())
			{
				SburbConnection connection = optional.get();
				if(connection.getServerIdentifier().equals(server))
				{
					connection.setActive(reference, serverReference);
					
					computer.connected(server, true);
					serverComputer.connected(player, false);
					
					infoTracker.markDirty(connection);
					
					MinecraftForge.EVENT_BUS.post(new ConnectionCreatedEvent(mcServer, connection, sessionHandler.getPlayerSession(player),
							ConnectionCreatedEvent.ConnectionType.RESUME, ConnectionCreatedEvent.SessionJoinType.INTERNAL));
				} else if(!connection.hasServerPlayer())
				{
					Session s1 = sessionHandler.getPlayerSession(player), s2 = sessionHandler.getPlayerSession(server);
					ConnectionCreatedEvent.SessionJoinType joinType = s1 == null || s2 == null ? ConnectionCreatedEvent.SessionJoinType.JOIN
							: s1 == s2 ? ConnectionCreatedEvent.SessionJoinType.INTERNAL : ConnectionCreatedEvent.SessionJoinType.MERGE;
					
					connection.setNewServerPlayer(server);
					try
					{
						sessionHandler.onConnectionCreated(connection);    //TODO the function does the checks we want, but is not too relevant. Make a more appropriate function call (or perhaps we get there through further restructuring)
						connection.setActive(reference, serverReference);
						
						computer.connected(server, true);
						serverComputer.connected(player, false);
						
						infoTracker.markDirty(connection);
						
						MinecraftForge.EVENT_BUS.post(new ConnectionCreatedEvent(mcServer, connection, sessionHandler.getPlayerSession(player),
								ConnectionCreatedEvent.ConnectionType.NEW_SERVER, joinType));
					} catch(MergeResult.SessionMergeException e)
					{
						LOGGER.warn("SessionHandler denied connection between {} and {}, reason: {}", player.getUsername(), server.getUsername(), e.getMessage());
						computer.putClientMessage(e.getResult().translationKey());
						connection.removeServerPlayer();
						openedServers.put(server, serverReference);
					}
				} else
				{
					SburbConnection newConnection = new SburbConnection(player, server, this);
					newConnection.setActive(reference, serverReference);
					newConnection.copyFrom(connection);
					
					try
					{
						sessionHandler.onConnectionCreated(newConnection);
						
						computer.connected(server, true);
						serverComputer.connected(player, false);
						
						infoTracker.markDirty(newConnection);
						
						MinecraftForge.EVENT_BUS.post(new ConnectionCreatedEvent(mcServer, newConnection, sessionHandler.getPlayerSession(player),
								ConnectionCreatedEvent.ConnectionType.SECONDARY, ConnectionCreatedEvent.SessionJoinType.INTERNAL));
					} catch(MergeResult.SessionMergeException e)
					{
						LOGGER.warn("SessionHandler denied connection between {} and {}, reason: {}", player.getUsername(), server.getUsername(), e.getMessage());
						computer.putClientMessage(e.getResult().translationKey());
						openedServers.put(server, serverReference);
					}
				}
			} else
			{
				//TODO session join type is better gotten from the session handler in connection to its check
				Session s1 = sessionHandler.getPlayerSession(player), s2 = sessionHandler.getPlayerSession(server);
				ConnectionCreatedEvent.SessionJoinType joinType = s1 == null || s2 == null ? ConnectionCreatedEvent.SessionJoinType.JOIN
						: s1 == s2 ? ConnectionCreatedEvent.SessionJoinType.INTERNAL : ConnectionCreatedEvent.SessionJoinType.MERGE;
				
				SburbConnection newConnection = new SburbConnection(player, server, this);
				newConnection.setActive(reference, serverReference);
				
				try
				{
					sessionHandler.onConnectionCreated(newConnection);
					SburbHandler.onConnectionCreated(newConnection);
					
					computer.connected(server, true);
					serverComputer.connected(player, false);
					
					infoTracker.markDirty(newConnection);
					
					MinecraftForge.EVENT_BUS.post(new ConnectionCreatedEvent(mcServer, newConnection, sessionHandler.getPlayerSession(player),
							ConnectionCreatedEvent.ConnectionType.REGULAR, joinType));
				} catch(MergeResult.SessionMergeException e)
				{
					LOGGER.warn("SessionHandler denied connection between {} and {}, reason: {}", player.getUsername(), server.getUsername(), e.getMessage());
					computer.putClientMessage(e.getResult().translationKey());
					openedServers.put(server, serverReference);
				}
			}
		}
		
		checkAndUpdate();
	}
	
	public void resumeConnection(ISburbComputer computer, boolean isClient)
	{
		PlayerIdentifier player = computer.getOwner();
		ComputerReference reference = computer.createReference();
		if(reference.isInNether() || isConnectingBlocked(player, isClient))
			return;
		Optional<SburbConnection> optional = getMainConnection(player, isClient);
		
		optional.ifPresent(connection -> {
			PlayerIdentifier otherPlayer = isClient ? connection.getServerIdentifier() : connection.getClientIdentifier();
			ComputerReference otherReference = getResumeMap(!isClient).remove(otherPlayer);
			
			if(isClient && otherReference == null)
				otherReference = openedServers.remove(otherPlayer);
			
			if(otherReference != null)
			{
				infoTracker.markDirty(otherPlayer);
				ISburbComputer otherComputer = otherReference.getComputer(mcServer);
				if(otherComputer == null)
				{
					LOGGER.error("Tried to resume connection, between {} and {}, but the waiting computer was not found.",
							connection.getClientIdentifier().getUsername(), connection.getServerIdentifier().getUsername());
					checkAndUpdate();
					return;
				}
				
				if(isClient)
					connection.setActive(reference, otherReference);
				else connection.setActive(otherReference, reference);
				
				computer.connected(otherPlayer, isClient);
				otherComputer.connected(player, !isClient);
				
				infoTracker.markDirty(connection);
				
				MinecraftForge.EVENT_BUS.post(new ConnectionCreatedEvent(mcServer, connection, sessionHandler.getPlayerSession(player),
						ConnectionCreatedEvent.ConnectionType.RESUME, ConnectionCreatedEvent.SessionJoinType.INTERNAL));
			} else
			{
				getResumeMap(isClient).put(player, reference);
				computer.setIsResuming(isClient);
				infoTracker.markDirty(player);
			}
			checkAndUpdate();
		});
	}
	
	public void openServer(ISburbComputer computer)
	{
		PlayerIdentifier player = computer.getOwner();
		ComputerReference reference = computer.createReference();
		if(reference.isInNether() || isConnectingBlocked(player, false))
			return;
		
		Optional<SburbConnection> optional = getMainConnection(player, false);
		if(optional.isPresent() && resumingClients.containsKey(optional.get().getClientIdentifier()))
		{
			SburbConnection connection = optional.get();
			ComputerReference clientReference = resumingClients.remove(connection.getClientIdentifier());
			infoTracker.markDirty(connection.getClientIdentifier());
			
			ISburbComputer clientComputer = clientReference.getComputer(mcServer);
			if(clientComputer == null)
			{
				LOGGER.error("Tried to resume connection, between {} and {}, but the waiting computer was not found.",
						connection.getClientIdentifier().getUsername(), player.getUsername());
				checkAndUpdate();
				return;
			}
			
			connection.setActive(clientReference, reference);
			
			computer.connected(connection.getClientIdentifier(), false);
			clientComputer.connected(player, true);
			
			infoTracker.markDirty(connection);
			
			MinecraftForge.EVENT_BUS.post(new ConnectionCreatedEvent(mcServer, connection, sessionHandler.getPlayerSession(player),
					ConnectionCreatedEvent.ConnectionType.RESUME, ConnectionCreatedEvent.SessionJoinType.INTERNAL));
		} else
		{
			computer.putServerBoolean("isOpen", true);
			openedServers.put(player, reference);
			
			infoTracker.markDirty(player);
		}
		checkAndUpdate();
	}
	
	private boolean isConnectingBlocked(PlayerIdentifier player, boolean isClient)
	{
		if(isClient)
			return getActiveConnection(player) != null || resumingClients.containsKey(player);
		else return openedServers.containsKey(player) || resumingServers.containsKey(player);
	}
	
	private Map<PlayerIdentifier, ComputerReference> getResumeMap(boolean isClient)
	{
		return isClient ? resumingClients : resumingServers;
	}
	
	public void closeClientConnectionRemotely(PlayerIdentifier player)
	{
		if(resumingClients.containsKey(player))
		{
			ISburbComputer computer = resumingClients.remove(player).getComputer(mcServer);
			infoTracker.markDirty(player);
			if(computer != null)
			{
				computer.putClientBoolean("isResuming", false);
				computer.putClientMessage(STOP_RESUME);
			}
			checkAndUpdate();
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
			infoTracker.markDirty(owner);
			computer.putClientBoolean("isResuming", false);
			computer.putClientMessage(STOP_RESUME);
			checkAndUpdate();
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
			infoTracker.markDirty(owner);
		}
	}
	
	void closeConnection(SburbConnection connection)
	{
		closeConnection(connection, connection.getClientComputer().getComputer(mcServer),
				connection.getServerComputer().getComputer(mcServer));
	}
	
	private void closeConnection(SburbConnection connection, ISburbComputer clientComputer, ISburbComputer serverComputer)
	{
		sessionHandler.onConnectionClosed(connection, true);
		
		connection.close();
		infoTracker.markDirty(connection);
		
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
		
		ConnectionCreatedEvent.ConnectionType type = !connection.isMain() && getMainConnection(connection.getClientIdentifier(), true).isPresent()
				? ConnectionCreatedEvent.ConnectionType.SECONDARY : ConnectionCreatedEvent.ConnectionType.REGULAR;
		MinecraftForge.EVENT_BUS.post(new ConnectionClosedEvent(mcServer, connection, sessionHandler.getPlayerSession(connection.getClientIdentifier()), type));
		
		checkAndUpdate();
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
	
	void checkAndUpdate()
	{
		checkData();
		infoTracker.checkAndSend();
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
					closeConnection(c, cc, sc);
				}
			}
		});
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
		
		checkAndUpdate();
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