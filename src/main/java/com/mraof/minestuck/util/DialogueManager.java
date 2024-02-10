package com.mraof.minestuck.util;

import com.google.common.collect.ImmutableList;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimpleJsonResourceReloadListener;
import net.minecraft.util.RandomSource;
import net.minecraft.util.profiling.ProfilerFiller;
import net.minecraftforge.event.AddReloadListenerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.FORGE)
public class DialogueManager extends SimpleJsonResourceReloadListener
{
	private static final Logger LOGGER = LogManager.getLogger();
	private static final Gson GSON = new GsonBuilder().registerTypeAdapter(Dialogue.class, new Dialogue.Serializer()).create();
	
	private List<Dialogue> dialogues;
	
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
		ImmutableList.Builder<Dialogue> dialogues = ImmutableList.builder();
		for(Map.Entry<ResourceLocation, JsonElement> entry : jsonEntries.entrySet())
		{
			try
			{
				Dialogue dialogue = GSON.fromJson(entry.getValue(), Dialogue.class);
				dialogues.add(dialogue);
			} catch(Exception e)
			{
				LOGGER.error("Couldn't parse dialogue {}", entry.getKey(), e);
			}
		}
		
		this.dialogues = dialogues.build();
		LOGGER.info("Loaded {} dialogues", this.dialogues.size());
	}
	
	public Dialogue doRandomDialogue(RandomSource rand)
	{
		if(dialogues.isEmpty())
			return null;
		else
			return dialogues.stream().toList().get(rand.nextInt(dialogues.size()));
	}
	
	public Dialogue getDialogue(ResourceLocation location)
	{
		Optional<Dialogue> potentialDialogue = dialogues.stream().filter(dialogue ->
				dialogue.getPath().equals(location)).findAny();
		return potentialDialogue.orElse(null);
	}
	
	public static JsonElement parseDialogue(Dialogue dialogue)
	{
		return GSON.toJsonTree(dialogue);
	}
	
	@SubscribeEvent
	public static void onResourceReload(AddReloadListenerEvent event)
	{
		event.addListener(INSTANCE = new DialogueManager());
	}
}