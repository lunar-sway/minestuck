package com.mraof.minestuck.world.gen;

import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.world.World;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraft.world.gen.feature.WorldGenMinable;
import net.minecraftforge.fml.common.IWorldGenerator;

import com.mraof.minestuck.Minestuck;
import com.mraof.minestuck.block.OreCruxite;
import com.mraof.minestuck.world.WorldProviderLands;
import com.mraof.minestuck.world.gen.lands.BlockWithMetadata;

public class OreHandler implements IWorldGenerator
{
	
	@Override
	public void generate(Random random, int chunkX, int chunkZ, World world,
			IChunkProvider chunkGenerator, IChunkProvider chunkProvider) {
		if (world.provider.isSurfaceWorld()) {
		      this.addOreSpawn(Minestuck.oreCruxite, world, random, chunkX * 16, chunkZ * 16, 16, 16, 6 + random.nextInt(3), 10, 0, 60);
		}
	}
	
	public void addOreSpawn(Block block, World world, Random random, int blockXPos, int blockZPos, int maxX, int maxZ, int maxVeinSize, int chancesToSpawn, int minY, int maxY)
	{
		//int maxPossY = minY + (maxY - 1);
		int diffBtwnMinMaxY = maxY - minY;
		BlockWithMetadata groundType = new BlockWithMetadata(Blocks.stone);
		if(world.provider instanceof WorldProviderLands)
			groundType = ((ChunkProviderLands) world.provider.createChunkGenerator()).upperBlock;
		for(int x = 0; x < chancesToSpawn; x++)
		{
			int posX = blockXPos + random.nextInt(maxX);
			int posY = minY + random.nextInt(diffBtwnMinMaxY);
			int posZ = blockZPos + random.nextInt(maxZ);
			(new WorldGenMinable(block, OreCruxite.getMetadata(groundType), maxVeinSize, groundType.block)).generate(world, random, posX, posY, posZ);
		}
	}

}
