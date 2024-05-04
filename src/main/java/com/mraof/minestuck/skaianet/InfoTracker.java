package com.mraof.minestuck.skaianet;

import com.mraof.minestuck.Minestuck;
import com.mraof.minestuck.MinestuckConfig;
import com.mraof.minestuck.advancements.MSCriteriaTriggers;
import com.mraof.minestuck.network.computer.SkaianetInfoPacket;
import com.mraof.minestuck.player.IdentifierHandler;
import com.mraof.minestuck.player.PlayerIdentifier;
import com.mraof.minestuck.skaianet.client.ReducedConnection;
import com.mraof.minestuck.skaianet.client.ReducedPlayerState;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.Level;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.event.TickEvent;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import net.neoforged.neoforge.network.PacketDistributor;
import net.neoforged.neoforge.server.ServerLifecycleHooks;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.*;

/**
 * Works with the info that will be sent to players through {@link SkaianetInfoPacket}
 */
@Mod.EventBusSubscriber(modid = Minestuck.MOD_ID)
public final class InfoTracker
{
	private static final Logger LOGGER = LogManager.getLogger();
	
	public static final String PRIVATE_COMPUTER = "minestuck.private_computer";
	
	private final SkaianetData skaianet;
	
	private final Map<PlayerIdentifier, Set<PlayerIdentifier>> listenerMap = new HashMap<>();
	private final Set<PlayerIdentifier> toUpdate = new HashSet<>();
	private final Map<PlayerIdentifier, Set<Integer>> openedServersCache = new HashMap<>();
	/**
	 * Chains of lands to be used by the skybox render
	 */
	private boolean resendLandChains;
	
	InfoTracker(SkaianetData skaianet)
	{
		this.skaianet = skaianet;
	}
	
	@SubscribeEvent
	public static void onPlayerLoggedIn(PlayerEvent.PlayerLoggedInEvent event)
	{
		if(event.getEntity() instanceof ServerPlayer player)
		{
			SkaianetData.get(player.server).infoTracker.onPlayerLoggedIn(player);
		}
	}
	
	@SubscribeEvent
	public static void onPlayerLoggedOut(PlayerEvent.PlayerLoggedOutEvent event)
	{
		if(event.getEntity() instanceof ServerPlayer player)
		{
			PlayerIdentifier identifier = Objects.requireNonNull(IdentifierHandler.encode(player));
			SkaianetData.get(player.server).infoTracker.listenerMap.values().forEach(set -> set.removeIf(identifier::equals));
		}
	}
	
	@SubscribeEvent
	public static void onServerTick(TickEvent.ServerTickEvent event)
	{
		if(event.phase == TickEvent.Phase.END)
		{
			SkaianetData.get(ServerLifecycleHooks.getCurrentServer()).infoTracker.checkAndSend();
		}
	}
	
	private void onPlayerLoggedIn(ServerPlayer player)
	{
		PlayerIdentifier identifier = IdentifierHandler.encode(player);
		getSet(identifier).add(identifier);
		sendConnectionInfo(identifier);
		PacketDistributor.PLAYER.with(player).send(createLandChainPacket(),
				new SkaianetInfoPacket.HasEntered(SburbPlayerData.get(player).hasEntered()));
	}
	
	private Set<PlayerIdentifier> getSet(PlayerIdentifier identifier)
	{
		return listenerMap.computeIfAbsent(identifier, ignored -> new HashSet<>());
	}
	
	void requestInfo(ServerPlayer player, PlayerIdentifier p1)
	{
		PlayerIdentifier p0 = IdentifierHandler.encode(player);
		if(p0 == null)
			return;
		
		if(cannotAccess(player, p1))
		{
			player.sendSystemMessage(Component.literal("[Minestuck] ").withStyle(ChatFormatting.RED).append(Component.translatable(PRIVATE_COMPUTER)));
			return;
		}
		if(!getSet(p1).add(p0))
		{
			LOGGER.warn("[Skaianet] Player {} already got the requested data.", player.getName());
		}
		
		PacketDistributor.PLAYER.with(player).send(generateClientInfoPacket(p1));
	}
	
	
	private SkaianetInfoPacket.LandChains createLandChainPacket()
	{
		return new SkaianetInfoPacket.LandChains(createLandChains());
	}
	
	private List<List<ResourceKey<Level>>> createLandChains()
	{
		List<List<ResourceKey<Level>>> landChains = new ArrayList<>();
		
		Set<ResourceKey<Level>> checked = new HashSet<>();
		skaianet.players().forEach(player -> populateLandChain(landChains, checked, player));
		
		return landChains;
	}
	
