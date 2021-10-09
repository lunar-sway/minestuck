package com.mraof.minestuck.tileentity.redstone;

import com.mraof.minestuck.MinestuckConfig;
import com.mraof.minestuck.block.redstone.WirelessRedstoneReceiverBlock;
import com.mraof.minestuck.tileentity.MSTileEntityTypes;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SUpdateTileEntityPacket;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;

public class WirelessRedstoneReceiverTileEntity extends TileEntity implements ITickableTileEntity
{
	private BlockPos lastTransmitterBlockPos;
	private int lastTransmission;
	
	public WirelessRedstoneReceiverTileEntity()
	{
		super(MSTileEntityTypes.WIRELESS_REDSTONE_RECEIVER.get());
	}
	
	@Override
	public void tick()
	{
		if(world == null || !world.isAreaLoaded(pos, 1))
			return; // Forge: prevent loading unloaded chunks
		
		if(MinestuckConfig.SERVER.wirelessBlocksTickRate.get() != Integer.MAX_VALUE && lastTransmission >= MinestuckConfig.SERVER.wirelessBlocksTickRate.get() && world.getBlockState(pos).get(WirelessRedstoneReceiverBlock.AUTO_RESET))
		{
			renewFromLastTransmitter();
			lastTransmission = 0;
		}
		
		if(lastTransmission < MinestuckConfig.SERVER.wirelessBlocksTickRate.get()) //how many ticks since last transmission
			lastTransmission++;
	}
	
	@Override
	public void validate()
	{
		super.validate();
	}
	
	@Override
	public void remove()
	{
		super.remove();
	}
	
	public BlockPos getLastTransmitterBlockPos()
	{
		if(lastTransmitterBlockPos == null)
			lastTransmitterBlockPos = new BlockPos(0, 0, 0);
		return lastTransmitterBlockPos;
	}
	
	public void setLastTransmitterBlockPos(BlockPos lastTransmitterPosIn)
	{
		this.lastTransmitterBlockPos = lastTransmitterPosIn;
	}
	
	/**
	 * Finds the last stored Wireless Transmitter to give it a signal and checks whether it should update the redstone power(including resetting it to 0)
	 */
	public void renewFromLastTransmitter()
	{
		if(world != null && lastTransmitterBlockPos != null && !world.isRemote && world.isAreaLoaded(lastTransmitterBlockPos, 1))
		{
			TileEntity tileEntity = world.getTileEntity(lastTransmitterBlockPos);
			if(tileEntity instanceof WirelessRedstoneTransmitterTileEntity)
			{
				WirelessRedstoneTransmitterTileEntity te = (WirelessRedstoneTransmitterTileEntity) tileEntity;
				
				if(te.getDestinationBlockPos() == lastTransmitterBlockPos)
					te.sendUpdateToPosition(world, pos);
			}
		}
	}
	
	@Override
	public void read(CompoundNBT compound)
	{
		super.read(compound);
		lastTransmission = compound.getInt("lastTransmission");
		int transmitterX = compound.getInt("transmitterX");
		int transmitterY = compound.getInt("transmitterY");
		int transmitterZ = compound.getInt("transmitterZ");
		this.lastTransmitterBlockPos = new BlockPos(transmitterX, transmitterY, transmitterZ);
	}
	
	@Override
	public CompoundNBT write(CompoundNBT compound)
	{
		super.write(compound);
		
		compound.putInt("lastTransmission", lastTransmission);
		
		getLastTransmitterBlockPos();
		
		compound.putInt("transmitterX", lastTransmitterBlockPos.getX());
		compound.putInt("transmitterY", lastTransmitterBlockPos.getY());
		compound.putInt("transmitterZ", lastTransmitterBlockPos.getZ());
		
		return compound;
	}
	
	@Override
	public CompoundNBT getUpdateTag()
	{
		return this.write(new CompoundNBT());
	}
	
	@Override
	public SUpdateTileEntityPacket getUpdatePacket()
	{
		return new SUpdateTileEntityPacket(this.pos, 2, this.write(new CompoundNBT()));
	}
	
	@Override
	public void onDataPacket(NetworkManager net, SUpdateTileEntityPacket pkt)
	{
		this.read(pkt.getNbtCompound());
	}
	
}