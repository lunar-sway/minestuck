package com.mraof.minestuck.skaianet;

import com.mraof.minestuck.Minestuck;
import com.mraof.minestuck.MinestuckConfig;
import com.mraof.minestuck.blockentity.ComputerBlockEntity;
import com.mraof.minestuck.computer.ComputerReference;
import com.mraof.minestuck.computer.ISburbComputer;
import com.mraof.minestuck.event.OnEntryEvent;
import com.mraof.minestuck.event.SburbEvent;
import com.mraof.minestuck.player.IdentifierHandler;
import com.mraof.minestuck.player.PlayerIdentifier;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.saveddata.SavedData;
import net.minecraft.world.level.storage.DimensionDataStorage;
import net.minecraftforge.common.MinecraftForge;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.annotation.Nullable;
import java.util.*;
import java.util.stream.Stream;

/**
 * This class handles server sided stuff about the sburb connection network.
 * This class also handles the main saving and loading.
 * @author kirderf1
 */
public final class SkaianetHandler extends SavedData
{
	private static final Logger LOGGER = LogManager.getLogger();
	
	public static final String PRIVATE_COMPUTER = "minestuck.private_computer";
	public static final String CLOSED_SERVER = "minestuck.closed_server_message";
	public static final String STOP_RESUME = "minestuck.stop_resume_message";
	public static final String CLOSED = "minestuck.closed_message";
	
	final InfoTracker infoTracker = new InfoTracker(this);
	final ComputerWaitingList openedServers = new ComputerWaitingList(infoTracker, false, "opened server");
	private final ComputerWaitingList resumingClients = new ComputerWaitingList(infoTracker, true, "resuming client");
	private final ComputerWaitingList resumingServers = new ComputerWaitingList(infoTracker, false, "resuming server");
	private final Map<PlayerIdentifier, SburbPlayerData> playerDataMap = new HashMap<>();
	final List<ActiveConnection> activeConnections = new ArrayList<>();
	final SessionHandler sessionHandler;
	
	final MinecraftServer mcServer;
	
	private SkaianetHandler(MinecraftServer mcServer)
	{
		this.mcServer = mcServer;
		
		sessionHandler = new DefaultSessionHandler(this);
	}
	
	private SkaianetHandler(MinecraftServer mcServer, CompoundTag nbt)
	{
		this.mcServer = mcServer;
		
		SessionHandler sessions;
		if(nbt.contains("session", Tag.TAG_COMPOUND))
			sessions = new GlobalSessionHandler(this, nbt.getCompound("session"));
		else sessions = new DefaultSessionHandler(this, nbt.getList("sessions", Tag.TAG_COMPOUND));
		
		sessionHandler = sessions.getActual();
		
		openedServers.read(nbt.getList("serversOpen", Tag.TAG_COMPOUND));
		resumingClients.read(nbt.getList("resumingClients", Tag.TAG_COMPOUND));
		resumingServers.read(nbt.getList("resumingServers", Tag.TAG_COMPOUND));
		
		ListTag playerDataList = nbt.getList("player_data", Tag.TAG_COMPOUND);
		for(int i = 0; i < playerDataList.size(); i++)
		{
			CompoundTag playerDataTag = playerDataList.getCompound(i);
			PlayerIdentifier player = IdentifierHandler.load(playerDataTag, "player");
			getOrCreateData(player).read(playerDataTag);
		}
		
		ListTag connectionList = nbt.getList("connections", Tag.TAG_COMPOUND);
		for(int i = 0; i < connectionList.size(); i++)
		{
			activeConnections.add(ActiveConnection.read(connectionList.getCompound(i)));
		}
	}
	
	public Optional<ActiveConnection> getActiveConnection(PlayerIdentifier client)
	{
		return activeConnections().filter(c -> c.client().equals(client)).findAny();
	}
	
	public Optional<SburbConnection> primaryConnectionForClient(PlayerIdentifier player)
	{
		return primaryConnections().filter(c -> c.getClientIdentifier().equals(player)).findAny();
	}
	
	public Optional<SburbConnection> primaryConnectionForServer(PlayerIdentifier player)
	{
		return primaryConnections().filter(c -> c.hasServerPlayer() && c.getServerIdentifier().equals(player)).findAny();
	}
	
