package com.mraof.minestuck.world.gen;

import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.util.SharedSeedRandom;
import net.minecraft.util.Util;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.Blockreader;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorld;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.provider.BiomeProvider;
import net.minecraft.world.chunk.ChunkPrimer;
import net.minecraft.world.chunk.ChunkSection;
import net.minecraft.world.chunk.IChunk;
import net.minecraft.world.gen.*;
import net.minecraft.world.gen.feature.structure.StructureManager;
import net.minecraft.world.gen.settings.NoiseSettings;
import net.minecraft.world.spawner.WorldEntitySpawner;

import javax.annotation.Nullable;
import java.util.Random;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.IntStream;

/**
 * Helper class that behaves similarly to {@link NoiseChunkGenerator}, but extendable with additional features.
 */
public abstract class AbstractChunkGenerator extends ChunkGenerator
{
	
	private static final float[] BIOME_WEIGHTS = Util.make(new float[25], list -> {
		for(int dx = -2; dx <= 2; ++dx)
		{
			for(int dz = -2; dz <= 2; ++dz)
			{
				float weight = 10F / MathHelper.sqrt((float) (dx * dx + dz * dz) + 0.2F);
				list[(dx + 2) + 5 * (dz + 2)] = weight;
			}
		}
		
	});
	
	// Sections are portions of a chunk, which may contain multiple block
	// Noise values are generated for sections,
	// and actual blocks are interpolated between the four neighboring sections
	// Sections map 1:1 to the noise biome layer
	private final int sectionHeight;
	private final int sectionWidth;
	private final int sectionCountXZ;
	private final int sectionCountY;
	
	private final OctavesNoiseGenerator minLimitPerlinNoise;
	private final OctavesNoiseGenerator maxLimitPerlinNoise;
	private final OctavesNoiseGenerator mainPerlinNoise;
	private final INoiseGenerator surfaceNoise;
	private final OctavesNoiseGenerator depthNoise;
	
	protected final long seed;
	protected final SharedSeedRandom random;
	protected final BlockState defaultBlock;
	protected final BlockState defaultFluid;
	protected final Supplier<DimensionSettings> settings;
	private final int height;
	
	public AbstractChunkGenerator(BiomeProvider provider, BiomeProvider runtimeProvider, long seed, Supplier<DimensionSettings> settings)
	{
		super(provider, runtimeProvider, settings.get().structureSettings(), seed);
		
		this.seed = seed;
		random = new SharedSeedRandom(seed);
		this.settings = settings;
		defaultBlock = settings.get().getDefaultBlock();
		defaultFluid = settings.get().getDefaultFluid();
		
		NoiseSettings noiseSettings = settings.get().noiseSettings();
		height = noiseSettings.height();
		sectionHeight = 4 * noiseSettings.noiseSizeVertical();
		sectionWidth = 4 * noiseSettings.noiseSizeHorizontal();
		sectionCountXZ = 16 / sectionWidth;
		sectionCountY = height / sectionHeight;
		
		minLimitPerlinNoise = new OctavesNoiseGenerator(random, IntStream.rangeClosed(-15, 0));
		maxLimitPerlinNoise = new OctavesNoiseGenerator(random, IntStream.rangeClosed(-15, 0));
		mainPerlinNoise = new OctavesNoiseGenerator(random, IntStream.rangeClosed(-7, 0));
		surfaceNoise = new PerlinNoiseGenerator(random, IntStream.rangeClosed(-3, 0));
		random.consumeCount(2620);
		depthNoise = new OctavesNoiseGenerator(random, IntStream.rangeClosed(-15, 0));
		
	}
	
