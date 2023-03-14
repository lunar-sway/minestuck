package com.mraof.minestuck.item.weapon;

import com.mraof.minestuck.effects.CreativeShockEffect;
import com.mraof.minestuck.particle.MSParticleTypes;
import com.mraof.minestuck.player.IdentifierHandler;
import com.mraof.minestuck.player.PlayerData;
import com.mraof.minestuck.player.PlayerIdentifier;
import com.mraof.minestuck.player.PlayerSavedData;
import net.minecraft.ChatFormatting;
import net.minecraft.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.LargeFireball;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.*;
import net.minecraftforge.common.ForgeMod;

import java.util.List;
import java.util.function.Supplier;

public interface ItemRightClickEffect
{
	
	ItemRightClickEffect ACTIVE_HAND = (world, player, hand) -> {
		player.startUsingItem(hand);
		return InteractionResultHolder.consume(player.getItemInHand(hand));
	};
	
	ItemRightClickEffect EIGHTBALL = (world, player, hand) -> {
		if(world.isClientSide)
		{
			int key = player.getRandom().nextInt(20);
			TranslatableComponent message = new TranslatableComponent("message.eightball." + key);
			player.sendMessage(message.withStyle(ChatFormatting.BLUE), Util.NIL_UUID);
		}
		return InteractionResultHolder.success(player.getItemInHand(hand));
	};
	
	static ItemRightClickEffect switchTo(Supplier<Item> otherItem)
	{
		return (world, player, hand) -> {
			ItemStack itemStackIn = player.getItemInHand(hand);
			if(player.isShiftKeyDown())
			{
				ItemStack newItem = new ItemStack(otherItem.get(), itemStackIn.getCount());
				newItem.setTag(itemStackIn.getTag());
				
				return InteractionResultHolder.success(newItem);
			}
			return InteractionResultHolder.pass(itemStackIn);
		};
	}
	
	static ItemRightClickEffect caneSwitchTo(Supplier<Item> otherItem, int Swings, int Damage, int cost)
	{
		return (world, player, hand) -> {
			ItemStack heldItem = player.getItemInHand(hand);
			HitResult rayTraceResult = getPlayerPOVHitResult(world, player);//finds the target
			PlayerIdentifier identified = IdentifierHandler.encode(player);//encodes the player
			PlayerData data = PlayerSavedData.getData(identified, world);//gets the player's data
			
			if(rayTraceResult instanceof EntityHitResult entityHitResult)
			{
				LivingEntity target = (LivingEntity) entityHitResult.getEntity();
				data.takeCaegers(cost);//this reducts caegers directly from the player data
				for(int swing = 0; swing <= Swings; swing++)
				{
					target.hurt(DamageSource.sting(player), Damage);//deals damage to the target
					player.swing(hand);//swings the player's hand
					player.getLevel().addParticle(MSParticleTypes.SLASH.get(),//spawns the slash particle
							player.getX(), player.getY(), player.getZ(), 0d, 0d, 0d);
				}
			}
			ItemStack newItem = new ItemStack(otherItem.get(), heldItem.getCount());//switches the item
			newItem.setTag(heldItem.getTag());
			player.getCooldowns().removeCooldown(otherItem.get());//removes it's cooldown
			
			return InteractionResultHolder.success(newItem);
		};
	}
	
	static ItemRightClickEffect quickSwitchTo(Supplier<Item> otherItem)
	{
		//this is a version of the switchTo function that doesn't require crouching
		return (world, player, hand) -> {
			ItemStack heldItem = player.getItemInHand(hand);
			ItemStack newItem = new ItemStack(otherItem.get(), heldItem.getCount());
			newItem.setTag(heldItem.getTag());
			
			return InteractionResultHolder.success(newItem);
		};
	}
	
	static ItemRightClickEffect summonFireball()
	{
		return (world, player, hand) -> {
			ItemStack itemStackIn = player.getItemInHand(hand);
			world.playSound(null, player.getX(), player.getY(), player.getZ(), SoundEvents.BLAZE_SHOOT, SoundSource.PLAYERS, 1.0F, 0.8F);
			
			AABB axisalignedbb = player.getBoundingBox().inflate(32.0D, 32.0D, 32.0D);
			List<LivingEntity> list = player.level.getEntitiesOfClass(LivingEntity.class, axisalignedbb);
			list.remove(player);
			if(!list.isEmpty() && !world.isClientSide)
			{
				for(LivingEntity livingentity : list)
				{
					LargeFireball fireball = new LargeFireball(world, player, 0, -8.0, 0, 1);
					fireball.setPos(livingentity.getX() + (player.getRandom().nextInt(6) - 3), livingentity.getY() + 40, livingentity.getZ() + (player.getRandom().nextInt(6) - 3));
					player.getCooldowns().addCooldown(itemStackIn.getItem(), 20);
					itemStackIn.hurtAndBreak(2, player, playerEntity -> playerEntity.broadcastBreakEvent(InteractionHand.MAIN_HAND));
					world.addFreshEntity(fireball);
				}
			} else if(!world.isClientSide)
			{
				LargeFireball fireball = new LargeFireball(world, player, 0, -8.0, 0, 1);
				fireball.setPos(player.getX() + (player.getRandom().nextInt(20) - 10), player.getY() + 40, player.getZ() + (player.getRandom().nextInt(20) - 10));
				player.getCooldowns().addCooldown(itemStackIn.getItem(), 20);
				itemStackIn.hurtAndBreak(2, player, playerEntity -> playerEntity.broadcastBreakEvent(InteractionHand.MAIN_HAND));
				world.addFreshEntity(fireball);
			}
			return InteractionResultHolder.pass(itemStackIn);
		};
	}
	
