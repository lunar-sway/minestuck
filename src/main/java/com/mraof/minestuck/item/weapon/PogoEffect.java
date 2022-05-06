package com.mraof.minestuck.item.weapon;

import com.mraof.minestuck.effects.CreativeShockEffect;
import net.minecraft.block.Blocks;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.math.*;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeMod;

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
	public ActionResult<ItemStack> onRightClick(World world, PlayerEntity player, Hand hand)
	{
		ItemStack stack = player.getItemInHand(hand);
		BlockRayTraceResult blockraytraceresult = getPlayerPOVHitResult(world, player);
		
		if(blockraytraceresult.getType() == RayTraceResult.Type.BLOCK)
		{
			return onItemUse(player, world, blockraytraceresult.getBlockPos(), stack, blockraytraceresult.getDirection(), getPogoMotion(stack)) == ActionResultType.SUCCESS ? ActionResult.success(stack) : ActionResult.pass(stack);
		}
		
		return ActionResult.pass(stack);
	}
	
	//based on the Item class function of the same name
	private static BlockRayTraceResult getPlayerPOVHitResult(World world, PlayerEntity playerEntity)
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
		return world.clip(new RayTraceContext(eyeVec, endVec, RayTraceContext.BlockMode.COLLIDER, RayTraceContext.FluidMode.NONE, playerEntity));
	}
	
	private static void hitEntity(ItemStack stack, LivingEntity target, LivingEntity player, double pogoMotion)
	{
		pogoMotion = addEfficiencyModifier(pogoMotion, stack);
		if(player.fallDistance > 0.0F && !player.isOnGround() && !player.onClimbable() && !player.isInWater() && !player.isPassenger())
		{
			double knockbackModifier = 1D - target.getAttributeValue(Attributes.KNOCKBACK_RESISTANCE);
			double targetMotionY = Math.max(target.getDeltaMovement().y, knockbackModifier * Math.min(pogoMotion * 2, Math.abs(player.getDeltaMovement().y) + target.getDeltaMovement().y + pogoMotion));
			target.setDeltaMovement(target.getDeltaMovement().x, targetMotionY, target.getDeltaMovement().z);
			player.setDeltaMovement(player.getDeltaMovement().x, 0, player.getDeltaMovement().z);
			player.fallDistance = 0;
		}
	}
	
	private static ActionResultType onItemUse(PlayerEntity player, World worldIn, BlockPos pos, ItemStack stack, Direction facing, double pogoMotion)
	{
		pogoMotion = addEfficiencyModifier(pogoMotion, stack);
		if(worldIn.getBlockState(pos).getBlock() != Blocks.AIR && !CreativeShockEffect.doesCreativeShockLimit(player, CreativeShockEffect.LIMIT_MOBILITY_ITEMS))
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
			stack.hurtAndBreak(1, player, playerEntity -> playerEntity.broadcastBreakEvent(Hand.MAIN_HAND));
			return ActionResultType.SUCCESS;
		}
		return ActionResultType.PASS;
	}
}
