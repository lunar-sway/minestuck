package com.mraof.minestuck.inventory.captchalouge;

import com.mraof.minestuck.Minestuck;
import com.mraof.minestuck.inventory.SlotInput;
import com.mraof.minestuck.util.Debug;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.InventoryBasic;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagList;

public class ContainerCaptchaDeck extends Container
{
	
	public InventoryBasic inventory = new InventoryBasic("ModusInventory", false, 1);
	
	public ContainerCaptchaDeck(EntityPlayer player)
	{
		addSlots(player);
	}
	
	private void addSlots(EntityPlayer player)
	{
		for(int i = 9; i < 36; i++)
			addSlotToContainer(new Slot(player.inventory, i, 9 + (i%9)*18, 63 + ((i - 9)/9)*18));
		for(int i = 0; i < 9; i++)
			addSlotToContainer(new Slot(player.inventory, i, 9 + i*18, 121));
		addSlotToContainer(new SlotInput(this.inventory, 0, 81, 32, Minestuck.captchaModus)
		{
			@Override
			public boolean isItemValid(ItemStack stack)
			{
				return super.isItemValid(stack) || stack.getItem().equals(Minestuck.captchaCard) && (!stack.hasTagCompound() || !stack.getTagCompound().getBoolean("punched"));
			}
		});
	}
	
	@Override
	public void onContainerClosed(EntityPlayer player)
	{
		ItemStack stack = this.inventory.getStackInSlotOnClosing(0);
		if(stack != null)
			player.dropPlayerItemWithRandomChoice(stack, false);
	}
	
	@Override
	public boolean canInteractWith(EntityPlayer player)
	{
		return ((InventoryPlayer)this.getSlot(0).inventory).player == player;
	}
	
	@Override
	public ItemStack transferStackInSlot(EntityPlayer player, int slotNumber)
	{
		Slot slot = getSlot(slotNumber);
		int slotCount = inventorySlots.size();
		if(slot != null && slot.getHasStack())
			if(slotNumber == slotCount - 1)
			{
				ItemStack stack1 = slot.getStack();
				ItemStack stack2 = stack1.copy();
				if(mergeItemStack(stack1, 0, slotCount - 1, false))
					return stack2;
			} else
			{
				ItemStack stack1 = slot.getStack();
				ItemStack stack2 = stack1.copy();
				if(getSlot(slotCount - 1).isItemValid(stack1) && mergeItemStack(stack1, slotCount - 1, slotCount, false))
					return stack2;
			}
		return null;
	}
	
}