	static ItemRightClickEffect extinguishFire(int mod)
	{
		return withoutCreativeShock((world, player, hand) -> {
			ItemStack itemStackIn = player.getItemInHand(hand);
			
			if(!world.isClientSide)
			{
				world.playSound(null, player.getX(), player.getY(), player.getZ(), SoundEvents.ENDER_DRAGON_FLAP, SoundSource.PLAYERS, 1.0F, 1.4F);
				
				for(BlockPos blockPos : BlockPos.betweenClosed(player.blockPosition().offset(2 * mod, mod, 2 * mod), player.blockPosition().offset(-2 * mod, -1 * mod, -2 * mod)))
				{
					BlockState blockState = world.getBlockState(blockPos);
					if(blockState.getBlock() == Blocks.FIRE)
					{
						world.setBlockAndUpdate(blockPos, Blocks.AIR.defaultBlockState());
						world.playSound(null, blockPos.getX(), blockPos.getY(), blockPos.getZ(), SoundEvents.FIRE_EXTINGUISH, SoundSource.BLOCKS, 0.5F, 1.0F);
					}
				}
				
				AABB axisalignedbb = player.getBoundingBox().inflate(2 * mod, mod, 2 * mod);
				List<LivingEntity> list = player.level.getEntitiesOfClass(LivingEntity.class, axisalignedbb);
				for(LivingEntity livingentity : list)
				{
					if(livingentity.getRemainingFireTicks() > 0)
					{
						livingentity.clearFire();
						world.playSound(null, livingentity.getX(), livingentity.getY(), livingentity.getZ(), SoundEvents.FIRE_EXTINGUISH, SoundSource.NEUTRAL, 0.5F, 1.0F);
					}
				}
				
				player.swing(hand, true);
				player.getCooldowns().addCooldown(itemStackIn.getItem(), 15);
				itemStackIn.hurtAndBreak(1, player, playerEntity -> playerEntity.broadcastBreakEvent(InteractionHand.MAIN_HAND));
			}
			return InteractionResultHolder.pass(itemStackIn);
		});
	}
	
	static ItemRightClickEffect absorbFluid(Supplier<Block> fluidBlock, Supplier<Item> otherItem)
	{
		return withoutCreativeShock((world, player, hand) -> {
			ItemStack itemStack = player.getItemInHand(hand);
			
			BlockHitResult blockraytraceresult = getPlayerPOVHitResult(world, player);
			BlockPos rayTracedPos = blockraytraceresult.getBlockPos();
			
			if(blockraytraceresult.getType() == HitResult.Type.BLOCK && world.getBlockState(rayTracedPos).getBlock() == fluidBlock.get())
			{
				world.setBlockAndUpdate(rayTracedPos, Blocks.AIR.defaultBlockState());
				ItemStack newItem = new ItemStack(otherItem.get(), itemStack.getCount());
				newItem.setTag(itemStack.getTag()); //It is important that the item it is switching to has the same durability
				world.playSound(null, rayTracedPos, SoundEvents.BUCKET_FILL, SoundSource.BLOCKS, 1F, 2F);
				player.getCooldowns().addCooldown(otherItem.get(), 5);
				return InteractionResultHolder.success(newItem);
			}
			
			return InteractionResultHolder.fail(itemStack);
		});
	}
	
	//based on the Item class function of the same name
	static BlockHitResult getPlayerPOVHitResult(Level level, Player playerEntity)
	{
		float xRot = playerEntity.getXRot();
		float yRot = playerEntity.getYRot();
		Vec3 eyeVec = playerEntity.getEyePosition(1.0F);
		float f2 = Mth.cos(-yRot * ((float) Math.PI / 180F) - (float) Math.PI);
		float f3 = Mth.sin(-yRot * ((float) Math.PI / 180F) - (float) Math.PI);
		float f4 = -Mth.cos(-xRot * ((float) Math.PI / 180F));
		float yComponent = Mth.sin(-xRot * ((float) Math.PI / 180F));
		float xComponent = f3 * f4;
		float zComponent = f2 * f4;
		double reachDistance = playerEntity.getAttribute(ForgeMod.REACH_DISTANCE.get()).getValue();
		Vec3 endVec = eyeVec.add((double) xComponent * reachDistance, (double) yComponent * reachDistance, (double) zComponent * reachDistance);
		return level.clip(new ClipContext(eyeVec, endVec, ClipContext.Block.OUTLINE, ClipContext.Fluid.SOURCE_ONLY, playerEntity));
	}
	
	/**
	 * Prevents effect from working if the entity is subject to the effects of creative shock
	 */
	static ItemRightClickEffect withoutCreativeShock(ItemRightClickEffect effect) //TODO action result for client side may not work
	{
		return (world, player, hand) -> {
			ItemStack itemStackIn = player.getItemInHand(hand);
			
			if(!CreativeShockEffect.doesCreativeShockLimit(player, CreativeShockEffect.LIMIT_BLOCK_PLACEMENT_AND_BREAKING))
			{
				return effect.onRightClick(world, player, hand);
			}
			
			return InteractionResultHolder.pass(itemStackIn);
		};
	}
	
	InteractionResultHolder<ItemStack> onRightClick(Level level, Player player, InteractionHand hand);
}