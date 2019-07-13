package com.mraof.minestuck.block;

import net.minecraft.block.BlockState;
import net.minecraft.block.CakeBlock;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.stats.Stats;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.world.World;

public abstract class CustomCakeBlock extends CakeBlock
{
	protected CustomCakeBlock(Properties builder)
	{
		super(builder);
	}
	
	@Override
	public boolean onBlockActivated(BlockState state, World worldIn, BlockPos pos, PlayerEntity player, Hand hand, BlockRayTraceResult hit)
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
	
	public boolean eatCake(World worldIn, BlockPos pos, BlockState state, PlayerEntity player)
	{
		if (!player.canEat(false))
		{
			return false;
		}
		else
		{
			player.addStat(Stats.EAT_CAKE_SLICE);
			applyEffects(worldIn, pos, state, player);
			int i = state.get(BITES);
			
			if (i < 6)
			{
				worldIn.setBlockState(pos, state.with(BITES, i + 1), 3);
			}
			else
			{
				worldIn.removeBlock(pos, false);
			}
			
			return true;
		}
	}
	
	protected abstract void applyEffects(World worldIn, BlockPos pos, BlockState state, PlayerEntity player);
}