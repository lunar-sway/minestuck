package com.mraof.minestuck.world.gen.feature;

import com.mraof.minestuck.Minestuck;
import com.mraof.minestuck.block.MSBlocks;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.data.worldgen.features.TreeFeatures;
import net.minecraft.data.worldgen.features.VegetationFeatures;
import net.minecraft.data.worldgen.placement.PlacementUtils;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.levelgen.VerticalAnchor;
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
	public static final RegistryObject<PlacedFeature> BUCKET = REGISTER.register("bucket", () -> new PlacedFeature(MSCFeatures.BUCKET.getHolder().orElseThrow(),
			List.of(RarityFilter.onAverageOnceEvery(16), BiomeFilter.biome())));
	public static final RegistryObject<PlacedFeature> CAKE_PEDESTAL = REGISTER.register("cake_pedestal", () -> new PlacedFeature(MSCFeatures.CAKE_PEDESTAL.getHolder().orElseThrow(),
			List.of(RarityFilter.onAverageOnceEvery(100), BiomeFilter.biome())));
	public static final RegistryObject<PlacedFeature> COG = REGISTER.register("cog", () -> new PlacedFeature(MSCFeatures.COG.getHolder().orElseThrow(),
			List.of(RarityFilter.onAverageOnceEvery(2), BiomeFilter.biome())));
	public static final RegistryObject<PlacedFeature> UNCOMMON_COG = REGISTER.register("uncommon_cog", () -> new PlacedFeature(MSCFeatures.COG.getHolder().orElseThrow(),
			List.of(RarityFilter.onAverageOnceEvery(10), BiomeFilter.biome())));
	public static final RegistryObject<PlacedFeature> FLOOR_COG = REGISTER.register("floor_cog", () -> new PlacedFeature(MSCFeatures.FLOOR_COG.getHolder().orElseThrow(),
			List.of(RarityFilter.onAverageOnceEvery(3), InSquarePlacement.spread(), BiomeFilter.biome())));
	public static final RegistryObject<PlacedFeature> UNCOMMON_FLOOR_COG = REGISTER.register("uncommon_floor_cog", () -> new PlacedFeature(MSCFeatures.FLOOR_COG.getHolder().orElseThrow(),
			List.of(RarityFilter.onAverageOnceEvery(20), InSquarePlacement.spread(), BiomeFilter.biome())));
	public static final RegistryObject<PlacedFeature> SMALL_LIBRARY = REGISTER.register("small_library", () -> new PlacedFeature(MSCFeatures.SMALL_LIBRARY.getHolder().orElseThrow(),
			List.of(RarityFilter.onAverageOnceEvery(64), BiomeFilter.biome())));
	public static final RegistryObject<PlacedFeature> TOWER = REGISTER.register("tower", () -> new PlacedFeature(MSCFeatures.TOWER.getHolder().orElseThrow(),
			List.of(RarityFilter.onAverageOnceEvery(20), InSquarePlacement.spread(), PlacementUtils.HEIGHTMAP_WORLD_SURFACE, BiomeFilter.biome())));
	public static final RegistryObject<PlacedFeature> PARCEL_PYXIS = REGISTER.register("parcel_pyxis", () -> new PlacedFeature(MSCFeatures.PARCEL_PYXIS.getHolder().orElseThrow(),
			List.of(RarityFilter.onAverageOnceEvery(60), InSquarePlacement.spread(), PlacementUtils.HEIGHTMAP, BiomeFilter.biome())));
	
	public static final RegistryObject<PlacedFeature> BLOOD_POOL = REGISTER.register("blood_pool", () -> new PlacedFeature(MSCFeatures.BLOOD_POOL.getHolder().orElseThrow(),
			List.of(CountPlacement.of(5), InSquarePlacement.spread(), PlacementUtils.HEIGHTMAP, BiomeFilter.biome())));
	public static final RegistryObject<PlacedFeature> OASIS = REGISTER.register("oasis", () -> new PlacedFeature(MSCFeatures.OASIS.getHolder().orElseThrow(),
			List.of(RarityFilter.onAverageOnceEvery(128), InSquarePlacement.spread(), PlacementUtils.HEIGHTMAP, BiomeFilter.biome())));
	public static final RegistryObject<PlacedFeature> OCEAN_RUNDOWN = REGISTER.register("ocean_rundown", () -> new PlacedFeature(MSCFeatures.OCEAN_RUNDOWN.getHolder().orElseThrow(),
			List.of(CountPlacement.of(3), InSquarePlacement.spread(), PlacementUtils.HEIGHTMAP, BiomeFilter.biome())));
	
	public static final RegistryObject<PlacedFeature> FIRE_FIELD = REGISTER.register("fire_field", () -> new PlacedFeature(MSCFeatures.FIRE_FIELD.getHolder().orElseThrow(),
			List.of(CountPlacement.of(7), InSquarePlacement.spread(), HeightRangePlacement.uniform(VerticalAnchor.absolute(0), VerticalAnchor.absolute(256)), BiomeFilter.biome())));
	public static final RegistryObject<PlacedFeature> EXTRA_FIRE_FIELD = REGISTER.register("extra_fire_field", () -> new PlacedFeature(MSCFeatures.FIRE_FIELD.getHolder().orElseThrow(),
			List.of(CountPlacement.of(10), InSquarePlacement.spread(), HeightRangePlacement.uniform(VerticalAnchor.absolute(0), VerticalAnchor.absolute(256)), BiomeFilter.biome())));
	
	public static final RegistryObject<PlacedFeature> MESA = REGISTER.register("mesa", () -> new PlacedFeature(MSCFeatures.MESA.getHolder().orElseThrow(),
			List.of(RarityFilter.onAverageOnceEvery(25), InSquarePlacement.spread(), PlacementUtils.HEIGHTMAP, BiomeFilter.biome())));
	public static final RegistryObject<PlacedFeature> COBBLESTONE_BLOCK_BLOB = REGISTER.register("cobblestone_block_blob", () -> new PlacedFeature(MSCFeatures.COBBLESTONE_BLOCK_BLOB.getHolder().orElseThrow(),
			List.of(RarityFilter.onAverageOnceEvery(30), InSquarePlacement.spread(), PlacementUtils.HEIGHTMAP, BiomeFilter.biome())));
	public static final RegistryObject<PlacedFeature> RANDOM_ROCK_BLOCK_BLOB = REGISTER.register("random_rock_block_blob", () -> new PlacedFeature(MSCFeatures.RANDOM_ROCK_BLOCK_BLOB.getHolder().orElseThrow(),
			List.of(RarityFilter.onAverageOnceEvery(20), InSquarePlacement.spread(), PlacementUtils.HEIGHTMAP, BiomeFilter.biome())));
	public static final RegistryObject<PlacedFeature> LARGE_RANDOM_ROCK_BLOCK_BLOB = REGISTER.register("large_random_rock_block_blob", () -> new PlacedFeature(MSCFeatures.LARGE_RANDOM_ROCK_BLOCK_BLOB.getHolder().orElseThrow(),
			List.of(RarityFilter.onAverageOnceEvery(30), InSquarePlacement.spread(), PlacementUtils.HEIGHTMAP, BiomeFilter.biome())));
	
	public static final RegistryObject<PlacedFeature> DARK_OAK = REGISTER.register("dark_oak",  () -> new PlacedFeature(Holder.hackyErase(TreeFeatures.DARK_OAK),
			List.of(CountPlacement.of(10), InSquarePlacement.spread(), PlacementUtils.HEIGHTMAP, PlacementUtils.filteredByBlockSurvival(Blocks.DARK_OAK_SAPLING), BiomeFilter.biome())));
	public static final RegistryObject<PlacedFeature> RAINBOW_TREE = REGISTER.register("rainbow_tree", () -> new PlacedFeature(MSCFeatures.RAINBOW_TREE.getHolder().orElseThrow(),
			List.of(PlacementUtils.countExtra(2, 0.1F, 1), InSquarePlacement.spread(), PlacementUtils.HEIGHTMAP, PlacementUtils.filteredByBlockSurvival(MSBlocks.RAINBOW_SAPLING.get()), BiomeFilter.biome())));
	public static final RegistryObject<PlacedFeature> EXTRA_RAINBOW_TREE = REGISTER.register("extra_rainbow_tree", () -> new PlacedFeature(MSCFeatures.RAINBOW_TREE.getHolder().orElseThrow(),
			List.of(PlacementUtils.countExtra(4, 0.1F, 1), InSquarePlacement.spread(), PlacementUtils.HEIGHTMAP, PlacementUtils.filteredByBlockSurvival(MSBlocks.RAINBOW_SAPLING.get()), BiomeFilter.biome())));
	public static final RegistryObject<PlacedFeature> GLOWING_TREE = REGISTER.register("glowing_tree", () -> new PlacedFeature(MSCFeatures.GLOWING_TREE.getHolder().orElseThrow(),
			List.of(RarityFilter.onAverageOnceEvery(2), InSquarePlacement.spread(), PlacementUtils.HEIGHTMAP, BiomeFilter.biome())));
	public static final RegistryObject<PlacedFeature> PETRIFIED_TREE = REGISTER.register("petrified_tree", () -> new PlacedFeature(MSCFeatures.PETRIFIED_TREE.getHolder().orElseThrow(),
			List.of(PlacementUtils.countExtra(2, 0.5F, 1), InSquarePlacement.spread(), PlacementUtils.HEIGHTMAP, BiomeFilter.biome())));
	public static final RegistryObject<PlacedFeature> SPARSE_PETRIFIED_TREE = REGISTER.register("sparse_petrified_tree", () -> new PlacedFeature(MSCFeatures.PETRIFIED_TREE.getHolder().orElseThrow(),
			List.of(RarityFilter.onAverageOnceEvery(20), InSquarePlacement.spread(), PlacementUtils.HEIGHTMAP, BiomeFilter.biome())));
	public static final RegistryObject<PlacedFeature> DEAD_TREE = REGISTER.register("dead_tree", () -> new PlacedFeature(MSCFeatures.DEAD_TREE.getHolder().orElseThrow(),
			List.of(PlacementUtils.countExtra(2, 0.1F, 1), InSquarePlacement.spread(), PlacementUtils.HEIGHTMAP_TOP_SOLID, BiomeFilter.biome())));
	public static final RegistryObject<PlacedFeature> EXTRA_DEAD_TREE = REGISTER.register("extra_dead_tree", () -> new PlacedFeature(MSCFeatures.DEAD_TREE.getHolder().orElseThrow(),
			List.of(PlacementUtils.countExtra(4, 0.1F, 1), InSquarePlacement.spread(), PlacementUtils.HEIGHTMAP_TOP_SOLID, BiomeFilter.biome())));
	
	public static final RegistryObject<PlacedFeature> FOREST_LAND_TREES = REGISTER.register("forest_land_trees", () -> new PlacedFeature(MSCFeatures.FOREST_LAND_TREES.getHolder().orElseThrow(),
			List.of(PlacementUtils.countExtra(5, 0.1F, 1), InSquarePlacement.spread(), PlacementUtils.HEIGHTMAP, BiomeFilter.biome())));
	public static final RegistryObject<PlacedFeature> DENSE_FOREST_LAND_TREES = REGISTER.register("dense_forest_land_trees", () -> new PlacedFeature(MSCFeatures.FOREST_LAND_TREES.getHolder().orElseThrow(),
			List.of(PlacementUtils.countExtra(10, 0.1F, 1), InSquarePlacement.spread(), PlacementUtils.HEIGHTMAP, BiomeFilter.biome())));
	public static final RegistryObject<PlacedFeature> TAIGA_LAND_TREES = REGISTER.register("taiga_land_trees", () -> new PlacedFeature(MSCFeatures.TAIGA_LAND_TREES.getHolder().orElseThrow(),
			List.of(PlacementUtils.countExtra(5, 0.1F, 1), InSquarePlacement.spread(), PlacementUtils.HEIGHTMAP, BiomeFilter.biome())));
	public static final RegistryObject<PlacedFeature> DENSE_TAIGA_LAND_TREES = REGISTER.register("dense_taiga_land_trees", () -> new PlacedFeature(MSCFeatures.TAIGA_LAND_TREES.getHolder().orElseThrow(),
			List.of(PlacementUtils.countExtra(10, 0.1F, 1), InSquarePlacement.spread(), PlacementUtils.HEIGHTMAP, BiomeFilter.biome())));
	public static final RegistryObject<PlacedFeature> HUGE_MUSHROOMS = REGISTER.register("huge_mushrooms", () -> new PlacedFeature(Holder.hackyErase(VegetationFeatures.MUSHROOM_ISLAND_VEGETATION),
			List.of(CountPlacement.of(3), InSquarePlacement.spread(), PlacementUtils.HEIGHTMAP, BiomeFilter.biome())));
	
	public static final RegistryObject<PlacedFeature> SPARSE_JUNGLE_GRASS_PATCH = REGISTER.register("sparse_jungle_grass_patch", () -> new PlacedFeature(Holder.hackyErase(VegetationFeatures.PATCH_GRASS_JUNGLE),
			List.of(RarityFilter.onAverageOnceEvery(15), InSquarePlacement.spread(), PlacementUtils.HEIGHTMAP, BiomeFilter.biome())));
	public static final RegistryObject<PlacedFeature> STRAWBERRY_PATCH = REGISTER.register("strawberry_patch", () -> new PlacedFeature(MSCFeatures.STRAWBERRY_PATCH.getHolder().orElseThrow(),
			List.of(InSquarePlacement.spread(), PlacementUtils.HEIGHTMAP_WORLD_SURFACE, BiomeFilter.biome())));
	public static final RegistryObject<PlacedFeature> RARE_STRAWBERRY_PATCH = REGISTER.register("rare_strawberry_patch", () -> new PlacedFeature(MSCFeatures.STRAWBERRY_PATCH.getHolder().orElseThrow(),
			List.of(RarityFilter.onAverageOnceEvery(60), InSquarePlacement.spread(), PlacementUtils.HEIGHTMAP_WORLD_SURFACE, BiomeFilter.biome())));
	public static final RegistryObject<PlacedFeature> GLOWING_MUSHROOM_PATCH = REGISTER.register("glowing_mushroom_patch", () -> new PlacedFeature(MSCFeatures.GLOWING_MUSHROOM_PATCH.getHolder().orElseThrow(),
			List.of(RarityFilter.onAverageOnceEvery(2), InSquarePlacement.spread(), PlacementUtils.HEIGHTMAP, BiomeFilter.biome())));
	public static final RegistryObject<PlacedFeature> SPARSE_GLOWING_MUSHROOM_PATCH = REGISTER.register("sparse_glowing_mushroom_patch", () -> new PlacedFeature(MSCFeatures.GLOWING_MUSHROOM_PATCH.getHolder().orElseThrow(),
			List.of(RarityFilter.onAverageOnceEvery(4), InSquarePlacement.spread(), PlacementUtils.HEIGHTMAP, BiomeFilter.biome())));
	public static final RegistryObject<PlacedFeature> BROWN_MUSHROOM_PATCH = REGISTER.register("brown_mushroom_patch", () -> new PlacedFeature(Holder.hackyErase(VegetationFeatures.PATCH_BROWN_MUSHROOM),
			List.of(RarityFilter.onAverageOnceEvery(120), InSquarePlacement.spread(), PlacementUtils.HEIGHTMAP, BiomeFilter.biome())));
	public static final RegistryObject<PlacedFeature> RED_MUSHROOM_PATCH = REGISTER.register("red_mushroom_patch", () -> new PlacedFeature(Holder.hackyErase(VegetationFeatures.PATCH_RED_MUSHROOM),
			List.of(RarityFilter.onAverageOnceEvery(120), InSquarePlacement.spread(), PlacementUtils.HEIGHTMAP, BiomeFilter.biome())));
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
	public static final RegistryObject<PlacedFeature> WATERLILY_PATCH = REGISTER.register("waterlily_patch", () -> new PlacedFeature(Holder.hackyErase(VegetationFeatures.PATCH_WATERLILY),
			List.of(RarityFilter.onAverageOnceEvery(60), InSquarePlacement.spread(), PlacementUtils.HEIGHTMAP, BiomeFilter.biome())));
	
	public static final RegistryObject<PlacedFeature> PUMPKIN = REGISTER.register("pumpkin", () -> new PlacedFeature(MSCFeatures.PUMPKIN.getHolder().orElseThrow(),
			List.of(RarityFilter.onAverageOnceEvery(128), InSquarePlacement.spread(), PlacementUtils.HEIGHTMAP, BiomeFilter.biome())));
	
	public static final RegistryObject<PlacedFeature> RABBIT_PLACEMENT = REGISTER.register("rabbit_placement", () -> new PlacedFeature(MSCFeatures.RABBIT_PLACEMENT.getHolder().orElseThrow(),
			List.of(RarityFilter.onAverageOnceEvery(5), CountPlacement.of(6), InSquarePlacement.spread(), PlacementUtils.HEIGHTMAP, BiomeFilter.biome())));
	public static final RegistryObject<PlacedFeature> SMALL_RABBIT_PLACEMENT = REGISTER.register("small_rabbit_placement", () -> new PlacedFeature(MSCFeatures.RABBIT_PLACEMENT.getHolder().orElseThrow(),
			List.of(RarityFilter.onAverageOnceEvery(5), CountPlacement.of(3), InSquarePlacement.spread(), PlacementUtils.HEIGHTMAP, BiomeFilter.biome())));
	
}