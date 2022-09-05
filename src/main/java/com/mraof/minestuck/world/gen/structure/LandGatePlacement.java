package com.mraof.minestuck.world.gen.structure;

import com.mojang.serialization.Codec;
import com.mraof.minestuck.world.gen.LandChunkGenerator;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.chunk.ChunkStatus;
import net.minecraft.world.level.levelgen.structure.StructurePiece;
import net.minecraft.world.level.levelgen.structure.StructureStart;
import net.minecraft.world.level.levelgen.structure.placement.StructurePlacement;
import net.minecraft.world.level.levelgen.structure.placement.StructurePlacementType;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public final class LandGatePlacement implements StructurePlacement
{
	private static final Logger LOGGER = LogManager.getLogger();
	
	public static final LandGatePlacement INSTANCE = new LandGatePlacement();
	
	public static final Codec<LandGatePlacement> CODEC = Codec.unit(LandGatePlacement.INSTANCE);
	
	@Override
	public StructurePlacementType<LandGatePlacement> type()
	{
		return MSStructurePlacements.LAND_GATE.get();
	}
	
	@Override
	public boolean isFeatureChunk(ChunkGenerator generator, long seed, int chunkX, int chunkZ)
	{
		if(generator instanceof LandChunkGenerator landGenerator)
		{
			ChunkPos chunkPos = landGenerator.getOrFindLandGatePosition();
			return chunkPos.x == chunkX && chunkPos.z == chunkZ;
		} else
			return false;
	}
	
	public static BlockPos findLandGatePos(ServerLevel level)
	{
		if(level.getChunkSource().getGenerator() instanceof LandChunkGenerator landGenerator)
		{
			ChunkPos chunkPos = landGenerator.getOrFindLandGatePosition();
			StructureStart start = level.getChunk(chunkPos.x, chunkPos.z, ChunkStatus.STRUCTURE_STARTS).getStartForFeature(MSConfiguredStructures.LAND_GATE.get());
			
			if(start != null)
			{
				for(StructurePiece piece : start.getPieces())
				{
					if(piece instanceof GatePiece gatePiece)
						return gatePiece.getGatePos();
				}
				
				LOGGER.error("Did not find a gate piece in gate structure. Instead had components {}.", start.getPieces());
			} else
				LOGGER.warn("Expected to find gate structure at chunk coords {}, in dimension {}, but found nothing!", chunkPos, level.dimension());
		} else
			LOGGER.warn("No land gate position could be found for dimension {}.", level.dimension());
		
		return null;
	}
}
