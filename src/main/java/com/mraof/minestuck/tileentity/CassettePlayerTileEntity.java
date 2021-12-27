package com.mraof.minestuck.tileentity;

import net.minecraft.block.BlockState;
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
	
	@Override
	public void load(BlockState state, CompoundNBT nbt)
	{
		super.load(state, nbt);
		if(nbt.contains("CassetteItem", 10))
		{
			this.setCassette(ItemStack.of(nbt.getCompound("CassetteItem")));
		}
		
	}
	
	@Override
	public CompoundNBT save(CompoundNBT compound)
	{
		super.save(compound);
		if(!this.getCassette().isEmpty())
		{
			compound.put("CassetteItem", this.getCassette().save(new CompoundNBT()));
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
		this.setChanged();
	}
	
	@Override
	public void clearContent()
	{
		this.setCassette(ItemStack.EMPTY);
	}
}
