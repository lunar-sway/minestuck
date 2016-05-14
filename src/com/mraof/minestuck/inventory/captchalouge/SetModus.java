package com.mraof.minestuck.inventory.captchalouge;

import java.util.ArrayList;
import java.util.Iterator;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import com.mraof.minestuck.client.gui.captchalouge.SylladexGuiHandler;
import com.mraof.minestuck.inventory.captchalouge.CaptchaDeckHandler.ModusType;

public class SetModus extends Modus
{
	
	protected int size;
	protected ArrayList<ItemStack> list;
	
	@SideOnly(Side.CLIENT)
	protected boolean changed;
	@SideOnly(Side.CLIENT)
	protected ItemStack[] items;
	@SideOnly(Side.CLIENT)
	protected SylladexGuiHandler gui;
	
	@Override
	public void initModus(ItemStack[] prev, int size)
	{
		this.size = size;
		list = new ArrayList<ItemStack>();
		/*if(prev != null)
		{
			for(ItemStack stack : prev)
				if(stack != null)
					list.add(stack);
		}*/
		
		if(player.worldObj.isRemote)
		{
			items = new ItemStack[size];
			changed = prev != null;
		}
	}
	
	@Override
	public void readFromNBT(NBTTagCompound nbt)
	{
		size = nbt.getInteger("size");
		list = new ArrayList<ItemStack>();
		
		for(int i = 0; i < size; i++)
			if(nbt.hasKey("item"+i))
				list.add(ItemStack.loadItemStackFromNBT(nbt.getCompoundTag("item"+i)));
			else break;
		if(FMLCommonHandler.instance().getEffectiveSide().isClient())
		{
			items = new ItemStack[size];
			changed = true;
		}
	}
	
	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound nbt)
	{
		nbt.setInteger("size", size);
		Iterator<ItemStack> iter = list.iterator();
		for(int i = 0; i < list.size(); i++)
		{
			ItemStack stack = iter.next();
			nbt.setTag("item"+i, stack.writeToNBT(new NBTTagCompound()));
		}
		return nbt;
	}
	
	@Override
	public boolean putItemStack(ItemStack item)
	{
		if(size <= list.size() || item == null)
			return false;
		
		for(ItemStack stack : list)
			if(stack.getItem() == item.getItem() && (!stack.getHasSubtypes() || stack.getMetadata() == item.getMetadata()))
				return false;
		
		
		
		return true;
	}
	
	@Override
	public ItemStack[] getItems()
	{
		return null;
	}
	
	@Override
	public boolean increaseSize()
	{
		return false;
	}
	
	@Override
	public ItemStack getItem(int id, boolean asCard)
	{
		return null;
	}
	
	@Override
	public boolean canSwitchFrom(ModusType modus)
	{
		return false;
	}
	
	@Override
	public int getSize()
	{
		return 0;
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public SylladexGuiHandler getGuiHandler()
	{
		return null;
	}
}