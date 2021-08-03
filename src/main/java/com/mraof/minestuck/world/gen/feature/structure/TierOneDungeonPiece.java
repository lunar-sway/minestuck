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
		super(MSStructurePieces.TIER_ONE_DUNGEON, random, x, 64, z, 50, 50, 50);
		
		int posHeightPicked = Integer.MAX_VALUE;
		for(int xPos = boundingBox.minX; xPos <= boundingBox.maxX; xPos++)
			for(int zPos = boundingBox.minZ; zPos <= boundingBox.maxZ; zPos++)
			{
				int posHeight = generator.getHeight(xPos, zPos, Heightmap.Type.OCEAN_FLOOR_WG); //posHeight picks the first solid block, ignoring water
				posHeightPicked = Math.min(posHeightPicked, posHeight); //with each new x/z coord it checks whether or not it is lower than the previous
			}
		boundingBox.offset(0, posHeightPicked + 5 - boundingBox.minY, 0); //takes the lowest Ocean Floor gen viable height + 5
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
		buildIndoorBlocks(worldIn, boundingBoxIn);
		
		return true;
	}
	
	private void buildStructureFoundation(IWorld world, MutableBoundingBox boundingBox)
	{
		fillWithBlocks(world, boundingBox,
				entryRoomMinX, entryRoomMinY, entryRoomMinZ,
				entryRoomMaxX, entryRoomMaxY, entryRoomMaxZ,
				primaryBlock, air, false); //exposed entry structure
		fillWithBlocks(world, boundingBox,
				entryRoomMinX - 5, entryRoomMinY - 12, entryRoomMinZ - 5,
				entryRoomMaxX + 5, entryRoomMinY, entryRoomMaxZ + 5,
				primaryBlock, primaryBlock, false); //chamber below-connected to entry, not filled with air because of thick floor
	}
	
	private void buildWallsAndFloors(IWorld world, MutableBoundingBox boundingBox, Random rand)
	{
		BlockPos doorInterfacePos = new BlockPos(getXWithOffset(entryRoomMinX + 5, entryRoomMinZ), getYWithOffset(entryRoomMinY + 4), getZWithOffset(entryRoomMinX + 5, entryRoomMinZ));
		if(boundingBox.isVecInside(doorInterfacePos) || getBoundingBox().isVecInside(doorInterfacePos))
		{
			Debug.debugf("buildWallsAndFloors. doorInterfacePos = %s", doorInterfacePos);
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
		}
		
		fillWithBlocks(world, boundingBox,
				entryRoomMinX + 6, entryRoomMinY + 1, entryRoomMinZ,
				entryRoomMaxX - 6, entryRoomMaxY - 6, entryRoomMinZ,
				MSBlocks.DUNGEON_DOOR.getDefaultState(), MSBlocks.DUNGEON_DOOR.getDefaultState(), false);
		
		fillWithBlocks(world, boundingBox,
				entryRoomMinX - 4, entryRoomMinY, entryRoomMinZ - 4,
				entryRoomMaxX + 4, entryRoomMinY, entryRoomMaxZ + 4,
				secondaryBlock, air, false); //floor
	}
	
	private void buildIndoorBlocks(IWorld world, MutableBoundingBox boundingBox)
	{
		BlockPos staircaseMinPos = new BlockPos(getXWithOffset(entryRoomMinX + 6, entryRoomMinZ + 6), getYWithOffset(entryRoomMinY - 10), getZWithOffset(entryRoomMinX + 6, entryRoomMinZ + 6));
		BlockPos staircaseMaxPos = new BlockPos(getXWithOffset(entryRoomMaxX - 6, entryRoomMaxZ - 6), getYWithOffset(entryRoomMinY + 40), getZWithOffset(entryRoomMaxX - 6, entryRoomMaxZ - 6));
		
		Debug.debugf("buildIndoorBlocks. staircaseMinPos = %s, isPosMinInsideBounding = %s, boundingBox = %s, getBoundingBox = %s", staircaseMinPos, boundingBox.isVecInside(staircaseMinPos), boundingBox, getBoundingBox());
		//PlacementFunctionsUtil.fillWithBlocksFromPos(world, boundingBox, secondaryDecorativeBlock, PlacementFunctionsUtil.axisAlignBlockPosGetMin(staircaseMinPos, staircaseMaxPos).up(30), PlacementFunctionsUtil.axisAlignBlockPosGetMax(staircaseMinPos, staircaseMaxPos).up(30));
		PlacementFunctionsUtil.createPlainSpiralStaircase(
				PlacementFunctionsUtil.axisAlignBlockPosGetMin(staircaseMinPos, staircaseMaxPos),
				PlacementFunctionsUtil.axisAlignBlockPosGetMax(staircaseMinPos, staircaseMaxPos),
				primaryDecorativeBlock, world, boundingBox, getBoundingBox());
		
		fillWithBlocks(world, boundingBox, entryRoomMinX + 8, entryRoomMinY - 11, entryRoomMinZ + 8, entryRoomMaxX - 8, entryRoomMinY - 10, entryRoomMaxZ - 8, fluid, fluid, false);
		PlacementFunctionsUtil.fillWithBlocksCheckWater(world, boundingBox,
				getXWithOffset(entryRoomMinX + 9, entryRoomMinZ + 9), getYWithOffset(entryRoomMinY - 11), getZWithOffset(entryRoomMinX + 9, entryRoomMinZ + 9),
				getXWithOffset(entryRoomMaxX - 9, entryRoomMaxZ - 9), getYWithOffset(entryRoomMinY - 11), getZWithOffset(entryRoomMaxX - 9, entryRoomMaxZ - 9),
				MSBlocks.MINI_FROG_STATUE.getDefaultState());
	}
	
	private void carveRooms(IWorld world, MutableBoundingBox boundingBox)
	{
		//BlockPos startChamberMinCarve = new BlockPos(aboveGroundMin.getX() + 2, aboveGroundMin.getY() + 1, aboveGroundMin.getZ() + 2);
		//BlockPos startChamberMaxCarve = new BlockPos(aboveGroundMax.getX() - 2, aboveGroundMax.getY() - 2, aboveGroundMax.getZ() - 2);
		//BlockPos lowerChamberMinCarve = new BlockPos(lowerChamberMin.getX() - 5, lowerChamberMin.getY() - 10, lowerChamberMin.getZ() - 5);
		//BlockPos lowerChamberMaxCarve = new BlockPos(lowerChamberMax.getX() + 5, lowerChamberMax.getY() - 1, lowerChamberMax.getZ() + 5);
		
		fillWithAir(world, boundingBox, entryRoomMinX + 6, entryRoomMinY, entryRoomMinZ + 6, entryRoomMaxX - 6, entryRoomMinY, entryRoomMaxZ - 6); //stairway hole
		fillWithAir(world, boundingBox, entryRoomMinX - 4, entryRoomMinY - 9, entryRoomMinZ - 4, entryRoomMaxX + 4, entryRoomMinY - 1, entryRoomMaxZ + 4); //lower secondary chamber
		//fillWithBlocksFromPos(world, boundingBox, Blocks.AIR.getDefaultState(), startChamberMinCarve, startChamberMaxCarve); //top chamber carver
		//fillWithBlocksFromPos(world, boundingBox, Blocks.AIR.getDefaultState(), lowerChamberMinCarve, lowerChamberMaxCarve); //second chamber carver
	}
}