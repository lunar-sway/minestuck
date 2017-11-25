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
	private static HashMap<Item, ItemStack> radiations = new HashMap();
	private short fuel = 0;
	private short maxFuel = 128;
	
	@Override
	public boolean isAutomatic()
	{
		return true;
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
			return 3;
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
	
	public void update()
	{
		processContents();
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
		
		if(!input.isEmpty())
			System.err.println("Output stack is " + input.getCount() + " " + input.getItem().getItemStackDisplayName(input));
		//Usage of System.err is for readability. This is not an error at all.
		
		return input;
	}
	
	public static void setRadiation(Item in, ItemStack out)
	{
		radiations.put(in, out);
	}
	
	public static void removeRadiation(Item in)
	{
		radiations.remove(in);
	}

	public static Map getRadiations()
	{
		return radiations;
	}
	
	@Override
	public void processContents()
	{
		switch (getMachineType()) {
		case URANIUM_COOKER:
			//if(!world.isRemote)
			//{
				ItemStack item = inv.get(1);
				if(getFuel() <= getMaxFuel()-32 && !inv.get(0).isEmpty())
				{
					fuel += 32;
					this.decrStackSize(0, 1);
				}
				if(canIrradiate())
				{
					final ItemStack temp = irradiate(this.getStackInSlot(1));	//This stack *is* the output stack and should NEVER be changed.
					if(inv.get(2).isEmpty() && fuel > 0)
					{
						this.setInventorySlotContents(2, new ItemStack(temp.getItem(), temp.getCount()));
					} else
					{
						this.getStackInSlot(2).grow(temp.getCount());
					}
					this.decrStackSize(1, 1);
					fuel--;
				}
			//}
			//this.markDirty();
			break;
		}
	}
	
	private boolean canIrradiate()
	{
		ItemStack output = irradiate(inv.get(1));
		if(fuel>0 && !inv.get(1).isEmpty() && !output.isEmpty())
		{
			if(inv.get(2).isEmpty())
			{
				return true;
			}
			else if(inv.get(2).getMaxStackSize() >= output.getCount() + inv.get(2).getCount())
			{
				return true;
			}
		}
		return false;
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

	public short getFuel() {
		return fuel;
	}

	public void setFuel(short fuel) {
		this.fuel = fuel;
	}

	public short getMaxFuel() {
		return maxFuel;
	}

	public void setMaxFuel(short maxFuel) {
		this.maxFuel = maxFuel;
	}
}
