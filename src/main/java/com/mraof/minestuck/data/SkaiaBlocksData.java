package com.mraof.minestuck.data;

import com.mraof.minestuck.Minestuck;
import com.mraof.minestuck.api.alchemy.GristTypes;
import com.mraof.minestuck.api.alchemy.recipe.GristCostRecipeBuilder;
import com.mraof.minestuck.api.alchemy.recipe.combination.CombinationRecipeBuilder;
import com.mraof.minestuck.block.MSBlocks;
import com.mraof.minestuck.data.loot_table.MSBlockLootTables;
import com.mraof.minestuck.data.recipe.CommonRecipes;
import com.mraof.minestuck.data.tag.MinestuckBlockTagsProvider;
import com.mraof.minestuck.item.MSItems;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.data.recipes.SingleItemRecipeBuilder;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.neoforged.neoforge.common.Tags;

import static com.mraof.minestuck.block.SkaiaBlocks.*;
import static com.mraof.minestuck.data.MSBlockStateProvider.itemTexture;
import static com.mraof.minestuck.data.MSBlockStateProvider.texture;
import static com.mraof.minestuck.data.recipe.MinestuckRecipeProvider.has;
import static net.minecraft.tags.BlockTags.*;

public final class SkaiaBlocksData
{
	public static void addEnUsTranslations(MinestuckLanguageProvider provider)
	{
		provider.add(SKAIA_PORTAL, "Skaia Portal");
		
		provider.add(BLACK_CHESS_DIRT, "Black Chess Tile");
		provider.add(WHITE_CHESS_DIRT, "White Chess Tile");
		provider.add(DARK_GRAY_CHESS_DIRT, "Dark Gray Chess Tile");
		provider.add(LIGHT_GRAY_CHESS_DIRT, "Light Gray Chess Tile");
		
		provider.add(BLACK_CHESS_BRICKS, "Black Chess Bricks");
		provider.add(BLACK_CHESS_BRICK_STAIRS, "Black Chess Brick Stairs");
		provider.add(BLACK_CHESS_BRICK_SLAB, "Black Chess Brick Slab");
		provider.add(BLACK_CHESS_BRICK_WALL, "Black Chess Brick Wall");
		provider.add(BLACK_CHESS_BRICK_TRIM, "Black Chess Brick Trim");
		
		provider.add(BLACK_CHESS_BRICK_SMOOTH, "Smooth Black Chess Brick");
		provider.add(BLACK_CHESS_BRICK_SMOOTH_STAIRS, "Smooth Black Chess Brick Stairs");
		provider.add(BLACK_CHESS_BRICK_SMOOTH_SLAB, "Smooth Black Chess Brick Slab");
		provider.add(BLACK_CHESS_BRICK_SMOOTH_WALL, "Smooth Black Chess Brick Wall");
		provider.add(BLACK_CHESS_BRICK_SMOOTH_PRESSURE_PLATE, "Smooth Black Chess Brick Pressure Plate");
		provider.add(BLACK_CHESS_BRICK_SMOOTH_BUTTON, "Smooth Black Chess Brick Button");
		
		provider.add(WHITE_CHESS_BRICKS, "White Chess Bricks");
		provider.add(WHITE_CHESS_BRICK_STAIRS, "White Chess Brick Stairs");
		provider.add(WHITE_CHESS_BRICK_SLAB, "White Chess Brick Slab");
		provider.add(WHITE_CHESS_BRICK_WALL, "White Chess Brick Wall");
		provider.add(WHITE_CHESS_BRICK_TRIM, "White Chess Brick Trim");
		
		provider.add(WHITE_CHESS_BRICK_SMOOTH, "Smooth White Chess Brick");
		provider.add(WHITE_CHESS_BRICK_SMOOTH_STAIRS, "Smooth White Chess Brick Stairs");
		provider.add(WHITE_CHESS_BRICK_SMOOTH_SLAB, "Smooth White Chess Brick Slab");
		provider.add(WHITE_CHESS_BRICK_SMOOTH_WALL, "Smooth White Chess Brick Wall");
		provider.add(WHITE_CHESS_BRICK_SMOOTH_PRESSURE_PLATE, "Smooth White Chess Brick Pressure Plate");
		provider.add(WHITE_CHESS_BRICK_SMOOTH_BUTTON, "Smooth White Chess Brick Button");
		
		provider.add(DARK_GRAY_CHESS_BRICKS, "Dark Gray Chess Bricks");
		provider.add(DARK_GRAY_CHESS_BRICK_STAIRS, "Dark Gray Chess Brick Stairs");
		provider.add(DARK_GRAY_CHESS_BRICK_SLAB, "Dark Gray Chess Brick Slab");
		provider.add(DARK_GRAY_CHESS_BRICK_WALL, "Dark Gray Chess Brick Wall");
		provider.add(DARK_GRAY_CHESS_BRICK_TRIM, "Dark Gray Chess Brick Trim");
		
		provider.add(DARK_GRAY_CHESS_BRICK_SMOOTH, "Smooth Dark Gray Chess Brick");
		provider.add(DARK_GRAY_CHESS_BRICK_SMOOTH_STAIRS, "Smooth Dark Gray Chess Brick Stairs");
		provider.add(DARK_GRAY_CHESS_BRICK_SMOOTH_SLAB, "Smooth Dark Gray Chess Brick Slab");
		provider.add(DARK_GRAY_CHESS_BRICK_SMOOTH_WALL, "Smooth Dark Gray Chess Brick Wall");
		provider.add(DARK_GRAY_CHESS_BRICK_SMOOTH_PRESSURE_PLATE, "Smooth Dark Gray Chess Brick Pressure Plate");
		provider.add(DARK_GRAY_CHESS_BRICK_SMOOTH_BUTTON, "Smooth Dark Gray Chess Brick Button");
		
		provider.add(LIGHT_GRAY_CHESS_BRICKS, "Light Gray Chess Bricks");
		provider.add(LIGHT_GRAY_CHESS_BRICK_STAIRS, "Light Gray Chess Brick Stairs");
		provider.add(LIGHT_GRAY_CHESS_BRICK_SLAB, "Light Gray Chess Brick Slab");
		provider.add(LIGHT_GRAY_CHESS_BRICK_WALL, "Light Gray Chess Brick Wall");
		provider.add(LIGHT_GRAY_CHESS_BRICK_TRIM, "Light Gray Chess Brick Trim");
		
		provider.add(LIGHT_GRAY_CHESS_BRICK_SMOOTH, "Smooth Light Gray Chess Brick");
		provider.add(LIGHT_GRAY_CHESS_BRICK_SMOOTH_STAIRS, "Smooth Light Gray Chess Brick Stairs");
		provider.add(LIGHT_GRAY_CHESS_BRICK_SMOOTH_SLAB, "Smooth Light Gray Chess Brick Slab");
		provider.add(LIGHT_GRAY_CHESS_BRICK_SMOOTH_WALL, "Smooth Light Gray Chess Brick Wall");
		provider.add(LIGHT_GRAY_CHESS_BRICK_SMOOTH_PRESSURE_PLATE, "Smooth Light Gray Chess Brick Pressure Plate");
		provider.add(LIGHT_GRAY_CHESS_BRICK_SMOOTH_BUTTON, "Smooth Light Gray Chess Brick Button");
		
		provider.add(CHECKERED_STAINED_GLASS, "Checkered Stained Glass");
		provider.add(BLACK_PAWN_STAINED_GLASS, "Black Pawn Stained Glass");
		provider.add(BLACK_CROWN_STAINED_GLASS, "Black Crown Stained Glass");
		provider.add(WHITE_PAWN_STAINED_GLASS, "White Pawn Stained Glass");
		provider.add(WHITE_CROWN_STAINED_GLASS, "White Crown Stained Glass");
		
	}
	
