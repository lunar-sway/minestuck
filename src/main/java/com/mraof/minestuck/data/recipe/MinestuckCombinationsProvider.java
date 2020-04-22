package com.mraof.minestuck.data.recipe;

import com.mraof.minestuck.Minestuck;
import com.mraof.minestuck.block.BlockCollections;
import com.mraof.minestuck.block.MSBlocks;
import com.mraof.minestuck.item.MSItems;
import com.mraof.minestuck.item.crafting.alchemy.CombinationRecipe;
import com.mraof.minestuck.util.MSTags;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.client.Minecraft;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.IFinishedRecipe;
import net.minecraft.data.RecipeProvider;
import net.minecraft.item.DyeColor;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.tags.ItemTags;
import net.minecraft.util.IItemProvider;
import net.minecraftforge.common.Tags;

import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;

public class MinestuckCombinationsProvider extends RecipeProvider
{
	public MinestuckCombinationsProvider(DataGenerator generatorIn)
	{
		super(generatorIn);
	}
	
	@Override
	protected void registerRecipes(Consumer<IFinishedRecipe> consumer)
	{
		//Wood
		final IItemProvider[][] woodItems = {
				{Blocks.OAK_LOG, Blocks.SPRUCE_LOG, Blocks.BIRCH_LOG, Blocks.JUNGLE_LOG, Blocks.ACACIA_LOG, Blocks.DARK_OAK_LOG},
				{Blocks.OAK_PLANKS, Blocks.SPRUCE_PLANKS, Blocks.BIRCH_PLANKS, Blocks.JUNGLE_PLANKS, Blocks.ACACIA_PLANKS, Blocks.DARK_OAK_PLANKS},
				{Blocks.OAK_SLAB, Blocks.SPRUCE_SLAB, Blocks.BIRCH_SLAB, Blocks.JUNGLE_SLAB, Blocks.ACACIA_SLAB, Blocks.DARK_OAK_SLAB},
				{Blocks.OAK_STAIRS, Blocks.SPRUCE_STAIRS, Blocks.BIRCH_STAIRS, Blocks.JUNGLE_STAIRS, Blocks.ACACIA_STAIRS, Blocks.DARK_OAK_STAIRS},
				{Blocks.OAK_SAPLING, Blocks.SPRUCE_SAPLING, Blocks.BIRCH_SAPLING, Blocks.JUNGLE_SAPLING, Blocks.ACACIA_SAPLING, Blocks.DARK_OAK_SAPLING},
				{Blocks.OAK_LEAVES, Blocks.SPRUCE_LEAVES, Blocks.BIRCH_LEAVES, Blocks.JUNGLE_LEAVES,Blocks.ACACIA_LEAVES, Blocks.DARK_OAK_LEAVES},
				{Blocks.OAK_DOOR, Blocks.SPRUCE_DOOR, Blocks.BIRCH_DOOR, Blocks.JUNGLE_DOOR, Blocks.ACACIA_DOOR, Blocks.DARK_OAK_DOOR},
				{Blocks.OAK_FENCE, Blocks.SPRUCE_FENCE, Blocks.BIRCH_FENCE, Blocks.JUNGLE_FENCE, Blocks.ACACIA_FENCE, Blocks.DARK_OAK_FENCE},
				{Blocks.OAK_FENCE_GATE, Blocks.SPRUCE_FENCE_GATE, Blocks.BIRCH_FENCE_GATE, Blocks.JUNGLE_FENCE_GATE, Blocks.ACACIA_FENCE_GATE, Blocks.DARK_OAK_FENCE_GATE},
				{Blocks.OAK_TRAPDOOR, Blocks.SPRUCE_TRAPDOOR, Blocks.BIRCH_TRAPDOOR, Blocks.JUNGLE_TRAPDOOR, Blocks.ACACIA_TRAPDOOR, Blocks.DARK_OAK_TRAPDOOR}};
		//0: oak, 1: spruce, 2: birch, 3: jungle, 4: acacia, 5: dark oak
		// [0] || [1] -> [2]
		int[][] woodCombinations = {{0, 1, 5}, {2, 3, 4}};
		//0: log, 1: planks, 2: slab, 3: stairs, 4: sapling, 5: leaves, 6: door, 7: fence, 8: fence gate, 9: trapdoor
		// [0] || [1] -> [2]
		int[][] itemCombinations = {{1, 2, 3}, {0, 5, 4}, {6, 7, 8}, {6, 2, 9}};
		
		for(IItemProvider[] itemType : woodItems)
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
					CombinationRecipeBuilder.of(blockProvider.apply(color)).input(MSTags.dyeItemTag(color)).or().namedInput(blockProvider.apply(DyeColor.WHITE)).buildFor(consumer, Minestuck.MOD_ID);
			}
		}
		for (DyeColor color : DyeColor.values())
		{
			CombinationRecipeBuilder.of(BlockCollections.coloredGlass(color)).namedInput(Items.GLASS).and().input(MSTags.dyeItemTag(color)).buildFor(consumer, Minestuck.MOD_ID);
			CombinationRecipeBuilder.of(BlockCollections.coloredGlassPane(color)).namedInput(Items.GLASS_PANE).and().input(MSTags.dyeItemTag(color)).buildFor(consumer, Minestuck.MOD_ID);
			CombinationRecipeBuilder.of(BlockCollections.coloredTerracotta(color)).namedInput(Items.TERRACOTTA).and().input(MSTags.dyeItemTag(color)).buildFor(consumer, Minestuck.MOD_ID);
			CombinationRecipeBuilder.of(BlockCollections.coloredConcrete(color)).namedInput(BlockCollections.coloredConcretePowder(color)).or().input(Items.WATER_BUCKET).buildFor(consumer, Minestuck.MOD_ID);
			
			if(color != DyeColor.LIGHT_GRAY)
				CombinationRecipeBuilder.of(BlockCollections.coloredConcretePowder(color)).input(MSTags.dyeItemTag(color)).or().namedInput(BlockCollections.coloredConcretePowder(DyeColor.LIGHT_GRAY)).buildFor(consumer, Minestuck.MOD_ID);
		}
		
		CombinationRecipeBuilder.of(Items.COAL_BLOCK).input(Items.COAL).or().input(Items.STONE).buildFor(consumer, Minestuck.MOD_ID);
		CombinationRecipeBuilder.of(Items.DIAMOND_BLOCK).input(Items.DIAMOND).or().input(Items.STONE).buildFor(consumer, Minestuck.MOD_ID);
		CombinationRecipeBuilder.of(Items.LAPIS_BLOCK).input(Items.LAPIS_LAZULI).or().input(Items.STONE).buildFor(consumer, Minestuck.MOD_ID);
		CombinationRecipeBuilder.of(Items.EMERALD_BLOCK).input(Items.EMERALD).or().input(Items.STONE).buildFor(consumer, Minestuck.MOD_ID);
		CombinationRecipeBuilder.of(Items.GOLD_BLOCK).input(Items.GOLD_INGOT).or().input(Items.STONE).buildFor(consumer, Minestuck.MOD_ID);
		CombinationRecipeBuilder.of(Items.IRON_BLOCK).input(Items.IRON_INGOT).or().input(Items.STONE).buildFor(consumer, Minestuck.MOD_ID);
		CombinationRecipeBuilder.of(Items.QUARTZ_BLOCK).input(Items.QUARTZ).or().input(Items.STONE).buildFor(consumer, Minestuck.MOD_ID);
		CombinationRecipeBuilder.of(Items.REDSTONE_BLOCK).input(Items.REDSTONE).or().input(Items.STONE).buildFor(consumer, Minestuck.MOD_ID);
		
		CombinationRecipeBuilder.of(Items.COAL_ORE).input(Items.COAL).and().input(Items.STONE).buildFor(consumer, Minestuck.MOD_ID);
		CombinationRecipeBuilder.of(Items.DIAMOND_ORE).input(Items.DIAMOND).and().input(Items.STONE).buildFor(consumer, Minestuck.MOD_ID);
		CombinationRecipeBuilder.of(Items.LAPIS_ORE).input(Items.LAPIS_LAZULI).and().input(Items.STONE).buildFor(consumer, Minestuck.MOD_ID);
		CombinationRecipeBuilder.of(Items.EMERALD_ORE).input(Items.EMERALD).and().input(Items.STONE).buildFor(consumer, Minestuck.MOD_ID);
		CombinationRecipeBuilder.of(Items.GOLD_ORE).input(Items.GOLD_INGOT).and().input(Items.STONE).buildFor(consumer, Minestuck.MOD_ID);
		CombinationRecipeBuilder.of(Items.IRON_ORE).input(Items.IRON_INGOT).and().input(Items.STONE).buildFor(consumer, Minestuck.MOD_ID);
		CombinationRecipeBuilder.of(Items.NETHER_QUARTZ_ORE).input(Items.QUARTZ).and().input(Items.STONE).buildFor(consumer, Minestuck.MOD_ID);
		CombinationRecipeBuilder.of(Items.REDSTONE_ORE).input(Items.REDSTONE).and().input(Items.STONE).buildFor(consumer, Minestuck.MOD_ID);
		
		CombinationRecipeBuilder.of(Items.DIAMOND).input(Items.EMERALD).and().namedInput(Items.COAL).buildFor(consumer, Minestuck.MOD_ID);
		CombinationRecipeBuilder.of(Items.DIAMOND).input(Items.EMERALD).and().namedInput(Items.LAPIS_LAZULI).buildFor(consumer, Minestuck.MOD_ID);
		CombinationRecipeBuilder.of(Items.CHARCOAL).input(ItemTags.LOGS).and().input(Items.COAL).buildFor(consumer, Minestuck.MOD_ID);
		
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
		
		CombinationRecipeBuilder.of(Items.MOSSY_COBBLESTONE).input(Items.COBBLESTONE).or().input(Items.WHEAT_SEEDS).buildFor(consumer, Minestuck.MOD_ID);
		CombinationRecipeBuilder.of(Items.MOSSY_COBBLESTONE_WALL).input(Items.COBBLESTONE_WALL).or().namedInput(Items.WHEAT_SEEDS).buildFor(consumer, Minestuck.MOD_ID);
		CombinationRecipeBuilder.of(Items.MOSSY_COBBLESTONE_WALL).input(Items.COBBLESTONE_WALL).or().namedInput(Items.MOSSY_COBBLESTONE).buildFor(consumer, Minestuck.MOD_ID);
		CombinationRecipeBuilder.of(Items.MOSSY_STONE_BRICKS).input(Items.STONE_BRICKS).or().input(Items.WHEAT_SEEDS).buildFor(consumer, Minestuck.MOD_ID);
		
		CombinationRecipeBuilder.of(Items.GRASS_BLOCK).input(Items.DIRT).and().namedInput(Items.GRASS).buildFor(consumer, Minestuck.MOD_ID);
		CombinationRecipeBuilder.of(Items.GRASS_BLOCK).input(Items.DIRT).and().namedInput(Items.WHEAT_SEEDS).buildFor(consumer, Minestuck.MOD_ID);
		CombinationRecipeBuilder.of(Items.MYCELIUM).input(Items.GRASS_BLOCK).and().input(Tags.Items.MUSHROOMS).buildFor(consumer, Minestuck.MOD_ID);
		CombinationRecipeBuilder.of(Items.CACTUS).input(Items.SAND).and().input(Items.GRASS).buildFor(consumer, Minestuck.MOD_ID);
		CombinationRecipeBuilder.of(Items.DEAD_BUSH).input(Items.SAND).or().namedInput(Items.GRASS).buildFor(consumer, Minestuck.MOD_ID);
		CombinationRecipeBuilder.of(Items.DEAD_BUSH).input(Items.SAND).or().namedInput(ItemTags.SAPLINGS).buildFor(consumer, Minestuck.MOD_ID);
		CombinationRecipeBuilder.of(Items.LILY_PAD).input(ItemTags.LEAVES).or().input(Items.WATER_BUCKET).buildFor(consumer, Minestuck.MOD_ID);
		
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
		CombinationRecipeBuilder.of(Items.VINE).input(ItemTags.LEAVES).and().input(Items.LADDER).buildFor(consumer, Minestuck.MOD_ID);
		CombinationRecipeBuilder.of(Items.COMPASS).namedInput(Items.CLOCK).or().input(Items.IRON_INGOT).buildFor(consumer, Minestuck.MOD_ID);
		CombinationRecipeBuilder.of(Items.COMPASS).namedInput(Items.REDSTONE).or().input(Items.IRON_INGOT).buildFor(consumer, Minestuck.MOD_ID);
		CombinationRecipeBuilder.of(Items.CLOCK).namedInput(Items.COMPASS).and().input(Items.GOLD_INGOT).buildFor(consumer, Minestuck.MOD_ID);
		CombinationRecipeBuilder.of(Items.CLOCK).namedInput(Items.REDSTONE).and().input(Items.GOLD_INGOT).buildFor(consumer, Minestuck.MOD_ID);
		CombinationRecipeBuilder.of(Items.REDSTONE_TORCH).input(Items.TORCH).and().input(Items.REDSTONE).buildFor(consumer, Minestuck.MOD_ID);
		CombinationRecipeBuilder.of(Items.GLOWSTONE_DUST).input(Items.TORCH).or().input(Items.REDSTONE).buildFor(consumer, Minestuck.MOD_ID);
		CombinationRecipeBuilder.of(Items.REDSTONE_LAMP).input(Items.GLOWSTONE).and().input(Items.REDSTONE_TORCH).buildFor(consumer, Minestuck.MOD_ID);
		CombinationRecipeBuilder.of(Items.REDSTONE).input(Items.GUNPOWDER).or().namedInput(Items.STONE_BUTTON).buildFor(consumer, Minestuck.MOD_ID);
		CombinationRecipeBuilder.of(Items.REDSTONE).input(Items.GUNPOWDER).or().namedInput(Items.STONE_PRESSURE_PLATE).buildFor(consumer, Minestuck.MOD_ID);
		CombinationRecipeBuilder.of(Items.REDSTONE).input(Items.GUNPOWDER).or().namedInput(Items.LEVER).buildFor(consumer, Minestuck.MOD_ID);
		CombinationRecipeBuilder.of(Items.REDSTONE_TORCH).input(Items.GUNPOWDER).or().namedInput(ItemTags.WOODEN_BUTTONS).buildFor(consumer, Minestuck.MOD_ID);
		CombinationRecipeBuilder.of(Items.REDSTONE_TORCH).input(Items.GUNPOWDER).or().namedInput(ItemTags.WOODEN_PRESSURE_PLATES).buildFor(consumer, Minestuck.MOD_ID);
		CombinationRecipeBuilder.of(Items.REDSTONE).namedInput(Items.GRAVEL).and().input(Items.RED_DYE).buildFor(consumer, Minestuck.MOD_ID);
		
		CombinationRecipeBuilder.of(Items.NETHERRACK).input(Items.COBBLESTONE).and().input(Items.LAVA_BUCKET).buildFor(consumer, Minestuck.MOD_ID);
		CombinationRecipeBuilder.of(Items.SOUL_SAND).input(ItemTags.SAND).or().input(Items.NETHER_WART).buildFor(consumer, Minestuck.MOD_ID);
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
		CombinationRecipeBuilder.of(Items.MAGMA_CREAM).input(Items.SLIME_BALL).and().input(Items.BLAZE_POWDER).buildFor(consumer, Minestuck.MOD_ID);
		CombinationRecipeBuilder.of(Items.BLAZE_ROD).input(Items.STICK).and().namedInput(Items.LAVA_BUCKET).buildFor(consumer, Minestuck.MOD_ID);
		CombinationRecipeBuilder.of(Items.BLAZE_ROD).input(Items.STICK).and().namedInput(Items.BLAZE_POWDER).buildFor(consumer, Minestuck.MOD_ID);
		CombinationRecipeBuilder.of(Items.BLAZE_POWDER).input(Items.REDSTONE).or().namedInput(Items.LAVA_BUCKET).buildFor(consumer, Minestuck.MOD_ID);
		CombinationRecipeBuilder.of(Items.BLAZE_POWDER).input(Items.REDSTONE).or().namedInput(Items.NETHERRACK).buildFor(consumer, Minestuck.MOD_ID);
		
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
		
		CombinationRecipeBuilder.of(Items.END_STONE).input(Items.STONE).and().input(Items.ENDER_PEARL).buildFor(consumer, Minestuck.MOD_ID);
		CombinationRecipeBuilder.of(Items.DRAGON_EGG).input(Items.ENDER_EYE).and().input(Items.EGG).buildFor(consumer, Minestuck.MOD_ID);
		CombinationRecipeBuilder.of(Items.ENDER_EYE).input(Items.ENDER_PEARL).and().input(Items.BLAZE_POWDER).buildFor(consumer, Minestuck.MOD_ID);
		CombinationRecipeBuilder.of(Items.CHORUS_FLOWER).input(ItemTags.SMALL_FLOWERS).and().input(Items.CHORUS_FRUIT).buildFor(consumer, Minestuck.MOD_ID);
		CombinationRecipeBuilder.of(Items.ELYTRA).input(Items.FEATHER).or().input(Items.ENDER_PEARL).buildFor(consumer, Minestuck.MOD_ID);
		
		CombinationRecipeBuilder.of(Items.DIAMOND_HORSE_ARMOR).input(Items.DIAMOND).and().input(Items.SADDLE).buildFor(consumer, Minestuck.MOD_ID);
		CombinationRecipeBuilder.of(Items.GOLDEN_HORSE_ARMOR).input(Items.GOLD_INGOT).and().input(Items.SADDLE).buildFor(consumer, Minestuck.MOD_ID);
		CombinationRecipeBuilder.of(Items.IRON_HORSE_ARMOR).input(Items.IRON_INGOT).and().input(Items.SADDLE).buildFor(consumer, Minestuck.MOD_ID);
		CombinationRecipeBuilder.of(Items.SADDLE).input(Items.STRING).and().input(Items.LEATHER).buildFor(consumer, Minestuck.MOD_ID);
		CombinationRecipeBuilder.of(Items.SHEARS).input(Items.IRON_INGOT).and().input(Items.GRASS).buildFor(consumer, Minestuck.MOD_ID);
		CombinationRecipeBuilder.of(Items.FIRE_CHARGE).input(Items.BLAZE_POWDER).or().input(Items.GUNPOWDER).buildFor(consumer, Minestuck.MOD_ID);
		CombinationRecipeBuilder.of(Items.BOW).input(Items.WOODEN_SWORD).or().input(Items.ARROW).buildFor(consumer, Minestuck.MOD_ID);
		CombinationRecipeBuilder.of(Items.FLOWER_POT).input(ItemTags.SMALL_FLOWERS).and().input(Items.BRICK).buildFor(consumer, Minestuck.MOD_ID);
		CombinationRecipeBuilder.of(Items.EXPERIENCE_BOTTLE).namedInput(Items.POTION).or().input(Items.ENCHANTED_BOOK).buildFor(consumer, Minestuck.MOD_ID);
		CombinationRecipeBuilder.of(Items.EXPERIENCE_BOTTLE).namedInput(Items.GLASS_BOTTLE).or().input(Items.ENCHANTED_BOOK).buildFor(consumer, Minestuck.MOD_ID);
		CombinationRecipeBuilder.of(Items.TRIDENT).namedInput(Items.PRISMARINE_SHARD).and().input(MSItems.FORK).buildFor(consumer, Minestuck.MOD_ID);
		
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
		
		CombinationRecipeBuilder.of(Items.SKELETON_SKULL).input(Items.WITHER_SKELETON_SKULL).and().namedInput(Items.BONE).buildFor(consumer, Minestuck.MOD_ID);
		CombinationRecipeBuilder.of(Items.SKELETON_SKULL).input(Items.WITHER_SKELETON_SKULL).and().namedInput(Items.BONE_MEAL).buildFor(consumer, Minestuck.MOD_ID);
		CombinationRecipeBuilder.of(Items.ZOMBIE_HEAD).input(Items.SKELETON_SKULL).or().input(Items.ROTTEN_FLESH).buildFor(consumer, Minestuck.MOD_ID);
		
		/////////////////	MINESTUCK	\\\\\\\\\\\\\\\\\
		
		CombinationRecipeBuilder.of(MSItems.CACTACEAE_CUTLASS).input(Items.WOODEN_SWORD).and().namedInput(Items.CACTUS).build(consumer);
		CombinationRecipeBuilder.of(MSItems.CACTACEAE_CUTLASS).input(Items.WOODEN_SWORD).and().namedInput(MSBlocks.BLOOMING_CACTUS).build(consumer);
		CombinationRecipeBuilder.of(MSItems.STEAK_SWORD).namedInput(Items.WOODEN_SWORD).or().input(Items.COOKED_BEEF).build(consumer);
		CombinationRecipeBuilder.of(MSItems.STEAK_SWORD).namedInput(Items.STONE_SWORD).or().input(Items.COOKED_BEEF).build(consumer);
		CombinationRecipeBuilder.of(MSItems.BEEF_SWORD).input(Items.WOODEN_SWORD).or().input(Items.BEEF).build(consumer);
		CombinationRecipeBuilder.of(MSItems.IRRADIATED_STEAK_SWORD).input(Items.WOODEN_SWORD).or().input(MSItems.IRRADIATED_STEAK).build(consumer);
		CombinationRecipeBuilder.of(MSItems.SORD).namedInput(Items.WOODEN_SWORD).and().input(MSItems.SBAHJ_POSTER).build(consumer);
		CombinationRecipeBuilder.of(MSItems.SORD).namedInput(Items.STONE_SWORD).and().input(MSItems.SBAHJ_POSTER).build(consumer);
		CombinationRecipeBuilder.of(MSItems.KATANA).namedInput(Items.STONE_SWORD).and().input(Items.ROTTEN_FLESH).build(consumer);
		CombinationRecipeBuilder.of(MSItems.KATANA).namedInput(Items.IRON_SWORD).and().input(Items.ROTTEN_FLESH).build(consumer);
		CombinationRecipeBuilder.of(MSItems.FIRE_POKER).input(Items.IRON_SWORD).and().input(Items.BLAZE_ROD).build(consumer);
		CombinationRecipeBuilder.of(MSItems.TOO_HOT_TO_HANDLE).input(Items.IRON_SWORD).or().input(Items.BLAZE_ROD).build(consumer);
		CombinationRecipeBuilder.of(MSItems.REGISWORD).namedInput(Items.IRON_SWORD).and().input(MSBlocks.CHESSBOARD).build(consumer);
		CombinationRecipeBuilder.of(MSItems.REGISWORD).namedInput(MSItems.KATANA).and().input(MSBlocks.CHESSBOARD).build(consumer);
		CombinationRecipeBuilder.of(MSItems.CLAYMORE).input(Items.IRON_SWORD).and().input(Items.IRON_BLOCK).build(consumer);
		CombinationRecipeBuilder.of(MSItems.UNBREAKABLE_KATANA).input(MSItems.KATANA).and().input(Items.BEDROCK).build(consumer);
		CombinationRecipeBuilder.of(MSItems.COBALT_SABRE).input(MSItems.TOO_HOT_TO_HANDLE).or().input(Items.LAPIS_BLOCK).build(consumer);
		CombinationRecipeBuilder.of(MSItems.SCARLET_RIBBITAR).input(MSItems.CALEDSCRATCH).and().input(MSItems.FROG).build(consumer);
		CombinationRecipeBuilder.of(MSItems.SHATTER_BEACON).input(Items.BEACON).and().input(Items.DIAMOND_SWORD).build(consumer);
		
		CombinationRecipeBuilder.of(MSItems.BATLEACKS).namedInput(Items.WOODEN_AXE).and().input(MSItems.SBAHJ_POSTER).build(consumer);
		CombinationRecipeBuilder.of(MSItems.BATLEACKS).namedInput(Items.STONE_AXE).and().input(MSItems.SBAHJ_POSTER).build(consumer);
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
		CombinationRecipeBuilder.of(MSItems.SURPRISE_AXE).input(MSItems.BLACKSMITH_BANE).and().input(MSItems.SURPRISE_EMBRYO).build(consumer);
		CombinationRecipeBuilder.of(MSItems.SHOCK_AXE).input(MSItems.SURPRISE_AXE).and().input(MSItems.BATTERY).build(consumer);
		CombinationRecipeBuilder.of(MSItems.SCRAXE).input(Items.IRON_AXE).and().input(ItemTags.MUSIC_DISCS).build(consumer);
		CombinationRecipeBuilder.of(MSItems.PISTON_POWERED_POGO_AXEHAMMER).input(MSItems.COPSE_CRUSHER).and().input(MSItems.POGO_HAMMER).build(consumer);
		CombinationRecipeBuilder.of(MSItems.FISSION_FOCUSED_FAULT_FELLER).input(MSItems.PISTON_POWERED_POGO_AXEHAMMER).and().input(MSItems.ENERGY_CORE).build(consumer);
		CombinationRecipeBuilder.of(MSItems.HEPHAESTUS_LUMBERJACK).input(Items.GOLDEN_AXE).and().input(Items.LAVA_BUCKET).build(consumer);
		CombinationRecipeBuilder.of(MSItems.RUBY_CROAK).input(MSItems.EMERALD_AXE).and().input(MSItems.FROG).build(consumer);
		
		CombinationRecipeBuilder.of(MSItems.SICKLE).input(Items.IRON_HOE).and().input(Items.WHEAT).build(consumer);
		CombinationRecipeBuilder.of(MSItems.HOMES_SMELL_YA_LATER).input(MSItems.SICKLE).or().input(MSItems.THRESH_DVD).build(consumer);
		CombinationRecipeBuilder.of(MSItems.FUDGESICKLE).input(MSItems.SICKLE).or().input(Items.COCOA_BEANS).build(consumer);
		CombinationRecipeBuilder.of(MSItems.REGISICKLE).input(MSItems.SICKLE).and().input(MSBlocks.CHESSBOARD).build(consumer);
		CombinationRecipeBuilder.of(MSItems.CANDY_SICKLE).input(MSItems.SICKLE).or().namedInput(MSItems.CANDY_CORN).build(consumer);
		CombinationRecipeBuilder.of(MSItems.CANDY_SICKLE).namedInput(MSItems.FUDGESICKLE).and().input(Items.SUGAR).build(consumer);
		CombinationRecipeBuilder.of(MSItems.CLAW_OF_NRUBYIGLITH).namedInput(MSItems.CAT_CLAWS_DRAWN).and().input(MSItems.GRIMOIRE).build(consumer);
		CombinationRecipeBuilder.of(MSItems.CLAW_OF_NRUBYIGLITH).namedInput(MSItems.CAT_CLAWS_SHEATHED).and().input(MSItems.GRIMOIRE).build(consumer);
		
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
		CombinationRecipeBuilder.of(MSItems.NIGHT_CLUB).input(MSItems.DEUCE_CLUB).and().input(MSItems.CREW_POSTER).build(consumer);
		CombinationRecipeBuilder.of(MSItems.METAL_BAT).input(MSItems.DEUCE_CLUB).and().input(Items.IRON_INGOT).build(consumer);
		CombinationRecipeBuilder.of(MSItems.SPIKED_CLUB).input(MSItems.METAL_BAT).or().input(ItemTags.LOGS).build(consumer);
		
		CombinationRecipeBuilder.of(MSItems.SLEDGE_HAMMER).input(MSItems.CLAW_HAMMER).and().namedInput(Items.BRICKS).build(consumer);
		CombinationRecipeBuilder.of(MSItems.SLEDGE_HAMMER).input(MSItems.CLAW_HAMMER).and().namedInput(Items.COBBLESTONE).build(consumer);
		CombinationRecipeBuilder.of(MSItems.BLACKSMITH_HAMMER).input(MSItems.SLEDGE_HAMMER).and().namedInput(Items.ANVIL).build(consumer);
		CombinationRecipeBuilder.of(MSItems.BLACKSMITH_HAMMER).input(MSItems.SLEDGE_HAMMER).and().namedInput(Items.IRON_BLOCK).build(consumer);
		CombinationRecipeBuilder.of(MSItems.POGO_HAMMER).input(MSItems.SLEDGE_HAMMER).and().input(Items.SLIME_BALL).build(consumer);
		CombinationRecipeBuilder.of(MSItems.REGI_HAMMER).input(MSItems.CLAW_HAMMER).and().input(MSBlocks.CHESSBOARD).build(consumer);
		CombinationRecipeBuilder.of(MSItems.FEAR_NO_ANVIL).input(MSItems.BLACKSMITH_HAMMER).or().input(Items.CLOCK).build(consumer);
		CombinationRecipeBuilder.of(MSItems.TELESCOPIC_SASSACRUSHER).input(MSItems.SLEDGE_HAMMER).and().input(Items.BOOK).build(consumer);
		CombinationRecipeBuilder.of(MSItems.MELT_MASHER).input(MSItems.FEAR_NO_ANVIL).or().input(Items.LAVA_BUCKET).build(consumer);
		CombinationRecipeBuilder.of(MSItems.ESTROGEN_EMPOWERED_EVERYTHING_ERADICATOR).input(MSItems.FISSION_FOCUSED_FAULT_FELLER).or().input(MSItems.GAMEGRL_MAGAZINE).build(consumer);
		CombinationRecipeBuilder.of(MSItems.EEEEEEEEEEEE).input(MSItems.ESTROGEN_EMPOWERED_EVERYTHING_ERADICATOR).and().input(MSItems.SBAHJ_POSTER).build(consumer);
		CombinationRecipeBuilder.of(MSItems.POPAMATIC_VRILLYHOO).input(MSItems.ZILLYHOO_HAMMER).and().input(MSItems.FLUORITE_OCTET).build(consumer);
		CombinationRecipeBuilder.of(MSItems.SCARLET_ZILLYHOO).input(MSItems.ZILLYHOO_HAMMER).and().input(MSItems.FROG).build(consumer);
		
		CombinationRecipeBuilder.of(MSItems.VAUDEVILLE_HOOK).namedInput(MSItems.CANE).and().input(Items.FISHING_ROD).build(consumer);
		CombinationRecipeBuilder.of(MSItems.VAUDEVILLE_HOOK).namedInput(Items.STICK).and().input(Items.FISHING_ROD).build(consumer);
		CombinationRecipeBuilder.of(MSItems.BEAR_POKING_STICK).namedInput(MSItems.CANE).or().input(Items.LEATHER).build(consumer);
		CombinationRecipeBuilder.of(MSItems.BEAR_POKING_STICK).namedInput(Items.STICK).or().input(Items.LEATHER).build(consumer);
		CombinationRecipeBuilder.of(MSItems.UMBRELLA).input(Items.STICK).and().namedInput(Items.BLACK_WOOL).build(consumer);
		CombinationRecipeBuilder.of(MSItems.UMBRELLA).input(Items.STICK).and().namedInput(Items.SHIELD).build(consumer);
		CombinationRecipeBuilder.of(MSItems.UMBRELLA).namedInput(MSItems.CANE).and().namedInput(Items.BLACK_WOOL).build(consumer);
		CombinationRecipeBuilder.of(MSItems.UMBRELLA).namedInput(MSItems.CANE).and().namedInput(Items.SHIELD).build(consumer);
		CombinationRecipeBuilder.of(MSItems.UPPER_CRUST_CRUST_CANE).input(MSItems.PRIM_AND_PROPER_WALKING_POLE).or().namedInput(MSItems.STALE_BAGUETTE).build(consumer);
		CombinationRecipeBuilder.of(MSItems.UPPER_CRUST_CRUST_CANE).input(MSItems.PRIM_AND_PROPER_WALKING_POLE).or().namedInput(Items.BREAD).build(consumer);
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
		
		CombinationRecipeBuilder.of(MSItems.WOODEN_SPOON).input(Items.WOODEN_SHOVEL).and().namedInput(Items.BOWL).build(consumer);
		CombinationRecipeBuilder.of(MSItems.WOODEN_SPOON).input(Items.WOODEN_SHOVEL).and().namedInput(Items.MUSHROOM_STEW).build(consumer);
		CombinationRecipeBuilder.of(MSItems.WOODEN_SPOON).input(Items.WOODEN_SHOVEL).and().namedInput(Items.RABBIT_STEW).build(consumer);
		CombinationRecipeBuilder.of(MSItems.WOODEN_SPOON).input(Items.WOODEN_SHOVEL).and().namedInput(Items.BEETROOT_SOUP).build(consumer);
		CombinationRecipeBuilder.of(MSItems.SILVER_SPOON).input(Items.IRON_SHOVEL).and().namedInput(Items.BOWL).build(consumer);
		CombinationRecipeBuilder.of(MSItems.SILVER_SPOON).input(Items.IRON_SHOVEL).and().namedInput(Items.MUSHROOM_STEW).build(consumer);
		CombinationRecipeBuilder.of(MSItems.SILVER_SPOON).input(Items.IRON_SHOVEL).and().namedInput(Items.RABBIT_STEW).build(consumer);
		CombinationRecipeBuilder.of(MSItems.SILVER_SPOON).input(Items.IRON_SHOVEL).and().namedInput(Items.BEETROOT_SOUP).build(consumer);
		CombinationRecipeBuilder.of(MSItems.SILVER_SPOON).namedInput(MSItems.WOODEN_SPOON).and().input(Items.IRON_INGOT).build(consumer);
		CombinationRecipeBuilder.of(MSItems.CROCKER_SPOON).input(MSItems.SILVER_SPOON).and().input(Items.CAKE).build(consumer);
		CombinationRecipeBuilder.of(MSItems.TUNING_FORK).input(MSItems.FORK).and().input(Items.NOTE_BLOCK).build(consumer);
		CombinationRecipeBuilder.of(MSItems.SKAIA_FORK).input(MSItems.FORK).and().input(MSBlocks.CHESSBOARD).build(consumer);
		CombinationRecipeBuilder.of(MSItems.SPORK).input(MSItems.FORK).or().input(MSItems.WOODEN_SPOON).build(consumer);
		CombinationRecipeBuilder.of(MSItems.GOLDEN_SPORK).input(MSItems.SPORK).or().input(Items.GOLD_INGOT).build(consumer);
		
		CombinationRecipeBuilder.of(MSItems.CAT_CLAWS_SHEATHED).input(Items.IRON_BARS).and().input(Items.LEATHER).build(consumer);
		
		CombinationRecipeBuilder.of(MSBlocks.GRIST_WIDGET).namedInput(MSItems.CROCKER_SPOON).or().input(MSItems.CAPTCHA_CARD).build(consumer);
		CombinationRecipeBuilder.of(MSBlocks.GRIST_WIDGET).namedInput(MSItems.CROCKER_FORK).or().input(MSItems.CAPTCHA_CARD).build(consumer);
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
		
		CombinationRecipeBuilder.of(MSBlocks.GOLD_SEEDS).input(Items.WHEAT_SEEDS).and().namedInput(Items.GOLD_NUGGET).build(consumer);
		CombinationRecipeBuilder.of(MSBlocks.GOLD_SEEDS).input(Items.WHEAT_SEEDS).and().namedInput(Items.GOLD_INGOT).build(consumer);
		CombinationRecipeBuilder.of(MSItems.OBSIDIAN_BUCKET).input(Items.WATER_BUCKET).or().input(Items.LAVA_BUCKET).namedSource("buckets").build(consumer);
		CombinationRecipeBuilder.of(MSItems.OBSIDIAN_BUCKET).input(Items.BUCKET).and().namedInput(Items.OBSIDIAN).build(consumer);
		CombinationRecipeBuilder.of(MSItems.STONE_SLAB).input(Items.STONE).and().input(MSItems.CARVING_TOOL).build(consumer);
		CombinationRecipeBuilder.of(MSBlocks.GLOWYSTONE_DUST).input(Items.REDSTONE).or().input(Items.GLOWSTONE_DUST).build(consumer);
		
		CombinationRecipeBuilder.of(MSBlocks.BLUE_DIRT).input(Items.DIRT).or().input(Items.BLUE_DYE).build(consumer);
		CombinationRecipeBuilder.of(MSBlocks.THOUGHT_DIRT).input(Items.DIRT).or().input(Items.LIME_DYE).build(consumer);
		CombinationRecipeBuilder.of(MSBlocks.COARSE_END_STONE).input(Items.COARSE_DIRT).or().input(Items.END_STONE).build(consumer);
		CombinationRecipeBuilder.of(MSBlocks.END_GRASS).namedInput(Items.GRASS_BLOCK).or().input(Items.END_STONE).build(consumer);
		CombinationRecipeBuilder.of(MSBlocks.END_GRASS).namedInput(Items.MYCELIUM).or().input(Items.END_STONE).build(consumer);
		CombinationRecipeBuilder.of(Items.GRASS_BLOCK).namedInput(MSBlocks.END_GRASS).and().input(Items.DIRT).buildFor(consumer, Minestuck.MOD_ID);
		CombinationRecipeBuilder.of(MSBlocks.VINE_LOG).input(Items.OAK_LOG).and().input(Items.VINE).build(consumer);
		CombinationRecipeBuilder.of(MSBlocks.FLOWERY_VINE_LOG).input(MSBlocks.VINE_LOG).or().input(ItemTags.SMALL_FLOWERS).build(consumer);
		CombinationRecipeBuilder.of(MSBlocks.GLOWING_MUSHROOM).input(Items.BROWN_MUSHROOM).or().input(Items.GLOWSTONE_DUST).build(consumer);
		CombinationRecipeBuilder.of(MSBlocks.GLOWING_LOG).input(ItemTags.LOGS).or().input(MSBlocks.GLOWING_MUSHROOM).build(consumer);
		CombinationRecipeBuilder.of(MSBlocks.GLOWING_PLANKS).input(ItemTags.PLANKS).or().input(MSBlocks.GLOWING_MUSHROOM).build(consumer);
		CombinationRecipeBuilder.of(MSBlocks.GLOWY_GOOP).input(Items.SLIME_BLOCK).or().input(MSBlocks.GLOWING_MUSHROOM).build(consumer);
		CombinationRecipeBuilder.of(MSBlocks.PETRIFIED_POPPY).namedInput(Items.STONE).or().input(Items.POPPY).build(consumer);
		CombinationRecipeBuilder.of(MSBlocks.PETRIFIED_POPPY).namedInput(Items.GRAVEL).or().input(Items.POPPY).build(consumer);
		CombinationRecipeBuilder.of(MSBlocks.PETRIFIED_POPPY).namedInput(Items.COBBLESTONE).or().input(Items.POPPY).build(consumer);
		CombinationRecipeBuilder.of(MSBlocks.PETRIFIED_LOG).namedInput(Items.STONE).or().input(ItemTags.LOGS).build(consumer);
		CombinationRecipeBuilder.of(MSBlocks.PETRIFIED_LOG).namedInput(Items.GRAVEL).or().input(ItemTags.LOGS).build(consumer);
		CombinationRecipeBuilder.of(MSBlocks.PETRIFIED_LOG).namedInput(Items.COBBLESTONE).or().input(ItemTags.LOGS).build(consumer);
		CombinationRecipeBuilder.of(MSBlocks.BLOOMING_CACTUS).input(Items.CACTUS).and().input(ItemTags.SMALL_FLOWERS).build(consumer);
		CombinationRecipeBuilder.of(MSBlocks.SUGAR_CUBE).input(Items.COOKIE).and().input(Items.REDSTONE_BLOCK).build(consumer);
		CombinationRecipeBuilder.of(MSBlocks.WOODEN_CACTUS).input(Items.CACTUS).or().namedInput(Items.WOODEN_SWORD).build(consumer);
		CombinationRecipeBuilder.of(MSBlocks.WOODEN_CACTUS).input(Items.CACTUS).or().namedInput(ItemTags.PLANKS).build(consumer);
		CombinationRecipeBuilder.of(MSBlocks.WOODEN_CACTUS).input(Items.CACTUS).or().namedInput(ItemTags.LOGS).build(consumer);
		CombinationRecipeBuilder.of(MSBlocks.RAINBOW_SAPLING).input(MSBlocks.RAINBOW_LEAVES).or().input(MSBlocks.RAINBOW_LOG).build(consumer);
		CombinationRecipeBuilder.of(MSBlocks.END_SAPLING).input(MSBlocks.END_LEAVES).or().input(MSBlocks.END_LOG).build(consumer);
		
		CombinationRecipeBuilder.of(MSBlocks.COARSE_STONE).input(Items.STONE).and().input(Items.GRAVEL).build(consumer);
		CombinationRecipeBuilder.of(MSBlocks.CHISELED_COARSE_STONE).input(Items.CHISELED_STONE_BRICKS).and().namedInput(Items.GRAVEL).build(consumer);
		CombinationRecipeBuilder.of(MSBlocks.CHISELED_COARSE_STONE).input(Items.CHISELED_STONE_BRICKS).and().namedInput(MSBlocks.COARSE_STONE).build(consumer);
		CombinationRecipeBuilder.of(MSBlocks.SHADE_BRICKS).input(Items.STONE_BRICKS).or().namedInput(MSBlocks.BLUE_DIRT).build(consumer);
		CombinationRecipeBuilder.of(MSBlocks.SHADE_BRICKS).input(Items.STONE_BRICKS).or().namedInput(Items.LAPIS_LAZULI).build(consumer);
		CombinationRecipeBuilder.of(MSBlocks.SHADE_BRICK_STAIRS).input(Items.STONE_BRICK_STAIRS).or().namedInput(MSBlocks.BLUE_DIRT).build(consumer);
		CombinationRecipeBuilder.of(MSBlocks.SHADE_BRICK_STAIRS).input(Items.STONE_BRICK_STAIRS).or().namedInput(Items.LAPIS_LAZULI).build(consumer);
		CombinationRecipeBuilder.of(MSBlocks.SMOOTH_SHADE_STONE).input(Tags.Items.STONE).or().namedInput(MSBlocks.BLUE_DIRT).build(consumer);
		CombinationRecipeBuilder.of(MSBlocks.SMOOTH_SHADE_STONE).input(Tags.Items.STONE).or().namedInput(Items.LAPIS_LAZULI).build(consumer);
		CombinationRecipeBuilder.of(MSBlocks.FROST_TILE).input(Tags.Items.STONE).and().namedInput(Items.ICE).build(consumer);
		CombinationRecipeBuilder.of(MSBlocks.FROST_TILE).input(Tags.Items.STONE).and().namedInput(Items.PACKED_ICE).build(consumer);
		CombinationRecipeBuilder.of(MSBlocks.FROST_BRICKS).input(Items.STONE_BRICKS).and().namedInput(Items.ICE).build(consumer);
		CombinationRecipeBuilder.of(MSBlocks.FROST_BRICKS).input(Items.STONE_BRICKS).and().namedInput(Items.PACKED_ICE).build(consumer);
		CombinationRecipeBuilder.of(MSBlocks.CHISELED_FROST_BRICKS).input(Items.CHISELED_STONE_BRICKS).and().namedInput(Items.ICE).build(consumer);
		CombinationRecipeBuilder.of(MSBlocks.CHISELED_FROST_BRICKS).input(Items.CHISELED_STONE_BRICKS).and().namedInput(Items.PACKED_ICE).build(consumer);
		CombinationRecipeBuilder.of(MSBlocks.CAST_IRON).input(Items.IRON_BLOCK).and().input(Items.LAVA_BUCKET).build(consumer);
		CombinationRecipeBuilder.of(MSBlocks.CHISELED_CAST_IRON).input(Items.CHISELED_STONE_BRICKS).or().input(MSBlocks.CAST_IRON).build(consumer);
		CombinationRecipeBuilder.of(MSBlocks.FLOWERY_MOSSY_COBBLESTONE).input(Items.MOSSY_COBBLESTONE).or().input(ItemTags.SMALL_FLOWERS).build(consumer);
		CombinationRecipeBuilder.of(MSBlocks.FLOWERY_MOSSY_STONE_BRICKS).input(Items.MOSSY_STONE_BRICKS).or().input(ItemTags.SMALL_FLOWERS).build(consumer);
		CombinationRecipeBuilder.of(MSBlocks.TREATED_PLANKS).input(ItemTags.PLANKS).or().input(Items.NETHERRACK).build(consumer);
		CombinationRecipeBuilder.of(MSBlocks.FROST_PLANKS).input(ItemTags.PLANKS).or().namedInput(Items.SNOW_BLOCK).build(consumer);
		CombinationRecipeBuilder.of(MSBlocks.FROST_PLANKS).input(ItemTags.PLANKS).or().namedInput(Items.SNOW).build(consumer);
		CombinationRecipeBuilder.of(MSBlocks.FROST_PLANKS).input(ItemTags.PLANKS).or().namedInput(Items.SNOWBALL).build(consumer);
		CombinationRecipeBuilder.of(MSBlocks.FROST_LOG).input(ItemTags.LOGS).or().namedInput(Items.SNOW_BLOCK).build(consumer);
		CombinationRecipeBuilder.of(MSBlocks.FROST_LOG).input(ItemTags.LOGS).or().namedInput(Items.SNOW).build(consumer);
		CombinationRecipeBuilder.of(MSBlocks.FROST_LOG).input(ItemTags.LOGS).or().namedInput(Items.SNOWBALL).build(consumer);
		
		CombinationRecipeBuilder.of(MSItems.CANDY_CORN).input(Items.SUGAR).and().input(Items.WHEAT_SEEDS).build(consumer);
		CombinationRecipeBuilder.of(MSItems.GOLDEN_GRASSHOPPER).input(MSItems.GRASSHOPPER).or().input(Items.GOLD_INGOT).build(consumer);
		CombinationRecipeBuilder.of(MSItems.BUG_NET).input(Items.STICK).or().namedInput(Items.COBWEB).build(consumer);
		CombinationRecipeBuilder.of(MSItems.BUG_NET).input(Items.STRING).and().namedInput(Items.BUCKET).build(consumer);
		
		CombinationRecipeBuilder.of(MSItems.UNKNOWABLE_EGG).input(MSItems.SURPRISE_EMBRYO).or().input(MSItems.GRIMOIRE).build(consumer);
		CombinationRecipeBuilder.of(MSItems.LONG_FORGOTTEN_WARHORN).input(Items.NOTE_BLOCK).and().input(MSItems.GRIMOIRE).build(consumer);
		
		CombinationRecipeBuilder.of(MSBlocks.NETHERRACK_COAL_ORE).input(Items.COAL).and().input(Items.NETHERRACK).build(consumer);
		CombinationRecipeBuilder.of(MSBlocks.END_STONE_IRON_ORE).input(Items.IRON_INGOT).and().input(Items.END_STONE).build(consumer);
		CombinationRecipeBuilder.of(MSBlocks.SANDSTONE_IRON_ORE).input(Items.IRON_INGOT).and().input(Items.SANDSTONE).build(consumer);
		CombinationRecipeBuilder.of(MSBlocks.RED_SANDSTONE_IRON_ORE).input(Items.IRON_INGOT).and().input(Items.RED_SANDSTONE).build(consumer);
		CombinationRecipeBuilder.of(MSBlocks.SANDSTONE_GOLD_ORE).input(Items.GOLD_INGOT).and().input(Items.SANDSTONE).build(consumer);
		CombinationRecipeBuilder.of(MSBlocks.RED_SANDSTONE_GOLD_ORE).input(Items.GOLD_INGOT).and().input(Items.RED_SANDSTONE).build(consumer);
		CombinationRecipeBuilder.of(MSBlocks.END_STONE_REDSTONE_ORE).input(Items.REDSTONE).and().input(Items.END_STONE).build(consumer);
		
		CombinationRecipeBuilder.of(MSItems.EMERALD_SWORD).input(Items.DIAMOND_SWORD).or().input(Items.EMERALD).build(consumer);
		CombinationRecipeBuilder.of(MSItems.EMERALD_AXE).input(Items.DIAMOND_AXE).or().input(Items.EMERALD).build(consumer);
		CombinationRecipeBuilder.of(MSItems.EMERALD_PICKAXE).input(Items.DIAMOND_PICKAXE).or().input(Items.EMERALD).build(consumer);
		CombinationRecipeBuilder.of(MSItems.EMERALD_SHOVEL).input(Items.DIAMOND_SHOVEL).or().input(Items.EMERALD).build(consumer);
		CombinationRecipeBuilder.of(MSItems.EMERALD_HOE).input(Items.DIAMOND_HOE).or().input(Items.EMERALD).build(consumer);
		
		Item[] metalHelmets = new Item[] {Items.IRON_HELMET, Items.GOLDEN_HELMET, Items.DIAMOND_HELMET};
		Item[] metalChestplates = new Item[] {Items.IRON_CHESTPLATE, Items.GOLDEN_CHESTPLATE, Items.DIAMOND_CHESTPLATE};
		Item[] metalLeggings = new Item[] {Items.IRON_LEGGINGS, Items.GOLDEN_LEGGINGS, Items.DIAMOND_LEGGINGS};
		Item[] metalBoots = new Item[] {Items.IRON_BOOTS, Items.GOLDEN_BOOTS, Items.DIAMOND_BOOTS};
		for(int i = 0; i < metalHelmets.length; i++)	//Two out of three possible for-loops is enough for me
			for(IItemProvider prismarine : new IItemProvider[]{Items.PRISMARINE_SHARD, Blocks.PRISMARINE})
			{
				CombinationRecipeBuilder.of(MSItems.PRISMARINE_HELMET).namedInput(metalHelmets[i]).or().namedInput(prismarine).build(consumer);
				CombinationRecipeBuilder.of(MSItems.PRISMARINE_CHESTPLATE).namedInput(metalChestplates[i]).or().namedInput(prismarine).build(consumer);
				CombinationRecipeBuilder.of(MSItems.PRISMARINE_LEGGINGS).namedInput(metalLeggings[i]).or().namedInput(prismarine).build(consumer);
				CombinationRecipeBuilder.of(MSItems.PRISMARINE_BOOTS).namedInput(metalBoots[i]).or().namedInput(prismarine).build(consumer);
			}
		
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

		CombinationRecipeBuilder.of(MSItems.ENERGY_CORE).input(MSItems.RAW_CRUXITE).and().input(MSItems.RAW_URANIUM).build(consumer);
		CombinationRecipeBuilder.of(MSItems.IRRADIATED_STEAK).input(MSItems.RAW_URANIUM).or().input(Items.COOKED_BEEF).build(consumer);
		CombinationRecipeBuilder.of(MSItems.QUANTUM_SABRE).input(MSItems.URANIUM_POWERED_STICK).and().input(MSItems.ENERGY_CORE).build(consumer);
		
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
	
	@Override
	public String getName()
	{
		return "Minestuck combination recipes";
	}
}