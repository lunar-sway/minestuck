package com.mraof.minestuck.data;

import com.google.common.collect.Sets;
import com.mraof.minestuck.block.MSBlocks;
import com.mraof.minestuck.util.ExtraForgeTags;
import com.mraof.minestuck.util.MSTags;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.ItemTagsProvider;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.Tag;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.Tags;

import java.nio.file.Path;
import java.util.Set;

import static com.mraof.minestuck.item.MSItems.*;
import static com.mraof.minestuck.util.MSTags.Items.*;
import static net.minecraft.tags.ItemTags.*;
import static net.minecraftforge.common.Tags.Items.*;

public class MinestuckItemTagsProvider extends ItemTagsProvider
{
	private Set<ResourceLocation> filter;
	
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
		
		fakePopulate(INGOTS_IRON);
		fakePopulate(RODS_WOODEN);
		
		getBuilder(ItemTags.MUSIC_DISCS).add(RECORD_DANCE_STAB, RECORD_EMISSARY_OF_DANCE, RECORD_RETRO_BATTLE);
		getBuilder(Tags.Items.MUSIC_DISCS).add(RECORD_DANCE_STAB, RECORD_EMISSARY_OF_DANCE, RECORD_RETRO_BATTLE);
		getBuilder(DUSTS).add(MSBlocks.GLOWYSTONE_DUST.asItem());
		getBuilder(RODS).add(UP_STICK);
		getBuilder(ExtraForgeTags.Items.URANIUM_CHUNKS).add(RAW_URANIUM);
	}
	
	/**
	 * Used to work around an issue with using empty tags in recipe data providers
	 */
	protected void fakePopulate(Tag<Item> tag)
	{
		if(tagToBuilder.containsKey(tag))
			throw new IllegalStateException("Shouldn't do a fake populate on a tag with a builder!");
		getBuilder(tag).add(Items.BARRIER);
		if(filter == null)
			filter = Sets.newHashSet();
		filter.add(tag.getId());
	}
	
	@Override
	protected Tag.Builder<Item> getBuilder(Tag<Item> tag)
	{
		if(filter != null && filter.contains(tag.getId()))
			throw new IllegalStateException("Should't get a builder on a tag that has been passed to fakePopulate()!");
		return super.getBuilder(tag);
	}
	
	@Override
	protected Path makePath(ResourceLocation tagName)
	{
		return filter != null && filter.contains(tagName) ? null : super.makePath(tagName);
	}
	
	@Override
	public String getName()
	{
		return "Minestuck Item Tags";
	}
}
