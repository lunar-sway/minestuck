package com.mraof.minestuck.block;

import net.minecraft.block.BlockCake;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.stats.StatList;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public abstract class BlockCustomCake extends BlockCake
{
	protected BlockCustomCake(Properties builder)
	{
		super(builder);
	}
	
	@Override
	public boolean onBlockActivated(IBlockState state, World worldIn, BlockPos pos, EntityPlayer player, EnumHand hand, EnumFacing side, float hitX, float hitY, float hitZ)
	{
		if (!worldIn.isRemote)
		{
			return this.eatCake(worldIn, pos, state, player);
		}
		else
		{
			ItemStack itemstack = player.getHeldItem(hand);
			return this.eatCake(worldIn, pos, state, player) || itemstack.isEmpty();
		}
	}
	
	public boolean eatCake(World worldIn, BlockPos pos, IBlockState state, EntityPlayer player)
	{
		if (!player.canEat(false))
		{
			return false;
		}
		else
		{
			player.addStat(StatList.EAT_CAKE_SLICE);
			applyEffects(worldIn, pos, state, player);
			int i = state.get(BITES);
			
			if (i < 6)
			{
				worldIn.setBlockState(pos, state.with(BITES, i + 1), 3);
			}
			else
			{
				worldIn.removeBlock(pos);
			}
			
			return true;
		}
	}
	
	protected abstract void applyEffects(World worldIn, BlockPos pos, IBlockState state, EntityPlayer player);
}
