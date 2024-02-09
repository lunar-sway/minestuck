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

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.FORGE)
public class DialogueJsonManager extends SimpleJsonResourceReloadListener
{
	private static final Logger LOGGER = LogManager.getLogger();
	private static final Gson GSON = new GsonBuilder().registerTypeAdapter(DialogueJson.class, new DialogueJson.Serializer()).create();
	
	private List<DialogueJson> dialogues;
	
	public DialogueJsonManager()
	{
		super(GSON, "minestuck/dialogue");
	}
	
	private static DialogueJsonManager INSTANCE;
	
	public static DialogueJsonManager getInstance()
	{
		return INSTANCE;
	}
	
	@Override
	protected void apply(Map<ResourceLocation, JsonElement> jsonEntries, ResourceManager resourceManager, ProfilerFiller profiler)
	{
		ImmutableList.Builder<DialogueJson> dialogues = ImmutableList.builder();
		for(Map.Entry<ResourceLocation, JsonElement> entry : jsonEntries.entrySet())
		{
			try
			{
				DialogueJson dialogue = GSON.fromJson(entry.getValue(), DialogueJson.class);
				dialogues.add(dialogue);
			} catch(Exception e)
			{
				LOGGER.error("Couldn't parse dialogue {}", entry.getKey(), e);
			}
		}
		
		this.dialogues = dialogues.build();
		LOGGER.info("Loaded {} dialogues", this.dialogues.size());
	}
	
	public DialogueJson doRandomDialogue(RandomSource rand)
	{
		if(dialogues.isEmpty())
			return null;
		else
			return dialogues.stream().toList().get(rand.nextInt(dialogues.size()));
	}
	
	public static JsonElement parsePrice(DialogueJson dialogue)
	{
		return GSON.toJsonTree(dialogue);
	}
	
	@SubscribeEvent
	public static void onResourceReload(AddReloadListenerEvent event)
	{
		event.addListener(INSTANCE = new DialogueJsonManager());
	}
}