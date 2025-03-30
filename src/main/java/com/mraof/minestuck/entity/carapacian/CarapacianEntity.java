package com.mraof.minestuck.entity.carapacian;

import com.mraof.minestuck.entity.AnimatedPathfinderMob;
import com.mraof.minestuck.entity.EntityListFilter;
import com.mraof.minestuck.entity.ai.HurtByTargetAlliedGoal;
import com.mraof.minestuck.util.MSTags;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.WaterAvoidingRandomStrollGoal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public abstract class CarapacianEntity extends AnimatedPathfinderMob
{
	private final EnumEntityKingdom kingdom;
	
	protected List<EntityType<?>> enemyTypes = new ArrayList<>();    //TODO Save this!
	protected final TagKey<EntityType<?>> allyTag;
	protected EntityListFilter attackEntitySelector = new EntityListFilter(enemyTypes);
	
	public CarapacianEntity(EntityType<? extends CarapacianEntity> type, EnumEntityKingdom kingdom, Level level)
	{
		super(type, level);
		this.kingdom = kingdom;
		setEnemies();
		allyTag = kingdom == EnumEntityKingdom.PROSPITIAN ? MSTags.EntityTypes.PROSPITIAN_CARAPACIANS : MSTags.EntityTypes.DERSITE_CARAPACIANS;
	}
	
	@Override
	protected void registerGoals()
	{
		super.registerGoals();
		this.goalSelector.addGoal(1, new FloatGoal(this));
		//this.goalSelector.addGoal(4, new EntityAIMoveToBattle(this));
		this.targetSelector.addGoal(1, new HurtByTargetAlliedGoal(this, this::isAlly));
		this.goalSelector.addGoal(5, new WaterAvoidingRandomStrollGoal(this, 1.0F));
		this.goalSelector.addGoal(6, new RandomLookAroundGoal(this));
		this.goalSelector.addGoal(6, new LookAtPlayerGoal(this, Player.class, 8.0F));
	}
	
	public static AttributeSupplier.Builder carapacianAttributes()
	{
		return Mob.createMobAttributes().add(Attributes.FOLLOW_RANGE, 32);
	}
	
	private void setEnemies()
	{
		switch(this.getKingdom())
		{
			case PROSPITIAN:
				BuiltInRegistries.ENTITY_TYPE.getTag(MSTags.EntityTypes.DERSITE_CARAPACIANS).ifPresent(set -> enemyTypes.addAll(set.stream().map(Holder::value).toList()));    //TODO Should refer to tags directly. Entities will otherwise need to be reconstructed for resource reload changes to take place, is this now resolved?
				break;
			case DERSITE:
				BuiltInRegistries.ENTITY_TYPE.getTag(MSTags.EntityTypes.PROSPITIAN_CARAPACIANS).ifPresent(set -> enemyTypes.addAll(set.stream().map(Holder::value).toList()));
		}
	}
	
	public void addEnemy(EntityType<?> enemyType)
	{
		if(canAttackType(enemyType) && !enemyTypes.contains(enemyType))
		{
			enemyTypes.add(enemyType);
		}
	}
	
	@Override
	public void setTarget(LivingEntity entity)
	{
		super.setTarget(entity);
		if(entity != null)
		{
			this.addEnemy(entity.getType());
		}
	}
	
	@Override
	public boolean canAttackType(EntityType<?> typeIn)
	{
		return !typeIn.is(allyTag);
	}
	
	public EnumEntityKingdom getKingdom()
	{
		return Objects.requireNonNull(kingdom);
	}
	
	public boolean isAlly(Entity entity)
	{
		return entity.getType().is(allyTag);
	}
}