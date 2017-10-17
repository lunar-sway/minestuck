package com.mraof.minestuck.inventory;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IContainerListener;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

import javax.annotation.Nonnull;

import com.mraof.minestuck.block.BlockSburbMachine.MachineType;
import com.mraof.minestuck.item.MinestuckItems;
import com.mraof.minestuck.tileentity.TileEntityTotemlathe;
import com.mraof.minestuck.util.IdentifierHandler;

public class ContainerTotemlathe extends Container
{
	

	
	private static final int latheCard1X = 26;
	private static final int latheCard1Y = 25;
	private static final int latheCard2X = 26;
	private static final int latheCard2Y = 43;
	private static final int latheDowelX = 62;
	private static final int latheDowelY = 34;
	private static final int latheOutputX = 134;
	private static final int latheOutputY = 34;
	
	
	public TileEntityTotemlathe tileEntity;
	private int progress;
	
	public ContainerTotemlathe(InventoryPlayer inventoryPlayer, TileEntityTotemlathe te)
	{
		tileEntity = te;
		te.getMachineType();
		te.owner = IdentifierHandler.encode(inventoryPlayer.player);
		
	
			addSlotToContainer(new SlotInput(tileEntity, 0, latheCard1X, latheCard1Y, MinestuckItems.captchaCard));
			addSlotToContainer(new SlotInput(tileEntity, 1, latheCard2X, latheCard2Y, MinestuckItems.captchaCard));
			addSlotToContainer(new SlotInput(tileEntity, 2, latheDowelX, latheDowelY, MinestuckItems.cruxiteDowel));
			addSlotToContainer(new SlotOutput(tileEntity, 3, latheOutputX, latheOutputY));

		
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
			
			
			
				if (slotNumber <= 3)
				{
					//if it's a machine slot
					result = mergeItemStack(itemstackOrig,4,allSlots,false);
				}
				else if (slotNumber > 3)
				{
					//if it's an inventory slot with valid contents
					if (itemstackOrig.getItem() == MinestuckItems.captchaCard) 
						result = mergeItemStack(itemstackOrig,0,2,false);
					else if (itemstackOrig.getItem() == MinestuckItems.cruxiteDowel)
						result = mergeItemStack(itemstackOrig,2,3,false);
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
				listener.sendProgressBarUpdate(this, 0, tileEntity.progress);	//The server should update and send the progress bar to the client because client and server ticks aren't synchronized
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