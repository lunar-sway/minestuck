package com.mraof.minestuck.item.weapon;

import com.mraof.minestuck.Minestuck;

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemAxe;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ItemBattleaxe extends ItemAxe
{
	private final EnumBattleaxeType battleaxeType;
	
	public ItemBattleaxe(EnumBattleaxeType battleaxeType)
	{
		super(ToolMaterial.IRON);
		
		setCreativeTab(Minestuck.tabMinestuck);
		this.battleaxeType = battleaxeType;
		this.setMaxDamage(battleaxeType.getMaxUses());
		this.setUnlocalizedName(battleaxeType.getName());
		this.damageVsEntity = (float) battleaxeType.getDamageVsEntity();
		this.attackSpeed = (float) battleaxeType.getAttackSpeed();
	}
	
	@Override
	public int getItemEnchantability()
	{
		return this.battleaxeType.getEnchantability();
	}
	
	@Override
	public boolean hitEntity(ItemStack itemStack, EntityLivingBase target, EntityLivingBase player)
	{
		itemStack.damageItem(1, player);
		
		if (battleaxeType.equals(EnumBattleaxeType.HEPH))
			target.setFire(30);
		return true;
	}
	
	@Override
	public boolean onBlockDestroyed(ItemStack stack, World worldIn, IBlockState blockIn, BlockPos pos, EntityLivingBase entityLiving)
	{
		if ((double)blockIn.getBlockHardness(worldIn, pos) != 0.0D)
			stack.damageItem(2, entityLiving);
		
		return true;
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public boolean isFull3D()
	{
		return true;
	}
	
}