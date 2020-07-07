package com.mraof.minestuck.world.biome.gen;

import com.google.common.collect.Sets;
import com.mraof.minestuck.world.biome.LandBiomeHolder;
import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.provider.BiomeProvider;
import net.minecraft.world.gen.feature.structure.Structure;
import net.minecraft.world.gen.layer.Layer;

import javax.annotation.Nullable;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.Set;

public class LandBiomeProvider extends BiomeProvider
{
	private final Layer genLevelLayer;
	private final Layer blockLevelLayer;
	private final LandBiomeHolder biomeHolder;
	
	public LandBiomeProvider(LandBiomeProviderSettings settings)
	{
		topBlocksCache.add(settings.getGenSettings().getBlockRegistry().getBlockState("surface"));
		
		Layer[] layers = LandBiomeLayers.buildLandProcedure(settings.getSeed(), settings.getGenSettings());
		
		genLevelLayer = layers[0];
		blockLevelLayer = layers[1];
		biomeHolder = settings.getGenSettings().getBiomeHolder();
	}
	
	@Override
	public Biome getBiome(int x, int z)
	{
		return blockLevelLayer.func_215738_a(x, z);
	}
	
	@Override
	public Biome getBiomeAtFactorFour(int x, int z)
	{
		return genLevelLayer.func_215738_a(x, z);
	}
	
	@Override
	public Biome[] getBiomes(int x, int z, int width, int length, boolean cacheFlag)
	{
		return blockLevelLayer.generateBiomes(x, z, width, length);
	}
	
	@Override
	public Set<Biome> getBiomesInSquare(int centerX, int centerZ, int sideLength)
	{
		int minX = centerX - sideLength >> 2;
		int minZ = centerZ - sideLength >> 2;
		int maxX = centerX + sideLength >> 2;
		int maxZ = centerZ + sideLength >> 2;
		int sizeX = maxX - minX + 1;
		int sizeZ = maxZ - minZ + 1;
		Set<Biome> biomes = Sets.newHashSet();
		Collections.addAll(biomes, genLevelLayer.generateBiomes(minX, minZ, sizeX, sizeZ));
		return biomes;
	}
	
	@Nullable
	@Override
	public BlockPos findBiomePosition(int x, int z, int range, List<Biome> biomes, Random random)
	{
		int minX = x - range >> 2;
		int minZ = z - range >> 2;
		int maxX = x + range >> 2;
		int maxZ = z + range >> 2;
		int sizeX = maxX - minX + 1;
		int sizeZ = maxZ - minZ + 1;
		Biome[] generatedBiomes = genLevelLayer.generateBiomes(minX, minZ, sizeX, sizeZ);
		BlockPos pos = null;
		int matches = 0;
		
		for(int index = 0; index < sizeX * sizeZ; ++index)
		{
			int posX = minX + index % sizeX << 2;
			int posZ = minZ + index / sizeX << 2;
			if (biomes.contains(generatedBiomes[index]))
			{
				if (pos == null || random.nextInt(matches + 1) == 0)
					pos = new BlockPos(posX, 0, posZ);
				
				matches++;
			}
		}
		
		return pos;
	}
	
	@Override
	public boolean hasStructure(Structure<?> structureIn)
	{
		return hasStructureCache.computeIfAbsent(structureIn, this::isStructureInBiomes);
	}
	
	private boolean isStructureInBiomes(Structure<?> structure)
	{
		for(Biome biome : biomeHolder.getBiomes())
		{
			if(biome.hasStructure(structure))
				return true;
		}
		return false;
	}
	
	@Override
	public Set<BlockState> getSurfaceBlocks()
	{
		return topBlocksCache;
	}
}