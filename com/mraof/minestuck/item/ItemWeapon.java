package com.mraof.minestuck.item;

import net.minecraft.block.Block;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.item.EnumToolMaterial;
import net.minecraft.item.ItemTool;

import com.google.common.collect.HashMultimap;
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
		super(id, 0, EnumToolMaterial.IRON, blocksEffectiveAgainst);
		this.maxStackSize = 1;
		this.setCreativeTab(Minestuck.tabMinestuck);
	}
	
	protected abstract int getAttackDamage();

    @Override
    public Multimap getItemAttributeModifiers()
    {
        Multimap multimap = HashMultimap.create();
        multimap.put(SharedMonsterAttributes.attackDamage.getAttributeUnlocalizedName(), new AttributeModifier(field_111210_e, "Tool Modifier", (double)this.getAttackDamage(), 0));
        return multimap;
    }
}
