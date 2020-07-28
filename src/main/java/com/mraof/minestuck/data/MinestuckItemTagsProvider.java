package com.mraof.minestuck.data;

import com.mraof.minestuck.block.MSBlocks;
import com.mraof.minestuck.item.MSItems;
import com.mraof.minestuck.util.ExtraForgeTags;
import com.mraof.minestuck.util.MSTags;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.ItemTagsProvider;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ItemTags;
import net.minecraftforge.common.Tags;

import static com.mraof.minestuck.item.MSItems.*;
import static com.mraof.minestuck.util.MSTags.Items.*;
import static net.minecraft.tags.ItemTags.*;
import static net.minecraftforge.common.Tags.Items.*;

public class MinestuckItemTagsProvider extends ItemTagsProvider
{
	
	MinestuckItemTagsProvider(DataGenerator generatorIn)
	{
		super(generatorIn);
	}
	
	@Override
	protected void registerTags()
	{
		copy(BlockTags.PLANKS, PLANKS);
		copy(BlockTags.STONE_BRICKS, STONE_BRICKS);
		copy(BlockTags.WOODEN_BUTTONS, WOODEN_BUTTONS);
		copy(BlockTags.BUTTONS, BUTTONS);
		copy(BlockTags.WOODEN_STAIRS, WOODEN_STAIRS);
		copy(BlockTags.WOODEN_SLABS, WOODEN_SLABS);
		copy(BlockTags.SAPLINGS, SAPLINGS);
		copy(BlockTags.STAIRS, STAIRS);
		copy(BlockTags.SLABS, SLABS);
		copy(BlockTags.LEAVES, LEAVES);
		copy(Tags.Blocks.COBBLESTONE, COBBLESTONE);
		copy(Tags.Blocks.ORES, ORES);
		copy(Tags.Blocks.ORES_COAL, ORES_COAL);
		copy(Tags.Blocks.ORES_DIAMOND, ORES_DIAMOND);
		copy(Tags.Blocks.ORES_GOLD, ORES_GOLD);
		copy(Tags.Blocks.ORES_IRON, ORES_IRON);
		copy(Tags.Blocks.ORES_LAPIS, ORES_LAPIS);
		copy(Tags.Blocks.ORES_QUARTZ, ORES_QUARTZ);
		copy(Tags.Blocks.ORES_REDSTONE, ORES_REDSTONE);
		copy(Tags.Blocks.STONE, STONE);
		copy(Tags.Blocks.STORAGE_BLOCKS, STORAGE_BLOCKS);
		copy(ExtraForgeTags.Blocks.URANIUM_ORES, ExtraForgeTags.Items.URANIUM_ORES);
		copy(ExtraForgeTags.Blocks.URANIUM_STORAGE_BLOCKS, ExtraForgeTags.Items.URANIUM_STORAGE_BLOCKS);
		copy(ExtraForgeTags.Blocks.TERRACOTTA, ExtraForgeTags.Items.TERRACOTTA);
		copy(MSTags.Blocks.GLOWING_LOGS, GLOWING_LOGS);
		copy(MSTags.Blocks.FROST_LOGS, FROST_LOGS);
		copy(MSTags.Blocks.RAINBOW_LOGS, RAINBOW_LOGS);
		copy(MSTags.Blocks.END_LOGS, END_LOGS);
		copy(MSTags.Blocks.VINE_LOGS, VINE_LOGS);
		copy(MSTags.Blocks.FLOWERY_VINE_LOGS, FLOWERY_VINE_LOGS);
		copy(MSTags.Blocks.DEAD_LOGS, DEAD_LOGS);
		copy(MSTags.Blocks.PETRIFIED_LOGS, PETRIFIED_LOGS);
		copy(MSTags.Blocks.ASPECT_LOGS, ASPECT_LOGS);
		copy(MSTags.Blocks.ASPECT_PLANKS, ASPECT_PLANKS);
		copy(MSTags.Blocks.ASPECT_LEAVES, ASPECT_LEAVES);
		copy(MSTags.Blocks.ASPECT_SAPLINGS, ASPECT_SAPLINGS);
		copy(MSTags.Blocks.CRUXITE_ORES, CRUXITE_ORES);
		copy(MSTags.Blocks.URANIUM_ORES, URANIUM_ORES);
		copy(MSTags.Blocks.COAL_ORES, COAL_ORES);
		copy(MSTags.Blocks.IRON_ORES, IRON_ORES);
		copy(MSTags.Blocks.GOLD_ORES, GOLD_ORES);
		copy(MSTags.Blocks.REDSTONE_ORES, REDSTONE_ORES);
		copy(MSTags.Blocks.QUARTZ_ORES, QUARTZ_ORES);
		copy(MSTags.Blocks.LAPIS_ORES, LAPIS_ORES);
		copy(MSTags.Blocks.DIAMOND_ORES, DIAMOND_ORES);
		copy(MSTags.Blocks.CRUXITE_STORAGE_BLOCKS, CRUXITE_STORAGE_BLOCKS);

		getBuilder(ItemTags.MUSIC_DISCS).add(MUSIC_DISC_DANCE_STAB_DANCE, MUSIC_DISC_EMISSARY_OF_DANCE, MUSIC_DISC_RETRO_BATTLE);
		getBuilder(Tags.Items.MUSIC_DISCS).add(MUSIC_DISC_DANCE_STAB_DANCE, MUSIC_DISC_EMISSARY_OF_DANCE, MUSIC_DISC_RETRO_BATTLE);
		getBuilder(DUSTS).add(MSBlocks.GLOWYSTONE_DUST.asItem());
		getBuilder(RODS).add(URANIUM_POWERED_STICK);
		getBuilder(ExtraForgeTags.Items.URANIUM_CHUNKS).add(RAW_URANIUM);
		getBuilder(ExtraForgeTags.Items.COPPER_ORES);
		getBuilder(ExtraForgeTags.Items.TIN_ORES);
		getBuilder(ExtraForgeTags.Items.SILVER_ORES);
		getBuilder(ExtraForgeTags.Items.LEAD_ORES);
		getBuilder(ExtraForgeTags.Items.NICKEL_ORES);
		getBuilder(ExtraForgeTags.Items.ALUMINIUM_ORES);
		getBuilder(ExtraForgeTags.Items.COBALT_ORES);
		getBuilder(ExtraForgeTags.Items.ARDITE_ORES);
		getBuilder(ExtraForgeTags.Items.COPPER_INGOTS);
		getBuilder(ExtraForgeTags.Items.TIN_INGOTS);
		getBuilder(ExtraForgeTags.Items.SILVER_INGOTS);
		getBuilder(ExtraForgeTags.Items.LEAD_INGOTS);
		getBuilder(ExtraForgeTags.Items.NICKEL_INGOTS);
		getBuilder(ExtraForgeTags.Items.INVAR_INGOTS);
		getBuilder(ExtraForgeTags.Items.ALUMINIUM_INGOTS);
		getBuilder(ExtraForgeTags.Items.COBALT_INGOTS);
		getBuilder(ExtraForgeTags.Items.ARDITE_INGOTS);
		getBuilder(ExtraForgeTags.Items.RED_ALLOY_INGOTS);
		
		getBuilder(GRIST_CANDY).add(BUILD_GUSHERS, AMBER_GUMMY_WORM, CAULK_PRETZEL, CHALK_CANDY_CIGARETTE, IODINE_LICORICE, SHALE_PEEP, TAR_LICORICE, COBALT_GUM, MARBLE_JAWBREAKER, MERCURY_SIXLETS, QUARTZ_JELLY_BEAN, SULFUR_CANDY_APPLE, AMETHYST_HARD_CANDY, GARNET_TWIX, RUBY_LOLLIPOP, RUST_GUMMY_EYE, DIAMOND_MINT, GOLD_CANDY_RIBBON, URANIUM_GUMMY_BEAR, ARTIFACT_WARHEAD, ZILLIUM_SKITTLES);
		getBuilder(MSTags.Items.FAYGO).add(MSItems.ORANGE_FAYGO, CANDY_APPLE_FAYGO, FAYGO_COLA, COTTON_CANDY_FAYGO, CREME_SODA_FAYGO, GRAPE_FAYGO, MOON_MIST_FAYGO, PEACH_FAYGO, REDPOP_FAYGO);
		getBuilder(MODUS_CARD).add(STACK_MODUS_CARD, QUEUE_MODUS_CARD, QUEUESTACK_MODUS_CARD, TREE_MODUS_CARD, HASHMAP_MODUS_CARD, SET_MODUS_CARD);
		getBuilder(CASSETTES).add(MSItems.CASSETTE_MELLOHI, CASSETTE_13, CASSETTE_BLOCKS, CASSETTE_CAT, CASSETTE_CHIRP, CASSETTE_FAR, CASSETTE_MALL, CASSETTE_DANCE_STAB, CASSETTE_RETRO_BATTLE, CASSETTE_EMISSARY);
	}

	@Override
	public String getName()
	{
		return "Minestuck Item Tags";
	}
}
