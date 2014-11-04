package com.mraof.minestuck.inventory.captchalouge;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

public abstract class CaptchalougeModus
{
	
	public Side side;
	public EntityPlayer player;
	
	/**
	 * This is called when the modus is created without calling readFromNBT(nbt).
	 */
	public abstract void initModus(ItemStack[] prev);
	
	public abstract void readFromNBT(NBTTagCompound nbt);
	
	public abstract NBTTagCompound writeToNBT(NBTTagCompound nbt);
	
	public abstract boolean putItemStack(ItemStack item);
	
	@SideOnly(Side.CLIENT)
	public abstract ItemStack[] getItems();
	
	public abstract boolean increaseSize();
	
	public abstract ItemStack getItem(int id);
	
}
