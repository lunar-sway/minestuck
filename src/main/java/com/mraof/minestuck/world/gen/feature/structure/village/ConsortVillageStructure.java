package com.mraof.minestuck.world.gen.feature.structure.village;

import com.mojang.serialization.Codec;
import com.mraof.minestuck.world.lands.LandTypePair;
import com.mraof.minestuck.world.lands.LandTypes;
import net.minecraft.util.math.MutableBoundingBox;
import net.minecraft.util.registry.DynamicRegistries;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.GenerationStage;
import net.minecraft.world.gen.feature.NoFeatureConfig;
import net.minecraft.world.gen.feature.structure.Structure;
import net.minecraft.world.gen.feature.structure.StructurePiece;
import net.minecraft.world.gen.feature.structure.StructureStart;
import net.minecraft.world.gen.feature.template.TemplateManager;

import java.util.List;

public class ConsortVillageStructure extends Structure<NoFeatureConfig>
{
	private static final int VILLAGE_DISTANCE = 24;
	private static final int MIN_VILLAGE_DISTANCE = 5;
	
	public ConsortVillageStructure(Codec<NoFeatureConfig> codec)
	{
		super(codec);
	}
	
	@Override
	public GenerationStage.Decoration step()
	{
		return GenerationStage.Decoration.SURFACE_STRUCTURES;
	}
	
	@Override
	public IStartFactory<NoFeatureConfig> getStartFactory()
	{
		return Start::new;
	}
	
	private static class Start extends StructureStart<NoFeatureConfig>
	{
		
		Start(Structure<NoFeatureConfig> structure, int chunkX, int chunkZ, MutableBoundingBox boundingBox, int reference, long seed)
		{
			super(structure, chunkX, chunkZ, boundingBox, reference, seed);
		}
		
		@Override
		public void generatePieces(DynamicRegistries registries, ChunkGenerator generator, TemplateManager templates, int chunkX, int chunkZ, Biome biome, NoFeatureConfig config)
		{
			LandTypePair landTypes = new LandTypePair(LandTypes.TERRAIN_NULL, LandTypes.TITLE_NULL); //TODO get land types from somewhere, either by some dimension-linked object like biome provider, or through the feature config
			List<ConsortVillagePieces.PieceWeight> pieceWeightList = ConsortVillagePieces.getStructureVillageWeightedPieceList(random, landTypes);
			ConsortVillageCenter.VillageCenter start = ConsortVillageCenter.getVillageStart((chunkX << 4) + random.nextInt(16), (chunkZ << 4) + random.nextInt(16), random, pieceWeightList, landTypes);
			pieces.add(start);
			start.addChildren(start, pieces, random);
			
			while(!start.pendingHouses.isEmpty() || !start.pendingRoads.isEmpty())
			{
				if(!start.pendingRoads.isEmpty())
				{
					int index = random.nextInt(start.pendingRoads.size());
					StructurePiece component = start.pendingRoads.remove(index);
					component.addChildren(start, pieces, random);
				} else
				{
					int index = random.nextInt(start.pendingHouses.size());
					StructurePiece component = start.pendingHouses.remove(index);
					component.addChildren(start, pieces, random);
				}
			}
			calculateBoundingBox();
		}
	}
}