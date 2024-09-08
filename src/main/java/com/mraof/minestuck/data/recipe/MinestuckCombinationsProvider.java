package com.mraof.minestuck.data.recipe;

import com.mraof.minestuck.Minestuck;
import com.mraof.minestuck.api.alchemy.recipe.combination.CombinationRecipeBuilder;
import com.mraof.minestuck.block.BlockCollections;
import com.mraof.minestuck.block.MSBlocks;
import com.mraof.minestuck.block.SkaiaBlocks;
import com.mraof.minestuck.item.MSItems;
import com.mraof.minestuck.util.MSTags;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.neoforged.neoforge.common.Tags;

import java.util.Arrays;
import java.util.List;
import java.util.function.Function;

public final class MinestuckCombinationsProvider
{
	public static void buildRecipes(RecipeOutput consumer)
	{
		//Wood
		final ItemLike[][] woodItems = {
				{Blocks.OAK_LOG, Blocks.SPRUCE_LOG, Blocks.BIRCH_LOG, Blocks.JUNGLE_LOG, Blocks.ACACIA_LOG, Blocks.DARK_OAK_LOG, Blocks.MANGROVE_LOG, Blocks.CHERRY_LOG},
				{Blocks.OAK_PLANKS, Blocks.SPRUCE_PLANKS, Blocks.BIRCH_PLANKS, Blocks.JUNGLE_PLANKS, Blocks.ACACIA_PLANKS, Blocks.DARK_OAK_PLANKS, Blocks.MANGROVE_PLANKS, Blocks.CHERRY_PLANKS},
				{Blocks.OAK_SLAB, Blocks.SPRUCE_SLAB, Blocks.BIRCH_SLAB, Blocks.JUNGLE_SLAB, Blocks.ACACIA_SLAB, Blocks.DARK_OAK_SLAB, Blocks.MANGROVE_SLAB, Blocks.CHERRY_SLAB},
				{Blocks.OAK_STAIRS, Blocks.SPRUCE_STAIRS, Blocks.BIRCH_STAIRS, Blocks.JUNGLE_STAIRS, Blocks.ACACIA_STAIRS, Blocks.DARK_OAK_STAIRS, Blocks.MANGROVE_STAIRS, Blocks.CHERRY_STAIRS},
				{Blocks.OAK_SAPLING, Blocks.SPRUCE_SAPLING, Blocks.BIRCH_SAPLING, Blocks.JUNGLE_SAPLING, Blocks.ACACIA_SAPLING, Blocks.DARK_OAK_SAPLING, Blocks.MANGROVE_PROPAGULE, Blocks.CHERRY_SAPLING},
				{Blocks.OAK_LEAVES, Blocks.SPRUCE_LEAVES, Blocks.BIRCH_LEAVES, Blocks.JUNGLE_LEAVES,Blocks.ACACIA_LEAVES, Blocks.DARK_OAK_LEAVES, Blocks.MANGROVE_LEAVES, Blocks.CHERRY_LEAVES},
				{Blocks.OAK_DOOR, Blocks.SPRUCE_DOOR, Blocks.BIRCH_DOOR, Blocks.JUNGLE_DOOR, Blocks.ACACIA_DOOR, Blocks.DARK_OAK_DOOR, Blocks.MANGROVE_DOOR, Blocks.CHERRY_DOOR},
				{Blocks.OAK_FENCE, Blocks.SPRUCE_FENCE, Blocks.BIRCH_FENCE, Blocks.JUNGLE_FENCE, Blocks.ACACIA_FENCE, Blocks.DARK_OAK_FENCE, Blocks.MANGROVE_FENCE, Blocks.CHERRY_FENCE},
				{Blocks.OAK_FENCE_GATE, Blocks.SPRUCE_FENCE_GATE, Blocks.BIRCH_FENCE_GATE, Blocks.JUNGLE_FENCE_GATE, Blocks.ACACIA_FENCE_GATE, Blocks.DARK_OAK_FENCE_GATE, Blocks.MANGROVE_FENCE_GATE, Blocks.CHERRY_FENCE_GATE},
				{Blocks.OAK_TRAPDOOR, Blocks.SPRUCE_TRAPDOOR, Blocks.BIRCH_TRAPDOOR, Blocks.JUNGLE_TRAPDOOR, Blocks.ACACIA_TRAPDOOR, Blocks.DARK_OAK_TRAPDOOR, Blocks.MANGROVE_TRAPDOOR, Blocks.CHERRY_TRAPDOOR}};
		//0: oak, 1: spruce, 2: birch, 3: jungle, 4: acacia, 5: dark oak, 6: mangrove, 7: cherry
		// [0] || [1] -> [2]
		int[][] woodCombinations = {{0, 1, 5}, {2, 3, 4}};
		//0: log, 1: planks, 2: slab, 3: stairs, 4: sapling, 5: leaves, 6: door, 7: fence, 8: fence gate, 9: trapdoor
		// [0] || [1] -> [2]
		int[][] itemCombinations = {{1, 2, 3}, {0, 5, 4}, {6, 7, 8}, {6, 2, 9}};
		
		for(ItemLike[] itemType : woodItems)
		{
			for(int[] combination : woodCombinations)
				CombinationRecipeBuilder.of(itemType[combination[2]]).input(itemType[combination[0]]).or().input(itemType[combination[1]]).namedSource("wood_combination").buildFor(consumer, Minestuck.MOD_ID);
		}
		for(int woodType = 0; woodType < woodItems[0].length; woodType++)
		{
			for(int[] combination : itemCombinations)
				CombinationRecipeBuilder.of(woodItems[combination[2]][woodType]).input(woodItems[combination[0]][woodType]).or().input(woodItems[combination[1]][woodType]).namedSource("item_combination").buildFor(consumer, Minestuck.MOD_ID);
			CombinationRecipeBuilder.of(woodItems[4][woodType]).namedInput(Items.WHEAT_SEEDS).and().input(woodItems[5][woodType]).buildFor(consumer, Minestuck.MOD_ID);	//seeds || leaves -> sapling
			CombinationRecipeBuilder.of(woodItems[4][woodType]).namedInput(Items.STICK).and().input(woodItems[5][woodType]).buildFor(consumer, Minestuck.MOD_ID);	//stick || leaves -> sapling
		}
		for(int[] wood : woodCombinations)
		{
			for(int[] item : itemCombinations)
			{
				CombinationRecipeBuilder.of(woodItems[item[2]][wood[2]]).namedInput(woodItems[item[0]][wood[0]]).or().namedInput(woodItems[item[1]][wood[1]]).buildFor(consumer, Minestuck.MOD_ID);
				CombinationRecipeBuilder.of(woodItems[item[2]][wood[2]]).namedInput(woodItems[item[0]][wood[1]]).or().namedInput(woodItems[item[1]][wood[0]]).buildFor(consumer, Minestuck.MOD_ID);
			}
		}
		
		List<Function<DyeColor, Block>> coloredBlocks = Arrays.asList(BlockCollections::coloredWool, BlockCollections::coloredTerracotta, BlockCollections::coloredGlass, BlockCollections::coloredGlassPane);
		for(Function<DyeColor, Block> blockProvider : coloredBlocks)
		{
			for (DyeColor color : DyeColor.values())
			{
				if(color != DyeColor.WHITE)
					CombinationRecipeBuilder.of(blockProvider.apply(color)).input(color.getTag()).or().namedInput(blockProvider.apply(DyeColor.WHITE)).buildFor(consumer, Minestuck.MOD_ID);
			}
		}
		for (DyeColor color : DyeColor.values())
		{
			CombinationRecipeBuilder.of(BlockCollections.coloredGlass(color)).namedInput(Items.GLASS).and().input(color.getTag()).buildFor(consumer, Minestuck.MOD_ID);
			CombinationRecipeBuilder.of(BlockCollections.coloredGlassPane(color)).namedInput(Items.GLASS_PANE).and().input(color.getTag()).buildFor(consumer, Minestuck.MOD_ID);
			CombinationRecipeBuilder.of(BlockCollections.coloredTerracotta(color)).namedInput(Items.TERRACOTTA).and().input(color.getTag()).buildFor(consumer, Minestuck.MOD_ID);
			CombinationRecipeBuilder.of(BlockCollections.coloredConcrete(color)).namedInput(BlockCollections.coloredConcretePowder(color)).or().input(Items.WATER_BUCKET).buildFor(consumer, Minestuck.MOD_ID);
			
			if(color != DyeColor.LIGHT_GRAY)
				CombinationRecipeBuilder.of(BlockCollections.coloredConcretePowder(color)).input(color.getTag()).or().namedInput(BlockCollections.coloredConcretePowder(DyeColor.LIGHT_GRAY)).buildFor(consumer, Minestuck.MOD_ID);
		}
		//Precious Blocks
		CombinationRecipeBuilder.of(Items.COAL_BLOCK).input(Items.COAL).or().input(Items.STONE).buildFor(consumer, Minestuck.MOD_ID);
		CombinationRecipeBuilder.of(Items.DIAMOND_BLOCK).input(Items.DIAMOND).or().input(Items.STONE).buildFor(consumer, Minestuck.MOD_ID);
		CombinationRecipeBuilder.of(Items.LAPIS_BLOCK).input(Items.LAPIS_LAZULI).or().input(Items.STONE).buildFor(consumer, Minestuck.MOD_ID);
		CombinationRecipeBuilder.of(Items.EMERALD_BLOCK).input(Items.EMERALD).or().input(Items.STONE).buildFor(consumer, Minestuck.MOD_ID);
		CombinationRecipeBuilder.of(Items.COPPER_BLOCK).input(Items.COPPER_INGOT).or().input(Items.STONE).buildFor(consumer, Minestuck.MOD_ID);
		CombinationRecipeBuilder.of(Items.GOLD_BLOCK).input(Items.GOLD_INGOT).or().input(Items.STONE).buildFor(consumer, Minestuck.MOD_ID);
		CombinationRecipeBuilder.of(Items.IRON_BLOCK).input(Items.IRON_INGOT).or().input(Items.STONE).buildFor(consumer, Minestuck.MOD_ID);
		CombinationRecipeBuilder.of(Items.QUARTZ_BLOCK).input(Items.QUARTZ).or().input(Items.STONE).buildFor(consumer, Minestuck.MOD_ID);
		CombinationRecipeBuilder.of(Items.REDSTONE_BLOCK).input(Items.REDSTONE).or().input(Items.STONE).buildFor(consumer, Minestuck.MOD_ID);
		CombinationRecipeBuilder.of(Items.NETHERITE_BLOCK).input(Items.NETHERITE_INGOT).or().input(Items.STONE).buildFor(consumer, Minestuck.MOD_ID);
		CombinationRecipeBuilder.of(Items.CUT_COPPER_STAIRS).input(Items.COPPER_INGOT).or().input(Items.STONE_BRICK_STAIRS).buildFor(consumer, Minestuck.MOD_ID);
		CombinationRecipeBuilder.of(Items.CUT_COPPER_SLAB).input(Items.COPPER_INGOT).or().input(Items.STONE_BRICK_SLAB).buildFor(consumer, Minestuck.MOD_ID);
		CombinationRecipeBuilder.of(Items.CUT_COPPER).input(Items.COPPER_INGOT).or().input(Items.STONE_BRICKS).buildFor(consumer, Minestuck.MOD_ID);
		CombinationRecipeBuilder.of(Items.RAW_GOLD_BLOCK).input(Items.RAW_GOLD).and().namedInput(Items.STONE).buildFor(consumer, Minestuck.MOD_ID);
		CombinationRecipeBuilder.of(Items.RAW_IRON_BLOCK).input(Items.RAW_IRON).and().namedInput(Items.STONE).buildFor(consumer, Minestuck.MOD_ID);
		CombinationRecipeBuilder.of(Items.RAW_COPPER_BLOCK).input(Items.RAW_COPPER).and().namedInput(Items.STONE).buildFor(consumer, Minestuck.MOD_ID);
		//Ore blocks
		CombinationRecipeBuilder.of(Items.COAL_ORE).input(Items.COAL).and().input(Items.STONE).buildFor(consumer, Minestuck.MOD_ID);
		CombinationRecipeBuilder.of(Items.DIAMOND_ORE).input(Items.DIAMOND).and().input(Items.STONE).buildFor(consumer, Minestuck.MOD_ID);
		CombinationRecipeBuilder.of(Items.LAPIS_ORE).input(Items.LAPIS_LAZULI).and().input(Items.STONE).buildFor(consumer, Minestuck.MOD_ID);
		CombinationRecipeBuilder.of(Items.EMERALD_ORE).input(Items.EMERALD).and().input(Items.STONE).buildFor(consumer, Minestuck.MOD_ID);
		CombinationRecipeBuilder.of(Items.COPPER_ORE).input(Items.COPPER_INGOT).and().input(Items.STONE).buildFor(consumer, Minestuck.MOD_ID);
		CombinationRecipeBuilder.of(Items.GOLD_ORE).input(Items.GOLD_INGOT).and().input(Items.STONE).buildFor(consumer, Minestuck.MOD_ID);
		CombinationRecipeBuilder.of(Items.IRON_ORE).input(Items.IRON_INGOT).and().input(Items.STONE).buildFor(consumer, Minestuck.MOD_ID);
		CombinationRecipeBuilder.of(Items.NETHER_QUARTZ_ORE).input(Items.QUARTZ).and().input(Items.STONE).buildFor(consumer, Minestuck.MOD_ID);
		CombinationRecipeBuilder.of(Items.REDSTONE_ORE).input(Items.REDSTONE).and().input(Items.STONE).buildFor(consumer, Minestuck.MOD_ID);
		//Ores
		CombinationRecipeBuilder.of(Items.RAW_COPPER).input(Items.RAW_IRON).and().namedInput(Items.ORANGE_DYE).buildFor(consumer, Minestuck.MOD_ID);
		CombinationRecipeBuilder.of(Items.RAW_GOLD).input(Items.PORKCHOP).and().namedInput(Items.LIGHTNING_ROD).buildFor(consumer, Minestuck.MOD_ID);
		CombinationRecipeBuilder.of(Items.RAW_IRON).input(Items.STONE).or().namedInput(Items.GRAY_DYE).buildFor(consumer, Minestuck.MOD_ID);
		CombinationRecipeBuilder.of(Items.DIAMOND).input(Items.EMERALD).and().namedInput(Items.COAL).buildFor(consumer, Minestuck.MOD_ID);
		CombinationRecipeBuilder.of(Items.DIAMOND).input(Items.EMERALD).and().namedInput(Items.LAPIS_LAZULI).buildFor(consumer, Minestuck.MOD_ID);
		CombinationRecipeBuilder.of(Items.CHARCOAL).input(ItemTags.LOGS).and().input(Items.COAL).buildFor(consumer, Minestuck.MOD_ID);
		//Construction Blocks
		CombinationRecipeBuilder.of(Items.STONE_BRICKS).input(Items.STONE).and().input(Items.CRACKED_STONE_BRICKS).buildFor(consumer, Minestuck.MOD_ID);
		CombinationRecipeBuilder.of(Items.CRACKED_STONE_BRICKS).input(Items.COBBLESTONE).and().input(Items.STONE_BRICKS).buildFor(consumer, Minestuck.MOD_ID);
		CombinationRecipeBuilder.of(Items.COBBLESTONE).input(Items.STONE).or().namedInput(Items.CRACKED_STONE_BRICKS).buildFor(consumer, Minestuck.MOD_ID);
		CombinationRecipeBuilder.of(Items.COBBLESTONE).input(Items.STONE).or().namedInput(Items.GRAVEL).buildFor(consumer, Minestuck.MOD_ID);
		CombinationRecipeBuilder.of(Items.GRAVEL).input(ItemTags.SAND).and().input(Items.COBBLESTONE).buildFor(consumer, Minestuck.MOD_ID);
		CombinationRecipeBuilder.of(Items.STONE).input(Items.COBBLESTONE).and().namedInput(Items.CUT_SANDSTONE).buildFor(consumer, Minestuck.MOD_ID);
		CombinationRecipeBuilder.of(Items.SANDSTONE).namedInput(Items.COBBLESTONE).or().namedInput(Items.CUT_SANDSTONE).buildFor(consumer, Minestuck.MOD_ID);
		CombinationRecipeBuilder.of(Items.SANDSTONE).namedInput(Items.STONE).or().namedInput(Items.SAND).buildFor(consumer, Minestuck.MOD_ID);
		CombinationRecipeBuilder.of(Items.SANDSTONE).namedInput(Items.COBBLESTONE).or().namedInput(Items.SAND).buildFor(consumer, Minestuck.MOD_ID);
		CombinationRecipeBuilder.of(Items.SANDSTONE).namedInput(Items.GRAVEL).or().namedInput(Items.CUT_SANDSTONE).buildFor(consumer, Minestuck.MOD_ID);
		CombinationRecipeBuilder.of(Items.SANDSTONE).namedInput(Items.CUT_SANDSTONE).or().namedInput(Items.SAND).buildFor(consumer, Minestuck.MOD_ID);
		CombinationRecipeBuilder.of(Items.STONE).input(Items.COBBLESTONE).and().namedInput(Items.CUT_RED_SANDSTONE).buildFor(consumer, Minestuck.MOD_ID);
		CombinationRecipeBuilder.of(Items.RED_SANDSTONE).namedInput(Items.COBBLESTONE).or().namedInput(Items.CUT_RED_SANDSTONE).buildFor(consumer, Minestuck.MOD_ID);
		CombinationRecipeBuilder.of(Items.RED_SANDSTONE).namedInput(Items.COBBLESTONE).or().namedInput(Items.RED_SAND).buildFor(consumer, Minestuck.MOD_ID);
		CombinationRecipeBuilder.of(Items.RED_SANDSTONE).namedInput(Items.GRAVEL).or().namedInput(Items.CUT_RED_SANDSTONE).buildFor(consumer, Minestuck.MOD_ID);
		CombinationRecipeBuilder.of(Items.RED_SANDSTONE).namedInput(Items.CUT_RED_SANDSTONE).or().namedInput(Items.RED_SAND).buildFor(consumer, Minestuck.MOD_ID);
		CombinationRecipeBuilder.of(Items.SANDSTONE_STAIRS).input(Items.SAND).or().input(Items.STONE_BRICK_STAIRS).buildFor(consumer, Minestuck.MOD_ID);
		CombinationRecipeBuilder.of(Items.RED_SANDSTONE_STAIRS).input(Items.RED_SAND).or().input(Items.STONE_BRICK_STAIRS).buildFor(consumer, Minestuck.MOD_ID);
		CombinationRecipeBuilder.of(Items.RED_SAND).input(Items.SAND).and().input(Items.RED_DYE).buildFor(consumer, Minestuck.MOD_ID);
		CombinationRecipeBuilder.of(Items.RED_SANDSTONE).input(Items.SANDSTONE).and().input(Items.RED_DYE).namedSource("dye").buildFor(consumer, Minestuck.MOD_ID);
		CombinationRecipeBuilder.of(Items.CUT_RED_SANDSTONE).input(Items.CUT_SANDSTONE).and().input(Items.RED_DYE).namedSource("dye").buildFor(consumer, Minestuck.MOD_ID);
		CombinationRecipeBuilder.of(Items.CHISELED_RED_SANDSTONE).input(Items.CHISELED_SANDSTONE).and().input(Items.RED_DYE).namedSource("dye").buildFor(consumer, Minestuck.MOD_ID);
		CombinationRecipeBuilder.of(Items.SMOOTH_RED_SANDSTONE).input(Items.SMOOTH_SANDSTONE).and().input(Items.RED_DYE).namedSource("dye").buildFor(consumer, Minestuck.MOD_ID);
		CombinationRecipeBuilder.of(Items.RED_SANDSTONE_STAIRS).input(Items.SANDSTONE_STAIRS).and().input(Items.RED_DYE).namedSource("dye").buildFor(consumer, Minestuck.MOD_ID);
		CombinationRecipeBuilder.of(Items.LIGHT_GRAY_CONCRETE_POWDER).input(Items.GRAVEL).or().input(Items.SAND).buildFor(consumer, Minestuck.MOD_ID);
		CombinationRecipeBuilder.of(Items.MOSSY_COBBLESTONE).input(Items.COBBLESTONE).or().input(Items.WHEAT_SEEDS).buildFor(consumer, Minestuck.MOD_ID);
		CombinationRecipeBuilder.of(Items.MOSSY_COBBLESTONE_WALL).input(Items.COBBLESTONE_WALL).or().namedInput(Items.WHEAT_SEEDS).buildFor(consumer, Minestuck.MOD_ID);
		CombinationRecipeBuilder.of(Items.MOSSY_COBBLESTONE_WALL).input(Items.COBBLESTONE_WALL).or().namedInput(Items.MOSSY_COBBLESTONE).buildFor(consumer, Minestuck.MOD_ID);
		CombinationRecipeBuilder.of(Items.MOSSY_STONE_BRICKS).input(Items.STONE_BRICKS).or().input(Items.WHEAT_SEEDS).buildFor(consumer, Minestuck.MOD_ID);
		CombinationRecipeBuilder.of(Items.CLAY).input(Items.SAND).and().input(Items.WATER_BUCKET).buildFor(consumer, Minestuck.MOD_ID);
		CombinationRecipeBuilder.of(Items.AMETHYST_CLUSTER).input(Items.POINTED_DRIPSTONE).or().input(Items.AMETHYST_SHARD).buildFor(consumer, Minestuck.MOD_ID);
		CombinationRecipeBuilder.of(Items.CALCITE).input(Items.AMETHYST_SHARD).or().input(Items.STONE).buildFor(consumer, Minestuck.MOD_ID);
		CombinationRecipeBuilder.of(Items.CANDLE).input(Items.BOOK).and().input(Items.TORCH).buildFor(consumer, Minestuck.MOD_ID);
		CombinationRecipeBuilder.of(Items.COBBLED_DEEPSLATE).input(Items.COBBLESTONE).and().input(Items.CHARCOAL).buildFor(consumer, Minestuck.MOD_ID);
		CombinationRecipeBuilder.of(Items.DEEPSLATE).input(Items.STONE).or().input(Items.INK_SAC).buildFor(consumer, Minestuck.MOD_ID);
		CombinationRecipeBuilder.of(Items.POINTED_DRIPSTONE).input(Items.DRIPSTONE_BLOCK).or().input(Items.ARROW).buildFor(consumer, Minestuck.MOD_ID);
		CombinationRecipeBuilder.of(Items.LIGHTNING_ROD).input(Items.REDSTONE).and().input(Items.COPPER_INGOT).buildFor(consumer, Minestuck.MOD_ID);
		CombinationRecipeBuilder.of(Items.DRIPSTONE_BLOCK).input(Items.STONE).and().input(Items.BIG_DRIPLEAF).buildFor(consumer, Minestuck.MOD_ID);
		CombinationRecipeBuilder.of(Items.POWDER_SNOW_BUCKET).input(Items.ANVIL).or().input(Items.ICE).buildFor(consumer, Minestuck.MOD_ID);
		CombinationRecipeBuilder.of(Items.SMOOTH_BASALT).input(Items.HONEYCOMB).or().input(Items.BASALT).buildFor(consumer, Minestuck.MOD_ID);
		CombinationRecipeBuilder.of(Items.TINTED_GLASS).input(Items.INK_SAC).or().input(Items.FERMENTED_SPIDER_EYE).buildFor(consumer, Minestuck.MOD_ID);
		CombinationRecipeBuilder.of(Items.TUFF).input(Items.FLINT_AND_STEEL).or().input(Items.BIRCH_WOOD).buildFor(consumer, Minestuck.MOD_ID);
		CombinationRecipeBuilder.of(Items.COBBLED_DEEPSLATE_SLAB).input(Items.INK_SAC).or().input(Items.COBBLESTONE_SLAB).buildFor(consumer, Minestuck.MOD_ID);
		CombinationRecipeBuilder.of(Items.COBBLED_DEEPSLATE_STAIRS).input(Items.INK_SAC).or().input(Items.COBBLESTONE_STAIRS).buildFor(consumer, Minestuck.MOD_ID);
		CombinationRecipeBuilder.of(Items.COBBLED_DEEPSLATE_WALL).input(Items.INK_SAC).or().input(Items.COBBLESTONE_WALL).buildFor(consumer, Minestuck.MOD_ID);
		CombinationRecipeBuilder.of(Items.CHISELED_DEEPSLATE).input(Items.INK_SAC).or().input(Items.CHISELED_SANDSTONE).buildFor(consumer, Minestuck.MOD_ID);
		CombinationRecipeBuilder.of(Items.POLISHED_DEEPSLATE).input(Items.HONEYCOMB).or().input(Items.DEEPSLATE).buildFor(consumer, Minestuck.MOD_ID);
		CombinationRecipeBuilder.of(Items.POLISHED_DEEPSLATE_WALL).input(Items.HONEYCOMB).or().input(Items.DEEPSLATE_TILE_WALL).buildFor(consumer, Minestuck.MOD_ID);
		CombinationRecipeBuilder.of(Items.POLISHED_DEEPSLATE_SLAB).input(Items.HONEYCOMB).or().input(Items.DEEPSLATE_TILE_SLAB).buildFor(consumer, Minestuck.MOD_ID);
		CombinationRecipeBuilder.of(Items.POLISHED_DEEPSLATE_STAIRS).input(Items.HONEYCOMB).or().input(Items.DEEPSLATE_TILE_STAIRS).buildFor(consumer, Minestuck.MOD_ID);
		CombinationRecipeBuilder.of(Items.DEEPSLATE_BRICKS).input(Items.INK_SAC).or().input(Items.BRICKS).buildFor(consumer, Minestuck.MOD_ID);
		CombinationRecipeBuilder.of(Items.DEEPSLATE_BRICK_SLAB).input(Items.INK_SAC).or().input(Items.BRICK_SLAB).buildFor(consumer, Minestuck.MOD_ID);
		CombinationRecipeBuilder.of(Items.DEEPSLATE_BRICK_STAIRS).input(Items.INK_SAC).or().input(Items.BRICK_STAIRS).buildFor(consumer, Minestuck.MOD_ID);
		CombinationRecipeBuilder.of(Items.DEEPSLATE_BRICK_WALL).input(Items.INK_SAC).or().input(Items.BRICK_WALL).buildFor(consumer, Minestuck.MOD_ID);
		CombinationRecipeBuilder.of(Items.CRACKED_DEEPSLATE_BRICKS).input(Items.TNT).or().input(Items.DEEPSLATE_BRICKS).buildFor(consumer, Minestuck.MOD_ID);
		CombinationRecipeBuilder.of(Items.DEEPSLATE_TILE_SLAB).input(Items.INK_SAC).or().input(Items.NETHER_BRICK_SLAB).buildFor(consumer, Minestuck.MOD_ID);
		CombinationRecipeBuilder.of(Items.DEEPSLATE_TILE_STAIRS).input(Items.INK_SAC).or().input(Items.NETHER_BRICK_STAIRS).buildFor(consumer, Minestuck.MOD_ID);
		CombinationRecipeBuilder.of(Items.DEEPSLATE_TILE_WALL).input(Items.INK_SAC).or().input(Items.NETHER_BRICK_WALL).buildFor(consumer, Minestuck.MOD_ID);
		CombinationRecipeBuilder.of(Items.CRACKED_DEEPSLATE_TILES).input(Items.TNT).or().input(Items.DEEPSLATE_TILES).buildFor(consumer, Minestuck.MOD_ID);
		CombinationRecipeBuilder.of(Items.GLOW_ITEM_FRAME).input(Items.GOLDEN_CARROT).or().input(Items.ITEM_FRAME).buildFor(consumer, Minestuck.MOD_ID);
		CombinationRecipeBuilder.of(Items.AMETHYST_BLOCK).input(Items.GLASS).or().input(Items.PURPLE_DYE).buildFor(consumer, Minestuck.MOD_ID);
		//miscellaneous
		CombinationRecipeBuilder.of(Items.FURNACE).input(Items.COBBLESTONE).and().input(Items.COAL).buildFor(consumer, Minestuck.MOD_ID);
		CombinationRecipeBuilder.of(Items.JUKEBOX).input(Items.NOTE_BLOCK).and().namedInput(Items.DIAMOND).buildFor(consumer, Minestuck.MOD_ID);
		CombinationRecipeBuilder.of(Items.JUKEBOX).input(Items.NOTE_BLOCK).and().namedInput(ItemTags.MUSIC_DISCS).buildFor(consumer, Minestuck.MOD_ID);
		CombinationRecipeBuilder.of(Items.TNT).input(ItemTags.SAND).and().input(Items.GUNPOWDER).buildFor(consumer, Minestuck.MOD_ID);
		CombinationRecipeBuilder.of(Items.IRON_TRAPDOOR).input(ItemTags.WOODEN_TRAPDOORS).or().namedInput(Items.IRON_INGOT).buildFor(consumer, Minestuck.MOD_ID);
		CombinationRecipeBuilder.of(Items.IRON_TRAPDOOR).input(ItemTags.WOODEN_TRAPDOORS).or().namedInput(Items.IRON_BLOCK).buildFor(consumer, Minestuck.MOD_ID);
		CombinationRecipeBuilder.of(Items.DROPPER).input(Items.DISPENSER).and().input(Items.HOPPER).buildFor(consumer, Minestuck.MOD_ID);
		CombinationRecipeBuilder.of(Items.ENDER_CHEST).input(Items.ENDER_PEARL).and().input(Items.CHEST).buildFor(consumer, Minestuck.MOD_ID);
		CombinationRecipeBuilder.of(Items.BOOKSHELF).input(Items.BOOK).or().namedInput(ItemTags.PLANKS).buildFor(consumer, Minestuck.MOD_ID);
		CombinationRecipeBuilder.of(Items.BOOKSHELF).input(Items.BOOK).and().namedInput(Items.CHEST).buildFor(consumer, Minestuck.MOD_ID);
		CombinationRecipeBuilder.of(Items.ENCHANTING_TABLE).input(Items.BOOK).and().input(Items.EXPERIENCE_BOTTLE).buildFor(consumer, Minestuck.MOD_ID);
		CombinationRecipeBuilder.of(Items.ANVIL).input(Items.CRAFTING_TABLE).or().input(Items.IRON_BLOCK).buildFor(consumer, Minestuck.MOD_ID);
		CombinationRecipeBuilder.of(Items.IRON_DOOR).input(ItemTags.WOODEN_DOORS).and().input(Items.IRON_INGOT).buildFor(consumer, Minestuck.MOD_ID);
		CombinationRecipeBuilder.of(Items.COMPASS).namedInput(Items.CLOCK).or().input(Items.IRON_INGOT).buildFor(consumer, Minestuck.MOD_ID);
		CombinationRecipeBuilder.of(Items.COMPASS).namedInput(Items.REDSTONE).or().input(Items.IRON_INGOT).buildFor(consumer, Minestuck.MOD_ID);
		CombinationRecipeBuilder.of(Items.CLOCK).namedInput(Items.COMPASS).and().input(Items.GOLD_INGOT).buildFor(consumer, Minestuck.MOD_ID);
		CombinationRecipeBuilder.of(Items.CLOCK).namedInput(Items.REDSTONE).and().input(Items.GOLD_INGOT).buildFor(consumer, Minestuck.MOD_ID);
		CombinationRecipeBuilder.of(Items.BELL).namedInput(MSItems.HORN).and().input(Items.CLOCK).buildFor(consumer, Minestuck.MOD_ID);
		CombinationRecipeBuilder.of(Items.GLOWSTONE_DUST).input(Items.TORCH).or().input(Items.REDSTONE).buildFor(consumer, Minestuck.MOD_ID);
		CombinationRecipeBuilder.of(Items.WRITABLE_BOOK).input(Items.BOOK).or().input(ItemTags.SIGNS).buildFor(consumer, Minestuck.MOD_ID);
		CombinationRecipeBuilder.of(Items.NAME_TAG).input(ItemTags.SIGNS).and().input(Items.LEAD).buildFor(consumer, Minestuck.MOD_ID);
		CombinationRecipeBuilder.of(Items.BOOK).input(Items.INK_SAC).and().input(Items.PAPER).buildFor(consumer, Minestuck.MOD_ID);
		CombinationRecipeBuilder.of(Items.CARTOGRAPHY_TABLE).input(Items.CRAFTING_TABLE).or().input(Items.MAP).buildFor(consumer, Minestuck.MOD_ID);
		CombinationRecipeBuilder.of(Items.COBWEB).input(Items.SLIME_BLOCK).or().input(Items.STRING).buildFor(consumer, Minestuck.MOD_ID);
		CombinationRecipeBuilder.of(Items.TORCH).input(Items.REDSTONE_TORCH).or().input(ItemTags.COALS).buildFor(consumer, Minestuck.MOD_ID);
		CombinationRecipeBuilder.of(Items.AMETHYST_SHARD).input(Items.COAL).or().input(Items.PISTON).buildFor(consumer, Minestuck.MOD_ID);
		CombinationRecipeBuilder.of(Items.GLOW_INK_SAC).input(Items.LEATHER).or().input(Items.GLOWSTONE).buildFor(consumer, Minestuck.MOD_ID);
		CombinationRecipeBuilder.of(Items.PEARLESCENT_FROGLIGHT).input(Items.MAGMA_CREAM).or().input(Items.SAND).buildFor(consumer, Minestuck.MOD_ID);
		CombinationRecipeBuilder.of(Items.VERDANT_FROGLIGHT).input(Items.MAGMA_CREAM).or().input(Items.SNOW_BLOCK).buildFor(consumer, Minestuck.MOD_ID);
		CombinationRecipeBuilder.of(Items.OCHRE_FROGLIGHT).input(Items.MAGMA_CREAM).or().input(Items.GRASS_BLOCK).buildFor(consumer, Minestuck.MOD_ID);
		CombinationRecipeBuilder.of(Items.ECHO_SHARD).input(Items.SCULK).or().input(Items.DIAMOND).buildFor(consumer, Minestuck.MOD_ID);
		//Plants
		CombinationRecipeBuilder.of(Items.GRASS_BLOCK).input(Items.DIRT).and().namedInput(Items.SHORT_GRASS).buildFor(consumer, Minestuck.MOD_ID);
		CombinationRecipeBuilder.of(Items.GRASS_BLOCK).input(Items.DIRT).and().namedInput(Items.WHEAT_SEEDS).buildFor(consumer, Minestuck.MOD_ID);
		CombinationRecipeBuilder.of(Items.MUD).input(Items.DIRT).and().namedInput(Items.WATER_BUCKET).buildFor(consumer, Minestuck.MOD_ID);
		CombinationRecipeBuilder.of(Items.MYCELIUM).input(Items.GRASS_BLOCK).and().input(Tags.Items.MUSHROOMS).buildFor(consumer, Minestuck.MOD_ID);
		CombinationRecipeBuilder.of(Items.CACTUS).input(Items.SAND).and().input(Items.SHORT_GRASS).buildFor(consumer, Minestuck.MOD_ID);
		CombinationRecipeBuilder.of(Items.DEAD_BUSH).input(Items.SAND).or().namedInput(Items.SHORT_GRASS).buildFor(consumer, Minestuck.MOD_ID);
		CombinationRecipeBuilder.of(Items.DEAD_BUSH).input(Items.SAND).or().namedInput(ItemTags.SAPLINGS).buildFor(consumer, Minestuck.MOD_ID);
		CombinationRecipeBuilder.of(Items.LILY_PAD).input(ItemTags.LEAVES).or().input(Items.WATER_BUCKET).buildFor(consumer, Minestuck.MOD_ID);
		CombinationRecipeBuilder.of(Items.VINE).input(ItemTags.LEAVES).and().input(Items.LADDER).buildFor(consumer, Minestuck.MOD_ID);
		CombinationRecipeBuilder.of(Items.ROSE_BUSH).input(Items.LARGE_FERN).or().input(Items.POPPY).buildFor(consumer, Minestuck.MOD_ID);
		CombinationRecipeBuilder.of(Items.SUGAR_CANE).input(Items.BAMBOO).and().input(Items.SUGAR).buildFor(consumer, Minestuck.MOD_ID);
		CombinationRecipeBuilder.of(Items.AZALEA).input(Items.ROOTED_DIRT).or().input(Items.OAK_SAPLING).buildFor(consumer, Minestuck.MOD_ID);
		CombinationRecipeBuilder.of(Items.FLOWERING_AZALEA).input(ItemTags.FLOWERS).and().input(Items.AZALEA).buildFor(consumer, Minestuck.MOD_ID);
		CombinationRecipeBuilder.of(Items.AZALEA_LEAVES).input(Items.AZALEA).and().input(Items.OAK_LEAVES).buildFor(consumer, Minestuck.MOD_ID);
		CombinationRecipeBuilder.of(Items.FLOWERING_AZALEA_LEAVES).input(ItemTags.FLOWERS).and().input(Items.AZALEA_LEAVES).buildFor(consumer, Minestuck.MOD_ID);
		CombinationRecipeBuilder.of(Items.BIG_DRIPLEAF).input(Items.BONE_MEAL).or().input(Items.SMALL_DRIPLEAF).buildFor(consumer, Minestuck.MOD_ID);
		CombinationRecipeBuilder.of(Items.SMALL_DRIPLEAF).input(Items.SPYGLASS).or().input(Items.LILY_PAD).buildFor(consumer, Minestuck.MOD_ID);
		CombinationRecipeBuilder.of(Items.MOSS_BLOCK).input(Items.GRASS_BLOCK).or().input(Items.SHORT_GRASS).buildFor(consumer, Minestuck.MOD_ID);
		CombinationRecipeBuilder.of(Items.ROOTED_DIRT).input(Items.VINE).and().input(Items.DIRT).buildFor(consumer, Minestuck.MOD_ID);
		CombinationRecipeBuilder.of(Items.MOSS_CARPET).input(Items.GRASS_BLOCK).and().input(Items.SHORT_GRASS).buildFor(consumer, Minestuck.MOD_ID);
		CombinationRecipeBuilder.of(Items.SPORE_BLOSSOM).input(Items.LILY_PAD).or().input(Items.BONE_MEAL).buildFor(consumer, Minestuck.MOD_ID);
		CombinationRecipeBuilder.of(Items.GLOW_BERRIES).input(Items.GLOWSTONE_DUST).and().input(Items.SWEET_BERRIES).buildFor(consumer, Minestuck.MOD_ID);
		CombinationRecipeBuilder.of(Items.GLOW_LICHEN).input(Items.GLOWSTONE_DUST).or().input(Items.VINE).buildFor(consumer, Minestuck.MOD_ID);
		CombinationRecipeBuilder.of(Items.HANGING_ROOTS).input(Items.VINE).or().input(Items.DEAD_BUSH).buildFor(consumer, Minestuck.MOD_ID);
		CombinationRecipeBuilder.of(Items.MANGROVE_ROOTS).input(Items.HANGING_ROOTS).and().input(Items.STICK).buildFor(consumer, Minestuck.MOD_ID);
		CombinationRecipeBuilder.of(Items.SCULK).input(Items.MOSS_BLOCK).or().input(Items.SOUL_SOIL).buildFor(consumer, Minestuck.MOD_ID);
		CombinationRecipeBuilder.of(Items.SCULK_VEIN).input(Items.SCULK).and().input(Items.GLOW_LICHEN).buildFor(consumer, Minestuck.MOD_ID);
		CombinationRecipeBuilder.of(Items.SCULK_CATALYST).input(Items.SCULK).or().input(Items.SPAWNER).buildFor(consumer, Minestuck.MOD_ID);
		CombinationRecipeBuilder.of(Items.SCULK_SENSOR).input(Items.SCULK).and().input(Items.STRING).buildFor(consumer, Minestuck.MOD_ID);
		CombinationRecipeBuilder.of(Items.SCULK_SHRIEKER).input(Items.SCULK_SENSOR).or().input(Items.BELL).buildFor(consumer, Minestuck.MOD_ID);
		//Transport
		CombinationRecipeBuilder.of(Items.MINECART).input(ItemTags.BOATS).or().input(Items.RAIL).buildFor(consumer, Minestuck.MOD_ID);
		CombinationRecipeBuilder.of(Items.CHEST_MINECART).input(Items.MINECART).and().input(Items.CHEST).buildFor(consumer, Minestuck.MOD_ID);
		CombinationRecipeBuilder.of(Items.FURNACE_MINECART).input(Items.MINECART).and().input(Items.FURNACE).buildFor(consumer, Minestuck.MOD_ID);
		CombinationRecipeBuilder.of(Items.TNT_MINECART).input(Items.MINECART).and().input(Items.TNT).buildFor(consumer, Minestuck.MOD_ID);
		CombinationRecipeBuilder.of(Items.HOPPER_MINECART).input(Items.MINECART).and().input(Items.HOPPER).buildFor(consumer, Minestuck.MOD_ID);
		CombinationRecipeBuilder.of(Items.RAIL).namedInput(Items.IRON_BARS).and().input(Items.STICK).buildFor(consumer, Minestuck.MOD_ID);
		CombinationRecipeBuilder.of(Items.RAIL).namedInput(Items.LADDER).and().input(Items.IRON_INGOT).buildFor(consumer, Minestuck.MOD_ID);
		CombinationRecipeBuilder.of(Items.ACTIVATOR_RAIL).input(Items.RAIL).and().input(Items.REDSTONE_TORCH).buildFor(consumer, Minestuck.MOD_ID);
		CombinationRecipeBuilder.of(Items.DETECTOR_RAIL).input(Items.RAIL).and().namedInput(ItemTags.WOODEN_PRESSURE_PLATES).buildFor(consumer, Minestuck.MOD_ID);
		CombinationRecipeBuilder.of(Items.DETECTOR_RAIL).input(Items.RAIL).and().namedInput(Items.STONE_PRESSURE_PLATE).buildFor(consumer, Minestuck.MOD_ID);
		CombinationRecipeBuilder.of(Items.DETECTOR_RAIL).input(Items.RAIL).and().namedInput(Items.LIGHT_WEIGHTED_PRESSURE_PLATE).buildFor(consumer, Minestuck.MOD_ID);
		CombinationRecipeBuilder.of(Items.DETECTOR_RAIL).input(Items.RAIL).and().namedInput(Items.HEAVY_WEIGHTED_PRESSURE_PLATE).buildFor(consumer, Minestuck.MOD_ID);
		CombinationRecipeBuilder.of(Items.POWERED_RAIL).input(Items.RAIL).and().namedInput(Items.GOLD_INGOT).buildFor(consumer, Minestuck.MOD_ID);
		CombinationRecipeBuilder.of(Items.POWERED_RAIL).input(Items.RAIL).and().namedInput(Items.FURNACE_MINECART).buildFor(consumer, Minestuck.MOD_ID);
		CombinationRecipeBuilder.of(Items.LADDER).input(ItemTags.PLANKS).or().namedInput(Items.RAIL).buildFor(consumer, Minestuck.MOD_ID);
		CombinationRecipeBuilder.of(Items.LADDER).input(Items.STICK).or().input(Items.VINE).buildFor(consumer, Minestuck.MOD_ID);
		//Redstone
		CombinationRecipeBuilder.of(Items.REDSTONE_TORCH).input(Items.TORCH).and().input(Items.REDSTONE).buildFor(consumer, Minestuck.MOD_ID);
		CombinationRecipeBuilder.of(Items.REDSTONE_LAMP).input(Items.GLOWSTONE).and().input(Items.REDSTONE_TORCH).buildFor(consumer, Minestuck.MOD_ID);
		CombinationRecipeBuilder.of(Items.REDSTONE).input(Items.GUNPOWDER).or().namedInput(Items.STONE_BUTTON).buildFor(consumer, Minestuck.MOD_ID);
		CombinationRecipeBuilder.of(Items.REDSTONE).input(Items.GUNPOWDER).or().namedInput(Items.STONE_PRESSURE_PLATE).buildFor(consumer, Minestuck.MOD_ID);
		CombinationRecipeBuilder.of(Items.REDSTONE).input(Items.GUNPOWDER).or().namedInput(Items.LEVER).buildFor(consumer, Minestuck.MOD_ID);
		CombinationRecipeBuilder.of(Items.REDSTONE).namedInput(Items.GRAVEL).and().input(Items.RED_DYE).buildFor(consumer, Minestuck.MOD_ID);
		CombinationRecipeBuilder.of(Items.REDSTONE_TORCH).input(Items.GUNPOWDER).or().namedInput(ItemTags.WOODEN_BUTTONS).buildFor(consumer, Minestuck.MOD_ID);
		CombinationRecipeBuilder.of(Items.REDSTONE_TORCH).input(Items.GUNPOWDER).or().namedInput(ItemTags.WOODEN_PRESSURE_PLATES).buildFor(consumer, Minestuck.MOD_ID);
		//Nether
		CombinationRecipeBuilder.of(Items.NETHERRACK).input(Items.COBBLESTONE).and().input(Items.LAVA_BUCKET).buildFor(consumer, Minestuck.MOD_ID);
		CombinationRecipeBuilder.of(Items.SOUL_SAND).input(ItemTags.SAND).or().input(Items.NETHER_WART).buildFor(consumer, Minestuck.MOD_ID);
		CombinationRecipeBuilder.of(Items.SOUL_SOIL).input(Items.DIRT).or().input(Items.NETHER_WART).buildFor(consumer, Minestuck.MOD_ID);
		CombinationRecipeBuilder.of(Items.BLACKSTONE).input(Items.STONE).or().namedInput(Items.BLACK_DYE).buildFor(consumer, Minestuck.MOD_ID);
		CombinationRecipeBuilder.of(Items.BASALT).input(Items.LAVA_BUCKET).or().namedInput(Items.QUARTZ_PILLAR).buildFor(consumer, Minestuck.MOD_ID);
		CombinationRecipeBuilder.of(Items.BASALT).input(Items.LAVA_BUCKET).or().namedInput(Items.PURPUR_PILLAR).buildFor(consumer, Minestuck.MOD_ID);
		CombinationRecipeBuilder.of(Items.CRYING_OBSIDIAN).input(Items.OBSIDIAN).or().namedInput(Items.GHAST_TEAR).buildFor(consumer, Minestuck.MOD_ID);
		CombinationRecipeBuilder.of(Items.NETHER_WART).input(Items.RED_MUSHROOM).and().input(Items.SOUL_SAND).buildFor(consumer, Minestuck.MOD_ID);
		CombinationRecipeBuilder.of(Items.NETHER_BRICKS).input(Items.NETHERRACK).and().namedInput(Items.BRICKS).buildFor(consumer, Minestuck.MOD_ID);
		CombinationRecipeBuilder.of(Items.NETHER_BRICKS).input(Items.NETHERRACK).and().namedInput(Items.BRICK).buildFor(consumer, Minestuck.MOD_ID);
		CombinationRecipeBuilder.of(Items.NETHER_BRICK).namedInput(Items.NETHERRACK).or().input(Items.BRICK).buildFor(consumer, Minestuck.MOD_ID);
		CombinationRecipeBuilder.of(Items.NETHER_BRICK).namedInput(Items.NETHER_BRICKS).or().input(Items.BRICK).buildFor(consumer, Minestuck.MOD_ID);
		CombinationRecipeBuilder.of(Items.NETHER_BRICK_FENCE).input(ItemTags.WOODEN_FENCES).and().namedInput(Blocks.NETHER_BRICKS).buildFor(consumer, Minestuck.MOD_ID);
		CombinationRecipeBuilder.of(Items.NETHER_BRICK_FENCE).input(ItemTags.WOODEN_FENCES).and().namedInput(Items.NETHER_BRICK).buildFor(consumer, Minestuck.MOD_ID);
		CombinationRecipeBuilder.of(Items.NETHER_BRICK_STAIRS).input(ItemTags.WOODEN_STAIRS).and().namedInput(Blocks.NETHER_BRICKS).buildFor(consumer, Minestuck.MOD_ID);
		CombinationRecipeBuilder.of(Items.NETHER_BRICK_STAIRS).input(ItemTags.WOODEN_STAIRS).and().namedInput(Items.NETHER_BRICK).buildFor(consumer, Minestuck.MOD_ID);
		CombinationRecipeBuilder.of(Items.GLOWSTONE).input(Items.NETHERRACK).and().input(Items.GLOWSTONE_DUST).buildFor(consumer, Minestuck.MOD_ID);
		CombinationRecipeBuilder.of(Items.CRIMSON_STEM).input(ItemTags.LOGS).or().input(Items.CRIMSON_FUNGUS).buildFor(consumer, Minestuck.MOD_ID);
		CombinationRecipeBuilder.of(Items.WARPED_STEM).input(ItemTags.LOGS).or().input(Items.WARPED_FUNGUS).buildFor(consumer, Minestuck.MOD_ID);
		CombinationRecipeBuilder.of(Items.CRIMSON_ROOTS).input(Items.SHORT_GRASS).or().input(Items.CRIMSON_FUNGUS).buildFor(consumer, Minestuck.MOD_ID);
		CombinationRecipeBuilder.of(Items.WARPED_ROOTS).input(Items.SHORT_GRASS).or().input(Items.WARPED_FUNGUS).buildFor(consumer, Minestuck.MOD_ID);
		CombinationRecipeBuilder.of(Items.WEEPING_VINES).input(Items.VINE).and().input(Items.CRIMSON_ROOTS).buildFor(consumer, Minestuck.MOD_ID);
		CombinationRecipeBuilder.of(Items.TWISTING_VINES).input(Items.VINE).and().input(Items.WARPED_ROOTS).buildFor(consumer, Minestuck.MOD_ID);
		CombinationRecipeBuilder.of(Items.CRIMSON_NYLIUM).input(Items.MYCELIUM).or().input(Items.CRIMSON_FUNGUS).buildFor(consumer, Minestuck.MOD_ID);
		CombinationRecipeBuilder.of(Items.WARPED_NYLIUM).input(Items.MYCELIUM).or().input(Items.WARPED_FUNGUS).buildFor(consumer, Minestuck.MOD_ID);
		CombinationRecipeBuilder.of(Items.WARPED_WART_BLOCK).input(ItemTags.LEAVES).and().input(Items.WARPED_FUNGUS).buildFor(consumer, Minestuck.MOD_ID);
		CombinationRecipeBuilder.of(Items.SHROOMLIGHT).input(Items.GLOWSTONE).or().namedInput(Items.CRIMSON_FUNGUS).buildFor(consumer, Minestuck.MOD_ID);
		CombinationRecipeBuilder.of(Items.SHROOMLIGHT).input(Items.GLOWSTONE).or().namedInput(Items.WARPED_FUNGUS).buildFor(consumer, Minestuck.MOD_ID);
		CombinationRecipeBuilder.of(Items.MAGMA_CREAM).input(Items.SLIME_BALL).and().input(Items.BLAZE_POWDER).buildFor(consumer, Minestuck.MOD_ID);
		CombinationRecipeBuilder.of(Items.MAGMA_BLOCK).input(Items.LAVA_BUCKET).and().input(Items.STONE).buildFor(consumer, Minestuck.MOD_ID);
		CombinationRecipeBuilder.of(Items.BLAZE_ROD).input(Items.STICK).and().namedInput(Items.LAVA_BUCKET).buildFor(consumer, Minestuck.MOD_ID);
		CombinationRecipeBuilder.of(Items.BLAZE_ROD).input(Items.STICK).and().namedInput(Items.BLAZE_POWDER).buildFor(consumer, Minestuck.MOD_ID);
		CombinationRecipeBuilder.of(Items.BLAZE_POWDER).input(Items.REDSTONE).or().namedInput(Items.LAVA_BUCKET).buildFor(consumer, Minestuck.MOD_ID);
		CombinationRecipeBuilder.of(Items.BLAZE_POWDER).input(Items.REDSTONE).or().namedInput(Items.NETHERRACK).buildFor(consumer, Minestuck.MOD_ID);
		CombinationRecipeBuilder.of(Items.ANCIENT_DEBRIS).input(MSItems.STEEL_BEAM).and().input(MSItems.RAW_URANIUM).buildFor(consumer, Minestuck.MOD_ID);
		//food
		CombinationRecipeBuilder.of(Items.APPLE).namedInput(Items.OAK_SAPLING).and().input(Items.WHEAT_SEEDS).buildFor(consumer, Minestuck.MOD_ID);
		CombinationRecipeBuilder.of(Items.APPLE).namedInput(Items.OAK_LEAVES).and().input(Items.WHEAT_SEEDS).buildFor(consumer, Minestuck.MOD_ID);
		CombinationRecipeBuilder.of(Items.GOLDEN_APPLE).input(Items.APPLE).and().namedInput(Items.GOLD_INGOT).buildFor(consumer, Minestuck.MOD_ID);
		CombinationRecipeBuilder.of(Items.GOLDEN_APPLE).input(Items.APPLE).and().namedInput(Items.GOLD_NUGGET).buildFor(consumer, Minestuck.MOD_ID);
		CombinationRecipeBuilder.of(Items.ENCHANTED_GOLDEN_APPLE).input(Items.APPLE).and().input(Items.GOLD_BLOCK).buildFor(consumer, Minestuck.MOD_ID);
		CombinationRecipeBuilder.of(Items.POTATO).input(Items.CARROT).and().input(Items.WHEAT_SEEDS).buildFor(consumer, Minestuck.MOD_ID);
		CombinationRecipeBuilder.of(Items.CARROT).input(Items.POTATO).or().input(Items.WHEAT_SEEDS).buildFor(consumer, Minestuck.MOD_ID);
		CombinationRecipeBuilder.of(Items.MELON).input(Items.PUMPKIN).or().input(MSBlocks.STRAWBERRY).buildFor(consumer, Minestuck.MOD_ID);
		CombinationRecipeBuilder.of(Items.PUMPKIN).input(Items.MELON).and().input(Items.CARROT).buildFor(consumer, Minestuck.MOD_ID);
		CombinationRecipeBuilder.of(Items.SUGAR).input(Items.COOKIE).and().input(Items.REDSTONE).buildFor(consumer, Minestuck.MOD_ID);
		CombinationRecipeBuilder.of(Items.PORKCHOP).input(Items.ROTTEN_FLESH).or().input(Items.CARROT).buildFor(consumer, Minestuck.MOD_ID);
		CombinationRecipeBuilder.of(Items.LEATHER).input(Items.ROTTEN_FLESH).or().input(Items.WATER_BUCKET).buildFor(consumer, Minestuck.MOD_ID);
		CombinationRecipeBuilder.of(Items.BEEF).input(Items.ROTTEN_FLESH).or().input(Items.WHEAT).buildFor(consumer, Minestuck.MOD_ID);
		CombinationRecipeBuilder.of(Items.CHICKEN).input(Items.ROTTEN_FLESH).or().input(Items.WHEAT_SEEDS).buildFor(consumer, Minestuck.MOD_ID);
		CombinationRecipeBuilder.of(Items.POISONOUS_POTATO).input(Items.POTATO).or().input(Items.PUFFERFISH).buildFor(consumer, Minestuck.MOD_ID);
		CombinationRecipeBuilder.of(Items.FERMENTED_SPIDER_EYE).input(Items.SPIDER_EYE).or().input(Items.ROTTEN_FLESH).buildFor(consumer, Minestuck.MOD_ID);
		//end
		CombinationRecipeBuilder.of(Items.END_STONE).input(Items.STONE).and().input(Items.ENDER_PEARL).buildFor(consumer, Minestuck.MOD_ID);
		CombinationRecipeBuilder.of(Items.DRAGON_EGG).input(Items.ENDER_EYE).and().input(Items.EGG).buildFor(consumer, Minestuck.MOD_ID);
		CombinationRecipeBuilder.of(Items.ENDER_EYE).input(Items.ENDER_PEARL).and().input(Items.BLAZE_POWDER).buildFor(consumer, Minestuck.MOD_ID);
		CombinationRecipeBuilder.of(Items.CHORUS_FLOWER).input(ItemTags.SMALL_FLOWERS).and().input(Items.CHORUS_FRUIT).buildFor(consumer, Minestuck.MOD_ID);
		CombinationRecipeBuilder.of(Items.ELYTRA).namedInput(Items.FEATHER).or().input(Items.CHORUS_FRUIT).buildFor(consumer, Minestuck.MOD_ID);
		CombinationRecipeBuilder.of(Items.ELYTRA).namedInput(Items.LEATHER_CHESTPLATE).and().input(Items.PHANTOM_MEMBRANE).buildFor(consumer, Minestuck.MOD_ID);
		CombinationRecipeBuilder.of(Items.CHORUS_FRUIT).input(Items.ENDER_PEARL).and().input(Items.POTATO).buildFor(consumer, Minestuck.MOD_ID);
		CombinationRecipeBuilder.of(Items.DRAGON_BREATH).input(Items.GLASS_BOTTLE).or().input(Items.DRAGON_HEAD).buildFor(consumer, Minestuck.MOD_ID);
		//Fighting
		CombinationRecipeBuilder.of(Items.DIAMOND_HORSE_ARMOR).input(Items.DIAMOND).and().input(Items.SADDLE).buildFor(consumer, Minestuck.MOD_ID);
		CombinationRecipeBuilder.of(Items.GOLDEN_HORSE_ARMOR).input(Items.GOLD_INGOT).and().input(Items.SADDLE).buildFor(consumer, Minestuck.MOD_ID);
		CombinationRecipeBuilder.of(Items.IRON_HORSE_ARMOR).input(Items.IRON_INGOT).and().input(Items.SADDLE).buildFor(consumer, Minestuck.MOD_ID);
		CombinationRecipeBuilder.of(Items.SADDLE).input(Items.STRING).and().input(Items.LEATHER).buildFor(consumer, Minestuck.MOD_ID);
		CombinationRecipeBuilder.of(Items.SHEARS).input(Items.IRON_INGOT).and().input(Items.SHORT_GRASS).buildFor(consumer, Minestuck.MOD_ID);
		CombinationRecipeBuilder.of(Items.FIRE_CHARGE).input(Items.BLAZE_POWDER).or().input(Items.GUNPOWDER).buildFor(consumer, Minestuck.MOD_ID);
		CombinationRecipeBuilder.of(Items.BOW).input(Items.WOODEN_SWORD).or().input(Items.ARROW).buildFor(consumer, Minestuck.MOD_ID);
		CombinationRecipeBuilder.of(Items.FLOWER_POT).input(ItemTags.SMALL_FLOWERS).and().input(Items.BRICK).buildFor(consumer, Minestuck.MOD_ID);
		CombinationRecipeBuilder.of(Items.EXPERIENCE_BOTTLE).namedInput(Items.POTION).or().input(Items.ENCHANTED_BOOK).buildFor(consumer, Minestuck.MOD_ID);
		CombinationRecipeBuilder.of(Items.EXPERIENCE_BOTTLE).namedInput(Items.GLASS_BOTTLE).or().input(Items.ENCHANTED_BOOK).buildFor(consumer, Minestuck.MOD_ID);
		CombinationRecipeBuilder.of(Items.TRIDENT).namedInput(Items.PRISMARINE_SHARD).and().input(MSItems.FORK).buildFor(consumer, Minestuck.MOD_ID);
		CombinationRecipeBuilder.of(Items.TURTLE_HELMET).namedInput(Items.IRON_HELMET).or().input(Items.SEAGRASS).buildFor(consumer, Minestuck.MOD_ID);
		CombinationRecipeBuilder.of(Items.SPYGLASS).namedInput(Items.SPIDER_EYE).or().input(Items.OBSERVER).buildFor(consumer, Minestuck.MOD_ID);
		//watery stuff
		CombinationRecipeBuilder.of(Items.BUCKET).input(Items.BOWL).or().namedInput(Items.IRON_INGOT).buildFor(consumer, Minestuck.MOD_ID);
		CombinationRecipeBuilder.of(Items.WET_SPONGE).input(Items.WATER_BUCKET).and().namedInput(Items.SPONGE).buildFor(consumer, Minestuck.MOD_ID);
		CombinationRecipeBuilder.of(Items.WET_SPONGE).input(Items.WATER_BUCKET).and().namedInput(Items.YELLOW_WOOL).buildFor(consumer, Minestuck.MOD_ID);
		CombinationRecipeBuilder.of(Items.ICE).input(Items.GLASS).and().input(Items.SNOW_BLOCK).buildFor(consumer, Minestuck.MOD_ID);
		CombinationRecipeBuilder.of(Items.PRISMARINE_CRYSTALS).input(Items.PRISMARINE_SHARD).or().namedInput(Items.DIAMOND).buildFor(consumer, Minestuck.MOD_ID);
		CombinationRecipeBuilder.of(Items.PRISMARINE_CRYSTALS).input(Items.PRISMARINE_SHARD).or().namedInput(Items.EMERALD).buildFor(consumer, Minestuck.MOD_ID);
		CombinationRecipeBuilder.of(Items.PRISMARINE).input(Items.PRISMARINE_SHARD).and().input(Items.COBBLESTONE).buildFor(consumer, Minestuck.MOD_ID);
		CombinationRecipeBuilder.of(Items.PRISMARINE_BRICKS).input(Items.PRISMARINE_SHARD).and().input(Items.STONE_BRICKS).buildFor(consumer, Minestuck.MOD_ID);
		CombinationRecipeBuilder.of(Items.DARK_PRISMARINE).input(Items.PRISMARINE).and().input(Items.BLACK_DYE).buildFor(consumer, Minestuck.MOD_ID);
		CombinationRecipeBuilder.of(Items.SEA_LANTERN).input(Items.PRISMARINE_SHARD).or().namedInput(Items.GLOWSTONE).buildFor(consumer, Minestuck.MOD_ID);
		CombinationRecipeBuilder.of(Items.SEA_LANTERN).input(Items.PRISMARINE).and().namedInput(Items.PRISMARINE_CRYSTALS).buildFor(consumer, Minestuck.MOD_ID);
		CombinationRecipeBuilder.of(Items.PRISMARINE_SHARD).namedInput(Items.PRISMARINE).or().input(Items.FLINT).buildFor(consumer, Minestuck.MOD_ID);
		CombinationRecipeBuilder.of(Items.PRISMARINE_SHARD).namedInput(Items.PRISMARINE_BRICKS).or().input(Items.FLINT).buildFor(consumer, Minestuck.MOD_ID);
		CombinationRecipeBuilder.of(Items.PRISMARINE_SHARD).namedInput(Items.DARK_PRISMARINE).or().input(Items.FLINT).buildFor(consumer, Minestuck.MOD_ID);
		CombinationRecipeBuilder.of(Items.PRISMARINE_CRYSTALS).namedInput(Items.QUARTZ).or().input(Items.WATER_BUCKET).buildFor(consumer, Minestuck.MOD_ID);
		CombinationRecipeBuilder.of(Items.PRISMARINE_SHARD).namedInput(Items.QUARTZ).and().input(Items.WATER_BUCKET).buildFor(consumer, Minestuck.MOD_ID);
		CombinationRecipeBuilder.of(Items.HEART_OF_THE_SEA).namedInput(Items.NAUTILUS_SHELL).and().input(Items.DIAMOND).buildFor(consumer, Minestuck.MOD_ID);
		CombinationRecipeBuilder.of(Items.INK_SAC).input(ItemTags.FISHES).and().input(Items.BLACK_DYE).buildFor(consumer, Minestuck.MOD_ID);
		CombinationRecipeBuilder.of(Items.PACKED_ICE).input(Items.ICE).or().input(Items.CHEST).buildFor(consumer, Minestuck.MOD_ID);
		CombinationRecipeBuilder.of(Items.BLUE_ICE).input(Items.BLUE_DYE).and().input(Items.ICE).buildFor(consumer, Minestuck.MOD_ID);
		//Mob loot
		CombinationRecipeBuilder.of(Items.SKELETON_SKULL).input(Items.WITHER_SKELETON_SKULL).and().namedInput(Items.BONE).buildFor(consumer, Minestuck.MOD_ID);
		CombinationRecipeBuilder.of(Items.SKELETON_SKULL).input(Items.WITHER_SKELETON_SKULL).and().namedInput(Items.BONE_MEAL).buildFor(consumer, Minestuck.MOD_ID);
		CombinationRecipeBuilder.of(Items.ZOMBIE_HEAD).input(Items.SKELETON_SKULL).or().input(Items.ROTTEN_FLESH).buildFor(consumer, Minestuck.MOD_ID);
		CombinationRecipeBuilder.of(Items.CREEPER_HEAD).input(Items.VINE).or().input(Items.GUNPOWDER).buildFor(consumer, Minestuck.MOD_ID);
		CombinationRecipeBuilder.of(Items.BONE).input(Items.STICK).or().input(Items.SKELETON_SKULL).buildFor(consumer, Minestuck.MOD_ID);
		CombinationRecipeBuilder.of(Items.GHAST_TEAR).input(Items.PHANTOM_MEMBRANE).and().input(Items.GUNPOWDER).buildFor(consumer, Minestuck.MOD_ID);

		/////////////////	MINESTUCK	\\\\\\\\\\\\\\\\\
		
		CombinationRecipeBuilder.of(MSItems.PAPER_SWORD).input(Items.PAPER).or().namedInput(MSItems.KATANA).build(consumer);
		CombinationRecipeBuilder.of(MSItems.PAPER_SWORD).input(Items.PAPER).or().namedInput(Items.WOODEN_SWORD).build(consumer);
		CombinationRecipeBuilder.of(MSItems.PAPER_SWORD).input(Items.PAPER).or().namedInput(Items.STONE_SWORD).build(consumer);
		CombinationRecipeBuilder.of(MSItems.SWONGE).input(Items.WOODEN_SWORD).and().namedInput(Items.SPONGE).build(consumer);
		CombinationRecipeBuilder.of(MSItems.PUMORD).input(MSItems.SWONGE).or().input(MSBlocks.PUMICE_STONE).build(consumer);
		CombinationRecipeBuilder.of(MSItems.CACTACEAE_CUTLASS).input(Items.WOODEN_SWORD).and().namedInput(Items.CACTUS).build(consumer);
		CombinationRecipeBuilder.of(MSItems.CACTACEAE_CUTLASS).input(Items.WOODEN_SWORD).and().namedInput(MSBlocks.BLOOMING_CACTUS).build(consumer);
		CombinationRecipeBuilder.of(MSItems.STEAK_SWORD).namedInput(Items.WOODEN_SWORD).or().input(Items.COOKED_BEEF).build(consumer);
		CombinationRecipeBuilder.of(MSItems.STEAK_SWORD).namedInput(Items.STONE_SWORD).or().input(Items.COOKED_BEEF).build(consumer);
		CombinationRecipeBuilder.of(MSItems.BEEF_SWORD).input(Items.WOODEN_SWORD).or().input(Items.BEEF).build(consumer);
		CombinationRecipeBuilder.of(MSItems.IRRADIATED_STEAK_SWORD).input(Items.WOODEN_SWORD).or().input(MSItems.IRRADIATED_STEAK).build(consumer);
		CombinationRecipeBuilder.of(MSItems.SORD).namedInput(MSItems.KATANA).and().input(MSItems.SBAHJ_POSTER).build(consumer);
		CombinationRecipeBuilder.of(MSItems.SORD).namedInput(Items.WOODEN_SWORD).and().input(MSItems.SBAHJ_POSTER).build(consumer);
		CombinationRecipeBuilder.of(MSItems.FROSTY_MACUAHUITL).namedInput(MSItems.MACUAHUITL).or().input(MSItems.ICE_SHARD).build(consumer);
		CombinationRecipeBuilder.of(MSItems.KATANA).namedInput(Items.STONE_SWORD).and().input(Items.ROTTEN_FLESH).build(consumer);
		CombinationRecipeBuilder.of(MSItems.KATANA).namedInput(Items.IRON_SWORD).and().input(Items.ROTTEN_FLESH).build(consumer);
		CombinationRecipeBuilder.of(MSItems.FIRE_POKER).input(Items.IRON_SWORD).and().input(Items.BLAZE_ROD).build(consumer);
		CombinationRecipeBuilder.of(MSItems.TOO_HOT_TO_HANDLE).input(Items.IRON_SWORD).or().input(Items.BLAZE_ROD).build(consumer);
		CombinationRecipeBuilder.of(MSItems.REGISWORD).namedInput(Items.IRON_SWORD).and().input(MSBlocks.CHESSBOARD).build(consumer);
		CombinationRecipeBuilder.of(MSItems.REGISWORD).namedInput(MSItems.KATANA).and().input(MSBlocks.CHESSBOARD).build(consumer);
		CombinationRecipeBuilder.of(MSItems.CRUEL_FATE_CRUCIBLE).namedInput(Items.IRON_SWORD).or().input(MSItems.GRIMOIRE).build(consumer);
		CombinationRecipeBuilder.of(MSItems.CLAYMORE).input(Items.IRON_SWORD).and().input(Items.IRON_BLOCK).build(consumer);
		CombinationRecipeBuilder.of(MSItems.UNBREAKABLE_KATANA).input(MSItems.KATANA).and().input(Items.BEDROCK).build(consumer);
		CombinationRecipeBuilder.of(MSItems.ANGEL_APOCALYPSE).input(Items.IRON_SWORD).and().input(Items.FEATHER).build(consumer);
		CombinationRecipeBuilder.of(MSItems.COBALT_SABRE).input(MSItems.TOO_HOT_TO_HANDLE).or().input(Items.LAPIS_BLOCK).build(consumer);
		CombinationRecipeBuilder.of(MSItems.SCARLET_RIBBITAR).input(MSItems.CALEDSCRATCH).and().input(MSItems.FROG).build(consumer);
		CombinationRecipeBuilder.of(MSItems.SHATTER_BEACON).input(Items.BEACON).and().input(Items.DIAMOND_SWORD).build(consumer);
		CombinationRecipeBuilder.of(MSItems.SHATTER_BACON).input(Items.PORKCHOP).and().input(MSItems.SHATTER_BEACON).build(consumer);
		CombinationRecipeBuilder.of(MSItems.CALEDFWLCH).input(MSItems.ROYAL_DERINGER).or().input(MSItems.CUEBALL).build(consumer);
		CombinationRecipeBuilder.of(MSItems.MUSIC_SWORD).input(MSBlocks.CASSETTE_PLAYER).or().input(MSItems.CLAYMORE).build(consumer);
		CombinationRecipeBuilder.of(MSItems.PILLOW_TALK).input(MSItems.UNBREAKABLE_KATANA).and().input(Items.BAMBOO).build(consumer);
		CombinationRecipeBuilder.of(MSItems.KRAKENS_EYE).input(Items.HEART_OF_THE_SEA).or().input(MSItems.CLAYMORE).build(consumer);
		CombinationRecipeBuilder.of(MSItems.CINNAMON_SWORD).input(Items.WOODEN_SWORD).and().input(MSItems.CANDY_CORN).build(consumer);
		CombinationRecipeBuilder.of(MSItems.UNION_BUSTER).input(MSItems.ROCKEFELLERS_WALKING_BLADECANE).and().input(MSItems.NIGHTSTICK).build(consumer);
		CombinationRecipeBuilder.of(MSItems.THORN_IN_YOUR_SIDE).input(Items.IRON_SWORD).and().input(Items.ROSE_BUSH).build(consumer);
		CombinationRecipeBuilder.of(MSItems.ROSE_PROTOCOL).input(MSItems.THORN_IN_YOUR_SIDE).and().input(MSItems.COMPUTER_PARTS).build(consumer);
		
		CombinationRecipeBuilder.of(MSItems.DIAMOND_DAGGER).input(MSItems.DAGGER).and().input(Items.DIAMOND).build(consumer);
		CombinationRecipeBuilder.of(MSItems.PIGLINS_PRIDE).input(MSItems.DIAMOND_DAGGER).and().input(Items.NETHERITE_INGOT).build(consumer);
		CombinationRecipeBuilder.of(MSItems.BASILISK_BREATH_DRAGONSLAYER).input(MSItems.PIGLINS_PRIDE).and().input(Items.DRAGON_BREATH).build(consumer);
		CombinationRecipeBuilder.of(MSItems.HALLOWED_SKEWER).input(MSItems.DIAMOND_DAGGER).or().input(Items.TOTEM_OF_UNDYING).build(consumer);
		CombinationRecipeBuilder.of(MSItems.GENESIS_GODSTABBER).input(MSItems.HALLOWED_SKEWER).and().input(MSItems.FROG).build(consumer);
		CombinationRecipeBuilder.of(MSItems.NIFE).input(MSItems.DAGGER).and().input(MSItems.SBAHJ_POSTER).build(consumer);
		CombinationRecipeBuilder.of(MSItems.LIGHT_OF_MY_KNIFE).input(MSItems.DAGGER).and().input(Items.LANTERN).build(consumer);
		CombinationRecipeBuilder.of(MSItems.THOUSAND_DEGREE_KNIFE).input(MSItems.LIGHT_OF_MY_KNIFE).and().input(Items.LAVA_BUCKET).build(consumer);
		CombinationRecipeBuilder.of(MSItems.STARSHARD_TRI_BLADE).input(MSItems.LIGHT_OF_MY_KNIFE).or().input(Items.NETHER_STAR).build(consumer);
		CombinationRecipeBuilder.of(MSItems.TOOTHRIPPER).input(MSItems.DAGGER).or().namedInput(Items.ROTTEN_FLESH).build(consumer);
		CombinationRecipeBuilder.of(MSItems.TOOTHRIPPER).input(MSItems.DAGGER).or().namedInput(Items.BONE).build(consumer);
		CombinationRecipeBuilder.of(MSItems.SHADOWRAZOR).input(MSItems.TOOTHRIPPER).and().namedInput(MSItems.SORROW_GUSHERS).build(consumer);
		CombinationRecipeBuilder.of(MSItems.PRINCESS_PERIL).input(MSItems.SHADOWRAZOR).or().input(MSItems.GAMEGRL_MAGAZINE).build(consumer);
		
		CombinationRecipeBuilder.of(MSItems.KEYBLADE).input(MSItems.HOUSE_KEY).and().input(Items.IRON_SWORD).build(consumer);
		CombinationRecipeBuilder.of(MSItems.CANDY_KEY).input(MSItems.KEYBLADE).or().input(MSItems.CANDY_CORN).build(consumer);
		CombinationRecipeBuilder.of(MSItems.LOCKSOFTENER).input(MSItems.KEYBLADE).and().input(MSItems.CLOTHES_IRON).build(consumer);
		CombinationRecipeBuilder.of(MSItems.BISEKEYAL).input(MSItems.KEYBLADE).and().input(MSItems.BI_DYE).build(consumer);
		CombinationRecipeBuilder.of(MSItems.KEY_TO_THE_MACHINE).input(MSItems.KEYBLADE).or().input(Items.REPEATER).build(consumer);
		CombinationRecipeBuilder.of(MSItems.KEY_TO_THE_CITY).input(MSItems.DEMOCRATIC_DEMOLITIONER).or().input(MSItems.KEY_TO_THE_MACHINE).build(consumer);
		CombinationRecipeBuilder.of(MSItems.INNER_HEART).input(MSItems.GAMEGRL_MAGAZINE).and().input(MSItems.KEYBLADE).build(consumer);
		CombinationRecipeBuilder.of(MSItems.DRAGON_KEY).input(MSItems.KEYBLADE).and().input(MSItems.SCALEMATE_APPLESCAB).build(consumer);
		CombinationRecipeBuilder.of(MSItems.TRUE_BLUE).input(MSItems.KEYBLADE).and().input(MSItems.EIGHTBALL).build(consumer);
		CombinationRecipeBuilder.of(MSItems.BLUE_BEAMS).input(MSItems.TRUE_BLUE).or().input(MSItems.SHATTER_BEACON).build(consumer);
		CombinationRecipeBuilder.of(MSItems.INKSPLOCKER_UNLOCKER).input(MSItems.KEYBLADE).and().input(MSItems.INK_SQUID_PRO_QUO).build(consumer);
		CombinationRecipeBuilder.of(MSItems.INKSQUIDDER_DEPTHKEY).input(MSItems.INKSPLOCKER_UNLOCKER).or().input(Items.HEART_OF_THE_SEA).build(consumer);
		CombinationRecipeBuilder.of(MSItems.REGIKEY).input(MSItems.KEYBLADE).or().input(MSBlocks.CHESSBOARD).build(consumer);
		CombinationRecipeBuilder.of(MSItems.CLOCKKEEPER).input(MSItems.REGIKEY).and().input(Items.CLOCK).build(consumer);
		CombinationRecipeBuilder.of(MSItems.HOME_BY_MIDNIGHT).input(MSItems.CLOCKKEEPER).or().input(MSItems.ESTROGEN_EMPOWERED_EVERYTHING_ERADICATOR).build(consumer);
		CombinationRecipeBuilder.of(MSItems.LATCHMELTER).input(MSItems.BLAZING_GLORY).and().input(MSItems.HOUSE_KEY).build(consumer);
		CombinationRecipeBuilder.of(MSItems.CRIMSON_LEAP).input(MSItems.FROG).and().input(MSItems.KEYBLADE).build(consumer);
		CombinationRecipeBuilder.of(MSItems.NO_TIME_FOR_FLIES).input(MSItems.CLOCKKEEPER).and().input(MSItems.CRIMSON_LEAP).build(consumer);
		CombinationRecipeBuilder.of(MSItems.NATURES_HEART).input(MSItems.CROP_CHOP).or().input(MSItems.KEYBLADE).build(consumer);
		CombinationRecipeBuilder.of(MSItems.LOCH_PICK).input(MSItems.CRYPTID_PHOTO).or().input(MSItems.DRAGON_KEY).build(consumer);
		CombinationRecipeBuilder.of(MSItems.YALDABAOTHS_KEYTON).input(MSItems.LATCHMELTER).or().input(MSItems.CELESTIAL_FULCRUM).build(consumer);
		CombinationRecipeBuilder.of(MSItems.KEYTAR).input(MSItems.HOUSE_KEY).and().input(Items.NOTE_BLOCK).build(consumer);
		CombinationRecipeBuilder.of(MSItems.ALLWEDDOL).input(MSItems.YALDABAOTHS_KEYTON).and().input(MSItems.CUEBALL).build(consumer);
		
		CombinationRecipeBuilder.of(MSItems.CONDUCTORS_BATON).input(Items.STICK).and().input(Items.NOTE_BLOCK).build(consumer);
		CombinationRecipeBuilder.of(MSItems.SHARP_NOTE).input(MSItems.KNITTING_NEEDLE).and().input(MSItems.CONDUCTORS_BATON).build(consumer);
		CombinationRecipeBuilder.of(MSItems.URANIUM_BATON).input(MSItems.RAW_URANIUM).or().input(MSItems.CONDUCTORS_BATON).build(consumer);
		CombinationRecipeBuilder.of(MSItems.WIND_WAKER).input(Items.OAK_BOAT).or().input(MSItems.CONDUCTORS_BATON).build(consumer);
		CombinationRecipeBuilder.of(MSItems.CELESTIAL_FULCRUM).input(Items.AMETHYST_SHARD).or().input(MSItems.CONDUCTORS_BATON).build(consumer);
		CombinationRecipeBuilder.of(MSItems.HYMN_FOR_HORRORTERRORS).input(MSItems.THORN_OF_OGLOGOTH).and().input(MSItems.CELESTIAL_FULCRUM).build(consumer);
		CombinationRecipeBuilder.of(MSItems.TV_ANTENNA).input(MSItems.CONDUCTORS_BATON).and().input(MSBlocks.OLD_COMPUTER).build(consumer);
		
		CombinationRecipeBuilder.of(MSItems.BO_STAFF).input(Items.STICK).and().input(Items.IRON_INGOT).build(consumer);
		CombinationRecipeBuilder.of(MSItems.BAMBOO_BEATSTICK).input(MSItems.BO_STAFF).and().input(Items.BAMBOO).build(consumer);
		CombinationRecipeBuilder.of(MSItems.BARBERS_MAGIC_TOUCH).input(MSItems.BARBASOL).and().input(MSItems.WIZARD_STAFF).build(consumer);
		CombinationRecipeBuilder.of(MSItems.TELESCOPIC_BEATDOWN_BRUISER).input(MSItems.BO_STAFF).or().input(Items.SPYGLASS).build(consumer);
		CombinationRecipeBuilder.of(MSItems.ION_DESTABILIZER).input(MSItems.IRON_LASS_CHESTPLATE).or().input(MSItems.BO_STAFF).build(consumer);
		
		CombinationRecipeBuilder.of(MSItems.BATLEACKS).namedInput(MSItems.BATTLEAXE).and().input(MSItems.SBAHJ_POSTER).build(consumer);
		CombinationRecipeBuilder.of(MSItems.BATLEACKS).namedInput(Items.WOODEN_AXE).and().input(MSItems.SBAHJ_POSTER).build(consumer);
		CombinationRecipeBuilder.of(MSItems.COPSE_CRUSHER).input(Items.IRON_AXE).and().input(Items.PISTON).build(consumer);
		CombinationRecipeBuilder.of(MSItems.QUENCH_CRUSHER).input(MSItems.COPSE_CRUSHER).or().input(MSItems.DESERT_FRUIT).build(consumer);
		CombinationRecipeBuilder.of(MSItems.MELONSBANE).input(MSItems.QUENCH_CRUSHER).or().namedInput(Items.MELON_SLICE).build(consumer);
		CombinationRecipeBuilder.of(MSItems.MELONSBANE).input(MSItems.QUENCH_CRUSHER).or().namedInput(Items.MELON).build(consumer);
		CombinationRecipeBuilder.of(MSItems.CROP_CHOP).input(Items.GOLDEN_AXE).or().namedInput(Items.POTATO).build(consumer);
		CombinationRecipeBuilder.of(MSItems.CROP_CHOP).input(Items.GOLDEN_AXE).or().namedInput(Items.CARROT).build(consumer);
		CombinationRecipeBuilder.of(MSItems.THE_LAST_STRAW).input(MSItems.CROP_CHOP).and().input(Items.HAY_BLOCK).build(consumer);
		CombinationRecipeBuilder.of(MSItems.BATTLEAXE).input(Items.IRON_AXE).and().input(Items.IRON_BLOCK).build(consumer);
		CombinationRecipeBuilder.of(MSItems.CANDY_BATTLEAXE).input(MSItems.BATTLEAXE).or().input(MSItems.CANDY_CORN).build(consumer);
		CombinationRecipeBuilder.of(MSItems.CHOCO_LOCO_WOODSPLITTER).input(MSItems.CANDY_BATTLEAXE).or().input(Items.COCOA_BEANS).build(consumer);
		CombinationRecipeBuilder.of(MSItems.STEEL_EDGE_CANDYCUTTER).input(MSItems.CHOCO_LOCO_WOODSPLITTER).and().input(Items.IRON_INGOT).build(consumer);
		CombinationRecipeBuilder.of(MSItems.BLACKSMITH_BANE).input(Items.WOODEN_AXE).and().input(Items.ANVIL).build(consumer);
		CombinationRecipeBuilder.of(MSItems.REGIAXE).input(MSItems.BLACKSMITH_BANE).and().input(MSBlocks.CHESSBOARD).build(consumer);
		CombinationRecipeBuilder.of(MSItems.GOTHY_AXE).input(MSItems.REGIAXE).or().input(Items.SPIDER_EYE).build(consumer);
		CombinationRecipeBuilder.of(MSItems.SURPRISE_AXE).input(MSItems.BLACKSMITH_BANE).and().input(MSItems.SURPRISE_EMBRYO).build(consumer);
		CombinationRecipeBuilder.of(MSItems.SHOCK_AXE).input(MSItems.SURPRISE_AXE).and().input(MSItems.BATTERY).build(consumer);
		CombinationRecipeBuilder.of(MSItems.SCRAXE).input(Items.IRON_AXE).and().input(ItemTags.MUSIC_DISCS).build(consumer);
		CombinationRecipeBuilder.of(MSItems.LORENTZ_DISTRANSFORMATIONER).input(Items.IRON_AXE).and().namedInput(MSBlocks.TRANSPORTALIZER).build(consumer);
		CombinationRecipeBuilder.of(MSItems.LORENTZ_DISTRANSFORMATIONER).input(Items.IRON_AXE).and().namedInput(Items.CHORUS_FRUIT).build(consumer);
		CombinationRecipeBuilder.of(MSItems.PISTON_POWERED_POGO_AXEHAMMER).input(MSItems.COPSE_CRUSHER).and().input(MSItems.POGO_HAMMER).build(consumer);
		CombinationRecipeBuilder.of(MSItems.FISSION_FOCUSED_FAULT_FELLER).input(MSItems.PISTON_POWERED_POGO_AXEHAMMER).and().input(MSItems.ENERGY_CORE).build(consumer);
		CombinationRecipeBuilder.of(MSItems.HEPHAESTUS_LUMBERJACK).input(Items.GOLDEN_AXE).and().input(Items.LAVA_BUCKET).build(consumer);
		CombinationRecipeBuilder.of(MSItems.RUBY_CROAK).input(MSItems.EMERALD_AXE).and().input(MSItems.FROG).build(consumer);
		CombinationRecipeBuilder.of(MSItems.BISECTOR).input(MSItems.BI_DYE).and().input(Items.IRON_AXE).build(consumer);
		CombinationRecipeBuilder.of(MSItems.FINE_CHINA_AXE).input(Items.DECORATED_POT).or().input(Items.DIAMOND_AXE).build(consumer);
		
		CombinationRecipeBuilder.of(MSItems.SICKLE).input(Items.IRON_HOE).and().input(Items.WHEAT).build(consumer);
		CombinationRecipeBuilder.of(MSItems.OW_THE_EDGE).input(MSItems.HEMEOREAPER).or().input(MSItems.SBAHJ_POSTER).build(consumer);
		CombinationRecipeBuilder.of(MSItems.THORNY_SUBJECT).input(MSItems.SICKLE).and().input(Items.ROSE_BUSH).build(consumer);
		CombinationRecipeBuilder.of(MSItems.SNOW_WHITE_DREAM).input(MSItems.THORNY_SUBJECT).and().input(Items.APPLE).build(consumer);
		CombinationRecipeBuilder.of(MSItems.HOMES_SMELL_YA_LATER).input(MSItems.SICKLE).or().input(MSItems.THRESH_DVD).build(consumer);
		CombinationRecipeBuilder.of(MSItems.HEMEOREAPER).input(MSItems.HOMES_SMELL_YA_LATER).or().input(MSItems.BLOOD_BUCKET).build(consumer);
		CombinationRecipeBuilder.of(MSItems.FUDGESICKLE).input(MSItems.SICKLE).or().input(Items.COCOA_BEANS).build(consumer);
		CombinationRecipeBuilder.of(MSItems.REGISICKLE).input(MSItems.SICKLE).and().input(MSBlocks.CHESSBOARD).build(consumer);
		CombinationRecipeBuilder.of(MSItems.HERETICUS_AURURM).input(MSItems.REGISICKLE).and().input(Items.GOLD_INGOT).build(consumer);
		CombinationRecipeBuilder.of(MSItems.CANDY_SICKLE).input(MSItems.SICKLE).or().namedInput(MSItems.CANDY_CORN).build(consumer);
		CombinationRecipeBuilder.of(MSItems.CANDY_SICKLE).namedInput(MSItems.FUDGESICKLE).and().input(Items.SUGAR).build(consumer);
		CombinationRecipeBuilder.of(MSItems.CLAW_SICKLE).namedInput(MSItems.CAT_CLAWS_DRAWN).and().input(MSItems.HEMEOREAPER).build(consumer);
		CombinationRecipeBuilder.of(MSItems.CLAW_SICKLE).namedInput(MSItems.CAT_CLAWS_SHEATHED).and().input(MSItems.HEMEOREAPER).build(consumer);
		CombinationRecipeBuilder.of(MSItems.CLAW_OF_NRUBYIGLITH).namedInput(MSItems.CLAW_SICKLE).and().input(MSItems.GRIMOIRE).build(consumer);
		CombinationRecipeBuilder.of(MSItems.SCYTHE).input(MSItems.SICKLE).and().input(Items.WOODEN_SWORD).build(consumer);
		CombinationRecipeBuilder.of(MSItems.MARASCHINO_CHERRY_SCYTHE).input(MSItems.SCYTHE).and().input(MSItems.CANDY_CORN).build(consumer);
		CombinationRecipeBuilder.of(MSItems.PROSPECTING_PICKSCYTHE).input(MSItems.SCYTHE).and().input(Items.GOLDEN_PICKAXE).build(consumer);
		CombinationRecipeBuilder.of(MSItems.EIGHTBALL_SCYTHE).input(MSItems.SCYTHE).and().input(MSItems.EIGHTBALL).build(consumer);
		CombinationRecipeBuilder.of(MSItems.TIME_FLAYER).input(MSItems.EIGHTBALL_SCYTHE).and().input(Items.CLOCK).build(consumer);
		CombinationRecipeBuilder.of(MSItems.DESTINY_DECIMATOR).input(Items.NETHERITE_HOE).or().input(Items.WITHER_SKELETON_SKULL).build(consumer);
		CombinationRecipeBuilder.of(MSItems.SUNRAY_HARVESTER).input(MSItems.SCYTHE).and().input(Items.LAVA_BUCKET).build(consumer);
		CombinationRecipeBuilder.of(MSItems.GREEN_SUN_RAYREAPER).input(MSItems.SUNRAY_HARVESTER).and().input(MSItems.RAW_URANIUM).build(consumer);
		CombinationRecipeBuilder.of(MSItems.SKAITHE).input(MSItems.SCYTHE).and().input(MSBlocks.CHESSBOARD).build(consumer);
		CombinationRecipeBuilder.of(MSItems.HELLBRINGERS_HOE_INACTIVE).input(Items.NETHERITE_HOE).or().input(MSItems.CRYPTID_PHOTO).build(consumer);
		
		CombinationRecipeBuilder.of(MSItems.STALE_BAGUETTE).input(Items.BREAD).or().namedInput(Items.STICK).build(consumer);
		CombinationRecipeBuilder.of(MSItems.STALE_BAGUETTE).input(Items.BREAD).or().namedInput(MSItems.DEUCE_CLUB).build(consumer);
		CombinationRecipeBuilder.of(MSItems.GLUB_CLUB).input(MSItems.DEUCE_CLUB).or().namedInput(Items.COD).build(consumer);
		CombinationRecipeBuilder.of(MSItems.GLUB_CLUB).input(MSItems.DEUCE_CLUB).or().namedInput(Items.SALMON).build(consumer);
		CombinationRecipeBuilder.of(MSItems.GLUB_CLUB).input(MSItems.DEUCE_CLUB).or().namedInput(Items.TROPICAL_FISH).build(consumer);
		CombinationRecipeBuilder.of(MSItems.PRISMARINE_BASHER).input(MSItems.GLUB_CLUB).or().namedInput(Items.PRISMARINE).build(consumer);
		CombinationRecipeBuilder.of(MSItems.PRISMARINE_BASHER).input(MSItems.GLUB_CLUB).or().namedInput(Items.PRISMARINE_SHARD).build(consumer);
		CombinationRecipeBuilder.of(MSItems.PRISMARINE_BASHER).input(MSItems.GLUB_CLUB).or().namedInput(Items.DARK_PRISMARINE).build(consumer);
		CombinationRecipeBuilder.of(MSItems.PRISMARINE_BASHER).input(MSItems.GLUB_CLUB).or().namedInput(Items.PRISMARINE_BRICKS).build(consumer);
		CombinationRecipeBuilder.of(MSItems.CLUB_ZERO).input(MSItems.PRISMARINE_BASHER).and().namedInput(Items.ICE).build(consumer);
		CombinationRecipeBuilder.of(MSItems.CLUB_ZERO).input(MSItems.PRISMARINE_BASHER).and().namedInput(Items.PACKED_ICE).build(consumer);
		CombinationRecipeBuilder.of(MSItems.CLUB_ZERO).input(MSItems.PRISMARINE_BASHER).and().namedInput(Items.BLUE_ICE).build(consumer);
		CombinationRecipeBuilder.of(MSItems.CLUB_ZERO).input(MSItems.PRISMARINE_BASHER).and().namedInput(MSItems.ICE_SHARD).build(consumer);
		CombinationRecipeBuilder.of(MSItems.POGO_CLUB).input(MSItems.DEUCE_CLUB).and().input(Items.SLIME_BALL).build(consumer);
		CombinationRecipeBuilder.of(MSItems.BARBER_BASHER).input(MSItems.DEUCE_CLUB).and().input(MSItems.BARBASOL).build(consumer);
		CombinationRecipeBuilder.of(MSItems.NIGHT_CLUB).input(MSItems.DEUCE_CLUB).and().input(MSItems.CREW_POSTER).build(consumer);
		CombinationRecipeBuilder.of(MSItems.NIGHTSTICK).input(MSItems.NIGHT_CLUB).and().input(MSItems.SCALEMATE_APPLESCAB).build(consumer);
		CombinationRecipeBuilder.of(MSItems.RED_EYES).input(MSItems.NIGHT_CLUB).and().namedInput(Items.SPIDER_EYE).build(consumer);
		CombinationRecipeBuilder.of(MSItems.METAL_BAT).input(MSItems.DEUCE_CLUB).and().input(Items.IRON_INGOT).build(consumer);
		CombinationRecipeBuilder.of(MSItems.CRICKET_BAT).input(MSItems.METAL_BAT).or().input(MSItems.GRASSHOPPER).build(consumer);
		CombinationRecipeBuilder.of(MSItems.CLOWN_CLUB).input(MSItems.METAL_BAT).or().input(MSItems.HORN).build(consumer);
		CombinationRecipeBuilder.of(MSItems.DOCTOR_DETERRENT).input(MSItems.SNOW_WHITE_DREAM).and().input(MSItems.DEUCE_CLUB).build(consumer);
		CombinationRecipeBuilder.of(MSItems.SPIKED_CLUB).input(MSItems.METAL_BAT).or().input(ItemTags.LOGS).build(consumer);
		CombinationRecipeBuilder.of(MSItems.M_ACE).input(MSItems.MACE).and().input(MSItems.NONBINARY_CODE).build(consumer);
		CombinationRecipeBuilder.of(MSItems.DESOLATOR_MACE).input(MSItems.MACE).or().input(Items.FLINT).build(consumer);
		CombinationRecipeBuilder.of(MSItems.BLAZING_GLORY).input(MSItems.MACE).and().namedInput(Items.BLAZE_POWDER).build(consumer);
		CombinationRecipeBuilder.of(MSItems.RUBIKS_MACE).input(MSItems.MACE).and().input(MSItems.WATER_COLORS_BUCKET).build(consumer);
		CombinationRecipeBuilder.of(MSItems.M_ACE_OF_CLUBS).input(MSItems.M_ACE).and().input(MSItems.ACE_OF_CLUBS).build(consumer);
		CombinationRecipeBuilder.of(MSItems.HOME_GROWN_MACE).input(MSItems.MACE).and().input(Items.BAMBOO).build(consumer);
		CombinationRecipeBuilder.of(MSItems.CARNIE_CLUB).input(MSItems.CLOWN_CLUB).and().input(MSItems.CRYPTID_PHOTO).build(consumer);
		CombinationRecipeBuilder.of(MSItems.TOFFEE_CLUB).input(MSItems.METAL_BAT).and().input(MSItems.CANDY_CORN).build(consumer);
		
		CombinationRecipeBuilder.of(MSItems.SLEDGE_HAMMER).input(MSItems.CLAW_HAMMER).and().namedInput(Items.BRICKS).build(consumer);
		CombinationRecipeBuilder.of(MSItems.SLEDGE_HAMMER).input(MSItems.CLAW_HAMMER).and().namedInput(Items.COBBLESTONE).build(consumer);
		CombinationRecipeBuilder.of(MSItems.MAILBOX).input(MSItems.SLEDGE_HAMMER).and().namedInput(Items.CHEST).build(consumer);
		CombinationRecipeBuilder.of(MSItems.BLACKSMITH_HAMMER).input(MSItems.SLEDGE_HAMMER).and().namedInput(Items.ANVIL).build(consumer);
		CombinationRecipeBuilder.of(MSItems.BLACKSMITH_HAMMER).input(MSItems.SLEDGE_HAMMER).and().namedInput(Items.IRON_BLOCK).build(consumer);
		CombinationRecipeBuilder.of(MSItems.POGO_HAMMER).input(MSItems.SLEDGE_HAMMER).and().input(Items.SLIME_BALL).build(consumer);
		CombinationRecipeBuilder.of(MSItems.WRINKLEFUCKER).input(MSItems.POGO_HAMMER).or().input(MSItems.CLOTHES_IRON).build(consumer);
		CombinationRecipeBuilder.of(MSItems.DEMOCRATIC_DEMOLITIONER).input(MSItems.MAILBOX).or().input(MSItems.FOOD_CAN).build(consumer);
		CombinationRecipeBuilder.of(MSItems.BOOMBOX_BEATER).input(MSItems.CASSETTE_PLAYER).or().input(MSItems.SLEDGE_HAMMER).build(consumer);
		CombinationRecipeBuilder.of(MSItems.REGI_HAMMER).input(MSItems.CLAW_HAMMER).and().input(MSBlocks.CHESSBOARD).build(consumer);
		CombinationRecipeBuilder.of(MSItems.FEAR_NO_ANVIL).input(MSItems.BLACKSMITH_HAMMER).or().input(Items.CLOCK).build(consumer);
		CombinationRecipeBuilder.of(MSItems.TELESCOPIC_SASSACRUSHER).input(MSItems.SLEDGE_HAMMER).and().input(MSItems.SASSACRE_TEXT).build(consumer);
		CombinationRecipeBuilder.of(MSItems.MELT_MASHER).input(MSItems.FEAR_NO_ANVIL).or().input(Items.LAVA_BUCKET).build(consumer);
		CombinationRecipeBuilder.of(MSItems.ESTROGEN_EMPOWERED_EVERYTHING_ERADICATOR).input(MSItems.FISSION_FOCUSED_FAULT_FELLER).or().input(MSItems.GAMEGRL_MAGAZINE).build(consumer);
		CombinationRecipeBuilder.of(MSItems.EEEEEEEEEEEE).input(MSItems.ESTROGEN_EMPOWERED_EVERYTHING_ERADICATOR).and().input(MSItems.SBAHJ_POSTER).build(consumer);
		CombinationRecipeBuilder.of(MSItems.POPAMATIC_VRILLYHOO).input(MSItems.ZILLYHOO_HAMMER).and().input(MSItems.FLUORITE_OCTET).build(consumer);
		CombinationRecipeBuilder.of(MSItems.SCARLET_ZILLYHOO).input(MSItems.ZILLYHOO_HAMMER).and().input(MSItems.FROG).build(consumer);
		CombinationRecipeBuilder.of(MSItems.MWRTHWL).input(MSItems.REGI_HAMMER).or().input(MSItems.CUEBALL).build(consumer);
		
		CombinationRecipeBuilder.of(MSItems.WIZARD_STAFF).input(MSItems.MINI_WIZARD_STATUE).and().input(MSItems.CANE).build(consumer);
		CombinationRecipeBuilder.of(MSItems.WATER_STAFF).input(MSItems.WIZARD_STAFF).and().input(Items.HEART_OF_THE_SEA).build(consumer);
		CombinationRecipeBuilder.of(MSItems.FIRE_STAFF).input(MSItems.WIZARD_STAFF).and().input(Items.MAGMA_CREAM).build(consumer);
		CombinationRecipeBuilder.of(MSItems.PRIME_STAFF).input(MSItems.BLACK_KINGS_SCEPTER).and().input(MSItems.SBAHJ_POSTER).build(consumer);
		
		CombinationRecipeBuilder.of(MSItems.VAUDEVILLE_HOOK).namedInput(MSItems.CANE).and().input(Items.FISHING_ROD).build(consumer);
		CombinationRecipeBuilder.of(MSItems.VAUDEVILLE_HOOK).namedInput(Items.STICK).or().input(MSItems.WISEGUY).build(consumer);
		CombinationRecipeBuilder.of(MSItems.BEAR_POKING_STICK).namedInput(MSItems.CANE).or().input(Items.LEATHER).build(consumer);
		CombinationRecipeBuilder.of(MSItems.BEAR_POKING_STICK).namedInput(Items.STICK).or().input(Items.LEATHER).build(consumer);
		CombinationRecipeBuilder.of(MSItems.UMBRELLA).input(Items.STICK).and().namedInput(Items.BLACK_WOOL).build(consumer);
		CombinationRecipeBuilder.of(MSItems.UMBRELLA).input(Items.STICK).and().namedInput(Items.SHIELD).build(consumer);
		CombinationRecipeBuilder.of(MSItems.UMBRELLA).namedInput(MSItems.CANE).and().namedInput(Items.BLACK_WOOL).build(consumer);
		CombinationRecipeBuilder.of(MSItems.UMBRELLA).namedInput(MSItems.CANE).and().namedInput(Items.SHIELD).build(consumer);
		CombinationRecipeBuilder.of(MSItems.BARBERS_BEST_FRIEND).input(MSItems.UMBRELLA).and().input(MSItems.RAZOR_BLADE).build(consumer);
		CombinationRecipeBuilder.of(MSItems.UPPER_CRUST_CRUST_CANE).input(MSItems.PRIM_AND_PROPER_WALKING_POLE).or().namedInput(MSItems.STALE_BAGUETTE).build(consumer);
		CombinationRecipeBuilder.of(MSItems.UPPER_CRUST_CRUST_CANE).input(MSItems.PRIM_AND_PROPER_WALKING_POLE).or().namedInput(Items.BREAD).build(consumer);
		CombinationRecipeBuilder.of(MSItems.KISSY_CUTIE_HEART_HITTER).input(MSItems.IRON_CANE).or().input(MSItems.GAMEGRL_MAGAZINE).build(consumer);
		CombinationRecipeBuilder.of(MSItems.KISSY_CUTIE_HEART_SPLITTER).input(MSItems.SCYTHE).or().input(MSItems.GAMEGRL_MAGAZINE).build(consumer);
		CombinationRecipeBuilder.of(MSItems.MUTANT_CUTIE_CELL_PUTTER).input(MSItems.KISSY_CUTIE_HEART_HITTER).and().input(MSItems.PLUSH_MUTATED_CAT).build(consumer);
		CombinationRecipeBuilder.of(MSItems.MUTANT_CUTIE_CELL_CUTTER).input(MSItems.KISSY_CUTIE_HEART_SPLITTER).and().input(MSItems.PLUSH_MUTATED_CAT).build(consumer);
		CombinationRecipeBuilder.of(MSItems.ZEPHYR_CANE).namedInput(MSItems.IRON_CANE).or().namedInput(Items.FEATHER).build(consumer);
		CombinationRecipeBuilder.of(MSItems.ZEPHYR_CANE).namedInput(MSItems.IRON_CANE).or().namedInput(Items.PHANTOM_MEMBRANE).build(consumer);
		CombinationRecipeBuilder.of(MSItems.SPEAR_CANE).input(MSItems.CANE).or().namedInput(Items.IRON_SWORD).build(consumer);
		CombinationRecipeBuilder.of(MSItems.SPEAR_CANE).input(MSItems.CANE).or().namedInput(MSItems.KATANA).build(consumer);
		CombinationRecipeBuilder.of(MSItems.SPEAR_CANE).input(MSItems.IRON_CANE).or().namedInput(Items.STONE_SWORD).build(consumer);
		CombinationRecipeBuilder.of(MSItems.SPEAR_CANE).namedInput(MSItems.IRON_CANE).or().namedInput(Items.IRON_SWORD).build(consumer);
		CombinationRecipeBuilder.of(MSItems.SPEAR_CANE).namedInput(MSItems.IRON_CANE).or().namedInput(MSItems.KATANA).build(consumer);
		CombinationRecipeBuilder.of(MSItems.PARADISES_PORTABELLO).input(Items.STICK).or().namedInput(Items.RED_MUSHROOM).build(consumer);
		CombinationRecipeBuilder.of(MSItems.PARADISES_PORTABELLO).input(Items.STICK).or().namedInput(Items.BROWN_MUSHROOM).build(consumer);
		CombinationRecipeBuilder.of(MSItems.REGI_CANE).namedInput(MSItems.CANE).and().input(MSBlocks.CHESSBOARD).build(consumer);
		CombinationRecipeBuilder.of(MSItems.REGI_CANE).namedInput(MSItems.IRON_CANE).and().input(MSBlocks.CHESSBOARD).build(consumer);
		CombinationRecipeBuilder.of(MSItems.URANIUM_POWERED_STICK).input(Items.STICK).or().input(MSItems.RAW_URANIUM).build(consumer);
		CombinationRecipeBuilder.of(MSItems.CANDY_CANE).input(MSItems.VAUDEVILLE_HOOK).and().namedInput(MSItems.CANDY_CORN).build(consumer);
		CombinationRecipeBuilder.of(MSItems.CANDY_CANE).input(MSItems.VAUDEVILLE_HOOK).and().namedInput(MSItems.DIAMOND_MINT).build(consumer);
		CombinationRecipeBuilder.of(MSItems.CANDY_CANE).input(MSItems.VAUDEVILLE_HOOK).or().namedInput(MSItems.CANDY_SICKLE).build(consumer);
		CombinationRecipeBuilder.of(MSItems.PRIM_AND_PROPER_WALKING_POLE).namedInput(MSItems.CANE).and().input(Items.GOLD_INGOT).build(consumer);
		CombinationRecipeBuilder.of(MSItems.PRIM_AND_PROPER_WALKING_POLE).namedInput(MSItems.VAUDEVILLE_HOOK).and().input(Items.GOLD_INGOT).build(consumer);
		CombinationRecipeBuilder.of(MSItems.LESS_PROPER_WALKING_STICK).input(MSItems.PRIM_AND_PROPER_WALKING_POLE).and().namedInput(MSItems.KATANA).build(consumer);
		CombinationRecipeBuilder.of(MSItems.LESS_PROPER_WALKING_STICK).input(MSItems.PRIM_AND_PROPER_WALKING_POLE).and().namedInput(MSItems.UNBREAKABLE_KATANA).build(consumer);
		CombinationRecipeBuilder.of(MSItems.LESS_PROPER_WALKING_STICK).input(MSItems.PRIM_AND_PROPER_WALKING_POLE).and().namedInput(MSItems.IRON_CANE).build(consumer);
		CombinationRecipeBuilder.of(MSItems.ROCKEFELLERS_WALKING_BLADECANE).input(MSItems.LESS_PROPER_WALKING_STICK).and().input(Items.OBSIDIAN).build(consumer);
		CombinationRecipeBuilder.of(MSItems.DRAGON_CANE).input(MSItems.ROCKEFELLERS_WALKING_BLADECANE).or().namedInput(Items.DRAGON_BREATH).build(consumer);
		CombinationRecipeBuilder.of(MSItems.DRAGON_CANE).input(MSItems.ROCKEFELLERS_WALKING_BLADECANE).or().namedInput(Items.DRAGON_HEAD).build(consumer);
		CombinationRecipeBuilder.of(MSItems.CHANCEWYRMS_EXTRA_FORTUNATE_STABBING_IMPLEMENT).input(MSItems.DRAGON_CANE).and().namedInput(MSItems.FLUORITE_OCTET).build(consumer);
		
		CombinationRecipeBuilder.of(MSItems.WOODEN_SPOON).input(Items.WOODEN_SHOVEL).and().namedInput(Items.BOWL).build(consumer);
		CombinationRecipeBuilder.of(MSItems.WOODEN_SPOON).input(Items.WOODEN_SHOVEL).and().namedInput(Items.MUSHROOM_STEW).build(consumer);
		CombinationRecipeBuilder.of(MSItems.WOODEN_SPOON).input(Items.WOODEN_SHOVEL).and().namedInput(Items.RABBIT_STEW).build(consumer);
		CombinationRecipeBuilder.of(MSItems.WOODEN_SPOON).input(Items.WOODEN_SHOVEL).and().namedInput(Items.BEETROOT_SOUP).build(consumer);
		CombinationRecipeBuilder.of(MSItems.SILVER_SPOON).input(Items.IRON_SHOVEL).and().namedInput(Items.BOWL).build(consumer);
		CombinationRecipeBuilder.of(MSItems.SILVER_SPOON).input(Items.IRON_SHOVEL).and().namedInput(Items.MUSHROOM_STEW).build(consumer);
		CombinationRecipeBuilder.of(MSItems.SILVER_SPOON).input(Items.IRON_SHOVEL).and().namedInput(Items.RABBIT_STEW).build(consumer);
		CombinationRecipeBuilder.of(MSItems.SILVER_SPOON).input(Items.IRON_SHOVEL).and().namedInput(Items.BEETROOT_SOUP).build(consumer);
		CombinationRecipeBuilder.of(MSItems.SILVER_SPOON).namedInput(MSItems.WOODEN_SPOON).and().input(Items.IRON_INGOT).build(consumer);
		CombinationRecipeBuilder.of(MSItems.MELONBALLER).input(MSItems.SILVER_SPOON).or().namedInput(Items.MELON_SLICE).build(consumer);
		CombinationRecipeBuilder.of(MSItems.SIGHTSEEKER).input(MSItems.SILVER_SPOON).or().namedInput(Items.SPIDER_EYE).build(consumer);
		CombinationRecipeBuilder.of(MSItems.TERRAIN_FLATENATOR).input(Items.IRON_SHOVEL).or().namedInput(Items.PISTON).build(consumer);
		CombinationRecipeBuilder.of(MSItems.NOSFERATU_SPOON).input(MSItems.SILVER_SPOON).or().input(MSItems.GRIMOIRE).build(consumer);
		CombinationRecipeBuilder.of(MSItems.THRONGLER).input(MSItems.NOSFERATU_SPOON).and().input(MSItems.SBAHJ_POSTER).build(consumer);
		CombinationRecipeBuilder.of(MSItems.WET_MEAT_SHIT_THRONGLER).input(MSItems.THRONGLER).and().input(MSItems.MEATFORK).build(consumer);
		CombinationRecipeBuilder.of(MSItems.CROCKER_SPOON).input(MSItems.SILVER_SPOON).and().input(Items.CAKE).build(consumer);
		CombinationRecipeBuilder.of(MSItems.CANDY_FORK).input(MSItems.FORK).or().input(MSItems.CANDY_CORN).build(consumer);
		CombinationRecipeBuilder.of(MSItems.TUNING_FORK).input(MSItems.FORK).and().input(Items.NOTE_BLOCK).build(consumer);
		CombinationRecipeBuilder.of(MSItems.ELECTRIC_FORK).input(MSItems.FORK).or().input(MSItems.BATTERY).build(consumer);
		CombinationRecipeBuilder.of(MSItems.EATING_FORK_GEM).input(MSItems.NOSFERATU_SPOON).and().input(Items.PRISMARINE_CRYSTALS).build(consumer);
		CombinationRecipeBuilder.of(MSItems.DEVIL_FORK).input(MSItems.NOSFERATU_SPOON).and().input(Items.BLAZE_POWDER).build(consumer);
		CombinationRecipeBuilder.of(MSItems.SKAIA_FORK).input(MSItems.FORK).and().input(MSBlocks.CHESSBOARD).build(consumer);
		CombinationRecipeBuilder.of(MSItems.SKAIAN_CROCKER_ROCKER).input(MSItems.SKAIA_FORK).or().input(MSItems.CROCKER_FORK).build(consumer);
		CombinationRecipeBuilder.of(MSItems.SPORK).input(MSItems.FORK).or().input(MSItems.WOODEN_SPOON).build(consumer);
		CombinationRecipeBuilder.of(MSItems.GOLDEN_SPORK).input(MSItems.SPORK).or().input(Items.GOLD_INGOT).build(consumer);
		CombinationRecipeBuilder.of(MSItems.MEATFORK).input(Items.TRIDENT).and().input(Items.BEEF).build(consumer);
		CombinationRecipeBuilder.of(MSItems.BIDENT).input(MSItems.BI_DYE).and().input(Items.TRIDENT).build(consumer);
		CombinationRecipeBuilder.of(MSItems.DOUBLE_ENDED_TRIDENT).input(Items.TRIDENT).or().input(MSItems.MIRROR).build(consumer);
		CombinationRecipeBuilder.of(MSItems.EDISONS_FURY).input(MSItems.ELECTRIC_FORK).or().input(Items.REDSTONE_LAMP).build(consumer);
		
		CombinationRecipeBuilder.of(MSItems.CAT_CLAWS_DRAWN).input(MSItems.MAKESHIFT_CLAWS_DRAWN).and().input(Tags.Items.INGOTS_IRON).build(consumer);
		CombinationRecipeBuilder.of(MSItems.COFFEE_CLAWS_DRAWN).input(MSItems.CAT_CLAWS_DRAWN).and().input(MSItems.CANDY_CORN).build(consumer);
		CombinationRecipeBuilder.of(MSItems.POGO_CLAWS).input(MSItems.MAKESHIFT_CLAWS_DRAWN).and().input(Items.SLIME_BALL).build(consumer);
		CombinationRecipeBuilder.of(MSItems.ATOMIKITTY_KATAR_DRAWN).input(MSItems.CAT_CLAWS_DRAWN).and().input(MSItems.RAW_URANIUM).build(consumer);
		CombinationRecipeBuilder.of(MSItems.SKELETONIZER_DRAWN).input(MSItems.CAT_CLAWS_DRAWN).or().input(Items.BONE).build(consumer);
		CombinationRecipeBuilder.of(MSItems.SKELETON_DISPLACER_DRAWN).input(MSItems.SKELETONIZER_DRAWN).and().input(Items.ENDER_EYE).build(consumer);
		CombinationRecipeBuilder.of(MSItems.TEARS_OF_THE_ENDERLICH_DRAWN).input(MSItems.SKELETON_DISPLACER_DRAWN).and().input(Items.GHAST_TEAR).build(consumer);
		CombinationRecipeBuilder.of(MSItems.LION_LACERATORS_DRAWN).input(MSItems.TEARS_OF_THE_ENDERLICH_DRAWN).or().input(MSItems.CANDY_CORN).build(consumer);
		CombinationRecipeBuilder.of(MSItems.ACTION_CLAWS_DRAWN).input(MSItems.CAT_CLAWS_DRAWN).and().input(MSItems.FLUORITE_OCTET).build(consumer);
		
		CombinationRecipeBuilder.of(MSItems.LIPSTICK).input(MSItems.LIP_BALM).and().input(Items.GREEN_DYE).build(consumer);
		CombinationRecipeBuilder.of(MSItems.CAKESAW).input(MSItems.LIPSTICK_CHAINSAW).or().input(Items.CAKE).build(consumer);
		CombinationRecipeBuilder.of(MSItems.MAGENTA_MAULER).input(MSItems.LIPSTICK_CHAINSAW).and().input(Items.LILAC).build(consumer);
		CombinationRecipeBuilder.of(MSItems.THISTLEBLOWER).input(MSItems.LIPSTICK_CHAINSAW).or().input(Items.ROSE_BUSH).build(consumer);
		CombinationRecipeBuilder.of(MSItems.HAND_CRANKED_VAMPIRE_ERASER).input(MSItems.POINTY_STICK).or().input(MSItems.LIPSTICK_CHAINSAW).build(consumer);
		CombinationRecipeBuilder.of(MSItems.EMERALD_IMMOLATOR).input(MSItems.LIPSTICK_CHAINSAW).and().input(MSItems.EMERALD_SWORD).build(consumer);
		CombinationRecipeBuilder.of(MSItems.FROSTTOOTH).input(MSItems.EMERALD_IMMOLATOR).or().input(MSItems.CLUB_ZERO).build(consumer);
		CombinationRecipeBuilder.of(MSItems.OBSIDIATOR).input(MSItems.FROSTTOOTH).and().input(Items.OBSIDIAN).build(consumer);
		CombinationRecipeBuilder.of(MSItems.DEVILS_DELIGHT).input(MSItems.OBSIDIATOR).and().input(MSItems.CRYPTID_PHOTO).build(consumer);
		CombinationRecipeBuilder.of(MSItems.DEMONBANE_RAGRIPPER).input(MSItems.DEVILS_DELIGHT).and().input(MSItems.CANDY_CORN).build(consumer);
		CombinationRecipeBuilder.of(MSItems.CHAINSAW_KATANA).input(MSItems.LIPSTICK_CHAINSAW).and().input(MSItems.KATANA).build(consumer);
		
		CombinationRecipeBuilder.of(MSItems.LANEC).input(MSItems.WOODEN_LANCE).and().input(MSItems.SBAHJ_POSTER).build(consumer);
		CombinationRecipeBuilder.of(MSItems.JOUSTING_LANCE).input(MSItems.WOODEN_LANCE).and().input(Tags.Items.INGOTS_IRON).build(consumer);
		CombinationRecipeBuilder.of(MSItems.POGO_LANCE).input(MSItems.JOUSTING_LANCE).and().input(Items.SLIME_BALL).build(consumer);
        CombinationRecipeBuilder.of(MSItems.LANCELOTS_LOLLY).input(MSItems.JOUSTING_LANCE).and().input(MSItems.CANDY_CORN).build(consumer);
		CombinationRecipeBuilder.of(MSItems.DRAGON_LANCE).input(MSItems.JOUSTING_LANCE).and().input(MSItems.SCALEMATE_APPLESCAB).build(consumer);
		CombinationRecipeBuilder.of(MSItems.SKY_PIERCER).input(MSItems.DRAGON_LANCE).or().input(MSItems.STAR_RAY).build(consumer);
		CombinationRecipeBuilder.of(MSItems.FIDUSPAWN_LANCE).input(MSItems.DRAGON_LANCE).or().input(MSItems.URANIUM_GUMMY_BEAR).build(consumer);
		CombinationRecipeBuilder.of(MSItems.REGILANCE).input(MSItems.JOUSTING_LANCE).or().input(MSItems.CHESSBOARD).build(consumer);
		CombinationRecipeBuilder.of(MSItems.CIGARETTE_LANCE).input(MSItems.REGILANCE).and().input(MSItems.EIGHTBALL).build(consumer);
		
		CombinationRecipeBuilder.of(MSItems.LUCERNE_HAMMER).input(MSItems.SPEAR_CANE).and().namedInput(MSItems.CLAW_HAMMER).build(consumer);
		CombinationRecipeBuilder.of(MSItems.LUCERNE_HAMMER_OF_UNDYING).input(MSItems.LUCERNE_HAMMER).or().namedInput(Items.TOTEM_OF_UNDYING).build(consumer);
		
		CombinationRecipeBuilder.of(MSItems.CANDY_FAN).input(MSItems.FAN).and().input(MSItems.CANDY_CORN).build(consumer);
		CombinationRecipeBuilder.of(MSItems.SPINES_OF_FLUTHLU).input(MSItems.FAN).and().input(MSItems.GRIMOIRE).build(consumer);
		CombinationRecipeBuilder.of(MSItems.RAZOR_FAN).input(MSItems.FAN).and().input(MSItems.RAZOR_BLADE).build(consumer);
		CombinationRecipeBuilder.of(MSItems.MOTOR_FAN).input(MSItems.RAZOR_FAN).and().input(MSItems.BATTERY).build(consumer);
		CombinationRecipeBuilder.of(MSItems.ATOMIC_VAPORIZER).input(MSItems.MOTOR_FAN).or().input(MSItems.QUANTUM_SABRE).build(consumer);
		CombinationRecipeBuilder.of(MSItems.SHAVING_FAN).input(MSItems.RAZOR_FAN).and().input(MSItems.BARBASOL).build(consumer);
		CombinationRecipeBuilder.of(MSItems.FIRESTARTER).input(MSItems.RAZOR_FAN).or().input(MSItems.BLAZING_GLORY).build(consumer);
		CombinationRecipeBuilder.of(MSItems.STAR_RAY).input(MSItems.FIRESTARTER).or().input(MSItems.SUNRAY_HARVESTER).build(consumer);
		CombinationRecipeBuilder.of(MSItems.TYPHONIC_TRIVIALIZER).input(MSItems.FAN).or().namedInput(MSBlocks.SMOOTH_SHADE_STONE).build(consumer);
		
		CombinationRecipeBuilder.of(MSItems.WAND).input(MSItems.MINI_WIZARD_STATUE).or().input(Items.STICK).build(consumer);
		CombinationRecipeBuilder.of(MSItems.NEEDLE_WAND).input(MSItems.KNITTING_NEEDLE).and().namedInput(MSBlocks.MINI_WIZARD_STATUE).build(consumer);
		CombinationRecipeBuilder.of(MSItems.ARTIFUCKER).input(MSItems.POINTER_WAND).and().namedInput(MSItems.SBAHJ_POSTER).build(consumer);
		CombinationRecipeBuilder.of(MSItems.POINTER_WAND).input(MSItems.NEEDLE_WAND).and().namedInput(MSBlocks.COMPUTER).build(consumer);
		CombinationRecipeBuilder.of(MSItems.POOL_CUE_WAND).input(MSItems.NEEDLE_WAND).and().namedInput(MSItems.CUESTICK).build(consumer);
		CombinationRecipeBuilder.of(MSItems.THORN_OF_OGLOGOTH).input(MSItems.NEEDLE_WAND).and().namedInput(MSItems.GRIMOIRE).build(consumer);
		
		CombinationRecipeBuilder.of(MSItems.SBAHJARANG).input(MSItems.SHURIKEN).and().namedInput(MSItems.SBAHJ_POSTER).build(consumer);
		CombinationRecipeBuilder.of(MSItems.CLUBS_SUITARANG).input(MSItems.SHURIKEN).and().namedInput(MSItems.ACE_OF_CLUBS).build(consumer);
		CombinationRecipeBuilder.of(MSItems.DIAMONDS_SUITARANG).input(MSItems.SHURIKEN).and().namedInput(MSItems.ACE_OF_DIAMONDS).build(consumer);
		CombinationRecipeBuilder.of(MSItems.HEARTS_SUITARANG).input(MSItems.SHURIKEN).and().namedInput(MSItems.ACE_OF_HEARTS).build(consumer);
		CombinationRecipeBuilder.of(MSItems.SPADES_SUITARANG).input(MSItems.SHURIKEN).and().namedInput(MSItems.ACE_OF_SPADES).build(consumer);
		CombinationRecipeBuilder.of(MSItems.CHAKRAM).input(MSItems.SHURIKEN).or().namedInput(Items.ENDER_EYE).build(consumer);
		CombinationRecipeBuilder.of(MSItems.UMBRAL_INFILTRATOR).input(MSItems.CHAKRAM).or().namedInput(Items.OBSIDIAN).build(consumer);
		CombinationRecipeBuilder.of(MSItems.SORCERERS_PINBALL).input(Items.IRON_BLOCK).or().namedInput(MSBlocks.MINI_WIZARD_STATUE).build(consumer);
		
        CombinationRecipeBuilder.of(MSBlocks.LAPTOP).input(MSBlocks.COMPUTER).and().namedInput(MSItems.BATTERY).build(consumer);
		CombinationRecipeBuilder.of(MSBlocks.LAPTOP).input(MSBlocks.COMPUTER).and().namedInput(Items.BOOK).build(consumer);
		CombinationRecipeBuilder.of(MSBlocks.CROCKERTOP).input(MSBlocks.LAPTOP).and().namedInput(MSItems.CROCKER_SPOON).build(consumer);
		CombinationRecipeBuilder.of(MSBlocks.CROCKERTOP).input(MSBlocks.LAPTOP).and().namedInput(MSItems.CROCKER_FORK).build(consumer);
		CombinationRecipeBuilder.of(MSBlocks.HUBTOP).input(MSBlocks.LAPTOP).and().input(MSBlocks.POWER_HUB).build(consumer);
		CombinationRecipeBuilder.of(MSBlocks.LUNCHTOP).input(MSBlocks.LAPTOP).and().input(Items.APPLE).build(consumer);
		CombinationRecipeBuilder.of(MSBlocks.OLD_COMPUTER).input(MSBlocks.COMPUTER).and().input(Items.CLOCK).build(consumer);
		
		CombinationRecipeBuilder.of(MSBlocks.GRIST_WIDGET).namedInput(MSItems.CROCKER_SPOON).or().input(MSItems.CAPTCHA_CARD).build(consumer);
		CombinationRecipeBuilder.of(MSBlocks.GRIST_WIDGET).namedInput(MSItems.CROCKER_FORK).or().input(MSItems.CAPTCHA_CARD).build(consumer);
		CombinationRecipeBuilder.of(MSBlocks.GRIST_COLLECTOR).input(MSItems.ITEM_MAGNET).or().input(Items.CHEST).build(consumer);
		CombinationRecipeBuilder.of(MSBlocks.ANTHVIL).input(MSBlocks.POWER_HUB).and().input(Items.ANVIL).build(consumer);
		CombinationRecipeBuilder.of(MSBlocks.POWER_HUB).input(MSItems.ENERGY_CORE).and().input(MSItems.BATTERY).build(consumer);
		CombinationRecipeBuilder.of(MSBlocks.TRANSPORTALIZER).input(Items.ENDER_PEARL).and().input(Items.IRON_BLOCK).build(consumer);
		CombinationRecipeBuilder.of(MSItems.CAPTCHAROID_CAMERA).input(MSItems.CAPTCHA_CARD).and().namedInput(MSBlocks.COMPUTER).build(consumer);
		CombinationRecipeBuilder.of(MSItems.CAPTCHAROID_CAMERA).input(MSItems.CAPTCHA_CARD).or().namedInput(Items.ENDER_EYE).build(consumer);
		
		CombinationRecipeBuilder.of(MSItems.QUEUESTACK_MODUS_CARD).input(MSItems.STACK_MODUS_CARD).and().input(MSItems.QUEUE_MODUS_CARD).build(consumer);
		CombinationRecipeBuilder.of(MSItems.TREE_MODUS_CARD).input(MSTags.Items.MODUS_CARD).or().namedInput(Items.STICK).build(consumer);
		CombinationRecipeBuilder.of(MSItems.TREE_MODUS_CARD).input(MSTags.Items.MODUS_CARD).or().namedInput(ItemTags.SAPLINGS).build(consumer);
		CombinationRecipeBuilder.of(MSItems.TREE_MODUS_CARD).input(MSTags.Items.MODUS_CARD).or().namedInput(ItemTags.LEAVES).build(consumer);
		CombinationRecipeBuilder.of(MSItems.HASHMAP_MODUS_CARD).input(MSTags.Items.MODUS_CARD).and().input(MSBlocks.COMPUTER).build(consumer);
		CombinationRecipeBuilder.of(MSItems.SET_MODUS_CARD).input(MSTags.Items.MODUS_CARD).and().input(Items.ITEM_FRAME).build(consumer);
		
		CombinationRecipeBuilder.of(MSItems.IRON_BOAT).input(ItemTags.BOATS).and().namedInput(Items.MINECART).build(consumer);
		CombinationRecipeBuilder.of(MSItems.IRON_BOAT).input(ItemTags.BOATS).and().namedInput(Items.IRON_INGOT).build(consumer);
		CombinationRecipeBuilder.of(MSItems.IRON_BOAT).input(ItemTags.BOATS).and().namedInput(Items.IRON_BLOCK).build(consumer);
		CombinationRecipeBuilder.of(MSItems.GOLD_BOAT).input(ItemTags.BOATS).and().namedInput(Items.GOLD_INGOT).build(consumer);
		CombinationRecipeBuilder.of(MSItems.GOLD_BOAT).input(ItemTags.BOATS).and().namedInput(Items.GOLD_BLOCK).build(consumer);
		
		CombinationRecipeBuilder.of(MSItems.CARDBOARD_TUBE).input(Items.STICK).or().input(Items.PAPER).build(consumer);
		CombinationRecipeBuilder.of(MSBlocks.GOLD_SEEDS).input(Items.WHEAT_SEEDS).and().namedInput(Items.GOLD_NUGGET).build(consumer);
		CombinationRecipeBuilder.of(MSBlocks.GOLD_SEEDS).input(Items.WHEAT_SEEDS).and().namedInput(Items.GOLD_INGOT).build(consumer);
		CombinationRecipeBuilder.of(MSItems.OBSIDIAN_BUCKET).input(Items.WATER_BUCKET).or().input(Items.LAVA_BUCKET).namedSource("buckets").build(consumer);
		CombinationRecipeBuilder.of(MSItems.OBSIDIAN_BUCKET).input(Items.BUCKET).and().namedInput(Items.OBSIDIAN).build(consumer);
		CombinationRecipeBuilder.of(MSItems.STONE_TABLET).input(Items.STONE).or().namedInput(MSItems.CARVING_TOOL).build(consumer);
		CombinationRecipeBuilder.of(MSItems.STONE_TABLET).input(Items.STONE).or().namedInput(Items.WRITABLE_BOOK).build(consumer);
		CombinationRecipeBuilder.of(MSItems.STONE_TABLET).input(Items.STONE).or().namedInput(MSItems.CAPTCHA_CARD).build(consumer);
		CombinationRecipeBuilder.of(MSBlocks.GLOWYSTONE_DUST).input(Items.REDSTONE).or().input(Items.GLOWSTONE_DUST).build(consumer);
		CombinationRecipeBuilder.of(MSItems.COCOA_WART).input(Items.COCOA_BEANS).and().input(Items.NETHER_WART).build(consumer);
		CombinationRecipeBuilder.of(MSBlocks.HORSE_CLOCK.getMainBlock()).input(Items.SADDLE).or().input(MSBlocks.REDSTONE_CLOCK).build(consumer);
		
		CombinationRecipeBuilder.of(MSItems.CASSETTE_13).input(Items.MUSIC_DISC_13).or().namedInput(MSBlocks.CASSETTE_PLAYER).build(consumer);
		CombinationRecipeBuilder.of(MSItems.CASSETTE_CAT).input(Items.MUSIC_DISC_CAT).or().namedInput(MSBlocks.CASSETTE_PLAYER).build(consumer);
		CombinationRecipeBuilder.of(MSItems.CASSETTE_BLOCKS).input(Items.MUSIC_DISC_BLOCKS).or().namedInput(MSBlocks.CASSETTE_PLAYER).build(consumer);
		CombinationRecipeBuilder.of(MSItems.CASSETTE_CHIRP).input(Items.MUSIC_DISC_CHIRP).or().namedInput(MSBlocks.CASSETTE_PLAYER).build(consumer);
		CombinationRecipeBuilder.of(MSItems.CASSETTE_FAR).input(Items.MUSIC_DISC_FAR).or().namedInput(MSBlocks.CASSETTE_PLAYER).build(consumer);
		CombinationRecipeBuilder.of(MSItems.CASSETTE_MALL).input(Items.MUSIC_DISC_MALL).or().namedInput(MSBlocks.CASSETTE_PLAYER).build(consumer);
		CombinationRecipeBuilder.of(MSItems.CASSETTE_MELLOHI).input(Items.MUSIC_DISC_MELLOHI).or().namedInput(MSBlocks.CASSETTE_PLAYER).build(consumer);
		CombinationRecipeBuilder.of(MSItems.CASSETTE_EMISSARY).input(MSItems.MUSIC_DISC_EMISSARY_OF_DANCE).or().namedInput(MSBlocks.CASSETTE_PLAYER).build(consumer);
		CombinationRecipeBuilder.of(MSItems.CASSETTE_DANCE_STAB).input(MSItems.MUSIC_DISC_DANCE_STAB_DANCE).or().namedInput(MSBlocks.CASSETTE_PLAYER).build(consumer);
		CombinationRecipeBuilder.of(MSItems.CASSETTE_RETRO_BATTLE).input(MSItems.MUSIC_DISC_RETRO_BATTLE).or().namedInput(MSBlocks.CASSETTE_PLAYER).build(consumer);
		CombinationRecipeBuilder.of(MSItems.CASSETTE_11).input(Items.MUSIC_DISC_11).or().namedInput(MSBlocks.CASSETTE_PLAYER).build(consumer);
		CombinationRecipeBuilder.of(MSItems.CASSETTE_PIGSTEP).input(Items.MUSIC_DISC_PIGSTEP).or().namedInput(MSBlocks.CASSETTE_PLAYER).build(consumer);
		CombinationRecipeBuilder.of(MSItems.CASSETTE_STAL).input(Items.MUSIC_DISC_STAL).or().namedInput(MSBlocks.CASSETTE_PLAYER).build(consumer);
		CombinationRecipeBuilder.of(MSItems.CASSETTE_STRAD).input(Items.MUSIC_DISC_STRAD).or().namedInput(MSBlocks.CASSETTE_PLAYER).build(consumer);
		CombinationRecipeBuilder.of(MSItems.CASSETTE_WAIT).input(Items.MUSIC_DISC_WAIT).or().namedInput(MSBlocks.CASSETTE_PLAYER).build(consumer);
		CombinationRecipeBuilder.of(MSItems.CASSETTE_WARD).input(Items.MUSIC_DISC_WARD).or().namedInput(MSBlocks.CASSETTE_PLAYER).build(consumer);
		CombinationRecipeBuilder.of(MSItems.CASSETTE_OTHERSIDE).input(Items.MUSIC_DISC_OTHERSIDE).or().namedInput(MSBlocks.CASSETTE_PLAYER).build(consumer);
		CombinationRecipeBuilder.of(MSItems.CASSETTE_5).input(Items.MUSIC_DISC_5).or().namedInput(MSBlocks.CASSETTE_PLAYER).build(consumer);
		
		CombinationRecipeBuilder.of(MSBlocks.BLUE_DIRT).input(Items.DIRT).or().input(Items.BLUE_DYE).build(consumer);
		CombinationRecipeBuilder.of(MSBlocks.THOUGHT_DIRT).input(Items.DIRT).or().input(Items.LIME_DYE).build(consumer);
		CombinationRecipeBuilder.of(MSBlocks.COARSE_END_STONE).input(Items.COARSE_DIRT).or().input(Items.END_STONE).build(consumer);
		CombinationRecipeBuilder.of(MSBlocks.END_GRASS).namedInput(Items.GRASS_BLOCK).or().input(Items.END_STONE).build(consumer);
		CombinationRecipeBuilder.of(MSBlocks.END_GRASS).namedInput(Items.MYCELIUM).or().input(Items.END_STONE).build(consumer);
		CombinationRecipeBuilder.of(Items.GRASS_BLOCK).namedInput(MSBlocks.END_GRASS).and().input(Items.DIRT).buildFor(consumer, Minestuck.MOD_ID);
		CombinationRecipeBuilder.of(MSBlocks.VINE_LOG).input(Items.OAK_LOG).and().input(Items.VINE).build(consumer);
		CombinationRecipeBuilder.of(MSBlocks.FLOWERY_VINE_LOG).input(MSBlocks.VINE_LOG).or().input(ItemTags.SMALL_FLOWERS).build(consumer);
		CombinationRecipeBuilder.of(MSBlocks.GLOWING_MUSHROOM).input(Items.BROWN_MUSHROOM).or().input(Items.GLOWSTONE_DUST).build(consumer);
		CombinationRecipeBuilder.of(MSBlocks.GLOWING_MUSHROOM_VINES).input(MSItems.GLOWING_MUSHROOM_VINES).and().input(Items.VINE).build(consumer);
		CombinationRecipeBuilder.of(MSBlocks.GLOWING_LOG).input(ItemTags.LOGS).or().input(MSBlocks.GLOWING_MUSHROOM).build(consumer);
		CombinationRecipeBuilder.of(MSBlocks.GLOWING_PLANKS).input(ItemTags.PLANKS).or().input(MSBlocks.GLOWING_MUSHROOM).build(consumer);
		CombinationRecipeBuilder.of(MSBlocks.GLOWY_GOOP).input(Items.SLIME_BLOCK).or().input(MSBlocks.GLOWING_MUSHROOM).build(consumer);
		CombinationRecipeBuilder.of(MSBlocks.COAGULATED_BLOOD).input(Items.SLIME_BLOCK).and().input(MSItems.BLOOD_BUCKET).build(consumer);
		CombinationRecipeBuilder.of(MSBlocks.PETRIFIED_POPPY).namedInput(Items.STONE).or().input(Items.POPPY).build(consumer);
		CombinationRecipeBuilder.of(MSBlocks.PETRIFIED_POPPY).namedInput(Items.GRAVEL).or().input(Items.POPPY).build(consumer);
		CombinationRecipeBuilder.of(MSBlocks.PETRIFIED_POPPY).namedInput(Items.COBBLESTONE).or().input(Items.POPPY).build(consumer);
		CombinationRecipeBuilder.of(MSBlocks.PETRIFIED_LOG).namedInput(Items.STONE).or().input(ItemTags.LOGS).build(consumer);
		CombinationRecipeBuilder.of(MSBlocks.PETRIFIED_LOG).namedInput(Items.GRAVEL).or().input(ItemTags.LOGS).build(consumer);
		CombinationRecipeBuilder.of(MSBlocks.PETRIFIED_LOG).namedInput(Items.COBBLESTONE).or().input(ItemTags.LOGS).build(consumer);
		CombinationRecipeBuilder.of(MSBlocks.CINDERED_LOG).input(ItemTags.LOGS).or().namedInput(Items.MAGMA_BLOCK).build(consumer);
		CombinationRecipeBuilder.of(MSBlocks.CINDERED_LOG).input(ItemTags.LOGS).or().namedInput(Items.MAGMA_CREAM).build(consumer);
		CombinationRecipeBuilder.of(MSBlocks.CINDERED_LOG).input(ItemTags.LOGS).or().namedInput(Items.LAVA_BUCKET).build(consumer);
		CombinationRecipeBuilder.of(MSBlocks.CINDERED_PLANKS).input(ItemTags.PLANKS).or().namedInput(Items.MAGMA_BLOCK).build(consumer);
		CombinationRecipeBuilder.of(MSBlocks.CINDERED_PLANKS).input(ItemTags.PLANKS).or().namedInput(Items.MAGMA_CREAM).build(consumer);
		CombinationRecipeBuilder.of(MSBlocks.CINDERED_PLANKS).input(ItemTags.PLANKS).or().namedInput(Items.LAVA_BUCKET).build(consumer);
		CombinationRecipeBuilder.of(MSBlocks.BLOOMING_CACTUS).input(Items.CACTUS).and().input(ItemTags.SMALL_FLOWERS).build(consumer);
		CombinationRecipeBuilder.of(MSBlocks.GLOWFLOWER).input(Items.GLOWSTONE_DUST).and().input(ItemTags.SMALL_FLOWERS).build(consumer);
		CombinationRecipeBuilder.of(MSBlocks.SUGAR_CUBE).input(Items.COOKIE).and().input(Items.REDSTONE_BLOCK).build(consumer);
		CombinationRecipeBuilder.of(MSBlocks.WOODEN_CACTUS).input(Items.CACTUS).or().namedInput(Items.WOODEN_SWORD).build(consumer);
		CombinationRecipeBuilder.of(MSBlocks.WOODEN_CACTUS).input(Items.CACTUS).or().namedInput(ItemTags.PLANKS).build(consumer);
		CombinationRecipeBuilder.of(MSBlocks.WOODEN_CACTUS).input(Items.CACTUS).or().namedInput(ItemTags.LOGS).build(consumer);
		CombinationRecipeBuilder.of(MSBlocks.RAINBOW_SAPLING).input(MSBlocks.RAINBOW_LEAVES).or().input(MSBlocks.RAINBOW_LOG).build(consumer);
		CombinationRecipeBuilder.of(MSBlocks.END_SAPLING).input(MSBlocks.END_LEAVES).or().input(MSBlocks.END_LOG).build(consumer);
		CombinationRecipeBuilder.of(MSBlocks.SHADEWOOD_SAPLING).input(MSBlocks.SHADEWOOD_LEAVES).or().input(MSBlocks.SHADEWOOD_LOG).build(consumer);
		
		CombinationRecipeBuilder.of(MSBlocks.SPIKES).input(Items.CACTUS).and().input(Items.IRON_SWORD).build(consumer);
		
		CombinationRecipeBuilder.of(MSBlocks.COARSE_STONE).input(Items.STONE).and().input(Items.BASALT).build(consumer);
		CombinationRecipeBuilder.of(MSBlocks.CHISELED_COARSE_STONE).input(Items.CHISELED_STONE_BRICKS).and().namedInput(Items.GRAVEL).build(consumer);
		CombinationRecipeBuilder.of(MSBlocks.CHISELED_COARSE_STONE).input(Items.CHISELED_STONE_BRICKS).and().namedInput(MSBlocks.COARSE_STONE).build(consumer);
		CombinationRecipeBuilder.of(MSBlocks.SHADE_STONE).input(Items.STONE).or().namedInput(MSBlocks.BLUE_DIRT).build(consumer);
		CombinationRecipeBuilder.of(MSBlocks.SHADE_STONE).input(Items.STONE).or().namedInput(Items.BLUE_DYE).build(consumer);
		CombinationRecipeBuilder.of(MSBlocks.SHADE_BRICKS).input(Items.STONE_BRICKS).or().namedInput(MSBlocks.BLUE_DIRT).build(consumer);
		CombinationRecipeBuilder.of(MSBlocks.SHADE_BRICKS).input(Items.STONE_BRICKS).or().namedInput(Items.LAPIS_LAZULI).build(consumer);
		CombinationRecipeBuilder.of(MSBlocks.SMOOTH_SHADE_STONE).input(Items.SMOOTH_STONE).or().namedInput(MSBlocks.BLUE_DIRT).build(consumer);
		CombinationRecipeBuilder.of(MSBlocks.SMOOTH_SHADE_STONE).input(Items.SMOOTH_STONE).or().namedInput(Items.LAPIS_LAZULI).build(consumer);
		CombinationRecipeBuilder.of(MSBlocks.SHADE_BRICK_STAIRS).input(Items.STONE_BRICK_STAIRS).or().namedInput(MSBlocks.BLUE_DIRT).build(consumer);
		CombinationRecipeBuilder.of(MSBlocks.SHADE_BRICK_STAIRS).input(Items.STONE_BRICK_STAIRS).or().namedInput(Items.LAPIS_LAZULI).build(consumer);
		CombinationRecipeBuilder.of(MSBlocks.TAR_SHADE_BRICKS).input(MSBlocks.SHADE_BRICKS).and().namedInput(MSItems.OIL_BUCKET).build(consumer);
		CombinationRecipeBuilder.of(MSBlocks.FROST_TILE).input(Tags.Items.STONE).and().namedInput(Items.ICE).build(consumer);
		CombinationRecipeBuilder.of(MSBlocks.FROST_TILE).input(Tags.Items.STONE).and().namedInput(Items.PACKED_ICE).build(consumer);
		CombinationRecipeBuilder.of(MSBlocks.FROST_BRICKS).input(Items.STONE_BRICKS).and().namedInput(Items.ICE).build(consumer);
		CombinationRecipeBuilder.of(MSBlocks.FROST_BRICKS).input(Items.STONE_BRICKS).and().namedInput(Items.PACKED_ICE).build(consumer);
		CombinationRecipeBuilder.of(MSBlocks.CHISELED_FROST_BRICKS).input(Items.CHISELED_STONE_BRICKS).and().namedInput(Items.ICE).build(consumer);
		CombinationRecipeBuilder.of(MSBlocks.CHISELED_FROST_BRICKS).input(Items.CHISELED_STONE_BRICKS).and().namedInput(Items.PACKED_ICE).build(consumer);
		CombinationRecipeBuilder.of(MSBlocks.SUSPICIOUS_CHISELED_MYCELIUM_BRICKS).input(MSBlocks.CHISELED_MYCELIUM_BRICKS).or().namedInput(MSItems.SUSHROOM).build(consumer);
		
		CombinationRecipeBuilder.of(MSBlocks.CAST_IRON).input(Items.IRON_BLOCK).and().input(Items.LAVA_BUCKET).build(consumer);
		CombinationRecipeBuilder.of(MSBlocks.CHISELED_CAST_IRON).input(Items.CHISELED_STONE_BRICKS).or().input(MSBlocks.CAST_IRON).build(consumer);
		CombinationRecipeBuilder.of(MSBlocks.CAST_IRON_TILE).input(MSBlocks.CAST_IRON).and().input(Blocks.COPPER_BLOCK).build(consumer);
		CombinationRecipeBuilder.of(MSBlocks.CAST_IRON_SHEET).input(MSBlocks.CAST_IRON).and().input(ItemTags.PLANKS).build(consumer);
		CombinationRecipeBuilder.of(MSBlocks.CAST_IRON_FRAME).input(MSBlocks.CAST_IRON).or().input(Blocks.SCAFFOLDING).build(consumer);
		
		CombinationRecipeBuilder.of(MSBlocks.STEEL_BEAM).input(Items.QUARTZ_PILLAR).and().input(Blocks.IRON_BLOCK).build(consumer);
		CombinationRecipeBuilder.of(MSBlocks.MYCELIUM_STONE).input(Tags.Items.STONE).and().input(Items.MYCELIUM).build(consumer);
		
		CombinationRecipeBuilder.of(MSBlocks.BLACK_STONE).input(Tags.Items.STONE).and().namedInput(Items.BLACK_DYE).build(consumer);
		CombinationRecipeBuilder.of(MSBlocks.BLACK_STONE).input(Items.STONE).or().namedInput(Items.MAGMA_BLOCK).build(consumer);
		CombinationRecipeBuilder.of(MSBlocks.BLACK_STONE_BRICKS).namedInput(Items.STONE_BRICKS).and().namedInput(Items.BLACK_DYE).build(consumer);
		CombinationRecipeBuilder.of(MSBlocks.BLACK_STONE_BRICKS).namedInput(Items.BRICKS).and().namedInput(MSBlocks.BLACK_STONE).build(consumer);
		CombinationRecipeBuilder.of(MSBlocks.BLACK_SAND).input(Items.SAND).and().namedInput(Items.BLACK_DYE).build(consumer);
		CombinationRecipeBuilder.of(MSBlocks.BLACK_SAND).input(Items.SAND).or().namedInput(Items.MAGMA_BLOCK).build(consumer);
		CombinationRecipeBuilder.of(MSBlocks.IGNEOUS_STONE).input(Items.BLACKSTONE).or().input(MSBlocks.BLACK_STONE).build(consumer);
		CombinationRecipeBuilder.of(MSBlocks.POLISHED_IGNEOUS_BRICKS).input(Items.BRICKS).and().input(MSBlocks.IGNEOUS_STONE).build(consumer);
		CombinationRecipeBuilder.of(MSBlocks.MAGMATIC_IGNEOUS_STONE).input(MSBlocks.IGNEOUS_STONE).or().input(Items.MAGMA_BLOCK).build(consumer);
		CombinationRecipeBuilder.of(MSBlocks.IGNEOUS_SPIKE).input(MSBlocks.IGNEOUS_STONE).or().input(Items.POINTED_DRIPSTONE).build(consumer);
		CombinationRecipeBuilder.of(MSBlocks.PUMICE_STONE).input(Items.SPONGE).or().namedInput(Items.MAGMA_BLOCK).build(consumer);
		CombinationRecipeBuilder.of(MSBlocks.PUMICE_BRICKS).input(Items.BRICKS).or().input(MSBlocks.PUMICE_STONE).build(consumer);
		CombinationRecipeBuilder.of(MSBlocks.PUMICE_TILES).input(Items.POLISHED_ANDESITE).or().input(MSBlocks.PUMICE_STONE).build(consumer);
		CombinationRecipeBuilder.of(MSBlocks.HEAT_LAMP).input(MSItems.MOLTEN_AMBER_BUCKET).or().input(Items.SHROOMLIGHT).build(consumer);
		
		CombinationRecipeBuilder.of(MSBlocks.FLOWERY_MOSSY_COBBLESTONE).input(Items.MOSSY_COBBLESTONE).or().input(ItemTags.SMALL_FLOWERS).build(consumer);
		CombinationRecipeBuilder.of(MSBlocks.FLOWERY_MOSSY_STONE_BRICKS).input(Items.MOSSY_STONE_BRICKS).or().input(ItemTags.SMALL_FLOWERS).build(consumer);
		CombinationRecipeBuilder.of(MSBlocks.CHALK).input(Tags.Items.STONE).or().namedInput(Items.NAUTILUS_SHELL).build(consumer);
		CombinationRecipeBuilder.of(MSBlocks.CHALK).input(Tags.Items.STONE).and().namedInput(Items.WHITE_DYE).build(consumer);
		CombinationRecipeBuilder.of(MSBlocks.PINK_STONE).input(Tags.Items.STONE).and().namedInput(Items.PINK_DYE).build(consumer);
		CombinationRecipeBuilder.of(MSBlocks.BROWN_STONE).input(Tags.Items.STONE).and().namedInput(Items.BROWN_DYE).build(consumer);
		CombinationRecipeBuilder.of(MSBlocks.GREEN_STONE).input(Tags.Items.STONE).and().namedInput(Items.GREEN_DYE).build(consumer);
		CombinationRecipeBuilder.of(MSBlocks.CARVED_LOG).input(ItemTags.LOGS).or().input(Items.HONEY_BOTTLE).build(consumer); //TODO: replace honey bottle with amber resin
		CombinationRecipeBuilder.of(MSBlocks.CARVED_WOODEN_LEAF).input(ItemTags.LEAVES).or().input(Items.BOWL).build(consumer);
		CombinationRecipeBuilder.of(MSBlocks.UNCARVED_WOOD).input(ItemTags.LOGS).and().input(Items.SMOOTH_STONE).build(consumer);
		CombinationRecipeBuilder.of(MSBlocks.CARVED_HEAVY_PLANKS).input(ItemTags.LOGS).and().input(Items.STONE_BRICKS).build(consumer);
		CombinationRecipeBuilder.of(MSBlocks.CARVED_KNOTTED_WOOD).input(ItemTags.LOGS).and().input(Items.CHISELED_STONE_BRICKS).build(consumer);
		CombinationRecipeBuilder.of(MSBlocks.POLISHED_UNCARVED_WOOD).input(ItemTags.LOGS).and().input(Items.POLISHED_ANDESITE).build(consumer);
		CombinationRecipeBuilder.of(MSBlocks.CARVED_PLANKS).input(ItemTags.PLANKS).and().input(Items.STONE).build(consumer);
		CombinationRecipeBuilder.of(MSBlocks.CHIPBOARD).input(ItemTags.LOGS).and().input(Items.COBBLESTONE).build(consumer);
		CombinationRecipeBuilder.of(MSBlocks.WOOD_SHAVINGS).input(ItemTags.LOGS).or().namedInput(Items.STONECUTTER).build(consumer);
		CombinationRecipeBuilder.of(MSBlocks.WOOD_SHAVINGS).input(ItemTags.LOGS).or().namedInput(MSItems.CARVING_TOOL).build(consumer);
		CombinationRecipeBuilder.of(MSBlocks.WOODEN_GRASS).input(ItemTags.PLANKS).or().input(Items.SHORT_GRASS).build(consumer);
		CombinationRecipeBuilder.of(MSItems.CARVED_BUSH).input(MSItems.UNCARVED_WOOD).or().namedInput(ItemTags.SAPLINGS).buildFor(consumer, Minestuck.MOD_ID);
		CombinationRecipeBuilder.of(MSItems.CARVED_BUSH).input(MSItems.UNCARVED_WOOD).or().namedInput(ItemTags.LEAVES).buildFor(consumer, Minestuck.MOD_ID);
		CombinationRecipeBuilder.of(MSBlocks.TREATED_UNCARVED_WOOD).input(MSItems.UNCARVED_WOOD).or().input(MSItems.MOLTEN_AMBER_BUCKET).build(consumer);
		CombinationRecipeBuilder.of(MSBlocks.TREATED_CHIPBOARD).input(MSItems.CHIPBOARD).or().input(MSItems.MOLTEN_AMBER_BUCKET).build(consumer);
		CombinationRecipeBuilder.of(MSBlocks.TREATED_WOOD_SHAVINGS).input(MSItems.WOOD_SHAVINGS).or().input(MSItems.MOLTEN_AMBER_BUCKET).build(consumer);
		CombinationRecipeBuilder.of(MSBlocks.TREATED_HEAVY_PLANKS).input(MSItems.CARVED_HEAVY_PLANKS).or().input(MSItems.MOLTEN_AMBER_BUCKET).build(consumer);
		CombinationRecipeBuilder.of(MSBlocks.TREATED_PLANKS).input(MSItems.CARVED_PLANKS).or().input(MSItems.MOLTEN_AMBER_BUCKET).build(consumer);
		CombinationRecipeBuilder.of(MSBlocks.POLISHED_TREATED_UNCARVED_WOOD).input(MSItems.POLISHED_UNCARVED_WOOD).or().input(MSItems.MOLTEN_AMBER_BUCKET).build(consumer);
		CombinationRecipeBuilder.of(MSBlocks.TREATED_CARVED_KNOTTED_WOOD).input(MSItems.CARVED_KNOTTED_WOOD).or().input(MSItems.MOLTEN_AMBER_BUCKET).build(consumer);
		CombinationRecipeBuilder.of(MSBlocks.TREATED_WOODEN_GRASS).input(MSItems.WOODEN_GRASS).or().input(MSItems.MOLTEN_AMBER_BUCKET).build(consumer);
		CombinationRecipeBuilder.of(MSBlocks.LACQUERED_UNCARVED_WOOD).input(MSItems.TREATED_UNCARVED_WOOD).or().input(Items.HONEYCOMB_BLOCK).build(consumer);
		CombinationRecipeBuilder.of(MSBlocks.LACQUERED_CHIPBOARD).input(MSItems.TREATED_CHIPBOARD).or().input(Items.HONEYCOMB_BLOCK).build(consumer);
		CombinationRecipeBuilder.of(MSBlocks.LACQUERED_WOOD_SHAVINGS).input(MSItems.TREATED_WOOD_SHAVINGS).or().input(Items.HONEYCOMB_BLOCK).build(consumer);
		CombinationRecipeBuilder.of(MSBlocks.LACQUERED_HEAVY_PLANKS).input(MSItems.TREATED_HEAVY_PLANKS).or().input(Items.HONEYCOMB_BLOCK).build(consumer);
		CombinationRecipeBuilder.of(MSBlocks.LACQUERED_PLANKS).input(MSItems.TREATED_PLANKS).or().input(Items.HONEYCOMB_BLOCK).build(consumer);
		CombinationRecipeBuilder.of(MSBlocks.POLISHED_LACQUERED_UNCARVED_WOOD).input(MSItems.POLISHED_TREATED_UNCARVED_WOOD).or().input(Items.HONEYCOMB_BLOCK).build(consumer);
		CombinationRecipeBuilder.of(MSBlocks.LACQUERED_CARVED_KNOTTED_WOOD).input(MSItems.TREATED_CARVED_KNOTTED_WOOD).or().input(Items.HONEYCOMB_BLOCK).build(consumer);
		CombinationRecipeBuilder.of(MSBlocks.LACQUERED_WOODEN_MUSHROOM).input(MSItems.LACQUERED_UNCARVED_WOOD).or().input(Items.BROWN_MUSHROOM).build(consumer);
		CombinationRecipeBuilder.of(MSBlocks.WOODEN_LAMP).input(MSItems.LACQUERED_UNCARVED_WOOD).or().namedInput(Items.OCHRE_FROGLIGHT).build(consumer);
		CombinationRecipeBuilder.of(MSBlocks.WOODEN_LAMP).input(MSItems.LACQUERED_UNCARVED_WOOD).or().namedInput(Items.PEARLESCENT_FROGLIGHT).build(consumer);
		CombinationRecipeBuilder.of(MSBlocks.WOODEN_LAMP).input(MSItems.LACQUERED_UNCARVED_WOOD).or().namedInput(Items.VERDANT_FROGLIGHT).build(consumer);
		
		CombinationRecipeBuilder.of(MSBlocks.FROST_PLANKS).input(ItemTags.PLANKS).or().namedInput(Items.SNOW_BLOCK).build(consumer);
		CombinationRecipeBuilder.of(MSBlocks.FROST_PLANKS).input(ItemTags.PLANKS).or().namedInput(Items.SNOW).build(consumer);
		CombinationRecipeBuilder.of(MSBlocks.FROST_PLANKS).input(ItemTags.PLANKS).or().namedInput(Items.SNOWBALL).build(consumer);
		CombinationRecipeBuilder.of(MSBlocks.FROST_LOG).input(ItemTags.LOGS).or().namedInput(Items.SNOW_BLOCK).build(consumer);
		CombinationRecipeBuilder.of(MSBlocks.FROST_LOG).input(ItemTags.LOGS).or().namedInput(Items.SNOW).build(consumer);
		CombinationRecipeBuilder.of(MSBlocks.FROST_LOG).input(ItemTags.LOGS).or().namedInput(Items.SNOWBALL).build(consumer);
		
		CombinationRecipeBuilder.of(MSItems.CANDY_CORN).input(Items.SUGAR).and().input(Items.WHEAT_SEEDS).build(consumer);
		CombinationRecipeBuilder.of(MSItems.TUIX_BAR).input(Items.COCOA_BEANS).or().input(Items.BRICK).build(consumer);
		CombinationRecipeBuilder.of(MSItems.GOLDEN_GRASSHOPPER).input(MSItems.GRASSHOPPER).or().input(Items.GOLD_INGOT).build(consumer);
		CombinationRecipeBuilder.of(MSItems.BUG_NET).input(Items.STICK).or().namedInput(Items.COBWEB).build(consumer);
		CombinationRecipeBuilder.of(MSItems.BUG_NET).input(Items.STRING).and().namedInput(Items.BUCKET).build(consumer);
		CombinationRecipeBuilder.of(MSBlocks.MIRROR).input(Items.PAINTING).or().input(Items.WATER_BUCKET).build(consumer);
		CombinationRecipeBuilder.of(MSItems.PARTICLE_ACCELERATOR).input(Items.REPEATER).or().input(MSItems.ENERGY_CORE).build(consumer);
		
		CombinationRecipeBuilder.of(MSItems.OIL_BUCKET).input(Items.LAVA_BUCKET).and().input(Items.BONE).build(consumer);
		CombinationRecipeBuilder.of(MSItems.BLOOD_BUCKET).input(Items.WATER_BUCKET).and().input(Items.ROTTEN_FLESH).build(consumer);
		CombinationRecipeBuilder.of(MSItems.BRAIN_JUICE_BUCKET).input(Items.WATER_BUCKET).and().input(Items.SLIME_BALL).build(consumer);
		CombinationRecipeBuilder.of(MSItems.WATER_COLORS_BUCKET).input(Items.WATER_BUCKET).and().input(Tags.Items.DYES).build(consumer);
		CombinationRecipeBuilder.of(MSItems.ENDER_BUCKET).input(Items.LAVA_BUCKET).and().input(Items.ENDER_PEARL).build(consumer);
		CombinationRecipeBuilder.of(MSItems.LIGHT_WATER_BUCKET).input(Items.WATER_BUCKET).and().input(Items.GLOWSTONE_DUST).build(consumer);
		CombinationRecipeBuilder.of(MSItems.CAULK_BUCKET).input(Items.WATER_BUCKET).and().input(Items.AMETHYST_SHARD).build(consumer);
		CombinationRecipeBuilder.of(MSItems.MOLTEN_AMBER_BUCKET).input(Items.LAVA_BUCKET).and().input(Items.HONEY_BOTTLE).build(consumer);
		
		CombinationRecipeBuilder.of(MSItems.UNKNOWABLE_EGG).input(MSItems.SURPRISE_EMBRYO).or().input(MSItems.GRIMOIRE).build(consumer);
		CombinationRecipeBuilder.of(MSItems.LONG_FORGOTTEN_WARHORN).input(Items.NOTE_BLOCK).and().input(MSItems.GRIMOIRE).build(consumer);
		
		CombinationRecipeBuilder.of(MSBlocks.CRUXITE_BLOCK).input(MSItems.RAW_CRUXITE).or().input(Items.STONE).buildFor(consumer, Minestuck.MOD_ID);
		CombinationRecipeBuilder.of(MSBlocks.URANIUM_BLOCK).input(MSItems.RAW_URANIUM).or().input(Items.STONE).buildFor(consumer, Minestuck.MOD_ID);
		
		CombinationRecipeBuilder.of(MSBlocks.PIPE).input(Items.HOPPER).or().namedInput(Items.BAMBOO).buildFor(consumer, Minestuck.MOD_ID);
		CombinationRecipeBuilder.of(MSBlocks.PIPE_INTERSECTION).input(MSBlocks.CHALK).and().input(Items.CHISELED_STONE_BRICKS).buildFor(consumer, Minestuck.MOD_ID);
		CombinationRecipeBuilder.of(MSBlocks.PARCEL_PYXIS).input(MSBlocks.PIPE).or().input(MSItems.MAILBOX).buildFor(consumer, Minestuck.MOD_ID);
		
		CombinationRecipeBuilder.of(MSBlocks.TRAJECTORY_BLOCK).input(Items.POWERED_RAIL).or().input(Items.SLIME_BLOCK).buildFor(consumer, Minestuck.MOD_ID);
		CombinationRecipeBuilder.of(MSBlocks.STAT_STORER).input(MSItems.COMPUTER_PARTS).and().input(MSBlocks.REMOTE_OBSERVER).buildFor(consumer, Minestuck.MOD_ID);
		CombinationRecipeBuilder.of(MSBlocks.REMOTE_OBSERVER).input(Items.OBSERVER).or().input(MSItems.PLUTONIUM_CORE).buildFor(consumer, Minestuck.MOD_ID);
		CombinationRecipeBuilder.of(MSBlocks.WIRELESS_REDSTONE_TRANSMITTER).input(Items.REPEATER).or().input(MSItems.PLUTONIUM_CORE).buildFor(consumer, Minestuck.MOD_ID);
		CombinationRecipeBuilder.of(MSBlocks.WIRELESS_REDSTONE_RECEIVER).input(Items.COMPARATOR).or().input(MSItems.PLUTONIUM_CORE).buildFor(consumer, Minestuck.MOD_ID);
		CombinationRecipeBuilder.of(MSBlocks.SOLID_SWITCH).input(Items.LEVER).and().input(Items.REDSTONE_LAMP).buildFor(consumer, Minestuck.MOD_ID);
		CombinationRecipeBuilder.of(MSBlocks.VARIABLE_SOLID_SWITCH).input(MSBlocks.SOLID_SWITCH).or().input(Items.COMPARATOR).buildFor(consumer, Minestuck.MOD_ID);
		CombinationRecipeBuilder.of(MSBlocks.ONE_SECOND_INTERVAL_TIMED_SOLID_SWITCH).input(MSBlocks.SOLID_SWITCH).or().input(Items.STONE_BUTTON).buildFor(consumer, Minestuck.MOD_ID);
		CombinationRecipeBuilder.of(MSBlocks.TWO_SECOND_INTERVAL_TIMED_SOLID_SWITCH).input(MSBlocks.SOLID_SWITCH).or().input(ItemTags.WOODEN_BUTTONS).buildFor(consumer, Minestuck.MOD_ID);
		CombinationRecipeBuilder.of(MSBlocks.PLATFORM_GENERATOR).input(Items.PISTON).or().input(Items.SHIELD).buildFor(consumer, Minestuck.MOD_ID);
		CombinationRecipeBuilder.of(MSBlocks.PLATFORM_RECEPTACLE).input(Items.DAYLIGHT_DETECTOR).or().input(Items.IRON_BLOCK).buildFor(consumer, Minestuck.MOD_ID);
		CombinationRecipeBuilder.of(MSBlocks.ITEM_MAGNET).input(MSItems.PLUTONIUM_CORE).or().input(Items.FISHING_ROD).buildFor(consumer, Minestuck.MOD_ID);
		CombinationRecipeBuilder.of(MSBlocks.REDSTONE_CLOCK).input(Items.CLOCK).and().input(Items.COMPARATOR).buildFor(consumer, Minestuck.MOD_ID);
		CombinationRecipeBuilder.of(MSBlocks.ROTATOR).input(Items.PISTON).or().input(Items.COMPASS).buildFor(consumer, Minestuck.MOD_ID);
		CombinationRecipeBuilder.of(MSBlocks.TOGGLER).input(Items.PISTON).or().input(Items.LEVER).buildFor(consumer, Minestuck.MOD_ID);
		CombinationRecipeBuilder.of(MSBlocks.REMOTE_COMPARATOR).input(MSBlocks.BLOCK_PRESSURE_PLATE).or().input(MSItems.PLUTONIUM_CORE).buildFor(consumer, Minestuck.MOD_ID);
		CombinationRecipeBuilder.of(MSBlocks.FALL_PAD).input(ItemTags.WOOL).or().input(Items.HAY_BLOCK).buildFor(consumer, Minestuck.MOD_ID);
		CombinationRecipeBuilder.of(MSBlocks.FRAGILE_STONE).input(Tags.Items.STONE).and().input(Items.GRAVEL).buildFor(consumer, Minestuck.MOD_ID);
		CombinationRecipeBuilder.of(MSBlocks.RETRACTABLE_SPIKES).input(MSBlocks.SPIKES).and().input(Items.STICKY_PISTON).buildFor(consumer, Minestuck.MOD_ID);
		CombinationRecipeBuilder.of(MSBlocks.BLOCK_PRESSURE_PLATE).input(Items.OBSERVER).or().input(Items.STONE_PRESSURE_PLATE).buildFor(consumer, Minestuck.MOD_ID);
		CombinationRecipeBuilder.of(MSBlocks.PUSHABLE_BLOCK).input(Tags.Items.STONE).or().input(Items.GRAVEL).buildFor(consumer, Minestuck.MOD_ID);
		CombinationRecipeBuilder.of(MSBlocks.BLOCK_TELEPORTER).input(MSBlocks.SENDIFICATOR).or().input(MSBlocks.BLOCK_PRESSURE_PLATE).buildFor(consumer, Minestuck.MOD_ID);
		
		CombinationRecipeBuilder.of(MSBlocks.STONE_CRUXITE_ORE).input(MSItems.RAW_CRUXITE).and().input(Items.STONE).buildFor(consumer, Minestuck.MOD_ID);
		CombinationRecipeBuilder.of(MSBlocks.NETHERRACK_CRUXITE_ORE).input(MSItems.RAW_CRUXITE).and().input(Items.NETHERRACK).buildFor(consumer, Minestuck.MOD_ID);
		CombinationRecipeBuilder.of(MSBlocks.COBBLESTONE_CRUXITE_ORE).input(MSItems.RAW_CRUXITE).and().input(Items.COBBLESTONE).buildFor(consumer, Minestuck.MOD_ID);
		CombinationRecipeBuilder.of(MSBlocks.SANDSTONE_CRUXITE_ORE).input(MSItems.RAW_CRUXITE).and().input(Items.SANDSTONE).buildFor(consumer, Minestuck.MOD_ID);
		CombinationRecipeBuilder.of(MSBlocks.RED_SANDSTONE_CRUXITE_ORE).input(MSItems.RAW_CRUXITE).and().input(Items.RED_SANDSTONE).buildFor(consumer, Minestuck.MOD_ID);
		CombinationRecipeBuilder.of(MSBlocks.END_STONE_CRUXITE_ORE).input(MSItems.RAW_CRUXITE).and().input(Items.END_STONE).buildFor(consumer, Minestuck.MOD_ID);
		CombinationRecipeBuilder.of(MSBlocks.SHADE_STONE_CRUXITE_ORE).input(MSItems.RAW_CRUXITE).and().input(MSBlocks.SHADE_STONE).buildFor(consumer, Minestuck.MOD_ID);
		CombinationRecipeBuilder.of(MSBlocks.PINK_STONE_CRUXITE_ORE).input(MSItems.RAW_CRUXITE).and().input(MSBlocks.PINK_STONE).buildFor(consumer, Minestuck.MOD_ID);
		CombinationRecipeBuilder.of(MSBlocks.MYCELIUM_STONE_CRUXITE_ORE).input(MSItems.RAW_CRUXITE).and().input(MSBlocks.MYCELIUM_STONE).buildFor(consumer, Minestuck.MOD_ID);
		CombinationRecipeBuilder.of(MSBlocks.UNCARVED_WOOD_CRUXITE_ORE).input(MSItems.RAW_CRUXITE).and().input(MSBlocks.UNCARVED_WOOD).buildFor(consumer, Minestuck.MOD_ID);
		CombinationRecipeBuilder.of(MSBlocks.BLACK_STONE_CRUXITE_ORE).input(MSItems.RAW_CRUXITE).and().input(MSBlocks.BLACK_STONE).buildFor(consumer, Minestuck.MOD_ID);
		
		CombinationRecipeBuilder.of(MSBlocks.STONE_URANIUM_ORE).input(MSItems.RAW_URANIUM).and().input(Items.STONE).buildFor(consumer, Minestuck.MOD_ID);
		CombinationRecipeBuilder.of(MSBlocks.DEEPSLATE_URANIUM_ORE).input(MSItems.RAW_URANIUM).and().input(Items.DEEPSLATE).buildFor(consumer, Minestuck.MOD_ID);
		CombinationRecipeBuilder.of(MSBlocks.NETHERRACK_URANIUM_ORE).input(MSItems.RAW_URANIUM).and().input(Items.NETHERRACK).buildFor(consumer, Minestuck.MOD_ID);
		CombinationRecipeBuilder.of(MSBlocks.COBBLESTONE_URANIUM_ORE).input(MSItems.RAW_URANIUM).and().input(Items.COBBLESTONE).buildFor(consumer, Minestuck.MOD_ID);
		CombinationRecipeBuilder.of(MSBlocks.SANDSTONE_URANIUM_ORE).input(MSItems.RAW_URANIUM).and().input(Items.SANDSTONE).buildFor(consumer, Minestuck.MOD_ID);
		CombinationRecipeBuilder.of(MSBlocks.RED_SANDSTONE_URANIUM_ORE).input(MSItems.RAW_URANIUM).and().input(Items.RED_SANDSTONE).buildFor(consumer, Minestuck.MOD_ID);
		CombinationRecipeBuilder.of(MSBlocks.END_STONE_URANIUM_ORE).input(MSItems.RAW_URANIUM).and().input(Items.END_STONE).buildFor(consumer, Minestuck.MOD_ID);
		CombinationRecipeBuilder.of(MSBlocks.SHADE_STONE_URANIUM_ORE).input(MSItems.RAW_URANIUM).and().input(MSBlocks.SHADE_STONE).buildFor(consumer, Minestuck.MOD_ID);
		CombinationRecipeBuilder.of(MSBlocks.PINK_STONE_URANIUM_ORE).input(MSItems.RAW_URANIUM).and().input(MSBlocks.PINK_STONE).buildFor(consumer, Minestuck.MOD_ID);
		CombinationRecipeBuilder.of(MSBlocks.MYCELIUM_STONE_URANIUM_ORE).input(MSItems.RAW_URANIUM).and().input(MSBlocks.MYCELIUM_STONE).buildFor(consumer, Minestuck.MOD_ID);
		CombinationRecipeBuilder.of(MSBlocks.UNCARVED_WOOD_URANIUM_ORE).input(MSItems.RAW_URANIUM).and().input(MSBlocks.UNCARVED_WOOD).buildFor(consumer, Minestuck.MOD_ID);
		CombinationRecipeBuilder.of(MSBlocks.BLACK_STONE_URANIUM_ORE).input(MSItems.RAW_URANIUM).and().input(MSBlocks.BLACK_STONE).buildFor(consumer, Minestuck.MOD_ID);
		
		CombinationRecipeBuilder.of(MSBlocks.NETHERRACK_COAL_ORE).input(Items.COAL).and().input(Items.NETHERRACK).build(consumer);
		CombinationRecipeBuilder.of(MSBlocks.SHADE_STONE_COAL_ORE).input(Items.COAL).and().input(MSBlocks.SHADE_STONE).build(consumer);
		CombinationRecipeBuilder.of(MSBlocks.PINK_STONE_COAL_ORE).input(Items.COAL).and().input(MSBlocks.PINK_STONE).build(consumer);
		
		CombinationRecipeBuilder.of(MSBlocks.END_STONE_IRON_ORE).input(Items.IRON_INGOT).and().input(Items.END_STONE).build(consumer);
		CombinationRecipeBuilder.of(MSBlocks.SANDSTONE_IRON_ORE).input(Items.IRON_INGOT).and().input(Items.SANDSTONE).build(consumer);
		CombinationRecipeBuilder.of(MSBlocks.RED_SANDSTONE_IRON_ORE).input(Items.IRON_INGOT).and().input(Items.RED_SANDSTONE).build(consumer);
		CombinationRecipeBuilder.of(MSBlocks.UNCARVED_WOOD_IRON_ORE).input(Items.IRON_INGOT).and().input(MSBlocks.UNCARVED_WOOD).build(consumer);
		
		CombinationRecipeBuilder.of(MSBlocks.SANDSTONE_GOLD_ORE).input(Items.GOLD_INGOT).and().input(Items.SANDSTONE).build(consumer);
		CombinationRecipeBuilder.of(MSBlocks.RED_SANDSTONE_GOLD_ORE).input(Items.GOLD_INGOT).and().input(Items.RED_SANDSTONE).build(consumer);
		CombinationRecipeBuilder.of(MSBlocks.SHADE_STONE_GOLD_ORE).input(Items.GOLD_INGOT).and().input(MSBlocks.SHADE_STONE).build(consumer);
		CombinationRecipeBuilder.of(MSBlocks.PINK_STONE_GOLD_ORE).input(Items.GOLD_INGOT).and().input(MSBlocks.PINK_STONE).build(consumer);
		CombinationRecipeBuilder.of(MSBlocks.BLACK_STONE_GOLD_ORE).input(Items.GOLD_INGOT).and().input(MSBlocks.BLACK_STONE).build(consumer);
		
		CombinationRecipeBuilder.of(MSBlocks.END_STONE_REDSTONE_ORE).input(Items.REDSTONE).and().input(Items.END_STONE).build(consumer);
		CombinationRecipeBuilder.of(MSBlocks.UNCARVED_WOOD_REDSTONE_ORE).input(Items.REDSTONE).and().input(MSBlocks.UNCARVED_WOOD).build(consumer);
		CombinationRecipeBuilder.of(MSBlocks.BLACK_STONE_REDSTONE_ORE).input(Items.REDSTONE).and().input(MSBlocks.BLACK_STONE).build(consumer);
		
		CombinationRecipeBuilder.of(MSBlocks.STONE_QUARTZ_ORE).input(Items.QUARTZ).and().input(Items.STONE).build(consumer);
		CombinationRecipeBuilder.of(MSBlocks.BLACK_STONE_QUARTZ_ORE).input(Items.QUARTZ).and().input(MSBlocks.BLACK_STONE).build(consumer);
		
		CombinationRecipeBuilder.of(MSBlocks.PINK_STONE_LAPIS_ORE).input(Items.LAPIS_LAZULI).and().input(MSBlocks.PINK_STONE).build(consumer);
		
		CombinationRecipeBuilder.of(MSBlocks.PINK_STONE_DIAMOND_ORE).input(Items.DIAMOND).and().input(MSBlocks.PINK_STONE).build(consumer);
		
		CombinationRecipeBuilder.of(MSBlocks.UNCARVED_WOOD_EMERALD_ORE).input(Items.EMERALD).and().input(MSBlocks.UNCARVED_WOOD).build(consumer);
		
		CombinationRecipeBuilder.of(MSItems.EMERALD_SWORD).input(Items.DIAMOND_SWORD).or().input(Items.EMERALD).build(consumer);
		CombinationRecipeBuilder.of(MSItems.EMERALD_AXE).input(Items.DIAMOND_AXE).or().input(Items.EMERALD).build(consumer);
		CombinationRecipeBuilder.of(MSItems.EMERALD_PICKAXE).input(Items.DIAMOND_PICKAXE).or().input(Items.EMERALD).build(consumer);
		CombinationRecipeBuilder.of(MSItems.EMERALD_SHOVEL).input(Items.DIAMOND_SHOVEL).or().input(Items.EMERALD).build(consumer);
		CombinationRecipeBuilder.of(MSItems.EMERALD_HOE).input(Items.DIAMOND_HOE).or().input(Items.EMERALD).build(consumer);
		CombinationRecipeBuilder.of(MSItems.MINE_AND_GRIST).input(Items.DIAMOND_PICKAXE).and().input(MSBlocks.GRIST_WIDGET).build(consumer);
		
		Item[] metalHelmets = new Item[] {Items.IRON_HELMET, Items.GOLDEN_HELMET, Items.NETHERITE_HELMET};
		Item[] metalChestplates = new Item[] {Items.IRON_CHESTPLATE, Items.GOLDEN_CHESTPLATE, Items.NETHERITE_CHESTPLATE};
		Item[] metalLeggings = new Item[] {Items.IRON_LEGGINGS, Items.GOLDEN_LEGGINGS, Items.NETHERITE_LEGGINGS};
		Item[] metalBoots = new Item[] {Items.IRON_BOOTS, Items.GOLDEN_BOOTS, Items.NETHERITE_BOOTS};
		for(int i = 0; i < metalHelmets.length; i++)	//Two out of three possible for-loops is enough for me
			for(ItemLike prismarine : new ItemLike[]{Items.PRISMARINE_SHARD, Blocks.PRISMARINE})
			{
				CombinationRecipeBuilder.of(MSItems.PRISMARINE_HELMET).namedInput(metalHelmets[i]).or().namedInput(prismarine).build(consumer);
				CombinationRecipeBuilder.of(MSItems.PRISMARINE_CHESTPLATE).namedInput(metalChestplates[i]).or().namedInput(prismarine).build(consumer);
				CombinationRecipeBuilder.of(MSItems.PRISMARINE_LEGGINGS).namedInput(metalLeggings[i]).or().namedInput(prismarine).build(consumer);
				CombinationRecipeBuilder.of(MSItems.PRISMARINE_BOOTS).namedInput(metalBoots[i]).or().namedInput(prismarine).build(consumer);
			}
		
		CombinationRecipeBuilder.of(MSItems.IRON_LASS_GLASSES).namedInput(Items.IRON_HELMET).and().input(MSItems.PARTICLE_ACCELERATOR).build(consumer);
		CombinationRecipeBuilder.of(MSItems.IRON_LASS_CHESTPLATE).namedInput(Items.IRON_CHESTPLATE).and().input(MSItems.PARTICLE_ACCELERATOR).build(consumer);
		CombinationRecipeBuilder.of(MSItems.IRON_LASS_SKIRT).namedInput(Items.IRON_LEGGINGS).and().input(MSItems.PARTICLE_ACCELERATOR).build(consumer);
		CombinationRecipeBuilder.of(MSItems.IRON_LASS_SHOES).namedInput(Items.IRON_BOOTS).and().input(MSItems.PARTICLE_ACCELERATOR).build(consumer);
		CombinationRecipeBuilder.of(MSItems.PROSPIT_CIRCLET).namedInput(Items.GOLDEN_HELMET).and().input(SkaiaBlocks.WHITE_CROWN_STAINED_GLASS).build(consumer);
		CombinationRecipeBuilder.of(MSItems.PROSPIT_SHIRT).namedInput(Items.LEATHER_CHESTPLATE).and().input(SkaiaBlocks.WHITE_CROWN_STAINED_GLASS).build(consumer);
		CombinationRecipeBuilder.of(MSItems.PROSPIT_PANTS).namedInput(Items.LEATHER_LEGGINGS).and().input(SkaiaBlocks.WHITE_CROWN_STAINED_GLASS).build(consumer);
		CombinationRecipeBuilder.of(MSItems.PROSPIT_SHOES).namedInput(Items.LEATHER_BOOTS).and().input(SkaiaBlocks.WHITE_CROWN_STAINED_GLASS).build(consumer);
		CombinationRecipeBuilder.of(MSItems.DERSE_CIRCLET).namedInput(Items.GOLDEN_HELMET).or().input(SkaiaBlocks.BLACK_CROWN_STAINED_GLASS).build(consumer);
		CombinationRecipeBuilder.of(MSItems.DERSE_SHIRT).namedInput(Items.LEATHER_CHESTPLATE).or().input(SkaiaBlocks.BLACK_CROWN_STAINED_GLASS).build(consumer);
		CombinationRecipeBuilder.of(MSItems.DERSE_PANTS).namedInput(Items.LEATHER_LEGGINGS).or().input(SkaiaBlocks.BLACK_CROWN_STAINED_GLASS).build(consumer);
		CombinationRecipeBuilder.of(MSItems.DERSE_SHOES).namedInput(Items.LEATHER_BOOTS).or().input(SkaiaBlocks.BLACK_CROWN_STAINED_GLASS).build(consumer);
		
		CombinationRecipeBuilder.of(MSItems.AMPHIBEANIE).namedInput(MSItems.CRUMPLY_HAT).and().input(MSItems.FROG).build(consumer);
		CombinationRecipeBuilder.of(MSItems.NOSTRILDAMUS).namedInput(MSItems.TEMPLE_SCANNER).or().input(Items.EMERALD).build(consumer);
		CombinationRecipeBuilder.of(MSItems.PONYTAIL).namedInput(Items.HANGING_ROOTS).or().input(Items.SADDLE).build(consumer);
		
		CombinationRecipeBuilder.of(MSBlocks.PRIMED_TNT).input(Items.TNT).or().input(ItemTags.BUTTONS).build(consumer);
		CombinationRecipeBuilder.of(MSBlocks.UNSTABLE_TNT).input(Items.TNT).or().input(Items.REDSTONE_TORCH).build(consumer);
		CombinationRecipeBuilder.of(MSBlocks.INSTANT_TNT).input(Items.TNT).or().input(Items.REDSTONE).build(consumer);
		CombinationRecipeBuilder.of(MSBlocks.STONE_EXPLOSIVE_BUTTON).namedInput(Items.TNT).and().input(Items.STONE_BUTTON).build(consumer);
		CombinationRecipeBuilder.of(MSBlocks.STONE_EXPLOSIVE_BUTTON).namedInput(MSBlocks.INSTANT_TNT).and().input(Items.STONE_BUTTON).build(consumer);
		CombinationRecipeBuilder.of(MSBlocks.WOODEN_EXPLOSIVE_BUTTON).namedInput(Items.TNT).and().input(ItemTags.WOODEN_BUTTONS).build(consumer);
		CombinationRecipeBuilder.of(MSBlocks.WOODEN_EXPLOSIVE_BUTTON).namedInput(MSBlocks.INSTANT_TNT).and().input(ItemTags.WOODEN_BUTTONS).build(consumer);
		
		CombinationRecipeBuilder.of(MSBlocks.APPLE_CAKE).input(Items.CAKE).or().input(Items.APPLE).build(consumer);
		CombinationRecipeBuilder.of(MSBlocks.BLUE_CAKE).input(Items.CAKE).or().input(MSBlocks.GLOWING_MUSHROOM).build(consumer);
		CombinationRecipeBuilder.of(MSBlocks.COLD_CAKE).input(Items.CAKE).or().namedInput(Items.ICE).build(consumer);
		CombinationRecipeBuilder.of(MSBlocks.COLD_CAKE).input(Items.CAKE).or().namedInput(Items.PACKED_ICE).build(consumer);
		CombinationRecipeBuilder.of(MSBlocks.RED_CAKE).input(Items.CAKE).or().namedInput(Items.MELON_SLICE).build(consumer);
		CombinationRecipeBuilder.of(MSBlocks.RED_CAKE).input(Items.CAKE).or().namedInput(Items.GLISTERING_MELON_SLICE).build(consumer);
		CombinationRecipeBuilder.of(MSBlocks.HOT_CAKE).input(Items.CAKE).or().namedInput(Items.LAVA_BUCKET).build(consumer);
		CombinationRecipeBuilder.of(MSBlocks.HOT_CAKE).input(Items.CAKE).or().namedInput(Items.BLAZE_POWDER).build(consumer);
		CombinationRecipeBuilder.of(MSBlocks.HOT_CAKE).input(Items.CAKE).or().namedInput(Items.MAGMA_BLOCK).build(consumer);
		CombinationRecipeBuilder.of(MSBlocks.REVERSE_CAKE).input(Items.CAKE).or().namedInput(Items.GLASS).build(consumer);
		CombinationRecipeBuilder.of(MSBlocks.REVERSE_CAKE).input(Items.CAKE).or().namedInput(Items.GLASS_PANE).build(consumer);
		CombinationRecipeBuilder.of(MSBlocks.NEGATIVE_CAKE).input(MSBlocks.REVERSE_CAKE).and().input(MSBlocks.FUCHSIA_CAKE).build(consumer);	//Had a drop in creativity, but I guess its fine
		CombinationRecipeBuilder.of(MSBlocks.CARROT_CAKE).input(Items.CAKE).or().input(Items.CARROT).build(consumer);
		CombinationRecipeBuilder.of(MSBlocks.LARGE_CAKE).input(Items.CAKE).or().input(Items.BONE_MEAL).build(consumer);
		CombinationRecipeBuilder.of(MSBlocks.PINK_FROSTED_TOP_LARGE_CAKE).input(MSItems.LARGE_CAKE).and().input(Items.PINK_DYE).build(consumer);
		CombinationRecipeBuilder.of(MSBlocks.CHOCOLATEY_CAKE).input(Items.CAKE).or().input(Items.COCOA_BEANS).build(consumer);
		
		CombinationRecipeBuilder.of(MSItems.ROCK_COOKIE).input(Items.COOKIE).and().namedInput(Items.STONE).build(consumer);
		CombinationRecipeBuilder.of(MSItems.ROCK_COOKIE).input(Items.COOKIE).and().namedInput(Items.COBBLESTONE).build(consumer);
		CombinationRecipeBuilder.of(MSItems.ROCK_COOKIE).input(Items.COOKIE).and().namedInput(Items.GRAVEL).build(consumer);
		CombinationRecipeBuilder.of(MSItems.WOODEN_CARROT).input(Items.CARROT).and().namedInput(ItemTags.PLANKS).build(consumer);
		CombinationRecipeBuilder.of(MSItems.WOODEN_CARROT).input(Items.CARROT).and().namedInput(ItemTags.LOGS).build(consumer);
		CombinationRecipeBuilder.of(MSItems.FUNGAL_SPORE).input(Items.WHEAT_SEEDS).or().input(Tags.Items.MUSHROOMS).build(consumer);
		CombinationRecipeBuilder.of(MSItems.SPOREO).input(Items.COOKIE).and().input(MSItems.FUNGAL_SPORE).build(consumer);
		CombinationRecipeBuilder.of(MSItems.FRENCH_FRY).input(Items.POTATO).and().namedInput(Items.STICK).build(consumer);
		CombinationRecipeBuilder.of(MSItems.FRENCH_FRY).input(Items.POTATO).and().namedInput(Items.BLAZE_ROD).build(consumer);
		CombinationRecipeBuilder.of(MSItems.SURPRISE_EMBRYO).input(Items.EGG).and().input(Items.PUMPKIN).build(consumer);
		CombinationRecipeBuilder.of(MSItems.FOOD_CAN).input(Items.COOKED_BEEF).and().input(Items.IRON_INGOT).build(consumer);
		
		CombinationRecipeBuilder.of(MSItems.INK_SQUID_PRO_QUO).input(MSItems.PAPER_SWORD).or().namedInput(Items.INK_SAC).build(consumer);
		CombinationRecipeBuilder.of(MSItems.EIGHTBALL).input(MSItems.DICE).or().namedInput(Items.WATER_BUCKET).build(consumer);
		
		CombinationRecipeBuilder.of(MSItems.PHLEGM_GUSHERS).input(MSItems.BUILD_GUSHERS).and().namedInput(Items.SLIME_BALL).build(consumer);
		CombinationRecipeBuilder.of(MSItems.SORROW_GUSHERS).input(MSItems.PHLEGM_GUSHERS).and().namedInput(MSItems.INK_SQUID_PRO_QUO).build(consumer);
		
		CombinationRecipeBuilder.of(MSItems.BARBASOL_BOMB).input(MSItems.BUILD_GUSHERS).and().input(MSItems.BARBASOL).build(consumer);
		
		CombinationRecipeBuilder.of(MSItems.SCALEMATE_APPLESCAB).input(Items.RED_WOOL).and().input(Items.DRAGON_BREATH).build(consumer);
		CombinationRecipeBuilder.of(MSItems.SCALEMATE_BERRYBREATH).input(Items.BLUE_WOOL).and().input(Items.DRAGON_BREATH).build(consumer);
		CombinationRecipeBuilder.of(MSItems.SCALEMATE_CINNAMONWHIFF).input(Items.BROWN_WOOL).and().input(Items.DRAGON_BREATH).build(consumer);
		CombinationRecipeBuilder.of(MSItems.SCALEMATE_HONEYTONGUE).input(Items.YELLOW_WOOL).and().input(Items.DRAGON_BREATH).build(consumer);
		CombinationRecipeBuilder.of(MSItems.SCALEMATE_LEMONSNOUT).input(Items.LIME_WOOL).and().input(Items.DRAGON_BREATH).build(consumer);
		CombinationRecipeBuilder.of(MSItems.SCALEMATE_PINESNORT).input(Items.LIGHT_BLUE_WOOL).and().input(Items.DRAGON_BREATH).build(consumer);
		CombinationRecipeBuilder.of(MSItems.SCALEMATE_PUCEFOOT).input(Items.PINK_WOOL).and().input(Items.DRAGON_BREATH).build(consumer);
		CombinationRecipeBuilder.of(MSItems.SCALEMATE_PUMPKINSNUFFLE).input(Items.ORANGE_WOOL).and().input(Items.DRAGON_BREATH).build(consumer);
		CombinationRecipeBuilder.of(MSItems.SCALEMATE_PYRALSPITE).input(Items.WHITE_WOOL).and().input(Items.DRAGON_BREATH).build(consumer);
		CombinationRecipeBuilder.of(MSItems.SCALEMATE_WITNESS).input(Items.GREEN_WOOL).and().input(Items.DRAGON_BREATH).build(consumer);
		
		CombinationRecipeBuilder.of(MSItems.PLUSH_SALAMANDER).input(Items.YELLOW_WOOL).and().input(Items.AXOLOTL_BUCKET).build(consumer);
		CombinationRecipeBuilder.of(MSItems.PLUSH_NAKAGATOR).input(Items.RED_WOOL).and().input(MSItems.NAKAGATOR_STATUE).build(consumer);
		CombinationRecipeBuilder.of(MSItems.PLUSH_TURTLE).input(Items.PINK_WOOL).and().input(Items.SCUTE).build(consumer);
		CombinationRecipeBuilder.of(MSItems.PLUSH_IGUANA).input(Items.BLUE_WOOL).and().input(Items.MANGROVE_ROOTS).build(consumer);
		
		CombinationRecipeBuilder.of(MSItems.ENERGY_CORE).input(MSItems.RAW_CRUXITE).and().input(MSItems.RAW_URANIUM).build(consumer);
		CombinationRecipeBuilder.of(MSItems.IRRADIATED_STEAK).input(MSItems.RAW_URANIUM).or().input(Items.COOKED_BEEF).build(consumer);
		CombinationRecipeBuilder.of(MSItems.QUANTUM_SABRE).input(MSItems.URANIUM_POWERED_STICK).and().input(MSItems.ENERGY_CORE).build(consumer);
		
		CombinationRecipeBuilder.of(MSItems.SOPOR_SLIME_PIE).input(MSItems.BATTERY).or().input(MSItems.HORN).build(consumer);
		
		CombinationRecipeBuilder.of(MSItems.APPLE_JUICE).input(Items.GLASS_BOTTLE).and().input(Items.APPLE).build(consumer);
		CombinationRecipeBuilder.of(MSItems.TAB).input(Items.POTION).or().input(Items.SUGAR).build(consumer);
		CombinationRecipeBuilder.of(MSItems.ORANGE_FAYGO).input(Items.POTION).or().input(Items.ORANGE_DYE).build(consumer);
		CombinationRecipeBuilder.of(MSItems.CANDY_APPLE_FAYGO).input(MSItems.ORANGE_FAYGO).or().input(Items.APPLE).build(consumer);
		CombinationRecipeBuilder.of(MSItems.FAYGO_COLA).input(MSItems.ORANGE_FAYGO).or().input(MSItems.TAB).build(consumer);
		CombinationRecipeBuilder.of(MSItems.COTTON_CANDY_FAYGO).input(MSItems.ORANGE_FAYGO).or().input(Items.LIGHT_BLUE_WOOL).build(consumer);
		CombinationRecipeBuilder.of(MSItems.CREME_SODA_FAYGO).input(MSItems.ORANGE_FAYGO).or().input(Items.MILK_BUCKET).build(consumer);
		CombinationRecipeBuilder.of(MSItems.GRAPE_FAYGO).input(MSItems.ORANGE_FAYGO).or().input(Items.CHORUS_FRUIT).build(consumer);
		CombinationRecipeBuilder.of(MSItems.MOON_MIST_FAYGO).input(MSItems.ORANGE_FAYGO).or().input(Items.LIME_DYE).build(consumer);
		CombinationRecipeBuilder.of(MSItems.PEACH_FAYGO).input(MSItems.CANDY_APPLE_FAYGO).and().input(Items.PINK_DYE).build(consumer);
		CombinationRecipeBuilder.of(MSItems.REDPOP_FAYGO).input(MSItems.ORANGE_FAYGO).or().input(Items.TNT).build(consumer);
		
	}
}
