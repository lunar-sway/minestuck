package com.mraof.minestuck.world.gen;

import static com.mraof.minestuck.MinestuckConfig.baseCruxiteVeinSize;
import static com.mraof.minestuck.MinestuckConfig.baseUraniumVeinSize;
import static com.mraof.minestuck.MinestuckConfig.bonusCruxiteVeinSize;
import static com.mraof.minestuck.MinestuckConfig.bonusUraniumVeinSize;
import static com.mraof.minestuck.MinestuckConfig.cruxiteStratumMax;
import static com.mraof.minestuck.MinestuckConfig.cruxiteStratumMin;
import static com.mraof.minestuck.MinestuckConfig.cruxiteVeinsPerChunk;
import static com.mraof.minestuck.MinestuckConfig.disableCruxite;
import static com.mraof.minestuck.MinestuckConfig.disableUranium;
import static com.mraof.minestuck.MinestuckConfig.generateCruxiteOre;
import static com.mraof.minestuck.MinestuckConfig.generateUraniumOre;
import static com.mraof.minestuck.MinestuckConfig.uraniumStratumMax;
import static com.mraof.minestuck.MinestuckConfig.uraniumStratumMin;
import static com.mraof.minestuck.MinestuckConfig.uraniumVeinsPerChunk;
import static com.mraof.minestuck.block.MinestuckBlocks.oreCruxite;
import static com.mraof.minestuck.block.MinestuckBlocks.oreUranium;

import java.util.Random;

import com.google.common.base.Predicate;
import com.mraof.minestuck.block.BlockCruxiteOre;
import com.mraof.minestuck.block.BlockUraniumOre;
import com.mraof.minestuck.world.lands.LandDimension;
import com.mraof.minestuck.world.lands.gen.ChunkProviderLands;

import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraft.world.gen.IChunkGenerator;
import net.minecraft.world.gen.feature.WorldGenMinable;
import net.minecraftforge.fml.common.IWorldGenerator;

public class OreHandler implements IWorldGenerator
{
	@Override
	public void generate(Random random, int chunkX, int chunkZ, World world, IChunkGenerator chunkGenerator, IChunkProvider chunkProvider)
	{
		if(world.provider.isSurfaceWorld() && (generateCruxiteOre || chunkGenerator instanceof ChunkProviderLands) && !disableCruxite)
		{
			this.addOreSpawn(oreCruxite.getDefaultState(), world, random, chunkX * 16, chunkZ * 16, 16, 16,
					baseCruxiteVeinSize + random.nextInt(bonusCruxiteVeinSize), cruxiteVeinsPerChunk, cruxiteStratumMin, cruxiteStratumMax);
		}
		
		if(world.provider.isSurfaceWorld() && (generateUraniumOre || chunkGenerator instanceof ChunkProviderLands) && !disableUranium)
		{
			this.addOreSpawn(oreUranium.getDefaultState(), world, random, chunkX * 16, chunkZ * 16, 16, 16,
					baseUraniumVeinSize + random.nextInt(bonusUraniumVeinSize), uraniumVeinsPerChunk, uraniumStratumMin, uraniumStratumMax);
		}
	}
	
	public void addOreSpawn(IBlockState block, World world, Random random, int blockXPos, int blockZPos, int maxX, int maxZ, int maxVeinSize, int chancesToSpawn, int minY, int maxY)
	{
		//int maxPossY = minY + (maxY - 1);
		int diffBtwnMinMaxY = maxY - minY;
		IBlockState groundType = Blocks.STONE.getDefaultState();
		if(world.provider instanceof LandDimension)
			groundType = ((ChunkProviderLands) world.provider.createChunkGenerator()).getGroundBlock();
		if(block.getBlock() == oreCruxite)
			block = BlockCruxiteOre.getBlockState(groundType);
		if(block.getBlock() == oreUranium)
			block = BlockUraniumOre.getBlockState(groundType);
		for(int x = 0; x < chancesToSpawn; x++)
		{
			int posX = blockXPos + random.nextInt(maxX);
			int posY = minY + random.nextInt(diffBtwnMinMaxY);
			int posZ = blockZPos + random.nextInt(maxZ);
			(new WorldGenMinable(block, maxVeinSize, new BlockStatePredicate(groundType))).generate(world, random, new BlockPos(posX, posY, posZ));
		}
	}
	
	public static class BlockStatePredicate implements Predicate
	{
		IBlockState[] states;
		public BlockStatePredicate(IBlockState... blockStates)
		{
			states = blockStates;
		}
		@Override
		public boolean apply(Object input)
		{
			for(IBlockState state : states)
				if(state.equals(input))
					return true;
			return false;
		}
	}
}
