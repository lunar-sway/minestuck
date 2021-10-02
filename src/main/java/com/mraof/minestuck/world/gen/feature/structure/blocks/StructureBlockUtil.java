package com.mraof.minestuck.world.gen.feature.structure.blocks;

import com.mraof.minestuck.block.EnumKeyType;
import com.mraof.minestuck.block.MSBlocks;
import com.mraof.minestuck.block.ReturnNodeBlock;
import com.mraof.minestuck.block.redstone.WirelessRedstoneReceiverBlock;
import com.mraof.minestuck.tileentity.DungeonDoorInterfaceTileEntity;
import com.mraof.minestuck.tileentity.LootBlockTileEntity;
import com.mraof.minestuck.tileentity.redstone.RemoteObserverTileEntity;
import com.mraof.minestuck.tileentity.redstone.StatStorerTileEntity;
import com.mraof.minestuck.tileentity.redstone.SummonerTileEntity;
import com.mraof.minestuck.tileentity.redstone.WirelessRedstoneTransmitterTileEntity;
import com.mraof.minestuck.util.Debug;
import com.mraof.minestuck.util.MSRotationUtil;
import com.mraof.minestuck.world.gen.feature.StructureBlockRegistryProcessor;
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
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.MutableBoundingBox;
import net.minecraft.world.IWorld;
import net.minecraft.world.gen.feature.template.PlacementSettings;
import net.minecraft.world.gen.feature.template.Template;
import net.minecraft.world.gen.feature.template.TemplateManager;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.common.util.Constants;

import java.util.Objects;
import java.util.Random;

