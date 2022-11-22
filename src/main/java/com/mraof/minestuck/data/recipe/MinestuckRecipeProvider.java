package com.mraof.minestuck.data.recipe;

import com.google.gson.JsonObject;
import com.mraof.minestuck.Minestuck;
import com.mraof.minestuck.block.MSBlocks;
import com.mraof.minestuck.item.MSItems;
import com.mraof.minestuck.item.crafting.MSRecipeTypes;
import com.mraof.minestuck.util.ExtraForgeTags;
import com.mraof.minestuck.util.MSTags;
import net.minecraft.advancements.critereon.InventoryChangeTrigger;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.recipes.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.ItemLike;
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
	protected void buildCraftingRecipes(Consumer<FinishedRecipe> recipeBuilder)
	{
		ShapelessRecipeBuilder.shapeless(MSItems.RAW_CRUXITE.get(), 9).requires(MSBlocks.CRUXITE_BLOCK.get()).unlockedBy("has_raw_cruxite", has(MSItems.RAW_CRUXITE.get())).unlockedBy("has_cruxite_block", has(MSBlocks.CRUXITE_BLOCK.get())).save(recipeBuilder, new ResourceLocation(Minestuck.MOD_ID, "raw_cruxite_from_block"));
		ShapedRecipeBuilder.shaped(MSBlocks.CRUXITE_BLOCK.get()).define('#', MSItems.RAW_CRUXITE.get()).pattern("###").pattern("###").pattern("###").unlockedBy("has_raw_cruxite", has(MSItems.RAW_CRUXITE.get())).save(recipeBuilder);
		ShapelessRecipeBuilder.shapeless(MSItems.RAW_URANIUM.get(), 9).requires(MSBlocks.URANIUM_BLOCK.get()).unlockedBy("has__raw_uranium", has(MSItems.RAW_URANIUM.get())).unlockedBy("has_uranium_block", has(MSBlocks.URANIUM_BLOCK.get())).save(recipeBuilder, new ResourceLocation(Minestuck.MOD_ID, "raw_uranium_from_block"));
		ShapedRecipeBuilder.shaped(MSBlocks.URANIUM_BLOCK.get()).define('#', MSItems.RAW_URANIUM.get()).pattern("###").pattern("###").pattern("###").unlockedBy("has_raw_uranium", has(MSItems.RAW_URANIUM.get())).save(recipeBuilder);
		ShapelessRecipeBuilder.shapeless(Items.SUGAR, 9).requires(MSBlocks.SUGAR_CUBE.get()).unlockedBy("has_sugar", has(Items.SUGAR)).unlockedBy("has_sugar_cube", has(MSBlocks.SUGAR_CUBE.get())).save(recipeBuilder, new ResourceLocation(Minestuck.MOD_ID, "sugar_from_cube"));
		ShapedRecipeBuilder.shaped(MSBlocks.SUGAR_CUBE.get()).define('#', Items.SUGAR).pattern("###").pattern("###").pattern("###").unlockedBy("has_sugar", has(Items.SUGAR)).save(recipeBuilder);
		ShapelessRecipeBuilder.shapeless(MSItems.STRAWBERRY_CHUNK.get(), 4).requires(MSBlocks.STRAWBERRY.get()).unlockedBy("has_strawberry_chunks", has( MSItems.STRAWBERRY_CHUNK.get())).unlockedBy("has_strawberry_block", has(MSBlocks.STRAWBERRY.get())).save(recipeBuilder);
		ShapedRecipeBuilder.shaped(MSBlocks.STRAWBERRY.get()).define('#', MSItems.STRAWBERRY_CHUNK.get()).pattern("##").pattern("##").unlockedBy("has_strawberry_chunks", has(MSItems.STRAWBERRY_CHUNK.get())).save(recipeBuilder);
		
		ShapedRecipeBuilder.shaped(MSBlocks.CHISELED_COARSE_STONE.get()).define('#', MSBlocks.COARSE_STONE_SLAB.get()).pattern("#").pattern("#").unlockedBy("has_coarse_stone_slab", has(MSBlocks.COARSE_STONE_SLAB.get())).save(recipeBuilder);
		ShapedRecipeBuilder.shaped(MSBlocks.COARSE_STONE_BRICKS.get(), 4).define('#', MSBlocks.COARSE_STONE.get()).pattern("##").pattern("##").unlockedBy("has_coarse_stone", has(MSBlocks.COARSE_STONE.get())).save(recipeBuilder);
		ShapedRecipeBuilder.shaped(MSBlocks.CHISELED_COARSE_STONE_BRICKS.get()).define('#', MSBlocks.COARSE_STONE_BRICK_SLAB.get()).pattern("#").pattern("#").unlockedBy("has_coarse_stone_brick_slab", has(MSBlocks.COARSE_STONE_BRICK_SLAB.get())).save(recipeBuilder);
		ShapedRecipeBuilder.shaped(MSBlocks.COARSE_STONE_COLUMN.get(), 3).define('#', MSBlocks.COARSE_STONE_BRICKS.get()).pattern("#").pattern("#").pattern("#").unlockedBy("has_coarse_stone_bricks", has(MSBlocks.COARSE_STONE_BRICKS.get())).save(recipeBuilder);
		ShapelessRecipeBuilder.shapeless(MSBlocks.MOSSY_COARSE_STONE_BRICKS.get()).requires(MSBlocks.COARSE_STONE_BRICKS.get()).requires(Items.VINE).unlockedBy("has_coarse_stone_bricks", has( MSBlocks.COARSE_STONE_BRICKS.get())).save(recipeBuilder);
		ShapedRecipeBuilder.shaped(MSBlocks.SHADE_BRICKS.get(), 4).define('#', MSBlocks.SHADE_STONE.get()).pattern("##").pattern("##").unlockedBy("has_shade_stone", has(MSBlocks.SHADE_STONE.get())).save(recipeBuilder);
		ShapedRecipeBuilder.shaped(MSBlocks.CHISELED_SHADE_BRICKS.get()).define('#', MSBlocks.SHADE_BRICK_SLAB.get()).pattern("#").pattern("#").unlockedBy("has_shade_brick_slab", has(MSBlocks.SHADE_BRICK_SLAB.get())).save(recipeBuilder);
		ShapelessRecipeBuilder.shapeless(MSBlocks.MOSSY_SHADE_BRICKS.get()).requires(MSBlocks.SHADE_BRICKS.get()).requires(MSBlocks.GLOWING_MUSHROOM.get()).unlockedBy("has_shade_bricks", has( MSBlocks.SHADE_BRICKS.get())).save(recipeBuilder);
		ShapelessRecipeBuilder.shapeless(MSBlocks.BLOOD_SHADE_BRICKS.get()).requires(MSBlocks.SHADE_BRICKS.get()).requires(MSBlocks.COAGULATED_BLOOD.get()).unlockedBy("has_shade_bricks", has( MSBlocks.SHADE_BRICKS.get())).save(recipeBuilder);
		ShapedRecipeBuilder.shaped(MSBlocks.SHADE_COLUMN.get(), 3).define('#', MSBlocks.SHADE_BRICKS.get()).pattern("#").pattern("#").pattern("#").unlockedBy("has_shade_bricks", has(MSBlocks.SHADE_BRICKS.get())).save(recipeBuilder);
		ShapedRecipeBuilder.shaped(MSBlocks.MYCELIUM_BRICKS.get(), 4).define('#', MSBlocks.MYCELIUM_STONE.get()).pattern("##").pattern("##").unlockedBy("has_mycelium_stone", has(MSBlocks.MYCELIUM_STONE.get())).save(recipeBuilder);
		ShapedRecipeBuilder.shaped(MSBlocks.CHISELED_MYCELIUM_BRICKS.get()).define('#', MSBlocks.MYCELIUM_BRICK_SLAB.get()).pattern("#").pattern("#").unlockedBy("has_mycelium_brick_slab", has(MSBlocks.MYCELIUM_BRICK_SLAB.get())).save(recipeBuilder);
		ShapedRecipeBuilder.shaped(MSBlocks.MYCELIUM_COLUMN.get(), 3).define('#', MSBlocks.MYCELIUM_BRICKS.get()).pattern("#").pattern("#").pattern("#").unlockedBy("has_mycelium_bricks", has(MSBlocks.MYCELIUM_BRICKS.get())).save(recipeBuilder);
		ShapelessRecipeBuilder.shapeless(MSBlocks.MOSSY_MYCELIUM_BRICKS.get()).requires(MSBlocks.MYCELIUM_BRICKS.get()).requires(Items.VINE).unlockedBy("has_mycelium_bricks", has( MSBlocks.MYCELIUM_BRICKS.get())).save(recipeBuilder);
		ShapelessRecipeBuilder.shapeless(MSBlocks.FLOWERY_MYCELIUM_BRICKS.get()).requires(MSBlocks.MYCELIUM_BRICKS.get()).requires(Items.RED_MUSHROOM).unlockedBy("has_mycelium_bricks", has( MSBlocks.MYCELIUM_BRICKS.get())).save(recipeBuilder);
		ShapedRecipeBuilder.shaped(MSBlocks.BLACK_STONE_BRICKS.get(), 4).define('#', MSBlocks.BLACK_STONE.get()).pattern("##").pattern("##").unlockedBy("has_black_stone", has(MSBlocks.BLACK_STONE.get())).save(recipeBuilder);
		ShapedRecipeBuilder.shaped(MSBlocks.CHISELED_BLACK_STONE_BRICKS.get()).define('#', MSBlocks.BLACK_STONE_BRICK_SLAB.get()).pattern("#").pattern("#").unlockedBy("has_black_stone_brick_slab", has(MSBlocks.BLACK_STONE_BRICK_SLAB.get())).save(recipeBuilder);
		ShapedRecipeBuilder.shaped(MSBlocks.BLACK_STONE_COLUMN.get(), 3).define('#', MSBlocks.BLACK_STONE_BRICKS.get()).pattern("#").pattern("#").pattern("#").unlockedBy("has_black_stone_bricks", has(MSBlocks.BLACK_STONE_BRICKS.get())).save(recipeBuilder);
		ShapedRecipeBuilder.shaped(MSBlocks.CHISELED_FROST_TILE.get()).define('#', MSBlocks.FROST_TILE_SLAB.get()).pattern("#").pattern("#").unlockedBy("has_frost_tile_slab", has(MSBlocks.FROST_TILE_SLAB.get())).save(recipeBuilder);
		ShapedRecipeBuilder.shaped(MSBlocks.FROST_BRICKS.get(), 4).define('#', MSBlocks.FROST_TILE.get()).pattern("##").pattern("##").unlockedBy("has_frost_tile", has(MSBlocks.FROST_TILE.get())).save(recipeBuilder);
		ShapedRecipeBuilder.shaped(MSBlocks.CHISELED_FROST_BRICKS.get()).define('#', MSBlocks.FROST_BRICK_SLAB.get()).pattern("#").pattern("#").unlockedBy("has_frost_brick_slab", has(MSBlocks.FROST_BRICK_SLAB.get())).save(recipeBuilder);
		ShapedRecipeBuilder.shaped(MSBlocks.FROST_COLUMN.get(), 3).define('#', MSBlocks.FROST_BRICKS.get()).pattern("#").pattern("#").pattern("#").unlockedBy("has_frost_bricks", has(MSBlocks.FROST_BRICKS.get())).save(recipeBuilder);
		ShapelessRecipeBuilder.shapeless(MSBlocks.FLOWERY_FROST_BRICKS.get()).requires(MSBlocks.FROST_BRICKS.get()).requires(Items.VINE).requires(Items.BONE_MEAL).unlockedBy("has_frost_bricks", has( MSBlocks.FROST_BRICKS.get())).save(recipeBuilder);
		ShapedRecipeBuilder.shaped(MSBlocks.POLISHED_CHALK.get(), 4).define('#', MSBlocks.CHALK.get()).pattern("##").pattern("##").unlockedBy("has_chalk", has(MSBlocks.CHALK.get())).save(recipeBuilder);
		ShapedRecipeBuilder.shaped(MSBlocks.CHALK_BRICKS.get(), 4).define('#', MSBlocks.POLISHED_CHALK.get()).pattern("##").pattern("##").unlockedBy("has_polished_chalk", has(MSBlocks.POLISHED_CHALK.get())).save(recipeBuilder);
		ShapedRecipeBuilder.shaped(MSBlocks.CHISELED_CHALK_BRICKS.get(), 4).define('#', MSBlocks.CHALK_BRICKS.get()).pattern("##").pattern("##").unlockedBy("has_chalk_bricks", has(MSBlocks.CHALK_BRICKS.get())).save(recipeBuilder);
		ShapedRecipeBuilder.shaped(MSBlocks.CHALK_COLUMN.get(), 3).define('#', MSBlocks.CHALK_BRICKS.get()).pattern("#").pattern("#").pattern("#").unlockedBy("has_chalk_bricks", has(MSBlocks.CHALK_BRICKS.get())).save(recipeBuilder);
		ShapelessRecipeBuilder.shapeless(MSBlocks.MOSSY_CHALK_BRICKS.get()).requires(MSBlocks.CHALK_BRICKS.get()).requires(Items.VINE).unlockedBy("has_chalk_bricks", has( MSBlocks.CHALK_BRICKS.get())).save(recipeBuilder);
		ShapelessRecipeBuilder.shapeless(MSBlocks.FLOWERY_CHALK_BRICKS.get()).requires(MSBlocks.MOSSY_CHALK_BRICKS.get()).requires(Items.BONE_MEAL).unlockedBy("has_mossy_chalk_bricks", has( MSBlocks.MOSSY_CHALK_BRICKS.get())).save(recipeBuilder);
		ShapedRecipeBuilder.shaped(MSBlocks.PINK_STONE_BRICKS.get(), 4).define('#', MSBlocks.PINK_STONE.get()).pattern("##").pattern("##").unlockedBy("has_pink_stone", has(MSBlocks.PINK_STONE.get())).save(recipeBuilder);
		ShapedRecipeBuilder.shaped(MSBlocks.CHISELED_PINK_STONE_BRICKS.get()).define('#', MSBlocks.PINK_STONE_BRICK_SLAB.get()).pattern("#").pattern("#").unlockedBy("has_pink_stone_brick_slab", has(MSBlocks.PINK_STONE_BRICK_SLAB.get())).save(recipeBuilder);
		ShapelessRecipeBuilder.shapeless(MSBlocks.MOSSY_PINK_STONE_BRICKS.get()).requires(MSBlocks.PINK_STONE_BRICKS.get()).requires(Items.VINE).unlockedBy("has_vine", has(Items.VINE)).save(recipeBuilder);
		ShapedRecipeBuilder.shaped(MSBlocks.PINK_STONE_COLUMN.get(), 3).define('#', MSBlocks.PINK_STONE_BRICKS.get()).pattern("#").pattern("#").pattern("#").unlockedBy("has_pink_stone_bricks", has(MSBlocks.PINK_STONE_BRICKS.get())).save(recipeBuilder);
		ShapedRecipeBuilder.shaped(MSBlocks.BROWN_STONE_BRICKS.get(), 4).define('#', MSBlocks.BROWN_STONE.get()).pattern("##").pattern("##").unlockedBy("has_brown_stone", has(MSBlocks.BROWN_STONE.get())).save(recipeBuilder);
		ShapedRecipeBuilder.shaped(MSBlocks.BROWN_STONE_COLUMN.get(), 3).define('#', MSBlocks.BROWN_STONE_BRICKS.get()).pattern("#").pattern("#").pattern("#").unlockedBy("has_brown_stone_bricks", has(MSBlocks.BROWN_STONE_BRICKS.get())).save(recipeBuilder);
		ShapedRecipeBuilder.shaped(MSBlocks.POLISHED_GREEN_STONE.get(), 4).define('#', MSBlocks.GREEN_STONE.get()).pattern("##").pattern("##").unlockedBy("has_green_stone", has(MSBlocks.GREEN_STONE.get())).save(recipeBuilder);
		ShapedRecipeBuilder.shaped(MSBlocks.GREEN_STONE_BRICKS.get(), 4).define('#', MSBlocks.POLISHED_GREEN_STONE.get()).pattern("##").pattern("##").unlockedBy("has_polished_green_stone", has(MSBlocks.POLISHED_GREEN_STONE.get())).save(recipeBuilder);
		ShapedRecipeBuilder.shaped(MSBlocks.GREEN_STONE_COLUMN.get(), 3).define('#', MSBlocks.GREEN_STONE_BRICKS.get()).pattern("#").pattern("#").pattern("#").unlockedBy("has_green_stone_bricks", has(MSBlocks.GREEN_STONE_BRICKS.get())).save(recipeBuilder);
		ShapedRecipeBuilder.shaped(MSBlocks.HORIZONTAL_GREEN_STONE_BRICKS.get(), 2).define('#', MSBlocks.POLISHED_GREEN_STONE.get()).pattern("##").unlockedBy("has_polished_green_stone", has(MSBlocks.POLISHED_GREEN_STONE.get())).save(recipeBuilder);
		ShapedRecipeBuilder.shaped(MSBlocks.VERTICAL_GREEN_STONE_BRICKS.get(), 2).define('#', MSBlocks.POLISHED_GREEN_STONE.get()).pattern("#").pattern("#").unlockedBy("has_polished_green_stone", has(MSBlocks.POLISHED_GREEN_STONE.get())).save(recipeBuilder);
		ShapelessRecipeBuilder.shapeless(MSBlocks.MOSSY_DECREPIT_STONE_BRICKS.get()).requires(MSBlocks.DECREPIT_STONE_BRICKS.get()).requires(Items.VINE).unlockedBy("has_vine", has(Items.VINE)).save(recipeBuilder);
		ShapelessRecipeBuilder.shapeless(MSBlocks.FLOWERY_MOSSY_STONE_BRICKS.get()).requires(MSBlocks.MOSSY_DECREPIT_STONE_BRICKS.get()).requires(MSBlocks.COAGULATED_BLOOD.get()).unlockedBy("has_coagulated_blood", has(MSBlocks.COAGULATED_BLOOD.get())).save(recipeBuilder);
		ShapedRecipeBuilder.shaped(MSBlocks.BLACK_CHESS_BRICK_STAIRS.get(), 4).define('#', MSBlocks.BLACK_CHESS_BRICKS.get()).pattern("#  ").pattern("## ").pattern("###").unlockedBy("has_black_chess_bricks", has(MSBlocks.BLACK_CHESS_BRICKS.get())).save(recipeBuilder);
		ShapedRecipeBuilder.shaped(MSBlocks.DARK_GRAY_CHESS_BRICK_STAIRS.get(), 4).define('#', MSBlocks.DARK_GRAY_CHESS_BRICKS.get()).pattern("#  ").pattern("## ").pattern("###").unlockedBy("has_dark_gray_chess_bricks", has(MSBlocks.DARK_GRAY_CHESS_BRICKS.get())).save(recipeBuilder);
		ShapedRecipeBuilder.shaped(MSBlocks.LIGHT_GRAY_CHESS_BRICK_STAIRS.get(), 4).define('#', MSBlocks.LIGHT_GRAY_CHESS_BRICKS.get()).pattern("#  ").pattern("## ").pattern("###").unlockedBy("has_light_gray_chess_bricks", has(MSBlocks.LIGHT_GRAY_CHESS_BRICKS.get())).save(recipeBuilder);
		ShapedRecipeBuilder.shaped(MSBlocks.WHITE_CHESS_BRICK_STAIRS.get(), 4).define('#', MSBlocks.WHITE_CHESS_BRICKS.get()).pattern("#  ").pattern("## ").pattern("###").unlockedBy("has_white_chess_bricks", has(MSBlocks.WHITE_CHESS_BRICKS.get())).save(recipeBuilder);
		ShapedRecipeBuilder.shaped(MSBlocks.COARSE_STONE_STAIRS.get(), 4).define('#', MSBlocks.COARSE_STONE.get()).pattern("#  ").pattern("## ").pattern("###").unlockedBy("has_coarse_stone", has(MSBlocks.COARSE_STONE.get())).save(recipeBuilder);
		ShapedRecipeBuilder.shaped(MSBlocks.COARSE_STONE_BRICK_STAIRS.get(), 4).define('#', MSBlocks.COARSE_STONE_BRICKS.get()).pattern("#  ").pattern("## ").pattern("###").unlockedBy("has_coarse_stone_bricks", has(MSBlocks.COARSE_STONE_BRICKS.get())).save(recipeBuilder);
		ShapedRecipeBuilder.shaped(MSBlocks.SHADE_STAIRS.get(), 4).define('#', MSBlocks.SHADE_STONE.get()).pattern("#  ").pattern("## ").pattern("###").unlockedBy("has_shade_stone", has(MSBlocks.SHADE_STONE.get())).save(recipeBuilder);
		ShapedRecipeBuilder.shaped(MSBlocks.SHADE_BRICK_STAIRS.get(), 4).define('#', MSBlocks.SHADE_BRICKS.get()).pattern("#  ").pattern("## ").pattern("###").unlockedBy("has_shade_bricks", has(MSBlocks.SHADE_BRICKS.get())).save(recipeBuilder);
		ShapedRecipeBuilder.shaped(MSBlocks.FROST_TILE_STAIRS.get(), 4).define('#', MSBlocks.FROST_TILE.get()).pattern("#  ").pattern("## ").pattern("###").unlockedBy("has_frost_tile", has(MSBlocks.FROST_TILE.get())).save(recipeBuilder);
		ShapedRecipeBuilder.shaped(MSBlocks.FROST_BRICK_STAIRS.get(), 4).define('#', MSBlocks.FROST_BRICKS.get()).pattern("#  ").pattern("## ").pattern("###").unlockedBy("has_frost_bricks", has(MSBlocks.FROST_BRICKS.get())).save(recipeBuilder);
		ShapedRecipeBuilder.shaped(MSBlocks.CAST_IRON_STAIRS.get(), 4).define('#', MSBlocks.CAST_IRON.get()).pattern("#  ").pattern("## ").pattern("###").unlockedBy("has_cast_iron", has(MSBlocks.CAST_IRON.get())).save(recipeBuilder);
		ShapedRecipeBuilder.shaped(MSBlocks.MYCELIUM_STAIRS.get(), 4).define('#', MSBlocks.MYCELIUM_STONE.get()).pattern("#  ").pattern("## ").pattern("###").unlockedBy("has_mycelium_stone", has(MSBlocks.MYCELIUM_STONE.get())).save(recipeBuilder);
		ShapedRecipeBuilder.shaped(MSBlocks.MYCELIUM_BRICK_STAIRS.get(), 4).define('#', MSBlocks.MYCELIUM_BRICKS.get()).pattern("#  ").pattern("## ").pattern("###").unlockedBy("has_mycelium_bricks", has(MSBlocks.MYCELIUM_BRICKS.get())).save(recipeBuilder);
		ShapedRecipeBuilder.shaped(MSBlocks.BLACK_STONE_STAIRS.get(), 4).define('#', MSBlocks.BLACK_STONE.get()).pattern("#  ").pattern("## ").pattern("###").unlockedBy("has_black_stone", has(MSBlocks.BLACK_STONE.get())).save(recipeBuilder);
		ShapedRecipeBuilder.shaped(MSBlocks.BLACK_STONE_BRICK_STAIRS.get(), 4).define('#', MSBlocks.BLACK_STONE_BRICKS.get()).pattern("#  ").pattern("## ").pattern("###").unlockedBy("has_black_stone_bricks", has(MSBlocks.BLACK_STONE_BRICKS.get())).save(recipeBuilder);
		ShapedRecipeBuilder.shaped(MSBlocks.CHALK_STAIRS.get(), 4).define('#', MSBlocks.CHALK.get()).pattern("#  ").pattern("## ").pattern("###").unlockedBy("has_chalk", has(MSBlocks.CHALK.get())).save(recipeBuilder);
		ShapedRecipeBuilder.shaped(MSBlocks.CHALK_BRICK_STAIRS.get(), 4).define('#', MSBlocks.CHALK_BRICKS.get()).pattern("#  ").pattern("## ").pattern("###").unlockedBy("has_chalk_bricks", has(MSBlocks.CHALK_BRICKS.get())).save(recipeBuilder);
		ShapedRecipeBuilder.shaped(MSBlocks.PINK_STONE_STAIRS.get(), 4).define('#', MSBlocks.PINK_STONE.get()).pattern("#  ").pattern("## ").pattern("###").unlockedBy("has_pink_stone", has(MSBlocks.PINK_STONE.get())).save(recipeBuilder);
		ShapedRecipeBuilder.shaped(MSBlocks.PINK_STONE_BRICK_STAIRS.get(), 4).define('#', MSBlocks.PINK_STONE_BRICKS.get()).pattern("#  ").pattern("## ").pattern("###").unlockedBy("has_pink_stone_bricks", has(MSBlocks.PINK_STONE_BRICKS.get())).save(recipeBuilder);
		ShapedRecipeBuilder.shaped(MSBlocks.BROWN_STONE_STAIRS.get(), 4).define('#', MSBlocks.BROWN_STONE.get()).pattern("#  ").pattern("## ").pattern("###").unlockedBy("has_brown_stone", has(MSBlocks.BROWN_STONE.get())).save(recipeBuilder);
		ShapedRecipeBuilder.shaped(MSBlocks.BROWN_STONE_BRICK_STAIRS.get(), 4).define('#', MSBlocks.BROWN_STONE_BRICKS.get()).pattern("#  ").pattern("## ").pattern("###").unlockedBy("has_brown_stone_bricks", has(MSBlocks.BROWN_STONE_BRICKS.get())).save(recipeBuilder);
		ShapedRecipeBuilder.shaped(MSBlocks.GREEN_STONE_STAIRS.get(), 4).define('#', MSBlocks.GREEN_STONE.get()).pattern("#  ").pattern("## ").pattern("###").unlockedBy("has_green_stone", has(MSBlocks.GREEN_STONE.get())).save(recipeBuilder);
		ShapedRecipeBuilder.shaped(MSBlocks.GREEN_STONE_BRICK_STAIRS.get(), 4).define('#', MSBlocks.GREEN_STONE_BRICKS.get()).pattern("#  ").pattern("## ").pattern("###").unlockedBy("has_green_stone_bricks", has(MSBlocks.GREEN_STONE_BRICKS.get())).save(recipeBuilder);
		ShapedRecipeBuilder.shaped(MSBlocks.STEEP_GREEN_STONE_BRICK_STAIRS_BASE.get(), 2).define('#', MSBlocks.GREEN_STONE_BRICK_STAIRS.get()).pattern("##").unlockedBy("has_green_stone_brick_stairs", has(MSBlocks.GREEN_STONE_BRICK_STAIRS.get())).save(recipeBuilder);
		ShapedRecipeBuilder.shaped(MSBlocks.STEEP_GREEN_STONE_BRICK_STAIRS_TOP.get(), 2).define('#', MSBlocks.GREEN_STONE_BRICK_STAIRS.get()).pattern("#").pattern("#").unlockedBy("has_green_stone_brick_stairs", has(MSBlocks.GREEN_STONE_BRICK_STAIRS.get())).save(recipeBuilder);
		ShapedRecipeBuilder.shaped(MSBlocks.FLOWERY_MOSSY_STONE_BRICK_STAIRS.get(), 4).define('#', MSBlocks.FLOWERY_MOSSY_STONE_BRICKS.get()).pattern("#  ").pattern("## ").pattern("###").unlockedBy("has_flowery_mossy_bricks", has(MSBlocks.FLOWERY_MOSSY_STONE_BRICKS.get())).save(recipeBuilder);
		ShapedRecipeBuilder.shaped(MSBlocks.BLACK_CHESS_BRICK_SLAB.get(), 6).define('#', MSBlocks.BLACK_CHESS_BRICKS.get()).pattern("###").unlockedBy("has_black_chess_bricks", has(MSBlocks.BLACK_CHESS_BRICKS.get())).save(recipeBuilder);
		ShapedRecipeBuilder.shaped(MSBlocks.DARK_GRAY_CHESS_BRICK_SLAB.get(), 6).define('#', MSBlocks.DARK_GRAY_CHESS_BRICKS.get()).pattern("###").unlockedBy("has_dark_gray_chess_bricks", has(MSBlocks.DARK_GRAY_CHESS_BRICKS.get())).save(recipeBuilder);
		ShapedRecipeBuilder.shaped(MSBlocks.LIGHT_GRAY_CHESS_BRICK_SLAB.get(), 6).define('#', MSBlocks.LIGHT_GRAY_CHESS_BRICKS.get()).pattern("###").unlockedBy("has_light_gray_chess_bricks", has(MSBlocks.LIGHT_GRAY_CHESS_BRICKS.get())).save(recipeBuilder);
		ShapedRecipeBuilder.shaped(MSBlocks.WHITE_CHESS_BRICK_SLAB.get(), 6).define('#', MSBlocks.WHITE_CHESS_BRICKS.get()).pattern("###").unlockedBy("has_white_chess_bricks", has(MSBlocks.WHITE_CHESS_BRICKS.get())).save(recipeBuilder);
		ShapedRecipeBuilder.shaped(MSBlocks.COARSE_STONE_SLAB.get(), 6).define( '#', MSBlocks.COARSE_STONE.get()).pattern("###").unlockedBy("has_coarse_stone", has(MSBlocks.COARSE_STONE.get())).save(recipeBuilder);
		ShapedRecipeBuilder.shaped(MSBlocks.COARSE_STONE_BRICK_SLAB.get(), 6).define( '#', MSBlocks.COARSE_STONE_BRICKS.get()).pattern("###").unlockedBy("has_coarse_stone_bricks", has(MSBlocks.COARSE_STONE_BRICKS.get())).save(recipeBuilder);
		ShapedRecipeBuilder.shaped(MSBlocks.SHADE_SLAB.get(), 6).define('#', MSBlocks.SHADE_STONE.get()).pattern("###").unlockedBy("has_shade_stone", has(MSBlocks.SHADE_STONE.get())).save(recipeBuilder);
		ShapedRecipeBuilder.shaped(MSBlocks.SHADE_BRICK_SLAB.get(), 6).define('#', MSBlocks.SHADE_BRICKS.get()).pattern("###").unlockedBy("has_shade_bricks", has(MSBlocks.SHADE_BRICKS.get())).save(recipeBuilder);
		ShapedRecipeBuilder.shaped(MSBlocks.FROST_TILE_SLAB.get(), 6).define('#', MSBlocks.FROST_TILE.get()).pattern("###").unlockedBy("has_frost_tile", has(MSBlocks.FROST_TILE.get())).save(recipeBuilder);
		ShapedRecipeBuilder.shaped(MSBlocks.FROST_BRICK_SLAB.get(), 6).define('#', MSBlocks.FROST_BRICKS.get()).pattern("###").unlockedBy("has_frost_bricks", has(MSBlocks.FROST_BRICKS.get())).save(recipeBuilder);
		ShapedRecipeBuilder.shaped(MSBlocks.CHALK_SLAB.get(), 6).define('#', MSBlocks.CHALK.get()).pattern("###").unlockedBy("has_chalk", has(MSBlocks.CHALK.get())).save(recipeBuilder);
		ShapedRecipeBuilder.shaped(MSBlocks.CHALK_BRICK_SLAB.get(), 6).define('#', MSBlocks.CHALK_BRICKS.get()).pattern("###").unlockedBy("has_chalk_bricks", has(MSBlocks.CHALK_BRICKS.get())).save(recipeBuilder);
		ShapedRecipeBuilder.shaped(MSBlocks.BLACK_STONE_SLAB.get(), 6).define('#', MSBlocks.BLACK_STONE.get()).pattern("###").unlockedBy("has_black_stone", has(MSBlocks.BLACK_STONE.get())).save(recipeBuilder);
		ShapedRecipeBuilder.shaped(MSBlocks.BLACK_STONE_BRICK_SLAB.get(), 6).define('#', MSBlocks.BLACK_STONE_BRICKS.get()).pattern("###").unlockedBy("has_black_stone_bricks", has(MSBlocks.BLACK_STONE_BRICKS.get())).save(recipeBuilder);
		ShapedRecipeBuilder.shaped(MSBlocks.MYCELIUM_SLAB.get(), 6).define('#', MSBlocks.MYCELIUM_STONE.get()).pattern("###").unlockedBy("has_mycelium_stone", has(MSBlocks.MYCELIUM_STONE.get())).save(recipeBuilder);
		ShapedRecipeBuilder.shaped(MSBlocks.MYCELIUM_BRICK_SLAB.get(), 6).define('#', MSBlocks.MYCELIUM_BRICKS.get()).pattern("###").unlockedBy("has_mycelium_bricks", has(MSBlocks.MYCELIUM_BRICKS.get())).save(recipeBuilder);
		ShapedRecipeBuilder.shaped(MSBlocks.PINK_STONE_SLAB.get(), 6).define('#', MSBlocks.PINK_STONE.get()).pattern("###").unlockedBy("has_pink_stone", has(MSBlocks.PINK_STONE.get())).save(recipeBuilder);
		ShapedRecipeBuilder.shaped(MSBlocks.PINK_STONE_BRICK_SLAB.get(), 6).define('#', MSBlocks.PINK_STONE_BRICKS.get()).pattern("###").unlockedBy("has_pink_stone_bricks", has(MSBlocks.PINK_STONE_BRICKS.get())).save(recipeBuilder);
		ShapedRecipeBuilder.shaped(MSBlocks.BROWN_STONE_SLAB.get(), 6).define('#', MSBlocks.BROWN_STONE.get()).pattern("###").unlockedBy("has_brown_stone", has(MSBlocks.BROWN_STONE.get())).save(recipeBuilder);
		ShapedRecipeBuilder.shaped(MSBlocks.BROWN_STONE_BRICK_SLAB.get(), 6).define('#', MSBlocks.BROWN_STONE_BRICKS.get()).pattern("###").unlockedBy("has_brown_stone_bricks", has(MSBlocks.BROWN_STONE_BRICKS.get())).save(recipeBuilder);
		ShapedRecipeBuilder.shaped(MSBlocks.GREEN_STONE_SLAB.get(), 6).define('#', MSBlocks.GREEN_STONE.get()).pattern("###").unlockedBy("has_green_stone", has(MSBlocks.GREEN_STONE.get())).save(recipeBuilder);
		ShapedRecipeBuilder.shaped(MSBlocks.GREEN_STONE_BRICK_SLAB.get(), 6).define('#', MSBlocks.GREEN_STONE_BRICKS.get()).pattern("###").unlockedBy("has_green_stone_bricks", has(MSBlocks.GREEN_STONE_BRICKS.get())).save(recipeBuilder);
		ShapedRecipeBuilder.shaped(MSBlocks.FLOWERY_MOSSY_STONE_BRICK_SLAB.get(), 6).define('#', MSBlocks.FLOWERY_MOSSY_STONE_BRICKS.get()).pattern("###").unlockedBy("has_flowery_mossy_stone_bricks", has(MSBlocks.FLOWERY_MOSSY_STONE_BRICKS.get())).save(recipeBuilder);
		ShapedRecipeBuilder.shaped(MSBlocks.SANDSTONE_COLUMN.get(), 3).define('#', Items.SANDSTONE).pattern("#").pattern("#").pattern("#").unlockedBy("has_sandstone", has(Items.SANDSTONE)).save(recipeBuilder);
		ShapedRecipeBuilder.shaped(MSBlocks.CHISELED_SANDSTONE_COLUMN.get(), 3).define('#', Items.CHISELED_SANDSTONE).pattern("#").pattern("#").pattern("#").unlockedBy("has_chiseled_sandstone", has(Items.CHISELED_SANDSTONE)).save(recipeBuilder);
		ShapedRecipeBuilder.shaped(MSBlocks.RED_SANDSTONE_COLUMN.get(), 3).define('#', Items.RED_SANDSTONE).pattern("#").pattern("#").pattern("#").unlockedBy("has_red_sandstone", has(Items.RED_SANDSTONE)).save(recipeBuilder);
		ShapedRecipeBuilder.shaped(MSBlocks.CHISELED_RED_SANDSTONE_COLUMN.get(), 3).define('#', Items.CHISELED_RED_SANDSTONE).pattern("#").pattern("#").pattern("#").unlockedBy("has_chiseled_red_sandstone", has(Items.CHISELED_RED_SANDSTONE)).save(recipeBuilder);
		
		ShapedRecipeBuilder.shaped(MSBlocks.SPIKES.get()).define('#', Items.IRON_INGOT).define('n', Items.IRON_NUGGET).pattern("n n").pattern("# #").pattern("###").unlockedBy("has_iron_ingot", has(Items.IRON_INGOT)).save(recipeBuilder);
		ShapedRecipeBuilder.shaped(MSBlocks.RETRACTABLE_SPIKES.get()).define('#', Items.IRON_INGOT).define('p', Items.PISTON).define('s', MSBlocks.SPIKES.get()).define('w', ItemTags.WOODEN_PRESSURE_PLATES).pattern(" s ").pattern("#w#").pattern("#p#").unlockedBy("has_spikes", has(MSBlocks.SPIKES.get())).save(recipeBuilder);
		
		ShapedRecipeBuilder.shaped(MSBlocks.GLOWING_WOOD.get(), 3).define('#', MSBlocks.GLOWING_LOG.get()).pattern("##").pattern("##").group("bark").unlockedBy("has_log", has(MSTags.Items.GLOWING_LOGS)).save(recipeBuilder);
		ShapelessRecipeBuilder.shapeless(MSBlocks.GLOWING_PLANKS.get(), 4).requires(MSTags.Items.GLOWING_LOGS).group("planks").unlockedBy("has_log", has(MSTags.Items.GLOWING_LOGS)).save(recipeBuilder);
		ShapedRecipeBuilder.shaped(MSBlocks.FROST_WOOD.get(), 3).define('#', MSBlocks.FROST_LOG.get()).pattern("##").pattern("##").group("bark").unlockedBy("has_log", has(MSTags.Items.FROST_LOGS)).save(recipeBuilder);
		ShapelessRecipeBuilder.shapeless(MSBlocks.FROST_PLANKS.get(), 4).requires(MSTags.Items.FROST_LOGS).group("planks").unlockedBy("has_log", has(MSTags.Items.FROST_LOGS)).save(recipeBuilder);
		ShapedRecipeBuilder.shaped(MSBlocks.RAINBOW_WOOD.get(), 3).define('#', MSBlocks.RAINBOW_LOG.get()).pattern("##").pattern("##").group("bark").unlockedBy("has_log", has(MSTags.Items.RAINBOW_LOGS)).save(recipeBuilder);
		ShapelessRecipeBuilder.shapeless(MSBlocks.RAINBOW_PLANKS.get(), 4).requires(MSTags.Items.RAINBOW_LOGS).group("planks").unlockedBy("has_log", has(MSTags.Items.RAINBOW_LOGS)).save(recipeBuilder);
		ShapedRecipeBuilder.shaped(MSBlocks.END_WOOD.get(), 3).define('#', MSBlocks.END_LOG.get()).pattern("##").pattern("##").group("bark").unlockedBy("has_log", has(MSTags.Items.END_LOGS)).save(recipeBuilder);
		ShapelessRecipeBuilder.shapeless(MSBlocks.END_PLANKS.get(), 4).requires(MSTags.Items.END_LOGS).group("planks").unlockedBy("has_log", has(MSTags.Items.END_LOGS)).save(recipeBuilder);
		ShapedRecipeBuilder.shaped(MSBlocks.VINE_WOOD.get(), 3).define('#', MSBlocks.VINE_LOG.get()).pattern("##").pattern("##").group("bark").unlockedBy("has_log", has(MSTags.Items.VINE_LOGS)).save(recipeBuilder);
		ShapedRecipeBuilder.shaped(MSBlocks.FLOWERY_VINE_WOOD.get(), 3).define('#', MSBlocks.FLOWERY_VINE_LOG.get()).pattern("##").pattern("##").group("bark").unlockedBy("has_log", has(MSTags.Items.FLOWERY_VINE_LOGS)).save(recipeBuilder);
		ShapedRecipeBuilder.shaped(MSBlocks.DEAD_WOOD.get(), 3).define('#', MSBlocks.DEAD_LOG.get()).pattern("##").pattern("##").group("bark").unlockedBy("has_log", has(MSTags.Items.DEAD_LOGS)).save(recipeBuilder);
		ShapelessRecipeBuilder.shapeless(MSBlocks.DEAD_PLANKS.get(), 4).requires(MSTags.Items.DEAD_LOGS).group("planks").unlockedBy("has_log", has(MSTags.Items.DEAD_LOGS)).save(recipeBuilder);
		ShapedRecipeBuilder.shaped(MSBlocks.PETRIFIED_WOOD.get(), 3).define('#', MSBlocks.PETRIFIED_LOG.get()).pattern("##").pattern("##").group("bark").unlockedBy("has_log", has(MSTags.Items.PETRIFIED_LOGS)).save(recipeBuilder);
		ShapelessRecipeBuilder.shapeless(Items.OAK_PLANKS).requires(MSBlocks.TREATED_PLANKS.get()).requires(Items.WATER_BUCKET).group("planks").unlockedBy("has_log", has(MSBlocks.TREATED_PLANKS.get())).save(recipeBuilder, new ResourceLocation(Minestuck.MOD_ID, "oak_planks_from_treated_planks"));
		ShapedRecipeBuilder.shaped(MSBlocks.RAINBOW_PLANKS_STAIRS.get(), 4).define('#', MSBlocks.RAINBOW_PLANKS.get()).pattern("#  ").pattern("## ").pattern("###").group("wooden_stairs").unlockedBy("has_planks", has(MSBlocks.RAINBOW_PLANKS.get())).save(recipeBuilder);
		ShapedRecipeBuilder.shaped(MSBlocks.END_PLANKS_STAIRS.get(), 4).define('#', MSBlocks.END_PLANKS.get()).pattern("#  ").pattern("## ").pattern("###").group("wooden_stairs").unlockedBy("has_planks", has(MSBlocks.END_PLANKS.get())).save(recipeBuilder);
		ShapedRecipeBuilder.shaped(MSBlocks.DEAD_PLANKS_STAIRS.get(), 4).define('#', MSBlocks.DEAD_PLANKS.get()).pattern("#  ").pattern("## ").pattern("###").group("wooden_stairs").unlockedBy("has_planks", has(MSBlocks.DEAD_PLANKS.get())).save(recipeBuilder);
		ShapedRecipeBuilder.shaped(MSBlocks.TREATED_PLANKS_STAIRS.get(), 4).define('#', MSBlocks.TREATED_PLANKS.get()).pattern("#  ").pattern("## ").pattern("###").group("wooden_stairs").unlockedBy("has_planks", has(MSBlocks.TREATED_PLANKS.get())).save(recipeBuilder);
		ShapedRecipeBuilder.shaped(MSBlocks.RAINBOW_PLANKS_SLAB.get(), 6).define('#', MSBlocks.RAINBOW_PLANKS.get()).pattern("###").group("wooden_slab").unlockedBy("has_planks", has(MSBlocks.RAINBOW_PLANKS.get())).save(recipeBuilder);
		ShapedRecipeBuilder.shaped(MSBlocks.END_PLANKS_SLAB.get(), 6).define('#', MSBlocks.END_PLANKS.get()).pattern("###").group("wooden_slab").unlockedBy("has_planks", has(MSBlocks.END_PLANKS.get())).save(recipeBuilder);
		ShapedRecipeBuilder.shaped(MSBlocks.DEAD_PLANKS_SLAB.get(), 6).define('#', MSBlocks.DEAD_PLANKS.get()).pattern("###").group("wooden_slab").unlockedBy("has_planks", has(MSBlocks.DEAD_PLANKS.get())).save(recipeBuilder);
		ShapedRecipeBuilder.shaped(MSBlocks.TREATED_PLANKS_SLAB.get(), 6).define('#', MSBlocks.TREATED_PLANKS.get()).pattern("###").group("wooden_slab").unlockedBy("has_planks", has(MSBlocks.TREATED_PLANKS.get())).save(recipeBuilder);
		
		ShapelessRecipeBuilder.shapeless(MSBlocks.COMPUTER.get()).requires(MSItems.ENERGY_CORE.get()).requires(MSItems.COMPUTER_PARTS.get()).unlockedBy("has_computer_parts", has(MSItems.COMPUTER_PARTS.get())).requires(Items.QUARTZ).requires(Items.REDSTONE).requires(MSItems.RAW_CRUXITE.get()).save(recipeBuilder);
		ShapedRecipeBuilder.shaped(MSBlocks.URANIUM_COOKER.get()).define('e', MSItems.ENERGY_CORE.get()).define('i', Tags.Items.INGOTS_IRON).define('F', Items.FURNACE).pattern("iii").pattern("iFi").pattern("iei").unlockedBy("has_energy_core", has(MSItems.ENERGY_CORE.get())).save(recipeBuilder);
		
		ShapelessRecipeBuilder.shapeless(Items.SPRUCE_PLANKS).requires(MSBlocks.WOODEN_CACTUS.get()).group("planks").unlockedBy("has_cactus", has(MSBlocks.WOODEN_CACTUS.get())).save(recipeBuilder, new ResourceLocation(Minestuck.MOD_ID, "spruce_planks_from_wooden_cactus"));
		ShapedRecipeBuilder.shaped(MSBlocks.APPLE_CAKE.get()).define('m', Items.MILK_BUCKET).define('a', Items.APPLE).define('w', Items.WHEAT).define('e', Items.EGG).pattern("mmm").pattern("aea").pattern("www").unlockedBy("has_egg", has(Items.EGG)).save(recipeBuilder);
		ShapedRecipeBuilder.shaped(MSBlocks.CHESSBOARD.get()).define('W', Items.WHITE_TERRACOTTA).define('B', Items.BLACK_TERRACOTTA).pattern("WBW").pattern("BWB").pattern("WBW").group("chessboard").unlockedBy("has_white_terracotta", has(Items.WHITE_TERRACOTTA)).unlockedBy("has_black_terracotta", has(Items.BLACK_TERRACOTTA)).save(recipeBuilder, new ResourceLocation(Minestuck.MOD_ID, "chessboard_white"));
		ShapedRecipeBuilder.shaped(MSBlocks.CHESSBOARD.get()).define('W', Items.WHITE_TERRACOTTA).define('B', Items.BLACK_TERRACOTTA).pattern("BWB").pattern("WBW").pattern("BWB").group("chessboard").unlockedBy("has_white_terracotta", has(Items.WHITE_TERRACOTTA)).unlockedBy("has_black_terracotta", has(Items.BLACK_TERRACOTTA)).save(recipeBuilder, new ResourceLocation(Minestuck.MOD_ID, "chessboard_black"));
		ShapedRecipeBuilder.shaped(MSItems.CLOTHES_IRON.get()).define('s', Tags.Items.STONE).define('i', Tags.Items.INGOTS_IRON).define('r', Tags.Items.RODS_WOODEN).pattern(" rr").pattern("sss").pattern("iii").unlockedBy("has_stick", has(Tags.Items.RODS_WOODEN)).unlockedBy("has_iron_ingot", has(Tags.Items.INGOTS_IRON)).save(recipeBuilder);
		ShapedRecipeBuilder.shaped(MSItems.HORN.get()).define('w', Items.WHITE_WOOL).define('i', Tags.Items.INGOTS_IRON).define('r', Tags.Items.RODS_WOODEN).pattern("  i").pattern(" r ").pattern("w  ").unlockedBy("has_stick", has(Tags.Items.RODS_WOODEN)).unlockedBy("has_iron_ingot", has(Tags.Items.INGOTS_IRON)).unlockedBy("has_white_wool", has(Items.WHITE_WOOL)).save(recipeBuilder);
		ShapelessRecipeBuilder.shapeless(MSItems.BI_DYE.get(), 2).requires(Items.BLUE_DYE).unlockedBy("has_blue_dye", has(Items.BLUE_DYE)).requires(Items.PINK_DYE).unlockedBy("has_pink_dye", has(Items.PINK_DYE)).requires(Items.PURPLE_DYE).unlockedBy("has_purple_dye", has(Items.PURPLE_DYE)).save(recipeBuilder);
		ShapedRecipeBuilder.shaped(MSItems.LIP_BALM.get()).define('n', Items.IRON_NUGGET).define('w', Items.HONEYCOMB).pattern("nwn").pattern(" n ").unlockedBy("has_honeycomb", has(Items.HONEYCOMB)).unlockedBy("has_iron_nugget", has(Items.IRON_NUGGET)).save(recipeBuilder);
		
		ShapedRecipeBuilder.shaped(MSItems.CLAW_HAMMER.get()).define('i', Tags.Items.INGOTS_IRON).define('s', Tags.Items.RODS_WOODEN).pattern(" ii").pattern("is ").pattern(" s ").unlockedBy("has_iron_ingot", has(Tags.Items.INGOTS_IRON)).save(recipeBuilder);
		ShapedRecipeBuilder.shaped(MSItems.SLEDGE_HAMMER.get()).define('i', Tags.Items.INGOTS_IRON).define('s', Tags.Items.RODS_WOODEN).define('S', Tags.Items.STONE).pattern("iSi").pattern(" s ").pattern(" s ").unlockedBy("has_iron_ingot", has(Tags.Items.INGOTS_IRON)).save(recipeBuilder);
		ShapedRecipeBuilder.shaped(MSItems.KATANA.get()).define('i', Tags.Items.INGOTS_IRON).define('s', Tags.Items.RODS_WOODEN).pattern("  i").pattern(" i ").pattern("s  ").unlockedBy("has_iron_ingot", has(Tags.Items.INGOTS_IRON)).save(recipeBuilder);
		ShapedRecipeBuilder.shaped(MSItems.DAGGER.get()).define('i', Tags.Items.INGOTS_IRON).define('s', Tags.Items.RODS_WOODEN).pattern("s").pattern("i").pattern("i").unlockedBy("has_iron_ingot", has(Tags.Items.INGOTS_IRON)).save(recipeBuilder);
		ShapedRecipeBuilder.shaped(MSItems.MACUAHUITL.get()).define('P', ItemTags.PLANKS).define('s', Tags.Items.RODS_WOODEN).define('O', Items.OBSIDIAN).pattern("OPO").pattern("OPO").pattern(" s ").unlockedBy("has_stick", has(Tags.Items.RODS_WOODEN)).save(recipeBuilder);
		ShapedRecipeBuilder.shaped(MSItems.OBSIDIAN_AXE_KNIFE.get()).define('s', Tags.Items.RODS_WOODEN).define('O', Items.OBSIDIAN).pattern("  O").pattern(" Os").pattern("Os ").unlockedBy("has_stick", has(Tags.Items.RODS_WOODEN)).save(recipeBuilder);
		ShapedRecipeBuilder.shaped(MSItems.SICKLE.get()).define('i', Tags.Items.INGOTS_IRON).define('s', Tags.Items.RODS_WOODEN).pattern("ii ").pattern("  i").pattern(" s ").unlockedBy("has_iron_ingot", has(Tags.Items.INGOTS_IRON)).save(recipeBuilder);
		ShapedRecipeBuilder.shaped(MSItems.DEUCE_CLUB.get()).define('P', ItemTags.PLANKS).define('s', Tags.Items.RODS_WOODEN).pattern("  P").pattern(" s ").pattern("s  ").unlockedBy("has_stick", has(Tags.Items.RODS_WOODEN)).save(recipeBuilder);
		ShapedRecipeBuilder.shaped(MSItems.CANE.get()).define('s', Tags.Items.RODS_WOODEN).pattern("  s").pattern(" s ").pattern("s  ").unlockedBy("has_stick", has(Tags.Items.RODS_WOODEN)).save(recipeBuilder);
		ShapedRecipeBuilder.shaped(MSItems.BEAR_POKING_STICK.get()).define('s', Tags.Items.RODS_WOODEN).define('l', Tags.Items.LEATHER).pattern("  s").pattern(" s ").pattern("l  ").unlockedBy("has_stick", has(Tags.Items.RODS_WOODEN)).save(recipeBuilder);
		ShapedRecipeBuilder.shaped(MSItems.UMBRELLA.get()).define('s', Tags.Items.RODS_WOODEN).define('w', Items.BLACK_WOOL).pattern(" w ").pattern("wsw").pattern(" s ").unlockedBy("has_stick", has(Tags.Items.RODS_WOODEN)).save(recipeBuilder);
		ShapedRecipeBuilder.shaped(MSItems.FAN.get()).define('s', Tags.Items.RODS_WOODEN).define('p', Items.PAPER).pattern("  p").pattern(" pp").pattern("pps").unlockedBy("has_stick", has(Tags.Items.RODS_WOODEN)).save(recipeBuilder);
		ShapedRecipeBuilder.shaped(MSItems.IRON_CANE.get()).define('i', Tags.Items.INGOTS_IRON).define('c', MSItems.CANE.get()).pattern("  i").pattern(" c ").pattern("i  ").unlockedBy("has_iron_ingot", has(Tags.Items.INGOTS_IRON)).save(recipeBuilder);
		ShapedRecipeBuilder.shaped(MSItems.WOODEN_SPOON.get()).define('s', Tags.Items.RODS_WOODEN).define('b', Items.BOWL).pattern("b").pattern("s").pattern("s").unlockedBy("has_stick", has(Tags.Items.RODS_WOODEN)).save(recipeBuilder);
		ShapedRecipeBuilder.shaped(MSItems.FORK.get()).define('S', Tags.Items.STONE).pattern("S S").pattern(" S ").pattern(" S ").unlockedBy("has_stone", has(Tags.Items.STONE)).save(recipeBuilder);
		ShapedRecipeBuilder.shaped(MSItems.POINTY_STICK.get(), 1).define('s', Tags.Items.RODS_WOODEN).pattern("s").pattern("s").pattern("s").unlockedBy("has_stick", has(Tags.Items.RODS_WOODEN)).save(recipeBuilder);
		ShapedRecipeBuilder.shaped(MSItems.KNITTING_NEEDLE.get(), 2).define('i', Items.IRON_INGOT).define('n', Items.IRON_NUGGET).pattern("  n").pattern(" i ").pattern("i  ").unlockedBy("has_iron_ingot", has(Items.IRON_INGOT)).save(recipeBuilder);
		ShapedRecipeBuilder.shaped(MSItems.SHURIKEN.get(), 2).define('i', Items.IRON_INGOT).define('n', Items.IRON_NUGGET).pattern(" i ").pattern("ini").pattern(" i ").unlockedBy("has_iron_ingot", has(Items.IRON_INGOT)).save(recipeBuilder);
		
		ShapedRecipeBuilder.shaped(MSItems.ENERGY_CORE.get()).define('u', ExtraForgeTags.Items.URANIUM_CHUNKS).define('c', MSItems.RAW_CRUXITE.get()).pattern("cuc").pattern("ucu").pattern("cuc").unlockedBy("has_raw_uranium", has(MSItems.RAW_URANIUM.get())).save(recipeBuilder);
		ShapedRecipeBuilder.shaped(MSItems.PLUTONIUM_CORE.get()).define('e', MSItems.ENERGY_CORE.get()).define('i', Items.IRON_INGOT).define('n', Items.IRON_NUGGET).define('p', Items.ENDER_PEARL).pattern("nin").pattern("pep").pattern("nin").unlockedBy("has_energy_core", has(MSItems.ENERGY_CORE.get())).save(recipeBuilder);
		ShapedRecipeBuilder.shaped(MSItems.TEMPLE_SCANNER.get()).define('e', MSItems.ENERGY_CORE.get()).define('c', MSItems.RAW_CRUXITE.get()).define('i', Items.IRON_INGOT).define('r', Items.REDSTONE).define('g', Items.GLASS_PANE).pattern("igi").pattern("iei").pattern("crc").unlockedBy("has_energy_core", has(MSItems.ENERGY_CORE.get())).save(recipeBuilder);
		ShapedRecipeBuilder.shaped(MSItems.SERVER_DISK.get()).define('d', MSItems.BLANK_DISK.get()).define('c', MSItems.SBURB_CODE.get()).pattern(" c ").pattern("cdc").pattern(" c ").unlockedBy("has_sburb_code", has(MSItems.SBURB_CODE.get())).save(recipeBuilder);
		ShapedRecipeBuilder.shaped(MSItems.CLIENT_DISK.get()).define('d', MSItems.BLANK_DISK.get()).define('c', MSItems.SBURB_CODE.get()).pattern("c c").pattern(" d ").pattern("c c").unlockedBy("has_sburb_code", has(MSItems.SBURB_CODE.get())).save(recipeBuilder);
		ShapedRecipeBuilder.shaped(MSItems.MAILBOX.get()).define('c', Ingredient.of(Tags.Items.CHESTS)).define('r', Ingredient.of(Tags.Items.RODS)).define('i', Items.IRON_INGOT).pattern("ici").pattern(" r ").pattern(" r ").unlockedBy("has_chest", has(Tags.Items.CHESTS)).save(recipeBuilder);
		ShapedRecipeBuilder.shaped(MSItems.CAPTCHA_CARD.get()).define('p', Items.PAPER).define('c', MSItems.RAW_CRUXITE.get()).pattern("ppp").pattern("pcp").pattern("ppp").unlockedBy("has_raw_cruxite", has(MSItems.RAW_CRUXITE.get())).save(recipeBuilder);
		//TODO custom ingredient type to disallow punched cards or cards with items to be used here
		NonMirroredRecipeBuilder.nonMirroredRecipe(MSItems.STACK_MODUS_CARD.get()).define('a', Ingredient.of(MSItems.CAPTCHA_CARD.get())).define('c', MSItems.RAW_CRUXITE.get()).define('C', MSBlocks.CRUXITE_BLOCK.get()).pattern("Cac").unlockedBy("has_card", has(MSItems.CAPTCHA_CARD.get())).save(recipeBuilder);
		NonMirroredRecipeBuilder.nonMirroredRecipe(MSItems.QUEUE_MODUS_CARD.get()).define('a', Ingredient.of(MSItems.CAPTCHA_CARD.get())).define('c', MSItems.RAW_CRUXITE.get()).define('C', MSBlocks.CRUXITE_BLOCK.get()).pattern("caC").unlockedBy("has_card", has(MSItems.CAPTCHA_CARD.get())).save(recipeBuilder);
		
		ShapelessRecipeBuilder.shapeless(MSItems.BUG_ON_A_STICK.get(), 3).requires(Ingredient.of(Tags.Items.RODS_WOODEN), 3).requires(MSItems.JAR_OF_BUGS.get()).unlockedBy("has_jag_of_bugs", has(MSItems.JAR_OF_BUGS.get())).save(recipeBuilder);
		ShapedRecipeBuilder.shaped(MSItems.GRUB_SAUCE.get(),3).define('p', Items.PAPER).define('b', MSTags.Items.BUGS).pattern("bbb").pattern("p p").pattern(" p ").unlockedBy("has_bugs", has(MSTags.Items.BUGS)).save(recipeBuilder);
		ShapelessRecipeBuilder.shapeless(MSItems.BUG_MAC.get(), 2).requires(Items.BREAD, 1).requires(MSTags.Items.BUGS).requires(MSTags.Items.BUGS).requires(MSItems.FRENCH_FRY.get(), 4).requires(MSItems.GRUB_SAUCE.get(), 1).unlockedBy("has_grub_sauce", has(MSItems.GRUB_SAUCE.get())).save(recipeBuilder);
		ShapelessRecipeBuilder.shapeless(MSItems.SALAD.get()).requires(Items.BOWL).requires(ItemTags.LEAVES).unlockedBy("has_bowl", has(Items.BOWL)).save(recipeBuilder);
		
		ShapelessRecipeBuilder.shapeless(MSBlocks.TRANS_PORTALIZER.get()).requires(MSBlocks.TRANSPORTALIZER.get()).requires(Items.PINK_DYE).requires(Items.LIGHT_BLUE_DYE).unlockedBy("has_transportalizer", has(MSBlocks.TRANSPORTALIZER.get())).save(recipeBuilder);
		ShapedRecipeBuilder.shaped(MSBlocks.WIRELESS_REDSTONE_TRANSMITTER.get()).define('e', MSItems.PLUTONIUM_CORE.get()).define('i', Items.IRON_INGOT).define('c', Items.COMPARATOR).define('q', MSItems.COMPUTER_PARTS.get()).pattern("cqc").pattern("iei").pattern("iii").unlockedBy("has_plutonium_core", has(MSItems.PLUTONIUM_CORE.get())).save(recipeBuilder);
		ShapedRecipeBuilder.shaped(MSBlocks.WIRELESS_REDSTONE_RECEIVER.get()).define('c', Items.COMPARATOR).define('i', Items.IRON_INGOT).define('r', Items.REDSTONE).define('q', Items.QUARTZ).define('s', Tags.Items.STONE).pattern("rqr").pattern("scs").pattern("ici").unlockedBy("has_plutonium_core", has(MSItems.PLUTONIUM_CORE.get())).save(recipeBuilder);
		ShapedRecipeBuilder.shaped(MSBlocks.SOLID_SWITCH.get(), 2).define('g', Items.GLOWSTONE).define('i', Items.IRON_INGOT).define('r', Items.REDSTONE).pattern("iri").pattern("rgr").pattern("iri").unlockedBy("has_glowstone", has(Items.GLOWSTONE)).save(recipeBuilder);
		ShapelessRecipeBuilder.shapeless(MSBlocks.VARIABLE_SOLID_SWITCH.get()).requires(MSBlocks.SOLID_SWITCH.get()).requires(Items.COMPARATOR).unlockedBy("has_solid_switch", has(MSBlocks.SOLID_SWITCH.get())).save(recipeBuilder);
		ShapelessRecipeBuilder.shapeless(MSBlocks.ONE_SECOND_INTERVAL_TIMED_SOLID_SWITCH.get()).requires(MSBlocks.SOLID_SWITCH.get()).requires(Items.CLOCK).requires(Items.REPEATER).unlockedBy("has_solid_switch", has(MSBlocks.SOLID_SWITCH.get())).save(recipeBuilder);
		ShapelessRecipeBuilder.shapeless(MSBlocks.TWO_SECOND_INTERVAL_TIMED_SOLID_SWITCH.get()).requires(MSBlocks.SOLID_SWITCH.get()).requires(Items.CLOCK).requires(Items.REPEATER).requires(Items.REPEATER).unlockedBy("has_solid_switch", has(MSBlocks.SOLID_SWITCH.get())).save(recipeBuilder);
		ShapedRecipeBuilder.shaped(MSBlocks.ITEM_MAGNET.get()).define('e', MSItems.PLUTONIUM_CORE.get()).define('i', Items.IRON_INGOT).define('r', Items.REDSTONE).define('g', Items.GLASS_PANE).pattern("iri").pattern("geg").pattern("iri").unlockedBy("has_plutonium_core", has(MSItems.PLUTONIUM_CORE.get())).save(recipeBuilder);
		ShapedRecipeBuilder.shaped(MSBlocks.REDSTONE_CLOCK.get()).define('c', Items.CLOCK).define('i', Items.IRON_INGOT).define('r', Items.REPEATER).define('q', Items.QUARTZ).define('P', ItemTags.PLANKS).pattern("PiP").pattern("rcr").pattern("PqP").unlockedBy("has_clock", has(Items.CLOCK)).save(recipeBuilder);
		ShapedRecipeBuilder.shaped(MSBlocks.ROTATOR.get()).define('p', Items.PISTON).define('i', Items.IRON_INGOT).define('r', Items.REDSTONE).pattern("ipi").pattern("rir").pattern("iri").unlockedBy("has_piston", has(Items.PISTON)).save(recipeBuilder);
		ShapedRecipeBuilder.shaped(MSBlocks.TOGGLER.get()).define('t', Items.REPEATER).define('i', Items.IRON_INGOT).define('r', Items.REDSTONE).pattern("iti").pattern("rir").pattern("iri").unlockedBy("has_piston", has(Items.PISTON)).save(recipeBuilder);
		ShapedRecipeBuilder.shaped(MSBlocks.REMOTE_COMPARATOR.get()).define('e', MSItems.PLUTONIUM_CORE.get()).define('o', Items.OBSERVER).define('i', Items.IRON_INGOT).define('c', Items.COMPARATOR).pattern("ici").pattern("oeo").pattern("iii").unlockedBy("has_plutonium_core", has(MSItems.PLUTONIUM_CORE.get())).save(recipeBuilder);
		ShapedRecipeBuilder.shaped(MSBlocks.BLOCK_PRESSURE_PLATE.get()).define('p', Items.STONE_PRESSURE_PLATE).define('s', Items.STONE).define('i', Items.IRON_INGOT).define('r', Items.REDSTONE).pattern("sps").pattern("sis").pattern("srs").unlockedBy("has_stone_pressure_plate", has(Items.STONE_PRESSURE_PLATE)).save(recipeBuilder);
		
		SimpleCookingRecipeBuilder.smelting(Ingredient.of(MSBlocks.COARSE_STONE_BRICKS.get()), MSBlocks.CRACKED_COARSE_STONE_BRICKS.get(), 0.1f, 200).unlockedBy("has_coarse_stone_bricks", has(MSBlocks.COARSE_STONE_BRICKS.get())).save(recipeBuilder);
		SimpleCookingRecipeBuilder.smelting(Ingredient.of(MSBlocks.SHADE_STONE.get()), MSBlocks.SMOOTH_SHADE_STONE.get(), 0.1f, 200).unlockedBy("has_shade_stone", has(MSBlocks.SHADE_STONE.get())).save(recipeBuilder);
		SimpleCookingRecipeBuilder.smelting(Ingredient.of(MSBlocks.SHADE_BRICKS.get()), MSBlocks.CRACKED_SHADE_BRICKS.get(), 0.1f, 200).unlockedBy("has_shade_bricks", has(MSBlocks.SHADE_BRICKS.get())).save(recipeBuilder);
		SimpleCookingRecipeBuilder.smelting(Ingredient.of(MSBlocks.FROST_BRICKS.get()), MSBlocks.CRACKED_FROST_BRICKS.get(), 0.1f, 100).unlockedBy("has_frost_bricks", has(MSBlocks.FROST_BRICKS.get())).save(recipeBuilder);
		SimpleCookingRecipeBuilder.smelting(Ingredient.of(MSBlocks.MYCELIUM_COBBLESTONE.get()), MSBlocks.MYCELIUM_STONE.get(), 0.1f, 200).unlockedBy("has_mycelium_cobblestone", has(MSBlocks.MYCELIUM_COBBLESTONE.get())).save(recipeBuilder);
		SimpleCookingRecipeBuilder.smelting(Ingredient.of(MSBlocks.MYCELIUM_STONE.get()), MSBlocks.POLISHED_MYCELIUM_STONE.get(), 0.1f, 200).unlockedBy("has_mycelium_stone", has(MSBlocks.MYCELIUM_STONE.get())).save(recipeBuilder);
		SimpleCookingRecipeBuilder.smelting(Ingredient.of(MSBlocks.MYCELIUM_BRICKS.get()), MSBlocks.CRACKED_MYCELIUM_BRICKS.get(), 0.1f, 200).unlockedBy("has_mycelium_bricks", has(MSBlocks.MYCELIUM_BRICKS.get())).save(recipeBuilder);
		SimpleCookingRecipeBuilder.smelting(Ingredient.of(MSBlocks.BLACK_COBBLESTONE.get()), MSBlocks.BLACK_STONE.get(), 0.1f, 200).unlockedBy("has_black_cobblestone", has(MSBlocks.BLACK_COBBLESTONE.get())).save(recipeBuilder);
		SimpleCookingRecipeBuilder.smelting(Ingredient.of(MSBlocks.BLACK_STONE.get()), MSBlocks.POLISHED_BLACK_STONE.get(), 0.1f, 200).unlockedBy("has_black_stone", has(MSBlocks.BLACK_STONE.get())).save(recipeBuilder);
		SimpleCookingRecipeBuilder.smelting(Ingredient.of(MSBlocks.BLACK_STONE_BRICKS.get()), MSBlocks.CRACKED_BLACK_STONE_BRICKS.get(), 0.1f, 200).unlockedBy("has_black_stone_bricks", has(MSBlocks.BLACK_STONE_BRICKS.get())).save(recipeBuilder);
		SimpleCookingRecipeBuilder.smelting(Ingredient.of(MSBlocks.PINK_STONE.get()), MSBlocks.POLISHED_PINK_STONE.get(), 0.1F, 200).unlockedBy("has_pink_stone", has(MSBlocks.PINK_STONE.get())).save(recipeBuilder);
		SimpleCookingRecipeBuilder.smelting(Ingredient.of(MSBlocks.PINK_STONE_BRICKS.get()), MSBlocks.CRACKED_PINK_STONE_BRICKS.get(), 0.1F, 200).unlockedBy("has_pink_stone_bricks", has(MSBlocks.PINK_STONE_BRICKS.get())).save(recipeBuilder);
		SimpleCookingRecipeBuilder.smelting(Ingredient.of(MSBlocks.BROWN_STONE.get()), MSBlocks.POLISHED_BROWN_STONE.get(), 0.1f, 200).unlockedBy("has_brown_stone", has(MSBlocks.BROWN_STONE.get())).save(recipeBuilder);
		SimpleCookingRecipeBuilder.smelting(Ingredient.of(MSBlocks.BROWN_STONE_BRICKS.get()), MSBlocks.CRACKED_BROWN_STONE_BRICKS.get(), 0.1F, 200).unlockedBy("has_brown_stone_bricks", has(MSBlocks.BROWN_STONE_BRICKS.get())).save(recipeBuilder);
		SimpleCookingRecipeBuilder.smelting(Ingredient.of(MSItems.CAKE_MIX.get()), Items.CAKE, 0.0F, 200).unlockedBy("has_cake_mix", has(MSItems.CAKE_MIX.get())).save(recipeBuilder, new ResourceLocation (Minestuck.MOD_ID, "cake_from_mix"));
		
		SimpleCookingRecipeBuilder.smelting(Ingredient.of(MSTags.Items.CRUXITE_ORES), MSItems.RAW_CRUXITE.get(), 0.2F, 200).unlockedBy("has_cruxite_ore", has(MSTags.Items.CRUXITE_ORES)).save(recipeBuilder, new ResourceLocation(Minestuck.MOD_ID, "raw_cruxite_from_smelting"));
		SimpleCookingRecipeBuilder.blasting(Ingredient.of(MSTags.Items.CRUXITE_ORES), MSItems.RAW_CRUXITE.get(), 0.2F, 100).unlockedBy("has_cruxite_ore", has(MSTags.Items.CRUXITE_ORES)).save(recipeBuilder, new ResourceLocation(Minestuck.MOD_ID, "raw_cruxite_from_blasting"));
		SimpleCookingRecipeBuilder.smelting(Ingredient.of(MSTags.Items.URANIUM_ORES), MSItems.RAW_URANIUM.get(), 0.2F, 200).unlockedBy("has_uranium_ore", has(MSTags.Items.URANIUM_ORES)).save(recipeBuilder, new ResourceLocation(Minestuck.MOD_ID, "raw_uranium_from_smelting"));
		SimpleCookingRecipeBuilder.blasting(Ingredient.of(MSTags.Items.URANIUM_ORES), MSItems.RAW_URANIUM.get(), 0.2F, 100).unlockedBy("has_uranium_ore", has(MSTags.Items.URANIUM_ORES)).save(recipeBuilder, new ResourceLocation(Minestuck.MOD_ID, "raw_uranium_from_blasting"));
		SimpleCookingRecipeBuilder.smelting(Ingredient.of(MSTags.Items.COAL_ORES), Items.COAL, 0.1F, 200).unlockedBy("has_coal_ore", has(MSTags.Items.COAL_ORES)).save(recipeBuilder, new ResourceLocation(Minestuck.MOD_ID, "coal_from_smelting"));
		SimpleCookingRecipeBuilder.blasting(Ingredient.of(MSTags.Items.COAL_ORES), Items.COAL, 0.1F, 100).unlockedBy("has_coal_ore", has(MSTags.Items.COAL_ORES)).save(recipeBuilder, new ResourceLocation(Minestuck.MOD_ID, "coal_from_blasting"));
		SimpleCookingRecipeBuilder.smelting(Ingredient.of(MSTags.Items.IRON_ORES), Items.IRON_INGOT, 0.7F, 200).unlockedBy("has_iron_ore", has(MSTags.Items.IRON_ORES)).save(recipeBuilder, new ResourceLocation(Minestuck.MOD_ID, "iron_ingot_from_smelting"));
		SimpleCookingRecipeBuilder.blasting(Ingredient.of(MSTags.Items.IRON_ORES), Items.IRON_INGOT, 0.7F, 100).unlockedBy("has_iron_ore", has(MSTags.Items.IRON_ORES)).save(recipeBuilder, new ResourceLocation(Minestuck.MOD_ID, "iron_ingot_from_blasting"));
		SimpleCookingRecipeBuilder.smelting(Ingredient.of(MSTags.Items.GOLD_ORES), Items.GOLD_INGOT, 1.0F, 200).unlockedBy("has_gold_ore", has(MSTags.Items.GOLD_ORES)).save(recipeBuilder, new ResourceLocation(Minestuck.MOD_ID, "gold_ingot_from_smelting"));
		SimpleCookingRecipeBuilder.blasting(Ingredient.of(MSTags.Items.GOLD_ORES), Items.GOLD_INGOT, 1.0F, 100).unlockedBy("has_gold_ore", has(MSTags.Items.GOLD_ORES)).save(recipeBuilder, new ResourceLocation(Minestuck.MOD_ID, "gold_ingot_from_blasting"));
		SimpleCookingRecipeBuilder.smelting(Ingredient.of(MSTags.Items.REDSTONE_ORES), Items.REDSTONE, 0.7F, 200).unlockedBy("has_redstone_ore", has(MSTags.Items.REDSTONE_ORES)).save(recipeBuilder, new ResourceLocation(Minestuck.MOD_ID, "redstone_from_smelting"));
		SimpleCookingRecipeBuilder.blasting(Ingredient.of(MSTags.Items.REDSTONE_ORES), Items.REDSTONE, 0.7F, 100).unlockedBy("has_redstone_ore", has(MSTags.Items.REDSTONE_ORES)).save(recipeBuilder, new ResourceLocation(Minestuck.MOD_ID, "redstone_from_blasting"));
		SimpleCookingRecipeBuilder.smelting(Ingredient.of(MSTags.Items.QUARTZ_ORES), Items.QUARTZ, 0.2F, 200).unlockedBy("has_quartz_ore", has(MSTags.Items.QUARTZ_ORES)).save(recipeBuilder, new ResourceLocation(Minestuck.MOD_ID, "quartz_from_smelting"));
		SimpleCookingRecipeBuilder.blasting(Ingredient.of(MSTags.Items.QUARTZ_ORES), Items.QUARTZ, 0.2F, 100).unlockedBy("has_quartz_ore", has(MSTags.Items.QUARTZ_ORES)).save(recipeBuilder, new ResourceLocation(Minestuck.MOD_ID, "quartz_from_blasting"));
		SimpleCookingRecipeBuilder.smelting(Ingredient.of(MSTags.Items.LAPIS_ORES), Items.LAPIS_LAZULI, 0.2F, 200).unlockedBy("has_lapis_ore", has(MSTags.Items.LAPIS_ORES)).save(recipeBuilder, new ResourceLocation(Minestuck.MOD_ID, "lapis_lazuli_from_smelting"));
		SimpleCookingRecipeBuilder.blasting(Ingredient.of(MSTags.Items.LAPIS_ORES), Items.LAPIS_LAZULI, 0.2F, 100).unlockedBy("has_lapis_ore", has(MSTags.Items.LAPIS_ORES)).save(recipeBuilder, new ResourceLocation(Minestuck.MOD_ID, "lapis_lazuli_from_blasting"));
		SimpleCookingRecipeBuilder.smelting(Ingredient.of(MSTags.Items.DIAMOND_ORES), Items.DIAMOND, 1.0F, 200).unlockedBy("has_diamond_ore", has(MSTags.Items.DIAMOND_ORES)).save(recipeBuilder, new ResourceLocation(Minestuck.MOD_ID, "diamond_from_smelting"));
		SimpleCookingRecipeBuilder.blasting(Ingredient.of(MSTags.Items.DIAMOND_ORES), Items.DIAMOND, 1.0F, 100).unlockedBy("has_diamond_ore", has(MSTags.Items.DIAMOND_ORES)).save(recipeBuilder, new ResourceLocation(Minestuck.MOD_ID, "diamond_from_blasting"));
		
		SimpleCookingRecipeBuilder.smelting(Ingredient.of(MSBlocks.CRUXITE_DOWEL.get()), MSItems.RAW_CRUXITE.get(), 0.0F, 200).unlockedBy("has_cruxite_dowel", has(MSBlocks.CRUXITE_DOWEL.get())).save(recipeBuilder, new ResourceLocation(Minestuck.MOD_ID, "raw_cruxite_from_dowel"));
		SimpleCookingRecipeBuilder.smelting(Ingredient.of(MSBlocks.GOLD_SEEDS.get()), Items.GOLD_NUGGET, 0.1F, 200).unlockedBy("has_gold_seeds", has(MSBlocks.GOLD_SEEDS.get())).save(recipeBuilder, new ResourceLocation(Minestuck.MOD_ID, "gold_nugget_from_seeds"));
		SimpleCookingRecipeBuilder.smelting(Ingredient.of(MSBlocks.WOODEN_CACTUS.get()), Items.CHARCOAL, 0.15F, 200).unlockedBy("has_wooden_cactus", has(MSBlocks.WOODEN_CACTUS.get())).save(recipeBuilder, new ResourceLocation(Minestuck.MOD_ID, "charcoal_from_wooden_cactus"));
		
		cookingRecipesFor(recipeBuilder, Ingredient.of(MSItems.BEEF_SWORD.get()), MSItems.STEAK_SWORD.get(), 0.5F, "has_beef_sword", has(MSItems.BEEF_SWORD.get()));
		
		SimpleCookingRecipeBuilder.cooking(Ingredient.of(Items.BEEF), MSItems.IRRADIATED_STEAK.get(), 0.2F, 20, MSRecipeTypes.IRRADIATING.get()).unlockedBy("has_beef", has(Items.BEEF)).save(skipAdvancement(recipeBuilder));
		SimpleCookingRecipeBuilder.cooking(Ingredient.of(MSItems.BEEF_SWORD.get()), MSItems.IRRADIATED_STEAK_SWORD.get(), 0.35F, 20, MSRecipeTypes.IRRADIATING.get()).unlockedBy("has_beef_sword", has(MSItems.BEEF_SWORD.get())).save(skipAdvancement(recipeBuilder));
		SimpleCookingRecipeBuilder.cooking(Ingredient.of(Items.STICK), MSItems.URANIUM_POWERED_STICK.get(), 0.1F, 100, MSRecipeTypes.IRRADIATING.get()).unlockedBy("has_stick", has(Items.STICK)).save(skipAdvancement(recipeBuilder));
		SimpleCookingRecipeBuilder.cooking(Ingredient.of(Items.MUSHROOM_STEW), Items.SLIME_BALL, 0.1F, 20, MSRecipeTypes.IRRADIATING.get()).unlockedBy("has_mushroom_stew", has(Items.MUSHROOM_STEW)).save(skipAdvancement(recipeBuilder), new ResourceLocation(Minestuck.MOD_ID, "slimeball_from_irradiating"));
		IrradiatingFallbackRecipeBuilder.fallback(RecipeType.SMOKING).build(recipeBuilder, new ResourceLocation(Minestuck.MOD_ID, "irradiate_smoking_fallback"));
		
		SingleItemRecipeBuilder.stonecutting(Ingredient.of(MSBlocks.BLACK_CHESS_BRICKS.get()), MSBlocks.BLACK_CHESS_BRICK_STAIRS.get()).unlockedBy("has_black_chess_bricks", has(MSBlocks.BLACK_CHESS_BRICKS.get())).save(recipeBuilder, new ResourceLocation(Minestuck.MOD_ID, "black_chess_brick_stairs_from_black_chess_bricks_stonecutting"));
		SingleItemRecipeBuilder.stonecutting(Ingredient.of(MSBlocks.DARK_GRAY_CHESS_BRICKS.get()), MSBlocks.DARK_GRAY_CHESS_BRICK_STAIRS.get()).unlockedBy("has_dark_gray_chess_bricks", has(MSBlocks.DARK_GRAY_CHESS_BRICKS.get())).save(recipeBuilder, new ResourceLocation(Minestuck.MOD_ID, "dark_gray_chess_brick_stairs_from_dark_gray_chess_bricks_stonecutting"));
		SingleItemRecipeBuilder.stonecutting(Ingredient.of(MSBlocks.LIGHT_GRAY_CHESS_BRICKS.get()), MSBlocks.LIGHT_GRAY_CHESS_BRICK_STAIRS.get()).unlockedBy("has_light_gray_chess_bricks", has(MSBlocks.LIGHT_GRAY_CHESS_BRICKS.get())).save(recipeBuilder, new ResourceLocation(Minestuck.MOD_ID, "light_gray_chess_brick_stairs_from_light_gray_chess_bricks_stonecutting"));
		SingleItemRecipeBuilder.stonecutting(Ingredient.of(MSBlocks.WHITE_CHESS_BRICKS.get()), MSBlocks.WHITE_CHESS_BRICK_STAIRS.get()).unlockedBy("has_white_chess_bricks", has(MSBlocks.WHITE_CHESS_BRICKS.get())).save(recipeBuilder, new ResourceLocation(Minestuck.MOD_ID, "white_chess_brick_stairs_from_white_chess_bricks_stonecutting"));
		SingleItemRecipeBuilder.stonecutting(Ingredient.of(MSBlocks.BLACK_CHESS_BRICKS.get()), MSBlocks.BLACK_CHESS_BRICK_SLAB.get(), 2).unlockedBy("has_black_chess_bricks", has(MSBlocks.BLACK_CHESS_BRICKS.get())).save(recipeBuilder, new ResourceLocation(Minestuck.MOD_ID, "black_chess_brick_slab_from_black_chess_bricks_stonecutting"));
		SingleItemRecipeBuilder.stonecutting(Ingredient.of(MSBlocks.DARK_GRAY_CHESS_BRICKS.get()), MSBlocks.DARK_GRAY_CHESS_BRICK_SLAB.get(), 2).unlockedBy("has_dark_gray_chess_bricks", has(MSBlocks.DARK_GRAY_CHESS_BRICKS.get())).save(recipeBuilder, new ResourceLocation(Minestuck.MOD_ID, "dark_gray_chess_brick_slab_from_dark_gray_chess_bricks_stonecutting"));
		SingleItemRecipeBuilder.stonecutting(Ingredient.of(MSBlocks.LIGHT_GRAY_CHESS_BRICKS.get()), MSBlocks.LIGHT_GRAY_CHESS_BRICK_SLAB.get(), 2).unlockedBy("has_light_gray_chess_bricks", has(MSBlocks.LIGHT_GRAY_CHESS_BRICKS.get())).save(recipeBuilder, new ResourceLocation(Minestuck.MOD_ID, "light_gray_chess_brick_slab_from_light_gray_chess_bricks_stonecutting"));
		SingleItemRecipeBuilder.stonecutting(Ingredient.of(MSBlocks.WHITE_CHESS_BRICKS.get()), MSBlocks.WHITE_CHESS_BRICK_SLAB.get(), 2).unlockedBy("has_white_chess_bricks", has(MSBlocks.WHITE_CHESS_BRICKS.get())).save(recipeBuilder, new ResourceLocation(Minestuck.MOD_ID, "white_chess_brick_slab_from_white_chess_bricks_stonecutting"));
		SingleItemRecipeBuilder.stonecutting(Ingredient.of(MSBlocks.COARSE_STONE.get()), MSBlocks.CHISELED_COARSE_STONE.get()).unlockedBy("has_coarse_stone", has(MSBlocks.COARSE_STONE.get())).save(recipeBuilder, new ResourceLocation(Minestuck.MOD_ID, "chiseled_coarse_stone_from_coarse_stone_stonecutting"));
		SingleItemRecipeBuilder.stonecutting(Ingredient.of(MSBlocks.COARSE_STONE.get()), MSBlocks.COARSE_STONE_BRICKS.get()).unlockedBy("has_coarse_stone", has(MSBlocks.COARSE_STONE.get())).save(recipeBuilder, new ResourceLocation(Minestuck.MOD_ID, "coarse_stone_bricks_from_coarse_stone_stonecutting"));
		SingleItemRecipeBuilder.stonecutting(Ingredient.of(MSBlocks.COARSE_STONE_BRICKS.get()), MSBlocks.CHISELED_COARSE_STONE_BRICKS.get()).unlockedBy("has_coarse_stone_bricks", has(MSBlocks.COARSE_STONE_BRICKS.get())).save(recipeBuilder, new ResourceLocation(Minestuck.MOD_ID, "chiseled_coarse_stone_bricks_from_coarse_stone_bricks_stonecutting"));
		SingleItemRecipeBuilder.stonecutting(Ingredient.of(MSBlocks.COARSE_STONE_BRICKS.get()), MSBlocks.COARSE_STONE_COLUMN.get()).unlockedBy("has_coarse_stone_bricks", has(MSBlocks.COARSE_STONE_BRICKS.get())).save(recipeBuilder, new ResourceLocation(Minestuck.MOD_ID, "coarse_stone_column_from_coarse_stone_bricks_stonecutting"));
		SingleItemRecipeBuilder.stonecutting(Ingredient.of(MSBlocks.COARSE_STONE.get()), MSBlocks.COARSE_STONE_STAIRS.get()).unlockedBy("has_coarse_stone", has(MSBlocks.COARSE_STONE.get())).save(recipeBuilder, new ResourceLocation(Minestuck.MOD_ID, "coarse_stone_stairs_from_coarse_stone_stonecutting"));
		SingleItemRecipeBuilder.stonecutting(Ingredient.of(MSBlocks.COARSE_STONE.get()), MSBlocks.COARSE_STONE_SLAB.get(), 2).unlockedBy("has_coarse_stone", has(MSBlocks.COARSE_STONE.get())).save(recipeBuilder, new ResourceLocation(Minestuck.MOD_ID, "coarse_stone_slab_from_coarse_stone_stonecutting"));
		SingleItemRecipeBuilder.stonecutting(Ingredient.of(MSBlocks.COARSE_STONE_BRICKS.get()), MSBlocks.COARSE_STONE_BRICK_STAIRS.get()).unlockedBy("has_coarse_stone_bricks", has(MSBlocks.COARSE_STONE_BRICKS.get())).save(recipeBuilder, new ResourceLocation(Minestuck.MOD_ID, "coarse_stone_brick_stairs_from_coarse_stone_bricks_stonecutting"));
		SingleItemRecipeBuilder.stonecutting(Ingredient.of(MSBlocks.COARSE_STONE_BRICKS.get()), MSBlocks.COARSE_STONE_BRICK_SLAB.get(), 2).unlockedBy("has_coarse_stone_bricks", has(MSBlocks.COARSE_STONE_BRICKS.get())).save(recipeBuilder, new ResourceLocation(Minestuck.MOD_ID, "coarse_stone_brick_slab_from_coarse_stone_bricks_stonecutting"));
		SingleItemRecipeBuilder.stonecutting(Ingredient.of(MSBlocks.SHADE_STONE.get()), MSBlocks.SHADE_BRICKS.get()).unlockedBy("has_shade_stone", has(MSBlocks.SHADE_STONE.get())).save(recipeBuilder, new ResourceLocation(Minestuck.MOD_ID, "shade_bricks_from_shade_stone_stonecutting"));
		SingleItemRecipeBuilder.stonecutting(Ingredient.of(MSBlocks.SHADE_STONE.get()), MSBlocks.SHADE_STAIRS.get()).unlockedBy("has_shade_stone", has(MSBlocks.SHADE_STONE.get())).save(recipeBuilder, new ResourceLocation(Minestuck.MOD_ID, "shade_stairs_from_shade_stone_stonecutting"));
		SingleItemRecipeBuilder.stonecutting(Ingredient.of(MSBlocks.SHADE_STONE.get()), MSBlocks.SHADE_SLAB.get(), 2).unlockedBy("has_shade_stone", has(MSBlocks.SHADE_STONE.get())).save(recipeBuilder, new ResourceLocation(Minestuck.MOD_ID, "shade_slab_from_shade_stone_stonecutting"));
		SingleItemRecipeBuilder.stonecutting(Ingredient.of(MSBlocks.SHADE_BRICKS.get()), MSBlocks.SHADE_BRICK_STAIRS.get()).unlockedBy("has_shade_bricks", has(MSBlocks.SHADE_BRICKS.get())).save(recipeBuilder, new ResourceLocation(Minestuck.MOD_ID, "shade_brick_stairs_from_shade_bricks_stonecutting"));
		SingleItemRecipeBuilder.stonecutting(Ingredient.of(MSBlocks.SHADE_BRICKS.get()), MSBlocks.SHADE_BRICK_SLAB.get(), 2).unlockedBy("has_shade_bricks", has(MSBlocks.SHADE_BRICKS.get())).save(recipeBuilder, new ResourceLocation(Minestuck.MOD_ID, "shade_brick_slab_from_shade_bricks_stonecutting"));
		SingleItemRecipeBuilder.stonecutting(Ingredient.of(MSBlocks.SHADE_BRICKS.get()), MSBlocks.CHISELED_SHADE_BRICKS.get()).unlockedBy("has_shade_bricks", has(MSBlocks.SHADE_BRICKS.get())).save(recipeBuilder, new ResourceLocation(Minestuck.MOD_ID, "chiseled_shade_bricks_from_shade_bricks_stonecutting"));
		SingleItemRecipeBuilder.stonecutting(Ingredient.of(MSBlocks.SHADE_BRICKS.get()), MSBlocks.SHADE_COLUMN.get()).unlockedBy("has_shade_bricks", has(MSBlocks.SHADE_BRICKS.get())).save(recipeBuilder, new ResourceLocation(Minestuck.MOD_ID, "shade_column_from_shade_bricks_stonecutting"));
		SingleItemRecipeBuilder.stonecutting(Ingredient.of(MSBlocks.FROST_TILE.get()), MSBlocks.CHISELED_FROST_TILE.get()).unlockedBy("has_frost_tile", has(MSBlocks.FROST_TILE.get())).save(recipeBuilder, new ResourceLocation(Minestuck.MOD_ID, "chiseled_frost_tile_from_frost_tile_stonecutting"));
		SingleItemRecipeBuilder.stonecutting(Ingredient.of(MSBlocks.FROST_TILE.get()), MSBlocks.FROST_BRICKS.get()).unlockedBy("has_frost_tile", has(MSBlocks.FROST_TILE.get())).save(recipeBuilder, new ResourceLocation(Minestuck.MOD_ID, "frost_bricks_from_frost_tile_stonecutting"));
		SingleItemRecipeBuilder.stonecutting(Ingredient.of(MSBlocks.FROST_BRICKS.get()), MSBlocks.CHISELED_FROST_BRICKS.get()).unlockedBy("has_frost_bricks", has(MSBlocks.FROST_BRICKS.get())).save(recipeBuilder, new ResourceLocation(Minestuck.MOD_ID, "chiseled_frost_bricks_from_frost_bricks_stonecutting"));
		SingleItemRecipeBuilder.stonecutting(Ingredient.of(MSBlocks.FROST_BRICKS.get()), MSBlocks.FROST_COLUMN.get()).unlockedBy("has_frost_bricks", has(MSBlocks.FROST_BRICKS.get())).save(recipeBuilder, new ResourceLocation(Minestuck.MOD_ID, "frost_column_from_frost_bricks_stonecutting"));
		SingleItemRecipeBuilder.stonecutting(Ingredient.of(MSBlocks.FROST_TILE.get()), MSBlocks.FROST_TILE_STAIRS.get()).unlockedBy("has_frost_tile", has(MSBlocks.FROST_TILE.get())).save(recipeBuilder, new ResourceLocation(Minestuck.MOD_ID, "frost_tile_stairs_from_frost_tile_stonecutting"));
		SingleItemRecipeBuilder.stonecutting(Ingredient.of(MSBlocks.FROST_TILE.get()), MSBlocks.FROST_TILE_SLAB.get(), 2).unlockedBy("has_frost_tile", has(MSBlocks.FROST_TILE.get())).save(recipeBuilder, new ResourceLocation(Minestuck.MOD_ID, "frost_tile_slab_from_frost_tile_stonecutting"));
		SingleItemRecipeBuilder.stonecutting(Ingredient.of(MSBlocks.FROST_BRICKS.get()), MSBlocks.FROST_BRICK_STAIRS.get()).unlockedBy("has_frost_bricks", has(MSBlocks.FROST_BRICKS.get())).save(recipeBuilder, new ResourceLocation(Minestuck.MOD_ID, "frost_brick_stairs_from_frost_bricks_stonecutting"));
		SingleItemRecipeBuilder.stonecutting(Ingredient.of(MSBlocks.FROST_BRICKS.get()), MSBlocks.FROST_BRICK_SLAB.get(), 2).unlockedBy("has_frost_bricks", has(MSBlocks.FROST_BRICKS.get())).save(recipeBuilder, new ResourceLocation(Minestuck.MOD_ID, "frost_brick_slab_from_frost_bricks_stonecutting"));
		SingleItemRecipeBuilder.stonecutting(Ingredient.of(MSBlocks.CAST_IRON.get()), MSBlocks.CAST_IRON_STAIRS.get()).unlockedBy("has_cast_iron", has(MSBlocks.CAST_IRON.get())).save(recipeBuilder, new ResourceLocation(Minestuck.MOD_ID, "cast_iron_stairs_from_cast_iron_stonecutting"));
		SingleItemRecipeBuilder.stonecutting(Ingredient.of(MSBlocks.CAST_IRON.get()), MSBlocks.CHISELED_CAST_IRON.get()).unlockedBy("has_cast_iron", has(MSBlocks.CAST_IRON.get())).save(recipeBuilder, new ResourceLocation(Minestuck.MOD_ID, "chiseled_cast_iron_from_cast_iron_stonecutting"));
		SingleItemRecipeBuilder.stonecutting(Ingredient.of(MSBlocks.MYCELIUM_STONE.get()), MSBlocks.MYCELIUM_BRICKS.get()).unlockedBy("has_mycelium_stone", has(MSBlocks.MYCELIUM_STONE.get())).save(recipeBuilder, new ResourceLocation(Minestuck.MOD_ID, "mycelium_bricks_from_mycelium_stone_stonecutting"));
		SingleItemRecipeBuilder.stonecutting(Ingredient.of(MSBlocks.MYCELIUM_BRICKS.get()), MSBlocks.CHISELED_MYCELIUM_BRICKS.get()).unlockedBy("has_mycelium_bricks", has(MSBlocks.MYCELIUM_BRICKS.get())).save(recipeBuilder, new ResourceLocation(Minestuck.MOD_ID, "chiseled_mycelium_bricks_from_mycelium_bricks_stonecutting"));
		SingleItemRecipeBuilder.stonecutting(Ingredient.of(MSBlocks.MYCELIUM_BRICKS.get()), MSBlocks.MYCELIUM_COLUMN.get()).unlockedBy("has_mycelium_bricks", has(MSBlocks.MYCELIUM_BRICKS.get())).save(recipeBuilder, new ResourceLocation(Minestuck.MOD_ID, "mycelium_column_from_mycelium_bricks_stonecutting"));
		SingleItemRecipeBuilder.stonecutting(Ingredient.of(MSBlocks.MYCELIUM_STONE.get()), MSBlocks.MYCELIUM_STAIRS.get()).unlockedBy("has_mycelium_stone", has(MSBlocks.MYCELIUM_STONE.get())).save(recipeBuilder, new ResourceLocation(Minestuck.MOD_ID, "mycelium_stairs_from_mycelium_stone_stonecutting"));
		SingleItemRecipeBuilder.stonecutting(Ingredient.of(MSBlocks.MYCELIUM_BRICKS.get()), MSBlocks.MYCELIUM_BRICK_STAIRS.get()).unlockedBy("has_mycelium_bricks", has(MSBlocks.MYCELIUM_BRICKS.get())).save(recipeBuilder, new ResourceLocation(Minestuck.MOD_ID, "mycelium_brick_stairs_from_mycelium_bricks_stonecutting"));
		SingleItemRecipeBuilder.stonecutting(Ingredient.of(MSBlocks.MYCELIUM_STONE.get()), MSBlocks.MYCELIUM_SLAB.get(), 2).unlockedBy("has_mycelium_stone", has(MSBlocks.MYCELIUM_STONE.get())).save(recipeBuilder, new ResourceLocation(Minestuck.MOD_ID, "mycelium_slab_from_mycelium_stone_stonecutting"));
		SingleItemRecipeBuilder.stonecutting(Ingredient.of(MSBlocks.MYCELIUM_BRICKS.get()), MSBlocks.MYCELIUM_BRICK_SLAB.get(), 2).unlockedBy("has_mycelium_bricks", has(MSBlocks.MYCELIUM_BRICKS.get())).save(recipeBuilder, new ResourceLocation(Minestuck.MOD_ID, "mycelium_brick_slab_from_mycelium_bricks_stonecutting"));
		SingleItemRecipeBuilder.stonecutting(Ingredient.of(MSBlocks.BLACK_STONE.get()), MSBlocks.BLACK_STONE_BRICKS.get()).unlockedBy("has_black_stone", has(MSBlocks.BLACK_STONE.get())).save(recipeBuilder, new ResourceLocation(Minestuck.MOD_ID, "black_stone_bricks_from_black_stone_stonecutting"));
		SingleItemRecipeBuilder.stonecutting(Ingredient.of(MSBlocks.BLACK_STONE_BRICKS.get()), MSBlocks.CHISELED_BLACK_STONE_BRICKS.get()).unlockedBy("has_black_stone_bricks", has(MSBlocks.BLACK_STONE_BRICKS.get())).save(recipeBuilder, new ResourceLocation(Minestuck.MOD_ID, "chiseled_black_stone_bricks_from_black_stone_bricks_stonecutting"));
		SingleItemRecipeBuilder.stonecutting(Ingredient.of(MSBlocks.BLACK_STONE_BRICKS.get()), MSBlocks.BLACK_STONE_COLUMN.get()).unlockedBy("has_black_stone_bricks", has(MSBlocks.BLACK_STONE_BRICKS.get())).save(recipeBuilder, new ResourceLocation(Minestuck.MOD_ID, "black_stone_column_from_black_stone_bricks_stonecutting"));
		SingleItemRecipeBuilder.stonecutting(Ingredient.of(MSBlocks.BLACK_STONE.get()), MSBlocks.BLACK_STONE_STAIRS.get()).unlockedBy("has_black_stone", has(MSBlocks.BLACK_STONE.get())).save(recipeBuilder, new ResourceLocation(Minestuck.MOD_ID, "black_stone_stairs_from_black_stone_stonecutting"));
		SingleItemRecipeBuilder.stonecutting(Ingredient.of(MSBlocks.BLACK_STONE_BRICKS.get()), MSBlocks.BLACK_STONE_BRICK_STAIRS.get()).unlockedBy("has_black_stone_bricks", has(MSBlocks.BLACK_STONE_BRICKS.get())).save(recipeBuilder, new ResourceLocation(Minestuck.MOD_ID, "black_stone_brick_stairs_from_black_stone_bricks_stonecutting"));
		SingleItemRecipeBuilder.stonecutting(Ingredient.of(MSBlocks.BLACK_STONE.get()), MSBlocks.BLACK_STONE_SLAB.get(), 2).unlockedBy("has_black_stone", has(MSBlocks.BLACK_STONE.get())).save(recipeBuilder, new ResourceLocation(Minestuck.MOD_ID, "black_stone_slab_from_black_stone_stonecutting"));
		SingleItemRecipeBuilder.stonecutting(Ingredient.of(MSBlocks.BLACK_STONE_BRICKS.get()), MSBlocks.BLACK_STONE_BRICK_SLAB.get(), 2).unlockedBy("has_black_stone_bricks", has(MSBlocks.BLACK_STONE_BRICKS.get())).save(recipeBuilder, new ResourceLocation(Minestuck.MOD_ID, "black_stone_brick_slab_from_black_stone_bricks_stonecutting"));
		SingleItemRecipeBuilder.stonecutting(Ingredient.of(MSBlocks.CHALK.get()), MSBlocks.CHALK_STAIRS.get()).unlockedBy("has_chalk", has(MSBlocks.CHALK.get())).save(recipeBuilder, new ResourceLocation(Minestuck.MOD_ID, "chalk_stairs_from_chalk_stonecutting"));
		SingleItemRecipeBuilder.stonecutting(Ingredient.of(MSBlocks.CHALK.get()), MSBlocks.CHALK_SLAB.get(), 2).unlockedBy("has_chalk", has(MSBlocks.CHALK.get())).save(recipeBuilder, new ResourceLocation(Minestuck.MOD_ID, "chalk_slab_from_chalk_stonecutting"));
		SingleItemRecipeBuilder.stonecutting(Ingredient.of(MSBlocks.CHALK.get()), MSBlocks.POLISHED_CHALK.get()).unlockedBy("has_chalk", has(MSBlocks.CHALK.get())).save(recipeBuilder, new ResourceLocation(Minestuck.MOD_ID, "polished_chalk_from_chalk_stonecutting"));
		SingleItemRecipeBuilder.stonecutting(Ingredient.of(MSBlocks.CHALK.get()), MSBlocks.CHALK_BRICKS.get()).unlockedBy("has_chalk", has(MSBlocks.CHALK.get())).save(recipeBuilder, new ResourceLocation(Minestuck.MOD_ID, "chalk_bricks_from_chalk_stonecutting"));
		SingleItemRecipeBuilder.stonecutting(Ingredient.of(MSBlocks.POLISHED_CHALK.get()), MSBlocks.CHALK_BRICKS.get()).unlockedBy("has_polished_chalk", has(MSBlocks.POLISHED_CHALK.get())).save(recipeBuilder, new ResourceLocation(Minestuck.MOD_ID, "chalk_bricks_from_polished_chalk_stonecutting"));
		SingleItemRecipeBuilder.stonecutting(Ingredient.of(MSBlocks.CHALK_BRICKS.get()), MSBlocks.CHISELED_CHALK_BRICKS.get()).unlockedBy("has_chalk_bricks", has(MSBlocks.CHALK_BRICKS.get())).save(recipeBuilder, new ResourceLocation(Minestuck.MOD_ID, "chiseled_chalk_bricks_from_chalk_bricks_stonecutting"));
		SingleItemRecipeBuilder.stonecutting(Ingredient.of(MSBlocks.CHALK_BRICKS.get()), MSBlocks.CHALK_COLUMN.get()).unlockedBy("has_chalk_bricks", has(MSBlocks.CHALK_BRICKS.get())).save(recipeBuilder, new ResourceLocation(Minestuck.MOD_ID, "chalk_column_from_chalk_bricks_stonecutting"));
		SingleItemRecipeBuilder.stonecutting(Ingredient.of(MSBlocks.CHALK_BRICKS.get()), MSBlocks.CHALK_BRICK_STAIRS.get()).unlockedBy("has_chalk_bricks", has(MSBlocks.CHALK_BRICKS.get())).save(recipeBuilder, new ResourceLocation(Minestuck.MOD_ID, "chalk_brick_stairs_from_chalk_bricks_stonecutting"));
		SingleItemRecipeBuilder.stonecutting(Ingredient.of(MSBlocks.CHALK_BRICKS.get()), MSBlocks.CHALK_BRICK_SLAB.get(), 2).unlockedBy("has_chalk_bricks", has(MSBlocks.CHALK_BRICKS.get())).save(recipeBuilder, new ResourceLocation(Minestuck.MOD_ID, "chalk_brick_slab_from_chalk_bricks_stonecutting"));
		SingleItemRecipeBuilder.stonecutting(Ingredient.of(MSBlocks.PINK_STONE.get()), MSBlocks.PINK_STONE_BRICKS.get()).unlockedBy("has_pink_stone", has(MSBlocks.PINK_STONE.get())).save(recipeBuilder, new ResourceLocation(Minestuck.MOD_ID, "pink_stone_bricks_from_pink_stone_stonecutting"));
		SingleItemRecipeBuilder.stonecutting(Ingredient.of(MSBlocks.PINK_STONE_BRICKS.get()), MSBlocks.CHISELED_PINK_STONE_BRICKS.get()).unlockedBy("has_pink_stone_bricks", has(MSBlocks.PINK_STONE_BRICKS.get())).save(recipeBuilder, new ResourceLocation(Minestuck.MOD_ID, "chiseled_pink_stone_bricks_from_pink_stone_bricks_stonecutting"));
		SingleItemRecipeBuilder.stonecutting(Ingredient.of(MSBlocks.PINK_STONE_BRICKS.get()), MSBlocks.PINK_STONE_COLUMN.get()).unlockedBy("has_pink_stone_bricks", has(MSBlocks.PINK_STONE_BRICKS.get())).save(recipeBuilder, new ResourceLocation(Minestuck.MOD_ID, "pink_stone_column_from_pink_stone_bricks_stonecutting"));
		SingleItemRecipeBuilder.stonecutting(Ingredient.of(MSBlocks.PINK_STONE.get()), MSBlocks.PINK_STONE_STAIRS.get()).unlockedBy("has_pink_stone", has(MSBlocks.PINK_STONE.get())).save(recipeBuilder, new ResourceLocation(Minestuck.MOD_ID, "pink_stone_stairs_from_pink_stone_stonecutting"));
		SingleItemRecipeBuilder.stonecutting(Ingredient.of(MSBlocks.PINK_STONE.get()), MSBlocks.PINK_STONE_SLAB.get(), 2).unlockedBy("has_pink_stone", has(MSBlocks.PINK_STONE.get())).save(recipeBuilder, new ResourceLocation(Minestuck.MOD_ID, "pink_stone_slab_from_pink_stone_stonecutting"));
		SingleItemRecipeBuilder.stonecutting(Ingredient.of(MSBlocks.PINK_STONE_BRICKS.get()), MSBlocks.PINK_STONE_BRICK_STAIRS.get()).unlockedBy("has_pink_stone_bricks", has(MSBlocks.PINK_STONE_BRICKS.get())).save(recipeBuilder, new ResourceLocation(Minestuck.MOD_ID, "pink_stone_brick_stairs_from_pink_stone_bricks_stonecutting"));
		SingleItemRecipeBuilder.stonecutting(Ingredient.of(MSBlocks.PINK_STONE_BRICKS.get()), MSBlocks.PINK_STONE_BRICK_SLAB.get(), 2).unlockedBy("has_pink_stone_bricks", has(MSBlocks.PINK_STONE_BRICKS.get())).save(recipeBuilder, new ResourceLocation(Minestuck.MOD_ID, "pink_stone_brick_slab_from_pink_stone_bricks_stonecutting"));
		SingleItemRecipeBuilder.stonecutting(Ingredient.of(MSBlocks.BROWN_STONE.get()), MSBlocks.BROWN_STONE_BRICKS.get()).unlockedBy("has_brown_stone", has(MSBlocks.BROWN_STONE.get())).save(recipeBuilder, new ResourceLocation(Minestuck.MOD_ID, "brown_stone_bricks_from_brown_stone_stonecutting"));
		SingleItemRecipeBuilder.stonecutting(Ingredient.of(MSBlocks.POLISHED_BROWN_STONE.get()), MSBlocks.BROWN_STONE_BRICKS.get()).unlockedBy("has_polished_brown_stone", has(MSBlocks.POLISHED_BROWN_STONE.get())).save(recipeBuilder, new ResourceLocation(Minestuck.MOD_ID, "brown_stone_bricks_from_polished_brown_stone_stonecutting"));
		SingleItemRecipeBuilder.stonecutting(Ingredient.of(MSBlocks.BROWN_STONE_BRICKS.get()), MSBlocks.BROWN_STONE_COLUMN.get()).unlockedBy("has_brown_stone_bricks", has(MSBlocks.BROWN_STONE_BRICKS.get())).save(recipeBuilder, new ResourceLocation(Minestuck.MOD_ID, "brown_stone_column_from_brown_stone_bricks_stonecutting"));
		SingleItemRecipeBuilder.stonecutting(Ingredient.of(MSBlocks.BROWN_STONE.get()), MSBlocks.BROWN_STONE_STAIRS.get()).unlockedBy("has_brown_stone", has(MSBlocks.BROWN_STONE.get())).save(recipeBuilder, new ResourceLocation(Minestuck.MOD_ID, "brown_stone_stairs_from_brown_stone_stonecutting"));
		SingleItemRecipeBuilder.stonecutting(Ingredient.of(MSBlocks.BROWN_STONE.get()), MSBlocks.BROWN_STONE_SLAB.get(), 2).unlockedBy("has_brown_stone", has(MSBlocks.BROWN_STONE.get())).save(recipeBuilder, new ResourceLocation(Minestuck.MOD_ID, "brown_stone_slab_from_brown_stone_stonecutting"));
		SingleItemRecipeBuilder.stonecutting(Ingredient.of(MSBlocks.BROWN_STONE_BRICKS.get()), MSBlocks.BROWN_STONE_BRICK_STAIRS.get()).unlockedBy("has_brown_stone_bricks", has(MSBlocks.BROWN_STONE_BRICKS.get())).save(recipeBuilder, new ResourceLocation(Minestuck.MOD_ID, "brown_stone_brick_stairs_from_brown_stone_bricks_stonecutting"));
		SingleItemRecipeBuilder.stonecutting(Ingredient.of(MSBlocks.BROWN_STONE_BRICKS.get()), MSBlocks.BROWN_STONE_BRICK_SLAB.get(), 2).unlockedBy("has_brown_stone_bricks", has(MSBlocks.BROWN_STONE_BRICKS.get())).save(recipeBuilder, new ResourceLocation(Minestuck.MOD_ID, "brown_stone_brick_slab_from_brown_stone_bricks_stonecutting"));
		SingleItemRecipeBuilder.stonecutting(Ingredient.of(MSBlocks.GREEN_STONE.get()), MSBlocks.POLISHED_GREEN_STONE.get()).unlockedBy("has_green_stone", has(MSBlocks.GREEN_STONE.get())).save(recipeBuilder, new ResourceLocation(Minestuck.MOD_ID, "polished_green_stone_from_green_stone_stonecutting"));
		SingleItemRecipeBuilder.stonecutting(Ingredient.of(MSBlocks.POLISHED_GREEN_STONE.get()), MSBlocks.GREEN_STONE_BRICKS.get()).unlockedBy("has_polished_green_stone", has(MSBlocks.POLISHED_GREEN_STONE.get())).save(recipeBuilder, new ResourceLocation(Minestuck.MOD_ID, "green_stone_bricks_from_polished_green_stone_stonecutting"));
		SingleItemRecipeBuilder.stonecutting(Ingredient.of(MSBlocks.POLISHED_GREEN_STONE.get()), MSBlocks.HORIZONTAL_GREEN_STONE_BRICKS.get()).unlockedBy("has_polished_green_stone", has(MSBlocks.POLISHED_GREEN_STONE.get())).save(recipeBuilder, new ResourceLocation(Minestuck.MOD_ID, "horizontal_green_stone_bricks_from_polished_green_stone_stonecutting"));
		SingleItemRecipeBuilder.stonecutting(Ingredient.of(MSBlocks.POLISHED_GREEN_STONE.get()), MSBlocks.VERTICAL_GREEN_STONE_BRICKS.get()).unlockedBy("has_polished_green_stone", has(MSBlocks.POLISHED_GREEN_STONE.get())).save(recipeBuilder, new ResourceLocation(Minestuck.MOD_ID, "vertical_green_stone_bricks_from_polished_green_stone_stonecutting"));
		SingleItemRecipeBuilder.stonecutting(Ingredient.of(MSBlocks.POLISHED_GREEN_STONE.get()), MSBlocks.CHISELED_GREEN_STONE_BRICKS.get()).unlockedBy("has_polished_green_stone", has(MSBlocks.POLISHED_GREEN_STONE.get())).save(recipeBuilder, new ResourceLocation(Minestuck.MOD_ID, "chiseled_green_stone_bricks_from_polished_green_stone_stonecutting"));
		SingleItemRecipeBuilder.stonecutting(Ingredient.of(MSBlocks.GREEN_STONE_BRICKS.get()), MSBlocks.GREEN_STONE_COLUMN.get()).unlockedBy("has_green_stone_bricks", has(MSBlocks.GREEN_STONE_BRICKS.get())).save(recipeBuilder, new ResourceLocation(Minestuck.MOD_ID, "green_stone_column_from_green_stone_bricks_stonecutting"));
		SingleItemRecipeBuilder.stonecutting(Ingredient.of(MSBlocks.GREEN_STONE_BRICKS.get()), MSBlocks.GREEN_STONE_BRICK_TRIM.get()).unlockedBy("has_green_stone_bricks", has(MSBlocks.GREEN_STONE_BRICKS.get())).save(recipeBuilder, new ResourceLocation(Minestuck.MOD_ID, "green_stone_brick_trim_from_green_stone_bricks_stonecutting"));
		SingleItemRecipeBuilder.stonecutting(Ingredient.of(MSBlocks.GREEN_STONE.get()), MSBlocks.GREEN_STONE_STAIRS.get()).unlockedBy("has_green_stone", has(MSBlocks.GREEN_STONE.get())).save(recipeBuilder, new ResourceLocation(Minestuck.MOD_ID, "green_stone_stairs_from_green_stone_stonecutting"));
		SingleItemRecipeBuilder.stonecutting(Ingredient.of(MSBlocks.GREEN_STONE.get()), MSBlocks.GREEN_STONE_SLAB.get(), 2).unlockedBy("has_green_stone", has(MSBlocks.GREEN_STONE.get())).save(recipeBuilder, new ResourceLocation(Minestuck.MOD_ID, "green_stone_slab_from_green_stone_stonecutting"));
		SingleItemRecipeBuilder.stonecutting(Ingredient.of(MSBlocks.GREEN_STONE_BRICKS.get()), MSBlocks.GREEN_STONE_BRICK_STAIRS.get()).unlockedBy("has_green_stone_bricks", has(MSBlocks.GREEN_STONE_BRICKS.get())).save(recipeBuilder, new ResourceLocation(Minestuck.MOD_ID, "green_stone_brick_stairs_from_green_stone_bricks_stonecutting"));
		SingleItemRecipeBuilder.stonecutting(Ingredient.of(MSBlocks.GREEN_STONE_BRICKS.get()), MSBlocks.GREEN_STONE_BRICK_SLAB.get(), 2).unlockedBy("has_green_stone_bricks", has(MSBlocks.GREEN_STONE_BRICKS.get())).save(recipeBuilder, new ResourceLocation(Minestuck.MOD_ID, "green_stone_brick_slab_from_green_stone_bricks_stonecutting"));
		SingleItemRecipeBuilder.stonecutting(Ingredient.of(Items.STONE_BRICKS), MSBlocks.DECREPIT_STONE_BRICKS.get()).unlockedBy("has_stone_bricks", has(Items.STONE_BRICKS)).save(recipeBuilder, new ResourceLocation(Minestuck.MOD_ID, "decrepit_stone_bricks_from_stone_bricks_stonecutting"));
		SingleItemRecipeBuilder.stonecutting(Ingredient.of(Items.STONE_BRICKS), MSBlocks.PUSHABLE_BLOCK.get()).unlockedBy("has_stone_bricks", has(Items.STONE_BRICKS)).save(recipeBuilder, new ResourceLocation(Minestuck.MOD_ID, "pushable_block_from_stone_bricks_stonecutting"));
		SingleItemRecipeBuilder.stonecutting(Ingredient.of(Items.STONE), MSBlocks.FRAGILE_STONE.get()).unlockedBy("has_stone", has(Items.STONE)).save(recipeBuilder, new ResourceLocation(Minestuck.MOD_ID, "fragile_stone_from_stone_stonecutting"));
	}
	
	private void cookingRecipesFor(Consumer<FinishedRecipe> recipeBuilder, Ingredient input, ItemLike result, float experience, String criterionName, InventoryChangeTrigger.TriggerInstance criterion)
	{
		ResourceLocation itemName = Objects.requireNonNull(ForgeRegistries.ITEMS.getKey(result.asItem()), "Registry name for "+result+" was found to be null!");
		SimpleCookingRecipeBuilder.smelting(input, result, experience, 200).unlockedBy(criterionName, criterion).save(recipeBuilder);
		SimpleCookingRecipeBuilder.cooking(input, result, experience, 100, RecipeSerializer.SMOKING_RECIPE).unlockedBy(criterionName, criterion).save(recipeBuilder, new ResourceLocation(itemName.getNamespace(), itemName.getPath()+"_from_smoking"));
		SimpleCookingRecipeBuilder.cooking(input, result, experience, 600, RecipeSerializer.CAMPFIRE_COOKING_RECIPE).unlockedBy(criterionName, criterion).save(recipeBuilder, new ResourceLocation(itemName.getNamespace(), itemName.getPath()+"_from_campfire_cooking"));
	}
	
	//TODO check between mc versions if this is still needed
	//As of writing this, the categories used by the vanilla recipe books are hardcoded, and all recipes are put in these categories
	// Because of this, recipes of modded recipe types will when unlocked show up in a vanilla recipe category
	// As a temporary solution, this function helps to remove the recipe advancement, thus preventing the recipe from "unlocking"
	private Consumer<FinishedRecipe> skipAdvancement(Consumer<FinishedRecipe> recipeBuilder)
	{
		return recipe -> recipeBuilder.accept(new Wrapper(recipe));
	}
	
	private static class Wrapper implements FinishedRecipe
	{
		FinishedRecipe recipe;
		
		Wrapper(FinishedRecipe recipe)
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
		public RecipeSerializer<?> getType()
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