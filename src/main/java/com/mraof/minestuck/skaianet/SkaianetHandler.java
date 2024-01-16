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
	private final List<ActiveConnection> activeConnections = new ArrayList<>();
	final Map<PlayerIdentifier, PredefineData> predefineData = new HashMap<>();
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
		
		if(nbt.contains("predefine_data", Tag.TAG_LIST))
		{
			ListTag list = nbt.getList("predefine_data", Tag.TAG_COMPOUND);
			for(int i = 0; i < list.size(); i++)
			{
				CompoundTag compound = list.getCompound(i);
				PlayerIdentifier player = IdentifierHandler.load(compound, "player");
				predefineData.put(player, new PredefineData(player).read(compound));
			}
		}
		
		ListTag connectionList = nbt.getList("connections", Tag.TAG_COMPOUND);
		for(int i = 0; i < connectionList.size(); i++)
			activeConnections.add(ActiveConnection.read(connectionList.getCompound(i)));
		
		sessionHandler.getSessions().forEach(Session::updatePlayerSet);
		sessionHandler.getSessions().forEach(Session::checkIfCompleted);
	}
	
	public Optional<ActiveConnection> getActiveConnection(PlayerIdentifier client)
	{
		return activeConnections().filter(c -> c.client().equals(client)).findAny();
	}
	
	public boolean hasPrimaryConnectionForClient(PlayerIdentifier player)
	{
		return getOrCreateData(player).hasPrimaryConnection();
	}
	
	public Optional<PlayerIdentifier> primaryPartnerForClient(PlayerIdentifier player)
	{
		return getOrCreateData(player).primaryServerPlayer();
	}
	
	public boolean hasPrimaryConnectionForServer(PlayerIdentifier player)
	{
		return playerDataMap.values().stream().anyMatch(data -> data.isPrimaryServerPlayer(player));
	}
	
	public Optional<PlayerIdentifier> primaryPartnerForServer(PlayerIdentifier player)
	{
		return playerDataMap.values().stream().filter(data -> data.isPrimaryServerPlayer(player)).findAny().map(SburbPlayerData::playerId);
	}
	
	public boolean canMakeNewRegularConnectionAsServer(PlayerIdentifier serverPlayer)
	{
		return !hasPrimaryConnectionForServer(serverPlayer)
				&& activeConnections().filter(connection -> connection.server().equals(serverPlayer))
				.allMatch(connection -> getOrCreateData(connection.client()).hasPrimaryConnection());
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
		
		SburbPlayerData playerData = getOrCreateData(player);
		if(!playerData.hasPrimaryConnection())
		{
			if(!canMakeNewRegularConnectionAsServer(server))
			{
				LOGGER.warn("Connection failed between {} and {}", player.getUsername(), server.getUsername());
				return;
			}
			
			Session session = sessionHandler.prepareSessionFor(player, server);
			session.addConnectedClient(player);
			
			setActive(computer, serverComputer, SburbEvent.ConnectionType.REGULAR);
			openedServers.remove(server);
			return;
		}
		
		Optional<PlayerIdentifier> primaryServer = playerData.primaryServerPlayer();
		if(primaryServer.isEmpty())
		{
			if(!canMakeNewRegularConnectionAsServer(server))
			{
				LOGGER.warn("Connection between {} and {} denied because the latter is already in a connection", player.getUsername(), server.getUsername());
				return;
			}
			
			sessionHandler.prepareSessionFor(player, server);
			newServerForClient(player, server);
			
			setActive(computer, serverComputer, SburbEvent.ConnectionType.NEW_SERVER);
			openedServers.remove(server);
			return;
		}
		
		if(primaryServer.get().equals(server))
		{
			setActive(computer, serverComputer, SburbEvent.ConnectionType.RESUME);
			openedServers.remove(server);
		} else if(sessionHandler.canMakeSecondaryConnection(player, server))
		{
			setActive(computer, serverComputer, SburbEvent.ConnectionType.SECONDARY);
			openedServers.remove(server);
		}
	}
	
	private void setActive(ISburbComputer client, ISburbComputer server, SburbEvent.ConnectionType type)
	{
		Objects.requireNonNull(client);
		Objects.requireNonNull(server);
		
		if(getActiveConnection(client.getOwner()).isPresent())
			throw new IllegalStateException("Should not activate sburb connection when already active");
		
		ActiveConnection activeConnection = new ActiveConnection(client, server);
		activeConnections.add(activeConnection);
		this.infoTracker.markDirty(activeConnection);
		
		client.connected(server.getOwner(), true);
		server.connected(client.getOwner(), false);
		
		MinecraftForge.EVENT_BUS.post(new SburbEvent.ConnectionCreated(this.mcServer, activeConnection, type));
	}
	
	public void resumeClientConnection(ISburbComputer computer)
	{
		PlayerIdentifier player = computer.getOwner();
		if(computer.createReference().isInNether() || isConnectingBlocked(player, true))
			return;
		
		if(!hasPrimaryConnectionForClient(player))
			return;
		
		Optional<PlayerIdentifier> server = primaryPartnerForClient(player);
		if(server.isPresent())
		{
			ISburbComputer otherComputer = resumingServers.getComputer(mcServer, server.get());
			if(otherComputer != null)
			{
				setActive(computer, otherComputer, SburbEvent.ConnectionType.RESUME);
				resumingServers.remove(server.get());
				return;
			}
			
			otherComputer = openedServers.getComputer(mcServer, server.get());
			if(otherComputer != null)
			{
				setActive(computer, otherComputer, SburbEvent.ConnectionType.RESUME);
				openedServers.remove(server.get());
				return;
			}
		}
		
		resumingClients.put(player, computer);
	}
	
	public void resumeServerConnection(ISburbComputer computer)
	{
		PlayerIdentifier player = computer.getOwner();
		if(computer.createReference().isInNether() || isConnectingBlocked(player, false))
			return;
		
		Optional<PlayerIdentifier> optionalClient = primaryPartnerForServer(player);
		if(optionalClient.isEmpty())
			return;
		
		PlayerIdentifier client = optionalClient.get();
		if(getActiveConnection(client).isPresent())
			return;
		
		ISburbComputer otherComputer = resumingClients.getComputer(mcServer, client);
		if(otherComputer != null)
		{
			setActive(otherComputer, computer, SburbEvent.ConnectionType.RESUME);
			resumingClients.remove(client);
			return;
		}
		
		resumingServers.put(player, computer);
	}
	
	public void openServer(ISburbComputer computer)
	{
		PlayerIdentifier player = computer.getOwner();
		ComputerReference reference = computer.createReference();
		if(reference.isInNether() || isConnectingBlocked(player, false))
			return;
		
		Optional<PlayerIdentifier> primaryClient = primaryPartnerForServer(player);
		if(primaryClient.isPresent() && getActiveConnection(primaryClient.get()).isEmpty()
				&& resumingClients.contains(primaryClient.get()))
		{
			ISburbComputer clientComputer = resumingClients.getComputer(mcServer, primaryClient.get());
			
			if(clientComputer != null)
			{
				resumingClients.remove(primaryClient.get());
				setActive(clientComputer, computer, SburbEvent.ConnectionType.RESUME);
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
		if(clientComputer == null)
			clientComputer = connection.clientComputer().getComputer(mcServer);
		if(serverComputer == null)
			serverComputer = connection.serverComputer().getComputer(mcServer);
		
		sessionHandler.onConnectionClosed(connection);
		
		activeConnections.remove(connection);
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
		
		ListTag predefineList = new ListTag();
		for(Map.Entry<PlayerIdentifier, PredefineData> entry : predefineData.entrySet())
			predefineList.add(entry.getKey().saveToNBT(entry.getValue().write(), "player"));
		compound.put("predefine_data", predefineList);
		
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
		return activeConnections.stream();
	}
	
	public Stream<ActiveConnection> getConnectionsInEntry()
	{
		return activeConnections().filter(connection -> {
			SburbPlayerData playerData = getOrCreateData(connection.client());
			return playerData.hasPrimaryConnection() && !playerData.hasEntered();
		});
	}
	
	Stream<PlayerIdentifier> players()
	{
		return playerDataMap.keySet().stream();
	}
	
	void trySetPrimaryConnection(ActiveConnection connection)
	{
		trySetPrimaryConnection(connection.client(), connection.server());
	}
	
	void trySetPrimaryConnection(PlayerIdentifier client, PlayerIdentifier server)
	{
		if(hasPrimaryConnectionForClient(client) || hasPrimaryConnectionForServer(server))
			throw new IllegalStateException();
		
		Optional<ActiveConnection> activeConnection = getActiveConnection(client);
		if(activeConnection.isPresent() && !activeConnection.get().server().equals(server))
			throw new IllegalStateException();
		
		if(activeConnection.isEmpty())
		{
			if(!activeConnections().filter(connection -> connection.server().equals(server))
					.allMatch(connection -> getOrCreateData(connection.client()).hasPrimaryConnection()))
				throw new IllegalStateException();
			
			sessionHandler.prepareSessionFor(client, server)
					.addConnectedClient(client);
		}
		getOrCreateData(client).setHasPrimaryConnection(server);
	}
	
	void unlinkClientPlayer(PlayerIdentifier clientPlayer)
	{
		getActiveConnection(clientPlayer).ifPresent(this::closeConnection);
		getOrCreateData(clientPlayer).removeServerPlayer();
	}
	
	void unlinkServerPlayer(PlayerIdentifier serverPlayer)
	{
		primaryPartnerForServer(serverPlayer).ifPresent(this::unlinkClientPlayer);
		activeConnections().filter(connection -> connection.server().equals(serverPlayer) && !getOrCreateData(connection.client()).hasPrimaryConnection())
				.forEach(this::closeConnection);
	}
	
	void newServerForClient(PlayerIdentifier clientPlayer, PlayerIdentifier serverPlayer)
	{
		getOrCreateData(clientPlayer).setNewServerPlayer(serverPlayer);
	}
	
	/**
	 * Prepares the sburb connection and data needed for after entry.
	 * Should only be called by the cruxite artifact on trigger before teleportation
	 * @param target the identifier of the player that is entering
	 * @return The dimension type of the new land created, or null if the player can't enter at this time.
	 */
	public ResourceKey<Level> prepareEntry(PlayerIdentifier target)
	{
		SburbPlayerData playerData = getOrCreateData(target);
		if(!playerData.hasPrimaryConnection())
		{
			Optional<ActiveConnection> connection = getActiveConnection(target);
			if(connection.isPresent())
				trySetPrimaryConnection(connection.get());
			else
			{
				LOGGER.info("Player {} entered without connection.", target.getUsername());
				
				trySetPrimaryConnection(target, IdentifierHandler.NULL_IDENTIFIER);
			}
		}
		
		if(playerData.getLandDimension() == null)
			SburbHandler.prepareEntry(mcServer, playerData);
		
		return playerData.getLandDimension();
	}
	
	/**
	 * Called when entry teleportation has successfully finished.
	 */
	public void onEntry(PlayerIdentifier target)
	{
		SburbHandler.onEntry(mcServer, getOrCreateData(target));
		
		infoTracker.markLandChainDirty();
		
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
	
	void predefineCall(PlayerIdentifier player, SkaianetException.SkaianetConsumer<PredefineData> consumer) throws SkaianetException
	{
		PredefineData data = predefineData.get(player);
		if(data == null)    //TODO Do not create data for players that have entered (and clear predefined data when no longer needed)
			data = new PredefineData(player);
		consumer.consume(data);
		predefineData.put(player, data);
	}
	
	Optional<PredefineData> predefineData(PlayerIdentifier player)
	{
		return Optional.ofNullable(this.predefineData.get(player));
	}
	
	public Collection<SburbPlayerData> allPlayerData()
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
}