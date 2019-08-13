package com.mraof.minestuck.world.lands.gen;

import com.mraof.minestuck.world.biome.LandBiomeHolder;
import com.mraof.minestuck.world.biome.LandWrapperBiome;
import com.mraof.minestuck.world.biome.ModBiomes;
import com.mraof.minestuck.world.lands.LandAspects;
import com.mraof.minestuck.world.lands.structure.blocks.StructureBlockRegistry;
import net.minecraft.entity.EntityClassification;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.IWorld;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.provider.BiomeProvider;
import net.minecraft.world.chunk.IChunk;
import net.minecraft.world.gen.*;
import net.minecraft.world.gen.feature.IFeatureConfig;
import net.minecraft.world.gen.feature.structure.Structure;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Objects;

public class LandChunkGenerator extends NoiseChunkGenerator<LandGenSettings>
{
	private static final float[] biomeWeight;
	
	static {
		biomeWeight = new float[25];
		for(int x = -2; x <= 2; x++)
			for(int z = -2; z <= 2; z++)
				biomeWeight[(x + 2)*5 + z + 2] = 10.0F / MathHelper.sqrt((float)(x * x + z * z) + 0.2F);
	}
	
	public final LandAspects landAspects;
	public final StructureBlockRegistry blockRegistry;
	public final LandBiomeHolder biomeHolder;
	
	public LandChunkGenerator(IWorld worldIn, BiomeProvider biomeProviderIn, LandGenSettings settings)
	{
		super(worldIn, biomeProviderIn, 4, 8, 256, settings, false);
		
		landAspects = Objects.requireNonNull(settings.getLandAspects());
		blockRegistry = Objects.requireNonNull(settings.getBlockRegistry());
		
		biomeHolder = Objects.requireNonNull(settings.getBiomeHolder());
		biomeHolder.initBiomesWith(settings);
	}
	
	
	@Override
	protected double[] func_222549_a(int columnX, int columnZ)
	{
		float baseDepth = biomeHolder.localBiomeFrom(biomeProvider.func_222366_b(columnX, columnZ)).getDepth();
		
		float depthSum = 0, scaleSum = 0, weightSum = 0;
		for(int x = -2; x <= 2; x++)
		{
			for(int z = -2; z <= 2; z++)
			{
				Biome biome = biomeHolder.localBiomeFrom(biomeProvider.func_222366_b(columnX + x, columnZ + z));
				float weight = biomeWeight[(x + 2)*5 + z + 2] / (biome.getDepth() + 2);
				if(biome.getDepth() > baseDepth)
					weight /= 2;
				
				depthSum += biome.getDepth() * weight;
				scaleSum += biome.getScale() * weight;
				weightSum += weight;
			}
		}
		
		float depth = (depthSum / weightSum) * 0.5F - 1/8F;
		float scale = (scaleSum / weightSum) * 0.9F + 0.1F;
		return new double[]{depth, scale};
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
	
	@Override
	protected Biome getBiome(IChunk chunkIn)
	{
		return biomeHolder.localBiomeFrom(super.getBiome(chunkIn));
	}
	
	@Override
	protected Biome getBiome(WorldGenRegion worldRegionIn, BlockPos pos)
	{
		return biomeHolder.localBiomeFrom(super.getBiome(worldRegionIn, pos));
	}
	
	@Override
	public boolean hasStructure(Biome biomeIn, Structure<? extends IFeatureConfig> structureIn)
	{
		return biomeHolder.localBiomeFrom(biomeIn).hasStructure(structureIn);
	}
	
	@Nullable
	@Override
	public <C extends IFeatureConfig> C getStructureConfig(Biome biomeIn, Structure<C> structureIn)
	{
		return biomeHolder.localBiomeFrom(biomeIn).getStructureConfig(structureIn);
	}
}