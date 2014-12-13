package com.mraof.minestuck.inventory.captchalouge;

import java.util.ArrayList;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import com.mraof.minestuck.Minestuck;
import com.mraof.minestuck.client.gui.captchalouge.SylladexGuiHandler;
import com.mraof.minestuck.client.gui.captchalouge.TreeGuiHandler;
import com.mraof.minestuck.inventory.captchalouge.CaptchaDeckHandler.ModusType;
import com.mraof.minestuck.util.AlchemyRecipeHandler;

public class TreeModus extends Modus
{
	
	public TreeNode node;
	public int size;
	@SideOnly(Side.CLIENT)
	protected TreeGuiHandler guiHandler;
	
	@Override
	public void initModus(ItemStack[] prev)
	{
		size = Minestuck.defaultModusSize;
	}
	
	@Override
	public void readFromNBT(NBTTagCompound nbt)
	{
		size = nbt.getInteger("size");
		node = readNode(nbt, 0, 0);
	}
	
	private TreeNode readNode(NBTTagCompound nbt, int currentIndex, int level)
	{
		if(nbt.hasKey("node"+currentIndex))
		{
			ItemStack stack = ItemStack.loadItemStackFromNBT(nbt.getCompoundTag("node"+currentIndex));
			TreeNode node = new TreeNode(stack);
			node.node1 = readNode(nbt, currentIndex + (int) Math.pow(2, level), level + 1);
			node.node2 = readNode(nbt, currentIndex + (int) Math.pow(2, level + 1), level + 1);
			
			return node;
		} else return null;
	}
	
	private void saveNode(NBTTagCompound nbt, TreeNode node, int currentIndex, int level)
	{
		nbt.setTag("node"+currentIndex, node.stack.writeToNBT(new NBTTagCompound()));
		if(node.node1 != null)
			saveNode(nbt, node.node1, currentIndex + (int) Math.pow(2, level), level + 1);
		if(node.node2 != null)
			saveNode(nbt, node.node2, currentIndex + (int) Math.pow(2, level + 1), level + 1);
	}
	
	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound nbt)
	{
		nbt.setInteger("size", size);
		if(node != null)
			saveNode(nbt, node, 0, 0);
		
		return nbt;
	}
	
	@Override
	public boolean putItemStack(ItemStack item)
	{
		int currentSize = node == null ? 0 : node.getSize();
		if(item == null || currentSize >= size && !Minestuck.infiniteTreeModus)
			return false;
		if(node == null)
			node = new TreeNode(item);
		else node.addNode(new TreeNode(item));
		return true;
	}
	
	@Override
	public ItemStack[] getItems()
	{
		if(node == null)
			return new ItemStack[size];
		ArrayList<ItemStack> list = node.getItems();
		while(list.size() < size)
			list.add(null);
		return (ItemStack[]) list.toArray();
	}
	
	@Override
	public boolean increaseSize()
	{
		if(Minestuck.modusMaxSize > 0 && size >= Minestuck.modusMaxSize)
			return false;
		
		size++;
		return true;
	}
	
	@Override
	public ItemStack getItem(int id, boolean asCard)
	{
		if(id == CaptchaDeckHandler.EMPTY_CARD)
		{
			if(size <= 0)
				return null;
			size--;
			return new ItemStack(Minestuck.captchaCard);
		}
		if(node == null)
			return null;
		if(id == CaptchaDeckHandler.EMPTY_SYLLADEX)
		{
			ArrayList<ItemStack> list = node.removeItems(0);
			node = null;
			for(ItemStack stack : list)
				CaptchaDeckHandler.launchAnyItem(player, stack);
			return null;
		}
		
		ArrayList<ItemStack> list = node.removeItems(id);
		if(list.isEmpty())
			return null;
		ItemStack stack = list.get(0);
		for(int i = 1; i < list.size(); i++)
			CaptchaDeckHandler.launchAnyItem(player, list.get(i));
		if(asCard)
		{
			stack = AlchemyRecipeHandler.createCard(stack, false);
			if(!Minestuck.infiniteTreeModus)
				size--;
		}
		if(id == 0)
			node = null;
		return stack;
	}
	
	@Override
	public boolean canSwitchFrom(ModusType modus)
	{
		return false;
	}
	
	@SideOnly(Side.CLIENT)
	@Override
	public SylladexGuiHandler getGuiHandler()
	{
		if(guiHandler == null)
			guiHandler = new TreeGuiHandler(this);
		return guiHandler;
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
			if(this.stack.getItem() == node.stack.getItem() && this.stack.getItemDamage() == node.stack.getItemDamage() && ItemStack.areItemStackTagsEqual(this.stack, node.stack)
				&& this.stack.stackSize + node.stack.stackSize <= this.stack.getMaxStackSize())
			{
				this.stack.stackSize += node.stack.stackSize;
			}
			else
			{
				int compare = this.itemToString().compareTo(node.itemToString());
				if(compare == 0)
					compare = this.stack.getItemDamage() - node.stack.getItemDamage();
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
			ResourceLocation name = (ResourceLocation) Item.itemRegistry.getNameForObject(stack.getItem());
			return name.getResourcePath()+":"+name.getResourceDomain();	//Don't want the items to be sorted mod-wise.
		}
		
		public ArrayList<ItemStack> removeItems(int index)
		{
			if(index == 0)
			{
				ArrayList<ItemStack> list = new ArrayList<ItemStack>();
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
				} else return new ArrayList<ItemStack>();
			}
			else
			{
				if(node2 != null)
				{
					ArrayList<ItemStack> list = node2.removeItems(index/2);
					if(index/2 == 0)
						node2 = null;
					return list;
				} else return new ArrayList<ItemStack>();
			}
		}
		
		public ArrayList<ItemStack> getItems()
		{
			ArrayList<ItemStack> list = new ArrayList<ItemStack>();
			if(node1 != null)
				list.addAll(node1.getItems());
			list.add(stack);
			if(node2 != null)
				list.addAll(node2.getItems());
			return list;
		}
		
	}
	
}