	public Optional<SburbConnection> getPrimaryOrCandidateConnection(PlayerIdentifier player, boolean isClient)
	{
		if(player == null || player.equals(IdentifierHandler.NULL_IDENTIFIER))
			return Optional.empty();
		
		Stream<SburbConnection> connections = sessionHandler.getConnectionStream();
		if(isClient)
			connections = connections.filter(c -> c.getClientIdentifier().equals(player));
		else connections = connections.filter(c -> c.getServerIdentifier().equals(player));
		
		return connections.max(Comparator.comparingInt(c -> c.isMain() ? 1 : 0));
	}
	
	public void connectToServer(ISburbComputer computer, PlayerIdentifier server)
	{
		PlayerIdentifier player = computer.getOwner();
		ComputerReference reference = computer.createReference();
		if(reference.isInNether() || isConnectingBlocked(player, true)
				|| !sessionHandler.getServerList(player).containsKey(server.getId()))
			return;
		
		ISburbComputer serverComputer = openedServers.getComputer(mcServer, server);
		
		if(serverComputer == null)
			return;
		
		Optional<SburbConnection> optional = getPrimaryOrCandidateConnection(player, true);
		if(optional.isEmpty())
		{
			try
			{
				SburbConnection newConnection = tryCreateNewConnectionFor(player, server);
				setActive(newConnection, computer, serverComputer, SburbEvent.ConnectionType.REGULAR);
				openedServers.remove(server);
			} catch(MergeResult.SessionMergeException e)
			{
				LOGGER.warn("Connection failed between {} and {}, reason: {}", player.getUsername(), server.getUsername(), e.getMessage());
				computer.putClientMessage(e.getResult().translationKey());
			}
			return;
		}
		
		SburbConnection connection = optional.get();
		if(connection.getServerIdentifier().equals(server))
		{
			setActive(connection, computer, serverComputer, SburbEvent.ConnectionType.RESUME);
			openedServers.remove(server);
		} else if(!connection.hasServerPlayer())
		{
			try
			{
				sessionHandler.getSessionForConnecting(player, server);
				connection.setNewServerPlayer(server);
				
				setActive(connection, computer, serverComputer, SburbEvent.ConnectionType.NEW_SERVER);
				openedServers.remove(server);
			} catch(MergeResult.SessionMergeException e)
			{
				LOGGER.warn("SessionHandler denied connection between {} and {}, reason: {}", player.getUsername(), server.getUsername(), e.getMessage());
				computer.putClientMessage(e.getResult().translationKey());
			}
		} else
		{
			try
			{
				SburbConnection newConnection = tryCreateSecondaryConnectionFor(connection, server);
				setActive(newConnection, computer, serverComputer, SburbEvent.ConnectionType.SECONDARY);
				openedServers.remove(server);
			} catch(MergeResult.SessionMergeException e)
			{
				LOGGER.warn("Secondary connection failed between {} and {}, reason: {}", player.getUsername(), server.getUsername(), e.getMessage());
				computer.putClientMessage(e.getResult().translationKey());
			}
		}
	}
	
	private void setActive(SburbConnection connection, ISburbComputer client, ISburbComputer server, SburbEvent.ConnectionType type)
	{
		Objects.requireNonNull(client);
		Objects.requireNonNull(server);
		
		if(connection.getActiveConnection() != null)
			throw new IllegalStateException("Should not activate sburb connection when already active");
		
		ActiveConnection activeConnection = new ActiveConnection(connection, client.createReference(), server.createReference());
		connection.skaianet.activeConnections.add(activeConnection);
		
		this.infoTracker.markDirty(connection);
		
		client.connected(connection.getServerIdentifier(), true);
		server.connected(connection.getClientIdentifier(), false);
		
		MinecraftForge.EVENT_BUS.post(new SburbEvent.ConnectionCreated(this.mcServer, activeConnection, type));
	}
	
	private SburbConnection tryCreateNewConnectionFor(PlayerIdentifier client, PlayerIdentifier server) throws MergeResult.SessionMergeException
	{
		Session session = sessionHandler.getSessionForConnecting(client, server);
		SburbConnection newConnection = new SburbConnection(client, server, this);
		session.connections.add(newConnection);
		
		return newConnection;
	}
	
