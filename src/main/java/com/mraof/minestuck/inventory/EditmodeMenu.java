package com.mraof.minestuck.inventory;

import com.mraof.minestuck.computer.editmode.DeployEntry;
import com.mraof.minestuck.computer.editmode.DeployList;
import com.mraof.minestuck.computer.editmode.EditData;
import com.mraof.minestuck.computer.editmode.ServerEditHandler;
import com.mraof.minestuck.network.editmode.EditmodeInventoryPackets;
import com.mraof.minestuck.skaianet.SburbPlayerData;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.Mth;
import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.network.PacketDistributor;

import java.util.ArrayList;
import java.util.List;

public class EditmodeMenu extends AbstractContainerMenu
{
	
	private final Player player;
	private final Container inventory = new SimpleContainer(14);
	private List<ItemStack> items  = new ArrayList<>();
	private int scroll;
	
	public EditmodeMenu(int windowId, Inventory playerInventory)
	{
		super(MSMenuTypes.EDIT_MODE.get(), windowId);
		this.player = playerInventory.player;
		addSlots();
		if(player instanceof ServerPlayer)
		{
			updateInventory();
		}
	}
	
	public void updateScroll(boolean increase)
	{
		scroll += increase ? 1 : -1;
		scroll = Mth.clamp(scroll, 0, items.size()/2-7);
		
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
			addSlot(new ToolbarSlot(player.getInventory(), i, 8+i*18, 74));
	}
	
	private void updateInventory()
	{
		EditData editData = ServerEditHandler.getData(player);
		if(editData == null)
			throw new IllegalStateException("Creating an editmode inventory menu, but the player is not in editmode");
		List<ItemStack> itemList = new ArrayList<>();
		SburbPlayerData playerData = editData.sburbData();
		List<ItemStack> tools = DeployList.getEditmodeTools();
		//Fill list with harvestTool items when implemented
		
		List<DeployEntry> deployItems = DeployList.getItemList(player.getServer(), playerData, DeployList.EntryLists.DEPLOY);
		deployItems.removeIf(deployEntry -> deployEntry.getCurrentCost(playerData) == null);
		
		for(int i = 0; i < Math.max(tools.size(), deployItems.size()); i++)
		{
			itemList.add(i >= tools.size() ? ItemStack.EMPTY : tools.get(i));
			itemList.add(i >= deployItems.size() ? ItemStack.EMPTY : deployItems.get(i).getItemStack(playerData, player.level()));
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
		if(!(player instanceof ServerPlayer serverPlayer))
			throw new IllegalStateException("Can't send update packet to player! Found player object "+player+".");
		
		ArrayList<ItemStack> itemList = new ArrayList<>();
		for(int i = 0; i < 14; i++)
		{
			itemList.add(this.items.size() <= i + scroll*2? ItemStack.EMPTY:this.items.get(i + scroll*2));
			this.inventory.setItem(i, itemList.get(i));
		}
		
		CustomPacketPayload packet = new EditmodeInventoryPackets.Update(itemList, scroll > 0, scroll*2 + 14 < items.size());
		PacketDistributor.PLAYER.with(serverPlayer).send(packet);
	}
	
	public void receiveUpdatePacket(EditmodeInventoryPackets.Update packet)
	{
		if(!player.level().isClientSide)
			throw new IllegalStateException("Should not receive update packet here for server-side menu");
		for(int i = 0; i < packet.inventory().size(); i++)
		{
			inventory.setItem(i, packet.inventory().get(i));
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
		{}
		
	}
}
