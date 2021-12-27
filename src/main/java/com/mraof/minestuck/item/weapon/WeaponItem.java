package com.mraof.minestuck.item.weapon;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import net.minecraft.block.BlockState;
import net.minecraft.block.material.Material;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentType;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.attributes.Attribute;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.*;
import net.minecraft.util.ActionResult;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.ToolType;

import javax.annotation.Nullable;
import java.util.*;

public class WeaponItem extends TieredItem
{
	private final float efficiency;
	private final boolean disableShield;
	//private static final HashMap<ToolType, Set<Material>> toolMaterials = new HashMap<>();
	
	@Nullable
	private final MSToolType toolType;
	private final List<OnHitEffect> onHitEffects;
	@Nullable
	private final DestroyBlockEffect destroyBlockEffect;
	@Nullable
	private final RightClickBlockEffect rightClickBlockEffect;
	@Nullable
	private final ItemRightClickEffect itemRightClickEffect;
	private final int useDuration;
	private final UseAction useAction;
	private final List<FinishUseItemEffect> itemUsageEffects;
	private final List<InventoryTickEffect> tickEffects;
	//Item attributes that are applied when the weapon is in the main hand.
	private final Multimap<Attribute, AttributeModifier> attributeModifiers;
	
	@Deprecated
	public WeaponItem(IItemTier tier, int attackDamage, float attackSpeed, float efficiency, @Nullable MSToolType toolType, Properties properties)
	{
		this(new Builder(tier, attackDamage, attackSpeed).efficiency(efficiency).set(toolType), properties);
	}
	
