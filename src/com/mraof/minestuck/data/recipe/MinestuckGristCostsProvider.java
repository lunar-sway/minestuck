package com.mraof.minestuck.data.recipe;

import com.mraof.minestuck.Minestuck;
import com.mraof.minestuck.item.MSItems;
import com.mraof.minestuck.item.crafting.alchemy.GristTypes;
import com.mraof.minestuck.util.ExtraForgeTags;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.IFinishedRecipe;
import net.minecraft.data.RecipeProvider;
import net.minecraft.item.Items;
import net.minecraft.tags.ItemTags;
import net.minecraftforge.common.Tags;

import java.util.function.Consumer;

public class MinestuckGristCostsProvider extends RecipeProvider
{
	public MinestuckGristCostsProvider(DataGenerator generator)
	{
		super(generator);
	}
	
	@Override
	protected void registerRecipes(Consumer<IFinishedRecipe> recipeSaver)
	{
		//Blocks
		GristCostRecipeBuilder.of(Tags.Items.STONE).grist(GristTypes.BUILD, 2).buildFor(recipeSaver, Minestuck.MOD_ID);
		GristCostRecipeBuilder.of(Tags.Items.COBBLESTONE).grist(GristTypes.BUILD, 2).buildFor(recipeSaver, Minestuck.MOD_ID);
		GristCostRecipeBuilder.of(Items.GRAVEL).grist(GristTypes.BUILD, 3).buildFor(recipeSaver, Minestuck.MOD_ID);
		GristCostRecipeBuilder.of(ItemTags.SAND).grist(GristTypes.BUILD, 2).buildFor(recipeSaver, Minestuck.MOD_ID);
		GristCostRecipeBuilder.of(ExtraForgeTags.Items.TERRACOTTA).grist(GristTypes.SHALE, 12).grist(GristTypes.MARBLE, 4).buildFor(recipeSaver, Minestuck.MOD_ID);
		GristCostRecipeBuilder.of(Items.DIRT).grist(GristTypes.BUILD, 2).buildFor(recipeSaver, Minestuck.MOD_ID);
		GristCostRecipeBuilder.of(Items.GRASS_BLOCK).grist(GristTypes.BUILD, 2).buildFor(recipeSaver, Minestuck.MOD_ID);
		GristCostRecipeBuilder.of(Items.PODZOL).grist(GristTypes.BUILD, 2).buildFor(recipeSaver, Minestuck.MOD_ID);
		
		GristCostRecipeBuilder.of(ItemTags.LOGS).grist(GristTypes.BUILD, 8).buildFor(recipeSaver, Minestuck.MOD_ID);
		GristCostRecipeBuilder.of(ItemTags.LEAVES).grist(GristTypes.BUILD, 1).buildFor(recipeSaver, Minestuck.MOD_ID);
		GristCostRecipeBuilder.of(ItemTags.SAPLINGS).grist(GristTypes.BUILD, 16).buildFor(recipeSaver, Minestuck.MOD_ID);
		GristCostRecipeBuilder.of(ItemTags.PLANKS).grist(GristTypes.BUILD, 2).buildFor(recipeSaver, Minestuck.MOD_ID);
		GristCostRecipeBuilder.of(Items.DEAD_BUSH).grist(GristTypes.AMBER, 2).grist(GristTypes.SULFUR, 1).buildFor(recipeSaver, Minestuck.MOD_ID);
		GristCostRecipeBuilder.of(Items.DRAGON_EGG).grist(GristTypes.URANIUM, 800).grist(GristTypes.TAR, 800).grist(GristTypes.ZILLIUM, 10).buildFor(recipeSaver, Minestuck.MOD_ID);
		GristCostRecipeBuilder.of(Items.END_STONE).grist(GristTypes.CAULK, 3).grist(GristTypes.BUILD, 4).buildFor(recipeSaver, Minestuck.MOD_ID);
		GristCostRecipeBuilder.of(Items.GLASS).grist(GristTypes.BUILD, 1).buildFor(recipeSaver, Minestuck.MOD_ID);
		GristCostRecipeBuilder.of(Items.ICE).grist(GristTypes.COBALT, 2).buildFor(recipeSaver, Minestuck.MOD_ID);
		GristCostRecipeBuilder.of(Items.MELON).grist(GristTypes.AMBER, 8).grist(GristTypes.CAULK, 8).grist(GristTypes.BUILD, 2).buildFor(recipeSaver, Minestuck.MOD_ID);
		GristCostRecipeBuilder.of(Items.MYCELIUM).grist(GristTypes.IODINE, 2).grist(GristTypes.RUBY, 2).grist(GristTypes.BUILD, 2).buildFor(recipeSaver, Minestuck.MOD_ID);
		GristCostRecipeBuilder.of(Items.NETHERRACK).grist(GristTypes.BUILD, 2).grist(GristTypes.TAR, 1).buildFor(recipeSaver, Minestuck.MOD_ID);
		GristCostRecipeBuilder.of(Items.OBSIDIAN).grist(GristTypes.BUILD, 6).grist(GristTypes.COBALT, 8).grist(GristTypes.TAR, 16).buildFor(recipeSaver, Minestuck.MOD_ID);
		GristCostRecipeBuilder.of(Items.PUMPKIN).grist(GristTypes.AMBER, 12).grist(GristTypes.CAULK, 6).buildFor(recipeSaver, Minestuck.MOD_ID);
		GristCostRecipeBuilder.of(Items.SNOW_BLOCK).grist(GristTypes.BUILD, 5).grist(GristTypes.COBALT, 3).buildFor(recipeSaver, Minestuck.MOD_ID);
		GristCostRecipeBuilder.of(Items.SNOW).grist(GristTypes.COBALT, 2).buildFor(recipeSaver, Minestuck.MOD_ID);
		GristCostRecipeBuilder.of(Items.SOUL_SAND).grist(GristTypes.BUILD, 2).grist(GristTypes.SULFUR, 5).grist(GristTypes.CAULK, 3).buildFor(recipeSaver, Minestuck.MOD_ID);
		GristCostRecipeBuilder.of(Items.SPONGE).grist(GristTypes.AMBER, 20).grist(GristTypes.SULFUR, 30).buildFor(recipeSaver, Minestuck.MOD_ID);
		GristCostRecipeBuilder.of(Items.WET_SPONGE).grist(GristTypes.AMBER, 20).grist(GristTypes.SULFUR, 30).grist(GristTypes.COBALT, 10).buildFor(recipeSaver, Minestuck.MOD_ID);
		GristCostRecipeBuilder.of(Items.STONE_SLAB).grist(GristTypes.BUILD, 1).buildFor(recipeSaver, Minestuck.MOD_ID);
		GristCostRecipeBuilder.of(Items.STONE_BRICKS).grist(GristTypes.BUILD, 2).buildFor(recipeSaver, Minestuck.MOD_ID);
		GristCostRecipeBuilder.of(Items.STONE_BRICK_STAIRS).grist(GristTypes.BUILD, 3).buildFor(recipeSaver, Minestuck.MOD_ID);
		GristCostRecipeBuilder.of(Items.SANDSTONE_STAIRS).grist(GristTypes.BUILD, 6).buildFor(recipeSaver, Minestuck.MOD_ID);
		GristCostRecipeBuilder.of(Items.COBWEB).grist(GristTypes.BUILD, 18).buildFor(recipeSaver, Minestuck.MOD_ID);
		GristCostRecipeBuilder.of(Items.TORCH).grist(GristTypes.BUILD, 2).buildFor(recipeSaver, Minestuck.MOD_ID);
		GristCostRecipeBuilder.of(Items.PRISMARINE).grist(GristTypes.COBALT, 7).grist(GristTypes.BUILD, 12).buildFor(recipeSaver, Minestuck.MOD_ID);
		GristCostRecipeBuilder.of(Items.PRISMARINE_BRICKS).grist(GristTypes.COBALT, 12).grist(GristTypes.BUILD, 18).buildFor(recipeSaver, Minestuck.MOD_ID);
		GristCostRecipeBuilder.of(Items.DARK_PRISMARINE).grist(GristTypes.COBALT, 10).grist(GristTypes.TAR, 2).grist(GristTypes.BUILD, 18).buildFor(recipeSaver, Minestuck.MOD_ID);
		GristCostRecipeBuilder.of(Items.SEA_LANTERN).grist(GristTypes.COBALT, 32).grist(GristTypes.DIAMOND, 6).grist(GristTypes.AMETHYST, 12).buildFor(recipeSaver, Minestuck.MOD_ID);
		GristCostRecipeBuilder.of(Items.SANDSTONE).grist(GristTypes.BUILD, 4).buildFor(recipeSaver, Minestuck.MOD_ID);
		GristCostRecipeBuilder.of(Items.CHORUS_FLOWER).grist(GristTypes.BUILD, 26).grist(GristTypes.AMETHYST, 23).grist(GristTypes.SHALE, 10).buildFor(recipeSaver, Minestuck.MOD_ID);
		GristCostRecipeBuilder.of(Items.PURPUR_PILLAR).grist(GristTypes.AMETHYST, 2).grist(GristTypes.SHALE, 4).buildFor(recipeSaver, Minestuck.MOD_ID);
		GristCostRecipeBuilder.of(Items.DISPENSER).grist(GristTypes.BUILD, 17).grist(GristTypes.GARNET, 4).grist(GristTypes.CHALK, 6).buildFor(recipeSaver, Minestuck.MOD_ID);
		
		GristCostRecipeBuilder.of(Items.WHITE_WOOL).grist(GristTypes.CHALK, 6).buildFor(recipeSaver, Minestuck.MOD_ID);
		GristCostRecipeBuilder.of(Items.ORANGE_WOOL).grist(GristTypes.CHALK, 6).grist(GristTypes.AMBER, 1).grist(GristTypes.GARNET, 1).buildFor(recipeSaver, Minestuck.MOD_ID);
		GristCostRecipeBuilder.of(Items.MAGENTA_WOOL).grist(GristTypes.CHALK, 6).grist(GristTypes.GARNET, 2).grist(GristTypes.AMETHYST, 1).buildFor(recipeSaver, Minestuck.MOD_ID);
		GristCostRecipeBuilder.of(Items.LIGHT_BLUE_WOOL).grist(GristTypes.CHALK, 8).grist(GristTypes.AMETHYST, 1).buildFor(recipeSaver, Minestuck.MOD_ID);
		GristCostRecipeBuilder.of(Items.YELLOW_WOOL).grist(GristTypes.CHALK, 6).grist(GristTypes.AMBER, 2).buildFor(recipeSaver, Minestuck.MOD_ID);
		GristCostRecipeBuilder.of(Items.LIME_WOOL).grist(GristTypes.CHALK, 6).grist(GristTypes.AMBER, 2).buildFor(recipeSaver, Minestuck.MOD_ID);
		GristCostRecipeBuilder.of(Items.PINK_WOOL).grist(GristTypes.CHALK, 6).grist(GristTypes.AMETHYST, 1).buildFor(recipeSaver, Minestuck.MOD_ID);
		GristCostRecipeBuilder.of(Items.GRAY_WOOL).grist(GristTypes.CHALK, 6).grist(GristTypes.TAR, 1).buildFor(recipeSaver, Minestuck.MOD_ID);
		GristCostRecipeBuilder.of(Items.LIGHT_GRAY_WOOL).grist(GristTypes.CHALK, 6).grist(GristTypes.TAR, 1).buildFor(recipeSaver, Minestuck.MOD_ID);
		GristCostRecipeBuilder.of(Items.CYAN_WOOL).grist(GristTypes.CHALK, 6).grist(GristTypes.AMBER, 1).grist(GristTypes.AMETHYST, 1).buildFor(recipeSaver, Minestuck.MOD_ID);
		GristCostRecipeBuilder.of(Items.PURPLE_WOOL).grist(GristTypes.CHALK, 6).grist(GristTypes.AMETHYST, 1).grist(GristTypes.GARNET, 1).buildFor(recipeSaver, Minestuck.MOD_ID);
		GristCostRecipeBuilder.of(Items.BLUE_WOOL).grist(GristTypes.CHALK, 6).grist(GristTypes.AMETHYST, 2).buildFor(recipeSaver, Minestuck.MOD_ID);
		GristCostRecipeBuilder.of(Items.BROWN_WOOL).grist(GristTypes.CHALK, 6).grist(GristTypes.IODINE, 2).buildFor(recipeSaver, Minestuck.MOD_ID);
		GristCostRecipeBuilder.of(Items.GREEN_WOOL).grist(GristTypes.CHALK, 6).grist(GristTypes.AMBER, 2).buildFor(recipeSaver, Minestuck.MOD_ID);
		GristCostRecipeBuilder.of(Items.RED_WOOL).grist(GristTypes.CHALK, 6).grist(GristTypes.GARNET, 2).buildFor(recipeSaver, Minestuck.MOD_ID);
		GristCostRecipeBuilder.of(Items.BLACK_WOOL).grist(GristTypes.CHALK, 6).grist(GristTypes.TAR, 2).buildFor(recipeSaver, Minestuck.MOD_ID);
		
		//Items
		GristCostRecipeBuilder.of(Items.BLAZE_ROD).grist(GristTypes.TAR, 20).grist(GristTypes.URANIUM, 2).buildFor(recipeSaver, Minestuck.MOD_ID);
		GristCostRecipeBuilder.of(Items.BONE).grist(GristTypes.CHALK, 6).buildFor(recipeSaver, Minestuck.MOD_ID);
		GristCostRecipeBuilder.of(Items.BRICK).grist(GristTypes.SHALE, 3).grist(GristTypes.TAR, 1).buildFor(recipeSaver, Minestuck.MOD_ID);
		GristCostRecipeBuilder.of(Items.CHAINMAIL_BOOTS).grist(GristTypes.RUST, 16).grist(GristTypes.MERCURY, 8).buildFor(recipeSaver, Minestuck.MOD_ID);
		GristCostRecipeBuilder.of(Items.CHAINMAIL_CHESTPLATE).grist(GristTypes.RUST, 32).grist(GristTypes.MERCURY, 16).buildFor(recipeSaver, Minestuck.MOD_ID);
		GristCostRecipeBuilder.of(Items.CHAINMAIL_HELMET).grist(GristTypes.RUST, 20).grist(GristTypes.MERCURY, 10).buildFor(recipeSaver, Minestuck.MOD_ID);
		GristCostRecipeBuilder.of(Items.CHAINMAIL_LEGGINGS).grist(GristTypes.RUST, 28).grist(GristTypes.MERCURY, 14).buildFor(recipeSaver, Minestuck.MOD_ID);
		GristCostRecipeBuilder.of(Items.CLAY_BALL).grist(GristTypes.SHALE, 3).buildFor(recipeSaver, Minestuck.MOD_ID);
		GristCostRecipeBuilder.of(Items.DIAMOND_HORSE_ARMOR).grist(GristTypes.DIAMOND, 80).buildFor(recipeSaver, Minestuck.MOD_ID);
		
		WildcardGristCostBuilder.of(MSItems.CAPTCHA_CARD).cost(1).build(recipeSaver);
	}
	
	@Override
	public String getName()
	{
		return "Minestuck Grist Costs";
	}
}
