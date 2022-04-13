package com.mraof.minestuck.data.recipe;

import com.google.gson.JsonObject;
import com.mraof.minestuck.Minestuck;
import com.mraof.minestuck.block.MSBlocks;
import com.mraof.minestuck.item.MSItems;
import com.mraof.minestuck.item.crafting.MSRecipeTypes;
import com.mraof.minestuck.util.ExtraForgeTags;
import com.mraof.minestuck.util.MSTags;
import net.minecraft.advancements.criterion.InventoryChangeTrigger;
import net.minecraft.block.Blocks;
import net.minecraft.data.*;
import net.minecraft.item.Items;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.tags.ItemTags;
import net.minecraft.util.IItemProvider;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.Tags;
import net.minecraftforge.registries.ForgeRegistries;

import javax.annotation.Nullable;
import java.util.Objects;
import java.util.function.Consumer;

public class MinestuckRecipeProvider extends RecipeProvider
{
	public MinestuckRecipeProvider(DataGenerator generator)
	{
		super(generator);
	}
	
	@Override
	protected void buildShapelessRecipes(Consumer<IFinishedRecipe> recipeBuilder)
	{
		ShapelessRecipeBuilder.shapeless(MSItems.RAW_CRUXITE, 9).requires(MSBlocks.CRUXITE_BLOCK).unlockedBy("has_raw_cruxite", has(MSItems.RAW_CRUXITE)).unlockedBy("has_cruxite_block", has(MSBlocks.CRUXITE_BLOCK)).save(recipeBuilder, new ResourceLocation(Minestuck.MOD_ID, "raw_cruxite_from_block"));
		ShapedRecipeBuilder.shaped(MSBlocks.CRUXITE_BLOCK).define('#', MSItems.RAW_CRUXITE).pattern("###").pattern("###").pattern("###").unlockedBy("has_raw_cruxite", has(MSItems.RAW_CRUXITE)).save(recipeBuilder);
		ShapelessRecipeBuilder.shapeless(MSItems.RAW_URANIUM, 9).requires(MSBlocks.URANIUM_BLOCK).unlockedBy("has__raw_uranium", has(MSItems.RAW_URANIUM)).unlockedBy("has_uranium_block", has(MSBlocks.URANIUM_BLOCK)).save(recipeBuilder, new ResourceLocation(Minestuck.MOD_ID, "raw_uranium_from_block"));
		ShapedRecipeBuilder.shaped(MSBlocks.URANIUM_BLOCK).define('#', MSItems.RAW_URANIUM).pattern("###").pattern("###").pattern("###").unlockedBy("has_raw_uranium", has(MSItems.RAW_URANIUM)).save(recipeBuilder);
		ShapelessRecipeBuilder.shapeless(Items.SUGAR, 9).requires(MSBlocks.SUGAR_CUBE).unlockedBy("has_sugar", has(Items.SUGAR)).unlockedBy("has_sugar_cube", has(MSBlocks.SUGAR_CUBE)).save(recipeBuilder, new ResourceLocation(Minestuck.MOD_ID, "sugar_from_cube"));
		ShapedRecipeBuilder.shaped(MSBlocks.SUGAR_CUBE).define('#', Items.SUGAR).pattern("###").pattern("###").pattern("###").unlockedBy("has_sugar", has(Items.SUGAR)).save(recipeBuilder);
		ShapelessRecipeBuilder.shapeless(MSItems.STRAWBERRY_CHUNK, 4).requires(MSBlocks.STRAWBERRY).unlockedBy("has_strawberry_chunks", has( MSItems.STRAWBERRY_CHUNK)).unlockedBy("has_strawberry_block", has(MSBlocks.STRAWBERRY)).save(recipeBuilder);
		ShapedRecipeBuilder.shaped(MSBlocks.STRAWBERRY).define('#', MSItems.STRAWBERRY_CHUNK).pattern("##").pattern("##").unlockedBy("has_strawberry_chunks", has(MSItems.STRAWBERRY_CHUNK)).save(recipeBuilder);
		
		ShapedRecipeBuilder.shaped(MSBlocks.CHISELED_COARSE_STONE).define('#', MSBlocks.COARSE_STONE_SLAB).pattern("#").pattern("#").unlockedBy("has_coarse_stone_slab", has(MSBlocks.COARSE_STONE_SLAB)).save(recipeBuilder);
		ShapedRecipeBuilder.shaped(MSBlocks.COARSE_STONE_BRICKS, 4).define('#', MSBlocks.COARSE_STONE).pattern("##").pattern("##").unlockedBy("has_coarse_stone", has(MSBlocks.COARSE_STONE)).save(recipeBuilder);
		ShapedRecipeBuilder.shaped(MSBlocks.CHISELED_COARSE_STONE_BRICKS).define('#', MSBlocks.COARSE_STONE_BRICK_SLAB).pattern("#").pattern("#").unlockedBy("has_coarse_stone_brick_slab", has(MSBlocks.COARSE_STONE_BRICK_SLAB)).save(recipeBuilder);
		ShapedRecipeBuilder.shaped(MSBlocks.COARSE_STONE_COLUMN, 3).define('#', MSBlocks.COARSE_STONE_BRICKS).pattern("#").pattern("#").pattern("#").unlockedBy("has_coarse_stone_bricks", has(MSBlocks.COARSE_STONE_BRICKS)).save(recipeBuilder);
		ShapelessRecipeBuilder.shapeless(MSBlocks.MOSSY_COARSE_STONE).requires(MSBlocks.COARSE_STONE_BRICKS).requires(Items.VINE).unlockedBy("has_coarse_stone_bricks", has( MSBlocks.COARSE_STONE_BRICKS)).save(recipeBuilder);
		ShapedRecipeBuilder.shaped(MSBlocks.SHADE_BRICKS, 4).define('#', MSBlocks.SHADE_STONE).pattern("##").pattern("##").unlockedBy("has_shade_stone", has(MSBlocks.SHADE_STONE)).save(recipeBuilder);
		ShapedRecipeBuilder.shaped(MSBlocks.CHISELED_SHADE_BRICKS).define('#', MSBlocks.SHADE_BRICK_SLAB).pattern("#").pattern("#").unlockedBy("has_shade_brick_slab", has(MSBlocks.SHADE_BRICK_SLAB)).save(recipeBuilder);
		ShapelessRecipeBuilder.shapeless(MSBlocks.MOSSY_SHADE_BRICKS).requires(MSBlocks.SHADE_BRICKS).requires(MSBlocks.GLOWING_MUSHROOM).unlockedBy("has_shade_bricks", has( MSBlocks.SHADE_BRICKS)).save(recipeBuilder);
		ShapelessRecipeBuilder.shapeless(MSBlocks.BLOOD_SHADE_BRICKS).requires(MSBlocks.SHADE_BRICKS).requires(MSBlocks.COAGULATED_BLOOD).unlockedBy("has_shade_bricks", has( MSBlocks.SHADE_BRICKS)).save(recipeBuilder);
		ShapedRecipeBuilder.shaped(MSBlocks.SHADE_COLUMN, 3).define('#', MSBlocks.SHADE_BRICKS).pattern("#").pattern("#").pattern("#").unlockedBy("has_shade_bricks", has(MSBlocks.SHADE_BRICKS)).save(recipeBuilder);
		ShapedRecipeBuilder.shaped(MSBlocks.MYCELIUM_BRICKS, 4).define('#', MSBlocks.MYCELIUM_STONE).pattern("##").pattern("##").unlockedBy("has_mycelium_stone", has(MSBlocks.MYCELIUM_STONE)).save(recipeBuilder);
		ShapedRecipeBuilder.shaped(MSBlocks.CHISELED_MYCELIUM_BRICKS).define('#', MSBlocks.MYCELIUM_BRICK_SLAB).pattern("#").pattern("#").unlockedBy("has_mycelium_brick_slab", has(MSBlocks.MYCELIUM_BRICK_SLAB)).save(recipeBuilder);
		ShapedRecipeBuilder.shaped(MSBlocks.MYCELIUM_COLUMN, 3).define('#', MSBlocks.MYCELIUM_BRICKS).pattern("#").pattern("#").pattern("#").unlockedBy("has_mycelium_bricks", has(MSBlocks.MYCELIUM_BRICKS)).save(recipeBuilder);
		ShapelessRecipeBuilder.shapeless(MSBlocks.MOSSY_MYCELIUM_BRICKS).requires(MSBlocks.MYCELIUM_BRICKS).requires(Items.VINE).unlockedBy("has_mycelium_bricks", has( MSBlocks.MYCELIUM_BRICKS)).save(recipeBuilder);
		ShapelessRecipeBuilder.shapeless(MSBlocks.FLOWERY_MYCELIUM_BRICKS).requires(MSBlocks.MYCELIUM_BRICKS).requires(Items.RED_MUSHROOM).unlockedBy("has_mycelium_bricks", has( MSBlocks.MYCELIUM_BRICKS)).save(recipeBuilder);
		ShapedRecipeBuilder.shaped(MSBlocks.BLACK_STONE_BRICKS, 4).define('#', MSBlocks.BLACK_STONE).pattern("##").pattern("##").unlockedBy("has_black_stone", has(MSBlocks.BLACK_STONE)).save(recipeBuilder);
		ShapedRecipeBuilder.shaped(MSBlocks.CHISELED_BLACK_STONE_BRICKS).define('#', MSBlocks.BLACK_STONE_BRICK_SLAB).pattern("#").pattern("#").unlockedBy("has_black_stone_brick_slab", has(MSBlocks.BLACK_STONE_BRICK_SLAB)).save(recipeBuilder);
		ShapedRecipeBuilder.shaped(MSBlocks.BLACK_STONE_COLUMN, 3).define('#', MSBlocks.BLACK_STONE_BRICKS).pattern("#").pattern("#").pattern("#").unlockedBy("has_black_stone_bricks", has(MSBlocks.BLACK_STONE_BRICKS)).save(recipeBuilder);
		ShapedRecipeBuilder.shaped(MSBlocks.CHISELED_FROST_TILE).define('#', MSBlocks.FROST_TILE_SLAB).pattern("#").pattern("#").unlockedBy("has_frost_tile_slab", has(MSBlocks.FROST_TILE_SLAB)).save(recipeBuilder);
		ShapedRecipeBuilder.shaped(MSBlocks.FROST_BRICKS, 4).define('#', MSBlocks.FROST_TILE).pattern("##").pattern("##").unlockedBy("has_frost_tile", has(MSBlocks.FROST_TILE)).save(recipeBuilder);
		ShapedRecipeBuilder.shaped(MSBlocks.CHISELED_FROST_BRICKS).define('#', MSBlocks.FROST_BRICK_SLAB).pattern("#").pattern("#").unlockedBy("has_frost_brick_slab", has(MSBlocks.FROST_BRICK_SLAB)).save(recipeBuilder);
		ShapedRecipeBuilder.shaped(MSBlocks.FROST_COLUMN, 3).define('#', MSBlocks.FROST_BRICKS).pattern("#").pattern("#").pattern("#").unlockedBy("has_frost_bricks", has(MSBlocks.FROST_BRICKS)).save(recipeBuilder);
		ShapelessRecipeBuilder.shapeless(MSBlocks.FLOWERY_FROST_BRICKS).requires(MSBlocks.FROST_BRICKS).requires(Items.VINE).requires(Items.BONE_MEAL).unlockedBy("has_frost_bricks", has( MSBlocks.FROST_BRICKS)).save(recipeBuilder);
		ShapedRecipeBuilder.shaped(MSBlocks.POLISHED_CHALK, 4).define('#', MSBlocks.CHALK).pattern("##").pattern("##").unlockedBy("has_chalk", has(MSBlocks.CHALK)).save(recipeBuilder);
		ShapedRecipeBuilder.shaped(MSBlocks.CHALK_BRICKS, 4).define('#', MSBlocks.POLISHED_CHALK).pattern("##").pattern("##").unlockedBy("has_polished_chalk", has(MSBlocks.POLISHED_CHALK)).save(recipeBuilder);
		ShapedRecipeBuilder.shaped(MSBlocks.CHISELED_CHALK_BRICKS, 4).define('#', MSBlocks.CHALK_BRICKS).pattern("##").pattern("##").unlockedBy("has_chalk_bricks", has(MSBlocks.CHALK_BRICKS)).save(recipeBuilder);
		ShapedRecipeBuilder.shaped(MSBlocks.CHALK_COLUMN, 3).define('#', MSBlocks.CHALK_BRICKS).pattern("#").pattern("#").pattern("#").unlockedBy("has_chalk_bricks", has(MSBlocks.CHALK_BRICKS)).save(recipeBuilder);
		ShapelessRecipeBuilder.shapeless(MSBlocks.MOSSY_CHALK_BRICKS).requires(MSBlocks.CHALK_BRICKS).requires(Items.VINE).unlockedBy("has_chalk_bricks", has( MSBlocks.CHALK_BRICKS)).save(recipeBuilder);
		ShapelessRecipeBuilder.shapeless(MSBlocks.FLOWERY_CHALK_BRICKS).requires(MSBlocks.MOSSY_CHALK_BRICKS).requires(Items.BONE_MEAL).unlockedBy("has_mossy_chalk_bricks", has( MSBlocks.MOSSY_CHALK_BRICKS)).save(recipeBuilder);
		ShapedRecipeBuilder.shaped(MSBlocks.PINK_STONE_BRICKS, 4).define('#', MSBlocks.PINK_STONE).pattern("##").pattern("##").unlockedBy("has_pink_stone", has(MSBlocks.PINK_STONE)).save(recipeBuilder);
		ShapedRecipeBuilder.shaped(MSBlocks.CHISELED_PINK_STONE_BRICKS).define('#', MSBlocks.PINK_STONE_BRICK_SLAB).pattern("#").pattern("#").unlockedBy("has_pink_stone_brick_slab", has(MSBlocks.PINK_STONE_BRICK_SLAB)).save(recipeBuilder);
		ShapelessRecipeBuilder.shapeless(MSBlocks.MOSSY_PINK_STONE_BRICKS).requires(MSBlocks.PINK_STONE_BRICKS).requires(Blocks.VINE).unlockedBy("has_vine", has(Blocks.VINE)).save(recipeBuilder);
		ShapedRecipeBuilder.shaped(MSBlocks.PINK_STONE_COLUMN, 3).define('#', MSBlocks.PINK_STONE_BRICKS).pattern("#").pattern("#").pattern("#").unlockedBy("has_pink_stone_bricks", has(MSBlocks.PINK_STONE_BRICKS)).save(recipeBuilder);
		ShapedRecipeBuilder.shaped(MSBlocks.BROWN_STONE_BRICKS, 4).define('#', MSBlocks.BROWN_STONE).pattern("##").pattern("##").unlockedBy("has_brown_stone", has(MSBlocks.BROWN_STONE)).save(recipeBuilder);
		ShapedRecipeBuilder.shaped(MSBlocks.BROWN_STONE_COLUMN, 3).define('#', MSBlocks.BROWN_STONE_BRICKS).pattern("#").pattern("#").pattern("#").unlockedBy("has_brown_stone_bricks", has(MSBlocks.BROWN_STONE_BRICKS)).save(recipeBuilder);
		ShapedRecipeBuilder.shaped(MSBlocks.POLISHED_GREEN_STONE, 4).define('#', MSBlocks.GREEN_STONE).pattern("##").pattern("##").unlockedBy("has_green_stone", has(MSBlocks.GREEN_STONE)).save(recipeBuilder);
		ShapedRecipeBuilder.shaped(MSBlocks.GREEN_STONE_BRICKS, 4).define('#', MSBlocks.POLISHED_GREEN_STONE).pattern("##").pattern("##").unlockedBy("has_polished_green_stone", has(MSBlocks.POLISHED_GREEN_STONE)).save(recipeBuilder);
		ShapedRecipeBuilder.shaped(MSBlocks.GREEN_STONE_COLUMN, 3).define('#', MSBlocks.GREEN_STONE_BRICKS).pattern("#").pattern("#").pattern("#").unlockedBy("has_green_stone_bricks", has(MSBlocks.GREEN_STONE_BRICKS)).save(recipeBuilder);
		ShapedRecipeBuilder.shaped(MSBlocks.HORIZONTAL_GREEN_STONE_BRICKS, 2).define('#', MSBlocks.POLISHED_GREEN_STONE).pattern("##").unlockedBy("has_polished_green_stone", has(MSBlocks.POLISHED_GREEN_STONE)).save(recipeBuilder);
		ShapedRecipeBuilder.shaped(MSBlocks.VERTICAL_GREEN_STONE_BRICKS, 2).define('#', MSBlocks.POLISHED_GREEN_STONE).pattern("#").pattern("#").unlockedBy("has_polished_green_stone", has(MSBlocks.POLISHED_GREEN_STONE)).save(recipeBuilder);
		ShapelessRecipeBuilder.shapeless(MSBlocks.MOSSY_DECREPIT_STONE_BRICKS).requires(MSBlocks.DECREPIT_STONE_BRICKS).requires(Blocks.VINE).unlockedBy("has_vine", has(Blocks.VINE)).save(recipeBuilder);
		ShapelessRecipeBuilder.shapeless(MSBlocks.FLOWERY_MOSSY_STONE_BRICKS).requires(MSBlocks.MOSSY_DECREPIT_STONE_BRICKS).requires(MSBlocks.COAGULATED_BLOOD).unlockedBy("has_coagulated_blood", has(MSBlocks.COAGULATED_BLOOD)).save(recipeBuilder);
		ShapedRecipeBuilder.shaped(MSBlocks.BLACK_CASTLE_BRICK_STAIRS, 4).define('#', MSBlocks.BLACK_CASTLE_BRICKS).pattern("#  ").pattern("## ").pattern("###").unlockedBy("has_black_chess_bricks", has(MSBlocks.BLACK_CASTLE_BRICKS)).save(recipeBuilder);
		ShapedRecipeBuilder.shaped(MSBlocks.DARK_GRAY_CASTLE_BRICK_STAIRS, 4).define('#', MSBlocks.DARK_GRAY_CASTLE_BRICKS).pattern("#  ").pattern("## ").pattern("###").unlockedBy("has_dark_gray_chess_bricks", has(MSBlocks.DARK_GRAY_CASTLE_BRICKS)).save(recipeBuilder);
		ShapedRecipeBuilder.shaped(MSBlocks.LIGHT_GRAY_CASTLE_BRICK_STAIRS, 4).define('#', MSBlocks.LIGHT_GRAY_CASTLE_BRICKS).pattern("#  ").pattern("## ").pattern("###").unlockedBy("has_light_gray_chess_bricks", has(MSBlocks.LIGHT_GRAY_CASTLE_BRICKS)).save(recipeBuilder);
		ShapedRecipeBuilder.shaped(MSBlocks.WHITE_CASTLE_BRICK_STAIRS, 4).define('#', MSBlocks.WHITE_CASTLE_BRICKS).pattern("#  ").pattern("## ").pattern("###").unlockedBy("has_white_chess_bricks", has(MSBlocks.WHITE_CASTLE_BRICKS)).save(recipeBuilder);
		ShapedRecipeBuilder.shaped(MSBlocks.COARSE_STONE_STAIRS, 4).define('#', MSBlocks.COARSE_STONE).pattern("#  ").pattern("## ").pattern("###").unlockedBy("has_coarse_stone", has(MSBlocks.COARSE_STONE)).save(recipeBuilder);
		ShapedRecipeBuilder.shaped(MSBlocks.COARSE_STONE_BRICK_STAIRS, 4).define('#', MSBlocks.COARSE_STONE_BRICKS).pattern("#  ").pattern("## ").pattern("###").unlockedBy("has_coarse_stone_bricks", has(MSBlocks.COARSE_STONE_BRICKS)).save(recipeBuilder);
		ShapedRecipeBuilder.shaped(MSBlocks.SHADE_STAIRS, 4).define('#', MSBlocks.SHADE_STONE).pattern("#  ").pattern("## ").pattern("###").unlockedBy("has_shade_stone", has(MSBlocks.SHADE_STONE)).save(recipeBuilder);
		ShapedRecipeBuilder.shaped(MSBlocks.SHADE_BRICK_STAIRS, 4).define('#', MSBlocks.SHADE_BRICKS).pattern("#  ").pattern("## ").pattern("###").unlockedBy("has_shade_bricks", has(MSBlocks.SHADE_BRICKS)).save(recipeBuilder);
		ShapedRecipeBuilder.shaped(MSBlocks.FROST_TILE_STAIRS, 4).define('#', MSBlocks.FROST_TILE).pattern("#  ").pattern("## ").pattern("###").unlockedBy("has_frost_tile", has(MSBlocks.FROST_TILE)).save(recipeBuilder);
		ShapedRecipeBuilder.shaped(MSBlocks.FROST_BRICK_STAIRS, 4).define('#', MSBlocks.FROST_BRICKS).pattern("#  ").pattern("## ").pattern("###").unlockedBy("has_frost_bricks", has(MSBlocks.FROST_BRICKS)).save(recipeBuilder);
		ShapedRecipeBuilder.shaped(MSBlocks.CAST_IRON_STAIRS, 4).define('#', MSBlocks.CAST_IRON).pattern("#  ").pattern("## ").pattern("###").unlockedBy("has_cast_iron", has(MSBlocks.CAST_IRON)).save(recipeBuilder);
		ShapedRecipeBuilder.shaped(MSBlocks.MYCELIUM_STAIRS, 4).define('#', MSBlocks.MYCELIUM_STONE).pattern("#  ").pattern("## ").pattern("###").unlockedBy("has_mycelium_stone", has(MSBlocks.MYCELIUM_STONE)).save(recipeBuilder);
		ShapedRecipeBuilder.shaped(MSBlocks.MYCELIUM_BRICK_STAIRS, 4).define('#', MSBlocks.MYCELIUM_BRICKS).pattern("#  ").pattern("## ").pattern("###").unlockedBy("has_mycelium_bricks", has(MSBlocks.MYCELIUM_BRICKS)).save(recipeBuilder);
		ShapedRecipeBuilder.shaped(MSBlocks.BLACK_STONE_STAIRS, 4).define('#', MSBlocks.BLACK_STONE).pattern("#  ").pattern("## ").pattern("###").unlockedBy("has_black_stone", has(MSBlocks.BLACK_STONE)).save(recipeBuilder);
		ShapedRecipeBuilder.shaped(MSBlocks.BLACK_STONE_BRICK_STAIRS, 4).define('#', MSBlocks.BLACK_STONE_BRICKS).pattern("#  ").pattern("## ").pattern("###").unlockedBy("has_black_stone_bricks", has(MSBlocks.BLACK_STONE_BRICKS)).save(recipeBuilder);
		ShapedRecipeBuilder.shaped(MSBlocks.CHALK_STAIRS, 4).define('#', MSBlocks.CHALK).pattern("#  ").pattern("## ").pattern("###").unlockedBy("has_chalk", has(MSBlocks.CHALK)).save(recipeBuilder);
		ShapedRecipeBuilder.shaped(MSBlocks.CHALK_BRICK_STAIRS, 4).define('#', MSBlocks.CHALK_BRICKS).pattern("#  ").pattern("## ").pattern("###").unlockedBy("has_chalk_bricks", has(MSBlocks.CHALK_BRICKS)).save(recipeBuilder);
		ShapedRecipeBuilder.shaped(MSBlocks.PINK_STONE_STAIRS, 4).define('#', MSBlocks.PINK_STONE).pattern("#  ").pattern("## ").pattern("###").unlockedBy("has_pink_stone", has(MSBlocks.PINK_STONE)).save(recipeBuilder);
		ShapedRecipeBuilder.shaped(MSBlocks.PINK_STONE_BRICK_STAIRS, 4).define('#', MSBlocks.PINK_STONE_BRICKS).pattern("#  ").pattern("## ").pattern("###").unlockedBy("has_pink_stone_bricks", has(MSBlocks.PINK_STONE_BRICKS)).save(recipeBuilder);
		ShapedRecipeBuilder.shaped(MSBlocks.BROWN_STONE_STAIRS, 4).define('#', MSBlocks.BROWN_STONE).pattern("#  ").pattern("## ").pattern("###").unlockedBy("has_brown_stone", has(MSBlocks.BROWN_STONE)).save(recipeBuilder);
		ShapedRecipeBuilder.shaped(MSBlocks.BROWN_STONE_BRICK_STAIRS, 4).define('#', MSBlocks.BROWN_STONE_BRICKS).pattern("#  ").pattern("## ").pattern("###").unlockedBy("has_brown_stone_bricks", has(MSBlocks.BROWN_STONE_BRICKS)).save(recipeBuilder);
		ShapedRecipeBuilder.shaped(MSBlocks.GREEN_STONE_STAIRS, 4).define('#', MSBlocks.GREEN_STONE).pattern("#  ").pattern("## ").pattern("###").unlockedBy("has_green_stone", has(MSBlocks.GREEN_STONE)).save(recipeBuilder);
		ShapedRecipeBuilder.shaped(MSBlocks.GREEN_STONE_BRICK_STAIRS, 4).define('#', MSBlocks.GREEN_STONE_BRICKS).pattern("#  ").pattern("## ").pattern("###").unlockedBy("has_green_stone_bricks", has(MSBlocks.GREEN_STONE_BRICKS)).save(recipeBuilder);
		ShapedRecipeBuilder.shaped(MSBlocks.STEEP_GREEN_STONE_BRICK_STAIRS_BASE, 2).define('#', MSBlocks.GREEN_STONE_BRICK_STAIRS).pattern("##").unlockedBy("has_green_stone_brick_stairs", has(MSBlocks.GREEN_STONE_BRICK_STAIRS)).save(recipeBuilder);
		ShapedRecipeBuilder.shaped(MSBlocks.STEEP_GREEN_STONE_BRICK_STAIRS_TOP, 2).define('#', MSBlocks.GREEN_STONE_BRICK_STAIRS).pattern("#").pattern("#").unlockedBy("has_green_stone_brick_stairs", has(MSBlocks.GREEN_STONE_BRICK_STAIRS)).save(recipeBuilder);
		ShapedRecipeBuilder.shaped(MSBlocks.FLOWERY_MOSSY_STONE_BRICK_STAIRS, 4).define('#', MSBlocks.FLOWERY_MOSSY_STONE_BRICKS).pattern("#  ").pattern("## ").pattern("###").unlockedBy("has_flowery_mossy_bricks", has(MSBlocks.FLOWERY_MOSSY_STONE_BRICKS)).save(recipeBuilder);
		ShapedRecipeBuilder.shaped(MSBlocks.BLACK_CASTLE_BRICK_SLAB, 6).define('#', MSBlocks.BLACK_CASTLE_BRICKS).pattern("###").unlockedBy("has_black_chess_bricks", has(MSBlocks.BLACK_CASTLE_BRICKS)).save(recipeBuilder);
		ShapedRecipeBuilder.shaped(MSBlocks.DARK_GRAY_CASTLE_BRICK_SLAB, 6).define('#', MSBlocks.DARK_GRAY_CASTLE_BRICKS).pattern("###").unlockedBy("has_dark_gray_chess_bricks", has(MSBlocks.DARK_GRAY_CASTLE_BRICKS)).save(recipeBuilder);
		ShapedRecipeBuilder.shaped(MSBlocks.LIGHT_GRAY_CASTLE_BRICK_SLAB, 6).define('#', MSBlocks.LIGHT_GRAY_CASTLE_BRICKS).pattern("###").unlockedBy("has_light_gray_chess_bricks", has(MSBlocks.LIGHT_GRAY_CASTLE_BRICKS)).save(recipeBuilder);
		ShapedRecipeBuilder.shaped(MSBlocks.WHITE_CASTLE_BRICK_SLAB, 6).define('#', MSBlocks.WHITE_CASTLE_BRICKS).pattern("###").unlockedBy("has_white_chess_bricks", has(MSBlocks.WHITE_CASTLE_BRICKS)).save(recipeBuilder);
		ShapedRecipeBuilder.shaped(MSBlocks.COARSE_STONE_SLAB, 6).define( '#', MSBlocks.COARSE_STONE).pattern("###").unlockedBy("has_coarse_stone", has(MSBlocks.COARSE_STONE)).save(recipeBuilder);
		ShapedRecipeBuilder.shaped(MSBlocks.COARSE_STONE_BRICK_SLAB, 6).define( '#', MSBlocks.COARSE_STONE_BRICKS).pattern("###").unlockedBy("has_coarse_stone_bricks", has(MSBlocks.COARSE_STONE_BRICKS)).save(recipeBuilder);
		ShapedRecipeBuilder.shaped(MSBlocks.SHADE_SLAB, 6).define('#', MSBlocks.SHADE_STONE).pattern("###").unlockedBy("has_shade_stone", has(MSBlocks.SHADE_STONE)).save(recipeBuilder);
		ShapedRecipeBuilder.shaped(MSBlocks.SHADE_BRICK_SLAB, 6).define('#', MSBlocks.SHADE_BRICKS).pattern("###").unlockedBy("has_shade_bricks", has(MSBlocks.SHADE_BRICKS)).save(recipeBuilder);
		ShapedRecipeBuilder.shaped(MSBlocks.FROST_TILE_SLAB, 6).define('#', MSBlocks.FROST_TILE).pattern("###").unlockedBy("has_frost_tile", has(MSBlocks.FROST_TILE)).save(recipeBuilder);
		ShapedRecipeBuilder.shaped(MSBlocks.FROST_BRICK_SLAB, 6).define('#', MSBlocks.FROST_BRICKS).pattern("###").unlockedBy("has_frost_bricks", has(MSBlocks.FROST_BRICKS)).save(recipeBuilder);
		ShapedRecipeBuilder.shaped(MSBlocks.CHALK_SLAB, 6).define('#', MSBlocks.CHALK).pattern("###").unlockedBy("has_chalk", has(MSBlocks.CHALK)).save(recipeBuilder);
		ShapedRecipeBuilder.shaped(MSBlocks.CHALK_BRICK_SLAB, 6).define('#', MSBlocks.CHALK_BRICKS).pattern("###").unlockedBy("has_chalk_bricks", has(MSBlocks.CHALK_BRICKS)).save(recipeBuilder);
		ShapedRecipeBuilder.shaped(MSBlocks.BLACK_STONE_SLAB, 6).define('#', MSBlocks.BLACK_STONE).pattern("###").unlockedBy("has_black_stone", has(MSBlocks.BLACK_STONE)).save(recipeBuilder);
		ShapedRecipeBuilder.shaped(MSBlocks.BLACK_STONE_BRICK_SLAB, 6).define('#', MSBlocks.BLACK_STONE_BRICKS).pattern("###").unlockedBy("has_black_stone_bricks", has(MSBlocks.BLACK_STONE_BRICKS)).save(recipeBuilder);
		ShapedRecipeBuilder.shaped(MSBlocks.MYCELIUM_SLAB, 6).define('#', MSBlocks.MYCELIUM_STONE).pattern("###").unlockedBy("has_mycelium_stone", has(MSBlocks.MYCELIUM_STONE)).save(recipeBuilder);
		ShapedRecipeBuilder.shaped(MSBlocks.MYCELIUM_BRICK_SLAB, 6).define('#', MSBlocks.MYCELIUM_BRICKS).pattern("###").unlockedBy("has_mycelium_bricks", has(MSBlocks.MYCELIUM_BRICKS)).save(recipeBuilder);
		ShapedRecipeBuilder.shaped(MSBlocks.PINK_STONE_SLAB, 6).define('#', MSBlocks.PINK_STONE).pattern("###").unlockedBy("has_pink_stone", has(MSBlocks.PINK_STONE)).save(recipeBuilder);
		ShapedRecipeBuilder.shaped(MSBlocks.PINK_STONE_BRICK_SLAB, 6).define('#', MSBlocks.PINK_STONE_BRICKS).pattern("###").unlockedBy("has_pink_stone_bricks", has(MSBlocks.PINK_STONE_BRICKS)).save(recipeBuilder);
		ShapedRecipeBuilder.shaped(MSBlocks.BROWN_STONE_SLAB, 6).define('#', MSBlocks.BROWN_STONE).pattern("###").unlockedBy("has_brown_stone", has(MSBlocks.BROWN_STONE)).save(recipeBuilder);
		ShapedRecipeBuilder.shaped(MSBlocks.BROWN_STONE_BRICK_SLAB, 6).define('#', MSBlocks.BROWN_STONE_BRICKS).pattern("###").unlockedBy("has_brown_stone_bricks", has(MSBlocks.BROWN_STONE_BRICKS)).save(recipeBuilder);
		ShapedRecipeBuilder.shaped(MSBlocks.GREEN_STONE_SLAB, 6).define('#', MSBlocks.GREEN_STONE).pattern("###").unlockedBy("has_green_stone", has(MSBlocks.GREEN_STONE)).save(recipeBuilder);
		ShapedRecipeBuilder.shaped(MSBlocks.GREEN_STONE_BRICK_SLAB, 6).define('#', MSBlocks.GREEN_STONE_BRICKS).pattern("###").unlockedBy("has_green_stone_bricks", has(MSBlocks.GREEN_STONE_BRICKS)).save(recipeBuilder);
		ShapedRecipeBuilder.shaped(MSBlocks.FLOWERY_MOSSY_STONE_BRICK_SLAB, 6).define('#', MSBlocks.FLOWERY_MOSSY_STONE_BRICKS).pattern("###").unlockedBy("has_flowery_mossy_stone_bricks", has(MSBlocks.FLOWERY_MOSSY_STONE_BRICKS)).save(recipeBuilder);
		ShapedRecipeBuilder.shaped(MSBlocks.SANDSTONE_COLUMN, 3).define('#', Blocks.SANDSTONE).pattern("#").pattern("#").pattern("#").unlockedBy("has_sandstone", has(Items.SANDSTONE)).save(recipeBuilder);
		ShapedRecipeBuilder.shaped(MSBlocks.CHISELED_SANDSTONE_COLUMN, 3).define('#', Blocks.CHISELED_SANDSTONE).pattern("#").pattern("#").pattern("#").unlockedBy("has_chiseled_sandstone", has(Items.CHISELED_SANDSTONE)).save(recipeBuilder);
		ShapedRecipeBuilder.shaped(MSBlocks.RED_SANDSTONE_COLUMN, 3).define('#', Blocks.RED_SANDSTONE).pattern("#").pattern("#").pattern("#").unlockedBy("has_red_sandstone", has(Items.RED_SANDSTONE)).save(recipeBuilder);
		ShapedRecipeBuilder.shaped(MSBlocks.CHISELED_RED_SANDSTONE_COLUMN, 3).define('#', Blocks.CHISELED_RED_SANDSTONE).pattern("#").pattern("#").pattern("#").unlockedBy("has_chiseled_red_sandstone", has(Items.CHISELED_RED_SANDSTONE)).save(recipeBuilder);
		
		ShapedRecipeBuilder.shaped(MSBlocks.SPIKES).define('#', Items.IRON_INGOT).define('n', Items.IRON_NUGGET).pattern("n n").pattern("# #").pattern("###").unlockedBy("has_iron_ingot", has(Items.IRON_INGOT)).save(recipeBuilder);
		ShapedRecipeBuilder.shaped(MSBlocks.RETRACTABLE_SPIKES).define('#', Items.IRON_INGOT).define('p', Items.PISTON).define('s', MSBlocks.SPIKES).define('w', ItemTags.WOODEN_PRESSURE_PLATES).pattern(" s ").pattern("#w#").pattern("#p#").unlockedBy("has_spikes", has(MSBlocks.SPIKES)).save(recipeBuilder);
		
		ShapedRecipeBuilder.shaped(MSBlocks.GLOWING_WOOD, 3).define('#', MSBlocks.GLOWING_LOG).pattern("##").pattern("##").group("bark").unlockedBy("has_log", has(MSTags.Items.GLOWING_LOGS)).save(recipeBuilder);
		ShapelessRecipeBuilder.shapeless(MSBlocks.GLOWING_PLANKS, 4).requires(MSTags.Items.GLOWING_LOGS).group("planks").unlockedBy("has_log", has(MSTags.Items.GLOWING_LOGS)).save(recipeBuilder);
		ShapedRecipeBuilder.shaped(MSBlocks.FROST_WOOD, 3).define('#', MSBlocks.FROST_LOG).pattern("##").pattern("##").group("bark").unlockedBy("has_log", has(MSTags.Items.FROST_LOGS)).save(recipeBuilder);
		ShapelessRecipeBuilder.shapeless(MSBlocks.FROST_PLANKS, 4).requires(MSTags.Items.FROST_LOGS).group("planks").unlockedBy("has_log", has(MSTags.Items.FROST_LOGS)).save(recipeBuilder);
		ShapedRecipeBuilder.shaped(MSBlocks.RAINBOW_WOOD, 3).define('#', MSBlocks.RAINBOW_LOG).pattern("##").pattern("##").group("bark").unlockedBy("has_log", has(MSTags.Items.RAINBOW_LOGS)).save(recipeBuilder);
		ShapelessRecipeBuilder.shapeless(MSBlocks.RAINBOW_PLANKS, 4).requires(MSTags.Items.RAINBOW_LOGS).group("planks").unlockedBy("has_log", has(MSTags.Items.RAINBOW_LOGS)).save(recipeBuilder);
		ShapedRecipeBuilder.shaped(MSBlocks.END_WOOD, 3).define('#', MSBlocks.END_LOG).pattern("##").pattern("##").group("bark").unlockedBy("has_log", has(MSTags.Items.END_LOGS)).save(recipeBuilder);
		ShapelessRecipeBuilder.shapeless(MSBlocks.END_PLANKS, 4).requires(MSTags.Items.END_LOGS).group("planks").unlockedBy("has_log", has(MSTags.Items.END_LOGS)).save(recipeBuilder);
		ShapedRecipeBuilder.shaped(MSBlocks.VINE_WOOD, 3).define('#', MSBlocks.VINE_LOG).pattern("##").pattern("##").group("bark").unlockedBy("has_log", has(MSTags.Items.VINE_LOGS)).save(recipeBuilder);
		ShapedRecipeBuilder.shaped(MSBlocks.FLOWERY_VINE_WOOD, 3).define('#', MSBlocks.FLOWERY_VINE_LOG).pattern("##").pattern("##").group("bark").unlockedBy("has_log", has(MSTags.Items.FLOWERY_VINE_LOGS)).save(recipeBuilder);
		ShapedRecipeBuilder.shaped(MSBlocks.DEAD_WOOD, 3).define('#', MSBlocks.DEAD_LOG).pattern("##").pattern("##").group("bark").unlockedBy("has_log", has(MSTags.Items.DEAD_LOGS)).save(recipeBuilder);
		ShapelessRecipeBuilder.shapeless(MSBlocks.DEAD_PLANKS, 4).requires(MSTags.Items.DEAD_LOGS).group("planks").unlockedBy("has_log", has(MSTags.Items.DEAD_LOGS)).save(recipeBuilder);
		ShapedRecipeBuilder.shaped(MSBlocks.PETRIFIED_WOOD, 3).define('#', MSBlocks.PETRIFIED_LOG).pattern("##").pattern("##").group("bark").unlockedBy("has_log", has(MSTags.Items.PETRIFIED_LOGS)).save(recipeBuilder);
		ShapelessRecipeBuilder.shapeless(Blocks.OAK_PLANKS).requires(MSBlocks.TREATED_PLANKS).requires(Items.WATER_BUCKET).group("planks").unlockedBy("has_log", has(MSBlocks.TREATED_PLANKS)).save(recipeBuilder, new ResourceLocation(Minestuck.MOD_ID, "oak_planks_from_treated_planks"));
		ShapedRecipeBuilder.shaped(MSBlocks.RAINBOW_PLANKS_STAIRS, 4).define('#', MSBlocks.RAINBOW_PLANKS).pattern("#  ").pattern("## ").pattern("###").group("wooden_stairs").unlockedBy("has_planks", has(MSBlocks.RAINBOW_PLANKS)).save(recipeBuilder);
		ShapedRecipeBuilder.shaped(MSBlocks.END_PLANKS_STAIRS, 4).define('#', MSBlocks.END_PLANKS).pattern("#  ").pattern("## ").pattern("###").group("wooden_stairs").unlockedBy("has_planks", has(MSBlocks.END_PLANKS)).save(recipeBuilder);
		ShapedRecipeBuilder.shaped(MSBlocks.DEAD_PLANKS_STAIRS, 4).define('#', MSBlocks.DEAD_PLANKS).pattern("#  ").pattern("## ").pattern("###").group("wooden_stairs").unlockedBy("has_planks", has(MSBlocks.DEAD_PLANKS)).save(recipeBuilder);
		ShapedRecipeBuilder.shaped(MSBlocks.TREATED_PLANKS_STAIRS, 4).define('#', MSBlocks.TREATED_PLANKS).pattern("#  ").pattern("## ").pattern("###").group("wooden_stairs").unlockedBy("has_planks", has(MSBlocks.TREATED_PLANKS)).save(recipeBuilder);
		ShapedRecipeBuilder.shaped(MSBlocks.RAINBOW_PLANKS_SLAB, 6).define('#', MSBlocks.RAINBOW_PLANKS).pattern("###").group("wooden_slab").unlockedBy("has_planks", has(MSBlocks.RAINBOW_PLANKS)).save(recipeBuilder);
		ShapedRecipeBuilder.shaped(MSBlocks.END_PLANKS_SLAB, 6).define('#', MSBlocks.END_PLANKS).pattern("###").group("wooden_slab").unlockedBy("has_planks", has(MSBlocks.END_PLANKS)).save(recipeBuilder);
		ShapedRecipeBuilder.shaped(MSBlocks.DEAD_PLANKS_SLAB, 6).define('#', MSBlocks.DEAD_PLANKS).pattern("###").group("wooden_slab").unlockedBy("has_planks", has(MSBlocks.DEAD_PLANKS)).save(recipeBuilder);
		ShapedRecipeBuilder.shaped(MSBlocks.TREATED_PLANKS_SLAB, 6).define('#', MSBlocks.TREATED_PLANKS).pattern("###").group("wooden_slab").unlockedBy("has_planks", has(MSBlocks.TREATED_PLANKS)).save(recipeBuilder);
		
		ShapelessRecipeBuilder.shapeless(MSBlocks.COMPUTER).requires(MSItems.ENERGY_CORE).requires(MSItems.COMPUTER_PARTS).unlockedBy("has_computer_parts", has(MSItems.COMPUTER_PARTS)).requires(Items.QUARTZ).requires(Items.REDSTONE).requires(MSItems.RAW_CRUXITE).save(recipeBuilder);
		ShapedRecipeBuilder.shaped(MSBlocks.URANIUM_COOKER).define('e', MSItems.ENERGY_CORE).define('i', Tags.Items.INGOTS_IRON).define('F', Blocks.FURNACE).pattern("iii").pattern("iFi").pattern("iei").unlockedBy("has_energy_core", has(MSItems.ENERGY_CORE)).save(recipeBuilder);
		
		ShapelessRecipeBuilder.shapeless(Blocks.SPRUCE_PLANKS).requires(MSBlocks.WOODEN_CACTUS).group("planks").unlockedBy("has_cactus", has(MSBlocks.WOODEN_CACTUS)).save(recipeBuilder, new ResourceLocation(Minestuck.MOD_ID, "spruce_planks_from_wooden_cactus"));
		ShapedRecipeBuilder.shaped(MSBlocks.APPLE_CAKE).define('m', Items.MILK_BUCKET).define('a', Items.APPLE).define('w', Items.WHEAT).define('e', Items.EGG).pattern("mmm").pattern("aea").pattern("www").unlockedBy("has_egg", has(Items.EGG)).save(recipeBuilder);
		ShapedRecipeBuilder.shaped(MSBlocks.CHESSBOARD).define('W', Blocks.WHITE_TERRACOTTA).define('B', Blocks.BLACK_TERRACOTTA).pattern("WBW").pattern("BWB").pattern("WBW").group("chessboard").unlockedBy("has_white_terracotta", has(Blocks.WHITE_TERRACOTTA)).unlockedBy("has_black_terracotta", has(Blocks.BLACK_TERRACOTTA)).save(recipeBuilder, new ResourceLocation(Minestuck.MOD_ID, "chessboard_white"));
		ShapedRecipeBuilder.shaped(MSBlocks.CHESSBOARD).define('W', Blocks.WHITE_TERRACOTTA).define('B', Blocks.BLACK_TERRACOTTA).pattern("BWB").pattern("WBW").pattern("BWB").group("chessboard").unlockedBy("has_white_terracotta", has(Blocks.WHITE_TERRACOTTA)).unlockedBy("has_black_terracotta", has(Blocks.BLACK_TERRACOTTA)).save(recipeBuilder, new ResourceLocation(Minestuck.MOD_ID, "chessboard_black"));
		ShapedRecipeBuilder.shaped(MSItems.CLOTHES_IRON).define('s', Tags.Items.STONE).define('i', Tags.Items.INGOTS_IRON).define('r', Tags.Items.RODS_WOODEN).pattern(" rr").pattern("sss").pattern("iii").unlockedBy("has_stick", has(Tags.Items.RODS_WOODEN)).unlockedBy("has_iron_ingot", has(Tags.Items.INGOTS_IRON)).save(recipeBuilder);
		ShapedRecipeBuilder.shaped(MSItems.HORN).define('w', Items.WHITE_WOOL).define('i', Tags.Items.INGOTS_IRON).define('r', Tags.Items.RODS_WOODEN).pattern("  i").pattern(" r ").pattern("w  ").unlockedBy("has_stick", has(Tags.Items.RODS_WOODEN)).unlockedBy("has_iron_ingot", has(Tags.Items.INGOTS_IRON)).unlockedBy("has_white_wool", has(Items.WHITE_WOOL)).save(recipeBuilder);
		ShapelessRecipeBuilder.shapeless(MSItems.BI_DYE, 2).requires(Items.BLUE_DYE).unlockedBy("has_blue_dye", has(Items.BLUE_DYE)).requires(Items.PINK_DYE).unlockedBy("has_pink_dye", has(Items.PINK_DYE)).requires(Items.PURPLE_DYE).unlockedBy("has_purple_dye", has(Items.PURPLE_DYE)).save(recipeBuilder);
		ShapedRecipeBuilder.shaped(MSItems.LIP_BALM).define('n', Items.IRON_NUGGET).define('w', Items.HONEYCOMB).pattern("nwn").pattern(" n ").unlockedBy("has_honeycomb", has(Items.HONEYCOMB)).unlockedBy("has_iron_nugget", has(Items.IRON_NUGGET)).save(recipeBuilder);
		
		ShapedRecipeBuilder.shaped(MSItems.CLAW_HAMMER).define('i', Tags.Items.INGOTS_IRON).define('s', Tags.Items.RODS_WOODEN).pattern(" ii").pattern("is ").pattern(" s ").unlockedBy("has_iron_ingot", has(Tags.Items.INGOTS_IRON)).save(recipeBuilder);
		ShapedRecipeBuilder.shaped(MSItems.SLEDGE_HAMMER).define('i', Tags.Items.INGOTS_IRON).define('s', Tags.Items.RODS_WOODEN).define('S', Tags.Items.STONE).pattern("iSi").pattern(" s ").pattern(" s ").unlockedBy("has_iron_ingot", has(Tags.Items.INGOTS_IRON)).save(recipeBuilder);
		ShapedRecipeBuilder.shaped(MSItems.KATANA).define('i', Tags.Items.INGOTS_IRON).define('s', Tags.Items.RODS_WOODEN).pattern("  i").pattern(" i ").pattern("s  ").unlockedBy("has_iron_ingot", has(Tags.Items.INGOTS_IRON)).save(recipeBuilder);
		ShapedRecipeBuilder.shaped(MSItems.DAGGER).define('i', Tags.Items.INGOTS_IRON).define('s', Tags.Items.RODS_WOODEN).pattern("s").pattern("i").pattern("i").unlockedBy("has_iron_ingot", has(Tags.Items.INGOTS_IRON)).save(recipeBuilder);
		ShapedRecipeBuilder.shaped(MSItems.MACUAHUITL).define('P', ItemTags.PLANKS).define('s', Tags.Items.RODS_WOODEN).define('O', Items.OBSIDIAN).pattern("OPO").pattern("OPO").pattern(" s ").unlockedBy("has_stick", has(Tags.Items.RODS_WOODEN)).save(recipeBuilder);
		ShapedRecipeBuilder.shaped(MSItems.OBSIDIAN_AXE_KNIFE).define('s', Tags.Items.RODS_WOODEN).define('O', Items.OBSIDIAN).pattern("  O").pattern(" Os").pattern("Os ").unlockedBy("has_stick", has(Tags.Items.RODS_WOODEN)).save(recipeBuilder);
		ShapedRecipeBuilder.shaped(MSItems.SICKLE).define('i', Tags.Items.INGOTS_IRON).define('s', Tags.Items.RODS_WOODEN).pattern("ii ").pattern("  i").pattern(" s ").unlockedBy("has_iron_ingot", has(Tags.Items.INGOTS_IRON)).save(recipeBuilder);
		ShapedRecipeBuilder.shaped(MSItems.DEUCE_CLUB).define('P', ItemTags.PLANKS).define('s', Tags.Items.RODS_WOODEN).pattern("  P").pattern(" s ").pattern("s  ").unlockedBy("has_stick", has(Tags.Items.RODS_WOODEN)).save(recipeBuilder);
		ShapedRecipeBuilder.shaped(MSItems.CANE).define('s', Tags.Items.RODS_WOODEN).pattern("  s").pattern(" s ").pattern("s  ").unlockedBy("has_stick", has(Tags.Items.RODS_WOODEN)).save(recipeBuilder);
		ShapedRecipeBuilder.shaped(MSItems.BEAR_POKING_STICK).define('s', Tags.Items.RODS_WOODEN).define('l', Tags.Items.LEATHER).pattern("  s").pattern(" s ").pattern("l  ").unlockedBy("has_stick", has(Tags.Items.RODS_WOODEN)).save(recipeBuilder);
		ShapedRecipeBuilder.shaped(MSItems.UMBRELLA).define('s', Tags.Items.RODS_WOODEN).define('w', Items.BLACK_WOOL).pattern(" w ").pattern("wsw").pattern(" s ").unlockedBy("has_stick", has(Tags.Items.RODS_WOODEN)).save(recipeBuilder);
		ShapedRecipeBuilder.shaped(MSItems.FAN).define('s', Tags.Items.RODS_WOODEN).define('p', Items.PAPER).pattern("  p").pattern(" pp").pattern("pps").unlockedBy("has_stick", has(Tags.Items.RODS_WOODEN)).save(recipeBuilder);
		ShapedRecipeBuilder.shaped(MSItems.IRON_CANE).define('i', Tags.Items.INGOTS_IRON).define('c', MSItems.CANE).pattern("  i").pattern(" c ").pattern("i  ").unlockedBy("has_iron_ingot", has(Tags.Items.INGOTS_IRON)).save(recipeBuilder);
		ShapedRecipeBuilder.shaped(MSItems.WOODEN_SPOON).define('s', Tags.Items.RODS_WOODEN).define('b', Items.BOWL).pattern("b").pattern("s").pattern("s").unlockedBy("has_stick", has(Tags.Items.RODS_WOODEN)).save(recipeBuilder);
		ShapedRecipeBuilder.shaped(MSItems.FORK).define('S', Tags.Items.STONE).pattern("S S").pattern(" S ").pattern(" S ").unlockedBy("has_stone", has(Tags.Items.STONE)).save(recipeBuilder);
		ShapedRecipeBuilder.shaped(MSItems.POINTY_STICK, 1).define('s', Tags.Items.RODS_WOODEN).pattern("s").pattern("s").pattern("s").unlockedBy("has_stick", has(Tags.Items.RODS_WOODEN)).save(recipeBuilder);
		ShapedRecipeBuilder.shaped(MSItems.KNITTING_NEEDLE, 2).define('i', Items.IRON_INGOT).define('n', Items.IRON_NUGGET).pattern("  n").pattern(" i ").pattern("i  ").unlockedBy("has_iron_ingot", has(Items.IRON_INGOT)).save(recipeBuilder);
		ShapedRecipeBuilder.shaped(MSItems.SHURIKEN, 2).define('i', Items.IRON_INGOT).define('n', Items.IRON_NUGGET).pattern(" i ").pattern("ini").pattern(" i ").unlockedBy("has_iron_ingot", has(Items.IRON_INGOT)).save(recipeBuilder);
		
		ShapedRecipeBuilder.shaped(MSItems.ENERGY_CORE).define('u', ExtraForgeTags.Items.URANIUM_CHUNKS).define('c', MSItems.RAW_CRUXITE).pattern("cuc").pattern("ucu").pattern("cuc").unlockedBy("has_raw_uranium", has(MSItems.RAW_URANIUM)).save(recipeBuilder);
		ShapedRecipeBuilder.shaped(MSItems.PLUTONIUM_CORE).define('e', MSItems.ENERGY_CORE).define('i', Items.IRON_INGOT).define('n', Items.IRON_NUGGET).define('p', Items.ENDER_PEARL).pattern("nin").pattern("pep").pattern("nin").unlockedBy("has_energy_core", has(MSItems.ENERGY_CORE)).save(recipeBuilder);
		ShapedRecipeBuilder.shaped(MSItems.TEMPLE_SCANNER).define('e', MSItems.ENERGY_CORE).define('c', MSItems.RAW_CRUXITE).define('i', Items.IRON_INGOT).define('r', Items.REDSTONE).define('g', Items.GLASS_PANE).pattern("igi").pattern("iei").pattern("crc").unlockedBy("has_energy_core", has(MSItems.ENERGY_CORE)).save(recipeBuilder);
		ShapedRecipeBuilder.shaped(MSItems.SERVER_DISK).define('d', MSItems.BLANK_DISK).define('c', MSItems.SBURB_CODE).pattern(" c ").pattern("cdc").pattern(" c ").unlockedBy("has_sburb_code", has(MSItems.SBURB_CODE)).save(recipeBuilder);
		ShapedRecipeBuilder.shaped(MSItems.CLIENT_DISK).define('d', MSItems.BLANK_DISK).define('c', MSItems.SBURB_CODE).pattern("c c").pattern(" d ").pattern("c c").unlockedBy("has_sburb_code", has(MSItems.SBURB_CODE)).save(recipeBuilder);
		ShapedRecipeBuilder.shaped(MSItems.MAILBOX).define('c', Ingredient.of(Tags.Items.CHESTS)).define('r', Ingredient.of(Tags.Items.RODS)).define('i', Items.IRON_INGOT).pattern("ici").pattern(" r ").pattern(" r ").unlockedBy("has_chest", has(Tags.Items.CHESTS)).save(recipeBuilder);
		ShapedRecipeBuilder.shaped(MSItems.CAPTCHA_CARD).define('p', Items.PAPER).define('c', MSItems.RAW_CRUXITE).pattern("ppp").pattern("pcp").pattern("ppp").unlockedBy("has_raw_cruxite", has(MSItems.RAW_CRUXITE)).save(recipeBuilder);
		//TODO custom ingredient type to disallow punched cards or cards with items to be used here
		NonMirroredRecipeBuilder.nonMirroredRecipe(MSItems.STACK_MODUS_CARD).define('a', Ingredient.of(MSItems.CAPTCHA_CARD)).define('c', MSItems.RAW_CRUXITE).define('C', MSBlocks.CRUXITE_BLOCK).pattern("Cac").unlockedBy("has_card", has(MSItems.CAPTCHA_CARD)).save(recipeBuilder);
		NonMirroredRecipeBuilder.nonMirroredRecipe(MSItems.QUEUE_MODUS_CARD).define('a', Ingredient.of(MSItems.CAPTCHA_CARD)).define('c', MSItems.RAW_CRUXITE).define('C', MSBlocks.CRUXITE_BLOCK).pattern("caC").unlockedBy("has_card", has(MSItems.CAPTCHA_CARD)).save(recipeBuilder);
		
		ShapelessRecipeBuilder.shapeless(MSItems.BUG_ON_A_STICK, 3).requires(Ingredient.of(Tags.Items.RODS_WOODEN), 3).requires(MSItems.JAR_OF_BUGS).unlockedBy("has_jag_of_bugs", has(MSItems.JAR_OF_BUGS)).save(recipeBuilder);
		ShapedRecipeBuilder.shaped(MSItems.GRUB_SAUCE,3).define('p', Items.PAPER).define('b', MSTags.Items.BUGS).pattern("bbb").pattern("p p").pattern(" p ").unlockedBy("has_bugs", has(MSTags.Items.BUGS)).save(recipeBuilder);
		ShapelessRecipeBuilder.shapeless(MSItems.BUG_MAC, 2).requires(Items.BREAD, 1).requires(MSTags.Items.BUGS).requires(MSTags.Items.BUGS).requires(MSItems.FRENCH_FRY, 4).requires(MSItems.GRUB_SAUCE, 1).unlockedBy("has_grub_sauce", has(MSItems.GRUB_SAUCE)).save(recipeBuilder);
		ShapelessRecipeBuilder.shapeless(MSItems.SALAD).requires(Items.BOWL).requires(ItemTags.LEAVES).unlockedBy("has_bowl", has(Items.BOWL)).save(recipeBuilder);
		
		ShapelessRecipeBuilder.shapeless(MSBlocks.TRANS_PORTALIZER).requires(MSBlocks.TRANSPORTALIZER).requires(Items.PINK_DYE).requires(Items.LIGHT_BLUE_DYE).unlockedBy("has_transportalizer", has(MSBlocks.TRANSPORTALIZER)).save(recipeBuilder);
		ShapedRecipeBuilder.shaped(MSBlocks.WIRELESS_REDSTONE_TRANSMITTER).define('e', MSItems.PLUTONIUM_CORE).define('i', Items.IRON_INGOT).define('c', Items.COMPARATOR).define('q', Items.QUARTZ).pattern("cqc").pattern("iei").pattern("iii").unlockedBy("has_plutonium_core", has(MSItems.PLUTONIUM_CORE)).save(recipeBuilder);
		ShapedRecipeBuilder.shaped(MSBlocks.WIRELESS_REDSTONE_RECEIVER).define('c', Items.COMPARATOR).define('i', Items.IRON_INGOT).define('r', Items.REDSTONE).define('q', Items.QUARTZ).define('s', Tags.Items.STONE).pattern("rqr").pattern("scs").pattern("ici").unlockedBy("has_plutonium_core", has(MSItems.PLUTONIUM_CORE)).save(recipeBuilder);
		ShapedRecipeBuilder.shaped(MSBlocks.SOLID_SWITCH, 2).define('g', Items.GLOWSTONE).define('i', Items.IRON_INGOT).define('r', Items.REDSTONE).pattern("iri").pattern("rgr").pattern("iri").unlockedBy("has_glowstone", has(Items.GLOWSTONE)).save(recipeBuilder);
		ShapelessRecipeBuilder.shapeless(MSBlocks.VARIABLE_SOLID_SWITCH).requires(MSBlocks.SOLID_SWITCH).requires(Items.COMPARATOR).unlockedBy("has_solid_switch", has(MSBlocks.SOLID_SWITCH)).save(recipeBuilder);
		ShapelessRecipeBuilder.shapeless(MSBlocks.ONE_SECOND_INTERVAL_TIMED_SOLID_SWITCH).requires(MSBlocks.SOLID_SWITCH).requires(Items.CLOCK).requires(Items.REPEATER).unlockedBy("has_solid_switch", has(MSBlocks.SOLID_SWITCH)).save(recipeBuilder);
		ShapelessRecipeBuilder.shapeless(MSBlocks.TWO_SECOND_INTERVAL_TIMED_SOLID_SWITCH).requires(MSBlocks.SOLID_SWITCH).requires(Items.CLOCK).requires(Items.REPEATER).requires(Items.REPEATER).unlockedBy("has_solid_switch", has(MSBlocks.SOLID_SWITCH)).save(recipeBuilder);
		ShapedRecipeBuilder.shaped(MSBlocks.ITEM_MAGNET).define('e', MSItems.PLUTONIUM_CORE).define('i', Items.IRON_INGOT).define('r', Items.REDSTONE).define('g', Items.GLASS_PANE).pattern("iri").pattern("geg").pattern("iri").unlockedBy("has_plutonium_core", has(MSItems.PLUTONIUM_CORE)).save(recipeBuilder);
		ShapedRecipeBuilder.shaped(MSBlocks.REDSTONE_CLOCK).define('c', Items.CLOCK).define('i', Items.IRON_INGOT).define('r', Items.REPEATER).define('q', Items.QUARTZ).define('P', ItemTags.PLANKS).pattern("PiP").pattern("rcr").pattern("PqP").unlockedBy("has_clock", has(Items.CLOCK)).save(recipeBuilder);
		
		CookingRecipeBuilder.smelting(Ingredient.of(MSBlocks.COARSE_STONE_BRICKS), MSBlocks.CRACKED_COARSE_STONE_BRICKS, 0.1f, 200).unlockedBy("has_coarse_stone_bricks", has(MSBlocks.COARSE_STONE_BRICKS)).save(recipeBuilder);
		CookingRecipeBuilder.smelting(Ingredient.of(MSBlocks.SHADE_STONE), MSBlocks.SMOOTH_SHADE_STONE, 0.1f, 200).unlockedBy("has_shade_stone", has(MSBlocks.SHADE_STONE)).save(recipeBuilder);
		CookingRecipeBuilder.smelting(Ingredient.of(MSBlocks.SHADE_BRICKS), MSBlocks.CRACKED_SHADE_BRICKS, 0.1f, 200).unlockedBy("has_shade_bricks", has(MSBlocks.SHADE_BRICKS)).save(recipeBuilder);
		CookingRecipeBuilder.smelting(Ingredient.of(MSBlocks.FROST_BRICKS), MSBlocks.CRACKED_FROST_BRICKS, 0.1f, 100).unlockedBy("has_frost_bricks", has(MSBlocks.FROST_BRICKS)).save(recipeBuilder);
		CookingRecipeBuilder.smelting(Ingredient.of(MSBlocks.MYCELIUM_COBBLESTONE), MSBlocks.MYCELIUM_STONE, 0.1f, 200).unlockedBy("has_mycelium_cobblestone", has(MSBlocks.MYCELIUM_COBBLESTONE)).save(recipeBuilder);
		CookingRecipeBuilder.smelting(Ingredient.of(MSBlocks.MYCELIUM_STONE), MSBlocks.POLISHED_MYCELIUM_STONE, 0.1f, 200).unlockedBy("has_mycelium_stone", has(MSBlocks.MYCELIUM_STONE)).save(recipeBuilder);
		CookingRecipeBuilder.smelting(Ingredient.of(MSBlocks.MYCELIUM_BRICKS), MSBlocks.CRACKED_MYCELIUM_BRICKS, 0.1f, 200).unlockedBy("has_mycelium_bricks", has(MSBlocks.MYCELIUM_BRICKS)).save(recipeBuilder);
		CookingRecipeBuilder.smelting(Ingredient.of(MSBlocks.BLACK_COBBLESTONE), MSBlocks.BLACK_STONE, 0.1f, 200).unlockedBy("has_black_cobblestone", has(MSBlocks.BLACK_COBBLESTONE)).save(recipeBuilder);
		CookingRecipeBuilder.smelting(Ingredient.of(MSBlocks.BLACK_STONE), MSBlocks.POLISHED_BLACK_STONE, 0.1f, 200).unlockedBy("has_black_stone", has(MSBlocks.BLACK_STONE)).save(recipeBuilder);
		CookingRecipeBuilder.smelting(Ingredient.of(MSBlocks.BLACK_STONE_BRICKS), MSBlocks.CRACKED_BLACK_STONE_BRICKS, 0.1f, 200).unlockedBy("has_black_stone_bricks", has(MSBlocks.BLACK_STONE_BRICKS)).save(recipeBuilder);
		CookingRecipeBuilder.smelting(Ingredient.of(MSBlocks.PINK_STONE), MSBlocks.POLISHED_PINK_STONE, 0.1F, 200).unlockedBy("has_pink_stone", has(MSBlocks.PINK_STONE)).save(recipeBuilder);
		CookingRecipeBuilder.smelting(Ingredient.of(MSBlocks.PINK_STONE_BRICKS), MSBlocks.CRACKED_PINK_STONE_BRICKS, 0.1F, 200).unlockedBy("has_pink_stone_bricks", has(MSBlocks.PINK_STONE_BRICKS)).save(recipeBuilder);
		CookingRecipeBuilder.smelting(Ingredient.of(MSBlocks.BROWN_STONE), MSBlocks.POLISHED_BROWN_STONE, 0.1f, 200).unlockedBy("has_brown_stone", has(MSBlocks.BROWN_STONE)).save(recipeBuilder);
		CookingRecipeBuilder.smelting(Ingredient.of(MSBlocks.BROWN_STONE_BRICKS), MSBlocks.CRACKED_BROWN_STONE_BRICKS, 0.1F, 200).unlockedBy("has_brown_stone_bricks", has(MSBlocks.BROWN_STONE_BRICKS)).save(recipeBuilder);
		CookingRecipeBuilder.smelting(Ingredient.of(MSItems.CAKE_MIX), Blocks.CAKE, 0.0F, 200).unlockedBy("has_cake_mix", has(MSItems.CAKE_MIX)).save(recipeBuilder, new ResourceLocation (Minestuck.MOD_ID, "cake_from_mix"));
		
		CookingRecipeBuilder.smelting(Ingredient.of(MSTags.Items.CRUXITE_ORES), MSItems.RAW_CRUXITE, 0.2F, 200).unlockedBy("has_cruxite_ore", has(MSTags.Items.CRUXITE_ORES)).save(recipeBuilder, new ResourceLocation(Minestuck.MOD_ID, "raw_cruxite_from_smelting"));
		CookingRecipeBuilder.blasting(Ingredient.of(MSTags.Items.CRUXITE_ORES), MSItems.RAW_CRUXITE, 0.2F, 100).unlockedBy("has_cruxite_ore", has(MSTags.Items.CRUXITE_ORES)).save(recipeBuilder, new ResourceLocation(Minestuck.MOD_ID, "raw_cruxite_from_blasting"));
		CookingRecipeBuilder.smelting(Ingredient.of(MSTags.Items.URANIUM_ORES), MSItems.RAW_URANIUM, 0.2F, 200).unlockedBy("has_uranium_ore", has(MSTags.Items.URANIUM_ORES)).save(recipeBuilder, new ResourceLocation(Minestuck.MOD_ID, "raw_uranium_from_smelting"));
		CookingRecipeBuilder.blasting(Ingredient.of(MSTags.Items.URANIUM_ORES), MSItems.RAW_URANIUM, 0.2F, 100).unlockedBy("has_uranium_ore", has(MSTags.Items.URANIUM_ORES)).save(recipeBuilder, new ResourceLocation(Minestuck.MOD_ID, "raw_uranium_from_blasting"));
		CookingRecipeBuilder.smelting(Ingredient.of(MSTags.Items.COAL_ORES), Items.COAL, 0.1F, 200).unlockedBy("has_coal_ore", has(MSTags.Items.COAL_ORES)).save(recipeBuilder, new ResourceLocation(Minestuck.MOD_ID, "coal_from_smelting"));
		CookingRecipeBuilder.blasting(Ingredient.of(MSTags.Items.COAL_ORES), Items.COAL, 0.1F, 100).unlockedBy("has_coal_ore", has(MSTags.Items.COAL_ORES)).save(recipeBuilder, new ResourceLocation(Minestuck.MOD_ID, "coal_from_blasting"));
		CookingRecipeBuilder.smelting(Ingredient.of(MSTags.Items.IRON_ORES), Items.IRON_INGOT, 0.7F, 200).unlockedBy("has_iron_ore", has(MSTags.Items.IRON_ORES)).save(recipeBuilder, new ResourceLocation(Minestuck.MOD_ID, "iron_ingot_from_smelting"));
		CookingRecipeBuilder.blasting(Ingredient.of(MSTags.Items.IRON_ORES), Items.IRON_INGOT, 0.7F, 100).unlockedBy("has_iron_ore", has(MSTags.Items.IRON_ORES)).save(recipeBuilder, new ResourceLocation(Minestuck.MOD_ID, "iron_ingot_from_blasting"));
		CookingRecipeBuilder.smelting(Ingredient.of(MSTags.Items.GOLD_ORES), Items.GOLD_INGOT, 1.0F, 200).unlockedBy("has_gold_ore", has(MSTags.Items.GOLD_ORES)).save(recipeBuilder, new ResourceLocation(Minestuck.MOD_ID, "gold_ingot_from_smelting"));
		CookingRecipeBuilder.blasting(Ingredient.of(MSTags.Items.GOLD_ORES), Items.GOLD_INGOT, 1.0F, 100).unlockedBy("has_gold_ore", has(MSTags.Items.GOLD_ORES)).save(recipeBuilder, new ResourceLocation(Minestuck.MOD_ID, "gold_ingot_from_blasting"));
		CookingRecipeBuilder.smelting(Ingredient.of(MSTags.Items.REDSTONE_ORES), Items.REDSTONE, 0.7F, 200).unlockedBy("has_redstone_ore", has(MSTags.Items.REDSTONE_ORES)).save(recipeBuilder, new ResourceLocation(Minestuck.MOD_ID, "redstone_from_smelting"));
		CookingRecipeBuilder.blasting(Ingredient.of(MSTags.Items.REDSTONE_ORES), Items.REDSTONE, 0.7F, 100).unlockedBy("has_redstone_ore", has(MSTags.Items.REDSTONE_ORES)).save(recipeBuilder, new ResourceLocation(Minestuck.MOD_ID, "redstone_from_blasting"));
		CookingRecipeBuilder.smelting(Ingredient.of(MSTags.Items.QUARTZ_ORES), Items.QUARTZ, 0.2F, 200).unlockedBy("has_quartz_ore", has(MSTags.Items.QUARTZ_ORES)).save(recipeBuilder, new ResourceLocation(Minestuck.MOD_ID, "quartz_from_smelting"));
		CookingRecipeBuilder.blasting(Ingredient.of(MSTags.Items.QUARTZ_ORES), Items.QUARTZ, 0.2F, 100).unlockedBy("has_quartz_ore", has(MSTags.Items.QUARTZ_ORES)).save(recipeBuilder, new ResourceLocation(Minestuck.MOD_ID, "quartz_from_blasting"));
		CookingRecipeBuilder.smelting(Ingredient.of(MSTags.Items.LAPIS_ORES), Items.LAPIS_LAZULI, 0.2F, 200).unlockedBy("has_lapis_ore", has(MSTags.Items.LAPIS_ORES)).save(recipeBuilder, new ResourceLocation(Minestuck.MOD_ID, "lapis_lazuli_from_smelting"));
		CookingRecipeBuilder.blasting(Ingredient.of(MSTags.Items.LAPIS_ORES), Items.LAPIS_LAZULI, 0.2F, 100).unlockedBy("has_lapis_ore", has(MSTags.Items.LAPIS_ORES)).save(recipeBuilder, new ResourceLocation(Minestuck.MOD_ID, "lapis_lazuli_from_blasting"));
		CookingRecipeBuilder.smelting(Ingredient.of(MSTags.Items.DIAMOND_ORES), Items.DIAMOND, 1.0F, 200).unlockedBy("has_diamond_ore", has(MSTags.Items.DIAMOND_ORES)).save(recipeBuilder, new ResourceLocation(Minestuck.MOD_ID, "diamond_from_smelting"));
		CookingRecipeBuilder.blasting(Ingredient.of(MSTags.Items.DIAMOND_ORES), Items.DIAMOND, 1.0F, 100).unlockedBy("has_diamond_ore", has(MSTags.Items.DIAMOND_ORES)).save(recipeBuilder, new ResourceLocation(Minestuck.MOD_ID, "diamond_from_blasting"));
		
		CookingRecipeBuilder.smelting(Ingredient.of(MSBlocks.CRUXITE_DOWEL), MSItems.RAW_CRUXITE, 0.0F, 200).unlockedBy("has_cruxite_dowel", has(MSBlocks.CRUXITE_DOWEL)).save(recipeBuilder, new ResourceLocation(Minestuck.MOD_ID, "raw_cruxite_from_dowel"));
		CookingRecipeBuilder.smelting(Ingredient.of(MSBlocks.GOLD_SEEDS), Items.GOLD_NUGGET, 0.1F, 200).unlockedBy("has_gold_seeds", has(MSBlocks.GOLD_SEEDS)).save(recipeBuilder, new ResourceLocation(Minestuck.MOD_ID, "gold_nugget_from_seeds"));
		CookingRecipeBuilder.smelting(Ingredient.of(MSBlocks.WOODEN_CACTUS), Items.CHARCOAL, 0.15F, 200).unlockedBy("has_wooden_cactus", has(MSBlocks.WOODEN_CACTUS)).save(recipeBuilder, new ResourceLocation(Minestuck.MOD_ID, "charcoal_from_wooden_cactus"));
		
		cookingRecipesFor(recipeBuilder, Ingredient.of(MSItems.BEEF_SWORD), MSItems.STEAK_SWORD, 0.5F, "has_beef_sword", has(MSItems.BEEF_SWORD));
		
		CookingRecipeBuilder.cooking(Ingredient.of(Items.BEEF), MSItems.IRRADIATED_STEAK, 0.2F, 20, MSRecipeTypes.IRRADIATING).unlockedBy("has_beef", has(Items.BEEF)).save(skipAdvancement(recipeBuilder));
		CookingRecipeBuilder.cooking(Ingredient.of(MSItems.BEEF_SWORD), MSItems.IRRADIATED_STEAK_SWORD, 0.35F, 20, MSRecipeTypes.IRRADIATING).unlockedBy("has_beef_sword", has(MSItems.BEEF_SWORD)).save(skipAdvancement(recipeBuilder));
		CookingRecipeBuilder.cooking(Ingredient.of(Items.STICK), MSItems.URANIUM_POWERED_STICK, 0.1F, 100, MSRecipeTypes.IRRADIATING).unlockedBy("has_stick", has(Items.STICK)).save(skipAdvancement(recipeBuilder));
		CookingRecipeBuilder.cooking(Ingredient.of(Items.MUSHROOM_STEW), Items.SLIME_BALL, 0.1F, 20, MSRecipeTypes.IRRADIATING).unlockedBy("has_mushroom_stew", has(Items.MUSHROOM_STEW)).save(skipAdvancement(recipeBuilder), new ResourceLocation(Minestuck.MOD_ID, "slimeball_from_irradiating"));
		IrradiatingFallbackRecipeBuilder.fallback(IRecipeType.SMOKING).build(recipeBuilder, new ResourceLocation(Minestuck.MOD_ID, "irradiate_smoking_fallback"));
		
		SingleItemRecipeBuilder.stonecutting(Ingredient.of(MSBlocks.BLACK_CASTLE_BRICKS), MSBlocks.BLACK_CASTLE_BRICK_STAIRS).unlocks("has_black_chess_bricks", has(MSBlocks.BLACK_CASTLE_BRICKS)).save(recipeBuilder, new ResourceLocation(Minestuck.MOD_ID, "black_chess_brick_stairs_from_black_chess_bricks_stonecutting"));
		SingleItemRecipeBuilder.stonecutting(Ingredient.of(MSBlocks.DARK_GRAY_CASTLE_BRICKS), MSBlocks.DARK_GRAY_CASTLE_BRICK_STAIRS).unlocks("has_dark_gray_chess_bricks", has(MSBlocks.DARK_GRAY_CASTLE_BRICKS)).save(recipeBuilder, new ResourceLocation(Minestuck.MOD_ID, "dark_gray_chess_brick_stairs_from_dark_gray_chess_bricks_stonecutting"));
		SingleItemRecipeBuilder.stonecutting(Ingredient.of(MSBlocks.LIGHT_GRAY_CASTLE_BRICKS), MSBlocks.LIGHT_GRAY_CASTLE_BRICK_STAIRS).unlocks("has_light_gray_chess_bricks", has(MSBlocks.LIGHT_GRAY_CASTLE_BRICKS)).save(recipeBuilder, new ResourceLocation(Minestuck.MOD_ID, "light_gray_chess_brick_stairs_from_light_gray_chess_bricks_stonecutting"));
		SingleItemRecipeBuilder.stonecutting(Ingredient.of(MSBlocks.WHITE_CASTLE_BRICKS), MSBlocks.WHITE_CASTLE_BRICK_STAIRS).unlocks("has_white_chess_bricks", has(MSBlocks.WHITE_CASTLE_BRICKS)).save(recipeBuilder, new ResourceLocation(Minestuck.MOD_ID, "white_chess_brick_stairs_from_white_chess_bricks_stonecutting"));
		SingleItemRecipeBuilder.stonecutting(Ingredient.of(MSBlocks.BLACK_CASTLE_BRICKS), MSBlocks.BLACK_CASTLE_BRICK_SLAB, 2).unlocks("has_black_chess_bricks", has(MSBlocks.BLACK_CASTLE_BRICKS)).save(recipeBuilder, new ResourceLocation(Minestuck.MOD_ID, "black_chess_brick_slab_from_black_chess_bricks_stonecutting"));
		SingleItemRecipeBuilder.stonecutting(Ingredient.of(MSBlocks.DARK_GRAY_CASTLE_BRICKS), MSBlocks.DARK_GRAY_CASTLE_BRICK_SLAB, 2).unlocks("has_dark_gray_chess_bricks", has(MSBlocks.DARK_GRAY_CASTLE_BRICKS)).save(recipeBuilder, new ResourceLocation(Minestuck.MOD_ID, "dark_gray_chess_brick_slab_from_dark_gray_chess_bricks_stonecutting"));
		SingleItemRecipeBuilder.stonecutting(Ingredient.of(MSBlocks.LIGHT_GRAY_CASTLE_BRICKS), MSBlocks.LIGHT_GRAY_CASTLE_BRICK_SLAB, 2).unlocks("has_light_gray_chess_bricks", has(MSBlocks.LIGHT_GRAY_CASTLE_BRICKS)).save(recipeBuilder, new ResourceLocation(Minestuck.MOD_ID, "light_gray_chess_brick_slab_from_light_gray_chess_bricks_stonecutting"));
		SingleItemRecipeBuilder.stonecutting(Ingredient.of(MSBlocks.WHITE_CASTLE_BRICKS), MSBlocks.WHITE_CASTLE_BRICK_SLAB, 2).unlocks("has_white_chess_bricks", has(MSBlocks.WHITE_CASTLE_BRICKS)).save(recipeBuilder, new ResourceLocation(Minestuck.MOD_ID, "white_chess_brick_slab_from_white_chess_bricks_stonecutting"));
		SingleItemRecipeBuilder.stonecutting(Ingredient.of(MSBlocks.COARSE_STONE), MSBlocks.CHISELED_COARSE_STONE).unlocks("has_coarse_stone", has(MSBlocks.COARSE_STONE)).save(recipeBuilder, new ResourceLocation(Minestuck.MOD_ID, "chiseled_coarse_stone_from_coarse_stone_stonecutting"));
		SingleItemRecipeBuilder.stonecutting(Ingredient.of(MSBlocks.COARSE_STONE), MSBlocks.COARSE_STONE_BRICKS).unlocks("has_coarse_stone", has(MSBlocks.COARSE_STONE)).save(recipeBuilder, new ResourceLocation(Minestuck.MOD_ID, "coarse_stone_bricks_from_coarse_stone_stonecutting"));
		SingleItemRecipeBuilder.stonecutting(Ingredient.of(MSBlocks.COARSE_STONE_BRICKS), MSBlocks.CHISELED_COARSE_STONE_BRICKS).unlocks("has_coarse_stone_bricks", has(MSBlocks.COARSE_STONE_BRICKS)).save(recipeBuilder, new ResourceLocation(Minestuck.MOD_ID, "chiseled_coarse_stone_bricks_from_coarse_stone_bricks_stonecutting"));
		SingleItemRecipeBuilder.stonecutting(Ingredient.of(MSBlocks.COARSE_STONE_BRICKS), MSBlocks.COARSE_STONE_COLUMN).unlocks("has_coarse_stone_bricks", has(MSBlocks.COARSE_STONE_BRICKS)).save(recipeBuilder, new ResourceLocation(Minestuck.MOD_ID, "coarse_stone_column_from_coarse_stone_bricks_stonecutting"));
		SingleItemRecipeBuilder.stonecutting(Ingredient.of(MSBlocks.COARSE_STONE), MSBlocks.COARSE_STONE_STAIRS).unlocks("has_coarse_stone", has(MSBlocks.COARSE_STONE)).save(recipeBuilder, new ResourceLocation(Minestuck.MOD_ID, "coarse_stone_stairs_from_coarse_stone_stonecutting"));
		SingleItemRecipeBuilder.stonecutting(Ingredient.of(MSBlocks.COARSE_STONE), MSBlocks.COARSE_STONE_SLAB, 2).unlocks("has_coarse_stone", has(MSBlocks.COARSE_STONE)).save(recipeBuilder, new ResourceLocation(Minestuck.MOD_ID, "coarse_stone_slab_from_coarse_stone_stonecutting"));
		SingleItemRecipeBuilder.stonecutting(Ingredient.of(MSBlocks.COARSE_STONE_BRICKS), MSBlocks.COARSE_STONE_BRICK_STAIRS).unlocks("has_coarse_stone_bricks", has(MSBlocks.COARSE_STONE_BRICKS)).save(recipeBuilder, new ResourceLocation(Minestuck.MOD_ID, "coarse_stone_brick_stairs_from_coarse_stone_bricks_stonecutting"));
		SingleItemRecipeBuilder.stonecutting(Ingredient.of(MSBlocks.COARSE_STONE_BRICKS), MSBlocks.COARSE_STONE_BRICK_SLAB, 2).unlocks("has_coarse_stone_bricks", has(MSBlocks.COARSE_STONE_BRICKS)).save(recipeBuilder, new ResourceLocation(Minestuck.MOD_ID, "coarse_stone_brick_slab_from_coarse_stone_bricks_stonecutting"));
		SingleItemRecipeBuilder.stonecutting(Ingredient.of(MSBlocks.SHADE_STONE), MSBlocks.SHADE_BRICKS).unlocks("has_shade_stone", has(MSBlocks.SHADE_STONE)).save(recipeBuilder, new ResourceLocation(Minestuck.MOD_ID, "shade_bricks_from_shade_stone_stonecutting"));
		SingleItemRecipeBuilder.stonecutting(Ingredient.of(MSBlocks.SHADE_STONE), MSBlocks.SHADE_STAIRS).unlocks("has_shade_stone", has(MSBlocks.SHADE_STONE)).save(recipeBuilder, new ResourceLocation(Minestuck.MOD_ID, "shade_stairs_from_shade_stone_stonecutting"));
		SingleItemRecipeBuilder.stonecutting(Ingredient.of(MSBlocks.SHADE_STONE), MSBlocks.SHADE_SLAB, 2).unlocks("has_shade_stone", has(MSBlocks.SHADE_STONE)).save(recipeBuilder, new ResourceLocation(Minestuck.MOD_ID, "shade_slab_from_shade_stone_stonecutting"));
		SingleItemRecipeBuilder.stonecutting(Ingredient.of(MSBlocks.SHADE_BRICKS), MSBlocks.SHADE_BRICK_STAIRS).unlocks("has_shade_bricks", has(MSBlocks.SHADE_BRICKS)).save(recipeBuilder, new ResourceLocation(Minestuck.MOD_ID, "shade_brick_stairs_from_shade_bricks_stonecutting"));
		SingleItemRecipeBuilder.stonecutting(Ingredient.of(MSBlocks.SHADE_BRICKS), MSBlocks.SHADE_BRICK_SLAB, 2).unlocks("has_shade_bricks", has(MSBlocks.SHADE_BRICKS)).save(recipeBuilder, new ResourceLocation(Minestuck.MOD_ID, "shade_brick_slab_from_shade_bricks_stonecutting"));
		SingleItemRecipeBuilder.stonecutting(Ingredient.of(MSBlocks.SHADE_BRICKS), MSBlocks.CHISELED_SHADE_BRICKS).unlocks("has_shade_bricks", has(MSBlocks.SHADE_BRICKS)).save(recipeBuilder, new ResourceLocation(Minestuck.MOD_ID, "chiseled_shade_bricks_from_shade_bricks_stonecutting"));
		SingleItemRecipeBuilder.stonecutting(Ingredient.of(MSBlocks.SHADE_BRICKS), MSBlocks.SHADE_COLUMN).unlocks("has_shade_bricks", has(MSBlocks.SHADE_BRICKS)).save(recipeBuilder, new ResourceLocation(Minestuck.MOD_ID, "shade_column_from_shade_bricks_stonecutting"));
		SingleItemRecipeBuilder.stonecutting(Ingredient.of(MSBlocks.FROST_TILE), MSBlocks.CHISELED_FROST_TILE).unlocks("has_frost_tile", has(MSBlocks.FROST_TILE)).save(recipeBuilder, new ResourceLocation(Minestuck.MOD_ID, "chiseled_frost_tile_from_frost_tile_stonecutting"));
		SingleItemRecipeBuilder.stonecutting(Ingredient.of(MSBlocks.FROST_TILE), MSBlocks.FROST_BRICKS).unlocks("has_frost_tile", has(MSBlocks.FROST_TILE)).save(recipeBuilder, new ResourceLocation(Minestuck.MOD_ID, "frost_bricks_from_frost_tile_stonecutting"));
		SingleItemRecipeBuilder.stonecutting(Ingredient.of(MSBlocks.FROST_BRICKS), MSBlocks.CHISELED_FROST_BRICKS).unlocks("has_frost_bricks", has(MSBlocks.FROST_BRICKS)).save(recipeBuilder, new ResourceLocation(Minestuck.MOD_ID, "chiseled_frost_bricks_from_frost_bricks_stonecutting"));
		SingleItemRecipeBuilder.stonecutting(Ingredient.of(MSBlocks.FROST_BRICKS), MSBlocks.FROST_COLUMN).unlocks("has_frost_bricks", has(MSBlocks.FROST_BRICKS)).save(recipeBuilder, new ResourceLocation(Minestuck.MOD_ID, "frost_column_from_frost_bricks_stonecutting"));
		SingleItemRecipeBuilder.stonecutting(Ingredient.of(MSBlocks.FROST_TILE), MSBlocks.FROST_TILE_STAIRS).unlocks("has_frost_tile", has(MSBlocks.FROST_TILE)).save(recipeBuilder, new ResourceLocation(Minestuck.MOD_ID, "frost_tile_stairs_from_frost_tile_stonecutting"));
		SingleItemRecipeBuilder.stonecutting(Ingredient.of(MSBlocks.FROST_TILE), MSBlocks.FROST_TILE_SLAB, 2).unlocks("has_frost_tile", has(MSBlocks.FROST_TILE)).save(recipeBuilder, new ResourceLocation(Minestuck.MOD_ID, "frost_tile_slab_from_frost_tile_stonecutting"));
		SingleItemRecipeBuilder.stonecutting(Ingredient.of(MSBlocks.FROST_BRICKS), MSBlocks.FROST_BRICK_STAIRS).unlocks("has_frost_bricks", has(MSBlocks.FROST_BRICKS)).save(recipeBuilder, new ResourceLocation(Minestuck.MOD_ID, "frost_brick_stairs_from_frost_bricks_stonecutting"));
		SingleItemRecipeBuilder.stonecutting(Ingredient.of(MSBlocks.FROST_BRICKS), MSBlocks.FROST_BRICK_SLAB, 2).unlocks("has_frost_bricks", has(MSBlocks.FROST_BRICKS)).save(recipeBuilder, new ResourceLocation(Minestuck.MOD_ID, "frost_brick_slab_from_frost_bricks_stonecutting"));
		SingleItemRecipeBuilder.stonecutting(Ingredient.of(MSBlocks.CAST_IRON), MSBlocks.CAST_IRON_STAIRS).unlocks("has_cast_iron", has(MSBlocks.CAST_IRON)).save(recipeBuilder, new ResourceLocation(Minestuck.MOD_ID, "cast_iron_stairs_from_cast_iron_stonecutting"));
		SingleItemRecipeBuilder.stonecutting(Ingredient.of(MSBlocks.CAST_IRON), MSBlocks.CHISELED_CAST_IRON).unlocks("has_cast_iron", has(MSBlocks.CAST_IRON)).save(recipeBuilder, new ResourceLocation(Minestuck.MOD_ID, "chiseled_cast_iron_from_cast_iron_stonecutting"));
		SingleItemRecipeBuilder.stonecutting(Ingredient.of(MSBlocks.MYCELIUM_STONE), MSBlocks.MYCELIUM_BRICKS).unlocks("has_mycelium_stone", has(MSBlocks.MYCELIUM_STONE)).save(recipeBuilder, new ResourceLocation(Minestuck.MOD_ID, "mycelium_bricks_from_mycelium_stone_stonecutting"));
		SingleItemRecipeBuilder.stonecutting(Ingredient.of(MSBlocks.MYCELIUM_BRICKS), MSBlocks.CHISELED_MYCELIUM_BRICKS).unlocks("has_mycelium_bricks", has(MSBlocks.MYCELIUM_BRICKS)).save(recipeBuilder, new ResourceLocation(Minestuck.MOD_ID, "chiseled_mycelium_bricks_from_mycelium_bricks_stonecutting"));
		SingleItemRecipeBuilder.stonecutting(Ingredient.of(MSBlocks.MYCELIUM_BRICKS), MSBlocks.MYCELIUM_COLUMN).unlocks("has_mycelium_bricks", has(MSBlocks.MYCELIUM_BRICKS)).save(recipeBuilder, new ResourceLocation(Minestuck.MOD_ID, "mycelium_column_from_mycelium_bricks_stonecutting"));
		SingleItemRecipeBuilder.stonecutting(Ingredient.of(MSBlocks.MYCELIUM_STONE), MSBlocks.MYCELIUM_STAIRS).unlocks("has_mycelium_stone", has(MSBlocks.MYCELIUM_STONE)).save(recipeBuilder, new ResourceLocation(Minestuck.MOD_ID, "mycelium_stairs_from_mycelium_stone_stonecutting"));
		SingleItemRecipeBuilder.stonecutting(Ingredient.of(MSBlocks.MYCELIUM_BRICKS), MSBlocks.MYCELIUM_BRICK_STAIRS).unlocks("has_mycelium_bricks", has(MSBlocks.MYCELIUM_BRICKS)).save(recipeBuilder, new ResourceLocation(Minestuck.MOD_ID, "mycelium_brick_stairs_from_mycelium_bricks_stonecutting"));
		SingleItemRecipeBuilder.stonecutting(Ingredient.of(MSBlocks.MYCELIUM_STONE), MSBlocks.MYCELIUM_SLAB, 2).unlocks("has_mycelium_stone", has(MSBlocks.MYCELIUM_STONE)).save(recipeBuilder, new ResourceLocation(Minestuck.MOD_ID, "mycelium_slab_from_mycelium_stone_stonecutting"));
		SingleItemRecipeBuilder.stonecutting(Ingredient.of(MSBlocks.MYCELIUM_BRICKS), MSBlocks.MYCELIUM_BRICK_SLAB, 2).unlocks("has_mycelium_bricks", has(MSBlocks.MYCELIUM_BRICKS)).save(recipeBuilder, new ResourceLocation(Minestuck.MOD_ID, "mycelium_brick_slab_from_mycelium_bricks_stonecutting"));
		SingleItemRecipeBuilder.stonecutting(Ingredient.of(MSBlocks.BLACK_STONE), MSBlocks.BLACK_STONE_BRICKS).unlocks("has_black_stone", has(MSBlocks.BLACK_STONE)).save(recipeBuilder, new ResourceLocation(Minestuck.MOD_ID, "black_stone_bricks_from_black_stone_stonecutting"));
		SingleItemRecipeBuilder.stonecutting(Ingredient.of(MSBlocks.BLACK_STONE_BRICKS), MSBlocks.CHISELED_BLACK_STONE_BRICKS).unlocks("has_black_stone_bricks", has(MSBlocks.BLACK_STONE_BRICKS)).save(recipeBuilder, new ResourceLocation(Minestuck.MOD_ID, "chiseled_black_stone_bricks_from_black_stone_bricks_stonecutting"));
		SingleItemRecipeBuilder.stonecutting(Ingredient.of(MSBlocks.BLACK_STONE_BRICKS), MSBlocks.BLACK_STONE_COLUMN).unlocks("has_black_stone_bricks", has(MSBlocks.BLACK_STONE_BRICKS)).save(recipeBuilder, new ResourceLocation(Minestuck.MOD_ID, "black_stone_column_from_black_stone_bricks_stonecutting"));
		SingleItemRecipeBuilder.stonecutting(Ingredient.of(MSBlocks.BLACK_STONE), MSBlocks.BLACK_STONE_STAIRS).unlocks("has_black_stone", has(MSBlocks.BLACK_STONE)).save(recipeBuilder, new ResourceLocation(Minestuck.MOD_ID, "black_stone_stairs_from_black_stone_stonecutting"));
		SingleItemRecipeBuilder.stonecutting(Ingredient.of(MSBlocks.BLACK_STONE_BRICKS), MSBlocks.BLACK_STONE_BRICK_STAIRS).unlocks("has_black_stone_bricks", has(MSBlocks.BLACK_STONE_BRICKS)).save(recipeBuilder, new ResourceLocation(Minestuck.MOD_ID, "black_stone_brick_stairs_from_black_stone_bricks_stonecutting"));
		SingleItemRecipeBuilder.stonecutting(Ingredient.of(MSBlocks.BLACK_STONE), MSBlocks.BLACK_STONE_SLAB, 2).unlocks("has_black_stone", has(MSBlocks.BLACK_STONE)).save(recipeBuilder, new ResourceLocation(Minestuck.MOD_ID, "black_stone_slab_from_black_stone_stonecutting"));
		SingleItemRecipeBuilder.stonecutting(Ingredient.of(MSBlocks.BLACK_STONE_BRICKS), MSBlocks.BLACK_STONE_BRICK_SLAB, 2).unlocks("has_black_stone_bricks", has(MSBlocks.BLACK_STONE_BRICKS)).save(recipeBuilder, new ResourceLocation(Minestuck.MOD_ID, "black_stone_brick_slab_from_black_stone_bricks_stonecutting"));
		SingleItemRecipeBuilder.stonecutting(Ingredient.of(MSBlocks.CHALK), MSBlocks.CHALK_STAIRS).unlocks("has_chalk", has(MSBlocks.CHALK)).save(recipeBuilder, new ResourceLocation(Minestuck.MOD_ID, "chalk_stairs_from_chalk_stonecutting"));
		SingleItemRecipeBuilder.stonecutting(Ingredient.of(MSBlocks.CHALK), MSBlocks.CHALK_SLAB, 2).unlocks("has_chalk", has(MSBlocks.CHALK)).save(recipeBuilder, new ResourceLocation(Minestuck.MOD_ID, "chalk_slab_from_chalk_stonecutting"));
		SingleItemRecipeBuilder.stonecutting(Ingredient.of(MSBlocks.CHALK), MSBlocks.POLISHED_CHALK).unlocks("has_chalk", has(MSBlocks.CHALK)).save(recipeBuilder, new ResourceLocation(Minestuck.MOD_ID, "polished_chalk_from_chalk_stonecutting"));
		SingleItemRecipeBuilder.stonecutting(Ingredient.of(MSBlocks.CHALK), MSBlocks.CHALK_BRICKS).unlocks("has_chalk", has(MSBlocks.CHALK)).save(recipeBuilder, new ResourceLocation(Minestuck.MOD_ID, "chalk_bricks_from_chalk_stonecutting"));
		SingleItemRecipeBuilder.stonecutting(Ingredient.of(MSBlocks.POLISHED_CHALK), MSBlocks.CHALK_BRICKS).unlocks("has_polished_chalk", has(MSBlocks.POLISHED_CHALK)).save(recipeBuilder, new ResourceLocation(Minestuck.MOD_ID, "chalk_bricks_from_polished_chalk_stonecutting"));
		SingleItemRecipeBuilder.stonecutting(Ingredient.of(MSBlocks.CHALK_BRICKS), MSBlocks.CHISELED_CHALK_BRICKS).unlocks("has_chalk_bricks", has(MSBlocks.CHALK_BRICKS)).save(recipeBuilder, new ResourceLocation(Minestuck.MOD_ID, "chiseled_chalk_bricks_from_chalk_bricks_stonecutting"));
		SingleItemRecipeBuilder.stonecutting(Ingredient.of(MSBlocks.CHALK_BRICKS), MSBlocks.CHALK_COLUMN).unlocks("has_chalk_bricks", has(MSBlocks.CHALK_BRICKS)).save(recipeBuilder, new ResourceLocation(Minestuck.MOD_ID, "chalk_column_from_chalk_bricks_stonecutting"));
		SingleItemRecipeBuilder.stonecutting(Ingredient.of(MSBlocks.CHALK_BRICKS), MSBlocks.CHALK_BRICK_STAIRS).unlocks("has_chalk_bricks", has(MSBlocks.CHALK_BRICKS)).save(recipeBuilder, new ResourceLocation(Minestuck.MOD_ID, "chalk_brick_stairs_from_chalk_bricks_stonecutting"));
		SingleItemRecipeBuilder.stonecutting(Ingredient.of(MSBlocks.CHALK_BRICKS), MSBlocks.CHALK_BRICK_SLAB, 2).unlocks("has_chalk_bricks", has(MSBlocks.CHALK_BRICKS)).save(recipeBuilder, new ResourceLocation(Minestuck.MOD_ID, "chalk_brick_slab_from_chalk_bricks_stonecutting"));
		SingleItemRecipeBuilder.stonecutting(Ingredient.of(MSBlocks.PINK_STONE), MSBlocks.PINK_STONE_BRICKS).unlocks("has_pink_stone", has(MSBlocks.PINK_STONE)).save(recipeBuilder, new ResourceLocation(Minestuck.MOD_ID, "pink_stone_bricks_from_pink_stone_stonecutting"));
		SingleItemRecipeBuilder.stonecutting(Ingredient.of(MSBlocks.PINK_STONE_BRICKS), MSBlocks.CHISELED_PINK_STONE_BRICKS).unlocks("has_pink_stone_bricks", has(MSBlocks.PINK_STONE_BRICKS)).save(recipeBuilder, new ResourceLocation(Minestuck.MOD_ID, "chiseled_pink_stone_bricks_from_pink_stone_bricks_stonecutting"));
		SingleItemRecipeBuilder.stonecutting(Ingredient.of(MSBlocks.PINK_STONE_BRICKS), MSBlocks.PINK_STONE_COLUMN).unlocks("has_pink_stone_bricks", has(MSBlocks.PINK_STONE_BRICKS)).save(recipeBuilder, new ResourceLocation(Minestuck.MOD_ID, "pink_stone_column_from_pink_stone_bricks_stonecutting"));
		SingleItemRecipeBuilder.stonecutting(Ingredient.of(MSBlocks.PINK_STONE), MSBlocks.PINK_STONE_STAIRS).unlocks("has_pink_stone", has(MSBlocks.PINK_STONE)).save(recipeBuilder, new ResourceLocation(Minestuck.MOD_ID, "pink_stone_stairs_from_pink_stone_stonecutting"));
		SingleItemRecipeBuilder.stonecutting(Ingredient.of(MSBlocks.PINK_STONE), MSBlocks.PINK_STONE_SLAB, 2).unlocks("has_pink_stone", has(MSBlocks.PINK_STONE)).save(recipeBuilder, new ResourceLocation(Minestuck.MOD_ID, "pink_stone_slab_from_pink_stone_stonecutting"));
		SingleItemRecipeBuilder.stonecutting(Ingredient.of(MSBlocks.PINK_STONE_BRICKS), MSBlocks.PINK_STONE_BRICK_STAIRS).unlocks("has_pink_stone_bricks", has(MSBlocks.PINK_STONE_BRICKS)).save(recipeBuilder, new ResourceLocation(Minestuck.MOD_ID, "pink_stone_brick_stairs_from_pink_stone_bricks_stonecutting"));
		SingleItemRecipeBuilder.stonecutting(Ingredient.of(MSBlocks.PINK_STONE_BRICKS), MSBlocks.PINK_STONE_BRICK_SLAB, 2).unlocks("has_pink_stone_bricks", has(MSBlocks.PINK_STONE_BRICKS)).save(recipeBuilder, new ResourceLocation(Minestuck.MOD_ID, "pink_stone_brick_slab_from_pink_stone_bricks_stonecutting"));
		SingleItemRecipeBuilder.stonecutting(Ingredient.of(MSBlocks.BROWN_STONE), MSBlocks.BROWN_STONE_BRICKS).unlocks("has_brown_stone", has(MSBlocks.BROWN_STONE)).save(recipeBuilder, new ResourceLocation(Minestuck.MOD_ID, "brown_stone_bricks_from_brown_stone_stonecutting"));
		SingleItemRecipeBuilder.stonecutting(Ingredient.of(MSBlocks.POLISHED_BROWN_STONE), MSBlocks.BROWN_STONE_BRICKS).unlocks("has_polished_brown_stone", has(MSBlocks.POLISHED_BROWN_STONE)).save(recipeBuilder, new ResourceLocation(Minestuck.MOD_ID, "brown_stone_bricks_from_polished_brown_stone_stonecutting"));
		SingleItemRecipeBuilder.stonecutting(Ingredient.of(MSBlocks.BROWN_STONE_BRICKS), MSBlocks.BROWN_STONE_COLUMN).unlocks("has_brown_stone_bricks", has(MSBlocks.BROWN_STONE_BRICKS)).save(recipeBuilder, new ResourceLocation(Minestuck.MOD_ID, "brown_stone_column_from_brown_stone_bricks_stonecutting"));
		SingleItemRecipeBuilder.stonecutting(Ingredient.of(MSBlocks.BROWN_STONE), MSBlocks.BROWN_STONE_STAIRS).unlocks("has_brown_stone", has(MSBlocks.BROWN_STONE)).save(recipeBuilder, new ResourceLocation(Minestuck.MOD_ID, "brown_stone_stairs_from_brown_stone_stonecutting"));
		SingleItemRecipeBuilder.stonecutting(Ingredient.of(MSBlocks.BROWN_STONE), MSBlocks.BROWN_STONE_SLAB, 2).unlocks("has_brown_stone", has(MSBlocks.BROWN_STONE)).save(recipeBuilder, new ResourceLocation(Minestuck.MOD_ID, "brown_stone_slab_from_brown_stone_stonecutting"));
		SingleItemRecipeBuilder.stonecutting(Ingredient.of(MSBlocks.BROWN_STONE_BRICKS), MSBlocks.BROWN_STONE_BRICK_STAIRS).unlocks("has_brown_stone_bricks", has(MSBlocks.BROWN_STONE_BRICKS)).save(recipeBuilder, new ResourceLocation(Minestuck.MOD_ID, "brown_stone_brick_stairs_from_brown_stone_bricks_stonecutting"));
		SingleItemRecipeBuilder.stonecutting(Ingredient.of(MSBlocks.BROWN_STONE_BRICKS), MSBlocks.BROWN_STONE_BRICK_SLAB, 2).unlocks("has_brown_stone_bricks", has(MSBlocks.BROWN_STONE_BRICKS)).save(recipeBuilder, new ResourceLocation(Minestuck.MOD_ID, "brown_stone_brick_slab_from_brown_stone_bricks_stonecutting"));
		SingleItemRecipeBuilder.stonecutting(Ingredient.of(MSBlocks.GREEN_STONE), MSBlocks.POLISHED_GREEN_STONE).unlocks("has_green_stone", has(MSBlocks.GREEN_STONE)).save(recipeBuilder, new ResourceLocation(Minestuck.MOD_ID, "polished_green_stone_from_green_stone_stonecutting"));
		SingleItemRecipeBuilder.stonecutting(Ingredient.of(MSBlocks.POLISHED_GREEN_STONE), MSBlocks.GREEN_STONE_BRICKS).unlocks("has_polished_green_stone", has(MSBlocks.POLISHED_GREEN_STONE)).save(recipeBuilder, new ResourceLocation(Minestuck.MOD_ID, "green_stone_bricks_from_polished_green_stone_stonecutting"));
		SingleItemRecipeBuilder.stonecutting(Ingredient.of(MSBlocks.POLISHED_GREEN_STONE), MSBlocks.HORIZONTAL_GREEN_STONE_BRICKS).unlocks("has_polished_green_stone", has(MSBlocks.POLISHED_GREEN_STONE)).save(recipeBuilder, new ResourceLocation(Minestuck.MOD_ID, "horizontal_green_stone_bricks_from_polished_green_stone_stonecutting"));
		SingleItemRecipeBuilder.stonecutting(Ingredient.of(MSBlocks.POLISHED_GREEN_STONE), MSBlocks.VERTICAL_GREEN_STONE_BRICKS).unlocks("has_polished_green_stone", has(MSBlocks.POLISHED_GREEN_STONE)).save(recipeBuilder, new ResourceLocation(Minestuck.MOD_ID, "vertical_green_stone_bricks_from_polished_green_stone_stonecutting"));
		SingleItemRecipeBuilder.stonecutting(Ingredient.of(MSBlocks.POLISHED_GREEN_STONE), MSBlocks.CHISELED_GREEN_STONE_BRICKS).unlocks("has_polished_green_stone", has(MSBlocks.POLISHED_GREEN_STONE)).save(recipeBuilder, new ResourceLocation(Minestuck.MOD_ID, "chiseled_green_stone_bricks_from_polished_green_stone_stonecutting"));
		SingleItemRecipeBuilder.stonecutting(Ingredient.of(MSBlocks.GREEN_STONE_BRICKS), MSBlocks.GREEN_STONE_COLUMN).unlocks("has_green_stone_bricks", has(MSBlocks.GREEN_STONE_BRICKS)).save(recipeBuilder, new ResourceLocation(Minestuck.MOD_ID, "green_stone_column_from_green_stone_bricks_stonecutting"));
		SingleItemRecipeBuilder.stonecutting(Ingredient.of(MSBlocks.GREEN_STONE_BRICKS), MSBlocks.GREEN_STONE_BRICK_TRIM).unlocks("has_green_stone_bricks", has(MSBlocks.GREEN_STONE_BRICKS)).save(recipeBuilder, new ResourceLocation(Minestuck.MOD_ID, "green_stone_brick_trim_from_green_stone_bricks_stonecutting"));
		SingleItemRecipeBuilder.stonecutting(Ingredient.of(MSBlocks.GREEN_STONE), MSBlocks.GREEN_STONE_STAIRS).unlocks("has_green_stone", has(MSBlocks.GREEN_STONE)).save(recipeBuilder, new ResourceLocation(Minestuck.MOD_ID, "green_stone_stairs_from_green_stone_stonecutting"));
		SingleItemRecipeBuilder.stonecutting(Ingredient.of(MSBlocks.GREEN_STONE), MSBlocks.GREEN_STONE_SLAB, 2).unlocks("has_green_stone", has(MSBlocks.GREEN_STONE)).save(recipeBuilder, new ResourceLocation(Minestuck.MOD_ID, "green_stone_slab_from_green_stone_stonecutting"));
		SingleItemRecipeBuilder.stonecutting(Ingredient.of(MSBlocks.GREEN_STONE_BRICKS), MSBlocks.GREEN_STONE_BRICK_STAIRS).unlocks("has_green_stone_bricks", has(MSBlocks.GREEN_STONE_BRICKS)).save(recipeBuilder, new ResourceLocation(Minestuck.MOD_ID, "green_stone_brick_stairs_from_green_stone_bricks_stonecutting"));
		SingleItemRecipeBuilder.stonecutting(Ingredient.of(MSBlocks.GREEN_STONE_BRICKS), MSBlocks.GREEN_STONE_BRICK_SLAB, 2).unlocks("has_green_stone_bricks", has(MSBlocks.GREEN_STONE_BRICKS)).save(recipeBuilder, new ResourceLocation(Minestuck.MOD_ID, "green_stone_brick_slab_from_green_stone_bricks_stonecutting"));
		SingleItemRecipeBuilder.stonecutting(Ingredient.of(Items.STONE_BRICKS), MSBlocks.DECREPIT_STONE_BRICKS).unlocks("has_stone_bricks", has(Items.STONE_BRICKS)).save(recipeBuilder, new ResourceLocation(Minestuck.MOD_ID, "decrepit_stone_bricks_from_stone_bricks_stonecutting"));
	}
	
