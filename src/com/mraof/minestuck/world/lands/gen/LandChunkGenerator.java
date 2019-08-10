package com.mraof.minestuck.world.lands.gen;

import net.minecraft.util.SharedSeedRandom;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.provider.BiomeProvider;
import net.minecraft.world.chunk.ChunkStatus;
import net.minecraft.world.chunk.IChunk;
import net.minecraft.world.gen.*;

import java.util.List;

public class LandChunkGenerator extends NoiseChunkGenerator<LandGenSettings>
{
	public LandChunkGenerator(IWorld worldIn, BiomeProvider biomeProviderIn, LandGenSettings settings)
	{
		super(worldIn, biomeProviderIn, 4, 8, 256, settings, false);
	}
	
	@Override
	protected double[] func_222549_a(int columnX, int columnZ)
	{
		return new double[]{0, 0.1};
	}
	
	@Override
	protected double func_222545_a(double depth, double scale, int height)
	{
		double modifier = ((double)height - (8.5D + depth * 8.5D / 8.0D * 4.0D)) * 12.0D * 128.0D / 256.0D / scale;
		if (modifier < 0.0D)
			modifier *= 4.0D;
		
		return modifier;
	}
	
	@Override
	protected void func_222548_a(double[] noiseColumn, int columnX, int columnZ)
	{
		double horizontal = 684.412D;
		double vertical = 684.412D;
		double horizontal2 = 17.1103D;
		double vertical2 = 4.277575D;
		int lerpModifier = 3;
		int skyValueTarget = -10;
		this.func_222546_a(noiseColumn, columnX, columnZ, horizontal, vertical, horizontal2, vertical2, lerpModifier, skyValueTarget);
	}
	
	@Override
	public int getGroundHeight()
	{
		return 64;
	}
	
	/*
	private final OctavesNoiseGenerator noiseGen;
	
	public static float[] biomeWeight;
	
	static {
		biomeWeight = new float[25];
		for(int x = -2; x <= 2; x++)
			for(int z = -2; z <= 2; z++)
				biomeWeight[(x + 2)*5 + z + 2] = 10.0F / MathHelper.sqrt((float)(x * x + z * z) + 0.2F);
	}
	
	public ChunkGeneratorLands(World worldIn, BiomeProvider biomeProviderIn, LandGenSettings settings)
	{
		super(worldIn, biomeProviderIn, settings);
		SharedSeedRandom rand = new SharedSeedRandom(this.seed);
		this.noiseGen = new OctavesNoiseGenerator(rand, 10);
	}
	
	@Override
	public LandGenSettings getSettings()
	{
		return settings;
	}
	
	@Override
	public void makeBase(IWorld world, IChunk chunkIn)
	{
		ChunkPos chunkpos = chunkIn.getPos();
		int x = chunkpos.x;
		int z = chunkpos.z;
		SharedSeedRandom random = new SharedSeedRandom();
		random.setBaseChunkSeed(x, z);
		Biome[] abiome = this.biomeProvider.getBiomeBlock(x * 16, z * 16, 16, 16);
		chunkIn.setBiomes(abiome);
		this.genBase(x, z, chunkIn);
		/*chunkIn.createHeightMap(Heightmap.Type.WORLD_SURFACE_WG, Heightmap.Type.OCEAN_FLOOR_WG);
		this.buildSurface(chunkIn, abiome, random, this.world.getSeaLevel());
		this.makeBedrock(chunkIn, random);
		chunkIn.createHeightMap(Heightmap.Type.WORLD_SURFACE_WG, Heightmap.Type.OCEAN_FLOOR_WG);
		chunkIn.setStatus(ChunkStatus.BASE);*//*
	}
	
	@Override
	public void generateSurface(IChunk p_222535_1_)
	{
	
	}
	
	@Override
	public int func_222529_a(int p_222529_1_, int p_222529_2_, Heightmap.Type p_222529_3_)
	{
		return 0;
	}
	
	private void genBase(int x, int z, IChunk primer) {
		
		int[] topBlock = new int[256];
		
		double[] heightMap = null;//this.noiseGen.func_202647_a(x * 16, 10, z * 16, 16, 1, 16, 10, 1, 10);
		Biome[] biomes = this.biomeProvider.getBiomes(x*4 - 2, z*4 - 2, 9, 9, true);
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
						primer.setBlockState(pos, settings.getDefaultBlock(), false);
					else primer.setBlockState(pos, settings.getDefaultFluid(), false);
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
	public int getGroundHeight()
	{
		return 63;
	}
	*/
}