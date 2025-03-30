package com.mraof.minestuck.world.gen.structure.castle;

import com.mraof.minestuck.block.SkaiaBlocks;
import com.mraof.minestuck.world.gen.structure.MSStructures;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.StructureManager;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.levelgen.structure.StructurePiece;
import net.minecraft.world.level.levelgen.structure.StructurePieceAccessor;

public class CastleWallPiece extends CastlePiece
{
	boolean cornerPiece;
	
	protected CastleWallPiece(boolean isBlack, BoundingBox boundingBox, int direction, boolean cornerPiece)
	{
		super(MSStructures.SkaiaCastle.WALL_PIECE.get(), 1, boundingBox, isBlack);
		this.direction = direction;
		this.cornerPiece = cornerPiece;
	}
	
	public CastleWallPiece(CompoundTag nbt)
	{
		super(MSStructures.SkaiaCastle.WALL_PIECE.get(), nbt);
	}
	
	@Override
	public void addChildren(StructurePiece componentIn, StructurePieceAccessor accessor, RandomSource rand)
	{
		CastleStartPiece startPiece = (CastleStartPiece) componentIn;
//		if(Math.abs(this.startPiece.x - this.boundingBox.minX) >= this.startPiece.castleWidth && ((this.direction & 1) == 0) || (this.startPiece.z - this.boundingBox.minZ >= this.startPiece.castleLength && ((this.direction & 1) == 1)))
//			direction++;
		boolean incrementDirection = false;
		switch(this.direction)
		{
			case 0:
				if(this.boundingBox.minX() - startPiece.x >= startPiece.castleWidth) incrementDirection = true;
				break;
			case 1:
				if(Math.abs(startPiece.z - this.boundingBox.minZ()) >= startPiece.castleLength)
					incrementDirection = true;
				break;
			case 2:
				if(startPiece.x - this.boundingBox.minX() >= startPiece.castleWidth) incrementDirection = true;
				break;
			case 3:
				if(this.boundingBox.minZ() <= startPiece.z) incrementDirection = true;
				break;
			case 4:
				if(this.boundingBox.minX() >= startPiece.x) incrementDirection = true;
				break;
		}
		if(incrementDirection)
		{
			direction++;
			this.cornerPiece = true;
		}
		if(startPiece.bottom)
		{
			this.getNextComponentNormal(startPiece, accessor, rand, 0, -8, 0, StructureCastlePieces.Type.SOLID);
		}
		if(this.direction == 5 && startPiece.castleLength > 16 && startPiece.castleWidth > 8)
		{
			for(int depth = 8; depth < startPiece.castleLength; depth += 8)
				for(int row = -startPiece.castleWidth + 8; row < startPiece.castleWidth; row += 8)
					this.getNextComponentNormal(startPiece, accessor, rand, row, depth, StructureCastlePieces.Type.RANDOM_ROOM);
			this.cornerPiece = false;
			startPiece.z += 8;
			startPiece.castleWidth -= 8;
			startPiece.castleLength -= 16;
			startPiece.averageGroundLevel += 8;
			startPiece.bottom = false;
			this.direction = 0;
			this.boundingBox.move(0, 8, 8);
		}
		
		switch(this.direction)
		{
			case 4:
			case 0:
				this.getNextComponentNormal(startPiece, accessor, rand, 8, 0, StructureCastlePieces.Type.WALL);
				break;
			case 1:
				this.getNextComponentNormal(startPiece, accessor, rand, 0, 8, StructureCastlePieces.Type.WALL);
				break;
			case 2:
				this.getNextComponentNormal(startPiece, accessor, rand, -8, 0, StructureCastlePieces.Type.WALL);
				break;
			case 3:
				this.getNextComponentNormal(startPiece, accessor, rand, 0, -8, StructureCastlePieces.Type.WALL);
				break;
			default:
		}
	}
	
	public static CastleWallPiece findValidPlacement(boolean isBlack, int x, int y, int z, int par5, boolean cornerPiece)
	{
		BoundingBox boundingBox = new BoundingBox(x, y, z, x + 8, y + 8, z + 8);
		return new CastleWallPiece(isBlack, boundingBox, par5, cornerPiece);
	}
	
