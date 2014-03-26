package com.mraof.minestuck.item;

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

	public ItemWeapon(Set blocksEffectiveAgainst)
	{
		super(0, Item.ToolMaterial.IRON, blocksEffectiveAgainst);
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
    public boolean func_150894_a(ItemStack p_150894_1_, World p_150894_2_, Block p_150894_3_, int p_150894_4_, int p_150894_5_, int p_150894_6_, EntityLivingBase p_150894_7_)
    {
    	return onBlockDestroyed(p_150894_1_, p_150894_2_, p_150894_3_, p_150894_4_, p_150894_5_, p_150894_6_, p_150894_7_);
    }

	public boolean onBlockDestroyed(ItemStack itemStack, World world, Block par3, int par4, int par5, int par6, EntityLivingBase par7EntityLiving) {
		return super.onBlockDestroyed(itemStack, world, par3, par4, par5, par6, par7EntityLiving);
	}

	@Override
	public float func_150893_a(ItemStack p_150893_1_, Block p_150893_2_) 
	{
		return getStrVsBlock(p_150893_1_, p_150893_2_);
	}
	
	public float getStrVsBlock(ItemStack itemStack, Block block) 
	{
		return super.func_150893_a(itemStack, block);
	}

	@Override
	public boolean func_150897_b(Block p_150897_1_) {
		return canHarvestBlock(p_150897_1_);
	}
	
	public boolean canHarvestBlock(Block block) {
		return super.func_150897_b(block);
	}

}
