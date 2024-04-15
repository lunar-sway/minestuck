package com.mraof.minestuck.inventory;

import com.mraof.minestuck.block.MSBlocks;
import com.mraof.minestuck.inventory.slot.InputSlot;
import com.mraof.minestuck.item.MSItems;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.*;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.items.IItemHandler;
import net.neoforged.neoforge.items.ItemStackHandler;
import net.neoforged.neoforge.items.SlotItemHandler;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.function.Consumer;

public class SendificatorMenu extends MachineContainerMenu
{
	private static final int uraniumInputX = 23;
	private static final int uraniumInputY = 51;
	private static final int itemInputX = 23;
	private static final int itemInputY = 22;
	
	private final DataSlot fuelHolder;
	private final OptionalPosHolder destinationHolder;
	
	public static Consumer<FriendlyByteBuf> makeExtraDataWriter(BlockPos position, @Nullable BlockPos destination)
	{
		return buffer -> {
			buffer.writeBlockPos(position);
			if(destination == null)
			{
				buffer.writeBoolean(false);
			} else
			{
				buffer.writeBoolean(true);
				buffer.writeBlockPos(destination);
			}
		};
	}
	
	public static SendificatorMenu newFromPacket(int windowId, Inventory playerInventory, FriendlyByteBuf buffer)
	{
		BlockPos pos = buffer.readBlockPos();
		BlockPos dest = buffer.readBoolean() ? buffer.readBlockPos() : null;
		
		return new SendificatorMenu(MSMenuTypes.SENDIFICATOR.get(), windowId, playerInventory, new ItemStackHandler(2),
				new SimpleContainerData(3), DataSlot.standalone(), OptionalPosHolder.dummy(dest),
				ContainerLevelAccess.NULL, pos);
	}
	
	public SendificatorMenu(int windowId, Inventory playerInventory, IItemHandler inventory,
							ContainerData parameters, DataSlot fuelHolder, OptionalPosHolder destinationHolder,
							ContainerLevelAccess position, BlockPos machinePos)
	{
		this(MSMenuTypes.SENDIFICATOR.get(), windowId, playerInventory, inventory,
				parameters, fuelHolder, destinationHolder, position, machinePos);
	}
	
	public SendificatorMenu(MenuType<? extends SendificatorMenu> type, int windowId, Inventory playerInventory, IItemHandler inventory,
							ContainerData parameters, DataSlot fuelHolder, OptionalPosHolder destinationHolder,
							ContainerLevelAccess position, BlockPos machinePos)
	{
		super(type, windowId, parameters, position, machinePos);
		
		assertItemHandlerSize(inventory, 2);
		this.fuelHolder = fuelHolder;
		this.destinationHolder = destinationHolder;
		
		addSlot(new SlotItemHandler(inventory, 0, itemInputX, itemInputY));
		addSlot(new InputSlot(inventory, 1, uraniumInputX, uraniumInputY, MSItems.RAW_URANIUM.get()));
		addDataSlot(fuelHolder);
		addDataSlots(destinationHolder.getIntArray());
		
		ContainerHelper.addPlayerInventorySlots(this::addSlot, 8, 84, playerInventory);
	}
	
	@Override
	protected Block getValidBlock()
	{
		return MSBlocks.SENDIFICATOR.get();
	}
	
	@Nonnull
	@Override
	public ItemStack quickMoveStack(Player player, int slotNumber)
	{
		ItemStack itemstack = ItemStack.EMPTY;
		Slot slot = this.slots.get(slotNumber);
		int allSlots = this.slots.size();
		
		if(slot.hasItem())
		{
			ItemStack itemstackOrig = slot.getItem().copy();
			itemstack = itemstackOrig.copy();
			boolean result;
			
			if(slotNumber == 0)    //Shift-clicking from the item input
			{
				result = moveItemStackTo(itemstackOrig, 3, allSlots, false);    //Send into the inventory
				
			} else if(slotNumber == 1)    //Shift-clicking from the Uranium input
			{
				result = moveItemStackTo(itemstackOrig, 3, allSlots, false);    //Send into the inventory
			} else    //Shift-clicking from the inventory
			{
				if(itemstackOrig.getItem() == MSItems.RAW_URANIUM.get())
				{
					result = moveItemStackTo(itemstackOrig, 1, 2, false);    //Send the uranium to the uranium input
				} else
				{
					result = moveItemStackTo(itemstackOrig, 0, 1, false);    //Send the non-uranium to the other input
				}
			}
			
			if(!result)
				return ItemStack.EMPTY;
			
			if(!ItemStack.matches(itemstackOrig, slot.getItem()))
				slot.set(itemstackOrig);
		}
		
		return itemstack;
	}
	
	public int getFuel()
	{
		return fuelHolder.get();
	}
	
	public boolean hasDestination()
	{
		return destinationHolder.getBlockPos().isPresent();
	}
	
	@Nullable
	public BlockPos getDestination()
	{
		return destinationHolder.getBlockPos().orElse(null);
	}
}