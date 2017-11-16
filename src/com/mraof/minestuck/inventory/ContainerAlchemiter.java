package com.mraof.minestuck.inventory;

import com.mraof.minestuck.item.MinestuckItems;
import com.mraof.minestuck.tileentity.TileEntityAlchemiter;
import com.mraof.minestuck.util.IdentifierHandler;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IContainerListener;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

import javax.annotation.Nonnull;

public class ContainerAlchemiter extends Container
{

	
	private static final int alchemiterInputX = 27;
	private static final int alchemiterInputY = 20;
	private static final int alchemiterOutputX = 135;
	private static final int alchemiterOutputY = 20;
	
	public TileEntityAlchemiter tileEntity;
	
	private boolean operator = true;
	private int progress;
	
	public ContainerAlchemiter(InventoryPlayer inventoryPlayer, TileEntityAlchemiter te)
	{
		tileEntity = te;
		te.setOwner(IdentifierHandler.encode(inventoryPlayer.player));
		

			addSlotToContainer(new SlotInput(tileEntity, 0, alchemiterInputX, alchemiterInputY, MinestuckItems.cruxiteDowel));
			addSlotToContainer(new SlotOutput(tileEntity, 1, alchemiterOutputX, alchemiterOutputY));
		
		
		
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
				addSlotToContainer(new Slot(inventoryPlayer, j + i * 9 + 9,
						8 + j * 18, 84 + i * 18));
		
		for (int i = 0; i < 9; i++)
			addSlotToContainer(new Slot(inventoryPlayer, i, 8 + i * 18, 142));
	}
	
	@Nonnull
	@Override
	public ItemStack transferStackInSlot(EntityPlayer player, int slotNumber)
	{
		ItemStack itemstack = ItemStack.EMPTY;
		Slot slot = (Slot)this.inventorySlots.get(slotNumber);
		int allSlots = this.inventorySlots.size();
		
		if (slot != null && slot.getHasStack())
		{
			ItemStack itemstackOrig = slot.getStack();
			itemstack = itemstackOrig.copy();
			boolean result = false;
			
			
			
				if (slotNumber <= 1)
				{
					//if it's a machine slot
					result = mergeItemStack(itemstackOrig,2,allSlots,false);
				} else if (slotNumber > 1)
				{
					//if it's an inventory slot with valid contents
					if (itemstackOrig.getItem() == MinestuckItems.cruxiteDowel)
						result = mergeItemStack(itemstackOrig,0,1,false);
				}
		
			
			if (!result)
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
			return;
		}
	}
}