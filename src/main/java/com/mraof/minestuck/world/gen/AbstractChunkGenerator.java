package com.mraof.minestuck.world.gen;

import net.minecraft.core.*;
import net.minecraft.server.level.WorldGenRegion;
import net.minecraft.world.level.*;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.BiomeManager;
import net.minecraft.world.level.biome.BiomeSource;
import net.minecraft.world.level.biome.Climate;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.ChunkAccess;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.chunk.LevelChunkSection;
import net.minecraft.world.level.chunk.ProtoChunk;
import net.minecraft.world.level.levelgen.*;
import net.minecraft.world.level.levelgen.blending.Blender;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;
import net.minecraft.world.level.levelgen.structure.StructureSet;
import net.minecraft.world.level.levelgen.synth.NormalNoise;

import javax.annotation.Nullable;
import java.text.DecimalFormat;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.function.Predicate;

/**
 * Helper class that behaves similarly to {@link NoiseBasedChunkGenerator}, but extendable with additional features.
 */
public abstract class AbstractChunkGenerator extends ChunkGenerator
{
	// Sections are portions of a chunk, which may contain multiple block
	// Noise values are generated for sections,
	// and actual blocks are interpolated between the four neighboring sections
	// Sections map 1:1 to the noise biome layer
	private final int sectionHeight;
	private final int sectionWidth;
	private final int sectionCountXZ;
	private final int sectionCountY;
	
	protected final long seed;
	protected final BlockState defaultBlock;
	protected final BlockState defaultFluid;
	protected final Holder<NoiseGeneratorSettings> settings;
	private final int height;
	
	private final NoiseRouter router;
	private final Climate.Sampler sampler;
	private final SurfaceSystem surfaceSystem;
	private final Aquifer.FluidPicker globalFluidPicker;
	protected final Registry<NormalNoise.NoiseParameters> noises;
	
	@SuppressWarnings("OptionalUsedAsFieldOrParameterType")
	public AbstractChunkGenerator(Registry<StructureSet> structureSets, Registry<NormalNoise.NoiseParameters> noises, Optional<HolderSet<StructureSet>> structureOverrides, BiomeSource biomeSource, BiomeSource runtimeBiomeSource, long seed, Holder<NoiseGeneratorSettings> settingsHolder)
	{
		super(structureSets, structureOverrides, biomeSource, runtimeBiomeSource, seed);
		
		this.seed = seed;
		this.settings = settingsHolder;
		NoiseGeneratorSettings settings = settingsHolder.value();
		defaultBlock = settings.defaultBlock();
		defaultFluid = settings.defaultFluid();
		
		NoiseSettings noiseSettings = settings.noiseSettings();
		height = noiseSettings.height();
		sectionHeight = noiseSettings.getCellHeight();
		sectionWidth = noiseSettings.getCellWidth();
		sectionCountXZ = 16 / sectionWidth;
		sectionCountY = noiseSettings.getCellCountY();
		
		this.noises = noises;
		this.router = settings.createNoiseRouter(noises, seed);
		this.sampler = new Climate.Sampler(this.router.temperature(), this.router.humidity(), this.router.continents(), this.router.erosion(), this.router.depth(), this.router.ridges(), this.router.spawnTarget());
		Aquifer.FluidStatus oceanFluidStatus = new Aquifer.FluidStatus(settings.seaLevel(), this.defaultFluid);
		this.globalFluidPicker = (x, y, z) -> oceanFluidStatus;
		
		this.surfaceSystem = new SurfaceSystem(noises, this.defaultBlock, settings.seaLevel(), seed, settings.getRandomSource());
	}
	
	@Override
	public Climate.Sampler climateSampler()
	{
		return this.sampler;
	}
	
	/**
	 * Not as optimised as the usual implementation for applyBiomeDecoration(), and does not yet place structures.
	 * But it does properly work when the two biome sources provide different biomes.
	 */
	@Override
	public void applyBiomeDecoration(WorldGenLevel level, ChunkAccess chunk, StructureFeatureManager structureManager)
	{
		SectionPos sectionPos = SectionPos.of(chunk.getPos(), level.getMinSection());
		BlockPos pos = sectionPos.origin();
		WorldgenRandom worldgenrandom = new WorldgenRandom(new XoroshiroRandomSource(RandomSupport.seedUniquifier()));
		//Override WorldGenLevel.getBiome() in order to control the biome checked in BiomeFilter
		WorldGenLevel customBiomeLevel = new WorldGenLevelWrapper(level)
		{
			@Override
			public Holder<Biome> getBiome(BlockPos pos)
			{
				return AbstractChunkGenerator.this.getWorldGenBiome(super.getBiome(pos));
			}
		};
		long seed = worldgenrandom.setDecorationSeed(level.getSeed(), pos.getX(), pos.getZ());
		
		for(int decorationStep = 0; decorationStep < this.biomeSource.featuresPerStep().size(); decorationStep++)
		{
			BiomeSource.StepFeatureData featureStep = this.biomeSource.featuresPerStep().get(decorationStep);
			for(PlacedFeature feature : featureStep.features())
			{
				worldgenrandom.setFeatureSeed(seed, featureStep.indexMapping().applyAsInt(feature), decorationStep);
				feature.placeWithBiomeCheck(customBiomeLevel, this, worldgenrandom, pos);
			}
		}
	}
	
