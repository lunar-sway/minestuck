package com.mraof.minestuck.data.loot_table;

import com.mraof.minestuck.Minestuck;
import com.mraof.minestuck.item.MSItems;
import com.mraof.minestuck.tileentity.ItemStackTileEntity;
import net.minecraft.advancements.criterion.EnchantmentPredicate;
import net.minecraft.advancements.criterion.ItemPredicate;
import net.minecraft.advancements.criterion.MinMaxBounds;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.data.loot.BlockLootTables;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.item.Items;
import net.minecraft.loot.*;
import net.minecraft.loot.conditions.ILootCondition;
import net.minecraft.loot.conditions.MatchTool;
import net.minecraft.loot.conditions.TableBonus;
import net.minecraft.loot.functions.ApplyBonus;
import net.minecraft.loot.functions.CopyName;
import net.minecraft.loot.functions.CopyNbt;
import net.minecraft.loot.functions.SetCount;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.stream.Collectors;

import static com.mraof.minestuck.block.MSBlocks.*;

public class MSBlockLootTables extends BlockLootTables
{
	private static final ILootCondition.IBuilder SILK_TOUCH_CONDITION = MatchTool.toolMatches(ItemPredicate.Builder.item().hasEnchantment(new EnchantmentPredicate(Enchantments.SILK_TOUCH, MinMaxBounds.IntBound.atLeast(1))));
	private static final ILootCondition.IBuilder SHEAR_CONDITION = MatchTool.toolMatches(ItemPredicate.Builder.item().of(Items.SHEARS));
	private static final ILootCondition.IBuilder SILK_AND_SHEAR_CONDITION = SHEAR_CONDITION.or(SILK_TOUCH_CONDITION);
	private static final ILootCondition.IBuilder NO_SILK_OR_SHEAR_CONDITION = SILK_AND_SHEAR_CONDITION.invert();
	private static final float[] SAPLING_CHANCES = new float[]{0.05F, 0.0625F, 0.083333336F, 0.1F};
	
