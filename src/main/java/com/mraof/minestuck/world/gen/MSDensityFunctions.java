package com.mraof.minestuck.world.gen;

import com.mraof.minestuck.Minestuck;
import net.minecraft.core.Registry;
import net.minecraft.world.level.levelgen.DensityFunction;
import net.minecraft.world.level.levelgen.DensityFunctions;
import net.minecraft.world.level.levelgen.synth.BlendedNoise;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

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
}
