package com.mraof.minestuck.item.weapon;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Streams;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.core.BlockPos;
import net.minecraft.core.component.DataComponents;
import net.minecraft.tags.TagKey;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.item.component.Tool;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.common.ItemAbility;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.*;
import java.util.stream.Stream;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class WeaponItem extends TieredItem
{
	private final float efficiency;
	private final boolean disableShield;
	//private static final HashMap<ToolType, Set<Material>> toolMaterials = new HashMap<>();
	
	@Nullable
	private final List<MSToolType> toolTypes;
	private final Set<ItemAbility> toolActions;
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
	
	public WeaponItem(Builder builder, Properties properties)
	{
		super(builder.tier, builder.updateProperties(properties));
		toolTypes = builder.toolType;
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
	}
	
	public boolean canAttackBlock(BlockState state, Level level, BlockPos pos, Player player)
	{
		return !player.isCreative();
	}
	
	
	@Override
	public boolean mineBlock(ItemStack stack, Level level, BlockState state, BlockPos pos, LivingEntity entityLiving)
	{
		if(destroyBlockEffect != null)
			destroyBlockEffect.onDestroyBlock(stack, level, state, pos, entityLiving);
		return super.mineBlock(stack, level, state, pos, entityLiving);
	}
	
	
	
	@Override
	public boolean canPerformAction(ItemStack stack, ItemAbility toolAction)
	{
		return this.toolActions.contains(toolAction);
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
		stack.hurtAndBreak(1, attacker, EquipmentSlot.MAINHAND);
		onHitEffects.forEach(effect -> effect.onHit(stack, target, attacker));
		return true;
	}
	
	@Nullable
	public List<MSToolType> getToolTypes()
	{
		return toolTypes;
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
		private List<MSToolType> toolType = List.of();
		private final Set<ItemAbility> toolActions = new HashSet<>();
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
		
		public Builder set(MSToolType... toolTypes)
		{
			this.toolType = List.of(toolTypes);
			for(MSToolType toolType : this.toolType)
				toolActions.addAll(List.of(toolType.abilities()));
			return this;
		}
		
		public Builder set(DestroyBlockEffect effect)
		{
			if(destroyBlockEffect != null)
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
		
		public Builder add(ItemAbility... actions)
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
		
		private Properties updateProperties(Properties properties)
		{
			properties.attributes(DiggerItem.createAttributes(this.tier, this.attackDamage, this.attackSpeed));
			ArrayList<TagKey<Block>> mineableBlocks = new ArrayList<>();
			for(MSToolType toolType : this.toolType)
			{
				if(toolType.mineableBlocks() != null)
					mineableBlocks.add(toolType.mineableBlocks());
			}
			if(!mineableBlocks.isEmpty())
				properties.component(DataComponents.TOOL, createToolProperties(this.tier, mineableBlocks, efficiency));
			return properties;
		}
	}
	
	private static Tool createToolProperties(Tier tier, List<TagKey<Block>> mineableBlocks, float efficiency)
	{
		return new Tool(Streams.concat(mineableBlocks.stream().map(tag -> Tool.Rule.minesAndDrops(tag, efficiency)), Stream.of(Tool.Rule.deniesDrops(tier.getIncorrectBlocksForDrops()))).toList(),
				1.0F, 1);
	}
}
