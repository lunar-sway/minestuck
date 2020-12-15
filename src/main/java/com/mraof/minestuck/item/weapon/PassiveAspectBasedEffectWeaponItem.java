package com.mraof.minestuck.item.weapon;

import com.mraof.minestuck.player.EnumAspect;
import com.mraof.minestuck.player.Title;
import com.mraof.minestuck.world.storage.PlayerSavedData;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.IItemTier;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.EffectInstance;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.function.Supplier;

public class PassiveAspectBasedEffectWeaponItem extends WeaponItem
{
	private final EnumAspect aspect;
	private final Supplier<EffectInstance> effect;
	
	public PassiveAspectBasedEffectWeaponItem(IItemTier tier, int attackDamageIn, float attackSpeedIn, float efficiency, EnumAspect aspect, Supplier<EffectInstance> effect, @Nullable MSToolType toolType, Properties builder)
	{
		super(tier, attackDamageIn, attackSpeedIn, efficiency, toolType, builder);
		this.aspect = aspect;
		this.effect = effect;
	}
	
	@Override
	public void inventoryTick(ItemStack stack, World worldIn, Entity entityIn, int itemSlot, boolean isSelected)
	{
		if(isSelected && entityIn instanceof ServerPlayerEntity)
		{
			Title title = PlayerSavedData.getData((ServerPlayerEntity) entityIn).getTitle();
			if(title != null)
			{
				if(title.getHeroAspect() == aspect)
					((ServerPlayerEntity) entityIn).addPotionEffect(effect.get());
			}
		}
	}
}