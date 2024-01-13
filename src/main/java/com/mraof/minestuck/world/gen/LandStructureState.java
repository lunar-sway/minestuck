package com.mraof.minestuck.world.gen;

import com.mojang.datafixers.util.Pair;
import com.mraof.minestuck.util.MSTags;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.BiomeSource;
import net.minecraft.world.level.chunk.ChunkGeneratorStructureState;
import net.minecraft.world.level.levelgen.RandomState;
import net.minecraft.world.level.levelgen.structure.StructureSet;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;

public final class LandStructureState extends ChunkGeneratorStructureState
{
	private static final Logger LOGGER = LogManager.getLogger();
	
	private final BiomeSource biomeSource;
	
	private ChunkPos landGatePosition;
	
	public LandStructureState(RandomState randomState, BiomeSource biomeSource, long seed, List<Holder<StructureSet>> possibleStructureSets)
	{
		super(randomState, biomeSource, seed, seed, possibleStructureSets);
		this.biomeSource = biomeSource;
	}
	
	public ChunkPos getOrFindLandGatePosition()
	{
		if (this.landGatePosition == null)
			this.landGatePosition = findLandGatePosition(this.biomeSource, this.randomState(), RandomSource.create(this.getLevelSeed()));
		return this.landGatePosition;
	}
	
	private static ChunkPos findLandGatePosition(BiomeSource biomeSource, RandomState randomState, RandomSource worldRand)
	{
		double angle = 2 * Math.PI * worldRand.nextDouble();
		int radius = 38 + worldRand.nextInt(12);
		
		for(; radius < 65; radius += 6)
		{
			int posX = (int) Math.round(Math.cos(angle) * radius);
			int posZ = (int) Math.round(Math.sin(angle) * radius);
			
			//TODO Could there be a better way to search for a position? (Look for possible positions with the "surrounded by normal biomes" property rather than pick a random one and then check if it has this property)
			Pair<BlockPos, Holder<Biome>> result = biomeSource.findBiomeHorizontal((posX << 4) + 8, 0,(posZ << 4) + 8, 96,
					biome -> biome.is(MSTags.Biomes.LAND_NORMAL), worldRand, randomState.sampler());
			
			if(result != null)
			{
				BlockPos pos = result.getFirst();
				if(biomeSource.getBiomesWithin(pos.getX(), 0, pos.getZ(), 16,
						randomState.sampler()).stream().allMatch(biome -> biome.is(MSTags.Biomes.LAND_NORMAL)))
					return new ChunkPos(pos);
			}
		}
		
		int posX = (int) Math.round(Math.cos(angle) * radius);
		int posZ = (int) Math.round(Math.sin(angle) * radius);
		LOGGER.warn("Did not come across a decent location for land gates. Placing it without regard to any biomes.");
		
		Pair<BlockPos, Holder<Biome>> result = biomeSource.findBiomeHorizontal((posX << 4) + 8, 0, (posZ << 4) + 8, 96,
				biome -> biome.is(MSTags.Biomes.LAND_NORMAL), worldRand, randomState.sampler());
		
		if(result != null)
			return new ChunkPos(result.getFirst());
		
		return new ChunkPos(posX, posZ);
	}
}
