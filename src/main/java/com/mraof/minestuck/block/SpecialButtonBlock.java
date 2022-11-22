package com.mraof.minestuck.block;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.block.ButtonBlock;
import net.minecraft.world.level.block.state.BlockState;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Random;

public class SpecialButtonBlock extends ButtonBlock
{
	private static final Logger LOGGER = LogManager.getLogger();
	
	public final boolean explosive, wooden;
	
	public SpecialButtonBlock(Properties builder, boolean explosive, boolean wooden)
	{
		super(wooden, builder);
		this.explosive = explosive;
		this.wooden = wooden;
	}
	
	@Override
	public void tick(BlockState state, ServerLevel level, BlockPos pos, Random random)
	{
		boolean b = state.getValue(POWERED);
		super.tick(state, level, pos, random);
		if(level.getBlockState(pos).getBlock() != this)
		{
			LOGGER.warn("Tick update without the correct block/position?");
			return;
		}
		boolean b1 = level.getBlockState(pos).getValue(POWERED);
		if(explosive && b && !b1)
		{
			level.removeBlock(pos, false);
			level.explode(null, (double)pos.getX() + 0.5D, (double)pos.getY() + 0.5D, (double)pos.getZ() + 0.5D, 1.5F, Explosion.BlockInteraction.DESTROY);
		}
	}
	
	@Override
	protected SoundEvent getSound(boolean clickOn)
	{
		if(clickOn)
		{
			if(wooden)
				return SoundEvents.WOODEN_BUTTON_CLICK_ON;
			else return SoundEvents.STONE_BUTTON_CLICK_ON;
		} else
		{
			if(wooden)
				return SoundEvents.WOODEN_BUTTON_CLICK_OFF;
			else return SoundEvents.STONE_BUTTON_CLICK_OFF;
		}
	}
}