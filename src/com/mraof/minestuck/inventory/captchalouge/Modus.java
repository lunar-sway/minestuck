package com.mraof.minestuck.inventory.captchalouge;

import com.mraof.minestuck.Minestuck;
import com.mraof.minestuck.client.gui.captchalouge.SylladexGuiHandler;
import com.mraof.minestuck.inventory.captchalouge.CaptchaDeckHandler.ModusType;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

public abstract class Modus
{
	
	public EntityPlayer player;
	
	/**
	 * This is called when the modus is created without calling readFromNBT(nbt).
	 */
	public abstract void initModus(ItemStack[] prev);
	
	public abstract void readFromNBT(NBTTagCompound nbt);
	
	public abstract NBTTagCompound writeToNBT(NBTTagCompound nbt);
	
	public abstract boolean putItemStack(ItemStack item);
	
	public abstract ItemStack[] getItems();
	
	public abstract boolean increaseSize();
	
	public abstract ItemStack getItem(int id, boolean asCard);
	
	public abstract boolean canSwitchFrom(ModusType modus);
	
	@SideOnly(Side.CLIENT)
	public abstract SylladexGuiHandler getGuiHandler();
	
	@SideOnly(Side.CLIENT)
	public String getName()
	{
		ModusType type = ModusType.getType(this);
		if(type == null)
			return "";
		else return new ItemStack(Minestuck.captchaModus, 1, type.ordinal()).getDisplayName();
	}
	
}
