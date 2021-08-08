package com.mraof.minestuck.world.gen.feature.structure.blocks;

import com.mraof.minestuck.block.ReturnNodeBlock;
import com.mraof.minestuck.util.Debug;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.ChestBlock;
import net.minecraft.block.StairsBlock;
import net.minecraft.entity.EntityType;
import net.minecraft.fluid.Fluids;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.state.properties.ChestType;
import net.minecraft.tileentity.ChestTileEntity;
import net.minecraft.tileentity.MobSpawnerTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.WeightedSpawnerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MutableBoundingBox;
import net.minecraft.world.IWorld;
import net.minecraftforge.common.util.Constants;

import java.util.Objects;
import java.util.Random;

/**
 * Functions in this often require using get[X/Y/Z]WithOffset
 */
public class StructureBlockUtil
{
	
	public static boolean placeSpawner(BlockPos pos, IWorld world, MutableBoundingBox bb, EntityType<?> entityType)
	{
		WeightedSpawnerEntity entity = new WeightedSpawnerEntity();
		entity.getNbt().putString("id", Objects.requireNonNull(entityType.getRegistryName()).toString());
		return placeSpawner(pos, world, bb, entity);
	}
	
	public static boolean placeSpawner(BlockPos pos, IWorld world, MutableBoundingBox bb, WeightedSpawnerEntity entity)
	{
		if(bb.isVecInside(pos))
		{
			world.setBlockState(pos, Blocks.SPAWNER.getDefaultState(), Constants.BlockFlags.BLOCK_UPDATE);
			
			TileEntity te = world.getTileEntity(pos);
			if(te instanceof MobSpawnerTileEntity)
			{
				MobSpawnerTileEntity spawner = (MobSpawnerTileEntity) te;
				spawner.getSpawnerBaseLogic().setNextSpawnData(entity);
				
				return true;
			}
		}
		return false;
	}
	
	public static void placeLootChest(BlockPos pos, IWorld world, MutableBoundingBox bb, Direction direction, ResourceLocation lootTable, Random rand)
	{
		placeLootChest(pos, world, bb, direction, ChestType.SINGLE, lootTable, rand);
	}
	
	public static void placeLootChest(BlockPos pos, IWorld world, MutableBoundingBox bb, Direction direction, ChestType type, ResourceLocation lootTable, Random rand)
	{
		if(bb == null || bb.isVecInside(pos))
		{
			world.setBlockState(pos, Blocks.CHEST.getDefaultState().with(ChestBlock.FACING, direction).with(ChestBlock.TYPE, type), Constants.BlockFlags.BLOCK_UPDATE);
			
			TileEntity te = world.getTileEntity(pos);
			if(te instanceof ChestTileEntity)
			{
				ChestTileEntity chest = (ChestTileEntity) te;
				chest.setLootTable(lootTable, rand.nextLong());
			}
			
			if(bb != null)
				world.getChunk(pos).markBlockForPostprocessing(pos);
		}
	}
	
	public static void placeReturnNode(IWorld world, MutableBoundingBox boundingBox, BlockPos blockPos, Direction structureDirection)
	{
		int posX = blockPos.getX(), posY = blockPos.getY(), posZ = blockPos.getZ();
		
		if(structureDirection == Direction.NORTH || structureDirection == Direction.WEST)
			posX--;
		if(structureDirection == Direction.NORTH || structureDirection == Direction.EAST)
			posZ--;
		
		ReturnNodeBlock.placeReturnNode(world, new BlockPos(posX, posY, posZ), boundingBox);
	}
	
