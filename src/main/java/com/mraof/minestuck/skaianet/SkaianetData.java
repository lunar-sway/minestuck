package com.mraof.minestuck.skaianet;

import com.mraof.minestuck.Minestuck;
import com.mraof.minestuck.MinestuckConfig;
import com.mraof.minestuck.computer.ISburbComputer;
import com.mraof.minestuck.player.IdentifierHandler;
import com.mraof.minestuck.player.PlayerIdentifier;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.saveddata.SavedData;
import net.minecraft.world.level.storage.DimensionDataStorage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.*;
import java.util.stream.Stream;

/**
 * This class handles server sided stuff about the sburb connection network.
 * This class also handles the main saving and loading.
 * @author kirderf1
 */
public final class SkaianetData extends SavedData
{
	private static final Logger LOGGER = LogManager.getLogger();
	
	final InfoTracker infoTracker = new InfoTracker(this);
	final SkaianetComputerInteractions computerInteractions;
	private final Map<PlayerIdentifier, SburbPlayerData> playerDataMap = new HashMap<>();
	final List<ActiveConnection> activeConnections = new ArrayList<>();
	final Map<PlayerIdentifier, PredefineData> predefineData = new HashMap<>();
	final SessionHandler sessionHandler;
	
	final MinecraftServer mcServer;
	
	private SkaianetData(MinecraftServer mcServer)
	{
		this.mcServer = mcServer;
		
		computerInteractions = new SkaianetComputerInteractions(this);
		sessionHandler = new SessionHandler.Multi(this).getActual();
	}
	
	private SkaianetData(MinecraftServer mcServer, CompoundTag nbt)
	{
		this.mcServer = mcServer;
		
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
		
		computerInteractions = new SkaianetComputerInteractions(this, nbt);
		
		SessionHandler sessions;
		if(nbt.contains("session", Tag.TAG_COMPOUND))
			sessions = new SessionHandler.Global(this, nbt.getCompound("session"));
		else sessions = new SessionHandler.Multi(this, nbt.getList("sessions", Tag.TAG_COMPOUND));
		
		sessionHandler = sessions.getActual();
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
	
	public void requestInfo(ServerPlayer player, PlayerIdentifier p1)
	{
		checkData();
		infoTracker.requestInfo(player, p1);
	}
	
	@Override
	public CompoundTag save(CompoundTag compound)
	{
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
		
		computerInteractions.write(compound);
		
		sessionHandler.write(compound);
		
		return compound;
	}
	
	void checkData()
	{
		if(!MinestuckConfig.SERVER.skaianetCheck.get())
			return;
		
		computerInteractions.validate();
		
		activeConnections().forEach(activeConnection -> {
			
			ISburbComputer cc = activeConnection.clientComputer().getComputer(mcServer),
					sc = activeConnection.serverComputer().getComputer(mcServer);
			if(cc == null || sc == null
					|| activeConnection.clientComputer().isInNether() || activeConnection.serverComputer().isInNether()
					|| !activeConnection.client().equals(cc.getOwner()) || !activeConnection.server().equals(sc.getOwner())
					|| !cc.getClientBoolean("connectedToServer"))
			{
				LOGGER.warn("[SKAIANET] Invalid computer in connection between {} and {}.", activeConnection.client(), activeConnection.server());
				SkaianetConnectionInteractions.closeConnection(activeConnection, cc, sc, this);
			}
		});
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
		sessionHandler.newPredefineData(player);
	}
	
	Optional<PredefineData> predefineData(PlayerIdentifier player)
	{
		return Optional.ofNullable(this.predefineData.get(player));
	}
	
	public Collection<SburbPlayerData> allPlayerData()
	{
		return this.playerDataMap.values();
	}
	
	public static SkaianetData get(Level level)
	{
		MinecraftServer server = level.getServer();
		if(server == null)
			throw new IllegalArgumentException("Can't get skaianet instance on client side! (Got null server from level)");
		return get(server);
	}
	
	private static final String DATA_NAME = Minestuck.MOD_ID+"_skaianet";
	
	public static SkaianetData get(MinecraftServer server)
	{
		Objects.requireNonNull(server);
		
		ServerLevel level = server.overworld();
		
		DimensionDataStorage storage = level.getDataStorage();
		
		return storage.computeIfAbsent(nbt -> new SkaianetData(server, nbt), () -> new SkaianetData(server), DATA_NAME);
	}
	
	// Always save skaianet data, since it's difficult to reliably tell when skaianet data has changed.
	@Override
	public boolean isDirty()
	{
		return true;
	}
}
