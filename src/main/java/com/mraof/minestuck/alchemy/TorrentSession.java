package com.mraof.minestuck.alchemy;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.mraof.minestuck.Minestuck;
import com.mraof.minestuck.MinestuckConfig;
import com.mraof.minestuck.api.alchemy.*;
import com.mraof.minestuck.item.MSItems;
import com.mraof.minestuck.network.TorrentPackets;
import com.mraof.minestuck.player.Echeladder;
import com.mraof.minestuck.player.GristCache;
import com.mraof.minestuck.player.IdentifierHandler;
import com.mraof.minestuck.player.PlayerData;
import com.mraof.minestuck.skaianet.SessionHandler;
import com.mraof.minestuck.world.storage.MSExtraData;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.event.TickEvent;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import net.neoforged.neoforge.network.PacketDistributor;

import java.util.*;

@Mod.EventBusSubscriber(modid = Minestuck.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class TorrentSession
{
	private static final Codec<List<GristType>> GRISTS_CODEC = Codec.list(GristTypes.REGISTRY.byNameCodec());
	private static final Codec<List<Leech>> LEECHES_CODEC = Codec.list(Leech.CODEC);
	public static final Codec<TorrentSession> CODEC = RecordCodecBuilder.create(instance -> instance.group(
			IdentifierHandler.UUIDIdentifier.CODEC.fieldOf("seeder").forGetter(TorrentSession::getSeeder),
			GRISTS_CODEC.fieldOf("seeding").forGetter(TorrentSession::getSeeding),
			LEECHES_CODEC.fieldOf("leeching").forGetter(TorrentSession::getLeeching)
	).apply(instance, TorrentSession::new));
	public static final Codec<List<TorrentSession>> LIST_CODEC = CODEC.listOf();
	
	private final IdentifierHandler.UUIDIdentifier seeder;
	private final List<GristType> seeding = new ArrayList<>();
	private final List<Leech> leeching = new ArrayList<>();
	
	public TorrentSession(IdentifierHandler.UUIDIdentifier seeder, List<GristType> seeding, List<Leech> leeching)
	{
		this.seeder = seeder;
		this.seeding.addAll(seeding);
		this.leeching.addAll(leeching);
	}
	
	public static TorrentSession createPlayerTorrentSession(ServerPlayer player, MinecraftServer server)
	{
		return createPlayerTorrentSession((IdentifierHandler.UUIDIdentifier) IdentifierHandler.encode(player), server);
	}
	
	public static TorrentSession createPlayerTorrentSession(IdentifierHandler.UUIDIdentifier player, MinecraftServer server)
	{
		List<GristType> seeding = new ArrayList<>();
		
		if(MinestuckConfig.SERVER.gristTorrentSeedAll.get())
			seeding.addAll(GristTypes.REGISTRY.stream().toList());
		
		TorrentSession torrentSession = new TorrentSession(player, seeding, new ArrayList<>());
		
		MSExtraData.get(server).tryAddTorrentSession(torrentSession);
		
		return torrentSession;
	}
	
	public static TorrentSession createTestTorrentSession(int id, boolean seedAll)
	{
		List<GristType> seeding = new ArrayList<>();
		
		if(seedAll)
			seeding.addAll(GristTypes.REGISTRY.stream().toList());
		
		return new TorrentSession(new IdentifierHandler.UUIDIdentifier(id, UUID.randomUUID()), seeding, new ArrayList<>());
	}
	
	public IdentifierHandler.UUIDIdentifier getSeeder()
	{
		return seeder;
	}
	
	public List<GristType> getSeeding()
	{
		return seeding;
	}
	
	public void setSeeding(List<GristType> newSeeding)
	{
		seeding.clear();
		seeding.addAll(newSeeding);
	}
	
	public List<Leech> getLeeching()
	{
		return leeching;
	}
	
	public void addLeech(Leech leech)
	{
		leeching.add(leech);
	}
	
	public void setLeeching(List<Leech> newLeeching)
	{
		leeching.clear();
		leeching.addAll(newLeeching);
	}
	
	/**
	 * @return whether the input PlayerIdentifier is trying to leech the given grist type from this TorrentSession
	 */
	public boolean isLeechForGristType(IdentifierHandler.UUIDIdentifier identifier, GristType gristType)
	{
		return leeching.stream().anyMatch(leech -> leech.UUIDMatches(identifier) && leech.gristTypes.contains(gristType));
	}
	
	public boolean sameOwner(TorrentSession otherSession)
	{
		return sameOwner(otherSession.seeder);
	}
	
	public boolean sameOwner(IdentifierHandler.UUIDIdentifier identifier)
	{
		return this.seeder.getUUID().equals(identifier.getUUID());
	}
	
	@SubscribeEvent
	public static void onServerTickEvent(TickEvent.ServerTickEvent event)
	{
		MinecraftServer server = event.getServer();
		
		if(event.phase == TickEvent.Phase.START && server.overworld().getGameTime() % 20 == 0)
		{
			if(MinestuckConfig.SERVER.gristTorrentVisibility.get().equals(MinestuckConfig.TorrentVisibility.NONE))
				return; //no need to update if nothing is visible
			
			MSExtraData data = MSExtraData.get(server);
			List<TorrentSession> sessions = data.getTorrentSessions();
			for(TorrentSession torrentSession : sessions)
			{
				handleTorrent(torrentSession, sessions, server);
			}
			
			//TODO placeholders
			debugStuff(server, data);
		}
	}
	
	private static void debugStuff(MinecraftServer server, MSExtraData data)
	{
		for(ServerPlayer player : server.getPlayerList().getPlayers())
			if(player.isHolding(MSItems.ALLWEDDOL.get()))
				data.removesSessions();
		for(ServerPlayer player : server.getPlayerList().getPlayers())
			if(player.isHolding(MSItems.MWRTHWL.get()))
			{
				TorrentSession playerSession = TorrentSession.createPlayerTorrentSession(player, server);
				List<IdentifierHandler.UUIDIdentifier> testIDs = new ArrayList<>();
				
				if(data.getTorrentSessions().stream().anyMatch(session -> session.seeder.getId() == 99))
					return;
				
				for(int i = 99; i < 102; i++)
				{
					TorrentSession iterateSession = TorrentSession.createTestTorrentSession(i, true);
					data.tryAddTorrentSession(iterateSession);
					
					IdentifierHandler.UUIDIdentifier testID = iterateSession.seeder;
					testIDs.add(testID);
					
					List<GristAmount> gristAmounts = new ArrayList<>();
					gristAmounts.add(new GristAmount(GristTypes.SHALE, 100));
					
					GristCache.get(server, testID).set(new NonNegativeGristSet(gristAmounts));
				}
				
				List<GristType> leechGrist = new ArrayList<>();
				leechGrist.add(GristTypes.BUILD.get());
				playerSession.addLeech(new Leech(testIDs.get(0), leechGrist));
				MSExtraData.get(server).tryAddTorrentSession(playerSession);
			}
	}
	
	private static void handleTorrent(TorrentSession torrentSession, List<TorrentSession> sessions, MinecraftServer server)
	{
		IdentifierHandler.UUIDIdentifier seeder = torrentSession.seeder;
		
		PlayerData seederData = PlayerData.get(seeder, server);
		GristCache seederCache = GristCache.get(seederData);
		ImmutableGristSet seederCacheGristSet = seederCache.getGristSet();
		
		List<GristType> seeding = torrentSession.seeding;
		seeding = seeding.stream().filter(seederCacheGristSet::hasType).toList(); //remove any grist types from the seeding list if the cache has none of it to seed
		int numberSeeds = seeding.size();
		
		if(numberSeeds == 0)
			return;
		
		int seedRateMod = Math.max(1, (int) (GristTypes.REGISTRY.stream().count() / numberSeeds));
		
		List<Leech> leeching = torrentSession.leeching;
		
		for(GristType grist : seeding)
		{
			handleGristSeed(grist, leeching, seedRateMod, seederCache, server);
		}
		
		//TODO consider holding on to torrent data that may be sent to multiple players
		ServerPlayer seederPlayer = seeder.getPlayer(server);
		if(seederPlayer != null)
		{
			MinestuckConfig.TorrentVisibility visibilityConfig = MinestuckConfig.SERVER.gristTorrentVisibility.get();
			boolean sessionOnly = visibilityConfig.equals(MinestuckConfig.TorrentVisibility.SESSION);
			boolean globalVisibility = visibilityConfig.equals(MinestuckConfig.TorrentVisibility.GLOBAL);
			
			Map<TorrentSession, TorrentSession.LimitedCache> visibleTorrentData = new HashMap<>();
			sessions.forEach(session -> {
				IdentifierHandler.UUIDIdentifier sessionPlayerID = session.seeder;
				
				//TODO add Land config option functionality
				//SkaianetData.get(server).getOrCreatePredefineData(sessionPlayerID);
				
				boolean inSameSession = SessionHandler.get(server).isInSameSession(seeder, sessionPlayerID);
				boolean isVisible = globalVisibility || (sessionOnly && inSameSession);
				
				if(isVisible)
				{
					PlayerData leechData = PlayerData.get(sessionPlayerID, server);
					GristCache leechCache = GristCache.get(leechData);
					LimitedCache leechLimitedCache = new LimitedCache(leechCache.getGristSet(), Echeladder.get(leechData).getGristCapacity());
					
					visibleTorrentData.put(session, leechLimitedCache);
				}
			});
			
			PacketDistributor.PLAYER.with(seederPlayer).send(new TorrentPackets.UpdateClient(visibleTorrentData));
		}
	}
	
	private static void handleGristSeed(GristType grist, List<Leech> leeching, int seedRateMod, GristCache seederCache, MinecraftServer server)
	{
		List<Leech> eligibleLeeches = leeching.stream().filter(leech -> leech.gristTypes.contains(grist)).toList();
		int leechCount = eligibleLeeches.size();
		
		int combinedRate = Math.max(1, leechCount / seedRateMod);
		GristAmount gristAmount = new GristAmount(grist, combinedRate);
		
		//TODO ensure that grist cannot enter the leeches gutter
		eligibleLeeches.forEach(leech -> {
			if(!seederCache.canAfford(gristAmount))
				return;
			
			PlayerData leechData = PlayerData.get(leech.id, server);
			GristCache leechCache = GristCache.get(leechData);
			
			//try to add the grist to the leeches cache and if successful then try to remove from seeder cache
			GristSet remainder = leechCache.addWithinCapacity(gristAmount, null);
			if(remainder.isEmpty())
				seederCache.tryTake(gristAmount, null);
		});
	}
	
	@SubscribeEvent
	public static void onPlayerLoggedIn(PlayerEvent.PlayerLoggedInEvent event)
	{
		ServerPlayer player = (ServerPlayer) event.getEntity();
		TorrentSession.createPlayerTorrentSession(player, player.server);
	}
	
	public record Leech(IdentifierHandler.UUIDIdentifier id, List<GristType> gristTypes)
	{
		public static final Codec<Leech> CODEC = RecordCodecBuilder.create(instance -> instance.group(
				IdentifierHandler.UUIDIdentifier.CODEC.fieldOf("id").forGetter(Leech::id),
				GRISTS_CODEC.fieldOf("grist_types").forGetter(Leech::gristTypes)
		).apply(instance, Leech::new));
		
		public boolean UUIDMatches(IdentifierHandler.UUIDIdentifier identifierIn)
		{
			return id.getUUID().equals(identifierIn.getUUID());
		}
		
		//TODO find a way to express the share ratio, for potential leech banning
	}
	
	//TODO This overlaps heavily with ClientCache, however that uses GristSet which does not have a CODEC
	public record LimitedCache(ImmutableGristSet set, long limit)
	{
		public static final Codec<LimitedCache> CODEC = RecordCodecBuilder.create(instance -> instance.group(
				ImmutableGristSet.NON_NEGATIVE_CODEC.fieldOf("grist_set").forGetter(LimitedCache::set),
				Codec.LONG.fieldOf("limit").forGetter(LimitedCache::limit)
		).apply(instance, LimitedCache::new));
	}
	
	public static final Codec<Map<TorrentSession, LimitedCache>> TORRENT_DATA_CODEC = Codec.unboundedMap(TorrentSession.CODEC, LimitedCache.CODEC);
}