package com.mraof.minestuck.world.gen.structure.gate;

import com.mraof.minestuck.world.gen.structure.MSStructures;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.LevelHeightAccessor;
import net.minecraft.world.level.StructureManager;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.HugeMushroomBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.RandomState;
import net.minecraft.world.level.levelgen.structure.BoundingBox;

public class GateMushroomPiece extends GatePiece
{
	public GateMushroomPiece(ChunkGenerator generator, LevelHeightAccessor level, RandomState randomState, RandomSource random, int minX, int minZ)
	{
		super(MSStructures.GATE_MUSHROOM_PIECE.get(), level, randomState, generator, random, minX, minZ, 11, 25, 11, 0);
	}
	
	public GateMushroomPiece(CompoundTag nbt)
	{
		super(MSStructures.GATE_MUSHROOM_PIECE.get(), nbt);
	}
	
	@Override
	protected BlockPos getRelativeGatePos()
	{
		return new BlockPos(5, 24, 5);
	}
	
	@Override
	public void postProcess(WorldGenLevel level, StructureManager manager, ChunkGenerator chunkGenerator, RandomSource random, BoundingBox boundingBoxIn, ChunkPos chunkPos, BlockPos pos)
	{
		
		BlockState stem = Blocks.MUSHROOM_STEM.defaultBlockState().setValue(HugeMushroomBlock.DOWN, false);
		BlockState mushroom = Blocks.BROWN_MUSHROOM_BLOCK.defaultBlockState().setValue(HugeMushroomBlock.UP, false).setValue(HugeMushroomBlock.DOWN, false);
		
		generateBox(level, boundingBoxIn, 5, 0, 5, 5, 19, 5, stem, stem, false);
		
		generateBox(level, boundingBoxIn, 4, 20, 0, 6, 20, 0, mushroom, mushroom, false);
		generateBox(level, boundingBoxIn, 3, 20, 1, 7, 20, 1, mushroom, mushroom, false);
		generateBox(level, boundingBoxIn, 2, 20, 2, 8, 20, 2, mushroom, mushroom, false);
		generateBox(level, boundingBoxIn, 1, 20, 3, 9, 20, 3, mushroom, mushroom, false);
		generateBox(level, boundingBoxIn, 0, 20, 4, 10, 20, 6, mushroom, mushroom, false);
		generateBox(level, boundingBoxIn, 1, 20, 7, 9, 20, 7, mushroom, mushroom, false);
		generateBox(level, boundingBoxIn, 2, 20, 8, 8, 20, 8, mushroom, mushroom, false);
		generateBox(level, boundingBoxIn, 3, 20, 9, 7, 20, 9, mushroom, mushroom, false);
		generateBox(level, boundingBoxIn, 4, 20, 10, 6, 20, 10, mushroom, mushroom, false);
		
		super.postProcess(level, manager, chunkGenerator, random, boundingBoxIn, chunkPos, pos);
	}
}
