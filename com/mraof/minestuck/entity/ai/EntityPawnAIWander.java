package com.mraof.minestuck.entity.ai;

import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.ai.EntityAIWander;

public class EntityPawnAIWander extends EntityAIWander {

    private EntityCreature entity;
    private double xPosition;
    private double yPosition;
    private double zPosition;
    private float speed;
    
    public EntityPawnAIWander(EntityCreature par1EntityCreature, float par2, boolean isBlack)
    {
    	super(par1EntityCreature, par2);
        this.entity = par1EntityCreature;
        this.speed = par2;
        this.setMutexBits(1);
    }

}
