package com.mraof.minestuck.inventory;

import com.mraof.minestuck.block.MinestuckBlocks;
import com.mraof.minestuck.editmode.DeployList;
import com.mraof.minestuck.editmode.ServerEditHandler;
import com.mraof.minestuck.item.ItemCruxiteArtifact;
import com.mraof.minestuck.item.MinestuckItems;
import com.mraof.minestuck.network.MinestuckChannelHandler;
import com.mraof.minestuck.network.MinestuckPacket;
import com.mraof.minestuck.network.MinestuckPacket.Type;
import com.mraof.minestuck.network.skaianet.SburbConnection;
import com.mraof.minestuck.network.skaianet.SburbHandler;
import com.mraof.minestuck.network.skaianet.SkaianetHandler;
import com.mraof.minestuck.util.AlchemyRecipeHandler;
import com.mraof.minestuck.util.MinestuckPlayerData;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.InventoryBasic;
import net.minecraft.inventory.Slot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class ContainerEditmode extends Container
{
	
	private EntityPlayer player;
	public InventoryBasic inventory = new InventoryBasic("InventoryEditmode", false, 14);
	public ArrayList<ItemStack> items  = new ArrayList<ItemStack>();
	public int scroll;
	public static int clientScroll;
	
	public ContainerEditmode(EntityPlayer player)
	{
		this.player = player;
		addSlots();
		if(player instanceof EntityPlayerMP)
		{
			updateInventory();
		}
	}
	
	@Override
	public boolean canInteractWith(EntityPlayer player)
	{
		return player == this.player;
	}
	
	@Override
	public ItemStack transferStackInSlot(EntityPlayer player, int slotIndex)
	{
		if(slotIndex >= 14 && slotIndex < this.inventorySlots.size())
		{
			Slot slot = (Slot) this.inventorySlots.get(slotIndex);
			ItemStack stack = slot.getStack();
			slot.putStack(ItemStack.EMPTY);
			return stack;
		}
		if(slotIndex >= 0 && slotIndex < 14)
		{
			Slot slot = (Slot) this.inventorySlots.get(slotIndex);
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
			addSlotToContainer(new InventorySlot(inventory, i, 26+(i/2)*18, 16+(i%2)*18));
		
		for(int i = 0; i < 9; i++)
			addSlotToContainer(new ToolbarSlot(player.inventory, i, 8+i*18, 74));
	}
	
	private void updateInventory()
	{
		ArrayList<ItemStack> itemList = new ArrayList<ItemStack>();
		SburbConnection c = SkaianetHandler.getClientConnection(ServerEditHandler.getData(player).getTarget());
		ArrayList<ItemStack> tools = new ArrayList<ItemStack>();
		//Fill list with tool items when implemented
		
		List<DeployList.DeployEntry> deployItems = DeployList.getItemList(c);
		deployItems.removeIf(deployEntry -> c.givenItems()[DeployList.getOrdinal(deployEntry.getName())] &&
				deployEntry.getSecondaryGristCost(c) == null);
		
		for(int i = 0; i < Math.max(tools.size(), deployItems.size()); i++)
		{
			itemList.add(i >= tools.size() ? ItemStack.EMPTY : tools.get(i));
			itemList.add(i >= deployItems.size() ? ItemStack.EMPTY : deployItems.get(i).getItemStack(c));
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
	
	public void sendPacket()
	{
		ArrayList<ItemStack> itemList = new ArrayList<ItemStack>();
		for(int i = 0; i < 14; i++)
		{
			itemList.add(this.items.size() <= i + scroll*2? ItemStack.EMPTY:this.items.get(i + scroll*2));
			this.inventory.setInventorySlotContents(i, itemList.get(i));
			this.inventoryItemStacks.set(i, itemList.get(i));
		}
		
		MinestuckChannelHandler.sendToPlayer(MinestuckPacket.makePacket(Type.INVENTORY, 0, itemList, scroll > 0, scroll*2 + 14 < items.size()), player);
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