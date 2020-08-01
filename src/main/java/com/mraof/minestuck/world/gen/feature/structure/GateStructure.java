package com.mraof.minestuck.world.gen.feature.structure;

import com.mojang.datafixers.Dynamic;
import com.mraof.minestuck.Minestuck;
import com.mraof.minestuck.util.Debug;
import com.mraof.minestuck.world.biome.MSBiomes;
import com.mraof.minestuck.world.gen.LandChunkGenerator;
import com.mraof.minestuck.world.gen.LandGenSettings;
import net.minecraft.util.SharedSeedRandom;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.MutableBoundingBox;
import net.minecraft.world.IWorld;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.BiomeManager;
import net.minecraft.world.chunk.ChunkStatus;
import net.minecraft.world.dimension.DimensionType;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.feature.NoFeatureConfig;
import net.minecraft.world.gen.feature.structure.MineshaftConfig;
import net.minecraft.world.gen.feature.structure.Structure;
import net.minecraft.world.gen.feature.structure.StructurePiece;
import net.minecraft.world.gen.feature.structure.StructureStart;
import net.minecraft.world.gen.feature.template.TemplateManager;
import net.minecraft.world.server.ServerWorld;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.function.Function;

public class GateStructure extends Structure<NoFeatureConfig>
{
	private final Map<DimensionType, ChunkPos> positionCache = new HashMap<>();
	
	public GateStructure(Function<Dynamic<?>, ? extends NoFeatureConfig> configFactory)
	{
		super(configFactory);
	}
	
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
	
	@Override
	public IStartFactory getStartFactory()
	{
		return GateStructure.Start::new;
	}
	
	@Override
	public String getStructureName()
	{
		return Minestuck.MOD_ID + ":land_gate";
	}
	
	@Override
	public int getSize()
	{
		return 3; //Note: might not agree with actual gate pieces that are added in the future
	}
	
	public BlockPos findLandGatePos(IWorld world)
	{
		if (world instanceof ServerWorld)
		{
			if(((ServerWorld) world).getChunkProvider().getChunkGenerator().getBiomeProvider().hasStructure(this))
			{
				ChunkPos chunkPos = findGatePosition(world.getDimension().getType(), ((ServerWorld) world).getChunkProvider().getChunkGenerator());

				StructureStart start = world.getChunk(chunkPos.x, chunkPos.z, ChunkStatus.STRUCTURE_STARTS).getStructureStart(getStructureName());

				if(start instanceof Start)
				{
					return ((Start) start).findGatePos();
				} else Debug.warnf("Expected to find gate structure at chunk coords %s, in dimension %s, but found %s!", chunkPos, world.getDimension().getType().getRegistryName(), start);
			}
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
		
		for(; radius < 65; radius += 6)
		{
			int posX = (int) Math.round(Math.cos(angle) * radius);
			int posZ = (int) Math.round(Math.sin(angle) * radius);
			
			//TODO Could there be a better way to search for a position? (Look for possible positions with the "surrounded by normal biomes" property rather than pick a random one and then check if it has this property)
			BlockPos pos = chunkGenerator.getBiomeProvider().func_225531_a_((posX << 4) + 8, 0,(posZ << 4) + 8, 96, Collections.singletonList(MSBiomes.LAND_NORMAL), worldRand);

			if(pos != null && chunkGenerator.getBiomeProvider().getBiomes(pos.getX(), 0, pos.getZ(), 16).stream().allMatch(biome -> biome == MSBiomes.LAND_NORMAL))
				return new ChunkPos(pos.getX() >> 4, pos.getZ() >> 4);
		}
		
		int posX = (int) Math.round(Math.cos(angle) * radius);
		int posZ = (int) Math.round(Math.sin(angle) * radius);
		Debug.warn("Did not come across a decent location for land gates. Placing it without regard to any biomes.");
		
		BlockPos pos = chunkGenerator.getBiomeProvider().func_225531_a_((posX << 4) + 8, 0, (posZ << 4) + 8, 96, Collections.singletonList(MSBiomes.LAND_NORMAL), worldRand);
		
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
	
	public static class Start extends StructureStart
	{
		public Start(Structure<?> p_i225811_1_, int p_i225811_2_, int p_i225811_3_, MutableBoundingBox p_i225811_4_, int p_i225811_5_, long p_i225811_6_) {
			super(p_i225811_1_, p_i225811_2_, p_i225811_3_, p_i225811_4_, p_i225811_5_, p_i225811_6_);
		}
		
		@Override
		public void init(ChunkGenerator<?> generator, TemplateManager templateManagerIn, int chunkX, int chunkZ, Biome biomeIn)
		{
			PieceFactory factory = null;
			if(generator.getSettings() instanceof LandGenSettings)
			{
				LandGenSettings settings = (LandGenSettings) generator.getSettings();
				factory = settings.getGatePiece();
			}
			
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
			
			Debug.errorf("Did not find a gate piece in gate structure. Instead had components %s.", components);
			return null;
		}
	}
	
	public interface PieceFactory
	{
		GatePiece create(ChunkGenerator<?> generator, Random rand, int minX, int minZ);
	}
}