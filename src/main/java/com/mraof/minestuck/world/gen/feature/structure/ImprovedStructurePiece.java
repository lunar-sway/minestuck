package com.mraof.minestuck.world.gen.feature.structure;

import com.mraof.minestuck.block.ReturnNodeBlock;
import net.minecraft.block.*;
import net.minecraft.fluid.FluidState;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.state.properties.BedPart;
import net.minecraft.state.properties.DoorHingeSide;
import net.minecraft.state.properties.DoubleBlockHalf;
import net.minecraft.util.Direction;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MutableBoundingBox;
import net.minecraft.world.ISeedReader;
import net.minecraft.world.IWorld;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.Heightmap;
import net.minecraft.world.gen.feature.structure.IStructurePieceType;
import net.minecraft.world.gen.feature.structure.StructurePiece;
import net.minecraftforge.common.util.Constants;

import java.util.Objects;
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
	
	public void setRandomDirection(Random random)
	{
		setOrientation(Direction.Plane.HORIZONTAL.getRandomDirection(random));
	}
	
	public void setBoundsWithWorldHeight(ChunkGenerator generator, int x, int z, int width, int height, int depth, int yOffset, Heightmap.Type type)
	{
		int y = generator.getBaseHeight(x, z, type) + yOffset;
		setBounds(x, y, z, width, height, depth);
	}
	
	public void setBounds(int x, int y, int z, int width, int height, int depth)
	{
		if (Objects.requireNonNull(getOrientation()).getAxis() == Direction.Axis.Z)
			this.boundingBox = new MutableBoundingBox(x, y, z, x + width - 1, y + height - 1, z + depth - 1);
		else
			this.boundingBox = new MutableBoundingBox(x, y, z, x + depth - 1, y + height - 1, z + width - 1);
	}
	
	protected void generateDoor(ISeedReader worldIn, MutableBoundingBox sbb, Random rand, int x, int y, int z, Direction direction, Block door, DoorHingeSide hinge)
	{
		BlockState state = door.defaultBlockState().setValue(DoorBlock.FACING, direction).setValue(DoorBlock.HINGE, hinge);
		placeBlock(worldIn, state, x, y, z, sbb);
		placeBlock(worldIn, state.setValue(DoorBlock.HALF, DoubleBlockHalf.UPPER), x, y + 1, z, sbb);
	}
	
	protected void generateBed(ISeedReader worldIn, MutableBoundingBox sbb, Random rand, int x, int y, int z, Direction direction, BlockState state)
	{
		state = state.setValue(BedBlock.FACING, direction);
		placeBlock(worldIn, state, x, y, z, sbb);
		placeBlock(worldIn, state.setValue(BedBlock.PART, BedPart.HEAD), x + direction.getStepX(), y, z + direction.getStepZ(), sbb);
	}
	
	protected void placeReturnNode(IWorld world, MutableBoundingBox boundingBox, int x, int y, int z)
	{
		int posX = getWorldX(x, z), posY = getWorldY(y), posZ = getWorldZ(x, z);
		
		if(getOrientation() == Direction.NORTH || getOrientation() == Direction.WEST)
			posX--;
		if(getOrientation() == Direction.NORTH || getOrientation() == Direction.EAST)
			posZ--;
		
		ReturnNodeBlock.placeReturnNode(world, new BlockPos(posX, posY, posZ), boundingBox);
	}
	
	protected int getAverageGroundLevel(IWorld worldIn, ChunkGenerator chunkGeneratorIn, MutableBoundingBox structurebb)
	{
		int i = 0;
		int j = 0;
		BlockPos.Mutable mutablePos = new BlockPos.Mutable();
		
		for (int k = boundingBox.z0; k <= boundingBox.z1; ++k)
		{
			for (int l = boundingBox.x0; l <= boundingBox.x1; ++l)
			{
				mutablePos.set(l, 64, k);
				
				if (structurebb.isInside(mutablePos))
				{
					i += Math.max(worldIn.getHeightmapPos(Heightmap.Type.WORLD_SURFACE_WG, mutablePos).getY(),
							chunkGeneratorIn.getSpawnHeight() - 1);
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
	
	protected BlockPos getActualPos(int x, int y, int z)
	{
		return new BlockPos(getWorldX(x, z), getWorldY(y), getWorldZ(x, z));
	}
	
	@Override
	protected int getWorldX(int x, int z)
	{
		Direction direction = getOrientation();
		
		if (direction == null)
			return x;
		else switch (direction)
		{
			case NORTH:
				return boundingBox.x1 - x;
			case SOUTH:
				return boundingBox.x0 + x;
			case WEST:
				return boundingBox.x1 - z;
			case EAST:
				return boundingBox.x0 + z;
			default:
				return x;
		}
	}
	
	@Override
	protected int getWorldZ(int x, int z)
	{
		Direction direction = getOrientation();
		
		if (direction == null)
			return z;
		else switch (direction)
		{
			case NORTH:
				return boundingBox.z1 - z;
			case SOUTH:
				return boundingBox.z0 + z;
			case WEST:
				return boundingBox.z0 + x;
			case EAST:
				return boundingBox.z1 - x;
			default:
				return z;
		}
	}
	
	protected boolean needPostprocessing(Block block)
	{
		return block instanceof FourWayBlock || block instanceof TorchBlock || block instanceof LadderBlock || block instanceof StairsBlock;
	}
	
	
	@Override
	protected void placeBlock(ISeedReader worldIn, BlockState blockstateIn, int x, int y, int z, MutableBoundingBox boundingboxIn)
	{
		BlockPos blockpos = new BlockPos(getWorldX(x, z), getWorldY(y), getWorldZ(x, z));
		
		if (boundingboxIn.isInside(blockpos))
		{
			Direction direction = getOrientation();
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
			
			worldIn.setBlock(blockpos, blockstateIn, Constants.BlockFlags.BLOCK_UPDATE);
			
			FluidState ifluidstate = worldIn.getFluidState(blockpos);
			if(!ifluidstate.isEmpty())
				worldIn.getLiquidTicks().scheduleTick(blockpos, ifluidstate.getType(), 0);
			
			if(needPostprocessing(blockstateIn.getBlock()))
				worldIn.getChunk(blockpos).markPosForPostprocessing(blockpos);
		}
	}
}
