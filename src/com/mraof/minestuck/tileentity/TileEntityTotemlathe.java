package com.mraof.minestuck.tileentity;

import com.mraof.minestuck.block.MinestuckBlocks;
import com.mraof.minestuck.item.MinestuckItems;
import com.mraof.minestuck.util.AlchemyRecipeHandler;
import com.mraof.minestuck.util.CombinationRegistry;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

public class TileEntityTotemlathe extends TileEntityMachine
{
	private boolean broken=false;
	
	//constructor

	public boolean isBroken() {
		return broken;
	}
	
	public void Brake() {
		broken=true;
	}
	
	
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

		return 4;
		
	}
	
	@Override
	public void readFromNBT(NBTTagCompound tagCompound)
	{
		super.readFromNBT(tagCompound);
	}
	
	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound tagCompound)
	{
		super.writeToNBT(tagCompound);
		return tagCompound;
	}
	
	@Override
	public boolean isItemValidForSlot(int i, ItemStack itemstack)
	{
	
			return i == 0 || i == 1 ? itemstack.getItem() == MinestuckItems.captchaCard : i == 2 ? itemstack.getItem() == MinestuckItems.cruxiteDowel : false;
	}
	
	@Override
	public boolean contentsValid()
	{
			if((!inv.get(0).isEmpty() || !inv.get(1).isEmpty()) && !inv.get(2).isEmpty() && !(inv.get(2).hasTagCompound() && inv.get(2).getTagCompound().hasKey("contentID"))
					&& (inv.get(3).isEmpty() || inv.get(3).getCount() < inv.get(3).getMaxStackSize() && inv.get(3).getItemDamage() == inv.get(2).getItemDamage()))
			{
				if(!inv.get(0).isEmpty() && !inv.get(1).isEmpty())
				{
					if(!inv.get(0).hasTagCompound() || !inv.get(0).getTagCompound().getBoolean("punched") || !inv.get(1).hasTagCompound() || !inv.get(1).getTagCompound().getBoolean("punched"))
						return inv.get(3).isEmpty() || !(inv.get(3).hasTagCompound() && inv.get(3).getTagCompound().hasKey("contentID"));
					else
					{
						ItemStack output = CombinationRegistry.getCombination(AlchemyRecipeHandler.getDecodedItem(inv.get(0)), AlchemyRecipeHandler.getDecodedItem(inv.get(1)), CombinationRegistry.MODE_AND);
						return !output.isEmpty() && (inv.get(3).isEmpty() || AlchemyRecipeHandler.getDecodedItem(inv.get(3)).isItemEqual(output));
					}
				}
				else
				{
					ItemStack input = inv.get(0).isEmpty() ? inv.get(1) : inv.get(0);
					return (inv.get(3).isEmpty() || (AlchemyRecipeHandler.getDecodedItem(inv.get(3)).isItemEqual(AlchemyRecipeHandler.getDecodedItem(input))
							|| !(input.hasTagCompound() && input.getTagCompound().getBoolean("punched")) && !(inv.get(3).hasTagCompound() && inv.get(3).getTagCompound().hasKey("contentID"))));
				}
			}
			else return false;
		
	}
	
	public int comparatorValue()
	{
		
		return 0;
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

			if(!inv.get(3).isEmpty())
			{
				this.inv.get(3).grow(1);
				decrStackSize(2, 1);
				return;
			}
			
			ItemStack output;
			if(!inv.get(0).isEmpty() && !inv.get(1).isEmpty())
				if(!inv.get(0).hasTagCompound() || !inv.get(0).getTagCompound().getBoolean("punched") || !inv.get(1).hasTagCompound() || !inv.get(1).getTagCompound().getBoolean("punched"))
					output = new ItemStack(MinestuckBlocks.genericObject);
				else output = CombinationRegistry.getCombination(AlchemyRecipeHandler.getDecodedItem(inv.get(0)), AlchemyRecipeHandler.getDecodedItem(inv.get(1)), CombinationRegistry.MODE_AND);
			else
			{
				ItemStack input = inv.get(0).isEmpty() ? inv.get(1) : inv.get(0);
				if(!input.hasTagCompound() || !input.getTagCompound().getBoolean("punched"))
					output = new ItemStack(MinestuckBlocks.genericObject);
				else output = AlchemyRecipeHandler.getDecodedItem(input);
			}
			
			ItemStack outputDowel = output.getItem().equals(Item.getItemFromBlock(MinestuckBlocks.genericObject))
					? new ItemStack(MinestuckItems.cruxiteDowel) : AlchemyRecipeHandler.createEncodedItem(output, false);
			outputDowel.setItemDamage(inv.get(2).getItemDamage());
			
			setInventorySlotContents(3, outputDowel);
			decrStackSize(2, 1);

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
		return "tile.sburbMachine.Totemlathe.name";
	}
	
}
