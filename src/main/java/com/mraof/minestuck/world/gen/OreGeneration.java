package com.mraof.minestuck.world.gen;

import com.mraof.minestuck.Minestuck;
import com.mraof.minestuck.MinestuckConfig;
import com.mraof.minestuck.block.MSBlocks;
import net.minecraft.util.RegistryKey;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.gen.GenerationStage;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.OreFeatureConfig;
import net.minecraftforge.common.BiomeDictionary;
import net.minecraftforge.event.world.BiomeLoadingEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = Minestuck.MOD_ID)
public class OreGeneration
{
	public static final int cruxiteVeinsPerChunk = 10;
	public static final int uraniumVeinsPerChunk = 5;
	public static final int baseCruxiteVeinSize = 6;
	public static final int baseUraniumVeinSize = 5;
	public static final int bonusCruxiteVeinSize = 3;
	public static final int bonusUraniumVeinSize = 3;
	public static final int cruxiteStratumMax = 60;
	public static final int uraniumStratumMax = 35;
	
	@SubscribeEvent(priority = EventPriority.HIGH)
	public static void onBiomeLoad(BiomeLoadingEvent event)
	{
		ResourceLocation name = event.getName();
		if(name != null && BiomeDictionary.hasType(RegistryKey.create(Registry.BIOME_REGISTRY, name), BiomeDictionary.Type.OVERWORLD))
		{
			if(MinestuckConfig.SERVER.generateCruxiteOre.get())
			{
				event.getGeneration().addFeature(GenerationStage.Decoration.UNDERGROUND_ORES, Feature.ORE.configured(new OreFeatureConfig(OreFeatureConfig.FillerBlockType.NATURAL_STONE, MSBlocks.STONE_CRUXITE_ORE.defaultBlockState(), baseCruxiteVeinSize))
						.range(cruxiteStratumMax).squared().count(cruxiteVeinsPerChunk));
			}
			if(MinestuckConfig.SERVER.generateUraniumOre.get())
			{
				event.getGeneration().addFeature(GenerationStage.Decoration.UNDERGROUND_ORES, Feature.ORE.configured(new OreFeatureConfig(OreFeatureConfig.FillerBlockType.NATURAL_STONE, MSBlocks.STONE_URANIUM_ORE.defaultBlockState(), baseUraniumVeinSize))
						.range(uraniumStratumMax).squared().count(uraniumVeinsPerChunk));
			}
		}
	}
}