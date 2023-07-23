package com.mraof.minestuck.entity.carapacian;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.monster.Enemy;
import net.minecraft.world.level.Level;

public class RookEntity extends CarapacianEntity implements Enemy
{
	protected RookEntity(EntityType<? extends RookEntity> type, EnumEntityKingdom kingdom, Level level)
	{
		super(type, kingdom, level);
		this.xpReward = 10;
	}
	
	public static RookEntity createProspitian(EntityType<? extends RookEntity> type, Level level)
	{
		return new RookEntity(type, EnumEntityKingdom.PROSPITIAN, level);
	}
	
	public static RookEntity createDersite(EntityType<? extends RookEntity> type, Level level)
	{
		return new RookEntity(type, EnumEntityKingdom.DERSITE, level);
	}
	
	public static AttributeSupplier.Builder rookAttributes()
	{
		return CarapacianEntity.carapacianAttributes().add(Attributes.MAX_HEALTH, 50)
				.add(Attributes.MOVEMENT_SPEED, 0.3);
	}
	
	@Override
	protected void registerGoals()
	{
		super.registerGoals();
		this.goalSelector.addGoal(4, new MeleeAttackGoal(this, 4 / 3F, false));
		this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, LivingEntity.class, 0, true, false, entity -> attackEntitySelector.isEntityApplicable(entity)));
	}
	
	public float getAttackStrength(Entity entity)
	{
		return 5;
	}
	
	@Override
	public boolean doHurtTarget(Entity entity)
	{
		float damage = this.getAttackStrength(entity);
		return entity.hurt(this.damageSources().mobAttack(this), damage);
	}
}