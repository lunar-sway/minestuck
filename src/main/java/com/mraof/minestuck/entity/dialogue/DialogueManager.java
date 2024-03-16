package com.mraof.minestuck.entity.dialogue;

import com.google.common.collect.BiMap;
import com.google.common.collect.ImmutableBiMap;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.mojang.serialization.JsonOps;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimpleJsonResourceReloadListener;
import net.minecraft.util.profiling.ProfilerFiller;
import net.minecraftforge.event.AddReloadListenerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.Map;
import java.util.Objects;

@ParametersAreNonnullByDefault
@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.FORGE)
public class DialogueManager extends SimpleJsonResourceReloadListener
{
	private static final Logger LOGGER = LogManager.getLogger();
	private static final Gson GSON = new GsonBuilder().create();
	
	private BiMap<ResourceLocation, Dialogue> dialogues;
	
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
		ImmutableBiMap.Builder<ResourceLocation, Dialogue> dialogues = ImmutableBiMap.builder();
		for(Map.Entry<ResourceLocation, JsonElement> entry : jsonEntries.entrySet())
		{
			Dialogue.CODEC.parse(JsonOps.INSTANCE, entry.getValue())
					.resultOrPartial(message -> LOGGER.error("Problem parsing dialogue {}: {}", entry.getKey(), message))
					.ifPresent(dialogue -> dialogues.put(entry.getKey(), dialogue));
		}
		
		this.dialogues = dialogues.build();
		LOGGER.info("Loaded {} dialogues", this.dialogues.size());
	}
	
	@Nullable
	public Dialogue getDialogue(ResourceLocation location)
	{
		return this.dialogues.get(location);
	}
	
	public ResourceLocation getId(Dialogue dialogue)
	{
		return Objects.requireNonNull(this.dialogues.inverse().get(dialogue), "Dialogue is missing an id!");
	}
	
	@SubscribeEvent
	public static void onResourceReload(AddReloadListenerEvent event)
	{
		event.addListener(INSTANCE = new DialogueManager());
	}
}