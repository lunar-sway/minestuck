package com.mraof.minestuck.world.lands.decorator.structure;

import java.util.Random;

import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;

import com.mraof.minestuck.block.MinestuckBlocks;
import com.mraof.minestuck.world.lands.gen.ChunkProviderLands;


public class RabbitHoleDecorator extends SimpleStructureDecorator
{
	public RabbitHoleDecorator(Biome biome)
	{
		super(biome);
	}

	@Override
	public float getPriority()
	{
		return 0.5f;
	}

	@Override
	protected BlockPos generateStructure(World world, Random random, BlockPos pos, ChunkProviderLands provider)
	{

		IBlockState ground = provider.getSurfaceBlock();
		IBlockState air = Blocks.AIR.getDefaultState();

		BlockPos newpos = pos;
		while ((world.getBlockState(newpos) == air))
		{
			newpos = newpos.down();
		}
		while (world.getBlockState(newpos.up()) != air)
		{
			newpos = newpos.up();
		}
		if (world.getBlockState(newpos) != ground)
		{
			if (world.getBlockState(newpos.down()) == ground)
			{
				newpos = newpos.down();
			}
			else
			{
				return null;
			}
		}

		boolean mirror;
		xCoord = newpos.getX();
		zCoord = newpos.getZ();
		yCoord = newpos.getY();
		//check wich way it should be facing
		if (world.getBlockState(new BlockPos(xCoord, yCoord - 1, zCoord)).getMaterial().isLiquid())
			return null;
		if (world.getBlockState(newpos.north()) == air)
		{
			rotation = false;
			mirror = false;
		}
		else if (world.getBlockState(newpos.east()) == air)
		{
			rotation = true;
			mirror = true;
		}
		else if (world.getBlockState(newpos.south()) == air)
		{
			rotation = false;
			mirror = true;
		}
		else if (world.getBlockState(newpos.west()) == air)
		{
			rotation = true;
			mirror = false;
		}
		else return null;

		if (mirror)
		{
			placeBlock(world, Blocks.DEADBUSH.getDefaultState(), 0, 0, 1);
			placeBlock(world, air, 0, 0, 0);
			placeBlock(world, air, 0, -1, 0);
			placeBlock(world, air, 0, -1, -1);
			placeBlock(world, MinestuckBlocks.rabbitSpawner.getDefaultState(), 0, -1, -2);
		}
		else
		{
			placeBlock(world, Blocks.DEADBUSH.getDefaultState(), 0, 0, -1);
			placeBlock(world, air, 0, 0, 0);
			placeBlock(world, air, 0, -1, 0);
			placeBlock(world, air, 0, -1, 1);
			placeBlock(world, MinestuckBlocks.rabbitSpawner.getDefaultState(), 0, -1, 2);
		}

		System.out.println(ground);
		return null;
	}

	@Override
	public int getCount(Random random)
	{
		return 5;
	}
}

