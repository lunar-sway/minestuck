package com.mraof.minestuck.world.lands.decorator;

import java.util.Random;

import net.minecraft.block.BlockPlanks;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.WorldGenAbstractTree;
import net.minecraft.world.gen.feature.WorldGenTrees;

import com.mraof.minestuck.world.lands.gen.ChunkProviderLands;

public class TreeDecorator implements ILandDecorator
{
	int[] treeTypes;
	
	public TreeDecorator(int[] trees)
	{
		this.treeTypes = trees;
	}
	
	public TreeDecorator(int treeType)
	{
		this(new int[] {treeType});
	}
	
	public TreeDecorator()
	{
		this(BlockPlanks.EnumType.OAK.getMetadata());
	}
	
	@Override
	public void generate(World world, Random random, int chunkX, int chunkZ, ChunkProviderLands provider)
	{
		int type = treeTypes[random.nextInt(treeTypes.length)];
		WorldGenTrees gen = new WorldGenTrees(false, 5, type, type, false);
		
		int treeCount = random.nextInt(5) + 5;
		for (int i = 0; i < treeCount; i++)
		{
			int x = random.nextInt(16) + 8;
			int z = random.nextInt(16) + 8;
			BlockPos pos = world.getHorizon(new BlockPos((chunkX << 4) + x, 0, (chunkZ << 4) + z));
			
			if (gen.generate(world, random, pos))
			{
				gen.func_180711_a(world, random, pos);
			}
		}
	}
	
	@Override
	public float getPriority()
	{
		return 0.6F;
	}
	
}
