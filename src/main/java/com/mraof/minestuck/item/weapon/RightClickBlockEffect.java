package com.mraof.minestuck.item.weapon;

import com.mraof.minestuck.effects.CreativeShockEffect;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;

import java.util.function.Supplier;

public interface RightClickBlockEffect
{
	InteractionResult onClick(UseOnContext context);
	
	static RightClickBlockEffect placeFluid(Supplier<Block> fluidBlock, Supplier<Item> otherItem)
	{
		return withoutCreativeShock((context) -> {
			Level level = context.getLevel();
			Player player = context.getPlayer();
			ItemStack itemStack = context.getItemInHand();
			Direction facing = context.getClickedFace();
			BlockPos pos = context.getClickedPos().relative(facing);
			
			BlockState state = level.getBlockState(pos);
			if(state.getBlock() == Blocks.AIR || state.getBlock() == fluidBlock.get())
			{
				if(!level.isClientSide && player != null)
				{
					level.setBlockAndUpdate(pos, fluidBlock.get().defaultBlockState());
					ItemStack newItem = new ItemStack(otherItem.get(), itemStack.getCount());
					newItem.setTag(itemStack.getTag()); //It is important that the item it is switching to has the same durability
					player.setItemInHand(context.getHand(), newItem);
					level.playSound(null, pos, SoundEvents.BUCKET_EMPTY, SoundSource.BLOCKS, 1F, 2F);
					player.getCooldowns().addCooldown(otherItem.get(), 5);
				}
				return InteractionResult.SUCCESS;
			}
			return InteractionResult.PASS;
		});
	}
	
	static RightClickBlockEffect scoopBlock(Supplier<Block> validBlock)
	{
		return withoutCreativeShock((context) -> {
			Level level = context.getLevel();
			BlockPos pos = context.getClickedPos();
			Player player = context.getPlayer();
			Direction facing = context.getClickedFace();
			boolean inside = context.isInside();
			
			BlockState state = level.getBlockState(pos);
			BlockHitResult blockRayTrace = new BlockHitResult(context.getClickLocation(), facing, pos, inside);
			Item lookedAtBlockItem = state.getCloneItemStack(blockRayTrace, level, pos, player).getItem();
			
			if(player != null && state.getBlock() == validBlock.get())
			{
				if(!level.isClientSide)
				{
					if(!player.getInventory().add(new ItemStack(lookedAtBlockItem)))
					{
						player.drop(new ItemStack(lookedAtBlockItem), false);
					}
					context.getItemInHand().hurtAndBreak(1, player, (playerEntity) -> playerEntity.broadcastBreakEvent(InteractionHand.MAIN_HAND));
					level.setBlockAndUpdate(blockRayTrace.getBlockPos(), Blocks.AIR.defaultBlockState());
				}
				level.playSound(null, pos, SoundEvents.ITEM_PICKUP, SoundSource.NEUTRAL, 1F, 1F);
				
				return InteractionResult.SUCCESS;
			}
			
			return InteractionResult.PASS;
		});
	}
	
	/**
	 * Prevents effect from working if the entity is subject to the effects of creative shock
	 */
	static RightClickBlockEffect withoutCreativeShock(RightClickBlockEffect effect)
	{
		return (context) -> {
			Player player = context.getPlayer();
			if(player != null)
			{
				if(!CreativeShockEffect.doesCreativeShockLimit(player, CreativeShockEffect.LIMIT_BLOCK_PLACEMENT_AND_BREAKING))
				{
					return effect.onClick(context);
				}
			}
			
			return InteractionResult.PASS;
		};
	}
}