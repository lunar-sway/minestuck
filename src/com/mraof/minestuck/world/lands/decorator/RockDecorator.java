package com.mraof.minestuck.world.lands.decorator;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;
import java.util.Random;
import java.util.function.BiConsumer;

import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import com.mraof.minestuck.util.CoordPair;
import com.mraof.minestuck.world.lands.decorator.MesaDecorator.BlockRestorer;
import com.mraof.minestuck.world.lands.gen.ChunkProviderLands;

public class RockDecorator extends BiomeSpecificDecorator
{
	private boolean stomps=false;
	
	@Override
	public int getCount(Random random)
	{
		return random.nextFloat() < 0.02 ? 1 : 0;
	}
	
	@Override
	public BlockPos generate(World world, Random random, BlockPos pos, ChunkProviderLands provider)
	{
		pos = world.getTopSolidOrLiquidBlock(pos);
		int height = random.nextInt(7) + 10;
		
		if(world.getBlockState(pos.up(height*2/3)).getMaterial().isLiquid())	//At least 1/3rd of the height should be above the liquid surface
			return null;
		float plateauSize = 0.2F + random.nextFloat()*(height/25F);
		
		BlockPos nodePos = generateRock(pos.up(height), height, plateauSize, world, random, provider);
		stomps = false;
		
/*		float rockRarity = plateauSize + height/15F + random.nextFloat()*0.5F - 0.5F;
		
		if(rockRarity > 1F)
		{
			generateSubRock(pos, height, plateauSize, world, random, worldProvider);
			rockRarity -= 1F;
		}
		if(random.nextFloat() < rockRarity)
			generateSubRock(pos, height, plateauSize, world, random, worldProvider);*/
		
		return nodePos;
	}
	
	private void generateSubRock(BlockPos pos, int heightOld, float plateauOld, World world, Random rand, ChunkProviderLands provider)
	{
		int height = 5 + rand.nextInt((int) ((heightOld - 6)*0.75));
		BlockPos newPos = pos.add(rand.nextInt(10) - 5, 0, rand.nextInt(10) - 5);
		newPos = world.getTopSolidOrLiquidBlock(newPos).up(height);
		float plateauSize = rand.nextFloat()*plateauOld*0.75F;
		
		generateRock(newPos, height, plateauSize, world, rand, provider);
		stomps = false;
	}
	
