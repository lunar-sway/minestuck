package com.mraof.minestuck.block;

import java.util.Random;

import com.mraof.minestuck.Minestuck;
import com.mraof.minestuck.util.Debug;

import net.minecraft.block.BlockButton;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;

public class BlockButtonSpecial extends BlockButton
{
	
	public final boolean explosive;
	
	public BlockButtonSpecial(boolean wooden, boolean explosive)
	{
		super(wooden);
		this.explosive = explosive;
		setCreativeTab(Minestuck.tabMinestuck);
		setHardness(0.5F);
		if(wooden)
			setStepSound(soundTypeWood);
		else setStepSound(soundTypePiston);
	}
	
	@Override
	public void updateTick(World worldIn, BlockPos pos, IBlockState state, Random rand)
	{
		boolean b = (Boolean) state.getValue(POWERED);
		super.updateTick(worldIn, pos, state, rand);
		if(worldIn.getBlockState(pos).getBlock() != this)
		{
			Debug.warn("Tick update without the correct block/position?");
			return;
		}
		boolean b1 = (Boolean) worldIn.getBlockState(pos).getValue(POWERED);
		if(explosive && b && !b1)
		{
			worldIn.setBlockToAir(pos);
			worldIn.createExplosion((Entity)null, (double)pos.getX() + 0.5D, (double)pos.getY() + 0.5D, (double)pos.getZ() + 0.5D, 2.0F, true);
		}
	}
	
}