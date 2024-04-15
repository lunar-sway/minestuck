package com.mraof.minestuck.entity.dialogue;

import com.google.common.collect.BiMap;
import com.google.common.collect.ImmutableBiMap;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.mojang.serialization.JsonOps;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimpleJsonResourceReloadListener;
import net.minecraft.util.profiling.ProfilerFiller;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.event.AddReloadListenerEvent;
import net.neoforged.neoforge.event.server.ServerStoppedEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.Collection;
import java.util.Map;
import java.util.Objects;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.FORGE)
public class DialogueNodes
{
	private final BiMap<ResourceLocation, Dialogue.NodeSelector> dialogues;
	
	private DialogueNodes(BiMap<ResourceLocation, Dialogue.NodeSelector> dialogues)
	{
		this.dialogues = dialogues;
	}
	
	@Nullable
	private static DialogueNodes INSTANCE;
	
	public static DialogueNodes getInstance()
	{
		return Objects.requireNonNull(INSTANCE, "Called getInstance() before dialogue nodes had loaded");
	}
	
	@Nullable
	public Dialogue.NodeSelector getDialogue(ResourceLocation location)
	{
		return this.dialogues.get(location);
	}
	
	public Collection<ResourceLocation> allIds()
	{
		return this.dialogues.keySet();
	}
	
	@SubscribeEvent
	public static void onResourceReload(AddReloadListenerEvent event)
	{
		event.addListener(new Loader());
	}
	
	@SubscribeEvent
	public static void onServerStopped(ServerStoppedEvent event)
	{
		INSTANCE = null;
	}
	
	private static final class Loader extends SimpleJsonResourceReloadListener
	{
		private static final Logger LOGGER = LogManager.getLogger();
		private static final Gson GSON = new GsonBuilder().create();
		
		public Loader()
		{
			super(GSON, "minestuck/dialogue");
		}
		
		@Override
		protected void apply(Map<ResourceLocation, JsonElement> jsonEntries, ResourceManager resourceManager, ProfilerFiller profiler)
		{
			ImmutableBiMap.Builder<ResourceLocation, Dialogue.NodeSelector> dialogues = ImmutableBiMap.builder();
			for(Map.Entry<ResourceLocation, JsonElement> entry : jsonEntries.entrySet())
			{
				Dialogue.NodeSelector.CODEC.parse(JsonOps.INSTANCE, entry.getValue())
						.resultOrPartial(message -> LOGGER.error("Problem parsing dialogue {}: {}", entry.getKey(), message))
						.ifPresent(dialogue -> dialogues.put(entry.getKey(), dialogue));
			}
			
			INSTANCE = new DialogueNodes(dialogues.build());
			LOGGER.info("Loaded {} dialogues", INSTANCE.dialogues.size());
		}
	}
}