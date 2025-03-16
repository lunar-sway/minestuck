package com.mraof.minestuck.world.gen.structure.castle;

import com.mraof.minestuck.world.gen.structure.MSStructures;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.StructureManager;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.structure.BoundingBox;

public class CastleLibraryPiece extends CastleRoomPiece
{
	public CastleLibraryPiece(boolean isBlack, BoundingBox boundingBox)
	{
		super(MSStructures.SkaiaCastle.LIBRARY_PIECE.get(), isBlack, boundingBox);
	}
	
	public CastleLibraryPiece(CompoundTag nbt)
	{
		super(MSStructures.SkaiaCastle.LIBRARY_PIECE.get(), nbt);
	}
	
	public static CastleLibraryPiece findValidPlacement(boolean isBlack, int x, int y, int z)
	{
		BoundingBox boundingBox = new BoundingBox(x, y, z, x + 8, y + 8, z + 8);
		return new CastleLibraryPiece(isBlack, boundingBox);
	}
	
	@Override
	public void postProcess(WorldGenLevel level, StructureManager manager, ChunkGenerator generator, RandomSource random, BoundingBox boundingBox, ChunkPos chunkPosIn, BlockPos pos)
	{
		super.postProcess(level, manager, generator, random, boundingBox, chunkPosIn, pos);
		this.generateBox(level, boundingBox, 1, 1, 1, 1, 3, 6, Blocks.BOOKSHELF.defaultBlockState(), Blocks.AIR.defaultBlockState(), false);
		this.generateBox(level, boundingBox, 6, 1, 1, 6, 3, 6, Blocks.BOOKSHELF.defaultBlockState(), Blocks.AIR.defaultBlockState(), false);
	}
	
}
