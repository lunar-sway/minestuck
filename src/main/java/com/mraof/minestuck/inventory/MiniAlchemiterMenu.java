package com.mraof.minestuck.inventory;

import com.mraof.minestuck.api.alchemy.GristType;
import com.mraof.minestuck.api.alchemy.GristTypes;
import com.mraof.minestuck.block.MSBlocks;
import com.mraof.minestuck.blockentity.machine.MiniAlchemiterBlockEntity;
import com.mraof.minestuck.inventory.slot.InputSlot;
import com.mraof.minestuck.inventory.slot.OutputSlot;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.*;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.items.IItemHandler;
import net.neoforged.neoforge.items.ItemStackHandler;

import javax.annotation.Nonnull;

public class MiniAlchemiterMenu extends MachineContainerMenu
{
	
	private static final int INPUT_X = 27;
	private static final int INPUT_Y = 20;
	private static final int OUTPUT_X = 135;
	private static final int OUTPUT_Y = 20;
	
	private final DataSlot wildcardHolder;
	
	public MiniAlchemiterMenu(int windowId, Inventory playerInventory, FriendlyByteBuf buffer)
	{
		this(MSMenuTypes.MINI_ALCHEMITER.get(), windowId, playerInventory, new ItemStackHandler(2), new SimpleContainerData(3), DataSlot.standalone(), ContainerLevelAccess.NULL, buffer.readBlockPos());
	}
	
	public MiniAlchemiterMenu(int windowId, Inventory playerInventory, IItemHandler inventory, ContainerData parameters, DataSlot wildcardHolder, ContainerLevelAccess access, BlockPos machinePos)
	{
		this(MSMenuTypes.MINI_ALCHEMITER.get(), windowId, playerInventory, inventory, parameters, wildcardHolder, access, machinePos);
	}
	
	public MiniAlchemiterMenu(MenuType<? extends MiniAlchemiterMenu> type, int windowId, Inventory playerInventory, IItemHandler inventory, ContainerData parameters, DataSlot wildcardHolder, ContainerLevelAccess access, BlockPos machinePos)
	{
		super(type, windowId, parameters, access, machinePos);
		
		assertItemHandlerSize(inventory, 2);
		this.wildcardHolder = wildcardHolder;
		
		addSlot(new InputSlot(inventory, MiniAlchemiterBlockEntity.INPUT, INPUT_X, INPUT_Y, MSBlocks.CRUXITE_DOWEL.get().asItem()));
		addSlot(new OutputSlot(inventory, MiniAlchemiterBlockEntity.OUTPUT, OUTPUT_X, OUTPUT_Y));
		addDataSlot(wildcardHolder);
		
		ContainerHelper.addPlayerInventorySlots(this::addSlot, 8, 84, playerInventory);
	}
	
	@Override
	protected Block getValidBlock()
	{
		return MSBlocks.MINI_ALCHEMITER.get();
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
			boolean result = false;
			
			if(slotNumber <= 1)
			{
				//if it's a machine slot
				result = moveItemStackTo(itemstackOrig, 2, allSlots, false);
			} else
			{
				//if it's an inventory slot with valid contents
				if(itemstackOrig.getItem() == MSBlocks.CRUXITE_DOWEL.get().asItem())
					result = moveItemStackTo(itemstackOrig, 0, 1, false);
			}
			
			if(!result)
				return ItemStack.EMPTY;
			
			if(!ItemStack.matches(itemstackOrig, slot.getItem()))
				slot.set(itemstackOrig);
		}
		
		return itemstack;
	}
	
	public GristType getWildcardType()
	{
		GristType type = GristTypes.REGISTRY.byId(wildcardHolder.get());
		if(type == null)
			type = GristTypes.BUILD.get();
		return type;
	}
}