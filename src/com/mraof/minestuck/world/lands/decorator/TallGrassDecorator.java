package com.mraof.minestuck.world.lands.decorator;

import java.util.Random;

import net.minecraft.block.BlockDoublePlant;
import net.minecraft.block.BlockTallGrass;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;

import com.mraof.minestuck.world.lands.gen.ChunkProviderLands;

public class TallGrassDecorator implements ILandDecorator
{
	
	private float grassChance;	//Chance for each block to have grass on it
	private float fernChance;	//Chance to place fern instead of grass
	private float doubleGrassChance;	//Chance for each placed grass to be the double-block variant
	
	public TallGrassDecorator(float grassChance)
	{
		this(grassChance, 0);
	}
	
	public TallGrassDecorator(float grassChance, float fernChance)
	{
		this(grassChance, fernChance, 0);
	}
	
	public TallGrassDecorator(float grassChance, float fernChance, float doubleGrassChance)
	{
		this.grassChance = grassChance;
		this.fernChance = fernChance;
		this.doubleGrassChance = doubleGrassChance;
	}
	
	@Override
	public BlockPos generate(World world, Random random, int chunkX, int chunkZ, ChunkProviderLands provider)
	{
		BlockPos pos = new BlockPos((chunkX << 4) + 8, 0, (chunkZ << 4) + 8);
		for(int x = 0; x < 16; x++)
			for(int z = 0; z < 16; z++)
				if(random.nextFloat() < grassChance)
				{
					BlockPos grassPos = world.getHeight(pos.add(x, 0, z));
					if(!world.isAirBlock(grassPos))
						continue;
					if(random.nextFloat() < doubleGrassChance)
					{
						BlockDoublePlant.EnumPlantType type = random.nextFloat() < fernChance ? BlockDoublePlant.EnumPlantType.FERN : BlockDoublePlant.EnumPlantType.GRASS;
						
						if(Blocks.double_plant.canPlaceBlockAt(world, grassPos))
							Blocks.double_plant.placeAt(world, grassPos, type, 2);
					} else
					{
						IBlockState state = Blocks.tallgrass.getDefaultState().withProperty(BlockTallGrass.TYPE, random.nextFloat() < fernChance ? BlockTallGrass.EnumType.FERN : BlockTallGrass.EnumType.GRASS);
						
						if(Blocks.tallgrass.canBlockStay(world, grassPos, state))
							world.setBlockState(grassPos, state, 2);
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