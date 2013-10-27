package com.mraof.minestuck.entity.consort;

import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.util.MathHelper;
import net.minecraft.world.EnumSkyBlock;
import net.minecraft.world.World;

import com.mraof.minestuck.entity.EntityMinestuck;

public abstract class EntityConsort extends EntityMinestuck
{

	public EntityConsort(World world) 
	{
		super(world);
//		moveSpeed = 2.5F;
		
        this.getEntityAttribute(SharedMonsterAttributes.maxHealth).setAttribute((double)(this.getMaximumHealth()));
		this.setHealth(this.getMaximumHealth());
		
        setSize(0.6F, 1.5F);
        this.experienceValue = 1;
	}

	@Override
	protected float getMaximumHealth() 
	{
		return 10;
	}
	

	@Override
	   public boolean getCanSpawnHere()
 {
     return this.isValidLightLevel() && super.getCanSpawnHere();
 }
	
 protected boolean isValidLightLevel()
 {
     int i = MathHelper.floor_double(this.posX);
     int j = MathHelper.floor_double(this.boundingBox.minY);
     int k = MathHelper.floor_double(this.posZ);

     if (this.worldObj.getSavedLightValue(EnumSkyBlock.Sky, i, j, k) < this.rand.nextInt(8))
     {
         return false;
     }
     else
     {
         int l = this.worldObj.getBlockLightValue(i, j, k);

         if (this.worldObj.isThundering())
         {
             int i1 = this.worldObj.skylightSubtracted;
             this.worldObj.skylightSubtracted = 10;
             l = this.worldObj.getBlockLightValue(i, j, k);
             this.worldObj.skylightSubtracted = i1;
         }

         return l >= this.rand.nextInt(8);
     }
 }
}