	public static void addModels(MSBlockStateProvider provider)
	{
		provider.simpleBlock(SKAIA_PORTAL.blockHolder(),
				id -> provider.empty(id.getPath(), itemTexture(id)));
		provider.flatItem(SKAIA_PORTAL.itemHolder(), MSBlockStateProvider::itemTexture);
		
		provider.simpleBlockWithItem(BLACK_CHESS_DIRT);
		provider.simpleBlockWithItem(WHITE_CHESS_DIRT);
		provider.simpleBlockWithItem(DARK_GRAY_CHESS_DIRT);
		provider.simpleBlockWithItem(LIGHT_GRAY_CHESS_DIRT);
		
		provider.simpleBlockWithItem(BLACK_CHESS_BRICKS);
		provider.stairsWithItem(BLACK_CHESS_BRICK_STAIRS::asBlock, "black_chess_brick", texture(BLACK_CHESS_BRICKS.blockHolder()));
		provider.slabWithItem(BLACK_CHESS_BRICK_SLAB::asBlock, BLACK_CHESS_BRICKS.blockHolder());
		provider.wallWithItem(BLACK_CHESS_BRICK_WALL::asBlock, BLACK_CHESS_BRICKS.blockHolder());
		provider.trimWithItem(BLACK_CHESS_BRICK_TRIM.blockHolder(),
				id -> provider.models().cubeColumn(
						id.getPath(),
						texture(id),
						texture("black_chess_bricks")),
				id -> provider.models().cubeColumn(
						id.withSuffix("_flipped").getPath(),
						texture(id.withSuffix("_flipped")),
						texture("black_chess_bricks")));
		
		provider.simpleBlockWithItem(BLACK_CHESS_BRICK_SMOOTH);
		provider.stairsWithItem(BLACK_CHESS_BRICK_SMOOTH_STAIRS::asBlock, BLACK_CHESS_BRICK_SMOOTH.blockHolder());
		provider.slabWithItem(BLACK_CHESS_BRICK_SMOOTH_SLAB::asBlock, BLACK_CHESS_BRICK_SMOOTH.blockHolder());
		provider.wallWithItem(BLACK_CHESS_BRICK_SMOOTH_WALL::asBlock, BLACK_CHESS_BRICK_SMOOTH.blockHolder());
		provider.pressurePlateWithItem(BLACK_CHESS_BRICK_SMOOTH_PRESSURE_PLATE::asBlock, BLACK_CHESS_BRICK_SMOOTH.blockHolder());
		provider.buttonWithItem(BLACK_CHESS_BRICK_SMOOTH_BUTTON::asBlock, BLACK_CHESS_BRICK_SMOOTH.blockHolder());
		
		provider.simpleBlockWithItem(WHITE_CHESS_BRICKS);
		provider.stairsWithItem(WHITE_CHESS_BRICK_STAIRS::asBlock, "white_chess_brick", texture(WHITE_CHESS_BRICKS.blockHolder()));
		provider.slabWithItem(WHITE_CHESS_BRICK_SLAB::asBlock, WHITE_CHESS_BRICKS.blockHolder());
		provider.wallWithItem(WHITE_CHESS_BRICK_WALL::asBlock, WHITE_CHESS_BRICKS.blockHolder());
		provider.trimWithItem(WHITE_CHESS_BRICK_TRIM.blockHolder(),
				id -> provider.models().cubeColumn(
						id.getPath(),
						texture(id),
						texture("white_chess_bricks")),
				id -> provider.models().cubeColumn(
						id.withSuffix("_flipped").getPath(),
						texture(id.withSuffix("_flipped")),
						texture("white_chess_bricks")));
		
		provider.simpleBlockWithItem(WHITE_CHESS_BRICK_SMOOTH);
		provider.stairsWithItem(WHITE_CHESS_BRICK_SMOOTH_STAIRS::asBlock, WHITE_CHESS_BRICK_SMOOTH.blockHolder());
		provider.slabWithItem(WHITE_CHESS_BRICK_SMOOTH_SLAB::asBlock, WHITE_CHESS_BRICK_SMOOTH.blockHolder());
		provider.wallWithItem(WHITE_CHESS_BRICK_SMOOTH_WALL::asBlock, WHITE_CHESS_BRICK_SMOOTH.blockHolder());
		provider.pressurePlateWithItem(WHITE_CHESS_BRICK_SMOOTH_PRESSURE_PLATE::asBlock, WHITE_CHESS_BRICK_SMOOTH.blockHolder());
		provider.buttonWithItem(WHITE_CHESS_BRICK_SMOOTH_BUTTON::asBlock, WHITE_CHESS_BRICK_SMOOTH.blockHolder());
		
		provider.simpleBlockWithItem(DARK_GRAY_CHESS_BRICKS);
		provider.stairsWithItem(DARK_GRAY_CHESS_BRICK_STAIRS::asBlock, "dark_gray_chess_brick", texture(DARK_GRAY_CHESS_BRICKS.blockHolder()));
		provider.slabWithItem(DARK_GRAY_CHESS_BRICK_SLAB::asBlock, DARK_GRAY_CHESS_BRICKS.blockHolder());
		provider.wallWithItem(DARK_GRAY_CHESS_BRICK_WALL::asBlock, DARK_GRAY_CHESS_BRICKS.blockHolder());
		provider.trimWithItem(DARK_GRAY_CHESS_BRICK_TRIM.blockHolder(),
				id -> provider.models().cubeColumn(
						id.getPath(),
						texture(id),
						texture("dark_gray_chess_bricks")),
				id -> provider.models().cubeColumn(
						id.withSuffix("_flipped").getPath(),
						texture(id.withSuffix("_flipped")),
						texture("dark_gray_chess_bricks")));
		
		provider.simpleBlockWithItem(DARK_GRAY_CHESS_BRICK_SMOOTH);
		provider.stairsWithItem(DARK_GRAY_CHESS_BRICK_SMOOTH_STAIRS::asBlock, DARK_GRAY_CHESS_BRICK_SMOOTH.blockHolder());
		provider.slabWithItem(DARK_GRAY_CHESS_BRICK_SMOOTH_SLAB::asBlock, DARK_GRAY_CHESS_BRICK_SMOOTH.blockHolder());
		provider.wallWithItem(DARK_GRAY_CHESS_BRICK_SMOOTH_WALL::asBlock, DARK_GRAY_CHESS_BRICK_SMOOTH.blockHolder());
		provider.pressurePlateWithItem(DARK_GRAY_CHESS_BRICK_SMOOTH_PRESSURE_PLATE::asBlock, DARK_GRAY_CHESS_BRICK_SMOOTH.blockHolder());
		provider.buttonWithItem(DARK_GRAY_CHESS_BRICK_SMOOTH_BUTTON::asBlock, DARK_GRAY_CHESS_BRICK_SMOOTH.blockHolder());
		
		provider.simpleBlockWithItem(LIGHT_GRAY_CHESS_BRICKS);
		provider.stairsWithItem(LIGHT_GRAY_CHESS_BRICK_STAIRS::asBlock, "light_gray_chess_brick", texture(LIGHT_GRAY_CHESS_BRICKS.blockHolder()));
		provider.slabWithItem(LIGHT_GRAY_CHESS_BRICK_SLAB::asBlock, LIGHT_GRAY_CHESS_BRICKS.blockHolder());
		provider.wallWithItem(LIGHT_GRAY_CHESS_BRICK_WALL::asBlock, LIGHT_GRAY_CHESS_BRICKS.blockHolder());
		provider.trimWithItem(LIGHT_GRAY_CHESS_BRICK_TRIM.blockHolder(),
				id -> provider.models().cubeColumn(
						id.getPath(),
						texture(id),
						texture("light_gray_chess_bricks")),
				id -> provider.models().cubeColumn(
						id.withSuffix("_flipped").getPath(),
						texture(id.withSuffix("_flipped")),
						texture("light_gray_chess_bricks")));
		
		provider.simpleBlockWithItem(LIGHT_GRAY_CHESS_BRICK_SMOOTH);
		provider.stairsWithItem(LIGHT_GRAY_CHESS_BRICK_SMOOTH_STAIRS::asBlock, LIGHT_GRAY_CHESS_BRICK_SMOOTH.blockHolder());
		provider.slabWithItem(LIGHT_GRAY_CHESS_BRICK_SMOOTH_SLAB::asBlock, LIGHT_GRAY_CHESS_BRICK_SMOOTH.blockHolder());
		provider.wallWithItem(LIGHT_GRAY_CHESS_BRICK_SMOOTH_WALL::asBlock, LIGHT_GRAY_CHESS_BRICK_SMOOTH.blockHolder());
		provider.pressurePlateWithItem(LIGHT_GRAY_CHESS_BRICK_SMOOTH_PRESSURE_PLATE::asBlock, LIGHT_GRAY_CHESS_BRICK_SMOOTH.blockHolder());
		provider.buttonWithItem(LIGHT_GRAY_CHESS_BRICK_SMOOTH_BUTTON::asBlock, LIGHT_GRAY_CHESS_BRICK_SMOOTH.blockHolder());
		
		provider.simpleBlockWithItem(CHECKERED_STAINED_GLASS.blockHolder(),
				id -> provider.models().cubeAll(id.getPath(), texture(id)).renderType("translucent"));
		provider.simpleBlockWithItem(BLACK_PAWN_STAINED_GLASS.blockHolder(),
				id -> provider.models().cubeAll(id.getPath(), texture(id)).renderType("translucent"));
		provider.simpleBlockWithItem(BLACK_CROWN_STAINED_GLASS.blockHolder(),
				id -> provider.models().cubeAll(id.getPath(), texture(id)).renderType("translucent"));
		provider.simpleBlockWithItem(WHITE_PAWN_STAINED_GLASS.blockHolder(),
				id -> provider.models().cubeAll(id.getPath(), texture(id)).renderType("translucent"));
		provider.simpleBlockWithItem(WHITE_CROWN_STAINED_GLASS.blockHolder(),
				id -> provider.models().cubeAll(id.getPath(), texture(id)).renderType("translucent"));
		
	}
	
