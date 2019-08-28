package com.mraof.minestuck.data;

import com.mraof.minestuck.util.MinestuckTags;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.ItemTagsProvider;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ItemTags;
import net.minecraftforge.common.Tags;

import static com.mraof.minestuck.item.MinestuckItems.*;
import static com.mraof.minestuck.util.MinestuckTags.Items.*;
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
		copy(MinestuckTags.Blocks.GLOWING_LOGS, GLOWING_LOGS);
		copy(MinestuckTags.Blocks.FROST_LOGS, FROST_LOGS);
		copy(MinestuckTags.Blocks.RAINBOW_LOGS, RAINBOW_LOGS);
		copy(MinestuckTags.Blocks.END_LOGS, END_LOGS);
		copy(MinestuckTags.Blocks.VINE_LOGS, VINE_LOGS);
		copy(MinestuckTags.Blocks.FLOWERY_VINE_LOGS, FLOWERY_VINE_LOGS);
		copy(MinestuckTags.Blocks.DEAD_LOGS, DEAD_LOGS);
		copy(MinestuckTags.Blocks.PETRIFIED_LOGS, PETRIFIED_LOGS);
		copy(MinestuckTags.Blocks.ASPECT_LOGS, ASPECT_LOGS);
		copy(MinestuckTags.Blocks.ASPECT_PLANKS, ASPECT_PLANKS);
		copy(MinestuckTags.Blocks.ASPECT_LEAVES, ASPECT_LEAVES);
		copy(MinestuckTags.Blocks.ASPECT_SAPLINGS, ASPECT_SAPLINGS);
		copy(MinestuckTags.Blocks.CRUXITE_ORES, CRUXITE_ORES);
		copy(MinestuckTags.Blocks.URANIUM_ORES, URANIUM_ORES);
		copy(MinestuckTags.Blocks.CRUXITE_STORAGE_BLOCKS, CRUXITE_STORAGE_BLOCKS);
		copy(MinestuckTags.Blocks.URANIUM_STORAGE_BLOCKS, URANIUM_STORAGE_BLOCKS);
		
		getBuilder(ItemTags.MUSIC_DISCS).add(RECORD_DANCE_STAB, RECORD_EMISSARY_OF_DANCE, RECORD_RETRO_BATTLE);
		getBuilder(Tags.Items.MUSIC_DISCS).add(RECORD_DANCE_STAB, RECORD_EMISSARY_OF_DANCE, RECORD_RETRO_BATTLE);
		getBuilder(RODS).add(UP_STICK);
	}
	
	@Override
	public String getName()
	{
		return "Minestuck Item Tags";
	}
}
