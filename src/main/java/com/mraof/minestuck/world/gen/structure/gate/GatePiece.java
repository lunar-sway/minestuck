package com.mraof.minestuck.world.gen.structure.gate;

import com.mraof.minestuck.block.GateBlock;
import com.mraof.minestuck.world.GateHandler;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.*;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.RandomState;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.levelgen.structure.ScatteredFeaturePiece;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePieceType;

public abstract class GatePiece extends ScatteredFeaturePiece
{
	public GatePiece(StructurePieceType type, LevelHeightAccessor level, RandomState randomState, ChunkGenerator generator, RandomSource random, int x, int z, int width, int height, int depth, int heightOffset)
	{
		super(type, x, 64, z, width, height, depth, getRandomHorizontalDirection(random));
		
		int count = 0;
		int heightSum = 0;
		for(int xPos = boundingBox.minX(); xPos <= boundingBox.maxX(); xPos++)
			for(int zPos = boundingBox.minZ(); zPos <= boundingBox.maxZ(); zPos++)
			{
				int posHeight = generator.getBaseHeight(xPos, zPos, Heightmap.Types.OCEAN_FLOOR_WG, level, randomState);
				heightSum += posHeight;
				count++;
			}
		
		if(count > 0)
			boundingBox.move(0, heightSum/count + heightOffset - boundingBox.minY(), 0);
	}
	
	public GatePiece(StructurePieceType type, CompoundTag nbt)
	{
		super(type, nbt);
	}
	
	protected final BlockPos getGatePos()
	{
		BlockPos relativePos = getRelativeGatePos();
		int x = relativePos.getX(), y = relativePos.getY(), z = relativePos.getZ();
		return new BlockPos(this.getWorldX(x, z), this.getWorldY(y), this.getWorldZ(x, z));
	}
	
	protected abstract BlockPos getRelativeGatePos();
	
	@Override
	protected boolean updateAverageGroundHeight(LevelAccessor level, BoundingBox boundsIn, int heightIn)
	{
		throw new UnsupportedOperationException("Shouldn't change the bounding box after creating the gate piece. Look at other gate pieces for an example of what to do instead.");
	}
	
	@Override
	public void postProcess(WorldGenLevel level, StructureManager manager, ChunkGenerator chunkGenerator, RandomSource random, BoundingBox box, ChunkPos chunkPos, BlockPos pos)
	{
		placeGate(level, box);
	}
	
	private void placeGate(LevelAccessor level, BoundingBox boundingBoxIn)
	{
		BlockPos gatePos = getGatePos();
		if (boundingBoxIn.isInside(gatePos))
		{
			GateBlock.placeGate(level, gatePos, GateHandler.Type.LAND_GATE, Block.UPDATE_CLIENTS);
		}
	}
}