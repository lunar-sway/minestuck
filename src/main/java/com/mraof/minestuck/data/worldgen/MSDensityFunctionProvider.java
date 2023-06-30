package com.mraof.minestuck.data.worldgen;

import com.mojang.serialization.JsonOps;
import com.mraof.minestuck.Minestuck;
import com.mraof.minestuck.world.gen.MSNoiseParameters;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.core.RegistryAccess;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.DataProvider;
import net.minecraft.resources.RegistryOps;
import net.minecraft.resources.ResourceKey;
import net.minecraft.util.CubicSpline;
import net.minecraft.world.level.levelgen.DensityFunction;
import net.minecraft.world.level.levelgen.DensityFunctions;
import net.minecraft.world.level.levelgen.Noises;
import net.minecraft.world.level.levelgen.synth.NormalNoise;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.common.data.JsonCodecProvider;

import java.util.function.BiConsumer;

import static com.mraof.minestuck.world.gen.MSDensityFunctions.*;

public final class MSDensityFunctionProvider
{
	public static DataProvider create(RegistryAccess.Writable registryAccess, DataGenerator generator, ExistingFileHelper existingFileHelper)
	{
		var registry = registryAccess.ownedWritableRegistryOrThrow(Registry.DENSITY_FUNCTION_REGISTRY);
		DataEntriesBuilder<DensityFunction> builder = new DataEntriesBuilder<>(registry);
		generate(registry, registryAccess.registryOrThrow(Registry.NOISE_REGISTRY), builder::add);
		
		return JsonCodecProvider.forDatapackRegistry(generator, existingFileHelper, Minestuck.MOD_ID,
				RegistryOps.create(JsonOps.INSTANCE, registryAccess), Registry.DENSITY_FUNCTION_REGISTRY, builder.getMap());
	}
	
	private static void generate(Registry<DensityFunction> functions, Registry<NormalNoise.NoiseParameters> noises,
								 BiConsumer<ResourceKey<DensityFunction>, DensityFunction> consumer)
	{
		consumer.accept(SHIFT_X, DensityFunctions.flatCache(DensityFunctions.cache2d(DensityFunctions.shiftA(noises.getHolderOrThrow(Noises.SHIFT)))));
		consumer.accept(SHIFT_Z, DensityFunctions.flatCache(DensityFunctions.cache2d(DensityFunctions.shiftB(noises.getHolderOrThrow(Noises.SHIFT)))));
		DensityFunction shiftX = get(functions, SHIFT_X), shiftZ = get(functions, SHIFT_Z);
		
		consumer.accept(SKAIA_RIDGES, base2dNoise(shiftX, shiftZ, noises.getHolderOrThrow(MSNoiseParameters.SKAIA_RIDGES)));
		
		consumer.accept(SKAIA_OFFSET, skaiaOffset(functions.getHolderOrThrow(SKAIA_RIDGES)));
		consumer.accept(SKAIA_DEPTH, depth(get(functions, SKAIA_OFFSET)));
		DensityFunction skaiaDepth = get(functions, SKAIA_DEPTH), skaiaFactor = DensityFunctions.constant(5);
		
		consumer.accept(SKAIA_INITIAL_DENSITY, initialDensity(skaiaDepth, skaiaFactor));
		consumer.accept(SKAIA_FINAL_DENSITY, finalDensity(skaiaDepth, skaiaFactor, DensityFunctions.zero(), noises.getHolderOrThrow(Noises.JAGGED), 256));
		
		consumer.accept(LAND_CONTINENTS, base2dNoise(shiftX, shiftZ, noises.getHolderOrThrow(MSNoiseParameters.LAND_CONTINENTS)));
		consumer.accept(LAND_EROSION, base2dNoise(shiftX, shiftZ, noises.getHolderOrThrow(MSNoiseParameters.LAND_EROSION)));
	}
	
	private static DensityFunction get(Registry<DensityFunction> registry, ResourceKey<DensityFunction> key)
	{
		return new DensityFunctions.HolderHolder(registry.getHolderOrThrow(key));
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
	
	private static DensityFunction base2dNoise(DensityFunction shiftX, DensityFunction shiftZ, Holder<NormalNoise.NoiseParameters> noise)
	{
		return DensityFunctions.flatCache(DensityFunctions.shiftedNoise2d(shiftX, shiftZ, 0.25, noise));
	}
}
