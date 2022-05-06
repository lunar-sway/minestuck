package com.mraof.minestuck.item.weapon;

import com.mraof.minestuck.effects.CreativeShockEffect;
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
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.util.text.IFormattableTextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeMod;

import java.util.List;
import java.util.function.Supplier;

public interface ItemRightClickEffect
{
	
	ItemRightClickEffect ACTIVE_HAND = (world, player, hand) -> {
		player.startUsingItem(hand);
		return ActionResult.consume(player.getItemInHand(hand));
	};
	
	ItemRightClickEffect EIGHTBALL = (world, player, hand) -> {
		if(world.isClientSide)
		{
			int key = player.getRandom().nextInt(20);
			IFormattableTextComponent message = new TranslationTextComponent("message.eightball." + key);
			player.sendMessage(message.withStyle(TextFormatting.BLUE), Util.NIL_UUID);
		}
		return ActionResult.success(player.getItemInHand(hand));
	};
	
	static ItemRightClickEffect switchTo(Supplier<Item> otherItem)
	{
		return (world, player, hand) -> {
			ItemStack itemStackIn = player.getItemInHand(hand);
			if(player.isShiftKeyDown())
			{
				ItemStack newItem = new ItemStack(otherItem.get(), itemStackIn.getCount());
				newItem.setTag(itemStackIn.getTag());
				
				return ActionResult.success(newItem);
			}
			return ActionResult.pass(itemStackIn);
		};
	}
	
	static ItemRightClickEffect summonFireball()
	{
		return (world, player, hand) -> {
			ItemStack itemStackIn = player.getItemInHand(hand);
			world.playSound(null, player.getX(), player.getY(), player.getZ(), SoundEvents.BLAZE_SHOOT, SoundCategory.PLAYERS, 1.0F, 0.8F);
			
			AxisAlignedBB axisalignedbb = player.getBoundingBox().inflate(32.0D, 32.0D, 32.0D);
			List<LivingEntity> list = player.level.getEntitiesOfClass(LivingEntity.class, axisalignedbb);
			list.remove(player);
			if(!list.isEmpty() && !world.isClientSide)
			{
				for(LivingEntity livingentity : list)
				{
					FireballEntity fireball = new FireballEntity(world, player, 0, -8.0, 0);
					fireball.explosionPower = 1;
					fireball.setPos(livingentity.getX() + (player.getRandom().nextInt(6) - 3), livingentity.getY() + 40, livingentity.getZ() + (player.getRandom().nextInt(6) - 3));
					player.getCooldowns().addCooldown(itemStackIn.getItem(), 20);
					itemStackIn.hurtAndBreak(2, player, playerEntity -> playerEntity.broadcastBreakEvent(Hand.MAIN_HAND));
					world.addFreshEntity(fireball);
				}
			} else if(!world.isClientSide)
			{
				FireballEntity fireball = new FireballEntity(world, player, 0, -8.0, 0);
				fireball.explosionPower = 1;
				fireball.setPos(player.getX() + (player.getRandom().nextInt(20) - 10), player.getY() + 40, player.getZ() + (player.getRandom().nextInt(20) - 10));
				player.getCooldowns().addCooldown(itemStackIn.getItem(), 20);
				itemStackIn.hurtAndBreak(2, player, playerEntity -> playerEntity.broadcastBreakEvent(Hand.MAIN_HAND));
				world.addFreshEntity(fireball);
			}
			return ActionResult.pass(itemStackIn);
		};
	}
	
