package com.mraof.minestuck.world.gen.feature.structure;

import com.mraof.minestuck.block.*;
import com.mraof.minestuck.tileentity.DungeonDoorInterfaceTileEntity;
import com.mraof.minestuck.util.Debug;
import com.mraof.minestuck.util.PlacementFunctionsUtil;
import com.mraof.minestuck.world.gen.feature.MSStructurePieces;
import com.mraof.minestuck.world.gen.feature.structure.blocks.StructureBlockRegistry;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.fluid.Fluids;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.MutableBoundingBox;
import net.minecraft.world.IWorld;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.Heightmap;
import net.minecraft.world.gen.feature.structure.ScatteredStructurePiece;
import net.minecraft.world.gen.feature.template.TemplateManager;
import net.minecraftforge.common.util.Constants;

import java.util.Random;

public class TierOneDungeonPiece extends ScatteredStructurePiece
{
	/*private BlockPos aboveGroundMin;
	private BlockPos aboveGroundMax;
	private BlockPos lowerChamberMin;
	private BlockPos lowerChamberMax;*/
	private final int entryRoomMinX = 10;
	private final int entryRoomMinY = 0;
	private final int entryRoomMinZ = 10;
	private final int entryRoomMaxX = 30;
	private final int entryRoomMaxY = 12;
	private final int entryRoomMaxZ = 30;
	private final BlockState air = Blocks.AIR.getDefaultState();
	private BlockState primaryBlock;
	private BlockState primaryDecorativeBlock;
	private BlockState secondaryBlock;
	private BlockState secondaryDecorativeBlock;
	private BlockState fluid;
	
	public TierOneDungeonPiece(ChunkGenerator<?> generator, Random random, int x, int z)
	{
		super(MSStructurePieces.TIER_ONE_DUNGEON, random, x, 64, z, 42, 30, 30);
		
		int posHeightPicked = Integer.MAX_VALUE;
		int posXPicked = boundingBox.minX;
		int posZPicked = boundingBox.minZ;
		for(int xPos = boundingBox.minX; xPos <= boundingBox.maxX; xPos++)
		{
			for(int zPos = boundingBox.minZ; zPos <= boundingBox.maxZ; zPos++)
			{
				int posHeight = generator.getHeight(xPos, zPos, Heightmap.Type.OCEAN_FLOOR_WG); //posHeight picks the first solid block, ignoring water
				if(posHeight <= posHeightPicked)
				{
					posXPicked = xPos;
					posZPicked = zPos;
				}
				posHeightPicked = Math.min(posHeightPicked, posHeight); //with each new x/z coord it checks whether or not it is lower than the previous
			}
		}
		
		boundingBox.offset(posXPicked - boundingBox.minX, posHeightPicked - boundingBox.minY, posZPicked - boundingBox.minZ); //takes the lowest Ocean Floor gen viable height
		//boundingBox.offset(posXPicked - boundingBox.minX, posHeightPicked - boundingBox.minY, posZPicked - boundingBox.minZ); //takes the lowest Ocean Floor gen viable height
	}
	
	public TierOneDungeonPiece(TemplateManager templates, CompoundNBT nbt)
	{
		super(MSStructurePieces.TIER_ONE_DUNGEON, nbt);
	}
	
	@Override
	public boolean create(IWorld worldIn, ChunkGenerator<?> chunkGeneratorIn, Random randomIn, MutableBoundingBox boundingBoxIn, ChunkPos chunkPosIn)
	{
		StructureBlockRegistry blocks = StructureBlockRegistry.getOrDefault(chunkGeneratorIn.getSettings());
		
		primaryBlock = blocks.getBlockState("structure_primary");
		primaryDecorativeBlock = blocks.getBlockState("structure_primary_decorative");
		secondaryBlock = blocks.getBlockState("structure_secondary");
		secondaryDecorativeBlock = blocks.getBlockState("structure_secondary_decorative");
		fluid = blocks.getBlockState("fall_fluid");
		
		buildStructureFoundation(worldIn, boundingBoxIn);
		buildWallsAndFloors(worldIn, boundingBoxIn, randomIn);
		carveRooms(worldIn, boundingBoxIn);
		//buildIndoorBlocks(columnBlock, worldIn, boundingBoxIn);
		
		return true;
	}
	
	private void buildStructureFoundation(IWorld world, MutableBoundingBox boundingBox)
	{
		fillWithBlocks(world, boundingBox, entryRoomMinX, entryRoomMinY, entryRoomMinZ, entryRoomMaxX, entryRoomMaxY, entryRoomMaxZ, primaryBlock, air, false); //exposed entry structure
		fillWithBlocks(world, boundingBox, entryRoomMinX - 5, entryRoomMinY - 10, entryRoomMinZ - 5, entryRoomMaxX + 5, entryRoomMinY, entryRoomMaxZ + 5, primaryBlock, air, false); //chamber below-connected to entry
	}
	
