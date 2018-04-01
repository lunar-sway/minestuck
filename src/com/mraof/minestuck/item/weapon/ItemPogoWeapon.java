package com.mraof.minestuck.item.weapon;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

/**
 * Created by mraof on 2017 January 18 at 6:17 PM.
 */
public class ItemPogoWeapon extends ItemWeapon
{
	private double pogoMotion;
	
	public ItemPogoWeapon(int maxUses, double damageVsEntity, double weaponSpeed, int enchantability, String name, double pogoMotion)
	{
		super(maxUses, damageVsEntity, weaponSpeed, enchantability, name);
		this.pogoMotion = pogoMotion;
	}
	
	@Override
	public boolean hitEntity(ItemStack stack, EntityLivingBase target, EntityLivingBase player)
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
	public EnumActionResult onItemUse(EntityPlayer player, World worldIn, BlockPos pos, EnumHand hand,
									  EnumFacing facing, float hitX, float hitY, float hitZ)
	{
		return onItemUse(player, worldIn, pos, hand, facing, getPogoMotion(player.getHeldItem(hand)));
	}
	
	public static void hitEntity(ItemStack stack, EntityLivingBase target, EntityLivingBase player, double pogoMotion)
	{
		if (player.fallDistance > 0.0F && !player.onGround && !player.isOnLadder() && !player.isInWater() && !player.isRiding())
		{
			double knockbackModifier = 1D - target.getAttributeMap().getAttributeInstance(SharedMonsterAttributes.KNOCKBACK_RESISTANCE).getAttributeValue();
			target.motionY = Math.max(target.motionY, knockbackModifier * Math.min(pogoMotion * 2, Math.abs(player.motionY) + target.motionY + pogoMotion));
			player.motionY = 0;
			player.fallDistance = 0;
		}
	}
	
	public static EnumActionResult onItemUse(EntityPlayer player, World worldIn, BlockPos pos, EnumHand hand, EnumFacing facing, double pogoMotion)
	{
		ItemStack stack = player.getHeldItem(hand);
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
			return EnumActionResult.SUCCESS;
		}
		return EnumActionResult.PASS;
	}
}