	public static void addLootTables(MSBlockLootTables provider)
	{
		provider.dropSelf(BLACK_CHESS_DIRT.asBlock());
		provider.dropSelf(WHITE_CHESS_DIRT.asBlock());
		provider.dropSelf(DARK_GRAY_CHESS_DIRT.asBlock());
		provider.dropSelf(LIGHT_GRAY_CHESS_DIRT.asBlock());
		
		provider.dropSelf(BLACK_CHESS_BRICKS.asBlock());
		provider.dropSelf(BLACK_CHESS_BRICK_STAIRS.asBlock());
		provider.add(BLACK_CHESS_BRICK_SLAB.asBlock(), provider::createSlabItemTable);
		provider.dropSelf(BLACK_CHESS_BRICK_WALL.asBlock());
		provider.dropSelf(BLACK_CHESS_BRICK_TRIM.asBlock());
		
		provider.dropSelf(BLACK_CHESS_BRICK_SMOOTH.asBlock());
		provider.dropSelf(BLACK_CHESS_BRICK_SMOOTH_STAIRS.asBlock());
		provider.add(BLACK_CHESS_BRICK_SMOOTH_SLAB.asBlock(), provider::createSlabItemTable);
		provider.dropSelf(BLACK_CHESS_BRICK_SMOOTH_WALL.asBlock());
		provider.dropSelf(BLACK_CHESS_BRICK_SMOOTH_PRESSURE_PLATE.asBlock());
		provider.dropSelf(BLACK_CHESS_BRICK_SMOOTH_BUTTON.asBlock());
		
		provider.dropSelf(WHITE_CHESS_BRICKS.asBlock());
		provider.dropSelf(WHITE_CHESS_BRICK_STAIRS.asBlock());
		provider.add(WHITE_CHESS_BRICK_SLAB.asBlock(), provider::createSlabItemTable);
		provider.dropSelf(WHITE_CHESS_BRICK_WALL.asBlock());
		provider.dropSelf(WHITE_CHESS_BRICK_TRIM.asBlock());
		
		provider.dropSelf(WHITE_CHESS_BRICK_SMOOTH.asBlock());
		provider.dropSelf(WHITE_CHESS_BRICK_SMOOTH_STAIRS.asBlock());
		provider.add(WHITE_CHESS_BRICK_SMOOTH_SLAB.asBlock(), provider::createSlabItemTable);
		provider.dropSelf(WHITE_CHESS_BRICK_SMOOTH_WALL.asBlock());
		provider.dropSelf(WHITE_CHESS_BRICK_SMOOTH_PRESSURE_PLATE.asBlock());
		provider.dropSelf(WHITE_CHESS_BRICK_SMOOTH_BUTTON.asBlock());
		
		provider.dropSelf(DARK_GRAY_CHESS_BRICKS.asBlock());
		provider.dropSelf(DARK_GRAY_CHESS_BRICK_STAIRS.asBlock());
		provider.add(DARK_GRAY_CHESS_BRICK_SLAB.asBlock(), provider::createSlabItemTable);
		provider.dropSelf(DARK_GRAY_CHESS_BRICK_WALL.asBlock());
		provider.dropSelf(DARK_GRAY_CHESS_BRICK_TRIM.asBlock());
		
		provider.dropSelf(DARK_GRAY_CHESS_BRICK_SMOOTH.asBlock());
		provider.dropSelf(DARK_GRAY_CHESS_BRICK_SMOOTH_STAIRS.asBlock());
		provider.add(DARK_GRAY_CHESS_BRICK_SMOOTH_SLAB.asBlock(), provider::createSlabItemTable);
		provider.dropSelf(DARK_GRAY_CHESS_BRICK_SMOOTH_WALL.asBlock());
		provider.dropSelf(DARK_GRAY_CHESS_BRICK_SMOOTH_PRESSURE_PLATE.asBlock());
		provider.dropSelf(DARK_GRAY_CHESS_BRICK_SMOOTH_BUTTON.asBlock());
		
		provider.dropSelf(LIGHT_GRAY_CHESS_BRICKS.asBlock());
		provider.dropSelf(LIGHT_GRAY_CHESS_BRICK_STAIRS.asBlock());
		provider.add(LIGHT_GRAY_CHESS_BRICK_SLAB.asBlock(), provider::createSlabItemTable);
		provider.dropSelf(LIGHT_GRAY_CHESS_BRICK_WALL.asBlock());
		provider.dropSelf(LIGHT_GRAY_CHESS_BRICK_TRIM.asBlock());
		
		provider.dropSelf(LIGHT_GRAY_CHESS_BRICK_SMOOTH.asBlock());
		provider.dropSelf(LIGHT_GRAY_CHESS_BRICK_SMOOTH_STAIRS.asBlock());
		provider.add(LIGHT_GRAY_CHESS_BRICK_SMOOTH_SLAB.asBlock(), provider::createSlabItemTable);
		provider.dropSelf(LIGHT_GRAY_CHESS_BRICK_SMOOTH_WALL.asBlock());
		provider.dropSelf(LIGHT_GRAY_CHESS_BRICK_SMOOTH_PRESSURE_PLATE.asBlock());
		provider.dropSelf(LIGHT_GRAY_CHESS_BRICK_SMOOTH_BUTTON.asBlock());
		
		provider.dropSelf(CHECKERED_STAINED_GLASS.asBlock());
		provider.dropSelf(BLACK_PAWN_STAINED_GLASS.asBlock());
		provider.dropSelf(BLACK_CROWN_STAINED_GLASS.asBlock());
		provider.dropSelf(WHITE_PAWN_STAINED_GLASS.asBlock());
		provider.dropSelf(WHITE_CROWN_STAINED_GLASS.asBlock());
		
	}
	
