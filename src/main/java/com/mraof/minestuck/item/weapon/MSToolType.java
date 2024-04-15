package com.mraof.minestuck.item.weapon;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentCategory;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.common.ToolAction;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class MSToolType
{
	private final Set<Enchantment> enchantments = new HashSet<>();
	private final Set<TagKey<Block>> miningEfficiencyTags = new HashSet<>();
	private final Set<ToolAction> miningActions = new HashSet<>();
	
	public MSToolType()
	{
	}
	
	public MSToolType(MSToolType... classCombo)
	{
		for(MSToolType cls : classCombo)
		{
			enchantments.addAll(cls.enchantments);
			miningEfficiencyTags.addAll(cls.miningEfficiencyTags);
			miningActions.addAll(cls.miningActions);
		}
	}
	
	public boolean canHarvest(BlockState state)
	{
		for(TagKey<Block> tag : miningEfficiencyTags)
			if(state.is(tag))
				return true;
		
		return false;
	}
	
	public MSToolType addAction(ToolAction action)
	{
		this.miningActions.add(action);
		return this;
	}
	
	public MSToolType addMining(TagKey<Block> tag, ToolAction action)
	{
		this.miningEfficiencyTags.add(tag);
		return this.addAction(action);
	}
	
	public boolean hasAction(ToolAction toolAction)
	{
		return miningActions.contains(toolAction);
	}
	
	//TODO Tool types WILL be created before mod enchantments are created and registered. We should use suppliers instead
	public MSToolType addEnchantments(Enchantment... enchantments)
	{
		this.enchantments.addAll(Arrays.asList(enchantments));
		return this;
	}
	
	public MSToolType addEnchantments(EnchantmentCategory... enchantmentTypes)
	{
		for(EnchantmentCategory type : enchantmentTypes)
		{
			BuiltInRegistries.ENCHANTMENT.forEach(enchantment ->
			{
				if(enchantment.category == type) addEnchantments(enchantment);
			});
		}
		
		return this;
	}
	
	//TODO How about functions that checks if an enchantment is valid, rather than make the whole lists accessible?
	public Set<Enchantment> getEnchantments() {return enchantments;}
}
