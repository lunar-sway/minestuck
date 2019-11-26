package com.mraof.minestuck.world.gen.feature.structure;

import com.mojang.datafixers.Dynamic;
import com.mraof.minestuck.world.gen.LandGenSettings;
import net.minecraft.util.math.MutableBoundingBox;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.feature.NoFeatureConfig;
import net.minecraft.world.gen.feature.structure.Structure;
import net.minecraft.world.gen.feature.structure.StructureStart;
import net.minecraft.world.gen.feature.template.TemplateManager;

import java.util.Random;
import java.util.function.Function;

public class GateStructure extends Structure<NoFeatureConfig>
{
	public GateStructure(Function<Dynamic<?>, ? extends NoFeatureConfig> configFactory)
	{
		super(configFactory);
	}
	
	@Override
	public boolean hasStartAt(ChunkGenerator<?> chunkGenerator, Random random, int chunkX, int chunkZ)
	{
		//TODO implement something similar to the stronghold structure here as a replacement to GateHandler.findGatePlacement()
		//Notes: The seed might be the same for all dimensions. Check that.
		//Strongholds use caching for their generation. If we're doing something similar, we have to keep in mind that it'll likely be active to several dimensions at the same time.
		return chunkX == 40 && chunkZ == 40;
	}
	
	@Override
	public IStartFactory getStartFactory()
	{
		return Start::new;
	}
	
	@Override
	public String getStructureName()
	{
		return "land_gate";
	}
	
	@Override
	public int getSize()
	{
		return 3; //Note: might not agree with actual gate pieces that are added in the future
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
			
			components.add(factory.create(rand, chunkX * 16 + rand.nextInt(16), chunkZ * 16 + rand.nextInt(16)));
			recalculateStructureSize();
		}
	}
	
	public interface PieceFactory
	{
		GatePiece create(Random rand, int minX, int minZ);
	}
}