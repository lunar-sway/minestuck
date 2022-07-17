package com.mraof.minestuck.world.gen.feature.structure.castle;

import com.mraof.minestuck.world.gen.feature.MSStructurePieces;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.StructureFeatureManager;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.structure.BoundingBox;

import java.util.Random;

public class CastleLibraryPiece extends CastleRoomPiece
{
	public CastleLibraryPiece(boolean isBlack, BoundingBox boundingBox)
	{
		super(MSStructurePieces.SKAIA_CASTLE_LIBRARY, isBlack, boundingBox);
	}
	
	public CastleLibraryPiece(CompoundTag nbt)
	{
		super(MSStructurePieces.SKAIA_CASTLE_LIBRARY, nbt);
	}
	
	public static CastleLibraryPiece findValidPlacement(boolean isBlack, int x, int y, int z)
	{
		BoundingBox boundingBox = new BoundingBox(x, y, z, x + 8, y + 8, z + 8);
		return new CastleLibraryPiece(isBlack, boundingBox);
	}
	
	@Override
	public void postProcess(WorldGenLevel level, StructureFeatureManager manager, ChunkGenerator generator, Random random, BoundingBox boundingBox, ChunkPos chunkPosIn, BlockPos pos)
	{
		super.postProcess(level, manager, generator, random, boundingBox, chunkPosIn, pos);
		this.generateBox(level, boundingBox, 1, 1, 1, 1, 3, 6, Blocks.BOOKSHELF.defaultBlockState(), Blocks.AIR.defaultBlockState(), false);
		this.generateBox(level, boundingBox, 6, 1, 1, 6, 3, 6, Blocks.BOOKSHELF.defaultBlockState(), Blocks.AIR.defaultBlockState(), false);
	}
	
}
