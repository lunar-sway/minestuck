package com.mraof.minestuck.world.gen.feature.structure.blocks;

import com.mraof.minestuck.block.EnumKeyType;
import com.mraof.minestuck.block.MSBlocks;
import com.mraof.minestuck.block.ReturnNodeBlock;
import com.mraof.minestuck.player.EnumAspect;
import com.mraof.minestuck.tileentity.DungeonDoorInterfaceTileEntity;
import com.mraof.minestuck.tileentity.redstone.RemoteObserverTileEntity;
import com.mraof.minestuck.tileentity.redstone.StatStorerTileEntity;
import com.mraof.minestuck.tileentity.redstone.WirelessRedstoneTransmitterTileEntity;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.ChestBlock;
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

import javax.annotation.Nullable;
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
	 * Creates a wireless redstone transmitter in the first blockpos, then creates a wireless redstone receiver in the second blockpos. The transmitter is linked to the receiver
	 */
	public static void placeWirelessRelay(IWorld world, MutableBoundingBox boundingBox, BlockPos transmitterBlockPos, BlockPos receiverBlockPos)
	{
		if(boundingBox.isVecInside(transmitterBlockPos))
		{
			world.setBlockState(transmitterBlockPos, MSBlocks.WIRELESS_REDSTONE_TRANSMITTER.getDefaultState(), Constants.BlockFlags.BLOCK_UPDATE);
			MSBlocks.WIRELESS_REDSTONE_TRANSMITTER.getDefaultState().createTileEntity(world);
			TileEntity TE = world.getTileEntity(transmitterBlockPos);
			if(!(TE instanceof WirelessRedstoneTransmitterTileEntity))
			{
				TE = new WirelessRedstoneTransmitterTileEntity();
				world.getWorld().setTileEntity(transmitterBlockPos, TE);
			}
			WirelessRedstoneTransmitterTileEntity transmitterTE = (WirelessRedstoneTransmitterTileEntity) TE;
			transmitterTE.setDestinationBlockPos(receiverBlockPos);
		}
		
		if(boundingBox.isVecInside(receiverBlockPos))
		{
			world.setBlockState(receiverBlockPos, MSBlocks.WIRELESS_REDSTONE_RECEIVER.getDefaultState(), Constants.BlockFlags.BLOCK_UPDATE);
		}
	}
	
	/**
	 * Creates a remote observer with the designated active type(null will be converted to player detection)
	 */
	public static void placeRemoteObserver(IWorld world, MutableBoundingBox boundingBox, BlockPos blockPos, RemoteObserverTileEntity.ActiveType activeType)
	{
		if(boundingBox.isVecInside(blockPos))
		{
			world.setBlockState(blockPos, MSBlocks.REMOTE_OBSERVER.getDefaultState(), Constants.BlockFlags.BLOCK_UPDATE);
			MSBlocks.REMOTE_OBSERVER.getDefaultState().createTileEntity(world);
			TileEntity TE = world.getTileEntity(blockPos);
			if(!(TE instanceof RemoteObserverTileEntity))
			{
				TE = new RemoteObserverTileEntity();
				world.getWorld().setTileEntity(blockPos, TE);
			}
			RemoteObserverTileEntity observerTE = (RemoteObserverTileEntity) TE;
			if(activeType == null)
				activeType = RemoteObserverTileEntity.ActiveType.IS_PLAYER_PRESENT;
			observerTE.setActiveType(activeType);
		}
	}
	
	/**
	 * Creates a stat storer with the designated active type(null will be converted to measuring deaths)
	 */
	public static void placeStatStorer(IWorld world, MutableBoundingBox boundingBox, BlockPos blockPos, StatStorerTileEntity.ActiveType activeType, int statDivideBy)
	{
		if(boundingBox.isVecInside(blockPos))
		{
			world.setBlockState(blockPos, MSBlocks.STAT_STORER.getDefaultState(), Constants.BlockFlags.BLOCK_UPDATE);
			MSBlocks.STAT_STORER.getDefaultState().createTileEntity(world);
			TileEntity TE = world.getTileEntity(blockPos);
			if(!(TE instanceof StatStorerTileEntity))
			{
				TE = new StatStorerTileEntity();
				world.getWorld().setTileEntity(blockPos, TE);
			}
			StatStorerTileEntity storerTE = (StatStorerTileEntity) TE;
			if(activeType == null)
				activeType = StatStorerTileEntity.ActiveType.DEATHS;
			storerTE.setActiveType(activeType);
			storerTE.setDivideValue(statDivideBy);
		}
	}
	
	/**
	 * Creates a dungeon door interface(with relevant data) and then the dungeon door blocks it will be meant to unlock
	 */
	public static void placeDungeonDoor(IWorld world, MutableBoundingBox boundingBox, BlockPos interfaceBlockPos, BlockPos minDoorBlockPos, BlockPos maxDoorBlockPos, EnumKeyType keyType)
	{
		if(boundingBox.isVecInside(interfaceBlockPos))
		{
			world.setBlockState(interfaceBlockPos, MSBlocks.DUNGEON_DOOR_INTERFACE.getDefaultState(), Constants.BlockFlags.BLOCK_UPDATE);
			MSBlocks.DUNGEON_DOOR_INTERFACE.getDefaultState().createTileEntity(world);
			TileEntity TE = world.getTileEntity(interfaceBlockPos);
			if(!(TE instanceof DungeonDoorInterfaceTileEntity))
			{
				TE = new DungeonDoorInterfaceTileEntity();
				world.getWorld().setTileEntity(interfaceBlockPos, TE);
			}
			DungeonDoorInterfaceTileEntity interfaceTE = (DungeonDoorInterfaceTileEntity) TE;
			interfaceTE.setKey(keyType);
			
			/*world.setBlockState(interfaceBlockPos, MSBlocks.DUNGEON_DOOR_INTERFACE.getDefaultState(), Constants.BlockFlags.BLOCK_UPDATE);
			TileEntity interfaceTE = world.getTileEntity(interfaceBlockPos);
			if(!(interfaceTE instanceof DungeonDoorInterfaceTileEntity) && interfaceTE != null)
			{
				interfaceTE = new DungeonDoorInterfaceTileEntity();
				world.getWorld().setTileEntity(interfaceBlockPos, interfaceTE);
			}
			if(interfaceTE != null)
			{
				((DungeonDoorInterfaceTileEntity) interfaceTE).setKey(keyType);
			} else
				throw new IllegalStateException("Unable to create a new dungeon door interface tile entity. Returned null!");
		
		doorInterfaceBlockState.createTileEntity(world);*/
		}
		fillWithBlocksFromPos(world, boundingBox, MSBlocks.DUNGEON_DOOR.getDefaultState(), minDoorBlockPos, maxDoorBlockPos);
		
	}
	
	/**
	 * Will generate the circular structure with the blockPosIn as the center. 16x resolution. Structure is 25 blocks wide and 1 block thick
	 */
	public static void placeLargeAspectSymbol(IWorld world, MutableBoundingBox boundingBox, BlockPos blockPosIn, BlockState blockStateIn, EnumAspect aspectIn)
	{
		createCylinder(world, boundingBox, blockStateIn, blockPosIn, 12, 1);
		BlockState aspectBlockStateLight = Blocks.LIGHT_GRAY_CONCRETE.getDefaultState();
		BlockState aspectBlockStateDark = Blocks.LIGHT_GRAY_CONCRETE.getDefaultState();
		if(aspectIn == EnumAspect.BLOOD) //based on banner art by Riotmode
		{
			aspectBlockStateLight = Blocks.RED_CONCRETE.getDefaultState();
			aspectBlockStateDark = Blocks.BLACK_CONCRETE.getDefaultState();
			
			//gash from left to right
			world.setBlockState(blockPosIn.west(10).south(2), aspectBlockStateDark, Constants.BlockFlags.BLOCK_UPDATE);
			fillWithBlocksFromPos(world, boundingBox, aspectBlockStateDark, blockPosIn.west(9).south(2), blockPosIn.west(9).south(1));
			world.setBlockState(blockPosIn.west(8), aspectBlockStateDark, Constants.BlockFlags.BLOCK_UPDATE);
			fillWithBlocksFromPos(world, boundingBox, aspectBlockStateLight, blockPosIn.west(8).south(1), blockPosIn.west(7).south(1));
			fillWithBlocksFromPos(world, boundingBox, aspectBlockStateLight, blockPosIn.west(7), blockPosIn.west(5));
			fillWithBlocksFromPos(world, boundingBox, aspectBlockStateLight, blockPosIn.west(6).north(1), blockPosIn.west(3).north(1));
			world.setBlockState(blockPosIn.west(5).north(2), aspectBlockStateDark, Constants.BlockFlags.BLOCK_UPDATE);
			world.setBlockState(blockPosIn.west(4), aspectBlockStateDark, Constants.BlockFlags.BLOCK_UPDATE);
			fillWithBlocksFromPos(world, boundingBox, aspectBlockStateLight, blockPosIn.west(4).north(2), blockPosIn.north(2));
			fillWithBlocksFromPos(world, boundingBox, aspectBlockStateLight, blockPosIn.west(2).north(3), blockPosIn.east(1).north(3));
			fillWithBlocksFromPos(world, boundingBox, aspectBlockStateLight, blockPosIn.north(4), blockPosIn.east(3).north(4));
			fillWithBlocksFromPos(world, boundingBox, aspectBlockStateLight, blockPosIn.east(2).north(5), blockPosIn.east(5).north(5));
			fillWithBlocksFromPos(world, boundingBox, aspectBlockStateLight, blockPosIn.east(4).north(6), blockPosIn.east(6).north(6));
			fillWithBlocksFromPos(world, boundingBox, aspectBlockStateLight, blockPosIn.east(6).north(7), blockPosIn.east(7).north(7));
			
			//left drip
			fillWithBlocksFromPos(world, boundingBox, aspectBlockStateLight, blockPosIn.west(3), blockPosIn.west(3).south(3));
			fillWithBlocksFromPos(world, boundingBox, aspectBlockStateDark, blockPosIn.west(4).south(2), blockPosIn.west(4).south(3));
			fillWithBlocksFromPos(world, boundingBox, aspectBlockStateDark, blockPosIn.west(2).south(2), blockPosIn.west(2).south(3));
			world.setBlockState(blockPosIn.west(3).south(4), aspectBlockStateDark, Constants.BlockFlags.BLOCK_UPDATE);
			
			//middle drip
			fillWithBlocksFromPos(world, boundingBox, aspectBlockStateLight, blockPosIn.up(1), blockPosIn.south(7));
			fillWithBlocksFromPos(world, boundingBox, aspectBlockStateDark, blockPosIn.west(1).south(6), blockPosIn.west(1).south(7));
			fillWithBlocksFromPos(world, boundingBox, aspectBlockStateDark, blockPosIn.east(1).south(6), blockPosIn.east(1).south(7));
			world.setBlockState(blockPosIn.south(8), aspectBlockStateDark, Constants.BlockFlags.BLOCK_UPDATE);
			
			//right drip
			fillWithBlocksFromPos(world, boundingBox, aspectBlockStateLight, blockPosIn.east(3).up(3), blockPosIn.east(3).south(2));
			fillWithBlocksFromPos(world, boundingBox, aspectBlockStateDark, blockPosIn.east(4).south(1), blockPosIn.east(4).south(2));
			fillWithBlocksFromPos(world, boundingBox, aspectBlockStateDark, blockPosIn.east(2).south(1), blockPosIn.east(2).south(2));
			world.setBlockState(blockPosIn.east(3).south(3), aspectBlockStateDark, Constants.BlockFlags.BLOCK_UPDATE);
		}
	}
	
	/**
	 * Built in use of axisAlignBlockPos. Will start from min blockpos and start building up in the positive x direction first
	 */
	public static void createPlainSpiralStaircase(BlockPos minBlockPosIn, BlockPos maxBlockPosIn, BlockState blockState, IWorld world, MutableBoundingBox boundingBox1/*, MutableBoundingBox boundingBox2*/)
	{
		//TODO placement happens twice(because of two bounding boxes?), one set of stairs can go beyond intended x/z bounds, stairs are fragmented(because it tries to generate in two bounding boxes?)
		
		minBlockPosIn = axisAlignBlockPosGetMin(minBlockPosIn, maxBlockPosIn);
		maxBlockPosIn = axisAlignBlockPosGetMax(minBlockPosIn, maxBlockPosIn);
		
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
			
			//TODO use Direction variable and rotate direction as it reaches end of line
			
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
			
			
			
			//Debug.debugf("createPlainSpiralStaircase. placed at %s", iteratorPos);
			if(boundingBox1.isVecInside(iteratorPos)/* || boundingBox2.isVecInside(iteratorPos)*/)
			{
				world.setBlockState(iteratorPos, blockState, Constants.BlockFlags.BLOCK_UPDATE);
			}
			
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
		//TODO create flipped option, where the stairs generate upside down
		
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
			
			fillWithBlocksFromPos(worldIn, structurebb, bodyBlockState, frontEdge, backEdge.up(y));
			
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
	 * Built in use of axisAlignBlockPos
	 */
	public static void fillWithBlocksFromPos(IWorld worldIn, MutableBoundingBox structurebb, BlockState blockState, BlockPos minBlockPos, BlockPos maxBlockPos)
	{
		minBlockPos = axisAlignBlockPosGetMin(minBlockPos, maxBlockPos);
		maxBlockPos = axisAlignBlockPosGetMax(minBlockPos, maxBlockPos);
		
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