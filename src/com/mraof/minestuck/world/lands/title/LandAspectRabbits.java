package com.mraof.minestuck.world.lands.title;

import com.mraof.minestuck.MinestuckConfig;
import com.mraof.minestuck.entity.EntityFrog;
import com.mraof.minestuck.entity.EntityRabbitMedium;
import com.mraof.minestuck.world.biome.BiomeMinestuck;
import com.mraof.minestuck.world.lands.decorator.structure.RabbitHoleDecorator;
import com.mraof.minestuck.world.lands.gen.ChunkProviderLands;
import com.mraof.minestuck.world.lands.structure.blocks.StructureBlockRegistry;
import com.mraof.minestuck.world.lands.terrain.TerrainLandAspect;
import net.minecraft.block.BlockColored;
import net.minecraft.block.material.Material;
import net.minecraft.entity.passive.EntityRabbit;
import net.minecraft.init.Blocks;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.world.biome.Biome;

public class LandAspectRabbits extends TitleLandAspect
{
	
	@Override
	public String getPrimaryName()
	{
		return "rabbits";
	}
	
	@Override
	public String[] getNames()
	{
		return new String[] {"rabbit", "bunny"};
	}
	
	@Override
	public void prepareChunkProviderServer(ChunkProviderLands chunkProvider)
	{
		chunkProvider.blockRegistry.setBlockState("structure_wool_2", Blocks.WOOL.getDefaultState().withProperty(BlockColored.COLOR, EnumDyeColor.PINK));
		chunkProvider.blockRegistry.setBlockState("carpet", Blocks.CARPET.getDefaultState().withProperty(BlockColored.COLOR, EnumDyeColor.SILVER));

		chunkProvider.monsterList.add(new Biome.SpawnListEntry(EntityRabbitMedium.class, 10, MinestuckConfig.landAnimalSpawnAmount - 4, MinestuckConfig.landAnimalSpawnAmount));

		chunkProvider.decorators.add(new RabbitHoleDecorator(BiomeMinestuck.mediumNormal, BiomeMinestuck.mediumRough));
		chunkProvider.sortDecorators();
	}
	
	@Override
	public boolean isAspectCompatible(TerrainLandAspect aspect)
	{
		StructureBlockRegistry registry = new StructureBlockRegistry();
		aspect.registerBlocks(registry);
		return registry.getBlockState("ocean").getMaterial() != Material.LAVA;
	}
	
}