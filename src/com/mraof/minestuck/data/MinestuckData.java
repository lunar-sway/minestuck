package com.mraof.minestuck.data;

import com.mraof.minestuck.Minestuck;
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
			
			gen.addProvider(new MinestuckAdvancementProvider(gen));
		}
	}
}