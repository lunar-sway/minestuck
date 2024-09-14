package com.mraof.minestuck.data.loot_table;

import com.mraof.minestuck.Minestuck;
import com.mraof.minestuck.blockentity.ItemStackBlockEntity;
import com.mraof.minestuck.blockentity.TransportalizerBlockEntity;
import com.mraof.minestuck.data.AspectTreeBlocksData;
import com.mraof.minestuck.data.SkaiaBlocksData;
import com.mraof.minestuck.item.MSItems;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.advancements.critereon.EnchantmentPredicate;
import net.minecraft.advancements.critereon.ItemPredicate;
import net.minecraft.advancements.critereon.MinMaxBounds;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.loot.BlockLootSubProvider;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.FlowerPotBlock;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.DynamicLoot;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.functions.ApplyBonusCount;
import net.minecraft.world.level.storage.loot.functions.CopyNbtFunction;
import net.minecraft.world.level.storage.loot.functions.SetItemCountFunction;
import net.minecraft.world.level.storage.loot.predicates.BonusLevelTableCondition;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraft.world.level.storage.loot.predicates.MatchTool;
import net.minecraft.world.level.storage.loot.providers.nbt.ContextNbtProvider;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;
import net.minecraft.world.level.storage.loot.providers.number.UniformGenerator;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;

