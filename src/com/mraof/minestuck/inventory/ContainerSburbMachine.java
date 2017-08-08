package com.mraof.minestuck.inventory;

import com.mraof.minestuck.block.BlockSburbMachine.MachineType;
import com.mraof.minestuck.item.MinestuckItems;
import com.mraof.minestuck.tileentity.TileEntitySburbMachine;
import com.mraof.minestuck.util.IdentifierHandler;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IContainerListener;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

import javax.annotation.Nonnull;

public class ContainerSburbMachine extends Container
{
	
	private static final int cruxtruderInputX = 79;
	private static final int cruxtruderInputY = 57;
	private static final int cruxtruderOutputX = 79;
	private static final int cruxtruderOutputY = 19;
	
	private static final int designixInputX = 44;
	private static final int designixInputY = 26;
	private static final int designixCardsX = 44;
	private static final int designixCardsY = 50;
	private static final int designixOutputX = 116;
	private static final int designixOutputY = 37;
	
	private static final int latheCard1X = 26;
	private static final int latheCard1Y = 25;
	private static final int latheCard2X = 26;
	private static final int latheCard2Y = 43;
	private static final int latheDowelX = 62;
	private static final int latheDowelY = 34;
	private static final int latheOutputX = 134;
	private static final int latheOutputY = 34;
	
	private static final int alchemiterInputX = 27;
	private static final int alchemiterInputY = 20;
	private static final int alchemiterOutputX = 135;
	private static final int alchemiterOutputY = 20;
	
	public TileEntitySburbMachine tileEntity;
	private MachineType type;
	private boolean operator = true;
	private int progress;
	
	public ContainerSburbMachine(InventoryPlayer inventoryPlayer, TileEntitySburbMachine te)
	{
		tileEntity = te;
		type = te.getMachineType();
		te.owner = IdentifierHandler.encode(inventoryPlayer.player);
		
		switch (type)
		{
		case CRUXTRUDER:
			addSlotToContainer(new SlotInput(tileEntity, 0, cruxtruderInputX, cruxtruderInputY, MinestuckItems.rawCruxite));
			addSlotToContainer(new SlotOutput(tileEntity, 1, cruxtruderOutputX, cruxtruderOutputY));
			break;
		case PUNCH_DESIGNIX:
			addSlotToContainer(new Slot(tileEntity, 0, designixInputX, designixInputY));
			addSlotToContainer(new SlotInput(tileEntity, 1, designixCardsX, designixCardsY, MinestuckItems.captchaCard));
			addSlotToContainer(new SlotOutput(tileEntity, 2, designixOutputX, designixOutputY));
			break;
		case TOTEM_LATHE:
			addSlotToContainer(new SlotInput(tileEntity, 0, latheCard1X, latheCard1Y, MinestuckItems.captchaCard));
			addSlotToContainer(new SlotInput(tileEntity, 1, latheCard2X, latheCard2Y, MinestuckItems.captchaCard));
			addSlotToContainer(new SlotInput(tileEntity, 2, latheDowelX, latheDowelY, MinestuckItems.cruxiteDowel));
			addSlotToContainer(new SlotOutput(tileEntity, 3, latheOutputX, latheOutputY));
			break;
		case ALCHEMITER:
			addSlotToContainer(new SlotInput(tileEntity, 0, alchemiterInputX, alchemiterInputY, MinestuckItems.cruxiteDowel));
			addSlotToContainer(new SlotOutput(tileEntity, 1, alchemiterOutputX, alchemiterOutputY));
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
			case CRUXTRUDER:
				if (slotNumber <= 1)
				{
					//if it's a machine slot
					result = mergeItemStack(itemstackOrig,2,allSlots,false);
				} else if (slotNumber > 1)
				{
					//if it's an inventory slot with valid contents
					//Debug.print("item ID of " + itemstackOrig.itemID + ". Expected " + Minestuck.rawCruxite.itemID);
					if (itemstackOrig.getItem() == MinestuckItems.rawCruxite)
					{
						//Debug.print("Transferring...");
						result = mergeItemStack(itemstackOrig,0,1,false);
					}
				}
				break;
			case PUNCH_DESIGNIX:
				if (slotNumber <= 2)
				{
					//if it's a machine slot
					result = mergeItemStack(itemstackOrig,3,allSlots,false);
				}
				else if (slotNumber > 2)
				{
					//if it's an inventory slot with valid contents
					if (itemstackOrig.getItem() == MinestuckItems.captchaCard && (itemstackOrig.getTagCompound() == null
							|| !itemstackOrig.getTagCompound().hasKey("contentID") || itemstackOrig.getTagCompound().getBoolean("punched")))
						result = mergeItemStack(itemstackOrig,1,2,false);
					else result = mergeItemStack(itemstackOrig,0,1,false);
				}
				break;
			case TOTEM_LATHE:
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
				break;
			case ALCHEMITER:
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