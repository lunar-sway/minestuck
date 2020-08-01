package com.mraof.minestuck.world.biome.gen;

import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Sets;
import com.mraof.minestuck.world.biome.LandBiomeHolder;
import com.mraof.minestuck.world.biome.MSBiomes;
import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.Biomes;
import net.minecraft.world.biome.provider.BiomeProvider;
import net.minecraft.world.gen.LazyAreaLayerContext;
import net.minecraft.world.gen.area.IAreaFactory;
import net.minecraft.world.gen.area.LazyArea;
import net.minecraft.world.gen.feature.structure.Structure;
import net.minecraft.world.gen.layer.Layer;
import net.minecraft.world.gen.layer.LayerUtil;

import javax.annotation.Nullable;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.Set;

public class LandBiomeProvider extends BiomeProvider
{
	private final Layer genLevelLayer;
	private final LandBiomeHolder biomeHolder;
	
	public LandBiomeProvider(LandBiomeProviderSettings settings)
	{
		super(ImmutableSet.of(MSBiomes.LAND_NORMAL, MSBiomes.LAND_OCEAN, MSBiomes.LAND_ROUGH));
		topBlocksCache.add(settings.getGenSettings().getBlockRegistry().getBlockState("surface"));

		this.genLevelLayer = LandBiomeLayers.buildLandProcedure(settings.getSeed(), settings.getGenSettings());
		biomeHolder = settings.getGenSettings().getBiomeHolder();
	}

	public Biome getNoiseBiome(int x, int y, int z) {
		return this.genLevelLayer.func_215738_a(x, z);
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