package com.mraof.minestuck.item.weapon.aspect;

import com.mraof.minestuck.item.MSItems;
import com.mraof.minestuck.item.weapon.MSToolType;
import com.mraof.minestuck.item.weapon.WeaponItem;
import com.mraof.minestuck.player.EnumAspect;
import com.mraof.minestuck.player.Title;
import com.mraof.minestuck.util.MSSoundEvents;
import com.mraof.minestuck.world.storage.PlayerSavedData;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.IItemTier;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.util.SoundCategory;

import javax.annotation.Nullable;
import java.util.function.Supplier;

public class AspectBasedEffectWeaponItem extends WeaponItem
{
	private final EnumAspect aspect;
	private final Supplier<EffectInstance> playerEffect;
	private final Supplier<EffectInstance> enemyEffect;
	
	public AspectBasedEffectWeaponItem(IItemTier tier, int attackDamageIn, float attackSpeedIn, float efficiency, EnumAspect aspect, Supplier<EffectInstance> playerEffect, Supplier<EffectInstance> enemyEffect, @Nullable MSToolType toolType, Properties builder)
	{
		super(tier, attackDamageIn, attackSpeedIn, efficiency, toolType, builder);
		this.aspect = aspect;
		this.playerEffect = playerEffect;
		this.enemyEffect = enemyEffect;
	}
	
	@Override
	public boolean hitEntity(ItemStack itemStack, LivingEntity target, LivingEntity attacker)
	{
		boolean critical = attacker.fallDistance > 0.0F && !attacker.onGround && !attacker.isOnLadder() && !attacker.isInWater() && !attacker.isPotionActive(Effects.BLINDNESS) && !attacker.isPassenger() && !attacker.isBeingRidden();
		float randFloat = attacker.getRNG().nextFloat();
		
		if(attacker instanceof ServerPlayerEntity)
		{
			Title title = PlayerSavedData.getData((ServerPlayerEntity) attacker).getTitle();
			
			if(critical)
				randFloat = randFloat - .1F;
			if(title != null && randFloat < .1)
			{
				if(title.getHeroAspect() == aspect)
				{
					attacker.addPotionEffect(playerEffect.get());
					target.addPotionEffect(enemyEffect.get());
				}
			}
		}
		
		if(itemStack.getItem() == MSItems.CLOWN_CLUB)
			attacker.world.playSound(null, attacker.getPosX(), attacker.getPosY(), attacker.getPosZ(), MSSoundEvents.ITEM_HORN_USE, SoundCategory.AMBIENT, 1.5F, 1.0F);
		
		return super.hitEntity(itemStack, target, attacker);
	}
}