package com.mraof.minestuck.inventory;

import com.mraof.minestuck.editmode.DeployList;
import com.mraof.minestuck.editmode.ServerEditHandler;
import com.mraof.minestuck.network.EditmodeInventoryPacket;
import com.mraof.minestuck.network.MSPacketHandler;
import com.mraof.minestuck.network.skaianet.SburbConnection;
import com.mraof.minestuck.network.skaianet.SkaianetHandler;
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
	
	private PlayerEntity player;
	public Inventory inventory = new Inventory(14);
	public ArrayList<ItemStack> items  = new ArrayList<>();
	private int scroll;
	public static int clientScroll;
	
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
	public boolean canInteractWith(PlayerEntity player)
	{
		return player == this.player;
	}
	
	@Override
	public ItemStack transferStackInSlot(PlayerEntity player, int slotIndex)
	{
		if(slotIndex >= 14 && slotIndex < this.inventorySlots.size())
		{
			Slot slot = this.inventorySlots.get(slotIndex);
			ItemStack stack = slot.getStack();
			slot.putStack(ItemStack.EMPTY);
			return stack;
		}
		if(slotIndex >= 0 && slotIndex < 14)
		{
			Slot slot = this.inventorySlots.get(slotIndex);
			ItemStack stack = slot.getStack();
			if(!stack.isEmpty())
				for(int i = 14; i < inventorySlots.size(); i++)
					if(!getSlot(i).getHasStack())
					{
						getSlot(i).putStack(stack);
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
		ArrayList<ItemStack> itemList = new ArrayList<>();
		SburbConnection c = SkaianetHandler.get(player.world).getActiveConnection(ServerEditHandler.getData(player).getTarget());
		ArrayList<ItemStack> tools = new ArrayList<>();
		//Fill list with harvestTool items when implemented
		
		List<DeployList.DeployEntry> deployItems = DeployList.getItemList(player.getServer(), c);
		deployItems.removeIf(deployEntry -> c.givenItems()[DeployList.getOrdinal(deployEntry.getName())] &&
				deployEntry.getSecondaryGristCost(c) == null);
		
		for(int i = 0; i < Math.max(tools.size(), deployItems.size()); i++)
		{
			itemList.add(i >= tools.size() ? ItemStack.EMPTY : tools.get(i));
			itemList.add(i >= deployItems.size() ? ItemStack.EMPTY : deployItems.get(i).getItemStack(c, player.world));
		}
		
		boolean changed = false;
		if(itemList.size() != this.items.size())
			changed = true;
		else for(int i = 0; i < itemList.size(); i++)
			if(!ItemStack.areItemStacksEqual(itemList.get(i), this.items.get(i)))
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
			this.inventory.setInventorySlotContents(i, itemList.get(i));
		}
		
		EditmodeInventoryPacket packet = EditmodeInventoryPacket.update(itemList, scroll > 0, scroll*2 + 14 < items.size());
		MSPacketHandler.sendToPlayer(packet, (ServerPlayerEntity) player);
	}
	
	private static class ToolbarSlot extends Slot
	{
		
		public ToolbarSlot(IInventory inventory, int index, int x, int y)
		{
			super(inventory, index, x, y);
		}
		
		@Override
		public int getSlotStackLimit()
		{
			return 1;
		}
		
	}
	
	private static class InventorySlot extends ToolbarSlot
	{
		
		public InventorySlot(IInventory inventory, int index, int x, int y)
		{
			super(inventory, index, x, y);
		}
		
		@Override
		public ItemStack decrStackSize(int index)
		{
			return this.getStack();
		}
		
		@Override
		public ItemStack getStack()
		{
			return this.inventory.getStackInSlot(this.slotNumber).copy();
		}
		
		@Override
		public void putStack(ItemStack stack)
		{}
		
	}
}