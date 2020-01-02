package com.mraof.minestuck.inventory;

import com.mraof.minestuck.block.MSBlocks;
import com.mraof.minestuck.inventory.slot.InputSlot;
import com.mraof.minestuck.inventory.slot.OutputSlot;
import com.mraof.minestuck.item.MSItems;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.IIntArray;
import net.minecraft.util.IntArray;
import net.minecraft.util.math.BlockPos;

import javax.annotation.Nonnull;

public class MiniTotemLatheContainer extends MachineContainer
{
	private static final int CARD_1_X = 26;
	private static final int CARD_1_Y = 25;
	private static final int CARD_2_X = 26;
	private static final int CARD_2_Y = 43;
	private static final int DOWEL_X = 62;
	private static final int DOWEL_Y = 34;
	private static final int OUTPUT_X = 134;
	private static final int OUTPUT_Y = 34;
	
	private final IInventory totemLatheInventory;
	
	public MiniTotemLatheContainer(int windowId, PlayerInventory playerInventory, PacketBuffer buffer)
	{
		this(MSContainerTypes.MINI_TOTEM_LATHE, windowId, playerInventory, new Inventory(4), new IntArray(3), buffer.readBlockPos());
	}
	
	public MiniTotemLatheContainer(int windowId, PlayerInventory playerInventory, IInventory inventory, IIntArray parameters, BlockPos machinePos)
	{
		this(MSContainerTypes.MINI_TOTEM_LATHE, windowId, playerInventory, inventory, parameters, machinePos);
	}
	
	public MiniTotemLatheContainer(ContainerType<? extends MiniTotemLatheContainer> type, int windowId, PlayerInventory playerInventory, IInventory inventory, IIntArray parameters, BlockPos machinePos)
	{
		super(type, windowId, parameters, machinePos);
		
		assertInventorySize(inventory, 4);
		this.totemLatheInventory = inventory;
		
		addSlot(new InputSlot(inventory, 0, CARD_1_X, CARD_1_Y, MSItems.CAPTCHA_CARD));
		addSlot(new InputSlot(inventory, 1, CARD_2_X, CARD_2_Y, MSItems.CAPTCHA_CARD));
		addSlot(new InputSlot(inventory, 2, DOWEL_X, DOWEL_Y, MSBlocks.CRUXITE_DOWEL.asItem()));
		addSlot(new OutputSlot(inventory, 3, OUTPUT_X, OUTPUT_Y));
		
		bindPlayerInventory(playerInventory);
	}
	@Override
	public boolean canInteractWith(PlayerEntity player)
	{
		return totemLatheInventory.isUsableByPlayer(player);
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
				if(itemstackOrig.getItem() == MSItems.CAPTCHA_CARD)
					result = mergeItemStack(itemstackOrig, 0, 2, false);
				else if(itemstackOrig.getItem() == MSBlocks.CRUXITE_DOWEL.asItem())
					result = mergeItemStack(itemstackOrig, 2, 3, false);
			}
			
			if(!result)
				return ItemStack.EMPTY;
			
			if(!itemstackOrig.isEmpty())
				slot.onSlotChanged();
		}
		
		return itemstack;
	}
}