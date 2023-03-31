package com.mraof.minestuck.world.gen.structure.castle;

import com.mraof.minestuck.block.MSBlocks;
import com.mraof.minestuck.world.gen.structure.MSStructurePieces;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.StructureManager;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.levelgen.structure.StructurePiece;
import net.minecraft.world.level.levelgen.structure.StructurePieceAccessor;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePieceType;

public class CastleRoomPiece extends CastlePiece
{
	protected CastleRoomPiece(boolean isBlack, BoundingBox boundingBox)
	{
		super(MSStructurePieces.SKAIA_CASTLE_ROOM.get(), 2, boundingBox, isBlack);
	}
	
	public CastleRoomPiece(CompoundTag nbt)
	{
		super(MSStructurePieces.SKAIA_CASTLE_ROOM.get(), nbt);
	}
	
	protected CastleRoomPiece(StructurePieceType pieceType, boolean isBlack, BoundingBox boundingBox)
	{
		super(pieceType, 2, boundingBox, isBlack);
	}
	
	protected CastleRoomPiece(StructurePieceType pieceType, CompoundTag nbt)
	{
		super(pieceType, nbt);
	}
	
	@Override
	public void addChildren(StructurePiece componentIn, StructurePieceAccessor accessor, RandomSource rand)
	{
		if(((CastleStartPiece)componentIn).bottom)
		{
			this.getNextComponentNormal((CastleStartPiece)componentIn, accessor, rand, 0, -8, 0, StructureCastlePieces.Type.SOLID);
		}
	}

	public static CastleRoomPiece createRandomRoom(boolean isBlack, boolean bottom, int x, int y, int z, RandomSource random)
	{
		CastleRoomPiece piece;
		switch(random.nextInt(30))
		{
		case 0:
			if(!bottom)
				piece = CastleStaircasePiece.findValidPlacement(isBlack, x, y, z);
			else
				piece = findValidPlacement(isBlack, x, y, z);
			break;
		case 1:
			piece = CastleLibraryPiece.findValidPlacement(isBlack, x, y, z);
			break;
		default:
			piece = findValidPlacement(isBlack, x, y, z);
		}
		return piece;
	}

	public static CastleRoomPiece findValidPlacement(boolean isBlack, int x, int y, int z)
	{
		BoundingBox structureboundingbox = new BoundingBox(x, y, z, x + 8, y + 8, z + 8);
		return new CastleRoomPiece(isBlack, structureboundingbox);
	}
	
	@Override
	public void postProcess(WorldGenLevel level, StructureManager manager, ChunkGenerator generator, RandomSource random, BoundingBox boundingBox, ChunkPos chunkPosIn, BlockPos pos)
	{
		BlockState chessTile = (isBlack ? MSBlocks.BLACK_CHESS_DIRT.get() : MSBlocks.WHITE_CHESS_DIRT.get()).defaultBlockState();
		BlockState chessTile1 = (isBlack ? MSBlocks.DARK_GRAY_CHESS_DIRT.get() : MSBlocks.LIGHT_GRAY_CHESS_DIRT.get()).defaultBlockState();
		
		this.fillWithAlternatingBlocks(level, boundingBox, 0, 0, 0, 7 ,0, 7, chessTile,  chessTile1, false);
		this.fillWithAlternatingBlocks(level, boundingBox, 0, 7, 0, 7 ,7, 7, chessTile,  chessTile1, false);
		this.generateBox(level, boundingBox, 0, 1, 0, 7, 6, 7, Blocks.AIR.defaultBlockState(), Blocks.AIR.defaultBlockState(), false);
		this.placeBlock(level, Blocks.TORCH.defaultBlockState(), 3, 1, 3, boundingBox);
	}
}