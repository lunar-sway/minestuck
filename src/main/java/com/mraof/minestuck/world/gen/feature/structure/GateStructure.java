package com.mraof.minestuck.world.gen.feature.structure;

import com.mojang.serialization.Codec;
import com.mraof.minestuck.world.gen.LandChunkGenerator;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.LevelHeightAccessor;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.chunk.ChunkStatus;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.feature.StructureFeature;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;
import net.minecraft.world.level.levelgen.structure.StructurePiece;
import net.minecraft.world.level.levelgen.structure.StructureStart;
import net.minecraft.world.level.levelgen.structure.pieces.PieceGenerator;
import net.minecraft.world.level.levelgen.structure.pieces.PieceGeneratorSupplier;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePiecesBuilder;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Random;

//Note: placement is handled in a special way
// Configured spacing should be 1, and separation should be 0, or else the gate might sometimes not generate
public class GateStructure extends StructureFeature<NoneFeatureConfiguration>
{
	private static final Logger LOGGER = LogManager.getLogger();
	
	public GateStructure(Codec<NoneFeatureConfiguration> codec)
	{
		super(codec, PieceGeneratorSupplier.simple(GateStructure::checkLocation, GateStructure::generatePieces));
	}
	
	@Override
	public GenerationStep.Decoration step()
	{
		return GenerationStep.Decoration.SURFACE_STRUCTURES;
	}
	
	private static boolean checkLocation(PieceGeneratorSupplier.Context<NoneFeatureConfiguration> context)
	{
		return context.chunkPos().equals(findGatePosition(context.chunkGenerator()));
	}
	
	private static void generatePieces(StructurePiecesBuilder builder, PieceGenerator.Context<NoneFeatureConfiguration> context)
	{
		ChunkGenerator generator = context.chunkGenerator();
		ChunkPos pos = context.chunkPos();
		PieceFactory factory = getFactory(generator);
		
		if(factory == null)
			factory = GatePillarPiece::new;
		
		builder.addPiece(factory.create(generator, context.heightAccessor(), context.random(), pos.getMinBlockX(), pos.getMinBlockZ()));
	}
	
	public BlockPos findLandGatePos(ServerLevel level)
	{
		ChunkPos chunkPos = findGatePosition(level.getChunkSource().getGenerator());
		
		if (chunkPos != null)
		{
			StructureStart start = level.getChunk(chunkPos.x, chunkPos.z, ChunkStatus.STRUCTURE_STARTS).getStartForFeature(MSStructureFeatures.LAND_GATE.get());
			
			if(start != null)
			{
				for(StructurePiece piece : start.getPieces())
				{
					if(piece instanceof GatePiece)
						return ((GatePiece) piece).getGatePos();
				}
				
				LOGGER.error("Did not find a gate piece in gate structure. Instead had components {}.", start.getPieces());
			} else
				LOGGER.warn("Expected to find gate structure at chunk coords {}, in dimension {}, but found nothing!", chunkPos, level.dimension());
		} else
			LOGGER.warn("No land gate position could be found for dimension {}.", level.dimension());
		
		return null;
	}
	
	private static ChunkPos findGatePosition(ChunkGenerator chunkGenerator)
	{
		if (chunkGenerator instanceof LandChunkGenerator landGenerator)
		{
			return landGenerator.getOrFindLandGatePosition();
		} else
			return null;
	}
	
	public interface PieceFactory
	{
		GatePiece create(ChunkGenerator generator, LevelHeightAccessor level, Random rand, int minX, int minZ);
	}
	
	private static PieceFactory getFactory(ChunkGenerator generator)
	{
		if (generator instanceof LandChunkGenerator)
			return ((LandChunkGenerator) generator).gatePiece;
		else return null;
	}
}