/**
 * Functions in this often have usage of axisAlignBlockPos internal functions
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
	
	public static void placeChest(BlockPos pos, IWorld world, MutableBoundingBox bb, Direction direction, ResourceLocation lootTable, Random rand)
	{
		placeChest(pos, world, bb, direction, ChestType.SINGLE, lootTable, rand);
	}
	
	public static void placeChest(BlockPos pos, IWorld world, MutableBoundingBox bb, Direction direction, ChestType type, ResourceLocation lootTable, Random rand)
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
	
	public static void placeLootBlock(BlockPos pos, IWorld world, MutableBoundingBox bb, BlockState blockState, ResourceLocation lootTable)
	{
		if(bb == null || bb.isVecInside(pos))
		{
			world.setBlockState(pos, blockState, Constants.BlockFlags.BLOCK_UPDATE);
			
			TileEntity te = world.getTileEntity(pos);
			if(te instanceof LootBlockTileEntity)
			{
				LootBlockTileEntity chest = (LootBlockTileEntity) te;
				chest.setLootTable(lootTable);
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
	public static void placeWirelessRelay(IWorld world, MutableBoundingBox boundingBox, BlockPos transmitterBlockPos, BlockPos receiverBlockPos, boolean doesReceiverAutoReset)
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
			if(doesReceiverAutoReset)
				world.setBlockState(receiverBlockPos, MSBlocks.WIRELESS_REDSTONE_RECEIVER.getDefaultState().with(WirelessRedstoneReceiverBlock.AUTO_RESET, true), Constants.BlockFlags.BLOCK_UPDATE);
			else
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
	 * Creates a summoner with the designated active type(null will be converted to measuring deaths)
	 */
	public static void placeSummoner(IWorld world, MutableBoundingBox boundingBox, BlockPos blockPos, SummonerTileEntity.SummonType summonType)
	{
		if(boundingBox.isVecInside(blockPos))
		{
			world.setBlockState(blockPos, MSBlocks.SUMMONER.getDefaultState(), Constants.BlockFlags.BLOCK_UPDATE);
			MSBlocks.SUMMONER.getDefaultState().createTileEntity(world);
			TileEntity TE = world.getTileEntity(blockPos);
			if(!(TE instanceof SummonerTileEntity))
			{
				TE = new SummonerTileEntity();
				world.getWorld().setTileEntity(blockPos, TE);
			}
			SummonerTileEntity summonerTE = (SummonerTileEntity) TE;
			if(summonType == null)
				summonType = SummonerTileEntity.SummonType.IMP;
			summonerTE.setSummonedEntity(summonType);
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
		}
		fillWithBlocksFromPos(world, boundingBox, MSBlocks.DUNGEON_DOOR.getDefaultState(), minDoorBlockPos, maxDoorBlockPos);
		
	}
	
	/**
	 * Places a template translated such that the given position marks the center of the template.
	 */
	public static void placeCenteredTemplate(IWorld world, BlockPos pos, Template template, PlacementSettings settings)
	{
		BlockPos center = new BlockPos((template.getSize().getX() - 1)/2, 0, (template.getSize().getZ() - 1)/2);
		
		template.addBlocksToWorld(world, pos.subtract(center), settings.setCenterOffset(center));
	}
	
	/**
	 * Places a spiral staircase that:
	 * - Goes clockwise
	 * - Starts at minPos
	 * - Is kept within the box defined by minPos and maxPos together
	 * Placed blocks are not currently rotated, meaning that stair blocks currently aren't supported properly.
	 */
	public static void placeSpiralStaircase(IWorld world, MutableBoundingBox boundingBox, BlockPos minPos, BlockPos maxPos, BlockState blockState)
	{
		int xSize = Math.abs(maxPos.getX() - minPos.getX());
		int zSize = Math.abs(maxPos.getZ() - minPos.getZ());
		boolean facingX = (maxPos.getX() - minPos.getX()) > 0;
		boolean facingZ = (maxPos.getZ() - minPos.getZ()) > 0;
		Direction dir;
		if (facingX) {
			dir = facingZ ? Direction.EAST : Direction.NORTH;
		} else {
			dir = facingZ ? Direction.SOUTH : Direction.WEST;
		}
		
		placeSpiralStaircase(world, boundingBox, minPos, xSize, zSize, maxPos.getY(), dir, blockState);
	}
	
	/**
	 * Places a spiral staircase that goes clockwise within the given bounds.
	 * The top block will be placed at height maxY.
	 * Placed blocks are not currently rotated, meaning that stair blocks currently aren't supported properly.
	 */
	public static void placeSpiralStaircase(IWorld world, MutableBoundingBox boundingBox, BlockPos startPos, int xSize, int zSize, int maxY, Direction startDirection, BlockState blockState)
	{
		BlockPos pos = startPos;
		Direction direction = startDirection;
		
		while (true) {
			int hSize = direction.getAxis() == Direction.Axis.X ? xSize : zSize;
			
			for (int i = 0; i < hSize; i++) {
				if (pos.getY() > maxY)
					return;
				
				if (boundingBox.isVecInside(pos)) {
					world.setBlockState(pos, blockState, Constants.BlockFlags.BLOCK_UPDATE);
				}
				
				pos = pos.offset(direction).up();
			}
			
			direction = direction.rotateY();
		}
	}
	
	/**
	 * bottomPos is at the bottom and to the left, with the width increasing how many blocks it is 90 degrees clockwise from the direction
	 */
	public static void createStairs(IWorld worldIn, MutableBoundingBox structurebb, BlockState bodyBlockState, BlockState tipBlockState, BlockPos bottomPos, int height, int width, Direction direction, boolean flipped)
	{
		//TODO create flipped option, where the stairs generate upside down
		
		BlockPos backEdge = bottomPos.offset(direction, height -1).offset(direction.rotateY(), width - 1);
		
		for(int y = 0; y < height; ++y)
		{
			BlockPos frontEdge = bottomPos.up(y).offset(direction, y);
			
			fillWithBlocksFromPos(worldIn, structurebb, bodyBlockState, frontEdge, backEdge.up(y));
			fillWithBlocksFromPos(worldIn, structurebb, tipBlockState, frontEdge, frontEdge.offset(direction.rotateY(), width - 1));
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
	 * fills an area with blocks from one blockpos to another, but only places blocks a certain distance from one another
	 */
	public static void fillAsGrid(IWorld worldIn, MutableBoundingBox structurebb, BlockState blockState, BlockPos minBlockPos, BlockPos maxBlockPos, int toNextBlock)
	{
		minBlockPos = axisAlignBlockPosGetMin(minBlockPos, maxBlockPos);
		maxBlockPos = axisAlignBlockPosGetMax(minBlockPos, maxBlockPos);
		
		for(int y = minBlockPos.getY(); y <= maxBlockPos.getY(); y += toNextBlock)
		{
			for(int x = minBlockPos.getX(); x <= maxBlockPos.getX(); x += toNextBlock)
			{
				for(int z = minBlockPos.getZ(); z <= maxBlockPos.getZ(); z += toNextBlock)
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
	
	/**
	 * Built in use of axisAlignBlockPos
	 */
	public static void fillWithBlocksFromPos(IWorld worldIn, MutableBoundingBox structurebb, BlockState blockState, BlockPos pos1, BlockPos pos2)
	{
		BlockPos minBlockPos = axisAlignBlockPosGetMin(pos1, pos2);
		BlockPos maxBlockPos = axisAlignBlockPosGetMax(pos1, pos2);
		
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