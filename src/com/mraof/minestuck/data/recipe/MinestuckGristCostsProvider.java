package com.mraof.minestuck.data.recipe;

import com.mraof.minestuck.Minestuck;
import com.mraof.minestuck.item.MSItems;
import com.mraof.minestuck.item.crafting.alchemy.GristType;
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
		GristCostRecipeBuilder.of(Tags.Items.STONE).grist(GristType.BUILD, 2).buildFor(recipeSaver, Minestuck.MOD_ID);
		GristCostRecipeBuilder.of(Tags.Items.COBBLESTONE).grist(GristType.BUILD, 2).buildFor(recipeSaver, Minestuck.MOD_ID);
		GristCostRecipeBuilder.of(Items.GRAVEL).grist(GristType.BUILD, 3).buildFor(recipeSaver, Minestuck.MOD_ID);
		GristCostRecipeBuilder.of(ItemTags.SAND).grist(GristType.BUILD, 2).buildFor(recipeSaver, Minestuck.MOD_ID);
		GristCostRecipeBuilder.of(ExtraForgeTags.Items.TERRACOTTA).grist(GristType.SHALE, 12).grist(GristType.MARBLE, 4).buildFor(recipeSaver, Minestuck.MOD_ID);
		GristCostRecipeBuilder.of(Items.DIRT).grist(GristType.BUILD, 2).buildFor(recipeSaver, Minestuck.MOD_ID);
		GristCostRecipeBuilder.of(Items.GRASS_BLOCK).grist(GristType.BUILD, 2).buildFor(recipeSaver, Minestuck.MOD_ID);
		GristCostRecipeBuilder.of(Items.PODZOL).grist(GristType.BUILD, 2).buildFor(recipeSaver, Minestuck.MOD_ID);
		GristCostRecipeBuilder.of(Items.MYCELIUM).grist(GristType.BUILD, 2).buildFor(recipeSaver, Minestuck.MOD_ID);
		
		GristCostRecipeBuilder.of(ItemTags.LOGS).grist(GristType.BUILD, 8).buildFor(recipeSaver, Minestuck.MOD_ID);
		GristCostRecipeBuilder.of(ItemTags.LEAVES).grist(GristType.BUILD, 1).buildFor(recipeSaver, Minestuck.MOD_ID);
		GristCostRecipeBuilder.of(ItemTags.SAPLINGS).grist(GristType.BUILD, 16).buildFor(recipeSaver, Minestuck.MOD_ID);
		GristCostRecipeBuilder.of(ItemTags.PLANKS).grist(GristType.BUILD, 2).buildFor(recipeSaver, Minestuck.MOD_ID);
		GristCostRecipeBuilder.of(Items.DEAD_BUSH).grist(GristType.AMBER, 2).grist(GristType.SULFUR, 1).buildFor(recipeSaver, Minestuck.MOD_ID);
		GristCostRecipeBuilder.of(Items.DRAGON_EGG).grist(GristType.URANIUM, 800).grist(GristType.TAR, 800).grist(GristType.ZILLIUM, 10).buildFor(recipeSaver, Minestuck.MOD_ID);
		GristCostRecipeBuilder.of(Items.END_STONE).grist(GristType.CAULK, 3).grist(GristType.BUILD, 4).buildFor(recipeSaver, Minestuck.MOD_ID);
		GristCostRecipeBuilder.of(Items.GLASS).grist(GristType.BUILD, 1).buildFor(recipeSaver, Minestuck.MOD_ID);
		GristCostRecipeBuilder.of(Items.ICE).grist(GristType.COBALT, 2).buildFor(recipeSaver, Minestuck.MOD_ID);
		GristCostRecipeBuilder.of(Items.MELON).grist(GristType.AMBER, 8).grist(GristType.CAULK, 8).grist(GristType.BUILD, 2).buildFor(recipeSaver, Minestuck.MOD_ID);
		GristCostRecipeBuilder.of(Items.MYCELIUM).grist(GristType.IODINE, 2).grist(GristType.RUBY, 2).grist(GristType.BUILD, 2).buildFor(recipeSaver, Minestuck.MOD_ID);
		GristCostRecipeBuilder.of(Items.NETHERRACK).grist(GristType.BUILD, 2).grist(GristType.TAR, 1).buildFor(recipeSaver, Minestuck.MOD_ID);
		GristCostRecipeBuilder.of(Items.OBSIDIAN).grist(GristType.BUILD, 6).grist(GristType.COBALT, 8).grist(GristType.TAR, 16).buildFor(recipeSaver, Minestuck.MOD_ID);
		GristCostRecipeBuilder.of(Items.PUMPKIN).grist(GristType.AMBER, 12).grist(GristType.CAULK, 6).buildFor(recipeSaver, Minestuck.MOD_ID);
		GristCostRecipeBuilder.of(Items.SNOW_BLOCK).grist(GristType.BUILD, 5).grist(GristType.COBALT, 3).buildFor(recipeSaver, Minestuck.MOD_ID);
		GristCostRecipeBuilder.of(Items.SNOW).grist(GristType.COBALT, 2).buildFor(recipeSaver, Minestuck.MOD_ID);
		GristCostRecipeBuilder.of(Items.SOUL_SAND).grist(GristType.BUILD, 2).grist(GristType.SULFUR, 5).grist(GristType.CAULK, 3).buildFor(recipeSaver, Minestuck.MOD_ID);
		GristCostRecipeBuilder.of(Items.SPONGE).grist(GristType.AMBER, 20).grist(GristType.SULFUR, 30).buildFor(recipeSaver, Minestuck.MOD_ID);
		GristCostRecipeBuilder.of(Items.WET_SPONGE).grist(GristType.AMBER, 20).grist(GristType.SULFUR, 30).grist(GristType.COBALT, 10).buildFor(recipeSaver, Minestuck.MOD_ID);
		GristCostRecipeBuilder.of(Items.STONE_SLAB).grist(GristType.BUILD, 1).buildFor(recipeSaver, Minestuck.MOD_ID);
		GristCostRecipeBuilder.of(Items.STONE_BRICKS).grist(GristType.BUILD, 2).buildFor(recipeSaver, Minestuck.MOD_ID);
		GristCostRecipeBuilder.of(Items.STONE_BRICK_STAIRS).grist(GristType.BUILD, 3).buildFor(recipeSaver, Minestuck.MOD_ID);
		GristCostRecipeBuilder.of(Items.SANDSTONE_STAIRS).grist(GristType.BUILD, 6).buildFor(recipeSaver, Minestuck.MOD_ID);
		GristCostRecipeBuilder.of(Items.COBWEB).grist(GristType.BUILD, 18).buildFor(recipeSaver, Minestuck.MOD_ID);
		GristCostRecipeBuilder.of(Items.TORCH).grist(GristType.BUILD, 2).buildFor(recipeSaver, Minestuck.MOD_ID);
		GristCostRecipeBuilder.of(Items.PRISMARINE).grist(GristType.COBALT, 7).grist(GristType.BUILD, 12).buildFor(recipeSaver, Minestuck.MOD_ID);
		GristCostRecipeBuilder.of(Items.PRISMARINE_BRICKS).grist(GristType.COBALT, 12).grist(GristType.BUILD, 18).buildFor(recipeSaver, Minestuck.MOD_ID);
		GristCostRecipeBuilder.of(Items.DARK_PRISMARINE).grist(GristType.COBALT, 10).grist(GristType.TAR, 2).grist(GristType.BUILD, 18).buildFor(recipeSaver, Minestuck.MOD_ID);
		GristCostRecipeBuilder.of(Items.SEA_LANTERN).grist(GristType.COBALT, 32).grist(GristType.DIAMOND, 6).grist(GristType.AMETHYST, 12).buildFor(recipeSaver, Minestuck.MOD_ID);
		GristCostRecipeBuilder.of(Items.SANDSTONE).grist(GristType.BUILD, 4).buildFor(recipeSaver, Minestuck.MOD_ID);
		GristCostRecipeBuilder.of(Items.CHORUS_FLOWER).grist(GristType.BUILD, 26).grist(GristType.AMETHYST, 23).grist(GristType.SHALE, 10).buildFor(recipeSaver, Minestuck.MOD_ID);
		GristCostRecipeBuilder.of(Items.PURPUR_PILLAR).grist(GristType.AMETHYST, 2).grist(GristType.SHALE, 4).buildFor(recipeSaver, Minestuck.MOD_ID);;
		GristCostRecipeBuilder.of(Items.DISPENSER).grist(GristType.BUILD, 17).grist(GristType.GARNET, 4).grist(GristType.CHALK, 6).buildFor(recipeSaver, Minestuck.MOD_ID);
		
		GristCostRecipeBuilder.of(Items.WHITE_WOOL).grist(GristType.CHALK, 6).buildFor(recipeSaver, Minestuck.MOD_ID);
		GristCostRecipeBuilder.of(Items.ORANGE_WOOL).grist(GristType.CHALK, 6).grist(GristType.AMBER, 1).grist(GristType.GARNET, 1).buildFor(recipeSaver, Minestuck.MOD_ID);
		GristCostRecipeBuilder.of(Items.MAGENTA_WOOL).grist(GristType.CHALK, 6).grist(GristType.GARNET, 2).grist(GristType.AMETHYST, 1).buildFor(recipeSaver, Minestuck.MOD_ID);
		GristCostRecipeBuilder.of(Items.LIGHT_BLUE_WOOL).grist(GristType.CHALK, 8).grist(GristType.AMETHYST, 1).buildFor(recipeSaver, Minestuck.MOD_ID);
		GristCostRecipeBuilder.of(Items.YELLOW_WOOL).grist(GristType.CHALK, 6).grist(GristType.AMBER, 2).buildFor(recipeSaver, Minestuck.MOD_ID);
		GristCostRecipeBuilder.of(Items.LIME_WOOL).grist(GristType.CHALK, 6).grist(GristType.AMBER, 2).buildFor(recipeSaver, Minestuck.MOD_ID);
		GristCostRecipeBuilder.of(Items.PINK_WOOL).grist(GristType.CHALK, 6).grist(GristType.AMETHYST, 1).buildFor(recipeSaver, Minestuck.MOD_ID);
		GristCostRecipeBuilder.of(Items.GRAY_WOOL).grist(GristType.CHALK, 6).grist(GristType.TAR, 1).buildFor(recipeSaver, Minestuck.MOD_ID);
		GristCostRecipeBuilder.of(Items.LIGHT_GRAY_WOOL).grist(GristType.CHALK, 6).grist(GristType.TAR, 1).buildFor(recipeSaver, Minestuck.MOD_ID);
		GristCostRecipeBuilder.of(Items.CYAN_WOOL).grist(GristType.CHALK, 6).grist(GristType.AMBER, 1).grist(GristType.AMETHYST, 1).buildFor(recipeSaver, Minestuck.MOD_ID);
		GristCostRecipeBuilder.of(Items.PURPLE_WOOL).grist(GristType.CHALK, 6).grist(GristType.AMETHYST, 1).grist(GristType.GARNET, 1).buildFor(recipeSaver, Minestuck.MOD_ID);
		GristCostRecipeBuilder.of(Items.BLUE_WOOL).grist(GristType.CHALK, 6).grist(GristType.AMETHYST, 2).buildFor(recipeSaver, Minestuck.MOD_ID);
		GristCostRecipeBuilder.of(Items.BROWN_WOOL).grist(GristType.CHALK, 6).grist(GristType.IODINE, 2).buildFor(recipeSaver, Minestuck.MOD_ID);
		GristCostRecipeBuilder.of(Items.GREEN_WOOL).grist(GristType.CHALK, 6).grist(GristType.AMBER, 2).buildFor(recipeSaver, Minestuck.MOD_ID);
		GristCostRecipeBuilder.of(Items.RED_WOOL).grist(GristType.CHALK, 6).grist(GristType.GARNET, 2).buildFor(recipeSaver, Minestuck.MOD_ID);
		GristCostRecipeBuilder.of(Items.BLACK_WOOL).grist(GristType.CHALK, 6).grist(GristType.TAR, 2).buildFor(recipeSaver, Minestuck.MOD_ID);
		
		//Items
		GristCostRecipeBuilder.of(Items.BLAZE_ROD).grist(GristType.TAR, 20).grist(GristType.URANIUM, 2).buildFor(recipeSaver, Minestuck.MOD_ID);
		GristCostRecipeBuilder.of(Items.BONE).grist(GristType.CHALK, 6).buildFor(recipeSaver, Minestuck.MOD_ID);
		GristCostRecipeBuilder.of(Items.BRICK).grist(GristType.SHALE, 3).grist(GristType.TAR, 1).buildFor(recipeSaver, Minestuck.MOD_ID);
		GristCostRecipeBuilder.of(Items.CHAINMAIL_BOOTS).grist(GristType.RUST, 16).grist(GristType.MERCURY, 8).buildFor(recipeSaver, Minestuck.MOD_ID);
		GristCostRecipeBuilder.of(Items.CHAINMAIL_CHESTPLATE).grist(GristType.RUST, 32).grist(GristType.MERCURY, 16).buildFor(recipeSaver, Minestuck.MOD_ID);
		GristCostRecipeBuilder.of(Items.CHAINMAIL_HELMET).grist(GristType.RUST, 20).grist(GristType.MERCURY, 10).buildFor(recipeSaver, Minestuck.MOD_ID);
		GristCostRecipeBuilder.of(Items.CHAINMAIL_LEGGINGS).grist(GristType.RUST, 28).grist(GristType.MERCURY, 14).buildFor(recipeSaver, Minestuck.MOD_ID);
		GristCostRecipeBuilder.of(Items.CLAY_BALL).grist(GristType.SHALE, 3).buildFor(recipeSaver, Minestuck.MOD_ID);
		GristCostRecipeBuilder.of(Items.DIAMOND_HORSE_ARMOR).grist(GristType.DIAMOND, 80).buildFor(recipeSaver, Minestuck.MOD_ID);
		
		GristCostRecipeBuilder.of(MSItems.CAPTCHA_CARD).wildcard(1).build(recipeSaver);
	}
	
	@Override
	public String getName()
	{
		return "Minestuck Grist Costs";
	}
}
