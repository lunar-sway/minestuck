package com.mraof.minestuck.inventory;

import com.mraof.minestuck.block.BlockGristWidget.MachineType;
import com.mraof.minestuck.item.MinestuckItems;
import com.mraof.minestuck.tileentity.TileEntityGristWidget;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IContainerListener;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

import javax.annotation.Nonnull;

public class ContainerCrockerMachine extends Container
{
	
	private static final int gristWidgetInputX = 27;
	private static final int gristWidgetInputY = 20;
	
	public TileEntityGristWidget tileEntity;
	private MachineType type;
	private boolean operator = true;
	private int progress;
	
	public ContainerCrockerMachine(InventoryPlayer inventoryPlayer, TileEntityGristWidget te)
	{
		tileEntity = te;
		type = te.getMachineType();
		
		//the Slot constructor takes the IInventory and the slot number in that it binds to
		//and the x-y coordinates it resides on-screen
		switch (type)
		{
		case GRIST_WIDGET:
			addSlotToContainer(new SlotInput(tileEntity, 0, gristWidgetInputX, gristWidgetInputY, MinestuckItems.captchaCard)
			{
				@Override
				public boolean isItemValid(ItemStack stack)
				{
					return super.isItemValid(stack) && (stack.hasTagCompound() && stack.getTagCompound().hasKey("contentID") && !stack.getTagCompound().getBoolean("punched"));
				}
			});
			break;
		}
		
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
			
			
			switch (type)
			{
			case GRIST_WIDGET:
				if (slotNumber <= 0)
				{
					//if it's a machine slot
					result = mergeItemStack(itemstackOrig,2,allSlots,false);
				}
				else if (slotNumber > 0 && getSlot(0).isItemValid(itemstackOrig))
				{
					//if it's an inventory slot with valid contents
					result = mergeItemStack(itemstackOrig, 0, 1, false);
				}
				break;
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