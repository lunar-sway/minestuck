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

public class CastleSolidPiece extends CastlePiece
{
	protected CastleSolidPiece(boolean isBlack, BoundingBox boundingBox)
	{
		super(MSStructures.SkaiaCastle.SOLID_PIECE.get(), 0, boundingBox, isBlack);
	}
	
	public CastleSolidPiece(CompoundTag nbt)
	{
		super(MSStructures.SkaiaCastle.SOLID_PIECE.get(), nbt);
	}
	
	public static CastleSolidPiece findValidPlacement(boolean isBlack, int x, int y, int z)
    {
		BoundingBox structureboundingbox = new BoundingBox(x, y, z, x + 8, y + 8, z + 8);
        return new CastleSolidPiece(isBlack, structureboundingbox);
    }
	
	@Override
	public void postProcess(WorldGenLevel level, StructureManager structureManager, ChunkGenerator generator, RandomSource random, BoundingBox boundingBox, ChunkPos chunkPos, BlockPos pos)
	{
		BlockState chessTile = (isBlack ? SkaiaBlocks.BLACK_CHESS_DIRT : SkaiaBlocks.WHITE_CHESS_DIRT).asBlock().defaultBlockState();
		BlockState chessTile1 = (isBlack ? SkaiaBlocks.DARK_GRAY_CHESS_DIRT : SkaiaBlocks.LIGHT_GRAY_CHESS_DIRT).asBlock().defaultBlockState();
  
		this.fillWithAlternatingBlocks(level, boundingBox, 0, 0, 0, 7 ,7, 7, chessTile, chessTile1, false);
	}
}