	private SburbConnection tryCreateSecondaryConnectionFor(SburbConnection connection, PlayerIdentifier server) throws MergeResult.SessionMergeException
	{
		PlayerIdentifier client = connection.getClientIdentifier();
		Session session = sessionHandler.getSessionForConnecting(client, server);
		SburbConnection newConnection = new SburbConnection(client, server, this);
		session.connections.add(newConnection);
		
		return newConnection;
	}
	
	public void resumeConnection(ISburbComputer computer, boolean isClient)
	{
		PlayerIdentifier player = computer.getOwner();
		ComputerReference reference = computer.createReference();
		if(reference.isInNether() || isConnectingBlocked(player, isClient))
			return;
		Optional<SburbConnection> optional = getPrimaryOrCandidateConnection(player, isClient);
		
		if(optional.isEmpty() || getActiveConnection(optional.get().getClientIdentifier()).isPresent())
			return;
		
		SburbConnection connection = optional.get();
		
		if(isClient)
		{
			
			ISburbComputer otherComputer = resumingServers.getComputer(mcServer, connection.getServerIdentifier());
			
			if(otherComputer != null)
			{
				setActive(connection, computer, otherComputer, SburbEvent.ConnectionType.RESUME);
				resumingServers.remove(connection.getServerIdentifier());
				return;
			}
			
			otherComputer = openedServers.getComputer(mcServer, connection.getServerIdentifier());
			
			if(otherComputer != null)
			{
				setActive(connection, computer, otherComputer, SburbEvent.ConnectionType.RESUME);
				openedServers.remove(connection.getServerIdentifier());
			} else
				resumingClients.put(player, computer);
		} else
		{
			
			ISburbComputer otherComputer = resumingClients.getComputer(mcServer, connection.getClientIdentifier());
			
			if(otherComputer != null)
			{
				setActive(connection, otherComputer, computer, SburbEvent.ConnectionType.RESUME);
				resumingClients.remove(connection.getClientIdentifier());
			} else
				resumingServers.put(player, computer);
		}
	}
	
	public void openServer(ISburbComputer computer)
	{
		PlayerIdentifier player = computer.getOwner();
		ComputerReference reference = computer.createReference();
		if(reference.isInNether() || isConnectingBlocked(player, false))
			return;
		
		Optional<SburbConnection> optional = getPrimaryOrCandidateConnection(player, false);
		if(optional.isPresent() && !optional.get().isActive() && resumingClients.contains(optional.get().getClientIdentifier()))
		{
			SburbConnection connection = optional.get();
			ISburbComputer clientComputer = resumingClients.getComputer(mcServer, connection.getClientIdentifier());
			
			if(clientComputer != null)
			{
				resumingClients.remove(connection.getClientIdentifier());
				setActive(connection, clientComputer, computer, SburbEvent.ConnectionType.RESUME);
			}
		} else
		{
			openedServers.put(player, computer);
		}
	}
	
	private boolean isConnectingBlocked(PlayerIdentifier player, boolean isClient)
	{
		if(isClient)
			return getActiveConnection(player).isPresent() || resumingClients.contains(player);
		else return openedServers.contains(player) || resumingServers.contains(player);
	}
	
	public void closeClientConnectionRemotely(PlayerIdentifier player)
	{
		if(resumingClients.contains(player))
		{
			ISburbComputer computer = resumingClients.getComputer(mcServer, player);
			resumingClients.remove(player);
			if(computer != null)
			{
				computer.putClientBoolean("isResuming", false);
				computer.putClientMessage(STOP_RESUME);
			}
		} else
		{
			getActiveConnection(player).ifPresent(this::closeConnection);
		}
	}
	
	public void closeClientConnection(ISburbComputer computer)
	{
		PlayerIdentifier owner = computer.getOwner();
		if(resumingClients.contains(computer))
		{
			resumingClients.remove(owner);
			computer.putClientBoolean("isResuming", false);
			computer.putClientMessage(STOP_RESUME);
		} else
		{
			getClientConnection(computer).ifPresent(connection ->
					closeConnection(connection, computer, null));
		}
	}
	
	public void closeServerConnection(ISburbComputer computer)
	{
		checkAndCloseFromServerList(computer, openedServers);
		checkAndCloseFromServerList(computer, resumingServers);
		
		getServerConnection(computer).ifPresent(connection ->
				closeConnection(connection, null, computer));
	}
	
