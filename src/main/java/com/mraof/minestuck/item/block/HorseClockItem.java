package com.mraof.minestuck.item.block;

import com.mraof.minestuck.block.HorseClockMultiblock;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.block.state.BlockState;

public class HorseClockItem extends MultiblockItem
{
	public HorseClockItem(HorseClockMultiblock multiblock, Properties properties)
	{
		super(multiblock, properties);
	}
	
	@Override
	protected boolean placeBlock(BlockPlaceContext context, BlockState newState)
	{
		boolean superBool = super.placeBlock(context, newState);
		
		if(superBool)
		{
			Player player = context.getPlayer();
			ItemStack itemstack = context.getItemInHand();
			if (player == null || !player.getAbilities().instabuild) {
				itemstack.shrink(1);
			}
		}
		
		return superBool;
	}
}