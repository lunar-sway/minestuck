package com.mraof.minestuck.tileentity.redstone;

import com.mraof.minestuck.block.redstone.WirelessRedstoneReceiverBlock;
import com.mraof.minestuck.tileentity.MSTileEntityTypes;
import net.minecraft.block.BlockState;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SUpdateTileEntityPacket;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.common.util.Constants;

public class WirelessRedstoneReceiverTileEntity extends TileEntity implements ITickableTileEntity
{
	private BlockPos lastTransmitterBlockPos;
	private int lastTransmission;
	private static final int WIRELESS_CONSTANT = 6;
	
	public WirelessRedstoneReceiverTileEntity()
	{
		super(MSTileEntityTypes.WIRELESS_REDSTONE_RECEIVER.get());
	}
	
	@Override
	public void tick()
	{
		if(level == null || !level.isAreaLoaded(getBlockPos(), 1))
			return;
		
		if(lastTransmission >= WIRELESS_CONSTANT && level.getBlockState(getBlockPos()).getValue(WirelessRedstoneReceiverBlock.AUTO_RESET))
		{
			renewFromLastTransmitter();
			lastTransmission = 0;
		}
		
		if(lastTransmission < WIRELESS_CONSTANT) //how many ticks since last transmission
			lastTransmission++;
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
		if(level != null && lastTransmitterBlockPos != null && !level.isClientSide && level.isAreaLoaded(lastTransmitterBlockPos, 1))
		{
			TileEntity tileEntity = level.getBlockEntity(lastTransmitterBlockPos);
			if(tileEntity instanceof WirelessRedstoneTransmitterTileEntity)
			{
				WirelessRedstoneTransmitterTileEntity te = (WirelessRedstoneTransmitterTileEntity) tileEntity;
				
				te.sendUpdateToPosition(level, getBlockPos());
			} else
			{
				level.setBlock(getBlockPos(), level.getBlockState(getBlockPos()).setValue(WirelessRedstoneReceiverBlock.POWER, 0), Constants.BlockFlags.DEFAULT);
			}
		}
		else if(level != null && !level.isClientSide)
			level.setBlock(getBlockPos(), level.getBlockState(getBlockPos()).setValue(WirelessRedstoneReceiverBlock.POWER, 0), Constants.BlockFlags.DEFAULT);
	}
	
	@Override
	public void load(BlockState state, CompoundNBT compound)
	{
		super.load(state, compound);
		
		lastTransmission = compound.getInt("lastTransmission");
		int transmitterX = compound.getInt("transmitterX");
		int transmitterY = compound.getInt("transmitterY");
		int transmitterZ = compound.getInt("transmitterZ");
		this.lastTransmitterBlockPos = new BlockPos(transmitterX, transmitterY, transmitterZ);
	}
	
	@Override
	public CompoundNBT save(CompoundNBT compound)
	{
		super.save(compound);
		
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