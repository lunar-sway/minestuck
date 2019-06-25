package com.mraof.minestuck.item.weapon;

import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.MobEffects;
import net.minecraft.item.IItemTier;
import net.minecraft.potion.PotionEffect;

/**
 * Created by mraof on 2017 January 18 at 6:41 PM.
 */
public class ItemPotionWeapon extends ItemWeapon
{
    private final PotionEffect effect;
    private final boolean onCritical;
	
	public ItemPotionWeapon(IItemTier tier, int attackDamageIn, float attackSpeedIn, float efficiency, PotionEffect effect, Properties builder)
	{
		this(tier, attackDamageIn, attackSpeedIn, efficiency, effect ,true, builder);
	}
 
	public ItemPotionWeapon(IItemTier tier, int attackDamageIn, float attackSpeedIn, float efficiency, PotionEffect effect, boolean potionEffectOnCriticalHit, Properties builder)
	{
		super(tier, attackDamageIn, attackSpeedIn, efficiency, builder);
		this.effect = effect;
		this.onCritical = potionEffectOnCriticalHit;
	}
    
    public PotionEffect getEffect(EntityPlayerMP player)
    {
    	PotionEffect setEffect = effect;
        return new PotionEffect(setEffect.getPotion(), setEffect.getDuration(), setEffect.getAmplifier());
    }
    
    public boolean potionOnCrit()
    {
    	return onCritical;
    }
    
    public static PotionEffect getBeaconEffect(int id)
    {
    	PotionEffect beaconEffect;
    	switch(id)
    	{
    	default:
    	case 0: beaconEffect = new PotionEffect(MobEffects.SPEED, 220, 0); break;
    	case 1: beaconEffect = new PotionEffect(MobEffects.HASTE, 220, 0); break;
    	case 2: beaconEffect = new PotionEffect(MobEffects.JUMP_BOOST, 220, 0); break;
    	case 3: beaconEffect = new PotionEffect(MobEffects.STRENGTH, 220, 0); break;
    	
    	}
    	return beaconEffect;
    }
}
