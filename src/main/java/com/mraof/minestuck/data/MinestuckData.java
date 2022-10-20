package com.mraof.minestuck.data;

import com.mraof.minestuck.Minestuck;
import com.mraof.minestuck.data.loot_table.MinestuckLootTableProvider;
import com.mraof.minestuck.data.recipe.MinestuckCombinationsProvider;
import com.mraof.minestuck.data.recipe.MinestuckGristCostsProvider;
import com.mraof.minestuck.data.recipe.MinestuckRecipeProvider;
import com.mraof.minestuck.data.tag.*;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.tags.BlockTagsProvider;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.forge.event.lifecycle.GatherDataEvent;

@Mod.EventBusSubscriber(modid = Minestuck.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class MinestuckData
{
	@SubscribeEvent
	public static void gatherData(GatherDataEvent event)
	{
		DataGenerator gen = event.getGenerator();
		
		if(event.includeServer())
		{
			BlockTagsProvider blockTags = new MinestuckBlockTagsProvider(gen, event.getExistingFileHelper());
			gen.addProvider(blockTags);
			gen.addProvider(new MinestuckItemTagsProvider(gen, blockTags, event.getExistingFileHelper()));
			gen.addProvider(new MinestuckFluidTagsProvider(gen, event.getExistingFileHelper()));
			gen.addProvider(new MinestuckEntityTypeTagsProvider(gen, event.getExistingFileHelper()));
			gen.addProvider(new MinestuckBiomeTagsProvider(gen, event.getExistingFileHelper()));
			gen.addProvider(new MSStructureTagsProvider(gen, event.getExistingFileHelper()));
			gen.addProvider(new MSGristTypeTagsProvider(gen, event.getExistingFileHelper()));
			
			gen.addProvider(new MinestuckRecipeProvider(gen));
			gen.addProvider(new MinestuckGristCostsProvider(gen));
			gen.addProvider(new MinestuckCombinationsProvider(gen));
			gen.addProvider(new GeneratedGristCostConfigProvider(gen, Minestuck.MOD_ID));
			
			gen.addProvider(new BoondollarPricingProvider(gen, Minestuck.MOD_ID));
			gen.addProvider(new MinestuckLootTableProvider(gen));
			gen.addProvider(new MSAdvancementProvider(gen, event.getExistingFileHelper()));
			gen.addProvider(new MinestuckEnUsLanguageProvider(gen));
			
			gen.addProvider(new MinestuckBiomeProvider(gen));
			
			gen.addProvider(new StartingModusProvider(gen, Minestuck.MOD_ID));
		}
	}
}