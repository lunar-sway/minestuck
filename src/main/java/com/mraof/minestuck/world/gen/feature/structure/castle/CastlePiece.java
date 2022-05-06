package com.mraof.minestuck.world.gen.feature.structure.castle;

import net.minecraft.block.BlockState;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MutableBoundingBox;
import net.minecraft.world.ISeedReader;
import net.minecraft.world.gen.Heightmap;
import net.minecraft.world.gen.feature.structure.IStructurePieceType;
import net.minecraft.world.gen.feature.structure.StructurePiece;

import java.util.List;
import java.util.Random;

public abstract class CastlePiece extends StructurePiece
{
	protected boolean isBlack;
	protected int direction;
	
	protected CastlePiece(IStructurePieceType pieceType, int componentType, boolean isBlack)
	{
		super(pieceType, componentType);
		this.boundingBox = new MutableBoundingBox(0, 0, 0, 256, 7, 256);
		this.isBlack = isBlack;
		setOrientation(Direction.SOUTH);
		this.direction = 0;
	}
	
	protected CastlePiece(IStructurePieceType structurePierceTypeIn, CompoundNBT nbt)
	{
		super(structurePierceTypeIn, nbt);
		isBlack = nbt.getBoolean("isBlack");
	}
	
	@Override
	protected void addAdditionalSaveData(CompoundNBT nbt)
	{
		nbt.putBoolean("isBlack", isBlack);
	}
	
	protected StructurePiece getNextComponentNormal(
			CastleStartPiece castleStartPiece, List<StructurePiece> components, Random random, int xShift, int zShift, boolean par6)
	{
		return getNextComponentNormal(castleStartPiece, components, random, xShift, 0, zShift);
	}

	protected StructurePiece getNextComponentNormal(
			CastleStartPiece castleStartPiece, List<StructurePiece> components, Random random, int xShift, int yShift, int zShift)
	{
			return this.getNextComponent(castleStartPiece, components, random, this.boundingBox.x0 + xShift, this.boundingBox.y0 + yShift, this.boundingBox.z0 + zShift, this.direction, this.getGenDepth());
	}

	protected StructurePiece getNextComponent(
			CastleStartPiece castleStartPiece, List<StructurePiece> par2List, Random par3Random, int i, int j, int k, int coordBaseMode, int componentType)
	{
		return StructureCastlePieces.getNextValidComponent(castleStartPiece, par2List, par3Random, i, j, k, coordBaseMode, componentType);
	}
	/**
	 * Discover the y coordinate that will serve as the ground level of the supplied BoundingBox. (A median of all the
	 * levels in the BB's horizontal rectangle).
	 */
	protected int getAverageGroundLevel(ISeedReader par1World, MutableBoundingBox par2StructureBoundingBox)
	{
		int var3 = 0;
		int var4 = 0;

		for (int var5 = this.boundingBox.z0; var5 <= this.boundingBox.z1; ++var5)
		{
			for (int var6 = this.boundingBox.x0; var6 <= this.boundingBox.x1; ++var6)
			{
				if (par2StructureBoundingBox.isInside(new BlockPos(var6, 64, var5)))	//isVecInside
				{
					var3 += par1World.getHeight(Heightmap.Type.MOTION_BLOCKING, var6, var5);
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
	
	protected void fillWithAlternatingBlocks(ISeedReader world, MutableBoundingBox structureboundingbox, int x1, int y1, int z1, int x2, int y2, int z2, BlockState block1, BlockState block2, boolean b)
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
						this.placeBlock(world, block1, x, y, z, structureboundingbox);	//placeBlockAtCurrentPosition
					}
					else
					{
						this.placeBlock(world, block2, x, y, z, structureboundingbox);
//						Debug.print("Placing block at " + x + " " + y + " " + z + " " + blockID2 + " " + metadata2);
					}
				}
			}
		}
	}
	protected int getAverageGroundLevel(ISeedReader world)
	{
		return this.getAverageGroundLevel(world, this.boundingBox);
	}
	
}
