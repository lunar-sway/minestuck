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
	
	private static final int designexInput1X = 62;
	private static final int designexInput1Y = 26;
	private static final int designexInput2X = 62;
	private static final int designexInput2Y = 50;
	private static final int designexCardsX = 26;
	private static final int designexCardsY = 50;
	private static final int designexOutputX = 134;
	private static final int designexOutputY = 37;
	
	private static final int latheCardX = 44;
	private static final int latheCardY = 20;
	private static final int latheDowelX = 44;
	private static final int latheDowelY = 50;
	private static final int latheOutputX = 122;
	private static final int latheOutputY = 33;
	
	private static final int alchemiterInputX = 27;
	private static final int alchemiterInputY = 20;
	private static final int alchemiterOutputX = 135;
	private static final int alchemiterOutputY = 20;
	
	public TileEntityMachine tileEntity;
	private int metadata;
	private boolean operator = true;

	public ContainerMachine(InventoryPlayer inventoryPlayer, TileEntityMachine te) {
        tileEntity = te;
        metadata = te.getMetadata();
        te.owner = inventoryPlayer.player;

        //the Slot constructor takes the IInventory and the slot number in that it binds to
        //and the x-y coordinates it resides on-screen
        switch (metadata) {
        case (0):
        	addSlotToContainer(new SlotInput(tileEntity,1,cruxtruderInputX,cruxtruderInputY,Minestuck.rawCruxite.itemID));
    		addSlotToContainer(new SlotOutput(tileEntity,0,cruxtruderOutputX,cruxtruderOutputY));
        	break;
        case (1):
        	addSlotToContainer(new Slot(tileEntity,1,designexInput1X,designexInput1Y));
        	addSlotToContainer(new Slot(tileEntity,2,designexInput2X,designexInput2Y));
        	addSlotToContainer(new SlotInput(tileEntity,3,designexCardsX,designexCardsY,Minestuck.blankCard.itemID));
        	addSlotToContainer(new SlotOutput(tileEntity,0,designexOutputX,designexOutputY));
        	break;
        case (2):
        	addSlotToContainer(new SlotInput(tileEntity,1,latheCardX,latheCardY,Minestuck.punchedCard.itemID));
        	addSlotToContainer(new SlotInput(tileEntity,2,latheDowelX,latheDowelY,Minestuck.cruxiteDowel.itemID));
        	addSlotToContainer(new SlotOutput(tileEntity,0,latheOutputX,latheOutputY));
        	break;
        case (3):
        	addSlotToContainer(new SlotDualInput(tileEntity,1,alchemiterInputX,alchemiterInputY,Minestuck.cruxiteDowelCarved.itemID,Minestuck.cruxiteDowel.itemID));
        	addSlotToContainer(new SlotOutput(tileEntity,0,alchemiterOutputX,alchemiterOutputY));
        	//break;
        }
//        for (int i = 0; i < 3; i++) {
//                for (int j = 0; j < 3; j++) {
//                        addSlotToContainer(new Slot(tileEntity, j + i * 3, 62 + j * 18, 17 + i * 18));
//                }
//        }

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
        int invSlots = tileEntity.getSizeInventory();

        if (slot != null && slot.getHasStack())
        {
        	ItemStack itemstackOrig = slot.getStack();
            itemstack = itemstackOrig.copy();
            boolean result = false;
            
            //System.out.println("[MINESTUCK] Shifing slot "+slotNumber);
            
            switch (metadata) {
            case (0):
            	if (slotNumber <= 1) {
            		//if it's a machine slot
            		result = mergeItemStack(itemstackOrig,2,allSlots,false);
            	} else if (slotNumber > 1) {
            		//if it's an inventory slot with valid contents
            		//System.out.println("[MINESTUCK] item ID of " + itemstackOrig.itemID + ". Expected " + Minestuck.rawCruxite.itemID);
            		if (itemstackOrig.itemID == Minestuck.rawCruxite.itemID) {
            			//System.out.println("[MINESTUCK] Transferring...");
            			result = mergeItemStack(itemstackOrig,0,1,false);
            		}
            	}
            	break;
            case (1):
               	if (slotNumber <= 3) {
            		//if it's a machine slot
            		result = mergeItemStack(itemstackOrig,4,allSlots,false);
            	} else if (slotNumber > 3) {
            		//if it's an inventory slot with valid contents
            		if (itemstackOrig.itemID == Minestuck.blankCard.itemID) {
            			result = mergeItemStack(itemstackOrig,2,3,false);
            		} else {
            			result = mergeItemStack(itemstackOrig,0,2,false);
            		}
            	}
            	break;
            case (2):
               	if (slotNumber <= 2) {
            		//if it's a machine slot
            		result = mergeItemStack(itemstackOrig,3,allSlots,false);
            	} else if (slotNumber > 2) {
            		//if it's an inventory slot with valid contents
            		if (itemstackOrig.itemID == Minestuck.punchedCard.itemID) {
            			result = mergeItemStack(itemstackOrig,0,1,false);
            		} else if (itemstackOrig.itemID == Minestuck.cruxiteDowel.itemID) {
            			result = mergeItemStack(itemstackOrig,1,2,false);
            		}
            	}
            	break;
            case (3):
               	if (slotNumber <= 1) {
            		//if it's a machine slot
            		result = mergeItemStack(itemstackOrig,2,allSlots,false);
            	} else if (slotNumber > 1) {
            		//if it's an inventory slot with valid contents
            		if (itemstackOrig.itemID == Minestuck.cruxiteDowelCarved.itemID || itemstackOrig.itemID == Minestuck.cruxiteDowel.itemID) {
            			result = mergeItemStack(itemstackOrig,0,1,false);
            		}
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

    public void addCraftingToCrafters(ICrafting par1ICrafting)
    {
    	super.addCraftingToCrafters(par1ICrafting);
//    	System.out.printf("[MINESTUCK] addCraftingToCrafters running, the metadata is %d\n", this.metadata);
    	switch(this.metadata)
    	{
    	case 1:
//    		System.out.printf("[MINESTUCK] Mode is %b \n", this.tileEntity.mode);
    		par1ICrafting.sendProgressBarUpdate(this, 0, this.tileEntity.mode ? 0 : 1);
    	}
    }
    public void detectAndSendChanges()
    {
    	super.detectAndSendChanges();

    	for (int i = 0; i < this.crafters.size(); ++i)
    	{
    		ICrafting icrafting = (ICrafting)this.crafters.get(i);
    		switch(this.metadata)
    		{
    		case 1:
    			if (this.operator != (this.tileEntity.mode))
    			{
    				icrafting.sendProgressBarUpdate(this, 0, this.tileEntity.mode ? 0 : 1);
    				this.operator = this.tileEntity.mode;
    			}
    		}
    	}
    }
    @Override
    public void updateProgressBar(int par1, int par2) 
    {
    	switch(this.metadata)
    	{
    	case 1:
//    		System.out.println("Mode on Client is now " + par2);
    		tileEntity.mode = par2 == 0;
    	}
    }
}

