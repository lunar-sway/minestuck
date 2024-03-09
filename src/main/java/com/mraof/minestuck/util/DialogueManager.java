package com.mraof.minestuck.util;

import com.google.common.collect.ImmutableMap;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.mojang.serialization.JsonOps;
import com.mraof.minestuck.entity.dialogue.Dialogue;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimpleJsonResourceReloadListener;
import net.minecraft.util.RandomSource;
import net.minecraft.util.profiling.ProfilerFiller;
import net.minecraft.util.random.WeightedEntry;
import net.minecraft.util.random.WeightedRandom;
import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.event.AddReloadListenerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@ParametersAreNonnullByDefault
@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.FORGE)
public class DialogueManager extends SimpleJsonResourceReloadListener
{
	private static final Logger LOGGER = LogManager.getLogger();
	private static final Gson GSON = new GsonBuilder().create();
	
	private Map<ResourceLocation, Dialogue> dialogues;
	
	public DialogueManager()
	{
		super(GSON, "minestuck/dialogue");
	}
	
	private static DialogueManager INSTANCE;
	
	public static DialogueManager getInstance()
	{
		return INSTANCE;
	}
	
	@Override
	protected void apply(Map<ResourceLocation, JsonElement> jsonEntries, ResourceManager resourceManager, ProfilerFiller profiler)
	{
		ImmutableMap.Builder<ResourceLocation, Dialogue> dialogues = ImmutableMap.builder();
		for(Map.Entry<ResourceLocation, JsonElement> entry : jsonEntries.entrySet())
		{
			Dialogue.CODEC.parse(JsonOps.INSTANCE, entry.getValue())
					.resultOrPartial(message -> LOGGER.error("Problem parsing dialogue {}: {}", entry.getKey(), message))
					.ifPresent(dialogue -> dialogues.put(dialogue.path(), dialogue));
		}
		
		this.dialogues = dialogues.build();
		LOGGER.info("Loaded {} dialogues", this.dialogues.size());
	}
	
	@Nullable
	public Dialogue doRandomDialogue(LivingEntity entity, RandomSource rand)
	{
		List<WeightedEntry.Wrapper<Dialogue>> weightedFilteredDialogue = new ArrayList<>();
		dialogues.values().forEach(dialogue -> {
			dialogue.useContext().ifPresent(useContext -> {
				if(useContext.getConditions().testWithContext(entity, null))
					weightedFilteredDialogue.add(WeightedEntry.wrap(dialogue, useContext.getWeight()));
			});
		});
		
		return WeightedRandom.getRandomItem(rand, weightedFilteredDialogue)
				.map(WeightedEntry.Wrapper::getData).orElse(null);
	}
	
	@Nullable
	public Dialogue getDialogue(ResourceLocation location)
	{
		return this.dialogues.get(location);
	}
	
	@SubscribeEvent
	public static void onResourceReload(AddReloadListenerEvent event)
	{
		event.addListener(INSTANCE = new DialogueManager());
	}
}