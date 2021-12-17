package com.mraof.minestuck.tileentity.redstone;

import com.mraof.minestuck.block.redstone.RedstoneClockBlock;
import com.mraof.minestuck.tileentity.MSTileEntityTypes;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SUpdateTileEntityPacket;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TranslationTextComponent;

public class RedstoneClockTileEntity extends TileEntity implements ITickableTileEntity
{
	private int tickCycle;
	private int clockSpeed;
	
	public static final String TIME_CHANGE = "block.minestuck.redstone_clock.time_change";
	
	public RedstoneClockTileEntity()
	{
		super(MSTileEntityTypes.REDSTONE_CLOCK.get());
	}
	
	@Override
	public void tick()
	{
		if(world == null)
			return; // Forge: prevent loading unloaded chunks
		
		if(tickCycle >= clockSpeed)
		{
			sendUpdate();
			tickCycle = 0;
		}
		tickCycle++;
	}
	
	private void sendUpdate()
	{
		if(world != null && world.isAreaLoaded(pos, 1))
		{
			world.setBlockState(pos, getBlockState().with(RedstoneClockBlock.POWERED, true));
			world.getPendingBlockTicks().scheduleTick(new BlockPos(pos), world.getBlockState(pos).getBlock(), 10); //set to half a second
			world.playSound(null, pos, SoundEvents.UI_BUTTON_CLICK, SoundCategory.BLOCKS, 0.5F, 1.2F);
		}
	}
	
	public void incrementClockSpeed(PlayerEntity playerEntity)
	{
		if(clockSpeed <= 1190) //maxes out at 1200 ticks or 60 seconds
		{
			clockSpeed = clockSpeed + 10;
			world.playSound(null, pos, SoundEvents.BLOCK_PISTON_EXTEND, SoundCategory.BLOCKS, 0.5F, 1.6F);
		} else
		{
			clockSpeed = 20;
			world.playSound(null, pos, SoundEvents.BLOCK_PISTON_CONTRACT, SoundCategory.BLOCKS, 0.5F, 1.6F);
		}
		playerEntity.sendStatusMessage(new TranslationTextComponent(TIME_CHANGE, (double) clockSpeed / 20), true);
	}
	
	public void decrementClockSpeed(PlayerEntity playerEntity)
	{
		if(clockSpeed >= 30) //mins out at 20 ticks or 1 second
		{
			clockSpeed = clockSpeed - 10;
			world.playSound(null, pos, SoundEvents.BLOCK_PISTON_CONTRACT, SoundCategory.BLOCKS, 0.2F, 1.6F);
		} else
		{
			clockSpeed = 1200;
			world.playSound(null, pos, SoundEvents.BLOCK_PISTON_EXTEND, SoundCategory.BLOCKS, 0.2F, 1.6F);
		}
		playerEntity.sendStatusMessage(new TranslationTextComponent(TIME_CHANGE, (double) clockSpeed / 20), true);
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
	public void read(CompoundNBT compound)
	{
		super.read(compound);
		tickCycle = compound.getInt("tickCycle");
		clockSpeed = compound.getInt("clockSpeed");
	}
	
	@Override
	public CompoundNBT write(CompoundNBT compound)
	{
		super.write(compound);
		
		compound.putInt("tickCycle", tickCycle);
		compound.putInt("clockSpeed", clockSpeed);
		
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