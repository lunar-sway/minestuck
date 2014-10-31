package com.mraof.minestuck.inventory;

import java.util.ArrayList;
import java.util.Iterator;

import com.mraof.minestuck.Minestuck;
import com.mraof.minestuck.client.ClientProxy;
import com.mraof.minestuck.editmode.DeployList;
import com.mraof.minestuck.editmode.ServerEditHandler;
import com.mraof.minestuck.network.MinestuckChannelHandler;
import com.mraof.minestuck.network.MinestuckPacket;
import com.mraof.minestuck.network.MinestuckPacket.Type;
import com.mraof.minestuck.network.skaianet.SburbConnection;
import com.mraof.minestuck.network.skaianet.SessionHandler;
import com.mraof.minestuck.network.skaianet.SkaianetHandler;
import com.mraof.minestuck.util.Debug;
import com.mraof.minestuck.util.UsernameHandler;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.InventoryBasic;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

public class ContainerEditmode extends Container {
	
	private EntityPlayer player;
	public InventoryEditmode inventory = new InventoryEditmode();
	private boolean mode;
	
	public ContainerEditmode(boolean mode)
	{
		this.player = ClientProxy.getPlayer();
		this.mode = mode;
		addSlots();
	}
	
	public ContainerEditmode(EntityPlayer player, boolean mode)
	{
		this.player = player;
		this.mode = mode;
		addSlots();
		if(player instanceof EntityPlayerMP)
		{
			addCraftingToCrafters((EntityPlayerMP) player);
			updateInventory();
		}
		Debug.print("Slot 1:"+this.getSlot(1).getStack());
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
			slot.putStack(null);
			return stack;
		}
		if(slotIndex >= 0 && slotIndex < 14)
		{
			Slot slot = (Slot) this.inventorySlots.get(slotIndex);
			ItemStack stack = slot.getStack();
			if(stack != null)
				mergeItemStack(stack.copy(), 14, this.inventorySlots.size(), false);
		}
		return null;
	}
	
	private void addSlots()
	{
		
		for(int i = 0; i < 14; i++)
			addSlotToContainer(new Slot(inventory, i, 26+(i/2)*18, 16+(i%2)*18));
		
		for(int i = 0; i < 9; i++)
			addSlotToContainer(new Slot(player.inventory, i, 8+i*18, 74));
	}
	
	private void updateInventory()
	{
		ArrayList<ItemStack> itemList = new ArrayList<ItemStack>();
		SburbConnection c = SkaianetHandler.getClientConnection(ServerEditHandler.getData(player.getCommandSenderName()).getTarget());
		if(mode)
		{
			ArrayList<ItemStack> tools = new ArrayList<ItemStack>();
			//Fill list with tool items when implemented
			
			ArrayList<ItemStack> deployItems = DeployList.getItemList();
			Iterator<ItemStack> iter = deployItems.iterator();
			int playerTier = SessionHandler.availableTier(c.getClientName());
			while(iter.hasNext())
			{
				ItemStack stack = iter.next();
				if(DeployList.getTier(stack) > playerTier)
					iter.remove();
				else if(Minestuck.hardMode && DeployList.getSecondaryCost(stack) == null && c.givenItems()[DeployList.getOrdinal(stack)])
					iter.remove();
				if(stack.getItem().equals(Minestuck.captchaCard))
					stack.getTagCompound().setInteger("contentMeta", SessionHandler.getEntryItem(c.getClientName()));
			}
			
			for(int i = 0; i < Math.max(tools.size(), deployItems.size()); i++)
			{
				itemList.add(i >= tools.size()? null:tools.get(i));
				itemList.add(i >= deployItems.size()? null:deployItems.get(i));
			}
			
		}
		else
		{
			//Some sort of block-list
		}
		
		boolean changed = false;
		if(itemList.size() != inventory.items.size())
			changed = true;
		else for(int i = 0; i < itemList.size(); i++)
			if(!ItemStack.areItemStacksEqual(itemList.get(i), inventory.items.get(i)))
			{
				changed = true;
				break;
			}
		
		if(changed)
		{
			inventory.items = itemList;
			MinestuckChannelHandler.sendToPlayer(MinestuckPacket.makePacket(Type.INVENTORY, 0, itemList), player);
		}
		Debug.print("Inventory:"+(inventory.items.size() < 2?null:inventory.items.get(1)));
	}
	
}