	private void populateLandChain(List<List<ResourceKey<Level>>> landChains, Set<ResourceKey<Level>> checked, PlayerIdentifier player)
	{
		ResourceKey<Level> dimensionType = skaianet.getOrCreateData(player).getLandDimensionIfEntered();
		if(dimensionType == null || checked.contains(dimensionType))
			return;
		
		LinkedList<ResourceKey<Level>> chain = new LinkedList<>();
		chain.add(dimensionType);
		checked.add(dimensionType);
		
		PlayerIdentifier lastPlayer = player;
		while(true)
		{
			Optional<PlayerIdentifier> client = skaianet.connections.primaryPartnerForServer(lastPlayer);
			if(client.isEmpty())
			{
				chain.addLast(null);
				break;
			}
			PlayerIdentifier nextPlayer = client.get();
			ResourceKey<Level> nextLand = skaianet.getOrCreateData(nextPlayer).getLandDimensionIfEntered();
			if(nextLand == null)
			{
				chain.addLast(null);
				break;
			}
			if(checked.contains(nextLand))
				break;
			
			chain.addLast(nextLand);
			checked.add(nextLand);
			lastPlayer = nextPlayer;
		}
		
		lastPlayer = player;
		while(true)
		{
			Optional<PlayerIdentifier> server = skaianet.connections.primaryPartnerForClient(lastPlayer);
			if(server.isEmpty())
				break;
			PlayerIdentifier nextPlayer = server.get();
			
			ResourceKey<Level> nextLand = skaianet.getOrCreateData(nextPlayer).getLandDimensionIfEntered();
			if(nextLand == null || checked.contains(nextLand))
				break;
			
			chain.addFirst(nextLand);
			checked.add(nextLand);
			lastPlayer = nextPlayer;
		}
		landChains.add(chain);
	}
	
	void markLandChainDirty()
	{
		resendLandChains = true;
	}
	
	void markDirty(PlayerIdentifier player)
	{
		toUpdate.add(player);
	}
	
	void markDirty(ActiveConnection connection)
	{
		markDirty(connection.client());
		markDirty(connection.server());
	}
	
	private void checkAndSend()
	{
		checkListeners();
		
		for(Map.Entry<PlayerIdentifier, Set<Integer>> entry : openedServersCache.entrySet())
		{
			if(!entry.getValue().equals(skaianet.sessionHandler.getServerList(entry.getKey()).keySet()))
				markDirty(entry.getKey());
		}
		
		if(!toUpdate.isEmpty())
		{
			toUpdate.forEach(this::sendConnectionInfo);
			toUpdate.clear();
		}
		
		if(resendLandChains)
		{
			PacketDistributor.ALL.noArg().send(createLandChainPacket());
			resendLandChains = false;
		}
	}
	
	private void sendConnectionInfo(PlayerIdentifier player)
	{
		SkaianetInfoPacket.Data packet = generateClientInfoPacket(player);
		
		for(PlayerIdentifier listener : getSet(player))
		{
			ServerPlayer playerListener = listener.getPlayer(skaianet.mcServer);
			
			if(playerListener != null)
			{
				if(player.equals(listener))
				{
					if(skaianet.connections.activeConnections().anyMatch(c -> c.hasPlayer(player)))
						MSCriteriaTriggers.SBURB_CONNECTION.get().trigger(playerListener);
				}
				
				PacketDistributor.PLAYER.with(playerListener).send(packet);
			}
		}
	}
	
	private SkaianetInfoPacket.Data generateClientInfoPacket(PlayerIdentifier player)
	{
		boolean clientResuming = skaianet.computerInteractions.hasResumingClient(player);
		boolean serverResuming = skaianet.computerInteractions.hasResumingServer(player);
		
		boolean hasPrimaryConnectionAsClient = skaianet.connections.hasPrimaryConnectionForClient(player);
		boolean hasPrimaryConnectionAsServer = skaianet.connections.hasPrimaryConnectionForServer(player);
		
		Map<Integer, String> serverMap = skaianet.sessionHandler.getServerList(player);
		openedServersCache.put(player, serverMap.keySet());
		
		ReducedPlayerState playerState = new ReducedPlayerState(clientResuming, serverResuming,
				hasPrimaryConnectionAsClient, hasPrimaryConnectionAsServer, serverMap);
		
		// create list with all connections that the player is in
		List<ReducedConnection> list = skaianet.connections.activeConnections().filter(c -> c.hasPlayer(player)).map(ReducedConnection::new).toList();
		
		return new SkaianetInfoPacket.Data(player.getId(), playerState, list);
	}
	
	private void checkListeners()
	{
		listenerMap.forEach((identifier, set) -> set.removeIf(listener -> cannotAccess(listener.getPlayer(skaianet.mcServer), identifier)));
	}
	
	private boolean cannotAccess(ServerPlayer listener, PlayerIdentifier identifier)
	{
		return listener == null || (MinestuckConfig.SERVER.privateComputers.get() && !identifier.appliesTo(listener)
				&& !listener.hasPermissions(2));
	}
}