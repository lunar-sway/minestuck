package com.mraof.minestuck.alchemy;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.mraof.minestuck.Minestuck;
import com.mraof.minestuck.api.alchemy.*;
import com.mraof.minestuck.network.TorrentUpdatePacket;
import com.mraof.minestuck.player.GristCache;
import com.mraof.minestuck.player.IdentifierHandler;
import com.mraof.minestuck.player.PlayerData;
import com.mraof.minestuck.world.storage.MSExtraData;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.event.TickEvent;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import net.neoforged.neoforge.network.PacketDistributor;

import java.util.ArrayList;
import java.util.List;

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
	
	public static TorrentSession createPlayerTorrentSession(ServerPlayer player, boolean seedAll)
	{
		List<GristType> seeding = new ArrayList<>();
		
		if(seedAll)
			seeding.addAll(GristTypes.REGISTRY.stream().toList());
		
		return new TorrentSession((IdentifierHandler.UUIDIdentifier) IdentifierHandler.encode(player), seeding, new ArrayList<>());
	}
	
	public IdentifierHandler.UUIDIdentifier getSeeder()
	{
		return seeder;
	}
	
	public List<GristType> getSeeding()
	{
		return seeding;
	}
	
	public List<Leech> getLeeching()
	{
		return leeching;
	}
	
	@SubscribeEvent
	public static void onServerTickEvent(TickEvent.ServerTickEvent event)
	{
		MinecraftServer server = event.getServer();
		
		if(event.phase == TickEvent.Phase.START && server.overworld().getGameTime() % 20 == 0)
		{
			List<TorrentSession> sessions = MSExtraData.get(server).getTorrentSessions();
			for(TorrentSession torrentSession : sessions)
			{
				handleTorrent(torrentSession, sessions, server);
			}
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
		
		//TODO handle visibility limitation
		ServerPlayer seederPlayer = seeder.getPlayer(server);
		if(seederPlayer != null)
			PacketDistributor.PLAYER.with(seederPlayer).send(new TorrentUpdatePacket(sessions));
	}
	
	private static void handleGristSeed(GristType grist, List<Leech> leeching, int seedRateMod, GristCache seederCache, MinecraftServer server)
	{
		List<Leech> eligibleLeeches = leeching.stream().filter(leech -> leech.gristTypes.contains(grist)).toList();
		int leechCount = eligibleLeeches.size();
		
		int combinedRate = leechCount / seedRateMod;
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
		//TODO add config determining default seeding
		TorrentSession session = TorrentSession.createPlayerTorrentSession(player, true);
		MSExtraData.get(player.server).addTorrentSession(session);
	}
	
	public record Leech(IdentifierHandler.UUIDIdentifier id, List<GristType> gristTypes)
	{
		public static final Codec<Leech> CODEC = RecordCodecBuilder.create(instance -> instance.group(
				IdentifierHandler.UUIDIdentifier.CODEC.fieldOf("id").forGetter(Leech::id),
				GRISTS_CODEC.fieldOf("grist_types").forGetter(Leech::gristTypes)
		).apply(instance, Leech::new));
		
		//TODO find a way to express the share ratio, for potential leech banning
	}
}