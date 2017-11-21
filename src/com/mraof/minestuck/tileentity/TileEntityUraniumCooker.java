package com.mraof.minestuck.tileentity;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import com.mraof.minestuck.MinestuckConfig;
import com.mraof.minestuck.block.BlockUraniumCooker.MachineType;
import com.mraof.minestuck.entity.item.EntityGrist;
import com.mraof.minestuck.item.MinestuckItems;
import com.mraof.minestuck.util.AlchemyRecipeHandler;
import com.mraof.minestuck.util.GristAmount;
import com.mraof.minestuck.util.GristRegistry;
import com.mraof.minestuck.util.GristSet;
import com.mraof.minestuck.util.GristType;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.FurnaceRecipes;

public class TileEntityUraniumCooker extends TileEntityMachine
{
	private HashMap<Item, ItemStack> radiations = new HashMap();
	
	@Override
	public boolean isAutomatic()
	{
		return false;
	}
	
	@Override
	public boolean allowOverrideStop()
	{
		return true;
	}
	
	@Override
	public int getSizeInventory()
	{
		switch (getMachineType()) {
		case URANIUM_COOKER:
			return 1;
		default:
			return 0;
		}
	}
	
	@Override
	public boolean isItemValidForSlot(int i, ItemStack itemstack)
	{
		if(i == 0 && itemstack.getItem()!=MinestuckItems.rawUranium)
		{
			return false;
		}
		
		return true;
	}
	
	@Override
	public boolean contentsValid()
	{
		switch (getMachineType())
		{
		case URANIUM_COOKER:
			if(world.isBlockPowered(this.getPos()))
			{
				return false;
			}
			
			ItemStack inputA = this.inv.get(0);
			ItemStack inputB = this.inv.get(1);
			ItemStack output = irradiate(inputB);
			return (inputA.getItem() == MinestuckItems.rawUranium && !inputB.isEmpty());
		}
		return false;
	}
	
	private ItemStack irradiate(ItemStack input)
	{
		if(radiations.containsKey(input.getItem()))
		{
			input = radiations.get(input.getItem());
		} else
		{
			input = FurnaceRecipes.instance().getSmeltingResult(input);
		}
		
		if(input != null)
			System.err.println("Output stack is " + input.getCount() + " " + input.getItem().getItemStackDisplayName(input));
		
		return input;
	}
	
	public void setRadiation(Item in, ItemStack out)
	{
		radiations.put(in, out);
	}
	
	public void removeRadiation(Item in)
	{
		radiations.remove(in);
	}

	public Map getRadiations()
	{
		return radiations;
	}
	
	@Override
	public void processContents()
	{
		switch (getMachineType()) {
		case URANIUM_COOKER:
			if(!world.isRemote)
			{
				ItemStack item = inv.get(1);
			}
			
			if(inv.get(2).isEmpty())
			{
				this.setInventorySlotContents(2, irradiate(this.getStackInSlot(1)));
			} else
			{
				ItemStack newStack = this.getStackInSlot(2);
				newStack.grow(1);
				this.setInventorySlotContents(2, newStack);
			}
			//this.decrStackSize(1, 1);
			break;
		}
	}
	
	@Override
	public String getName()
	{
		return "tile.cooker." + getMachineType().getUnlocalizedName() + ".name";
	}
	
	public MachineType getMachineType()
	{
		return MachineType.values()[getBlockMetadata() % 4];
	}
}
