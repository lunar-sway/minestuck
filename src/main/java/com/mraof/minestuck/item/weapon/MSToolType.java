package com.mraof.minestuck.item.weapon;

import net.minecraft.block.BlockState;
import net.minecraft.block.material.Material;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentType;
import net.minecraftforge.common.ToolType;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class MSToolType
{
	private final Set<Material> harvestMaterials = new HashSet<>();
	private final Set<Enchantment> enchantments = new HashSet<>();
	private final Set<ToolType> toolType = new HashSet<>();
	
	public MSToolType(ToolType toolType, Material... materials)
	{
		this(materials);
		this.toolType.add(toolType);
	}
	
	public MSToolType(Material... materials)
	{
		harvestMaterials.addAll(Arrays.asList(materials));
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
		this.toolType.addAll(Arrays.asList(toolType));
		return this;
	}
	
	//TODO Tool types WILL be created before mod enchantments are created and registered. We should use suppliers instead
	public MSToolType addEnchantments(Enchantment... enchantments)
	{
		this.enchantments.addAll(Arrays.asList(enchantments));
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
			ForgeRegistries.ENCHANTMENTS.forEach(enchantment ->
			{if(enchantment.category == type) addEnchantments(enchantment);});
		}
		
		return this;
	}
	
	public Set<ToolType> getToolTypes() {return toolType;}
	//TODO How about functions that checks if a material/enchantment is valid, rather than make the whole lists accessible?
	public Set<Material> getHarvestMaterials() {return harvestMaterials;}
	public Set<Enchantment> getEnchantments() {return enchantments;}
}
