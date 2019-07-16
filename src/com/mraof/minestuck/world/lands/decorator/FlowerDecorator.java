package com.mraof.minestuck.world.lands.decorator;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;

import com.mraof.minestuck.world.lands.gen.ChunkProviderLands;
import net.minecraft.world.gen.Heightmap;

public class FlowerDecorator implements ILandDecorator
{
	
	private List<Biome> biomes;
	private float flowerChance;		//Chance for each block to have a flower on it
	private float doubleFlowerChance;	//Chance for each placed flower to be a double-flower
	
	public FlowerDecorator(float grassChance, Biome... biomes)
	{
		this(grassChance, 0, biomes);
	}
	
	public FlowerDecorator(float flowerChance, float doubleFlowerChance, Biome... biomes)
	{
		this.flowerChance = flowerChance;
		this.doubleFlowerChance = doubleFlowerChance;
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
				
				if(random.nextFloat() < flowerChance)
				{
					if(!world.isAirBlock(grassPos))
						continue;
					
					//TODO: Make the flower decorator able to use other enums
					
					if(random.nextFloat() < doubleFlowerChance)
					{/*
						BlockDoublePlant.EnumPlantType type;
						if(random.nextFloat() < 0.5)
						{
							type = random.nextFloat() < 0.5 ? BlockDoublePlant.EnumPlantType.PAEONIA : BlockDoublePlant.EnumPlantType.ROSE;
						} else
						{
							type = random.nextFloat() < 0.5 ? BlockDoublePlant.EnumPlantType.SUNFLOWER : BlockDoublePlant.EnumPlantType.SYRINGA;
						}
						
						if(!Blocks.DOUBLE_PLANT.canPlaceBlockAt(world, grassPos))
						{
							grassPos = grassPos.down();
						}
						
						if(Blocks.DOUBLE_PLANT.canPlaceBlockAt(world, grassPos))
						{
							Blocks.DOUBLE_PLANT.placeAt(world, grassPos, type, 2);
						}
						
					} else
					{
						IBlockState state;
						
						if(random.nextFloat() < 0.5)
						{
							state = random.nextFloat() < 0.5 ? Blocks.RED_FLOWER.getDefaultState() : Blocks.YELLOW_FLOWER.getDefaultState();
						} else
						{
							state = Blocks.RED_FLOWER.getStateFromMeta(random.nextInt(8) + 1);
						}
						
						if(!Blocks.RED_FLOWER.canPlaceBlockAt(world, grassPos))
						{
							grassPos = grassPos.down();
						}
						
						if(Blocks.RED_FLOWER.canPlaceBlockAt(world, grassPos))
						{
							world.setBlockState(grassPos, state);
						}*/
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