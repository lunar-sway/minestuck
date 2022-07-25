package com.mraof.minestuck.item;

import com.mraof.minestuck.block.MSBlocks;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;

import java.util.function.Supplier;

public class MSItemGroup extends CreativeModeTab
{
	public static final CreativeModeTab MAIN = new MSItemGroup("minestuck", () -> new ItemStack(MSItems.CLIENT_DISK));
	public static final CreativeModeTab LANDS = new MSItemGroup("minestuck_lands", () -> new ItemStack(MSBlocks.GLOWING_MUSHROOM.get()));
	public static final CreativeModeTab WEAPONS = new MSItemGroup("minestuck_weapons", () -> new ItemStack(MSItems.ZILLYHOO_HAMMER));
	
	private final Supplier<ItemStack> icon;
	
	public MSItemGroup(String label, Supplier<ItemStack> icon)
	{
		super(label);
		this.icon = icon;
	}
	
	@Override
	public ItemStack makeIcon()
	{
		return icon.get();
	}
}