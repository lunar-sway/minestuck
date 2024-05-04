package com.mraof.minestuck.item.weapon;

import com.mraof.minestuck.effects.CreativeShockEffect;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.common.NeoForgeMod;

public class PogoEffect implements ItemRightClickEffect, OnHitEffect
{
	public static final PogoEffect EFFECT_02 = new PogoEffect(0.2);
	public static final PogoEffect EFFECT_04 = new PogoEffect(0.4);
	public static final PogoEffect EFFECT_05 = new PogoEffect(0.5);
	public static final PogoEffect EFFECT_06 = new PogoEffect(0.6);
	public static final PogoEffect EFFECT_07 = new PogoEffect(0.7);
	
	private final double pogoMotion;
	
	public PogoEffect(double pogoMotion)
	{
		this.pogoMotion = pogoMotion;
	}
	
	@Override
	public void onHit(ItemStack stack, LivingEntity target, LivingEntity player)
	{
		hitEntity(stack, target, player, getPogoMotion(stack));
	}
	
	private double getPogoMotion(ItemStack stack)
	{
		return pogoMotion;
	}
	
	private static double addEfficiencyModifier(double pogoMotion, ItemStack stack)
	{
		return pogoMotion * ((EnchantmentHelper.getItemEnchantmentLevel(Enchantments.BLOCK_EFFICIENCY, stack) * 0.15) + 1);
	}
	
	@Override
	public InteractionResultHolder<ItemStack> onRightClick(Level level, Player player, InteractionHand hand)
	{
		ItemStack stack = player.getItemInHand(hand);
		BlockHitResult blockraytraceresult = getPlayerPOVHitResult(level, player);
		
		if(blockraytraceresult.getType() == HitResult.Type.BLOCK)
		{
			return onItemUse(player, level, blockraytraceresult.getBlockPos(), stack, blockraytraceresult.getDirection(), getPogoMotion(stack)) == InteractionResult.SUCCESS ? InteractionResultHolder.success(stack) : InteractionResultHolder.pass(stack);
		}
		
		return InteractionResultHolder.pass(stack);
	}
	
	//based on the Item class function of the same name
	private static BlockHitResult getPlayerPOVHitResult(Level level, Player playerEntity)
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
		double reachDistance = playerEntity.getAttribute(NeoForgeMod.BLOCK_REACH.value()).getValue();
		Vec3 endVec = eyeVec.add((double) xComponent * reachDistance, (double) yComponent * reachDistance, (double) zComponent * reachDistance);
		return level.clip(new ClipContext(eyeVec, endVec, ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, playerEntity));
	}
	
	private static void hitEntity(ItemStack stack, LivingEntity target, LivingEntity player, double pogoMotion)
	{
		pogoMotion = addEfficiencyModifier(pogoMotion, stack);
		if(player.fallDistance > 0.0F && !player.onGround() && !player.onClimbable() && !player.isInWater() && !player.isPassenger())
		{
			double knockbackModifier = 1D - target.getAttributeValue(Attributes.KNOCKBACK_RESISTANCE);
			double targetMotionY = Math.max(target.getDeltaMovement().y, knockbackModifier * Math.min(pogoMotion * 2, Math.abs(player.getDeltaMovement().y) + target.getDeltaMovement().y + pogoMotion));
			target.setDeltaMovement(target.getDeltaMovement().x, targetMotionY, target.getDeltaMovement().z);
			player.setDeltaMovement(player.getDeltaMovement().x, 0, player.getDeltaMovement().z);
			player.fallDistance = 0;
		}
	}
	
	private static InteractionResult onItemUse(Player player, Level level, BlockPos pos, ItemStack stack, Direction facing, double pogoMotion)
	{
		pogoMotion = addEfficiencyModifier(pogoMotion, stack);
		if(level.getBlockState(pos).getBlock() != Blocks.AIR && !CreativeShockEffect.doesCreativeShockLimit(player, CreativeShockEffect.LIMIT_MOBILITY_ITEMS))
		{
			double playerMotionX;
			double playerMotionY;
			double playerMotionZ;
			double velocity = Math.max(player.getDeltaMovement().y, Math.min(pogoMotion * 2, Math.abs(player.getDeltaMovement().y) + pogoMotion));
			final float HORIZONTAL_Y = 6f;
			switch(facing.getAxis())
			{
				case X:
					velocity += Math.abs(player.getDeltaMovement().x) / 2;
					playerMotionX = velocity * facing.getNormal().getX();
					playerMotionY = velocity / HORIZONTAL_Y;
					player.setDeltaMovement(playerMotionX, playerMotionY, player.getDeltaMovement().z);
					break;
				case Y:
					playerMotionY = velocity * facing.getNormal().getY();
					player.setDeltaMovement(player.getDeltaMovement().x, playerMotionY, player.getDeltaMovement().z);
					break;
				case Z:
					velocity += Math.abs(player.getDeltaMovement().z) / 2;
					playerMotionY = velocity / HORIZONTAL_Y;
					playerMotionZ = velocity * facing.getNormal().getZ();
					player.setDeltaMovement(player.getDeltaMovement().x, playerMotionY, playerMotionZ);
					break;
			}
			player.fallDistance = 0;
			stack.hurtAndBreak(1, player, playerEntity -> playerEntity.broadcastBreakEvent(InteractionHand.MAIN_HAND));
			return InteractionResult.SUCCESS;
		}
		return InteractionResult.PASS;
	}
}
