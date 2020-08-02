package com.mraof.minestuck.world.gen.feature.structure.village;

import com.mojang.datafixers.Dynamic;
import com.mraof.minestuck.Minestuck;
import com.mraof.minestuck.world.biome.MSBiomes;
import com.mraof.minestuck.world.gen.LandGenSettings;
import com.mraof.minestuck.world.lands.LandTypePair;
import net.minecraft.util.SharedSeedRandom;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.MutableBoundingBox;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.BiomeManager;
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
	public boolean canBeGenerated(BiomeManager biomeManagerIn, ChunkGenerator<?> generatorIn, Random randIn, int chunkX, int chunkZ, Biome biomeIn) {
		ChunkPos pos = this.getStartPositionForPosition(generatorIn, randIn, chunkX, chunkZ, 0, 0);

		if(chunkX == pos.x && chunkZ == pos.z)
		{
			return generatorIn.getBiomeProvider().getBiomes(chunkX * 16 + 8, 0, chunkZ * 16 + 8, 16).stream().allMatch(biome -> biome == MSBiomes.LAND_NORMAL);
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
		return Minestuck.MOD_ID + ":consort_village";
	}
	
	@Override
	public int getSize()
	{
		return 8;
	}
	
	private static class Start extends StructureStart
	{
		
		Start(Structure<?> p_i225801_1_, int p_i225801_2_, int p_i225801_3_, MutableBoundingBox p_i225801_4_, int p_i225801_5_, long p_i225801_6_) {
			super(p_i225801_1_, p_i225801_2_, p_i225801_3_, p_i225801_4_, p_i225801_5_, p_i225801_6_);
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