package com.mraof.minestuck.data.worldgen;

import com.mraof.minestuck.world.gen.MSNoiseParameters;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.util.CubicSpline;
import net.minecraft.world.level.levelgen.DensityFunction;
import net.minecraft.world.level.levelgen.DensityFunctions;
import net.minecraft.world.level.levelgen.Noises;
import net.minecraft.world.level.levelgen.synth.NormalNoise;

import static com.mraof.minestuck.world.gen.MSDensityFunctions.*;

public final class MSDensityFunctionProvider
{
	public static void register(BootstrapContext<DensityFunction> context)
	{
		HolderGetter<NormalNoise.NoiseParameters> noise = context.lookup(Registries.NOISE);
		
		DensityFunction shiftX = registerAndGet(context, SHIFT_X, DensityFunctions.flatCache(DensityFunctions.cache2d(DensityFunctions.shiftA(noise.getOrThrow(Noises.SHIFT)))));
		DensityFunction shiftZ = registerAndGet(context, SHIFT_Z, DensityFunctions.flatCache(DensityFunctions.cache2d(DensityFunctions.shiftB(noise.getOrThrow(Noises.SHIFT)))));
		
		Holder<DensityFunction> skaiaRidges = context.register(SKAIA_RIDGES, base2dNoise(shiftX, shiftZ, noise.getOrThrow(MSNoiseParameters.SKAIA_RIDGES)));
		
		DensityFunction skaiaOffset = registerAndGet(context, SKAIA_OFFSET, skaiaOffset(skaiaRidges));
		DensityFunction skaiaDepth = registerAndGet(context, SKAIA_DEPTH, depth(skaiaOffset));
		DensityFunction skaiaFactor = DensityFunctions.constant(5);
		
		context.register(SKAIA_INITIAL_DENSITY, initialDensity(skaiaDepth, skaiaFactor));
		context.register(SKAIA_FINAL_DENSITY, finalDensity(skaiaDepth, skaiaFactor, DensityFunctions.zero(), noise.getOrThrow(Noises.JAGGED), 256, 0.25F));
		
		Holder<DensityFunction> veilRidges = context.register(VEIL_RIDGES, base2dNoise(shiftX, shiftZ, noise.getOrThrow(MSNoiseParameters.VEIL_RIDGES)));
		
		DensityFunction veilOffset = registerAndGet(context, VEIL_OFFSET, veilOffset(veilRidges));
		DensityFunction veilDepth = registerAndGet(context, VEIL_DEPTH, depth(veilOffset, 0, 208));
		DensityFunction veilFactor = DensityFunctions.constant(6);
		
		context.register(VEIL_INITIAL_DENSITY, initialDensity(veilDepth, veilFactor));
		context.register(VEIL_FINAL_DENSITY, finalDensity(veilDepth, veilFactor, DensityFunctions.zero(), noise.getOrThrow(Noises.JAGGED), 208, 0.23F));
		
		context.register(LAND_CONTINENTS, base2dNoise(shiftX, shiftZ, noise.getOrThrow(MSNoiseParameters.LAND_CONTINENTS)));
		context.register(LAND_EROSION, base2dNoise(shiftX, shiftZ, noise.getOrThrow(MSNoiseParameters.LAND_EROSION)));
	}
	
	private static DensityFunction registerAndGet(BootstrapContext<DensityFunction> context, ResourceKey<DensityFunction> key, DensityFunction function)
	{
		return new DensityFunctions.HolderHolder(context.register(key, function));
	}
	
	private static DensityFunction skaiaOffset(Holder<DensityFunction> ridges)
	{
		var builder = CubicSpline.builder(new DensityFunctions.Spline.Coordinate(ridges));
		builder.addPoint(-1.5F, -0.2F);
		builder.addPoint(-0.5F, 1F);
		builder.addPoint(0.5F, -0.2F);
		builder.addPoint(1.5F, 1F);
		return DensityFunctions.add(DensityFunctions.constant(-0.50375), DensityFunctions.spline(builder.build()));
	}
	
	private static DensityFunction veilOffset(Holder<DensityFunction> ridges)
	{
		var builder = CubicSpline.builder(new DensityFunctions.Spline.Coordinate(ridges));
		builder.addPoint(-1.5F, -0.2F);
		builder.addPoint(-0.5F, 1F);
		builder.addPoint(0.5F, -0.2F);
		builder.addPoint(1.5F, 1F);
		return DensityFunctions.add(DensityFunctions.constant(-0.50375), DensityFunctions.spline(builder.build()));
	}
	
	private static DensityFunction base2dNoise(DensityFunction shiftX, DensityFunction shiftZ, Holder<NormalNoise.NoiseParameters> noise)
	{
		return DensityFunctions.flatCache(DensityFunctions.shiftedNoise2d(shiftX, shiftZ, 0.25, noise));
	}
}
