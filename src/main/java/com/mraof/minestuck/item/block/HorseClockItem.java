package com.mraof.minestuck.item.block;

import com.mraof.minestuck.block.HorseClockMultiblock;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;

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
			//Level level = context.getLevel();
			//BlockPos placementPos = getPlacementPos(context);
			
			if (player == null || !player.getAbilities().instabuild) {
				itemstack.shrink(1);
			}
			
			//level.gameEvent(player, GameEvent.BLOCK_PLACE, placementPos);
			//level.playSound(null, placementPos, SoundEvents.WOOD_PLACE, SoundSource.BLOCKS, 1.0F, 0.8F);
		}
		
		return superBool;
	}
	
	/*private BlockPos getPlacementPos(BlockPlaceContext context)
	{
		BlockPos pos = context.getClickedPos();
		if(!context.getLevel().getBlockState(pos).canBeReplaced(context))
		{
			pos = pos.above();
		}
		Direction facing = context.getHorizontalDirection().getOpposite();
		
		return getPlacementPos(pos, facing, context.getClickLocation().x - pos.getX(), context.getClickLocation().z - pos.getZ());
	}*/
}