	static ItemRightClickEffect extinguishFire(int mod)
	{
		return withoutCreativeShock((world, player, hand) -> {
			ItemStack itemStackIn = player.getItemInHand(hand);
			
			if(!world.isClientSide)
			{
				world.playSound(null, player.getX(), player.getY(), player.getZ(), SoundEvents.ENDER_DRAGON_FLAP, SoundCategory.PLAYERS, 1.0F, 1.4F);
				
				for(BlockPos blockPos : BlockPos.betweenClosed(player.blockPosition().offset(2 * mod, mod, 2 * mod), player.blockPosition().offset(-2 * mod, -1 * mod, -2 * mod)))
				{
					BlockState blockState = world.getBlockState(blockPos);
					if(blockState.getBlock() == Blocks.FIRE)
					{
						world.setBlockAndUpdate(blockPos, Blocks.AIR.defaultBlockState());
						world.playSound(null, blockPos.getX(), blockPos.getY(), blockPos.getZ(), SoundEvents.FIRE_EXTINGUISH, SoundCategory.BLOCKS, 0.5F, 1.0F);
					}
				}
				
				AxisAlignedBB axisalignedbb = player.getBoundingBox().inflate(2 * mod, mod, 2 * mod);
				List<LivingEntity> list = player.getCommandSenderWorld().getEntitiesOfClass(LivingEntity.class, axisalignedbb);
				for(LivingEntity livingentity : list)
				{
					if(livingentity.getRemainingFireTicks() > 0)
					{
						livingentity.clearFire();
						world.playSound(null, livingentity.getX(), livingentity.getY(), livingentity.getZ(), SoundEvents.FIRE_EXTINGUISH, SoundCategory.NEUTRAL, 0.5F, 1.0F);
					}
				}
				
				player.swing(hand, true);
				player.getCooldowns().addCooldown(itemStackIn.getItem(), 15);
				itemStackIn.hurtAndBreak(1, player, playerEntity -> playerEntity.broadcastBreakEvent(Hand.MAIN_HAND));
			}
			return ActionResult.pass(itemStackIn);
		});
	}
	
	static ItemRightClickEffect absorbFluid(Supplier<Block> fluidBlock, Supplier<Item> otherItem)
	{
		return withoutCreativeShock((world, player, hand) -> {
			ItemStack itemStack = player.getItemInHand(hand);
			
			BlockRayTraceResult blockraytraceresult = getPlayerPOVHitResult(world, player);
			BlockPos rayTracedPos = blockraytraceresult.getBlockPos();
			
			if(blockraytraceresult.getType() == RayTraceResult.Type.BLOCK && world.getBlockState(rayTracedPos).getBlock() == fluidBlock.get())
			{
				world.setBlockAndUpdate(rayTracedPos, Blocks.AIR.defaultBlockState());
				ItemStack newItem = new ItemStack(otherItem.get(), itemStack.getCount());
				newItem.setTag(itemStack.getTag()); //It is important that the item it is switching to has the same durability
				world.playSound(null, rayTracedPos, SoundEvents.BUCKET_FILL, SoundCategory.BLOCKS, 1F, 2F);
				player.getCooldowns().addCooldown(otherItem.get(), 5);
				return ActionResult.success(newItem);
			}
			
			return ActionResult.fail(itemStack);
		});
	}
	
	//based on the Item class function of the same name
	static BlockRayTraceResult getPlayerPOVHitResult(World world, PlayerEntity playerEntity)
	{
		float xRot = playerEntity.xRot;
		float yRot = playerEntity.yRot;
		Vector3d eyeVec = playerEntity.getEyePosition(1.0F);
		float f2 = MathHelper.cos(-yRot * ((float) Math.PI / 180F) - (float) Math.PI);
		float f3 = MathHelper.sin(-yRot * ((float) Math.PI / 180F) - (float) Math.PI);
		float f4 = -MathHelper.cos(-xRot * ((float) Math.PI / 180F));
		float yComponent = MathHelper.sin(-xRot * ((float) Math.PI / 180F));
		float xComponent = f3 * f4;
		float zComponent = f2 * f4;
		double reachDistance = playerEntity.getAttribute(ForgeMod.REACH_DISTANCE.get()).getValue();
		Vector3d endVec = eyeVec.add((double) xComponent * reachDistance, (double) yComponent * reachDistance, (double) zComponent * reachDistance);
		return world.clip(new RayTraceContext(eyeVec, endVec, RayTraceContext.BlockMode.OUTLINE, RayTraceContext.FluidMode.SOURCE_ONLY, playerEntity));
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
			
			return ActionResult.pass(itemStackIn);
		};
	}
	
	ActionResult<ItemStack> onRightClick(World world, PlayerEntity player, Hand hand);
}