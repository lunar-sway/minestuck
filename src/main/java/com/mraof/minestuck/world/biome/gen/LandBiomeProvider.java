package com.mraof.minestuck.world.biome.gen;

/* TODO
public class LandBiomeProvider extends BiomeProvider
{
	private final Layer genLevelLayer;
	private final LandBiomeHolder biomeHolder;
	
	public LandBiomeProvider(LandBiomeProviderSettings settings)
	{
		super(settings.getBiomes().getAll());
		topBlocksCache.add(settings.getGenSettings().getBlockRegistry().getBlockState("surface"));

		this.genLevelLayer = LandBiomeLayers.buildLandProcedure(settings.getSeed(), settings.getGenSettings());
		biomeHolder = settings.getGenSettings().getBiomeHolder();
	}
	
	@Override
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
}*/