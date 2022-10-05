package com.mraof.minestuck.blockentity.redstone;

import com.mraof.minestuck.MinestuckConfig;
import com.mraof.minestuck.block.redstone.WirelessRedstoneReceiverBlock;
import com.mraof.minestuck.blockentity.MSBlockEntityTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public class WirelessRedstoneReceiverBlockEntity extends BlockEntity
{
	private BlockPos lastTransmitterBlockPos;
	private int lastTransmission;
	
	public WirelessRedstoneReceiverBlockEntity(BlockPos pos, BlockState state)
	{
		super(MSBlockEntityTypes.WIRELESS_REDSTONE_RECEIVER.get(), pos, state);
	}
	
	public static void serverTick(Level level, BlockPos pos, BlockState state, WirelessRedstoneReceiverBlockEntity blockEntity)
	{
		if(!level.isAreaLoaded(pos, 1))
			return;
		
		if(blockEntity.lastTransmission >= MinestuckConfig.SERVER.puzzleBlockTickRate.get() && state.getValue(WirelessRedstoneReceiverBlock.AUTO_RESET))
		{
			blockEntity.renewFromLastTransmitter();
			blockEntity.lastTransmission = 0;
		}
		
		if(blockEntity.lastTransmission < MinestuckConfig.SERVER.puzzleBlockTickRate.get()) //how many ticks since last transmission
			blockEntity.lastTransmission++;
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
		if(level != null && !level.isClientSide)
		{
			BlockState state = getBlockState();
			BlockState unpoweredState = WirelessRedstoneReceiverBlock.setPower(state, 0);
			
			if(lastTransmitterBlockPos != null && level.isAreaLoaded(lastTransmitterBlockPos, 1))
			{
				if(level.getBlockEntity(lastTransmitterBlockPos) instanceof WirelessRedstoneTransmitterBlockEntity be)
				{
					
					be.sendUpdateToPosition(level, getBlockPos());
				} else
				{
					if(state != unpoweredState)
						level.setBlock(getBlockPos(), unpoweredState, Block.UPDATE_ALL);
				}
			} else
			{
				if(state != unpoweredState)
					level.setBlock(getBlockPos(), unpoweredState, Block.UPDATE_ALL);
			}
		}
	}
	
	@Override
	public void load(CompoundTag compound)
	{
		super.load(compound);
		
		lastTransmission = compound.getInt("lastTransmission");
		int transmitterX = compound.getInt("transmitterX");
		int transmitterY = compound.getInt("transmitterY");
		int transmitterZ = compound.getInt("transmitterZ");
		this.lastTransmitterBlockPos = new BlockPos(transmitterX, transmitterY, transmitterZ);
	}
	
	@Override
	public void saveAdditional(CompoundTag compound)
	{
		super.saveAdditional(compound);
		
		compound.putInt("lastTransmission", lastTransmission);
		
		getLastTransmitterBlockPos();
		
		compound.putInt("transmitterX", lastTransmitterBlockPos.getX());
		compound.putInt("transmitterY", lastTransmitterBlockPos.getY());
		compound.putInt("transmitterZ", lastTransmitterBlockPos.getZ());
	}
	
	@Override
	public CompoundTag getUpdateTag()
	{
		return this.saveWithoutMetadata();
	}
	
	@Override
	public Packet<ClientGamePacketListener> getUpdatePacket()
	{
		return ClientboundBlockEntityDataPacket.create(this);
	}
}