package com.mraof.minestuck.inventory;

import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

public class SlotDualInput extends Slot {

	public int id1;
	public int id2;
	
	public SlotDualInput(IInventory par1iInventory, int par2, int par3, int par4,int id1,int id2) {
		super(par1iInventory, par2, par3, par4);
		this.id1 = id1;
		this.id2 = id2;
	}
	
    public boolean isItemValid(ItemStack par1ItemStack)
    {
        return (par1ItemStack.itemID == id1 || par1ItemStack.itemID == id2);
    }

}
