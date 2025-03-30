package com.mraof.minestuck.world.lands.title;

import com.mraof.minestuck.block.MSBlocks;
import com.mraof.minestuck.util.MSSoundEvents;
import com.mraof.minestuck.world.biome.LandBiomeSetType;
import com.mraof.minestuck.world.biome.LandBiomeType;
import com.mraof.minestuck.world.gen.feature.FeatureModifier;
import com.mraof.minestuck.world.gen.feature.MSPlacedFeatures;
import com.mraof.minestuck.world.gen.structure.blocks.StructureBlockRegistry;
import com.mraof.minestuck.world.lands.LandBiomeGenBuilder;
import com.mraof.minestuck.world.lands.LandProperties;
import com.mraof.minestuck.world.lands.terrain.TerrainLandType;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.phys.Vec3;

public class LightLandType extends TitleLandType
{
	public static final String LIGHT = "minestuck.light";
	public static final String BRIGHTNESS = "minestuck.brightness";
	
	public LightLandType()
	{
	}
	
	@Override
	public String[] getNames()
	{
		return new String[] {LIGHT, BRIGHTNESS};
	}
	
	@Override
	public void registerBlocks(StructureBlockRegistry registry)
	{
		registry.setBlock("structure_wool_2", Blocks.ORANGE_WOOL);
		registry.setBlock("carpet", Blocks.ORANGE_CARPET);
		registry.setBlock("torch", Blocks.TORCH);
		registry.setBlock("slime", MSBlocks.GLOWY_GOOP);
	}
	
	@Override
	public void setProperties(LandProperties properties)
	{
		properties.skylightBase = 1.0F;
		properties.mergeFogColor(new Vec3(1, 1, 0.8), 0.5F);
	}
	
	@Override
	public void addBiomeGeneration(LandBiomeGenBuilder builder, StructureBlockRegistry blocks, LandBiomeSetType biomeSet)
	{
		BlockState lightBlock = blocks.getBlockState("light_block");
		
		builder.addFeature(GenerationStep.Decoration.LOCAL_MODIFICATIONS, MSPlacedFeatures.MIXED_PILLARS_EXTRA, FeatureModifier.withState(lightBlock), LandBiomeType.ROUGH);
		builder.addFeature(GenerationStep.Decoration.LOCAL_MODIFICATIONS, MSPlacedFeatures.SMALL_PILLAR, FeatureModifier.withState(lightBlock), LandBiomeType.anyExcept(LandBiomeType.ROUGH));
		
		builder.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, MSPlacedFeatures.LIGHT_TREE, LandBiomeType.anyExcept(LandBiomeType.OCEAN));
		
	}
	
	@Override
	public boolean isAspectCompatible(TerrainLandType otherType)
	{
		LandProperties properties = LandProperties.createPartial(otherType);
		
		return otherType.getSkylightBase() >= 1/2F && properties.forceThunder == LandProperties.ForceType.OFF;
	}
	
	@Override
	public SoundEvent getBackgroundMusic()
	{
		return MSSoundEvents.MUSIC_LIGHT.get();
	}
}