package com.mraof.minestuck.alchemy;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.mraof.minestuck.Minestuck;
import com.mraof.minestuck.MinestuckConfig;
import com.mraof.minestuck.api.alchemy.*;
import com.mraof.minestuck.player.IdentifierHandler;
import com.mraof.minestuck.world.storage.MSExtraData;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.event.TickEvent;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;

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
	
	private final IdentifierHandler.UUIDIdentifier seeder;
	private final List<GristType> seeding = new ArrayList<>();
	private final List<Leech> leeching = new ArrayList<>();
	
	public TorrentSession(IdentifierHandler.UUIDIdentifier seeder, List<GristType> seeding, List<Leech> leeching)
	{
		this.seeder = seeder;
		this.seeding.addAll(seeding);
		this.leeching.addAll(leeching);
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
				TorrentHelper.handleTorrent(torrentSession, sessions, server);
			}
			
			//TODO placeholders to remove, including createTestTorrentSession()
			TorrentHelper.debugStuff(server, data);
		}
	}
	
	@SubscribeEvent
	public static void onPlayerLoggedIn(PlayerEvent.PlayerLoggedInEvent event)
	{
		ServerPlayer player = (ServerPlayer) event.getEntity();
		TorrentHelper.createPlayerTorrentSession(player, player.server);
	}
	
	public List<GristType> getViableSeeding(LimitedCache cache)
	{
		return getViableSeeding(cache.set);
	}
	
	/**
	 * Returns the list of everything the torrent is seeding, minus any grist types the relevant cache cannot distribute
	 */
	public List<GristType> getViableSeeding(ImmutableGristSet seederCacheSet)
	{
		List<GristType> seeding = new ArrayList<>(this.seeding);
		
		seeding = seeding.stream().filter(seederCacheSet::hasType).toList();
		
		return seeding;
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