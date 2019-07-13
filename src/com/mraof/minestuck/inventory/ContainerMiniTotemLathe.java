package com.mraof.minestuck.inventory;

import com.mraof.minestuck.block.MinestuckBlocks;
import com.mraof.minestuck.item.MinestuckItems;
import com.mraof.minestuck.tileentity.MiniTotemLatheTileEntity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IContainerListener;
import net.minecraft.inventory.Slot;
import net.minecraft.inventory.container.Container;
import net.minecraft.item.ItemStack;

import javax.annotation.Nonnull;

public class ContainerMiniTotemLathe extends Container
{
	private static final int CARD_1_X = 26;
	private static final int CARD_1_Y = 25;
	private static final int CARD_2_X = 26;
	private static final int CARD_2_Y = 43;
	private static final int DOWEL_X = 62;
	private static final int DOWEL_Y = 34;
	private static final int OUTPUT_X = 134;
	private static final int OUTPUT_Y = 34;
	
	public MiniTotemLatheTileEntity tileEntity;
	private int progress;
	
	public ContainerMiniTotemLathe(PlayerInventory inventoryPlayer, MiniTotemLatheTileEntity te)
	{
		tileEntity = te;
		
		addSlot(new SlotInput(tileEntity, 0, CARD_1_X, CARD_1_Y, MinestuckItems.CAPTCHA_CARD));
		addSlot(new SlotInput(tileEntity, 1, CARD_2_X, CARD_2_Y, MinestuckItems.CAPTCHA_CARD));
		addSlot(new SlotInput(tileEntity, 2, DOWEL_X, DOWEL_Y, MinestuckBlocks.CRUXITE_DOWEL.asItem()));
		addSlot(new SlotOutput(tileEntity, 3, OUTPUT_X, OUTPUT_Y));
		
		bindPlayerInventory(inventoryPlayer);
	}
	@Override
	public boolean canInteractWith(EntityPlayer player)
	{
		return tileEntity.isUsableByPlayer(player);
	}
	
	protected void bindPlayerInventory(InventoryPlayer inventoryPlayer)
	{
		for (int i = 0; i < 3; i++)
			for (int j = 0; j < 9; j++)
				addSlot(new Slot(inventoryPlayer, j + i * 9 + 9,
						8 + j * 18, 84 + i * 18));
		
		for (int i = 0; i < 9; i++)
			addSlot(new Slot(inventoryPlayer, i, 8 + i * 18, 142));
	}
	
	@Nonnull
	@Override
	public ItemStack transferStackInSlot(EntityPlayer player, int slotNumber)
	{
		ItemStack itemstack = ItemStack.EMPTY;
		Slot slot = this.inventorySlots.get(slotNumber);
		int allSlots = this.inventorySlots.size();
		
		if (slot != null && slot.getHasStack())
		{
			ItemStack itemstackOrig = slot.getStack();
			itemstack = itemstackOrig.copy();
			boolean result = false;
			
			
			if(slotNumber <= 3)
			{
				//if it's a machine slot
				result = mergeItemStack(itemstackOrig, 4, allSlots, false);
			} else if(slotNumber > 3)
			{
				//if it's an inventory slot with valid contents
				if(itemstackOrig.getItem() == MinestuckItems.CAPTCHA_CARD)
					result = mergeItemStack(itemstackOrig, 0, 2, false);
				else if(itemstackOrig.getItem() == MinestuckBlocks.CRUXITE_DOWEL.asItem())
					result = mergeItemStack(itemstackOrig, 2, 3, false);
			}
			
			if(!result)
				return ItemStack.EMPTY;
			
			if(!itemstackOrig.isEmpty())
				slot.onSlotChanged();
		}
		
		return itemstack;
	}
	
	@Override
	public void detectAndSendChanges()
	{
		if(this.progress != tileEntity.progress && tileEntity.progress != 0)
			for(IContainerListener listener : listeners)
				listener.sendWindowProperty(this, 0, tileEntity.progress);	//The server should update and send the progress bar to the client because client and server ticks aren't synchronized
		this.progress = tileEntity.progress;
	}
	@Override
	public void updateProgressBar(int par1, int par2) 
	{
		if(par1 == 0)
		{
			tileEntity.progress = par2;
		}
	}
}