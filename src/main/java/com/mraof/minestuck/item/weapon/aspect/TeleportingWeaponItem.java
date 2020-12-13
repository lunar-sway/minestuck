package com.mraof.minestuck.item.weapon.aspect;

import com.mraof.minestuck.item.weapon.MSToolType;
import com.mraof.minestuck.item.weapon.WeaponItem;
import com.mraof.minestuck.player.EnumAspect;
import com.mraof.minestuck.player.Title;
import com.mraof.minestuck.world.storage.PlayerSavedData;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.IItemTier;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Effects;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;

public class TeleportingWeaponItem extends WeaponItem
{
    public TeleportingWeaponItem(IItemTier tier, int attackDamageIn, float attackSpeedIn, float efficiency, MSToolType toolType, Properties builder)
    {
        super(tier, attackDamageIn, attackSpeedIn, efficiency, toolType, builder);
    }
    
    @Override
    public boolean hitEntity(ItemStack itemStack, LivingEntity target, LivingEntity attacker)
    {
        Title title = PlayerSavedData.getData((ServerPlayerEntity) attacker).getTitle();
        float attackCooldown = ((ServerPlayerEntity) attacker).getCooldownPeriod();
        boolean critical = attackCooldown >= 1.0F && attacker.fallDistance > 0.0F && !attacker.onGround && !attacker.isOnLadder() && !attacker.isInWater() && !attacker.isPotionActive(Effects.BLINDNESS) && !attacker.isPassenger() && !attacker.isBeingRidden();
        
        if(attacker instanceof ServerPlayerEntity && critical)
        {
            if(title != null)
            {
                if(title.getHeroAspect() == EnumAspect.SPACE)
                {
                    double d0 = attacker.posX;
                    double d1 = attacker.posY;
                    double d2 = attacker.posZ;
                    World worldIn = attacker.world;
    
                    for(int i = 0; i < 16; ++i)
                    {
                        double d3 = attacker.posX + (attacker.getRNG().nextDouble() - 0.5D) * 16.0D;
                        double d4 = MathHelper.clamp(attacker.posY + (double) (attacker.getRNG().nextInt(16) - 8), 0.0D, (double) (worldIn.getActualHeight() - 1));
                        double d5 = attacker.posZ + (attacker.getRNG().nextDouble() - 0.5D) * 16.0D;
                        if(attacker.isPassenger())
                        {
                            attacker.stopRiding();
                        }
    
                        if(attacker.attemptTeleport(d3, d4, d5, true))
                        {
                            worldIn.playSound((PlayerEntity) null, d0, d1, d2, SoundEvents.ITEM_CHORUS_FRUIT_TELEPORT, SoundCategory.PLAYERS, 1.0F, 1.0F);
                            attacker.playSound(SoundEvents.ITEM_CHORUS_FRUIT_TELEPORT, 1.0F, 1.0F);
                            break;
                        }
                    }
                }
            }
        }
        return super.hitEntity(itemStack, target, attacker);
    }
}
