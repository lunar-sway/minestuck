package com.mraof.minestuck.tileentity;

import com.mraof.minestuck.block.BlockPunchDesignix;
import com.mraof.minestuck.block.BlockPunchDesignix.EnumParts;
import com.mraof.minestuck.item.MinestuckItems;
import com.mraof.minestuck.util.AlchemyRecipeHandler;
import com.mraof.minestuck.util.CombinationRegistry;
import com.mraof.minestuck.util.GristType;
import com.mraof.minestuck.util.IdentifierHandler;
import com.mraof.minestuck.util.IdentifierHandler.PlayerIdentifier;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.BlockPos;

public class TileEntityPunchDesignix extends TileEntityMachine
{
	public PlayerIdentifier owner;
	public GristType selectedGrist = GristType.Build;
	public int color = -1;
	public boolean isMaster;
	public boolean destroyed=false;
	public EnumParts part;
	//constructor
	public TileEntityPunchDesignix(IBlockState state){
		part = state.getValue(BlockPunchDesignix.PART);
		if(part== EnumParts.BOTTOM_LEFT){
			isMaster=true;
		}else{
			isMaster=false;
		}
	}
	public BlockPos GetMasterPos(IBlockState state){

		switch(part){
		case BOTTOM_LEFT:return getPos();		
		case BOTTOM_RIGHT:return getPos().west();
		case TOP_LEFT:return getPos().down();
		case TOP_RIGHT:return getPos().down().west();
		}
		return getPos();
	}
	public boolean isMaster(){
		return isMaster;
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
		
		return 3;
	}
	
	@Override
	public void readFromNBT(NBTTagCompound tagCompound)
	{
		super.readFromNBT(tagCompound);
		
		if(tagCompound.hasKey("gristType"))
			this.selectedGrist = GristType.values()[tagCompound.getInteger("gristType")];
		
		if(tagCompound.hasKey("color"))
			this.color = tagCompound.getInteger("color");
		
		if(tagCompound.hasKey("owner") || tagCompound.hasKey("ownerMost"))
			owner = IdentifierHandler.load(tagCompound, "owner");
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
