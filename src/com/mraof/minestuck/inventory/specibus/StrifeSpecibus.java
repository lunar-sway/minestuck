package com.mraof.minestuck.inventory.specibus;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;

import com.mraof.minestuck.util.IdentifierHandler.PlayerIdentifier;
import com.mraof.minestuck.util.KindAbstratusList;
import com.mraof.minestuck.util.KindAbstratusType;
import com.mraof.minestuck.util.MinestuckPlayerData;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.NonNullList;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class StrifeSpecibus 
{
	protected LinkedList<ItemStack> list;
	protected int abstIndex;
	public Side side = Side.CLIENT;
	
	@SideOnly(Side.CLIENT)
	protected NonNullList<ItemStack> items;
	@SideOnly(Side.CLIENT)
	protected NonNullList<ItemStack> unuseable;
	@SideOnly(Side.CLIENT)
	protected KindAbstratusType abstratus;
	
	public StrifeSpecibus()
	{
		list = new LinkedList<ItemStack>();
	}
	
	public StrifeSpecibus(NBTTagCompound nbt)
	{
		this();
		readFromNBT(nbt);
	}
	
	public StrifeSpecibus(int abstrataIndex)
	{
		this();
		abstIndex = abstrataIndex;
		readFromNBT(writeToNBT(new NBTTagCompound()));
		//System.out.println("creating a new specibus obj");
	}
	
	public StrifeSpecibus(KindAbstratusType type) 
	{
		this(KindAbstratusList.getTypeList().indexOf(type));
	}

	public void initSpecibus(Side side)
	{
		
	}
	
	public void addToPortfolio(PlayerIdentifier player)
	{
		MinestuckPlayerData.getStrifePortfolio(player).add(this);
	}
	
	public void readFromNBT(NBTTagCompound nbt)
	{
		abstIndex = nbt.getInteger("abstrata");
		list = new LinkedList<ItemStack>();
		
		if(side.isClient())
		{
			abstratus = KindAbstratusList.getTypeList().get(abstIndex);
			items = NonNullList.<ItemStack>create();
		}
		
		if(nbt.hasKey("items"))
		{
			NBTTagCompound itemsNBT = (NBTTagCompound) nbt.getTag("items");
			int i = 0;
			while(true)
			{
				if(itemsNBT.hasKey("slot"+i))
				{
					ItemStack stack = new ItemStack(itemsNBT.getCompoundTag("slot"+i));
					list.add(stack);
					if(side.isClient()) items.add(stack);
				}
				else break;
				i++;
			}
		}
	}
	
	public NBTTagCompound writeToNBT(NBTTagCompound nbt)
	{
		NBTTagCompound items = new NBTTagCompound();
		
		nbt.setInteger("abstrata", abstIndex);
			for(int i = 0; i < list.size(); i++)
		{
			if(list.get(i) != null)
			{
				ItemStack stack = list.get(i).copy();
				items.setTag("slot"+i, stack.writeToNBT(new NBTTagCompound()));
			}
		}
		nbt.setTag("items", items);
		
		return nbt;
	}
	
	public void setAbstratus(KindAbstratusType newKind)
	{
		this.abstratus = newKind;
		this.abstIndex = KindAbstratusList.getTypeList().indexOf(newKind);
	}
	
	public int getAbstratusIndex()
	{
		return this.abstIndex;
	}
	
	public KindAbstratusType getAbstratus()
	{
		return this.abstratus;
	}
	
	public boolean isBlank()
	{
		return this.abstratus.equals(KindAbstratusList.getTypeList().get(0)) || this.abstIndex == 0;
	}
	
	public boolean putItemStack(ItemStack stack)
	{
		if(stack.isEmpty())
			return false;
		
		if(this.abstratus.partOf(stack))
		{
			this.list.add(stack.copy());
			this.items.add(stack.copy());
			
			
			return true;
		}
		else return false; 	
	}
	
	public void forceItemStack(ItemStack stack)
	{
		this.list.add(stack.copy());
		this.items.add(stack.copy());
	}
	public NonNullList<ItemStack> getItems()
	{
		return items;
	}
	
	public ItemStack retrieveItem(int index)
	{
		ItemStack stack = ItemStack.EMPTY;
		if(this.items.get(index) != null)
		{
			stack = this.items.get(index);
			this.items.remove(index);
		}
		return stack;
	}
	
}
