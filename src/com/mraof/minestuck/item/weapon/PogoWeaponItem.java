package com.mraof.minestuck.item.weapon;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.block.Blocks;
import net.minecraft.item.IItemTier;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUseContext;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

/**
 * Created by mraof on 2017 January 18 at 6:17 PM.
 */
public class PogoWeaponItem extends WeaponItem
{
	private double pogoMotion;
	
	public PogoWeaponItem(IItemTier tier, int attackDamageIn, float attackSpeedIn, float efficiency, double pogoMotion, Properties builder)
	{
		super(tier, attackDamageIn, attackSpeedIn, efficiency, builder);
		this.pogoMotion = pogoMotion;
	}
	
	@Override
	public boolean hitEntity(ItemStack stack, LivingEntity target, LivingEntity player)
	{
		super.hitEntity(stack, target, player);
		hitEntity(stack, target, player, getPogoMotion(stack));
		return true;
	}
	
	private double getPogoMotion(ItemStack stack)
	{
//		return 0.5 + EnchantmentHelper.getEnchantmentLevel(Enchantment.efficiency.effectId, stack)*0.1;
		return pogoMotion;
	}
	
	@Override
	public ActionResultType onItemUse(ItemUseContext context)
	{
		return onItemUse(context.getPlayer(), context.getWorld(), context.getPos(), context.getItem(), context.getFace(), getPogoMotion(context.getItem()));
	}
	
	public static void hitEntity(ItemStack stack, LivingEntity target, LivingEntity player, double pogoMotion)
	{
		if (player.fallDistance > 0.0F && !player.onGround && !player.isOnLadder() && !player.isInWater() && !player.isPassenger())
		{
			double knockbackModifier = 1D - target.getAttributes().getAttributeInstance(SharedMonsterAttributes.KNOCKBACK_RESISTANCE).getValue();
			double targetMotionY = Math.max(target.getMotion().y, knockbackModifier * Math.min(pogoMotion* 2, Math.abs(player.getMotion().y) + target.getMotion().y + pogoMotion));
			target.setMotion(target.getMotion().x, targetMotionY, target.getMotion().z);
			player.setMotion(player.getMotion().x, 0, player.getMotion().z);
			player.fallDistance = 0;
		}
	}
	
	public static ActionResultType onItemUse(PlayerEntity player, World worldIn, BlockPos pos, ItemStack stack, Direction facing, double pogoMotion)
	{
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
					player.setMotion(player.getMotion().x, playerMotionY, player.getMotion().y);
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
