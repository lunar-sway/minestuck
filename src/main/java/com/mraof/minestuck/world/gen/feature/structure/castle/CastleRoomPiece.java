package com.mraof.minestuck.world.gen.feature.structure.castle;

import com.mraof.minestuck.block.MSBlocks;
import com.mraof.minestuck.world.gen.feature.MSStructurePieces;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.MutableBoundingBox;
import net.minecraft.world.IWorld;
import net.minecraft.world.gen.feature.structure.IStructurePieceType;
import net.minecraft.world.gen.feature.structure.StructurePiece;
import net.minecraft.world.gen.feature.template.TemplateManager;

import java.util.List;
import java.util.Random;

public class CastleRoomPiece extends CastlePiece
{
	protected CastleRoomPiece(boolean isBlack, MutableBoundingBox structureBoundingBox)
	{
		super(MSStructurePieces.SKAIA_CASTLE_ROOM, 2, isBlack);
		this.boundingBox = structureBoundingBox;
	}
	
	public CastleRoomPiece(TemplateManager templates, CompoundNBT nbt)
	{
		super(MSStructurePieces.SKAIA_CASTLE_ROOM, nbt);
	}
	
	protected CastleRoomPiece(IStructurePieceType pieceType, boolean isBlack, MutableBoundingBox structureBoundingBox)
	{
		super(pieceType, 2, isBlack);
		this.boundingBox = structureBoundingBox;
	}
	
	protected CastleRoomPiece(IStructurePieceType pieceType, CompoundNBT nbt)
	{
		super(pieceType, nbt);
	}
	
	@Override
	public void buildComponent(StructurePiece componentIn, List<StructurePiece> pieces, Random rand)
	{
		if(((CastleStartPiece)componentIn).bottom)
		{
			this.componentType = 0;
			this.getNextComponentNormal((CastleStartPiece)componentIn, pieces, rand, 0, -8, 0);
		}
	}

	public static CastleRoomPiece createRandomRoom(boolean isBlack, boolean bottom, int x, int y, int z, Random random)
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
		MutableBoundingBox structureboundingbox = new MutableBoundingBox(x, y, z, x + 8, y + 8, z + 8);
		return new CastleRoomPiece(isBlack, structureboundingbox);
	}
	
	@Override
	public boolean addComponentParts(IWorld world, Random random, MutableBoundingBox structureBoundingBox, ChunkPos chunkPosIn)
	{
		BlockState chessTile = (isBlack ? MSBlocks.BLACK_CHESS_DIRT : MSBlocks.WHITE_CHESS_DIRT).getDefaultState();
		BlockState chessTile1 = (isBlack ? MSBlocks.DARK_GRAY_CHESS_DIRT : MSBlocks.LIGHT_GRAY_CHESS_DIRT).getDefaultState();
		
		this.fillWithAlternatingBlocks(world, structureBoundingBox, 0, 0, 0, 7 ,0, 7, chessTile,  chessTile1, false);
		this.fillWithAlternatingBlocks(world, structureBoundingBox, 0, 7, 0, 7 ,7, 7, chessTile,  chessTile1, false);
		this.fillWithBlocks(world, structureBoundingBox, 0, 1, 0, 7, 6, 7, Blocks.AIR.getDefaultState(), Blocks.AIR.getDefaultState(), false);
		this.setBlockState(world, Blocks.TORCH.getDefaultState(), 3, 1, 3, structureBoundingBox);	//placeBlockAtCurrentPosition
		return true;
	}
}