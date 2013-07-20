package com.mraof.minestuck.inventory;

import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

public class SlotInput extends Slot {

	private int id;
	
	public SlotInput(IInventory par1iInventory, int par2, int par3, int par4,int id) {
		super(par1iInventory, par2, par3, par4);
		this.id = id;
	}
	
    public boolean isItemValid(ItemStack par1ItemStack)
    {
        return (par1ItemStack.itemID == id);
    }

}
