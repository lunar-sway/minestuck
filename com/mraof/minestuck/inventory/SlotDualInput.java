package com.mraof.minestuck.inventory;

import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class SlotDualInput extends Slot {

	public Item id1;
	public Item id2;
	
	public SlotDualInput(IInventory par1iInventory, int par2, int par3, int par4, Item id1, Item id2) {
		super(par1iInventory, par2, par3, par4);
		this.id1 = id1;
		this.id2 = id2;
	}
	
    public boolean isItemValid(ItemStack par1ItemStack)
    {
        return (par1ItemStack.getItem() == id1 || par1ItemStack.getItem() == id2);
    }

}