	private void cookingRecipesFor(Consumer<IFinishedRecipe> recipeBuilder, Ingredient input, IItemProvider result, float experience, String criterionName, InventoryChangeTrigger.Instance criterion)
	{
		ResourceLocation itemName = Objects.requireNonNull(ForgeRegistries.ITEMS.getKey(result.asItem()), "Registry name for "+result+" was found to be null!");
		CookingRecipeBuilder.smelting(input, result, experience, 200).unlockedBy(criterionName, criterion).save(recipeBuilder);
		CookingRecipeBuilder.cooking(input, result, experience, 100, IRecipeSerializer.SMOKING_RECIPE).unlockedBy(criterionName, criterion).save(recipeBuilder, new ResourceLocation(itemName.getNamespace(), itemName.getPath()+"_from_smoking"));
		CookingRecipeBuilder.cooking(input, result, experience, 600, IRecipeSerializer.CAMPFIRE_COOKING_RECIPE).unlockedBy(criterionName, criterion).save(recipeBuilder, new ResourceLocation(itemName.getNamespace(), itemName.getPath()+"_from_campfire_cooking"));
	}
	
	//TODO check between mc versions if this is still needed
	//As of writing this, the categories used by the vanilla recipe books are hardcoded, and all recipes are put in these categories
	// Because of this, recipes of modded recipe types will when unlocked show up in a vanilla recipe category
	// As a temporary solution, this function helps to remove the recipe advancement, thus preventing the recipe from "unlocking"
	private Consumer<IFinishedRecipe> skipAdvancement(Consumer<IFinishedRecipe> recipeBuilder)
	{
		return recipe -> recipeBuilder.accept(new Wrapper(recipe));
	}
	
	private static class Wrapper implements IFinishedRecipe
	{
		IFinishedRecipe recipe;
		
		Wrapper(IFinishedRecipe recipe)
		{
			this.recipe = recipe;
		}
		
		@Override
		public void serializeRecipeData(JsonObject json)
		{
			recipe.serializeRecipeData(json);
		}
		
		@Override
		public ResourceLocation getId()
		{
			return recipe.getId();
		}
		
		@Override
		public IRecipeSerializer<?> getType()
		{
			return recipe.getType();
		}
		
		@Nullable
		@Override
		public JsonObject serializeAdvancement()
		{
			return null;
		}
		
		@Nullable
		@Override
		public ResourceLocation getAdvancementId()
		{
			return null;
		}
	}
	
	@Override
	public String getName()
	{
		return "Minestuck Recipes";
	}
}