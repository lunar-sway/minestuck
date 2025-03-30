package com.mraof.minestuck.block.plant;

import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.common.ItemAbilities;
import net.neoforged.neoforge.common.ItemAbility;
import org.jetbrains.annotations.Nullable;

import java.util.function.Supplier;

public class StrippableFlammableLogBlock extends FlammableLogBlock
{
	private final Supplier<BlockState> strippedState;
	
	public StrippableFlammableLogBlock(Properties pProperties, Supplier<BlockState> strippedState)
	{
		super(pProperties);
		this.strippedState = strippedState;
	}
	
	@Nullable
	@Override
	public BlockState getToolModifiedState(BlockState state, UseOnContext context, ItemAbility toolAction, boolean simulate)
	{
		if(toolAction == ItemAbilities.AXE_STRIP && context.getItemInHand().canPerformAction(ItemAbilities.AXE_STRIP))
			return this.strippedState.get();
		else
			return super.getToolModifiedState(state, context, toolAction, simulate);
	}
}