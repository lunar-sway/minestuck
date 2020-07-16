package com.mraof.minestuck.data;

import com.mraof.minestuck.Minestuck;
import com.mraof.minestuck.data.loot_table.MinestuckLootTableProvider;
import com.mraof.minestuck.data.recipe.MinestuckCombinationsProvider;
import com.mraof.minestuck.data.recipe.MinestuckGristCostsProvider;
import com.mraof.minestuck.data.recipe.MinestuckRecipeProvider;
import net.minecraft.data.DataGenerator;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.GatherDataEvent;

@Mod.EventBusSubscriber(modid = Minestuck.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class MinestuckData
{
	@SubscribeEvent
	public static void gatherData(GatherDataEvent event)
	{
		DataGenerator gen = event.getGenerator();
		
		if(event.includeServer())
		{
			gen.addProvider(new MinestuckBlockTagsProvider(gen));
			gen.addProvider(new MinestuckItemTagsProvider(gen));
			gen.addProvider(new MinestuckFluidTagsProvider(gen));
			gen.addProvider(new MinestuckEntityTypeTagsProvider(gen));
			
			gen.addProvider(new MinestuckRecipeProvider(gen));
			gen.addProvider(new MinestuckGristCostsProvider(gen));
			gen.addProvider(new MinestuckCombinationsProvider(gen));
			gen.addProvider(new GeneratedGristCostConfigProvider(gen, Minestuck.MOD_ID));
			
			gen.addProvider(new MinestuckLootTableProvider(gen));
			gen.addProvider(new MSAvancementProvider(gen));
			gen.addProvider(new MinestuckEnUsLanguageProvider(gen));
			gen.addProvider(new MinestuckFrFrLanguageProvider(gen));
		}
	}
}