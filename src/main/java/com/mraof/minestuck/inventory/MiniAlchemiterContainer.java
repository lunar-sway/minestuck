package com.mraof.minestuck.inventory;

import com.mraof.minestuck.block.MSBlocks;
import com.mraof.minestuck.inventory.slot.InputSlot;
import com.mraof.minestuck.inventory.slot.OutputSlot;
import com.mraof.minestuck.item.crafting.alchemy.GristType;
import com.mraof.minestuck.item.crafting.alchemy.GristTypes;
import com.mraof.minestuck.tileentity.machine.MiniAlchemiterTileEntity;
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
import net.minecraft.util.IntReferenceHolder;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import net.minecraftforge.registries.ForgeRegistry;

import javax.annotation.Nonnull;

public class MiniAlchemiterContainer extends MachineContainer
{
	
	private static final int INPUT_X = 27;
	private static final int INPUT_Y = 20;
	private static final int OUTPUT_X = 135;
	private static final int OUTPUT_Y = 20;
	
	private final IntReferenceHolder wildcardHolder;
	
	public MiniAlchemiterContainer(int windowId, PlayerInventory playerInventory, PacketBuffer buffer)
	{
		this(MSContainerTypes.MINI_ALCHEMITER, windowId, playerInventory, new ItemStackHandler(2), new IntArray(3), IntReferenceHolder.standalone(), IWorldPosCallable.NULL, buffer.readBlockPos());
	}
	
	public MiniAlchemiterContainer(int windowId, PlayerInventory playerInventory, IItemHandler inventory, IIntArray parameters, IntReferenceHolder wildcardHolder, IWorldPosCallable position, BlockPos machinePos)
	{
		this(MSContainerTypes.MINI_ALCHEMITER, windowId, playerInventory, inventory, parameters, wildcardHolder, position, machinePos);
	}
	
	public MiniAlchemiterContainer(ContainerType<? extends MiniAlchemiterContainer> type, int windowId, PlayerInventory playerInventory, IItemHandler inventory, IIntArray parameters, IntReferenceHolder wildcardHolder, IWorldPosCallable position, BlockPos machinePos)
	{
		super(type, windowId, parameters, position, machinePos);
		
		assertItemHandlerSize(inventory, 2);
		this.wildcardHolder = wildcardHolder;
		
		addSlot(new InputSlot(inventory, MiniAlchemiterTileEntity.INPUT, INPUT_X, INPUT_Y, MSBlocks.CRUXITE_DOWEL.asItem()));
		addSlot(new OutputSlot(inventory, MiniAlchemiterTileEntity.OUTPUT, OUTPUT_X, OUTPUT_Y));
		addDataSlot(wildcardHolder);
		
		bindPlayerInventory(playerInventory);
	}
	
	@Override
	protected Block getValidBlock()
	{
		return MSBlocks.MINI_ALCHEMITER;
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
		
		if(slot != null && slot.hasItem())
		{
			ItemStack itemstackOrig = slot.getItem();
			itemstack = itemstackOrig.copy();
			boolean result = false;
			
			if(slotNumber <= 1)
			{
				//if it's a machine slot
				result = moveItemStackTo(itemstackOrig, 2, allSlots, false);
			} else if(slotNumber > 1)
			{
				//if it's an inventory slot with valid contents
				if(itemstackOrig.getItem() == MSBlocks.CRUXITE_DOWEL.asItem())
					result = moveItemStackTo(itemstackOrig, 0, 1, false);
			}
			
			if(!result)
				return ItemStack.EMPTY;
			
			if(!itemstackOrig.isEmpty())
				slot.setChanged();
		}
		
		return itemstack;
	}
	
	public GristType getWildcardType()
	{
		GristType type = ((ForgeRegistry<GristType>) GristTypes.getRegistry()).getValue(wildcardHolder.get());
		if(type == null)
			type = GristTypes.BUILD.get();
		return type;
	}
}