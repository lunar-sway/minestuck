package com.mraof.minestuck.tileentity.redstone;

import com.mraof.minestuck.MinestuckConfig;
import com.mraof.minestuck.block.redstone.WirelessRedstoneReceiverBlock;
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
		if(world == null || !world.isAreaLoaded(pos, 1))
			return; // Forge: prevent loading unloaded chunks
		
		if(tickCycle >= MinestuckConfig.SERVER.wirelessBlocksTickRate.get())
		{
			sendUpdateToPosition();
			tickCycle = 0;
		}
		
		tickCycle++;
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
		if(destBlockPos != null && world != null && !world.isRemote && world.isAreaLoaded(destBlockPos, 1))
		{
			int powerIn = world.getRedstonePowerFromNeighbors(pos);
			
			//Debug.debugf("not null destination of %s and area loaded, powerIn = %s", destBlockPos, powerIn);
			BlockState blockStateIn = world.getBlockState(destBlockPos);
			if(blockStateIn.getBlock() instanceof WirelessRedstoneReceiverBlock && blockStateIn.get(WirelessRedstoneReceiverBlock.POWER) < powerIn)
			{
				world.setBlockState(destBlockPos, blockStateIn.with(WirelessRedstoneReceiverBlock.POWER, powerIn));
				
				TileEntity tileEntity = world.getTileEntity(destBlockPos);
				if(tileEntity instanceof WirelessRedstoneReceiverTileEntity)
				{
					WirelessRedstoneReceiverTileEntity te = (WirelessRedstoneReceiverTileEntity) tileEntity;
					te.setLastTransmitterBlockPos(pos);
				}
			}
		}
	}
	
	public void sendUpdateToPosition(World worldIn, BlockPos destBlockPos) //for external use
	{
		if(destBlockPos != null && worldIn != null && !worldIn.isRemote && worldIn.isAreaLoaded(destBlockPos, 1))
		{
			if(destBlockPos.equals(this.destBlockPos))
			{
				int powerIn = worldIn.getRedstonePowerFromNeighbors(pos);
				
				BlockState blockStateIn = worldIn.getBlockState(destBlockPos);
				if(blockStateIn.getBlock() instanceof WirelessRedstoneReceiverBlock)
				{
					worldIn.setBlockState(destBlockPos, blockStateIn.with(WirelessRedstoneReceiverBlock.POWER, powerIn));
					
					TileEntity tileEntity = worldIn.getTileEntity(destBlockPos);
					if(tileEntity instanceof WirelessRedstoneReceiverTileEntity)
					{
						WirelessRedstoneReceiverTileEntity te = (WirelessRedstoneReceiverTileEntity) tileEntity;
						te.setLastTransmitterBlockPos(pos);
					}
				}
			} else
			{
				BlockState blockStateIn = worldIn.getBlockState(destBlockPos);
				if(blockStateIn.getBlock() instanceof WirelessRedstoneReceiverBlock)
				{
					worldIn.setBlockState(destBlockPos, blockStateIn.with(WirelessRedstoneReceiverBlock.POWER, 0));
				}
			}
		}
	}
	
	public BlockPos findReceiver()
	{
		if(getWorld() != null)
		{
			for(BlockPos blockPos : BlockPos.getAllInBoxMutable(getPos().add(24, 24, 24), getPos().add(-24, -24, -24)))
			{
				Block block = getWorld().getBlockState(blockPos).getBlock();
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
	public void read(CompoundNBT compound)
	{
		super.read(compound);
		tickCycle = compound.getInt("tickCycle");
		int destX = compound.getInt("destX");
		int destY = compound.getInt("destY");
		int destZ = compound.getInt("destZ");
		this.destBlockPos = new BlockPos(destX, destY, destZ);
	}
	
	@Override
	public CompoundNBT write(CompoundNBT compound)
	{
		super.write(compound);
		
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