	public static void addToBlockTags(MinestuckBlockTagsProvider provider)
	{
		provider.tag(PORTALS).add(SKAIA_PORTAL.asBlock());
		
		provider.tag(MINEABLE_WITH_SHOVEL).add(BLACK_CHESS_DIRT.asBlock(), WHITE_CHESS_DIRT.asBlock(),
				DARK_GRAY_CHESS_DIRT.asBlock(), LIGHT_GRAY_CHESS_DIRT.asBlock());
		
		provider.tag(STAIRS).add(BLACK_CHESS_BRICK_STAIRS.asBlock(), WHITE_CHESS_BRICK_STAIRS.asBlock(),
				DARK_GRAY_CHESS_BRICK_STAIRS.asBlock(), LIGHT_GRAY_CHESS_BRICK_STAIRS.asBlock());
		provider.tag(SLABS).add(BLACK_CHESS_BRICK_SLAB.asBlock(), WHITE_CHESS_BRICK_SLAB.asBlock(),
				DARK_GRAY_CHESS_BRICK_SLAB.asBlock(), LIGHT_GRAY_CHESS_BRICK_SLAB.asBlock());
		provider.tag(WALLS).add(BLACK_CHESS_BRICK_WALL.asBlock(), BLACK_CHESS_BRICK_SMOOTH_WALL.asBlock(),
				WHITE_CHESS_BRICK_WALL.asBlock(), WHITE_CHESS_BRICK_SMOOTH_WALL.asBlock(),
				DARK_GRAY_CHESS_BRICK_WALL.asBlock(), DARK_GRAY_CHESS_BRICK_SMOOTH_WALL.asBlock(),
				LIGHT_GRAY_CHESS_BRICK_WALL.asBlock(), LIGHT_GRAY_CHESS_BRICK_SMOOTH_WALL.asBlock());
		provider.tag(PRESSURE_PLATES).add(BLACK_CHESS_BRICK_SMOOTH_PRESSURE_PLATE.asBlock(), WHITE_CHESS_BRICK_SMOOTH_PRESSURE_PLATE.asBlock(),
				DARK_GRAY_CHESS_BRICK_SMOOTH_PRESSURE_PLATE.asBlock(), LIGHT_GRAY_CHESS_BRICK_SMOOTH_PRESSURE_PLATE.asBlock());
		
		provider.tag(Tags.Blocks.STAINED_GLASS).add(CHECKERED_STAINED_GLASS.asBlock(),
				BLACK_PAWN_STAINED_GLASS.asBlock(), BLACK_CROWN_STAINED_GLASS.asBlock(),
				WHITE_PAWN_STAINED_GLASS.asBlock(), WHITE_CROWN_STAINED_GLASS.asBlock());
		provider.tag(IMPERMEABLE).add(CHECKERED_STAINED_GLASS.asBlock(),
				BLACK_PAWN_STAINED_GLASS.asBlock(), BLACK_CROWN_STAINED_GLASS.asBlock(),
				WHITE_PAWN_STAINED_GLASS.asBlock(), WHITE_CROWN_STAINED_GLASS.asBlock());
		
		
		provider.needsWoodPickaxe(BLACK_CHESS_BRICKS.asBlock(), BLACK_CHESS_BRICK_STAIRS.asBlock(), BLACK_CHESS_BRICK_SLAB.asBlock(),
				BLACK_CHESS_BRICK_WALL.asBlock(), BLACK_CHESS_BRICK_TRIM.asBlock(),
				BLACK_CHESS_BRICK_SMOOTH.asBlock(), BLACK_CHESS_BRICK_SMOOTH_STAIRS.asBlock(), BLACK_CHESS_BRICK_SMOOTH_SLAB.asBlock(),
				BLACK_CHESS_BRICK_SMOOTH_WALL.asBlock(), BLACK_CHESS_BRICK_SMOOTH_PRESSURE_PLATE.asBlock(), BLACK_CHESS_BRICK_SMOOTH_BUTTON.asBlock());
		
		provider.needsWoodPickaxe(WHITE_CHESS_BRICKS.asBlock(), WHITE_CHESS_BRICK_STAIRS.asBlock(), WHITE_CHESS_BRICK_SLAB.asBlock(),
				WHITE_CHESS_BRICK_WALL.asBlock(), WHITE_CHESS_BRICK_TRIM.asBlock(),
				WHITE_CHESS_BRICK_SMOOTH.asBlock(), WHITE_CHESS_BRICK_SMOOTH_STAIRS.asBlock(), WHITE_CHESS_BRICK_SMOOTH_SLAB.asBlock(),
				WHITE_CHESS_BRICK_SMOOTH_WALL.asBlock(), WHITE_CHESS_BRICK_SMOOTH_PRESSURE_PLATE.asBlock(), WHITE_CHESS_BRICK_SMOOTH_BUTTON.asBlock());
		
		provider.needsWoodPickaxe(DARK_GRAY_CHESS_BRICKS.asBlock(), DARK_GRAY_CHESS_BRICK_STAIRS.asBlock(), DARK_GRAY_CHESS_BRICK_SLAB.asBlock(),
				DARK_GRAY_CHESS_BRICK_WALL.asBlock(), DARK_GRAY_CHESS_BRICK_TRIM.asBlock(),
				DARK_GRAY_CHESS_BRICK_SMOOTH.asBlock(), DARK_GRAY_CHESS_BRICK_SMOOTH_STAIRS.asBlock(), DARK_GRAY_CHESS_BRICK_SMOOTH_SLAB.asBlock(),
				DARK_GRAY_CHESS_BRICK_SMOOTH_WALL.asBlock(), DARK_GRAY_CHESS_BRICK_SMOOTH_PRESSURE_PLATE.asBlock(), DARK_GRAY_CHESS_BRICK_SMOOTH_BUTTON.asBlock());
		
		provider.needsWoodPickaxe(LIGHT_GRAY_CHESS_BRICKS.asBlock(), LIGHT_GRAY_CHESS_BRICK_STAIRS.asBlock(), LIGHT_GRAY_CHESS_BRICK_SLAB.asBlock(),
				LIGHT_GRAY_CHESS_BRICK_WALL.asBlock(), LIGHT_GRAY_CHESS_BRICK_TRIM.asBlock(),
				LIGHT_GRAY_CHESS_BRICK_SMOOTH.asBlock(), LIGHT_GRAY_CHESS_BRICK_SMOOTH_STAIRS.asBlock(), LIGHT_GRAY_CHESS_BRICK_SMOOTH_SLAB.asBlock(),
				LIGHT_GRAY_CHESS_BRICK_SMOOTH_WALL.asBlock(), LIGHT_GRAY_CHESS_BRICK_SMOOTH_PRESSURE_PLATE.asBlock(), LIGHT_GRAY_CHESS_BRICK_SMOOTH_BUTTON.asBlock());
		
	}
	
