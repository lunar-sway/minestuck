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
		System.out.println("start!");
		list = new LinkedList<ItemStack>();
	}
	
	public StrifeSpecibus(NBTTagCompound nbt)
	{
		this();
		System.out.println("stert!");
		readFromNBT(nbt);
	}
	
	public StrifeSpecibus(int abstrataIndex)
	{
		this();
		System.out.println("stirt!");
		abstIndex = abstrataIndex;
		readFromNBT(writeToNBT(new NBTTagCompound()));
		System.out.println("creating a new specibus obj");
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
		
		System.out.println("read");
		abstIndex = nbt.getInteger("abstrata");
		list = new LinkedList<ItemStack>();
		
		if(nbt.hasKey("items"))
		{
			NBTTagCompound items = (NBTTagCompound) nbt.getTag("items");
			for(int i = 0; i < list.size(); i++)
				if(items.hasKey("slot"+i))
					list.add(new ItemStack(items.getCompoundTag("slot"+i)));
				else break;
		}
		if(side.isClient())
		{
			abstratus = KindAbstratusList.getTypeList().get(abstIndex);
			items = NonNullList.<ItemStack>create();
			//changed = true;
		}
		
	}
	
	public NBTTagCompound writeToNBT(NBTTagCompound nbt)
	{
		
		System.out.println("write");
		NBTTagCompound items = new NBTTagCompound();
		
		nbt.setInteger("abstrata", abstIndex);
			for(int i = 0; i < list.size(); i++)
		{
			if(list.get(i) != null)
			{
				ItemStack stack = list.get(i).copy();
				System.out.println(stack);
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
			//System.out.println(stack);
			this.list.add(stack.copy());
			this.items.add(stack.copy());
			
			for(ItemStack i : list)
				System.out.println(i);
			
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
