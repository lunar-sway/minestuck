package com.mraof.minestuck.item.weapon;

import java.util.*;

import net.minecraft.block.BlockState;
import net.minecraft.block.material.Material;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.IItemTier;
import net.minecraft.item.ItemStack;
import net.minecraft.item.SwordItem;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.ToolType;

//TODO add system for Minestuck weapons that can replace enchantments
public class WeaponItem extends SwordItem //To allow weapons to have the sweep effect
{
	private float efficiency;
	//private static final HashMap<ToolType, Set<Material>> toolMaterials = new HashMap<>();
	
	private MSToolType toolType;
	
	public WeaponItem(IItemTier tier, int attackDamageIn, float attackSpeedIn, float efficiency, Properties builder)
	{
		super(tier, attackDamageIn, attackSpeedIn, builder);
		this.efficiency = efficiency;
	}
	
	public WeaponItem setTool(MSToolType toolType)
	{
		this.toolType = toolType;
		return this;
	}
	
	@Override
	public float getDestroySpeed(ItemStack stack, BlockState state)
	{
		Material material = state.getMaterial();
		for(ToolType tool : getToolTypes(stack))
			if(state.isToolEffective(tool))
				return efficiency;
		if(toolType.canHarvest(state))
			return efficiency;
			
		return super.getDestroySpeed(stack, state);
	}
	
	//Thanks to Mraof for supplying the base for this method.
	@Override
	public boolean canHarvestBlock(BlockState blockIn)
	{
        ToolType blockTool = blockIn.getHarvestTool();
		List<ToolType> itemTools = new ArrayList<>();
		itemTools.addAll(getToolTypes(new ItemStack(this)));
		itemTools.addAll(toolType.getToolTypes());
		int blockHarvestLevel = blockIn.getHarvestLevel();
		int toolHarvestLevel = toolHarvestLevel = getTier().getHarvestLevel();
		
        if(itemTools.contains(blockTool))
        {
            return toolHarvestLevel >= blockHarvestLevel;
        } else		//We know that no specific harvestTool is specified, meaning any harvestTool efficiency is defined in the harvestTool itself.
        {			//This also means that there's no harvestTool *level* specified, so any harvestTool of that class is sufficient.
        	Material mat = blockIn.getMaterial();
        	if(mat.isToolNotRequired())
        		return true;
        	
        	if(toolType != null)
			{
				if(toolType.getHarvestMaterials().contains(mat) && toolHarvestLevel >= blockHarvestLevel);
					return true;
			}
			return super.canHarvestBlock(blockIn);
        }
        
	}
	
	@Override
	public boolean onBlockDestroyed(ItemStack stack, World worldIn, BlockState state, BlockPos pos, LivingEntity entityLiving)
	{
		if (state.getBlockHardness(worldIn, pos) != 0.0F)
		{
			int dmg = 2;
			
			if(getToolTypes(stack).contains(state.getHarvestTool()))
				dmg = 1;
			else if(toolType != null)
			{
				if(toolType.getHarvestMaterials().contains(state.getMaterial()) || toolType.getToolTypes().contains(state.getHarvestTool()))
					dmg = 1;
			}
			
			stack.damageItem(dmg, entityLiving, (p_220044_0_) -> {
				p_220044_0_.sendBreakAnimation(EquipmentSlotType.MAINHAND);
			});
		}
		
		return true;
	}
	
	@Override
	public boolean isEnchantable(ItemStack stack)
	{
		return isDamageable() || (toolType != null && !toolType.getEnchantments().isEmpty());
	}
	
	@Override
	public boolean canApplyAtEnchantingTable(ItemStack stack, Enchantment enchantment)
	{
		if(isDamageable() && enchantment.type.equals(EnchantmentType.BREAKABLE))
			return true;
		if(toolType == null)
			return false;
		
		return toolType.getEnchantments().contains(enchantment);
	}
	
	public MSToolType getToolType() {return toolType;}
	public float getEfficiency()		{return efficiency;}
}