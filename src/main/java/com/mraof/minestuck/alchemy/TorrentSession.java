package com.mraof.minestuck.alchemy;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.mraof.minestuck.Minestuck;
import com.mraof.minestuck.MinestuckConfig;
import com.mraof.minestuck.api.alchemy.GristSet;
import com.mraof.minestuck.api.alchemy.GristType;
import com.mraof.minestuck.api.alchemy.GristTypes;
import com.mraof.minestuck.player.IdentifierHandler;
import com.mraof.minestuck.player.PlayerIdentifier;
import com.mraof.minestuck.world.storage.MSExtraData;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.nbt.Tag;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import net.neoforged.neoforge.event.tick.ServerTickEvent;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@EventBusSubscriber(modid = Minestuck.MOD_ID, bus = EventBusSubscriber.Bus.GAME)
public class TorrentSession
{
	private static final Codec<List<GristType>> GRISTS_CODEC = Codec.list(GristTypes.REGISTRY.byNameCodec());
	
	private final PlayerIdentifier seeder;
	private final List<GristType> seeding = new ArrayList<>();
	private final List<Leech> leeching = new ArrayList<>();
	
	public TorrentSession(PlayerIdentifier seeder, List<GristType> seeding, List<Leech> leeching)
	{
		this.seeder = seeder;
		this.seeding.addAll(seeding);
		this.leeching.addAll(leeching);
	}
	
	public static TorrentSession read(CompoundTag tag)
	{
		PlayerIdentifier seeder = IdentifierHandler.load(tag, "seeder").getOrThrow();
		List<GristType> seeding = GRISTS_CODEC.parse(NbtOps.INSTANCE, tag.get("seeding")).getPartialOrThrow();
		List<Leech> leeching = new ArrayList<>();
		ListTag leechTags = tag.getList("leeching", Tag.TAG_COMPOUND);
		for(int i = 0; i < leechTags.size(); i++)
			leeching.add(Leech.read(leechTags.getCompound(i)));
		
		return new TorrentSession(seeder, seeding, leeching);
	}
	
	public CompoundTag write()
	{
		CompoundTag tag = new CompoundTag();
		this.seeder.saveToNBT(tag, "seeder");
		tag.put("seeding", GRISTS_CODEC.encodeStart(NbtOps.INSTANCE, this.seeding).getPartialOrThrow());
		ListTag leechTags = new ListTag();
		for(Leech leech : leeching)
			leechTags.add(leech.write());
		tag.put("leeching", leechTags);
		
		return tag;
	}
	
	public PlayerIdentifier getSeeder()
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
	public boolean isLeechForGristType(PlayerIdentifier identifier, GristType gristType)
	{
		return leeching.stream().anyMatch(leech -> leech.matches(identifier) && leech.gristTypes.contains(gristType));
	}
	
	public boolean sameOwner(TorrentSession otherSession)
	{
		return sameOwner(otherSession.seeder);
	}
	
	public boolean sameOwner(PlayerIdentifier identifier)
	{
		return this.seeder.equals(identifier);
	}
	
	@SubscribeEvent
	public static void onServerTickEvent(ServerTickEvent.Post event)
	{
		MinecraftServer server = event.getServer();
		
		if(server.overworld().getGameTime() % 20 == 0)
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
	public List<GristType> getViableSeeding(GristSet.Immutable seederCacheSet)
	{
		List<GristType> seeding = new ArrayList<>(this.seeding);
		
		seeding = seeding.stream().filter(seederCacheSet::hasType).toList();
		
		return seeding;
	}
	
	public record Leech(PlayerIdentifier id, List<GristType> gristTypes)
	{
		static Leech read(CompoundTag tag)
		{
			PlayerIdentifier id = IdentifierHandler.load(tag, "id").getOrThrow();
			List<GristType> gristTypes = GRISTS_CODEC.parse(NbtOps.INSTANCE, tag.get("grist_types")).getPartialOrThrow();
			return new Leech(id, gristTypes);
		}
		
		CompoundTag write()
		{
			CompoundTag tag = new CompoundTag();
			this.id.saveToNBT(tag, "id");
			tag.put("grist_types", GRISTS_CODEC.encodeStart(NbtOps.INSTANCE, this.gristTypes).getPartialOrThrow());
			return tag;
		}
		
		public boolean matches(PlayerIdentifier identifierIn)
		{
			return id.equals(identifierIn);
		}
		
		//TODO find a way to express the share ratio, for potential leech banning
	}
	
	//TODO This overlaps heavily with ClientCache, however that uses GristSet which does not have a CODEC
	public record LimitedCache(GristSet.Immutable set, long limit)
	{
		public static final Codec<LimitedCache> CODEC = RecordCodecBuilder.create(instance -> instance.group(
				GristSet.Codecs.NON_NEGATIVE_CODEC.fieldOf("grist_set").forGetter(LimitedCache::set),
				Codec.LONG.fieldOf("limit").forGetter(LimitedCache::limit)
		).apply(instance, LimitedCache::new));
		public static final StreamCodec<RegistryFriendlyByteBuf, LimitedCache> STREAM_CODEC = StreamCodec.composite(
				GristSet.Codecs.STREAM_CODEC,
				LimitedCache::set,
				ByteBufCodecs.VAR_LONG,
				LimitedCache::limit,
				LimitedCache::new
		);
	}
	
	public record TorrentClientData(String username, List<GristType> seededTypes, Map<Integer, List<GristType>> leeches, LimitedCache cache)
	{
		public static final StreamCodec<RegistryFriendlyByteBuf, TorrentClientData> STREAM_CODEC = StreamCodec.composite(
				ByteBufCodecs.STRING_UTF8,
				TorrentClientData::username,
				GristType.STREAM_CODEC.apply(ByteBufCodecs.list()),
				TorrentClientData::seededTypes,
				ByteBufCodecs.map(HashMap::new, ByteBufCodecs.INT, GristType.STREAM_CODEC.apply(ByteBufCodecs.list())),
				TorrentClientData::leeches,
				LimitedCache.STREAM_CODEC,
				TorrentClientData::cache,
				TorrentClientData::new);
		
		public List<GristType> getViableSeeding()
		{
			return this.seededTypes.stream().filter(this.cache.set::hasType).toList();
		}
	}
}
