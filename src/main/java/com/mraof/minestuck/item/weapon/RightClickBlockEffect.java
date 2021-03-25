package com.mraof.minestuck.item.weapon;

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
		return (context) -> {
			World worldIn = context.getWorld();
			PlayerEntity player = context.getPlayer();
			ItemStack itemStack = context.getItem();
			Direction facing = context.getFace();
			BlockPos pos = context.getPos().offset(facing);
			
			if(!worldIn.isRemote && player != null)
			{
				BlockState state = worldIn.getBlockState(pos);
				
				if(state.getBlock() == Blocks.AIR || state.getBlock() == fluidBlock.get())
				{
					worldIn.setBlockState(pos, fluidBlock.get().getDefaultState());
					ItemStack newItem = new ItemStack(otherItem.get(), itemStack.getCount());
					newItem.setTag(itemStack.getTag()); //It is important that the item it is switching to has the same durability
					player.setHeldItem(context.getHand(), newItem);
					worldIn.playSound(null, pos, SoundEvents.ITEM_BUCKET_EMPTY, SoundCategory.BLOCKS, 1F, 2F);
					player.getCooldownTracker().setCooldown(otherItem.get(), 5);
					return ActionResultType.SUCCESS;
				}
			}
			return ActionResultType.PASS;
		};
	}
	
	static RightClickBlockEffect scoopBlock(Supplier<Block> validBlock)
	{
		return (context) -> {
			World worldIn = context.getWorld();
			BlockPos pos = context.getPos();
			PlayerEntity player = context.getPlayer();
			Direction facing = context.getFace();
			boolean inside = context.isInside();
			
			if(!worldIn.isRemote && player != null)
			{
				BlockState state = worldIn.getBlockState(pos);
				
				if(state.getBlock() == validBlock.get().getBlock())
				{
					ItemStack block = state.getPickBlock(new BlockRayTraceResult(context.getHitVec(), facing, pos, inside), worldIn, pos, player);
					player.inventory.addItemStackToInventory(block);
					context.getItem().damageItem(1, player, (playerEntity) -> playerEntity.sendBreakAnimation(Hand.MAIN_HAND));
					
					worldIn.playSound(null, pos, SoundEvents.ENTITY_ITEM_PICKUP, SoundCategory.NEUTRAL, 1F, 1F);
					worldIn.setBlockState(pos, Blocks.AIR.getDefaultState());
					
					return ActionResultType.SUCCESS;
				}
			}
			
			return ActionResultType.PASS;
		};
	}
}