package com.mraof.minestuck.world.gen.feature.structure;

import com.mojang.serialization.Codec;
import com.mraof.minestuck.Minestuck;
import com.mraof.minestuck.world.biome.LandBiomeSet;
import net.minecraft.util.RegistryKey;
import net.minecraft.util.SharedSeedRandom;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.MutableBoundingBox;
import net.minecraft.util.registry.DynamicRegistries;
import net.minecraft.world.World;
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

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

//Note: placement is handled in a special way
// Configured spacing should be 1, and separation should be 0, or else the gate might sometimes not generate
public class GateStructure extends Structure<NoFeatureConfig>
{
	private static final Logger LOGGER = LogManager.getLogger();
	
	private final Map<RegistryKey<World>, ChunkPos> positionCache = new HashMap<>();
	
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
	
	@Override
	public String getFeatureName()
	{
		return Minestuck.MOD_ID + ":land_gate";
	}
	
	public BlockPos findLandGatePos(ServerWorld world)
	{
		if(world.getChunkSource().getGenerator().getBiomeSource().canGenerateStructure(this))
		{
			ChunkPos chunkPos = findGatePosition(world.getChunkSource().getGenerator());
			
			StructureStart<?> start = world.getChunk(chunkPos.x, chunkPos.z, ChunkStatus.STRUCTURE_STARTS).getStartForFeature(this);
			
			if(start instanceof Start)
			{
				return ((Start) start).findGatePos();
			} else
				LOGGER.warn("Expected to find gate structure at chunk coords {}, in dimension {}, but found {}!", chunkPos, world.dimension(), start);
		}
		
		return null;
	}
	
	private ChunkPos findGatePosition(ChunkGenerator chunkGenerator)
	{
		//Idea; Dimtype -> location map that is cleared on server stopped
		RegistryKey<World> type = null; //TODO get world key (or other appropriate identifier) from the chunk generator, or move the cache to the chunk generator
		if(type != null)
		{
			ChunkPos pos = positionCache.get(type);
			if(pos != null)
				return pos;
		}
		
		long seed = 0; //TODO
		Random worldRand = new Random(seed);
		
		double angle = 2 * Math.PI * worldRand.nextDouble();
		int radius = 38 + worldRand.nextInt(12);
		
		Biome normalBiome = LandBiomeSet.getSet(chunkGenerator).NORMAL.get();
		
		for(; radius < 65; radius += 6)
		{
			int posX = (int) Math.round(Math.cos(angle) * radius);
			int posZ = (int) Math.round(Math.sin(angle) * radius);
			
			//TODO Could there be a better way to search for a position? (Look for possible positions with the "surrounded by normal biomes" property rather than pick a random one and then check if it has this property)
			BlockPos pos = chunkGenerator.getBiomeSource().findBiomeHorizontal((posX << 4) + 8, 0,(posZ << 4) + 8, 96, biome -> biome == normalBiome, worldRand);

			if(pos != null && chunkGenerator.getBiomeSource().getBiomesWithin(pos.getX(), 0, pos.getZ(), 16).stream().allMatch(biome -> biome == normalBiome))
				return new ChunkPos(pos.getX() >> 4, pos.getZ() >> 4);
		}
		
		int posX = (int) Math.round(Math.cos(angle) * radius);
		int posZ = (int) Math.round(Math.sin(angle) * radius);
		LOGGER.warn("Did not come across a decent location for land gates. Placing it without regard to any biomes.");
		
		BlockPos pos = chunkGenerator.getBiomeSource().findBiomeHorizontal((posX << 4) + 8, 0, (posZ << 4) + 8, 96, biome -> biome == normalBiome, worldRand);
		
		ChunkPos gatePos;
		if(pos != null)
			gatePos = new ChunkPos(pos);
		else gatePos = new ChunkPos(posX, posZ);
		
		if(type != null)
			positionCache.put(type, gatePos);
		
		return gatePos;
	}
	
	/**
	 * Should be called during a ServerStopped event. Otherwise, cached gate positions might end up being used for dimensions in other worlds.
	 */
	public void clearCache()
	{
		positionCache.clear();
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
		return null; //TODO
	}
}