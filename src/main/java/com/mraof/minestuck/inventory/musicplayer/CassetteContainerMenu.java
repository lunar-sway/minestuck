package com.mraof.minestuck.inventory.musicplayer;

import com.mraof.minestuck.inventory.ContainerHelper;
import com.mraof.minestuck.inventory.MSMenuTypes;
import com.mraof.minestuck.util.MSTags;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.items.IItemHandler;
import net.neoforged.neoforge.items.ItemStackHandler;
import net.neoforged.neoforge.items.SlotItemHandler;

import javax.annotation.Nonnull;

public class CassetteContainerMenu extends AbstractContainerMenu
{
	private static final int CASSETTE_X = 80;
	private static final int CASSETTE_Y = 36;
	private final ItemStack musicPlayer;
	
	/**
	 * Create a container menu that can store one cassette item
	 *
	 * @param windowId         The id of the container id
	 * @param playerInventory  The player inventory that will be linked to the GUI
	 * @param itemStackHandler The itemStackHandler referring to the cassette stored in the item
	 * @param musicPlayer      The musicPlayerItem that open the GUI
	 * @see AbstractContainerMenu
	 */
	
	public CassetteContainerMenu(int windowId, Inventory playerInventory, IItemHandler itemStackHandler, ItemStack musicPlayer)
	{
		super(MSMenuTypes.CASSETTE_CONTAINER.get(), windowId);
		this.musicPlayer = musicPlayer;
		addSlot(new SlotItemHandler(itemStackHandler, 0, CASSETTE_X, CASSETTE_Y){
			@Override
			public boolean mayPlace(@Nonnull ItemStack stack)
			{
				return stack.is(MSTags.Items.CASSETTES);
			}
		});
		
		ContainerHelper.addPlayerInventorySlots(this::addSlot, 8, 84, playerInventory);
	}
	
	public CassetteContainerMenu(int windowId, Inventory playerInventory)
	{
		this(windowId, playerInventory, new ItemStackHandler(1), ItemStack.EMPTY);
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
			
			if(slotNumber <= 0)
			{
				//if it's a cassette slot
				result = moveItemStackTo(itemstackOrig, 1, allSlots, false);
			} else
			{
				//if it's an inventory slot with valid contents
				if(itemstackOrig.is(MSTags.Items.CASSETTES))
					result = moveItemStackTo(itemstackOrig, 0, 1, false);
			}
			
			if(!result)
				return ItemStack.EMPTY;
			
			if(!itemstackOrig.isEmpty())
				slot.setChanged();
		}
		
		return itemstack;
	}
}
