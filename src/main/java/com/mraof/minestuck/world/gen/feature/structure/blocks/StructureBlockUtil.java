package com.mraof.minestuck.world.gen.feature.structure.blocks;

import com.mraof.minestuck.block.EnumKeyType;
import com.mraof.minestuck.block.MSBlocks;
import com.mraof.minestuck.block.ReturnNodeBlock;
import com.mraof.minestuck.block.redstone.WirelessRedstoneReceiverBlock;
import com.mraof.minestuck.tileentity.DungeonDoorInterfaceTileEntity;
import com.mraof.minestuck.tileentity.LootBlockTileEntity;
import com.mraof.minestuck.tileentity.redstone.*;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.ChestBlock;
import net.minecraft.entity.EntityType;
import net.minecraft.fluid.Fluids;
import net.minecraft.potion.Effect;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.state.properties.ChestType;
import net.minecraft.tileentity.ChestTileEntity;
import net.minecraft.tileentity.MobSpawnerTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MutableBoundingBox;
import net.minecraft.world.ISeedReader;
import net.minecraft.world.gen.feature.template.PlacementSettings;
import net.minecraft.world.gen.feature.template.Template;
import net.minecraftforge.common.util.Constants;

import java.util.Objects;
import java.util.Random;

/**
 * Functions in this often have usage of axisAlignBlockPos internal functions
 */
public class StructureBlockUtil
{
	public static boolean placeSpawner(BlockPos pos, ISeedReader world, MutableBoundingBox bb, EntityType<?> entityType)
	{
		WeightedSpawnerEntity entity = new WeightedSpawnerEntity();
		entity.getTag().putString("id", Objects.requireNonNull(entityType.getRegistryName()).toString());
		return placeSpawner(pos, world, bb, entity);
	}
	
	public static boolean placeSpawner(BlockPos pos, ISeedReader world, MutableBoundingBox bb, WeightedSpawnerEntity entity)
	{
		if(bb.isInside(pos))
		{
			world.setBlock(pos, Blocks.SPAWNER.defaultBlockState(), Constants.BlockFlags.BLOCK_UPDATE);
			
			TileEntity te = world.getBlockEntity(pos);
			if(te instanceof MobSpawnerTileEntity)
			{
				MobSpawnerTileEntity spawner = (MobSpawnerTileEntity) te;
				spawner.getSpawner().setNextSpawnData(entity);
				
				return true;
			}
		}
		return false;
	}
	
	public static void placeChest(BlockPos pos, ISeedReader world, MutableBoundingBox bb, Direction direction, ResourceLocation lootTable, Random rand)
	{
		placeChest(pos, world, bb, direction, ChestType.SINGLE, lootTable, rand);
	}
	
	public static void placeChest(BlockPos pos, ISeedReader world, MutableBoundingBox bb, Direction direction, ChestType type, ResourceLocation lootTable, Random rand)
	{
		if(bb == null || bb.isInside(pos))
		{
			world.setBlock(pos, Blocks.CHEST.defaultBlockState().setValue(ChestBlock.FACING, direction).setValue(ChestBlock.TYPE, type), Constants.BlockFlags.BLOCK_UPDATE);
			
			TileEntity te = world.getBlockEntity(pos);
			if(te instanceof ChestTileEntity)
			{
				ChestTileEntity chest = (ChestTileEntity) te;
				chest.setLootTable(lootTable, rand.nextLong());
			}
			
			if(bb != null)
				world.getChunk(pos).markPosForPostprocessing(pos);
		}
	}
	
	public static void placeLootBlock(BlockPos pos, ISeedReader world, MutableBoundingBox bb, BlockState blockState, ResourceLocation lootTable)
	{
		if(bb == null || bb.isInside(pos))
		{
			world.setBlock(pos, blockState, Constants.BlockFlags.BLOCK_UPDATE);
			
			TileEntity te = world.getBlockEntity(pos);
			if(te instanceof LootBlockTileEntity)
			{
				LootBlockTileEntity chest = (LootBlockTileEntity) te;
				chest.setLootTable(lootTable);
			}
			
			if(bb != null)
				world.getChunk(pos).markPosForPostprocessing(pos);
		}
	}
	
