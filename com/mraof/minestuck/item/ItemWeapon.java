package com.mraof.minestuck.item;

import net.minecraft.block.Block;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.item.EnumToolMaterial;
import net.minecraft.item.ItemTool;

import com.google.common.collect.Multimap;
import com.mraof.minestuck.Minestuck;

public abstract class ItemWeapon extends ItemTool
{
	protected int weaponDamage;
	
	public ItemWeapon(int id) 
	{
		this(id, new Block[0]);
	}

	public ItemWeapon(int id, Block[] blocksEffectiveAgainst)
	{
		super(id, 0, EnumToolMaterial.GOLD, blocksEffectiveAgainst);
		this.maxStackSize = 1;
		this.setCreativeTab(Minestuck.tabMinestuck);
	}
	
	protected abstract int getAttackDamage();

    @Override
    public Multimap func_111205_h()
    {
        Multimap multimap = super.func_111205_h();
        multimap.put(SharedMonsterAttributes.field_111264_e.func_111108_a(), new AttributeModifier(field_111210_e, "Weapon modifier", (double)this.getAttackDamage(), 0));
        return multimap;
    }
}
