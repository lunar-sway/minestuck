package com.mraof.minestuck.blockentity.redstone;

import com.mraof.minestuck.block.redstone.RedstoneClockBlock;
import com.mraof.minestuck.blockentity.MSBlockEntityTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public class RedstoneClockBlockEntity extends BlockEntity
{
	private int tickCycle;
	private int clockSpeed;
	
	public static final String TIME_CHANGE = "block.minestuck.redstone_clock.time_change";
	
	public RedstoneClockBlockEntity(BlockPos pos, BlockState state)
	{
		super(MSBlockEntityTypes.REDSTONE_CLOCK.get(), pos, state);
	}
	
	public static void serverTick(Level level, BlockPos pos, BlockState state, RedstoneClockBlockEntity blockEntity)
	{
		if(blockEntity.clockSpeed < 20)
			blockEntity.setClockSpeed(20);
		
		if(blockEntity.tickCycle >= blockEntity.clockSpeed)
		{
			blockEntity.sendUpdate();
			blockEntity.tickCycle = 0;
		}
		blockEntity.tickCycle++;
	}
	
	private void sendUpdate()
	{
		if(level != null && level.isAreaLoaded(getBlockPos(), 1))
		{
			level.setBlock(getBlockPos(), getBlockState().setValue(RedstoneClockBlock.POWERED, true), Block.UPDATE_ALL);
			level.scheduleTick(new BlockPos(getBlockPos()), level.getBlockState(getBlockPos()).getBlock(), 10); //set to half a second
			if(!level.getBlockState(getBlockPos().above()).is(BlockTags.OCCLUDES_VIBRATION_SIGNALS) && !level.getBlockState(getBlockPos().below()).is(BlockTags.OCCLUDES_VIBRATION_SIGNALS)) //will not make a sound if a sound dampening block is above or below it
				level.playSound(null, getBlockPos(), SoundEvents.UI_BUTTON_CLICK.value(), SoundSource.BLOCKS, 0.05F, 1.2F);
		}
	}
	
	public void incrementClockSpeed(Player playerEntity)
	{
		if(clockSpeed <= 1190) //maxes out at 1200 ticks or 60 seconds
		{
			clockSpeed = clockSpeed + 10;
			level.playSound(null, getBlockPos(), SoundEvents.PISTON_EXTEND, SoundSource.BLOCKS, 0.5F, 1.6F);
		} else
		{
			clockSpeed = 20;
			level.playSound(null, getBlockPos(), SoundEvents.PISTON_CONTRACT, SoundSource.BLOCKS, 0.5F, 1.6F);
		}
		playerEntity.displayClientMessage(Component.translatable(TIME_CHANGE, (double) clockSpeed / 20), true);
	}
	
	public void decrementClockSpeed(Player playerEntity)
	{
		if(clockSpeed >= 30) //mins out at 20 ticks or 1 second
		{
			clockSpeed = clockSpeed - 10;
			level.playSound(null, getBlockPos(), SoundEvents.PISTON_CONTRACT, SoundSource.BLOCKS, 0.5F, 1.6F);
		} else
		{
			clockSpeed = 1200;
			level.playSound(null, getBlockPos(), SoundEvents.PISTON_EXTEND, SoundSource.BLOCKS, 0.5F, 1.6F);
		}
		playerEntity.displayClientMessage(Component.translatable(TIME_CHANGE, (double) clockSpeed / 20), true);
	}
	
	public void setClockSpeed(int clockSpeed)
	{
		this.clockSpeed = clockSpeed;
	}
	
	public int getClockSpeed()
	{
		return clockSpeed;
	}
	
	@Override
	public void load(CompoundTag compound)
	{
		super.load(compound);
		tickCycle = compound.getInt("tickCycle");
		clockSpeed = compound.getInt("clockSpeed");
	}
	
	@Override
	public void saveAdditional(CompoundTag compound)
	{
		super.saveAdditional(compound);
		
		compound.putInt("tickCycle", tickCycle);
		compound.putInt("clockSpeed", clockSpeed);
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