	public static void placeReturnNode(ISeedReader world, MutableBoundingBox boundingBox, BlockPos blockPos, Direction structureDirection)
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
	public static void placeWirelessRelay(ISeedReader world, MutableBoundingBox boundingBox, BlockPos transmitterBlockPos, BlockPos receiverBlockPos, boolean doesReceiverAutoReset)
	{
		if(boundingBox.isInside(transmitterBlockPos))
		{
			world.setBlock(transmitterBlockPos, MSBlocks.WIRELESS_REDSTONE_TRANSMITTER.defaultBlockState(), Constants.BlockFlags.BLOCK_UPDATE);
			MSBlocks.WIRELESS_REDSTONE_TRANSMITTER.defaultBlockState().createTileEntity(world);
			TileEntity TE = world.getBlockEntity(transmitterBlockPos);
			if(!(TE instanceof WirelessRedstoneTransmitterTileEntity))
			{
				TE = new WirelessRedstoneTransmitterTileEntity();
				world.getLevel().setBlockEntity(transmitterBlockPos, TE);
			}
			WirelessRedstoneTransmitterTileEntity transmitterTE = (WirelessRedstoneTransmitterTileEntity) TE;
			transmitterTE.setDestinationBlockPos(receiverBlockPos);
		}
		
		if(boundingBox.isInside(receiverBlockPos))
		{
			if(doesReceiverAutoReset)
				world.setBlock(receiverBlockPos, MSBlocks.WIRELESS_REDSTONE_RECEIVER.defaultBlockState().setValue(WirelessRedstoneReceiverBlock.AUTO_RESET, true), Constants.BlockFlags.BLOCK_UPDATE);
			else
				world.setBlock(receiverBlockPos, MSBlocks.WIRELESS_REDSTONE_RECEIVER.defaultBlockState(), Constants.BlockFlags.BLOCK_UPDATE);
		}
	}
	
	/**
	 * Creates a remote observer with the designated active type(null will be converted to player detection)
	 */
	public static void placeRemoteObserver(ISeedReader world, MutableBoundingBox boundingBox, BlockPos blockPos, RemoteObserverTileEntity.ActiveType activeType)
	{
		if(boundingBox.isInside(blockPos))
		{
			world.setBlock(blockPos, MSBlocks.REMOTE_OBSERVER.defaultBlockState(), Constants.BlockFlags.BLOCK_UPDATE);
			MSBlocks.REMOTE_OBSERVER.defaultBlockState().createTileEntity(world);
			TileEntity TE = world.getBlockEntity(blockPos);
			if(!(TE instanceof RemoteObserverTileEntity))
			{
				TE = new RemoteObserverTileEntity();
				world.getLevel().setBlockEntity(blockPos, TE);
			}
			RemoteObserverTileEntity observerTE = (RemoteObserverTileEntity) TE;
			if(activeType == null)
				activeType = RemoteObserverTileEntity.ActiveType.CURRENT_ENTITY_PRESENT;
			observerTE.setActiveType(activeType);
		}
	}
	
