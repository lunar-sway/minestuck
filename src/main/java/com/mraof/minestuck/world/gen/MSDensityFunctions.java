package com.mraof.minestuck.world.gen;

import com.mraof.minestuck.Minestuck;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.levelgen.DensityFunction;
import net.minecraft.world.level.levelgen.DensityFunctions;
import net.minecraft.world.level.levelgen.synth.BlendedNoise;
import net.minecraft.world.level.levelgen.synth.NormalNoise;

public final class MSDensityFunctions
{
	// Our own set of shift functions imitating vanillas. By not using vanillas functions directly, we will not be affected by datapacks targeting the vanilla functions.
	public static final ResourceKey<DensityFunction> SHIFT_X = key("shift_x");
	public static final ResourceKey<DensityFunction> SHIFT_Z = key("shift_z");
	
	public static final ResourceKey<DensityFunction> SKAIA_RIDGES = key("skaia/ridges");
	
	public static final ResourceKey<DensityFunction> SKAIA_OFFSET = key("skaia/offset");
	public static final ResourceKey<DensityFunction> SKAIA_DEPTH = key("skaia/depth");
	public static final ResourceKey<DensityFunction> SKAIA_INITIAL_DENSITY = key("skaia/initial_density");
	public static final ResourceKey<DensityFunction> SKAIA_FINAL_DENSITY = key("skaia/final_density");
	
	public static final ResourceKey<DensityFunction> LAND_CONTINENTS = key("land/continents");
	public static final ResourceKey<DensityFunction> LAND_EROSION = key("land/erosion");
	
	private static ResourceKey<DensityFunction> key(String name)
	{
		return ResourceKey.create(Registries.DENSITY_FUNCTION, new ResourceLocation(Minestuck.MOD_ID, name));
	}
	
	public static DensityFunction depth(DensityFunction offset)
	{
		return DensityFunctions.add(DensityFunctions.yClampedGradient(-64, 320, 1.5, -1.5), offset);
	}
	
	public static DensityFunction initialDensity(DensityFunction depth, DensityFunction factor)
	{
		return DensityFunctions.mul(DensityFunctions.constant(4), DensityFunctions.mul(depth, factor).quarterNegative());
	}
	
	@SuppressWarnings("ConstantConditions")
	public static DensityFunction finalDensity(DensityFunction depth, DensityFunction factor, DensityFunction jaggedness, Holder<NormalNoise.NoiseParameters> jagged, int height)
	{
		DensityFunction noise = DensityFunctions.noise(jagged, 1500, 0);
		DensityFunction jaggednessFactor = DensityFunctions.mul(jaggedness, noise.halfNegative());
		
		DensityFunction modifiedDepth = DensityFunctions.add(depth, jaggednessFactor);
		DensityFunction baseDensity = initialDensity(modifiedDepth, factor);
		DensityFunction base3dNoise = BlendedNoise.createUnseeded(0.25, 0.125, 80, 160, 8);
		return DensityFunctions.mul(DensityFunctions.constant(0.64), DensityFunctions.interpolated(DensityFunctions.blendDensity(
				yLerpSlide(height, height - 16, -1, yLerpSlide(-64, -40, 1, DensityFunctions.add(base3dNoise, baseDensity)))))).squeeze();
	}
	
	private static DensityFunction yLerpSlide(int y1, int y2, double fixedValue, DensityFunction density)
	{
		DensityFunction lerpValue = DensityFunctions.yClampedGradient(y1, y2, 0, 1);
		return DensityFunctions.lerp(lerpValue, fixedValue, density);
	}
	
	public static DensityFunction get(HolderGetter<DensityFunction> registry, ResourceKey<DensityFunction> key)
	{
		return new DensityFunctions.HolderHolder(registry.getOrThrow(key));
	}
}