	public WeaponItem(Builder builder, Properties properties)
	{
		super(builder.tier, properties);
		toolType = builder.toolType;
		efficiency = builder.efficiency;
		disableShield = builder.disableShield;
		onHitEffects = ImmutableList.copyOf(builder.onHitEffects);
		destroyBlockEffect = builder.destroyBlockEffect;
		rightClickBlockEffect = builder.rightClickBlockEffect;
		itemRightClickEffect = builder.itemRightClickEffect;
		useDuration = builder.useDuration;
		useAction = builder.useAction;
		itemUsageEffects = ImmutableList.copyOf(builder.itemUsageEffects);
		tickEffects = ImmutableList.copyOf(builder.tickEffects);
		
		ImmutableMultimap.Builder<Attribute, AttributeModifier> modifiers = ImmutableMultimap.builder();
		modifiers.put(Attributes.ATTACK_DAMAGE, new AttributeModifier(BASE_ATTACK_DAMAGE_UUID, "Weapon modifier", (double) builder.attackDamage + getTier().getAttackDamageBonus(), AttributeModifier.Operation.ADDITION));
		modifiers.put(Attributes.ATTACK_SPEED, new AttributeModifier(BASE_ATTACK_SPEED_UUID, "Weapon modifier", builder.attackSpeed, AttributeModifier.Operation.ADDITION));
		attributeModifiers = modifiers.build();
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
	
	public boolean canAttackBlock(BlockState state, World worldIn, BlockPos pos, PlayerEntity player)
	{
		ToolType blockTool = state.getHarvestTool();
		Set<ToolType> itemTools = getToolTypes(new ItemStack(this));
		if(blockTool != null && itemTools.contains(blockTool))
		{
			return true;
		}
		return !player.isCreative();
	}
	
	//Thanks to Mraof for supplying the base for this method.
	@Override
	public boolean isCorrectToolForDrops(BlockState blockIn)
	{
		ToolType blockTool = blockIn.getHarvestTool();
		Set<ToolType> itemTools = getToolTypes(new ItemStack(this));
		int blockHarvestLevel = blockIn.getHarvestLevel();
		int toolHarvestLevel = getHarvestLevel(new ItemStack(this), blockTool, null, blockIn);
		
		if(blockTool != null && itemTools.contains(blockTool))
		{
			return toolHarvestLevel >= blockHarvestLevel;
		} else        //We know that no specific harvestTool is specified, meaning any harvestTool efficiency is defined in the harvestTool itself.
		{            //This also means that there's no harvestTool *level* specified, so any harvestTool of that class is sufficient.
			Material mat = blockIn.getMaterial();
			//if(mat.isToolNotRequired())
			//	return true;
			
			if(toolType != null)
			{
				if(toolType.getHarvestMaterials().contains(mat) && toolHarvestLevel >= blockHarvestLevel)
					return true;
			}
			return super.isCorrectToolForDrops(blockIn);
		}
		
	}
	
	@Override
	public boolean mineBlock(ItemStack stack, World worldIn, BlockState state, BlockPos pos, LivingEntity entityLiving)
	{
		if(destroyBlockEffect != null)
			destroyBlockEffect.onDestroyBlock(stack, worldIn, state, pos, entityLiving);
		
		if(state.getDestroySpeed(worldIn, pos) != 0.0F)
		{
			int damage = 2;
			
			if(getToolTypes(stack).contains(state.getHarvestTool()))
				damage = 1;
			else if(toolType != null)
			{
				if(toolType.getHarvestMaterials().contains(state.getMaterial()))
					damage = 1;
			}
			
			stack.hurtAndBreak(damage, entityLiving, (entity) -> entity.broadcastBreakEvent(EquipmentSlotType.MAINHAND));
		}
		
		return true;
	}
	
	@Override
	public ActionResultType useOn(ItemUseContext context)
	{
		if(rightClickBlockEffect != null)
			return rightClickBlockEffect.onClick(context);
		else return super.useOn(context);
	}
	
	@Override
	public boolean canDisableShield(ItemStack stack, ItemStack shield, LivingEntity entity, LivingEntity attacker)
	{
		return disableShield;
	}
	
	@Override
	public ActionResult<ItemStack> use(World worldIn, PlayerEntity playerIn, Hand handIn)
	{
		if(itemRightClickEffect != null)
			return itemRightClickEffect.onRightClick(worldIn, playerIn, handIn);
		else return super.use(worldIn, playerIn, handIn);
	}
	
	@Override
	public int getUseDuration(ItemStack stack)
	{
		return useDuration;
	}
	
	@Override
	public UseAction getUseAnimation(ItemStack stack)
	{
		return useAction;
	}
	
	@Override
	public ItemStack finishUsingItem(ItemStack stack, World worldIn, LivingEntity entityLiving)
	{
		for(FinishUseItemEffect effect : itemUsageEffects)
			stack = effect.onItemUseFinish(stack, worldIn, entityLiving);
		return super.finishUsingItem(stack, worldIn, entityLiving);
	}
	
	@Override
	public void inventoryTick(ItemStack stack, World worldIn, Entity entityIn, int itemSlot, boolean isSelected)
	{
		for(InventoryTickEffect effect : tickEffects)
			effect.inventoryTick(stack, worldIn, entityIn, itemSlot, isSelected);
	}
	
	@Override
	public boolean hurtEnemy(ItemStack stack, LivingEntity target, LivingEntity attacker)
	{
		stack.hurtAndBreak(1, attacker, (PlayerEntity) -> PlayerEntity.broadcastBreakEvent(EquipmentSlotType.MAINHAND));
		onHitEffects.forEach(effect -> effect.onHit(stack, target, attacker));
		return true;
	}
	
	@Override
	public int getHarvestLevel(ItemStack stack, ToolType tool, @Nullable PlayerEntity player, @Nullable BlockState blockState)
	{
		int harvestLevel = super.getHarvestLevel(stack, tool, player, blockState);
		if(harvestLevel == -1 && getToolTypes(stack).contains(tool))
			return getTier().getLevel();
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
		return canBeDepleted() || (toolType != null && !toolType.getEnchantments().isEmpty());
	}
	
	@Override
	public boolean canApplyAtEnchantingTable(ItemStack stack, Enchantment enchantment)
	{
		if(canBeDepleted() && enchantment.category == EnchantmentType.BREAKABLE)
			return true;
		if(toolType == null)
			return false;
		
		return toolType.getEnchantments().contains(enchantment);
	}
	
	@Override
	public Multimap<Attribute, AttributeModifier> getAttributeModifiers(EquipmentSlotType slot, ItemStack stack)
	{
		return slot == EquipmentSlotType.MAINHAND ? attributeModifiers : super.getAttributeModifiers(slot, stack);
	}
	
	@Nullable
	public MSToolType getToolType()
	{
		return toolType;
	}
	
	public float getEfficiency()
	{
		return efficiency;
	}
	
	public boolean getDisableShield()
	{
		return disableShield;
	}
	
	public static class Builder
	{
		private final IItemTier tier;
		private final int attackDamage;
		private final float attackSpeed;
		@Nullable
		private MSToolType toolType;
		private float efficiency;
		private boolean disableShield;
		private final List<OnHitEffect> onHitEffects = new ArrayList<>();
		@Nullable
		private DestroyBlockEffect destroyBlockEffect = null;
		@Nullable
		private RightClickBlockEffect rightClickBlockEffect = null;
		@Nullable
		private ItemRightClickEffect itemRightClickEffect;
		private int useDuration = 0;
		private UseAction useAction = UseAction.NONE;
		private final List<FinishUseItemEffect> itemUsageEffects = new ArrayList<>();
		private final List<InventoryTickEffect> tickEffects = new ArrayList<>();
		
		public Builder(IItemTier tier, int attackDamage, float attackSpeed)
		{
			this.tier = tier;
			this.attackDamage = attackDamage;
			this.attackSpeed = attackSpeed;
			efficiency = tier.getSpeed();
		}
		
		public Builder set(@Nullable MSToolType toolType)
		{
			this.toolType = toolType;
			return this;
		}
		
		public Builder set(DestroyBlockEffect effect)
		{
			if(rightClickBlockEffect != null)
				throw new IllegalStateException("Destroy block effect has already been set");
			destroyBlockEffect = effect;
			return this;
		}
		
		public Builder set(RightClickBlockEffect effect)
		{
			if(rightClickBlockEffect != null)
				throw new IllegalStateException("Right click block effect has already been set");
			rightClickBlockEffect = effect;
			return this;
		}
		
		public Builder set(ItemRightClickEffect effect)
		{
			if(itemRightClickEffect != null)
				throw new IllegalStateException("Item right click effect has already been set");
			itemRightClickEffect = effect;
			return this;
		}
		
		public Builder efficiency(float efficiency)
		{
			this.efficiency = efficiency;
			return this;
		}
		
		public Builder disableShield()
		{
			disableShield = true;
			return this;
		}
		
		public Builder add(OnHitEffect... effects)
		{
			onHitEffects.addAll(Arrays.asList(effects));
			return this;
		}
		
		public Builder add(InventoryTickEffect... effects)
		{
			tickEffects.addAll(Arrays.asList(effects));
			return this;
		}
		
		public Builder setEating(int useDuration, FinishUseItemEffect... effects)
		{
			return addItemUses(useDuration, UseAction.EAT, effects);
		}
		
		public Builder addItemUses(int duration, UseAction action, FinishUseItemEffect... effects)
		{
			useDuration = duration;
			useAction = action;
			itemUsageEffects.addAll(Arrays.asList(effects));
			set(ItemRightClickEffect.ACTIVE_HAND);
			return this;
		}
	}
}