	public static void addRecipes(RecipeOutput recipeSaver)
	{
		CommonRecipes.stairsRecipe(BLACK_CHESS_BRICK_STAIRS, BLACK_CHESS_BRICKS).save(recipeSaver);
		CommonRecipes.slabRecipe(BLACK_CHESS_BRICK_SLAB, BLACK_CHESS_BRICKS).save(recipeSaver);
		CommonRecipes.wallRecipe(BLACK_CHESS_BRICK_WALL, BLACK_CHESS_BRICKS).group("stone_wall").save(recipeSaver);
		SingleItemRecipeBuilder.stonecutting(Ingredient.of(BLACK_CHESS_BRICKS), RecipeCategory.BUILDING_BLOCKS, BLACK_CHESS_BRICK_STAIRS)
				.unlockedBy("has_black_chess_bricks", has(BLACK_CHESS_BRICKS)).save(recipeSaver, Minestuck.id("black_chess_brick_stairs_from_black_chess_bricks_stonecutting"));
		SingleItemRecipeBuilder.stonecutting(Ingredient.of(BLACK_CHESS_BRICKS), RecipeCategory.BUILDING_BLOCKS, BLACK_CHESS_BRICK_SLAB, 2)
				.unlockedBy("has_black_chess_bricks", has(BLACK_CHESS_BRICKS)).save(recipeSaver, Minestuck.id("black_chess_brick_slab_from_black_chess_bricks_stonecutting"));
		SingleItemRecipeBuilder.stonecutting(Ingredient.of(BLACK_CHESS_BRICKS), RecipeCategory.BUILDING_BLOCKS, BLACK_CHESS_BRICK_WALL, 2)
				.unlockedBy("has_black_chess_bricks", has(BLACK_CHESS_BRICKS)).save(recipeSaver, Minestuck.id("black_chess_stairs_from_stonecutting"));
		
		CommonRecipes.stairsRecipe(BLACK_CHESS_BRICK_SMOOTH_STAIRS, BLACK_CHESS_BRICK_SMOOTH).group("wooden_stairs").save(recipeSaver);
		CommonRecipes.slabRecipe(BLACK_CHESS_BRICK_SMOOTH_SLAB, BLACK_CHESS_BRICK_SMOOTH).group("wooden_slab").save(recipeSaver);
		CommonRecipes.wallRecipe(BLACK_CHESS_BRICK_SMOOTH_WALL, BLACK_CHESS_BRICK_SMOOTH).group("stone_wall").save(recipeSaver);
		CommonRecipes.pressurePlateRecipe(BLACK_CHESS_BRICK_SMOOTH_PRESSURE_PLATE, BLACK_CHESS_BRICK_SMOOTH).group("stone_pressure_plate").save(recipeSaver);
		CommonRecipes.buttonRecipe(BLACK_CHESS_BRICK_SMOOTH_BUTTON, BLACK_CHESS_BRICK_SMOOTH).save(recipeSaver);
		SingleItemRecipeBuilder.stonecutting(Ingredient.of(BLACK_CHESS_BRICK_SMOOTH), RecipeCategory.BUILDING_BLOCKS, BLACK_CHESS_BRICK_SMOOTH_STAIRS)
				.unlockedBy("has_black_chess_brick_smooth", has(BLACK_CHESS_BRICK_SMOOTH)).save(recipeSaver, Minestuck.id("black_chess_brick_smooth_stairs_from_stonecutting"));
		SingleItemRecipeBuilder.stonecutting(Ingredient.of(BLACK_CHESS_BRICK_SMOOTH), RecipeCategory.BUILDING_BLOCKS, BLACK_CHESS_BRICK_SMOOTH_SLAB, 2)
				.unlockedBy("has_black_chess_brick_smooth", has(BLACK_CHESS_BRICK_SMOOTH)).save(recipeSaver, Minestuck.id("black_chess_brick_smooth_slab_from_stonecutting"));
		SingleItemRecipeBuilder.stonecutting(Ingredient.of(BLACK_CHESS_BRICK_SMOOTH), RecipeCategory.BUILDING_BLOCKS, BLACK_CHESS_BRICK_SMOOTH_WALL, 2)
				.unlockedBy("has_black_chess_brick_smooth", has(BLACK_CHESS_BRICK_SMOOTH)).save(recipeSaver, Minestuck.id("black_chess_brick_smooth_wall_from_stonecutting"));
		
		
		CommonRecipes.stairsRecipe(WHITE_CHESS_BRICK_STAIRS, WHITE_CHESS_BRICKS).save(recipeSaver);
		CommonRecipes.slabRecipe(WHITE_CHESS_BRICK_SLAB, WHITE_CHESS_BRICKS).save(recipeSaver);
		CommonRecipes.wallRecipe(WHITE_CHESS_BRICK_WALL, WHITE_CHESS_BRICKS).group("stone_wall").save(recipeSaver);
		SingleItemRecipeBuilder.stonecutting(Ingredient.of(WHITE_CHESS_BRICKS), RecipeCategory.BUILDING_BLOCKS, WHITE_CHESS_BRICK_STAIRS)
				.unlockedBy("has_white_chess_bricks", has(WHITE_CHESS_BRICKS)).save(recipeSaver, Minestuck.id("white_chess_brick_stairs_from_white_chess_bricks_stonecutting"));
		SingleItemRecipeBuilder.stonecutting(Ingredient.of(WHITE_CHESS_BRICKS), RecipeCategory.BUILDING_BLOCKS, WHITE_CHESS_BRICK_SLAB, 2)
				.unlockedBy("has_white_chess_bricks", has(WHITE_CHESS_BRICKS)).save(recipeSaver, Minestuck.id("white_chess_brick_slab_from_white_chess_bricks_stonecutting"));
		SingleItemRecipeBuilder.stonecutting(Ingredient.of(WHITE_CHESS_BRICKS), RecipeCategory.BUILDING_BLOCKS, WHITE_CHESS_BRICK_WALL, 2)
				.unlockedBy("has_white_chess_bricks", has(WHITE_CHESS_BRICKS)).save(recipeSaver, Minestuck.id("white_chess_brick_wall_from_stonecutting"));
		
		CommonRecipes.stairsRecipe(WHITE_CHESS_BRICK_SMOOTH_STAIRS, WHITE_CHESS_BRICK_SMOOTH).group("wooden_stairs").save(recipeSaver);
		CommonRecipes.slabRecipe(WHITE_CHESS_BRICK_SMOOTH_SLAB, WHITE_CHESS_BRICK_SMOOTH).group("wooden_slab").save(recipeSaver);
		CommonRecipes.wallRecipe(WHITE_CHESS_BRICK_SMOOTH_WALL, WHITE_CHESS_BRICK_SMOOTH).group("stone_wall").save(recipeSaver);
		CommonRecipes.buttonRecipe(WHITE_CHESS_BRICK_SMOOTH_BUTTON, WHITE_CHESS_BRICK_SMOOTH).save(recipeSaver);
		CommonRecipes.pressurePlateRecipe(WHITE_CHESS_BRICK_SMOOTH_PRESSURE_PLATE, WHITE_CHESS_BRICK_SMOOTH).group("stone_pressure_plate").save(recipeSaver);
		SingleItemRecipeBuilder.stonecutting(Ingredient.of(WHITE_CHESS_BRICK_SMOOTH), RecipeCategory.BUILDING_BLOCKS, WHITE_CHESS_BRICK_SMOOTH_STAIRS)
				.unlockedBy("has_white_chess_brick_smooth", has(WHITE_CHESS_BRICK_SMOOTH)).save(recipeSaver, Minestuck.id("white_chess_brick_smooth_stairs_from_stonecutting"));
		SingleItemRecipeBuilder.stonecutting(Ingredient.of(WHITE_CHESS_BRICK_SMOOTH), RecipeCategory.BUILDING_BLOCKS, WHITE_CHESS_BRICK_SMOOTH_SLAB, 2)
				.unlockedBy("has_white_chess_brick_smooth", has(WHITE_CHESS_BRICK_SMOOTH)).save(recipeSaver, Minestuck.id("white_chess_brick_smooth_slab_from_stonecutting"));
		SingleItemRecipeBuilder.stonecutting(Ingredient.of(WHITE_CHESS_BRICK_SMOOTH), RecipeCategory.BUILDING_BLOCKS, WHITE_CHESS_BRICK_SMOOTH_WALL, 2)
				.unlockedBy("has_white_chess_brick_smooth", has(WHITE_CHESS_BRICK_SMOOTH)).save(recipeSaver, Minestuck.id("white_chess_brick_smooth_wall_from_stonecutting"));
		
		
		CommonRecipes.stairsRecipe(DARK_GRAY_CHESS_BRICK_STAIRS, DARK_GRAY_CHESS_BRICKS).save(recipeSaver);
		CommonRecipes.slabRecipe(DARK_GRAY_CHESS_BRICK_SLAB, DARK_GRAY_CHESS_BRICKS).save(recipeSaver);
		CommonRecipes.wallRecipe(DARK_GRAY_CHESS_BRICK_WALL, DARK_GRAY_CHESS_BRICKS).group("stone_wall").save(recipeSaver);
		SingleItemRecipeBuilder.stonecutting(Ingredient.of(DARK_GRAY_CHESS_BRICKS), RecipeCategory.BUILDING_BLOCKS, DARK_GRAY_CHESS_BRICK_STAIRS)
				.unlockedBy("has_dark_gray_chess_bricks", has(DARK_GRAY_CHESS_BRICKS)).save(recipeSaver, Minestuck.id("dark_gray_chess_brick_stairs_from_dark_gray_chess_bricks_stonecutting"));
		SingleItemRecipeBuilder.stonecutting(Ingredient.of(DARK_GRAY_CHESS_BRICKS), RecipeCategory.BUILDING_BLOCKS, DARK_GRAY_CHESS_BRICK_SLAB, 2)
				.unlockedBy("has_dark_gray_chess_bricks", has(DARK_GRAY_CHESS_BRICKS)).save(recipeSaver, Minestuck.id("dark_gray_chess_brick_slab_from_dark_gray_chess_bricks_stonecutting"));
		SingleItemRecipeBuilder.stonecutting(Ingredient.of(DARK_GRAY_CHESS_BRICKS), RecipeCategory.BUILDING_BLOCKS, DARK_GRAY_CHESS_BRICK_WALL, 2)
				.unlockedBy("has_dark_gray_chess_bricks", has(DARK_GRAY_CHESS_BRICKS)).save(recipeSaver, Minestuck.id("dark_gray_chess_brick_wall_from_stonecutting"));
		
		CommonRecipes.stairsRecipe(DARK_GRAY_CHESS_BRICK_SMOOTH_STAIRS, DARK_GRAY_CHESS_BRICK_SMOOTH).group("wooden_stairs").save(recipeSaver);
		CommonRecipes.slabRecipe(DARK_GRAY_CHESS_BRICK_SMOOTH_SLAB, DARK_GRAY_CHESS_BRICK_SMOOTH).group("wooden_slab").save(recipeSaver);
		CommonRecipes.wallRecipe(DARK_GRAY_CHESS_BRICK_SMOOTH_WALL, DARK_GRAY_CHESS_BRICK_SMOOTH).group("stone_wall").save(recipeSaver);
		CommonRecipes.pressurePlateRecipe(DARK_GRAY_CHESS_BRICK_SMOOTH_PRESSURE_PLATE, DARK_GRAY_CHESS_BRICK_SMOOTH).group("stone_pressure_plate").save(recipeSaver);
		CommonRecipes.buttonRecipe(DARK_GRAY_CHESS_BRICK_SMOOTH_BUTTON, DARK_GRAY_CHESS_BRICK_SMOOTH).save(recipeSaver);
		SingleItemRecipeBuilder.stonecutting(Ingredient.of(DARK_GRAY_CHESS_BRICK_SMOOTH), RecipeCategory.BUILDING_BLOCKS, DARK_GRAY_CHESS_BRICK_SMOOTH_STAIRS)
				.unlockedBy("has_dark_gray_chess_brick_smooth", has(DARK_GRAY_CHESS_BRICK_SMOOTH)).save(recipeSaver, Minestuck.id("dark_gray_chess_brick_smooth_stairs_from_stonecutting"));
		SingleItemRecipeBuilder.stonecutting(Ingredient.of(DARK_GRAY_CHESS_BRICK_SMOOTH), RecipeCategory.BUILDING_BLOCKS, DARK_GRAY_CHESS_BRICK_SMOOTH_SLAB, 2)
				.unlockedBy("has_dark_gray_chess_brick_smooth", has(DARK_GRAY_CHESS_BRICK_SMOOTH)).save(recipeSaver, Minestuck.id("dark_gray_chess_brick_smooth_slab_from_stonecutting"));
		SingleItemRecipeBuilder.stonecutting(Ingredient.of(DARK_GRAY_CHESS_BRICK_SMOOTH), RecipeCategory.BUILDING_BLOCKS, DARK_GRAY_CHESS_BRICK_SMOOTH_WALL, 2)
				.unlockedBy("has_dark_gray_chess_brick_smooth", has(DARK_GRAY_CHESS_BRICK_SMOOTH)).save(recipeSaver, Minestuck.id("dark_gray_chess_brick_smooth_wall_from_stonecutting"));
		
		
		CommonRecipes.stairsRecipe(LIGHT_GRAY_CHESS_BRICK_STAIRS, LIGHT_GRAY_CHESS_BRICKS).save(recipeSaver);
		CommonRecipes.slabRecipe(LIGHT_GRAY_CHESS_BRICK_SLAB, LIGHT_GRAY_CHESS_BRICKS).save(recipeSaver);
		CommonRecipes.wallRecipe(LIGHT_GRAY_CHESS_BRICK_WALL, LIGHT_GRAY_CHESS_BRICKS).group("stone_wall").save(recipeSaver);
		SingleItemRecipeBuilder.stonecutting(Ingredient.of(LIGHT_GRAY_CHESS_BRICKS), RecipeCategory.BUILDING_BLOCKS, LIGHT_GRAY_CHESS_BRICK_STAIRS)
				.unlockedBy("has_light_gray_chess_bricks", has(LIGHT_GRAY_CHESS_BRICKS)).save(recipeSaver, Minestuck.id("light_gray_chess_brick_stairs_from_light_gray_chess_bricks_stonecutting"));
		SingleItemRecipeBuilder.stonecutting(Ingredient.of(LIGHT_GRAY_CHESS_BRICKS), RecipeCategory.BUILDING_BLOCKS, LIGHT_GRAY_CHESS_BRICK_SLAB, 2)
				.unlockedBy("has_light_gray_chess_bricks", has(LIGHT_GRAY_CHESS_BRICKS)).save(recipeSaver, Minestuck.id("light_gray_chess_brick_slab_from_light_gray_chess_bricks_stonecutting"));
		SingleItemRecipeBuilder.stonecutting(Ingredient.of(LIGHT_GRAY_CHESS_BRICKS), RecipeCategory.BUILDING_BLOCKS, LIGHT_GRAY_CHESS_BRICK_WALL, 2)
				.unlockedBy("has_light_gray_chess_bricks", has(LIGHT_GRAY_CHESS_BRICKS)).save(recipeSaver, Minestuck.id("light_gray_chess_brick_wall_from_stonecutting"));
		
		CommonRecipes.stairsRecipe(LIGHT_GRAY_CHESS_BRICK_SMOOTH_STAIRS, LIGHT_GRAY_CHESS_BRICK_SMOOTH).group("wooden_stairs").save(recipeSaver);
		CommonRecipes.slabRecipe(LIGHT_GRAY_CHESS_BRICK_SMOOTH_SLAB, LIGHT_GRAY_CHESS_BRICK_SMOOTH).group("wooden_slab").save(recipeSaver);
		CommonRecipes.wallRecipe(LIGHT_GRAY_CHESS_BRICK_SMOOTH_WALL, LIGHT_GRAY_CHESS_BRICK_SMOOTH).group("stone_wall").save(recipeSaver);
		CommonRecipes.pressurePlateRecipe(LIGHT_GRAY_CHESS_BRICK_SMOOTH_PRESSURE_PLATE, LIGHT_GRAY_CHESS_BRICK_SMOOTH).group("stone_pressure_plate").save(recipeSaver);
		CommonRecipes.buttonRecipe(LIGHT_GRAY_CHESS_BRICK_SMOOTH_BUTTON, LIGHT_GRAY_CHESS_BRICK_SMOOTH).save(recipeSaver);
		SingleItemRecipeBuilder.stonecutting(Ingredient.of(LIGHT_GRAY_CHESS_BRICK_SMOOTH), RecipeCategory.BUILDING_BLOCKS, LIGHT_GRAY_CHESS_BRICK_SMOOTH_STAIRS)
				.unlockedBy("has_light_gray_chess_brick_smooth", has(LIGHT_GRAY_CHESS_BRICK_SMOOTH)).save(recipeSaver, Minestuck.id("light_gray_chess_brick_smooth_stairs_from_stonecutting"));
		SingleItemRecipeBuilder.stonecutting(Ingredient.of(LIGHT_GRAY_CHESS_BRICK_SMOOTH), RecipeCategory.BUILDING_BLOCKS, LIGHT_GRAY_CHESS_BRICK_SMOOTH_SLAB, 2)
				.unlockedBy("has_light_gray_chess_brick_smooth", has(LIGHT_GRAY_CHESS_BRICK_SMOOTH)).save(recipeSaver, Minestuck.id("light_gray_chess_brick_smooth_slab_from_stonecutting"));
		SingleItemRecipeBuilder.stonecutting(Ingredient.of(LIGHT_GRAY_CHESS_BRICK_SMOOTH), RecipeCategory.BUILDING_BLOCKS, LIGHT_GRAY_CHESS_BRICK_SMOOTH_WALL, 2)
				.unlockedBy("has_light_gray_chess_brick_smooth", has(LIGHT_GRAY_CHESS_BRICK_SMOOTH)).save(recipeSaver, Minestuck.id("light_gray_chess_brick_smooth_wall_from_stonecutting"));
		
		addGristCosts(recipeSaver);
		addCombinations(recipeSaver);
	}
	
