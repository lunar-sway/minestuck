package com.mraof.minestuck.world.lands.title;

import com.mraof.minestuck.player.EnumAspect;
import com.mraof.minestuck.util.MSSoundEvents;
import com.mraof.minestuck.world.gen.feature.structure.blocks.StructureBlockRegistry;
import com.mraof.minestuck.world.lands.terrain.TerrainLandType;
import net.minecraft.block.Blocks;
import net.minecraft.block.material.Material;
import net.minecraft.util.SoundEvent;

public class FrogsLandType extends TitleLandType
{
	public static final String FROGS = "minestuck.frogs";
	
	public FrogsLandType()
	{
		super(EnumAspect.SPACE, false);
	}
	
	@Override
	public String[] getNames()
	{
		return new String[]{FROGS};
	}
	
	@Override
	public void registerBlocks(StructureBlockRegistry registry)
	{
		registry.setBlockState("structure_wool_2", Blocks.GREEN_WOOL.defaultBlockState());
		registry.setBlockState("carpet", Blocks.LIME_CARPET.defaultBlockState());
	}
	/*
	@Override
	public void setBiomeSettings(LandWrapperBiome biome, StructureBlockRegistry blockRegistry)
	{
		if(biome.type == BiomeType.OCEAN)
		{
			biome.addFeature(GenerationStage.Decoration.VEGETAL_DECORATION, Feature.RANDOM_PATCH.withConfiguration(DefaultBiomeFeatures.LILY_PAD_CONFIG).withPlacement(Placement.COUNT_HEIGHTMAP_DOUBLE.configure(new FrequencyConfig(1))));
		}
		
		if(biome.type == BiomeType.NORMAL)
		{
			biome.addSpawn(EntityClassification.CREATURE, new Biome.SpawnListEntry(MSEntityTypes.FROG, 1, 0, 1));
		}
		
		if(biome.type == BiomeType.ROUGH)
		{
			biome.addSpawn(EntityClassification.CREATURE, new Biome.SpawnListEntry(MSEntityTypes.FROG, 10, 1, 6));
		}
	}
	*/
	@Override
	public boolean isAspectCompatible(TerrainLandType aspect)
	{
		StructureBlockRegistry registry = new StructureBlockRegistry();
		aspect.registerBlocks(registry);
		return registry.getBlockState("ocean").getMaterial() != Material.LAVA;
	}
	
	@Override
	public SoundEvent getBackgroundMusic()
	{
		return MSSoundEvents.MUSIC_FROGS;
	}
}