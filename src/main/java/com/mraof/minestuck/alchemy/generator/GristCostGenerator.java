package com.mraof.minestuck.alchemy.generator;

import com.mraof.minestuck.Minestuck;
import com.mraof.minestuck.MinestuckConfig;
import com.mraof.minestuck.alchemy.GristCostRecipe;
import com.mraof.minestuck.alchemy.GristSet;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.crafting.RecipeManager;
import net.minecraftforge.event.AddReloadListenerEvent;
import net.minecraftforge.event.TagsUpdatedEvent;
import net.minecraftforge.event.server.ServerStoppedEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.*;
import java.util.stream.Stream;

@Mod.EventBusSubscriber(modid = Minestuck.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public final class GristCostGenerator
{
	private static final Logger LOGGER = LogManager.getLogger();
	
	//Hacky access to the recipe manager, which isn't otherwise accessible in TagsUpdatedEvent
	private static RecipeManager recipes;
	@SubscribeEvent
	public static void onResourceReload(AddReloadListenerEvent event)
	{
		recipes = event.getServerResources().getRecipeManager();
	}
	@SubscribeEvent
	public static void onServerStopped(ServerStoppedEvent event)
	{
		recipes = null;
	}
	
	@SubscribeEvent
	public static void onTagsUpdated(TagsUpdatedEvent event)
	{
		if(event.getUpdateCause() == TagsUpdatedEvent.UpdateCause.SERVER_DATA_LOAD)
		{
			if(recipes != null)
				run(recipes);
			else
				LOGGER.error("Failed to access recipe manager for grist cost generation!");
		}
	}
	
	private static void run(RecipeManager recipes)
	{
		GeneratorProcess process = new GeneratorProcess();
		
		//Collect providers
		Stream<GristCostRecipe> stream = recipes.getRecipes().stream().filter(recipe -> recipe instanceof GristCostRecipe).map(recipe -> (GristCostRecipe) recipe);
		for(GristCostRecipe recipe : stream.sorted(Comparator.comparingInt(value -> -value.getPriority())).toList())
		{
			recipe.addCostProvider((item, provider) ->
			{
				process.providersByItem.computeIfAbsent(item, i -> new ArrayList<>()).add(provider);
				process.providers.add(provider);
			});
		}
		
		LOGGER.debug("Starting grist cost generation");
		//Iterate through items
		for(Item item : process.providersByItem.keySet())
		{
			lookupCost(process, new GenerationContext(item, (context1) -> lookupCost(process, context1)));
		}
		
		for(GeneratedCostProvider provider : process.providers)
		{
			try
			{
				provider.build();
			} catch(Exception e)
			{
				LOGGER.error("Got exception while building generated cost provider {}:", provider, e);
			}
		}
		LOGGER.debug("Finished grist cost generation");
	}
	
	private static GristSet lookupCost(GeneratorProcess process, GenerationContext context)
	{
		Item item = context.getCurrentItem();
		GristCostResult cost = null;
		List<GeneratedCostProvider> providers = process.providersByItem.getOrDefault(item, Collections.emptyList());
		for(GeneratedCostProvider provider : providers)
		{
			try
			{
				cost = provider.generate(item, cost, context);
			} catch(Exception e)
			{
				LOGGER.error("Got exception from generated cost provider {} while generating for item {}:", provider, item, e);
			}
		}
		
		if(providers.isEmpty() && MinestuckConfig.COMMON.logIngredientItemsWithoutCosts.get())
			LOGGER.info("Item {} was looked up, but it did not have any grist costs or recipes.", item.getRegistryName());
		
		return cost != null ? cost.getCost() : null;
	}
	
	private static class GeneratorProcess
	{
		private final Map<Item, List<GeneratedCostProvider>> providersByItem = new HashMap<>();
		private final Set<GeneratedCostProvider> providers = new HashSet<>();
	}
}