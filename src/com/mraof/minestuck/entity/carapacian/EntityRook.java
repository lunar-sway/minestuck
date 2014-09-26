package com.mraof.minestuck.entity.carapacian;

import net.minecraft.entity.Entity;
import net.minecraft.entity.ai.EntityAIAttackOnCollide;
import net.minecraft.entity.monster.IMob;
import net.minecraft.util.DamageSource;
import net.minecraft.world.World;

public abstract class EntityRook extends EntityCarapacian implements IMob
{
	private EntityAIAttackOnCollide entityAIAttackOnCollide = new EntityAIAttackOnCollide(this, .5F, false);

	public EntityRook(World world)
	{
		super(world);
		setSize(3.5F, 3.5F);
		this.experienceValue = 10;
	}

	@Override
	public float getMaximumHealth()
	{
		return 50;
	}

	@Override
	public float getWanderSpeed()
	{
		return .3F;
	}

	public float getAttackStrength(Entity entity)
	{
		return 5;
	}

	@Override
	public boolean attackEntityAsMob(Entity entity)
	{
		float damage = this.getAttackStrength(entity);
		return entity.attackEntityFrom(DamageSource.causeMobDamage(this), damage);
	}

	@Override
	protected void attackEntity(Entity entity, float par2)
	{
		if(this.attackTime <= 0	&& par2 < 2F && entity.boundingBox.maxY > this.boundingBox.minY && entity.boundingBox.minY < this.boundingBox.maxY)
		{
			this.attackTime = 20;
			this.attackEntityAsMob(entity);
		}
	}

	@Override
	public void setCombatTask()
	{
		if(this.entityAIAttackOnCollide == null)
			entityAIAttackOnCollide = new EntityAIAttackOnCollide(this, .4F, false);
		this.tasks.removeTask(this.entityAIAttackOnCollide);
		this.tasks.addTask(4, this.entityAIAttackOnCollide);
	}

}
