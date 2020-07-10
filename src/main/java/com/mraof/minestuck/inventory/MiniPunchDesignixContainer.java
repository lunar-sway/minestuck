package com.mraof.minestuck.inventory;

import com.mraof.minestuck.inventory.slot.InputSlot;
import com.mraof.minestuck.inventory.slot.OutputSlot;
import com.mraof.minestuck.item.MSItems;
import com.mraof.minestuck.item.crafting.alchemy.AlchemyRecipes;
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

public class MiniPunchDesignixContainer extends MachineContainer
{
	
	private static final int designixInputX = 44;
	private static final int designixInputY = 26;
	private static final int designixCardsX = 44;
	private static final int designixCardsY = 50;
	private static final int designixOutputX = 116;
	private static final int designixOutputY = 37;
	
	private final IInventory designixInventory;
	
	public MiniPunchDesignixContainer(int windowId, PlayerInventory playerInventory, PacketBuffer buffer)
	{
		this(MSContainerTypes.MINI_PUNCH_DESIGNIX, windowId, playerInventory, new Inventory(3), new IntArray(3), buffer.readBlockPos());
	}
	
	public MiniPunchDesignixContainer(int windowId, PlayerInventory playerInventory, IInventory inventory, IIntArray parameters, BlockPos machinePos)
	{
		this(MSContainerTypes.MINI_PUNCH_DESIGNIX, windowId, playerInventory, inventory, parameters, machinePos);
	}
	
	public MiniPunchDesignixContainer(ContainerType<? extends MiniPunchDesignixContainer> type, int windowId, PlayerInventory playerInventory, IInventory inventory, IIntArray parameters, BlockPos machinePos)
	{
		super(type, windowId, parameters, machinePos);
		
		assertInventorySize(inventory, 3);
		this.designixInventory = inventory;
		
		addSlot(new Slot(inventory, 0, designixInputX, designixInputY));
		addSlot(new InputSlot(inventory, 1, designixCardsX, designixCardsY, MSItems.CAPTCHA_CARD));
		addSlot(new OutputSlot(inventory, 2, designixOutputX, designixOutputY));
		
		bindPlayerInventory(playerInventory);
	}
	
	@Override
	public boolean canInteractWith(PlayerEntity player)
	{
		return designixInventory.isUsableByPlayer(player);
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
			
			
			if(slotNumber <= 2)
			{
				//if it's a machine slot
				result = mergeItemStack(itemstackOrig, 3, allSlots, false);
			} else if(slotNumber > 2)
			{
				//if it's an inventory slot with valid contents
				if(itemstackOrig.getItem() == MSItems.CAPTCHA_CARD && (!AlchemyRecipes.hasDecodedItem(itemstackOrig) || AlchemyRecipes.isPunchedCard(itemstackOrig)))
					result = mergeItemStack(itemstackOrig, 1, 2, false);
				else result = mergeItemStack(itemstackOrig, 0, 1, false);
			}
			
			if(!result)
				return ItemStack.EMPTY;
			
			if(!itemstackOrig.isEmpty())
				slot.onSlotChanged();
		}
		
		return itemstack;
	}
}