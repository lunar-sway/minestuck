package com.mraof.minestuck.tileentity;

import com.mraof.minestuck.item.MinestuckItems;
import com.mraof.minestuck.util.AlchemyRecipeHandler;
import com.mraof.minestuck.util.CombinationRegistry;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

public class TileEntityPunchDesignix extends TileEntityMachine
{
	public boolean broken = false;
	//constructor
	public TileEntityPunchDesignix() {}
	
	@Override
	public boolean isAutomatic()
	{
		return false;
	}
	
	@Override
	public boolean allowOverrideStop()
	{
		return false;
	}
	
	@Override
	public int getSizeInventory()
	{
		
		return 3;
	}
	
	@Override
	public void readFromNBT(NBTTagCompound tagCompound)
	{
		super.readFromNBT(tagCompound);
		this.broken = tagCompound.getBoolean("broken");
	}
	
	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound tagCompound)
	{
		super.writeToNBT(tagCompound);
		tagCompound.setBoolean("broken", this.broken);
		return tagCompound;
	}
	
	@Override
	public boolean isItemValidForSlot(int i, ItemStack itemstack)
	{
		return i == 1 ? itemstack.getItem() == MinestuckItems.captchaCard : i == 0;
	}
	
	@Override
	public boolean contentsValid()
	{
		if(!this.inv.get(0).isEmpty() && !inv.get(1).isEmpty())
		{
			ItemStack output = AlchemyRecipeHandler.getDecodedItemDesignix(inv.get(0));
			if(inv.get(1).hasTagCompound() && inv.get(1).getTagCompound().getBoolean("punched"))
			{
				output = CombinationRegistry.getCombination(output,
						AlchemyRecipeHandler.getDecodedItem(inv.get(1)), CombinationRegistry.MODE_OR);
			}
			if(output.isEmpty())
				return false;
			if(output.getItem().isDamageable())
				output.setItemDamage(0);
			output = AlchemyRecipeHandler.createCard(output, true);
			return (inv.get(2).isEmpty() || inv.get(2).getCount() < 16 && ItemStack.areItemStackTagsEqual(inv.get(2), output));
		}
		else
		{
			return false;
		}
		
	}
	
	// We're going to want to trigger a block update every 20 ticks to have comparators pull data from the Alchemeter.
	@Override
	public void update()
	{
		if(world.isRemote)
			return;
		super.update();
	}

	@Override
	public void processContents()
	{

		//Create a new card, using CombinationRegistry
		if(!inv.get(2).isEmpty())
		{
			decrStackSize(1, 1);
			if(!(inv.get(0).hasTagCompound() && inv.get(0).getTagCompound().hasKey("contentID")))
				decrStackSize(0, 1);
			this.inv.get(2).grow(1);
			
		}else{
			
			ItemStack outputItem = AlchemyRecipeHandler.getDecodedItemDesignix(inv.get(0));
			
			if(inv.get(1).hasTagCompound() && inv.get(1).getTagCompound().getBoolean("punched"))
				outputItem = CombinationRegistry.getCombination(outputItem, AlchemyRecipeHandler.getDecodedItem(inv.get(1)), CombinationRegistry.MODE_OR);
			if(outputItem.getItem().isDamageable())
				outputItem.setItemDamage(0);
			
			//Create card
			outputItem = AlchemyRecipeHandler.createCard(outputItem, true);
				
			setInventorySlotContents(2, outputItem);
			if(!(inv.get(0).hasTagCompound() && inv.get(0).getTagCompound().hasKey("contentID")))
				decrStackSize(0, 1);
			decrStackSize(1, 1);
		}
	}
	
	@Override
	public void markDirty()
	{
		
		this.progress = 0;
		this.ready = false;
		super.markDirty();
	}

	@Override
	public String getName()
	{
		return "tile.sburbMachine.PunchDesignix.name";
	}
	
	
}
