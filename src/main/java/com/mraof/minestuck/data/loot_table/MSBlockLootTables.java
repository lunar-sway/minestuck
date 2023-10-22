package com.mraof.minestuck.data.loot_table;

import com.mraof.minestuck.Minestuck;
import com.mraof.minestuck.blockentity.ItemStackBlockEntity;
import com.mraof.minestuck.item.MSItems;
import net.minecraft.advancements.critereon.EnchantmentPredicate;
import net.minecraft.advancements.critereon.ItemPredicate;
import net.minecraft.advancements.critereon.MinMaxBounds;
import net.minecraft.data.loot.BlockLootSubProvider;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.DynamicLoot;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.functions.ApplyBonusCount;
import net.minecraft.world.level.storage.loot.functions.CopyNameFunction;
import net.minecraft.world.level.storage.loot.functions.CopyNbtFunction;
import net.minecraft.world.level.storage.loot.functions.SetItemCountFunction;
import net.minecraft.world.level.storage.loot.predicates.BonusLevelTableCondition;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraft.world.level.storage.loot.predicates.MatchTool;
import net.minecraft.world.level.storage.loot.providers.nbt.ContextNbtProvider;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;
import net.minecraft.world.level.storage.loot.providers.number.UniformGenerator;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.Map;
import java.util.Set;

import static com.mraof.minestuck.block.MSBlocks.*;

public final class MSBlockLootTables extends BlockLootSubProvider
{
	private static final LootItemCondition.Builder SILK_TOUCH_CONDITION = MatchTool.toolMatches(ItemPredicate.Builder.item().hasEnchantment(new EnchantmentPredicate(Enchantments.SILK_TOUCH, MinMaxBounds.Ints.atLeast(1))));
	private static final LootItemCondition.Builder SHEAR_CONDITION = MatchTool.toolMatches(ItemPredicate.Builder.item().of(Items.SHEARS));
	private static final LootItemCondition.Builder SILK_AND_SHEAR_CONDITION = SHEAR_CONDITION.or(SILK_TOUCH_CONDITION);
	private static final LootItemCondition.Builder NO_SILK_OR_SHEAR_CONDITION = SILK_AND_SHEAR_CONDITION.invert();
	private static final float[] SAPLING_CHANCES = new float[]{0.05F, 0.0625F, 0.083333336F, 0.1F};
	
	MSBlockLootTables()
	{
		super(Set.of(), FeatureFlags.REGISTRY.allFlags());
	}
	
