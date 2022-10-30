package com.mraof.minestuck.world.gen;

import com.mraof.minestuck.Minestuck;
import net.minecraft.core.Registry;
import net.minecraft.data.BuiltinRegistries;
import net.minecraft.world.level.levelgen.DensityFunction;
import net.minecraft.world.level.levelgen.DensityFunctions;
import net.minecraft.world.level.levelgen.NoiseRouterWithOnlyNoises;
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
	
	public static final RegistryObject<DensityFunction> SKAIA_DEPTH = REGISTER.register("skaia/depth",
			() -> depth(DensityFunctions::zero, DensityFunctions::zero, SKAIA_RIDGES));
	public static final RegistryObject<DensityFunction> SKAIA_FACTOR = REGISTER.register("skaia/factor",
			() -> factor(DensityFunctions::zero, DensityFunctions::zero, SKAIA_RIDGES));
	public static final RegistryObject<DensityFunction> SKAIA_JAGGEDNESS = REGISTER.register("skaia/jaggedness",
			() -> jaggedness(DensityFunctions::zero, DensityFunctions::zero, SKAIA_RIDGES));
	
	public static final RegistryObject<DensityFunction> SKAIA_INITIAL_DENSITY = REGISTER.register("skaia/initial_density",
			() -> initialDensity(SKAIA_DEPTH, SKAIA_FACTOR));
	public static final RegistryObject<DensityFunction> SKAIA_FINAL_DENSITY = REGISTER.register("skaia/final_density",
			() -> finalDensity(SKAIA_DEPTH, SKAIA_FACTOR, SKAIA_JAGGEDNESS));
	
	
	public static final RegistryObject<DensityFunction> LAND_CONTINENTS = REGISTER.register("land/continents",
			() -> base2dNoise(MSNoiseParameters.LAND_CONTINENTS));
	public static final RegistryObject<DensityFunction> LAND_EROSION = REGISTER.register("land/erosion",
			() -> base2dNoise(MSNoiseParameters.LAND_EROSION));
	
	public static final RegistryObject<DensityFunction> LAND_DEPTH = REGISTER.register("land/depth",
			() -> depth(LAND_CONTINENTS, LAND_EROSION, DensityFunctions::zero));
	public static final RegistryObject<DensityFunction> LAND_FACTOR = REGISTER.register("land/factor",
			() -> factor(LAND_CONTINENTS, LAND_EROSION, DensityFunctions::zero));
	public static final RegistryObject<DensityFunction> LAND_JAGGEDNESS = REGISTER.register("land/jaggedness",
			() -> jaggedness(LAND_CONTINENTS, LAND_EROSION, DensityFunctions::zero));
	
	
	public static final RegistryObject<DensityFunction> LAND_INITIAL_DENSITY = REGISTER.register("land/initial_density",
			() -> initialDensity(LAND_DEPTH, LAND_FACTOR));
	public static final RegistryObject<DensityFunction> LAND_FINAL_DENSITY = REGISTER.register("land/final_density",
			() -> finalDensity(LAND_DEPTH, LAND_FACTOR, LAND_JAGGEDNESS));
	
	public static NoiseRouterWithOnlyNoises makeLandNoiseRouter(Registry<DensityFunction> registry)
	{
		return new NoiseRouterWithOnlyNoises(
				DensityFunctions.zero(), DensityFunctions.constant(-1), DensityFunctions.zero(), DensityFunctions.zero(),	// aquifer info
				DensityFunctions.zero(), DensityFunctions.zero(), from(registry, LAND_CONTINENTS), from(registry, LAND_EROSION), from(registry, LAND_DEPTH), DensityFunctions.zero(), // biome parameters
				from(registry, LAND_INITIAL_DENSITY), from(registry, LAND_FINAL_DENSITY),	// terrain and surface height
				DensityFunctions.zero(), DensityFunctions.zero(), DensityFunctions.zero());	// ore vein info
	}
	
	
	@Nonnull
	public static DensityFunction from(Registry<DensityFunction> registry, RegistryObject<DensityFunction> builtinFunction)
	{
		return registry.getOrThrow(Objects.requireNonNull(builtinFunction.getKey()));
	}
	
	private static DensityFunction base2dNoise(RegistryObject<NormalNoise.NoiseParameters> noise)
	{
		return DensityFunctions.flatCache(DensityFunctions.shiftedNoise2d(SHIFT_X.get(), SHIFT_Z.get(), 0.25, noise.getHolder().orElseThrow()));
	}
	
	@SuppressWarnings("deprecation")
	private static DensityFunction depth(Supplier<DensityFunction> continents, Supplier<DensityFunction> erosion, Supplier<DensityFunction> weirdness)
	{
		return DensityFunctions.add(DensityFunctions.yClampedGradient(-64, 320, 1.5, -1.5),
				DensityFunctions.flatCache(DensityFunctions.cache2d(DensityFunctions.terrainShaperSpline(continents.get(), erosion.get(), weirdness.get(),
						DensityFunctions.TerrainShaperSpline.SplineType.OFFSET, -0.81, 2.5))));
	}
	
	@SuppressWarnings("deprecation")
	private static DensityFunction factor(Supplier<DensityFunction> continents, Supplier<DensityFunction> erosion, Supplier<DensityFunction> weirdness)
	{
		return DensityFunctions.flatCache(DensityFunctions.cache2d(DensityFunctions.terrainShaperSpline(continents.get(), erosion.get(), weirdness.get(),
				DensityFunctions.TerrainShaperSpline.SplineType.FACTOR, 0, 8)));
	}
	
	@SuppressWarnings("deprecation")
	private static DensityFunction jaggedness(Supplier<DensityFunction> continents, Supplier<DensityFunction> erosion, Supplier<DensityFunction> weirdness)
	{
		// Uses vanilla noise settings because I'm lazy + we're not using jaggedness at the time of writing
		DensityFunction noise = DensityFunctions.noise(BuiltinRegistries.NOISE.getHolderOrThrow(Noises.JAGGED), 1500, 0);
		DensityFunction jaggednessFactor = DensityFunctions.flatCache(DensityFunctions.cache2d(DensityFunctions.terrainShaperSpline(
				continents.get(), erosion.get(), weirdness.get(), DensityFunctions.TerrainShaperSpline.SplineType.JAGGEDNESS, 0, 1.28)));
		return DensityFunctions.mul(jaggednessFactor, noise.halfNegative());
	}
	
	private static DensityFunction initialDensity(Supplier<DensityFunction> depth, Supplier<DensityFunction> factor)
	{
		return DensityFunctions.mul(DensityFunctions.constant(4), DensityFunctions.mul(depth.get(), factor.get()).quarterNegative());
	}
	
	@SuppressWarnings("ConstantConditions")
	private static DensityFunction finalDensity(Supplier<DensityFunction> depth, Supplier<DensityFunction> factor, Supplier<DensityFunction> jaggedness)
	{
		DensityFunction modifiedDepth = DensityFunctions.add(depth.get(), jaggedness.get());
		DensityFunction baseDensity = initialDensity(() -> modifiedDepth, factor);
		return DensityFunctions.mul(DensityFunctions.constant(0.64), DensityFunctions.interpolated(DensityFunctions.blendDensity(
				DensityFunctions.slide(null, DensityFunctions.add(BlendedNoise.UNSEEDED, baseDensity))))).squeeze();
	}
}
