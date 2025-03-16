package com.mraof.minestuck.block;

import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.common.ItemAbilities;
import net.neoforged.neoforge.common.ItemAbility;

import javax.annotation.Nullable;
import java.util.function.Supplier;

public class TillableBlock extends Block
{
	private final Supplier<BlockState> tilledState;
	
	public TillableBlock(Properties pProperties, Supplier<BlockState> tilledState)
	{
		super(pProperties);
		this.tilledState = tilledState;
	}
	
	@Nullable
	@Override
	public BlockState getToolModifiedState(BlockState state, UseOnContext context, ItemAbility toolAction, boolean simulate)
	{
		if(toolAction == ItemAbilities.HOE_TILL && context.getItemInHand().canPerformAction(ItemAbilities.HOE_TILL))
			return this.tilledState.get();
		else
			return super.getToolModifiedState(state, context, toolAction, simulate);
	}
}
