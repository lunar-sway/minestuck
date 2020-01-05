package com.mraof.minestuck.item.weapon;

import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.IItemTier;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;

/**
 * Created by mraof on 2017 January 18 at 6:41 PM.
 */
public class PotionWeaponItem extends WeaponItem
{
    private final EffectInstance effect;
    private final boolean onCritical;
	
	public PotionWeaponItem(IItemTier tier, int attackDamageIn, float attackSpeedIn, float efficiency, EffectInstance effect, MSToolType toolType, Properties builder)
	{
		this(tier, attackDamageIn, attackSpeedIn, efficiency, effect ,true, toolType, builder);
	}
 
	public PotionWeaponItem(IItemTier tier, int attackDamageIn, float attackSpeedIn, float efficiency, EffectInstance effect, boolean potionEffectOnCriticalHit, MSToolType toolType, Properties builder)
	{
		super(tier, attackDamageIn, attackSpeedIn, efficiency, toolType, builder);
		this.effect = effect;
		this.onCritical = potionEffectOnCriticalHit;
	}
    
    public EffectInstance getEffect(ServerPlayerEntity player)
    {
    	EffectInstance setEffect = effect;
        return new EffectInstance(setEffect.getPotion(), setEffect.getDuration(), setEffect.getAmplifier());
    }
    
    public boolean potionOnCrit()
    {
    	return onCritical;
    }
    
    public static EffectInstance getBeaconEffect(int id)
    {
    	EffectInstance beaconEffect;
    	switch(id)
    	{
    	default:
    	case 0: beaconEffect = new EffectInstance(Effects.SPEED, 220, 0); break;
    	case 1: beaconEffect = new EffectInstance(Effects.HASTE, 220, 0); break;
    	case 2: beaconEffect = new EffectInstance(Effects.JUMP_BOOST, 220, 0); break;
    	case 3: beaconEffect = new EffectInstance(Effects.STRENGTH, 220, 0); break;
    	
    	}
    	return beaconEffect;
    }
}