	private NoiseChunk getOrCreateNoiseChunk(StructureFeatureManager structureManager, ChunkAccess chunk, Blender blender)
	{
		return chunk.getOrCreateNoiseChunk(this.router, () -> new Beardifier(structureManager, chunk){}, this.settings.value(), this.globalFluidPicker, blender);
	}
	
	@Override
	public void buildSurface(WorldGenRegion level, StructureFeatureManager structureManager, ChunkAccess chunk)
	{
		NoiseGeneratorSettings settings = this.settings.value();
		NoiseChunk noiseChunk = getOrCreateNoiseChunk(structureManager, chunk, Blender.of(level));
		this.surfaceSystem.buildSurface(level.getBiomeManager(), level.registryAccess().registryOrThrow(Registry.BIOME_REGISTRY),
				settings.useLegacyRandomSource(), new WorldGenerationContext(this, level), chunk, noiseChunk, settings.surfaceRule());
	}
	
	@Override
	public CompletableFuture<ChunkAccess> fillFromNoise(Executor executor, Blender blender, StructureFeatureManager structures, ChunkAccess chunk)
	{
		NoiseChunk noiseChunk = getOrCreateNoiseChunk(structures, chunk, blender);
		ChunkPos chunkPos = chunk.getPos();
		
		int minX = chunkPos.getMinBlockX();
		int minZ = chunkPos.getMinBlockZ();
		
		Heightmap oceanHeight = chunk.getOrCreateHeightmapUnprimed(Heightmap.Types.OCEAN_FLOOR_WG);
		Heightmap surfaceHeight = chunk.getOrCreateHeightmapUnprimed(Heightmap.Types.WORLD_SURFACE_WG);
		BlockPos.MutableBlockPos pos = new BlockPos.MutableBlockPos();
		
		noiseChunk.initializeForFirstCellX();
		
		for(int sectX = 0; sectX < sectionCountXZ; sectX++)
		{
			noiseChunk.advanceCellX(sectX);
			
			for(int sectZ = 0; sectZ < sectionCountXZ; ++sectZ)
			{
				//TODO need to be updated to account for configurable world height
				LevelChunkSection section = chunk.getSection(15);
				section.acquire();
				
				for(int sectY = sectionCountY - 1; sectY >= 0; sectY--)
				{
					noiseChunk.selectCellYZ(sectY, sectZ);
					
					for(int relY = sectionHeight - 1; relY >= 0; relY--)
					{
						int y = sectY * sectionHeight + relY;
						//cs as in chunk-section (section used for block storage in a chunk, and not the noise sections)
						int csRelY = y & 15;
						int csY = y >> 4;
						if (section.bottomBlockY() >> 4 != csY) {
							section.release();
							section = chunk.getSection(csY);
							section.acquire();
						}
						
						double fracRelY = (double)relY / (double)sectionHeight;
						
						noiseChunk.updateForY(y, fracRelY);
						
						for(int relX = 0; relX < sectionWidth; relX++)
						{
							int x = minX + sectX * sectionWidth + relX;
							int csRelX = x & 15;
							double fracRelX = (double)relX / (double)sectionWidth;
							
							noiseChunk.updateForX(x, fracRelX);
							
							for(int relZ = 0; relZ < sectionWidth; relZ++)
							{
								int z = minZ + sectZ * sectionWidth + relZ;
								int csRelZ = z & 15;
								double fracRelZ = (double)relZ / (double)sectionWidth;
								
								noiseChunk.updateForZ(z, fracRelZ);
								
								BlockState state = noiseChunk.getInterpolatedState();
								if(state == null)
									state = defaultBlock;
								
								if (state != Blocks.AIR.defaultBlockState()) {
									pos.set(x, y, z);
									if (state.getLightEmission(chunk, pos) != 0 && chunk instanceof ProtoChunk) {
										((ProtoChunk) chunk).addLight(pos);
									}
									
									section.setBlockState(csRelX, csRelY, csRelZ, state, false);
									oceanHeight.update(csRelX, y, csRelZ, state);
									surfaceHeight.update(csRelX, y, csRelZ, state);
								}
							}
						}
					}
				}
				
				section.release();
			}
			
			noiseChunk.swapSlices();
		}
		
		noiseChunk.stopInterpolation();
		return CompletableFuture.completedFuture(chunk);
	}
	
