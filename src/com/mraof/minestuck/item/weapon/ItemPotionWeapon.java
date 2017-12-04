package com.mraof.minestuck.item.weapon;

import net.minecraft.item.Item;
import net.minecraft.potion.PotionEffect;

/**
 * Created by mraof on 2017 January 18 at 6:41 PM.
 */
public class ItemPotionWeapon extends ItemWeapon
{
    private final PotionEffect effect;

    public ItemPotionWeapon(int maxUses, double damageVsEntity, double weaponSpeed, int enchantability, String name, PotionEffect effect)
    {
        super(maxUses, damageVsEntity, weaponSpeed, enchantability, name);
        this.effect = effect;
    }

    public ItemPotionWeapon(Item.ToolMaterial material, int maxUses, double damageVsEntity, double weaponSpeed, int enchantability, String name, PotionEffect effect)
    {
        super(material, maxUses, damageVsEntity, weaponSpeed, enchantability, name);
        this.effect = effect;
    }

    public PotionEffect getEffect()
    {
        return new PotionEffect(effect.getPotion(), effect.getDuration(), effect.getAmplifier());
    }
}
