package com.mraof.minestuck.blockentity;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.Clearable;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public class CassettePlayerBlockEntity extends BlockEntity implements Clearable
{
	private ItemStack cassette = ItemStack.EMPTY;
	
	public CassettePlayerBlockEntity(BlockPos pos, BlockState state)
	{
		super(MSBlockEntityTypes.CASSETTE_PLAYER.get(), pos, state);
	}
	
	@Override
	public void load(CompoundTag nbt)
	{
		super.load(nbt);
		if(nbt.contains("CassetteItem", 10))
		{
			this.setCassette(ItemStack.of(nbt.getCompound("CassetteItem")));
		}
	}
	
	@Override
	public void saveAdditional(CompoundTag compound)
	{
		super.saveAdditional(compound);
		if(!this.getCassette().isEmpty())
		{
			compound.put("CassetteItem", this.getCassette().save(new CompoundTag()));
		}
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
