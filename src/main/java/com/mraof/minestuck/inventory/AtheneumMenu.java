package com.mraof.minestuck.inventory;

import com.mraof.minestuck.alchemy.AlchemyHelper;
import com.mraof.minestuck.computer.editmode.DeployEntry;
import com.mraof.minestuck.computer.editmode.DeployList;
import com.mraof.minestuck.computer.editmode.EditData;
import com.mraof.minestuck.computer.editmode.ServerEditHandler;
import com.mraof.minestuck.network.editmode.AtheneumPackets;
import com.mraof.minestuck.skaianet.SburbPlayerData;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.Mth;
import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ClickType;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.network.PacketDistributor;

import java.util.ArrayList;
import java.util.List;

public class AtheneumMenu extends AbstractContainerMenu
{
	private final int INVENTORY_SIZE = 21;
	private final int INVENTORY_COLUMNS = 7;
	private final Player player;
	private final Container inventory = new SimpleContainer(INVENTORY_SIZE);
	private List<ItemStack> items = new ArrayList<>();
	private int scroll;
	
	public AtheneumMenu(int windowId, Inventory playerInventory)
	{
		super(MSMenuTypes.ATHENEUM.get(), windowId);
		this.player = playerInventory.player;
		addSlots();
		if(player instanceof ServerPlayer)
		{
			updateInventory();
		}
	}
	
	public void updateScroll(boolean scrollUp)
	{
		scroll += scrollUp ? -1 : 1;
		scroll = Mth.clamp(scroll, 0, items.size() / INVENTORY_COLUMNS);
		
		sendPacket();
	}
	
	@Override
	public boolean stillValid(Player player)
	{
		return player == this.player;
	}
	
	@Override
	public ItemStack quickMoveStack(Player player, int slotIndex)
	{
		if(slotIndex >= INVENTORY_SIZE && slotIndex < this.slots.size())
		{
			Slot slot = this.slots.get(slotIndex);
			ItemStack stack = slot.getItem();
			slot.set(ItemStack.EMPTY);
			return stack;
		}
		if(slotIndex >= 0 && slotIndex < INVENTORY_SIZE)
		{
			Slot slot = this.slots.get(slotIndex);
			ItemStack stack = slot.getItem();
			if(!stack.isEmpty())
				for(int i = INVENTORY_SIZE; i < slots.size(); i++)
					if(!getSlot(i).hasItem())
					{
						getSlot(i).set(stack);
						return stack;
					}
		}
		return ItemStack.EMPTY;
	}
	
	private void addSlots()
	{
		
		for(int i = 0; i < INVENTORY_SIZE; i++)
			addSlot(new InventorySlot(inventory, i, 26 + (i % INVENTORY_COLUMNS) * 18, 14 + (i / INVENTORY_COLUMNS) * 18));
		
		for(int i = 0; i < 9; i++)
			addSlot(new ToolbarSlot(player.getInventory(), i, 8 + i * 18, 74));
	}
	
	private void updateInventory()
	{
		EditData editData = ServerEditHandler.getData(player);
		if(editData == null)
			throw new IllegalStateException("Creating an editmode inventory menu, but the player is not in editmode");
		List<ItemStack> itemList = new ArrayList<>();
		SburbPlayerData playerData = editData.sburbData();
		
		//Gets items from the atheneum category of the DeployList
		List<DeployEntry> atheneumItems = DeployList.getItemList(player.getServer(), playerData, DeployList.EntryLists.ATHENEUM);
		atheneumItems.removeIf(deployEntry -> deployEntry.getCurrentCost(playerData) == null);
		
		//if each stack is not empty, put it in the item list.
		for(DeployEntry atheneumItem : atheneumItems)
		{
			if(!atheneumItem.getItemStack(playerData, player.level()).isEmpty())
				itemList.add(atheneumItem.getItemStack(playerData, player.level()));
		}
		
		
		//Check whether all items match itemList
		boolean changed = false;
		if(itemList.size() != this.items.size())
			changed = true;
		else for(int i = 0; i < itemList.size(); i++)
			if(!ItemStack.matches(itemList.get(i), this.items.get(i)))
			{
				changed = true;
				break;
			}
		
		//if the item list has changed, send the packet to update the container's inventory.
		if(changed)
		{
			this.items = itemList;
			sendPacket();
		}
	}
	
	private void sendPacket()
	{
		if(!(player instanceof ServerPlayer serverPlayer))
			throw new IllegalStateException("Can't send update packet to player! Found player object " + player + ".");
		
		ArrayList<ItemStack> itemList = new ArrayList<>();
		for(int i = 0; i < INVENTORY_SIZE; i++)
		{
			itemList.add(this.items.size() <= i + (scroll * INVENTORY_COLUMNS) ? ItemStack.EMPTY : this.items.get(i + (scroll * INVENTORY_COLUMNS)));
			this.inventory.setItem(i, itemList.get(i));
		}
		
		AtheneumPackets.Update packet = new AtheneumPackets.Update(scroll > 0, INVENTORY_SIZE + (scroll * INVENTORY_COLUMNS) < items.size(), itemList);
		PacketDistributor.PLAYER.with(serverPlayer).send(packet);
	}
	
	public void receiveUpdatePacket(AtheneumPackets.Update packet)
	{
		if(!player.level().isClientSide)
			throw new IllegalStateException("Should not receive update packet here for server-side menu");
		for(int i = 0; i < packet.getInventory().size(); i++)
		{
			inventory.setItem(i, packet.getInventory().get(i));
		}
	}
	
	@Override
	public void clicked(int pSlotId, int pButton, ClickType pClickType, Player pPlayer)
	{
		super.clicked(pSlotId, pButton, pClickType, pPlayer);
		
		if(pClickType == ClickType.PICKUP && pButton == 1 && pSlotId >= 0)
		{
			this.setCarried(AlchemyHelper.createPunchedCard(this.slots.get(pSlotId).getItem()));
		}
		
	}
	
	private static class ToolbarSlot extends Slot
	{
		
		ToolbarSlot(Container inventory, int index, int x, int y)
		{
			super(inventory, index, x, y);
		}
		
		@Override
		public int getMaxStackSize()
		{
			return 1;
		}
		
	}
	
	private static class InventorySlot extends ToolbarSlot
	{
		
		InventorySlot(Container inventory, int index, int x, int y)
		{
			super(inventory, index, x, y);
		}
		
		@Override
		public ItemStack remove(int index)
		{
			return this.getItem();
		}
		
		@Override
		public ItemStack getItem()
		{
			return this.container.getItem(this.index).copy();
		}
		
		@Override
		public void set(ItemStack stack)
		{
		}
		
	}
}
