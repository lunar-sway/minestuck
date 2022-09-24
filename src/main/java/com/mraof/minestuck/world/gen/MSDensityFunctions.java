package com.mraof.minestuck.world.gen;

import com.mraof.minestuck.Minestuck;
import net.minecraft.core.Registry;
import net.minecraft.world.level.levelgen.DensityFunction;
import net.minecraft.world.level.levelgen.DensityFunctions;
import net.minecraft.world.level.levelgen.NoiseRouterWithOnlyNoises;
import net.minecraft.world.level.levelgen.synth.BlendedNoise;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

import javax.annotation.Nonnull;
import java.util.Objects;

public class MSDensityFunctions
{
	public static final DeferredRegister<DensityFunction> REGISTER = DeferredRegister.create(Registry.DENSITY_FUNCTION_REGISTRY, Minestuck.MOD_ID);
	
	public static final RegistryObject<DensityFunction> SKAIA_RIDGES = REGISTER.register("skaia/ridges",
			() -> DensityFunctions.noise(MSNoiseParameters.SKAIA_RIDGES.getHolder().orElseThrow(), 0.25, 0));
	
	public static final RegistryObject<DensityFunction> SKAIA_DEPTH = REGISTER.register("skaia/depth",
			() -> DensityFunctions.add(DensityFunctions.yClampedGradient(0, 256, 1, -1),
					DensityFunctions.terrainShaperSpline(DensityFunctions.zero(), DensityFunctions.zero(), SKAIA_RIDGES.get(), DensityFunctions.TerrainShaperSpline.SplineType.OFFSET, -0.81, 2.5)));
	public static final RegistryObject<DensityFunction> SKAIA_FACTOR = REGISTER.register("skaia/factor",
			() -> DensityFunctions.terrainShaperSpline(DensityFunctions.zero(), DensityFunctions.zero(), SKAIA_RIDGES.get(), DensityFunctions.TerrainShaperSpline.SplineType.FACTOR, 0, 8));
	
	public static final RegistryObject<DensityFunction> SKAIA_INITIAL_DENSITY = REGISTER.register("skaia/initial_density",
			() -> DensityFunctions.mul(DensityFunctions.constant(4), DensityFunctions.mul(SKAIA_DEPTH.get(), SKAIA_FACTOR.get()).quarterNegative()));
	public static final RegistryObject<DensityFunction> SKAIA_FINAL_DENSITY = REGISTER.register("skaia/final_density",
			() -> DensityFunctions.mul(DensityFunctions.constant(0.64), DensityFunctions.interpolated(DensityFunctions.blendDensity(
					DensityFunctions.slide(null, DensityFunctions.add(BlendedNoise.UNSEEDED, SKAIA_INITIAL_DENSITY.get()))))).squeeze());
	
	public static final RegistryObject<DensityFunction> LAND_CONTINENTS = REGISTER.register("land/continents",
			() -> DensityFunctions.noise(MSNoiseParameters.LAND_CONTINENTS.getHolder().orElseThrow(), 0.25, 0));
	public static final RegistryObject<DensityFunction> LAND_EROSION = REGISTER.register("land/erosion",
			() -> DensityFunctions.noise(MSNoiseParameters.LAND_EROSION.getHolder().orElseThrow(), 0.25, 0));
	
	public static final RegistryObject<DensityFunction> LAND_DEPTH = REGISTER.register("land/depth",
			() -> DensityFunctions.add(DensityFunctions.yClampedGradient(0, 256, 1, -1),
					DensityFunctions.terrainShaperSpline(LAND_CONTINENTS.get(), LAND_EROSION.get(), DensityFunctions.zero(), DensityFunctions.TerrainShaperSpline.SplineType.OFFSET, -0.81, 2.5)));
	public static final RegistryObject<DensityFunction> LAND_FACTOR = REGISTER.register("land/factor",
			() -> DensityFunctions.terrainShaperSpline(LAND_CONTINENTS.get(), LAND_EROSION.get(), DensityFunctions.zero(), DensityFunctions.TerrainShaperSpline.SplineType.FACTOR, 0, 8));
	
	public static final RegistryObject<DensityFunction> LAND_INITIAL_DENSITY = REGISTER.register("land/initial_density",
			() -> DensityFunctions.mul(DensityFunctions.constant(4), DensityFunctions.mul(LAND_DEPTH.get(), LAND_FACTOR.get()).quarterNegative()));
	public static final RegistryObject<DensityFunction> LAND_FINAL_DENSITY = REGISTER.register("land/final_density",
			() -> DensityFunctions.interpolated(DensityFunctions.slide(null, DensityFunctions.add(BlendedNoise.UNSEEDED, LAND_INITIAL_DENSITY.get()))).squeeze());
	
	@Nonnull
	public static DensityFunction from(Registry<DensityFunction> registry, RegistryObject<DensityFunction> builtinFunction)
	{
		return registry.getOrThrow(Objects.requireNonNull(builtinFunction.getKey()));
	}
	
	public static NoiseRouterWithOnlyNoises makeLandNoiseRouter(Registry<DensityFunction> registry)
	{
		return new NoiseRouterWithOnlyNoises(
				DensityFunctions.zero(), DensityFunctions.zero(), DensityFunctions.zero(), DensityFunctions.zero(),	// aquifier info
				DensityFunctions.zero(), DensityFunctions.zero(), from(registry, LAND_CONTINENTS), from(registry, LAND_EROSION), from(registry, LAND_DEPTH), DensityFunctions.zero(), // biome parameters
				from(registry, LAND_INITIAL_DENSITY), from(registry, LAND_FINAL_DENSITY),	// terrain and surface height
				DensityFunctions.zero(), DensityFunctions.zero(), DensityFunctions.zero());	// ore vein info
	}
}
