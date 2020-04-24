package com.mraof.minestuck.item.weapon;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.IItemTier;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Effect;
import net.minecraft.potion.EffectInstance;

import java.util.function.Supplier;

public class GivePotionEffectWeaponItem extends WeaponItem
{
    private final boolean giveToPlayers;
    private final Supplier<Effect> effect;
    private final int duration;
    private final int effectTier;
    
    public GivePotionEffectWeaponItem(IItemTier tier, int attackDamageIn, float attackSpeedIn, float efficiency, boolean giveToPlayers, Supplier<Effect> effect, int duration, int effectTier, MSToolType toolType, Properties builder)
    {
        super(tier, attackDamageIn, attackSpeedIn, efficiency, toolType, builder);
        this.giveToPlayers = giveToPlayers;
        this.effect = effect;
        this.duration = duration;
        this.effectTier = effectTier;
    }
    
    @Override
    public boolean hitEntity(ItemStack stack, LivingEntity target, LivingEntity player)
    {
        if((!(target instanceof PlayerEntity) || giveToPlayers) && !target.world.isRemote)
        {
            target.addPotionEffect(new EffectInstance(effect.get(), duration, effectTier));
        }
        return super.hitEntity(stack, target, player);
    }
}
