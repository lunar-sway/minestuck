package com.mraof.minestuck.world.gen.feature;

import com.mraof.minestuck.Minestuck;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.data.worldgen.features.TreeFeatures;
import net.minecraft.data.worldgen.placement.PlacementUtils;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.levelgen.placement.*;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

import java.util.List;

public class MSPlacedFeatures
{
	public static final DeferredRegister<PlacedFeature> REGISTER = DeferredRegister.create(Registry.PLACED_FEATURE_REGISTRY, Minestuck.MOD_ID);
	
	public static final RegistryObject<PlacedFeature> SURFACE_FOSSIL = REGISTER.register("surface_fossil", () -> new PlacedFeature(MSCFeatures.SURFACE_FOSSIL.getHolder().orElseThrow(),
			List.of(RarityFilter.onAverageOnceEvery(5), BiomeFilter.biome())));
	
	public static final RegistryObject<PlacedFeature> BROKEN_SWORD = REGISTER.register("broken_sword", () -> new PlacedFeature(MSCFeatures.BROKEN_SWORD.getHolder().orElseThrow(),
			List.of(RarityFilter.onAverageOnceEvery(10), BiomeFilter.biome())));
	public static final RegistryObject<PlacedFeature> UNCOMMON_BROKEN_SWORD = REGISTER.register("uncommon_broken_sword", () -> new PlacedFeature(MSCFeatures.BROKEN_SWORD.getHolder().orElseThrow(),
			List.of(RarityFilter.onAverageOnceEvery(50), BiomeFilter.biome())));
	
	public static final RegistryObject<PlacedFeature> BLOOD_POOL = REGISTER.register("blood_pool", () -> new PlacedFeature(MSCFeatures.BLOOD_POOL.getHolder().orElseThrow(),
			List.of(CountPlacement.of(5), InSquarePlacement.spread(), PlacementUtils.HEIGHTMAP, BiomeFilter.biome())));
	
	public static final RegistryObject<PlacedFeature> DARK_OAK = REGISTER.register("dark_oak",  () -> new PlacedFeature(Holder.hackyErase(TreeFeatures.DARK_OAK),
			List.of(CountPlacement.of(10), InSquarePlacement.spread(), PlacementUtils.HEIGHTMAP, PlacementUtils.filteredByBlockSurvival(Blocks.DARK_OAK_SAPLING), BiomeFilter.biome())));
	
	public static final RegistryObject<PlacedFeature> STRAWBERRY_PATCH = REGISTER.register("strawberry_patch", () -> new PlacedFeature(MSCFeatures.STRAWBERRY_PATCH.getHolder().orElseThrow(),
			List.of(InSquarePlacement.spread(), PlacementUtils.HEIGHTMAP_WORLD_SURFACE, BiomeFilter.biome())));
	public static final RegistryObject<PlacedFeature> RARE_STRAWBERRY_PATCH = REGISTER.register("rare_strawberry_patch", () -> new PlacedFeature(MSCFeatures.STRAWBERRY_PATCH.getHolder().orElseThrow(),
			List.of(RarityFilter.onAverageOnceEvery(60), InSquarePlacement.spread(), PlacementUtils.HEIGHTMAP_WORLD_SURFACE, BiomeFilter.biome())));
	
}