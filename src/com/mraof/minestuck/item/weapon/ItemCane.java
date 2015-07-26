package com.mraof.minestuck.item.weapon;

import net.minecraft.block.Block;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ItemCane extends ItemWeapon
{
	private int weaponDamage;
	private final EnumCaneType caneType;
    public float efficiencyOnProperMaterial = 4.0F;
    
	public ItemCane(EnumCaneType caneType) 
	{
		super();
		this.caneType = caneType;
		this.setMaxDamage(caneType.getMaxUses());
		switch(caneType)
		{
		case CANE:
			this.setUnlocalizedName("cane");
			break;
		case SPEAR:
			this.setUnlocalizedName("spearCane");
			break;
		case DRAGON:
			this.setUnlocalizedName("dragonCane");
			break;
		}
		this.weaponDamage = 2 + caneType.getDamageVsEntity();
	}

    @Override
	public int getAttackDamage() 
    {
		return weaponDamage;
	}
	
    @Override
	public int getItemEnchantability()
	{
		return this.caneType.getEnchantability();
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
