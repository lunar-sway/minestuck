package com.mraof.minestuck.skaianet;

import com.mraof.minestuck.Minestuck;
import com.mraof.minestuck.MinestuckConfig;
import com.mraof.minestuck.advancements.MSCriteriaTriggers;
import com.mraof.minestuck.network.MSPacketHandler;
import com.mraof.minestuck.network.SkaianetInfoPacket;
import com.mraof.minestuck.player.IdentifierHandler;
import com.mraof.minestuck.player.PlayerIdentifier;
import com.mraof.minestuck.util.Debug;
import com.mraof.minestuck.util.LazyInstance;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.Style;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.dimension.DimensionType;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Works with the info that will be sent to players through {@link com.mraof.minestuck.network.SkaianetInfoPacket}
 */
@Mod.EventBusSubscriber(modid = Minestuck.MOD_ID)
public final class InfoTracker
{
	private final SkaianetHandler skaianet;
	
	private final Map<PlayerIdentifier, Set<PlayerIdentifier>> infoToSend = new HashMap<>();	//Key: player, value: data to send to player
	/**
	 * Chains of lands to be used by the skybox render
	 */
	private final LazyInstance<List<List<ResourceLocation>>> landChains = new LazyInstance<>(this::createLandChains);
	
	InfoTracker(SkaianetHandler skaianet)
	{
		this.skaianet = skaianet;
	}
	
	@SubscribeEvent
	public static void onPlayerLoggedIn(PlayerEvent.PlayerLoggedInEvent event)
	{
		if(event.getPlayer() instanceof ServerPlayerEntity)
		{
			ServerPlayerEntity player = (ServerPlayerEntity) event.getPlayer();
			SkaianetHandler.get(player.server).infoTracker.onPlayerLoggedIn(player);
		}
	}
	
	private void onPlayerLoggedIn(ServerPlayerEntity player)
	{
		PlayerIdentifier identifier = IdentifierHandler.encode(player);
		infoToSend.put(identifier, new HashSet<>(Collections.singleton(identifier)));
		sendConnectionInfo(identifier);
		MSPacketHandler.sendToPlayer(createLandChainPacket(), player);
	}
	
	void requestInfo(ServerPlayerEntity player, PlayerIdentifier p1)
	{
		PlayerIdentifier p0 = IdentifierHandler.encode(player);
		if(p0 == null)
			return;
		
		Set<PlayerIdentifier> s = infoToSend.get(p0);
		if(s == null)
		{
			Debug.error("[SKAIANET] Something went wrong with player \"" + player.getName() + "\"'s skaianet data!");
			return;
		}
		
		if(MinestuckConfig.SERVER.privateComputers.get() && !p0.equals(p1) && !player.hasPermissionLevel(2))
		{
			player.sendMessage(new StringTextComponent("[Minestuck] ").setStyle(new Style().setColor(TextFormatting.RED)).appendSibling(new TranslationTextComponent(SkaianetHandler.PRIVATE_COMPUTER)));
			return;
		}
		if(!s.add(p1))
		{
			Debug.warnf("[Skaianet] Player %s already got the requested data.", player.getName());
			sendConnectionInfo(p0);	//Update anyway, to fix whatever went wrong
			return;
		}
		
		sendConnectionInfo(p0);
	}
	
	
	private SkaianetInfoPacket createLandChainPacket()
	{
		return SkaianetInfoPacket.landChains(landChains.get());
	}
	
	private List<List<ResourceLocation>> createLandChains()
	{
		List<List<ResourceLocation>> landChains = new ArrayList<>();
		
		Set<DimensionType> checked = new HashSet<>();
		skaianet.sessionHandler.getConnectionStream().forEach(c -> populateLandChain(landChains, checked, c));
		
		return landChains;
	}
	
	private void populateLandChain(List<List<ResourceLocation>> landChains, Set<DimensionType> checked, SburbConnection c)
	{
		DimensionType dimensionType = c.getClientDimension();
		if(c.isMain() && dimensionType != null && !checked.contains(dimensionType))
		{
			LinkedList<ResourceLocation> chain = new LinkedList<>();
			chain.add(c.getClientDimension().getRegistryName());
			checked.add(c.getClientDimension());
			SburbConnection cIter = c;
			while(true)
			{
				cIter = skaianet.getMainConnection(cIter.getClientIdentifier(), false).orElse(null);
				if(cIter != null && cIter.hasEntered())
				{
					if(!checked.contains(cIter.getClientDimension()))
					{
						chain.addLast(cIter.getClientDimension().getRegistryName());
						checked.add(cIter.getClientDimension());
					} else break;
				} else
				{
					chain.addLast(null);
					break;
				}
			}
			cIter = c;
			while(true)
			{
				cIter = skaianet.getMainConnection(cIter.getServerIdentifier(), true).orElse(null);
				if(cIter != null && cIter.hasEntered() && !checked.contains(cIter.getClientDimension()))
				{
					chain.addFirst(cIter.getClientDimension().getRegistryName());
					checked.add(cIter.getClientDimension());
				} else
				{
					break;
				}
			}
			landChains.add(chain);
		}
	}
	
	void reloadLandChains()
	{
		landChains.invalidate();
		MSPacketHandler.sendToAll(createLandChainPacket());
	}
	
	void sendInfoToAll()
	{
		for(PlayerIdentifier i : infoToSend.keySet())
			sendConnectionInfo(i);
	}
	
	void sendConnectionInfo(PlayerIdentifier player)
	{
		Set<PlayerIdentifier> iden = infoToSend.get(player);
		ServerPlayerEntity playerMP = player.getPlayer(skaianet.mcServer);
		if(iden == null || playerMP == null)//If the player disconnected
			return;
		
		//Trigger advancement if there is an active connection that the player is in
		skaianet.sessionHandler.getConnectionStream().filter(SburbConnection::isActive).filter(c -> c.hasPlayer(player))
				.findAny().ifPresent(c -> MSCriteriaTriggers.SBURB_CONNECTION.trigger(playerMP));
		
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
		boolean clientResuming = skaianet.hasResumingClient(player);
		boolean serverResuming = skaianet.hasResumingServer(player);
		
		Map<Integer, String> serverMap = skaianet.sessionHandler.getServerList(player);
		
		// create list with all connections that the player is in
		List<SburbConnection> list = skaianet.sessionHandler.getConnectionStream().filter(c -> c.hasPlayer(player)).collect(Collectors.toList());
		
		return SkaianetInfoPacket.update(player.getId(), clientResuming, serverResuming, serverMap, list);
	}
	
	void checkData()
	{
		Iterator<PlayerIdentifier> iter0 = infoToSend.keySet().iterator();
		while(iter0.hasNext())
			if(iter0.next().getPlayer(skaianet.mcServer) == null)
			{
				Debug.warn("[SKAIANET] Player disconnected, removing data.");
				iter0.remove();
			}
		
		if(MinestuckConfig.SERVER.privateComputers.get())
		{
			for(Map.Entry<PlayerIdentifier, Set<PlayerIdentifier>> entry : infoToSend.entrySet())
			{
				ServerPlayerEntity player = entry.getKey().getPlayer(skaianet.mcServer);
				if(player != null && player.hasPermissionLevel(2))
					continue;
				
				entry.getValue().removeIf(identifier -> !identifier.equals(entry.getKey()));
			}
		}
	}
}
