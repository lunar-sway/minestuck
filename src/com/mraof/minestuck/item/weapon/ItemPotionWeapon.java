package com.mraof.minestuck.item.weapon;

import java.util.Random;

import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.MobEffects;
import net.minecraft.item.Item;
import net.minecraft.potion.PotionEffect;

/**
 * Created by mraof on 2017 January 18 at 6:41 PM.
 */
public class ItemPotionWeapon extends ItemWeapon
{
    private final PotionEffect effect;
    private final boolean onCritical;
    private static boolean isRandom = false;;
    public ItemPotionWeapon(int maxUses, double damageVsEntity, double weaponSpeed, int enchantability, String name, PotionEffect effect, boolean potionEffectOnCriticalHit)
    {
        super(maxUses, damageVsEntity, weaponSpeed, enchantability, name);
        this.effect = effect;
        this.onCritical = potionEffectOnCriticalHit;
    }
    
    public ItemPotionWeapon(int maxUses, double damageVsEntity, double weaponSpeed, int enchantability, String name, PotionEffect effect)
    {
        this(maxUses, damageVsEntity, weaponSpeed, enchantability, name, effect, true);
    }

    public ItemPotionWeapon(Item.ToolMaterial material, int maxUses, double damageVsEntity, double weaponSpeed, int enchantability, String name, PotionEffect effect)
    {
        super(material, maxUses, damageVsEntity, weaponSpeed, enchantability, name);
        this.onCritical = false;
        this.effect = effect;
    }

    public static PotionEffect randomPotionEffect()
    {
    	isRandom = true;
    	return new PotionEffect(MobEffects.SPEED, 220, 0);
    }
    
    public PotionEffect getEffect(EntityPlayerMP player)
    {
    	PotionEffect setEffect = effect;
    	if(isRandom)
    	{
    		setEffect = getBeaconEffect(player.getRNG().nextInt(3));
    	}
        return new PotionEffect(setEffect.getPotion(), setEffect.getDuration(), setEffect.getAmplifier());
    }
    
    public boolean potionOnCrit()
    {
    	return onCritical;
    }
    
    public static PotionEffect getBeaconEffect(int id)
    {
    	PotionEffect beaconEffect;
    	int rng = (int) Math.random();
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
