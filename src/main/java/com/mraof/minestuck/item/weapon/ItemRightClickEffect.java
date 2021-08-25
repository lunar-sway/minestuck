package com.mraof.minestuck.item.weapon;

import com.mraof.minestuck.effects.MSEffects;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.FireballEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.*;
import net.minecraft.util.math.*;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;

import java.util.List;
import java.util.function.Supplier;

public interface ItemRightClickEffect
{
	
	ItemRightClickEffect ACTIVE_HAND = (world, player, hand) -> {
		player.setActiveHand(hand);
		return ActionResult.resultConsume(player.getHeldItem(hand));
	};
	
	ItemRightClickEffect EIGHTBALL = (world, player, hand) -> {
		if(world.isRemote)
		{
			int key = player.getRNG().nextInt(20);
			ITextComponent message = new TranslationTextComponent("message.eightball." + key);
			player.sendMessage(message.applyTextStyle(TextFormatting.BLUE));
		}
		return ActionResult.resultSuccess(player.getHeldItem(hand));
	};
	
	static ItemRightClickEffect switchTo(Supplier<Item> otherItem)
	{
		return (world, player, hand) -> {
			ItemStack itemStackIn = player.getHeldItem(hand);
			if(player.isSneaking())
			{
				ItemStack newItem = new ItemStack(otherItem.get(), itemStackIn.getCount());
				newItem.setTag(itemStackIn.getTag());
				
				return ActionResult.resultSuccess(newItem);
			}
			return ActionResult.resultPass(itemStackIn);
		};
	}
	
	static ItemRightClickEffect summonFireball()
	{
		return (world, player, hand) -> {
			ItemStack itemStackIn = player.getHeldItem(hand);
			
			world.playSound(null, player.getPosX(), player.getPosY(), player.getPosZ(), SoundEvents.ENTITY_BLAZE_SHOOT, SoundCategory.PLAYERS, 1.0F, 0.8F);
			
			AxisAlignedBB axisalignedbb = player.getBoundingBox().grow(32.0D, 32.0D, 32.0D);
			List<LivingEntity> list = player.world.getEntitiesWithinAABB(LivingEntity.class, axisalignedbb);
			list.remove(player);
			if(!list.isEmpty() && !world.isRemote)
			{
				for(LivingEntity livingentity : list)
				{
					FireballEntity fireball = new FireballEntity(world, player, 0, -8.0, 0);
					fireball.explosionPower = 1;
					fireball.setPosition(livingentity.getPosX() + (player.getRNG().nextInt(6) - 3), livingentity.getPosY() + 40, livingentity.getPosZ() + (player.getRNG().nextInt(6) - 3));
					player.getCooldownTracker().setCooldown(itemStackIn.getItem(), 20);
					itemStackIn.damageItem(2, player, playerEntity -> playerEntity.sendBreakAnimation(Hand.MAIN_HAND));
					world.addEntity(fireball);
				}
			} else if(!world.isRemote)
			{
				FireballEntity fireball = new FireballEntity(world, player, 0, -8.0, 0);
				fireball.explosionPower = 1;
				fireball.setPosition(player.getPosX() + (player.getRNG().nextInt(20) - 10), player.getPosY() + 40, player.getPosZ() + (player.getRNG().nextInt(20) - 10));
				player.getCooldownTracker().setCooldown(itemStackIn.getItem(), 20);
				itemStackIn.damageItem(2, player, playerEntity -> playerEntity.sendBreakAnimation(Hand.MAIN_HAND));
				world.addEntity(fireball);
			}
			return ActionResult.resultPass(itemStackIn);
		};
	}
	
