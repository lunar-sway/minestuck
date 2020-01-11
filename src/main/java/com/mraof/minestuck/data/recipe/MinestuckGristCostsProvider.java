package com.mraof.minestuck.data.recipe;

import com.mraof.minestuck.Minestuck;
import com.mraof.minestuck.item.MSItems;
import com.mraof.minestuck.util.ExtraForgeTags;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.IFinishedRecipe;
import net.minecraft.data.RecipeProvider;
import net.minecraft.item.Items;
import net.minecraft.tags.ItemTags;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.Tags;

import java.util.function.Consumer;

import static com.mraof.minestuck.item.crafting.alchemy.GristTypes.*;

public class MinestuckGristCostsProvider extends RecipeProvider
{
	public MinestuckGristCostsProvider(DataGenerator generator)
	{
		super(generator);
	}
	
	@Override
	protected void registerRecipes(Consumer<IFinishedRecipe> recipeSaver)
	{
		GeneratedGristCostBuilder.create().build(recipeSaver, new ResourceLocation(Minestuck.MOD_ID, "generated"));
		
		//Stone
		GristCostRecipeBuilder.of(Tags.Items.STONE).grist(BUILD, 2).buildFor(recipeSaver, Minestuck.MOD_ID);
		GristCostRecipeBuilder.of(Tags.Items.COBBLESTONE).grist(BUILD, 2).buildFor(recipeSaver, Minestuck.MOD_ID);
		GristCostRecipeBuilder.of(Items.STONE_SLAB).grist(BUILD, 1).buildFor(recipeSaver, Minestuck.MOD_ID);
		GristCostRecipeBuilder.of(Items.STONE_BRICKS).grist(BUILD, 2).buildFor(recipeSaver, Minestuck.MOD_ID);
		GristCostRecipeBuilder.of(Items.CRACKED_STONE_BRICKS).grist(BUILD, 2).buildFor(recipeSaver, Minestuck.MOD_ID);
		GristCostRecipeBuilder.of(Items.STONE_BRICK_STAIRS).grist(BUILD, 3).buildFor(recipeSaver, Minestuck.MOD_ID);
		GristCostRecipeBuilder.of(Items.GRAVEL).grist(BUILD, 3).buildFor(recipeSaver, Minestuck.MOD_ID);
		GristCostRecipeBuilder.of(ItemTags.SAND).grist(BUILD, 2).buildFor(recipeSaver, Minestuck.MOD_ID);
		GristCostRecipeBuilder.of(Items.GLASS).grist(BUILD, 1).buildFor(recipeSaver, Minestuck.MOD_ID);
		GristCostRecipeBuilder.of(Items.SANDSTONE).grist(BUILD, 4).buildFor(recipeSaver, Minestuck.MOD_ID);
		GristCostRecipeBuilder.of(Items.RED_SANDSTONE).grist(BUILD, 4).buildFor(recipeSaver, Minestuck.MOD_ID);
		GristCostRecipeBuilder.of(Items.SANDSTONE_STAIRS).grist(BUILD, 6).buildFor(recipeSaver, Minestuck.MOD_ID);
		GristCostRecipeBuilder.of(ExtraForgeTags.Items.TERRACOTTA).grist(SHALE, 12).grist(MARBLE, 4).buildFor(recipeSaver, Minestuck.MOD_ID);
		GristCostRecipeBuilder.of(Items.CLAY_BALL).grist(SHALE, 3).buildFor(recipeSaver, Minestuck.MOD_ID);
		GristCostRecipeBuilder.of(Items.BRICK).grist(SHALE, 3).grist(TAR, 1).buildFor(recipeSaver, Minestuck.MOD_ID);
		GristCostRecipeBuilder.of(Items.FLINT).grist(BUILD, 4).buildFor(recipeSaver, Minestuck.MOD_ID);
		
		//Ore
		GristCostRecipeBuilder.of(Tags.Items.ORES_COAL).grist(BUILD, 4).grist(TAR, 8).buildFor(recipeSaver, Minestuck.MOD_ID);
		GristCostRecipeBuilder.of(Tags.Items.ORES_IRON).grist(BUILD, 4).grist(RUST, 9).buildFor(recipeSaver, Minestuck.MOD_ID);
		GristCostRecipeBuilder.of(Tags.Items.ORES_GOLD).grist(BUILD, 4).grist(GOLD, 9).buildFor(recipeSaver, Minestuck.MOD_ID);
		GristCostRecipeBuilder.of(Tags.Items.ORES_REDSTONE).grist(BUILD, 4).grist(GARNET, 16).buildFor(recipeSaver, Minestuck.MOD_ID);
		GristCostRecipeBuilder.of(Tags.Items.ORES_LAPIS).grist(BUILD, 4).grist(AMETHYST, 16).buildFor(recipeSaver, Minestuck.MOD_ID);
		GristCostRecipeBuilder.of(Tags.Items.ORES_DIAMOND).grist(BUILD, 4).grist(DIAMOND, 18).buildFor(recipeSaver, Minestuck.MOD_ID);
		GristCostRecipeBuilder.of(Tags.Items.ORES_EMERALD).grist(BUILD, 4).grist(RUBY, 9).grist(DIAMOND, 9).buildFor(recipeSaver, Minestuck.MOD_ID);
		GristCostRecipeBuilder.of(Tags.Items.ORES_QUARTZ).grist(BUILD, 4).grist(QUARTZ, 8).grist(MARBLE, 2).buildFor(recipeSaver, Minestuck.MOD_ID);
		GristCostRecipeBuilder.of(Items.COAL).grist(TAR, 8).buildFor(recipeSaver, Minestuck.MOD_ID);
		GristCostRecipeBuilder.of(Items.CHARCOAL).grist(TAR, 6).grist(AMBER, 2).buildFor(recipeSaver, Minestuck.MOD_ID);
		GristCostRecipeBuilder.of(Items.GOLD_INGOT).grist(GOLD, 9).buildFor(recipeSaver, Minestuck.MOD_ID);
		GristCostRecipeBuilder.of(Items.IRON_INGOT).grist(RUST, 9).buildFor(recipeSaver, Minestuck.MOD_ID);
		GristCostRecipeBuilder.of(Items.REDSTONE).grist(GARNET, 4).buildFor(recipeSaver, Minestuck.MOD_ID);
		GristCostRecipeBuilder.of(Items.QUARTZ).grist(QUARTZ, 4).grist(MARBLE, 1).buildFor(recipeSaver, Minestuck.MOD_ID);
		GristCostRecipeBuilder.of(Items.QUARTZ_BLOCK).grist(QUARTZ, 16).grist(MARBLE, 4).buildFor(recipeSaver, Minestuck.MOD_ID);
		GristCostRecipeBuilder.of(Items.EMERALD).grist(RUBY, 9).grist(DIAMOND, 9).buildFor(recipeSaver, Minestuck.MOD_ID);
		GristCostRecipeBuilder.of(Items.DIAMOND).grist(DIAMOND, 18).buildFor(recipeSaver, Minestuck.MOD_ID);
		
		//Dirt
		GristCostRecipeBuilder.of(Items.DIRT).grist(BUILD, 2).buildFor(recipeSaver, Minestuck.MOD_ID);
		GristCostRecipeBuilder.of(Items.GRASS_BLOCK).grist(BUILD, 2).buildFor(recipeSaver, Minestuck.MOD_ID);
		GristCostRecipeBuilder.of(Items.PODZOL).grist(BUILD, 2).buildFor(recipeSaver, Minestuck.MOD_ID);
		GristCostRecipeBuilder.of(Items.MYCELIUM).grist(IODINE, 2).grist(RUBY, 2).grist(BUILD, 2).buildFor(recipeSaver, Minestuck.MOD_ID);
		
		//Snow/ice
		GristCostRecipeBuilder.of(Items.SNOWBALL).grist(COBALT, 1).buildFor(recipeSaver, Minestuck.MOD_ID);
		GristCostRecipeBuilder.of(Items.SNOW_BLOCK).grist(BUILD, 5).grist(COBALT, 3).buildFor(recipeSaver, Minestuck.MOD_ID);
		GristCostRecipeBuilder.of(Items.SNOW).grist(COBALT, 2).buildFor(recipeSaver, Minestuck.MOD_ID);
		GristCostRecipeBuilder.of(Items.ICE).grist(COBALT, 2).buildFor(recipeSaver, Minestuck.MOD_ID);
		GristCostRecipeBuilder.of(Items.PACKED_ICE).grist(BUILD, 10).grist(COBALT, 6).buildFor(recipeSaver, Minestuck.MOD_ID);
		
		GristCostRecipeBuilder.of(ItemTags.LOGS).grist(BUILD, 8).buildFor(recipeSaver, Minestuck.MOD_ID);
		GristCostRecipeBuilder.of(ItemTags.LEAVES).grist(BUILD, 1).buildFor(recipeSaver, Minestuck.MOD_ID);
		GristCostRecipeBuilder.of(ItemTags.SAPLINGS).grist(BUILD, 16).buildFor(recipeSaver, Minestuck.MOD_ID);
		GristCostRecipeBuilder.of(ItemTags.PLANKS).grist(BUILD, 2).buildFor(recipeSaver, Minestuck.MOD_ID);
		GristCostRecipeBuilder.of(Items.DEAD_BUSH).grist(AMBER, 2).grist(SULFUR, 1).buildFor(recipeSaver, Minestuck.MOD_ID);
		GristCostRecipeBuilder.of(Items.MELON).grist(AMBER, 8).grist(CAULK, 8).grist(BUILD, 2).buildFor(recipeSaver, Minestuck.MOD_ID);
		GristCostRecipeBuilder.of(Items.NETHERRACK).grist(BUILD, 2).grist(TAR, 1).buildFor(recipeSaver, Minestuck.MOD_ID);
		GristCostRecipeBuilder.of(Items.OBSIDIAN).grist(BUILD, 6).grist(COBALT, 8).grist(TAR, 16).buildFor(recipeSaver, Minestuck.MOD_ID);
		GristCostRecipeBuilder.of(Items.PUMPKIN).grist(AMBER, 12).grist(CAULK, 6).buildFor(recipeSaver, Minestuck.MOD_ID);
		GristCostRecipeBuilder.of(Items.COBWEB).grist(BUILD, 18).buildFor(recipeSaver, Minestuck.MOD_ID);
		GristCostRecipeBuilder.of(Items.TORCH).grist(BUILD, 2).buildFor(recipeSaver, Minestuck.MOD_ID);
		GristCostRecipeBuilder.of(Items.DISPENSER).grist(BUILD, 17).grist(GARNET, 4).grist(CHALK, 6).buildFor(recipeSaver, Minestuck.MOD_ID);
		
		//Underwater
		GristCostRecipeBuilder.of(Items.SPONGE).grist(AMBER, 20).grist(SULFUR, 30).buildFor(recipeSaver, Minestuck.MOD_ID);
		GristCostRecipeBuilder.of(Items.WET_SPONGE).grist(AMBER, 20).grist(SULFUR, 30).grist(COBALT, 10).buildFor(recipeSaver, Minestuck.MOD_ID);
		GristCostRecipeBuilder.of(Items.PRISMARINE_CRYSTALS).grist(COBALT, 5).grist(DIAMOND, 2).grist(AMETHYST, 4).buildFor(recipeSaver, Minestuck.MOD_ID);
		GristCostRecipeBuilder.of(Items.PRISMARINE_SHARD).grist(COBALT, 3).grist(BUILD, 3).buildFor(recipeSaver, Minestuck.MOD_ID);
		GristCostRecipeBuilder.of(Items.PRISMARINE).grist(COBALT, 7).grist(BUILD, 12).buildFor(recipeSaver, Minestuck.MOD_ID);
		GristCostRecipeBuilder.of(Items.PRISMARINE_BRICKS).grist(COBALT, 12).grist(BUILD, 18).buildFor(recipeSaver, Minestuck.MOD_ID);
		GristCostRecipeBuilder.of(Items.DARK_PRISMARINE).grist(COBALT, 10).grist(TAR, 2).grist(BUILD, 18).buildFor(recipeSaver, Minestuck.MOD_ID);
		GristCostRecipeBuilder.of(Items.SEA_LANTERN).grist(COBALT, 32).grist(DIAMOND, 6).grist(AMETHYST, 12).buildFor(recipeSaver, Minestuck.MOD_ID);
		
		//End
		GristCostRecipeBuilder.of(Items.DRAGON_EGG).grist(URANIUM, 800).grist(TAR, 800).grist(ZILLIUM, 10).buildFor(recipeSaver, Minestuck.MOD_ID);
		GristCostRecipeBuilder.of(Items.END_STONE).grist(CAULK, 3).grist(BUILD, 4).buildFor(recipeSaver, Minestuck.MOD_ID);
		GristCostRecipeBuilder.of(Items.DRAGON_BREATH).grist(RUBY, 4).grist(SULFUR, 13).buildFor(recipeSaver, Minestuck.MOD_ID);
		GristCostRecipeBuilder.of(Items.ELYTRA).grist(DIAMOND, 51).grist(SULFUR, 38).grist(CAULK, 65).buildFor(recipeSaver, Minestuck.MOD_ID);
		GristCostRecipeBuilder.of(Items.CHORUS_FRUIT).grist(AMETHYST, 2).grist(SHALE, 4).buildFor(recipeSaver, Minestuck.MOD_ID);
		GristCostRecipeBuilder.of(Items.POPPED_CHORUS_FRUIT).grist(AMETHYST, 2).grist(SHALE, 4).buildFor(recipeSaver, Minestuck.MOD_ID);
		GristCostRecipeBuilder.of(Items.CHORUS_FLOWER).grist(BUILD, 26).grist(AMETHYST, 23).grist(SHALE, 10).buildFor(recipeSaver, Minestuck.MOD_ID);
		GristCostRecipeBuilder.of(Items.PURPUR_PILLAR).grist(AMETHYST, 2).grist(SHALE, 4).buildFor(recipeSaver, Minestuck.MOD_ID);
		
		//Wool
		GristCostRecipeBuilder.of(Items.WHITE_WOOL).grist(CHALK, 6).buildFor(recipeSaver, Minestuck.MOD_ID);
		GristCostRecipeBuilder.of(Items.ORANGE_WOOL).grist(CHALK, 6).grist(AMBER, 1).grist(GARNET, 1).buildFor(recipeSaver, Minestuck.MOD_ID);
		GristCostRecipeBuilder.of(Items.MAGENTA_WOOL).grist(CHALK, 6).grist(GARNET, 2).grist(AMETHYST, 1).buildFor(recipeSaver, Minestuck.MOD_ID);
		GristCostRecipeBuilder.of(Items.LIGHT_BLUE_WOOL).grist(CHALK, 8).grist(AMETHYST, 1).buildFor(recipeSaver, Minestuck.MOD_ID);
		GristCostRecipeBuilder.of(Items.YELLOW_WOOL).grist(CHALK, 6).grist(AMBER, 2).buildFor(recipeSaver, Minestuck.MOD_ID);
		GristCostRecipeBuilder.of(Items.LIME_WOOL).grist(CHALK, 6).grist(AMBER, 2).buildFor(recipeSaver, Minestuck.MOD_ID);
		GristCostRecipeBuilder.of(Items.PINK_WOOL).grist(CHALK, 6).grist(AMETHYST, 1).buildFor(recipeSaver, Minestuck.MOD_ID);
		GristCostRecipeBuilder.of(Items.GRAY_WOOL).grist(CHALK, 6).grist(TAR, 1).buildFor(recipeSaver, Minestuck.MOD_ID);
		GristCostRecipeBuilder.of(Items.LIGHT_GRAY_WOOL).grist(CHALK, 6).grist(TAR, 1).buildFor(recipeSaver, Minestuck.MOD_ID);
		GristCostRecipeBuilder.of(Items.CYAN_WOOL).grist(CHALK, 6).grist(AMBER, 1).grist(AMETHYST, 1).buildFor(recipeSaver, Minestuck.MOD_ID);
		GristCostRecipeBuilder.of(Items.PURPLE_WOOL).grist(CHALK, 6).grist(AMETHYST, 1).grist(GARNET, 1).buildFor(recipeSaver, Minestuck.MOD_ID);
		GristCostRecipeBuilder.of(Items.BLUE_WOOL).grist(CHALK, 6).grist(AMETHYST, 2).buildFor(recipeSaver, Minestuck.MOD_ID);
		GristCostRecipeBuilder.of(Items.BROWN_WOOL).grist(CHALK, 6).grist(IODINE, 2).buildFor(recipeSaver, Minestuck.MOD_ID);
		GristCostRecipeBuilder.of(Items.GREEN_WOOL).grist(CHALK, 6).grist(AMBER, 2).buildFor(recipeSaver, Minestuck.MOD_ID);
		GristCostRecipeBuilder.of(Items.RED_WOOL).grist(CHALK, 6).grist(GARNET, 2).buildFor(recipeSaver, Minestuck.MOD_ID);
		GristCostRecipeBuilder.of(Items.BLACK_WOOL).grist(CHALK, 6).grist(TAR, 2).buildFor(recipeSaver, Minestuck.MOD_ID);
		
		//Items
		GristCostRecipeBuilder.of(Items.DANDELION).grist(AMBER, 4).grist(IODINE, 1).buildFor(recipeSaver, Minestuck.MOD_ID);
		GristCostRecipeBuilder.of(Items.POPPY).grist(GARNET, 4).grist(IODINE, 1).buildFor(recipeSaver, Minestuck.MOD_ID);
		GristCostRecipeBuilder.of(Items.BLUE_ORCHID).grist(AMETHYST, 2).grist(CHALK, 2).grist(IODINE, 1).buildFor(recipeSaver, Minestuck.MOD_ID);
		GristCostRecipeBuilder.of(Items.ALLIUM).grist(AMETHYST, 1).grist(GARNET, 3).grist(IODINE, 1).buildFor(recipeSaver, Minestuck.MOD_ID);
		GristCostRecipeBuilder.of(Items.AZURE_BLUET).grist(TAR, 1).grist(CHALK, 3).grist(IODINE, 1).buildFor(recipeSaver, Minestuck.MOD_ID);
		GristCostRecipeBuilder.of(Items.RED_TULIP).grist(TAR, 1).grist(CHALK, 3).grist(IODINE, 1).buildFor(recipeSaver, Minestuck.MOD_ID);
		GristCostRecipeBuilder.of(Items.ORANGE_TULIP).grist(GARNET, 2).grist(AMBER, 2).grist(IODINE, 1).buildFor(recipeSaver, Minestuck.MOD_ID);
		GristCostRecipeBuilder.of(Items.WHITE_TULIP).grist(AMETHYST, 2).grist(CHALK, 2).grist(IODINE, 1).buildFor(recipeSaver, Minestuck.MOD_ID);
		GristCostRecipeBuilder.of(Items.PINK_TULIP).grist(GARNET, 4).grist(IODINE, 1).buildFor(recipeSaver, Minestuck.MOD_ID);
		GristCostRecipeBuilder.of(Items.OXEYE_DAISY).grist(TAR, 1).grist(CHALK, 3).grist(IODINE, 1).buildFor(recipeSaver, Minestuck.MOD_ID);
		GristCostRecipeBuilder.of(Items.BROWN_MUSHROOM).grist(IODINE, 5).buildFor(recipeSaver, Minestuck.MOD_ID);
		GristCostRecipeBuilder.of(Items.RED_MUSHROOM).grist(IODINE, 3).grist(RUBY, 2).buildFor(recipeSaver, Minestuck.MOD_ID);
		GristCostRecipeBuilder.of(Items.CACTUS).grist(AMBER, 4).grist(IODINE, 1).buildFor(recipeSaver, Minestuck.MOD_ID);
		GristCostRecipeBuilder.of(Items.GRASS).grist(BUILD, 1).buildFor(recipeSaver, Minestuck.MOD_ID);
		GristCostRecipeBuilder.of(Items.VINE).grist(BUILD, 2).grist(AMBER, 1).buildFor(recipeSaver, Minestuck.MOD_ID);
		GristCostRecipeBuilder.of(Items.LILY_PAD).grist(AMBER, 4).grist(IODINE, 1).buildFor(recipeSaver, Minestuck.MOD_ID);
		GristCostRecipeBuilder.of(Items.SUNFLOWER).grist(AMBER, 7).grist(IODINE, 2).buildFor(recipeSaver, Minestuck.MOD_ID);
		GristCostRecipeBuilder.of(Items.LILAC).grist(AMETHYST, 2).grist(GARNET, 5).grist(IODINE, 2).buildFor(recipeSaver, Minestuck.MOD_ID);
		GristCostRecipeBuilder.of(Items.TALL_GRASS).grist(AMBER, 2).buildFor(recipeSaver, Minestuck.MOD_ID);
		GristCostRecipeBuilder.of(Items.LARGE_FERN).grist(AMBER, 2).buildFor(recipeSaver, Minestuck.MOD_ID);
		GristCostRecipeBuilder.of(Items.ROSE_BUSH).grist(GARNET, 7).grist(IODINE, 2).buildFor(recipeSaver, Minestuck.MOD_ID);
		GristCostRecipeBuilder.of(Items.PEONY).grist(GARNET, 4).grist(CHALK, 4).grist(IODINE, 2).buildFor(recipeSaver, Minestuck.MOD_ID);
		GristCostRecipeBuilder.of(Items.WHEAT).grist(IODINE, 2).buildFor(recipeSaver, Minestuck.MOD_ID);
		GristCostRecipeBuilder.of(Items.WHEAT_SEEDS).grist(AMBER, 1).grist(IODINE, 1).buildFor(recipeSaver, Minestuck.MOD_ID);
		GristCostRecipeBuilder.of(Items.SUGAR_CANE).grist(AMBER, 3).grist(IODINE, 3).buildFor(recipeSaver, Minestuck.MOD_ID);
		GristCostRecipeBuilder.of(Items.SUGAR).grist(AMBER, 3).grist(IODINE, 2).buildFor(recipeSaver, Minestuck.MOD_ID);
		GristCostRecipeBuilder.of(Items.BEETROOT).grist(RUST, 1).grist(CHALK, 2).buildFor(recipeSaver, Minestuck.MOD_ID);
		GristCostRecipeBuilder.of(Items.BEETROOT_SEEDS).grist(RUST, 2).grist(IODINE, 2).buildFor(recipeSaver, Minestuck.MOD_ID);
		GristCostRecipeBuilder.of(Items.APPLE).grist(AMBER, 2).grist(SHALE, 2).buildFor(recipeSaver, Minestuck.MOD_ID);
		GristCostRecipeBuilder.of(Items.CARROT).grist(AMBER, 3).grist(CHALK, 1).buildFor(recipeSaver, Minestuck.MOD_ID);
		GristCostRecipeBuilder.of(Items.POTATO).grist(AMBER, 4).buildFor(recipeSaver, Minestuck.MOD_ID);
		GristCostRecipeBuilder.of(Items.POISONOUS_POTATO).grist(AMBER, 4).grist(IODINE, 2).buildFor(recipeSaver, Minestuck.MOD_ID);
		GristCostRecipeBuilder.of(Items.BAKED_POTATO).grist(AMBER, 4).grist(TAR, 1).buildFor(recipeSaver, Minestuck.MOD_ID);
		GristCostRecipeBuilder.of(Items.MELON_SLICE).grist(AMBER, 1).grist(CAULK, 1).buildFor(recipeSaver, Minestuck.MOD_ID);
		GristCostRecipeBuilder.of(Items.ENCHANTED_GOLDEN_APPLE).grist(AMBER, 4).grist(GOLD, 150).grist(URANIUM, 10).buildFor(recipeSaver, Minestuck.MOD_ID);
		
		//Animal drop
		GristCostRecipeBuilder.of(Items.FEATHER).grist(CHALK, 4).buildFor(recipeSaver, Minestuck.MOD_ID);
		GristCostRecipeBuilder.of(Items.LEATHER).grist(IODINE, 3).grist(CHALK, 3).buildFor(recipeSaver, Minestuck.MOD_ID);
		GristCostRecipeBuilder.of(Items.RABBIT_HIDE).grist(IODINE, 1).grist(CHALK, 1).buildFor(recipeSaver, Minestuck.MOD_ID);
		GristCostRecipeBuilder.of(Items.RABBIT_FOOT).grist(IODINE, 10).grist(CHALK, 12).grist(RUST, 3).buildFor(recipeSaver, Minestuck.MOD_ID);
		GristCostRecipeBuilder.of(Items.EGG).grist(AMBER, 5).buildFor(recipeSaver, Minestuck.MOD_ID);
		GristCostRecipeBuilder.of(Items.BEEF).grist(IODINE, 12).buildFor(recipeSaver, Minestuck.MOD_ID);
		GristCostRecipeBuilder.of(Items.PORKCHOP).grist(IODINE, 10).buildFor(recipeSaver, Minestuck.MOD_ID);
		GristCostRecipeBuilder.of(Items.CHICKEN).grist(IODINE, 10).buildFor(recipeSaver, Minestuck.MOD_ID);
		GristCostRecipeBuilder.of(Items.COD).grist(CAULK, 4).grist(AMBER, 4).grist(COBALT, 2).buildFor(recipeSaver, Minestuck.MOD_ID);
		GristCostRecipeBuilder.of(Items.MUTTON).grist(IODINE, 10).buildFor(recipeSaver, Minestuck.MOD_ID);
		GristCostRecipeBuilder.of(Items.RABBIT).grist(IODINE, 8).buildFor(recipeSaver, Minestuck.MOD_ID);
		GristCostRecipeBuilder.of(Items.COOKED_BEEF).grist(IODINE, 12).grist(TAR, 1).buildFor(recipeSaver, Minestuck.MOD_ID);
		GristCostRecipeBuilder.of(Items.COOKED_PORKCHOP).grist(IODINE, 10).grist(TAR, 1).buildFor(recipeSaver, Minestuck.MOD_ID);
		GristCostRecipeBuilder.of(Items.COOKED_CHICKEN).grist(IODINE, 10).grist(TAR, 1).buildFor(recipeSaver, Minestuck.MOD_ID);
		GristCostRecipeBuilder.of(Items.COOKED_COD).grist(CAULK, 4).grist(AMBER, 4).grist(COBALT, 2).grist(TAR, 1).buildFor(recipeSaver, Minestuck.MOD_ID);
		GristCostRecipeBuilder.of(Items.COOKED_MUTTON).grist(IODINE, 10).grist(TAR, 1).buildFor(recipeSaver, Minestuck.MOD_ID);
		GristCostRecipeBuilder.of(Items.COOKED_RABBIT).grist(IODINE, 8).grist(TAR, 1).buildFor(recipeSaver, Minestuck.MOD_ID);
		
		//Monster drop
		GristCostRecipeBuilder.of(Items.ROTTEN_FLESH).grist(RUST, 1).grist(IODINE, 1).buildFor(recipeSaver, Minestuck.MOD_ID);
		GristCostRecipeBuilder.of(Items.BONE).grist(CHALK, 6).buildFor(recipeSaver, Minestuck.MOD_ID);
		GristCostRecipeBuilder.of(Items.STRING).grist(CHALK, 2).buildFor(recipeSaver, Minestuck.MOD_ID);
		GristCostRecipeBuilder.of(Items.SPIDER_EYE).grist(AMBER, 6).grist(IODINE, 2).buildFor(recipeSaver, Minestuck.MOD_ID);
		GristCostRecipeBuilder.of(Items.GUNPOWDER).grist(SULFUR, 3).grist(CHALK, 2).buildFor(recipeSaver, Minestuck.MOD_ID);
		GristCostRecipeBuilder.of(Items.SLIME_BALL).grist(CAULK, 8).buildFor(recipeSaver, Minestuck.MOD_ID);
		GristCostRecipeBuilder.of(Items.ENDER_PEARL).grist(URANIUM, 13).grist(DIAMOND, 5).grist(MERCURY, 8).buildFor(recipeSaver, Minestuck.MOD_ID);
		GristCostRecipeBuilder.of(Items.ZOMBIE_HEAD).grist(RUST, 5).grist(CHALK, 20).buildFor(recipeSaver, Minestuck.MOD_ID);
		GristCostRecipeBuilder.of(Items.SKELETON_SKULL).grist(CHALK, 28).buildFor(recipeSaver, Minestuck.MOD_ID);
		GristCostRecipeBuilder.of(Items.CREEPER_HEAD).grist(SULFUR, 10).grist(CHALK, 18).buildFor(recipeSaver, Minestuck.MOD_ID);
		GristCostRecipeBuilder.of(Items.WITHER_SKELETON_SKULL).grist(URANIUM, 35).grist(TAR, 48).grist(DIAMOND, 16).buildFor(recipeSaver, Minestuck.MOD_ID);
		GristCostRecipeBuilder.of(Items.DRAGON_HEAD).grist(URANIUM, 25).grist(TAR, 70).grist(ZILLIUM, 1).buildFor(recipeSaver, Minestuck.MOD_ID);
		GristCostRecipeBuilder.of(Items.PLAYER_HEAD).grist(ARTIFACT, 10).buildFor(recipeSaver, Minestuck.MOD_ID);
		
		//Nether
		GristCostRecipeBuilder.of(Items.SOUL_SAND).grist(BUILD, 2).grist(SULFUR, 5).grist(CAULK, 3).buildFor(recipeSaver, Minestuck.MOD_ID);
		GristCostRecipeBuilder.of(Items.BLAZE_ROD).grist(TAR, 20).grist(URANIUM, 2).buildFor(recipeSaver, Minestuck.MOD_ID);
		GristCostRecipeBuilder.of(Items.GHAST_TEAR).grist(COBALT, 10).grist(CHALK, 19).buildFor(recipeSaver, Minestuck.MOD_ID);
		GristCostRecipeBuilder.of(Items.GLOWSTONE_DUST).grist(TAR, 4).grist(CHALK, 6).buildFor(recipeSaver, Minestuck.MOD_ID);
		GristCostRecipeBuilder.of(Items.NETHER_WART).grist(IODINE, 3).grist(TAR, 5).buildFor(recipeSaver, Minestuck.MOD_ID);
		GristCostRecipeBuilder.of(Items.NETHER_BRICK).grist(BUILD, 1).grist(TAR, 2).buildFor(recipeSaver, Minestuck.MOD_ID);
		GristCostRecipeBuilder.of(Items.NETHER_STAR).grist(URANIUM, 344).grist(TAR, 135).grist(DIAMOND, 92).buildFor(recipeSaver, Minestuck.MOD_ID);
		
		GristCostRecipeBuilder.of(Items.CHAINMAIL_HELMET).grist(RUST, 20).grist(MERCURY, 10).buildFor(recipeSaver, Minestuck.MOD_ID);
		GristCostRecipeBuilder.of(Items.CHAINMAIL_CHESTPLATE).grist(RUST, 32).grist(MERCURY, 16).buildFor(recipeSaver, Minestuck.MOD_ID);
		GristCostRecipeBuilder.of(Items.CHAINMAIL_LEGGINGS).grist(RUST, 28).grist(MERCURY, 14).buildFor(recipeSaver, Minestuck.MOD_ID);
		GristCostRecipeBuilder.of(Items.CHAINMAIL_BOOTS).grist(RUST, 16).grist(MERCURY, 8).buildFor(recipeSaver, Minestuck.MOD_ID);
		GristCostRecipeBuilder.of(Items.IRON_HORSE_ARMOR).grist(RUST, 40).buildFor(recipeSaver, Minestuck.MOD_ID);
		GristCostRecipeBuilder.of(Items.GOLDEN_HORSE_ARMOR).grist(GOLD, 40).buildFor(recipeSaver, Minestuck.MOD_ID);
		GristCostRecipeBuilder.of(Items.DIAMOND_HORSE_ARMOR).grist(DIAMOND, 80).buildFor(recipeSaver, Minestuck.MOD_ID);
		
		GristCostRecipeBuilder.of(Items.INK_SAC).grist(TAR, 4).buildFor(recipeSaver, Minestuck.MOD_ID);
		GristCostRecipeBuilder.of(Items.COCOA_BEANS).grist(IODINE, 3).grist(AMBER, 1).buildFor(recipeSaver, Minestuck.MOD_ID);
		GristCostRecipeBuilder.of(Items.LAPIS_LAZULI).grist(AMETHYST, 4).buildFor(recipeSaver, Minestuck.MOD_ID);
		GristCostRecipeBuilder.of(Items.RED_DYE).grist(GARNET, 4).buildFor(recipeSaver, Minestuck.MOD_ID);
		GristCostRecipeBuilder.of(Items.YELLOW_DYE).grist(AMBER, 4).buildFor(recipeSaver, Minestuck.MOD_ID);
		GristCostRecipeBuilder.of(Items.GREEN_DYE).grist(AMBER, 3).grist(IODINE, 1).buildFor(recipeSaver, Minestuck.MOD_ID);
		GristCostRecipeBuilder.of(Items.LIME_DYE).grist(AMBER, 3).buildFor(recipeSaver, Minestuck.MOD_ID);
		GristCostRecipeBuilder.of(Items.LIGHT_BLUE_DYE).grist(AMETHYST, 2).grist(CHALK, 2).buildFor(recipeSaver, Minestuck.MOD_ID);
		GristCostRecipeBuilder.of(Items.MAGENTA_DYE).grist(AMETHYST, 1).grist(GARNET, 3).buildFor(recipeSaver, Minestuck.MOD_ID);
		GristCostRecipeBuilder.of(Items.ORANGE_DYE).grist(GARNET, 2).grist(AMBER, 2).buildFor(recipeSaver, Minestuck.MOD_ID);
		GristCostRecipeBuilder.of(Items.PURPLE_DYE).grist(AMETHYST, 2).grist(GARNET, 2).buildFor(recipeSaver, Minestuck.MOD_ID);
		GristCostRecipeBuilder.of(Items.CYAN_DYE).grist(AMETHYST, 2).grist(AMBER, 2).grist(IODINE, 1).buildFor(recipeSaver, Minestuck.MOD_ID);
		GristCostRecipeBuilder.of(Items.LIGHT_GRAY_DYE).grist(TAR, 1).grist(CHALK, 3).buildFor(recipeSaver, Minestuck.MOD_ID);
		GristCostRecipeBuilder.of(Items.GRAY_DYE).grist(TAR, 3).grist(CHALK, 1).buildFor(recipeSaver, Minestuck.MOD_ID);
		GristCostRecipeBuilder.of(Items.PINK_DYE).grist(GARNET, 2).grist(CHALK, 2).buildFor(recipeSaver, Minestuck.MOD_ID);
		
		GristCostRecipeBuilder.of(Items.WRITABLE_BOOK).grist(CHALK, 16).grist(IODINE, 2).buildFor(recipeSaver, Minestuck.MOD_ID);
		GristCostRecipeBuilder.of(Items.ENCHANTED_BOOK).grist(URANIUM, 8).grist(QUARTZ, 1).grist(DIAMOND, 4).grist(RUBY, 4).grist(CHALK, 16).grist(IODINE, 2).buildFor(recipeSaver, Minestuck.MOD_ID);
		GristCostRecipeBuilder.of(Items.EXPERIENCE_BOTTLE).grist(URANIUM, 16).grist(QUARTZ, 3).grist(DIAMOND, 4).grist(RUBY, 6).buildFor(recipeSaver, Minestuck.MOD_ID);
		GristCostRecipeBuilder.of(Items.FIREWORK_STAR).grist(SULFUR, 4).grist(CHALK, 2).buildFor(recipeSaver, Minestuck.MOD_ID);
		GristCostRecipeBuilder.of(Items.FIREWORK_ROCKET).grist(SULFUR, 4).grist(CHALK, 5).buildFor(recipeSaver, Minestuck.MOD_ID);
		GristCostRecipeBuilder.of(Items.MAP).grist(RUST, 32).grist(CHALK, 10).grist(GARNET, 2).buildFor(recipeSaver, Minestuck.MOD_ID);
		GristCostRecipeBuilder.of(Items.NAME_TAG).grist(BUILD, 4).grist(CAULK, 10).grist(AMBER, 12).grist(CHALK, 8).buildFor(recipeSaver, Minestuck.MOD_ID);
		GristCostRecipeBuilder.of(Items.SADDLE).grist(RUST, 16).grist(IODINE, 7).grist(CHALK, 14).buildFor(recipeSaver, Minestuck.MOD_ID);
		
		GristCostRecipeBuilder.of(Items.MUSIC_DISC_11).grist(BUILD, 10).grist(CAULK, 5).grist(TAR, 2).grist(MERCURY, 2).buildFor(recipeSaver, Minestuck.MOD_ID);
		GristCostRecipeBuilder.of(Items.MUSIC_DISC_13).grist(BUILD, 15).grist(CAULK, 8).grist(AMBER, 5).grist(CHALK, 5).buildFor(recipeSaver, Minestuck.MOD_ID);
		GristCostRecipeBuilder.of(Items.MUSIC_DISC_BLOCKS).grist(BUILD, 15).grist(CAULK, 8).grist(RUBY, 5).grist(RUST, 5).buildFor(recipeSaver, Minestuck.MOD_ID);
		GristCostRecipeBuilder.of(Items.MUSIC_DISC_CAT).grist(BUILD, 15).grist(CAULK, 8).grist(URANIUM, 5).grist(SHALE, 5).buildFor(recipeSaver, Minestuck.MOD_ID);
		GristCostRecipeBuilder.of(Items.MUSIC_DISC_CHIRP).grist(BUILD, 15).grist(CAULK, 8).grist(RUBY, 5).grist(GARNET, 5).buildFor(recipeSaver, Minestuck.MOD_ID);
		GristCostRecipeBuilder.of(Items.MUSIC_DISC_FAR).grist(BUILD, 15).grist(CAULK, 8).grist(URANIUM, 5).grist(SULFUR, 5).buildFor(recipeSaver, Minestuck.MOD_ID);
		GristCostRecipeBuilder.of(Items.MUSIC_DISC_MALL).grist(BUILD, 15).grist(CAULK, 8).grist(AMETHYST, 5).grist(TAR, 5).buildFor(recipeSaver, Minestuck.MOD_ID);
		GristCostRecipeBuilder.of(Items.MUSIC_DISC_MELLOHI).grist(BUILD, 15).grist(CAULK, 8).grist(MARBLE, 5).grist(SHALE, 5).buildFor(recipeSaver, Minestuck.MOD_ID);
		GristCostRecipeBuilder.of(Items.MUSIC_DISC_STAL).grist(BUILD, 15).grist(CAULK, 8).grist(TAR, 5).grist(MERCURY, 5).buildFor(recipeSaver, Minestuck.MOD_ID);
		GristCostRecipeBuilder.of(Items.MUSIC_DISC_STRAD).grist(BUILD, 15).grist(CAULK, 8).grist(CHALK, 5).grist(QUARTZ, 5).buildFor(recipeSaver, Minestuck.MOD_ID);
		GristCostRecipeBuilder.of(Items.MUSIC_DISC_WAIT).grist(BUILD, 15).grist(CAULK, 8).grist(COBALT, 5).grist(QUARTZ, 5).buildFor(recipeSaver, Minestuck.MOD_ID);
		GristCostRecipeBuilder.of(Items.MUSIC_DISC_WARD).grist(BUILD, 15).grist(CAULK, 8).grist(IODINE, 5).grist(GOLD, 2).buildFor(recipeSaver, Minestuck.MOD_ID);
		
		ContainerGristCostBuilder.of(Items.WATER_BUCKET).grist(COBALT, 4).buildFor(recipeSaver, Minestuck.MOD_ID);
		ContainerGristCostBuilder.of(Items.LAVA_BUCKET).grist(TAR, 16).buildFor(recipeSaver, Minestuck.MOD_ID);
		ContainerGristCostBuilder.of(Items.MILK_BUCKET).grist(CHALK, 8).buildFor(recipeSaver, Minestuck.MOD_ID);
		
		WildcardGristCostBuilder.of(MSItems.CAPTCHA_CARD).cost(1).build(recipeSaver);
	}
	
	@Override
	public String getName()
	{
		return "Minestuck Grist Costs";
	}
}
