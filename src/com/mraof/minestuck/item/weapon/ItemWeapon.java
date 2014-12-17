package com.mraof.minestuck.item.weapon;

import java.util.HashSet;
import java.util.Set;

import net.minecraft.block.Block;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemTool;
import net.minecraft.world.World;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import com.mraof.minestuck.Minestuck;

public abstract class ItemWeapon extends ItemTool
{
	protected int weaponDamage;
	
	public ItemWeapon() 
	{
		this(new HashSet<Block>());
	}

	public ItemWeapon(Set<Block> blocksEffectiveAgainst)
	{
		super(0, Item.ToolMaterial.IRON, blocksEffectiveAgainst);
		this.maxStackSize = 1;
		this.setCreativeTab(Minestuck.tabMinestuck);
	}
	
	protected abstract int getAttackDamage();

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public Multimap getItemAttributeModifiers()
	{
		Multimap multimap = HashMultimap.create();
		multimap.put(SharedMonsterAttributes.attackDamage.getAttributeUnlocalizedName(), new AttributeModifier(this.itemModifierUUID, "Tool Modifier", (double)this.getAttackDamage(), 0));
		return multimap;
	}
	
}