	static ItemRightClickEffect extinguishFire(int mod)
	{
		return withoutCreativeShock((world, player, hand) -> {
			ItemStack itemStackIn = player.getHeldItem(hand);
			
			if(!world.isRemote)
			{
				world.playSound(null, player.getPosX(), player.getPosY(), player.getPosZ(), SoundEvents.ENTITY_ENDER_DRAGON_FLAP, SoundCategory.PLAYERS, 1.0F, 1.4F);
				
				for(BlockPos blockPos : BlockPos.getAllInBoxMutable(player.getPosition().add(2 * mod, mod, 2 * mod), player.getPosition().add(-2 * mod, -1 * mod, -2 * mod)))
				{
					BlockState blockState = world.getBlockState(blockPos);
					if(blockState.getBlock() == Blocks.FIRE)
					{
						world.setBlockState(blockPos, Blocks.AIR.getDefaultState());
						world.playSound(null, blockPos.getX(), blockPos.getY(), blockPos.getZ(), SoundEvents.BLOCK_FIRE_EXTINGUISH, SoundCategory.BLOCKS, 0.5F, 1.0F);
					}
				}
				
				AxisAlignedBB axisalignedbb = player.getBoundingBox().grow(2 * mod, mod, 2 * mod);
				List<LivingEntity> list = player.world.getEntitiesWithinAABB(LivingEntity.class, axisalignedbb);
				for(LivingEntity livingentity : list)
				{
					if(livingentity.getFireTimer() > 0)
					{
						livingentity.extinguish();
						world.playSound(null, livingentity.getPosX(), livingentity.getPosY(), livingentity.getPosZ(), SoundEvents.BLOCK_FIRE_EXTINGUISH, SoundCategory.NEUTRAL, 0.5F, 1.0F);
					}
				}
				
				player.swing(hand, true);
				player.getCooldownTracker().setCooldown(itemStackIn.getItem(), 15);
				itemStackIn.damageItem(1, player, playerEntity -> playerEntity.sendBreakAnimation(Hand.MAIN_HAND));
			}
			return ActionResult.resultPass(itemStackIn);
		});
	}
	
	static ItemRightClickEffect absorbFluid(Supplier<Block> fluidBlock, Supplier<Item> otherItem)
	{
		return withoutCreativeShock((world, player, hand) -> {
			Vec3d eyePos = player.getEyePosition(1.0F);
			Vec3d lookVec = player.getLookVec();
			BlockState state;
			ItemStack itemStack = player.getHeldItem(hand);
			
			for(int step = 0; step < player.getAttribute(PlayerEntity.REACH_DISTANCE).getValue() * 10; step++) //raytraces from the players current eye position to the maximum their reach distance allows
			{
				Vec3d vecPos = eyePos.add(lookVec.scale(step / 10D));
				BlockPos blockPos = new BlockPos(vecPos);
				state = world.getBlockState(blockPos);
				
				if(state.getBlock() == fluidBlock.get() && state.getFluidState().isSource()) //may cause error if fed non-fluid "fluidBlock" parameter
				{
					world.setBlockState(blockPos, Blocks.AIR.getDefaultState());
					ItemStack newItem = new ItemStack(otherItem.get(), itemStack.getCount());
					newItem.setTag(itemStack.getTag()); //It is important that the item it is switching to has the same durability
					world.playSound(null, blockPos, SoundEvents.ITEM_BUCKET_FILL, SoundCategory.BLOCKS, 1F, 2F);
					player.getCooldownTracker().setCooldown(otherItem.get(), 5);
					return ActionResult.resultSuccess(newItem);
				}
			}
			return ActionResult.resultFail(itemStack);
		});
	}
	
	/**
	 * Prevents effect from working if the entity is subject to the effects of creative shock
	 */
	static ItemRightClickEffect withoutCreativeShock(ItemRightClickEffect effect) //TODO action result for client side may not work
	{
		return (world, player, hand) -> {
			ItemStack itemStackIn = player.getHeldItem(hand);
			
			if(!player.isPotionActive(MSEffects.CREATIVE_SHOCK.get()) || player.isCreative())
			{
				effect.onRightClick(world, player, hand);
				if(effect.onRightClick(world, player, hand).getType().isSuccess())
					return ActionResult.resultSuccess(itemStackIn);
			}
			
			return ActionResult.resultPass(itemStackIn);
		};
	}
	
	ActionResult<ItemStack> onRightClick(World world, PlayerEntity player, Hand hand);
}