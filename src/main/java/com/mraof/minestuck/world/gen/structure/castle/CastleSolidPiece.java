package com.mraof.minestuck.world.gen.structure.castle;

import com.mraof.minestuck.SkaiaObjects;
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
		super(SkaiaObjects.CastleParts.SOLID_PIECE.get(), 0, boundingBox, isBlack);
	}
	
	public CastleSolidPiece(CompoundTag nbt)
	{
		super(SkaiaObjects.CastleParts.SOLID_PIECE.get(), nbt);
	}
	
	public static CastleSolidPiece findValidPlacement(boolean isBlack, int x, int y, int z)
    {
		BoundingBox structureboundingbox = new BoundingBox(x, y, z, x + 8, y + 8, z + 8);
        return new CastleSolidPiece(isBlack, structureboundingbox);
    }
	
	@Override
	public void postProcess(WorldGenLevel level, StructureManager structureManager, ChunkGenerator generator, RandomSource random, BoundingBox boundingBox, ChunkPos chunkPos, BlockPos pos)
	{
		BlockState chessTile = (isBlack ? SkaiaObjects.BLACK_CHESS_DIRT : SkaiaObjects.WHITE_CHESS_DIRT).asBlock().defaultBlockState();
		BlockState chessTile1 = (isBlack ? SkaiaObjects.DARK_GRAY_CHESS_DIRT : SkaiaObjects.LIGHT_GRAY_CHESS_DIRT).asBlock().defaultBlockState();
  
		this.fillWithAlternatingBlocks(level, boundingBox, 0, 0, 0, 7 ,7, 7, chessTile, chessTile1, false);
	}
}