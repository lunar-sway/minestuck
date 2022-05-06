package com.mraof.minestuck.world.gen.feature.structure;

import com.mojang.serialization.Codec;
import com.mraof.minestuck.world.gen.LandChunkGenerator;
import net.minecraft.util.SharedSeedRandom;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.MutableBoundingBox;
import net.minecraft.util.registry.DynamicRegistries;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.provider.BiomeProvider;
import net.minecraft.world.chunk.ChunkStatus;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.GenerationStage;
import net.minecraft.world.gen.feature.NoFeatureConfig;
import net.minecraft.world.gen.feature.structure.Structure;
import net.minecraft.world.gen.feature.structure.StructurePiece;
import net.minecraft.world.gen.feature.structure.StructureStart;
import net.minecraft.world.gen.feature.template.TemplateManager;
import net.minecraft.world.server.ServerWorld;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Random;

//Note: placement is handled in a special way
// Configured spacing should be 1, and separation should be 0, or else the gate might sometimes not generate
public class GateStructure extends Structure<NoFeatureConfig>
{
	private static final Logger LOGGER = LogManager.getLogger();
	
	public GateStructure(Codec<NoFeatureConfig> codec)
	{
		super(codec);
	}
	
	@Override
	public GenerationStage.Decoration step()
	{
		return GenerationStage.Decoration.SURFACE_STRUCTURES;
	}
	
	@Override
	protected boolean isFeatureChunk(ChunkGenerator generator, BiomeProvider provider, long seed, SharedSeedRandom rand, int chunkX, int chunkZ, Biome biome, ChunkPos pos, NoFeatureConfig config)
	{
		return pos.equals(findGatePosition(generator));
	}
	
	@Override
	public IStartFactory<NoFeatureConfig> getStartFactory()
	{
		return GateStructure.Start::new;
	}
	
	public BlockPos findLandGatePos(ServerWorld world)
	{
		ChunkPos chunkPos = findGatePosition(world.getChunkSource().getGenerator());
		
		if (chunkPos != null)
		{
			StructureStart<?> start = world.getChunk(chunkPos.x, chunkPos.z, ChunkStatus.STRUCTURE_STARTS).getStartForFeature(this);
			
			if(start instanceof Start)
			{
				return ((Start) start).findGatePos();
			} else
				LOGGER.warn("Expected to find gate structure at chunk coords {}, in dimension {}, but found {}!", chunkPos, world.dimension(), start);
		} else
			LOGGER.warn("No land gate position could be found for dimension {}.", world.dimension());
		
		return null;
	}
	
	private ChunkPos findGatePosition(ChunkGenerator chunkGenerator)
	{
		if (chunkGenerator instanceof LandChunkGenerator)
		{
			LandChunkGenerator landGenerator = (LandChunkGenerator) chunkGenerator;
			
			return landGenerator.getOrFindLandGatePosition();
		} else
			return null;
	}
	
	public static class Start extends StructureStart<NoFeatureConfig>
	{
		public Start(Structure<NoFeatureConfig> structure, int chunkX, int chunkZ, MutableBoundingBox boundingBox, int reference, long seed)
		{
			super(structure, chunkX, chunkZ, boundingBox, reference, seed);
		}
		
		@Override
		public void generatePieces(DynamicRegistries registries, ChunkGenerator generator, TemplateManager templates, int chunkX, int chunkZ, Biome biome, NoFeatureConfig config)
		{
			PieceFactory factory = getFactory(generator);
			
			if(factory == null)
				factory = GatePillarPiece::new;
			
			pieces.add(factory.create(generator, random, chunkX * 16 + random.nextInt(16), chunkZ * 16 + random.nextInt(16)));
			calculateBoundingBox();
		}
		
		private BlockPos findGatePos()
		{
			for(StructurePiece piece : pieces)
			{
				if(piece instanceof GatePiece)
					return ((GatePiece) piece).getGatePos();
			}
			
			LOGGER.error("Did not find a gate piece in gate structure. Instead had components {}.", pieces);
			return null;
		}
	}
	
	public interface PieceFactory
	{
		GatePiece create(ChunkGenerator generator, Random rand, int minX, int minZ);
	}
	
	private static PieceFactory getFactory(ChunkGenerator generator)
	{
		if (generator instanceof LandChunkGenerator)
			return ((LandChunkGenerator) generator).gatePiece;
		else return null;
	}
}