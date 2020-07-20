package com.mraof.minestuck.item;

import com.mraof.minestuck.block.MSBlocks;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;

import java.util.function.Supplier;

public class MSItemGroup extends ItemGroup
{
	public static final ItemGroup MAIN = new MSItemGroup("minestuck", () -> new ItemStack(MSItems.CLIENT_DISK));
	public static final ItemGroup LANDS = new MSItemGroup("minestuck_lands", () -> new ItemStack(MSBlocks.GLOWING_MUSHROOM));
	public static final ItemGroup WEAPONS = new MSItemGroup("minestuck_weapons", () -> new ItemStack(MSItems.ZILLYHOO_HAMMER));
	public static final ItemGroup DECORATIONS = new MSItemGroup("minestuck_decorations", () -> new ItemStack(MSBlocks.CASSETTE_PLAYER_CLOSED));

	private final Supplier<ItemStack> icon;
	
	public MSItemGroup(String label, Supplier<ItemStack> icon)
	{
		super(label);
		this.icon = icon;
	}
	
	@Override
	public ItemStack createIcon()
	{
		return icon.get();
	}
}