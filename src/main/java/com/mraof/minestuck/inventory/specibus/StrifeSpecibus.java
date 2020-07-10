package com.mraof.minestuck.inventory.specibus;

import com.mraof.minestuck.util.IdentifierHandler.PlayerIdentifier;
import com.mraof.minestuck.util.KindAbstratusList;
import com.mraof.minestuck.util.KindAbstratusType;
import com.mraof.minestuck.world.storage.PlayerSavedData;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.NonNullList;
import net.minecraft.world.World;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.fml.LogicalSide;

import java.util.LinkedList;

public class StrifeSpecibus 
{
	protected LinkedList<ItemStack> list;
	protected int abstIndex;
	public LogicalSide side = LogicalSide.CLIENT;
	
	protected NonNullList<ItemStack> items;
	protected NonNullList<ItemStack> unuseable;
	protected KindAbstratusType abstratus;
	
	public StrifeSpecibus()
	{
		list = new LinkedList<ItemStack>();
	}
	
	public StrifeSpecibus(CompoundNBT nbt)
	{
		this();
		readFromNBT(nbt);
	}
	
	public StrifeSpecibus(int abstrataIndex)
	{
		this();
		abstIndex = abstrataIndex;
		readFromNBT(writeToNBT(new CompoundNBT()));
		//System.out.println("creating a new specibus obj");
	}
	
	public StrifeSpecibus(KindAbstratusType type) 
	{
		this(KindAbstratusList.getTypeList().indexOf(type));
	}

	public void initSpecibus(LogicalSide side)
	{
		
	}
	
	public void addToPortfolio(PlayerIdentifier player, World world)
	{
		PlayerSavedData.getData(player, world).addSpecibus(this);
	}
	
	public void readFromNBT(CompoundNBT nbt)
	{
		abstIndex = nbt.getInt("abstrata");
		list = new LinkedList<ItemStack>();
		
		if(side.isClient())
		{
			abstratus = KindAbstratusList.getTypeList().get(abstIndex);
			items = NonNullList.<ItemStack>create();
		}
		
		if(nbt.contains("items", Constants.NBT.TAG_COMPOUND))
		{
			CompoundNBT itemsNBT = nbt.getCompound("items");
			int i = 0;
			while(true)
			{
				if(itemsNBT.contains("slot"+i, Constants.NBT.TAG_COMPOUND))
				{
					ItemStack stack = ItemStack.read(itemsNBT.getCompound("slot"+i));
					list.add(stack);
					if(side.isClient()) items.add(stack);
				}
				else break;
				i++;
			}
		}
	}
	
	public CompoundNBT writeToNBT(CompoundNBT nbt)
	{
		CompoundNBT items = new CompoundNBT();
		
		nbt.putInt("abstrata", abstIndex);
			for(int i = 0; i < list.size(); i++)
		{
			if(list.get(i) != null)
			{
				ItemStack stack = list.get(i).copy();
				items.put("slot"+i, stack.write(new CompoundNBT()));
			}
		}
		nbt.put("items", items);
		
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
