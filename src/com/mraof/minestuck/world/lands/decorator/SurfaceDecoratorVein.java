package com.mraof.minestuck.world.lands.decorator;

import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.WorldGenMinable;

import com.google.common.base.Predicate;
import com.mraof.minestuck.util.Debug;
import com.mraof.minestuck.world.gen.OreHandler;
import com.mraof.minestuck.world.lands.gen.ChunkProviderLands;

public class SurfaceDecoratorVein implements ILandDecorator
{
	
	public int amount;
	public IBlockState block;
	public int size;
	
	public SurfaceDecoratorVein(IBlockState block,int amount,int size) 
	{
		this.amount = amount;
		this.block = block;
		this.size = size;
	}
	
	@Override
	public void generate(World world, Random random, int chunkX, int chunkZ, ChunkProviderLands provider)
	{
		int minY = 0;
		int maxY = 255;
		int diffBtwnMinMaxY = maxY - minY;
		for(int x = 0; x < amount; x++)
		{
			int posX = chunkX * 16 + random.nextInt(16);
			int posY = minY + random.nextInt(diffBtwnMinMaxY);
			int posZ = chunkZ * 16 + random.nextInt(16);
			(new WorldGenMinable(block, size/2 + random.nextInt(size*2), new OreHandler.BlockStatePredicate(provider.surfaceBlock))).generate(world, random, new BlockPos(posX, posY, posZ));
		}
	}
	
	@Override
	public float getPriority()
	{
		return 0.2F;
	}
	
}
