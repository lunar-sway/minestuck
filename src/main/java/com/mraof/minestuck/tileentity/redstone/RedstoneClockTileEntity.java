package com.mraof.minestuck.tileentity.redstone;

import com.mraof.minestuck.block.redstone.RedstoneClockBlock;
import com.mraof.minestuck.tileentity.MSTileEntityTypes;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SUpdateTileEntityPacket;
import net.minecraft.tags.ItemTags;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.common.util.Constants;

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
		if(level == null)
			return;
		
		if(tickCycle >= clockSpeed)
		{
			sendUpdate();
			tickCycle = 0;
		}
		tickCycle++;
	}
	
	private void sendUpdate()
	{
		if(level != null && level.isAreaLoaded(getBlockPos(), 1))
		{
			level.setBlock(getBlockPos(), getBlockState().setValue(RedstoneClockBlock.POWERED, true), Constants.BlockFlags.DEFAULT);
			level.getBlockTicks().scheduleTick(new BlockPos(getBlockPos()), level.getBlockState(getBlockPos()).getBlock(), 10); //set to half a second
			if(!level.getBlockState(getBlockPos().above()).getBlock().asItem().is(ItemTags.WOOL) && !level.getBlockState(getBlockPos().below()).getBlock().asItem().is(ItemTags.WOOL)) //will not make a sound if wool is above or below the block
				level.playSound(null, getBlockPos(), SoundEvents.UI_BUTTON_CLICK, SoundCategory.BLOCKS, 0.05F, 1.2F);
		}
	}
	
	public void incrementClockSpeed(PlayerEntity playerEntity)
	{
		if(clockSpeed <= 1190) //maxes out at 1200 ticks or 60 seconds
		{
			clockSpeed = clockSpeed + 10;
			level.playSound(null, getBlockPos(), SoundEvents.PISTON_EXTEND, SoundCategory.BLOCKS, 0.5F, 1.6F);
		} else
		{
			clockSpeed = 20;
			level.playSound(null, getBlockPos(), SoundEvents.PISTON_CONTRACT, SoundCategory.BLOCKS, 0.5F, 1.6F);
		}
		playerEntity.displayClientMessage(new TranslationTextComponent(TIME_CHANGE, (double) clockSpeed / 20), true);
	}
	
	public void decrementClockSpeed(PlayerEntity playerEntity)
	{
		if(clockSpeed >= 30) //mins out at 20 ticks or 1 second
		{
			clockSpeed = clockSpeed - 10;
			level.playSound(null, getBlockPos(), SoundEvents.PISTON_CONTRACT, SoundCategory.BLOCKS, 0.5F, 1.6F);
		} else
		{
			clockSpeed = 1200;
			level.playSound(null, getBlockPos(), SoundEvents.PISTON_EXTEND, SoundCategory.BLOCKS, 0.5F, 1.6F);
		}
		playerEntity.displayClientMessage(new TranslationTextComponent(TIME_CHANGE, (double) clockSpeed / 20), true);
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
	public void onLoad()
	{
		super.onLoad();
		this.clockSpeed = 60;
	}
	
	@Override
	public void load(BlockState state, CompoundNBT compound)
	{
		super.load(state, compound);
		tickCycle = compound.getInt("tickCycle");
		clockSpeed = compound.getInt("clockSpeed");
	}
	
	@Override
	public CompoundNBT save(CompoundNBT compound)
	{
		super.save(compound);
		
		compound.putInt("tickCycle", tickCycle);
		compound.putInt("clockSpeed", clockSpeed);
		
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