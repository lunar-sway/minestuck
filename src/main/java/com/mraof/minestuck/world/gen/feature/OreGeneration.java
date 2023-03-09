package com.mraof.minestuck.world.gen.feature;

import com.mraof.minestuck.Minestuck;
import com.mraof.minestuck.MinestuckConfig;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = Minestuck.MOD_ID)
public class OreGeneration
{
	public static final int cruxiteVeinsPerChunk = 21;
	public static final int uraniumVeinsPerChunk = 13;
	public static final int baseCruxiteVeinSize = 6;
	public static final int baseUraniumVeinSize = 5;
	public static final int bonusCruxiteVeinSize = 3;
	public static final int bonusUraniumVeinSize = 3;
	public static final int cruxiteStratumMax = 128; //1.18 copper distribution + 16
	public static final int cruxiteStratumMin = 0;
	public static final int uraniumStratumMaxAboveBottom = 96; //similar to 1.18 diamond ore distribution
	public static final int uraniumStratumMinAboveBottom = -60;
	
	/*TODO biome modifiers
	@SubscribeEvent(priority = EventPriority.HIGH)
	public static void onBiomeLoad(BiomeLoadingEvent event)
	{
		ResourceLocation name = event.getName();
		if(name != null && BiomeDictionary.hasType(ResourceKey.create(Registry.BIOME_REGISTRY, name), BiomeDictionary.Type.OVERWORLD))
		{
			if(MinestuckConfig.SERVER.generateCruxiteOre.get())
			{
				event.getGeneration().addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, MSPlacedFeatures.CRUXITE_ORE.getHolder().orElseThrow());
			}
			
			if(MinestuckConfig.SERVER.generateUraniumOre.get())
			{
				event.getGeneration().addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, MSPlacedFeatures.URANIUM_ORE.getHolder().orElseThrow());
			}
		}
	}
	 */
}