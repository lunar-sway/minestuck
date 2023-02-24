package com.mraof.minestuck.entity.underling;

import com.mraof.minestuck.entity.IEntityMultiPart;
import com.mraof.minestuck.entity.ai.CustomMeleeAttackGoal;
import com.mraof.minestuck.alchemy.GristHelper;
import com.mraof.minestuck.alchemy.GristSet;
import com.mraof.minestuck.alchemy.GristType;
import com.mraof.minestuck.player.Echeladder;
import com.mraof.minestuck.util.MSSoundEvents;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.level.Level;

public class BasiliskEntity extends UnderlingEntity implements IEntityMultiPart
{
	private UnderlingPartEntity tail;
	
	public BasiliskEntity(EntityType<? extends BasiliskEntity> type, Level level)
	{
		super(type, level, 5);
		tail = new UnderlingPartEntity(this, 0, 3F, 2F);
		//world.addEntity(tail); TODO Not safe to add entities to world on creation. A different solution is needed
	}
	
	public static AttributeSupplier.Builder basiliskAttributes()
	{
		return UnderlingEntity.underlingAttributes().add(Attributes.MAX_HEALTH, 85)
				.add(Attributes.KNOCKBACK_RESISTANCE, 0.6).add(Attributes.MOVEMENT_SPEED, 0.25)
				.add(Attributes.ATTACK_DAMAGE, 6);
	}
	
	@Override
	protected void registerGoals()
	{
		super.registerGoals();
		this.goalSelector.addGoal(3, new CustomMeleeAttackGoal(this, 1.0F, false, 40, 1.2F));
	}
	
	@Override
	protected SoundEvent getAmbientSound()
	{
		return MSSoundEvents.ENTITY_BASILISK_AMBIENT.get();
	}
	
	@Override
	protected SoundEvent getHurtSound(DamageSource damageSourceIn)
	{
		return MSSoundEvents.ENTITY_BASILISK_HURT.get();
	}
	
	@Override
	protected SoundEvent getDeathSound()
	{
		return MSSoundEvents.ENTITY_BASILISK_DEATH.get();
	}
	
	@Override
	public GristSet getGristSpoils()
	{
		return GristHelper.generateUnderlingGristDrops(this, damageMap, 6);
	}
	
	@Override
	protected int getVitalityGel()
	{
		return random.nextInt(3) + 4;
	}
	
	@Override
	protected void onGristTypeUpdated(GristType type)
	{
		super.onGristTypeUpdated(type);
		applyGristModifier(Attributes.MAX_HEALTH, 20 * type.getPower(), AttributeModifier.Operation.ADDITION);
		applyGristModifier(Attributes.ATTACK_DAMAGE, 2.7 * type.getPower(), AttributeModifier.Operation.ADDITION);
		this.xpReward = (int) (6 * type.getPower() + 4);
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
		return this.hurt(source, damage);
	}
	
	@Override
	protected void doPush(Entity par1Entity)
	{
		if(par1Entity != this.tail)
			super.doPush(par1Entity);
	}
	
	@Override
	public void absMoveTo(double par1, double par3, double par5, float par7, float par8)
	{
		super.absMoveTo(par1, par3, par5, par7, par8);
		this.updatePartPositions();
	}
	
	@Override
	public void updatePartPositions()
	{
		if(tail == null)
			return;
		float f1 = this.yRotO + (this.getYRot() - this.yRotO);
		double tailPosX = (this.getX() + Math.sin(f1 / 180.0 * Math.PI) * tail.getBbWidth());
		double tailPosZ = (this.getZ() + -Math.cos(f1 / 180.0 * Math.PI) * tail.getBbWidth());
		
		tail.absMoveTo(tailPosX, this.getY(), tailPosZ, this.getYRot(), this.getXRot());
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
	public void die(DamageSource cause)
	{
		super.die(cause);
		Entity entity = cause.getEntity();
		if(this.dead && !this.level.isClientSide)
		{
			computePlayerProgress((int) (30 + 2.4 * getGristType().getPower())); //most basilisks stop giving xp at rung 32
			firstKillBonus(entity, (byte) (Echeladder.UNDERLING_BONUS_OFFSET + 2));
		}
	}
}