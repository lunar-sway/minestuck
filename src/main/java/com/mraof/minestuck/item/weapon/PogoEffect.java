package com.mraof.minestuck.item.weapon;

import net.minecraft.block.Blocks;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUseContext;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class PogoEffect implements ItemUseEffect, OnHitEffect
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

	private double getPogoMotion(ItemStack stack){
		return pogoMotion;
	}

	private static double addEfficiencyModifier(double pogoMotion, ItemStack stack)
	{
		return pogoMotion * ((EnchantmentHelper.getEnchantmentLevel(Enchantments.EFFICIENCY, stack)*0.15)+1);
	}
	
	@Override
	public ActionResultType onItemUse(ItemUseContext context)
	{
		return onItemUse(context.getPlayer(), context.getWorld(), context.getPos(), context.getItem(), context.getFace(), getPogoMotion(context.getItem()));
	}
	
	private static void hitEntity(ItemStack stack, LivingEntity target, LivingEntity player, double pogoMotion)
	{
		pogoMotion = addEfficiencyModifier(pogoMotion, stack);
		if (player.fallDistance > 0.0F && !player.onGround && !player.isOnLadder() && !player.isInWater() && !player.isPassenger())
		{
			double knockbackModifier = 1D - target.getAttributes().getAttributeInstance(SharedMonsterAttributes.KNOCKBACK_RESISTANCE).getValue();
			double targetMotionY = Math.max(target.getMotion().y, knockbackModifier * Math.min(pogoMotion* 2, Math.abs(player.getMotion().y) + target.getMotion().y + pogoMotion));
			target.setMotion(target.getMotion().x, targetMotionY, target.getMotion().z);
			player.setMotion(player.getMotion().x, 0, player.getMotion().z);
			player.fallDistance = 0;
		}
	}
	
	private static ActionResultType onItemUse(PlayerEntity player, World worldIn, BlockPos pos, ItemStack stack, Direction facing, double pogoMotion)
	{
		pogoMotion = addEfficiencyModifier(pogoMotion, stack);
		if (worldIn.getBlockState(pos).getBlock() != Blocks.AIR)
		{
			double playerMotionX;
			double playerMotionY;
			double playerMotionZ;
			double velocity = Math.max(player.getMotion().y, Math.min(pogoMotion * 2, Math.abs(player.getMotion().y) + pogoMotion));
			final float HORIZONTAL_Y = 6f;
			switch (facing.getAxis())
			{
				case X:
					velocity += Math.abs(player.getMotion().x) / 2;
					playerMotionX = velocity * facing.getDirectionVec().getX();
					playerMotionY = velocity / HORIZONTAL_Y;
					player.setMotion(playerMotionX, playerMotionY, player.getMotion().z);
					break;
				case Y:
					playerMotionY = velocity * facing.getDirectionVec().getY();
					player.setMotion(player.getMotion().x, playerMotionY, player.getMotion().z);
					break;
				case Z:
					velocity += Math.abs(player.getMotion().z) / 2;
					playerMotionY = velocity / HORIZONTAL_Y;
					playerMotionZ = velocity * facing.getDirectionVec().getZ();
					player.setMotion(player.getMotion().x, playerMotionY, playerMotionZ);
					break;
			}
			player.fallDistance = 0;
			stack.damageItem(1, player, playerEntity -> playerEntity.sendBreakAnimation(Hand.MAIN_HAND));
			return ActionResultType.SUCCESS;
		}
		return ActionResultType.PASS;
	}
}
