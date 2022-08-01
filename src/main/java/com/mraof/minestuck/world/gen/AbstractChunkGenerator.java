package com.mraof.minestuck.world.gen;

import com.google.common.collect.ImmutableList;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderSet;
import net.minecraft.core.Registry;
import net.minecraft.server.level.WorldGenRegion;
import net.minecraft.util.Mth;
import net.minecraft.world.level.*;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.BiomeManager;
import net.minecraft.world.level.biome.BiomeSource;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.ChunkAccess;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.chunk.LevelChunkSection;
import net.minecraft.world.level.chunk.ProtoChunk;
import net.minecraft.world.level.levelgen.*;
import net.minecraft.world.level.levelgen.blending.Blender;
import net.minecraft.world.level.levelgen.structure.StructureSet;
import net.minecraft.world.level.levelgen.synth.BlendedNoise;
import net.minecraft.world.level.levelgen.synth.NormalNoise;
import net.minecraft.world.level.levelgen.synth.PerlinNoise;
import net.minecraft.world.level.levelgen.synth.PerlinSimplexNoise;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.function.Predicate;
import java.util.stream.IntStream;

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
	
	private final DensityFunction densityFunction;
	private final DensityFunction scaledDepthFunction;
	private final PerlinSimplexNoise surfaceNoise;
	private final PerlinNoise depthNoise;
	
	protected final long seed;
	protected final WorldgenRandom random;
	protected final BlockState defaultBlock;
	protected final BlockState defaultFluid;
	protected final Holder<NoiseGeneratorSettings> settings;
	private final int height;
	
	private final NoiseRouter router;
	private final SurfaceSystem surfaceSystem;
	private final Aquifer.FluidPicker globalFluidPicker;
	protected final Registry<NormalNoise.NoiseParameters> noises;
	
	@SuppressWarnings("OptionalUsedAsFieldOrParameterType")
	public AbstractChunkGenerator(Registry<StructureSet> structureSets, Registry<NormalNoise.NoiseParameters> noises, Optional<HolderSet<StructureSet>> structureOverrides, BiomeSource biomeSource, BiomeSource runtimeBiomeSource, long seed, Holder<NoiseGeneratorSettings> settingsHolder)
	{
		super(structureSets, structureOverrides, biomeSource, runtimeBiomeSource, seed);
		
		this.seed = seed;
		random = new WorldgenRandom(WorldgenRandom.Algorithm.LEGACY.newInstance(seed));
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
		
		densityFunction = new BlendedNoise(random, noiseSettings.noiseSamplingSettings(), sectionWidth, sectionHeight);
		surfaceNoise = new PerlinSimplexNoise(random, IntStream.rangeClosed(-3, 0).boxed().collect(ImmutableList.toImmutableList()));
		random.consumeCount(2620);
		depthNoise = PerlinNoise.createLegacyForBlendedNoise(random, IntStream.rangeClosed(-15, 0));
		
		DensityFunction factorFunction = new DensityFunctions.TerrainShaperSpline(DensityFunctions.zero(), DensityFunctions.zero(), DensityFunctions.zero(), noiseSettings.terrainShaper(), DensityFunctions.TerrainShaperSpline.SplineType.FACTOR, 0.0D, 8.0D);
		DensityFunction offsetFunction = new DensityFunctions.TerrainShaperSpline(DensityFunctions.zero(), DensityFunctions.zero(), DensityFunctions.zero(), noiseSettings.terrainShaper(), DensityFunctions.TerrainShaperSpline.SplineType.OFFSET, -0.81D, 2.5D);
		DensityFunction depthFunction = DensityFunctions.yClampedGradient(0, 256, 1, -1);
		scaledDepthFunction = DensityFunctions.add(DensityFunctions.mul(depthFunction, factorFunction), offsetFunction);
		
		this.noises = noises;
		this.router = settings.createNoiseRouter(noises, seed);
		Aquifer.FluidStatus oceanFluidStatus = new Aquifer.FluidStatus(settings.seaLevel(), this.defaultFluid);
		this.globalFluidPicker = (x, y, z) -> oceanFluidStatus;
		
		this.surfaceSystem = new SurfaceSystem(noises, this.defaultBlock, settings.seaLevel(), seed, settings.getRandomSource());
	}
	
	@Override
	public void buildSurface(WorldGenRegion level, StructureFeatureManager structureManager, ChunkAccess chunk)
	{
		NoiseGeneratorSettings settings = this.settings.value();
		NoiseChunk noiseChunk = chunk.getOrCreateNoiseChunk(this.router, () -> new Beardifier(structureManager, chunk){}, settings, this.globalFluidPicker, Blender.of(level));
		this.surfaceSystem.buildSurface(level.getBiomeManager(), level.registryAccess().registryOrThrow(Registry.BIOME_REGISTRY),
				settings.useLegacyRandomSource(), new WorldGenerationContext(this, level), chunk, noiseChunk, settings.surfaceRule());
	}
	/*TODO now a surface rule (see SurfaceRuleData)
	@Override
	public void buildSurfaceAndBedrock(WorldGenRegion region, ChunkAccess chunk)
	{
		ChunkPos chunkPos = chunk.getPos();
		SharedSeedRandom rand = new SharedSeedRandom();
		rand.setBaseChunkSeed(chunkPos.x, chunkPos.z);
		
		int startX = chunkPos.getMinBlockX();
		int startZ = chunkPos.getMinBlockZ();
		double noiseScale = 0.0625D;
		
		BlockPos.MutableBlockPos pos = new BlockPos.MutableBlockPos();
		for(int relX = 0; relX < 16; relX++)
		{
			for(int relZ = 0; relZ < 16; relZ++)
			{
				int x = startX + relX;
				int z = startZ + relZ;
				int surfaceY = chunk.getHeight(Heightmap.Types.WORLD_SURFACE_WG, relX, relZ) + 1;
				double noise = surfaceNoise.getSurfaceNoiseValue(x * noiseScale, z * noiseScale, noiseScale, relX * noiseScale) * 15.0D;
				
				Biome biome = getBiome(region, pos.set(startX + relX, surfaceY, startZ + relZ));
				biome.buildSurfaceAt(rand, chunk, x, z, surfaceY, noise, defaultBlock, defaultFluid, getSeaLevel(), region.getSeed());
			}
		}
		
		placeBedrock(chunk, rand);
	}
	
	private void placeBedrock(ChunkAccess chunk, Random rand)
	{
		int floor = settings.value().getBedrockFloorPosition();
		int roof = height - 1 - settings.value().getBedrockRoofPosition();
		int bedrockDepth = 5;
		
		boolean hasRoof = roof + bedrockDepth - 1 >= 0 && roof < height;
		boolean hasFloor = floor + bedrockDepth - 1 >= 0 && floor < height;
		
		if(hasRoof || hasFloor)
		{
			int startX = chunk.getPos().getMinBlockX();
			int startZ = chunk.getPos().getMinBlockZ();
			BlockPos.MutableBlockPos pos = new BlockPos.MutableBlockPos();
			
			for(BlockPos xzPos : BlockPos.betweenClosed(startX, 0, startZ, startX + 15, 0, startZ + 15))
			{
				for(int yOffset = 0; yOffset < bedrockDepth; yOffset++)
				{
					if(hasRoof && yOffset <= rand.nextInt(bedrockDepth))
						chunk.setBlockState(pos.set(xzPos.getX(), roof - yOffset, xzPos.getZ()), Blocks.BEDROCK.defaultBlockState(), false);
					
					if(hasFloor && yOffset <= rand.nextInt(bedrockDepth))
						chunk.setBlockState(pos.set(xzPos.getX(), floor + yOffset, xzPos.getZ()), Blocks.BEDROCK.defaultBlockState(), false);
				}
			}
		}
	}
	*/
	
	@Override
	public CompletableFuture<ChunkAccess> fillFromNoise(Executor executor, Blender blender, StructureFeatureManager structures, ChunkAccess chunk)
	{
		ChunkPos chunkPos = chunk.getPos();
		
		int chunkX = chunkPos.x;
		int chunkZ = chunkPos.z;
		int minX = chunkPos.getMinBlockX();
		int minZ = chunkPos.getMinBlockZ();
		
		double[][][] noiseColumns = new double[2][sectionCountXZ + 1][sectionCountY + 1];
		
		for(int sectZ = 0; sectZ < sectionCountXZ; sectZ++)
		{
			noiseColumns[0][sectZ] = new double[sectionCountY + 1];
			fillNoiseColumn(noiseColumns[0][sectZ], chunkX * sectionCountXZ, chunkZ * sectionCountXZ + sectZ);
			noiseColumns[1][sectZ] = new double[sectionCountY + 1];
		}
		
		Heightmap oceanHeight = chunk.getOrCreateHeightmapUnprimed(Heightmap.Types.OCEAN_FLOOR_WG);
		Heightmap surfaceHeight = chunk.getOrCreateHeightmapUnprimed(Heightmap.Types.WORLD_SURFACE_WG);
		BlockPos.MutableBlockPos pos = new BlockPos.MutableBlockPos();
		
		for(int sectX = 0; sectX < sectionCountXZ; sectX++)
		{
			for(int sectZ = 0; sectZ < sectionCountXZ + 1; sectZ++)
				fillNoiseColumn(noiseColumns[1][sectZ], chunkX * sectionCountXZ + sectX + 1, chunkZ * sectionCountXZ + sectZ);
			
			for(int sectZ = 0; sectZ < sectionCountXZ; ++sectZ)
			{
				LevelChunkSection section = chunk.getSection(15);
				section.acquire();
				
				for(int sectY = sectionCountY - 1; sectY >= 0; sectY--)
				{
					double noise = noiseColumns[0][sectZ][sectY];
					double noiseZ = noiseColumns[0][sectZ + 1][sectY];
					double noiseX = noiseColumns[1][sectZ][sectY];
					double noiseXZ = noiseColumns[1][sectZ + 1][sectY];
					double noiseY = noiseColumns[0][sectZ][sectY + 1];
					double noiseYZ = noiseColumns[0][sectZ + 1][sectY + 1];
					double noiseXY = noiseColumns[1][sectZ][sectY + 1];
					double noiseXYZ = noiseColumns[1][sectZ + 1][sectY + 1];
					
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
						double noiseForY = Mth.lerp(fracRelY, noise, noiseY);
						double noiseXForY = Mth.lerp(fracRelY, noiseX, noiseXY);
						double noiseZForY = Mth.lerp(fracRelY, noiseZ, noiseYZ);
						double noiseXZForY = Mth.lerp(fracRelY, noiseXZ, noiseXYZ);
						
						for(int relX = 0; relX < sectionWidth; relX++)
						{
							int x = minX + sectX * sectionWidth + relX;
							int csRelX = x & 15;
							double fracRelX = (double)relX / (double)sectionWidth;
							double noiseForXY = Mth.lerp(fracRelX, noiseForY, noiseXForY);
							double noiseZForXY = Mth.lerp(fracRelX, noiseZForY, noiseXZForY);
							
							for(int relZ = 0; relZ < sectionWidth; relZ++)
							{
								int z = minZ + sectZ * sectionWidth + relZ;
								int csRelZ = z & 15;
								double fracRelZ = (double)relZ / (double)sectionWidth;
								double noiseForXYZ = Mth.lerp(fracRelZ, noiseForXY, noiseZForXY);
								double clampedNoise = Mth.clamp(noiseForXYZ / 200.0D, -1.0D, 1.0D);
								
								BlockState state = baseStateForNoise(clampedNoise, y);
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
			
			double[][] oldColumns = noiseColumns[0];
			noiseColumns[0] = noiseColumns[1];
			noiseColumns[1] = oldColumns;
		}
		
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
		double[][] sectNoiseColumns = new double[][]{
				makeAndFillNoiseColumn(sectX, sectZ), makeAndFillNoiseColumn(sectX, sectZ + 1),
				makeAndFillNoiseColumn(sectX + 1, sectZ), makeAndFillNoiseColumn(sectX + 1, sectZ + 1)};
		
		for(int sectY = sectionCountY - 1; sectY >= 0; sectY--)
		{
			double noise = sectNoiseColumns[0][sectY];
			double noiseZ = sectNoiseColumns[1][sectY];
			double noiseX = sectNoiseColumns[2][sectY];
			double noiseXZ = sectNoiseColumns[3][sectY];
			double noiseY = sectNoiseColumns[0][sectY + 1];
			double noiseYZ = sectNoiseColumns[1][sectY + 1];
			double noiseXY = sectNoiseColumns[2][sectY + 1];
			double noiseXYZ = sectNoiseColumns[3][sectY + 1];
			
			for(int modY = sectionHeight - 1; modY >= 0; modY--)
			{
				double modFracY = (double) modY / (double) sectionHeight;
				// interpolated noise for this block
				double interNoise = Mth.lerp3(modFracY, modFracX, modFracZ, noise, noiseY, noiseX, noiseXY, noiseZ, noiseYZ, noiseXZ, noiseXYZ);
				int y = sectY * this.sectionHeight + modY;
				BlockState state = baseStateForNoise(interNoise, y);
				if(stateCollector != null)
					stateCollector[y] = state;
				
				if(condition != null && condition.test(state))
					return y + 1;
			}
		}
		
		return 0;
	}
	
	private BlockState baseStateForNoise(double noise, int y)
	{
		if(noise > 0)
			return defaultBlock;
		else if(y < getSeaLevel())
			return defaultFluid;
		else
			return Blocks.AIR.defaultBlockState();
	}
	
	
	private double[] makeAndFillNoiseColumn(int sectionX, int sectionZ)
	{
		double[] column = new double[sectionCountY + 1];
		fillNoiseColumn(column, sectionX, sectionZ);
		return column;
	}
	
	protected void fillNoiseColumn(double[] column, int sectX, int sectZ)
	{
		fillNoiseColumn(column, sectX, sectZ, -1 / 64D, 96 / 0.145D);
	}
	
	protected void fillNoiseColumn(double[] column, int sectX, int sectZ, double scaledHeight, double scaleMod)
	{
		NoiseSettings settings = this.settings.value().noiseSettings();
		
		for(int sectY = 0; sectY <= sectionCountY; sectY++)
		{
			DensityFunction.FunctionContext context = new DensityFunction.SinglePointContext(sectX * sectionWidth, sectY * sectionHeight, sectZ * sectionWidth);
			
			double noise = densityFunction.compute(context) * 128.0D;
			
			// modify noise value based on height and biome values
			double heightModifiedDensity = scaledDepthFunction.compute(context);
			double noiseMod = (heightModifiedDensity + scaledHeight) * scaleMod;
			if(noiseMod > 0)
				noise += noiseMod * 4;
			else
				noise += noiseMod;
			
			// smooths the noise value towards a certain value near the upper height limit
			noise = settings.topSlideSettings().applySlide(noise, settings.getCellCountY() - sectY);
			
			// smooths the noise value towards a certain value near the lower height limit
			noise = settings.bottomSlideSettings().applySlide(noise, sectY);
			
			column[sectY] = noise;
		}
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
	
	protected Holder<Biome> getBiome(WorldGenRegion region, BlockPos pos)
	{
		return region.getBiome(pos);
	}
	
	@Override
	public int getMinY()
	{
		return this.settings.value().noiseSettings().minY();
	}
	
	@Override
	public void addDebugScreenInfo(List<String> infoList, BlockPos pos)
	{}
	
	@Override
	public void applyCarvers(WorldGenRegion pLevel, long pSeed, BiomeManager pBiomeManager, StructureFeatureManager pStructureFeatureManager, ChunkAccess pChunk, GenerationStep.Carving pStep)
	{
	
	}
}