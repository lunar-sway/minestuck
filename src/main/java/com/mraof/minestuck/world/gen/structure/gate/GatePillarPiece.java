package com.mraof.minestuck.world.gen.structure.gate;

import com.mraof.minestuck.world.gen.structure.MSStructurePieces;
import com.mraof.minestuck.world.gen.structure.blocks.StructureBlockRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.LevelHeightAccessor;
import net.minecraft.world.level.StructureFeatureManager;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.structure.BoundingBox;

import java.util.Random;

public class GatePillarPiece extends GatePiece
{
	public GatePillarPiece(ChunkGenerator generator, LevelHeightAccessor level, Random random, int minX, int minZ)
	{
		super(MSStructurePieces.GATE_PILLAR.get(), level, generator, random, minX, minZ, 3, 25, 3, -3);
	}
	
	public GatePillarPiece(CompoundTag nbt)
	{
		super(MSStructurePieces.GATE_PILLAR.get(), nbt);
	}
	
	@Override
	protected BlockPos getRelativeGatePos()
	{
		return new BlockPos(1, 24, 1);
	}
	
	@Override
	public void postProcess(WorldGenLevel level, StructureFeatureManager manager, ChunkGenerator chunkGenerator, Random random, BoundingBox box, ChunkPos chunkPos, BlockPos pos)
	{
		StructureBlockRegistry blocks = StructureBlockRegistry.getOrDefault(chunkGenerator);
		
		BlockState ground = blocks.getBlockState("ground");
		
		generateBox(level, box, 0, 0, 1, 2, 20, 1, ground, ground, false);
		generateBox(level, box, 1, 0, 0, 1, 20, 0, ground, ground, false);
		generateBox(level, box, 1, 0, 2, 1, 20, 2, ground, ground, false);
		
		for(int y = 0; y <= 20; y++)
		{
			maybeGenerateBlock(level, box, random, 0.5F, 0, y, 0, ground);
			maybeGenerateBlock(level, box, random, 0.5F, 2, y, 0, ground);
			maybeGenerateBlock(level, box, random, 0.5F, 0, y, 2, ground);
			maybeGenerateBlock(level, box, random, 0.5F, 2, y, 2, ground);
		}

		super.postProcess(level, manager, chunkGenerator, random, box, chunkPos, pos);
	}
}