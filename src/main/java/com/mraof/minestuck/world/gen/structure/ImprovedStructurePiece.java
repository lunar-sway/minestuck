package com.mraof.minestuck.world.gen.structure;

import com.mraof.minestuck.block.ReturnNodeBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BedPart;
import net.minecraft.world.level.block.state.properties.DoorHingeSide;
import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.levelgen.structure.StructurePiece;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePieceType;
import net.minecraft.world.level.material.FluidState;

public abstract class ImprovedStructurePiece extends StructurePiece
{
	protected ImprovedStructurePiece(StructurePieceType structurePieceTypeIn, int genDepth, BoundingBox boundingBox)
	{
		super(structurePieceTypeIn, genDepth, boundingBox);
	}
	
	public ImprovedStructurePiece(StructurePieceType structurePierceTypeIn, CompoundTag nbt)
	{
		super(structurePierceTypeIn, nbt);
	}
	
	protected void generateDoor(WorldGenLevel level, BoundingBox sbb, RandomSource rand, int x, int y, int z, Direction direction, Block door, DoorHingeSide hinge)
	{
		BlockState state = door.defaultBlockState().setValue(DoorBlock.FACING, direction).setValue(DoorBlock.HINGE, hinge);
		placeBlock(level, state, x, y, z, sbb);
		placeBlock(level, state.setValue(DoorBlock.HALF, DoubleBlockHalf.UPPER), x, y + 1, z, sbb);
	}
	
	protected void generateBed(WorldGenLevel level, BoundingBox sbb, RandomSource rand, int x, int y, int z, Direction direction, BlockState state)
	{
		state = state.setValue(BedBlock.FACING, direction);
		placeBlock(level, state, x, y, z, sbb);
		placeBlock(level, state.setValue(BedBlock.PART, BedPart.HEAD), x + direction.getStepX(), y, z + direction.getStepZ(), sbb);
	}
	
	protected void placeReturnNode(LevelAccessor level, BoundingBox boundingBox, int x, int y, int z)
	{
		int posX = getWorldX(x, z), posY = getWorldY(y), posZ = getWorldZ(x, z);
		
		if(getOrientation() == Direction.NORTH || getOrientation() == Direction.WEST)
			posX--;
		if(getOrientation() == Direction.NORTH || getOrientation() == Direction.EAST)
			posZ--;
		
		ReturnNodeBlock.placeReturnNode(level, new BlockPos(posX, posY, posZ), boundingBox);
	}
	
	protected int getAverageGroundLevel(LevelAccessor level, ChunkGenerator chunkGeneratorIn, BoundingBox structurebb)
	{
		int i = 0;
		int j = 0;
		BlockPos.MutableBlockPos mutablePos = new BlockPos.MutableBlockPos();
		
		for (int k = boundingBox.minZ(); k <= boundingBox.maxZ(); ++k)
		{
			for (int l = boundingBox.minX(); l <= boundingBox.maxX(); ++l)
			{
				mutablePos.set(l, 64, k);
				
				if (structurebb.isInside(mutablePos))
				{
					i += Math.max(level.getHeightmapPos(Heightmap.Types.WORLD_SURFACE_WG, mutablePos).getY(),
							chunkGeneratorIn.getSpawnHeight(level) - 1);
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
	protected int getWorldX(int x, int z)
	{
		Direction direction = getOrientation();
		
		if (direction == null)
			return x;
		else switch (direction)
		{
			case NORTH:
				return boundingBox.maxX() - x;
			case SOUTH:
				return boundingBox.minX() + x;
			case WEST:
				return boundingBox.maxX() - z;
			case EAST:
				return boundingBox.minX() + z;
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
				return boundingBox.maxZ() - z;
			case SOUTH:
				return boundingBox.minZ() + z;
			case WEST:
				return boundingBox.minZ() + x;
			case EAST:
				return boundingBox.maxZ() - x;
			default:
				return z;
		}
	}
	
	protected boolean needPostprocessing(Block block)
	{
		return block instanceof CrossCollisionBlock || block instanceof TorchBlock || block instanceof LadderBlock || block instanceof StairBlock || block instanceof WallBlock;
	}
	
	
	@Override
	protected void placeBlock(WorldGenLevel level, BlockState blockstateIn, int x, int y, int z, BoundingBox boundingboxIn)
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
			
			level.setBlock(blockpos, blockstateIn, Block.UPDATE_CLIENTS);
			
			FluidState fluid = level.getFluidState(blockpos);
			if(!fluid.isEmpty())
				level.scheduleTick(blockpos, fluid.getType(), 0);
			
			if(needPostprocessing(blockstateIn.getBlock()))
				level.getChunk(blockpos).markPosForPostprocessing(blockpos);
		}
	}
	
}
