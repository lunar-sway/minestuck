package com.mraof.minestuck.entity.underling;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.DamageSource;
import net.minecraft.world.World;

import com.mraof.minestuck.entity.IEntityMultiPart;
import com.mraof.minestuck.util.Debug;

public class EntityUnderlingPart extends EntityLiving
{
	public EntityLiving entityUnderlingObj;

	public String name;

	public EntityUnderlingPart(World world)
	{
		super(world);
	}
	public EntityUnderlingPart(EntityLiving par1IEntityMultiPart, String par2Str, float par3, float par4)
	{
		super(((IEntityMultiPart) par1IEntityMultiPart).getWorld());
		this.setSize(par3, par4);
		//Debug.printf("Being created with %s, name is %s, vars %f and %f",par1IEntityMultiPart,par2Str,par3,par4);
		this.entityUnderlingObj = par1IEntityMultiPart;
		this.name = par2Str;
	}

	@Override
	protected void entityInit() 
	{
		super.entityInit();
	}

	/**
	 * (abstract) Protected helper method to read subclass entity data from NBT.
	 */
	@Override
	public void readEntityFromNBT(NBTTagCompound par1NBTTagCompound) 
	{
		super.readEntityFromNBT(par1NBTTagCompound);
	}

	/**
	 * (abstract) Protected helper method to write subclass entity data to NBT.
	 */
	@Override
	public void writeEntityToNBT(NBTTagCompound par1NBTTagCompound) 
	{
		if(this.entityUnderlingObj != null)
			super.writeEntityToNBT(par1NBTTagCompound);
		else
			this.setDead();
	}

	/**
	 * Returns true if other Entities should be prevented from moving through this Entity.
	 */
	@Override
	public boolean canBeCollidedWith()
	{
		return true;
	}

	/**
	 * Called when the entity is attacked.
	 */
	@Override
	public boolean attackEntityFrom(DamageSource par1DamageSource, float par2)
	{
		if(this.entityUnderlingObj == null || par1DamageSource == DamageSource.inWall)
			return false;
		return ((IEntityMultiPart) this.entityUnderlingObj).attackEntityFromPart(this, par1DamageSource, par2);
	}
	@Override
	public void onUpdate() 
	{
		super.onUpdate();
		if(this.entityUnderlingObj == null || (this.entityUnderlingObj).isDead)
		{
			Debug.printf("entity underling is %s, the side is %b", this.entityUnderlingObj, this.worldObj.isRemote);
			this.setDead();
		}
		else
		{
			this.entityUnderlingObj.motionX += this.motionX;
			this.entityUnderlingObj.motionY += this.motionY;
			this.entityUnderlingObj.motionZ += this.motionZ;
			this.motionX = 0;
			this.motionY = 0;
			this.motionZ = 0;
			((IEntityMultiPart)this.entityUnderlingObj).updatePartPositions();
		}
	}

	@Override
	public void setDead() 
	{
		super.setDead();
		Debug.print("Tail is dead");
		if(this.entityUnderlingObj != null && !this.entityUnderlingObj.isDead)
			entityUnderlingObj.setDead();
		
//		Debug.print("BEGIN STACK TRACE");
//		Thread.dumpStack();
//		Debug.print("END STACK TRACE");
	}
	/**
	 * Returns true if Entity argument is equal to this Entity
	 */
	@Override
	public boolean isEntityEqual(Entity par1Entity)
	{
		return this == par1Entity || this.entityUnderlingObj == par1Entity;
	}
	@Override
	public boolean handleWaterMovement() 
	{
		return this.inWater;
		//		return super.handleWaterMovement();
	}
	
	@Override
	 protected boolean canDespawn()
	 {
	     return false;
	 }
	   
	//	@Override
	//	protected boolean pushOutOfBlocks(double par1, double par3, double par5) {
	//		boolean flag = super.pushOutOfBlocks(par1, par3, par5);
	//		if(flag)
	//		{
	//			((EntityLiving)(this.entityUnderlingObj)).motionX += this.motionX;
	//			((EntityLiving)(this.entityUnderlingObj)).motionY += this.motionY;
	//			((EntityLiving)(this.entityUnderlingObj)).motionZ += this.motionZ;
	//		}
	//		return flag;
	//	}
	//	@Override
	//	public ItemStack getHeldItem() {
	//		return null;
	//	}
	//	@Override
	//	public ItemStack getCurrentItemOrArmor(int i) {
	//		return null;
	//	}
	//	@Override
	//	public void setCurrentItemOrArmor(int i, ItemStack itemstack) {
	//		
	//	}
	//	@Override
	//	public ItemStack[] getLastActiveItems() {
	//		return null;
	//	}
}