	@Override
	public void buildSurfaceAndBedrock(WorldGenRegion region, IChunk chunk)
	{
		ChunkPos chunkPos = chunk.getPos();
		SharedSeedRandom rand = new SharedSeedRandom();
		rand.setBaseChunkSeed(chunkPos.x, chunkPos.z);
		
		int startX = chunkPos.getMinBlockX();
		int startZ = chunkPos.getMinBlockZ();
		double noiseScale = 0.0625D;
		
		BlockPos.Mutable pos = new BlockPos.Mutable();
		for(int relX = 0; relX < 16; relX++)
		{
			for(int relZ = 0; relZ < 16; relZ++)
			{
				int x = startX + relX;
				int z = startZ + relZ;
				int surfaceY = chunk.getHeight(Heightmap.Type.WORLD_SURFACE_WG, relX, relZ) + 1;
				double noise = surfaceNoise.getSurfaceNoiseValue(x * noiseScale, z * noiseScale, noiseScale, relX * noiseScale) * 15.0D;
				
				Biome biome = getBiome(region, pos.set(startX + relX, surfaceY, startZ + relZ));
				biome.buildSurfaceAt(rand, chunk, x, z, surfaceY, noise, defaultBlock, defaultFluid, getSeaLevel(), region.getSeed());
			}
		}
		
		placeBedrock(chunk, rand);
	}
	
