package com.mraof.minestuck.block;

import com.mraof.minestuck.util.Debug;
import net.minecraft.block.BlockButton;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.SoundEvents;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.Random;

public class BlockButtonSpecial extends BlockButton
{
	
	public final boolean explosive, wooden;
	
	public BlockButtonSpecial(Properties builder, boolean explosive, boolean wooden)
	{
		super(wooden, builder);
		this.explosive = explosive;
		this.wooden = wooden;
	}
	
	@Override
	public void tick(IBlockState state, World worldIn, BlockPos pos, Random random)
	{
		boolean b = state.get(POWERED);
		super.tick(state, worldIn, pos, random);
		if(worldIn.getBlockState(pos).getBlock() != this)
		{
			Debug.warn("Tick update without the correct block/position?");
			return;
		}
		boolean b1 = worldIn.getBlockState(pos).get(POWERED);
		if(explosive && b && !b1)
		{
			worldIn.removeBlock(pos);
			worldIn.createExplosion(null, (double)pos.getX() + 0.5D, (double)pos.getY() + 0.5D, (double)pos.getZ() + 0.5D, 1.5F, true);
		}
	}
	
	@Override
	protected SoundEvent getSoundEvent(boolean clickOn)
	{
		if(clickOn)
		{
			if(wooden)
				return SoundEvents.BLOCK_WOODEN_BUTTON_CLICK_ON;
			else return SoundEvents.BLOCK_STONE_BUTTON_CLICK_ON;
		} else
		{
			if(wooden)
				return SoundEvents.BLOCK_WOODEN_BUTTON_CLICK_OFF;
			else return SoundEvents.BLOCK_STONE_BUTTON_CLICK_OFF;
		}
	}
}