package com.mraof.minestuck.blockentity.redstone;

import com.mraof.minestuck.MinestuckConfig;
import com.mraof.minestuck.block.redstone.WirelessRedstoneReceiverBlock;
import com.mraof.minestuck.block.redstone.WirelessRedstoneTransmitterBlock;
import com.mraof.minestuck.blockentity.MSBlockEntityTypes;
import com.mraof.minestuck.network.block.WirelessRedstoneTransmitterSettingsPacket;
import com.mraof.minestuck.util.MSRotationUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public class WirelessRedstoneTransmitterBlockEntity extends BlockEntity
{
	private BlockPos offsetPos = new BlockPos(0, 0, 0);
	private int tickCycle;
	
	public WirelessRedstoneTransmitterBlockEntity(BlockPos pos, BlockState state)
	{
		super(MSBlockEntityTypes.WIRELESS_REDSTONE_TRANSMITTER.get(), pos, state);
	}
	
	public static void serverTick(Level level, BlockPos pos, BlockState state, WirelessRedstoneTransmitterBlockEntity blockEntity)
	{
		if(!level.isAreaLoaded(pos, 1))
			return;
		
		if(blockEntity.tickCycle >= MinestuckConfig.SERVER.puzzleBlockTickRate.get())
		{
			blockEntity.sendUpdateToPosition();
			blockEntity.tickCycle = 0;
		}
		
		blockEntity.tickCycle++;
	}
	
	public BlockPos getDestinationBlockPosFromOffset()
	{
		Direction stateFacing = getBlockState().getValue(WirelessRedstoneTransmitterBlock.FACING);
		
		return this.getBlockPos().offset(offsetPos.rotate(MSRotationUtil.rotationBetween(Direction.NORTH, stateFacing))); //changes from  north facing to the facing direction
	}
	
	public void handleSettingsPacket(WirelessRedstoneTransmitterSettingsPacket packet)
	{
		Direction facing = this.getBlockState().getValue(WirelessRedstoneTransmitterBlock.FACING);
		//changes from the facing direction to north facing
		this.offsetPos = packet.destinationBlockPos().subtract(worldPosition).rotate(MSRotationUtil.rotationBetween(facing, Direction.NORTH));
		
		setChanged();
		if(getLevel() instanceof ServerLevel serverLevel)
			serverLevel.getChunkSource().blockChanged(getBlockPos());
	}
	
	private void sendUpdateToPosition() //for internal use
	{
		BlockPos destBlockPos = getDestinationBlockPosFromOffset();
		if(destBlockPos != null && level != null && !level.isClientSide && level.isAreaLoaded(destBlockPos, 1))
		{
			((WirelessRedstoneTransmitterBlock) getBlockState().getBlock()).updatePower(level, getBlockPos());
			
			BlockState destBlockState = level.getBlockState(destBlockPos);
			if(destBlockState.getBlock() instanceof WirelessRedstoneReceiverBlock receiverBlock
					&& destBlockState.getValue(WirelessRedstoneReceiverBlock.POWER) < getBlockState().getValue(WirelessRedstoneTransmitterBlock.POWER))
			{
				receiverBlock.updatePower(level, destBlockPos, getBlockPos());
			}
		}
	}
	
	public void sendUpdateToPosition(Level level, BlockPos destBlockPos) //for external use
	{
		if(destBlockPos != null && level != null && !level.isClientSide && level.isAreaLoaded(destBlockPos, 1))
		{
			if(destBlockPos.equals(getDestinationBlockPosFromOffset()))
			{
				BlockState blockStateIn = level.getBlockState(destBlockPos);
				if(blockStateIn.getBlock() instanceof WirelessRedstoneReceiverBlock receiverBlock)
				{
					receiverBlock.updatePower(level, destBlockPos, getBlockPos());
				}
			} else
			{
				BlockState blockStateIn = level.getBlockState(destBlockPos);
				if(blockStateIn.getBlock() instanceof WirelessRedstoneReceiverBlock)
				{
					BlockState newState = WirelessRedstoneReceiverBlock.setPower(blockStateIn, 0);
					if(blockStateIn != newState)
						level.setBlock(destBlockPos, newState, Block.UPDATE_ALL);
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
	public void load(CompoundTag compound)
	{
		super.load(compound);
		
		tickCycle = compound.getInt("tickCycle");
		
		int offsetX = compound.getInt("offsetX");
		int offsetY = compound.getInt("offsetY");
		int offsetZ = compound.getInt("offsetZ");
		this.offsetPos = new BlockPos(offsetX, offsetY, offsetZ);
	}
	
	@Override
	public void saveAdditional(CompoundTag compound)
	{
		super.saveAdditional(compound);
		
		compound.putInt("tickCycle", tickCycle);
		
		compound.putInt("offsetX", offsetPos.getX());
		compound.putInt("offsetY", offsetPos.getY());
		compound.putInt("offsetZ", offsetPos.getZ());
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
