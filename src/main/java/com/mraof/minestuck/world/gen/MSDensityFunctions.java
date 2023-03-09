package com.mraof.minestuck.world.gen;

import com.mraof.minestuck.Minestuck;
import net.minecraft.core.Registry;
import net.minecraft.data.BuiltinRegistries;
import net.minecraft.util.CubicSpline;
import net.minecraft.world.level.levelgen.DensityFunction;
import net.minecraft.world.level.levelgen.DensityFunctions;
import net.minecraft.world.level.levelgen.Noises;
import net.minecraft.world.level.levelgen.synth.BlendedNoise;
import net.minecraft.world.level.levelgen.synth.NormalNoise;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

import javax.annotation.Nonnull;
import java.util.Objects;
import java.util.function.Supplier;

public class MSDensityFunctions
{
	public static final DeferredRegister<DensityFunction> REGISTER = DeferredRegister.create(Registry.DENSITY_FUNCTION_REGISTRY, Minestuck.MOD_ID);
	
	// Our own set of shift functions imitating vanillas. By not using vanillas functions directly, we will not be affected by datapacks targeting the vanilla functions.
	public static final RegistryObject<DensityFunction> SHIFT_X = REGISTER.register("shift_x",
			() -> DensityFunctions.flatCache(DensityFunctions.cache2d(DensityFunctions.shiftA(BuiltinRegistries.NOISE.getHolderOrThrow(Noises.SHIFT)))));
	public static final RegistryObject<DensityFunction> SHIFT_Z = REGISTER.register("shift_z",
			() -> DensityFunctions.flatCache(DensityFunctions.cache2d(DensityFunctions.shiftB(BuiltinRegistries.NOISE.getHolderOrThrow(Noises.SHIFT)))));
	
	public static final RegistryObject<DensityFunction> SKAIA_RIDGES = REGISTER.register("skaia/ridges",
			() -> base2dNoise(MSNoiseParameters.SKAIA_RIDGES));
	
	public static final RegistryObject<DensityFunction> SKAIA_OFFSET = REGISTER.register("skaia/offset", MSDensityFunctions::skaiaOffset);
	public static final RegistryObject<DensityFunction> SKAIA_DEPTH = REGISTER.register("skaia/depth",
			() -> depth(SKAIA_OFFSET));
	public static final RegistryObject<DensityFunction> SKAIA_FACTOR = REGISTER.register("skaia/factor",
			() -> DensityFunctions.constant(5));
	public static final RegistryObject<DensityFunction> SKAIA_INITIAL_DENSITY = REGISTER.register("skaia/initial_density",
			() -> initialDensity(SKAIA_DEPTH.get(), SKAIA_FACTOR.get()));
	public static final RegistryObject<DensityFunction> SKAIA_FINAL_DENSITY = REGISTER.register("skaia/final_density",
			() -> finalDensity(SKAIA_DEPTH.get(), SKAIA_FACTOR.get(), DensityFunctions.zero(), 256));
	
	private static DensityFunction skaiaOffset()
	{
		var builder = CubicSpline.builder(new DensityFunctions.Spline.Coordinate(SKAIA_RIDGES.getHolder().orElseThrow()));
		builder.addPoint(-1.5F, -0.2F);
		builder.addPoint(-0.5F, 1F);
		builder.addPoint(0.5F, -0.2F);
		builder.addPoint(1.5F, 1F);
		return DensityFunctions.add(DensityFunctions.constant(-0.50375), DensityFunctions.spline(builder.build()));
	}
	
	
	public static final RegistryObject<DensityFunction> LAND_CONTINENTS = REGISTER.register("land/continents",
			() -> base2dNoise(MSNoiseParameters.LAND_CONTINENTS));
	public static final RegistryObject<DensityFunction> LAND_EROSION = REGISTER.register("land/erosion",
			() -> base2dNoise(MSNoiseParameters.LAND_EROSION));
	
	
	@Nonnull
	public static DensityFunction from(Registry<DensityFunction> registry, RegistryObject<DensityFunction> builtinFunction)
	{
		return registry.getOrThrow(Objects.requireNonNull(builtinFunction.getKey()));
	}
	
	private static DensityFunction base2dNoise(RegistryObject<NormalNoise.NoiseParameters> noise)
	{
		return DensityFunctions.flatCache(DensityFunctions.shiftedNoise2d(SHIFT_X.get(), SHIFT_Z.get(), 0.25, noise.getHolder().orElseThrow()));
	}
	
	public static DensityFunction depth(Supplier<DensityFunction> offset)
	{
		return DensityFunctions.add(DensityFunctions.yClampedGradient(-64, 320, 1.5, -1.5), offset.get());
	}
	
	public static DensityFunction initialDensity(DensityFunction depth, DensityFunction factor)
	{
		return DensityFunctions.mul(DensityFunctions.constant(4), DensityFunctions.mul(depth, factor).quarterNegative());
	}
	
	@SuppressWarnings("ConstantConditions")
	public static DensityFunction finalDensity(DensityFunction depth, DensityFunction factor, DensityFunction jaggedness, int height)
	{
		// Uses vanilla noise settings because I'm lazy + we're not using jaggedness at the time of writing
		DensityFunction noise = DensityFunctions.noise(BuiltinRegistries.NOISE.getHolderOrThrow(Noises.JAGGED), 1500, 0);
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
}
