package com.mraof.minestuck.item;

import com.mraof.minestuck.block.MinestuckBlocks;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class ModItemGroup extends ItemGroup
{
	public static final ItemGroup MAIN = new ModItemGroup("minestuck", () -> new ItemStack(MinestuckItems.disk));
	public static final ItemGroup LANDS = new ModItemGroup("minestuckLands", () -> new ItemStack(MinestuckBlocks.GLOWING_MUSHROOM));
	public static final ItemGroup WEAPONS = new ModItemGroup("minestuckWeapons", () -> new ItemStack(MinestuckItems.zillyhooHammer));
	
	private final IItemStackProvider icon;
	
	public ModItemGroup(String label, IItemStackProvider icon)
	{
		super(label);
		this.icon = icon;
	}
	
	@OnlyIn(Dist.CLIENT)
	@Override
	public ItemStack createIcon()
	{
		return icon.getStack();
	}
	
	public interface IItemStackProvider
	{
		ItemStack getStack();
	}
}