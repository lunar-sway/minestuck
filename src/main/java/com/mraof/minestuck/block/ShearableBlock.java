package com.mraof.minestuck.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.BlockHitResult;
import net.neoforged.neoforge.common.ItemAbilities;
import net.neoforged.neoforge.registries.*;

public class ShearableBlock extends Block
{
	private final DeferredBlock<Block> block; //what the sheared block turns into
	private final DeferredItem<Item> item; //the item it drops
	private final int stackSize; //how many of the item it drops
	
	public ShearableBlock(BlockBehaviour.Properties properties, DeferredBlock<Block> block, DeferredItem<Item> item, int stackSize) {
		super(properties);
		this.block = block;
		this.item = item;
		this.stackSize = stackSize;
	}
	
	protected ItemInteractionResult useItemOn(ItemStack itemStack, BlockState blockState, Level level, BlockPos blockPos, Player player, InteractionHand interactionHand, BlockHitResult blockHitResult) {
		if (!itemStack.canPerformAction(ItemAbilities.SHEARS_CARVE)) {
			return super.useItemOn(itemStack, blockState, level, blockPos, player, interactionHand, blockHitResult);
		} else if (level.isClientSide) {
			return ItemInteractionResult.sidedSuccess(level.isClientSide);
		} else {
			Direction direction = blockHitResult.getDirection();
			Direction direction1 = direction.getAxis() == Direction.Axis.Y ? player.getDirection().getOpposite() : direction;
			
			level.playSound((Player)null, blockPos, SoundEvents.BEEHIVE_SHEAR, SoundSource.BLOCKS, 1.0F, 1.0F);
			level.setBlock(blockPos, (BlockState)this.block.get().defaultBlockState(), 11);
			
			ItemEntity itementity = new ItemEntity(level, (double)blockPos.getX() + (double)0.5F + (double)direction1.getStepX() * 0.65, (double)blockPos.getY() + 0.1, (double)blockPos.getZ() + (double)0.5F + (double)direction1.getStepZ() * 0.65, new ItemStack(this.item.get(), stackSize));
			itementity.setDeltaMovement(0.05 * (double)direction1.getStepX() + level.random.nextDouble() * 0.02, 0.05, 0.05 * (double)direction1.getStepZ() + level.random.nextDouble() * 0.02);
			level.addFreshEntity(itementity);
			
			itemStack.hurtAndBreak(1, player, LivingEntity.getSlotForHand(interactionHand));
			level.gameEvent(player, GameEvent.SHEAR, blockPos);
			player.awardStat(Stats.ITEM_USED.get(Items.SHEARS));
			
			return ItemInteractionResult.sidedSuccess(level.isClientSide);
		}
	}

}
