package com.mraof.minestuck.entity.carapacian;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.attributes.AttributeModifierMap;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.entity.ai.goal.NearestAttackableTargetGoal;
import net.minecraft.entity.monster.IMob;
import net.minecraft.util.DamageSource;
import net.minecraft.world.World;
import software.bernie.geckolib3.core.manager.AnimationData;

public class RookEntity extends CarapacianEntity implements IMob
{
	protected RookEntity(EntityType<? extends RookEntity> type, EnumEntityKingdom kingdom, World world)
	{
		super(type, kingdom, world);
		this.xpReward = 10;
	}
	
	public static RookEntity createProspitian(EntityType<? extends RookEntity> type, World world)
	{
		return new RookEntity(type, EnumEntityKingdom.PROSPITIAN, world);
	}
	
	public static RookEntity createDersite(EntityType<? extends RookEntity> type, World world)
	{
		return new RookEntity(type, EnumEntityKingdom.DERSITE, world);
	}
	
	public static AttributeModifierMap.MutableAttribute rookAttributes()
	{
		return CarapacianEntity.carapacianAttributes().add(Attributes.MAX_HEALTH, 50)
				.add(Attributes.MOVEMENT_SPEED, 0.3);
	}
	
	@Override
	protected void registerGoals()
	{
		super.registerGoals();
		this.goalSelector.addGoal(4, new MeleeAttackGoal(this, 4/3F, false));
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
		return entity.hurt(DamageSource.mobAttack(this), damage);
	}

	@Override
	public void registerControllers(AnimationData data) {
		// TODO blockbench model
	}

//	@Override
//	protected void attackEntity(Entity entity, float par2)
//	{
//		if(this.attackTime <= 0	&& par2 < 2F && entity.boundingBox.maxY > this.boundingBox.minY && entity.boundingBox.minY < this.boundingBox.maxY)
//		{
//			this.attackTime = 20;
//			this.attackEntityAsMob(entity);
//		}
//	}
}