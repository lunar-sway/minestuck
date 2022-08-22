package com.mraof.minestuck.world.gen.structure.castle;

import com.mraof.minestuck.block.MSBlocks;
import com.mraof.minestuck.world.gen.structure.MSStructurePieces;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.StructureFeatureManager;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.levelgen.structure.StructurePiece;
import net.minecraft.world.level.levelgen.structure.StructurePieceAccessor;

import java.util.Random;

public class CastleStaircasePiece extends CastleRoomPiece
{
	public CastleStaircasePiece(boolean isBlack, BoundingBox boundingBox)
	{
		super(MSStructurePieces.SKAIA_CASTLE_STAIRCASE.get(), isBlack, boundingBox);
	}
	
	public CastleStaircasePiece(CompoundTag nbt)
	{
		super(MSStructurePieces.SKAIA_CASTLE_STAIRCASE.get(), nbt);
	}
	
	public static CastleStaircasePiece findValidPlacement(boolean isBlack, int x, int y, int z)
	{
		BoundingBox boundingBox = new BoundingBox(x, y - 8, z, x + 8, y + 7, z + 8);
		return new CastleStaircasePiece(isBlack, boundingBox);
	}
	
	@Override
	public void addChildren(StructurePiece componentIn, StructurePieceAccessor accessor, Random rand)
	{
		this.direction = rand.nextInt(4);
	}
	
	@Override
	public void postProcess(WorldGenLevel level, StructureFeatureManager manager, ChunkGenerator generator, Random random, BoundingBox structureBoundingBox, ChunkPos chunkPosIn, BlockPos pos)
	{
		BlockState chessTile = (isBlack ? MSBlocks.BLACK_CHESS_DIRT.get() : MSBlocks.WHITE_CHESS_DIRT.get()).defaultBlockState();
		BlockState chessTile1 = (isBlack ? MSBlocks.DARK_GRAY_CHESS_DIRT.get() : MSBlocks.LIGHT_GRAY_CHESS_DIRT.get()).defaultBlockState();
		this.generateAirBox(level, structureBoundingBox, 0, 1, 0, 7, 14, 7);
		this.fillWithAlternatingBlocks(level, structureBoundingBox, 0, 15, 0, 7, 15, 7, chessTile, chessTile1, false);
		for(int step = 0; step < 8; step++) //Come on, step it up!
			switch(this.direction)
			{
			case 0:
				this.fillWithAlternatingBlocks(level, structureBoundingBox, 0, step, step, 7, step, step, chessTile, chessTile1, false);
				break;
			case 1:
				this.fillWithAlternatingBlocks(level, structureBoundingBox, step, step, 0, step, step, 7, chessTile, chessTile1, false);
				break;
			case 2:
				this.fillWithAlternatingBlocks(level, structureBoundingBox, 0, step, 7 - step, 7, step, 7 - step, chessTile, chessTile1, false);
				break;
			case 3:
				this.fillWithAlternatingBlocks(level, structureBoundingBox, 7 - step, step, 0, 7 - step, step, 7, chessTile, chessTile1, false);
				break;
			}
	}
}
