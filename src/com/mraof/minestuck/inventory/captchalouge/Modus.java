package com.mraof.minestuck.inventory.captchalouge;

import com.mraof.minestuck.client.gui.captchalouge.SylladexGuiHandler;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nonnull;

public abstract class Modus
{
	
	public EntityPlayer player;	//TODO remove this and replace this by adding the player as argument in methods next version shift
	//This change will break addons that add their own modus
	public Side side;
	
	/**
	 * This is called when the modus is created without calling readFromNBT(nbt).
	 * Note that this method is used to clear the inventory/size after dropping stuff on death without creating a new instance.
	 */
	public abstract void initModus(NonNullList<ItemStack> prev, int size);
	
	public abstract void readFromNBT(NBTTagCompound nbt);
	
	public abstract NBTTagCompound writeToNBT(NBTTagCompound nbt);
	
	public abstract boolean putItemStack(ItemStack item);
	
	public abstract NonNullList<ItemStack> getItems();
	
	public int getNonEmptyCards()
	{
		int count = 0;
		for(ItemStack stack : getItems())
			if(!stack.isEmpty())
				count++;
		return count;
	}
	
	public abstract boolean increaseSize();
	
	@Nonnull
	public abstract ItemStack getItem(int id, boolean asCard);
	
	public abstract boolean canSwitchFrom(Modus modus);
	
	public abstract int getSize();
	
	public void setValue(byte type, int value) {}
	
	@SideOnly(Side.CLIENT)
	public abstract SylladexGuiHandler getGuiHandler();
	
	@SideOnly(Side.CLIENT)
	public String getName()
	{
		ResourceLocation type = CaptchaDeckHandler.getType(this.getClass());
		if(type == null)
			return "";
		else return CaptchaDeckHandler.getItem(type).getDisplayName();
	}
	
}
