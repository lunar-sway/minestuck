package com.mraof.minestuck.alchemy.recipe.generator;

import com.mraof.minestuck.Minestuck;
import com.mraof.minestuck.MinestuckConfig;
import com.mraof.minestuck.api.alchemy.GristSet;
import com.mraof.minestuck.api.alchemy.recipe.GristCostRecipe;
import com.mraof.minestuck.api.alchemy.recipe.generator.GeneratedCostProvider;
import com.mraof.minestuck.api.alchemy.recipe.generator.GristCostResult;
import it.unimi.dsi.fastutil.objects.Object2IntMap;
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.RecipeManager;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.event.AddReloadListenerEvent;
import net.neoforged.neoforge.event.TagsUpdatedEvent;
import net.neoforged.neoforge.event.server.ServerStoppedEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.*;

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
	
	private static void run(RecipeManager recipeManager)
	{
		long startTime = System.currentTimeMillis();
		LOGGER.debug("Starting grist cost generation");
		
		GeneratorProcess process = new GeneratorProcess();
		
		//Collect providers
		List<RecipeHolder<GristCostRecipe>> recipeHolders = recipeManager.getAllRecipesFor(GristCostRecipe.RECIPE_TYPE.get());
		for(RecipeHolder<GristCostRecipe> holder : recipeHolders.stream().sorted(Comparator.comparingInt(holder -> -holder.value().getPriority())).toList())
		{
			holder.value().addCostProvider((item, provider) ->
			{
				process.providersByItem.computeIfAbsent(item, i -> new ArrayList<>()).add(provider);
				process.providers.add(provider);
			}, holder.id());
		}
		
		Object2IntMap<Item> lookupCount = new Object2IntOpenHashMap<>();
		
		for(GeneratedCostProvider provider : process.providers)
		{
			provider.reportPreliminaryLookups(item -> lookupCount.mergeInt(item, 1, Integer::sum));
		}
		
		//Iterate through items
		process.providersByItem.keySet().stream()
				.sorted(Comparator.comparingInt(item -> -lookupCount.getInt(item)))
				.forEach(item -> {
					long itemStartTime = System.currentTimeMillis();
					
					lookupCost(process, new GenerationContext(item, (context1) -> lookupCost(process, context1)));
					
					double itemTime = (System.currentTimeMillis() - itemStartTime)/1000D;
					if(itemTime > 0.5)
						LOGGER.warn("Cost generation for {} took {}s", BuiltInRegistries.ITEM.getKey(item), itemTime);
				});
		
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
		
		LOGGER.info("Finished grist cost generation in {}s", (System.currentTimeMillis() - startTime)/1000D);
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
				if(cost == null)
					cost = provider.generate(item, context);
				else
					provider.onCostFromOtherRecipe(item, cost, context);
			} catch(Exception e)
			{
				LOGGER.error("Got exception from generated cost provider {} while generating for item {}:", provider, item, e);
			}
		}
		
		if(providers.isEmpty() && MinestuckConfig.COMMON.logIngredientItemsWithoutCosts.get())
			LOGGER.info("Item {} was looked up, but it did not have any grist costs or recipes.", BuiltInRegistries.ITEM.getKey(item));
		
		return cost != null ? cost.cost() : null;
	}
	
	private static class GeneratorProcess
	{
		private final Map<Item, List<GeneratedCostProvider>> providersByItem = new HashMap<>();
		private final Set<GeneratedCostProvider> providers = new HashSet<>();
	}
}