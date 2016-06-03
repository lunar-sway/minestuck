package com.mraof.minestuck.item.weapon;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import com.mraof.minestuck.Minestuck;

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemAxe;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ItemBattleaxe extends ItemAxe
{
	private int weaponDamage;
	private final EnumBattleaxeType battleaxeType;
	public float efficiencyOnProperMaterial = 4.0F;
	
	public ItemBattleaxe(EnumBattleaxeType battleaxeType)
	{
		super(ToolMaterial.IRON);
		
		setCreativeTab(Minestuck.tabMinestuck);
		this.battleaxeType = battleaxeType;
		this.setMaxDamage(battleaxeType.getMaxUses());
		this.setUnlocalizedName(battleaxeType.getName());
		this.damageVsEntity = battleaxeType.getDamageVsEntity();

	}
	
	public float getDamageVsEntity()
	{
		return battleaxeType.getDamageVsEntity();
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
	
    public Multimap<String, AttributeModifier> getItemAttributeModifiers(EntityEquipmentSlot equipmentSlot)
    {
        Multimap<String, AttributeModifier> multimap = super.getItemAttributeModifiers(equipmentSlot);

        if (equipmentSlot == EntityEquipmentSlot.MAINHAND)
        {
        	multimap.put(SharedMonsterAttributes.ATTACK_DAMAGE.getAttributeUnlocalizedName(), new AttributeModifier(ATTACK_DAMAGE_MODIFIER, "Weapon modifier", (double)this.weaponDamage, 0));
            multimap.put(SharedMonsterAttributes.ATTACK_SPEED.getAttributeUnlocalizedName(), new AttributeModifier(ATTACK_SPEED_MODIFIER, "Weapon modifier", -2.4000000953674316D, 0));
        }

        return multimap;
    }
}