	@Override
	public int getBaseHeight(int x, int z, Heightmap.Types type, LevelHeightAccessor height)
	{
		return iterateEarlyBlocks(x, z, null, type.isOpaque());
	}
	
	@Override
	public NoiseColumn getBaseColumn(int x, int z, LevelHeightAccessor height)
	{
		BlockState[] states = new BlockState[this.height];
		iterateEarlyBlocks(x, z, states, null);
		return new NoiseColumn(0, states);
	}
	
	/**
	 * Iterates block states from top to bottom from the earliest generation stage
	 *
	 * @param stateCollector If not null, the list will be filled with block states as they are iterated, indexed after height.
	 *                       If present, assumes that the list has the length of at least the world height
	 * @param condition      If not null, will return the height just above the top block which the condition is true for
	 */
	private int iterateEarlyBlocks(int x, int z, @Nullable BlockState[] stateCollector, @Nullable Predicate<BlockState> condition)
	{
		int sectX = Math.floorDiv(x, sectionWidth);
		int sectZ = Math.floorDiv(z, sectionWidth);
		int modX = Math.floorMod(x, sectionWidth);
		int modZ = Math.floorMod(z, sectionWidth);
		double modFracX = (double) modX / (double) sectionWidth;
		double modFracZ = (double) modZ / (double) sectionWidth;
		
		NoiseChunk noiseChunk = NoiseChunk.forColumn(sectX * sectionWidth, sectZ * sectionWidth, 0, sectionCountY, this.router, this.settings.value(), this.globalFluidPicker);
		noiseChunk.initializeForFirstCellX();
		noiseChunk.advanceCellX(0);
		
		for(int sectY = sectionCountY - 1; sectY >= 0; sectY--)
		{
			noiseChunk.selectCellYZ(sectY, 0);
			
			for(int modY = sectionHeight - 1; modY >= 0; modY--)
			{
				double modFracY = (double) modY / (double) sectionHeight;
				// interpolated noise for this block
				int y = sectY * this.sectionHeight + modY;
				noiseChunk.updateForY(y, modFracY);
				noiseChunk.updateForX(x, modFracX);
				noiseChunk.updateForZ(z, modFracZ);
				
				// Returns null if it should be solid ground. Otherwise, it'll use the fluid picker to determine if it should be some fluid or air.
				BlockState state = noiseChunk.getInterpolatedState();
				if(state == null)
					state = defaultBlock;
				
				if(stateCollector != null)
					stateCollector[y] = state;
				
				if(condition != null && condition.test(state))
					return y + 1;
			}
		}
		
		return 0;
	}
	
	@Override
	public int getGenDepth()
	{
		return height;
	}
	
	@Override
	public int getSeaLevel()
	{
		return settings.value().seaLevel();
	}
	
	@Override
	public void spawnOriginalMobs(WorldGenRegion region)
	{
		ChunkPos pos = region.getCenter();
		Holder<Biome> biome = region.getBiome(pos.getWorldPosition().atY(region.getMaxBuildHeight() - 1));
		WorldgenRandom random = new WorldgenRandom(new LegacyRandomSource(RandomSupport.seedUniquifier()));
		random.setDecorationSeed(region.getSeed(), pos.getMinBlockX(), pos.getMinBlockZ());
		NaturalSpawner.spawnMobsForChunkGeneration(region, biome, pos, random);
	}
	
	protected Holder<Biome> getWorldGenBiome(Holder<Biome> actualBiome)
	{
		return actualBiome;
	}
	
	@Override
	public int getMinY()
	{
		return this.settings.value().noiseSettings().minY();
	}
	
	@Override
	public void addDebugScreenInfo(List<String> infoList, BlockPos pos)
	{
		DecimalFormat decimalformat = new DecimalFormat("0.000");
		DensityFunction.SinglePointContext point = new DensityFunction.SinglePointContext(pos.getX(), pos.getY(), pos.getZ());
		infoList.add("NoiseRouter T: " + decimalformat.format(this.router.temperature().compute(point)) + " H: " + decimalformat.format(this.router.humidity().compute(point))
				+ " C: " + decimalformat.format(this.router.continents().compute(point)) + " E: " + decimalformat.format(this.router.erosion().compute(point))
				+ " D: " + decimalformat.format(this.router.depth().compute(point)) + " W: " + decimalformat.format(this.router.ridges().compute(point))
				+ " AS: " + decimalformat.format(this.router.initialDensityWithoutJaggedness().compute(point)) + " N: " + decimalformat.format(this.router.finalDensity().compute(point)));}
	
	@Override
	public void applyCarvers(WorldGenRegion pLevel, long pSeed, BiomeManager pBiomeManager, StructureFeatureManager pStructureFeatureManager, ChunkAccess pChunk, GenerationStep.Carving pStep)
	{
	
	}
}