	/**
	 * Creates a stat storer with the designated active type(null will be converted to measuring deaths)
	 */
	public static void placeStatStorer(ISeedReader world, MutableBoundingBox boundingBox, BlockPos blockPos, StatStorerTileEntity.ActiveType activeType, int statDivideBy)
	{
		if(boundingBox.isInside(blockPos))
		{
			world.setBlock(blockPos, MSBlocks.STAT_STORER.defaultBlockState(), Constants.BlockFlags.BLOCK_UPDATE);
			MSBlocks.STAT_STORER.defaultBlockState().createTileEntity(world);
			TileEntity TE = world.getBlockEntity(blockPos);
			if(!(TE instanceof StatStorerTileEntity))
			{
				TE = new StatStorerTileEntity();
				world.getLevel().setBlockEntity(blockPos, TE);
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
	public static void placeSummoner(ISeedReader world, MutableBoundingBox boundingBox, BlockPos blockPos, EntityType<?> entityType)
	{
		if(boundingBox.isInside(blockPos))
		{
			world.setBlock(blockPos, MSBlocks.SUMMONER.defaultBlockState(), Constants.BlockFlags.BLOCK_UPDATE);
			MSBlocks.SUMMONER.defaultBlockState().createTileEntity(world);
			TileEntity TE = world.getBlockEntity(blockPos);
			if(!(TE instanceof SummonerTileEntity))
			{
				TE = new SummonerTileEntity();
				world.getLevel().setBlockEntity(blockPos, TE);
			}
			SummonerTileEntity summonerTE = (SummonerTileEntity) TE;
			if(entityType == null)
				entityType = EntityType.PLAYER;
			summonerTE.setSummonedEntity(entityType, null);
		}
	}
	
	/**
	 * Creates an area effect block with the designated potion type and area of effect(from pos)
	 */
	public static void placeAreaEffectBlock(ISeedReader world, MutableBoundingBox boundingBox, BlockPos blockPos, Effect effect, int effectAmplifier, BlockPos minEffectPos, BlockPos maxEffectPos)
	{
		if(boundingBox.isInside(blockPos))
		{
			world.setBlock(blockPos, MSBlocks.AREA_EFFECT_BLOCK.defaultBlockState(), Constants.BlockFlags.BLOCK_UPDATE);
			MSBlocks.AREA_EFFECT_BLOCK.defaultBlockState().createTileEntity(world);
			TileEntity TE = world.getBlockEntity(blockPos);
			if(!(TE instanceof AreaEffectTileEntity))
			{
				TE = new AreaEffectTileEntity();
				world.getLevel().setBlockEntity(blockPos, TE);
			}
			AreaEffectTileEntity areaEffectTE = (AreaEffectTileEntity) TE;
			
			areaEffectTE.setEffect(effect);
			areaEffectTE.setEffectAmplifier(effectAmplifier);
			areaEffectTE.setMinEffectPos(minEffectPos);
			areaEffectTE.setMaxEffectPos(maxEffectPos);
		}
	}
	
	/**
	 * Creates an area effect block with the designated potion type and area of effect(from int)
	 */
	public static void placeAreaEffectBlock(ISeedReader world, MutableBoundingBox boundingBox, BlockPos blockPos, Effect effect, int effectAmplifier, int minAreaX, int minAreaY, int minAreaZ, int maxAreaX, int maxAreaY, int maxAreaZ)
	{
		if(boundingBox.isInside(blockPos))
		{
			BlockPos minEffectPos = new BlockPos(minAreaX, minAreaY, minAreaZ);
			BlockPos maxEffectPos = new BlockPos(maxAreaX, maxAreaY, maxAreaZ);
			
			world.setBlock(blockPos, MSBlocks.AREA_EFFECT_BLOCK.defaultBlockState(), Constants.BlockFlags.BLOCK_UPDATE);
			MSBlocks.AREA_EFFECT_BLOCK.defaultBlockState().createTileEntity(world);
			TileEntity TE = world.getBlockEntity(blockPos);
			if(!(TE instanceof AreaEffectTileEntity))
			{
				TE = new AreaEffectTileEntity();
				world.getLevel().setBlockEntity(blockPos, TE);
			}
			AreaEffectTileEntity areaEffectTE = (AreaEffectTileEntity) TE;
			
			areaEffectTE.setEffect(effect);
			areaEffectTE.setEffectAmplifier(effectAmplifier);
			areaEffectTE.setMinEffectPos(minEffectPos);
			areaEffectTE.setMaxEffectPos(maxEffectPos);
		}
	}
	
	/**
	 * Creates a dungeon door interface(with relevant data) and then the dungeon door blocks it will be meant to unlock
	 */
	public static void placeDungeonDoor(ISeedReader world, MutableBoundingBox boundingBox, BlockPos interfaceBlockPos, BlockPos minDoorBlockPos, BlockPos maxDoorBlockPos, EnumKeyType keyType)
	{
		if(boundingBox.isInside(interfaceBlockPos))
		{
			world.setBlock(interfaceBlockPos, MSBlocks.DUNGEON_DOOR_INTERFACE.defaultBlockState(), Constants.BlockFlags.BLOCK_UPDATE);
			MSBlocks.DUNGEON_DOOR_INTERFACE.defaultBlockState().createTileEntity(world);
			TileEntity TE = world.getBlockEntity(interfaceBlockPos);
			if(!(TE instanceof DungeonDoorInterfaceTileEntity))
			{
				TE = new DungeonDoorInterfaceTileEntity();
				world.getLevel().setBlockEntity(interfaceBlockPos, TE);
			}
			DungeonDoorInterfaceTileEntity interfaceTE = (DungeonDoorInterfaceTileEntity) TE;
			interfaceTE.setKey(keyType);
		}
		fillWithBlocksFromPos(world, boundingBox, MSBlocks.DUNGEON_DOOR.defaultBlockState(), minDoorBlockPos, maxDoorBlockPos);
		
	}
	
	/**
	 * Places a template translated such that the given position marks the center of the template.
	 */
	public static void placeCenteredTemplate(ISeedReader world, BlockPos pos, Template template, PlacementSettings settings)
	{
		//TODO placement at some directions will improperly rotate the FACING property of at least pipe blocks
		BlockPos center = new BlockPos((template.getSize().getX() - 1) / 2, 0, (template.getSize().getZ() - 1) / 2);
		
		template.placeInWorld(world.getLevel(), pos.subtract(center), settings.setRotationPivot(center), world.getRandom()); //was template.addBlocksToWorld(world, pos.subtract(center), settings.setCenterOffset(center));
	}
	
	/**
	 * Places a spiral staircase that:
	 * - Goes clockwise
	 * - Starts at minPos
	 * - Is kept within the box defined by minPos and maxPos together
	 * Placed blocks are not currently rotated, meaning that stair blocks currently aren't supported properly.
	 */
	public static void placeSpiralStaircase(ISeedReader world, MutableBoundingBox boundingBox, BlockPos minPos, BlockPos maxPos, BlockState blockState)
	{
		int xSize = Math.abs(maxPos.getX() - minPos.getX());
		int zSize = Math.abs(maxPos.getZ() - minPos.getZ());
		boolean facingX = (maxPos.getX() - minPos.getX()) > 0;
		boolean facingZ = (maxPos.getZ() - minPos.getZ()) > 0;
		Direction dir;
		if(facingX)
		{
			dir = facingZ ? Direction.EAST : Direction.NORTH;
		} else
		{
			dir = facingZ ? Direction.SOUTH : Direction.WEST;
		}
		
		placeSpiralStaircase(world, boundingBox, minPos, xSize, zSize, maxPos.getY(), dir, blockState);
	}
	
	/**
	 * Places a spiral staircase that goes clockwise within the given bounds.
	 * The top block will be placed at height maxY.
	 * Placed blocks are not currently rotated, meaning that stair blocks currently aren't supported properly.
	 */
	public static void placeSpiralStaircase(ISeedReader world, MutableBoundingBox boundingBox, BlockPos startPos, int xSize, int zSize, int maxY, Direction startDirection, BlockState blockState)
	{
		BlockPos pos = startPos;
		Direction direction = startDirection;
		
		while(true)
		{
			int hSize = direction.getAxis() == Direction.Axis.X ? xSize : zSize;
			
			for(int i = 0; i < hSize; i++)
			{
				if(pos.getY() > maxY)
					return;
				
				if(boundingBox.isInside(pos))
				{
					world.setBlock(pos, blockState, Constants.BlockFlags.BLOCK_UPDATE);
				}
				
				pos = pos.relative(direction).above(); //relative was offset
			}
			
			direction = direction.getClockWise();
		}
	}
	
	/**
	 * bottomPos is at the bottom and to the left, with the width increasing how many blocks it is 90 degrees clockwise from the direction
	 */
	public static void createStairs(ISeedReader worldIn, MutableBoundingBox structurebb, BlockState bodyBlockState, BlockState tipBlockState, BlockPos bottomPos, int height, int width, Direction direction, boolean flipped)
	{
		//TODO create flipped option, where the stairs generate upside down
		
		BlockPos backEdge = bottomPos.relative(direction, height - 1).relative(direction.getClockWise(), width - 1);
		
		for(int y = 0; y < height; ++y)
		{
			BlockPos frontEdge = bottomPos.above(y).relative(direction, y);
			
			fillWithBlocksFromPos(worldIn, structurebb, bodyBlockState, frontEdge, backEdge.above(y));
			fillWithBlocksFromPos(worldIn, structurebb, tipBlockState, frontEdge, frontEdge.relative(direction.getClockWise(), width - 1));
		}
	}
	
	/**
	 * skipReplaceBlockState takes in one blockstate that will not be overwritten by the sphere
	 */
	public static void createSphere(ISeedReader worldIn, MutableBoundingBox structurebb, BlockState blockState, BlockPos centerPos, int radius, BlockState skipReplaceBlockState)
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
					
					if(structurebb.isInside(currentPos) && Math.sqrt(centerPos.distSqr(currentPos)) <= radius && worldIn.getBlockState(currentPos) != skipReplaceBlockState)
					{
						worldIn.setBlock(currentPos, blockState, Constants.BlockFlags.BLOCK_UPDATE);
					}
				}
			}
		}
	}
	
	/**
	 * Cylinder is upright
	 */
	public static void createCylinder(ISeedReader worldIn, MutableBoundingBox structurebb, BlockState blockState, BlockPos bottomPos, int radius, int height)
	{
		for(int y = bottomPos.getY(); y <= bottomPos.getY() + height - 1; ++y)
		{
			for(int x = bottomPos.getX() - radius; x <= bottomPos.getX() + radius; ++x)
			{
				for(int z = bottomPos.getZ() - radius; z <= bottomPos.getZ() + radius; ++z)
				{
					BlockPos currentPos = new BlockPos(x, y, z);
					if(structurebb.isInside(currentPos) && Math.sqrt(currentPos.distSqr(bottomPos.above(y - bottomPos.getY()))) <= radius)
					{
						worldIn.setBlock(currentPos, blockState, Constants.BlockFlags.BLOCK_UPDATE);
					}
				}
			}
		}
	}
	
	/**
	 * fills an area with blocks from one blockpos to another, but only places blocks a certain distance from one another
	 */
	public static void fillAsGrid(ISeedReader worldIn, MutableBoundingBox structurebb, BlockState blockState, BlockPos minBlockPos, BlockPos maxBlockPos, int toNextBlock)
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
					if(structurebb.isInside(currentPos))
					{
						worldIn.setBlock(currentPos, blockState, Constants.BlockFlags.BLOCK_UPDATE);
					}
				}
			}
		}
	}
	
	/**
	 * Built in use of axisAlignBlockPos
	 */
	public static void fillWithBlocksFromPos(ISeedReader worldIn, MutableBoundingBox structurebb, BlockState blockState, BlockPos pos1, BlockPos pos2)
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
					if(structurebb.isInside(currentPos))
					{
						worldIn.setBlock(currentPos, blockState, Constants.BlockFlags.BLOCK_UPDATE);
					}
				}
			}
		}
	}
	
	/*public static void fillWithAirCheckWater(ISeedReader worldIn, MutableBoundingBox structurebb, int minX, int minY, int minZ, int maxX, int maxY, int maxZ)
	{
		for(int y = minY; y <= maxY; ++y)
		{
			for(int x = minX; x <= maxX; ++x)
			{
				for(int z = minZ; z <= maxZ; ++z)
				{
					BlockPos pos = new BlockPos(this.getXWithOffset(x, z), this.getYWithOffset(y), this.getZWithOffset(x, z));
					if(structurebb.isInside(pos) && !this.getBlockStateFromPos(worldIn, x, y, z, structurebb).getFluidState().getFluid().isEquivalentTo(Fluids.WATER)) //ensures that the chunk is loaded before attempted to remove block, setBlockState already does this check
						worldIn.removeBlock(pos, false);
				}
			}
		}
	}*/
	
	/**
	 * normal trimmed down fill command except that it waterlogs blocks if it is replacing water, blockState parameter must be waterloggable
	 */
	public static void fillWithBlocksCheckWater(ISeedReader worldIn, MutableBoundingBox boundingboxIn, int xMin, int yMin, int zMin, int xMax, int yMax, int zMax, BlockState blockState)
	{
		for(int y = yMin; y <= yMax; ++y)
		{
			for(int x = xMin; x <= xMax; ++x)
			{
				for(int z = zMin; z <= zMax; ++z)
				{
					BlockPos currentPos = new BlockPos(x, y, z);
					//Debug.debugf("fillWithBlocksCheckWater. currentPos = %s, bb = %s", currentPos, boundingboxIn);
					if(boundingboxIn.isInside(currentPos))
					{
						if(worldIn.getBlockState(currentPos).getFluidState().getType().isSame(Fluids.WATER)) //has no inside vs outside blockstates or existingOnly
							blockState = blockState.setValue(BlockStateProperties.WATERLOGGED, true); //only works with waterloggable blocks
						worldIn.setBlock(currentPos, blockState, Constants.BlockFlags.BLOCK_UPDATE);
					}
				}
			}
		}
	}
	
	/**
	 * normal trimmed down fill command that only places blocks when it can replace another specified blockstate
	 */
	public static void fillWithBlocksReplaceState(ISeedReader worldIn, MutableBoundingBox boundingboxIn, BlockPos minBlockPosIn, BlockPos maxBlockPosIn, BlockState replacementBlockState, BlockState replacedBlockState)
	{
		BlockPos minBlockPos = axisAlignBlockPosGetMin(minBlockPosIn, maxBlockPosIn);
		BlockPos maxBlockPos = axisAlignBlockPosGetMax(minBlockPosIn, maxBlockPosIn);
		
		for(int y = minBlockPos.getY(); y <= maxBlockPos.getY(); ++y)
		{
			for(int x = minBlockPos.getX(); x <= maxBlockPos.getX(); ++x)
			{
				for(int z = minBlockPos.getZ(); z <= maxBlockPos.getZ(); ++z)
				{
					BlockPos currentPos = new BlockPos(x, y, z);
					if(boundingboxIn.isInside(currentPos) && worldIn.getBlockState(currentPos) == replacedBlockState)
					{
						worldIn.setBlock(currentPos, replacementBlockState, Constants.BlockFlags.BLOCK_UPDATE);
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