	private void placeBedrock(IChunk chunk, Random rand)
	{
		int floor = settings.get().getBedrockFloorPosition();
		int roof = height - 1 - settings.get().getBedrockRoofPosition();
		int bedrockDepth = 5;
		
		boolean hasRoof = roof + bedrockDepth - 1 >= 0 && roof < height;
		boolean hasFloor = floor + bedrockDepth - 1 >= 0 && floor < height;
		
		if(hasRoof || hasFloor)
		{
			int startX = chunk.getPos().getMinBlockX();
			int startZ = chunk.getPos().getMinBlockZ();
			BlockPos.Mutable pos = new BlockPos.Mutable();
			
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
	
	@Override
	public void fillFromNoise(IWorld world, StructureManager structures, IChunk chunkIn)
	{
		ChunkPrimer chunk = (ChunkPrimer) chunkIn;
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
		
		Heightmap oceanHeight = chunk.getOrCreateHeightmapUnprimed(Heightmap.Type.OCEAN_FLOOR_WG);
		Heightmap surfaceHeight = chunk.getOrCreateHeightmapUnprimed(Heightmap.Type.WORLD_SURFACE_WG);
		BlockPos.Mutable pos = new BlockPos.Mutable();
		
		for(int sectX = 0; sectX < sectionCountXZ; sectX++)
		{
			for(int sectZ = 0; sectZ < sectionCountXZ + 1; sectZ++)
				fillNoiseColumn(noiseColumns[1][sectZ], chunkX * sectionCountXZ + sectX + 1, chunkZ * sectionCountXZ + sectZ);
			
			for(int sectZ = 0; sectZ < sectionCountXZ; ++sectZ)
			{
				ChunkSection section = chunk.getOrCreateSection(15);
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
							section = chunk.getOrCreateSection(csY);
							section.acquire();
						}
						
						double fracRelY = (double)relY / (double)sectionHeight;
						double noiseForY = MathHelper.lerp(fracRelY, noise, noiseY);
						double noiseXForY = MathHelper.lerp(fracRelY, noiseX, noiseXY);
						double noiseZForY = MathHelper.lerp(fracRelY, noiseZ, noiseYZ);
						double noiseXZForY = MathHelper.lerp(fracRelY, noiseXZ, noiseXYZ);
						
						for(int relX = 0; relX < sectionWidth; relX++)
						{
							int x = minX + sectX * sectionWidth + relX;
							int csRelX = x & 15;
							double fracRelX = (double)relX / (double)sectionWidth;
							double noiseForXY = MathHelper.lerp(fracRelX, noiseForY, noiseXForY);
							double noiseZForXY = MathHelper.lerp(fracRelX, noiseZForY, noiseXZForY);
							
							for(int relZ = 0; relZ < sectionWidth; relZ++)
							{
								int z = minZ + sectZ * sectionWidth + relZ;
								int csRelZ = z & 15;
								double fracRelZ = (double)relZ / (double)sectionWidth;
								double noiseForXYZ = MathHelper.lerp(fracRelZ, noiseForXY, noiseZForXY);
								double clampedNoise = MathHelper.clamp(noiseForXYZ / 200.0D, -1.0D, 1.0D);
								
								BlockState state = baseStateForNoise(clampedNoise, y);
								if (state != Blocks.AIR.defaultBlockState()) {
									pos.set(x, y, z);
									if (state.getLightValue(chunk, pos) != 0) {
										chunk.addLight(pos);
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
	}
	
	@Override
	public int getBaseHeight(int x, int z, Heightmap.Type type)
	{
		return iterateEarlyBlocks(x, z, null, type.isOpaque());
	}
	
	@Override
	public IBlockReader getBaseColumn(int x, int z)
	{
		BlockState[] states = new BlockState[height];
		iterateEarlyBlocks(x, z, states, null);
		return new Blockreader(states);
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
				double interNoise = MathHelper.lerp3(modFracY, modFracX, modFracZ, noise, noiseY, noiseX, noiseXY, noiseZ, noiseYZ, noiseXZ, noiseXYZ);
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
		float scaleSum = 0;
		float depthSum = 0;
		float weightSum = 0;
		int radius = 2;
		int seaHeight = getSeaLevel();
		float biomeDepth = biomeSource.getNoiseBiome(sectX, seaHeight, sectZ).getDepth();
		
		for(int dx = -radius; dx <= radius; dx++)
		{
			for(int dz = -radius; dz <= radius; dz++)
			{
				Biome neighborBiome = biomeSource.getNoiseBiome(sectX + dx, seaHeight, sectZ + dz);
				float depth = neighborBiome.getDepth();
				float scale = neighborBiome.getScale();
				float weightFactor = depth > biomeDepth ? 0.5F : 1;
				
				float neighborWeight = weightFactor * BIOME_WEIGHTS[(dx + 2) + 5 * (dz + 2)] / (depth + 2.0F);
				scaleSum += neighborWeight * scale;
				depthSum += neighborWeight * depth;
				weightSum += neighborWeight;
			}
		}
		
		float averageDepth = depthSum / weightSum;
		float averageScale = scaleSum / weightSum;
		double scaledHeight = ((averageDepth * 4 - 1) / 8D) * (17 / 64D);
		double scaleMod = 96D / (averageScale * 0.9 + 0.1);
		
		fillNoiseColumn(column, sectX, sectZ, scaledHeight, scaleMod);
	}
	
	protected void fillNoiseColumn(double[] column, int sectX, int sectZ, double scaledHeight, double scaleMod)
	{
		NoiseSettings settings = this.settings.get().noiseSettings();
		
		double xzScale = 684.412D * settings.noiseSamplingSettings().xzScale();
		double yScale = 684.412D * settings.noiseSamplingSettings().yScale();
		double xzFactor = xzScale / settings.noiseSamplingSettings().xzFactor();
		double yFactor = yScale / settings.noiseSamplingSettings().yFactor();
		
		double topTarget = settings.topSlideSettings().target();
		double topSize = settings.topSlideSettings().size();
		double topOffset = settings.topSlideSettings().offset();
		double bottomTarget = settings.bottomSlideSettings().target();
		double bottomSize = settings.bottomSlideSettings().size();
		double bottomOffset = settings.bottomSlideSettings().offset();
		
		double randomDensity = settings.randomDensityOffset() ? getRandomDensity(sectX, sectZ) : 0;
		double densityFactor = settings.densityFactor();
		double densityOffset = settings.densityOffset();
		
		for(int sectY = 0; sectY <= sectionCountY; sectY++)
		{
			double noise = sampleAndClampNoise(sectX, sectY, sectZ, xzScale, yScale, xzFactor, yFactor);
			
			// modify noise value based on height and biome values
			double heightDensity = 1 - 2 * (sectY / (double) sectionCountY) + randomDensity;
			double heightModifiedDensity = heightDensity * densityFactor + densityOffset;
			double noiseMod = (heightModifiedDensity + scaledHeight) * scaleMod;
			if(noiseMod > 0)
				noise += noiseMod * 4;
			else
				noise += noiseMod;
			
			// smooths the noise value towards a certain value near the upper height limit
			if(topSize > 0)
			{
				double topDistance = ((sectionCountY - sectY) - topOffset) / topSize;
				noise = MathHelper.clampedLerp(topTarget, noise, topDistance);
			}
			
			// smooths the noise value towards a certain value near the lower height limit
			if(bottomSize > 0)
			{
				double bottomDistance = (sectY - bottomOffset) / bottomSize;
				noise = MathHelper.clampedLerp(bottomTarget, noise, bottomDistance);
			}
			
			column[sectY] = noise;
		}
	}
	
	private double getRandomDensity(int x, int z)
	{
		double noise = this.depthNoise.getValue(x * 200, 10, z * 200, 1, 0, true);
		if(noise < 0)
			noise = -noise * 0.3;
		double scaledNoise = noise * (65535 / 8000.0D) * 3 - 2;
		
		return scaledNoise < 0.0D ? scaledNoise * 17 / 256D / 7D : Math.min(scaledNoise, 1) * 17 / 2560D;
	}
	
	private double sampleAndClampNoise(int sectX, int sectY, int sectZ, double scaleXZ, double scaleY, double factorXZ, double factorY)
	{
		double minNoise = 0;
		double maxNoise = 0;
		double mainNoise = 0;
		double layerScale = 1;
		
		for(int layer = 0; layer < 16; layer++)
		{
			double scaledX = OctavesNoiseGenerator.wrap(sectX * scaleXZ * layerScale);
			double scaledY = OctavesNoiseGenerator.wrap(sectY * scaleY * layerScale);
			double scaledZ = OctavesNoiseGenerator.wrap(sectZ * scaleXZ * layerScale);
			double lScaleY = scaleY * layerScale;
			ImprovedNoiseGenerator noiseGen = this.minLimitPerlinNoise.getOctaveNoise(layer);
			if(noiseGen != null)
				minNoise += noiseGen.noise(scaledX, scaledY, scaledZ, lScaleY, sectY * lScaleY) / layerScale;
			
			noiseGen = this.maxLimitPerlinNoise.getOctaveNoise(layer);
			if(noiseGen != null)
				maxNoise += noiseGen.noise(scaledX, scaledY, scaledZ, lScaleY, sectY * lScaleY) / layerScale;
			
			if(layer < 8)
			{
				noiseGen = this.mainPerlinNoise.getOctaveNoise(layer);
				if(noiseGen != null)
				{
					double fScaledX = OctavesNoiseGenerator.wrap(sectX * factorXZ * layerScale);
					double fScaledY = OctavesNoiseGenerator.wrap(sectY * factorY * layerScale);
					double fScaledZ = OctavesNoiseGenerator.wrap(sectZ * factorXZ * layerScale);
					mainNoise += noiseGen.noise(fScaledX, fScaledY, fScaledZ, factorY * layerScale, sectY * factorY * layerScale) / layerScale;
				}
			}
			
			layerScale /= 2;
		}
		
		return MathHelper.clampedLerp(minNoise / 512.0D, maxNoise / 512.0D, (mainNoise / 10.0D + 1.0D) / 2.0D);
	}
	
	@Override
	public int getGenDepth()
	{
		return height;
	}
	
	@Override
	public int getSeaLevel()
	{
		return settings.get().seaLevel();
	}
	
	@Override
	public void spawnOriginalMobs(WorldGenRegion region)
	{
		int x = region.getCenterX();
		int z = region.getCenterZ();
		Biome biome = getBiome(region, new ChunkPos(x, z).getWorldPosition());
		SharedSeedRandom sharedseedrandom = new SharedSeedRandom();
		sharedseedrandom.setDecorationSeed(region.getSeed(), x << 4, z << 4);
		WorldEntitySpawner.spawnMobsForChunkGeneration(region, biome, x, z, sharedseedrandom);
	}
	
	protected Biome getBiome(WorldGenRegion region, BlockPos pos)
	{
		return region.getBiome(pos);
	}
}