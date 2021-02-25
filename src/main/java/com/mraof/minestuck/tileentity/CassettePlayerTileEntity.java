package com.mraof.minestuck.tileentity;

import net.minecraft.inventory.IClearable;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.TileEntity;

public class CassettePlayerTileEntity extends TileEntity implements IClearable
{
	private ItemStack cassette = ItemStack.EMPTY;
	
	public CassettePlayerTileEntity()
	{
		super(MSTileEntityTypes.CASSETTE_PLAYER.get());
	}
	
	public void read(CompoundNBT compound)
	{
		super.read(compound);
		if(compound.contains("CassetteItem", 10))
		{
			this.setCassette(ItemStack.read(compound.getCompound("CassetteItem")));
		}
		
	}
	
	public CompoundNBT write(CompoundNBT compound)
	{
		super.write(compound);
		if(!this.getCassette().isEmpty())
		{
			compound.put("CassetteItem", this.getCassette().write(new CompoundNBT()));
		}
		
		return compound;
	}
	
	public ItemStack getCassette()
	{
		return this.cassette;
	}
	
	public void setCassette(ItemStack stack)
	{
		this.cassette = stack;
		this.markDirty();
	}
	
	public void clear()
	{
		this.setCassette(ItemStack.EMPTY);
	}
}
