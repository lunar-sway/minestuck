package com.mraof.minestuck.world.gen.feature;

import com.mraof.minestuck.Minestuck;
import com.mraof.minestuck.block.MSBlocks;
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
	public static final RegistryObject<PlacedFeature> OASIS = REGISTER.register("oasis", () -> new PlacedFeature(MSCFeatures.OASIS.getHolder().orElseThrow(),
			List.of(RarityFilter.onAverageOnceEvery(128), InSquarePlacement.spread(), PlacementUtils.HEIGHTMAP, BiomeFilter.biome())));
	
	public static final RegistryObject<PlacedFeature> COBBLESTONE_BLOCK_BLOB = REGISTER.register("cobblestone_block_blob", () -> new PlacedFeature(MSCFeatures.COBBLESTONE_BLOCK_BLOB.getHolder().orElseThrow(),
			List.of(RarityFilter.onAverageOnceEvery(30), InSquarePlacement.spread(), PlacementUtils.HEIGHTMAP, BiomeFilter.biome())));
	public static final RegistryObject<PlacedFeature> RANDOM_ROCK_BLOCK_BLOB = REGISTER.register("random_rock_block_blob", () -> new PlacedFeature(MSCFeatures.RANDOM_ROCK_BLOCK_BLOB.getHolder().orElseThrow(),
			List.of(RarityFilter.onAverageOnceEvery(20), InSquarePlacement.spread(), PlacementUtils.HEIGHTMAP, BiomeFilter.biome())));
	public static final RegistryObject<PlacedFeature> LARGE_RANDOM_ROCK_BLOCK_BLOB = REGISTER.register("large_random_rock_block_blob", () -> new PlacedFeature(MSCFeatures.LARGE_RANDOM_ROCK_BLOCK_BLOB.getHolder().orElseThrow(),
			List.of(RarityFilter.onAverageOnceEvery(30), InSquarePlacement.spread(), PlacementUtils.HEIGHTMAP, BiomeFilter.biome())));
	
	public static final RegistryObject<PlacedFeature> DARK_OAK = REGISTER.register("dark_oak",  () -> new PlacedFeature(Holder.hackyErase(TreeFeatures.DARK_OAK),
			List.of(CountPlacement.of(10), InSquarePlacement.spread(), PlacementUtils.HEIGHTMAP, PlacementUtils.filteredByBlockSurvival(Blocks.DARK_OAK_SAPLING), BiomeFilter.biome())));
	public static final RegistryObject<PlacedFeature> GLOWING_TREE = REGISTER.register("glowing_tree", () -> new PlacedFeature(MSCFeatures.GLOWING_TREE.getHolder().orElseThrow(),
			List.of(RarityFilter.onAverageOnceEvery(2), InSquarePlacement.spread(), PlacementUtils.HEIGHTMAP, BiomeFilter.biome())));
	public static final RegistryObject<PlacedFeature> PETRIFIED_TREE = REGISTER.register("petrified_tree", () -> new PlacedFeature(MSCFeatures.PETRIFIED_TREE.getHolder().orElseThrow(),
			List.of(PlacementUtils.countExtra(2, 0.5F, 1), InSquarePlacement.spread(), PlacementUtils.HEIGHTMAP, BiomeFilter.biome())));
	public static final RegistryObject<PlacedFeature> SPARSE_PETRIFIED_TREE = REGISTER.register("sparse_petrified_tree", () -> new PlacedFeature(MSCFeatures.PETRIFIED_TREE.getHolder().orElseThrow(),
			List.of(RarityFilter.onAverageOnceEvery(20), InSquarePlacement.spread(), PlacementUtils.HEIGHTMAP, BiomeFilter.biome())));
	
	public static final RegistryObject<PlacedFeature> STRAWBERRY_PATCH = REGISTER.register("strawberry_patch", () -> new PlacedFeature(MSCFeatures.STRAWBERRY_PATCH.getHolder().orElseThrow(),
			List.of(InSquarePlacement.spread(), PlacementUtils.HEIGHTMAP_WORLD_SURFACE, BiomeFilter.biome())));
	public static final RegistryObject<PlacedFeature> RARE_STRAWBERRY_PATCH = REGISTER.register("rare_strawberry_patch", () -> new PlacedFeature(MSCFeatures.STRAWBERRY_PATCH.getHolder().orElseThrow(),
			List.of(RarityFilter.onAverageOnceEvery(60), InSquarePlacement.spread(), PlacementUtils.HEIGHTMAP_WORLD_SURFACE, BiomeFilter.biome())));
	public static final RegistryObject<PlacedFeature> GLOWING_MUSHROOM_PATCH = REGISTER.register("glowing_mushroom_patch", () -> new PlacedFeature(MSCFeatures.GLOWING_MUSHROOM_PATCH.getHolder().orElseThrow(),
			List.of(RarityFilter.onAverageOnceEvery(2), InSquarePlacement.spread(), PlacementUtils.HEIGHTMAP, BiomeFilter.biome())));
	public static final RegistryObject<PlacedFeature> SPARSE_GLOWING_MUSHROOM_PATCH = REGISTER.register("sparse_glowing_mushroom_patch", () -> new PlacedFeature(MSCFeatures.GLOWING_MUSHROOM_PATCH.getHolder().orElseThrow(),
			List.of(RarityFilter.onAverageOnceEvery(4), InSquarePlacement.spread(), PlacementUtils.HEIGHTMAP, BiomeFilter.biome())));
	public static final RegistryObject<PlacedFeature> TALL_END_GRASS_PATCH = REGISTER.register("tall_end_grass_patch", () -> new PlacedFeature(MSCFeatures.TALL_END_GRASS_PATCH.getHolder().orElseThrow(),
			List.of(RarityFilter.onAverageOnceEvery(8), InSquarePlacement.spread(), PlacementUtils.HEIGHTMAP, PlacementUtils.filteredByBlockSurvival(MSBlocks.TALL_END_GRASS.get()), BiomeFilter.biome())));
	public static final RegistryObject<PlacedFeature> PETRIFIED_GRASS_PATCH = REGISTER.register("petrified_grass_patch", () -> new PlacedFeature(MSCFeatures.PETRIFIED_GRASS_PATCH.getHolder().orElseThrow(),
			List.of(RarityFilter.onAverageOnceEvery(30), InSquarePlacement.spread(), PlacementUtils.HEIGHTMAP, BiomeFilter.biome())));
	public static final RegistryObject<PlacedFeature> SPARSE_PETRIFIED_GRASS_PATCH = REGISTER.register("sparse_petrified_grass_patch", () -> new PlacedFeature(MSCFeatures.PETRIFIED_GRASS_PATCH.getHolder().orElseThrow(),
			List.of(RarityFilter.onAverageOnceEvery(60), InSquarePlacement.spread(), PlacementUtils.HEIGHTMAP, BiomeFilter.biome())));
	public static final RegistryObject<PlacedFeature> PETRIFIED_POPPY_PATCH = REGISTER.register("petrified_poppy_patch", () -> new PlacedFeature(MSCFeatures.PETRIFIED_POPPY_PATCH.getHolder().orElseThrow(),
			List.of(RarityFilter.onAverageOnceEvery(15), InSquarePlacement.spread(), PlacementUtils.HEIGHTMAP, BiomeFilter.biome())));
	public static final RegistryObject<PlacedFeature> SPARSE_PETRIFIED_POPPY_PATCH = REGISTER.register("sparse_petrified_poppy_patch", () -> new PlacedFeature(MSCFeatures.PETRIFIED_POPPY_PATCH.getHolder().orElseThrow(),
			List.of(RarityFilter.onAverageOnceEvery(30), InSquarePlacement.spread(), PlacementUtils.HEIGHTMAP, BiomeFilter.biome())));
	public static final RegistryObject<PlacedFeature> DESERT_BUSH_PATCH = REGISTER.register("desert_bush_patch", () -> new PlacedFeature(MSCFeatures.DESERT_BUSH_PATCH.getHolder().orElseThrow(),
			List.of(RarityFilter.onAverageOnceEvery(30), InSquarePlacement.spread(), PlacementUtils.HEIGHTMAP, BiomeFilter.biome())));
	public static final RegistryObject<PlacedFeature> SPARSE_DESERT_BUSH_PATCH = REGISTER.register("sparse_desert_bush_patch", () -> new PlacedFeature(MSCFeatures.DESERT_BUSH_PATCH.getHolder().orElseThrow(),
			List.of(RarityFilter.onAverageOnceEvery(120), InSquarePlacement.spread(), PlacementUtils.HEIGHTMAP, BiomeFilter.biome())));
	public static final RegistryObject<PlacedFeature> BLOOMING_CACTUS_PATCH = REGISTER.register("blooming_cactus_patch", () -> new PlacedFeature(MSCFeatures.BLOOMING_CACTUS_PATCH.getHolder().orElseThrow(),
			List.of(RarityFilter.onAverageOnceEvery(30), InSquarePlacement.spread(), PlacementUtils.HEIGHTMAP, BiomeFilter.biome())));
	
}