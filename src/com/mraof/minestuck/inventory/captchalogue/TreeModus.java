package com.mraof.minestuck.inventory.captchalogue;

import com.mraof.minestuck.MinestuckConfig;
import com.mraof.minestuck.advancements.MSCriteriaTriggers;
import com.mraof.minestuck.item.MSItems;
import com.mraof.minestuck.item.crafting.alchemy.AlchemyRecipes;
import com.mraof.minestuck.network.CaptchaDeckPacket;
import com.mraof.minestuck.network.MSPacketHandler;
import com.mraof.minestuck.world.storage.PlayerSavedData;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.LogicalSide;

import java.util.ArrayList;
import java.util.List;

public class TreeModus extends Modus
{
	
	public TreeNode node;
	public int size;
	public boolean autoBalance = true;
	
	public TreeModus(ModusType<? extends TreeModus> type, PlayerSavedData savedData, LogicalSide side)
	{
		super(type, savedData, side);
	}
	
	@Override
	public void initModus(ServerPlayerEntity player, NonNullList<ItemStack> prev, int size)
	{
		this.size = size;
		node = null; //TODO Handle potential prev list instead
	}
	
	@Override
	public void readFromNBT(CompoundNBT nbt)
	{
		size = nbt.getInt("size");
		autoBalance = nbt.getBoolean("auto_balance");
		node = readNode(nbt, 0, 0);
		if(side == LogicalSide.SERVER)
			autoBalance();
	}
	
	private TreeNode readNode(CompoundNBT nbt, int currentIndex, int level)
	{
		if(nbt.contains("node"+currentIndex))
		{
			ItemStack stack = ItemStack.read(nbt.getCompound("node"+currentIndex));
			if(stack.isEmpty()) return null;	//Should not happen
			TreeNode node = new TreeNode(stack);
			node.node1 = readNode(nbt, currentIndex + (int) Math.pow(2, level), level + 1);
			node.node2 = readNode(nbt, currentIndex + (int) Math.pow(2, level + 1), level + 1);
			
			return node;
		} else return null;
	}
	
	private void saveNode(CompoundNBT nbt, TreeNode node, int currentIndex, int level)
	{
		nbt.put("node"+currentIndex, node.stack.write(new CompoundNBT()));
		if(node.node1 != null)
			saveNode(nbt, node.node1, currentIndex + (int) Math.pow(2, level), level + 1);
		if(node.node2 != null)
			saveNode(nbt, node.node2, currentIndex + (int) Math.pow(2, level + 1), level + 1);
	}
	
	@Override
	public CompoundNBT writeToNBT(CompoundNBT nbt)
	{
		nbt.putInt("size", size);
		nbt.putBoolean("auto_balance", autoBalance);
		if(node != null)
			saveNode(nbt, node, 0, 0);
		
		return nbt;
	}
	
	@Override
	public boolean putItemStack(ServerPlayerEntity player, ItemStack item)
	{
		int currentSize = node == null ? 0 : node.getSize();
		if(item.isEmpty() || currentSize >= size)
			return false;
		if(node == null)
			node = new TreeNode(item);
		else node.addNode(new TreeNode(item));
		markDirty();
		autoBalance();
		return true;
	}
	
	@Override
	public NonNullList<ItemStack> getItems()
	{
		if(node == null)
			return NonNullList.withSize(size, ItemStack.EMPTY);
		NonNullList<ItemStack> list = node.getItems();
		while(list.size() < size)
			list.add(ItemStack.EMPTY);
		return list;
	}
	
	@Override
	public int getNonEmptyCards()
	{
		return node.getSize();
	}
	
	@Override
	public boolean increaseSize(ServerPlayerEntity player)
	{
		if(MinestuckConfig.modusMaxSize.get() > 0 && size >= MinestuckConfig.modusMaxSize.get())
			return false;
		
		size++;
		markDirty();
		return true;
	}
	
	@Override
	public ItemStack getItem(ServerPlayerEntity player, int id, boolean asCard)
	{
		if(id == CaptchaDeckHandler.EMPTY_CARD)
		{
			if(size <= 0 && (node == null || size > node.getSize()))
				return ItemStack.EMPTY;
			size--;
			markDirty();
			return new ItemStack(MSItems.CAPTCHA_CARD);
		}
		if(node == null)
			return ItemStack.EMPTY;
		if(id == CaptchaDeckHandler.EMPTY_SYLLADEX)
		{
			ArrayList<ItemStack> list = node.removeItems(0);
			node = null;
			markDirty();
			for(ItemStack stack : list)
				CaptchaDeckHandler.launchAnyItem(player, stack);
			return ItemStack.EMPTY;
		}
		
		if(id == 0)
			MSCriteriaTriggers.TREE_MODUS_ROOT.trigger(player, node.getSize());
		
		ArrayList<ItemStack> list = node.removeItems(id);
		markDirty();
		if(list.isEmpty())
			return ItemStack.EMPTY;
		ItemStack stack = list.get(0);
		for(int i = 1; i < list.size(); i++)
			CaptchaDeckHandler.launchAnyItem(player, list.get(i));
		if(asCard)
		{
			size--;
			markDirty();
			stack = AlchemyRecipes.createCard(stack, false);
		}
		if(id == 0)
			node = null;
		autoBalance();
		return stack;
	}
	
