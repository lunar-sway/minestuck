package com.mraof.minestuck.world.gen.feature;

import com.mojang.serialization.Codec;
import com.mraof.minestuck.util.CoordPair;
import com.mraof.minestuck.world.gen.feature.structure.blocks.StructureBlockRegistry;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MutableBoundingBox;
import net.minecraft.world.ISeedReader;
import net.minecraft.world.IWorld;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.NoFeatureConfig;
import net.minecraftforge.common.util.Constants;

import java.util.*;

public class MesaFeature extends Feature<NoFeatureConfig>
{
	private final BlockState[] baseBlock = {Blocks.RED_TERRACOTTA.defaultBlockState(),
			Blocks.ORANGE_TERRACOTTA.defaultBlockState(),
			Blocks.YELLOW_TERRACOTTA.defaultBlockState(),
			Blocks.GREEN_TERRACOTTA.defaultBlockState(),
			Blocks.CYAN_TERRACOTTA.defaultBlockState(),
			Blocks.BLUE_TERRACOTTA.defaultBlockState(),
			Blocks.PURPLE_TERRACOTTA.defaultBlockState(),
			Blocks.MAGENTA_TERRACOTTA.defaultBlockState()};
	private final BlockState[] altBlock = {Blocks.RED_GLAZED_TERRACOTTA.defaultBlockState(),
			Blocks.ORANGE_GLAZED_TERRACOTTA.defaultBlockState(),
			Blocks.YELLOW_GLAZED_TERRACOTTA.defaultBlockState(),
			Blocks.GREEN_GLAZED_TERRACOTTA.defaultBlockState(),
			Blocks.CYAN_GLAZED_TERRACOTTA.defaultBlockState(),
			Blocks.BLUE_GLAZED_TERRACOTTA.defaultBlockState(),
			Blocks.PURPLE_GLAZED_TERRACOTTA.defaultBlockState(),
			Blocks.MAGENTA_GLAZED_TERRACOTTA.defaultBlockState() };
	private final BlockState baseCore = Blocks.AIR.defaultBlockState();
	private final BlockState altCore = Blocks.BEACON.defaultBlockState();
	
	private final boolean stomps = false;
	
	public MesaFeature(Codec<NoFeatureConfig> codec)
	{
		super(codec);
	}
	
	@Override
	public boolean place(ISeedReader world, ChunkGenerator generator, Random rand, BlockPos pos, NoFeatureConfig config)
	{
		int tallness = 7;
		int height = rand.nextInt(tallness) + tallness + 3;
		
		if(world.getBlockState(pos.above(height*2/3)).getMaterial().isLiquid())	//At least 1/3rd of the height should be above the liquid surface
			return false;
		
		float plateauSize = 0.6F + rand.nextFloat()*(height/10F);
		float altFrequency = 0.01F;
		boolean isAlt = rand.nextFloat() < altFrequency;
		
		StructureBlockRegistry blocks = StructureBlockRegistry.getOrDefault(generator);
		
		BlockPos nodePos = generateMesa(pos.above(height), height, plateauSize, world, rand, isAlt, blocks.getBlockState("ground"));
		
		if(!stomps)
		{
				world.setBlock(nodePos, isAlt?altCore:baseCore, Constants.BlockFlags.BLOCK_UPDATE);
		} else
		{
			world.setBlock(nodePos, Blocks.AIR.defaultBlockState(), Constants.BlockFlags.BLOCK_UPDATE);
		}
		
		return true;
	}
	
	//TODO: Figure out how this code even works, make it more readable (and possibly more efficient), and make the same changes to RockDecorator.generateRock()
	private BlockPos generateMesa(BlockPos rockPos, int height, float plateauSize, IWorld world, Random random, boolean isAlt, BlockState groundBlock)
	{
		MutableBoundingBox boundingBox = new MutableBoundingBox(rockPos.getX() - 8, rockPos.getZ() - 8, rockPos.getX() + 7, rockPos.getZ() + 7);	//Extra solution to prevent the code to run indefinitely
		float xSlope = random.nextFloat(), zSlope = random.nextFloat();
		
		Map<CoordPair, Integer> heightMap = new HashMap<>();
		Queue<BlockPos> toProcess = new LinkedList<>();
		Map<BlockPos, BlockState> was = new HashMap<>();
		toProcess.add(rockPos);
		toProcess.add(null);
		
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
			
			/*if(provider.villageHandler.isPositionInStructure(world, pos) || provider.structureHandler.isPositionInStructure(world, pos)) TODO
			{
				stomps = true;
				break;
			}*/
			if(!boundingBox.isInside(pos))
				continue;
			
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
		
		Queue<BlockEntry> toProcess2 = new LinkedList<>();
		int h = 1;
		
		for(CoordPair coord : heightMap.keySet())
			if(checkCoord(coord, heightMap) && !stomps)
				toProcess2.add(new BlockEntry(coord, plateauSize + 0.2F));
		toProcess2.add(null);
		
		while(!toProcess2.isEmpty() && !stomps)
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
			
			if(!boundingBox.isInside(new BlockPos(entry.pos.x, 64, entry.pos.z)) || !checkCoord(entry.pos, heightMap))
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
					/*if(provider.villageHandler.isPositionInStructure(world, pos) || provider.structureHandler.isPositionInStructure(world, pos)) TODO
					{
						stomps=true;
						break;
					}*/
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
				world.setBlock(pos, isAlt?altBlock[pos.getY() % altBlock.length]:baseBlock[pos.getY() % baseBlock.length], Constants.BlockFlags.BLOCK_UPDATE);
				pos = pos.below();
			} while(!world.getBlockState(pos).equals(groundBlock));
		}
		
		CoordPair nodePos = new CoordPair(rockPos.getX(), rockPos.getZ());
		int maxBlocks = 0;
		for(int i = 0; i < 9 && !stomps; i++)
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
			was.forEach((t, u) -> world.setBlock(t, u, Constants.BlockFlags.BLOCK_UPDATE));
		}
		
		return corePosition;
	}
	
	private static boolean checkCoord(CoordPair pair, Map<CoordPair, Integer> map)
	{
		return !map.containsKey(pair.north()) || !map.containsKey(pair.east()) || !map.containsKey(pair.south()) || !map.containsKey(pair.west());
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
}