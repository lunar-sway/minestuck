package com.mraof.minestuck.entity.carapacian;

import com.mraof.minestuck.entity.AttackingAnimatedEntity;
import com.mraof.minestuck.entity.EntityListFilter;
import com.mraof.minestuck.entity.ai.HurtByTargetAlliedGoal;
import com.mraof.minestuck.util.MSTags;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.ai.attributes.AttributeModifierMap;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.ai.goal.LookAtGoal;
import net.minecraft.entity.ai.goal.LookRandomlyGoal;
import net.minecraft.entity.ai.goal.SwimGoal;
import net.minecraft.entity.ai.goal.WaterAvoidingRandomWalkingGoal;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.tags.ITag;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public abstract class CarapacianEntity extends AttackingAnimatedEntity
{
	private final EnumEntityKingdom kingdom;
	
	protected List<EntityType<?>> enemyTypes = new ArrayList<>();    //TODO Save this!
	protected final ITag<EntityType<?>> allyTag;
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
		super.registerGoals();
		this.goalSelector.addGoal(1, new SwimGoal(this));
		//this.goalSelector.addGoal(4, new EntityAIMoveToBattle(this));
		this.targetSelector.addGoal(1, new HurtByTargetAlliedGoal(this, this::isAlly));
		this.goalSelector.addGoal(5, new WaterAvoidingRandomWalkingGoal(this, 1.0F));
		this.goalSelector.addGoal(6, new LookRandomlyGoal(this));
		this.goalSelector.addGoal(6, new LookAtGoal(this, PlayerEntity.class, 8.0F));
	}
	
	public static AttributeModifierMap.MutableAttribute carapacianAttributes()
	{
		return MobEntity.createMobAttributes().add(Attributes.FOLLOW_RANGE, 32);
	}
	
	private void setEnemies()
	{
		switch(this.getKingdom())
		{
			case PROSPITIAN:
				enemyTypes.addAll(MSTags.EntityTypes.DERSITE_CARAPACIANS.getValues());    //TODO Should refer to tags directly. Entities will otherwise need to be reconstructed for resource reload changes to take place
				break;
			case DERSITE:
				enemyTypes.addAll(MSTags.EntityTypes.PROSPITIAN_CARAPACIANS.getValues());
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
		return !allyTag.contains(typeIn);
	}
	
	@Override
	protected void onAttackStart()
	{
		// Will stop the entity while performing its attack animation
		this.getNavigation().stop();
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