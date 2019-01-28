package com.mraof.minestuck.inventory;

import com.mraof.minestuck.entity.consort.EntityConsort;
import com.mraof.minestuck.entity.consort.EnumConsort;
import com.mraof.minestuck.util.Debug;
import com.mraof.minestuck.util.MinestuckPlayerData;
import com.mraof.minestuck.util.Pair;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.NonNullList;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.List;

public class InventoryConsortMerchant implements IInventory
{
	private final NonNullList<ItemStack> inv = NonNullList.withSize(9, ItemStack.EMPTY);
	private EnumConsort consortType;
	private EnumConsort.MerchantType merchantType;
	private final int[] prices = new int[9];
	private EntityConsort consort;
	
	public InventoryConsortMerchant(EntityConsort consort, NBTTagList list)
	{
		this.consort = consort;
		consortType = consort.getConsortType();
		merchantType = consort.merchantType;
		
		for(int i = 0; i < list.tagCount() && i < 9; i++)
		{
			NBTTagCompound nbt = list.getCompoundTagAt(i);
			ItemStack stack = new ItemStack(nbt);
			inv.set(i, stack);
			if(!stack.isEmpty())
				prices[i] = nbt.getInteger("price");
		}
	}
	
	@SideOnly(Side.CLIENT)
	public InventoryConsortMerchant()
	{
	}
	
	public InventoryConsortMerchant(EntityConsort consort, List<Pair<ItemStack, Integer>> stocks)
	{
		this.consort = consort;
		consortType = consort.getConsortType();
		merchantType = consort.merchantType;
		
		for (int i = 0; i < stocks.size(); i++)
		{
			Pair<ItemStack, Integer> entry = stocks.get(i);
			inv.set(i, entry.object1);
			prices[i] = entry.object2;
		}
	}
	
	protected void handlePurchase(EntityPlayer player, boolean all, int index)
	{
		if (!player.world.isRemote && index >= 0 && index < inv.size())
		{
			ItemStack stack = inv.get(index);
			if (stack.isEmpty())
				return;
			MinestuckPlayerData.PlayerData playerData = MinestuckPlayerData.getData(player);
			int amountPurchased = (int) Math.min(prices[index] != 0 ? playerData.boondollars / prices[index] : Integer.MAX_VALUE, all ? stack.getCount() : 1);
			if (amountPurchased == 0)
			{
				player.sendMessage(new TextComponentTranslation("consort.cantAfford"));
			} else
			{
				MinestuckPlayerData.addBoondollars(player, -(amountPurchased * prices[index]));
				ItemStack items = stack.splitStack(amountPurchased);
				
				if (!player.addItemStackToInventory(items))
				{
					EntityItem entity = player.dropItem(items, false);
					if (entity != null)
						entity.setNoPickupDelay();
					else Debug.warn("Couldn't spawn in an item purchased from a consort! "+items);
				} else player.inventoryContainer.detectAndSendChanges();
				
				player.openContainer.detectAndSendChanges();
			}
		}
	}
	
	public NBTTagList writeToNBT()
	{
		NBTTagList list = new NBTTagList();
		for (int i = 0; i < 9; i++)
		{
			NBTTagCompound nbt = inv.get(i).writeToNBT(new NBTTagCompound());
			nbt.setInteger("price", prices[i]);
			list.appendTag(nbt);
		}
		return list;
	}
	
	public EnumConsort getConsortType()
	{
		return consortType;
	}
	
	public EnumConsort.MerchantType getMerchantType()
	{
		return merchantType;
	}
	
	@Override
	public int getSizeInventory()
	{
		return 9;
	}
	
	@Override
	public boolean isEmpty()
	{
		for(ItemStack stack : inv)
			if(!stack.isEmpty())
				return false;
		
		return true;
	}
	
	@Override
	public ItemStack getStackInSlot(int index)
	{
		return inv.get(index);
	}
	
	@Override
	public ItemStack decrStackSize(int index, int count)
	{
		return ItemStack.EMPTY;
	}
	
	@Override
	public ItemStack removeStackFromSlot(int index)
	{
		return ItemStack.EMPTY;
	}
	
	@Override
	public void setInventorySlotContents(int index, ItemStack stack)
	{
		inv.set(index, stack);
	}
	
	@Override
	public int getInventoryStackLimit()
	{
		return 0;
	}
	
	@Override
	public void markDirty()
	{
	
	}
	
	@Override
	public boolean isUsableByPlayer(EntityPlayer player)
	{
		return true;
	}
	
	@Override
	public void openInventory(EntityPlayer player)
	{
	
	}
	
	@Override
	public void closeInventory(EntityPlayer player)
	{
	
	}
	
	@Override
	public boolean isItemValidForSlot(int index, ItemStack stack)
	{
		return false;
	}
	
	@Override
	public int getField(int id)
	{
		if (id == 0)
			return consortType.ordinal();
		else if (id == 1)
			return merchantType.ordinal();
		else return prices[(id - 2) % 9];
	}
	
	@Override
	public void setField(int id, int value)
	{
		if (id == 0)
			consortType = EnumConsort.values()[value % EnumConsort.values().length];
		else if (id == 1)
			merchantType = EnumConsort.MerchantType.values()[value % EnumConsort.MerchantType.values().length];
		else prices[(id - 2) % 9] = value;
	}
	
	@Override
	public int getFieldCount()
	{
		return 11;
	}
	
	@Override
	public void clear()
	{
		inv.clear();
		for(int i = 0; i < 9; i++)
			prices[i] = 0;
	}
	
	@Override
	public String getName()
	{
		return null;	//TODO
	}
	
	@Override
	public boolean hasCustomName()
	{
		return false;
	}
	
	@Override
	public ITextComponent getDisplayName()
	{
		return null;	//TODO
	}
}