	@Override
	public void postProcess(WorldGenLevel level, StructureManager manager, ChunkGenerator generator, RandomSource random, BoundingBox structureBoundingBox, ChunkPos chunkPosIn, BlockPos pos)
	{
		
		BlockState chessTile = (isBlack ? SkaiaBlocks.BLACK_CHESS_DIRT : SkaiaBlocks.WHITE_CHESS_DIRT).asBlock().defaultBlockState();
		BlockState chessTile1 = (isBlack ? SkaiaBlocks.DARK_GRAY_CHESS_DIRT : SkaiaBlocks.LIGHT_GRAY_CHESS_DIRT).asBlock().defaultBlockState();
		if(!(this.direction == 5 && this.cornerPiece))
		{
			this.fillWithAlternatingBlocks(level, structureBoundingBox, 0, 0, 0, 7, 6, 7, chessTile, chessTile1, false);
			this.generateAirBox(level, structureBoundingBox, 0, 7, 0, 7, 6, 7);
		}
		if(!this.cornerPiece)
			switch(this.direction)
			{
				case 0:
				case 2:
				case 4:
					this.fillWithAlternatingBlocks(level, structureBoundingBox, 0, 0, 0, 7, 7, 0, chessTile, chessTile1, false);
					this.fillWithAlternatingBlocks(level, structureBoundingBox, 0, 0, 7, 7, 7, 7, chessTile, chessTile1, false);
					break;
				case 1:
				case 3:
					this.fillWithAlternatingBlocks(level, structureBoundingBox, 0, 7, 0, 0, 7, 7, chessTile, chessTile1, false);
					this.fillWithAlternatingBlocks(level, structureBoundingBox, 7, 7, 0, 7, 7, 7, chessTile, chessTile1, false);
					break;
				default:
					this.fillWithAlternatingBlocks(level, structureBoundingBox, 0, 7, 0, 7, 7, 0, chessTile, chessTile1, false);
					this.fillWithAlternatingBlocks(level, structureBoundingBox, 0, 7, 7, 7, 7, 7, chessTile, chessTile1, false);
					this.fillWithAlternatingBlocks(level, structureBoundingBox, 0, 7, 0, 0, 7, 7, chessTile, chessTile1, false);
					this.fillWithAlternatingBlocks(level, structureBoundingBox, 7, 7, 0, 7, 7, 7, chessTile, chessTile1, false);
			}
		else
			switch(this.direction)
			{
				case 0:
				case 4:
					this.fillWithAlternatingBlocks(level, structureBoundingBox, 0, 0, 0, 7, 7, 0, chessTile, chessTile1, false);
					this.fillWithAlternatingBlocks(level, structureBoundingBox, 0, 7, 0, 0, 7, 7, chessTile, chessTile1, false);
					break;
				case 2:
					this.fillWithAlternatingBlocks(level, structureBoundingBox, 0, 0, 7, 7, 7, 7, chessTile, chessTile1, false);
					this.fillWithAlternatingBlocks(level, structureBoundingBox, 7, 7, 0, 7, 7, 7, chessTile, chessTile1, false);
					break;
				case 1:
					this.fillWithAlternatingBlocks(level, structureBoundingBox, 7, 7, 0, 7, 7, 7, chessTile, chessTile1, false);
					this.fillWithAlternatingBlocks(level, structureBoundingBox, 0, 0, 0, 7, 7, 0, chessTile, chessTile1, false);
					break;
				case 3:
					this.fillWithAlternatingBlocks(level, structureBoundingBox, 0, 7, 0, 0, 7, 7, chessTile, chessTile1, false);
					this.fillWithAlternatingBlocks(level, structureBoundingBox, 0, 0, 7, 7, 7, 7, chessTile, chessTile1, false);
					break;
				default:
					this.fillWithAlternatingBlocks(level, structureBoundingBox, 0, 7, 0, 7, 7, 0, chessTile, chessTile1, false);
					this.fillWithAlternatingBlocks(level, structureBoundingBox, 0, 7, 7, 7, 7, 7, chessTile, chessTile1, false);
					this.fillWithAlternatingBlocks(level, structureBoundingBox, 0, 7, 0, 0, 7, 7, chessTile, chessTile1, false);
					this.fillWithAlternatingBlocks(level, structureBoundingBox, 7, 7, 0, 7, 7, 7, chessTile, chessTile1, false);
			}
	}
}
