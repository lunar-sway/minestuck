package com.mraof.minestuck.tileentity.redstone;

import com.mraof.minestuck.MinestuckConfig;
import com.mraof.minestuck.block.redstone.RemoteComparatorBlock;
import com.mraof.minestuck.tileentity.MSTileEntityTypes;
import net.minecraft.block.BlockState;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SUpdateTileEntityPacket;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;

public class RemoteComparatorTileEntity extends TileEntity implements ITickableTileEntity
{
	private int tickCycle;
	
	public RemoteComparatorTileEntity()
	{
		super(MSTileEntityTypes.REMOTE_COMPARATOR.get());
	}
	
	@Override
	public void tick()
	{
		if(level == null)
			return;
		
		if(tickCycle >= MinestuckConfig.SERVER.puzzleBlockTickRate.get())
		{
			sendUpdate();
			tickCycle = 0;
		}
		tickCycle++;
	}
	
	private void sendUpdate()
	{
		if(level != null)
		{
			boolean shouldBePowered = RemoteComparatorBlock.isMatch(level, worldPosition);
			
			if(getBlockState().getValue(RemoteComparatorBlock.POWERED) != shouldBePowered)
				level.setBlockAndUpdate(getBlockPos(), getBlockState().setValue(RemoteComparatorBlock.POWERED, shouldBePowered));
			else level.sendBlockUpdated(getBlockPos(), getBlockState(), getBlockState(), 2);
		}
	}
	
	@Override
	public void load(BlockState state, CompoundNBT compound)
	{
		super.load(state, compound);
		tickCycle = compound.getInt("tickCycle");
	}
	
	@Override
	public CompoundNBT save(CompoundNBT compound)
	{
		super.save(compound);
		
		compound.putInt("tickCycle", tickCycle);
		
		return compound;
	}
	
	@Override
	public CompoundNBT getUpdateTag()
	{
		return this.save(new CompoundNBT());
	}
	
	@Override
	public SUpdateTileEntityPacket getUpdatePacket()
	{
		return new SUpdateTileEntityPacket(getBlockPos(), 2, getUpdateTag());
	}
	
	@Override
	public void onDataPacket(NetworkManager net, SUpdateTileEntityPacket pkt)
	{
		this.load(getBlockState(), pkt.getTag());
	}
}