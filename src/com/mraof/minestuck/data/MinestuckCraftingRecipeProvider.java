package com.mraof.minestuck.data;

import com.mraof.minestuck.Minestuck;
import com.mraof.minestuck.block.MinestuckBlocks;
import com.mraof.minestuck.item.MinestuckItems;
import com.mraof.minestuck.util.MinestuckTags;
import net.minecraft.advancements.criterion.InventoryChangeTrigger;
import net.minecraft.advancements.criterion.MinMaxBounds;
import net.minecraft.block.Blocks;
import net.minecraft.data.*;
import net.minecraft.item.Items;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.tags.ItemTags;
import net.minecraft.util.IItemProvider;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.Tags;
import net.minecraftforge.common.crafting.IngredientNBT;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.Objects;
import java.util.function.Consumer;

public class MinestuckCraftingRecipeProvider extends RecipeProvider
{
	MinestuckCraftingRecipeProvider(DataGenerator generator)
	{
		super(generator);
	}
	
	@Override
	protected void registerRecipes(Consumer<IFinishedRecipe> recipeBuilder)
	{
		ShapelessRecipeBuilder.shapelessRecipe(MinestuckItems.RAW_CRUXITE, 9).addIngredient(MinestuckBlocks.CRUXITE_BLOCK).addCriterion("has_at_least_9_raw_cruxite", hasItem(MinMaxBounds.IntBound.atLeast(9), MinestuckItems.RAW_CRUXITE)).addCriterion("has_cruxite_block", hasItem(MinestuckBlocks.CRUXITE_BLOCK)).build(recipeBuilder, new ResourceLocation(Minestuck.MOD_ID, "raw_cruxite_from_block"));
		ShapedRecipeBuilder.shapedRecipe(MinestuckBlocks.CRUXITE_BLOCK).key('#', MinestuckItems.RAW_CRUXITE).patternLine("###").patternLine("###").patternLine("###").addCriterion("has_at_least_9_raw_cruxite", hasItem(MinMaxBounds.IntBound.atLeast(9), MinestuckItems.RAW_CRUXITE)).build(recipeBuilder);
		ShapelessRecipeBuilder.shapelessRecipe(MinestuckItems.RAW_URANIUM, 9).addIngredient(MinestuckBlocks.URANIUM_BLOCK).addCriterion("has_at_least_9_raw_uranium", hasItem(MinMaxBounds.IntBound.atLeast(9), MinestuckItems.RAW_URANIUM)).addCriterion("has_uranium_block", hasItem(MinestuckBlocks.URANIUM_BLOCK)).build(recipeBuilder, new ResourceLocation(Minestuck.MOD_ID, "raw_uranium_from_block"));
		ShapedRecipeBuilder.shapedRecipe(MinestuckBlocks.URANIUM_BLOCK).key('#', MinestuckItems.RAW_URANIUM).patternLine("###").patternLine("###").patternLine("###").addCriterion("has_at_least_9_raw_uranium", hasItem(MinMaxBounds.IntBound.atLeast(9), MinestuckItems.RAW_URANIUM)).build(recipeBuilder);
		ShapelessRecipeBuilder.shapelessRecipe(Items.SUGAR, 9).addIngredient(MinestuckBlocks.SUGAR_CUBE).addCriterion("has_at_least_9_sugar", hasItem(MinMaxBounds.IntBound.atLeast(9), Items.SUGAR)).addCriterion("has_sugar_cube", hasItem(MinestuckBlocks.SUGAR_CUBE)).build(recipeBuilder, new ResourceLocation(Minestuck.MOD_ID, "sugar_from_cube"));
		ShapedRecipeBuilder.shapedRecipe(MinestuckBlocks.SUGAR_CUBE).key('#', Items.SUGAR).patternLine("###").patternLine("###").patternLine("###").addCriterion("has_at_least_9_sugar", hasItem(MinMaxBounds.IntBound.atLeast(9), Items.SUGAR)).build(recipeBuilder);
		ShapelessRecipeBuilder.shapelessRecipe(MinestuckItems.STRAWBERRY_CHUNK, 4).addIngredient(MinestuckBlocks.STRAWBERRY).addCriterion("has_at_least_4_strawberry_chunks", hasItem(MinMaxBounds.IntBound.atLeast(4), MinestuckItems.STRAWBERRY_CHUNK)).addCriterion("has_strawberry_block", hasItem(MinestuckBlocks.STRAWBERRY)).build(recipeBuilder);
		ShapedRecipeBuilder.shapedRecipe(MinestuckBlocks.STRAWBERRY).key('#', MinestuckItems.STRAWBERRY_CHUNK).patternLine("##").patternLine("##").addCriterion("has_at_least_4_strawberry_chunks", hasItem(MinMaxBounds.IntBound.atLeast(4), MinestuckItems.STRAWBERRY_CHUNK)).build(recipeBuilder);
		
		ShapedRecipeBuilder.shapedRecipe(MinestuckBlocks.SHADE_BRICKS, 4).key('#', MinestuckBlocks.SMOOTH_SHADE_STONE).patternLine("##").patternLine("##").addCriterion("has_shade_stone", hasItem(MinestuckBlocks.SMOOTH_SHADE_STONE)).build(recipeBuilder);
		ShapedRecipeBuilder.shapedRecipe(MinestuckBlocks.FROST_BRICKS, 4).key('#', MinestuckBlocks.FROST_TILE).patternLine("##").patternLine("##").addCriterion("has_frost_tile", hasItem(MinestuckBlocks.FROST_TILE)).build(recipeBuilder);
		ShapedRecipeBuilder.shapedRecipe(MinestuckBlocks.POLISHED_CHALK, 4).key('#', MinestuckBlocks.CHALK).patternLine("##").patternLine("##").addCriterion("has_chalk", hasItem(MinestuckBlocks.CHALK)).build(recipeBuilder);
		ShapedRecipeBuilder.shapedRecipe(MinestuckBlocks.CHALK_BRICKS, 4).key('#', MinestuckBlocks.POLISHED_CHALK).patternLine("##").patternLine("##").addCriterion("has_polished_chalk", hasItem(MinestuckBlocks.POLISHED_CHALK)).build(recipeBuilder);
		ShapedRecipeBuilder.shapedRecipe(MinestuckBlocks.CHISELED_CHALK_BRICKS, 4).key('#', MinestuckBlocks.CHALK_BRICKS).patternLine("##").patternLine("##").addCriterion("has_chalk_bricks", hasItem(MinestuckBlocks.CHALK_BRICKS)).build(recipeBuilder);
		ShapedRecipeBuilder.shapedRecipe(MinestuckBlocks.POLISHED_PINK_STONE, 4).key('#', MinestuckBlocks.PINK_STONE).patternLine("##").patternLine("##").addCriterion("has_pink_stone", hasItem(MinestuckBlocks.PINK_STONE)).build(recipeBuilder);
		ShapedRecipeBuilder.shapedRecipe(MinestuckBlocks.PINK_STONE_BRICKS, 4).key('#', MinestuckBlocks.POLISHED_PINK_STONE).patternLine("##").patternLine("##").addCriterion("has_polished_pink_stone", hasItem(MinestuckBlocks.POLISHED_PINK_STONE)).build(recipeBuilder);
		ShapedRecipeBuilder.shapedRecipe(MinestuckBlocks.CHISELED_PINK_STONE_BRICKS, 4).key('#', MinestuckBlocks.PINK_STONE_BRICKS).patternLine("##").patternLine("##").addCriterion("has_pink_stone_bricks", hasItem(MinestuckBlocks.PINK_STONE_BRICKS)).build(recipeBuilder);
		ShapedRecipeBuilder.shapedRecipe(MinestuckBlocks.COARSE_STONE_STAIRS, 4).key('#', MinestuckBlocks.COARSE_STONE).patternLine("#  ").patternLine("## ").patternLine("###").addCriterion("has_coarse_stone", hasItem(MinestuckBlocks.COARSE_STONE)).build(recipeBuilder);
		ShapedRecipeBuilder.shapedRecipe(MinestuckBlocks.SHADE_BRICK_STAIRS, 4).key('#', MinestuckBlocks.SHADE_BRICKS).patternLine("#  ").patternLine("## ").patternLine("###").addCriterion("has_shade_bricks", hasItem(MinestuckBlocks.SHADE_BRICKS)).build(recipeBuilder);
		ShapedRecipeBuilder.shapedRecipe(MinestuckBlocks.FROST_BRICK_STAIRS, 4).key('#', MinestuckBlocks.FROST_BRICKS).patternLine("#  ").patternLine("## ").patternLine("###").addCriterion("has_frost_bricks", hasItem(MinestuckBlocks.FROST_BRICKS)).build(recipeBuilder);
		ShapedRecipeBuilder.shapedRecipe(MinestuckBlocks.CAST_IRON_STAIRS, 4).key('#', MinestuckBlocks.CAST_IRON).patternLine("#  ").patternLine("## ").patternLine("###").addCriterion("has_cast_iron", hasItem(MinestuckBlocks.CAST_IRON)).build(recipeBuilder);
		ShapedRecipeBuilder.shapedRecipe(MinestuckBlocks.MYCELIUM_BRICK_STAIRS, 4).key('#', MinestuckBlocks.MYCELIUM_BRICKS).patternLine("#  ").patternLine("## ").patternLine("###").addCriterion("has_mycelium_bricks", hasItem(MinestuckBlocks.MYCELIUM_BRICKS)).build(recipeBuilder);
		ShapedRecipeBuilder.shapedRecipe(MinestuckBlocks.CHALK_STAIRS, 4).key('#', MinestuckBlocks.CHALK).patternLine("#  ").patternLine("## ").patternLine("###").addCriterion("has_chalk", hasItem(MinestuckBlocks.CHALK)).build(recipeBuilder);
		ShapedRecipeBuilder.shapedRecipe(MinestuckBlocks.CHALK_BRICK_STAIRS, 4).key('#', MinestuckBlocks.CHALK_BRICKS).patternLine("#  ").patternLine("## ").patternLine("###").addCriterion("has_chalk_bricks", hasItem(MinestuckBlocks.CHALK_BRICKS)).build(recipeBuilder);
		ShapedRecipeBuilder.shapedRecipe(MinestuckBlocks.PINK_STONE_BRICK_STAIRS, 4).key('#', MinestuckBlocks.PINK_STONE_BRICKS).patternLine("#  ").patternLine("## ").patternLine("###").addCriterion("has_pink_stone_bricks", hasItem(MinestuckBlocks.PINK_STONE_BRICKS)).build(recipeBuilder);
		ShapedRecipeBuilder.shapedRecipe(MinestuckBlocks.CHALK_SLAB, 6).key('#', MinestuckBlocks.CHALK).patternLine("###").addCriterion("has_chalk", hasItem(MinestuckBlocks.CHALK)).build(recipeBuilder);
		ShapedRecipeBuilder.shapedRecipe(MinestuckBlocks.CHALK_BRICK_SLAB, 6).key('#', MinestuckBlocks.CHALK_BRICKS).patternLine("###").addCriterion("has_chalk_bricks", hasItem(MinestuckBlocks.CHALK_BRICKS)).build(recipeBuilder);
		ShapedRecipeBuilder.shapedRecipe(MinestuckBlocks.PINK_STONE_BRICK_SLAB, 6).key('#', MinestuckBlocks.PINK_STONE_BRICKS).patternLine("###").addCriterion("has_pink_stone_bricks", hasItem(MinestuckBlocks.PINK_STONE_BRICKS)).build(recipeBuilder);
		
		ShapedRecipeBuilder.shapedRecipe(MinestuckBlocks.GLOWING_WOOD, 3).key('#', MinestuckBlocks.GLOWING_LOG).patternLine("##").patternLine("##").setGroup("bark").addCriterion("has_log", hasItem(MinestuckTags.Items.GLOWING_LOGS)).build(recipeBuilder);
		ShapelessRecipeBuilder.shapelessRecipe(MinestuckBlocks.GLOWING_PLANKS, 4).addIngredient(MinestuckTags.Items.GLOWING_LOGS).setGroup("planks").addCriterion("has_log", hasItem(MinestuckTags.Items.GLOWING_LOGS)).build(recipeBuilder);
		ShapedRecipeBuilder.shapedRecipe(MinestuckBlocks.FROST_WOOD, 3).key('#', MinestuckBlocks.FROST_LOG).patternLine("##").patternLine("##").setGroup("bark").addCriterion("has_log", hasItem(MinestuckTags.Items.FROST_LOGS)).build(recipeBuilder);
		ShapelessRecipeBuilder.shapelessRecipe(MinestuckBlocks.FROST_PLANKS, 4).addIngredient(MinestuckTags.Items.FROST_LOGS).setGroup("planks").addCriterion("has_log", hasItem(MinestuckTags.Items.FROST_LOGS)).build(recipeBuilder);
		ShapedRecipeBuilder.shapedRecipe(MinestuckBlocks.RAINBOW_WOOD, 3).key('#', MinestuckBlocks.RAINBOW_LOG).patternLine("##").patternLine("##").setGroup("bark").addCriterion("has_log", hasItem(MinestuckTags.Items.RAINBOW_LOGS)).build(recipeBuilder);
		ShapelessRecipeBuilder.shapelessRecipe(MinestuckBlocks.RAINBOW_PLANKS, 4).addIngredient(MinestuckTags.Items.RAINBOW_LOGS).setGroup("planks").addCriterion("has_log", hasItem(MinestuckTags.Items.RAINBOW_LOGS)).build(recipeBuilder);
		ShapedRecipeBuilder.shapedRecipe(MinestuckBlocks.END_WOOD, 3).key('#', MinestuckBlocks.END_LOG).patternLine("##").patternLine("##").setGroup("bark").addCriterion("has_log", hasItem(MinestuckTags.Items.END_LOGS)).build(recipeBuilder);
		ShapelessRecipeBuilder.shapelessRecipe(MinestuckBlocks.END_PLANKS, 4).addIngredient(MinestuckTags.Items.END_LOGS).setGroup("planks").addCriterion("has_log", hasItem(MinestuckTags.Items.END_LOGS)).build(recipeBuilder);
		ShapedRecipeBuilder.shapedRecipe(MinestuckBlocks.VINE_WOOD, 3).key('#', MinestuckBlocks.VINE_LOG).patternLine("##").patternLine("##").setGroup("bark").addCriterion("has_log", hasItem(MinestuckTags.Items.VINE_LOGS)).build(recipeBuilder);
		ShapedRecipeBuilder.shapedRecipe(MinestuckBlocks.FLOWERY_VINE_WOOD, 3).key('#', MinestuckBlocks.FLOWERY_VINE_LOG).patternLine("##").patternLine("##").setGroup("bark").addCriterion("has_log", hasItem(MinestuckTags.Items.FLOWERY_VINE_LOGS)).build(recipeBuilder);
		ShapedRecipeBuilder.shapedRecipe(MinestuckBlocks.DEAD_WOOD, 3).key('#', MinestuckBlocks.DEAD_LOG).patternLine("##").patternLine("##").setGroup("bark").addCriterion("has_log", hasItem(MinestuckTags.Items.DEAD_LOGS)).build(recipeBuilder);
		ShapelessRecipeBuilder.shapelessRecipe(MinestuckBlocks.DEAD_PLANKS, 4).addIngredient(MinestuckTags.Items.DEAD_LOGS).setGroup("planks").addCriterion("has_log", hasItem(MinestuckTags.Items.DEAD_LOGS)).build(recipeBuilder);
		ShapedRecipeBuilder.shapedRecipe(MinestuckBlocks.PETRIFIED_WOOD, 3).key('#', MinestuckBlocks.PETRIFIED_LOG).patternLine("##").patternLine("##").setGroup("bark").addCriterion("has_log", hasItem(MinestuckTags.Items.PETRIFIED_LOGS)).build(recipeBuilder);
		ShapelessRecipeBuilder.shapelessRecipe(Blocks.OAK_PLANKS).addIngredient(MinestuckBlocks.TREATED_PLANKS).addIngredient(Items.WATER_BUCKET).setGroup("planks").addCriterion("has_log", hasItem(MinestuckBlocks.TREATED_PLANKS)).build(recipeBuilder, new ResourceLocation(Minestuck.MOD_ID, "oak_planks_from_treated_planks"));
		ShapedRecipeBuilder.shapedRecipe(MinestuckBlocks.RAINBOW_PLANKS_STAIRS, 4).key('#', MinestuckBlocks.RAINBOW_PLANKS).patternLine("#  ").patternLine("## ").patternLine("###").setGroup("wooden_stairs").addCriterion("has_planks", hasItem(MinestuckBlocks.RAINBOW_PLANKS)).build(recipeBuilder);
		ShapedRecipeBuilder.shapedRecipe(MinestuckBlocks.END_PLANKS_STAIRS, 4).key('#', MinestuckBlocks.END_PLANKS).patternLine("#  ").patternLine("## ").patternLine("###").setGroup("wooden_stairs").addCriterion("has_planks", hasItem(MinestuckBlocks.END_PLANKS)).build(recipeBuilder);
		ShapedRecipeBuilder.shapedRecipe(MinestuckBlocks.DEAD_PLANKS_STAIRS, 4).key('#', MinestuckBlocks.DEAD_PLANKS).patternLine("#  ").patternLine("## ").patternLine("###").setGroup("wooden_stairs").addCriterion("has_planks", hasItem(MinestuckBlocks.DEAD_PLANKS)).build(recipeBuilder);
		ShapedRecipeBuilder.shapedRecipe(MinestuckBlocks.TREATED_PLANKS_STAIRS, 4).key('#', MinestuckBlocks.TREATED_PLANKS).patternLine("#  ").patternLine("## ").patternLine("###").setGroup("wooden_stairs").addCriterion("has_planks", hasItem(MinestuckBlocks.TREATED_PLANKS)).build(recipeBuilder);
		ShapedRecipeBuilder.shapedRecipe(MinestuckBlocks.RAINBOW_PLANKS_SLAB, 6).key('#', MinestuckBlocks.RAINBOW_PLANKS).patternLine("###").setGroup("wooden_slab").addCriterion("has_planks", hasItem(MinestuckBlocks.RAINBOW_PLANKS)).build(recipeBuilder);
		ShapedRecipeBuilder.shapedRecipe(MinestuckBlocks.END_PLANKS_SLAB, 6).key('#', MinestuckBlocks.END_PLANKS).patternLine("###").setGroup("wooden_slab").addCriterion("has_planks", hasItem(MinestuckBlocks.END_PLANKS)).build(recipeBuilder);
		ShapedRecipeBuilder.shapedRecipe(MinestuckBlocks.DEAD_PLANKS_SLAB, 6).key('#', MinestuckBlocks.DEAD_PLANKS).patternLine("###").setGroup("wooden_slab").addCriterion("has_planks", hasItem(MinestuckBlocks.DEAD_PLANKS)).build(recipeBuilder);
		ShapedRecipeBuilder.shapedRecipe(MinestuckBlocks.TREATED_PLANKS_SLAB, 6).key('#', MinestuckBlocks.TREATED_PLANKS).patternLine("###").setGroup("wooden_slab").addCriterion("has_planks", hasItem(MinestuckBlocks.TREATED_PLANKS)).build(recipeBuilder);
		
		ShapedRecipeBuilder.shapedRecipe(MinestuckBlocks.COMPUTER_OFF).key('e', MinestuckItems.ENERGY_CORE).key('i', Tags.Items.INGOTS_IRON).patternLine("iii").patternLine("iei").patternLine("iii").addCriterion("has_energy_core", hasItem(MinestuckItems.ENERGY_CORE)).build(recipeBuilder);
		ShapedRecipeBuilder.shapedRecipe(MinestuckBlocks.URANIUM_COOKER).key('e', MinestuckItems.ENERGY_CORE).key('i', Tags.Items.INGOTS_IRON).key('F', Blocks.FURNACE).patternLine("iii").patternLine("iFi").patternLine("iei").addCriterion("has_energy_core", hasItem(MinestuckItems.ENERGY_CORE)).build(recipeBuilder);
		
		ShapelessRecipeBuilder.shapelessRecipe(Blocks.SPRUCE_PLANKS).addIngredient(MinestuckBlocks.WOODEN_CACTUS).setGroup("planks").addCriterion("has_cactus", hasItem(MinestuckBlocks.WOODEN_CACTUS)).build(recipeBuilder, new ResourceLocation(Minestuck.MOD_ID, "spruce_planks_from_wooden_cactus"));
		ShapedRecipeBuilder.shapedRecipe(MinestuckBlocks.APPLE_CAKE).key('m', Items.MILK_BUCKET).key('a', Items.APPLE).key('w', Items.WHEAT).key('e', Items.EGG).patternLine("mmm").patternLine("aea").patternLine("www").addCriterion("has_egg", hasItem(Items.EGG)).build(recipeBuilder);
		ShapedRecipeBuilder.shapedRecipe(MinestuckBlocks.CHESSBOARD).key('W', Blocks.WHITE_TERRACOTTA).key('B', Blocks.BLACK_TERRACOTTA).patternLine("WBW").patternLine("BWB").patternLine("WBW").setGroup("chessboard").addCriterion("has_white_terracotta", hasItem(Blocks.WHITE_TERRACOTTA)).addCriterion("has_black_terracotta", hasItem(Blocks.BLACK_TERRACOTTA)).build(recipeBuilder, new ResourceLocation(Minestuck.MOD_ID, "chessboard_white"));
		ShapedRecipeBuilder.shapedRecipe(MinestuckBlocks.CHESSBOARD).key('W', Blocks.WHITE_TERRACOTTA).key('B', Blocks.BLACK_TERRACOTTA).patternLine("BWB").patternLine("WBW").patternLine("BWB").setGroup("chessboard").addCriterion("has_white_terracotta", hasItem(Blocks.WHITE_TERRACOTTA)).addCriterion("has_black_terracotta", hasItem(Blocks.BLACK_TERRACOTTA)).build(recipeBuilder, new ResourceLocation(Minestuck.MOD_ID, "chessboard_black"));
		
		ShapedRecipeBuilder.shapedRecipe(MinestuckItems.CLAW_HAMMER).key('i', Tags.Items.INGOTS_IRON).key('s', Tags.Items.RODS_WOODEN).patternLine(" ii").patternLine("is ").patternLine(" s ").addCriterion("has_iron_ingot", hasItem(Tags.Items.INGOTS_IRON)).build(recipeBuilder);
		ShapedRecipeBuilder.shapedRecipe(MinestuckItems.SLEDGE_HAMMER).key('i', Tags.Items.INGOTS_IRON).key('s', Tags.Items.RODS_WOODEN).key('S', Tags.Items.STONE).patternLine("iSi").patternLine(" s ").patternLine(" s ").addCriterion("has_iron_ingot", hasItem(Tags.Items.INGOTS_IRON)).build(recipeBuilder);
		ShapedRecipeBuilder.shapedRecipe(MinestuckItems.KATANA).key('i', Tags.Items.INGOTS_IRON).key('s', Tags.Items.RODS_WOODEN).patternLine("  i").patternLine(" i ").patternLine("s  ").addCriterion("has_iron_ingot", hasItem(Tags.Items.INGOTS_IRON)).build(recipeBuilder);
		ShapedRecipeBuilder.shapedRecipe(MinestuckItems.SICKLE).key('i', Tags.Items.INGOTS_IRON).key('s', Tags.Items.RODS_WOODEN).patternLine("ii ").patternLine("  i").patternLine(" s ").addCriterion("has_iron_ingot", hasItem(Tags.Items.INGOTS_IRON)).build(recipeBuilder);
		ShapedRecipeBuilder.shapedRecipe(MinestuckItems.DEUCE_CLUB).key('P', ItemTags.PLANKS).key('s', Tags.Items.RODS_WOODEN).patternLine("  P").patternLine(" s ").patternLine("s  ").addCriterion("has_stick", hasItem(Tags.Items.RODS_WOODEN)).build(recipeBuilder);
		ShapedRecipeBuilder.shapedRecipe(MinestuckItems.CANE).key('s', Tags.Items.RODS_WOODEN).patternLine("  s").patternLine(" s ").patternLine("s  ").addCriterion("has_stick", hasItem(Tags.Items.RODS_WOODEN)).build(recipeBuilder);
		ShapedRecipeBuilder.shapedRecipe(MinestuckItems.IRON_CANE).key('i', Tags.Items.INGOTS_IRON).key('c', MinestuckItems.CANE).patternLine("  i").patternLine(" c ").patternLine("i  ").addCriterion("has_iron_ingot", hasItem(Tags.Items.INGOTS_IRON)).build(recipeBuilder);
		ShapedRecipeBuilder.shapedRecipe(MinestuckItems.WOODEN_SPOON).key('s', Tags.Items.RODS_WOODEN).key('b', Items.BOWL).patternLine("b").patternLine("s").patternLine("s").addCriterion("has_stick", hasItem(Tags.Items.RODS_WOODEN)).build(recipeBuilder);
		ShapedRecipeBuilder.shapedRecipe(MinestuckItems.FORK).key('S', Tags.Items.STONE).patternLine("S S").patternLine(" S ").patternLine(" S ").addCriterion("has_stone", hasItem(Tags.Items.STONE)).build(recipeBuilder);
		
		ShapedRecipeBuilder.shapedRecipe(MinestuckItems.ENERGY_CORE).key('u', MinestuckItems.RAW_URANIUM).key('c', MinestuckItems.RAW_CRUXITE).patternLine("cuc").patternLine("ucu").patternLine("cuc").addCriterion("has_raw_uranium", hasItem(MinestuckItems.RAW_URANIUM)).build(recipeBuilder);
		ShapedRecipeBuilder.shapedRecipe(MinestuckItems.CLIENT_DISK).key('i', Items.IRON_INGOT).key('c', MinestuckItems.RAW_CRUXITE).patternLine("c c").patternLine(" i ").patternLine("c c").setGroup("sburb_disk").addCriterion("has_raw_cruxite", hasItem(MinestuckItems.RAW_CRUXITE)).build(recipeBuilder);
		ShapedRecipeBuilder.shapedRecipe(MinestuckItems.SERVER_DISK).key('i', Items.IRON_INGOT).key('c', MinestuckItems.RAW_CRUXITE).patternLine(" c ").patternLine("cic").patternLine(" c ").setGroup("sburb_disk").addCriterion("has_raw_cruxite", hasItem(MinestuckItems.RAW_CRUXITE)).build(recipeBuilder);
		ShapedRecipeBuilder.shapedRecipe(MinestuckItems.CAPTCHA_CARD).key('p', Items.PAPER).key('c', MinestuckItems.RAW_CRUXITE).patternLine("ppp").patternLine("pcp").patternLine("ppp").addCriterion("has_raw_cruxite", hasItem(MinestuckItems.RAW_CRUXITE)).build(recipeBuilder);
		NonMirroredRecipeBuilder.nonMirroredRecipe(MinestuckItems.STACK_MODUS_CARD).key('a', IngredientNBT.fromItems(MinestuckItems.CAPTCHA_CARD)).key('c', MinestuckItems.RAW_CRUXITE).key('C', MinestuckBlocks.CRUXITE_BLOCK).patternLine("Cac").addCriterion("has_card", hasItem(MinestuckItems.CAPTCHA_CARD)).build(recipeBuilder);
		NonMirroredRecipeBuilder.nonMirroredRecipe(MinestuckItems.QUEUE_MODUS_CARD).key('a', IngredientNBT.fromItems(MinestuckItems.CAPTCHA_CARD)).key('c', MinestuckItems.RAW_CRUXITE).key('C', MinestuckBlocks.CRUXITE_BLOCK).patternLine("caC").addCriterion("has_card", hasItem(MinestuckItems.CAPTCHA_CARD)).build(recipeBuilder);
		
		ShapelessRecipeBuilder.shapelessRecipe(MinestuckItems.BUG_ON_A_STICK, 3).addIngredient(Items.STICK, 3).addIngredient(MinestuckItems.JAR_OF_BUGS).addCriterion("has_jag_of_bugs", hasItem(MinestuckItems.JAR_OF_BUGS)).build(recipeBuilder);
		ShapelessRecipeBuilder.shapelessRecipe(MinestuckItems.SALAD).addIngredient(Items.BOWL).addIngredient(ItemTags.LEAVES).addCriterion("has_bowl", hasItem(Items.BOWL)).build(recipeBuilder);
		
		CookingRecipeBuilder.smeltingRecipe(Ingredient.fromTag(MinestuckTags.Items.CRUXITE_ORES), MinestuckItems.RAW_CRUXITE, 0.2F, 200).addCriterion("has_cruxite_ore", hasItem(MinestuckTags.Items.CRUXITE_ORES)).build(recipeBuilder, new ResourceLocation(Minestuck.MOD_ID, "raw_cruxite_from_smelting"));
		CookingRecipeBuilder.blastingRecipe(Ingredient.fromTag(MinestuckTags.Items.CRUXITE_ORES), MinestuckItems.RAW_CRUXITE, 0.2F, 100).addCriterion("has_cruxite_ore", hasItem(MinestuckTags.Items.CRUXITE_ORES)).build(recipeBuilder, new ResourceLocation(Minestuck.MOD_ID, "raw_cruxite_from_blasting"));
		CookingRecipeBuilder.smeltingRecipe(Ingredient.fromTag(MinestuckTags.Items.URANIUM_ORES), MinestuckItems.RAW_URANIUM, 0.2F, 200).addCriterion("has_uranium_ore", hasItem(MinestuckTags.Items.URANIUM_ORES)).build(recipeBuilder, new ResourceLocation(Minestuck.MOD_ID, "raw_uranium_from_smelting"));
		CookingRecipeBuilder.blastingRecipe(Ingredient.fromTag(MinestuckTags.Items.URANIUM_ORES), MinestuckItems.RAW_URANIUM, 0.2F, 100).addCriterion("has_uranium_ore", hasItem(MinestuckTags.Items.URANIUM_ORES)).build(recipeBuilder, new ResourceLocation(Minestuck.MOD_ID, "raw_uranium_from_blasting"));
		CookingRecipeBuilder.smeltingRecipe(Ingredient.fromTag(MinestuckTags.Items.COAL_ORES), Items.COAL, 0.1F, 200).addCriterion("has_coal_ore", hasItem(MinestuckTags.Items.COAL_ORES)).build(recipeBuilder, new ResourceLocation(Minestuck.MOD_ID, "coal_from_smelting"));
		CookingRecipeBuilder.blastingRecipe(Ingredient.fromTag(MinestuckTags.Items.COAL_ORES), Items.COAL, 0.1F, 100).addCriterion("has_coal_ore", hasItem(MinestuckTags.Items.COAL_ORES)).build(recipeBuilder, new ResourceLocation(Minestuck.MOD_ID, "coal_from_blasting"));
		CookingRecipeBuilder.smeltingRecipe(Ingredient.fromTag(MinestuckTags.Items.IRON_ORES), Items.IRON_INGOT, 0.7F, 200).addCriterion("has_iron_ore", hasItem(MinestuckTags.Items.IRON_ORES)).build(recipeBuilder, new ResourceLocation(Minestuck.MOD_ID, "iron_ingot_from_smelting"));
		CookingRecipeBuilder.blastingRecipe(Ingredient.fromTag(MinestuckTags.Items.IRON_ORES), Items.IRON_INGOT, 0.7F, 100).addCriterion("has_iron_ore", hasItem(MinestuckTags.Items.IRON_ORES)).build(recipeBuilder, new ResourceLocation(Minestuck.MOD_ID, "iron_ingot_from_blasting"));
		CookingRecipeBuilder.smeltingRecipe(Ingredient.fromTag(MinestuckTags.Items.GOLD_ORES), Items.GOLD_INGOT, 1.0F, 200).addCriterion("has_gold_ore", hasItem(MinestuckTags.Items.GOLD_ORES)).build(recipeBuilder, new ResourceLocation(Minestuck.MOD_ID, "gold_ingot_from_smelting"));
		CookingRecipeBuilder.blastingRecipe(Ingredient.fromTag(MinestuckTags.Items.GOLD_ORES), Items.GOLD_INGOT, 1.0F, 100).addCriterion("has_gold_ore", hasItem(MinestuckTags.Items.GOLD_ORES)).build(recipeBuilder, new ResourceLocation(Minestuck.MOD_ID, "gold_ingot_from_blasting"));
		CookingRecipeBuilder.smeltingRecipe(Ingredient.fromTag(MinestuckTags.Items.REDSTONE_ORES), Items.REDSTONE, 0.7F, 200).addCriterion("has_redstone_ore", hasItem(MinestuckTags.Items.REDSTONE_ORES)).build(recipeBuilder, new ResourceLocation(Minestuck.MOD_ID, "redstone_from_smelting"));
		CookingRecipeBuilder.blastingRecipe(Ingredient.fromTag(MinestuckTags.Items.REDSTONE_ORES), Items.REDSTONE, 0.7F, 100).addCriterion("has_redstone_ore", hasItem(MinestuckTags.Items.REDSTONE_ORES)).build(recipeBuilder, new ResourceLocation(Minestuck.MOD_ID, "redstone_from_blasting"));
		CookingRecipeBuilder.smeltingRecipe(Ingredient.fromTag(MinestuckTags.Items.QUARTZ_ORES), Items.QUARTZ, 0.2F, 200).addCriterion("has_quartz_ore", hasItem(MinestuckTags.Items.QUARTZ_ORES)).build(recipeBuilder, new ResourceLocation(Minestuck.MOD_ID, "quartz_from_smelting"));
		CookingRecipeBuilder.blastingRecipe(Ingredient.fromTag(MinestuckTags.Items.QUARTZ_ORES), Items.QUARTZ, 0.2F, 100).addCriterion("has_quartz_ore", hasItem(MinestuckTags.Items.QUARTZ_ORES)).build(recipeBuilder, new ResourceLocation(Minestuck.MOD_ID, "quartz_from_blasting"));
		CookingRecipeBuilder.smeltingRecipe(Ingredient.fromTag(MinestuckTags.Items.LAPIS_ORES), Items.LAPIS_LAZULI, 0.2F, 200).addCriterion("has_lapis_ore", hasItem(MinestuckTags.Items.LAPIS_ORES)).build(recipeBuilder, new ResourceLocation(Minestuck.MOD_ID, "lapis_lazuli_from_smelting"));
		CookingRecipeBuilder.blastingRecipe(Ingredient.fromTag(MinestuckTags.Items.LAPIS_ORES), Items.LAPIS_LAZULI, 0.2F, 100).addCriterion("has_lapis_ore", hasItem(MinestuckTags.Items.LAPIS_ORES)).build(recipeBuilder, new ResourceLocation(Minestuck.MOD_ID, "lapis_lazuli_from_blasting"));
		CookingRecipeBuilder.smeltingRecipe(Ingredient.fromTag(MinestuckTags.Items.DIAMOND_ORES), Items.DIAMOND, 1.0F, 200).addCriterion("has_diamond_ore", hasItem(MinestuckTags.Items.DIAMOND_ORES)).build(recipeBuilder, new ResourceLocation(Minestuck.MOD_ID, "diamond_from_smelting"));
		CookingRecipeBuilder.blastingRecipe(Ingredient.fromTag(MinestuckTags.Items.DIAMOND_ORES), Items.DIAMOND, 1.0F, 100).addCriterion("has_diamond_ore", hasItem(MinestuckTags.Items.DIAMOND_ORES)).build(recipeBuilder, new ResourceLocation(Minestuck.MOD_ID, "diamond_from_blasting"));
		
		CookingRecipeBuilder.smeltingRecipe(Ingredient.fromItems(MinestuckBlocks.CRUXITE_DOWEL), MinestuckItems.RAW_CRUXITE, 0.0F, 200).addCriterion("has_cruxite_dowel", hasItem(MinestuckBlocks.CRUXITE_DOWEL)).build(recipeBuilder, new ResourceLocation(Minestuck.MOD_ID, "raw_cruxite_from_dowel"));
		CookingRecipeBuilder.smeltingRecipe(Ingredient.fromItems(MinestuckBlocks.GOLD_SEEDS), Items.GOLD_NUGGET, 0.1F, 200).addCriterion("has_gold_seeds", hasItem(MinestuckBlocks.GOLD_SEEDS)).build(recipeBuilder, new ResourceLocation(Minestuck.MOD_ID, "gold_nugget_from_seeds"));
		CookingRecipeBuilder.smeltingRecipe(Ingredient.fromItems(MinestuckBlocks.WOODEN_CACTUS), Items.CHARCOAL, 0.15F, 200).addCriterion("has_wooden_cactus", hasItem(MinestuckBlocks.WOODEN_CACTUS)).build(recipeBuilder, new ResourceLocation(Minestuck.MOD_ID, "charcoal_from_wooden_cactus"));
		
		cookingRecipesFor(recipeBuilder, Ingredient.fromItems(MinestuckItems.BEEF_SWORD), MinestuckItems.STEAK_SWORD, 0.5F, "has_beef_sword", hasItem(MinestuckItems.BEEF_SWORD));
		
	}
	
	private void cookingRecipesFor(Consumer<IFinishedRecipe> recipeBuilder, Ingredient input, IItemProvider result, float experience, String criterionName, InventoryChangeTrigger.Instance criterion)
	{
		ResourceLocation itemName = Objects.requireNonNull(ForgeRegistries.ITEMS.getKey(result.asItem()), "Registry name for "+result+" was found to be null!");
		CookingRecipeBuilder.smeltingRecipe(input, result, experience, 200).addCriterion(criterionName, criterion).build(recipeBuilder);
		CookingRecipeBuilder.cookingRecipe(input, result, experience, 100, IRecipeSerializer.SMOKING).addCriterion(criterionName, criterion).build(recipeBuilder, new ResourceLocation(itemName.getNamespace(), itemName.getPath()+"_from_smoking"));
		CookingRecipeBuilder.cookingRecipe(input, result, experience, 600, IRecipeSerializer.CAMPFIRE_COOKING).addCriterion(criterionName, criterion).build(recipeBuilder, new ResourceLocation(itemName.getNamespace(), itemName.getPath()+"_from_campfire_cooking"));
	}
}