package com.mraof.minestuck.world.gen.structure.gate;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.mraof.minestuck.world.gen.LandChunkGenerator;
import com.mraof.minestuck.world.gen.structure.MSConfiguredStructures;
import com.mraof.minestuck.world.gen.structure.MSStructurePlacements;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Registry;
import net.minecraft.core.Vec3i;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.chunk.ChunkStatus;
import net.minecraft.world.level.levelgen.RandomState;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraft.world.level.levelgen.structure.StructurePiece;
import net.minecraft.world.level.levelgen.structure.StructureStart;
import net.minecraft.world.level.levelgen.structure.placement.StructurePlacement;
import net.minecraft.world.level.levelgen.structure.placement.StructurePlacementType;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Objects;
import java.util.Optional;

public final class LandGatePlacement extends StructurePlacement
{
	private static final Logger LOGGER = LogManager.getLogger();
	
	public static final Codec<LandGatePlacement> CODEC = RecordCodecBuilder.create(instance -> placementCodec(instance).apply(instance, LandGatePlacement::new));
	
	public LandGatePlacement()
	{
		this(Vec3i.ZERO, FrequencyReductionMethod.DEFAULT, 1, 0, Optional.empty());
	}
	
	public LandGatePlacement(Vec3i locateOffset, FrequencyReductionMethod frequencyReductionMethod, float frequency, int salt, @SuppressWarnings({"deprecation", "OptionalUsedAsFieldOrParameterType"}) Optional<ExclusionZone> exclusionZone)
	{
		super(locateOffset, frequencyReductionMethod, frequency, salt, exclusionZone);
	}
	
	@Override
	public StructurePlacementType<LandGatePlacement> type()
	{
		return MSStructurePlacements.LAND_GATE.get();
	}
	
	@Override
	public boolean isPlacementChunk(ChunkGenerator generator, RandomState state, long seed, int chunkX, int chunkZ)
	{
		if(generator instanceof LandChunkGenerator landGenerator)
		{
			ChunkPos chunkPos = landGenerator.getOrFindLandGatePosition(state);
			return chunkPos.x == chunkX && chunkPos.z == chunkZ;
		} else
			return false;
	}
	
	public static BlockPos findLandGatePos(ServerLevel level)
	{
		if(level.getChunkSource().getGenerator() instanceof LandChunkGenerator landGenerator)
		{
			// (Last checked mc 1.18) RegistryObject's refers to objects in regular / builtin registries,
			// but some registries also have separate dynamic registries that are world-specific
			// We need a configured structure from the dynamic registry and not the builtin registry for getStartForFeature() to work
			Structure landGate = level.registryAccess().registryOrThrow(Registry.STRUCTURE_REGISTRY)
					.get(MSConfiguredStructures.LAND_GATE.getKey());
			Objects.requireNonNull(landGate, "Unable to find land gate structure instance");
			
			ChunkPos chunkPos = landGenerator.getOrFindLandGatePosition(level.getChunkSource().randomState());
			StructureStart start = level.getChunk(chunkPos.x, chunkPos.z, ChunkStatus.STRUCTURE_STARTS).getStartForStructure(landGate);
			
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
