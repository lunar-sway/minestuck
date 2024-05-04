package com.mraof.minestuck.item.weapon;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.TieredItem;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentCategory;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.common.TierSortingRegistry;
import net.neoforged.neoforge.common.ToolAction;
import javax.annotation.Nullable;
import java.util.*;

public class WeaponItem extends TieredItem
{
	private final float efficiency;
	private final boolean disableShield;
	//private static final HashMap<ToolType, Set<Material>> toolMaterials = new HashMap<>();
	
	@Nullable
	private final MSToolType toolType;
	private final Set<ToolAction> toolActions;
	private final List<OnHitEffect> onHitEffects;
	@Nullable
	private final DestroyBlockEffect destroyBlockEffect;
	@Nullable
	private final RightClickBlockEffect rightClickBlockEffect;
	@Nullable
	private final ItemRightClickEffect itemRightClickEffect;
	private final int useDuration;
	private final UseAnim useAction;
	private final List<FinishUseItemEffect> itemUsageEffects;
	private final List<InventoryTickEffect> tickEffects;
	//Item attributes that are applied when the weapon is in the main hand.
	private final Multimap<Attribute, AttributeModifier> attributeModifiers;
	
	@Deprecated
	public WeaponItem(Tier tier, int attackDamage, float attackSpeed, float efficiency, @Nullable MSToolType toolType, Properties properties)
	{
		this(new Builder(tier, attackDamage, attackSpeed).efficiency(efficiency).set(toolType), properties);
	}
	
	public WeaponItem(Builder builder, Properties properties)
	{
		super(builder.tier, properties);
		toolType = builder.toolType;
		toolActions = builder.toolActions;
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
		if(toolType != null && toolType.canHarvest(state))
			return efficiency;
		
		return super.getDestroySpeed(stack, state);
	}
	
	public boolean canAttackBlock(BlockState state, Level level, BlockPos pos, Player player)
	{
		if(this.toolType != null && this.toolType.canHarvest(state))
		{
			return true;
		}
		return !player.isCreative();
	}
	
	//Thanks to Mraof for supplying the base for this method.
	@Override
	public boolean isCorrectToolForDrops(ItemStack item, BlockState blockIn)
	{
		return this.toolType != null && this.toolType.canHarvest(blockIn) && TierSortingRegistry.isCorrectTierForDrops(this.getTier(), blockIn);
	}
	
	@Override
	public boolean mineBlock(ItemStack stack, Level level, BlockState state, BlockPos pos, LivingEntity entityLiving)
	{
		if(destroyBlockEffect != null)
			destroyBlockEffect.onDestroyBlock(stack, level, state, pos, entityLiving);
		
		if(state.getDestroySpeed(level, pos) != 0.0F)
		{
			int damage;
			
			if(toolType != null && toolType.canHarvest(state))
				damage = 1;
			else
				damage = 2;
			
			stack.hurtAndBreak(damage, entityLiving, (entity) -> entity.broadcastBreakEvent(EquipmentSlot.MAINHAND));
		}
		
		return true;
	}
	
	@Override
	public boolean canPerformAction(ItemStack stack, ToolAction toolAction)
	{
		return this.toolActions.contains(toolAction) || this.toolType != null && this.toolType.hasAction(toolAction);
	}
	
	@Override
	public InteractionResult useOn(UseOnContext context)
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
	public InteractionResultHolder<ItemStack> use(Level level, Player playerIn, InteractionHand handIn)
	{
		if(itemRightClickEffect != null)
			return itemRightClickEffect.onRightClick(level, playerIn, handIn);
		else return super.use(level, playerIn, handIn);
	}
	
	@Override
	public int getUseDuration(ItemStack stack)
	{
		return useDuration;
	}
	
	@Override
	public UseAnim getUseAnimation(ItemStack stack)
	{
		return useAction;
	}
	
	@Override
	public ItemStack finishUsingItem(ItemStack stack, Level level, LivingEntity entityLiving)
	{
		for(FinishUseItemEffect effect : itemUsageEffects)
			stack = effect.onItemUseFinish(stack, level, entityLiving);
		return super.finishUsingItem(stack, level, entityLiving);
	}
	
	@Override
	public void inventoryTick(ItemStack stack, Level level, Entity entityIn, int itemSlot, boolean isSelected)
	{
		for(InventoryTickEffect effect : tickEffects)
			effect.inventoryTick(stack, level, entityIn, itemSlot, isSelected);
	}
	
	@Override
	public boolean hurtEnemy(ItemStack stack, LivingEntity target, LivingEntity attacker)
	{
		stack.hurtAndBreak(1, attacker, (PlayerEntity) -> PlayerEntity.broadcastBreakEvent(EquipmentSlot.MAINHAND));
		onHitEffects.forEach(effect -> effect.onHit(stack, target, attacker));
		return true;
	}
	
	@Override
	public boolean isEnchantable(ItemStack stack)
	{
		return canBeDepleted() || (toolType != null && !toolType.getEnchantments().isEmpty());
	}
	
	@Override
	public boolean canApplyAtEnchantingTable(ItemStack stack, Enchantment enchantment)
	{
		if(canBeDepleted() && enchantment.category == EnchantmentCategory.BREAKABLE)
			return true;
		if(toolType == null)
			return false;
		
		return toolType.getEnchantments().contains(enchantment);
	}
	
	@Override
	public Multimap<Attribute, AttributeModifier> getAttributeModifiers(EquipmentSlot slot, ItemStack stack)
	{
		return slot == EquipmentSlot.MAINHAND ? attributeModifiers : super.getAttributeModifiers(slot, stack);
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
	
	public ItemRightClickEffect getItemRightClickEffect()
	{
		return itemRightClickEffect;
	}
	
	public static class Builder
	{
		private final Tier tier;
		private final int attackDamage;
		private final float attackSpeed;
		@Nullable
		private MSToolType toolType;
		private final Set<ToolAction> toolActions = new HashSet<>();
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
		private UseAnim useAction = UseAnim.NONE;
		private final List<FinishUseItemEffect> itemUsageEffects = new ArrayList<>();
		private final List<InventoryTickEffect> tickEffects = new ArrayList<>();
		
		public Builder(Tier tier, int attackDamage, float attackSpeed)
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
		
		public Builder add(ToolAction... actions)
		{
			toolActions.addAll(List.of(actions));
			return this;
		}
		
		public Builder setEating(FinishUseItemEffect... effects)
		{
			return addItemUses(32, UseAnim.EAT, effects);
		}
		
		public Builder setEating(int duration, FinishUseItemEffect... effects)
		{
			return addItemUses(duration, UseAnim.EAT, effects);
		}
		
		public Builder addItemUses(int duration, UseAnim action, FinishUseItemEffect... effects)
		{
			useDuration = duration;
			useAction = action;
			itemUsageEffects.addAll(Arrays.asList(effects));
			set(ItemRightClickEffect.ACTIVE_HAND);
			return this;
		}
	}
}