import static com.mraof.minestuck.block.MSBlocks.*;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public final class MSBlockLootTables extends BlockLootSubProvider
{
	private static final LootItemCondition.Builder SILK_TOUCH_CONDITION = MatchTool.toolMatches(ItemPredicate.Builder.item().hasEnchantment(new EnchantmentPredicate(Enchantments.SILK_TOUCH, MinMaxBounds.Ints.atLeast(1))));
	private static final LootItemCondition.Builder SHEAR_CONDITION = MatchTool.toolMatches(ItemPredicate.Builder.item().of(Items.SHEARS));
	private static final LootItemCondition.Builder SILK_AND_SHEAR_CONDITION = SHEAR_CONDITION.or(SILK_TOUCH_CONDITION);
	private static final LootItemCondition.Builder NO_SILK_OR_SHEAR_CONDITION = SILK_AND_SHEAR_CONDITION.invert();
	public static final float[] SAPLING_CHANCES = new float[]{0.05F, 0.0625F, 0.083333336F, 0.1F};
	
	MSBlockLootTables()
	{
		super(Set.of(), FeatureFlags.REGISTRY.allFlags());
	}
	
	@Override
	protected void generate()
	{
		SkaiaBlocksData.addLootTables(this);
		AspectTreeBlocksData.addLootTables(this);
		
		add(CARVED_SIGN.get(), block ->
				createSingleItemTable(MSItems.CARVED_SIGN.get()));
		add(CARVED_WALL_SIGN.get(), block ->
				createSingleItemTable(MSItems.CARVED_SIGN.get()));
		add(CARVED_HANGING_SIGN.get(), block ->
				createSingleItemTable(MSItems.CARVED_HANGING_SIGN.get()));
		add(CARVED_WALL_HANGING_SIGN.get(), block ->
				createSingleItemTable(MSItems.CARVED_HANGING_SIGN.get()));
		
		add(DEAD_SIGN.get(), block ->
				createSingleItemTable(MSItems.DEAD_SIGN.get()));
		add(DEAD_WALL_SIGN.get(), block ->
				createSingleItemTable(MSItems.DEAD_SIGN.get()));
		add(DEAD_HANGING_SIGN.get(), block ->
				createSingleItemTable(MSItems.DEAD_HANGING_SIGN.get()));
		add(DEAD_WALL_HANGING_SIGN.get(), block ->
				createSingleItemTable(MSItems.DEAD_HANGING_SIGN.get()));
		
		add(END_SIGN.get(), block ->
				createSingleItemTable(MSItems.END_SIGN.get()));
		add(END_WALL_SIGN.get(), block ->
				createSingleItemTable(MSItems.END_SIGN.get()));
		add(END_HANGING_SIGN.get(), block ->
				createSingleItemTable(MSItems.END_HANGING_SIGN.get()));
		add(END_WALL_HANGING_SIGN.get(), block ->
				createSingleItemTable(MSItems.END_HANGING_SIGN.get()));
		
		add(FROST_SIGN.get(), block ->
				createSingleItemTable(MSItems.FROST_SIGN.get()));
		add(FROST_WALL_SIGN.get(), block ->
				createSingleItemTable(MSItems.FROST_SIGN.get()));
		add(FROST_HANGING_SIGN.get(), block ->
				createSingleItemTable(MSItems.FROST_HANGING_SIGN.get()));
		add(FROST_WALL_HANGING_SIGN.get(), block ->
				createSingleItemTable(MSItems.FROST_HANGING_SIGN.get()));
		
		add(GLOWING_SIGN.get(), block ->
				createSingleItemTable(MSItems.GLOWING_SIGN.get()));
		add(GLOWING_WALL_SIGN.get(), block ->
				createSingleItemTable(MSItems.GLOWING_SIGN.get()));
		add(GLOWING_HANGING_SIGN.get(), block ->
				createSingleItemTable(MSItems.GLOWING_HANGING_SIGN.get()));
		add(GLOWING_WALL_HANGING_SIGN.get(), block ->
				createSingleItemTable(MSItems.GLOWING_HANGING_SIGN.get()));
		
		add(RAINBOW_SIGN.get(), block ->
				createSingleItemTable(MSItems.RAINBOW_SIGN.get()));
		add(RAINBOW_WALL_SIGN.get(), block ->
				createSingleItemTable(MSItems.RAINBOW_SIGN.get()));
		add(RAINBOW_HANGING_SIGN.get(), block ->
				createSingleItemTable(MSItems.RAINBOW_HANGING_SIGN.get()));
		add(RAINBOW_WALL_HANGING_SIGN.get(), block ->
				createSingleItemTable(MSItems.RAINBOW_HANGING_SIGN.get()));
		
		add(SHADEWOOD_SIGN.get(), block ->
				createSingleItemTable(MSItems.SHADEWOOD_SIGN.get()));
		add(SHADEWOOD_WALL_SIGN.get(), block ->
				createSingleItemTable(MSItems.SHADEWOOD_SIGN.get()));
		add(SHADEWOOD_HANGING_SIGN.get(), block ->
				createSingleItemTable(MSItems.SHADEWOOD_HANGING_SIGN.get()));
		add(SHADEWOOD_WALL_HANGING_SIGN.get(), block ->
				createSingleItemTable(MSItems.SHADEWOOD_HANGING_SIGN.get()));
		
		add(TREATED_SIGN.get(), block ->
				createSingleItemTable(MSItems.TREATED_SIGN.get()));
		add(TREATED_WALL_SIGN.get(), block ->
				createSingleItemTable(MSItems.TREATED_SIGN.get()));
		add(TREATED_HANGING_SIGN.get(), block ->
				createSingleItemTable(MSItems.TREATED_HANGING_SIGN.get()));
		add(TREATED_WALL_HANGING_SIGN.get(), block ->
				createSingleItemTable(MSItems.TREATED_HANGING_SIGN.get()));
		
		add(LACQUERED_SIGN.get(), block ->
				createSingleItemTable(MSItems.LACQUERED_SIGN.get()));
		add(LACQUERED_WALL_SIGN.get(), block ->
				createSingleItemTable(MSItems.LACQUERED_SIGN.get()));
		add(LACQUERED_HANGING_SIGN.get(), block ->
				createSingleItemTable(MSItems.LACQUERED_HANGING_SIGN.get()));
		add(LACQUERED_WALL_HANGING_SIGN.get(), block ->
				createSingleItemTable(MSItems.LACQUERED_HANGING_SIGN.get()));
		
		add(PERFECTLY_GENERIC_SIGN.get(), block ->
				createSingleItemTable(MSItems.PERFECTLY_GENERIC_SIGN.get()));
		add(PERFECTLY_GENERIC_WALL_SIGN.get(), block ->
				createSingleItemTable(MSItems.PERFECTLY_GENERIC_SIGN.get()));
		add(PERFECTLY_GENERIC_HANGING_SIGN.get(), block ->
				createSingleItemTable(MSItems.PERFECTLY_GENERIC_HANGING_SIGN.get()));
		add(PERFECTLY_GENERIC_WALL_HANGING_SIGN.get(), block ->
				createSingleItemTable(MSItems.PERFECTLY_GENERIC_HANGING_SIGN.get()));
		
		add(CINDERED_SIGN.get(), block ->
				createSingleItemTable(MSItems.CINDERED_SIGN.get()));
		add(CINDERED_WALL_SIGN.get(), block ->
				createSingleItemTable(MSItems.CINDERED_SIGN.get()));
		add(CINDERED_HANGING_SIGN.get(), block ->
				createSingleItemTable(MSItems.CINDERED_HANGING_SIGN.get()));
		add(CINDERED_WALL_HANGING_SIGN.get(), block ->
				createSingleItemTable(MSItems.CINDERED_HANGING_SIGN.get()));
		
		dropSelf(STRIPPED_GLOWING_LOG.get());
		dropSelf(STRIPPED_FROST_LOG.get());
		dropSelf(STRIPPED_RAINBOW_LOG.get());
		dropSelf(STRIPPED_END_LOG.get());
		dropSelf(STRIPPED_DEAD_LOG.get());
		dropSelf(STRIPPED_GLOWING_WOOD.get());
		dropSelf(STRIPPED_CINDERED_LOG.get());
		dropSelf(STRIPPED_FROST_WOOD.get());
		dropSelf(STRIPPED_RAINBOW_WOOD.get());
		dropSelf(STRIPPED_END_WOOD.get());
		dropSelf(STRIPPED_DEAD_WOOD.get());
		dropSelf(STRIPPED_CINDERED_WOOD.get());
		
		dropSelf(FROST_SAPLING.get());
		
		add(STONE_CRUXITE_ORE.get(), this::cruxiteOreDrop);
		add(COBBLESTONE_CRUXITE_ORE.get(), this::cruxiteOreDrop);
		add(SANDSTONE_CRUXITE_ORE.get(), this::cruxiteOreDrop);
		add(RED_SANDSTONE_CRUXITE_ORE.get(), this::cruxiteOreDrop);
		add(NETHERRACK_CRUXITE_ORE.get(), this::cruxiteOreDrop);
		add(END_STONE_CRUXITE_ORE.get(), this::cruxiteOreDrop);
		add(SHADE_STONE_CRUXITE_ORE.get(), this::cruxiteOreDrop);
		add(PINK_STONE_CRUXITE_ORE.get(), this::cruxiteOreDrop);
		add(MYCELIUM_STONE_CRUXITE_ORE.get(), this::cruxiteOreDrop);
		add(UNCARVED_WOOD_CRUXITE_ORE.get(), this::cruxiteOreDrop);
		add(BLACK_STONE_CRUXITE_ORE.get(), this::cruxiteOreDrop);
		
		add(STONE_URANIUM_ORE.get(), this::uraniumOreDrop);
		add(DEEPSLATE_URANIUM_ORE.get(), this::uraniumOreDrop);
		add(COBBLESTONE_URANIUM_ORE.get(), this::uraniumOreDrop);
		add(SANDSTONE_URANIUM_ORE.get(), this::uraniumOreDrop);
		add(RED_SANDSTONE_URANIUM_ORE.get(), this::uraniumOreDrop);
		add(NETHERRACK_URANIUM_ORE.get(), this::uraniumOreDrop);
		add(END_STONE_URANIUM_ORE.get(), this::uraniumOreDrop);
		add(SHADE_STONE_URANIUM_ORE.get(), this::uraniumOreDrop);
		add(PINK_STONE_URANIUM_ORE.get(), this::uraniumOreDrop);
		add(MYCELIUM_STONE_URANIUM_ORE.get(), this::uraniumOreDrop);
		add(UNCARVED_WOOD_URANIUM_ORE.get(), this::uraniumOreDrop);
		add(BLACK_STONE_URANIUM_ORE.get(), this::uraniumOreDrop);
		
		add(NETHERRACK_COAL_ORE.get(), this::coalOreDrop);
		add(SHADE_STONE_COAL_ORE.get(), this::coalOreDrop);
		add(PINK_STONE_COAL_ORE.get(), this::coalOreDrop);
		
		add(SANDSTONE_IRON_ORE.get(), this::ironOreDrop);
		add(RED_SANDSTONE_IRON_ORE.get(), this::ironOreDrop);
		add(END_STONE_IRON_ORE.get(), this::ironOreDrop);
		add(UNCARVED_WOOD_IRON_ORE.get(), this::ironOreDrop);
		
		add(SANDSTONE_GOLD_ORE.get(), this::goldOreDrop);
		add(RED_SANDSTONE_GOLD_ORE.get(), this::goldOreDrop);
		add(SHADE_STONE_GOLD_ORE.get(), this::goldOreDrop);
		add(PINK_STONE_GOLD_ORE.get(), this::goldOreDrop);
		add(BLACK_STONE_GOLD_ORE.get(), this::goldOreDrop);
		
		add(END_STONE_REDSTONE_ORE.get(), this::redstoneOreDrop);
		add(UNCARVED_WOOD_REDSTONE_ORE.get(), this::redstoneOreDrop);
		add(BLACK_STONE_REDSTONE_ORE.get(), this::redstoneOreDrop);
		
		add(STONE_QUARTZ_ORE.get(), this::quartzOreDrop);
		add(BLACK_STONE_QUARTZ_ORE.get(), this::quartzOreDrop);
		
		add(PINK_STONE_LAPIS_ORE.get(), this::lapisOreDrop);
		
		add(PINK_STONE_DIAMOND_ORE.get(), this::diamondOreDrop);
		
		add(UNCARVED_WOOD_EMERALD_ORE.get(), this::emeraldOreDrop);
		
		dropSelf(CRUXITE_BLOCK.get());
		dropSelf(CRUXITE_STAIRS.get());
		add(CRUXITE_SLAB.get(), this::createSlabItemTable);
		dropSelf(CRUXITE_WALL.get());
		dropSelf(CRUXITE_BUTTON.get());
		dropSelf(CRUXITE_PRESSURE_PLATE.get());
		add(CRUXITE_DOOR.get(), this::createDoorTable);
		dropSelf(CRUXITE_TRAPDOOR.get());
		dropSelf(POLISHED_CRUXITE_BLOCK.get());
		dropSelf(POLISHED_CRUXITE_STAIRS.get());
		add(POLISHED_CRUXITE_SLAB.get(), this::createSlabItemTable);
		dropSelf(POLISHED_CRUXITE_WALL.get());
		dropSelf(CRUXITE_BRICKS.get());
		dropSelf(CRUXITE_BRICK_STAIRS.get());
		add(CRUXITE_BRICK_SLAB.get(), this::createSlabItemTable);
		dropSelf(CRUXITE_BRICK_WALL.get());
		dropSelf(SMOOTH_CRUXITE_BLOCK.get());
		dropSelf(CHISELED_CRUXITE_BLOCK.get());
		dropSelf(CRUXITE_PILLAR.get());
		dropSelf(CRUXITE_LAMP.get());
		
		dropSelf(URANIUM_BLOCK.get());
		dropSelf(URANIUM_STAIRS.get());
		add(URANIUM_SLAB.get(), this::createSlabItemTable);
		dropSelf(URANIUM_WALL.get());
		dropSelf(URANIUM_BUTTON.get());
		dropSelf(URANIUM_PRESSURE_PLATE.get());
		
		dropSelf(GENERIC_OBJECT.get());
		dropSelf(PERFECTLY_GENERIC_STAIRS.get());
		add(PERFECTLY_GENERIC_SLAB.get(), this::createSlabItemTable);
		dropSelf(PERFECTLY_GENERIC_WALL.get());
		dropSelf(PERFECTLY_GENERIC_FENCE.get());
		dropSelf(PERFECTLY_GENERIC_FENCE_GATE.get());
		dropSelf(PERFECTLY_GENERIC_BUTTON.get());
		dropSelf(PERFECTLY_GENERIC_PRESSURE_PLATE.get());
		add(PERFECTLY_GENERIC_DOOR.get(), this::createDoorTable);
		dropSelf(PERFECTLY_GENERIC_TRAPDOOR.get());
		
		dropSelf(BLUE_DIRT.get());
		dropSelf(THOUGHT_DIRT.get());
		
		dropSelf(COARSE_STONE.get());
		dropSelf(COARSE_STONE_WALL.get());
		dropSelf(COARSE_STONE_BUTTON.get());
		dropSelf(COARSE_STONE_PRESSURE_PLATE.get());
		
		dropSelf(CHISELED_COARSE_STONE.get());
		
		dropSelf(COARSE_STONE_BRICKS.get());
		dropSelf(COARSE_STONE_BRICK_WALL.get());
		
		dropSelf(COARSE_STONE_COLUMN.get());
		dropSelf(CHISELED_COARSE_STONE_BRICKS.get());
		dropSelf(CRACKED_COARSE_STONE_BRICKS.get());
		dropSelf(MOSSY_COARSE_STONE_BRICKS.get());
		
		dropSelf(SHADE_STONE.get());
		dropSelf(SHADE_WALL.get());
		dropSelf(SHADE_BUTTON.get());
		dropSelf(SHADE_PRESSURE_PLATE.get());
		
		dropSelf(SMOOTH_SHADE_STONE.get());
		dropSelf(SMOOTH_SHADE_STONE_STAIRS.get());
		add(SMOOTH_SHADE_STONE_SLAB.get(), this::createSlabItemTable);
		dropSelf(SMOOTH_SHADE_STONE_WALL.get());
		
		dropSelf(SHADE_BRICKS.get());
		dropSelf(SHADE_BRICK_WALL.get());
		
		dropSelf(SHADE_COLUMN.get());
		dropSelf(CHISELED_SHADE_BRICKS.get());
		dropSelf(CRACKED_SHADE_BRICKS.get());
		
		dropSelf(MOSSY_SHADE_BRICKS.get());
		dropSelf(MOSSY_SHADE_BRICK_STAIRS.get());
		add(MOSSY_SHADE_BRICK_SLAB.get(), this::createSlabItemTable);
		dropSelf(MOSSY_SHADE_BRICK_WALL.get());
		
		dropSelf(BLOOD_SHADE_BRICKS.get());
		dropSelf(BLOOD_SHADE_BRICK_STAIRS.get());
		add(BLOOD_SHADE_BRICK_SLAB.get(), this::createSlabItemTable);
		dropSelf(BLOOD_SHADE_BRICK_WALL.get());
		
		dropSelf(TAR_SHADE_BRICKS.get());
		dropSelf(TAR_SHADE_BRICK_STAIRS.get());
		add(TAR_SHADE_BRICK_SLAB.get(), this::createSlabItemTable);
		dropSelf(TAR_SHADE_BRICK_WALL.get());
		
		dropSelf(FROST_TILE.get());
		dropSelf(FROST_TILE_WALL.get());
		
		dropSelf(CHISELED_FROST_TILE.get());
		
		dropSelf(FROST_BRICKS.get());
		dropSelf(FROST_BRICK_WALL.get());
		
		dropSelf(FROST_COLUMN.get());
		dropSelf(CHISELED_FROST_BRICKS.get());
		dropSelf(CRACKED_FROST_BRICKS.get());
		
		dropSelf(FLOWERY_FROST_BRICKS.get());
		dropSelf(FLOWERY_FROST_BRICK_STAIRS.get());
		add(FLOWERY_FROST_BRICK_SLAB.get(), this::createSlabItemTable);
		dropSelf(FLOWERY_FROST_BRICK_WALL.get());
		
		dropSelf(CAST_IRON.get());
		dropSelf(CAST_IRON_STAIRS.get());
		add(CAST_IRON_SLAB.get(), this::createSlabItemTable);
		dropSelf(CAST_IRON_WALL.get());
		dropSelf(CAST_IRON_BUTTON.get());
		dropSelf(CAST_IRON_PRESSURE_PLATE.get());
		
		dropSelf(CAST_IRON_TILE.get());
		dropSelf(CAST_IRON_TILE_STAIRS.get());
		add(CAST_IRON_TILE_SLAB.get(), this::createSlabItemTable);
		
		dropSelf(CAST_IRON_SHEET.get());
		dropSelf(CAST_IRON_SHEET_STAIRS.get());
		add(CAST_IRON_SHEET_SLAB.get(), this::createSlabItemTable);
		
		dropSelf(CHISELED_CAST_IRON.get());
		dropSelf(CAST_IRON_FRAME.get());
		
		dropSelf(STEEL_BEAM.get());
		
		dropSelf(MYCELIUM_COBBLESTONE.get());
		dropSelf(MYCELIUM_COBBLESTONE_STAIRS.get());
		add(MYCELIUM_COBBLESTONE_SLAB.get(), this::createSlabItemTable);
		dropSelf(MYCELIUM_COBBLESTONE_WALL.get());
		
		add(MYCELIUM_STONE.get(), createSingleItemTableWithSilkTouch(MYCELIUM_STONE.get(), MYCELIUM_COBBLESTONE.get()));
		dropSelf(MYCELIUM_STONE_WALL.get());
		dropSelf(MYCELIUM_STONE_BUTTON.get());
		dropSelf(MYCELIUM_STONE_PRESSURE_PLATE.get());
		
		dropSelf(POLISHED_MYCELIUM_STONE.get());
		dropSelf(POLISHED_MYCELIUM_STONE_STAIRS.get());
		add(POLISHED_MYCELIUM_STONE_SLAB.get(), this::createSlabItemTable);
		dropSelf(POLISHED_MYCELIUM_STONE_WALL.get());
		
		dropSelf(MYCELIUM_BRICKS.get());
		dropSelf(MYCELIUM_BRICK_WALL.get());
		
		dropSelf(MYCELIUM_COLUMN.get());
		dropSelf(CHISELED_MYCELIUM_BRICKS.get());
		dropSelf(SUSPICIOUS_CHISELED_MYCELIUM_BRICKS.get());
		dropSelf(CRACKED_MYCELIUM_BRICKS.get());
		
		dropSelf(MOSSY_MYCELIUM_BRICKS.get());
		dropSelf(MOSSY_MYCELIUM_BRICK_STAIRS.get());
		add(MOSSY_MYCELIUM_BRICK_SLAB.get(), this::createSlabItemTable);
		dropSelf(MOSSY_MYCELIUM_BRICK_WALL.get());
		
		dropSelf(FLOWERY_MYCELIUM_BRICKS.get());
		dropSelf(FLOWERY_MYCELIUM_BRICK_STAIRS.get());
		add(FLOWERY_MYCELIUM_BRICK_SLAB.get(), this::createSlabItemTable);
		dropSelf(FLOWERY_MYCELIUM_BRICK_WALL.get());
		
		add(BLACK_STONE.get(), createSingleItemTableWithSilkTouch(BLACK_STONE.get(), BLACK_COBBLESTONE.get()));
		dropSelf(BLACK_STONE_STAIRS.get());
		add(BLACK_STONE_SLAB.get(), this::createSlabItemTable);
		dropSelf(BLACK_STONE_WALL.get());
		dropSelf(BLACK_STONE_BUTTON.get());
		dropSelf(BLACK_STONE_PRESSURE_PLATE.get());
		
		dropSelf(BLACK_COBBLESTONE.get());
		dropSelf(BLACK_COBBLESTONE_STAIRS.get());
		add(BLACK_COBBLESTONE_SLAB.get(), this::createSlabItemTable);
		dropSelf(BLACK_COBBLESTONE_WALL.get());
		
		dropSelf(POLISHED_BLACK_STONE.get());
		dropSelf(POLISHED_BLACK_STONE_STAIRS.get());
		add(POLISHED_BLACK_STONE_SLAB.get(), this::createSlabItemTable);
		dropSelf(POLISHED_BLACK_STONE_WALL.get());
		
		dropSelf(BLACK_STONE_BRICKS.get());
		dropSelf(BLACK_STONE_BRICK_STAIRS.get());
		add(BLACK_STONE_BRICK_SLAB.get(), this::createSlabItemTable);
		dropSelf(BLACK_STONE_BRICK_WALL.get());
		
		dropSelf(BLACK_STONE_COLUMN.get());
		dropSelf(CHISELED_BLACK_STONE_BRICKS.get());
		dropSelf(CRACKED_BLACK_STONE_BRICKS.get());
		
		dropSelf(MAGMATIC_BLACK_STONE_BRICKS.get());
		dropSelf(MAGMATIC_BLACK_STONE_BRICK_STAIRS.get());
		add(MAGMATIC_BLACK_STONE_BRICK_SLAB.get(), this::createSlabItemTable);
		dropSelf(MAGMATIC_BLACK_STONE_BRICK_WALL.get());
		
		dropSelf(BLACK_SAND.get());
		
		dropSelf(IGNEOUS_STONE.get());
		dropSelf(IGNEOUS_STONE_STAIRS.get());
		add(IGNEOUS_STONE_SLAB.get(), this::createSlabItemTable);
		dropSelf(IGNEOUS_STONE_WALL.get());
		dropSelf(IGNEOUS_STONE_BUTTON.get());
		dropSelf(IGNEOUS_STONE_PRESSURE_PLATE.get());
		
		dropSelf(POLISHED_IGNEOUS_STONE.get());
		dropSelf(POLISHED_IGNEOUS_STAIRS.get());
		add(POLISHED_IGNEOUS_SLAB.get(), this::createSlabItemTable);
		dropSelf(POLISHED_IGNEOUS_WALL.get());
		
		dropSelf(POLISHED_IGNEOUS_BRICKS.get());
		dropSelf(POLISHED_IGNEOUS_BRICK_STAIRS.get());
		add(POLISHED_IGNEOUS_BRICK_SLAB.get(), this::createSlabItemTable);
		dropSelf(POLISHED_IGNEOUS_BRICK_WALL.get());
		
		dropSelf(POLISHED_IGNEOUS_PILLAR.get());
		dropSelf(CHISELED_IGNEOUS_STONE.get());
		dropSelf(CRACKED_POLISHED_IGNEOUS_BRICKS.get());
		
		dropSelf(MAGMATIC_POLISHED_IGNEOUS_BRICKS.get());
		dropSelf(MAGMATIC_POLISHED_IGNEOUS_BRICK_STAIRS.get());
		add(MAGMATIC_POLISHED_IGNEOUS_BRICK_SLAB.get(), this::createSlabItemTable);
		dropSelf(MAGMATIC_POLISHED_IGNEOUS_BRICK_WALL.get());
		
		dropSelf(MAGMATIC_IGNEOUS_STONE.get());
		
		dropSelf(PUMICE_STONE.get());
		dropSelf(PUMICE_STONE_STAIRS.get());
		add(PUMICE_STONE_SLAB.get(), this::createSlabItemTable);
		dropSelf(PUMICE_STONE_WALL.get());
		dropSelf(PUMICE_STONE_BUTTON.get());
		dropSelf(PUMICE_STONE_PRESSURE_PLATE.get());
		
		dropSelf(PUMICE_BRICKS.get());
		dropSelf(PUMICE_BRICK_STAIRS.get());
		add(PUMICE_BRICK_SLAB.get(), this::createSlabItemTable);
		dropSelf(PUMICE_BRICK_WALL.get());
		
		dropSelf(PUMICE_TILES.get());
		dropSelf(PUMICE_TILE_STAIRS.get());
		add(PUMICE_TILE_SLAB.get(), this::createSlabItemTable);
		dropSelf(PUMICE_TILE_WALL.get());
		
		dropSelf(HEAT_LAMP.get());
		
		dropSelf(DECREPIT_STONE_BRICKS.get());
		dropSelf(DECREPIT_STONE_BRICK_STAIRS.get());
		add(DECREPIT_STONE_BRICK_SLAB.get(), this::createSlabItemTable);
		dropSelf(DECREPIT_STONE_BRICK_WALL.get());
		
		dropSelf(FLOWERY_MOSSY_COBBLESTONE.get());
		dropSelf(FLOWERY_MOSSY_COBBLESTONE_STAIRS.get());
		add(FLOWERY_MOSSY_COBBLESTONE_SLAB.get(), this::createSlabItemTable);
		dropSelf(FLOWERY_MOSSY_COBBLESTONE_WALL.get());
		
		dropSelf(MOSSY_DECREPIT_STONE_BRICKS.get());
		dropSelf(MOSSY_DECREPIT_STONE_BRICK_STAIRS.get());
		add(MOSSY_DECREPIT_STONE_BRICK_SLAB.get(), this::createSlabItemTable);
		dropSelf(MOSSY_DECREPIT_STONE_BRICK_WALL.get());
		
		dropSelf(FLOWERY_MOSSY_STONE_BRICKS.get());
		dropSelf(FLOWERY_MOSSY_STONE_BRICK_STAIRS.get());
		add(FLOWERY_MOSSY_STONE_BRICK_SLAB.get(), this::createSlabItemTable);
		dropSelf(FLOWERY_MOSSY_STONE_BRICK_WALL.get());
		
		dropSelf(COARSE_END_STONE.get());
		add(END_GRASS.get(), this::endGrassDrop);
		
		dropSelf(CHALK.get());
		dropSelf(CHALK_WALL.get());
		dropSelf(CHALK_BUTTON.get());
		dropSelf(CHALK_PRESSURE_PLATE.get());
		
		dropSelf(POLISHED_CHALK.get());
		dropSelf(POLISHED_CHALK_STAIRS.get());
		add(POLISHED_CHALK_SLAB.get(), this::createSlabItemTable);
		dropSelf(POLISHED_CHALK_WALL.get());
		
		dropSelf(CHALK_BRICKS.get());
		dropSelf(CHALK_BRICK_WALL.get());
		
		dropSelf(CHALK_COLUMN.get());
		dropSelf(CHISELED_CHALK_BRICKS.get());
		
		dropSelf(MOSSY_CHALK_BRICKS.get());
		dropSelf(MOSSY_CHALK_BRICK_STAIRS.get());
		add(MOSSY_CHALK_BRICK_SLAB.get(), this::createSlabItemTable);
		dropSelf(MOSSY_CHALK_BRICK_WALL.get());
		
		dropSelf(FLOWERY_CHALK_BRICKS.get());
		dropSelf(FLOWERY_CHALK_BRICK_STAIRS.get());
		add(FLOWERY_CHALK_BRICK_SLAB.get(), this::createSlabItemTable);
		dropSelf(FLOWERY_CHALK_BRICK_WALL.get());
		
		dropSelf(PINK_STONE.get());
		dropSelf(PINK_STONE_WALL.get());
		dropSelf(PINK_STONE_BUTTON.get());
		dropSelf(PINK_STONE_PRESSURE_PLATE.get());
		
		dropSelf(POLISHED_PINK_STONE.get());
		dropSelf(POLISHED_PINK_STONE_STAIRS.get());
		add(POLISHED_PINK_STONE_SLAB.get(), this::createSlabItemTable);
		dropSelf(POLISHED_PINK_STONE_WALL.get());
		
		dropSelf(PINK_STONE_BRICKS.get());
		dropSelf(PINK_STONE_BRICK_WALL.get());
		
		dropSelf(CHISELED_PINK_STONE_BRICKS.get());
		dropSelf(CRACKED_PINK_STONE_BRICKS.get());
		
		dropSelf(MOSSY_PINK_STONE_BRICKS.get());
		dropSelf(MOSSY_PINK_STONE_BRICK_STAIRS.get());
		add(MOSSY_PINK_STONE_BRICK_SLAB.get(), this::createSlabItemTable);
		dropSelf(MOSSY_PINK_STONE_BRICK_WALL.get());
		
		dropSelf(PINK_STONE_COLUMN.get());
		
		dropSelf(BROWN_STONE.get());
		dropSelf(BROWN_STONE_WALL.get());
		dropSelf(BROWN_STONE_BUTTON.get());
		dropSelf(BROWN_STONE_PRESSURE_PLATE.get());
		
		dropSelf(POLISHED_BROWN_STONE.get());
		dropSelf(POLISHED_BROWN_STONE_STAIRS.get());
		add(POLISHED_BROWN_STONE_SLAB.get(), this::createSlabItemTable);
		dropSelf(POLISHED_BROWN_STONE_WALL.get());
		
		dropSelf(BROWN_STONE_BRICKS.get());
		dropSelf(BROWN_STONE_BRICK_WALL.get());
		
		dropSelf(BROWN_STONE_COLUMN.get());
		dropSelf(CRACKED_BROWN_STONE_BRICKS.get());
		
		dropSelf(GREEN_STONE.get());
		dropSelf(GREEN_STONE_WALL.get());
		dropSelf(GREEN_STONE_BUTTON.get());
		dropSelf(GREEN_STONE_PRESSURE_PLATE.get());
		
		dropSelf(POLISHED_GREEN_STONE.get());
		dropSelf(POLISHED_GREEN_STONE_STAIRS.get());
		add(POLISHED_GREEN_STONE_SLAB.get(), this::createSlabItemTable);
		dropSelf(POLISHED_GREEN_STONE_WALL.get());
		
		dropSelf(GREEN_STONE_BRICKS.get());
		dropSelf(GREEN_STONE_BRICK_WALL.get());
		
		dropSelf(GREEN_STONE_COLUMN.get());
		dropSelf(CHISELED_GREEN_STONE_BRICKS.get());
		
		dropSelf(HORIZONTAL_GREEN_STONE_BRICKS.get());
		dropSelf(HORIZONTAL_GREEN_STONE_BRICK_STAIRS.get());
		add(HORIZONTAL_GREEN_STONE_BRICK_SLAB.get(), this::createSlabItemTable);
		dropSelf(HORIZONTAL_GREEN_STONE_BRICK_WALL.get());
		
		dropSelf(VERTICAL_GREEN_STONE_BRICKS.get());
		dropSelf(VERTICAL_GREEN_STONE_BRICK_STAIRS.get());
		add(VERTICAL_GREEN_STONE_BRICK_SLAB.get(), this::createSlabItemTable);
		dropSelf(VERTICAL_GREEN_STONE_BRICK_WALL.get());
		
		dropSelf(GREEN_STONE_BRICK_EMBEDDED_LADDER.get());
		dropSelf(GREEN_STONE_BRICK_TRIM.get());
		dropSelf(GREEN_STONE_BRICK_FROG.get());
		dropSelf(GREEN_STONE_BRICK_IGUANA_LEFT.get());
		dropSelf(GREEN_STONE_BRICK_IGUANA_RIGHT.get());
		dropSelf(GREEN_STONE_BRICK_LOTUS.get());
		dropSelf(GREEN_STONE_BRICK_NAK_LEFT.get());
		dropSelf(GREEN_STONE_BRICK_NAK_RIGHT.get());
		dropSelf(GREEN_STONE_BRICK_SALAMANDER_LEFT.get());
		dropSelf(GREEN_STONE_BRICK_SALAMANDER_RIGHT.get());
		dropSelf(GREEN_STONE_BRICK_SKAIA.get());
		dropSelf(GREEN_STONE_BRICK_TURTLE.get());
		dropSelf(SANDSTONE_COLUMN.get());
		dropSelf(CHISELED_SANDSTONE_COLUMN.get());
		dropSelf(RED_SANDSTONE_COLUMN.get());
		dropSelf(CHISELED_RED_SANDSTONE_COLUMN.get());
		
		dropSelf(CARVED_LOG.get());
		dropSelf(CARVED_WOODEN_LEAF.get());
		
		add(UNCARVED_WOOD.get(), createSingleItemTableWithSilkTouch(UNCARVED_WOOD.get(), CHIPBOARD.get()));
		dropSelf(UNCARVED_WOOD_STAIRS.get());
		add(UNCARVED_WOOD_SLAB.get(), this::createSlabItemTable);
		dropSelf(UNCARVED_WOOD_BUTTON.get());
		dropSelf(UNCARVED_WOOD_PRESSURE_PLATE.get());
		dropSelf(UNCARVED_WOOD_FENCE.get());
		dropSelf(UNCARVED_WOOD_FENCE_GATE.get());
		
		dropSelf(CHIPBOARD.get());
		dropSelf(CHIPBOARD_STAIRS.get());
		add(CHIPBOARD_SLAB.get(), this::createSlabItemTable);
		dropSelf(CHIPBOARD_BUTTON.get());
		dropSelf(CHIPBOARD_PRESSURE_PLATE.get());
		dropSelf(CHIPBOARD_FENCE.get());
		dropSelf(CHIPBOARD_FENCE_GATE.get());
		
		dropSelf(WOOD_SHAVINGS.get());
		
		dropSelf(CARVED_HEAVY_PLANKS.get());
		dropSelf(CARVED_HEAVY_PLANK_STAIRS.get());
		add(CARVED_HEAVY_PLANK_SLAB.get(), this::createSlabItemTable);
		
		dropSelf(CARVED_PLANKS.get());
		dropSelf(CARVED_STAIRS.get());
		add(CARVED_SLAB.get(), this::createSlabItemTable);
		dropSelf(CARVED_BUTTON.get());
		dropSelf(CARVED_PRESSURE_PLATE.get());
		dropSelf(CARVED_FENCE.get());
		dropSelf(CARVED_FENCE_GATE.get());
		add(CARVED_DOOR.get(),this::createDoorTable);
		dropSelf(CARVED_TRAPDOOR.get());
		
		dropSelf(POLISHED_UNCARVED_WOOD.get());
		dropSelf(POLISHED_UNCARVED_STAIRS.get());
		add(POLISHED_UNCARVED_SLAB.get(), this::createSlabItemTable);
		
		dropSelf(CARVED_KNOTTED_WOOD.get());
		dropSelf(CARVED_BUSH.get());
		dropSelf(WOODEN_GRASS.get());
		
		add(TREATED_UNCARVED_WOOD.get(), createSingleItemTableWithSilkTouch(TREATED_UNCARVED_WOOD.get(), TREATED_CHIPBOARD.get()));
		dropSelf(TREATED_UNCARVED_WOOD_STAIRS.get());
		add(TREATED_UNCARVED_WOOD_SLAB.get(), this::createSlabItemTable);
		dropSelf(TREATED_UNCARVED_WOOD_BUTTON.get());
		dropSelf(TREATED_UNCARVED_WOOD_PRESSURE_PLATE.get());
		dropSelf(TREATED_UNCARVED_WOOD_FENCE.get());
		dropSelf(TREATED_UNCARVED_WOOD_FENCE_GATE.get());
		
		dropSelf(TREATED_CHIPBOARD.get());
		dropSelf(TREATED_CHIPBOARD_STAIRS.get());
		add(TREATED_CHIPBOARD_SLAB.get(), this::createSlabItemTable);
		dropSelf(TREATED_CHIPBOARD_BUTTON.get());
		dropSelf(TREATED_CHIPBOARD_PRESSURE_PLATE.get());
		dropSelf(TREATED_CHIPBOARD_FENCE.get());
		dropSelf(TREATED_CHIPBOARD_FENCE_GATE.get());
		
		dropSelf(TREATED_WOOD_SHAVINGS.get());
		
		dropSelf(TREATED_HEAVY_PLANKS.get());
		dropSelf(TREATED_HEAVY_PLANK_STAIRS.get());
		add(TREATED_HEAVY_PLANK_SLAB.get(), this::createSlabItemTable);
		
		dropSelf(TREATED_PLANKS.get());
		dropSelf(TREATED_PLANKS_STAIRS.get());
		add(TREATED_PLANKS_SLAB.get(), this::createSlabItemTable);
		dropSelf(TREATED_BUTTON.get());
		dropSelf(TREATED_PRESSURE_PLATE.get());
		dropSelf(TREATED_FENCE.get());
		dropSelf(TREATED_FENCE_GATE.get());
		add(TREATED_DOOR.get(), this::createDoorTable);
		dropSelf(TREATED_TRAPDOOR.get());
		
		dropSelf(POLISHED_TREATED_UNCARVED_WOOD.get());
		dropSelf(POLISHED_TREATED_UNCARVED_STAIRS.get());
		add(POLISHED_TREATED_UNCARVED_SLAB.get(), this::createSlabItemTable);
		
		dropSelf(TREATED_CARVED_KNOTTED_WOOD.get());
		dropSelf(TREATED_WOODEN_GRASS.get());
		
		add(LACQUERED_UNCARVED_WOOD.get(), createSingleItemTableWithSilkTouch(LACQUERED_UNCARVED_WOOD.get(), LACQUERED_CHIPBOARD.get()));
		dropSelf(LACQUERED_UNCARVED_WOOD_STAIRS.get());
		add(LACQUERED_UNCARVED_WOOD_SLAB.get(), this::createSlabItemTable);
		dropSelf(LACQUERED_UNCARVED_WOOD_BUTTON.get());
		dropSelf(LACQUERED_UNCARVED_WOOD_PRESSURE_PLATE.get());
		dropSelf(LACQUERED_UNCARVED_WOOD_FENCE.get());
		dropSelf(LACQUERED_UNCARVED_WOOD_FENCE_GATE.get());
		
		dropSelf(LACQUERED_CHIPBOARD.get());
		dropSelf(LACQUERED_CHIPBOARD_STAIRS.get());
		add(LACQUERED_CHIPBOARD_SLAB.get(), this::createSlabItemTable);
		dropSelf(LACQUERED_CHIPBOARD_BUTTON.get());
		dropSelf(LACQUERED_CHIPBOARD_PRESSURE_PLATE.get());
		dropSelf(LACQUERED_CHIPBOARD_FENCE.get());
		dropSelf(LACQUERED_CHIPBOARD_FENCE_GATE.get());
		
		dropSelf(LACQUERED_WOOD_SHAVINGS.get());
		
		dropSelf(LACQUERED_HEAVY_PLANKS.get());
		dropSelf(LACQUERED_HEAVY_PLANK_STAIRS.get());
		add(LACQUERED_HEAVY_PLANK_SLAB.get(), this::createSlabItemTable);
		
		dropSelf(LACQUERED_PLANKS.get());
		dropSelf(LACQUERED_STAIRS.get());
		add(LACQUERED_SLAB.get(), this::createSlabItemTable);
		dropSelf(LACQUERED_BUTTON.get());
		dropSelf(LACQUERED_PRESSURE_PLATE.get());
		dropSelf(LACQUERED_FENCE.get());
		dropSelf(LACQUERED_FENCE_GATE.get());
		add(LACQUERED_DOOR.get(), this::createDoorTable);
		dropSelf(LACQUERED_TRAPDOOR.get());
		
		dropSelf(POLISHED_LACQUERED_UNCARVED_WOOD.get());
		dropSelf(POLISHED_LACQUERED_UNCARVED_STAIRS.get());
		add(POLISHED_LACQUERED_UNCARVED_SLAB.get(), this::createSlabItemTable);
		
		dropSelf(LACQUERED_CARVED_KNOTTED_WOOD.get());
		dropSelf(LACQUERED_WOODEN_MUSHROOM.get());
		
		dropSelf(WOODEN_LAMP.get());
		
		dropSelf(DENSE_CLOUD.get());
		dropSelf(BRIGHT_DENSE_CLOUD.get());
		dropSelf(SUGAR_CUBE.get());
		dropSelf(SPIKES.get());
		dropSelf(NATIVE_SULFUR.get());
		
		dropSelf(GLOWING_LOG.get());
		dropSelf(FROST_LOG.get());
		dropSelf(RAINBOW_LOG.get());
		dropSelf(END_LOG.get());
		dropSelf(VINE_LOG.get());
		dropSelf(FLOWERY_VINE_LOG.get());
		dropSelf(DEAD_LOG.get());
		dropSelf(PETRIFIED_LOG.get());
		dropSelf(GLOWING_WOOD.get());
		dropSelf(SHADEWOOD_LOG.get());
		dropSelf(SCARRED_SHADEWOOD_LOG.get());
		dropSelf(ROTTED_SHADEWOOD_LOG.get());
		dropSelf(STRIPPED_SHADEWOOD_LOG.get());
		dropSelf(STRIPPED_SCARRED_SHADEWOOD_LOG.get());
		dropSelf(STRIPPED_ROTTED_SHADEWOOD_LOG.get());
		dropSelf(CINDERED_LOG.get());
		dropSelf(SHADEWOOD.get());
		dropSelf(SCARRED_SHADEWOOD.get());
		dropSelf(ROTTED_SHADEWOOD.get());
		dropSelf(STRIPPED_SHADEWOOD.get());
		dropSelf(STRIPPED_SCARRED_SHADEWOOD.get());
		dropSelf(STRIPPED_ROTTED_SHADEWOOD.get());
		dropSelf(FROST_WOOD.get());
		dropSelf(RAINBOW_WOOD.get());
		dropSelf(END_WOOD.get());
		dropSelf(VINE_WOOD.get());
		dropSelf(FLOWERY_VINE_WOOD.get());
		dropSelf(DEAD_WOOD.get());
		dropSelf(PETRIFIED_WOOD.get());
		dropSelf(CINDERED_WOOD.get());
		
		dropSelf(GLOWING_PLANKS.get());
		dropSelf(GLOWING_STAIRS.get());
		add(GLOWING_SLAB.get(), this::createSlabItemTable);
		dropSelf(GLOWING_BUTTON.get());
		dropSelf(GLOWING_PRESSURE_PLATE.get());
		dropSelf(GLOWING_FENCE.get());
		dropSelf(GLOWING_FENCE_GATE.get());
		add(GLOWING_DOOR.get(), this::createDoorTable);
		dropSelf(GLOWING_TRAPDOOR.get());
		
		dropSelf(CINDERED_PLANKS.get());
		dropSelf(CINDERED_STAIRS.get());
		add(CINDERED_SLAB.get(), this::createSlabItemTable);
		dropSelf(CINDERED_BUTTON.get());
		dropSelf(CINDERED_PRESSURE_PLATE.get());
		dropSelf(CINDERED_FENCE.get());
		dropSelf(CINDERED_FENCE_GATE.get());
		add(CINDERED_DOOR.get(), this::createDoorTable);
		dropSelf(CINDERED_TRAPDOOR.get());
		
		dropSelf(SHADEWOOD_PLANKS.get());
		dropSelf(SHADEWOOD_STAIRS.get());
		add(SHADEWOOD_SLAB.get(), this::createSlabItemTable);
		dropSelf(SHADEWOOD_BUTTON.get());
		dropSelf(SHADEWOOD_PRESSURE_PLATE.get());
		dropSelf(SHADEWOOD_FENCE.get());
		dropSelf(SHADEWOOD_FENCE_GATE.get());
		add(SHADEWOOD_DOOR.get(), this::createDoorTable);
		dropSelf(SHADEWOOD_TRAPDOOR.get());
		
		dropSelf(FROST_PLANKS.get());
		dropSelf(FROST_STAIRS.get());
		add(FROST_SLAB.get(), this::createSlabItemTable);
		dropSelf(FROST_BUTTON.get());
		dropSelf(FROST_PRESSURE_PLATE.get());
		dropSelf(FROST_FENCE.get());
		dropSelf(FROST_FENCE_GATE.get());
		add(FROST_DOOR.get(), this::createDoorTable);
		dropSelf(FROST_TRAPDOOR.get());
		
		dropSelf(RAINBOW_PLANKS.get());
		dropSelf(RAINBOW_BUTTON.get());
		dropSelf(RAINBOW_PRESSURE_PLATE.get());
		dropSelf(RAINBOW_FENCE.get());
		dropSelf(RAINBOW_FENCE_GATE.get());
		add(RAINBOW_DOOR.get(), this::createDoorTable);
		dropSelf(RAINBOW_TRAPDOOR.get());
		
		dropSelf(END_PLANKS.get());
		dropSelf(END_BUTTON.get());
		dropSelf(END_PRESSURE_PLATE.get());
		dropSelf(END_FENCE.get());
		dropSelf(END_FENCE_GATE.get());
		add(END_DOOR.get(), this::createDoorTable);
		dropSelf(END_TRAPDOOR.get());
		
		dropSelf(DEAD_PLANKS.get());
		dropSelf(DEAD_BUTTON.get());
		dropSelf(DEAD_PRESSURE_PLATE.get());
		dropSelf(DEAD_FENCE.get());
		dropSelf(DEAD_FENCE_GATE.get());
		add(DEAD_DOOR.get(), this::createDoorTable);
		dropSelf(DEAD_TRAPDOOR.get());
		
		add(FROST_LEAVES.get(), this::frostLeavesDrop);
		add(FROST_LEAVES_FLOWERING.get(), this::floweringFrostLeavesDrop);
		add(RAINBOW_LEAVES.get(), this::rainbowLeavesDrop);
		add(SHADEWOOD_LEAVES.get(), this::shadewoodLeavesDrop);
		add(SHROOMY_SHADEWOOD_LEAVES.get(), this::shadewoodLeavesDrop);
		add(END_LEAVES.get(), this::endLeavesDrop);
		dropSelf(RAINBOW_SAPLING.get());
		dropSelf(END_SAPLING.get());
		dropSelf(SHADEWOOD_SAPLING.get());
	
		dropPottedContents(POTTED_FROST_SAPLING.get());
		dropPottedContents(POTTED_END_SAPLING.get());
		dropPottedContents(POTTED_RAINBOW_SAPLING.get());
		dropPottedContents(POTTED_SHADEWOOD_SAPLING.get());
		
		add(GLOWING_BOOKSHELF.get(), this::bookshelfDrop);
		add(FROST_BOOKSHELF.get(), this::bookshelfDrop);
		add(RAINBOW_BOOKSHELF.get(), this::bookshelfDrop);
		add(END_BOOKSHELF.get(), this::bookshelfDrop);
		add(DEAD_BOOKSHELF.get(), this::bookshelfDrop);
		add(TREATED_BOOKSHELF.get(), this::bookshelfDrop);
		
		dropSelf(GLOWING_LADDER.get());
		dropSelf(FROST_LADDER.get());
		dropSelf(RAINBOW_LADDER.get());
		dropSelf(END_LADDER.get());
		dropSelf(DEAD_LADDER.get());
		dropSelf(TREATED_LADDER.get());
		
		dropSelf(GLOWING_MUSHROOM.get());
		add(DESERT_BUSH.get(), this::desertBushDrop);
		dropSelf(BLOOMING_CACTUS.get());
		add(SANDY_GRASS.get(), noDrop());
		add(TALL_SANDY_GRASS.get(), noDrop());
		add(DEAD_FOLIAGE.get(), noDrop());
		add(TALL_DEAD_BUSH.get(), noDrop());
		dropSelf(PETRIFIED_GRASS.get());
		dropSelf(PETRIFIED_POPPY.get());
		dropSelf(IGNEOUS_SPIKE.get());
		add(SINGED_GRASS.get(), noDrop());
		add(SINGED_FOLIAGE.get(), noDrop());
		dropSelf(SULFUR_BUBBLE.get());
		dropSelf(GLOWING_MUSHROOM_VINES.get());
		dropSelf(STRAWBERRY.get());
		add(ATTACHED_STRAWBERRY_STEM.get(), (stemBlock) -> createAttachedStemDrops(stemBlock, MSItems.STRAWBERRY_CHUNK.get()));
		add(STRAWBERRY_STEM.get(), (stemBlock) -> createStemDrops(stemBlock, MSItems.STRAWBERRY_CHUNK.get()));
		add(TALL_END_GRASS.get(), noDrop());
		dropSelf(GLOWFLOWER.get());
		
		dropSelf(GLOWY_GOOP.get());
		dropSelf(COAGULATED_BLOOD.get());
		dropSelf(PIPE.get());
		dropSelf(PIPE_INTERSECTION.get());
		dropSelf(PARCEL_PYXIS.get());
		dropSelf(PYXIS_LID.get());
		add(STONE_TABLET.get(), this::droppingWithTEItem);
		dropSelf(NAKAGATOR_STATUE.get());
		
		dropSelf(COARSE_STONE_STAIRS.get());
		dropSelf(COARSE_STONE_BRICK_STAIRS.get());
		dropSelf(SHADE_STAIRS.get());
		dropSelf(SHADE_BRICK_STAIRS.get());
		dropSelf(FROST_TILE_STAIRS.get());
		dropSelf(FROST_BRICK_STAIRS.get());
		dropSelf(MYCELIUM_STAIRS.get());
		dropSelf(MYCELIUM_BRICK_STAIRS.get());
		dropSelf(CHALK_STAIRS.get());
		dropSelf(CHALK_BRICK_STAIRS.get());
		dropSelf(PINK_STONE_STAIRS.get());
		dropSelf(PINK_STONE_BRICK_STAIRS.get());
		dropSelf(BROWN_STONE_STAIRS.get());
		dropSelf(BROWN_STONE_BRICK_STAIRS.get());
		dropSelf(GREEN_STONE_STAIRS.get());
		dropSelf(GREEN_STONE_BRICK_STAIRS.get());
		dropSelf(RAINBOW_STAIRS.get());
		dropSelf(END_STAIRS.get());
		dropSelf(DEAD_STAIRS.get());
		dropSelf(STEEP_GREEN_STONE_BRICK_STAIRS_BASE.get());
		dropSelf(STEEP_GREEN_STONE_BRICK_STAIRS_TOP.get());
		add(CHALK_SLAB.get(), this::createSlabItemTable);
		add(CHALK_BRICK_SLAB.get(), this::createSlabItemTable);
		add(PINK_STONE_SLAB.get(), this::createSlabItemTable);
		add(PINK_STONE_BRICK_SLAB.get(), this::createSlabItemTable);
		add(BROWN_STONE_SLAB.get(), this::createSlabItemTable);
		add(BROWN_STONE_BRICK_SLAB.get(), this::createSlabItemTable);
		add(GREEN_STONE_SLAB.get(), this::createSlabItemTable);
		add(GREEN_STONE_BRICK_SLAB.get(), this::createSlabItemTable);
		add(RAINBOW_SLAB.get(), this::createSlabItemTable);
		add(END_SLAB.get(), this::createSlabItemTable);
		add(DEAD_SLAB.get(), this::createSlabItemTable);
		add(BLACK_STONE_SLAB.get(), this::createSlabItemTable);
		add(BLACK_STONE_BRICK_SLAB.get(), this::createSlabItemTable);
		add(MYCELIUM_SLAB.get(), this::createSlabItemTable);
		add(MYCELIUM_BRICK_SLAB.get(), this::createSlabItemTable);
		add(FROST_TILE_SLAB.get(), this::createSlabItemTable);
		add(FROST_BRICK_SLAB.get(), this::createSlabItemTable);
		add(SHADE_SLAB.get(), this::createSlabItemTable);
		add(SHADE_BRICK_SLAB.get(), this::createSlabItemTable);
		add(COARSE_STONE_SLAB.get(), this::createSlabItemTable);
		add(COARSE_STONE_BRICK_SLAB.get(), this::createSlabItemTable);
		
		dropSelf(TRAJECTORY_BLOCK.get());
		dropSelf(STAT_STORER.get());
		dropSelf(REMOTE_OBSERVER.get());
		dropSelf(WIRELESS_REDSTONE_TRANSMITTER.get());
		dropSelf(WIRELESS_REDSTONE_RECEIVER.get());
		dropSelf(SOLID_SWITCH.get());
		dropSelf(VARIABLE_SOLID_SWITCH.get());
		dropSelf(ONE_SECOND_INTERVAL_TIMED_SOLID_SWITCH.get());
		dropSelf(TWO_SECOND_INTERVAL_TIMED_SOLID_SWITCH.get());
		add(SUMMONER.get(), noDrop());
		add(AREA_EFFECT_BLOCK.get(), noDrop());
		dropSelf(PLATFORM_GENERATOR.get());
		add(PLATFORM_BLOCK.get(), noDrop());
		dropSelf(PLATFORM_RECEPTACLE.get());
		dropSelf(ITEM_MAGNET.get());
		dropSelf(REDSTONE_CLOCK.get());
		dropSelf(ROTATOR.get());
		dropSelf(TOGGLER.get());
		dropSelf(REMOTE_COMPARATOR.get());
		add(STRUCTURE_CORE.get(), noDrop());
		dropSelf(FALL_PAD.get());
		dropSelf(FRAGILE_STONE.get());
		dropSelf(RETRACTABLE_SPIKES.get());
		dropSelf(BLOCK_PRESSURE_PLATE.get());
		dropSelf(PUSHABLE_BLOCK.get());
		dropSelf(BLOCK_TELEPORTER.get());
		dropSelf(AND_GATE_BLOCK.get());
		dropSelf(OR_GATE_BLOCK.get());
		dropSelf(XOR_GATE_BLOCK.get());
		dropSelf(NAND_GATE_BLOCK.get());
		dropSelf(NOR_GATE_BLOCK.get());
		dropSelf(XNOR_GATE_BLOCK.get());
		
		dropSelf(HOLOPAD.get());
		dropSelf(INTELLIBEAM_LASERSTATION.get());
		dropSelf(CRUXTRUDER_LID.get());
		add(MINI_CRUXTRUDER.get(), this::droppingWithColor);
		dropSelf(MINI_TOTEM_LATHE.get());
		dropSelf(MINI_ALCHEMITER.get());
		dropSelf(MINI_PUNCH_DESIGNIX.get());
		
		dropSelf(COMPUTER.get());
		dropSelf(LAPTOP.get());
		dropSelf(CROCKERTOP.get());
		dropSelf(HUBTOP.get());
		dropSelf(LUNCHTOP.get());
		dropSelf(OLD_COMPUTER.get());
		add(TRANSPORTALIZER.get(), this::droppingWithIds);
		add(TRANS_PORTALIZER.get(), this::droppingWithIds);
		dropSelf(SENDIFICATOR.get());
		dropSelf(GRIST_WIDGET.get());
		dropSelf(URANIUM_COOKER.get());
		dropSelf(GRIST_COLLECTOR.get());
		dropSelf(ANTHVIL.get());
		dropSelf(SKAIANET_DENIER.get());
		dropSelf(POWER_HUB.get());
		
		add(CRUXITE_DOWEL.get(), this::droppingWithTEItem);
		add(EMERGING_CRUXITE_DOWEL.get(), this::droppingWithTEItem);
		
		dropSelf(GOLD_SEEDS.get());
		dropSelf(WOODEN_CACTUS.get());
		
		add(APPLE_CAKE.get(), noDrop());
		add(BLUE_CAKE.get(), noDrop());
		add(COLD_CAKE.get(), noDrop());
		add(RED_CAKE.get(), noDrop());
		add(HOT_CAKE.get(), noDrop());
		add(REVERSE_CAKE.get(), noDrop());
		add(FUCHSIA_CAKE.get(), noDrop());
		add(NEGATIVE_CAKE.get(), noDrop());
		add(CARROT_CAKE.get(), noDrop());
		dropSelf(LARGE_CAKE.get());
		dropSelf(PINK_FROSTED_TOP_LARGE_CAKE.get());
		add(CHOCOLATEY_CAKE.get(), noDrop());
		
		dropSelf(PRIMED_TNT.get());
		dropSelf(UNSTABLE_TNT.get());
		dropSelf(INSTANT_TNT.get());
		dropSelf(WOODEN_EXPLOSIVE_BUTTON.get());
		dropSelf(STONE_EXPLOSIVE_BUTTON.get());
		
		dropSelf(BLENDER.get());
		dropSelf(CHESSBOARD.get());
		dropSelf(MINI_FROG_STATUE.get());
		dropSelf(MINI_WIZARD_STATUE.get());
		dropSelf(MINI_TYPHEUS_STATUE.get());
		dropSelf(CASSETTE_PLAYER.get());
		dropSelf(HORSE_CLOCK.BOTTOM.get());
		dropSelf(GLOWYSTONE_DUST.get());
		dropSelf(MIRROR.get());
	}
	
	@Override
	public LootTable.Builder createSlabItemTable(Block block)
	{
		return super.createSlabItemTable(block);
	}
	
	@Override
	public LootTable.Builder createLeavesDrops(Block leavesBlock, Block saplingBlock, float... chances)
	{
		return super.createLeavesDrops(leavesBlock, saplingBlock, chances);
	}
	
	@Override
	public LootTable.Builder createDoorTable(Block doorBlock)
	{
		return super.createDoorTable(doorBlock);
	}
	
	@Override
	public void dropSelf(Block block)
	{
		super.dropSelf(block);
	}
	
	@Override
	public void add(Block block, Function<Block, LootTable.Builder> factory)
	{
		super.add(block, factory);
	}
	
	@Override
	public void dropPottedContents(Block pFlowerPot)
	{
		super.dropPottedContents(pFlowerPot);
	}
	
	private LootTable.Builder cruxiteOreDrop(Block block)
	{
		return createSilkTouchDispatchTable(block, applyExplosionDecay(block, LootItem.lootTableItem(MSItems.RAW_CRUXITE.get()).apply(SetItemCountFunction.setCount(UniformGenerator.between(2.0F, 5.0F))).apply(ApplyBonusCount.addOreBonusCount(Enchantments.BLOCK_FORTUNE))));
	}
	
	private LootTable.Builder uraniumOreDrop(Block block)
	{
		return createOreDrop(block, MSItems.RAW_URANIUM.get());
	}
	
	private LootTable.Builder coalOreDrop(Block block)
	{
		return createOreDrop(block, Items.COAL);
	}
	
	private LootTable.Builder goldOreDrop(Block block)
	{
		return createOreDrop(block, Items.RAW_GOLD);
	}
	
	private LootTable.Builder ironOreDrop(Block block)
	{
		return createOreDrop(block, Items.RAW_IRON);
	}
	
	private LootTable.Builder redstoneOreDrop(Block block)
	{
		return createSilkTouchDispatchTable(block, applyExplosionDecay(block, LootItem.lootTableItem(Items.REDSTONE).apply(SetItemCountFunction.setCount(UniformGenerator.between(4.0F, 5.0F))).apply(ApplyBonusCount.addUniformBonusCount(Enchantments.BLOCK_FORTUNE))));
	}
	
	private LootTable.Builder quartzOreDrop(Block block)
	{
		return createOreDrop(block, Items.QUARTZ);
	}
	
	private LootTable.Builder lapisOreDrop(Block block)
	{
		return createSilkTouchDispatchTable(block, applyExplosionDecay(block, LootItem.lootTableItem(Items.LAPIS_LAZULI).apply(SetItemCountFunction.setCount(UniformGenerator.between(4.0F, 9.0F))).apply(ApplyBonusCount.addOreBonusCount(Enchantments.BLOCK_FORTUNE))));
	}
	
	private LootTable.Builder diamondOreDrop(Block block)
	{
		return createOreDrop(block, Items.DIAMOND);
	}
	
	private LootTable.Builder emeraldOreDrop(Block block) { return createOreDrop(block, Items.EMERALD); }
	
	private LootTable.Builder endGrassDrop(Block block)
	{
		return createSingleItemTableWithSilkTouch(block, Blocks.END_STONE);
	}
	
	private LootTable.Builder frostLeavesDrop(Block block)
	{
		return createLeavesDrops(block, FROST_SAPLING.get(), SAPLING_CHANCES);
	}
	
	private LootTable.Builder floweringFrostLeavesDrop(Block block)
	{
		return createLeavesDrops(block, FROST_SAPLING.get(), SAPLING_CHANCES);
	}
	
	private LootTable.Builder rainbowLeavesDrop(Block block)
	{
		return createLeavesDrops(block, RAINBOW_SAPLING.get(), SAPLING_CHANCES);
	}
	
	private LootTable.Builder endLeavesDrop(Block block)
	{
		return createLeavesDrops(block, END_SAPLING.get(), SAPLING_CHANCES).withPool(LootPool.lootPool().setRolls(ConstantValue.exactly(1)).when(NO_SILK_OR_SHEAR_CONDITION).add(applyExplosionCondition(block, LootItem.lootTableItem(Items.CHORUS_FRUIT)).when(BonusLevelTableCondition.bonusLevelFlatChance(Enchantments.BLOCK_FORTUNE, 0.005F, 0.0055555557F, 0.00625F, 0.008333334F, 0.025F))));
	}
	
	private LootTable.Builder shadewoodLeavesDrop(Block block)
	{
		return createLeavesDrops(block, SHADEWOOD_SAPLING.get(), SAPLING_CHANCES);
	}
	
	public LootTable.Builder bookshelfDrop(Block block)
	{
		return createSingleItemTableWithSilkTouch(block, Items.BOOK, ConstantValue.exactly(3.0F));
	}
	
	private LootTable.Builder desertBushDrop(Block block)
	{
		return createSilkTouchDispatchTable(block, applyExplosionDecay(block, LootItem.lootTableItem(MSItems.DESERT_FRUIT.get()).apply(SetItemCountFunction.setCount(UniformGenerator.between(3.0F, 6.0F))).apply(ApplyBonusCount.addUniformBonusCount(Enchantments.BLOCK_FORTUNE))));
	}
	
	private LootTable.Builder droppingWithColor(Block block)
	{
		return LootTable.lootTable().withPool(applyExplosionCondition(block, LootPool.lootPool().setRolls(ConstantValue.exactly(1)).add(LootItem.lootTableItem(block).apply(CopyNbtFunction.copyData(ContextNbtProvider.BLOCK_ENTITY).copy("color", "color")))));
	}
	
	private LootTable.Builder droppingWithTEItem(Block block)
	{
		return LootTable.lootTable().withPool(applyExplosionCondition(block, LootPool.lootPool().setRolls(ConstantValue.exactly(1)).add(DynamicLoot.dynamicEntry(ItemStackBlockEntity.ITEM_DYNAMIC))));
	}

	
	private LootTable.Builder droppingWithIds(Block block)
	{
		return LootTable.lootTable().withPool(applyExplosionCondition(block, LootPool.lootPool().setRolls(ConstantValue.exactly(1))
				.add(LootItem.lootTableItem(block).apply(CopyNbtFunction.copyData(ContextNbtProvider.BLOCK_ENTITY)
						.copy(TransportalizerBlockEntity.ID, TransportalizerBlockEntity.ID)
						.copy(TransportalizerBlockEntity.DEST_ID, TransportalizerBlockEntity.DEST_ID)
						.copy(TransportalizerBlockEntity.LOCKED, TransportalizerBlockEntity.LOCKED))
				)));
	}
	
	
	@Override
	protected Iterable<Block> getKnownBlocks()
	{
		return BuiltInRegistries.BLOCK.entrySet().stream()
				.filter(entry -> entry.getKey().location().getNamespace().equals(Minestuck.MOD_ID))
				.map(Map.Entry::getValue).toList();
	}
}
