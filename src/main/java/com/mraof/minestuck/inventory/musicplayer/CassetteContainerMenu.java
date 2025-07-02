package com.mraof.minestuck.inventory.musicplayer;

import com.mraof.minestuck.inventory.ContainerHelper;
import com.mraof.minestuck.inventory.MSMenuTypes;
import com.mraof.minestuck.util.MSTags;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.ItemContainerContents;

import javax.annotation.Nonnull;
import java.util.List;

public class CassetteContainerMenu extends AbstractContainerMenu
{
	private static final int CASSETTE_X = 80;
	private static final int CASSETTE_Y = 36;
	private static final int SLOT_INDEX = 0;
	private final ItemStack musicPlayer;
	
	/**
	 * Create a container menu that can store one cassette item
	 *
	 * @param windowId        The id of the container id
	 * @param playerInventory The player inventory that will be linked to the GUI
	 * @param musicPlayer     The musicPlayerItem that opens the GUI
	 * @param storedCassette  The cassette stored in musicPlayer
	 * @see AbstractContainerMenu
	 */
	
	public CassetteContainerMenu(int windowId, Inventory playerInventory, ItemStack musicPlayer, ItemStack storedCassette)
	{
		super(MSMenuTypes.CASSETTE_CONTAINER.get(), windowId);
		this.musicPlayer = musicPlayer;
		
		addSlot(new Slot(new SimpleContainer(storedCassette), SLOT_INDEX, CASSETTE_X, CASSETTE_Y)
		{
			@Override
			public boolean mayPlace(@Nonnull ItemStack stack)
			{
				return stack.is(MSTags.Items.CASSETTES);
			}
			
			@Override
			public void setChanged()
			{
				super.setChanged();
				musicPlayer.set(DataComponents.CONTAINER, ItemContainerContents.fromItems(List.of(getItem())));
			}
		});
		
		ContainerHelper.addPlayerInventorySlots(this::addSlot, 8, 84, playerInventory);
	}
	
	public CassetteContainerMenu(int windowId, Inventory playerInventory)
	{
		this(windowId, playerInventory, ItemStack.EMPTY, ItemStack.EMPTY);
	}
	
	@Override
	public boolean stillValid(Player playerIn)
	{
		ItemStack main = playerIn.getMainHandItem();
		ItemStack off = playerIn.getOffhandItem();
		return !main.isEmpty() && main == musicPlayer || !off.isEmpty() && off == musicPlayer;
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
			boolean result = false;
			
			if(slotNumber <= SLOT_INDEX)
			{
				//if slotNumber is a cassette slot
				result = moveItemStackTo(itemstackOrig, 1, allSlots, false);
			} else
			{
				//if slotNumber is an inventory slot with valid contents
				if(itemstackOrig.is(MSTags.Items.CASSETTES))
					result = moveItemStackTo(itemstackOrig, 0, 1, false);
			}
			
			if(!result)
				return ItemStack.EMPTY;
			
			slot.setChanged();
		}
		
		return itemstack;
	}
}
