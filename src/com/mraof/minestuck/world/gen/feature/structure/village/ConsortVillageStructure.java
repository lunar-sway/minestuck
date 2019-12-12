package com.mraof.minestuck.world.gen.feature.structure.village;

import com.mojang.datafixers.Dynamic;
import com.mraof.minestuck.world.biome.MSBiomes;
import com.mraof.minestuck.world.gen.LandGenSettings;
import com.mraof.minestuck.world.lands.LandTypePair;
import net.minecraft.util.SharedSeedRandom;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.MutableBoundingBox;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.feature.NoFeatureConfig;
import net.minecraft.world.gen.feature.structure.Structure;
import net.minecraft.world.gen.feature.structure.StructurePiece;
import net.minecraft.world.gen.feature.structure.StructureStart;
import net.minecraft.world.gen.feature.template.TemplateManager;

import java.util.List;
import java.util.Random;
import java.util.function.Function;

public class ConsortVillageStructure extends Structure<NoFeatureConfig>	//TODO Implement this
{
	private static final int VILLAGE_DISTANCE = 24;
	private static final int MIN_VILLAGE_DISTANCE = 5;
	
	public ConsortVillageStructure(Function<Dynamic<?>, ? extends NoFeatureConfig> configFactory)
	{
		super(configFactory);
	}
	
	@Override
	protected ChunkPos getStartPositionForPosition(ChunkGenerator<?> chunkGenerator, Random random, int x, int z, int spacingOffsetsX, int spacingOffsetsZ)
	{
		if(x < 0)
		{
			x -= VILLAGE_DISTANCE - 1;
		}
		
		if(z < 0)
		{
			z -= VILLAGE_DISTANCE - 1;
		}
		
		x = x / VILLAGE_DISTANCE;
		z = z / VILLAGE_DISTANCE;
		x += spacingOffsetsX;
		z += spacingOffsetsZ;
		((SharedSeedRandom)random).setLargeFeatureSeedWithSalt(chunkGenerator.getSeed(), x, z, 10387312);
		x = x * VILLAGE_DISTANCE;
		z = z * VILLAGE_DISTANCE;
		x = x + random.nextInt(VILLAGE_DISTANCE - MIN_VILLAGE_DISTANCE);
		z = z + random.nextInt(VILLAGE_DISTANCE - MIN_VILLAGE_DISTANCE);
		
		return new ChunkPos(x, z);
	}
	
	@Override
	public boolean hasStartAt(ChunkGenerator<?> chunkGenerator, Random random, int chunkX, int chunkZ)
	{
		ChunkPos pos = this.getStartPositionForPosition(chunkGenerator, random, chunkX, chunkZ, 0, 0);
		
		if(chunkX == pos.x && chunkZ == pos.z)
		{
			return chunkGenerator.getBiomeProvider().getBiomesInSquare(chunkX * 16 + 8, chunkZ * 16 + 8, 16).stream().allMatch(biome -> biome == MSBiomes.LAND_NORMAL);
		}
		return false;
	}
	
	@Override
	public IStartFactory getStartFactory()
	{
		return Start::new;
	}
	
	@Override
	public String getStructureName()
	{
		return "consort_village";
	}
	
	@Override
	public int getSize()
	{
		return 8;
	}
	
	private static class Start extends StructureStart
	{
		
		Start(Structure<?> structureIn, int chunkX, int chunkZ, Biome biomeIn, MutableBoundingBox boundsIn, int referenceIn, long seed)
		{
			super(structureIn, chunkX, chunkZ, biomeIn, boundsIn, referenceIn, seed);
		}
		
		@Override
		public void init(ChunkGenerator<?> generator, TemplateManager templateManagerIn, int chunkX, int chunkZ, Biome biomeIn)
		{
			if(generator.getSettings() instanceof LandGenSettings)
			{
				LandGenSettings settings = (LandGenSettings) generator.getSettings();
				LandTypePair landTypes = settings.getLandTypes();
				List<ConsortVillagePieces.PieceWeight> pieceWeightList = ConsortVillagePieces.getStructureVillageWeightedPieceList(rand, landTypes.terrain.getConsortType(), landTypes);
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
			}
		}
	}
}