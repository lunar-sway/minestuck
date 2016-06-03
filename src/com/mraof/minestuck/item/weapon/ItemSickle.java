package com.mraof.minestuck.item.weapon;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import com.mraof.minestuck.Minestuck;

public class ItemSickle extends ItemWeapon
{
	private int weaponDamage;
	private final EnumSickleType sickleType;
	
	public ItemSickle(EnumSickleType sickleType)
	{
		super();
		
		this.sickleType = sickleType;
		this.maxStackSize = 1;
		this.setMaxDamage(sickleType.getMaxUses());
		this.setCreativeTab(Minestuck.tabMinestuck);
		this.setUnlocalizedName(sickleType.getName());
		switch(sickleType)
		{
		case SICKLE:
			this.setUnlocalizedName("sickle");
			break;
		case HOMES:
			this.setUnlocalizedName("homesSmellYaLater");
			break;
		case REGISICKLE:
			this.setUnlocalizedName("regiSickle");
			break;
		case CLAW:
			this.setUnlocalizedName("clawSickle");
			break;
		}
		this.weaponDamage = sickleType.getDamageVsEntity();
	}
    
    @Override
	public int getAttackDamage() 
    {
		return weaponDamage;
	}
	
    @Override
	public int getItemEnchantability()
	{
		return this.sickleType.getEnchantability();
	}
	
	@Override
	public boolean hitEntity(ItemStack itemStack, EntityLivingBase target, EntityLivingBase attacker)
	{
		itemStack.damageItem(1, attacker);
		return true;
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public boolean isFull3D()
	{
		return true;
	}
	
    @Override
    public Multimap<String, AttributeModifier> getAttributeModifiers(EntityEquipmentSlot slot, ItemStack stack)
    {
        Multimap multimap = HashMultimap.create();
        if(slot == EntityEquipmentSlot.MAINHAND)
        {
        multimap.put(SharedMonsterAttributes.ATTACK_DAMAGE.getAttributeUnlocalizedName(), new AttributeModifier(ATTACK_DAMAGE_MODIFIER, "Weapon modifier", (double)this.weaponDamage, 0));
        multimap.put(SharedMonsterAttributes.ATTACK_SPEED.getAttributeUnlocalizedName(), new AttributeModifier(ATTACK_SPEED_MODIFIER, "Weapon modifier", -2.4000000953674316D, 0));
        }
        return multimap;
    }
	
}
