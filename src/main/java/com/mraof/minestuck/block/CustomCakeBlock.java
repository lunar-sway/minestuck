package com.mraof.minestuck.block;

import net.minecraft.core.BlockPos;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.CakeBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;

public abstract class CustomCakeBlock extends CakeBlock
{
	protected CustomCakeBlock(Properties builder)
	{
		super(builder);
	}
	
	@Override
	public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit)
	{
		if (!level.isClientSide)
		{
			return this.eatCake(level, pos, state, player) ? InteractionResult.SUCCESS : InteractionResult.PASS;
		}
		else
		{
			ItemStack itemstack = player.getItemInHand(hand);
			return this.eatCake(level, pos, state, player) ? InteractionResult.SUCCESS : itemstack.isEmpty() ? InteractionResult.CONSUME : InteractionResult.PASS;
		}
	}
	
	public boolean eatCake(Level level, BlockPos pos, BlockState state, Player player)
	{
		if (!player.canEat(false))
		{
			return false;
		}
		else
		{
			player.awardStat(Stats.EAT_CAKE_SLICE);
			applyEffects(level, pos, state, player);
			int i = state.getValue(BITES);
			
			if (i < 6)
			{
				level.setBlock(pos, state.setValue(BITES, i + 1), Block.UPDATE_ALL);
			}
			else
			{
				level.removeBlock(pos, false);
			}
			
			return true;
		}
	}
	
	protected abstract void applyEffects(Level level, BlockPos pos, BlockState state, Player player);
}