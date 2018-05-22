package com.mraof.minestuck.item.weapon;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class ItemPogoFarmine extends ItemFarmine
{
	private double pogoMotion;
	
	public ItemPogoFarmine(int maxUses, double damageVsEntity, double weaponSpeed, int enchantability, String name, int r, int t, double pogoMotion)
	{
		super(maxUses, damageVsEntity, weaponSpeed, enchantability, name, r, t);
		this.pogoMotion = pogoMotion;
	}
	
	@Override
	public boolean hitEntity(ItemStack stack, EntityLivingBase target, EntityLivingBase player)
	{
		super.hitEntity(stack, target, player);
		ItemPogoWeapon.hitEntity(stack, target, player, pogoMotion);
		return true;
	}
	
	@Override
	public EnumActionResult onItemUse(EntityPlayer player, World worldIn, BlockPos pos, EnumHand hand,
									  EnumFacing facing, float hitX, float hitY, float hitZ)
	{
		return ItemPogoWeapon.onItemUse(player, worldIn, pos, hand, facing, pogoMotion);
	}
}
