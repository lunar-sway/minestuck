package com.mraof.minestuck.inventory;

import com.mraof.minestuck.item.crafting.alchemy.GristType;
import com.mraof.minestuck.block.MSBlocks;
import com.mraof.minestuck.inventory.slot.InputSlot;
import com.mraof.minestuck.inventory.slot.OutputSlot;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIntArray;
import net.minecraft.util.IntArray;
import net.minecraft.util.IntReferenceHolder;

import javax.annotation.Nonnull;

public class MiniAlchemiterContainer extends MachineContainer
{
	
	private static final int INPUT_X = 27;
	private static final int INPUT_Y = 20;
	private static final int OUTPUT_X = 135;
	private static final int OUTPUT_Y = 20;
	
	private final IInventory alchemiterInventory;
	private final IntReferenceHolder wildcardHolder;
	
	public MiniAlchemiterContainer(int windowId, PlayerInventory playerInventory)
	{
		this(MSContainerTypes.MINI_ALCHEMITER, windowId, playerInventory, new Inventory(2), new IntArray(3), IntReferenceHolder.single());
	}
	
	public MiniAlchemiterContainer(int windowId, PlayerInventory playerInventory, IInventory inventory, IIntArray parameters, IntReferenceHolder wildcardHolder)
	{
		this(MSContainerTypes.MINI_ALCHEMITER, windowId, playerInventory, inventory, parameters, wildcardHolder);
	}
	
	public MiniAlchemiterContainer(ContainerType<? extends MiniAlchemiterContainer> type, int windowId, PlayerInventory playerInventory, IInventory inventory, IIntArray parameters, IntReferenceHolder wildcardHolder)
	{
		super(type, windowId, parameters);
		
		assertInventorySize(inventory, 2);
		this.alchemiterInventory = inventory;
		this.wildcardHolder = wildcardHolder;
		
		addSlot(new InputSlot(inventory, 0, INPUT_X, INPUT_Y, MSBlocks.CRUXITE_DOWEL.asItem()));
		addSlot(new OutputSlot(inventory, 1, OUTPUT_X, OUTPUT_Y));
		trackInt(wildcardHolder);
		
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
				if(itemstackOrig.getItem() == MSBlocks.CRUXITE_DOWEL.asItem())
					result = mergeItemStack(itemstackOrig, 0, 1, false);
			}
			
			if(!result)
				return ItemStack.EMPTY;
			
			if(!itemstackOrig.isEmpty())
				slot.onSlotChanged();
		}
		
		return itemstack;
	}
	
	public GristType getWildcardType()
	{
		GristType type = GristType.REGISTRY.getValue(wildcardHolder.get());
		if(type == null)
			type = GristType.BUILD;
		return type;
	}
}