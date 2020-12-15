package com.mraof.minestuck.world.gen.feature.structure;

import com.mojang.serialization.Codec;
import com.mraof.minestuck.Minestuck;
import net.minecraft.util.RegistryKey;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.MutableBoundingBox;
import net.minecraft.util.registry.DynamicRegistries;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.GenerationStage;
import net.minecraft.world.gen.feature.NoFeatureConfig;
import net.minecraft.world.gen.feature.structure.Structure;
import net.minecraft.world.gen.feature.structure.StructurePiece;
import net.minecraft.world.gen.feature.structure.StructureStart;
import net.minecraft.world.gen.feature.template.TemplateManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class GateStructure extends Structure<NoFeatureConfig>
{
	private static final Logger LOGGER = LogManager.getLogger();
	
	private final Map<RegistryKey<World>, ChunkPos> positionCache = new HashMap<>();
	
	public GateStructure(Codec<NoFeatureConfig> codec)
	{
		super(codec);
	}
	
	@Override
	public GenerationStage.Decoration getDecorationStage()
	{
		return GenerationStage.Decoration.SURFACE_STRUCTURES;
	}
	
	/*
		@Override
		protected ChunkPos getStartPositionForPosition(ChunkGenerator<?> chunkGenerator, Random random, int x, int z, int spacingOffsetsX, int spacingOffsetsZ)
		{
			DimensionType type = chunkGenerator instanceof LandChunkGenerator ? ((LandChunkGenerator) chunkGenerator).getDimensionType() : null;
			return findGatePosition(type, chunkGenerator);
		}
	
		public boolean canBeGenerated(BiomeManager biomeManagerIn, ChunkGenerator<?> generatorIn, Random randIn, int chunkX, int chunkZ, Biome biomeIn) {
			ChunkPos pos = this.getStartPositionForPosition(generatorIn, randIn, chunkX, chunkZ, 0, 0);
	
			return chunkX == pos.x && chunkZ == pos.z;
		}
		*/
	@Override
	public IStartFactory<NoFeatureConfig> getStartFactory()
	{
		return GateStructure.Start::new;
	}
	
	@Override
	public String getStructureName()
	{
		return Minestuck.MOD_ID + ":land_gate";
	}
	/* TODO
	public BlockPos findLandGatePos(ServerWorld world)
	{
		if(world.getChunkProvider().getChunkGenerator().getBiomeProvider().hasStructure(this))
		{
			ChunkPos chunkPos = findGatePosition(world.getDimension().getType(), world.getChunkProvider().getChunkGenerator());
			
			StructureStart start = world.getChunk(chunkPos.x, chunkPos.z, ChunkStatus.STRUCTURE_STARTS).getStructureStart(getStructureName());
			
			if(start instanceof Start)
			{
				return ((Start) start).findGatePos();
			} else
				LOGGER.warn("Expected to find gate structure at chunk coords {}, in dimension {}, but found {}!", chunkPos, world.getDimension().getType().getRegistryName(), start);
		}
		
		return null;
	}
	
	private ChunkPos findGatePosition(DimensionType type, ChunkGenerator<?> chunkGenerator)
	{
		//Idea; Dimtype -> location map that is cleared on server stopped
		if(type != null)
		{
			ChunkPos pos = positionCache.get(type);
			if(pos != null)
				return pos;
		}
		
		Random worldRand = new Random(chunkGenerator.getSeed());
		
		double angle = 2 * Math.PI * worldRand.nextDouble();
		int radius = 38 + worldRand.nextInt(12);
		
		Biome normalBiome = LandBiomeSet.getSet(chunkGenerator.getSettings()).NORMAL.get();
		
		for(; radius < 65; radius += 6)
		{
			int posX = (int) Math.round(Math.cos(angle) * radius);
			int posZ = (int) Math.round(Math.sin(angle) * radius);
			
			//TODO Could there be a better way to search for a position? (Look for possible positions with the "surrounded by normal biomes" property rather than pick a random one and then check if it has this property)
			BlockPos pos = chunkGenerator.getBiomeProvider().func_225531_a_((posX << 4) + 8, 0,(posZ << 4) + 8, 96, Collections.singletonList(normalBiome), worldRand);

			if(pos != null && chunkGenerator.getBiomeProvider().getBiomes(pos.getX(), 0, pos.getZ(), 16).stream().allMatch(biome -> biome == normalBiome))
				return new ChunkPos(pos.getX() >> 4, pos.getZ() >> 4);
		}
		
		int posX = (int) Math.round(Math.cos(angle) * radius);
		int posZ = (int) Math.round(Math.sin(angle) * radius);
		LOGGER.warn("Did not come across a decent location for land gates. Placing it without regard to any biomes.");
		
		BlockPos pos = chunkGenerator.getBiomeProvider().func_225531_a_((posX << 4) + 8, 0, (posZ << 4) + 8, 96, Collections.singletonList(normalBiome), worldRand);
		
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
		public void func_230364_a_(DynamicRegistries registries, ChunkGenerator generator, TemplateManager templates, int chunkX, int chunkZ, Biome biome, NoFeatureConfig config)
		{
			PieceFactory factory = null;
			/*if(generator.getSettings() instanceof LandGenSettings) TODO
			{
				LandGenSettings settings = (LandGenSettings) generator.getSettings();
				factory = settings.getGatePiece();
			}*/
			
			if(factory == null)
				factory = GatePillarPiece::new;
			
			components.add(factory.create(generator, rand, chunkX * 16 + rand.nextInt(16), chunkZ * 16 + rand.nextInt(16)));
			recalculateStructureSize();
		}
		
		private BlockPos findGatePos()
		{
			for(StructurePiece piece : components)
			{
				if(piece instanceof GatePiece)
					return ((GatePiece) piece).getGatePos();
			}
			
			LOGGER.error("Did not find a gate piece in gate structure. Instead had components {}.", components);
			return null;
		}
	}
	
	public interface PieceFactory
	{
		GatePiece create(ChunkGenerator generator, Random rand, int minX, int minZ);
	}
}