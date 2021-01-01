package com.mraof.minestuck.entity.underling;

import com.mraof.minestuck.item.crafting.alchemy.GristHelper;
import com.mraof.minestuck.item.crafting.alchemy.GristSet;
import com.mraof.minestuck.item.crafting.alchemy.GristType;
import com.mraof.minestuck.player.Echeladder;
import com.mraof.minestuck.util.MSSoundEvents;
import com.mraof.minestuck.world.storage.PlayerSavedData;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.DamageSource;
import net.minecraft.util.SoundEvent;
import net.minecraft.world.World;

public class ImpEntity extends UnderlingEntity
{
	public ImpEntity(EntityType<? extends ImpEntity> type, World world)
	{
		super(type, world, 1);
	}
	
	@Override
	protected void registerAttributes()
	{
		super.registerAttributes();
		getAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(6.0D);
		getAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.28D);
		getAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).setBaseValue(1.0D);
	}
	
	@Override
	public GristSet getGristSpoils()
	{
		return GristHelper.generateUnderlingGristDrops(this, damageMap, 2);
	}
	
	@Override
	protected void registerGoals()
	{
		super.registerGoals();
		this.goalSelector.addGoal(3, new MeleeAttackGoal(this, 1.0F, false));
	}
	
	protected SoundEvent getAmbientSound()
	{
		return MSSoundEvents.ENTITY_IMP_AMBIENT;
	}
	
	protected SoundEvent getHurtSound(DamageSource damageSourceIn)
	{
		return MSSoundEvents.ENTITY_IMP_HURT;
	}
	
	protected SoundEvent getDeathSound()
	{
		return MSSoundEvents.ENTITY_IMP_DEATH;
	}
	
	@Override
	protected int getVitalityGel()
	{
		return rand.nextInt(3)+1;
	}
	
	@Override
	protected void onGristTypeUpdated(GristType type)
	{
		super.onGristTypeUpdated(type);
		applyGristModifier(SharedMonsterAttributes.MAX_HEALTH, 8 * type.getPower(), AttributeModifier.Operation.ADDITION);
		applyGristModifier(SharedMonsterAttributes.ATTACK_DAMAGE, Math.ceil(type.getPower()), AttributeModifier.Operation.ADDITION);
		this.experienceValue = (int) (3 * type.getPower() + 1);
	}
	
	@Override
	public void onDeath(DamageSource cause)
	{
		super.onDeath(cause);
		Entity entity = cause.getTrueSource();
		if(this.dead && !this.world.isRemote)
		{
			computePlayerProgress((int) (2 + 3* getGristType().getPower()));
			if(entity instanceof ServerPlayerEntity)
			{
				Echeladder ladder = PlayerSavedData.getData((ServerPlayerEntity) entity).getEcheladder();
				ladder.checkBonus(Echeladder.UNDERLING_BONUS_OFFSET);
			}
		}
	}
	
	@Override
	protected boolean isAppropriateTarget(LivingEntity entity)
	{
		if(entity instanceof ServerPlayerEntity)
		{
			//Rung was chosen fairly arbitrary. Feel free to change it if you think a different rung is better
			return PlayerSavedData.getData((ServerPlayerEntity) entity).getEcheladder().getRung() < 19;
		}
		return super.isAppropriateTarget(entity);
	}
}