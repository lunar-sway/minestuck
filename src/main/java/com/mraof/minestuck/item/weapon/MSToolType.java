package com.mraof.minestuck.item.weapon;

import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.common.ItemAbility;

import javax.annotation.Nullable;
import java.util.Arrays;
import java.util.List;

public record MSToolType(@Nullable TagKey<Block> mineableBlocks, ItemAbility... abilities)
{
	public boolean hasAction(ItemAbility toolAction)
	{
		return Arrays.asList(abilities).contains(toolAction);
	}
}
