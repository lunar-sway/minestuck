package com.mraof.minestuck.inventory;

import com.mraof.minestuck.computer.editmode.DeployEntry;
import com.mraof.minestuck.computer.editmode.DeployList;
import com.mraof.minestuck.computer.editmode.EditData;
import com.mraof.minestuck.computer.editmode.ServerEditHandler;
import com.mraof.minestuck.network.EditmodeInventoryPacket;
import com.mraof.minestuck.network.MSPacketHandler;
import com.mraof.minestuck.skaianet.SburbConnection;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.MathHelper;

import java.util.ArrayList;
import java.util.List;

public class EditmodeContainer extends Container
{
	
	private final PlayerEntity player;
	private final Inventory inventory = new Inventory(14);
	private List<ItemStack> items  = new ArrayList<>();
	private int scroll;
	
	public EditmodeContainer(int windowId, PlayerInventory playerInventory)
	{
		super(MSContainerTypes.EDIT_MODE, windowId);
		this.player = playerInventory.player;
		addSlots();
		if(player instanceof ServerPlayerEntity)
		{
			updateInventory();
		}
	}
	
	public void updateScroll(boolean increase)
	{
		scroll += increase ? 1 : -1;
		scroll = MathHelper.clamp(scroll, 0, items.size()/2-7);
		
		sendPacket();
	}
	
	@Override
	public boolean stillValid(PlayerEntity player)
	{
		return player == this.player;
	}
	
	@Override
	public ItemStack quickMoveStack(PlayerEntity player, int slotIndex)
	{
		if(slotIndex >= 14 && slotIndex < this.slots.size())
		{
			Slot slot = this.slots.get(slotIndex);
			ItemStack stack = slot.getItem();
			slot.set(ItemStack.EMPTY);
			return stack;
		}
		if(slotIndex >= 0 && slotIndex < 14)
		{
			Slot slot = this.slots.get(slotIndex);
			ItemStack stack = slot.getItem();
			if(!stack.isEmpty())
				for(int i = 14; i < slots.size(); i++)
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
		
		for(int i = 0; i < 14; i++)
			addSlot(new InventorySlot(inventory, i, 26+(i/2)*18, 16+(i%2)*18));
		
		for(int i = 0; i < 9; i++)
			addSlot(new ToolbarSlot(player.inventory, i, 8+i*18, 74));
	}
	
	private void updateInventory()
	{
		EditData editData = ServerEditHandler.getData(player);
		if(editData == null)
			throw new IllegalStateException("Creating an editmode inventory container, but the player is not in editmode");
		List<ItemStack> itemList = new ArrayList<>();
		SburbConnection c = editData.getConnection();
		List<ItemStack> tools = DeployList.getEditmodeTools();
		//Fill list with harvestTool items when implemented
		
		List<DeployEntry> deployItems = DeployList.getItemList(player.getServer(), c);
		deployItems.removeIf(deployEntry -> deployEntry.getCurrentCost(c) == null);
		
		for(int i = 0; i < Math.max(tools.size(), deployItems.size()); i++)
		{
			itemList.add(i >= tools.size() ? ItemStack.EMPTY : tools.get(i));
			itemList.add(i >= deployItems.size() ? ItemStack.EMPTY : deployItems.get(i).getItemStack(c, player.level));
		}
		
		boolean changed = false;
		if(itemList.size() != this.items.size())
			changed = true;
		else for(int i = 0; i < itemList.size(); i++)
			if(!ItemStack.matches(itemList.get(i), this.items.get(i)))
			{
				changed = true;
				break;
			}
		
		if(changed)
		{
			this.items = itemList;
			sendPacket();
		}
	}
	
	private void sendPacket()
	{
		if(!(player instanceof ServerPlayerEntity))
			throw new IllegalStateException("Can't send update packet to player! Found player object "+player+".");
		
		ArrayList<ItemStack> itemList = new ArrayList<>();
		for(int i = 0; i < 14; i++)
		{
			itemList.add(this.items.size() <= i + scroll*2? ItemStack.EMPTY:this.items.get(i + scroll*2));
			this.inventory.setItem(i, itemList.get(i));
		}
		
		EditmodeInventoryPacket packet = EditmodeInventoryPacket.update(itemList, scroll > 0, scroll*2 + 14 < items.size());
		MSPacketHandler.sendToPlayer(packet, (ServerPlayerEntity) player);
	}
	
	public void receiveUpdatePacket(EditmodeInventoryPacket packet)
	{
		if(!player.level.isClientSide)
			throw new IllegalStateException("Should not receive update packet here for server-side container");
		for(int i = 0; i < packet.getInventory().size(); i++)
		{
			inventory.setItem(i, packet.getInventory().get(i));
		}
	}
	
	private static class ToolbarSlot extends Slot
	{
		
		ToolbarSlot(IInventory inventory, int index, int x, int y)
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
		
		InventorySlot(IInventory inventory, int index, int x, int y)
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
		{}
		
	}
}