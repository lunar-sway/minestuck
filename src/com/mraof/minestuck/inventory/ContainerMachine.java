package com.mraof.minestuck.inventory;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.ICrafting;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

import com.mraof.minestuck.Minestuck;
import com.mraof.minestuck.tileentity.TileEntityMachine;

public class ContainerMachine extends Container {
	
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
	
	public TileEntityMachine tileEntity;
	private int metadata;
	private boolean operator = true;
	private int progress;

	public ContainerMachine(InventoryPlayer inventoryPlayer, TileEntityMachine te)
	{
		tileEntity = te;
		metadata = te.getMachineType();
		te.owner = inventoryPlayer.player;
		
		//the Slot constructor takes the IInventory and the slot number in that it binds to
		//and the x-y coordinates it resides on-screen
		switch (metadata) {
		case (0):
			addSlotToContainer(new SlotInput(tileEntity,1,cruxtruderInputX,cruxtruderInputY,Minestuck.rawCruxite));
			addSlotToContainer(new SlotOutput(tileEntity,0,cruxtruderOutputX,cruxtruderOutputY));
			break;
		case (1):
			addSlotToContainer(new Slot(tileEntity,1,designixInputX,designixInputY));
			addSlotToContainer(new SlotInput(tileEntity,2,designixCardsX,designixCardsY,Minestuck.captchaCard));
			addSlotToContainer(new SlotOutput(tileEntity,0,designixOutputX,designixOutputY));
			break;
		case (2):
			addSlotToContainer(new SlotInput(tileEntity,1,latheCard1X,latheCard1Y,Minestuck.captchaCard));
			addSlotToContainer(new SlotInput(tileEntity,2,latheCard2X,latheCard2Y,Minestuck.captchaCard));
			addSlotToContainer(new SlotInput(tileEntity,3,latheDowelX,latheDowelY,Minestuck.cruxiteDowel));
			addSlotToContainer(new SlotOutput(tileEntity,0,latheOutputX,latheOutputY));
			break;
		case (3):
			addSlotToContainer(new SlotInput(tileEntity,1,alchemiterInputX,alchemiterInputY, Minestuck.cruxiteDowel));
			addSlotToContainer(new SlotOutput(tileEntity,0,alchemiterOutputX,alchemiterOutputY));
			break;
		case (4):
			addSlotToContainer(new SlotInput(tileEntity,1,alchemiterInputX,alchemiterInputY, Minestuck.captchaCard)
			{
				@Override
				public boolean isItemValid(ItemStack stack)
				{
					return super.isItemValid(stack) && (stack.hasTagCompound() && stack.getTagCompound().hasKey("contentID") && !stack.getTagCompound().getBoolean("punched"));
				}
			});
			break;
		}
//		for (int i = 0; i < 3; i++) {
//				for (int j = 0; j < 3; j++) {
//						addSlotToContainer(new Slot(tileEntity, j + i * 3, 62 + j * 18, 17 + i * 18));
//				}
//		}

		//commonly used vanilla code that adds the player's inventory
		bindPlayerInventory(inventoryPlayer);
	}
	@Override
	public boolean canInteractWith(EntityPlayer player) {
		return tileEntity.isUseableByPlayer(player);
	}


	protected void bindPlayerInventory(InventoryPlayer inventoryPlayer) {
		for (int i = 0; i < 3; i++) {
			for (int j = 0; j < 9; j++) {
				addSlotToContainer(new Slot(inventoryPlayer, j + i * 9 + 9,
						8 + j * 18, 84 + i * 18));
			}
		}

		for (int i = 0; i < 9; i++) {
			addSlotToContainer(new Slot(inventoryPlayer, i, 8 + i * 18, 142));
		}
	}

	@Override
	public ItemStack transferStackInSlot(EntityPlayer player, int slotNumber) {
		ItemStack itemstack = null;
		Slot slot = (Slot)this.inventorySlots.get(slotNumber);
		int allSlots = this.inventorySlots.size();
		//int invSlots = tileEntity.getSizeInventory();

		if (slot != null && slot.getHasStack())
		{
			ItemStack itemstackOrig = slot.getStack();
			itemstack = itemstackOrig.copy();
			boolean result = false;
			
			//Debug.print("Shifing slot "+slotNumber);
			
			switch (metadata) {
			case (0):
				if (slotNumber <= 1) {
					//if it's a machine slot
					result = mergeItemStack(itemstackOrig,2,allSlots,false);
				} else if (slotNumber > 1) {
					//if it's an inventory slot with valid contents
					//Debug.print("item ID of " + itemstackOrig.itemID + ". Expected " + Minestuck.rawCruxite.itemID);
					if (itemstackOrig.getItem() == Minestuck.rawCruxite) {
						//Debug.print("Transferring...");
						result = mergeItemStack(itemstackOrig,0,1,false);
					}
				}
				break;
			case (1):
				if (slotNumber <= 2)
				{
					//if it's a machine slot
					result = mergeItemStack(itemstackOrig,3,allSlots,false);
				}
				else if (slotNumber > 2)
				{
					//if it's an inventory slot with valid contents
					if (itemstackOrig.getItem() == Minestuck.captchaCard)
						result = mergeItemStack(itemstackOrig,1,2,false);
					else result = mergeItemStack(itemstackOrig,0,1,false);
				}
				break;
			case (2):
				if (slotNumber <= 3)
				{
					//if it's a machine slot
					result = mergeItemStack(itemstackOrig,4,allSlots,false);
				}
				else if (slotNumber > 3)
				{
					//if it's an inventory slot with valid contents
					if (itemstackOrig.getItem() == Minestuck.captchaCard) 
						result = mergeItemStack(itemstackOrig,0,2,false);
					else if (itemstackOrig.getItem() == Minestuck.cruxiteDowel)
						result = mergeItemStack(itemstackOrig,2,3,false);
				}
				break;
			case (3):
			   	if (slotNumber <= 1) {
					//if it's a machine slot
					result = mergeItemStack(itemstackOrig,2,allSlots,false);
				} else if (slotNumber > 1) {
					//if it's an inventory slot with valid contents
					if (itemstackOrig.getItem() == Minestuck.cruxiteDowel)
						result = mergeItemStack(itemstackOrig,0,1,false);
				}
				break;
			case (4):
				if (slotNumber <= 0) {
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
			
			if (!result) {
				return null;
			}
			
			if (itemstackOrig.stackSize == 0)
			{
				slot.putStack((ItemStack)null);
			}
			else
			{
				slot.onSlotChanged();
			}
		}
		
		return itemstack;
	}
	
	public void detectAndSendChanges()
	{
		if(this.progress != tileEntity.progress && tileEntity.progress != 0)
			for(ICrafting crafter : (Iterable<ICrafting>) crafters)
				crafter.sendProgressBarUpdate(this, 0, tileEntity.progress);	//The server should update and send the progress bar to the client because client and server ticks aren't synchronized
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

