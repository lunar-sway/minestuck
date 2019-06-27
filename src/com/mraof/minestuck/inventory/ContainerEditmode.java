package com.mraof.minestuck.inventory;

import com.mraof.minestuck.editmode.DeployList;
import com.mraof.minestuck.editmode.ServerEditHandler;
import com.mraof.minestuck.network.EditmodeInventoryPacket;
import com.mraof.minestuck.network.MinestuckPacketHandler;
import com.mraof.minestuck.network.skaianet.SburbConnection;
import com.mraof.minestuck.network.skaianet.SkaianetHandler;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.InventoryBasic;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.TextComponentString;

import java.util.ArrayList;
import java.util.List;

public class ContainerEditmode extends Container
{
	
	private EntityPlayer player;
	public InventoryBasic inventory = new InventoryBasic(new TextComponentString("InventoryEditmode"), 14);
	public ArrayList<ItemStack> items  = new ArrayList<ItemStack>();
	private int scroll;
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
	
	public void updateScroll(boolean increase)
	{
		scroll += increase ? 1 : -1;
		scroll = MathHelper.clamp(scroll, 0, items.size()/2-7);
		
		sendPacket();
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
	
	private void sendPacket()
	{
		if(!(player instanceof EntityPlayerMP))
			throw new IllegalStateException("Can't send update packet to player! Found player object "+player+".");
		
		ArrayList<ItemStack> itemList = new ArrayList<ItemStack>();
		for(int i = 0; i < 14; i++)
		{
			itemList.add(this.items.size() <= i + scroll*2? ItemStack.EMPTY:this.items.get(i + scroll*2));
			this.inventory.setInventorySlotContents(i, itemList.get(i));
			this.inventoryItemStacks.set(i, itemList.get(i));
		}
		
		EditmodeInventoryPacket packet = EditmodeInventoryPacket.update(itemList, scroll > 0, scroll*2 + 14 < items.size());
		MinestuckPacketHandler.sendToPlayer(packet, (EntityPlayerMP) player);
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