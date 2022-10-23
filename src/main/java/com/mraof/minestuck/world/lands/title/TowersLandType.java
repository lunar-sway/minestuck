package com.mraof.minestuck.world.lands.title;

import com.mraof.minestuck.player.EnumAspect;
import com.mraof.minestuck.util.MSSoundEvents;
import com.mraof.minestuck.world.biome.LandBiomeSetType;
import com.mraof.minestuck.world.biome.LandBiomeType;
import com.mraof.minestuck.world.gen.feature.FeatureModifier;
import com.mraof.minestuck.world.gen.feature.MSPlacedFeatures;
import com.mraof.minestuck.world.gen.structure.blocks.StructureBlockRegistry;
import com.mraof.minestuck.world.lands.LandBiomeGenBuilder;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.levelgen.GenerationStep;

public class TowersLandType extends TitleLandType
{
	public static final String TOWERS = "minestuck.towers";
	
	public TowersLandType()
	{
		super(EnumAspect.HOPE);
	}
	
	@Override
	public String[] getNames()
	{
		return new String[] {TOWERS};
	}
	
	@Override
	public void registerBlocks(StructureBlockRegistry registry)
	{
		registry.setBlock("structure_wool_2", Blocks.LIGHT_BLUE_WOOL);
		registry.setBlock("carpet", Blocks.YELLOW_CARPET);
	}
	
	@Override
	public void addBiomeGeneration(LandBiomeGenBuilder builder, StructureBlockRegistry blocks, LandBiomeSetType biomeSet)
	{
		builder.addFeature(GenerationStep.Decoration.SURFACE_STRUCTURES, MSPlacedFeatures.TOWER, LandBiomeType.anyExcept(LandBiomeType.OCEAN));
		
		builder.addModified(GenerationStep.Decoration.LOCAL_MODIFICATIONS, MSPlacedFeatures.LARGE_PILLAR, FeatureModifier.withState(blocks.getBlockState("structure_primary")), LandBiomeType.ROUGH);
		
	}
	
	@Override
	public SoundEvent getBackgroundMusic()
	{
		return MSSoundEvents.MUSIC_TOWERS.get();
	}
}