	private static void addGristCosts(RecipeOutput recipeSaver)
	{
		GristCostRecipeBuilder.of(BLACK_CHESS_DIRT).grist(GristTypes.BUILD, 1).build(recipeSaver);
		GristCostRecipeBuilder.of(WHITE_CHESS_DIRT).grist(GristTypes.BUILD, 1).build(recipeSaver);
		GristCostRecipeBuilder.of(DARK_GRAY_CHESS_DIRT).grist(GristTypes.BUILD, 1).build(recipeSaver);
		GristCostRecipeBuilder.of(LIGHT_GRAY_CHESS_DIRT).grist(GristTypes.BUILD, 1).build(recipeSaver);
		
		GristCostRecipeBuilder.of(BLACK_CHESS_BRICKS).grist(GristTypes.BUILD, 2).build(recipeSaver);
		GristCostRecipeBuilder.of(BLACK_CHESS_BRICK_TRIM).grist(GristTypes.BUILD, 3).build(recipeSaver);
		GristCostRecipeBuilder.of(BLACK_CHESS_BRICK_SMOOTH).grist(GristTypes.BUILD, 2).build(recipeSaver);
		
		GristCostRecipeBuilder.of(WHITE_CHESS_BRICKS).grist(GristTypes.BUILD, 2).build(recipeSaver);
		GristCostRecipeBuilder.of(WHITE_CHESS_BRICK_TRIM).grist(GristTypes.BUILD, 3).build(recipeSaver);
		GristCostRecipeBuilder.of(WHITE_CHESS_BRICK_SMOOTH).grist(GristTypes.BUILD, 2).build(recipeSaver);
		
		GristCostRecipeBuilder.of(DARK_GRAY_CHESS_BRICKS).grist(GristTypes.BUILD, 2).build(recipeSaver);
		GristCostRecipeBuilder.of(DARK_GRAY_CHESS_BRICK_TRIM).grist(GristTypes.BUILD, 3).build(recipeSaver);
		GristCostRecipeBuilder.of(DARK_GRAY_CHESS_BRICK_SMOOTH).grist(GristTypes.BUILD, 2).build(recipeSaver);
		
		GristCostRecipeBuilder.of(LIGHT_GRAY_CHESS_BRICKS).grist(GristTypes.BUILD, 2).build(recipeSaver);
		GristCostRecipeBuilder.of(LIGHT_GRAY_CHESS_BRICK_TRIM).grist(GristTypes.BUILD, 3).build(recipeSaver);
		GristCostRecipeBuilder.of(LIGHT_GRAY_CHESS_BRICK_SMOOTH).grist(GristTypes.BUILD, 2).build(recipeSaver);
		
		
		GristCostRecipeBuilder.of(CHECKERED_STAINED_GLASS).grist(GristTypes.BUILD, 5).build(recipeSaver);
		GristCostRecipeBuilder.of(BLACK_PAWN_STAINED_GLASS).grist(GristTypes.BUILD, 5).build(recipeSaver);
		GristCostRecipeBuilder.of(BLACK_CROWN_STAINED_GLASS).grist(GristTypes.BUILD, 5).build(recipeSaver);
		GristCostRecipeBuilder.of(WHITE_PAWN_STAINED_GLASS).grist(GristTypes.BUILD, 5).build(recipeSaver);
		GristCostRecipeBuilder.of(WHITE_CROWN_STAINED_GLASS).grist(GristTypes.BUILD, 5).build(recipeSaver);
		
	}
	
