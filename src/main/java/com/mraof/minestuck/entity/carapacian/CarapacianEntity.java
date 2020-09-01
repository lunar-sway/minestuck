package com.mraof.minestuck.entity.carapacian;

import com.mraof.minestuck.entity.EntityListFilter;
import com.mraof.minestuck.entity.MinestuckEntity;
import com.mraof.minestuck.entity.ai.HurtByTargetAlliedGoal;
import com.mraof.minestuck.util.MSTags;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.goal.LookAtGoal;
import net.minecraft.entity.ai.goal.LookRandomlyGoal;
import net.minecraft.entity.ai.goal.SwimGoal;
import net.minecraft.entity.ai.goal.WaterAvoidingRandomWalkingGoal;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

public abstract class CarapacianEntity extends MinestuckEntity
{
	protected List<EntityType<?>> enemyTypes = new ArrayList<>();	//TODO Save this!
	protected List<EntityType<?>> allyTypes = new ArrayList<>();
	protected static final Predicate<Entity> PROSPITIAN_SELECTOR = entity -> MSTags.EntityTypes.PROSPITIAN_CARAPACIANS.contains(entity.getType());
	protected static final Predicate<Entity> DERSITE_SELECTOR = entity -> MSTags.EntityTypes.DERSITE_CARAPACIANS.contains(entity.getType());
	protected EntityListFilter attackEntitySelector = new EntityListFilter(enemyTypes);

	public CarapacianEntity(EntityType<? extends CarapacianEntity> type, World world)
	{
		super(type, world);
		setEnemies();
		setAllies();
	}
	
	@Override
	protected void registerGoals()
	{
		this.goalSelector.addGoal(1, new SwimGoal(this));
		//this.goalSelector.addGoal(4, new EntityAIMoveToBattle(this));
		this.targetSelector.addGoal(1, new HurtByTargetAlliedGoal(this, this.getKingdom() == EnumEntityKingdom.PROSPITIAN ? PROSPITIAN_SELECTOR : DERSITE_SELECTOR));
		this.goalSelector.addGoal(5, new WaterAvoidingRandomWalkingGoal(this, 1.0F));
		this.goalSelector.addGoal(6, new LookRandomlyGoal(this));
		this.goalSelector.addGoal(6, new LookAtGoal(this, PlayerEntity.class, 8.0F));
	}
	
	@Override
	protected void registerAttributes()
	{
		super.registerAttributes();
		getAttribute(SharedMonsterAttributes.FOLLOW_RANGE).setBaseValue(32.0D);
	}
	
	private void setAllies()
	{
		switch(this.getKingdom())
		{
			case PROSPITIAN:
				allyTypes.addAll(MSTags.EntityTypes.PROSPITIAN_CARAPACIANS.getAllElements());
				break;
			case DERSITE:
				allyTypes.addAll(MSTags.EntityTypes.DERSITE_CARAPACIANS.getAllElements());
		}
	}
	
	private void setEnemies()
	{
		switch(this.getKingdom())
		{
			case PROSPITIAN:
				enemyTypes.addAll(MSTags.EntityTypes.DERSITE_CARAPACIANS.getAllElements());	//TODO Should refer to tags directly. Entities will otherwise need to be reconstructed for resource reload changes to take place
				break;
			case DERSITE:
				enemyTypes.addAll(MSTags.EntityTypes.PROSPITIAN_CARAPACIANS.getAllElements());
		}
	}
	
	public void addEnemy(EntityType<?> enemyType)
	{
		if(canAttack(enemyType) && !enemyTypes.contains(enemyType))
		{
			enemyTypes.add(enemyType);
		}
	}
	
	@Override
	public void setAttackTarget(LivingEntity entity)
	{
		super.setAttackTarget(entity);
		if(entity != null)
		{
			this.addEnemy(entity.getType());
		}
	}
	
	@Override
	public boolean canAttack(EntityType<?> typeIn)
	{
		return !this.allyTypes.contains(typeIn);
	}
	
	public abstract EnumEntityKingdom getKingdom();
}