	@Override
	protected void addTables()
	{
		dropSelf(BLACK_CHESS_DIRT);
		dropSelf(WHITE_CHESS_DIRT);
		dropSelf(DARK_GRAY_CHESS_DIRT);
		dropSelf(LIGHT_GRAY_CHESS_DIRT);
		
		dropSelf(BLACK_CASTLE_BRICKS);
		dropSelf(WHITE_CASTLE_BRICKS);
		dropSelf(LIGHT_GRAY_CASTLE_BRICKS);
		dropSelf(DARK_GRAY_CASTLE_BRICKS);
		dropSelf(BLACK_CASTLE_BRICK_SMOOTH);
		dropSelf(WHITE_CASTLE_BRICK_SMOOTH);
		dropSelf(LIGHT_GRAY_CASTLE_BRICK_SMOOTH);
		dropSelf(DARK_GRAY_CASTLE_BRICK_SMOOTH);
		dropSelf(BLACK_CASTLE_BRICK_TRIM);
		dropSelf(WHITE_CASTLE_BRICK_TRIM);
		dropSelf(LIGHT_GRAY_CASTLE_BRICK_TRIM);
		dropSelf(DARK_GRAY_CASTLE_BRICK_TRIM);
		dropSelf(CHECKERED_STAINED_GLASS);
		dropSelf(BLACK_CROWN_STAINED_GLASS);
		dropSelf(BLACK_PAWN_STAINED_GLASS);
		dropSelf(WHITE_CROWN_STAINED_GLASS);
		dropSelf(WHITE_PAWN_STAINED_GLASS);
		
		add(STONE_CRUXITE_ORE, MSBlockLootTables::cruxiteOreDrop);
		add(COBBLESTONE_CRUXITE_ORE, MSBlockLootTables::cruxiteOreDrop);
		add(SANDSTONE_CRUXITE_ORE, MSBlockLootTables::cruxiteOreDrop);
		add(RED_SANDSTONE_CRUXITE_ORE, MSBlockLootTables::cruxiteOreDrop);
		add(NETHERRACK_CRUXITE_ORE, MSBlockLootTables::cruxiteOreDrop);
		add(END_STONE_CRUXITE_ORE, MSBlockLootTables::cruxiteOreDrop);
		add(SHADE_STONE_CRUXITE_ORE, MSBlockLootTables::cruxiteOreDrop);
		add(PINK_STONE_CRUXITE_ORE, MSBlockLootTables::cruxiteOreDrop);
		add(STONE_URANIUM_ORE, MSBlockLootTables::uraniumOreDrop);
		add(COBBLESTONE_URANIUM_ORE, MSBlockLootTables::uraniumOreDrop);
		add(SANDSTONE_URANIUM_ORE, MSBlockLootTables::uraniumOreDrop);
		add(RED_SANDSTONE_URANIUM_ORE, MSBlockLootTables::uraniumOreDrop);
		add(NETHERRACK_URANIUM_ORE, MSBlockLootTables::uraniumOreDrop);
		add(END_STONE_URANIUM_ORE, MSBlockLootTables::uraniumOreDrop);
		add(SHADE_STONE_URANIUM_ORE, MSBlockLootTables::uraniumOreDrop);
		add(PINK_STONE_URANIUM_ORE, MSBlockLootTables::uraniumOreDrop);
		
		add(NETHERRACK_COAL_ORE, MSBlockLootTables::coalOreDrop);
		add(SHADE_STONE_COAL_ORE, MSBlockLootTables::coalOreDrop);
		add(PINK_STONE_COAL_ORE, MSBlockLootTables::coalOreDrop);
		dropSelf(SANDSTONE_IRON_ORE);
		dropSelf(RED_SANDSTONE_IRON_ORE);
		dropSelf(END_STONE_IRON_ORE);
		dropSelf(SANDSTONE_GOLD_ORE);
		dropSelf(RED_SANDSTONE_GOLD_ORE);
		dropSelf(SHADE_STONE_GOLD_ORE);
		dropSelf(PINK_STONE_GOLD_ORE);
		add(END_STONE_REDSTONE_ORE, MSBlockLootTables::redstoneOreDrop);
		add(STONE_QUARTZ_ORE, MSBlockLootTables::quartzOreDrop);
		add(PINK_STONE_LAPIS_ORE, MSBlockLootTables::lapisOreDrop);
		add(PINK_STONE_DIAMOND_ORE, MSBlockLootTables::diamondOreDrop);
		
		dropSelf(CRUXITE_BLOCK);
		dropSelf(URANIUM_BLOCK);
		dropSelf(GENERIC_OBJECT);
		
		dropSelf(BLUE_DIRT);
		dropSelf(THOUGHT_DIRT);
		dropSelf(COARSE_STONE);
		dropSelf(CHISELED_COARSE_STONE);
		dropSelf(COARSE_STONE_BRICKS);
		dropSelf(COARSE_STONE_COLUMN);
		dropSelf(CHISELED_COARSE_STONE_BRICKS);
		dropSelf(CRACKED_COARSE_STONE_BRICKS);
		dropSelf(MOSSY_COARSE_STONE);
		dropSelf(SHADE_STONE);
		dropSelf(SMOOTH_SHADE_STONE);
		dropSelf(SHADE_BRICKS);
		dropSelf(SHADE_COLUMN);
		dropSelf(CHISELED_SHADE_BRICKS);
		dropSelf(CRACKED_SHADE_BRICKS);
		dropSelf(MOSSY_SHADE_BRICKS);
		dropSelf(BLOOD_SHADE_BRICKS);
		dropSelf(TAR_SHADE_BRICKS);
		dropSelf(FROST_TILE);
		dropSelf(CHISELED_FROST_TILE);
		dropSelf(FROST_BRICKS);
		dropSelf(FROST_COLUMN);
		dropSelf(CHISELED_FROST_BRICKS);
		dropSelf(CRACKED_FROST_BRICKS);
		dropSelf(FLOWERY_FROST_BRICKS);
		dropSelf(CAST_IRON);
		dropSelf(CHISELED_CAST_IRON);
		dropSelf(STEEL_BEAM);
		dropSelf(MYCELIUM_COBBLESTONE);
		add(MYCELIUM_STONE, createSingleItemTableWithSilkTouch(MYCELIUM_STONE, MYCELIUM_COBBLESTONE));
		dropSelf(POLISHED_MYCELIUM_STONE);
		dropSelf(MYCELIUM_BRICKS);
		dropSelf(MYCELIUM_COLUMN);
		dropSelf(CHISELED_MYCELIUM_BRICKS);
		dropSelf(CRACKED_MYCELIUM_BRICKS);
		dropSelf(MOSSY_MYCELIUM_BRICKS);
		dropSelf(FLOWERY_MYCELIUM_BRICKS);
		add(BLACK_STONE, createSingleItemTableWithSilkTouch(BLACK_STONE, BLACK_COBBLESTONE));
		dropSelf(BLACK_COBBLESTONE);
		dropSelf(POLISHED_BLACK_STONE);
		dropSelf(BLACK_STONE_BRICKS);
		dropSelf(BLACK_STONE_COLUMN);
		dropSelf(CHISELED_BLACK_STONE_BRICKS);
		dropSelf(CRACKED_BLACK_STONE_BRICKS);
		dropSelf(BLACK_SAND);
		dropSelf(DECREPIT_STONE_BRICKS);
		dropSelf(FLOWERY_MOSSY_COBBLESTONE);
		dropSelf(MOSSY_DECREPIT_STONE_BRICKS);
		dropSelf(FLOWERY_MOSSY_STONE_BRICKS);
		dropSelf(COARSE_END_STONE);
		add(END_GRASS, MSBlockLootTables::endGrassDrop);
		dropSelf(CHALK);
		dropSelf(POLISHED_CHALK);
		dropSelf(CHALK_BRICKS);
		dropSelf(CHALK_COLUMN);
		dropSelf(CHISELED_CHALK_BRICKS);
		dropSelf(MOSSY_CHALK_BRICKS);
		dropSelf(FLOWERY_CHALK_BRICKS);
		dropSelf(PINK_STONE);
		dropSelf(POLISHED_PINK_STONE);
		dropSelf(PINK_STONE_BRICKS);
		dropSelf(CHISELED_PINK_STONE_BRICKS);
		dropSelf(CRACKED_PINK_STONE_BRICKS);
		dropSelf(MOSSY_PINK_STONE_BRICKS);
		dropSelf(PINK_STONE_COLUMN);
		dropSelf(BROWN_STONE);
		dropSelf(POLISHED_BROWN_STONE);
		dropSelf(BROWN_STONE_BRICKS);
		dropSelf(BROWN_STONE_COLUMN);
		dropSelf(CRACKED_BROWN_STONE_BRICKS);
		dropSelf(GREEN_STONE);
		dropSelf(POLISHED_GREEN_STONE);
		dropSelf(GREEN_STONE_BRICKS);
		dropSelf(GREEN_STONE_COLUMN);
		dropSelf(CHISELED_GREEN_STONE_BRICKS);
		dropSelf(HORIZONTAL_GREEN_STONE_BRICKS);
		dropSelf(VERTICAL_GREEN_STONE_BRICKS);
		dropSelf(GREEN_STONE_BRICK_TRIM);
		dropSelf(GREEN_STONE_BRICK_FROG);
		dropSelf(GREEN_STONE_BRICK_IGUANA_LEFT);
		dropSelf(GREEN_STONE_BRICK_IGUANA_RIGHT);
		dropSelf(GREEN_STONE_BRICK_LOTUS);
		dropSelf(GREEN_STONE_BRICK_NAK_LEFT);
		dropSelf(GREEN_STONE_BRICK_NAK_RIGHT);
		dropSelf(GREEN_STONE_BRICK_SALAMANDER_LEFT);
		dropSelf(GREEN_STONE_BRICK_SALAMANDER_RIGHT);
		dropSelf(GREEN_STONE_BRICK_SKAIA);
		dropSelf(GREEN_STONE_BRICK_TURTLE);
		dropSelf(SANDSTONE_COLUMN);
		dropSelf(CHISELED_SANDSTONE_COLUMN);
		dropSelf(RED_SANDSTONE_COLUMN);
		dropSelf(CHISELED_RED_SANDSTONE_COLUMN);
		add(UNCARVED_WOOD, createSingleItemTableWithSilkTouch(UNCARVED_WOOD, CHIPBOARD));
		dropSelf(CHIPBOARD);
		dropSelf(WOOD_SHAVINGS);
		dropSelf(DENSE_CLOUD);
		dropSelf(BRIGHT_DENSE_CLOUD);
		dropSelf(SUGAR_CUBE);
		dropSelf(SPIKES);
		
		dropSelf(GLOWING_LOG);
		dropSelf(FROST_LOG);
		dropSelf(RAINBOW_LOG);
		dropSelf(END_LOG);
		dropSelf(VINE_LOG);
		dropSelf(FLOWERY_VINE_LOG);
		dropSelf(DEAD_LOG);
		dropSelf(PETRIFIED_LOG);
		dropSelf(GLOWING_WOOD);
		dropSelf(FROST_WOOD);
		dropSelf(RAINBOW_WOOD);
		dropSelf(END_WOOD);
		dropSelf(VINE_WOOD);
		dropSelf(FLOWERY_VINE_WOOD);
		dropSelf(DEAD_WOOD);
		dropSelf(PETRIFIED_WOOD);
		dropSelf(GLOWING_PLANKS);
		dropSelf(FROST_PLANKS);
		dropSelf(RAINBOW_PLANKS);
		dropSelf(END_PLANKS);
		dropSelf(DEAD_PLANKS);
		dropSelf(TREATED_PLANKS);
		add(FROST_LEAVES, MSBlockLootTables::frostLeavesDrop);
		add(RAINBOW_LEAVES, MSBlockLootTables::rainbowLeavesDrop);
		add(END_LEAVES, MSBlockLootTables::endLeavesDrop);
		dropSelf(RAINBOW_SAPLING);
		dropSelf(END_SAPLING);
		
		dropSelf(BLOOD_ASPECT_LOG);
		dropSelf(BREATH_ASPECT_LOG);
		dropSelf(DOOM_ASPECT_LOG);
		dropSelf(HEART_ASPECT_LOG);
		dropSelf(HOPE_ASPECT_LOG);
		dropSelf(LIFE_ASPECT_LOG);
		dropSelf(LIGHT_ASPECT_LOG);
		dropSelf(MIND_ASPECT_LOG);
		dropSelf(RAGE_ASPECT_LOG);
		dropSelf(SPACE_ASPECT_LOG);
		dropSelf(TIME_ASPECT_LOG);
		dropSelf(VOID_ASPECT_LOG);
		dropSelf(BLOOD_ASPECT_PLANKS);
		dropSelf(BREATH_ASPECT_PLANKS);
		dropSelf(DOOM_ASPECT_PLANKS);
		dropSelf(HEART_ASPECT_PLANKS);
		dropSelf(HOPE_ASPECT_PLANKS);
		dropSelf(LIFE_ASPECT_PLANKS);
		dropSelf(LIGHT_ASPECT_PLANKS);
		dropSelf(MIND_ASPECT_PLANKS);
		dropSelf(RAGE_ASPECT_PLANKS);
		dropSelf(SPACE_ASPECT_PLANKS);
		dropSelf(TIME_ASPECT_PLANKS);
		dropSelf(VOID_ASPECT_PLANKS);
		add(BLOOD_ASPECT_LEAVES, MSBlockLootTables::bloodAspectLeavesDrop);
		add(BREATH_ASPECT_LEAVES, MSBlockLootTables::breathAspectLeavesDrop);
		add(DOOM_ASPECT_LEAVES, MSBlockLootTables::doomAspectLeavesDrop);
		add(HEART_ASPECT_LEAVES, MSBlockLootTables::heartAspectLeavesDrop);
		add(HOPE_ASPECT_LEAVES, MSBlockLootTables::hopeAspectLeavesDrop);
		add(LIFE_ASPECT_LEAVES, MSBlockLootTables::lifeAspectLeavesDrop);
		add(LIGHT_ASPECT_LEAVES, MSBlockLootTables::lightAspectLeavesDrop);
		add(MIND_ASPECT_LEAVES, MSBlockLootTables::mindAspectLeavesDrop);
		add(RAGE_ASPECT_LEAVES, MSBlockLootTables::rageAspectLeavesDrop);
		add(SPACE_ASPECT_LEAVES, MSBlockLootTables::spaceAspectLeavesDrop);
		add(TIME_ASPECT_LEAVES, MSBlockLootTables::timeAspectLeavesDrop);
		add(VOID_ASPECT_LEAVES, MSBlockLootTables::voidAspectLeavesDrop);
		dropSelf(BLOOD_ASPECT_SAPLING);
		dropSelf(BREATH_ASPECT_SAPLING);
		dropSelf(DOOM_ASPECT_SAPLING);
		dropSelf(HEART_ASPECT_SAPLING);
		dropSelf(HOPE_ASPECT_SAPLING);
		dropSelf(LIFE_ASPECT_SAPLING);
		dropSelf(LIGHT_ASPECT_SAPLING);
		dropSelf(MIND_ASPECT_SAPLING);
		dropSelf(RAGE_ASPECT_SAPLING);
		dropSelf(SPACE_ASPECT_SAPLING);
		dropSelf(TIME_ASPECT_SAPLING);
		dropSelf(VOID_ASPECT_SAPLING);
		
		dropSelf(GLOWING_MUSHROOM);
		add(DESERT_BUSH, MSBlockLootTables::desertBushDrop);
		dropSelf(BLOOMING_CACTUS);
		dropSelf(PETRIFIED_GRASS);
		dropSelf(PETRIFIED_POPPY);
		dropSelf(STRAWBERRY);
		add(ATTACHED_STRAWBERRY_STEM, noDrop());	//TODO vanilla has a different loot table for their attached stems, should we replicate?
		add(STRAWBERRY_STEM, MSBlockLootTables::strawberryStemDrop);
		add(TALL_END_GRASS, noDrop());
		dropSelf(GLOWFLOWER);
		
		dropSelf(GLOWY_GOOP);
		dropSelf(COAGULATED_BLOOD);
		add(VEIN, noDrop());
		add(VEIN_CORNER, noDrop());
		add(INVERTED_VEIN_CORNER, noDrop());
		dropSelf(PIPE);
		dropSelf(PIPE_INTERSECTION);
		dropSelf(PARCEL_PYXIS);
		dropSelf(PYXIS_LID);
		add(STONE_SLAB, MSBlockLootTables::droppingWithTEItem);
		dropSelf(NAKAGATOR_STATUE);
		
		dropSelf(BLACK_CASTLE_BRICK_STAIRS);
		dropSelf(DARK_GRAY_CASTLE_BRICK_STAIRS);
		dropSelf(LIGHT_GRAY_CASTLE_BRICK_STAIRS);
		dropSelf(WHITE_CASTLE_BRICK_STAIRS);
		dropSelf(COARSE_STONE_STAIRS);
		dropSelf(COARSE_STONE_BRICK_STAIRS);
		dropSelf(SHADE_STAIRS);
		dropSelf(SHADE_BRICK_STAIRS);
		dropSelf(FROST_TILE_STAIRS);
		dropSelf(FROST_BRICK_STAIRS);
		dropSelf(CAST_IRON_STAIRS);
		dropSelf(BLACK_STONE_STAIRS);
		dropSelf(BLACK_STONE_BRICK_STAIRS);
		dropSelf(MYCELIUM_STAIRS);
		dropSelf(MYCELIUM_BRICK_STAIRS);
		dropSelf(CHALK_STAIRS);
		dropSelf(CHALK_BRICK_STAIRS);
		dropSelf(PINK_STONE_STAIRS);
		dropSelf(PINK_STONE_BRICK_STAIRS);
		dropSelf(BROWN_STONE_STAIRS);
		dropSelf(BROWN_STONE_BRICK_STAIRS);
		dropSelf(GREEN_STONE_STAIRS);
		dropSelf(GREEN_STONE_BRICK_STAIRS);
		dropSelf(RAINBOW_PLANKS_STAIRS);
		dropSelf(END_PLANKS_STAIRS);
		dropSelf(DEAD_PLANKS_STAIRS);
		dropSelf(TREATED_PLANKS_STAIRS);
		dropSelf(FLOWERY_MOSSY_STONE_BRICK_STAIRS);
		dropSelf(STEEP_GREEN_STONE_BRICK_STAIRS_BASE);
		dropSelf(STEEP_GREEN_STONE_BRICK_STAIRS_TOP);
		dropSelf(BLACK_CASTLE_BRICK_SLAB);
		dropSelf(DARK_GRAY_CASTLE_BRICK_SLAB);
		dropSelf(LIGHT_GRAY_CASTLE_BRICK_SLAB);
		dropSelf(WHITE_CASTLE_BRICK_SLAB);
		dropSelf(CHALK_SLAB);
		dropSelf(CHALK_BRICK_SLAB);
		dropSelf(PINK_STONE_SLAB);
		dropSelf(PINK_STONE_BRICK_SLAB);
		dropSelf(BROWN_STONE_SLAB);
		dropSelf(BROWN_STONE_BRICK_SLAB);
		dropSelf(GREEN_STONE_SLAB);
		dropSelf(GREEN_STONE_BRICK_SLAB);
		dropSelf(RAINBOW_PLANKS_SLAB);
		dropSelf(END_PLANKS_SLAB);
		dropSelf(DEAD_PLANKS_SLAB);
		dropSelf(TREATED_PLANKS_SLAB);
		dropSelf(BLACK_STONE_SLAB);
		dropSelf(BLACK_STONE_BRICK_SLAB);
		dropSelf(MYCELIUM_SLAB);
		dropSelf(MYCELIUM_BRICK_SLAB);
		dropSelf(FLOWERY_MOSSY_STONE_BRICK_SLAB);
		dropSelf(FROST_TILE_SLAB);
		dropSelf(FROST_BRICK_SLAB);
		dropSelf(SHADE_SLAB);
		dropSelf(SHADE_BRICK_SLAB);
		dropSelf(COARSE_STONE_SLAB);
		dropSelf(COARSE_STONE_BRICK_SLAB);
		
		dropSelf(TRAJECTORY_BLOCK);
		dropSelf(STAT_STORER);
		dropSelf(REMOTE_OBSERVER);
		dropSelf(WIRELESS_REDSTONE_TRANSMITTER);
		dropSelf(WIRELESS_REDSTONE_RECEIVER);
		dropSelf(SOLID_SWITCH);
		dropSelf(VARIABLE_SOLID_SWITCH);
		dropSelf(ONE_SECOND_INTERVAL_TIMED_SOLID_SWITCH);
		dropSelf(TWO_SECOND_INTERVAL_TIMED_SOLID_SWITCH);
		add(SUMMONER, noDrop());
		add(AREA_EFFECT_BLOCK, noDrop());
		dropSelf(PLATFORM_GENERATOR);
		add(PLATFORM_BLOCK, noDrop());
		dropSelf(PLATFORM_RECEPTACLE);
		dropSelf(ITEM_MAGNET);
		dropSelf(REDSTONE_CLOCK);
		dropSelf(ROTATOR);
		dropSelf(FALL_PAD);
		dropSelf(FRAGILE_STONE);
		dropSelf(RETRACTABLE_SPIKES);
		dropSelf(AND_GATE_BLOCK);
		dropSelf(OR_GATE_BLOCK);
		dropSelf(XOR_GATE_BLOCK);
		dropSelf(NAND_GATE_BLOCK);
		dropSelf(NOR_GATE_BLOCK);
		dropSelf(XNOR_GATE_BLOCK);
		
		dropSelf(HOLOPAD);
		dropSelf(CRUXTRUDER_LID);
		add(MINI_CRUXTRUDER, MSBlockLootTables::droppingWithColor);
		dropSelf(MINI_TOTEM_LATHE);
		dropSelf(MINI_ALCHEMITER);
		dropSelf(MINI_PUNCH_DESIGNIX);
		
		dropSelf(COMPUTER);
		dropSelf(LAPTOP);
		dropSelf(CROCKERTOP);
		dropSelf(HUBTOP);
		dropSelf(LUNCHTOP);
		dropSelf(OLD_COMPUTER);
		add(TRANSPORTALIZER, MSBlockLootTables::droppingWithNameOnSilkTouch);
		add(TRANS_PORTALIZER, MSBlockLootTables::droppingWithNameOnSilkTouch);
		dropSelf(SENDIFICATOR);
		dropSelf(GRIST_WIDGET);
		dropSelf(URANIUM_COOKER);
		
		add(CRUXITE_DOWEL, MSBlockLootTables::droppingWithTEItem);
		
		dropSelf(GOLD_SEEDS);
		dropSelf(WOODEN_CACTUS);
		
		add(APPLE_CAKE, noDrop());
		add(BLUE_CAKE, noDrop());
		add(COLD_CAKE, noDrop());
		add(RED_CAKE, noDrop());
		add(HOT_CAKE, noDrop());
		add(REVERSE_CAKE, noDrop());
		add(FUCHSIA_CAKE, noDrop());
		add(NEGATIVE_CAKE, noDrop());
		add(CARROT_CAKE, noDrop());
		
		dropSelf(PRIMED_TNT);
		dropSelf(UNSTABLE_TNT);
		dropSelf(INSTANT_TNT);
		dropSelf(WOODEN_EXPLOSIVE_BUTTON);
		dropSelf(STONE_EXPLOSIVE_BUTTON);
		
		dropSelf(BLENDER);
		dropSelf(CHESSBOARD);
		dropSelf(MINI_FROG_STATUE);
		dropSelf(MINI_WIZARD_STATUE);
		dropSelf(MINI_TYPHEUS_STATUE);
		dropSelf(CASSETTE_PLAYER);
		dropSelf(GLOWYSTONE_DUST);
	}
	
