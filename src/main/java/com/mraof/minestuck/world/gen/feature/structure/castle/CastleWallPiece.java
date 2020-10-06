package com.mraof.minestuck.world.gen.feature.structure.castle;

import com.mraof.minestuck.block.MSBlocks;
import com.mraof.minestuck.world.gen.feature.MSStructurePieces;
import net.minecraft.block.BlockState;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.MutableBoundingBox;
import net.minecraft.world.ISeedReader;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.feature.structure.StructureManager;
import net.minecraft.world.gen.feature.structure.StructurePiece;
import net.minecraft.world.gen.feature.template.TemplateManager;

import java.util.List;
import java.util.Random;

public class CastleWallPiece extends CastlePiece
{
	boolean cornerPiece;
	
	protected CastleWallPiece(boolean isBlack, MutableBoundingBox structureboundingbox, int direction, boolean cornerPiece)
	{
		super(MSStructurePieces.SKAIA_CASTLE_WALL, 1, isBlack);
		this.boundingBox = structureboundingbox;
		this.direction = direction;
		this.cornerPiece = cornerPiece;
	}
	
	public CastleWallPiece(TemplateManager templates, CompoundNBT nbt)
	{
		super(MSStructurePieces.SKAIA_CASTLE_WALL, nbt);
	}
	
	@Override
	protected void readAdditional(CompoundNBT nbt)
	{
	}
	
	@Override
	public void buildComponent(StructurePiece componentIn, List<StructurePiece> pieces, Random rand)
	{
		CastleStartPiece startPiece = (CastleStartPiece) componentIn;
//		if(Math.abs(this.startPiece.x - this.boundingBox.minX) >= this.startPiece.castleWidth && ((this.direction & 1) == 0) || (this.startPiece.z - this.boundingBox.minZ >= this.startPiece.castleLength && ((this.direction & 1) == 1)))
//			direction++;
		boolean incrementDirection = false;
		switch(this.direction)
		{
		case 0:
			if(this.boundingBox.minX - startPiece.x >= startPiece.castleWidth)incrementDirection = true;
			break;
		case 1:
			if(Math.abs(startPiece.z - this.boundingBox.minZ) >= startPiece.castleLength)incrementDirection = true;
			break;
		case 2:
			if(startPiece.x - this.boundingBox.minX >= startPiece.castleWidth)incrementDirection = true;
			break;
		case 3:
			if(this.boundingBox.minZ <= startPiece.z)incrementDirection = true;
			break;
		case 4:
			if(this.boundingBox.minX >= startPiece.x)incrementDirection = true;
			break;
		}
		if(incrementDirection)
		{
			direction++;
			this.cornerPiece = true;
		}
		if(startPiece.bottom)
		{
			this.componentType = 0;
			this.getNextComponentNormal(startPiece, pieces, rand, 0, -8, 0);
		}
		if(this.direction == 5 && startPiece.castleLength > 16 && startPiece.castleWidth > 8)
		{
			this.componentType = 3;
			for(int depth = 8; depth < startPiece.castleLength; depth += 8)
				for(int row = -startPiece.castleWidth + 8; row < startPiece.castleWidth; row += 8)
					this.getNextComponentNormal(startPiece, pieces, rand,  row, depth, true);//TODO change this so it generates the whole floor smartly
			this.cornerPiece = false;
			startPiece.z += 8;
			startPiece.castleWidth -= 8;
			startPiece.castleLength -= 16;
			startPiece.averageGroundLevel += 8;
			startPiece.bottom = false;
			this.direction = 0;
			this.boundingBox.offset(0, 8, 8);
		}
		this.componentType = 1;
		switch(this.direction)
		{
		case 4:
		case 0:
			this.getNextComponentNormal(startPiece, pieces, rand,  8, 0, true);
			break;
		case 1:
			this.getNextComponentNormal(startPiece, pieces, rand, 0, 8, true);
			break;
		case 2:
			this.getNextComponentNormal(startPiece, pieces, rand,  -8, 0, true);
			break;
		case 3:
			this.getNextComponentNormal(startPiece, pieces, rand, 0, -8, true);
			break;
		default:
//			Debug.print("Wall done");
		}
		this.componentType = 3;
//		if(!this.cornerPiece)
//			if((this.direction & 3) == 0)
				
	}

	public static CastleWallPiece findValidPlacement(boolean isBlack, int x, int y, int z, int par5, boolean cornerPiece)
	{
		MutableBoundingBox structureboundingbox = new MutableBoundingBox(x, y, z, x + 8, y + 8, z + 8);
		return new CastleWallPiece(isBlack, structureboundingbox, par5, cornerPiece);
	}
	
