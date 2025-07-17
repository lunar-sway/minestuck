package com.mraof.minestuck.player;

import com.google.common.collect.ImmutableList;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.mojang.serialization.Codec;
import com.mojang.serialization.JsonOps;
import com.mojang.serialization.MapCodec;
import com.mraof.minestuck.Minestuck;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimpleJsonResourceReloadListener;
import net.minecraft.util.profiling.ProfilerFiller;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.AddReloadListenerEvent;
import net.neoforged.neoforge.event.server.ServerStoppedEvent;
import net.neoforged.neoforge.registries.DeferredRegister;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;

@EventBusSubscriber(bus = EventBusSubscriber.Bus.GAME)
public final class EcheladderExpSources
{
	public static final DeferredRegister<MapCodec<? extends EcheladderExpSource>> REGISTER = DeferredRegister.create(Minestuck.id("exp_source"), Minestuck.MOD_ID);
	public static final Registry<MapCodec<? extends EcheladderExpSource>> REGISTRY = REGISTER.makeRegistry(builder -> {});
	
	public static final Codec<EcheladderExpSource> CODEC = REGISTRY.byNameCodec().dispatch(EcheladderExpSource::codec, Function.identity());
	private static final List<EcheladderExpSource> INSTANCE_LIST = new ArrayList<>();
	
	static
	{
		REGISTER.register("kill_entity", () -> EcheladderExpSource.KillEntity.CODEC);
		REGISTER.register("kill_entity_tag", () -> EcheladderExpSource.KillEntityTag.CODEC);
		REGISTER.register("advancement_earned", () -> EcheladderExpSource.AdvancementEarned.CODEC);
	}
	
	public static List<EcheladderExpSource> instance()
	{
		return Objects.requireNonNull(INSTANCE_LIST);
	}
	
	@SubscribeEvent
	public static void onResourceReload(AddReloadListenerEvent event)
	{
		event.addListener(new Loader());
	}
	
	@SubscribeEvent
	public static void onServerStopped(ServerStoppedEvent event)
	{
		INSTANCE_LIST.clear();
	}
	
	private static class Loader extends SimpleJsonResourceReloadListener
	{
		private static final Logger LOGGER = LogManager.getLogger();
		private static final Gson GSON = new GsonBuilder().create();
		
		public Loader()
		{
			super(GSON, "minestuck/exp_source");
		}
		
		@Override
		protected void apply(Map<ResourceLocation, JsonElement> jsonElements, ResourceManager resourceManager, ProfilerFiller profiler)
		{
			ImmutableList.Builder<EcheladderExpSource> listBuilder = ImmutableList.builder();
			for(Map.Entry<ResourceLocation, JsonElement> entry : jsonElements.entrySet())
			{
				EcheladderExpSources.CODEC.parse(JsonOps.INSTANCE, entry.getValue())
						.resultOrPartial(message -> LOGGER.error("Couldn't parse echeladder exp source {}: {}", entry.getKey(), message))
						.ifPresent(listBuilder::add);
			}
			
			INSTANCE_LIST.clear();
			INSTANCE_LIST.addAll(listBuilder.build());
			LOGGER.info("Loaded {} echeladder exp sources", INSTANCE_LIST.size());
		}
	}
}