	private BlockPos generateRock(BlockPos rockPos, int height, float plateauSize, World world, Random random, ChunkProviderLands provider)
	{
		float xSlope = random.nextFloat(), zSlope = random.nextFloat();
		IBlockState block = provider.getGroundBlock();
		
		Map<CoordPair, Integer> heightMap = new HashMap<CoordPair, Integer>();
		Queue<BlockPos> toProcess = new LinkedList<BlockPos>();
		Map<BlockPos, IBlockState> was = new HashMap<BlockPos, IBlockState>();
		toProcess.add(rockPos);
		toProcess.add(null);
		
		stomps = false;
		
		while(!toProcess.isEmpty())	//place the top layer of blocks
		{
			BlockPos pos = toProcess.remove();
			if(pos == null)
				if(toProcess.isEmpty())
					break;
				else
				{
					plateauSize -= 0.25;
					toProcess.add(null);
					continue;
				}
			
			if(provider.villageHandler.isPositionInStructure(world, pos) || provider.structureHandler.isPositionInStructure(world, pos))
			{
				stomps = true;
				break;
			}
			
			if(random.nextFloat()*xSlope < plateauSize)
				toProcess.add(pos.west());
			if(random.nextFloat()*(1F - xSlope) < plateauSize)
				toProcess.add(pos.east());
			if(random.nextFloat()*zSlope < plateauSize)
				toProcess.add(pos.north());
			if(random.nextFloat()*(1F - zSlope) < plateauSize)
				toProcess.add(pos.south());
			
			CoordPair xz = new CoordPair(pos.getX(), pos.getZ());
			if(!heightMap.containsKey(xz))
				heightMap.put(xz, pos.getY());
		}
		
		Queue<BlockEntry> toProcess2 = new LinkedList<BlockEntry>();
		int h = 1;
		
		for(CoordPair coord : heightMap.keySet())
			if(checkCoord(coord, heightMap) && stomps==false)
				toProcess2.add(new BlockEntry(coord, plateauSize + 0.2F));
		toProcess2.add(null);
		
		while(!toProcess2.isEmpty() && stomps==false)
		{
			BlockEntry entry = toProcess2.remove();
			if(entry == null)
			{
				h++;
				if(toProcess2.isEmpty())
					break;
				toProcess2.add(null);
				continue;
			}
			
			if(!checkCoord(entry.pos, heightMap))
				continue;
			if(random.nextFloat() < entry.spreadChance)
			{
				CoordPair coord;
				if(random.nextBoolean())
					coord = random.nextFloat() < xSlope ? entry.pos.east() : entry.pos.west();
				else coord = random.nextFloat() < zSlope ? entry.pos.south() : entry.pos.north();
				entry.spreadChance -= Math.min(0.5F, (2F*h)/height);
				if(!heightMap.containsKey(coord))
				{
					BlockPos pos = new BlockPos(coord.x, rockPos.getY() - h, coord.z);
					if(provider.villageHandler.isPositionInStructure(world, pos) || provider.structureHandler.isPositionInStructure(world, pos))
					{
						stomps=true;
						break;
					}
					heightMap.put(coord, rockPos.getY() - h);
					if(checkCoord(coord, heightMap))
						toProcess2.add(new BlockEntry(coord, entry.spreadChance));
				}
			} else entry.spreadChance += 0.5F;
			
			if(!world.getBlockState(new BlockPos(entry.pos.x, rockPos.getY() - h - 1, entry.pos.z)).equals(block))
				toProcess2.add(entry);
		}
		
		for(Map.Entry<CoordPair, Integer> entry : heightMap.entrySet())
		{
			BlockPos pos = new BlockPos(entry.getKey().x, entry.getValue(), entry.getKey().z);
			do
			{
				if(provider.villageHandler.isPositionInStructure(world, pos) || provider.structureHandler.isPositionInStructure(world, pos) || stomps==true)
				{
					stomps=true;
					break;
				}
				was.put(pos, world.getBlockState(pos));
				world.setBlockState(pos, block, 2);
				pos = pos.down();
			} while(!world.getBlockState(pos).equals(block));
		}
		
		CoordPair nodePos = new CoordPair(rockPos.getX(), rockPos.getZ());
		int maxBlocks = 0;
		for(int i = 0; i < 9 && stomps==false; i++)
		{
			CoordPair coords = new CoordPair(rockPos.getX() + (i % 3) - 1, rockPos.getZ() + i/3 - 1);
			int blockCount = 0;
			for(int i1 = 0; i1 < 4; i1++)
			{
				Integer coordsHeight = heightMap.get(new CoordPair(coords.x + (i1 % 2), coords.z + i1/2));
				if(coordsHeight != null && coordsHeight == rockPos.getY())
					blockCount++;
			}
			if(blockCount > maxBlocks)
			{
				nodePos = coords;
				maxBlocks = blockCount;
			}
		}
		
		BlockPos corePosition = new BlockPos(nodePos.x, rockPos.getY() + 1, nodePos.z);
		
		if(stomps)
		{
			BiConsumer<? super BlockPos, ? super IBlockState> action = new BlockRestorer().setWorld(world);
			was.forEach(action);
		}
		
		return corePosition;
	}
	
	public class BlockRestorer implements BiConsumer<BlockPos, IBlockState>
	{
		World world = null;
		public BlockRestorer setWorld(World w) {world = w; return this;}
		
		@Override
		public void accept(BlockPos t, IBlockState u)
		{
			world.setBlockState(t, u);
		}
	}
	
	private static boolean checkCoord(CoordPair pair, Map<CoordPair, Integer> map)
	{
		if(map.containsKey(pair.north()) && map.containsKey(pair.east()) && map.containsKey(pair.south()) && map.containsKey(pair.west()))
			return false;
		return true;
	}
	
	private static class BlockEntry
	{
		private final CoordPair pos;
		private float spreadChance;
		private BlockEntry(CoordPair pos, float spreadChance)
		{
			this.pos = pos;
			this.spreadChance = spreadChance;
		}
	}
	
	@Override
	public float getPriority()
	{
		return 0.8F;
	}
	
}