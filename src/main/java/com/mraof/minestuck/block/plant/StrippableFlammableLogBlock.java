package com.mraof.minestuck.block.plant;

import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraftforge.common.ToolActions;

import java.util.function.Supplier;

public class StrippableFlammableLogBlock extends FlammableLogBlock
{
	private final Supplier<BlockState> strippedState;
	
	public StrippableFlammableLogBlock(Properties pProperties, Supplier<BlockState> strippedState)
	{
		super(pProperties);
		this.strippedState = strippedState;
	}
	
	@Override
	@SuppressWarnings("deprecation")
	public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit)
	{
		ItemStack itemStack = player.getItemInHand(hand);
		
		if(itemStack.canPerformAction(ToolActions.AXE_STRIP))
		{
			if(player instanceof ServerPlayer)
			{
				CriteriaTriggers.ITEM_USED_ON_BLOCK.trigger((ServerPlayer) player, pos, itemStack);
			}
			
			level.playSound(player, pos, SoundEvents.AXE_STRIP, SoundSource.BLOCKS, 1.0F, 1.0F);
			
			level.setBlock(pos, strippedState.get().setValue(AXIS, state.getValue(StrippableFlammableLogBlock.AXIS)), 11);
			level.gameEvent(GameEvent.BLOCK_CHANGE, pos, GameEvent.Context.of(player, state));
			itemStack.hurtAndBreak(1, player, (playerEntity) -> {
				playerEntity.broadcastBreakEvent(hand);
			});
			
			return InteractionResult.sidedSuccess(level.isClientSide);
		}
		
		return InteractionResult.PASS;
	}
}