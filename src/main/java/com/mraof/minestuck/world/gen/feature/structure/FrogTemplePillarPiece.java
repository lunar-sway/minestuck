package com.mraof.minestuck.world.gen.feature.structure;

import com.mraof.minestuck.block.MSBlocks;
import com.mraof.minestuck.block.MSDirectionalBlock;
import com.mraof.minestuck.world.gen.feature.MSStructurePieces;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.StructureFeatureManager;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.levelgen.structure.ScatteredFeaturePiece;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePieceSerializationContext;

import java.util.Random;

public class FrogTemplePillarPiece extends ScatteredFeaturePiece
{
	private final boolean eroded;
	private final boolean uraniumFilled;
	private final int randReduction;
	
	public FrogTemplePillarPiece(Random random, int x, int y, int z) //this constructor is used when the structure is first initialized
	{
		super(MSStructurePieces.FROG_TEMPLE_PILLAR, x - 2, y, z - 2, 5, 46, 5, getRandomHorizontalDirection(random));
		eroded = random.nextBoolean();
		uraniumFilled = random.nextBoolean();
		randReduction = random.nextInt(10);
	}
	
	public FrogTemplePillarPiece(CompoundTag nbt) //this constructor is used for reading from data
	{
		super(MSStructurePieces.FROG_TEMPLE_PILLAR, nbt);
		eroded = nbt.getBoolean("eroded");
		uraniumFilled = nbt.getBoolean("uraniumFilled");
		randReduction = nbt.getInt("randReduction");
	}
	
	@Override
	protected void addAdditionalSaveData(StructurePieceSerializationContext context, CompoundTag tagCompound)
	{
		tagCompound.putBoolean("eroded", eroded);
		tagCompound.putBoolean("uraniumFilled", uraniumFilled);
		tagCompound.putInt("randReduction", randReduction);
		super.addAdditionalSaveData(context, tagCompound);
	}
	
	
	@Override
	public void postProcess(WorldGenLevel level, StructureFeatureManager manager, ChunkGenerator chunkGenerator, Random randomIn, BoundingBox boundingBoxIn, ChunkPos chunkPosIn, BlockPos pos)
	{
		BlockState columnBlock = MSBlocks.GREEN_STONE_COLUMN.defaultBlockState().setValue(MSDirectionalBlock.FACING, Direction.UP);
		if(eroded)
		{
			generateBox(level, boundingBoxIn, 1, -20, 1, 3, 40 - randReduction, 3, columnBlock, columnBlock, false);
		} else
		{
			Block innerBlock = uraniumFilled ? MSBlocks.URANIUM_BLOCK : MSBlocks.CRUXITE_BLOCK;
			generateBox(level, boundingBoxIn, 1, -20, 1, 3, 40, 3, columnBlock, columnBlock, false);
			generateBox(level, boundingBoxIn, 0, 41, 0, 4, 45, 4, MSBlocks.GREEN_STONE.defaultBlockState(), innerBlock.defaultBlockState(), false); //top of pillar with a randomly filled center picked by uraniumFilled
		}
	}
}