package com.mraof.minestuck.world.gen.feature.structure;

import net.minecraft.block.BedBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.DoorBlock;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.state.properties.BedPart;
import net.minecraft.state.properties.DoorHingeSide;
import net.minecraft.state.properties.DoubleBlockHalf;
import net.minecraft.util.Direction;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MutableBoundingBox;
import net.minecraft.world.IWorld;
import net.minecraft.world.gen.Heightmap;
import net.minecraft.world.gen.feature.structure.IStructurePieceType;
import net.minecraft.world.gen.feature.structure.StructurePiece;

import java.util.Random;

public abstract class ImprovedStructurePiece extends StructurePiece
{
	protected ImprovedStructurePiece(IStructurePieceType structurePieceTypeIn, int componentTypeIn)
	{
		super(structurePieceTypeIn, componentTypeIn);
	}
	
	public ImprovedStructurePiece(IStructurePieceType structurePierceTypeIn, CompoundNBT nbt)
	{
		super(structurePierceTypeIn, nbt);
	}
	
	protected void generateDoor(IWorld worldIn, MutableBoundingBox sbb, Random rand, int x, int y, int z, Direction direction, Block door, DoorHingeSide hinge)
	{
		BlockState state = door.getDefaultState().with(DoorBlock.FACING, direction).with(DoorBlock.HINGE, hinge);
		setBlockState(worldIn, state, x, y, z, sbb);
		setBlockState(worldIn, state.with(DoorBlock.HALF, DoubleBlockHalf.UPPER), x, y + 1, z, sbb);
	}
	
	protected void generateBed(IWorld worldIn, MutableBoundingBox sbb, Random rand, int x, int y, int z, Direction direction, BlockState state)
	{
		state = state.with(BedBlock.HORIZONTAL_FACING, direction);
		setBlockState(worldIn, state, x, y, z, sbb);
		setBlockState(worldIn, state.with(BedBlock.PART, BedPart.HEAD), x + direction.getXOffset(), y, z + direction.getZOffset(), sbb);
	}
	
	protected int getAverageGroundLevel(IWorld worldIn, MutableBoundingBox structurebb)
	{
		int i = 0;
		int j = 0;
		BlockPos.MutableBlockPos mutablePos = new BlockPos.MutableBlockPos();
		
		for (int k = boundingBox.minZ; k <= boundingBox.maxZ; ++k)
		{
			for (int l = boundingBox.minX; l <= boundingBox.maxX; ++l)
			{
				mutablePos.setPos(l, 64, k);
				
				if (structurebb.isVecInside(mutablePos))
				{
					i += Math.max(worldIn.getHeight(Heightmap.Type.WORLD_SURFACE_WG, mutablePos).getY(),
							worldIn.getChunkProvider().getChunkGenerator().getGroundHeight() - 1);
					++j;
				}
			}
		}
		
		if (j == 0)
		{
			return -1;
		} else
		{
			return i / j;
		}
	}
	
	@Override
	protected int getXWithOffset(int x, int z)
	{
		Direction direction = getCoordBaseMode();
		
		if (direction == null)
			return x;
		else switch (direction)
		{
			case NORTH:
				return boundingBox.maxX - x;
			case SOUTH:
				return boundingBox.minX + x;
			case WEST:
				return boundingBox.maxX - z;
			case EAST:
				return boundingBox.minX + z;
			default:
				return x;
		}
	}
	
	@Override
	protected int getZWithOffset(int x, int z)
	{
		Direction direction = getCoordBaseMode();
		
		if (direction == null)
			return z;
		else switch (direction)
		{
			case NORTH:
				return boundingBox.maxZ - z;
			case SOUTH:
				return boundingBox.minZ + z;
			case WEST:
				return boundingBox.minZ + x;
			case EAST:
				return boundingBox.maxZ - x;
			default:
				return z;
		}
	}
	
	@Override
	protected void setBlockState(IWorld worldIn, BlockState blockstateIn, int x, int y, int z, MutableBoundingBox boundingboxIn)
	{
		BlockPos blockpos = new BlockPos(getXWithOffset(x, z), getYWithOffset(y), getZWithOffset(x, z));
		
		if (boundingboxIn.isVecInside(blockpos))
		{
			Direction direction = getCoordBaseMode();
			switch(direction)
			{
				case NORTH:
					blockstateIn = blockstateIn.rotate(Rotation.CLOCKWISE_180);
					break;
				case WEST:
					blockstateIn = blockstateIn.rotate(Rotation.CLOCKWISE_90);
					break;
				case EAST:
					blockstateIn = blockstateIn.rotate(Rotation.COUNTERCLOCKWISE_90);
					break;
				default:
			}
			
			worldIn.setBlockState(blockpos, blockstateIn, 2);
		}
	}
}
