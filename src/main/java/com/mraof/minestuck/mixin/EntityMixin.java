package com.mraof.minestuck.mixin;

import com.mraof.minestuck.mixinIntefaces.ILivingEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.monster.*;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(LivingEntity.class)
abstract class EntityMixinGlobal implements ILivingEntity
{
    public int getMinestuckXpValue(){
        return 0;
    }
}

@Mixin({SkeletonEntity.class, ZombieEntity.class})
abstract class EntityMixinTierOne implements ILivingEntity
{
    public int getMinestuckXpValue(){
        return 1;
    }
}

@Mixin({CreeperEntity.class, SpiderEntity.class, SilverfishEntity.class})
abstract class EntityMixinTierTwo implements ILivingEntity
{
    public int getMinestuckXpValue(){
        return 2;
    }
}

@Mixin({EndermanEntity.class, BlazeEntity.class, WitchEntity.class, GuardianEntity.class})
abstract class EntityMixinTierThree implements ILivingEntity
{
    public int getMinestuckXpValue(){
        return 3;
    }
}

@Mixin(SlimeEntity.class)
abstract class EntityMixinSlime implements ILivingEntity
{
    @Shadow public abstract int getSize();

    public int getMinestuckXpValue(){
        return Math.min(this.getSize() - 1, 9);
    }
}