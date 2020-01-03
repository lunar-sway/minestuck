package com.mraof.minestuck.item.weapon;

import net.minecraft.block.BlockState;
import net.minecraft.block.material.Material;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentType;
import net.minecraft.util.registry.Registry;
import net.minecraftforge.common.ToolType;

import java.util.ArrayList;
import java.util.List;

public class MSToolType
{
	private final List<Material> harvestMaterials = new ArrayList<>();
	private final List<Enchantment> enchantments = new ArrayList<>();
	private final List<ToolType> toolType = new ArrayList<>();
	
	public MSToolType(ToolType toolType, Material... materials)
	{
		this(materials);
		this.toolType.add(toolType);
	}
	
	public MSToolType(Material... materials)
	{
		for(Material mat : materials)
			harvestMaterials.add(mat);
	}
	
	public MSToolType() {}
	
	public MSToolType(MSToolType... classCombo)
	{
		for(MSToolType cls : classCombo)
		{
			harvestMaterials.addAll(cls.harvestMaterials);
			enchantments.addAll(cls.enchantments);
			toolType.addAll(cls.toolType);
		}
	}
	
	public boolean canHarvest(BlockState state)
	{
		if(harvestMaterials.contains(state.getMaterial()))
			return true;
		
		for(ToolType type : toolType)
			if(state.getBlock().isToolEffective(state, type))
				return true;
		
		return false;
	}
	
	public MSToolType addToolType(ToolType... toolType)
	{
		for(ToolType type : toolType)
			this.toolType.add(type);
		return this;
	}
	
	public MSToolType addEnchantments(Enchantment... enchantments)
	{
		for(Enchantment ench : enchantments)
			this.enchantments.add(ench);
		return this;
	}
	
	public MSToolType addEnchantments(List<Enchantment> enchantments)
	{
		this.enchantments.addAll(enchantments);
		return this;
	}
	
	public MSToolType addEnchantments(EnchantmentType... enchantmentTypes)
	{
		for(EnchantmentType type : enchantmentTypes)
		{
			Registry.ENCHANTMENT.forEach(enchantment ->
			{if(enchantment.type.equals(type)) addEnchantments(enchantment);});
		}
		
		return this;
	}
	
	public List<ToolType> getToolTypes() {return toolType;}
	public List<Material> getHarvestMaterials() {return harvestMaterials;}
	public List<Enchantment> getEnchantments() {return enchantments;}
}
