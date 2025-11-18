package com.mraof.minestuck.inventory.musicplayer;

import com.google.common.collect.ImmutableMap;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.mojang.serialization.JsonOps;
import com.mraof.minestuck.item.components.CassettePlayable;
import com.mraof.minestuck.item.components.MSItemComponents;
import com.mraof.minestuck.network.CassetteSongsDataPacket;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimpleJsonResourceReloadListener;
import net.minecraft.util.profiling.ProfilerFiller;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.AddReloadListenerEvent;
import net.neoforged.neoforge.event.OnDatapackSyncEvent;
import net.neoforged.neoforge.event.server.ServerStoppedEvent;
import net.neoforged.neoforge.network.PacketDistributor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.*;

@EventBusSubscriber(bus = EventBusSubscriber.Bus.GAME)
public final class CassetteSongs
{
	private static final Logger LOGGER = LogManager.getLogger();
	
	private final Map<ResourceLocation, CassetteSong> songs;
	
	private static CassetteSongs INSTANCE;
	
	private CassetteSongs(Map<ResourceLocation, CassetteSong> songs)
	{
		this.songs = songs;
	}
	
	public Optional<CassetteSong> findSong(ItemStack stack)
	{
		if(stack.has(MSItemComponents.CASSETTE_SONG))
		{
			CassettePlayable song = stack.get(MSItemComponents.CASSETTE_SONG);
			return findSong(song.song());
		}
		return Optional.empty();
	}
	
	public Optional<CassetteSong> findSong(ResourceLocation song)
	{
		if(songs.containsKey(song)) return Optional.of(songs.get(song));
		return Optional.empty();
	}
	
	
	@SubscribeEvent
	private static void onResourceReload(AddReloadListenerEvent event)
	{
		event.addListener(new CassetteSongs.Loader());
	}
	
	@SubscribeEvent
	private static void onServerStopped(ServerStoppedEvent event)
	{
		INSTANCE = null;
	}
	
	@SubscribeEvent
	private static void onResourceReload(OnDatapackSyncEvent event)
	{
		List<ResourceLocation> locations = new ArrayList<>();
		List<CassetteSong> songs = new ArrayList<>();
		
		for(Map.Entry<ResourceLocation, CassetteSong> entry : INSTANCE.songs.entrySet())
		{
			locations.add(entry.getKey());
			songs.add(entry.getValue());
		}
		
		if(event.getPlayer() != null)
		{
			PacketDistributor.sendToPlayer(event.getPlayer(), new CassetteSongsDataPacket(locations, songs), new CustomPacketPayload[0]);
		} else
		{
			PacketDistributor.sendToAllPlayers(new CassetteSongsDataPacket(locations, songs), new CustomPacketPayload[0]);
		}
	}
	
	public static CassetteSongs getInstance()
	{
		return Objects.requireNonNull(INSTANCE);
	}
	
	public static CassetteSongs createfromLists(List<ResourceLocation> locations, List<CassetteSong> songs)
	{
		if(locations.size() != songs.size())
		{
			throw new IllegalStateException(String.format("Attempted to store cassette data with from {} locations with {} songs", locations.size(), songs.size()));
		}
		
		ImmutableMap.Builder<ResourceLocation, CassetteSong> map = ImmutableMap.builder();
		for(int i = 0; i < locations.size(); i++)
		{
			map.put(locations.get(i), songs.get(i));
		}
		
		INSTANCE = new CassetteSongs(map.build());
		LOGGER.info("Loaded {} cassette songs client-side", INSTANCE.songs.size());
		return INSTANCE;
	}
	
	private static final class Loader extends SimpleJsonResourceReloadListener
	{
		private static final Logger LOGGER = LogManager.getLogger();
		
		Loader()
		{
			super(new GsonBuilder().create(), "minestuck/cassette_effects");
		}
		
		@Override
		protected void apply(Map<ResourceLocation, JsonElement> entries, ResourceManager resourceManager, ProfilerFiller profiler)
		{
			ImmutableMap.Builder<ResourceLocation, CassetteSong> songs = ImmutableMap.builder();
			
			for(Map.Entry<ResourceLocation, JsonElement> entry : entries.entrySet())
			{
				CassetteSong.CODEC.parse(JsonOps.INSTANCE, entry.getValue())
						.resultOrPartial(message -> LOGGER.error("Couldn't parse cassette song {}: {}", entry.getKey(), message))
						.ifPresent(value -> {
							songs.put(entry.getKey(), value);
						});
			}
			
			INSTANCE = new CassetteSongs(songs.build());
			LOGGER.info("Loaded {} cassette songs", INSTANCE.songs.size());
		}
	}
}
