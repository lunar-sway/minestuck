package com.mraof.minestuck.world.lands.title;

import com.mraof.minestuck.util.EnumAspect;
import com.mraof.minestuck.world.biome.LandWrapperBiome;
import com.mraof.minestuck.world.biome.MSBiomes;
import com.mraof.minestuck.world.gen.structure.blocks.StructureBlockRegistry;
import com.mraof.minestuck.world.lands.terrain.TerrainLandAspect;
import net.minecraft.block.Blocks;
import net.minecraft.block.material.Material;
import net.minecraft.entity.EntityClassification;
import net.minecraft.entity.EntityType;
import net.minecraft.world.biome.Biome;

public class RabbitsLandAspect extends TitleLandAspect
{
	public static final String RABBITS = "minestuck.rabbits";
	public static final String BUNNIES = "minestuck.bunnies";
	
	public RabbitsLandAspect()
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
		registry.setBlockState("structure_wool_2", Blocks.PINK_WOOL.getDefaultState());
		registry.setBlockState("carpet", Blocks.LIGHT_GRAY_CARPET.getDefaultState());
	}
	
	@Override
	public void setBiomeGenSettings(LandWrapperBiome biome, StructureBlockRegistry blocks)
	{
		if(biome.staticBiome == MSBiomes.LAND_NORMAL)
			biome.addSpawn(EntityClassification.CREATURE, new Biome.SpawnListEntry(EntityType.RABBIT, 5, 2, 5));
		if(biome.staticBiome == MSBiomes.LAND_ROUGH) //TODO verify that rabbits actually spawn properly in all lands with this spawn method
			biome.addSpawn(EntityClassification.CREATURE, new Biome.SpawnListEntry(EntityType.RABBIT, 3, 1, 3));
	}
	
	@Override
	public boolean isAspectCompatible(TerrainLandAspect aspect)
	{
		StructureBlockRegistry registry = new StructureBlockRegistry();
		aspect.registerBlocks(registry);
		return registry.getBlockState("ocean").getMaterial() != Material.LAVA;
	}
}