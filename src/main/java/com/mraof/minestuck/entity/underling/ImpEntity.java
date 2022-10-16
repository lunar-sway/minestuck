package com.mraof.minestuck.entity.underling;

import com.mraof.minestuck.alchemy.GristHelper;
import com.mraof.minestuck.alchemy.GristSet;
import com.mraof.minestuck.alchemy.GristType;
import com.mraof.minestuck.player.Echeladder;
import com.mraof.minestuck.util.MSSoundEvents;
import com.mraof.minestuck.player.PlayerSavedData;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.world.level.Level;

public class ImpEntity extends UnderlingEntity
{
	public ImpEntity(EntityType<? extends ImpEntity> type, Level level)
	{
		super(type, level, 1);
	}
	
	public static AttributeSupplier.Builder impAttributes()
	{
		return UnderlingEntity.underlingAttributes().add(Attributes.MAX_HEALTH, 6)
				.add(Attributes.MOVEMENT_SPEED, 0.28).add(Attributes.ATTACK_DAMAGE, 1);
	}
	
	@Override
	public GristSet getGristSpoils()
	{
		return GristHelper.generateUnderlingGristDrops(this, damageMap, 1);
	}
	
	@Override
	protected void registerGoals()
	{
		super.registerGoals();
		this.goalSelector.addGoal(3, new MeleeAttackGoal(this, 1.0F, false));
	}
	
	protected SoundEvent getAmbientSound()
	{
		return MSSoundEvents.ENTITY_IMP_AMBIENT.get();
	}
	
	protected SoundEvent getHurtSound(DamageSource damageSourceIn)
	{
		return MSSoundEvents.ENTITY_IMP_HURT.get();
	}
	
	protected SoundEvent getDeathSound()
	{
		return MSSoundEvents.ENTITY_IMP_DEATH.get();
	}
	
	@Override
	protected int getVitalityGel()
	{
		return random.nextInt(3) + 1;
	}
	
	@Override
	protected void onGristTypeUpdated(GristType type)
	{
		super.onGristTypeUpdated(type);
		applyGristModifier(Attributes.MAX_HEALTH, 8 * type.getPower(), AttributeModifier.Operation.ADDITION);
		applyGristModifier(Attributes.ATTACK_DAMAGE, Math.ceil(type.getPower()), AttributeModifier.Operation.ADDITION);
		this.xpReward = (int) (3 * type.getPower() + 1);
	}
	
	@Override
	public void die(DamageSource cause)
	{
		super.die(cause);
		Entity entity = cause.getEntity();
		if(this.dead && !this.level.isClientSide)
		{
			computePlayerProgress((int) (5 + 2 * getGristType().getPower())); //most imps stop giving xp at rung 8
			firstKillBonus(entity, Echeladder.UNDERLING_BONUS_OFFSET);
		}
	}
	
	@Override
	protected boolean isAppropriateTarget(LivingEntity entity)
	{
		if(entity instanceof ServerPlayer)
		{
			//Rung was chosen fairly arbitrary. Feel free to change it if you think a different rung is better
			return PlayerSavedData.getData((ServerPlayer) entity).getEcheladder().getRung() < 19;
		}
		return super.isAppropriateTarget(entity);
	}
}