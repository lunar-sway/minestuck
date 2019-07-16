package com.mraof.minestuck.world.lands.decorator;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;

import com.mraof.minestuck.world.lands.gen.ChunkProviderLands;
import net.minecraft.world.gen.Heightmap;

public class TallGrassDecorator implements ILandDecorator
{
	
	private List<Biome> biomes;
	private float grassChance;	//Chance for each block to have grass on it
	private float fernChance;	//Chance to place fern instead of grass
	private float doubleGrassChance;	//Chance for each placed grass to be the double-block variant
	
	public TallGrassDecorator(float grassChance, Biome... biomes)
	{
		this(grassChance, 0, biomes);
	}
	
	public TallGrassDecorator(float grassChance, float fernChance, Biome... biomes)
	{
		this(grassChance, fernChance, 0, biomes);
	}
	
	public TallGrassDecorator(float grassChance, float fernChance, float doubleGrassChance, Biome... biomes)
	{
		this.grassChance = grassChance;
		this.fernChance = fernChance;
		this.doubleGrassChance = doubleGrassChance;
		this.biomes = Arrays.asList(biomes);
	}
	
	@Override
	public BlockPos generate(World world, Random random, int chunkX, int chunkZ, ChunkProviderLands provider)
	{
		BlockPos pos = new BlockPos((chunkX << 4) + 8, 0, (chunkZ << 4) + 8);
		for(int x = 0; x < 16; x++)
			for(int z = 0; z < 16; z++)
			{
				BlockPos grassPos = world.getHeight(Heightmap.Type.WORLD_SURFACE, pos.add(x, 0, z));
				if(!biomes.isEmpty() && !biomes.contains(world.getBiomeBody(grassPos)))
					continue;
				
				if(random.nextFloat() < grassChance)
				{
					if(!world.isAirBlock(grassPos))
						continue;
					if(random.nextFloat() < doubleGrassChance)
					{
						/*BlockDoublePlant.EnumPlantType type = random.nextFloat() < fernChance ? BlockDoublePlant.EnumPlantType.FERN : BlockDoublePlant.EnumPlantType.GRASS;
						
						if(Blocks.DOUBLE_PLANT.canPlaceBlockAt(world, grassPos))
							Blocks.DOUBLE_PLANT.placeAt(world, grassPos, type, 2);
					} else
					{
						IBlockState state = Blocks.TALLGRASS.getDefaultState().withProperty(BlockTallGrass.TYPE, random.nextFloat() < fernChance ? BlockTallGrass.EnumType.FERN : BlockTallGrass.EnumType.GRASS);
						
						if(Blocks.TALLGRASS.canBlockStay(world, grassPos, state))
							world.setBlockState(grassPos, state, 2);*/
					}
				}
			}
		
		return null;
	}
	
	@Override
	public float getPriority()
	{
		return 0.4F;
	}
	
}