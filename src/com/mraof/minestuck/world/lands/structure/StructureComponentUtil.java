package com.mraof.minestuck.world.lands.structure;

import net.minecraft.block.BlockBed;
import net.minecraft.block.BlockDoor;
import net.minecraft.block.BlockHorizontal;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.gen.structure.StructureBoundingBox;
import net.minecraft.world.gen.structure.StructureComponent;

import java.util.Random;

public abstract class StructureComponentUtil extends StructureComponent
{
	protected void generateDoor(World worldIn, StructureBoundingBox sbb, Random rand, int x, int y, int z, EnumFacing facing, BlockDoor door, BlockDoor.EnumHingePosition hinge)
	{
		IBlockState state = door.getDefaultState().withProperty(BlockHorizontal.FACING, facing).withProperty(BlockDoor.HINGE, hinge);
		setBlockState(worldIn, state, x, y, z, sbb);
		setBlockState(worldIn, state.withProperty(BlockDoor.HALF, BlockDoor.EnumDoorHalf.UPPER), x, y + 1, z, sbb);
	}
	
	protected void generateBed(World worldIn, StructureBoundingBox sbb, Random rand, int x, int y, int z, EnumFacing facing, IBlockState state)
	{
		state = state.withProperty(BlockHorizontal.FACING, facing);
		setBlockState(worldIn, state, x, y, z, sbb);
		setBlockState(worldIn, state.withProperty(BlockBed.PART, BlockBed.EnumPartType.HEAD), x + facing.getFrontOffsetX(), y, z + facing.getFrontOffsetZ(), sbb);
	}
	
	protected int getAverageGroundLevel(World worldIn, StructureBoundingBox structurebb)
	{
		int i = 0;
		int j = 0;
		BlockPos.MutableBlockPos blockpos$mutableblockpos = new BlockPos.MutableBlockPos();
		
		for (int k = boundingBox.minZ; k <= boundingBox.maxZ; ++k)
		{
			for (int l = boundingBox.minX; l <= boundingBox.maxX; ++l)
			{
				blockpos$mutableblockpos.setPos(l, 64, k);
				
				if (structurebb.isVecInside(blockpos$mutableblockpos))
				{
					i += Math.max(worldIn.getTopSolidOrLiquidBlock(blockpos$mutableblockpos).getY(),
							worldIn.provider.getAverageGroundLevel() - 1);
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
		EnumFacing enumfacing = getCoordBaseMode();
		
		if (enumfacing == null)
			return x;
		else switch (enumfacing)
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
		EnumFacing enumfacing = getCoordBaseMode();
		
		if (enumfacing == null)
			return z;
		else switch (enumfacing)
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
	protected void setBlockState(World worldIn, IBlockState blockstateIn, int x, int y, int z, StructureBoundingBox boundingboxIn)
	{
		BlockPos blockpos = new BlockPos(getXWithOffset(x, z), getYWithOffset(y), getZWithOffset(x, z));
		
		if (boundingboxIn.isVecInside(blockpos))
		{
			EnumFacing facing = getCoordBaseMode();
			switch(facing)
			{
				case NORTH:
					blockstateIn = blockstateIn.withRotation(Rotation.CLOCKWISE_180);
					break;
				case WEST:
					blockstateIn = blockstateIn.withRotation(Rotation.CLOCKWISE_90);
					break;
				case EAST:
					blockstateIn = blockstateIn.withRotation(Rotation.COUNTERCLOCKWISE_90);
					break;
				default:
			}
			
			worldIn.setBlockState(blockpos, blockstateIn, 2);
		}
	}
}
