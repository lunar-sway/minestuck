package com.mraof.minestuck.world.gen.feature.structure;

import com.mraof.minestuck.block.MSBlocks;
import com.mraof.minestuck.world.gen.feature.MSStructurePieces;
import com.mraof.minestuck.world.gen.feature.structure.blocks.StructureBlockRegistry;
import net.minecraft.block.BlockState;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.MutableBoundingBox;
import net.minecraft.world.IWorld;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.feature.template.TemplateManager;

import java.util.Random;

public class FrogTemplePillarPiece extends FrogTemplePiece
{
	public FrogTemplePillarPiece(ChunkGenerator<?> generator, Random random, int x, int z)
	{
		super(MSStructurePieces.FROG_TEMPLE_PILLAR, random, x - 70, 64, z - 70, 140, 100, 140);
	}
	
	public FrogTemplePillarPiece(TemplateManager templates, CompoundNBT nbt)
	{
		super(MSStructurePieces.FROG_TEMPLE_PILLAR, nbt);
	}
	
	@Override
	protected void readAdditional(CompoundNBT tagCompound)
	{
	
	}
	
	@Override
	protected BlockPos getRelativeGatePos()
	{
		return new BlockPos(1, 24, 1);
	}

	@Override
	public boolean create(IWorld worldIn, ChunkGenerator<?> chunkGenerator, Random randomIn, MutableBoundingBox boundingBoxIn, ChunkPos chunkPosIn)
	{
		StructureBlockRegistry blocks = StructureBlockRegistry.getOrDefault(chunkGenerator.getSettings());
		
		BlockState ground = blocks.getBlockState("ground");
		
		fillWithBlocks(worldIn, boundingBoxIn, 0, 0, 1, 2, 20, 1, ground, ground, false);
		fillWithBlocks(worldIn, boundingBoxIn, 1, 0, 0, 1, 20, 0, ground, ground, false);
		fillWithBlocks(worldIn, boundingBoxIn, 1, 0, 2, 1, 20, 2, ground, ground, false);
		
		for(int y = 0; y <= 20; y++)
		{
			randomlyPlaceBlock(worldIn, boundingBoxIn, randomIn, 0.5F, 0, y, 0, ground);
			randomlyPlaceBlock(worldIn, boundingBoxIn, randomIn, 0.5F, 2, y, 0, ground);
			randomlyPlaceBlock(worldIn, boundingBoxIn, randomIn, 0.5F, 0, y, 2, ground);
			randomlyPlaceBlock(worldIn, boundingBoxIn, randomIn, 0.5F, 2, y, 2, ground);
		}

		super.create(worldIn, chunkGenerator, randomIn, boundingBoxIn, chunkPosIn);
		
		return true;
	}
	
	private void buildPillars(BlockState block, IWorld world, MutableBoundingBox boundingBox, Random random) //TODO pieces of pillars often fail to generate, which is why it is not in use currently
	{
		if(random.nextBoolean())
		{
			if(random.nextBoolean())
			{
				fillWithBlocks(world, boundingBox, 19 + 20, -20, 1, 22, 30, 4, block, block, false); //front/south pillar
				fillWithBlocks(world, boundingBox, 18 + 20, 31, 0, 23 + 20, 36, 5, MSBlocks.GREEN_STONE.getDefaultState(), MSBlocks.CRUXITE_BLOCK.getDefaultState(), false);
			} else
			{
				int randReduction = random.nextInt(5);
				fillWithBlocks(world, boundingBox, 18 + 20, -20, 1, 23, 30 - randReduction, 4, block, block, false); //front/south pillar
			}
		}
		if(random.nextBoolean())
		{
			if(random.nextBoolean())
			{
				fillWithBlocks(world, boundingBox, 1, -20, 57 + 20, 4, 30, 60 + 20, block, block, false); //west pillar
				fillWithBlocks(world, boundingBox, 0, 31, 56 + 20, 5, 36, 61 + 20, MSBlocks.GREEN_STONE.getDefaultState(), MSBlocks.URANIUM_BLOCK.getDefaultState(), false);
			} else
			{
				int randReduction = random.nextInt(5);
				fillWithBlocks(world, boundingBox, 1, -20, 57 + 20, 4, 30 - randReduction, 60 + 20, block, block, false); //west pillar
			}
		}
		if(random.nextBoolean())
		{
			if(random.nextBoolean())
			{
				fillWithBlocks(world, boundingBox, 18 + 20, -20, 103 + 20, 21 + 20, 30, 108 + 20, block, block, false); //north pillar
				fillWithBlocks(world, boundingBox, 17 + 20, 31, 102 + 20, 22 + 20, 36, 109 + 20, MSBlocks.GREEN_STONE.getDefaultState(), MSBlocks.CRUXITE_BLOCK.getDefaultState(), false);
			} else
			{
				int randReduction = random.nextInt(5);
				fillWithBlocks(world, boundingBox, 18 + 20, -20, 103 + 20, 21 + 20, 30 - randReduction, 108 + 20, block, block, false); //north pillar
			}
		}
		if(random.nextBoolean())
		{
			if(random.nextBoolean())
			{
				fillWithBlocks(world, boundingBox, 67 + 20, -20, 57 + 20, 70 + 20, 30, 60 + 20, block, block, false); //east pillar
				fillWithBlocks(world, boundingBox, 66 + 20, 31, 56 + 20, 71 + 20, 36, 61 + 20, MSBlocks.GREEN_STONE.getDefaultState(), MSBlocks.URANIUM_BLOCK.getDefaultState(), false);
			} else
			{
				int randReduction = random.nextInt(5);
				fillWithBlocks(world, boundingBox, 67 + 20, -20, 57 + 20, 70 + 20, 30 - randReduction, 60 + 20, block, block, false); //east pillar
			}
		}
	}
}