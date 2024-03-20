package com.mraof.minestuck.data;

import com.mraof.minestuck.Minestuck;
import com.mraof.minestuck.data.dialogue.ConsortDialogue;
import com.mraof.minestuck.data.dialogue.ConsortFoodMerchantDialogue;
import com.mraof.minestuck.data.dialogue.ConsortGeneralMerchantDialogue;
import com.mraof.minestuck.data.dialogue.ShadyConsortDialogue;
import com.mraof.minestuck.data.loot_table.MSLootModifiers;
import com.mraof.minestuck.data.loot_table.MinestuckLootTableProvider;
import com.mraof.minestuck.data.recipe.MinestuckRecipeProvider;
import com.mraof.minestuck.data.tag.*;
import com.mraof.minestuck.data.worldgen.*;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.RegistrySetBuilder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.minecraftforge.common.data.DatapackBuiltinEntriesProvider;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.data.event.GatherDataEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.Set;
import java.util.concurrent.CompletableFuture;

@Mod.EventBusSubscriber(modid = Minestuck.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public final class MinestuckData
{
	@SubscribeEvent
	public static void gatherData(GatherDataEvent event)
	{
		DataGenerator gen = event.getGenerator();
		PackOutput output = gen.getPackOutput();
		CompletableFuture<HolderLookup.Provider> lookupProvider = event.getLookupProvider().thenApply(provider -> {
			return registrySetBuilder().buildPatch(RegistryAccess.fromRegistryOfRegistries(BuiltInRegistries.REGISTRY), provider);
		});
		ExistingFileHelper fileHelper = event.getExistingFileHelper();
		
		gen.addProvider(event.includeServer(), new DatapackBuiltinEntriesProvider(output, lookupProvider, Set.of(Minestuck.MOD_ID)));
		
		var blockTags = gen.addProvider(event.includeServer(), new MinestuckBlockTagsProvider(output, lookupProvider, fileHelper));
		gen.addProvider(event.includeServer(), new MinestuckItemTagsProvider(output, lookupProvider, blockTags.contentsGetter(), fileHelper));
		gen.addProvider(event.includeServer(), new MinestuckFluidTagsProvider(output, lookupProvider, fileHelper));
		gen.addProvider(event.includeServer(), new MinestuckEntityTypeTagsProvider(output, lookupProvider, fileHelper));
		gen.addProvider(event.includeServer(), new MSEffectTagsProvider(output, lookupProvider, fileHelper));
		gen.addProvider(event.includeServer(), new MinestuckBiomeTagsProvider(output, lookupProvider, fileHelper));
		gen.addProvider(event.includeServer(), new MSStructureTagsProvider(output, lookupProvider, fileHelper));
		gen.addProvider(event.includeServer(), new MSGristTypeTagsProvider(output, lookupProvider, fileHelper));
		gen.addProvider(event.includeServer(), new TerrainLandTypeTagsProvider(output, lookupProvider, fileHelper));
		gen.addProvider(event.includeServer(), new TitleLandTypeTagsProvider(output, lookupProvider, fileHelper));
		gen.addProvider(event.includeServer(), new MSDamageTypeProvider.Tags(output, lookupProvider, fileHelper));
		
		gen.addProvider(event.includeServer(), new MinestuckRecipeProvider(output));
		gen.addProvider(event.includeServer(), new GeneratedGristCostConfigProvider(output, Minestuck.MOD_ID));
		
		gen.addProvider(event.includeServer(), new BoondollarPricingProvider(output, Minestuck.MOD_ID));
		gen.addProvider(event.includeServer(), MinestuckLootTableProvider.create(output));
		gen.addProvider(event.includeServer(), new MSLootModifiers(output));
		gen.addProvider(event.includeServer(), MSAdvancementProvider.create(output, lookupProvider, fileHelper));
		
		gen.addProvider(event.includeServer(), new StartingModusProvider(output, Minestuck.MOD_ID));
		
		gen.addProvider(event.includeClient(), new MSBlockStateProvider(output, fileHelper));
		gen.addProvider(event.includeClient(), new MinestuckItemModelProvider(output, fileHelper));
		var enUsLanguageProvider = gen.addProvider(event.includeClient(), new MinestuckEnUsLanguageProvider(output));
		
		gen.addProvider(event.includeServer(), ConsortDialogue.create(output, enUsLanguageProvider));
		gen.addProvider(event.includeServer(), ShadyConsortDialogue.create(output, enUsLanguageProvider));
		gen.addProvider(event.includeServer(), ConsortFoodMerchantDialogue.create(output, enUsLanguageProvider));
		gen.addProvider(event.includeServer(), ConsortGeneralMerchantDialogue.create(output, enUsLanguageProvider));
	}
	
	private static RegistrySetBuilder registrySetBuilder()
	{
		return new RegistrySetBuilder()
				.add(Registries.NOISE, MSNoiseParametersProvider::register)
				.add(Registries.DENSITY_FUNCTION, MSDensityFunctionProvider::register)
				.add(Registries.CONFIGURED_FEATURE, MSConfiguredFeatureProvider::register)
				.add(Registries.PLACED_FEATURE, MSPlacedFeatureProvider::register)
				.add(Registries.BIOME, MSBiomeProvider::register)
				.add(Registries.STRUCTURE, MSStructureProvider::register)
				.add(Registries.STRUCTURE_SET, MSStructureSetProvider::register)
				.add(Registries.DAMAGE_TYPE, MSDamageTypeProvider::register)
				.add(ForgeRegistries.Keys.BIOME_MODIFIERS, BiomeModifierProvider::register);
	}
}