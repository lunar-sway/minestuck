package com.mraof.minestuck.world.gen.feature.structure;

import com.mraof.minestuck.block.*;
import com.mraof.minestuck.tileentity.DungeonDoorInterfaceTileEntity;
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
	private BlockPos aboveGroundMin;
	private BlockPos aboveGroundMax;
	private BlockPos lowerChamberMin;
	private BlockPos lowerChamberMax;
	
	public TierOneDungeonPiece(ChunkGenerator<?> generator, Random random, int x, int z)
	{
		super(MSStructurePieces.TIER_ONE_DUNGEON, random, x, 64, z, 42, 60, 60);
		
		int posHeightPicked = Integer.MAX_VALUE;
		int posXPicked = boundingBox.minX;
		int posZPicked = boundingBox.minZ;
		for(int xPos = boundingBox.minX; xPos <= boundingBox.maxX; xPos++)
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
		
		aboveGroundMin = new BlockPos(boundingBoxIn.minX + 10, boundingBoxIn.minY, boundingBoxIn.minZ + 10);
		aboveGroundMax = new BlockPos(boundingBoxIn.minX + 20, boundingBoxIn.minY + 20, boundingBoxIn.minZ + 20);
		lowerChamberMin = new BlockPos(aboveGroundMin.getX() - 5, aboveGroundMin.getY() - 10, aboveGroundMin.getZ() - 5);
		lowerChamberMax = new BlockPos(aboveGroundMin.getX() + 5, aboveGroundMin.getY() - 1, aboveGroundMin.getZ() + 5);
		
		BlockState primaryBlock = blocks.getBlockState("structure_primary");
		BlockState primaryDecorativeBlock = blocks.getBlockState("structure_primary_decorative");
		BlockState secondaryBlock = blocks.getBlockState("structure_secondary");
		BlockState secondaryDecorativeBlock = blocks.getBlockState("structure_secondary_decorative");
		BlockState fluid = blocks.getBlockState("fall_fluid");
		
		buildStructureFoundation(primaryBlock, worldIn, boundingBoxIn);
		buildWallsAndFloors(secondaryBlock, worldIn, boundingBoxIn, randomIn);
		carveRooms(worldIn, boundingBoxIn);
		//buildIndoorBlocks(columnBlock, worldIn, boundingBoxIn);
		
		return true;
	}
	
	private void buildStructureFoundation(BlockState block, IWorld world, MutableBoundingBox boundingBox)
	{
		//fillWithBlocks(world, boundingBox, aboveGroundMin.getX(), aboveGroundMin.getY(), aboveGroundMin.getZ(), aboveGroundMax.getX(), aboveGroundMax.getY(), aboveGroundMax.getZ(), block, block, false);
		fillWithBlocksFromPos(world, boundingBox, block, aboveGroundMin, aboveGroundMax); //exposed entry structure
		BlockPos lowerChamberMin = new BlockPos(aboveGroundMin.getX() - 5, aboveGroundMin.getY() - 10, aboveGroundMin.getZ() - 5);
		BlockPos lowerChamberMax = new BlockPos(aboveGroundMin.getX() + 5, aboveGroundMin.getY() - 1, aboveGroundMin.getZ() + 5);
		fillWithBlocksFromPos(world, boundingBox, block, lowerChamberMin, lowerChamberMax); //chamber below-connected to entry
	}
	
	private void buildWallsAndFloors(BlockState block, IWorld world, MutableBoundingBox boundingBox, Random rand)
	{
		BlockPos doorInterfacePos = new BlockPos(this.getXWithOffset(aboveGroundMin.getX() + 2, aboveGroundMin.getZ()), this.getYWithOffset(10), this.getZWithOffset(aboveGroundMin.getX() + 2, aboveGroundMin.getZ()));
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
		
		//fillWithBlocks(world, boundingBox, aboveGroundMin.getX() + 3, 1, 0, aboveGroundMax.getX() - 3, 19, 0, MSBlocks.DUNGEON_DOOR.getDefaultState(), block, false);
		BlockPos doorMin = new BlockPos(aboveGroundMin.getX() + 3, aboveGroundMin.getY() + 1, aboveGroundMin.getZ());
		BlockPos doorMax = new BlockPos(aboveGroundMax.getX() - 3, aboveGroundMax.getY() - 1, aboveGroundMax.getZ());
		fillWithBlocksFromPos(world, boundingBox, MSBlocks.DUNGEON_DOOR.getDefaultState(), doorMin, doorMax);
	}
	
	private void buildIndoorBlocks(BlockState block, IWorld world, MutableBoundingBox boundingBox)
	{
	
	}
	
	private void carveRooms(IWorld world, MutableBoundingBox boundingBox)
	{
		BlockPos startChamberMinCarve = new BlockPos(aboveGroundMin.getX() + 2, aboveGroundMin.getY() + 1, aboveGroundMin.getZ() + 2);
		BlockPos startChamberMaxCarve = new BlockPos(aboveGroundMax.getX() - 2, aboveGroundMax.getY() - 2, aboveGroundMax.getZ() - 2);
		BlockPos lowerChamberMinCarve = new BlockPos(lowerChamberMin.getX() - 5, lowerChamberMin.getY() - 10, lowerChamberMin.getZ() - 5);
		BlockPos lowerChamberMaxCarve = new BlockPos(lowerChamberMax.getX() + 5, lowerChamberMax.getY() - 1, lowerChamberMax.getZ() + 5);
		
		fillWithBlocksFromPos(world, boundingBox, Blocks.AIR.getDefaultState(), startChamberMinCarve, startChamberMaxCarve); //top chamber carver
		fillWithBlocksFromPos(world, boundingBox, Blocks.AIR.getDefaultState(), lowerChamberMinCarve, lowerChamberMaxCarve); //second chamber carver
	}
	
	protected void fillWithBlocksFromPos(IWorld worldIn, MutableBoundingBox structurebb, BlockState blockState, BlockPos minBlockPos, BlockPos maxBlockPos)
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
	}
}