package com.mraof.minestuck.entity.carapacian;

import com.mraof.minestuck.entity.EntityListFilter;
import com.mraof.minestuck.entity.SimpleTexturedEntity;
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
import net.minecraft.tags.Tag;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public abstract class CarapacianEntity extends SimpleTexturedEntity
{
	private final EnumEntityKingdom kingdom;
	
	protected List<EntityType<?>> enemyTypes = new ArrayList<>();	//TODO Save this!
	protected final Tag<EntityType<?>> allyTag;
	protected EntityListFilter attackEntitySelector = new EntityListFilter(enemyTypes);

	public CarapacianEntity(EntityType<? extends CarapacianEntity> type, EnumEntityKingdom kingdom, World world)
	{
		super(type, world);
		this.kingdom = kingdom;
		setEnemies();
		allyTag = kingdom == EnumEntityKingdom.PROSPITIAN ? MSTags.EntityTypes.PROSPITIAN_CARAPACIANS : MSTags.EntityTypes.DERSITE_CARAPACIANS;
	}
	
	@Override
	protected void registerGoals()
	{
		this.goalSelector.addGoal(1, new SwimGoal(this));
		//this.goalSelector.addGoal(4, new EntityAIMoveToBattle(this));
		this.targetSelector.addGoal(1, new HurtByTargetAlliedGoal(this, this::isAlly));
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
		return !allyTag.contains(typeIn);
	}
	
	public EnumEntityKingdom getKingdom()
	{
		return Objects.requireNonNull(kingdom);
	}
	
	public boolean isAlly(Entity entity)
	{
		return allyTag.contains(entity.getType());
	}
}