	/**
	 * Use reordered min and max blockpos. Will start from min blockpos and start building up in the positive x direction first
	 */
	public static void createPlainSpiralStaircase(BlockPos minBlockPosIn, BlockPos maxBlockPosIn, BlockState blockState, IWorld world, MutableBoundingBox boundingBox1/*, MutableBoundingBox boundingBox2*/)
	{
		//TODO placement happens twice(because of two bounding boxes?), one set of stairs can go beyond intended x/z bounds, stairs are fragmented(because it tries to generate in two bounding boxes?)
		
		world.setBlockState(minBlockPosIn, Blocks.GOLD_BLOCK.getDefaultState(), Constants.BlockFlags.BLOCK_UPDATE);
		world.setBlockState(maxBlockPosIn, Blocks.DIAMOND_BLOCK.getDefaultState(), Constants.BlockFlags.BLOCK_UPDATE);
		
		//Debug.debugf("createPlainSpiralStaircase. boundingBox = %s", boundingBox1);
		int xIterator = minBlockPosIn.getX();
		
		int zIterator = minBlockPosIn.getZ();
		
		boolean isXIterating = true; //toggles between moving in x or z direction
		boolean isXIterateReversed = false; //moving in negative x direction if positive
		boolean isZIterateReversed = false; //moving in negative z direction if positive
		for(int yIterator = minBlockPosIn.getY(); yIterator <= maxBlockPosIn.getY(); yIterator++)
		{
			BlockPos iteratorPos = new BlockPos(xIterator, yIterator, zIterator);
			
			/*Debug.debugf("createPlainSpiralStaircase. isXIterating = %s, isPosInsideBounding = %s, iteratorPos = %s",
					isXIterating, boundingBox1.isVecInside(iteratorPos), iteratorPos);*/
			
			if(xIterator >= maxBlockPosIn.getX() && !isXIterateReversed) //moving positive x
			{
				isXIterating = false;
				isXIterateReversed = true;
			} else if(zIterator >= maxBlockPosIn.getZ() && !isZIterateReversed) //moving positive z
			{
				isXIterating = true;
				isZIterateReversed = true;
			} else if(xIterator <= minBlockPosIn.getX() && isXIterateReversed) //moving negative x
			{
				isXIterating = false;
				isXIterateReversed = false;
			} else if(zIterator <= minBlockPosIn.getZ() && isZIterateReversed) //moving negative z
			{
				isXIterating = true;
				isZIterateReversed = true;
			}
			
			if(boundingBox1.isVecInside(iteratorPos)/* || boundingBox2.isVecInside(iteratorPos)*/)
			{
				//Debug.debugf("createPlainSpiralStaircase. placed at %s", iteratorPos);
				world.setBlockState(iteratorPos, blockState, Constants.BlockFlags.BLOCK_UPDATE);
				
				if(isXIterating && !isXIterateReversed)
				{
					xIterator++;
				} else if(!isXIterating && !isZIterateReversed)
				{
					zIterator++;
				} else if(isXIterating && isXIterateReversed)
				{
					xIterator--;
				} else if(!isXIterating && isZIterateReversed)
				{
					zIterator--;
				}
				/*
				if(isXIterating && !isXIterateReversed)
				{
					xIterator++;
				}
				if(!isXIterating && !isZIterateReversed)
				{
					zIterator++;
				}
				if(isXIterating && isXIterateReversed)
				{
					xIterator--;
				}
				if(!isXIterating && isZIterateReversed)
				{
					zIterator--;
				}
				 */
			}
			
			if(yIterator >= maxBlockPosIn.getY())
			{
				break;
			}
		}
	}
	/**
	 * bottomPos is at the bottom and to the left, with the width increasing how many blocks it is 90 degrees clockwise from the direction
	*/
	public static void createStairs(IWorld worldIn, MutableBoundingBox structurebb, BlockState bodyBlockState, BlockState tipBlockState, BlockPos bottomPos, int height, int width, Direction direction, boolean flipped)
	{
		//int xPos = bottomPos.getX(), zPos = bottomPos.getZ();
		
		BlockPos backEdge = bottomPos;
		if(direction == Direction.NORTH)
			backEdge = backEdge.north(height - 1).east(width);
		else if(direction == Direction.EAST)
			backEdge = backEdge.east(height - 1).south(width);
		else if(direction == Direction.SOUTH)
			backEdge = backEdge.south(height - 1).west(width);
		else if(direction == Direction.WEST)
			backEdge = backEdge.west(height - 1).north(width);
		
		for(int y = 0; y < height; ++y)
		{
			BlockPos frontEdge = bottomPos.up(y);
			if(direction == Direction.NORTH)
				frontEdge = frontEdge.north(y);
			else if(direction == Direction.EAST)
				frontEdge = frontEdge.east(y);
			else if(direction == Direction.SOUTH)
				frontEdge = frontEdge.south(y);
			else if(direction == Direction.WEST)
				frontEdge = frontEdge.west(y);
			
			//Debug.debugf("frontEdge = %s, backEdge = %s", frontEdge, backEdge.up(y));
			
			fillWithBlocksFromPos(worldIn, structurebb, bodyBlockState, axisAlignBlockPosGetMin(frontEdge, backEdge.up(y)), axisAlignBlockPosGetMax(frontEdge, backEdge.up(y)));
			
			BlockPos frontEdgeWidth = frontEdge;
			for(int frontWidth = 0; frontWidth < width + 1; ++frontWidth)
			{
				if(direction == Direction.NORTH)
					frontEdgeWidth = frontEdgeWidth.east(frontWidth);
				else if(direction == Direction.EAST)
					frontEdgeWidth = frontEdgeWidth.south(frontWidth);
				else if(direction == Direction.SOUTH)
					frontEdgeWidth = frontEdgeWidth.west(frontWidth);
				else if(direction == Direction.WEST)
					frontEdgeWidth = frontEdgeWidth.north(frontWidth);
				
				if(structurebb.isVecInside(frontEdgeWidth))
				{
					worldIn.setBlockState(frontEdgeWidth, tipBlockState, Constants.BlockFlags.BLOCK_UPDATE);
				}
			}
		}
	}
	
