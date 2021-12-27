package com.mraof.minestuck.tileentity;

import com.mraof.minestuck.Minestuck;
import com.mraof.minestuck.util.ColorHandler;
import net.minecraft.block.BlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SUpdateTileEntityPacket;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;

import javax.annotation.Nullable;

public class ItemStackTileEntity extends TileEntity implements IColored
{
	public static final ResourceLocation ITEM_DYNAMIC = new ResourceLocation(Minestuck.MOD_ID, "item");
	
	public ItemStackTileEntity()
	{
		super(MSTileEntityTypes.ITEM_STACK.get());
	}
	
	private ItemStack stack = ItemStack.EMPTY;
	
	public ItemStack getStack()
	{
		return stack;
	}
	
	public void setStack(ItemStack stack)
	{
		if(stack != null)
		{
			this.stack = stack;
			setChanged();
		}
	}
	
	@Override
	public int getColor()
	{
		return ColorHandler.getColorFromStack(stack);
	}
	
	@Override
	public void load(BlockState state, CompoundNBT nbt)
	{
		super.load(state, nbt);
		stack = ItemStack.of(nbt.getCompound("stack"));
	}
	
	@Override
	public CompoundNBT save(CompoundNBT compound)
	{
		compound = super.save(compound);
		compound.put("stack", stack.save(new CompoundNBT()));
		return compound;
	}
	
	@Override
	public CompoundNBT getUpdateTag()
	{
		CompoundNBT nbt = super.getUpdateTag();
		nbt.put("stack", stack.save(new CompoundNBT()));
		return nbt;
	}
	
	
	@Override
	public void handleUpdateTag(BlockState state, CompoundNBT tag)
	{
		stack = ItemStack.of(tag.getCompound("stack"));
	}
	
	@Nullable
	@Override
	public SUpdateTileEntityPacket getUpdatePacket()
	{
		return new SUpdateTileEntityPacket(getBlockPos(), 2, getUpdateTag());
	}
	
	@Override
	public void onDataPacket(NetworkManager net, SUpdateTileEntityPacket pkt)
	{
		handleUpdateTag(getBlockState(), pkt.getTag());
		if(level != null)
		{
			BlockState state = level.getBlockState(worldPosition);
			level.sendBlockUpdated(worldPosition, state, state, 2);
		}
	}
}