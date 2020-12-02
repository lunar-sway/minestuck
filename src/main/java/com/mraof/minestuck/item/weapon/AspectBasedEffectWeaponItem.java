package com.mraof.minestuck.item.weapon;

import com.mraof.minestuck.item.MSItems;
import com.mraof.minestuck.player.EnumAspect;
import com.mraof.minestuck.player.Title;
import com.mraof.minestuck.util.MSSoundEvents;
import com.mraof.minestuck.world.storage.PlayerSavedData;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.IItemTier;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Effect;
import net.minecraft.potion.EffectInstance;
import net.minecraft.util.SoundCategory;

import javax.annotation.Nullable;
import java.util.function.Supplier;

public class AspectBasedEffectWeaponItem extends WeaponItem
{
    private final EnumAspect aspect;
    private final Supplier<Effect> effect;
    private final int duration;
    private final int effectTier;
    
    public AspectBasedEffectWeaponItem(IItemTier tier, int attackDamageIn, float attackSpeedIn, float efficiency, EnumAspect aspect, Supplier<Effect> effect, int duration, int effectTier, @Nullable MSToolType toolType, Properties builder)
    {
        super(tier, attackDamageIn, attackSpeedIn, efficiency, toolType, builder);
        this.aspect = aspect;
        this.effect = effect;
        this.duration = duration;
        this.effectTier = effectTier;
    }
    
    @Override
    public boolean hitEntity(ItemStack itemStack, LivingEntity target, LivingEntity attacker)
    {
        Title title = PlayerSavedData.getData((ServerPlayerEntity) attacker).getTitle();
        
        if(attacker instanceof ServerPlayerEntity && attacker.getRNG().nextFloat() < .1)
        {
            if(title != null)
            {
                if (title.getHeroAspect() == aspect)
                {
                    ((ServerPlayerEntity) attacker).addPotionEffect(new EffectInstance(effect.get(), duration, effectTier));
                }
            }
        }
    
        if (itemStack.getItem() == MSItems.CLOWN_CLUB) {
            attacker.world.playSound(null, attacker.posX, attacker.posY, attacker.posZ, MSSoundEvents.ITEM_HORN_USE, SoundCategory.AMBIENT, 1.5F, 1.0F);
        }
        
        return super.hitEntity(itemStack, target, attacker);
    }
}
