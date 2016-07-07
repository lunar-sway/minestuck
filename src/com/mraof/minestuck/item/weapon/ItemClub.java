/**
 * 
 */
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
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * @author mraof
 *
 */
public class ItemClub extends ItemWeapon
{
	private final EnumClubType clubType;
	
	public ItemClub(EnumClubType clubType) 
	{
		super();
		this.clubType = clubType;
		this.setMaxDamage(clubType.getMaxUses());
		this.setUnlocalizedName(clubType.getName());
		this.weaponDamage = clubType.getDamageVsEntity();
		this.weaponSpeed = clubType.getAttackSpeed();
	}
	
	@Override
	public int getItemEnchantability()
	{
		return this.clubType.getEnchantability();
	}
	
	/*is there a better method of including this here?
	 * i've made the pogo club a bit weaker than the hammer as well.
	 * -killdragons
	 */
	
	@Override
	public boolean hitEntity(ItemStack itemStack, EntityLivingBase target, EntityLivingBase player)
	{
		itemStack.damageItem(1, player);
		if(clubType.equals(EnumClubType.POGO) && player.fallDistance > 0.0F && !player.onGround && !player.isOnLadder() && !player.isInWater() && !player.isRiding())
		{
			double knockbackModifier = 1D - target.getAttributeMap().getAttributeInstance(SharedMonsterAttributes.KNOCKBACK_RESISTANCE).getAttributeValue();
			target.motionY = Math.max(target.motionY, knockbackModifier*Math.min(getPogoMotion(itemStack)*2, Math.abs(player.motionY) + target.motionY + getPogoMotion(itemStack)));
			player.motionY = 0;
			player.fallDistance = 0;
		}
		return true;
	}
	
	@Override
	public EnumActionResult onItemUse(ItemStack stack, EntityPlayer playerIn, World worldIn, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ)
	{
		if(worldIn.getBlockState(pos).getBlock() != Blocks.AIR)
		{
			if (clubType.equals(EnumClubType.POGO))
			{
				playerIn.motionY = Math.max(playerIn.motionY, Math.min(getPogoMotion(stack)*2, Math.abs(playerIn.motionY) + getPogoMotion(stack)));
				playerIn.fallDistance = 0;
				stack.damageItem(1, playerIn);
				return EnumActionResult.SUCCESS;
			} 
		}
		return EnumActionResult.PASS;
	}
	
	protected double getPogoMotion(ItemStack stack)
	{
		return 0.5;
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public boolean isFull3D()
	{
		return true;
	}
	
}