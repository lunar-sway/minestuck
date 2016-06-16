package com.mraof.minestuck.entity.underling;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.DamageSource;
import net.minecraft.world.World;

import com.mraof.minestuck.entity.IEntityMultiPart;
import com.mraof.minestuck.entity.ai.EntityAIAttackOnCollideWithRate;
import com.mraof.minestuck.util.Echeladder;
import com.mraof.minestuck.util.GristHelper;
import com.mraof.minestuck.util.GristSet;
import com.mraof.minestuck.util.GristType;
import com.mraof.minestuck.util.MinestuckPlayerData;

public class EntityBasilisk extends EntityUnderling implements IEntityMultiPart 
{
	private EntityAIAttackOnCollideWithRate entityAIAttackOnCollideWithRate;
	EntityUnderlingPart tail;
	
	public EntityBasilisk(World world) 
	{
		super(world, "Basilisk");
		this.setSize(3F, 2F);
		tail = new EntityUnderlingPart(this, 0, 3F, 2F);
		world.spawnEntityInWorld(tail);
	}

	@Override
	public GristSet getGristSpoils() 
	{
		return GristHelper.getRandomDrop(type, 6);
	}

	@Override
	protected void setCombatTask() 
	{
		if(entityAIAttackOnCollideWithRate == null)
			entityAIAttackOnCollideWithRate = new EntityAIAttackOnCollideWithRate(this, .3F, 40, false);
		entityAIAttackOnCollideWithRate.setDistanceMultiplier(1.2F);
		this.tasks.removeTask(this.entityAIAttackOnCollideWithRate);
		this.tasks.addTask(4, entityAIAttackOnCollideWithRate);
	}
	
	@Override
	protected float getMaximumHealth() 
	{
		return type != null ? 20 * type.getPower() + 85 : 1;
	}

	@Override
	protected double getWanderSpeed()
	{
		return 0.75;
	}
	
	@Override
	protected float getKnockbackResistance()
	{
		return 0.6F;
	}
	
	@Override
	protected double getAttackDamage()
	{
		return this.type.getPower()*2.7 + 6;
	}
	
	@Override
	protected int getVitalityGel()
	{
		return rand.nextInt(3) + 4;
	}
	
	@Override
	protected void applyGristType(GristType type, boolean fullHeal)
	{
		super.applyGristType(type, fullHeal);
		this.experienceValue = (int) (6 * type.getPower() + 4);
	}
	
	@Override
	public World getWorld() 
	{
		return this.worldObj;
	}
	
	@Override
	public void onEntityUpdate() 
	{
		super.onEntityUpdate();
		this.updatePartPositions();
	}

	@Override
	public boolean attackEntityFromPart(Entity entityPart, DamageSource source, float damage) 
	{
		boolean flag = this.attackEntityFrom(source, damage);
		
		return flag;
	}
	@Override
	public Entity[] getParts() 
	{
		return new Entity[] {tail};
	}
	@Override
	protected void collideWithEntity(Entity par1Entity) 
	{
		if(par1Entity != this.tail)
			super.collideWithEntity(par1Entity);
	}
	@Override
	public void setPositionAndRotation(double par1, double par3, double par5, float par7, float par8) {
		super.setPositionAndRotation(par1, par3, par5, par7, par8);
		this.updatePartPositions();
	}
	
	@Override
	public void updatePartPositions() 
	{
		if(tail == null)
			return;
		float f1 = this.prevRotationYaw + (this.rotationYaw - this.prevRotationYaw);
		double tailPosX = (this.posX +  Math.sin(f1 / 180.0 * Math.PI) * tail.width);
		double tailPosZ = (this.posZ + -Math.cos(f1 / 180.0 * Math.PI) * tail.width);

		tail.setPositionAndRotation(tailPosX, this.posY, tailPosZ, this.rotationYaw, this.rotationPitch);
	}

	@Override
	public void addPart(Entity entityPart, int id) 
	{
		this.tail = (EntityUnderlingPart) entityPart;
		this.tail.setSize(3F, 2F);
	}

	@Override
	public void onPartDeath(Entity entityPart, int id) 
	{

	}
	
	@Override
	public void onDeath(DamageSource cause)
	{
		super.onDeath(cause);
		Entity entity = cause.getEntity();
		if(this.dead && !this.worldObj.isRemote && type != null)
		{
			computePlayerProgress((int) (100*type.getPower() + 160));
			if(entity != null && entity instanceof EntityPlayerMP)
			{
				Echeladder ladder = MinestuckPlayerData.getData((EntityPlayerMP) entity).echeladder;
				ladder.checkBonus((byte) (Echeladder.UNDERLING_BONUS_OFFSET + 2));
			}
		}
	}
}