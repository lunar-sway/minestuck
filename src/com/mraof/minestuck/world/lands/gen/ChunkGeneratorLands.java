package com.mraof.minestuck.world.lands.gen;

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EnumCreatureType;
import net.minecraft.util.SharedSeedRandom;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.provider.BiomeProvider;
import net.minecraft.world.chunk.ChunkStatus;
import net.minecraft.world.chunk.IChunk;
import net.minecraft.world.gen.AbstractChunkGenerator;
import net.minecraft.world.gen.Heightmap;
import net.minecraft.world.gen.NoiseGeneratorOctaves;
import net.minecraft.world.gen.WorldGenRegion;

import java.util.List;

public class ChunkGeneratorLands extends AbstractChunkGenerator<LandGenSettings>
{
	private final NoiseGeneratorOctaves noiseGen;
	private final IBlockState defaultBlock;
	private final IBlockState defaultFluid;
	
	private final LandGenSettings settings;
	
	public static float[] biomeWeight;
	
	static {
		biomeWeight = new float[25];
		for(int x = -2; x <= 2; x++)
			for(int z = -2; z <= 2; z++)
				biomeWeight[(x + 2)*5 + z + 2] = 10.0F / MathHelper.sqrt((float)(x * x + z * z) + 0.2F);
	}
	
	public ChunkGeneratorLands(World worldIn, BiomeProvider biomeProviderIn, LandGenSettings settings)
	{
		super(worldIn, biomeProviderIn);
		this.settings = settings;
		SharedSeedRandom rand = new SharedSeedRandom(this.seed);
		this.noiseGen = new NoiseGeneratorOctaves(rand, 10);
		this.defaultBlock = this.settings.getDefaultBlock();
		this.defaultFluid = this.settings.getDefaultFluid();
		
	}
	
	@Override
	public LandGenSettings getSettings()
	{
		return settings;
	}
	
	@Override
	public double[] generateNoiseRegion(int x, int z)
	{
		return new double[256];
	}
	
	@Override
	public void makeBase(IChunk chunkIn)
	{
		ChunkPos chunkpos = chunkIn.getPos();
		int x = chunkpos.x;
		int z = chunkpos.z;
		SharedSeedRandom random = new SharedSeedRandom();
		random.setBaseChunkSeed(x, z);
		Biome[] abiome = this.biomeProvider.getBiomeBlock(x * 16, z * 16, 16, 16);
		chunkIn.setBiomes(abiome);
		this.genBase(x, z, chunkIn);
		chunkIn.createHeightMap(Heightmap.Type.WORLD_SURFACE_WG, Heightmap.Type.OCEAN_FLOOR_WG);
		this.buildSurface(chunkIn, abiome, random, this.world.getSeaLevel());
		this.makeBedrock(chunkIn, random);
		chunkIn.createHeightMap(Heightmap.Type.WORLD_SURFACE_WG, Heightmap.Type.OCEAN_FLOOR_WG);
		chunkIn.setStatus(ChunkStatus.BASE);
	}
	
	private void genBase(int x, int z, IChunk primer) {
		
		int[] topBlock = new int[256];
		
		double[] heightMap = this.noiseGen.func_202647_a(x * 16, 10, z * 16, 16, 1, 16, 10, 1, 10);
		Biome[] biomes = this.biomeProvider.getBiomes(x*4 - 2, z*4 - 2, 9, 9);
		double[] biomeHeightMap = new double[25];
		double[] biomeVariationMap = new double[25];
		
		for(int x0 = 0; x0 < 5; x0++)
			for(int z0 = 0; z0 < 5; z0++)
			{
				float biomeHeight = getBiomeHeight(biomes[(z0 + 2)*9 + x0 + 2]);
				double totalBiomeHeight = 0;
				double totalBiomeVariation = 0;
				float divisor = 0;
				for(int x1 = 0; x1 < 5; x1++)
					for(int z1 = 0; z1 < 5; z1++)
					{
						Biome biome = biomes[(z0 + z1)*9 + x0 + x1];
						float multiplier = biomeWeight[x1*5 + z1];
						float biomeHeight1 = getBiomeHeight(biome);
						if(biomeHeight1 > biomeHeight)
							multiplier /= 2F;
						
						totalBiomeHeight += biomeHeight1*multiplier;
						totalBiomeVariation += getBiomeVariation(biome)*multiplier;
						divisor += multiplier;
					}
				biomeHeightMap[x0*5 + z0] = totalBiomeHeight / divisor;
				biomeVariationMap[x0*5 + z0] = totalBiomeVariation / divisor;
			}
		
		for(int x0 = 0; x0 < 4; x0++)
			for(int z0 = 0; z0 < 4; z0++)
			{
				float f1 = 0.25F;
				double height0 = biomeHeightMap[x0*5 + z0];
				double height1 = biomeHeightMap[x0*5 + z0 + 1];
				double height0Diff = (biomeHeightMap[(x0 + 1)*5 + z0] - height0)*f1;
				double height1Diff = (biomeHeightMap[(x0 + 1)*5 + z0 + 1] - height1)*f1;
				double variation0 = biomeVariationMap[x0*5 + z0];
				double variation1 = biomeVariationMap[x0*5 + z0 + 1];
				double variation0Diff = (biomeVariationMap[(x0 + 1)*5 + z0] - variation0)*f1;
				double variation1Diff = (biomeVariationMap[(x0 + 1)*5 + z0 + 1] - variation1)*f1;
				for(int x1 = 0; x1 < 4; x1++)
				{
					double biomeHeight = height0;
					double biomeHeightDiff = (height1 - height0)*f1;
					double biomeVariation = variation0;
					double biomeVariationDiff = (variation1 - variation0)*f1;
					for(int z1 = 0; z1 < 4; z1++)
					{
						int index = (x0*4 + x1)*16 + z0*4 + z1;
						double height = heightMap[index]/40D;
						
						topBlock[index] = (int) (62 + 32*biomeHeight + height*biomeVariation);
						
						biomeHeight += biomeHeightDiff;
						biomeVariation += biomeVariationDiff;
					}
					height0 += height0Diff;
					height1 += height1Diff;
					variation0 += variation0Diff;
					variation1 += variation1Diff;
				}
			}
		
		BlockPos.MutableBlockPos pos = new BlockPos.MutableBlockPos();
		for(int posX = 0; posX < 16; posX++)
			for(int posZ = 0; posZ < 16; posZ++)
				for(int posY = 0; posY <= Math.max(topBlock[posX * 16 + posZ], getGroundHeight() - 1); posY++)
				{
					pos.setPos(posX, posY, posZ);
					if(posY <= topBlock[posX * 16 + posZ])
						primer.setBlockState(pos, this.defaultBlock, false);
					else primer.setBlockState(pos, this.defaultFluid, false);
				}
	}
	
	protected float getBiomeHeight(Biome biome)
	{
		return 0.3F;
	}
	
	protected float getBiomeVariation(Biome biome)
	{
		return 0.5F;
	}
	
	@Override
	public void spawnMobs(WorldGenRegion worldGenRegion)
	{
	
	}
	
	@Override
	public List<Biome.SpawnListEntry> getPossibleCreatures(EnumCreatureType creatureType, BlockPos pos)
	{
		return this.world.getBiome(pos).getSpawns(creatureType);
	}
	
	@Override
	public int spawnMobs(World world, boolean spawnHostileMobs, boolean spawnPeacefulMobs)
	{
		return 0;
	}
	
	@Override
	public int getGroundHeight()
	{
		return 63;
	}
}
