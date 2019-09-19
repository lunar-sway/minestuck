package com.mraof.minestuck.entity.underling;

import com.mraof.minestuck.item.crafting.alchemy.GristHelper;
import com.mraof.minestuck.item.crafting.alchemy.GristSet;
import com.mraof.minestuck.item.crafting.alchemy.GristType;
import com.mraof.minestuck.entity.IEntityMultiPart;
import com.mraof.minestuck.entity.ai.AttackOnCollideWithRateGoal;
import com.mraof.minestuck.util.*;
import com.mraof.minestuck.world.storage.PlayerSavedData;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.DamageSource;
import net.minecraft.util.SoundEvent;
import net.minecraft.world.World;

public class BasiliskEntity extends UnderlingEntity implements IEntityMultiPart
{
	UnderlingPartEntity tail;
	
	public BasiliskEntity(EntityType<? extends BasiliskEntity> type, World world)
	{
		super(type, world);
		tail = new UnderlingPartEntity(this, 0, 3F, 2F);
		world.addEntity(tail);
	}
	
	@Override
	protected String getUnderlingName()
	{
		return "basilisk";
	}
	
	@Override
	protected void registerGoals()
	{
		super.registerGoals();
		AttackOnCollideWithRateGoal aiAttack = new AttackOnCollideWithRateGoal(this, .3F, 40, false);
		aiAttack.setDistanceMultiplier(1.2F);
		this.goalSelector.addGoal(3, aiAttack);
	}
	
	protected SoundEvent getAmbientSound()
	{
		return ModSoundEvents.ENTITY_BASILISK_AMBIENT;
	}
	
	protected SoundEvent getHurtSound(DamageSource damageSourceIn)
	{
		return ModSoundEvents.ENTITY_BASILISK_HURT;
	}
	
	protected SoundEvent getDeathSound()
	{
		return ModSoundEvents.ENTITY_BASILISK_DEATH;
	}
	
	@Override
	public GristSet getGristSpoils()
	{
		return GristHelper.getRandomDrop(type, 6);
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
		return this.world;
	}
	
	@Override
	public void baseTick()
	{
		super.baseTick();
		this.updatePartPositions();
	}

	@Override
	public boolean attackEntityFromPart(Entity entityPart, DamageSource source, float damage) 
	{
		boolean flag = this.attackEntityFrom(source, damage);
		
		return flag;
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
		double tailPosX = (this.posX +  Math.sin(f1 / 180.0 * Math.PI) * tail.getWidth());
		double tailPosZ = (this.posZ + -Math.cos(f1 / 180.0 * Math.PI) * tail.getWidth());

		tail.setPositionAndRotation(tailPosX, this.posY, tailPosZ, this.rotationYaw, this.rotationPitch);
	}

	@Override
	public void addPart(Entity entityPart, int id) 
	{
		this.tail = (UnderlingPartEntity) entityPart;
	}

	@Override
	public void onPartDeath(Entity entityPart, int id) 
	{

	}
	
	@Override
	public void onDeath(DamageSource cause)
	{
		super.onDeath(cause);
		Entity entity = cause.getTrueSource();
		if(this.dead && !this.world.isRemote && type != null)
		{
			computePlayerProgress((int) (100*type.getPower() + 160));
			if(entity instanceof ServerPlayerEntity)
			{
				Echeladder ladder = PlayerSavedData.getData((ServerPlayerEntity) entity).echeladder;
				ladder.checkBonus((byte) (Echeladder.UNDERLING_BONUS_OFFSET + 2));
			}
		}
	}
}