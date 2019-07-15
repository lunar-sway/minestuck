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
			double knockbackModifier = 1D - target.getAttributeMap().getAttributeInstance(SharedMonsterAttributes.KNOCKBACK_RESISTANCE).getValue();
			target.motionY = Math.max(target.motionY, knockbackModifier * Math.min(pogoMotion * 2, Math.abs(player.motionY) + target.motionY + pogoMotion));
			player.motionY = 0;
			player.fallDistance = 0;
		}
	}
	
	public static ActionResultType onItemUse(PlayerEntity player, World worldIn, BlockPos pos, ItemStack stack, Direction facing, double pogoMotion)
	{
		if (worldIn.getBlockState(pos).getBlock() != Blocks.AIR)
		{
			double velocity = Math.max(player.motionY, Math.min(pogoMotion * 2, Math.abs(player.motionY) + pogoMotion));
			final float HORIZONTAL_Y = 6f;
			switch (facing.getAxis())
			{
				case X:
					velocity += Math.abs(player.motionX) / 2;
					player.motionX = velocity * facing.getDirectionVec().getX();
					player.motionY = velocity / HORIZONTAL_Y;
					break;
				case Y:
					player.motionY = velocity * facing.getDirectionVec().getY();
					break;
				case Z:
					velocity += Math.abs(player.motionZ) / 2;
					player.motionZ = velocity * facing.getDirectionVec().getZ();
					player.motionY = velocity / HORIZONTAL_Y;
					break;
			}
			player.fallDistance = 0;
			stack.damageItem(1, player);
			return ActionResultType.SUCCESS;
		}
		return ActionResultType.PASS;
	}
}
