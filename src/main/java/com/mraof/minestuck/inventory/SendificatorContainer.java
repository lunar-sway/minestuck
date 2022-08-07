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
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import net.minecraftforge.items.SlotItemHandler;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.function.Consumer;

public class SendificatorContainer extends MachineContainer
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
	
	public static SendificatorContainer newFromPacket(int windowId, Inventory playerInventory, FriendlyByteBuf buffer)
	{
		BlockPos pos = buffer.readBlockPos();
		BlockPos dest = buffer.readBoolean() ? buffer.readBlockPos() : null;
		
		return new SendificatorContainer(MSContainerTypes.SENDIFICATOR, windowId, playerInventory, new ItemStackHandler(2),
				new SimpleContainerData(3), DataSlot.standalone(), OptionalPosHolder.dummy(dest),
				ContainerLevelAccess.NULL, pos);
	}
	
	public SendificatorContainer(int windowId, Inventory playerInventory, IItemHandler inventory,
								 ContainerData parameters, DataSlot fuelHolder, OptionalPosHolder destinationHolder,
								 ContainerLevelAccess position, BlockPos machinePos)
	{
		this(MSContainerTypes.SENDIFICATOR, windowId, playerInventory, inventory,
				parameters, fuelHolder, destinationHolder, position, machinePos);
	}
	
	public SendificatorContainer(MenuType<? extends SendificatorContainer> type, int windowId, Inventory playerInventory, IItemHandler inventory,
								 ContainerData parameters, DataSlot fuelHolder, OptionalPosHolder destinationHolder,
								 ContainerLevelAccess position, BlockPos machinePos)
	{
		super(type, windowId, parameters, position, machinePos);
		
		assertItemHandlerSize(inventory, 2);
		this.fuelHolder = fuelHolder;
		this.destinationHolder = destinationHolder;
		
		addSlot(new SlotItemHandler(inventory, 0, itemInputX, itemInputY));
		addSlot(new InputSlot(inventory, 1, uraniumInputX, uraniumInputY, MSItems.RAW_URANIUM));
		addDataSlot(fuelHolder);
		addDataSlots(destinationHolder.getIntArray());
		
		ContainerHelper.addPlayerInventorySlots(this::addSlot, 8, 84, playerInventory);
	}
	
	@Override
	protected Block getValidBlock()
	{
		return MSBlocks.SENDIFICATOR;
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
			ItemStack itemstackOrig = slot.getItem();
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
				if(itemstackOrig.getItem() == MSItems.RAW_URANIUM)
				{
					result = moveItemStackTo(itemstackOrig, 1, 2, false);    //Send the uranium to the uranium input
				} else
				{
					result = moveItemStackTo(itemstackOrig, 0, 1, false);    //Send the non-uranium to the other input
				}
			}
			
			if(!result)
				return ItemStack.EMPTY;
			
			if(!itemstackOrig.isEmpty())
				slot.setChanged();
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