package com.mraof.minestuck.inventory;

import com.mraof.minestuck.block.MinestuckBlocks;
import com.mraof.minestuck.inventory.slot.InputSlot;
import com.mraof.minestuck.inventory.slot.OutputSlot;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIntArray;
import net.minecraft.util.IntArray;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nonnull;

public class MiniAlchemiterContainer extends Container
{
	
	private static final int INPUT_X = 27;
	private static final int INPUT_Y = 20;
	private static final int OUTPUT_X = 135;
	private static final int OUTPUT_Y = 20;
	
	private final IInventory alchemiterInventory;
	private final IIntArray parameters;
	
	public MiniAlchemiterContainer(int windowId, PlayerInventory playerInventory)
	{
		this(ModContainerTypes.MINI_ALCHEMITER, windowId, playerInventory, new Inventory(2), new IntArray(1));
	}
	
	public MiniAlchemiterContainer(int windowId, PlayerInventory playerInventory, IInventory inventory, IIntArray parameters)
	{
		this(ModContainerTypes.MINI_ALCHEMITER, windowId, playerInventory, inventory, parameters);
	}
	
	public MiniAlchemiterContainer(ContainerType<? extends MiniAlchemiterContainer> type, int windowId, PlayerInventory playerInventory, IInventory inventory, IIntArray parameters)
	{
		super(type, windowId);
		
		assertInventorySize(inventory, 2);
		assertIntArraySize(parameters, 1);
		this.alchemiterInventory = inventory;
		this.parameters = parameters;
		
		addSlot(new InputSlot(inventory, 0, INPUT_X, INPUT_Y, MinestuckBlocks.CRUXITE_DOWEL.asItem()));
		addSlot(new OutputSlot(inventory, 1, OUTPUT_X, OUTPUT_Y));
		trackIntArray(parameters);
		
		bindPlayerInventory(playerInventory);
	}
	@Override
	public boolean canInteractWith(PlayerEntity player)
	{
		return alchemiterInventory.isUsableByPlayer(player);
	}
	
	protected void bindPlayerInventory(PlayerInventory playerInventory)
	{
		for (int i = 0; i < 3; i++)
			for (int j = 0; j < 9; j++)
				addSlot(new Slot(playerInventory, j + i * 9 + 9,
						8 + j * 18, 84 + i * 18));
		
		for (int i = 0; i < 9; i++)
			addSlot(new Slot(playerInventory, i, 8 + i * 18, 142));
	}
	
	@Nonnull
	@Override
	public ItemStack transferStackInSlot(PlayerEntity player, int slotNumber)
	{
		ItemStack itemstack = ItemStack.EMPTY;
		Slot slot = this.inventorySlots.get(slotNumber);
		int allSlots = this.inventorySlots.size();
		
		if(slot != null && slot.getHasStack())
		{
			ItemStack itemstackOrig = slot.getStack();
			itemstack = itemstackOrig.copy();
			boolean result = false;
			
			if(slotNumber <= 1)
			{
				//if it's a machine slot
				result = mergeItemStack(itemstackOrig, 2, allSlots, false);
			} else if(slotNumber > 1)
			{
				//if it's an inventory slot with valid contents
				if(itemstackOrig.getItem() == MinestuckBlocks.CRUXITE_DOWEL.asItem())
					result = mergeItemStack(itemstackOrig, 0, 1, false);
			}
			
			if(!result)
				return ItemStack.EMPTY;
			
			if(!itemstackOrig.isEmpty())
				slot.onSlotChanged();
		}
		
		return itemstack;
	}
	
	@OnlyIn(Dist.CLIENT)
	public int getProgress()
	{
		return parameters.get(0);
	}
}