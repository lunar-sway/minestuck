package com.mraof.minestuck.world.gen.feature.structure;

import com.mraof.minestuck.world.gen.feature.MSStructurePieces;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.HugeMushroomBlock;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.MutableBoundingBox;
import net.minecraft.world.ISeedReader;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.feature.structure.StructureManager;
import net.minecraft.world.gen.feature.template.TemplateManager;

import java.util.Random;

public class GateMushroomPiece extends GatePiece
{
	public GateMushroomPiece(ChunkGenerator generator, Random random, int minX, int minZ)
	{
		super(MSStructurePieces.GATE_MUSHROOM, generator, random, minX, minZ, 11, 25, 11, 0);
	}
	
	public GateMushroomPiece(TemplateManager templates, CompoundNBT nbt)
	{
		super(MSStructurePieces.GATE_MUSHROOM, nbt);
	}
	
	@Override
	protected void addAdditionalSaveData(CompoundNBT tagCompound)
	{
	
	}
	
	@Override
	protected BlockPos getRelativeGatePos()
	{
		return new BlockPos(5, 24, 5);
	}
	
	@Override
	public boolean postProcess(ISeedReader worldIn, StructureManager manager, ChunkGenerator chunkGenerator, Random random, MutableBoundingBox boundingBoxIn, ChunkPos chunkPos, BlockPos pos)
	{
		
		BlockState stem = Blocks.MUSHROOM_STEM.defaultBlockState().setValue(HugeMushroomBlock.DOWN, false);
		BlockState mushroom = Blocks.BROWN_MUSHROOM_BLOCK.defaultBlockState().setValue(HugeMushroomBlock.UP, false).setValue(HugeMushroomBlock.DOWN, false);
		
		generateBox(worldIn, boundingBoxIn, 5, 0, 5, 5, 19, 5, stem, stem, false);
		
		generateBox(worldIn, boundingBoxIn, 4, 20, 0, 6, 20, 0, mushroom, mushroom, false);
		generateBox(worldIn, boundingBoxIn, 3, 20, 1, 7, 20, 1, mushroom, mushroom, false);
		generateBox(worldIn, boundingBoxIn, 2, 20, 2, 8, 20, 2, mushroom, mushroom, false);
		generateBox(worldIn, boundingBoxIn, 1, 20, 3, 9, 20, 3, mushroom, mushroom, false);
		generateBox(worldIn, boundingBoxIn, 0, 20, 4, 10, 20, 6, mushroom, mushroom, false);
		generateBox(worldIn, boundingBoxIn, 1, 20, 7, 9, 20, 7, mushroom, mushroom, false);
		generateBox(worldIn, boundingBoxIn, 2, 20, 8, 8, 20, 8, mushroom, mushroom, false);
		generateBox(worldIn, boundingBoxIn, 3, 20, 9, 7, 20, 9, mushroom, mushroom, false);
		generateBox(worldIn, boundingBoxIn, 4, 20, 10, 6, 20, 10, mushroom, mushroom, false);
		
		super.postProcess(worldIn, manager, chunkGenerator, random, boundingBoxIn, chunkPos, pos);
		
		return true;
	}
}