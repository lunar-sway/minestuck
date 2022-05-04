package com.mraof.minestuck.world.gen.feature.structure;

import com.mraof.minestuck.world.gen.feature.MSStructurePieces;
import com.mraof.minestuck.world.gen.feature.structure.blocks.StructureBlockRegistry;
import net.minecraft.block.BlockState;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.MutableBoundingBox;
import net.minecraft.world.ISeedReader;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.feature.structure.StructureManager;
import net.minecraft.world.gen.feature.template.TemplateManager;

import java.util.Random;

public class GatePillarPiece extends GatePiece
{
	public GatePillarPiece(ChunkGenerator generator, Random random, int minX, int minZ)
	{
		super(MSStructurePieces.GATE_PILLAR, generator, random, minX, minZ, 3, 25, 3, -3);
	}
	
	public GatePillarPiece(TemplateManager templates, CompoundNBT nbt)
	{
		super(MSStructurePieces.GATE_PILLAR, nbt);
	}
	
	@Override
	protected void addAdditionalSaveData(CompoundNBT tagCompound)
	{
	
	}
	
	@Override
	protected BlockPos getRelativeGatePos()
	{
		return new BlockPos(1, 24, 1);
	}
	
	@Override
	public boolean postProcess(ISeedReader world, StructureManager manager, ChunkGenerator chunkGenerator, Random random, MutableBoundingBox mutableBoundingBox, ChunkPos chunkPos, BlockPos pos)
	{
		StructureBlockRegistry blocks = StructureBlockRegistry.getOrDefault(chunkGenerator);
		
		BlockState ground = blocks.getBlockState("ground");
		
		generateBox(world, boundingBox, 0, 0, 1, 2, 20, 1, ground, ground, false);
		generateBox(world, boundingBox, 1, 0, 0, 1, 20, 0, ground, ground, false);
		generateBox(world, boundingBox, 1, 0, 2, 1, 20, 2, ground, ground, false);
		
		for(int y = 0; y <= 20; y++)
		{
			maybeGenerateBlock(world, boundingBox, random, 0.5F, 0, y, 0, ground);
			maybeGenerateBlock(world, boundingBox, random, 0.5F, 2, y, 0, ground);
			maybeGenerateBlock(world, boundingBox, random, 0.5F, 0, y, 2, ground);
			maybeGenerateBlock(world, boundingBox, random, 0.5F, 2, y, 2, ground);
		}

		super.postProcess(world, manager, chunkGenerator, random, boundingBox, chunkPos, pos);
		
		return true;
	}
}