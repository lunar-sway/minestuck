package com.mraof.minestuck.tileentity.redstone;

import com.mraof.minestuck.block.redstone.WirelessRedstoneReceiverBlock;
import com.mraof.minestuck.block.redstone.WirelessRedstoneTransmitterBlock;
import com.mraof.minestuck.tileentity.MSTileEntityTypes;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SUpdateTileEntityPacket;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.INameable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.common.util.Constants;

public class WirelessRedstoneTransmitterTileEntity extends TileEntity implements INameable, ITickableTileEntity
{
	private BlockPos destBlockPos;
	private int tickCycle;
	
	public WirelessRedstoneTransmitterTileEntity()
	{
		super(MSTileEntityTypes.WIRELESS_REDSTONE_TRANSMITTER.get());
	}
	
	@Override
	public void tick()
	{
		if(level == null || !level.isAreaLoaded(getBlockPos(), 1))
			return;
		
		if(tickCycle >= 6) //6 is wireless constant
		{
			sendUpdateToPosition();
			tickCycle = 0;
		}
		
		tickCycle++;
	}
	
	public BlockPos getDestinationBlockPos()
	{
		if(destBlockPos == null)
			destBlockPos = new BlockPos(0, 0, 0);
		return destBlockPos;
	}
	
	public void setDestinationBlockPos(BlockPos destinationPosIn)
	{
		this.destBlockPos = destinationPosIn;
	}
	
	private void sendUpdateToPosition() //for internal use
	{
		if(destBlockPos != null && level != null && !level.isClientSide && level.isAreaLoaded(destBlockPos, 1))
		{
			((WirelessRedstoneTransmitterBlock) getBlockState().getBlock()).updatePower(level, getBlockPos());
			
			BlockState destBlockState = level.getBlockState(destBlockPos);
			if(destBlockState.getBlock() instanceof WirelessRedstoneReceiverBlock && destBlockState.getValue(WirelessRedstoneReceiverBlock.POWER) < getBlockState().getValue(WirelessRedstoneTransmitterBlock.POWER))
			{
				WirelessRedstoneReceiverBlock receiverBlock = (WirelessRedstoneReceiverBlock) destBlockState.getBlock();
				receiverBlock.updatePower(level, destBlockPos, getBlockPos());
			}
		}
	}
	
	public void sendUpdateToPosition(World worldIn, BlockPos destBlockPos) //for external use
	{
		if(destBlockPos != null && worldIn != null && !worldIn.isClientSide && worldIn.isAreaLoaded(destBlockPos, 1))
		{
			if(destBlockPos.equals(this.destBlockPos))
			{
				BlockState blockStateIn = worldIn.getBlockState(destBlockPos);
				if(blockStateIn.getBlock() instanceof WirelessRedstoneReceiverBlock)
				{
					WirelessRedstoneReceiverBlock receiverBlock = (WirelessRedstoneReceiverBlock) blockStateIn.getBlock();
					receiverBlock.updatePower(worldIn, destBlockPos, getBlockPos());
				}
			} else
			{
				BlockState blockStateIn = worldIn.getBlockState(destBlockPos);
				if(blockStateIn.getBlock() instanceof WirelessRedstoneReceiverBlock)
				{
					worldIn.setBlock(destBlockPos, blockStateIn.setValue(WirelessRedstoneReceiverBlock.POWER, 0), Constants.BlockFlags.DEFAULT);
				}
			}
		}
	}
	
	public BlockPos findReceiver()
	{
		if(level != null)
		{
			for(BlockPos blockPos : BlockPos.betweenClosed(getBlockPos().offset(24, 24, 24), getBlockPos().offset(-24, -24, -24)))
			{
				Block block = level.getBlockState(blockPos).getBlock();
				if(block instanceof WirelessRedstoneReceiverBlock)
				{
					return blockPos;
				}
			}
		}
		
		return null;
	}
	
	@Override
	public ITextComponent getName()
	{
		return new StringTextComponent("Wireless Redstone Interface");
	}
	
	@Override
	public ITextComponent getDisplayName()
	{
		return hasCustomName() ? getCustomName() : getName();
	}
	
	@Override
	public void load(BlockState state, CompoundNBT compound)
	{
		super.load(state, compound);
		
		tickCycle = compound.getInt("tickCycle");
		int destX = compound.getInt("destX");
		int destY = compound.getInt("destY");
		int destZ = compound.getInt("destZ");
		this.destBlockPos = new BlockPos(destX, destY, destZ);
	}
	
	@Override
	public CompoundNBT save(CompoundNBT compound)
	{
		super.save(compound);
		
		compound.putInt("tickCycle", tickCycle);
		
		getDestinationBlockPos();
		
		compound.putInt("destX", destBlockPos.getX());
		compound.putInt("destY", destBlockPos.getY());
		compound.putInt("destZ", destBlockPos.getZ());
		
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