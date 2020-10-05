package com.mraof.minestuck.entity.underling;

import com.mraof.minestuck.item.crafting.alchemy.GristHelper;
import com.mraof.minestuck.item.crafting.alchemy.GristSet;
import com.mraof.minestuck.item.crafting.alchemy.GristType;
import com.mraof.minestuck.player.Echeladder;
import com.mraof.minestuck.util.MSSoundEvents;
import com.mraof.minestuck.world.storage.PlayerSavedData;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.AttributeModifierMap;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.DamageSource;
import net.minecraft.util.SoundEvent;
import net.minecraft.world.World;

public class LichEntity extends UnderlingEntity
{
	
	public LichEntity(EntityType<? extends LichEntity> type, World world)
	{
		super(type, world);
	}
	
	public static AttributeModifierMap.MutableAttribute lichAttributes()
	{
		return UnderlingEntity.underlingAttributes().createMutableAttribute(Attributes.MAX_HEALTH, 175)
				.createMutableAttribute(Attributes.KNOCKBACK_RESISTANCE, 0.3).createMutableAttribute(Attributes.MOVEMENT_SPEED, 0.2)
				.createMutableAttribute(Attributes.ATTACK_DAMAGE, 8);
	}
	
	@Override
	protected void registerGoals()
	{
		super.registerGoals();
		this.goalSelector.addGoal(3, new MeleeAttackGoal(this, 1.0F, false));
	}
	
	protected SoundEvent getAmbientSound()
	{
		return MSSoundEvents.ENTITY_LICH_AMBIENT;
	}
	
	protected SoundEvent getHurtSound(DamageSource damageSourceIn)
	{
		return MSSoundEvents.ENTITY_LICH_HURT;
	}
	
	protected SoundEvent getDeathSound()
	{
		return MSSoundEvents.ENTITY_LICH_DEATH;
	}
	
	@Override
	public GristSet getGristSpoils()
	{
		return GristHelper.generateUnderlingGristDrops(this, damageMap, 8);
	}
	
	@Override
	protected int getVitalityGel()
	{
		return rand.nextInt(3)+6;
	}
	
	@Override
	protected void onGristTypeUpdated(GristType type)
	{
		super.onGristTypeUpdated(type);
		applyGristModifier(Attributes.MAX_HEALTH, 30 * type.getPower(), AttributeModifier.Operation.ADDITION);
		applyGristModifier(Attributes.ATTACK_DAMAGE, 3.4 * type.getPower(), AttributeModifier.Operation.ADDITION);
		this.experienceValue = (int) (6.5 * type.getPower() + 4);
	}
	
	@Override
	public void onDeath(DamageSource cause)
	{
		super.onDeath(cause);
		Entity entity = cause.getTrueSource();
		if(this.dead && !this.world.isRemote)
		{
			computePlayerProgress((int) (300* getGristType().getPower() + 650));
			if(entity instanceof ServerPlayerEntity)
			{
				Echeladder ladder = PlayerSavedData.getData((ServerPlayerEntity) entity).getEcheladder();
				ladder.checkBonus((byte) (Echeladder.UNDERLING_BONUS_OFFSET + 3));
			}
		}
	}
}