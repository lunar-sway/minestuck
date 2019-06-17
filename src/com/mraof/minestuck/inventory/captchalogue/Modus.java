package com.mraof.minestuck.inventory.captchalogue;

import com.mraof.minestuck.client.gui.captchalouge.SylladexGuiHandler;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.LogicalSide;

public abstract class Modus
{
	public final LogicalSide side;
	
	public Modus(LogicalSide side)
	{
		this.side = side;
	}
	
	public abstract ResourceLocation getRegistryName();
	
	/**
	 * This is called when the modus is created without calling readFromNBT(nbt).
	 * Note that this method is used to clear the inventory/size after dropping stuff on death without creating a new instance.
	 */
	public abstract void initModus(EntityPlayerMP player, NonNullList<ItemStack> prev, int size);
	
	public abstract void readFromNBT(NBTTagCompound nbt);
	
	public abstract NBTTagCompound writeToNBT(NBTTagCompound nbt);
	
	public abstract boolean putItemStack(EntityPlayerMP player, ItemStack item);
	
	public abstract NonNullList<ItemStack> getItems();
	
	public int getNonEmptyCards()
	{
		int count = 0;
		for(ItemStack stack : getItems())
			if(!stack.isEmpty())
				count++;
		return count;
	}
	
	public abstract boolean increaseSize(EntityPlayerMP player);
	
	public abstract ItemStack getItem(EntityPlayerMP player, int id, boolean asCard);
	
	public abstract boolean canSwitchFrom(Modus modus);
	
	public abstract int getSize();
	
	public void setValue(EntityPlayerMP player, byte type, int value) {}
	
	@OnlyIn(Dist.CLIENT)
	public abstract SylladexGuiHandler getGuiHandler();
	
	@OnlyIn(Dist.CLIENT)
	public ITextComponent getName()
	{
		return CaptchaDeckHandler.getItem(this.getRegistryName()).getDisplayName();
	}
	
}
