package com.mraof.minestuck.world.lands.decorator;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;
import java.util.Random;
import java.util.function.BiConsumer;

import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import com.mraof.minestuck.util.CoordPair;
import com.mraof.minestuck.world.lands.decorator.MesaDecorator.BlockRestorer;
import com.mraof.minestuck.world.lands.gen.ChunkProviderLands;
import net.minecraft.world.gen.Heightmap;

/**
 * A decorator that generates Mesas. By default, generates the rainbow mesas seen in rainbow Lands.
 * However, this generator is heavily customizable by resetting the information in the decorator prior to using it.
 *  
 * @author BenjaminK
 *
 */
public class MesaDecorator extends BiomeSpecificDecorator
{
	private float frequency = 0.04F;
	private float priority = 0.6F;
	private int tallness = 7;
	private float altFrequency = 0.025F;
	private BlockState baseBlock[] = {Blocks.RED_TERRACOTTA.getDefaultState(),
			Blocks.ORANGE_TERRACOTTA.getDefaultState(),
			Blocks.YELLOW_TERRACOTTA.getDefaultState(),
			Blocks.GREEN_TERRACOTTA.getDefaultState(),
			Blocks.CYAN_TERRACOTTA.getDefaultState(),
			Blocks.BLUE_TERRACOTTA.getDefaultState(),
			Blocks.PURPLE_TERRACOTTA.getDefaultState(),
			Blocks.MAGENTA_TERRACOTTA.getDefaultState()};
	private BlockState altBlock[] = {Blocks.RED_GLAZED_TERRACOTTA.getDefaultState(),
			Blocks.ORANGE_GLAZED_TERRACOTTA.getDefaultState(), 
			Blocks.YELLOW_GLAZED_TERRACOTTA.getDefaultState(),
			Blocks.GREEN_GLAZED_TERRACOTTA.getDefaultState(), 
			Blocks.CYAN_GLAZED_TERRACOTTA.getDefaultState(), 
			Blocks.BLUE_GLAZED_TERRACOTTA.getDefaultState(), 
			Blocks.PURPLE_GLAZED_TERRACOTTA.getDefaultState(), 
			Blocks.MAGENTA_GLAZED_TERRACOTTA.getDefaultState() };
	private BlockState baseCore = Blocks.AIR.getDefaultState();
	private BlockState altCore = Blocks.BEACON.getDefaultState();
	
	private boolean stomps=false;
	
	@Override
	public int getCount(Random random)
	{
		return random.nextFloat() < frequency ? 1 : 0;
	}
	
	@Override
	public BlockPos generate(World world, Random random, BlockPos pos, ChunkProviderLands provider)
	{
		pos = world.getHeight(Heightmap.Type.WORLD_SURFACE_WG, pos);
		int height = random.nextInt(tallness) + tallness + 3;
		
		if(world.getBlockState(pos.up(height*2/3)).getMaterial().isLiquid())	//At least 1/3rd of the height should be above the liquid surface
			return null;
		
		float plateauSize = 0.6F + random.nextFloat()*(height/10F);
		boolean isAlt = random.nextFloat()<altFrequency;
		
		BlockPos nodePos = generateMesa(pos.up(height), height, plateauSize, world, random, provider, isAlt);
		
		if(stomps == false)
		{
			if (isAlt ? altCore!=null : baseCore!=null)
			{
				world.setBlockState(nodePos, isAlt?altCore:baseCore, 2);
			}
		} else
		{
			world.setBlockState(nodePos, Blocks.AIR.getDefaultState(), 2);
		}
		stomps = false;
		
		return nodePos;
	}
	
	//TODO: Figure out how this code even works, make it more readable (and possibly more efficient), and make the same changes to RockDecorator.generateRock() 
	private BlockPos generateMesa(BlockPos rockPos, int height, float plateauSize, World world, Random random, ChunkProviderLands provider, boolean isAlt)
	{
		float xSlope = random.nextFloat(), zSlope = random.nextFloat();
		BlockState groundBlock = provider.blockRegistry.getBlockState("ground");
		
		Map<CoordPair, Integer> heightMap = new HashMap<>();
		Queue<BlockPos> toProcess = new LinkedList<>();
		Map<BlockPos, BlockState> was = new HashMap<>();
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
			
			//if(provider.villageHandler.isPositionInStructure(world, pos) || provider.structureHandler.isPositionInStructure(world, pos))
			{
				stomps = true;
				//break;
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
					//if(provider.villageHandler.isPositionInStructure(world, pos) || provider.structureHandler.isPositionInStructure(world, pos))
					{
						stomps=true;
						//break;
					}
					heightMap.put(coord, pos.getY());
					if(checkCoord(coord, heightMap))
						toProcess2.add(new BlockEntry(coord, entry.spreadChance));
				}
			} else entry.spreadChance += 0.5F;
			
			if(!world.getBlockState(new BlockPos(entry.pos.x, rockPos.getY() - h - 1, entry.pos.z)).equals(groundBlock))
				toProcess2.add(entry);
		}
		
		for(Map.Entry<CoordPair, Integer> entry : heightMap.entrySet())
		{
			BlockPos pos = new BlockPos(entry.getKey().x, entry.getValue(), entry.getKey().z);
			do
			{
				was.put(pos, world.getBlockState(pos));
				world.setBlockState(pos, isAlt?altBlock[pos.getY() % altBlock.length]:baseBlock[pos.getY() % baseBlock.length], 2);
				pos = pos.down();
			} while(!world.getBlockState(pos).equals(groundBlock));
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
			BiConsumer<? super BlockPos, ? super BlockState> action = new BlockRestorer().setWorld(world);
			was.forEach(action);
		}
		
		return corePosition;
	}
	
	public class BlockRestorer implements BiConsumer<BlockPos, BlockState>
	{
		World world = null;
		public BlockRestorer setWorld(World w) {world = w; return this;}
		
		@Override
		public void accept(BlockPos t, BlockState u)
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
	
	/*
	 * Getters
	 */

	@Override
	// getPriority works exactly the same way as it does in every other decorator.
	// Documentation should be provided with the method overridden.
	// The need to override and define this method at all could be avoided by defining it as a default method in ILandDecorator.
	// However, that functionality is exclusive to Java 8, and Minestuck tries to maintain compatibility as far back as Java 6.
	public float getPriority() { return priority; }
	
	public float getFrequency() { return frequency; }
	
	public int getTallness() { return tallness; }
	
	public float getAltFrequency() { return altFrequency; }
	
	public BlockState[] getBaseBlock() { return baseBlock; }
	
	public BlockState[] getAltBlock() { return altBlock; }
	
	/*
	 * Setters
	 */
	
	public MesaDecorator setPriority(float priority)
	{
		this.priority = priority;
		return this;
	}

	public MesaDecorator setFrequency(float frequency) {
		this.frequency = frequency;
		return this;
	}

	public MesaDecorator setTallness(int tallness) {
		this.tallness = tallness;
		return this;
	}

	public MesaDecorator setAltFrequency(float altFrequency) {
		this.altFrequency = altFrequency;
		return this;
	}

	public MesaDecorator setAltBlock(BlockState[] altBlock) {
		this.altBlock= altBlock;
		return this;
	}

	public MesaDecorator setBaseBlock(BlockState[] baseBlock) {
		this.baseBlock = baseBlock;
		return this;
	}
}