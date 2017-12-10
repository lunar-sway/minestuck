package com.mraof.minestuck.tileentity;

import com.mraof.minestuck.item.MinestuckItems;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

public class TileEntityCruxtruder extends TileEntityMachine
{
	private int color = -1;
	private boolean destroyed=false;
	
	public int getColor(){
		return color;
	}
	public void setColor(int Color){
		color = Color;
	}
	public boolean isDestroyed(){
		return destroyed;
	}
	public void destroy(){
		destroyed=true;
	}
	
	
	
	
	@Override
	public boolean isAutomatic()
	{
		return true;
	}
	
	@Override
	public boolean allowOverrideStop()
	{
		return false;
	}
	
	@Override
	public int getSizeInventory()
	{
		return 2;		
	}
	
	@Override
	public void readFromNBT(NBTTagCompound tagCompound)
	{
		super.readFromNBT(tagCompound);
		
		if(tagCompound.hasKey("color"))
			this.color = tagCompound.getInteger("color");
	}
	
	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound tagCompound)
	{
		super.writeToNBT(tagCompound);
		//tagCompound.setInteger("color", color);
		return tagCompound;
	}
	
	@Override
	public boolean isItemValidForSlot(int i, ItemStack itemstack)
	{
		return i == 0 ? itemstack.getItem() == MinestuckItems.rawCruxite : false;
		
	}
	
	@Override
	public boolean contentsValid()
	{
			ItemStack stack1 = this.inv.get(1);
			return (!world.isBlockPowered(this.getPos()) && !this.inv.get(0).isEmpty() && (stack1.isEmpty() || stack1.getCount() < stack1.getMaxStackSize() && stack1.getItemDamage() == this.color + 1));
		
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
			// Process the Raw Cruxite
			
			if (this.inv.get(1).isEmpty())
				setInventorySlotContents(1, new ItemStack(MinestuckItems.cruxiteDowel, 1, color + 1));
			else this.inv.get(1).grow(1);
			decrStackSize(0, 1);
			
			this.progress++;
		
	}
	
	@Override
	public void markDirty()
	{

		super.markDirty();
	}

	@Override
	public String getName()
	{
		return "tile.sburbMachine.cruxtruder.name";
	}
	

	
}
