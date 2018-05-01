package com.mraof.minestuck.tileentity;

import com.mraof.minestuck.block.BlockUraniumCooker.MachineType;
import com.mraof.minestuck.item.MinestuckItems;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.FurnaceRecipes;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;

import java.util.HashMap;
import java.util.Map;

public class TileEntityUraniumCooker extends TileEntityMachine
{
	private static HashMap<Item, ItemStack> radiations = new HashMap();
	private short fuel = 0;
	private short maxFuel = 128;
	
	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound in)
	{
		super.writeToNBT(in);
		in.setShort("fuel", fuel);
		return in;
	}
	@Override
	public void readFromNBT(NBTTagCompound in)
	{
		super.readFromNBT(in);
		fuel = in.getShort("fuel");
	}
	
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
		switch (getMachineType())
		{
		case URANIUM_COOKER:
			return 3;
		default:
			return 0;
		}
	}
	
	@Override
	public boolean isItemValidForSlot(int i, ItemStack itemstack)
	{
		if(i == 0 && itemstack.getItem() != MinestuckItems.rawUranium)
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
		
		return input.copy();
	}
	
	//This is called mostly from AlchemyRecipeHandler
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
				if(getFuel() <= getMaxFuel() - 32 && inv.get(0).getItem() == MinestuckItems.rawUranium)
				{	//Refill fuel
					fuel += 32;
					this.decrStackSize(0, 1);
				}
				if(canIrradiate())
				{
					ItemStack output = irradiate(this.getStackInSlot(1));
					if(inv.get(2).isEmpty() && fuel > 0)
					{
						this.setInventorySlotContents(2, output);
					} else
					{
						this.getStackInSlot(2).grow(output.getCount());
					}
					if(this.getStackInSlot(1).getItem() == Items.MUSHROOM_STEW)
					{
						this.setInventorySlotContents(1, new ItemStack(Items.BOWL));
					} else
					{
						this.decrStackSize(1, 1);
					}
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
		if(fuel > 0 && !inv.get(1).isEmpty() && !output.isEmpty())
		{
			ItemStack out = inv.get(2);
			if(out.isEmpty())
			{
				return true;
			}
			else if(out.getMaxStackSize() >= output.getCount() + out.getCount() && out.isItemEqual(output))
			{
				return true;
			}
		}
		return false;
	}
	
	@Override
	public int[] getSlotsForFace(EnumFacing side)
	{
		if(side == EnumFacing.UP)
			return new int[] {1};
		if(side == EnumFacing.DOWN)
			return new int[] {2};
		else return new int[] {0};
	}
	
	@Override
	public boolean canInsertItem(int index, ItemStack itemStackIn, EnumFacing direction)
	{
		return true;
	}
	
	@Override
	public boolean canExtractItem(int index, ItemStack stack, EnumFacing direction)
	{
		return true;
	}

	@Override
	public String getName()
	{
		return "tile.cooker." + getMachineType().getUnlocalizedName() + ".name";
	}
	
	public MachineType getMachineType()
	{
		return MachineType.values()[getBlockMetadata() % 1];
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
