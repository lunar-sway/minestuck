package com.mraof.minestuck.entity.carapacian;

import com.mraof.minestuck.entity.item.GristEntity;
import com.mraof.minestuck.entity.item.VitalityGelEntity;
import com.mraof.minestuck.item.crafting.alchemy.GristAmount;
import com.mraof.minestuck.item.crafting.alchemy.GristSet;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class DersiteFarmerEntity extends PassiveCarapacianEntity
{
    public DersiteFarmerEntity(EntityType<? extends DersiteFarmerEntity> type, World world)
    {
        super(type, world);
    }

    @Override
    public float getWanderSpeed()
    {
        return .2F;
    }

    @Override
    protected float getMaximumHealth()
    {
        return 40;
    }

    @Override
    public boolean canDespawn(double distanceToClosestPlayer)
    {
        return false;
    }

    //They should drop cans of food, Mayor-esque
    /*@Override
    protected void onDeathUpdate()
    {
        super.onDeathUpdate();
        if(this.deathTime == 20 && !this.world.isRemote)
        {
            this.world.addEntity(new ItemEntity(world, randX(), this.posY, randZ(), candyItem));
        }
    }*/
}
