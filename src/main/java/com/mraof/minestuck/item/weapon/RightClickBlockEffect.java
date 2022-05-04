package com.mraof.minestuck.item.weapon;

import com.mraof.minestuck.effects.CreativeShockEffect;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUseContext;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.world.World;

import java.util.function.Supplier;

public interface RightClickBlockEffect
{
	ActionResultType onClick(ItemUseContext context);
	
	static RightClickBlockEffect placeFluid(Supplier<Block> fluidBlock, Supplier<Item> otherItem)
	{
		return withoutCreativeShock((context) -> {
			World worldIn = context.getLevel();
			PlayerEntity player = context.getPlayer();
			ItemStack itemStack = context.getItemInHand();
			Direction facing = context.getClickedFace();
			BlockPos pos = context.getClickedPos().relative(facing);
			
			BlockState state = worldIn.getBlockState(pos);
			if(state.getBlock() == Blocks.AIR || state.getBlock() == fluidBlock.get())
			{
				if(!worldIn.isClientSide && player != null)
				{
					worldIn.setBlockAndUpdate(pos, fluidBlock.get().defaultBlockState());
					ItemStack newItem = new ItemStack(otherItem.get(), itemStack.getCount());
					newItem.setTag(itemStack.getTag()); //It is important that the item it is switching to has the same durability
					player.setItemInHand(context.getHand(), newItem);
					worldIn.playSound(null, pos, SoundEvents.BUCKET_EMPTY, SoundCategory.BLOCKS, 1F, 2F);
					player.getCooldowns().addCooldown(otherItem.get(), 5);
				}
				return ActionResultType.SUCCESS;
			}
			return ActionResultType.PASS;
		});
	}
	
	static RightClickBlockEffect scoopBlock(Supplier<Block> validBlock)
	{
		return withoutCreativeShock((context) -> {
			World worldIn = context.getLevel();
			BlockPos pos = context.getClickedPos();
			PlayerEntity player = context.getPlayer();
			Direction facing = context.getClickedFace();
			boolean inside = context.isInside();
			
			BlockState state = worldIn.getBlockState(pos);
			BlockRayTraceResult blockRayTrace = new BlockRayTraceResult(context.getClickLocation(), facing, pos, inside);
			Item lookedAtBlockItem = state.getPickBlock(blockRayTrace, worldIn, pos, player).getItem();
			
			if(player != null && state.getBlock() == validBlock.get().getBlock())
			{
				if(!worldIn.isClientSide)
				{
					if(!player.inventory.add(new ItemStack(lookedAtBlockItem)))
					{
						player.drop(new ItemStack(lookedAtBlockItem), false);
					}
					context.getItemInHand().hurtAndBreak(1, player, (playerEntity) -> playerEntity.broadcastBreakEvent(Hand.MAIN_HAND));
					worldIn.setBlockAndUpdate(blockRayTrace.getBlockPos(), Blocks.AIR.defaultBlockState());
				}
				worldIn.playSound(null, pos, SoundEvents.ITEM_PICKUP, SoundCategory.NEUTRAL, 1F, 1F);
				
				return ActionResultType.SUCCESS;
			}
			
			return ActionResultType.PASS;
		});
	}
	
	/**
	 * Prevents effect from working if the entity is subject to the effects of creative shock
	 */
	static RightClickBlockEffect withoutCreativeShock(RightClickBlockEffect effect)
	{
		return (context) -> {
			PlayerEntity player = context.getPlayer();
			if(player != null)
			{
				if(!CreativeShockEffect.doesCreativeShockLimit(player, CreativeShockEffect.LIMIT_BLOCK_PLACEMENT_AND_BREAKING))
				{
					return effect.onClick(context);
				}
			}
			
			return ActionResultType.PASS;
		};
	}
}