	private void checkAndCloseFromServerList(ISburbComputer computer, ComputerWaitingList map)
	{
		PlayerIdentifier owner = computer.getOwner();
		if(map.contains(computer))
		{
			map.remove(owner);
			computer.putServerBoolean("isOpen", false);
			computer.putServerMessage(STOP_RESUME);
		}
	}
	
	void closeConnection(ActiveConnection activeConnection)
	{
		closeConnection(activeConnection, null, null);
	}
	
	private void closeConnection(ActiveConnection connection, @Nullable ISburbComputer clientComputer, @Nullable ISburbComputer serverComputer)
	{
		SburbConnection sburbConnection = getConnection(connection);
		
		if(clientComputer == null)
			clientComputer = connection.clientComputer().getComputer(mcServer);
		if(serverComputer == null)
			serverComputer = connection.serverComputer().getComputer(mcServer);
		
		sessionHandler.onConnectionClosed(sburbConnection, true);
		
		activeConnections.remove(connection);
		infoTracker.markDirty(sburbConnection);
		
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
		
		MinecraftForge.EVENT_BUS.post(new SburbEvent.ConnectionClosed(mcServer, connection));
	}
	
	public void requestInfo(ServerPlayer player, PlayerIdentifier p1)
	{
		checkData();
		infoTracker.requestInfo(player, p1);
	}
	
	@Override
	public CompoundTag save(CompoundTag compound)
	{
		//checkData();
		
		sessionHandler.write(compound);
		
		compound.put("serversOpen", openedServers.write());
		compound.put("resumingClients", resumingClients.write());
		compound.put("resumingServers", resumingServers.write());
		
		ListTag playerDataList = new ListTag();
		for(SburbPlayerData playerData : playerDataMap.values())
		{
			CompoundTag playerDataTag = new CompoundTag();
			playerData.playerId().saveToNBT(playerDataTag, "player");
			playerData.write(playerDataTag);
			playerDataList.add(playerDataTag);
		}
		compound.put("player_data", playerDataList);
		
		ListTag connectionList = new ListTag();
		for(ActiveConnection connection : this.activeConnections)
			connectionList.add(connection.write());
		compound.put("connections", connectionList);
		
		return compound;
	}
	
	void checkData()
	{
		if(!MinestuckConfig.SERVER.skaianetCheck.get())
			return;
		
		openedServers.validate(mcServer);
		resumingClients.validate(mcServer);
		resumingServers.validate(mcServer);
		
		activeConnections().forEach(activeConnection -> {
			
			ISburbComputer cc = activeConnection.clientComputer().getComputer(mcServer),
					sc = activeConnection.serverComputer().getComputer(mcServer);
			if(cc == null || sc == null
					|| activeConnection.clientComputer().isInNether() || activeConnection.serverComputer().isInNether()
					|| !activeConnection.client().equals(cc.getOwner()) || !activeConnection.server().equals(sc.getOwner())
					|| !cc.getClientBoolean("connectedToServer"))
			{
				LOGGER.warn("[SKAIANET] Invalid computer in connection between {} and {}.", activeConnection.client(), activeConnection.server());
				closeConnection(activeConnection, cc, sc);
			}
		});
	}
	
	public SburbConnection getConnection(ActiveConnection connection)
	{
		return getConnection(connection.client(), connection.server());
	}
	
	public SburbConnection getConnection(PlayerIdentifier client, PlayerIdentifier server)
	{
		return sessionHandler.getConnectionStream().filter(c -> c.getClientIdentifier().equals(client) && c.getServerIdentifier().equals(server))
				.findAny().orElse(null);
	}
	
	boolean hasResumingClient(PlayerIdentifier identifier)
	{
		return resumingClients.contains(identifier);
	}
	
	boolean hasResumingServer(PlayerIdentifier identifier)
	{
		return resumingServers.contains(identifier) || openedServers.contains(identifier);
	}
	
	public Optional<ActiveConnection> getServerConnection(ISburbComputer computer)
	{
		return activeConnections().filter(c -> c.isServer(computer)).findAny();
	}
	
	public Optional<ActiveConnection> getClientConnection(ISburbComputer computer)
	{
		return activeConnections().filter(c -> c.isClient(computer)).findAny();
	}
	
