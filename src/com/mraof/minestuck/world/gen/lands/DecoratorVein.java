package com.mraof.minestuck.world.gen.lands;

import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.WorldGenMinable;

import com.mraof.minestuck.util.Debug;
import com.mraof.minestuck.world.gen.ChunkProviderLands;

public class DecoratorVein implements ILandDecorator
{
	
	int amount;
	Block block;
	int meta = 0;
	int size;
	
	public DecoratorVein(Block block,int amount,int size) 
	{
		this.amount = amount;
		this.block = block;
		this.size = size;
	}
	
	public DecoratorVein(Block block,int meta,int amount,int size) 
	{
		this(block,amount,size);
		this.meta = meta;
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
//			Debug.printf("Generating vien at %d %d %d",posX,posY,posZ);
			(new WorldGenMinable(block, meta, size/2 + random.nextInt(size*2),provider.surfaceBlock.block)).generate(world, random, posX, posY, posZ);
		}
	}
}
