package com.mraof.minestuck.world.lands.decorator;

import java.util.Random;

import net.minecraft.util.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.WorldGenAbstractTree;

import com.mraof.minestuck.util.Debug;
import com.mraof.minestuck.world.lands.gen.ChunkProviderLands;

public abstract class TreeDecoratorBase implements ILandDecorator
{
	
	@Override
	public BlockPos generate(World world, Random random, int chunkX, int chunkZ, ChunkProviderLands provider)
	{
		int treeCount = getTreesPerChunk(random);
		for (int i = 0; i < treeCount; i++)
		{
			int x = random.nextInt(16) + 8;
			int z = random.nextInt(16) + 8;
			BlockPos pos = world.getHeight(new BlockPos((chunkX << 4) + x, 0, (chunkZ << 4) + z));
			
			WorldGenAbstractTree gen = getTreeToGenerate(world, pos, random);
			Debug.print("Trying to generate tree at "+pos);
			if (gen.generate(world, random, pos))
				gen.func_180711_a(world, random, pos);
			else Debug.print("Tree placement failed at "+pos);
		}
		
		return null;
	}
	
	protected abstract int getTreesPerChunk(Random rand);
	
	protected abstract WorldGenAbstractTree getTreeToGenerate(World world, BlockPos pos, Random rand);
	
	@Override
	public float getPriority()
	{
		return 0.6F;
	}
}