/**
 * 
 */
package com.mraof.minestuck.item.weapon;


import net.minecraft.block.Block;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;
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
    public float efficiencyOnProperMaterial = 4.0F;
    
	public ItemClub(EnumClubType clubType) 
	{
		super();
		this.clubType = clubType;
		this.setMaxDamage(clubType.getMaxUses());
		switch(clubType)
		{
		case DEUCE:
			this.setUnlocalizedName("deuceClub");
			break;
		}
		this.weaponDamage = 2 + clubType.getDamageVsEntity();
	}

    @Override
	public int getAttackDamage() 
	{
		return weaponDamage;
	}

    @Override
	public int getItemEnchantability()
	{
		return this.clubType.getEnchantability();
	}
	
	@Override
	public boolean hitEntity(ItemStack itemStack, EntityLivingBase target, EntityLivingBase player)
	{
		itemStack.damageItem(1, player);
		return true;
	}
	
	@Override
	public boolean onBlockDestroyed(ItemStack stack, World worldIn, Block blockIn, BlockPos pos, EntityLivingBase playerIn)
	{
		if ((double)blockIn.getBlockHardness(worldIn, pos) != 0.0D)
		{
			stack.damageItem(2, playerIn);
		}
		
		return true;
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public boolean isFull3D()
	{
		return true;
	}
	
}
