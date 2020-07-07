package com.mraof.minestuck.item.weapon;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.IItemTier;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;

/**
 * Created by mraof on 2017 January 18 at 6:33 PM.
 */
public class RandomWeaponItem extends WeaponItem
{
	private final int maxRand;
	
	public RandomWeaponItem(IItemTier tier, int attackDamageIn, float attackSpeedIn, float efficiency, MSToolType toolType, int maxRand, Properties builder)
	{
		super(tier, attackDamageIn, attackSpeedIn, efficiency, toolType, builder);
		this.maxRand = maxRand;
	}
	
	public RandomWeaponItem(IItemTier tier, int attackDamageIn, float attackSpeedIn, float efficiency, MSToolType toolType, Properties builder)
	{
		this(tier, attackDamageIn, attackSpeedIn, efficiency, toolType, 7, builder);
	}
	
	@Override
	public boolean hitEntity(ItemStack itemStack, LivingEntity target, LivingEntity player)
	{
		DamageSource source;
		if(player instanceof PlayerEntity)
		{
			source = DamageSource.causePlayerDamage((PlayerEntity) player);
		}
		else
		{
			source = DamageSource.causeMobDamage(player);
		}
		
		float rng = (float) (player.getRNG().nextInt(maxRand)+1) * (player.getRNG().nextInt(maxRand)+1);
		target.attackEntityFrom(source, rng );
		
		
		return super.hitEntity(itemStack, target, player);
	}
}
