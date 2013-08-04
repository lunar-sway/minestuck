package com.mraof.minestuck.inventory;

import com.mraof.minestuck.Minestuck;
import com.mraof.minestuck.tileentity.TileEntityMachine;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

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
        	addSlotToContainer(new SlotInput(tileEntity,1,alchemiterInputX,alchemiterInputY,Minestuck.cruxiteDowelCarved.itemID));
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
    public ItemStack transferStackInSlot(EntityPlayer player, int slot) {
//            ItemStack stack = null;
//            Slot slotObject = (Slot) inventorySlots.get(slot);
//
//            //null checks and checks if the item can be stacked (maxStackSize > 1)
//            if (slotObject != null && slotObject.getHasStack()) {
//                    ItemStack stackInSlot = slotObject.getStack();
//                    stack = stackInSlot.copy();
//
//                    //merges the item into player inventory since its in the tileEntity
//                    if (slot < 4) {
//                            if (!this.mergeItemStack(stackInSlot, 0, 35, true)) {
//                                    return null;
//                            }
//                    }
//                    //places it into the tileEntity is possible since its in the player inventory
//                    else if (!this.mergeItemStack(stackInSlot, 1, 3, false)) {
//                            return null;
//                    }
//
//                    if (stackInSlot.stackSize == 0) {
//                            slotObject.putStack(null);
//                    } else {
//                            slotObject.onSlotChanged();
//                    }
//
//                    if (stackInSlot.stackSize == stack.stackSize) {
//                            return null;
//                    }
//                    slotObject.onPickupFromSlot(player, stackInSlot);
//            }
//            return stack;
    	return null;
    }
}