	private static LootTable.Builder cruxiteOreDrop(Block block)
	{
		return createSilkTouchDispatchTable(block, applyExplosionDecay(block, ItemLootEntry.lootTableItem(MSItems.RAW_CRUXITE).apply(SetCount.setCount(RandomValueRange.between(2.0F, 5.0F))).apply(ApplyBonus.addOreBonusCount(Enchantments.BLOCK_FORTUNE))));
	}
	private static LootTable.Builder uraniumOreDrop(Block block)
	{
		return createOreDrop(block, MSItems.RAW_URANIUM);
	}
	private static LootTable.Builder coalOreDrop(Block block)
	{
		return createOreDrop(block, Items.COAL);
	}
	private static LootTable.Builder redstoneOreDrop(Block block)
	{
		return createSilkTouchDispatchTable(block, applyExplosionDecay(block, ItemLootEntry.lootTableItem(Items.REDSTONE).apply(SetCount.setCount(RandomValueRange.between(4.0F, 5.0F))).apply(ApplyBonus.addUniformBonusCount(Enchantments.BLOCK_FORTUNE))));
	}
	private static LootTable.Builder quartzOreDrop(Block block)
	{
		return createOreDrop(block, Items.QUARTZ);
	}
	private static LootTable.Builder lapisOreDrop(Block block)
	{
		return createSilkTouchDispatchTable(block, applyExplosionDecay(block, ItemLootEntry.lootTableItem(Items.LAPIS_LAZULI).apply(SetCount.setCount(RandomValueRange.between(4.0F, 9.0F))).apply(ApplyBonus.addOreBonusCount(Enchantments.BLOCK_FORTUNE))));
	}
	private static LootTable.Builder diamondOreDrop(Block block)
	{
		return createOreDrop(block, Items.DIAMOND);
	}
	private static LootTable.Builder endGrassDrop(Block block)
	{
		return createSingleItemTableWithSilkTouch(block, Blocks.END_STONE);
	}
	private static LootTable.Builder frostLeavesDrop(Block block)
	{
		return createLeavesDrops(block, Blocks.AIR, SAPLING_CHANCES);
	}
	private static LootTable.Builder rainbowLeavesDrop(Block block)
	{
		return createLeavesDrops(block, RAINBOW_SAPLING, SAPLING_CHANCES);
	}
	private static LootTable.Builder endLeavesDrop(Block block)
	{
		return createLeavesDrops(block, END_SAPLING, SAPLING_CHANCES).withPool(LootPool.lootPool().setRolls(ConstantRange.exactly(1)).when(NO_SILK_OR_SHEAR_CONDITION).add(applyExplosionCondition(block, ItemLootEntry.lootTableItem(Items.CHORUS_FRUIT)).when(TableBonus.bonusLevelFlatChance(Enchantments.BLOCK_FORTUNE, 0.005F, 0.0055555557F, 0.00625F, 0.008333334F, 0.025F))));
	}
	private static LootTable.Builder bloodAspectLeavesDrop(Block block)
	{
		return createLeavesDrops(block, BLOOD_ASPECT_SAPLING, SAPLING_CHANCES);
	}
	private static LootTable.Builder breathAspectLeavesDrop(Block block)
	{
		return createLeavesDrops(block, BREATH_ASPECT_SAPLING, SAPLING_CHANCES);
	}
	private static LootTable.Builder doomAspectLeavesDrop(Block block)
	{
		return createLeavesDrops(block, DOOM_ASPECT_SAPLING, SAPLING_CHANCES);
	}
	private static LootTable.Builder heartAspectLeavesDrop(Block block)
	{
		return createLeavesDrops(block, HEART_ASPECT_SAPLING, SAPLING_CHANCES);
	}
	private static LootTable.Builder hopeAspectLeavesDrop(Block block)
	{
		return createLeavesDrops(block, HOPE_ASPECT_SAPLING, SAPLING_CHANCES);
	}
	private static LootTable.Builder lifeAspectLeavesDrop(Block block)
	{
		return createLeavesDrops(block, LIFE_ASPECT_SAPLING, SAPLING_CHANCES);
	}
	private static LootTable.Builder lightAspectLeavesDrop(Block block)
	{
		return createLeavesDrops(block, LIGHT_ASPECT_SAPLING, SAPLING_CHANCES);
	}
	private static LootTable.Builder mindAspectLeavesDrop(Block block)
	{
		return createLeavesDrops(block, MIND_ASPECT_SAPLING, SAPLING_CHANCES);
	}
	private static LootTable.Builder rageAspectLeavesDrop(Block block)
	{
		return createLeavesDrops(block, RAGE_ASPECT_SAPLING, SAPLING_CHANCES);
	}
	private static LootTable.Builder spaceAspectLeavesDrop(Block block)
	{
		return createLeavesDrops(block, SPACE_ASPECT_SAPLING, SAPLING_CHANCES);
	}
	private static LootTable.Builder timeAspectLeavesDrop(Block block)
	{
		return createLeavesDrops(block, TIME_ASPECT_SAPLING, SAPLING_CHANCES);
	}
	private static LootTable.Builder voidAspectLeavesDrop(Block block)
	{
		return createLeavesDrops(block, VOID_ASPECT_SAPLING, SAPLING_CHANCES);
	}
	private static LootTable.Builder desertBushDrop(Block block)
	{
		return createSilkTouchDispatchTable(block, applyExplosionDecay(block, ItemLootEntry.lootTableItem(MSItems.DESERT_FRUIT).apply(SetCount.setCount(RandomValueRange.between(3.0F, 6.0F))).apply(ApplyBonus.addUniformBonusCount(Enchantments.BLOCK_FORTUNE))));
	}
	private static LootTable.Builder strawberryStemDrop(Block block)
	{
		return createStemDrops(block, MSItems.STRAWBERRY_CHUNK);
	}
	protected static LootTable.Builder droppingWithColor(Block block)
	{
		return LootTable.lootTable().withPool(applyExplosionCondition(block, LootPool.lootPool().setRolls(ConstantRange.exactly(1)).add(ItemLootEntry.lootTableItem(block).apply(CopyNbt.copyData(CopyNbt.Source.BLOCK_ENTITY).copy("color", "color")))));
	}
	protected static LootTable.Builder droppingWithTEItem(Block block)
	{
		return LootTable.lootTable().withPool(applyExplosionCondition(block, LootPool.lootPool().setRolls(ConstantRange.exactly(1)).add(DynamicLootEntry.dynamicEntry(ItemStackTileEntity.ITEM_DYNAMIC))));
	}
	protected static LootTable.Builder droppingWithNameOnSilkTouch(Block block)
	{
		return createSelfDropDispatchTable(block, SILK_TOUCH_CONDITION.invert(), ItemLootEntry.lootTableItem(block).apply(CopyName.copyName(CopyName.Source.BLOCK_ENTITY)));
	}
	
	@Override
	protected Iterable<Block> getKnownBlocks()
	{
		return ForgeRegistries.BLOCKS.getValues().stream().filter(block -> block.getRegistryName().getNamespace().equals(Minestuck.MOD_ID)).collect(Collectors.toList());
	}
}
