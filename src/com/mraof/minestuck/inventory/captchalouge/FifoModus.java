package com.mraof.minestuck.inventory.captchalouge;

import java.util.Iterator;
import java.util.LinkedList;

import com.mraof.minestuck.Minestuck;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

public class FifoModus extends FiloModus
{
	
	@Override
	public ItemStack getItem(int id)
	{
		return list.removeLast();
	}
	
}
