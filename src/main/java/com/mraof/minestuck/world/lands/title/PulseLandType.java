package com.mraof.minestuck.world.lands.title;

import com.mraof.minestuck.block.MSBlocks;
import com.mraof.minestuck.util.MSSoundEvents;
import com.mraof.minestuck.util.MSTags;
import com.mraof.minestuck.world.biome.LandBiomeSetType;
import com.mraof.minestuck.world.biome.LandBiomeType;
import com.mraof.minestuck.world.gen.LandGenSettings;
import com.mraof.minestuck.world.gen.feature.FeatureModifier;
import com.mraof.minestuck.world.gen.feature.MSPlacedFeatures;
import com.mraof.minestuck.world.gen.structure.blocks.StructureBlockRegistry;
import com.mraof.minestuck.world.lands.LandBiomeGenBuilder;
import com.mraof.minestuck.world.lands.LandProperties;
import com.mraof.minestuck.world.lands.terrain.TerrainLandType;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.blockpredicates.BlockPredicate;
import net.minecraft.world.phys.Vec3;

public class PulseLandType extends TitleLandType
{
	public static final String PULSE = "minestuck.pulse";
	public static final String BLOOD = "minestuck.blood";
	
	public PulseLandType()
	{
	}
	
	@Override
	public String[] getNames()
	{
		return new String[]{PULSE, BLOOD};
	}
	
	@Override
	public void registerBlocks(StructureBlockRegistry registry)
	{
		registry.setBlock("structure_wool_2", Blocks.RED_WOOL);
		registry.setBlock("carpet", Blocks.BROWN_CARPET);
		
		registry.setBlock("ocean", MSBlocks.BLOOD);
		registry.setBlock("river", MSBlocks.BLOOD);
		registry.setBlock("slime", MSBlocks.COAGULATED_BLOOD);
	}
	
	@Override
	public void setProperties(LandProperties properties)
	{
		properties.mergeFogColor(new Vec3(0.8, 0, 0), 0.8F);
	}
	
	@Override
	public void setGenSettings(LandGenSettings settings)
	{
		settings.oceanThreshold = Math.max(settings.oceanThreshold, -0.4F);
	}
	
	@Override
	public void addBiomeGeneration(LandBiomeGenBuilder builder, StructureBlockRegistry blocks, LandBiomeSetType biomeSet)
	{
		builder.addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, MSPlacedFeatures.COAGULATED_BLOOD_DISK,
				FeatureModifier.withTargets(BlockPredicate.matchesBlocks(blocks.getBlockState("surface").getBlock(), blocks.getBlockState("upper").getBlock())), LandBiomeType.ROUGH);
		builder.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, MSPlacedFeatures.BLOOD_TREE, LandBiomeType.anyExcept(LandBiomeType.OCEAN));
		
	}
	
	@Override
	public boolean isAspectCompatible(TerrainLandType otherType)
	{
		return !otherType.is(MSTags.TerrainLandTypes.IS_FLUID_IMPORTANT);
	}
	
	@Override
	public SoundEvent getBackgroundMusic()
	{
		return MSSoundEvents.MUSIC_PULSE.get();
	}
}