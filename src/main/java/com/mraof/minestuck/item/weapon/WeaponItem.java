package com.mraof.minestuck.item.weapon;

import com.google.common.collect.ImmutableList;
import net.minecraft.block.BlockState;
import net.minecraft.block.material.Material;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.IItemTier;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUseContext;
import net.minecraft.item.SwordItem;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.ToolType;

import javax.annotation.Nullable;
import java.util.*;

public class WeaponItem extends SwordItem //To allow weapons to have the sweep effect
{
	private final float efficiency;
	//private static final HashMap<ToolType, Set<Material>> toolMaterials = new HashMap<>();
	
	@Nullable
	private final MSToolType toolType;
	private final List<OnHitEffect> onHitEffects;
	@Nullable
	private final DestroyBlockEffect destroyBlockEffect;
	@Nullable
	private final ItemUseEffect itemUseEffect;
	
	@Deprecated
	public WeaponItem(IItemTier tier, int attackDamage, float attackSpeed, float efficiency, @Nullable MSToolType toolType, Properties properties)
	{
		this(new Builder(tier, attackDamage, attackSpeed).efficiency(efficiency).set(toolType), properties);
	}
	
	public WeaponItem(Builder builder, Properties properties)
	{
		super(builder.tier, builder.attackDamage, builder.attackSpeed, properties);
		toolType = builder.toolType;
		efficiency = builder.efficiency;
		onHitEffects = ImmutableList.copyOf(builder.onHitEffects);
		destroyBlockEffect = builder.destroyBlockEffect;
		itemUseEffect = builder.itemUseEffect;
	}
	
	@Override
	public float getDestroySpeed(ItemStack stack, BlockState state)
	{
		for(ToolType tool : getToolTypes(stack))
			if(state.isToolEffective(tool))
				return efficiency;
		if(toolType != null && toolType.canHarvest(state))
			return efficiency;
			
		return super.getDestroySpeed(stack, state);
	}
	
	//Thanks to Mraof for supplying the base for this method.
	@Override
	public boolean canHarvestBlock(BlockState blockIn)
	{
        ToolType blockTool = blockIn.getHarvestTool();
		Set<ToolType> itemTools = getToolTypes(new ItemStack(this));
		int blockHarvestLevel = blockIn.getHarvestLevel();
		int toolHarvestLevel = getHarvestLevel(new ItemStack(this), blockTool, null, blockIn);
		
        if(blockTool != null && itemTools.contains(blockTool))
        {
            return toolHarvestLevel >= blockHarvestLevel;
        } else		//We know that no specific harvestTool is specified, meaning any harvestTool efficiency is defined in the harvestTool itself.
        {			//This also means that there's no harvestTool *level* specified, so any harvestTool of that class is sufficient.
        	Material mat = blockIn.getMaterial();
        	if(mat.isToolNotRequired())
        		return true;
        	
        	if(toolType != null)
			{
				if(toolType.getHarvestMaterials().contains(mat) && toolHarvestLevel >= blockHarvestLevel)
					return true;
			}
			return super.canHarvestBlock(blockIn);
        }
        
	}
	
	@Override
	public boolean onBlockDestroyed(ItemStack stack, World worldIn, BlockState state, BlockPos pos, LivingEntity entityLiving)
	{
		if(destroyBlockEffect != null)
			destroyBlockEffect.onDestroyBlock(stack, worldIn, state, pos, entityLiving);
		
		if (state.getBlockHardness(worldIn, pos) != 0.0F)
		{
			int damage = 2;
			
			if(getToolTypes(stack).contains(state.getHarvestTool()))
				damage = 1;
			else if(toolType != null)
			{
				if(toolType.getHarvestMaterials().contains(state.getMaterial()))
					damage = 1;
			}
			
			stack.damageItem(damage, entityLiving, (entity) -> entity.sendBreakAnimation(EquipmentSlotType.MAINHAND));
		}
		
		return true;
	}
	
	@Override
	public ActionResultType onItemUse(ItemUseContext context)
	{
		if(itemUseEffect != null)
			return itemUseEffect.onItemUse(context);
		else return super.onItemUse(context);
	}
	
	@Override
	public boolean hitEntity(ItemStack stack, LivingEntity target, LivingEntity attacker)
	{
		onHitEffects.forEach(effect -> effect.onHit(stack, target, attacker));
		return super.hitEntity(stack, target, attacker);
	}
	
	@Override
	public int getHarvestLevel(ItemStack stack, ToolType tool, @Nullable PlayerEntity player, @Nullable BlockState blockState)
	{
		int harvestLevel = super.getHarvestLevel(stack, tool, player, blockState);
		if(harvestLevel == -1 && getToolTypes(stack).contains(tool))
			return getTier().getHarvestLevel();
		return harvestLevel;
	}
	
	@Override
	public Set<ToolType> getToolTypes(ItemStack stack)
	{
		if(toolType == null)
			return super.getToolTypes(stack);
		else
		{
			Set<ToolType> types = new HashSet<>();
			types.addAll(toolType.getToolTypes());
			types.addAll(super.getToolTypes(stack));
			return types;
		}
	}
	
	@Override
	public boolean isEnchantable(ItemStack stack)
	{
		return isDamageable() || (toolType != null && !toolType.getEnchantments().isEmpty());
	}
	
	@Override
	public boolean canApplyAtEnchantingTable(ItemStack stack, Enchantment enchantment)
	{
		if(isDamageable() && enchantment.type == EnchantmentType.BREAKABLE)
			return true;
		if(toolType == null)
			return false;
		
		return toolType.getEnchantments().contains(enchantment);
	}
	
	@Nullable
	public MSToolType getToolType() {return toolType;}
	public float getEfficiency()		{return efficiency;}
	
	public static class Builder
	{
		private final IItemTier tier;
		private final int attackDamage;
		private final float attackSpeed;
		@Nullable
		private MSToolType toolType;
		private float efficiency;
		private final List<OnHitEffect> onHitEffects = new ArrayList<>();
		@Nullable
		private DestroyBlockEffect destroyBlockEffect = null;
		@Nullable
		private ItemUseEffect itemUseEffect = null;
		
		public Builder(IItemTier tier, int attackDamage, float attackSpeed)
		{
			this.tier = tier;
			this.attackDamage = attackDamage;
			this.attackSpeed = attackSpeed;
			efficiency = tier.getEfficiency();
		}
		
		public Builder set(@Nullable MSToolType toolType)
		{
			this.toolType = toolType;
			return this;
		}
		
		public Builder set(DestroyBlockEffect effect)
		{
			destroyBlockEffect = effect;
			return this;
		}
		
		public Builder set(ItemUseEffect effect)
		{
			itemUseEffect = effect;
			return this;
		}
		
		public Builder efficiency(float efficiency)
		{
			this.efficiency = efficiency;
			return this;
		}
		
		public Builder add(OnHitEffect... effects)
		{
			onHitEffects.addAll(Arrays.asList(effects));
			return this;
		}
	}
}