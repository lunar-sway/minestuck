package com.mraof.minestuck.data;

import com.mraof.minestuck.Minestuck;
import com.mraof.minestuck.data.loot_table.MSLootModifiers;
import com.mraof.minestuck.data.loot_table.MinestuckLootTableProvider;
import com.mraof.minestuck.data.recipe.MinestuckCombinationsProvider;
import com.mraof.minestuck.data.recipe.MinestuckGristCostsProvider;
import com.mraof.minestuck.data.recipe.MinestuckRecipeProvider;
import com.mraof.minestuck.data.tag.*;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.tags.BlockTagsProvider;
import net.minecraftforge.data.event.GatherDataEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = Minestuck.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class MinestuckData
{
	@SubscribeEvent
	public static void gatherData(GatherDataEvent event)
	{
		DataGenerator gen = event.getGenerator();
		
		gen.addProvider(event.includeServer(), new MinestuckBiomeProvider(gen, event.getExistingFileHelper()));
		
		BlockTagsProvider blockTags = new MinestuckBlockTagsProvider(gen, event.getExistingFileHelper());
		gen.addProvider(event.includeServer(), blockTags);
		gen.addProvider(event.includeServer(), new MinestuckItemTagsProvider(gen, blockTags, event.getExistingFileHelper()));
		gen.addProvider(event.includeServer(), new MinestuckFluidTagsProvider(gen, event.getExistingFileHelper()));
		gen.addProvider(event.includeServer(), new MinestuckEntityTypeTagsProvider(gen, event.getExistingFileHelper()));
		gen.addProvider(event.includeServer(), new MinestuckBiomeTagsProvider(gen, event.getExistingFileHelper()));
		gen.addProvider(event.includeServer(), new MSStructureTagsProvider(gen, event.getExistingFileHelper()));
		gen.addProvider(event.includeServer(), new MSGristTypeTagsProvider(gen, event.getExistingFileHelper()));
		
		gen.addProvider(event.includeServer(), new MinestuckRecipeProvider(gen));
		gen.addProvider(event.includeServer(), new MinestuckGristCostsProvider(gen));
		gen.addProvider(event.includeServer(), new MinestuckCombinationsProvider(gen));
		gen.addProvider(event.includeServer(), new GeneratedGristCostConfigProvider(gen, Minestuck.MOD_ID));
		
		gen.addProvider(event.includeServer(), new BoondollarPricingProvider(gen, Minestuck.MOD_ID));
		gen.addProvider(event.includeServer(), new MinestuckLootTableProvider(gen));
		gen.addProvider(event.includeServer(), new MSLootModifiers(gen));
		gen.addProvider(event.includeServer(), new MSAdvancementProvider(gen, event.getExistingFileHelper()));
		
		gen.addProvider(event.includeServer(), new StartingModusProvider(gen, Minestuck.MOD_ID));
		
		gen.addProvider(event.includeClient(), new MinestuckEnUsLanguageProvider(gen));
	}
}