package com.mraof.minestuck.inventory;

import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class SlotInput extends Slot {

	public Item id;
	
	public SlotInput(IInventory par1iInventory, int par2, int par3, int par4, Item id) {
		super(par1iInventory, par2, par3, par4);
		this.id = id;
	}
	
    public boolean isItemValid(ItemStack par1ItemStack)
    {
        return (par1ItemStack.getItem() == id);
    }

}
