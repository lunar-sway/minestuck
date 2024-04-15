package com.mraof.minestuck.entity.dialogue;

import com.google.common.collect.ImmutableList;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.mojang.serialization.JsonOps;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimpleJsonResourceReloadListener;
import net.minecraft.util.profiling.ProfilerFiller;
import net.minecraft.util.random.WeightedEntry;
import net.minecraft.util.random.WeightedRandom;
import net.minecraft.world.entity.LivingEntity;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.event.AddReloadListenerEvent;
import net.neoforged.neoforge.event.server.ServerStoppedEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.*;

@ParametersAreNonnullByDefault
@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.FORGE)
public final class RandomlySelectableDialogue
{
	private final List<Dialogue.SelectableDialogue> selectableDialogueList;
	
	private RandomlySelectableDialogue(List<Dialogue.SelectableDialogue> selectableDialogueList)
	{
		this.selectableDialogueList = selectableDialogueList;
	}
	
	private static final Map<DialogueCategory, RandomlySelectableDialogue> INSTANCE_MAP = new HashMap<>();
	
	public static RandomlySelectableDialogue instance(DialogueCategory category)
	{
		return Objects.requireNonNull(INSTANCE_MAP.get(category), "Called instance() before this has loaded!");
	}
	
	public Optional<Dialogue.SelectableDialogue> pickRandomForEntity(LivingEntity entity)
	{
		List<WeightedEntry.Wrapper<Dialogue.SelectableDialogue>> weightedFilteredDialogue = new ArrayList<>();
		selectableDialogueList.forEach(selectable -> {
			if(selectable.condition().test(entity, null))
					weightedFilteredDialogue.add(WeightedEntry.wrap(selectable, selectable.weight()));
		});
		
		return WeightedRandom.getRandomItem(entity.getRandom(), weightedFilteredDialogue)
				.map(WeightedEntry.Wrapper::getData);
	}
	
	public Collection<Dialogue.SelectableDialogue> getAll()
	{
		return this.selectableDialogueList;
	}
	
	@SubscribeEvent
	public static void onResourceReload(AddReloadListenerEvent event)
	{
		for(DialogueCategory category : DialogueCategory.values())
			event.addListener(new Loader(category));
	}
	
	@SubscribeEvent
	public static void onServerStopped(ServerStoppedEvent event)
	{
		INSTANCE_MAP.clear();
	}
	
	public enum DialogueCategory
	{
		CONSORT("consort"),
		SHADY_CONSORT("shady_consort"),
		CONSORT_FOOD_MERCHANT("consort_food_merchant"),
		CONSORT_GENERAL_MERCHANT("consort_general_merchant"),
		CARAPACIAN_SOLDIER("carapacian_soldier"),
		;
		private final String folderName;
		
		DialogueCategory(String folderName)
		{
			this.folderName = folderName;
		}
		
		public String folderName()
		{
			return this.folderName;
		}
		
		public String folderNameForSelectable()
		{
			return "minestuck/selectable_dialogue/" + this.folderName();
		}
	}
	
	private static class Loader extends SimpleJsonResourceReloadListener
	{
		private static final Logger LOGGER = LogManager.getLogger();
		private static final Gson GSON = new GsonBuilder().create();
		
		private final DialogueCategory category;
		
		public Loader(DialogueCategory category)
		{
			super(GSON, category.folderNameForSelectable());
			this.category = category;
		}
		
		@Override
		protected void apply(Map<ResourceLocation, JsonElement> jsonElements, ResourceManager resourceManager, ProfilerFiller profiler)
		{
			ImmutableList.Builder<Dialogue.SelectableDialogue> listBuilder = ImmutableList.builder();
			
			for(Map.Entry<ResourceLocation, JsonElement> entry : jsonElements.entrySet())
			{
				Dialogue.SelectableDialogue.CODEC.parse(JsonOps.INSTANCE, entry.getValue())
						.resultOrPartial(message -> LOGGER.error("Problem loading {} selectable dialogue {}: {}", this.category.folderName(), entry.getKey(), message))
						.ifPresent(listBuilder::add);
			}
			
			INSTANCE_MAP.put(this.category, new RandomlySelectableDialogue(listBuilder.build()));
		}
	}
}
