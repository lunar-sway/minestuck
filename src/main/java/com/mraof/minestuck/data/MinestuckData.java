package com.mraof.minestuck.data;

import com.google.gson.JsonElement;
import com.mojang.serialization.JsonOps;
import com.mraof.minestuck.Minestuck;
import com.mraof.minestuck.data.loot_table.MSLootModifiers;
import com.mraof.minestuck.data.loot_table.MinestuckLootTableProvider;
import com.mraof.minestuck.data.recipe.MinestuckCombinationsProvider;
import com.mraof.minestuck.data.recipe.MinestuckGristCostsProvider;
import com.mraof.minestuck.data.recipe.MinestuckRecipeProvider;
import com.mraof.minestuck.data.tag.*;
import com.mraof.minestuck.data.worldgen.*;
import net.minecraft.core.RegistryAccess;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.tags.BlockTagsProvider;
import net.minecraft.resources.RegistryOps;
import net.minecraftforge.common.data.ExistingFileHelper;
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
		ExistingFileHelper fileHelper = event.getExistingFileHelper();
		RegistryAccess.Writable registryAccess = RegistryAccess.builtinCopy();
		RegistryOps<JsonElement> registryOps = RegistryOps.create(JsonOps.INSTANCE, registryAccess);
		
		gen.addProvider(event.includeServer(), MSPlacedFeatureProvider.create(registryAccess, gen, fileHelper));
		gen.addProvider(event.includeServer(), MSBiomeProvider.create(gen, fileHelper));
		gen.addProvider(event.includeServer(), MSStructureProvider.create(registryAccess, gen, fileHelper));
		gen.addProvider(event.includeServer(), MSStructureSetProvider.create(registryAccess, gen, fileHelper));
		gen.addProvider(event.includeServer(), BiomeModifierProvider.create(gen, fileHelper, registryOps));
		
		BlockTagsProvider blockTags = new MinestuckBlockTagsProvider(gen, fileHelper);
		gen.addProvider(event.includeServer(), blockTags);
		gen.addProvider(event.includeServer(), new MinestuckItemTagsProvider(gen, blockTags, fileHelper));
		gen.addProvider(event.includeServer(), new MinestuckFluidTagsProvider(gen, fileHelper));
		gen.addProvider(event.includeServer(), new MinestuckEntityTypeTagsProvider(gen, fileHelper));
		gen.addProvider(event.includeServer(), new MinestuckBiomeTagsProvider(gen, fileHelper));
		gen.addProvider(event.includeServer(), new MSStructureTagsProvider(gen, fileHelper));
		gen.addProvider(event.includeServer(), new MSGristTypeTagsProvider(gen, fileHelper));
		gen.addProvider(event.includeServer(), new TerrainLandTypeTagsProvider(gen, fileHelper));
		gen.addProvider(event.includeServer(), new TitleLandTypeTagsProvider(gen, fileHelper));
		
		gen.addProvider(event.includeServer(), new MinestuckRecipeProvider(gen));
		gen.addProvider(event.includeServer(), new MinestuckGristCostsProvider(gen));
		gen.addProvider(event.includeServer(), new MinestuckCombinationsProvider(gen));
		gen.addProvider(event.includeServer(), new GeneratedGristCostConfigProvider(gen, Minestuck.MOD_ID));
		
		gen.addProvider(event.includeServer(), new BoondollarPricingProvider(gen, Minestuck.MOD_ID));
		gen.addProvider(event.includeServer(), new MinestuckLootTableProvider(gen));
		gen.addProvider(event.includeServer(), new MSLootModifiers(gen));
		gen.addProvider(event.includeServer(), new MSAdvancementProvider(gen, fileHelper));
		
		gen.addProvider(event.includeServer(), new StartingModusProvider(gen, Minestuck.MOD_ID));
		
		gen.addProvider(event.includeClient(), new MinestuckEnUsLanguageProvider(gen));
	}
}