package com.mraof.minestuck.world.gen.feature.structure.castle;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.levelgen.structure.StructurePiece;
import net.minecraft.world.level.levelgen.structure.StructurePieceAccessor;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePieceSerializationContext;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePieceType;

import java.util.Random;

public abstract class CastlePiece extends StructurePiece
{
	protected boolean isBlack;
	protected int direction;
	
	protected CastlePiece(StructurePieceType pieceType, int genDepth, BoundingBox boundingBox, boolean isBlack)
	{
		super(pieceType, genDepth, boundingBox);
		this.isBlack = isBlack;
		setOrientation(Direction.SOUTH);
		this.direction = 0;
	}
	
	protected CastlePiece(StructurePieceType type, CompoundTag nbt)
	{
		super(type, nbt);
		isBlack = nbt.getBoolean("isBlack");
	}
	
	@Override
	protected void addAdditionalSaveData(StructurePieceSerializationContext context, CompoundTag nbt)
	{
		nbt.putBoolean("isBlack", isBlack);
	}
	
	protected StructurePiece getNextComponentNormal(
			CastleStartPiece castleStartPiece, StructurePieceAccessor accessor, Random random, int xShift, int zShift, boolean par6)
	{
		return getNextComponentNormal(castleStartPiece, accessor, random, xShift, 0, zShift);
	}

	protected StructurePiece getNextComponentNormal(
			CastleStartPiece castleStartPiece, StructurePieceAccessor accessor, Random random, int xShift, int yShift, int zShift)
	{
			return this.getNextComponent(castleStartPiece, accessor, random, this.boundingBox.minX() + xShift, this.boundingBox.minY() + yShift, this.boundingBox.minZ() + zShift, this.direction, this.getGenDepth());
	}

	protected StructurePiece getNextComponent(
			CastleStartPiece castleStartPiece, StructurePieceAccessor accessor, Random par3Random, int i, int j, int k, int coordBaseMode, int componentType)
	{
		return StructureCastlePieces.getNextValidComponent(castleStartPiece, accessor, par3Random, i, j, k, coordBaseMode, componentType);
	}
	/**
	 * Discover the y coordinate that will serve as the ground level of the supplied BoundingBox. (A median of all the
	 * levels in the BB's horizontal rectangle).
	 */
	protected int getAverageGroundLevel(WorldGenLevel level, BoundingBox par2StructureBoundingBox)
	{
		int var3 = 0;
		int var4 = 0;

		for (int var5 = this.boundingBox.minZ(); var5 <= this.boundingBox.maxZ(); ++var5)
		{
			for (int var6 = this.boundingBox.minX(); var6 <= this.boundingBox.maxX(); ++var6)
			{
				if (par2StructureBoundingBox.isInside(new BlockPos(var6, 64, var5)))	//isVecInside
				{
					var3 += level.getHeight(Heightmap.Types.MOTION_BLOCKING, var6, var5);
					++var4;
				}
			}
		}
		
		if (var4 == 0)
		{
			return -1;
		}
		else
		{
			if(var3 / var4 - 8 < 255)
				return var3 / var4;
			else
				return 255 - 8;
		}
	}
	
	protected void fillWithAlternatingBlocks(WorldGenLevel level, BoundingBox boundingBox, int x1, int y1, int z1, int x2, int y2, int z2, BlockState block1, BlockState block2, boolean b)
	{
		for (int y = y1; y <= y2; ++y)
		{
			for (int x = x1; x <= x2; ++x)
			{
				for (int z = z1; z <= z2; ++z)
				{
					if(((x + y + z) % 2 == 0) ^ b)
					{
//						Debug.print("Placing block at " + x + " " + y + " " + z + " " + blockID + " " + metadata1);
						this.placeBlock(level, block1, x, y, z, boundingBox);	//placeBlockAtCurrentPosition
					}
					else
					{
						this.placeBlock(level, block2, x, y, z, boundingBox);
//						Debug.print("Placing block at " + x + " " + y + " " + z + " " + blockID2 + " " + metadata2);
					}
				}
			}
		}
	}
}