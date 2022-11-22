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

import java.util.Random;

public class CastleSolidPiece extends CastlePiece
{
	protected CastleSolidPiece(boolean isBlack, BoundingBox boundingBox)
	{
		super(MSStructurePieces.SKAIA_CASTLE_SOLID.get(), 0, boundingBox, isBlack);
	}
	
	public CastleSolidPiece(CompoundTag nbt)
	{
		super(MSStructurePieces.SKAIA_CASTLE_SOLID.get(), nbt);
	}
	
	public static CastleSolidPiece findValidPlacement(boolean isBlack, int x, int y, int z)
    {
		BoundingBox structureboundingbox = new BoundingBox(x, y, z, x + 8, y + 8, z + 8);
        return new CastleSolidPiece(isBlack, structureboundingbox);
    }
	
	@Override
	public void postProcess(WorldGenLevel level, StructureFeatureManager structureManager, ChunkGenerator generator, Random random, BoundingBox boundingBox, ChunkPos chunkPos, BlockPos pos)
	{
		BlockState chessTile = (isBlack ? MSBlocks.BLACK_CHESS_DIRT.get() : MSBlocks.WHITE_CHESS_DIRT.get()).defaultBlockState();
		BlockState chessTile1 = (isBlack ? MSBlocks.DARK_GRAY_CHESS_DIRT.get() : MSBlocks.LIGHT_GRAY_CHESS_DIRT.get()).defaultBlockState();
  
		this.fillWithAlternatingBlocks(level, boundingBox, 0, 0, 0, 7 ,7, 7, chessTile, chessTile1, false);
	}
}