	/**
	 * skipReplaceBlockState takes in one blockstate that will not be overwritten by the sphere
	 */
	public static void createSphere(IWorld worldIn, MutableBoundingBox structurebb, BlockState blockState, BlockPos centerPos, int radius, BlockState skipReplaceBlockState)
	{
		for(int y = centerPos.getY() - radius; y <= centerPos.getY() + radius; ++y)
		{
			for(int x = centerPos.getX() - radius; x <= centerPos.getX() + radius; ++x)
			{
				for(int z = centerPos.getZ() - radius; z <= centerPos.getZ() + radius; ++z)
				{
					BlockPos currentPos = new BlockPos(x, y, z);
					
					if(skipReplaceBlockState == null)
						skipReplaceBlockState = blockState;
					
					if(structurebb.isVecInside(currentPos) && Math.sqrt(centerPos.distanceSq(currentPos)) <= radius && worldIn.getBlockState(currentPos) != skipReplaceBlockState)
					{
						worldIn.setBlockState(currentPos, blockState, Constants.BlockFlags.BLOCK_UPDATE);
					}
				}
			}
		}
	}
	
	/**
	 * Cylinder is upright
	 */
	public static void createCylinder(IWorld worldIn, MutableBoundingBox structurebb, BlockState blockState, BlockPos bottomPos, int radius, int height)
	{
		for(int y = bottomPos.getY(); y <= bottomPos.getY() + height - 1; ++y)
		{
			for(int x = bottomPos.getX() - radius; x <= bottomPos.getX() + radius; ++x)
			{
				for(int z = bottomPos.getZ() - radius; z <= bottomPos.getZ() + radius; ++z)
				{
					BlockPos currentPos = new BlockPos(x, y, z);
					if(structurebb.isVecInside(currentPos) && Math.sqrt(currentPos.distanceSq(bottomPos.up(y - bottomPos.getY()))) <= radius)
					{
						worldIn.setBlockState(currentPos, blockState, Constants.BlockFlags.BLOCK_UPDATE);
					}
				}
			}
		}
	}
	