	@Override
	public boolean func_230383_a_(ISeedReader world, StructureManager manager, ChunkGenerator generator, Random random, MutableBoundingBox structureBoundingBox, ChunkPos chunkPosIn, BlockPos pos)
	{
		
		BlockState chessTile = (isBlack ? MSBlocks.BLACK_CHESS_DIRT : MSBlocks.WHITE_CHESS_DIRT).getDefaultState();
		BlockState chessTile1 = (isBlack ? MSBlocks.DARK_GRAY_CHESS_DIRT : MSBlocks.LIGHT_GRAY_CHESS_DIRT).getDefaultState();
		if (this.isLiquidInStructureBoundingBox(world, structureBoundingBox))
		{
			return false;
		}
		else
		{
			if(!(this.direction == 5 && this.cornerPiece))
			{
				this.fillWithAlternatingBlocks(world, structureBoundingBox, 0, 0, 0, 7 ,6, 7, chessTile, chessTile1, false);
				this.fillWithAir(world, structureBoundingBox, 0, 7, 0, 7, 6, 7);
			}
			if(!this.cornerPiece)
				switch(this.direction)
				{
				case 0:
				case 2:
				case 4:
					this.fillWithAlternatingBlocks(world, structureBoundingBox, 0, 0, 0, 7 ,7, 0, chessTile, chessTile1, false);
					this.fillWithAlternatingBlocks(world, structureBoundingBox, 0, 0, 7, 7 ,7, 7, chessTile, chessTile1, false);
					break;
				case 1:
				case 3:
					this.fillWithAlternatingBlocks(world, structureBoundingBox, 0, 7, 0, 0 ,7, 7, chessTile, chessTile1, false);
					this.fillWithAlternatingBlocks(world, structureBoundingBox, 7, 7, 0, 7 ,7, 7, chessTile, chessTile1, false);
					break;
				default:
					this.fillWithAlternatingBlocks(world, structureBoundingBox, 0, 7, 0, 7 ,7, 0, chessTile, chessTile1, false);
					this.fillWithAlternatingBlocks(world, structureBoundingBox, 0, 7, 7, 7 ,7, 7, chessTile, chessTile1, false);
					this.fillWithAlternatingBlocks(world, structureBoundingBox, 0, 7, 0, 0 ,7, 7, chessTile, chessTile1, false);
					this.fillWithAlternatingBlocks(world, structureBoundingBox, 7, 7, 0, 7 ,7, 7, chessTile, chessTile1, false);
				}
			else 
				switch(this.direction)
				{
				case 0:
				case 4:
					this.fillWithAlternatingBlocks(world, structureBoundingBox, 0, 0, 0, 7 ,7, 0, chessTile, chessTile1, false);
					this.fillWithAlternatingBlocks(world, structureBoundingBox, 0, 7, 0, 0 ,7, 7, chessTile, chessTile1, false);
					break;
				case 2:
					this.fillWithAlternatingBlocks(world, structureBoundingBox, 0, 0, 7, 7 ,7, 7, chessTile, chessTile1, false);
					this.fillWithAlternatingBlocks(world, structureBoundingBox, 7, 7, 0, 7 ,7, 7, chessTile, chessTile1, false);
					break;
				case 1:
					this.fillWithAlternatingBlocks(world, structureBoundingBox, 7, 7, 0, 7 ,7, 7, chessTile, chessTile1, false);
					this.fillWithAlternatingBlocks(world, structureBoundingBox, 0, 0, 0, 7 ,7, 0, chessTile, chessTile1, false);
					break;
				case 3:
					this.fillWithAlternatingBlocks(world, structureBoundingBox, 0, 7, 0, 0 ,7, 7, chessTile, chessTile1, false);
					this.fillWithAlternatingBlocks(world, structureBoundingBox, 0, 0, 7, 7 ,7, 7, chessTile, chessTile1, false);
					break;
				default:
					this.fillWithAlternatingBlocks(world, structureBoundingBox, 0, 7, 0, 7 ,7, 0, chessTile, chessTile1, false);
					this.fillWithAlternatingBlocks(world, structureBoundingBox, 0, 7, 7, 7 ,7, 7, chessTile, chessTile1, false);
					this.fillWithAlternatingBlocks(world, structureBoundingBox, 0, 7, 0, 0 ,7, 7, chessTile, chessTile1, false);
					this.fillWithAlternatingBlocks(world, structureBoundingBox, 7, 7, 0, 7 ,7, 7, chessTile, chessTile1, false);
				}
				return true;
		}
	}
}