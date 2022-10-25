package com.mraof.minestuck.world.lands.title;

import com.mraof.minestuck.player.EnumAspect;
import com.mraof.minestuck.util.MSSoundEvents;
import com.mraof.minestuck.world.biome.LandBiomeSetType;
import com.mraof.minestuck.world.biome.LandBiomeType;
import com.mraof.minestuck.world.gen.feature.MSPlacedFeatures;
import com.mraof.minestuck.world.gen.structure.blocks.StructureBlockRegistry;
import com.mraof.minestuck.world.lands.LandBiomeGenBuilder;
import com.mraof.minestuck.world.lands.terrain.TerrainLandType;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.material.Material;

public class RabbitsLandType extends TitleLandType
{
	public static final String RABBITS = "minestuck.rabbits";
	public static final String BUNNIES = "minestuck.bunnies";
	
	public RabbitsLandType()
	{
		super(EnumAspect.LIFE);
	}
	
	@Override
	public String[] getNames()
	{
		return new String[] {RABBITS, BUNNIES};
	}
	
	@Override
	public void registerBlocks(StructureBlockRegistry registry)
	{
		registry.setBlock("structure_wool_2", Blocks.PINK_WOOL);
		registry.setBlock("carpet", Blocks.LIGHT_GRAY_CARPET);
	}
	
	@Override
	public void addBiomeGeneration(LandBiomeGenBuilder builder, StructureBlockRegistry blocks, LandBiomeSetType biomeSet)
	{
		builder.addFeature(GenerationStep.Decoration.TOP_LAYER_MODIFICATION, MSPlacedFeatures.RABBIT_PLACEMENT, LandBiomeType.NORMAL);
		builder.addFeature(GenerationStep.Decoration.TOP_LAYER_MODIFICATION, MSPlacedFeatures.SMALL_RABBIT_PLACEMENT, LandBiomeType.ROUGH);
	}
	
	@Override
	public boolean isAspectCompatible(TerrainLandType otherType)
	{
		StructureBlockRegistry registry = new StructureBlockRegistry();
		otherType.registerBlocks(registry);
		return registry.getBlockState("ocean").getMaterial() != Material.LAVA;
	}
	
	@Override
	public SoundEvent getBackgroundMusic()
	{
		return MSSoundEvents.MUSIC_RABBITS.get();
	}
}