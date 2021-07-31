package com.mraof.minestuck.world.gen.feature.structure;

import com.mraof.minestuck.block.*;
import com.mraof.minestuck.tileentity.DungeonDoorInterfaceTileEntity;
import com.mraof.minestuck.world.gen.feature.MSStructurePieces;
import net.minecraft.block.BlockState;
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
	public TierOneDungeonPiece(ChunkGenerator<?> generator, Random random, int x, int z)
	{
		super(MSStructurePieces.TIER_ONE_DUNGEON, random, x, 64, z, 42, 100, 70);
		
		int posHeightPicked = Integer.MAX_VALUE;
		for(int xPos = boundingBox.minX; xPos <= boundingBox.maxX; xPos++)
			for(int zPos = boundingBox.minZ; zPos <= boundingBox.maxZ; zPos++)
			{
				int posHeight = generator.getHeight(xPos, zPos, Heightmap.Type.OCEAN_FLOOR_WG); //posHeight picks the first solid block, ignoring water
				posHeightPicked = Math.min(posHeightPicked, posHeight); //with each new x/z coord it checks whether or not it is lower than the previous
			}
		
		boundingBox.offset(0, posHeightPicked - boundingBox.minY, 0); //takes the lowest Ocean Floor gen viable height
	}
	
	public TierOneDungeonPiece(TemplateManager templates, CompoundNBT nbt)
	{
		super(MSStructurePieces.TIER_ONE_DUNGEON, nbt);
	}
	
	@Override
	public boolean create(IWorld worldIn, ChunkGenerator<?> chunkGeneratorIn, Random randomIn, MutableBoundingBox boundingBoxIn, ChunkPos chunkPosIn)
	{
		BlockState wallBlock = MSBlocks.GREEN_STONE_BRICKS.getDefaultState();
		BlockState columnBlock = MSBlocks.GREEN_STONE_COLUMN.getDefaultState().with(MSDirectionalBlock.FACING, Direction.UP);
		BlockState floorBlock = MSBlocks.POLISHED_GREEN_STONE.getDefaultState();
		BlockState stoneBlock = MSBlocks.GREEN_STONE.getDefaultState();
		
		buildWallsAndFloors(floorBlock, worldIn, boundingBoxIn, randomIn);
		carveRooms(worldIn, boundingBoxIn);
		//buildIndoorBlocks(columnBlock, worldIn, boundingBoxIn);
		
		return true;
	}
	
	private void buildWallsAndFloors(BlockState block, IWorld world, MutableBoundingBox boundingBox, Random rand)
	{
		fillWithBlocks(world, boundingBox, 0, 0, 0, 20, 20, 20, block, block, false);
		
		BlockPos doorInterfacePos = new BlockPos(this.getXWithOffset(1, 0), this.getYWithOffset(10), this.getZWithOffset(1, 0));
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
		
		fillWithBlocks(world, boundingBox, 2, 1, 0, 19, 19, 0, MSBlocks.DUNGEON_DOOR.getDefaultState(), block, false);
	}
	
	private void buildIndoorBlocks(BlockState block, IWorld world, MutableBoundingBox boundingBox)
	{
	
	}
	
	private void carveRooms(IWorld world, MutableBoundingBox boundingBox)
	{
		fillWithAir(world, boundingBox, 1, 1, 1, 19, 19, 19);
	}
	
	/*protected void fillWithDungeonDoorBlocks(IWorld worldIn, MutableBoundingBox boundingboxIn, int xMin, int yMin, int zMin, int xMax, int yMax, int zMax, BlockState blockState)
	{
		for(int y = yMin; y <= yMax; ++y)
		{
			for(int x = xMin; x <= xMax; ++x)
			{
				for(int z = zMin; z <= zMax; ++z)
				{
					blockState = blockState.with(DungeonDoorInterfaceBlock.KEY, EnumKeyType.TIER_ONE);
					this.setBlockState(worldIn, blockState, x, y, z, boundingboxIn);
				}
			}
		}
	}*/
}