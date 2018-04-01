package com.mraof.minestuck.block;

import com.mraof.minestuck.item.TabMinestuck;
import com.mraof.minestuck.util.Debug;
import net.minecraft.block.BlockButton;
import net.minecraft.block.SoundType;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.Random;

public class BlockButtonSpecial extends BlockButton
{
	
	public final boolean explosive, wooden;
	
	public BlockButtonSpecial(boolean wooden, boolean explosive)
	{
		super(wooden);
		this.explosive = explosive;
		this.wooden = wooden;
		setCreativeTab(TabMinestuck.instance);
		setHardness(0.5F);
		if(wooden)
			setSoundType(SoundType.WOOD);
		else setSoundType(SoundType.STONE);
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
			worldIn.createExplosion((Entity)null, (double)pos.getX() + 0.5D, (double)pos.getY() + 0.5D, (double)pos.getZ() + 0.5D, 1.5F, true);
		}
	}
	
	@Override
	protected void playClickSound(EntityPlayer p_185615_1_, World player, BlockPos pos)
	{
		if(wooden)
			player.playSound(p_185615_1_, pos, SoundEvents.BLOCK_WOOD_BUTTON_CLICK_ON, SoundCategory.BLOCKS, 0.3F, 0.6F);
		else player.playSound(p_185615_1_, pos, SoundEvents.BLOCK_STONE_BUTTON_CLICK_ON, SoundCategory.BLOCKS, 0.3F, 0.6F);
	}
	
	@Override
	protected void playReleaseSound(World worldIn, BlockPos pos)
	{
		if(wooden)
			worldIn.playSound((EntityPlayer)null, pos, SoundEvents.BLOCK_WOOD_BUTTON_CLICK_OFF, SoundCategory.BLOCKS, 0.3F, 0.5F);
		else worldIn.playSound((EntityPlayer)null, pos, SoundEvents.BLOCK_STONE_BUTTON_CLICK_OFF, SoundCategory.BLOCKS, 0.3F, 0.5F);
	}
}