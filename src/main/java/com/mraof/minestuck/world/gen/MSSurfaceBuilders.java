package com.mraof.minestuck.world.gen;

import com.mraof.minestuck.Minestuck;
import net.minecraft.world.gen.surfacebuilders.SurfaceBuilder;
import net.minecraft.world.gen.surfacebuilders.SurfaceBuilderConfig;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class MSSurfaceBuilders
{
	public static final DeferredRegister<SurfaceBuilder<?>> REGISTER = DeferredRegister.create(ForgeRegistries.SURFACE_BUILDERS, Minestuck.MOD_ID);
	
	public static final RegistryObject<SkaiaSurfaceBuilder> SKAIA = REGISTER.register("skaia", () -> new SkaiaSurfaceBuilder(SurfaceBuilderConfig::deserialize));
	public static final RegistryObject<RainbowSurfaceBuilder> RAINBOW = REGISTER.register("rainbow", () -> new RainbowSurfaceBuilder(SurfaceBuilderConfig::deserialize));
}