	/**
	 * Use reordered blockpos for min and max BlockPos parameters(axisAlignBlockPosGetMin/Max)
	 */
	public static void fillWithBlocksFromPos(IWorld worldIn, MutableBoundingBox structurebb, BlockState blockState, BlockPos minBlockPos, BlockPos maxBlockPos)
	{
		for(int y = minBlockPos.getY(); y <= maxBlockPos.getY(); ++y)
		{
			for(int x = minBlockPos.getX(); x <= maxBlockPos.getX(); ++x)
			{
				for(int z = minBlockPos.getZ(); z <= maxBlockPos.getZ(); ++z)
				{
					BlockPos currentPos = new BlockPos(x, y, z);
					if(structurebb.isVecInside(currentPos))
					{
						worldIn.setBlockState(currentPos, blockState, Constants.BlockFlags.BLOCK_UPDATE);
					}
				}
			}
		}
	}
	
	/*public static void fillWithAirCheckWater(IWorld worldIn, MutableBoundingBox structurebb, int minX, int minY, int minZ, int maxX, int maxY, int maxZ)
	{
		for(int y = minY; y <= maxY; ++y)
		{
			for(int x = minX; x <= maxX; ++x)
			{
				for(int z = minZ; z <= maxZ; ++z)
				{
					BlockPos pos = new BlockPos(this.getXWithOffset(x, z), this.getYWithOffset(y), this.getZWithOffset(x, z));
					if(structurebb.isVecInside(pos) && !this.getBlockStateFromPos(worldIn, x, y, z, structurebb).getFluidState().getFluid().isEquivalentTo(Fluids.WATER)) //ensures that the chunk is loaded before attempted to remove block, setBlockState already does this check
						worldIn.removeBlock(pos, false);
				}
			}
		}
	}*/
	
	/**
	 * normal trimmed down fill command except that it waterlogs blocks if it is replacing water, blockState parameter must be waterloggable
	 */
	public static void fillWithBlocksCheckWater(IWorld worldIn, MutableBoundingBox boundingboxIn, int xMin, int yMin, int zMin, int xMax, int yMax, int zMax, BlockState blockState)
	{
		for(int y = yMin; y <= yMax; ++y)
		{
			for(int x = xMin; x <= xMax; ++x)
			{
				for(int z = zMin; z <= zMax; ++z)
				{
					BlockPos currentPos = new BlockPos(x, y, z);
					//Debug.debugf("fillWithBlocksCheckWater. currentPos = %s, bb = %s", currentPos, boundingboxIn);
					if(boundingboxIn.isVecInside(currentPos))
					{
						if(worldIn.getBlockState(currentPos).getFluidState().getFluid().isEquivalentTo(Fluids.WATER)) //has no inside vs outside blockstates or existingOnly
							blockState = blockState.with(BlockStateProperties.WATERLOGGED, true); //only works with waterloggable blocks
						worldIn.setBlockState(currentPos, blockState, Constants.BlockFlags.BLOCK_UPDATE);
					}
				}
			}
		}
	}
	
	public static BlockPos axisAlignBlockPosGetMin(BlockPos minBlockPosIn, BlockPos maxBlockPosIn)
	{
		int blockPosMinX = Math.min(minBlockPosIn.getX(), maxBlockPosIn.getX());
		int blockPosMinY = Math.min(minBlockPosIn.getY(), maxBlockPosIn.getY());
		int blockPosMinZ = Math.min(minBlockPosIn.getZ(), maxBlockPosIn.getZ());
		
		BlockPos minBlockPos = new BlockPos(blockPosMinX, blockPosMinY, blockPosMinZ);
		
		return minBlockPos;
	}
	
	public static BlockPos axisAlignBlockPosGetMax(BlockPos minBlockPosIn, BlockPos maxBlockPosIn)
	{
		int blockPosMaxX = Math.max(minBlockPosIn.getX(), maxBlockPosIn.getX());
		int blockPosMaxY = Math.max(minBlockPosIn.getY(), maxBlockPosIn.getY());
		int blockPosMaxZ = Math.max(minBlockPosIn.getZ(), maxBlockPosIn.getZ());
		
		BlockPos maxBlockPos = new BlockPos(blockPosMaxX, blockPosMaxY, blockPosMaxZ);
		
		return maxBlockPos;
	}
}