	@Override
	public boolean canSwitchFrom(Modus modus)
	{
		return false;
	}
	
	@Override
	public int getSize()
	{
		return size;
	}
	
	@Override
	public void setValue(ServerPlayerEntity player, byte type, int value)
	{
		if(autoBalance != value > 0)
		{
			autoBalance = value > 0;
			markDirty();
			if(autoBalance)
			{
				TreeNode node = this.node;
				autoBalance();
				if(node != this.node)
					MSPacketHandler.sendToPlayer(CaptchaDeckPacket.data(CaptchaDeckHandler.writeToNBT(this)), player);
			}
		}
	}
	
	protected void autoBalance()
	{
		if(!autoBalance && MinestuckConfig.treeModusSetting != 1 || MinestuckConfig.treeModusSetting == 2)
			return;
		
		int minDepth = getDepth(node, true);
		int maxDepth = getDepth(node, false);
		
		if(minDepth + 1 < maxDepth)
		{
			List<ItemStack> list = node.getItems();
			
			node = createNode(list);
			markDirty();
		}
	}
	
	protected TreeNode createNode(List<ItemStack> list)	//Used only by auto balance
	{
		if(list.isEmpty())
			return null;
		int i = list.size()/2;
		TreeNode node = new TreeNode(list.get(i));
		node.node1 = createNode(list.subList(0, i));
		node.node2 = createNode(list.subList(i+1, list.size()));
		return node;
	}
	
	protected int getDepth(TreeNode node, boolean min)
	{
		if(node == null)
			return 0;
		if(min)
			return Math.min(getDepth(node.node1, true), getDepth(node.node2, true)) + 1;
		else return Math.max(getDepth(node.node1, false), getDepth(node.node2, false)) + 1;
	}
	
	public static class TreeNode
	{
		public final ItemStack stack;
		public TreeNode node1;
		public TreeNode node2;
		
		protected TreeNode(ItemStack stack)
		{
			this.stack = stack;
		}
		
		public int getSize()
		{
			int size = 1;
			if(node1 != null)
				size += node1.getSize();
			if(node2 != null)
				size += node2.getSize();
			return size;
		}
		
		public void addNode(TreeNode node)
		{
			if(this.stack.getItem() == node.stack.getItem() && ItemStack.areItemStackTagsEqual(this.stack, node.stack)
				&& this.stack.getCount() + node.stack.getCount() <= this.stack.getMaxStackSize())
			{
				this.stack.grow(node.stack.getCount());
			}
			else
			{
				int compare = this.itemToString().compareTo(node.itemToString());
				if(compare >= 0)
				{
					if(node1 != null)
						node1.addNode(node);
					else node1 = node;
				}
				else
				{
					if(node2 != null)
						node2.addNode(node);
					else node2 = node;
				}
			}
		}
		
		private String itemToString()
		{
			ResourceLocation name = stack.getItem().getRegistryName();
			if(name == null)
				throw new IllegalStateException("Item "+stack.getItem()+" does not have a registry name, but ended up in a tree modus!");
			return name.getPath()+":"+name.getNamespace();	//Don't want the items to be sorted mod-wise.
		}
		
		public ArrayList<ItemStack> removeItems(int index)
		{
			if(index == 0)
			{
				ArrayList<ItemStack> list = new ArrayList<>();
				list.add(this.stack);
				if(node1 != null)
					list.addAll(node1.getItems());
				if(node2 != null)
					list.addAll(node2.getItems());
				return list;
			}
			index -= 1;
			if(index % 2 == 0)
			{
				if(node1 != null)
				{
					ArrayList<ItemStack> list = node1.removeItems(index/2);
					if(index/2 == 0)
						node1 = null;
					return list;
				} else return new ArrayList<>();
			}
			else
			{
				if(node2 != null)
				{
					ArrayList<ItemStack> list = node2.removeItems(index/2);
					if(index/2 == 0)
						node2 = null;
					return list;
				} else return new ArrayList<>();
			}
		}
		
		public NonNullList<ItemStack> getItems()
		{	//TODO Maybe something more efficient than repeatedly creating and discarding lists?
			NonNullList<ItemStack> list = NonNullList.create();
			if(node1 != null)
				list.addAll(node1.getItems());
			list.add(stack);
			if(node2 != null)
				list.addAll(node2.getItems());
			return list;
		}
		
	}
}