	@Override
	protected void generate()
	{
		dropSelf(BLACK_CHESS_DIRT.get());
		dropSelf(WHITE_CHESS_DIRT.get());
		dropSelf(DARK_GRAY_CHESS_DIRT.get());
		dropSelf(LIGHT_GRAY_CHESS_DIRT.get());
		
		dropSelf(BLACK_CHESS_BRICKS.get());
		dropSelf(WHITE_CHESS_BRICKS.get());
		dropSelf(LIGHT_GRAY_CHESS_BRICKS.get());
		dropSelf(DARK_GRAY_CHESS_BRICKS.get());
		dropSelf(BLACK_CHESS_BRICK_SMOOTH.get());
		dropSelf(WHITE_CHESS_BRICK_SMOOTH.get());
		dropSelf(LIGHT_GRAY_CHESS_BRICK_SMOOTH.get());
		dropSelf(DARK_GRAY_CHESS_BRICK_SMOOTH.get());
		dropSelf(BLACK_CHESS_BRICK_TRIM.get());
		dropSelf(WHITE_CHESS_BRICK_TRIM.get());
		dropSelf(LIGHT_GRAY_CHESS_BRICK_TRIM.get());
		dropSelf(DARK_GRAY_CHESS_BRICK_TRIM.get());
		dropSelf(CHECKERED_STAINED_GLASS.get());
		dropSelf(BLACK_CROWN_STAINED_GLASS.get());
		dropSelf(BLACK_PAWN_STAINED_GLASS.get());
		dropSelf(WHITE_CROWN_STAINED_GLASS.get());
		dropSelf(WHITE_PAWN_STAINED_GLASS.get());
		
		add(STONE_CRUXITE_ORE.get(), this::cruxiteOreDrop);
		add(COBBLESTONE_CRUXITE_ORE.get(), this::cruxiteOreDrop);
		add(SANDSTONE_CRUXITE_ORE.get(), this::cruxiteOreDrop);
		add(RED_SANDSTONE_CRUXITE_ORE.get(), this::cruxiteOreDrop);
		add(NETHERRACK_CRUXITE_ORE.get(), this::cruxiteOreDrop);
		add(END_STONE_CRUXITE_ORE.get(), this::cruxiteOreDrop);
		add(SHADE_STONE_CRUXITE_ORE.get(), this::cruxiteOreDrop);
		add(PINK_STONE_CRUXITE_ORE.get(), this::cruxiteOreDrop);
		add(MYCELIUM_STONE_CRUXITE_ORE.get(), this::cruxiteOreDrop);
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
		
		add(NETHERRACK_COAL_ORE.get(), this::coalOreDrop);
		add(SHADE_STONE_COAL_ORE.get(), this::coalOreDrop);
		add(PINK_STONE_COAL_ORE.get(), this::coalOreDrop);
		
		add(SANDSTONE_IRON_ORE.get(), this::ironOreDrop);
		add(RED_SANDSTONE_IRON_ORE.get(), this::ironOreDrop);
		add(END_STONE_IRON_ORE.get(), this::ironOreDrop);
		add(SANDSTONE_GOLD_ORE.get(), this::goldOreDrop);
		add(RED_SANDSTONE_GOLD_ORE.get(), this::goldOreDrop);
		add(SHADE_STONE_GOLD_ORE.get(), this::goldOreDrop);
		add(PINK_STONE_GOLD_ORE.get(), this::goldOreDrop);
		add(END_STONE_REDSTONE_ORE.get(), this::redstoneOreDrop);
		add(STONE_QUARTZ_ORE.get(), this::quartzOreDrop);
		add(PINK_STONE_LAPIS_ORE.get(), this::lapisOreDrop);
		add(PINK_STONE_DIAMOND_ORE.get(), this::diamondOreDrop);
		
		dropSelf(CRUXITE_BLOCK.get());
		dropSelf(URANIUM_BLOCK.get());
		dropSelf(GENERIC_OBJECT.get());
		
		dropSelf(BLUE_DIRT.get());
		dropSelf(THOUGHT_DIRT.get());
		dropSelf(COARSE_STONE.get());
		dropSelf(CHISELED_COARSE_STONE.get());
		dropSelf(COARSE_STONE_BRICKS.get());
		dropSelf(COARSE_STONE_COLUMN.get());
		dropSelf(CHISELED_COARSE_STONE_BRICKS.get());
		dropSelf(CRACKED_COARSE_STONE_BRICKS.get());
		dropSelf(MOSSY_COARSE_STONE_BRICKS.get());
		dropSelf(SHADE_STONE.get());
		dropSelf(SMOOTH_SHADE_STONE.get());
		dropSelf(SHADE_BRICKS.get());
		dropSelf(SHADE_COLUMN.get());
		dropSelf(CHISELED_SHADE_BRICKS.get());
		dropSelf(CRACKED_SHADE_BRICKS.get());
		dropSelf(MOSSY_SHADE_BRICKS.get());
		dropSelf(BLOOD_SHADE_BRICKS.get());
		dropSelf(TAR_SHADE_BRICKS.get());
		dropSelf(FROST_TILE.get());
		dropSelf(CHISELED_FROST_TILE.get());
		dropSelf(FROST_BRICKS.get());
		dropSelf(FROST_COLUMN.get());
		dropSelf(CHISELED_FROST_BRICKS.get());
		dropSelf(CRACKED_FROST_BRICKS.get());
		dropSelf(FLOWERY_FROST_BRICKS.get());
		dropSelf(CAST_IRON.get());
		dropSelf(CHISELED_CAST_IRON.get());
		dropSelf(STEEL_BEAM.get());
		dropSelf(MYCELIUM_COBBLESTONE.get());
		add(MYCELIUM_STONE.get(), createSingleItemTableWithSilkTouch(MYCELIUM_STONE.get(), MYCELIUM_COBBLESTONE.get()));
		dropSelf(POLISHED_MYCELIUM_STONE.get());
		dropSelf(MYCELIUM_BRICKS.get());
		dropSelf(MYCELIUM_COLUMN.get());
		dropSelf(CHISELED_MYCELIUM_BRICKS.get());
		dropSelf(SUSPICIOUS_CHISELED_MYCELIUM_BRICKS.get());
		dropSelf(CRACKED_MYCELIUM_BRICKS.get());
		dropSelf(MOSSY_MYCELIUM_BRICKS.get());
		dropSelf(FLOWERY_MYCELIUM_BRICKS.get());
		add(BLACK_STONE.get(), createSingleItemTableWithSilkTouch(BLACK_STONE.get(), BLACK_COBBLESTONE.get()));
		dropSelf(BLACK_COBBLESTONE.get());
		dropSelf(POLISHED_BLACK_STONE.get());
		dropSelf(BLACK_STONE_BRICKS.get());
		dropSelf(BLACK_STONE_COLUMN.get());
		dropSelf(CHISELED_BLACK_STONE_BRICKS.get());
		dropSelf(CRACKED_BLACK_STONE_BRICKS.get());
		dropSelf(BLACK_SAND.get());
		dropSelf(IGNEOUS_STONE.get());
		dropSelf(PUMICE_STONE.get());
		dropSelf(DECREPIT_STONE_BRICKS.get());
		dropSelf(FLOWERY_MOSSY_COBBLESTONE.get());
		dropSelf(MOSSY_DECREPIT_STONE_BRICKS.get());
		dropSelf(FLOWERY_MOSSY_STONE_BRICKS.get());
		dropSelf(COARSE_END_STONE.get());
		add(END_GRASS.get(), this::endGrassDrop);
		dropSelf(CHALK.get());
		dropSelf(POLISHED_CHALK.get());
		dropSelf(CHALK_BRICKS.get());
		dropSelf(CHALK_COLUMN.get());
		dropSelf(CHISELED_CHALK_BRICKS.get());
		dropSelf(MOSSY_CHALK_BRICKS.get());
		dropSelf(FLOWERY_CHALK_BRICKS.get());
		dropSelf(PINK_STONE.get());
		dropSelf(POLISHED_PINK_STONE.get());
		dropSelf(PINK_STONE_BRICKS.get());
		dropSelf(CHISELED_PINK_STONE_BRICKS.get());
		dropSelf(CRACKED_PINK_STONE_BRICKS.get());
		dropSelf(MOSSY_PINK_STONE_BRICKS.get());
		dropSelf(PINK_STONE_COLUMN.get());
		dropSelf(BROWN_STONE.get());
		dropSelf(POLISHED_BROWN_STONE.get());
		dropSelf(BROWN_STONE_BRICKS.get());
		dropSelf(BROWN_STONE_COLUMN.get());
		dropSelf(CRACKED_BROWN_STONE_BRICKS.get());
		dropSelf(GREEN_STONE.get());
		dropSelf(POLISHED_GREEN_STONE.get());
		dropSelf(GREEN_STONE_BRICKS.get());
		dropSelf(GREEN_STONE_COLUMN.get());
		dropSelf(CHISELED_GREEN_STONE_BRICKS.get());
		dropSelf(HORIZONTAL_GREEN_STONE_BRICKS.get());
		dropSelf(VERTICAL_GREEN_STONE_BRICKS.get());
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
		add(UNCARVED_WOOD.get(), createSingleItemTableWithSilkTouch(UNCARVED_WOOD.get(), CHIPBOARD.get()));
		dropSelf(CHIPBOARD.get());
		dropSelf(WOOD_SHAVINGS.get());
		dropSelf(CARVED_HEAVY_PLANKS.get());
		dropSelf(CARVED_PLANKS.get());
		dropSelf(POLISHED_UNCARVED_WOOD.get());
		dropSelf(CARVED_KNOTTED_WOOD.get());
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
		dropSelf(GLOWING_PLANKS.get());
		dropSelf(SHADEWOOD_PLANKS.get());
		dropSelf(FROST_PLANKS.get());
		dropSelf(RAINBOW_PLANKS.get());
		dropSelf(END_PLANKS.get());
		dropSelf(DEAD_PLANKS.get());
		dropSelf(TREATED_PLANKS.get());
		add(FROST_LEAVES.get(), this::frostLeavesDrop);
		add(RAINBOW_LEAVES.get(), this::rainbowLeavesDrop);
		add(SHADEWOOD_LEAVES.get(), this::shadewoodLeavesDrop);
		add(SHROOMY_SHADEWOOD_LEAVES.get(), this::shadewoodLeavesDrop);
		add(END_LEAVES.get(), this::endLeavesDrop);
		dropSelf(RAINBOW_SAPLING.get());
		dropSelf(END_SAPLING.get());
		dropSelf(SHADEWOOD_SAPLING.get());
		
		dropSelf(BLOOD_ASPECT_LOG.get());
		dropSelf(BREATH_ASPECT_LOG.get());
		dropSelf(DOOM_ASPECT_LOG.get());
		dropSelf(HEART_ASPECT_LOG.get());
		dropSelf(HOPE_ASPECT_LOG.get());
		dropSelf(LIFE_ASPECT_LOG.get());
		dropSelf(LIGHT_ASPECT_LOG.get());
		dropSelf(MIND_ASPECT_LOG.get());
		dropSelf(RAGE_ASPECT_LOG.get());
		dropSelf(SPACE_ASPECT_LOG.get());
		dropSelf(TIME_ASPECT_LOG.get());
		dropSelf(VOID_ASPECT_LOG.get());
		dropSelf(BLOOD_ASPECT_PLANKS.get());
		dropSelf(BREATH_ASPECT_PLANKS.get());
		dropSelf(DOOM_ASPECT_PLANKS.get());
		dropSelf(HEART_ASPECT_PLANKS.get());
		dropSelf(HOPE_ASPECT_PLANKS.get());
		dropSelf(LIFE_ASPECT_PLANKS.get());
		dropSelf(LIGHT_ASPECT_PLANKS.get());
		dropSelf(MIND_ASPECT_PLANKS.get());
		dropSelf(RAGE_ASPECT_PLANKS.get());
		dropSelf(SPACE_ASPECT_PLANKS.get());
		dropSelf(TIME_ASPECT_PLANKS.get());
		dropSelf(VOID_ASPECT_PLANKS.get());
		add(BLOOD_ASPECT_LEAVES.get(), this::bloodAspectLeavesDrop);
		add(BREATH_ASPECT_LEAVES.get(), this::breathAspectLeavesDrop);
		add(DOOM_ASPECT_LEAVES.get(), this::doomAspectLeavesDrop);
		add(HEART_ASPECT_LEAVES.get(), this::heartAspectLeavesDrop);
		add(HOPE_ASPECT_LEAVES.get(), this::hopeAspectLeavesDrop);
		add(LIFE_ASPECT_LEAVES.get(), this::lifeAspectLeavesDrop);
		add(LIGHT_ASPECT_LEAVES.get(), this::lightAspectLeavesDrop);
		add(MIND_ASPECT_LEAVES.get(), this::mindAspectLeavesDrop);
		add(RAGE_ASPECT_LEAVES.get(), this::rageAspectLeavesDrop);
		add(SPACE_ASPECT_LEAVES.get(), this::spaceAspectLeavesDrop);
		add(TIME_ASPECT_LEAVES.get(), this::timeAspectLeavesDrop);
		add(VOID_ASPECT_LEAVES.get(), this::voidAspectLeavesDrop);
		dropSelf(BLOOD_ASPECT_SAPLING.get());
		dropSelf(BREATH_ASPECT_SAPLING.get());
		dropSelf(DOOM_ASPECT_SAPLING.get());
		dropSelf(HEART_ASPECT_SAPLING.get());
		dropSelf(HOPE_ASPECT_SAPLING.get());
		dropSelf(LIFE_ASPECT_SAPLING.get());
		dropSelf(LIGHT_ASPECT_SAPLING.get());
		dropSelf(MIND_ASPECT_SAPLING.get());
		dropSelf(RAGE_ASPECT_SAPLING.get());
		dropSelf(SPACE_ASPECT_SAPLING.get());
		dropSelf(TIME_ASPECT_SAPLING.get());
		dropSelf(VOID_ASPECT_SAPLING.get());
		
		add(BLOOD_ASPECT_BOOKSHELF.get(), this::bookshelfDrop);
		add(BREATH_ASPECT_BOOKSHELF.get(), this::bookshelfDrop);
		add(DOOM_ASPECT_BOOKSHELF.get(), this::bookshelfDrop);
		add(HEART_ASPECT_BOOKSHELF.get(), this::bookshelfDrop);
		add(HOPE_ASPECT_BOOKSHELF.get(), this::bookshelfDrop);
		add(LIFE_ASPECT_BOOKSHELF.get(), this::bookshelfDrop);
		add(LIGHT_ASPECT_BOOKSHELF.get(), this::bookshelfDrop);
		add(MIND_ASPECT_BOOKSHELF.get(), this::bookshelfDrop);
		add(RAGE_ASPECT_BOOKSHELF.get(), this::bookshelfDrop);
		add(SPACE_ASPECT_BOOKSHELF.get(), this::bookshelfDrop);
		add(TIME_ASPECT_BOOKSHELF.get(), this::bookshelfDrop);
		add(VOID_ASPECT_BOOKSHELF.get(), this::bookshelfDrop);
		
		add(GLOWING_BOOKSHELF.get(), this::bookshelfDrop);
		add(FROST_BOOKSHELF.get(), this::bookshelfDrop);
		add(RAINBOW_BOOKSHELF.get(), this::bookshelfDrop);
		add(END_BOOKSHELF.get(), this::bookshelfDrop);
		add(DEAD_BOOKSHELF.get(), this::bookshelfDrop);
		add(TREATED_BOOKSHELF.get(), this::bookshelfDrop);
		
		dropSelf(BLOOD_ASPECT_LADDER.get());
		dropSelf(BREATH_ASPECT_LADDER.get());
		dropSelf(DOOM_ASPECT_LADDER.get());
		dropSelf(HEART_ASPECT_LADDER.get());
		dropSelf(HOPE_ASPECT_LADDER.get());
		dropSelf(LIFE_ASPECT_LADDER.get());
		dropSelf(LIGHT_ASPECT_LADDER.get());
		dropSelf(MIND_ASPECT_LADDER.get());
		dropSelf(RAGE_ASPECT_LADDER.get());
		dropSelf(SPACE_ASPECT_LADDER.get());
		dropSelf(TIME_ASPECT_LADDER.get());
		dropSelf(VOID_ASPECT_LADDER.get());
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
		
		dropSelf(BLACK_CHESS_BRICK_STAIRS.get());
		dropSelf(DARK_GRAY_CHESS_BRICK_STAIRS.get());
		dropSelf(LIGHT_GRAY_CHESS_BRICK_STAIRS.get());
		dropSelf(WHITE_CHESS_BRICK_STAIRS.get());
		dropSelf(COARSE_STONE_STAIRS.get());
		dropSelf(COARSE_STONE_BRICK_STAIRS.get());
		dropSelf(SHADE_STAIRS.get());
		dropSelf(SHADE_BRICK_STAIRS.get());
		dropSelf(FROST_TILE_STAIRS.get());
		dropSelf(FROST_BRICK_STAIRS.get());
		dropSelf(CAST_IRON_STAIRS.get());
		dropSelf(BLACK_STONE_STAIRS.get());
		dropSelf(BLACK_STONE_BRICK_STAIRS.get());
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
		dropSelf(RAINBOW_PLANKS_STAIRS.get());
		dropSelf(END_PLANKS_STAIRS.get());
		dropSelf(DEAD_PLANKS_STAIRS.get());
		dropSelf(TREATED_PLANKS_STAIRS.get());
		dropSelf(FLOWERY_MOSSY_STONE_BRICK_STAIRS.get());
		dropSelf(STEEP_GREEN_STONE_BRICK_STAIRS_BASE.get());
		dropSelf(STEEP_GREEN_STONE_BRICK_STAIRS_TOP.get());
		dropSelf(BLACK_CHESS_BRICK_SLAB.get());
		dropSelf(DARK_GRAY_CHESS_BRICK_SLAB.get());
		dropSelf(LIGHT_GRAY_CHESS_BRICK_SLAB.get());
		dropSelf(WHITE_CHESS_BRICK_SLAB.get());
		dropSelf(CHALK_SLAB.get());
		dropSelf(CHALK_BRICK_SLAB.get());
		dropSelf(PINK_STONE_SLAB.get());
		dropSelf(PINK_STONE_BRICK_SLAB.get());
		dropSelf(BROWN_STONE_SLAB.get());
		dropSelf(BROWN_STONE_BRICK_SLAB.get());
		dropSelf(GREEN_STONE_SLAB.get());
		dropSelf(GREEN_STONE_BRICK_SLAB.get());
		dropSelf(RAINBOW_PLANKS_SLAB.get());
		dropSelf(END_PLANKS_SLAB.get());
		dropSelf(DEAD_PLANKS_SLAB.get());
		dropSelf(TREATED_PLANKS_SLAB.get());
		dropSelf(BLACK_STONE_SLAB.get());
		dropSelf(BLACK_STONE_BRICK_SLAB.get());
		dropSelf(MYCELIUM_SLAB.get());
		dropSelf(MYCELIUM_BRICK_SLAB.get());
		dropSelf(FLOWERY_MOSSY_STONE_BRICK_SLAB.get());
		dropSelf(FROST_TILE_SLAB.get());
		dropSelf(FROST_BRICK_SLAB.get());
		dropSelf(SHADE_SLAB.get());
		dropSelf(SHADE_BRICK_SLAB.get());
		dropSelf(COARSE_STONE_SLAB.get());
		dropSelf(COARSE_STONE_BRICK_SLAB.get());
		
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
		add(TRANSPORTALIZER.get(), MSBlockLootTables::droppingWithNameOnSilkTouch);
		add(TRANS_PORTALIZER.get(), MSBlockLootTables::droppingWithNameOnSilkTouch);
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
	private LootTable.Builder endGrassDrop(Block block)
	{
		return createSingleItemTableWithSilkTouch(block, Blocks.END_STONE);
	}
	private LootTable.Builder frostLeavesDrop(Block block)
	{
		return createLeavesDrops(block, Blocks.AIR, SAPLING_CHANCES);
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
	private LootTable.Builder bloodAspectLeavesDrop(Block block)
	{
		return createLeavesDrops(block, BLOOD_ASPECT_SAPLING.get(), SAPLING_CHANCES);
	}
	private LootTable.Builder breathAspectLeavesDrop(Block block)
	{
		return createLeavesDrops(block, BREATH_ASPECT_SAPLING.get(), SAPLING_CHANCES);
	}
	private LootTable.Builder doomAspectLeavesDrop(Block block)
	{
		return createLeavesDrops(block, DOOM_ASPECT_SAPLING.get(), SAPLING_CHANCES);
	}
	private LootTable.Builder heartAspectLeavesDrop(Block block)
	{
		return createLeavesDrops(block, HEART_ASPECT_SAPLING.get(), SAPLING_CHANCES);
	}
	private LootTable.Builder hopeAspectLeavesDrop(Block block)
	{
		return createLeavesDrops(block, HOPE_ASPECT_SAPLING.get(), SAPLING_CHANCES);
	}
	private LootTable.Builder lifeAspectLeavesDrop(Block block)
	{
		return createLeavesDrops(block, LIFE_ASPECT_SAPLING.get(), SAPLING_CHANCES);
	}
	private LootTable.Builder lightAspectLeavesDrop(Block block)
	{
		return createLeavesDrops(block, LIGHT_ASPECT_SAPLING.get(), SAPLING_CHANCES);
	}
	private LootTable.Builder mindAspectLeavesDrop(Block block)
	{
		return createLeavesDrops(block, MIND_ASPECT_SAPLING.get(), SAPLING_CHANCES);
	}
	private LootTable.Builder rageAspectLeavesDrop(Block block)
	{
		return createLeavesDrops(block, RAGE_ASPECT_SAPLING.get(), SAPLING_CHANCES);
	}
	private LootTable.Builder spaceAspectLeavesDrop(Block block)
	{
		return createLeavesDrops(block, SPACE_ASPECT_SAPLING.get(), SAPLING_CHANCES);
	}
	private LootTable.Builder timeAspectLeavesDrop(Block block)
	{
		return createLeavesDrops(block, TIME_ASPECT_SAPLING.get(), SAPLING_CHANCES);
	}
	private LootTable.Builder voidAspectLeavesDrop(Block block)
	{
		return createLeavesDrops(block, VOID_ASPECT_SAPLING.get(), SAPLING_CHANCES);
	}
	private LootTable.Builder bookshelfDrop(Block block)
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
	private static LootTable.Builder droppingWithNameOnSilkTouch(Block block)
	{
		return createSelfDropDispatchTable(block, SILK_TOUCH_CONDITION.invert(), LootItem.lootTableItem(block).apply(CopyNameFunction.copyName(CopyNameFunction.NameSource.BLOCK_ENTITY)));
	}
	
	@Override
	protected Iterable<Block> getKnownBlocks()
	{
		return ForgeRegistries.BLOCKS.getEntries().stream()
				.filter(entry -> entry.getKey().location().getNamespace().equals(Minestuck.MOD_ID))
				.map(Map.Entry::getValue).toList();
	}
}
