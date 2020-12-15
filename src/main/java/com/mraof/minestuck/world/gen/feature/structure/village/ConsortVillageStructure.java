package com.mraof.minestuck.world.gen.feature.structure.village;

import com.mojang.serialization.Codec;
import com.mraof.minestuck.Minestuck;
import com.mraof.minestuck.world.biome.LandBiomeSet;
import com.mraof.minestuck.world.gen.LandGenSettings;
import com.mraof.minestuck.world.lands.LandTypePair;
import net.minecraft.util.SharedSeedRandom;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.MutableBoundingBox;
import net.minecraft.util.registry.DynamicRegistries;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.GenerationStage;
import net.minecraft.world.gen.feature.NoFeatureConfig;
import net.minecraft.world.gen.feature.structure.Structure;
import net.minecraft.world.gen.feature.structure.StructureStart;
import net.minecraft.world.gen.feature.template.TemplateManager;

public class ConsortVillageStructure extends Structure<NoFeatureConfig>
{
	private static final int VILLAGE_DISTANCE = 24;
	private static final int MIN_VILLAGE_DISTANCE = 5;
	
	public ConsortVillageStructure(Codec<NoFeatureConfig> codec)
	{
		super(codec);
	}
	
	@Override
	public GenerationStage.Decoration getDecorationStage()
	{
		return GenerationStage.Decoration.SURFACE_STRUCTURES;
	}
	
	@Override
	public IStartFactory<NoFeatureConfig> getStartFactory()
	{
		return Start::new;
	}
	
	@Override
	public String getStructureName()
	{
		return Minestuck.MOD_ID + ":consort_village";
	}
	
	private static class Start extends StructureStart<NoFeatureConfig>
	{
		
		Start(Structure<NoFeatureConfig> structure, int chunkX, int chunkZ, MutableBoundingBox boundingBox, int reference, long seed)
		{
			super(structure, chunkX, chunkZ, boundingBox, reference, seed);
		}
		
		@Override
		public void func_230364_a_(DynamicRegistries registries, ChunkGenerator generator, TemplateManager templates, int chunkX, int chunkZ, Biome biome, NoFeatureConfig config)
		{/* TODO
			if(generator.getSettings() instanceof LandGenSettings)
			{
				LandGenSettings settings = (LandGenSettings) generator.getSettings();
				LandTypePair landTypes = settings.getLandTypes();
				List<ConsortVillagePieces.PieceWeight> pieceWeightList = ConsortVillagePieces.getStructureVillageWeightedPieceList(rand, landTypes);
				ConsortVillageCenter.VillageCenter start = ConsortVillageCenter.getVillageStart((chunkX << 4) + rand.nextInt(16), (chunkZ << 4) + rand.nextInt(16), rand, pieceWeightList, landTypes);
				components.add(start);
				start.buildComponent(start, components, rand);
				
				while(!start.pendingHouses.isEmpty() || !start.pendingRoads.isEmpty())
				{
					if(!start.pendingRoads.isEmpty())
					{
						int index = rand.nextInt(start.pendingRoads.size());
						StructurePiece component = start.pendingRoads.remove(index);
						component.buildComponent(start, components, rand);
					} else
					{
						int index = rand.nextInt(start.pendingHouses.size());
						StructurePiece component = start.pendingHouses.remove(index);
						component.buildComponent(start, components, rand);
					}
				}
				recalculateStructureSize();
			}*/
		}
	}
}