package com.mraof.minestuck.data;

import com.mraof.minestuck.Minestuck;
import com.mraof.minestuck.util.ExtraForgeTags;
import com.mraof.minestuck.util.MSTags;
import net.minecraft.block.Blocks;
import net.minecraft.data.BlockTagsProvider;
import net.minecraft.data.DataGenerator;
import net.minecraftforge.common.Tags;
import net.minecraftforge.common.data.ExistingFileHelper;

import javax.annotation.Nullable;

import static com.mraof.minestuck.block.MSBlocks.*;
import static com.mraof.minestuck.util.MSTags.Blocks.*;
import static net.minecraft.tags.BlockTags.*;

public class MinestuckBlockTagsProviderOLD extends BlockTagsProvider
{
	public MinestuckBlockTagsProvider(DataGenerator generator, @Nullable ExistingFileHelper existingFileHelper)
	{
		super(generator, Minestuck.MOD_ID, existingFileHelper);
	}
	
	@Override
	@SuppressWarnings("unchecked")
	protected void addTags()
	{
		tag(HIEROGLYPHS).add(GREEN_STONE_BRICK_FROG, GREEN_STONE_BRICK_LOTUS, GREEN_STONE_BRICK_IGUANA_LEFT, GREEN_STONE_BRICK_IGUANA_RIGHT, GREEN_STONE_BRICK_NAK_LEFT, GREEN_STONE_BRICK_NAK_RIGHT, GREEN_STONE_BRICK_SALAMANDER_LEFT, GREEN_STONE_BRICK_SALAMANDER_RIGHT, GREEN_STONE_BRICK_SKAIA, GREEN_STONE_BRICK_TURTLE);
	}
	
	@Override
	public String getName()
	{
		return "Minestuck Block Tags";
	}
}