	private void buildWallsAndFloors(IWorld world, MutableBoundingBox boundingBox, Random rand)
	{
		BlockPos doorInterfacePos = new BlockPos(getXWithOffset(entryRoomMinX + 5, entryRoomMinZ), getYWithOffset(entryRoomMinY + 4), getZWithOffset(entryRoomMinX + 5, entryRoomMinZ));
		Debug.debugf("doorInterfacePos = %s", doorInterfacePos);
		BlockState doorInterfaceBlockState = MSBlocks.DUNGEON_DOOR_INTERFACE.getDefaultState();
		doorInterfaceBlockState.createTileEntity(world);
		world.setBlockState(doorInterfacePos, doorInterfaceBlockState, Constants.BlockFlags.BLOCK_UPDATE);
		TileEntity interfaceTE = world.getTileEntity(doorInterfacePos);
		if(!(interfaceTE instanceof DungeonDoorInterfaceTileEntity) && interfaceTE != null)
		{
			interfaceTE = new DungeonDoorInterfaceTileEntity();
			world.getWorld().setTileEntity(doorInterfacePos, interfaceTE);
		}
		if(interfaceTE != null)
		{
			((DungeonDoorInterfaceTileEntity) interfaceTE).setKey(EnumKeyType.tier_1_key);
		} else
			throw new IllegalStateException("Unable to create a new dungeon door interface tile entity. Returned null!");
		
		fillWithBlocks(world, boundingBox, entryRoomMinX + 6, entryRoomMinY + 1, entryRoomMinZ, entryRoomMaxX - 3, entryRoomMaxY - 6, entryRoomMinZ, MSBlocks.DUNGEON_DOOR.getDefaultState(), MSBlocks.DUNGEON_DOOR.getDefaultState(), false);
		
		fillWithBlocks(world, boundingBox, entryRoomMinX + 1, entryRoomMinY, entryRoomMinZ + 1, entryRoomMaxX - 1, entryRoomMinY, entryRoomMaxZ - 1, secondaryBlock, air, false); //floor
		
		//BlockPos staircaseMinPos = new BlockPos(getXWithOffset(entryRoomMinX + 6, entryRoomMinZ), getYWithOffset(entryRoomMinY - 10), getZWithOffset(entryRoomMinX + 6, entryRoomMinZ));
		//BlockPos staircaseMaxPos = new BlockPos(getXWithOffset(entryRoomMaxX - 6, entryRoomMaxZ), getYWithOffset(entryRoomMinY), getZWithOffset(entryRoomMaxX - 6, entryRoomMaxZ));
		BlockPos staircaseMinPos = new BlockPos(getXWithOffset(entryRoomMinX + 6, entryRoomMinZ), getYWithOffset(entryRoomMinY - 10), getZWithOffset(entryRoomMinX + 6, entryRoomMinZ));
		BlockPos staircaseMaxPos = new BlockPos(getXWithOffset(entryRoomMaxX - 6, entryRoomMaxZ), getYWithOffset(entryRoomMinY), getZWithOffset(entryRoomMaxX - 6, entryRoomMaxZ));
		//int staircaseMinX = getXWithOffset(entryRoomMinX + 2, entryRoomMinZ);
		//int staircaseMinY = getYWithOffset(entryRoomMinY);
		//int staircaseMinZ = getZWithOffset(entryRoomMinX + 2, entryRoomMinZ);
		if(boundingBox.)
		PlacementFunctionsUtil.createPlainSpiralStaircase(staircaseMinPos, staircaseMaxPos, primaryDecorativeBlock, world, boundingBox);
	}
	
	private void buildIndoorBlocks(BlockState block, IWorld world, MutableBoundingBox boundingBox)
	{
	
	}
	
	private void carveRooms(IWorld world, MutableBoundingBox boundingBox)
	{
		//BlockPos startChamberMinCarve = new BlockPos(aboveGroundMin.getX() + 2, aboveGroundMin.getY() + 1, aboveGroundMin.getZ() + 2);
		//BlockPos startChamberMaxCarve = new BlockPos(aboveGroundMax.getX() - 2, aboveGroundMax.getY() - 2, aboveGroundMax.getZ() - 2);
		//BlockPos lowerChamberMinCarve = new BlockPos(lowerChamberMin.getX() - 5, lowerChamberMin.getY() - 10, lowerChamberMin.getZ() - 5);
		//BlockPos lowerChamberMaxCarve = new BlockPos(lowerChamberMax.getX() + 5, lowerChamberMax.getY() - 1, lowerChamberMax.getZ() + 5);
		
		//fillWithBlocksFromPos(world, boundingBox, Blocks.AIR.getDefaultState(), startChamberMinCarve, startChamberMaxCarve); //top chamber carver
		//fillWithBlocksFromPos(world, boundingBox, Blocks.AIR.getDefaultState(), lowerChamberMinCarve, lowerChamberMaxCarve); //second chamber carver
	}
	
	/*protected void fillWithBlocksFromPos(IWorld worldIn, MutableBoundingBox structurebb, BlockState blockState, BlockPos minBlockPos, BlockPos maxBlockPos)
	{
		for(int y = minBlockPos.getY(); y <= maxBlockPos.getY(); ++y)
		{
			for(int x = minBlockPos.getX(); x <= maxBlockPos.getX(); ++x)
			{
				for(int z = minBlockPos.getZ(); z <= maxBlockPos.getZ(); ++z)
				{
					this.setBlockState(worldIn, blockState, x, y, z, structurebb);
				}
			}
		}
	}*/
}