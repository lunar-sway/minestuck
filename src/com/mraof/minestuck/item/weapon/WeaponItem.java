package com.mraof.minestuck.item.weapon;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import net.minecraft.block.BlockState;
import net.minecraft.block.material.Material;
import net.minecraft.item.IItemTier;
import net.minecraft.item.ItemStack;
import net.minecraft.item.SwordItem;
import net.minecraftforge.common.ToolType;

//TODO add system for Minestuck weapons that can replace enchantments
public class WeaponItem extends SwordItem //To allow enchantments such as sharpness
{
	private float efficiency;
	private static final HashMap<ToolType, Set<Material>> toolMaterials = new HashMap<>();
	
	public WeaponItem(IItemTier tier, int attackDamageIn, float attackSpeedIn, float efficiency, Properties builder)
	{
		super(tier, attackDamageIn, attackSpeedIn, builder);
		this.efficiency = efficiency;
	}

	@Override
	public float getDestroySpeed(ItemStack stack, BlockState state)
	{
		Material material = state.getMaterial();
		for(ToolType tool : getToolTypes(stack))
		{
			if(state.isToolEffective(tool) || toolMaterials.get(tool).contains(material))
			{
				return efficiency;
			}
		}
		return super.getDestroySpeed(stack, state);
	}
	
	//Thanks to Mraof for supplying the base for this method.
	@Override
	public boolean canHarvestBlock(BlockState blockIn)
	{
        ToolType tool = blockIn.getHarvestTool();
        if(getToolTypes(new ItemStack(this)).contains(tool))
        {
            int blockHarvestLevel = blockIn.getHarvestLevel();
            int toolHarvestLevel = getHarvestLevel(new ItemStack(this), tool, null, blockIn);
            return toolHarvestLevel >= blockHarvestLevel;
        } else		//We know that no specific harvestTool is specified, meaning any harvestTool efficiency is defined in the harvestTool itself.
        {			//This also means that there's no harvestTool *level* specified, so any harvestTool of that class is sufficient.
        	Material mat = blockIn.getMaterial();
        	if(mat.isToolNotRequired())
        		return true;
        	for(ToolType tool2 : getToolTypes(new ItemStack(this)))
        	{
        		if(toolMaterials.get(tool2) != null && toolMaterials.get(tool2).contains(mat))
        		{
        			return true;
        		}
        	}
        }
        return super.canHarvestBlock(blockIn);
	}
	
	public static void addToolMaterial(ToolType tool, Collection<Material> materials)
	{
		if(!toolMaterials.containsKey(tool))
			toolMaterials.put(tool, new HashSet<>());
		toolMaterials.get(tool).addAll(materials);
	}
	
	public float getEfficiency()		{return efficiency;}
}