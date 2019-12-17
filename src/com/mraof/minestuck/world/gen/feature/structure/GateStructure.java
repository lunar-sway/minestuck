package com.mraof.minestuck.world.gen.feature.structure;

import com.mojang.datafixers.Dynamic;
import com.mraof.minestuck.Minestuck;
import com.mraof.minestuck.util.Debug;
import com.mraof.minestuck.world.biome.MSBiomes;
import com.mraof.minestuck.world.gen.LandGenSettings;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.MutableBoundingBox;
import net.minecraft.world.IWorld;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.chunk.ChunkStatus;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.feature.NoFeatureConfig;
import net.minecraft.world.gen.feature.structure.Structure;
import net.minecraft.world.gen.feature.structure.StructurePiece;
import net.minecraft.world.gen.feature.structure.StructureStart;
import net.minecraft.world.gen.feature.template.TemplateManager;

import java.util.Collections;
import java.util.Random;
import java.util.function.Function;

public class GateStructure extends Structure<NoFeatureConfig>
{
	public GateStructure(Function<Dynamic<?>, ? extends NoFeatureConfig> configFactory)
	{
		super(configFactory);
	}
	
	@Override
	protected ChunkPos getStartPositionForPosition(ChunkGenerator<?> chunkGenerator, Random random, int x, int z, int spacingOffsetsX, int spacingOffsetsZ)
	{
		//TODO implement something similar to the stronghold structure here as a replacement to GateHandler.findGatePlacement()
		//Notes: The seed might be the same for all dimensions. Check that.
		// Idea: Make the LandDimension combine the seed with something based on the land dimension.
		//Strongholds use caching for their generation. If we're doing something similar, we have to keep in mind that it'll likely be active to several dimensions at the same time.
		return findGatePosition(chunkGenerator);
	}
	
	@Override
	public boolean hasStartAt(ChunkGenerator<?> chunkGenerator, Random random, int chunkX, int chunkZ)
	{
		ChunkPos pos = this.getStartPositionForPosition(chunkGenerator, random, chunkX, chunkZ, 0, 0);
		
		return chunkX == pos.x && chunkZ == pos.z;
	}
	
	@Override
	public IStartFactory getStartFactory()
	{
		return Start::new;
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
		if(world.getChunkProvider().getChunkGenerator().getBiomeProvider().hasStructure(this))
		{
			ChunkPos chunkPos = findGatePosition(world.getChunkProvider().getChunkGenerator());
			
			StructureStart start = world.getChunk(chunkPos.x, chunkPos.z, ChunkStatus.STRUCTURE_STARTS).getStructureStart(getStructureName());
			
			if(start instanceof Start)
			{
				return ((Start) start).findGatePos();
			} else Debug.warnf("Expected to find gate structure at chunk coords %s, in dimension %s, but found %s!", chunkPos, world.getDimension().getType().getRegistryName(), start);
		}
		
		return null;
	}
	
	private ChunkPos findGatePosition(ChunkGenerator<?> chunkGenerator)
	{
		//TODO The result here could be cached
		Random worldRand = new Random(chunkGenerator.getSeed());
		
		double angle = 2 * Math.PI * worldRand.nextDouble();
		int radius = 38 + worldRand.nextInt(12);
		
		for(; radius < 65; radius += 6)
		{
			int posX = (int) Math.round(Math.cos(angle) * radius);
			int posZ = (int) Math.round(Math.sin(angle) * radius);
			
			BlockPos pos = chunkGenerator.getBiomeProvider().findBiomePosition((posX << 4) + 8, (posZ << 4) + 8, 96, Collections.singletonList(MSBiomes.LAND_NORMAL), worldRand);
			
			if(pos != null && chunkGenerator.getBiomeProvider().getBiomesInSquare(pos.getX(), pos.getZ(), 16).stream().allMatch(biome -> biome == MSBiomes.LAND_NORMAL))
				return new ChunkPos(pos.getX() >> 4, pos.getZ() >> 4);
		}
		
		int posX = (int) Math.round(Math.cos(angle) * radius);
		int posZ = (int) Math.round(Math.sin(angle) * radius);
		Debug.warn("Did not come across a decent location for land gates. Placing it without regard to any biomes.");
		
		BlockPos pos = chunkGenerator.getBiomeProvider().findBiomePosition((posX << 4) + 8, (posZ << 4) + 8, 96, Collections.singletonList(MSBiomes.LAND_NORMAL), worldRand);
		
		if(pos != null)
			return new ChunkPos(pos.getX() >> 4, pos.getZ() >> 4);
		return new ChunkPos(posX, posZ);
	}
	
	public static class Start extends StructureStart
	{
		Start(Structure<?> structure, int chunkX, int chunkZ, Biome biome, MutableBoundingBox boundingBox, int reference, long seed)
		{
			super(structure, chunkX, chunkZ, biome, boundingBox, reference, seed);
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