	private static void addCombinations(RecipeOutput recipeSaver)
	{
		CombinationRecipeBuilder.of(BLACK_CHESS_DIRT).input(Items.DIRT).and().input(Items.BLACK_DYE).build(recipeSaver);
		CombinationRecipeBuilder.of(WHITE_CHESS_DIRT).input(Items.DIRT).and().input(Items.WHITE_DYE).build(recipeSaver);
		CombinationRecipeBuilder.of(DARK_GRAY_CHESS_DIRT).input(Items.DIRT).and().input(Items.GRAY_DYE).build(recipeSaver);
		CombinationRecipeBuilder.of(LIGHT_GRAY_CHESS_DIRT).input(Items.DIRT).and().input(Items.LIGHT_GRAY_DYE).build(recipeSaver);
		
		CombinationRecipeBuilder.of(BLACK_CHESS_BRICKS).input(Items.STONE_BRICKS).or().input(BLACK_CHESS_DIRT).build(recipeSaver);
		CombinationRecipeBuilder.of(BLACK_CHESS_BRICK_TRIM).input(Items.YELLOW_WOOL).and().input(BLACK_CHESS_BRICKS).build(recipeSaver);
		CombinationRecipeBuilder.of(BLACK_CHESS_BRICK_SMOOTH).input(Items.STONE).or().input(BLACK_CHESS_BRICKS).build(recipeSaver);
		
		CombinationRecipeBuilder.of(WHITE_CHESS_BRICKS).input(Items.STONE_BRICKS).or().input(WHITE_CHESS_DIRT).build(recipeSaver);
		CombinationRecipeBuilder.of(WHITE_CHESS_BRICK_TRIM).input(Items.YELLOW_WOOL).and().input(WHITE_CHESS_BRICKS).build(recipeSaver);
		CombinationRecipeBuilder.of(WHITE_CHESS_BRICK_SMOOTH).input(Items.STONE).or().input(WHITE_CHESS_BRICKS).build(recipeSaver);
		
		CombinationRecipeBuilder.of(DARK_GRAY_CHESS_BRICKS).input(Items.STONE_BRICKS).or().input(DARK_GRAY_CHESS_DIRT).build(recipeSaver);
		CombinationRecipeBuilder.of(DARK_GRAY_CHESS_BRICK_TRIM).input(Items.YELLOW_WOOL).and().input(DARK_GRAY_CHESS_BRICKS).build(recipeSaver);
		CombinationRecipeBuilder.of(DARK_GRAY_CHESS_BRICK_SMOOTH).input(Items.STONE).or().input(DARK_GRAY_CHESS_BRICKS).build(recipeSaver);
		
		CombinationRecipeBuilder.of(LIGHT_GRAY_CHESS_BRICKS).input(Items.STONE_BRICKS).or().input(LIGHT_GRAY_CHESS_DIRT).build(recipeSaver);
		CombinationRecipeBuilder.of(LIGHT_GRAY_CHESS_BRICK_TRIM).input(Items.YELLOW_WOOL).and().input(LIGHT_GRAY_CHESS_BRICKS).build(recipeSaver);
		CombinationRecipeBuilder.of(LIGHT_GRAY_CHESS_BRICK_SMOOTH).input(Items.STONE).or().input(LIGHT_GRAY_CHESS_BRICKS).build(recipeSaver);
		
		
		CombinationRecipeBuilder.of(CHECKERED_STAINED_GLASS).input(Items.BLUE_STAINED_GLASS).and().input(MSBlocks.CHESSBOARD).build(recipeSaver);
		CombinationRecipeBuilder.of(BLACK_PAWN_STAINED_GLASS).input(Items.BLACK_STAINED_GLASS).and().input(MSBlocks.CHESSBOARD).build(recipeSaver);
		CombinationRecipeBuilder.of(BLACK_CROWN_STAINED_GLASS).input(BLACK_PAWN_STAINED_GLASS).and().input(MSItems.PRIM_AND_PROPER_WALKING_POLE).build(recipeSaver);
		CombinationRecipeBuilder.of(WHITE_PAWN_STAINED_GLASS).input(Items.WHITE_STAINED_GLASS).and().input(MSBlocks.CHESSBOARD).build(recipeSaver);
		CombinationRecipeBuilder.of(WHITE_CROWN_STAINED_GLASS).input(WHITE_PAWN_STAINED_GLASS).and().input(MSItems.PRIM_AND_PROPER_WALKING_POLE).build(recipeSaver);
		
	}
}