	public Stream<ActiveConnection> activeConnections()
	{
		return sessionHandler.getConnectionStream().flatMap(connection -> Stream.ofNullable(connection.getActiveConnection()));
	}
	
	public Stream<SburbConnection> getConnectionsInEntry()
	{
		return primaryConnections().filter(connection -> !connection.data().hasEntered());
	}
	
	Stream<SburbConnection> primaryConnections()
	{
		return sessionHandler.getConnectionStream().filter(SburbConnection::isMain);
	}
	
	/**
	 * Prepares the sburb connection and data needed for after entry.
	 * Should only be called by the cruxite artifact on trigger before teleportation
	 * @param target the identifier of the player that is entering
	 * @return The dimension type of the new land created, or null if the player can't enter at this time.
	 */
	public ResourceKey<Level> prepareEntry(PlayerIdentifier target)
	{
		SburbConnection c = getPrimaryOrCandidateConnection(target, true).orElse(null);
		SburbPlayerData playerData = getOrCreateData(target);
		if(c == null)
		{
			LOGGER.info("Player {} entered without connection. Creating connection... ", target.getUsername());
			try
			{
				c = tryCreateNewConnectionFor(target, IdentifierHandler.NULL_IDENTIFIER);
				SburbHandler.onFirstItemGiven(c);
			} catch(MergeResult.SessionMergeException e)
			{
				LOGGER.error("Couldn't create a connection for {}: {}. Stopping entry.", target.getUsername(), e.getMessage());
				return null;
			}
		} else if(!c.isMain())
			SburbHandler.giveItems(mcServer, target);
		else if(playerData.getLandDimension() != null)
			return playerData.getLandDimension();
		
		SburbHandler.prepareEntry(mcServer, playerData);
		
		return playerData.getLandDimension();
	}
	
	/**
	 * Called when entry teleportation has successfully finished.
	 */
	public void onEntry(PlayerIdentifier target)
	{
		SburbHandler.onEntry(mcServer, getOrCreateData(target));
		
		infoTracker.reloadLandChains();
		
		MinecraftForge.EVENT_BUS.post(new OnEntryEvent(mcServer, target));
	}
	
	public void movingComputer(ComputerBlockEntity oldBE, ComputerBlockEntity newBE)
	{
		ComputerReference oldRef = ComputerReference.of(oldBE), newRef = ComputerReference.of(newBE);
		if(!oldBE.owner.equals(newBE.owner))
			throw new IllegalStateException("Moving computers with different owners! ("+oldBE.owner+" and "+newBE.owner+")");
		
		activeConnections().forEach(c -> c.updateComputer(oldBE, newRef));
		
		resumingClients.replace(oldBE.owner, oldRef, newRef);
		resumingServers.replace(oldBE.owner, oldRef, newRef);
		openedServers.replace(oldBE.owner, oldRef, newRef);
	}
	
	SburbPlayerData getOrCreateData(PlayerIdentifier player)
	{
		return this.playerDataMap.computeIfAbsent(player, playerId -> {
			var data = new SburbPlayerData(playerId, this.mcServer);
			SburbHandler.initNewData(data);
			return data;
		});
	}
	
	Collection<SburbPlayerData> allPlayerData()
	{
		return this.playerDataMap.values();
	}
	
	public static SkaianetHandler get(Level level)
	{
		MinecraftServer server = level.getServer();
		if(server == null)
			throw new IllegalArgumentException("Can't get skaianet instance on client side! (Got null server from level)");
		return get(server);
	}
	
	private static final String DATA_NAME = Minestuck.MOD_ID+"_skaianet";
	
	public static SkaianetHandler get(MinecraftServer server)
	{
		Objects.requireNonNull(server);
		
		ServerLevel level = server.overworld();
		
		DimensionDataStorage storage = level.getDataStorage();
		
		return storage.computeIfAbsent(nbt -> new SkaianetHandler(server, nbt), () -> new SkaianetHandler(server), DATA_NAME);
	}
	
	// Always save skaianet data, since it's difficult to reliably tell when skaianet data has changed.
	@Override
	public boolean isDirty()
	{
		return true;
	}
	
	/**
	 * Clears data connected to skaianet. Should only be called on a ServerStopped event by minestuck itself.
	 */
	public static void clear()
	{
		TitleSelectionHook.playersInTitleSelection.clear();
	}
}