package com.mraof.minestuck.inventory;

import com.mraof.minestuck.block.MSBlocks;
import com.mraof.minestuck.inventory.slot.InputSlot;
import com.mraof.minestuck.inventory.slot.OutputSlot;
import com.mraof.minestuck.item.MSItems;
import net.minecraft.block.Block;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.IIntArray;
import net.minecraft.util.IWorldPosCallable;
import net.minecraft.util.IntArray;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;

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
	
	public MiniTotemLatheContainer(int windowId, PlayerInventory playerInventory, PacketBuffer buffer)
	{
		this(MSContainerTypes.MINI_TOTEM_LATHE, windowId, playerInventory, new ItemStackHandler(4), new IntArray(3), IWorldPosCallable.NULL, buffer.readBlockPos());
	}
	
	public MiniTotemLatheContainer(int windowId, PlayerInventory playerInventory, IItemHandler inventory, IIntArray parameters, IWorldPosCallable position, BlockPos machinePos)
	{
		this(MSContainerTypes.MINI_TOTEM_LATHE, windowId, playerInventory, inventory, parameters, position, machinePos);
	}
	
	public MiniTotemLatheContainer(ContainerType<? extends MiniTotemLatheContainer> type, int windowId, PlayerInventory playerInventory, IItemHandler inventory, IIntArray parameters, IWorldPosCallable position, BlockPos machinePos)
	{
		super(type, windowId, parameters, position, machinePos);
		
		assertItemHandlerSize(inventory, 4);
		
		addSlot(new InputSlot(inventory, 0, CARD_1_X, CARD_1_Y, MSItems.CAPTCHA_CARD));
		addSlot(new InputSlot(inventory, 1, CARD_2_X, CARD_2_Y, MSItems.CAPTCHA_CARD));
		addSlot(new InputSlot(inventory, 2, DOWEL_X, DOWEL_Y, MSBlocks.CRUXITE_DOWEL.asItem()));
		addSlot(new OutputSlot(inventory, 3, OUTPUT_X, OUTPUT_Y));
		
		bindPlayerInventory(playerInventory);
	}
	
	@Override
	protected Block getValidBlock()
	{
		return MSBlocks.MINI_TOTEM_LATHE;
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
	public ItemStack quickMoveStack(PlayerEntity player, int slotNumber)
	{
		ItemStack itemstack = ItemStack.EMPTY;
		Slot slot = this.slots.get(slotNumber);
		int allSlots = this.slots.size();
		
		if (slot != null && slot.hasItem())
		{
			ItemStack itemstackOrig = slot.getItem();
			itemstack = itemstackOrig.copy();
			boolean result = false;
			
			
			if(slotNumber <= 3)
			{
				//if it's a machine slot
				result = moveItemStackTo(itemstackOrig, 4, allSlots, false);
			} else if(slotNumber > 3)
			{
				//if it's an inventory slot with valid contents
				if(itemstackOrig.getItem() == MSItems.CAPTCHA_CARD)
					result = moveItemStackTo(itemstackOrig, 0, 2, false);
				else if(itemstackOrig.getItem() == MSBlocks.CRUXITE_DOWEL.asItem())
					result = moveItemStackTo(itemstackOrig, 2, 3, false);
			}
			
			if(!result)
				return ItemStack.EMPTY;
			
			if(!itemstackOrig.isEmpty())
				slot.setChanged();
		}
		
		return itemstack;
	}
}