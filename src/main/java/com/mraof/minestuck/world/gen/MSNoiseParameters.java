package com.mraof.minestuck.world.gen;

import com.mraof.minestuck.Minestuck;
import net.minecraft.core.Registry;
import net.minecraft.world.level.levelgen.synth.NormalNoise;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

public class MSNoiseParameters
{
	public static final DeferredRegister<NormalNoise.NoiseParameters> REGISTER = DeferredRegister.create(Registry.NOISE_REGISTRY, Minestuck.MOD_ID);
	
	public static final RegistryObject<NormalNoise.NoiseParameters> LAND_CONTINENTS = REGISTER.register("land_continents", () -> new NormalNoise.NoiseParameters(-6, 1));
	public static final RegistryObject<NormalNoise.NoiseParameters> LAND_EROSION = REGISTER.register("land_erosion", () -> new NormalNoise.NoiseParameters(-5, 1));
	
}
