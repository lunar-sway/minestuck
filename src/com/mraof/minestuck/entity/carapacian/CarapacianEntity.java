package com.mraof.minestuck.entity.carapacian;

import java.util.ArrayList;
import java.util.List;

import com.mraof.minestuck.entity.MSEntityTypes;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.goal.LookAtGoal;
import net.minecraft.entity.ai.goal.LookRandomlyGoal;
import net.minecraft.entity.ai.goal.SwimGoal;
import net.minecraft.entity.ai.goal.WaterAvoidingRandomWalkingGoal;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.world.World;

import com.mraof.minestuck.entity.EntityListFilter;
import com.mraof.minestuck.entity.MinestuckEntity;
import com.mraof.minestuck.entity.ai.HurtByTargetAlliedGoal;
import com.mraof.minestuck.entity.ai.NearestAttackableTargetWithHeightGoal;

public abstract class CarapacianEntity extends MinestuckEntity
{
	protected List<EntityType<?>> enemyTypes;
	protected List<EntityType<?>> allyTypes;
	protected static List<EntityType<?>> prospitianTypes = new ArrayList<>();
	protected static List<EntityType<?>> dersiteTypes = new ArrayList<>();
	protected static EntityListFilter prospitianSelector = new EntityListFilter(prospitianTypes);
	protected static EntityListFilter dersiteSelector = new EntityListFilter(dersiteTypes);
	static
	{
		dersiteTypes.add(MSEntityTypes.DERSITE_PAWN);
		dersiteTypes.add(MSEntityTypes.DERSITE_BISHOP);
		dersiteTypes.add(MSEntityTypes.DERSITE_ROOK);

		prospitianTypes.add(MSEntityTypes.PROSPITIAN_PAWN);
		prospitianTypes.add(MSEntityTypes.PROSPITIAN_BISHOP);
		prospitianTypes.add(MSEntityTypes.PROSPITIAN_ROOK);
	}
	protected EntityListFilter attackEntitySelector;

	public CarapacianEntity(EntityType<? extends CarapacianEntity> type, World world)
	{
		super(type, world);
		enemyTypes = new ArrayList<>();
		allyTypes = new ArrayList<>();
		setEnemies();
		setAllies();
		
		this.goalSelector.addGoal(1, new SwimGoal(this));
		//this.goalSelector.addGoal(4, new EntityAIMoveToBattle(this));
		this.targetSelector.addGoal(1, new HurtByTargetAlliedGoal(this, this.getKingdom() == EnumEntityKingdom.PROSPITIAN ? prospitianSelector : dersiteSelector));
		this.targetSelector.addGoal(2, this.entityAINearestAttackableTargetWithHeight());
		this.goalSelector.addGoal(5, new WaterAvoidingRandomWalkingGoal(this, this.getWanderSpeed()));
		this.goalSelector.addGoal(6, new LookRandomlyGoal(this));
		this.goalSelector.addGoal(6, new LookAtGoal(this, PlayerEntity.class, 8.0F));
		
		if (world != null && !world.isRemote)
		{
			this.setCombatTask();
		}
	}
	
	protected abstract void setCombatTask();

	public abstract float getWanderSpeed();

	public void setEnemies()
	{
		attackEntitySelector = new EntityListFilter(enemyTypes);
		switch(this.getKingdom())
		{
			case PROSPITIAN:
				enemyTypes.addAll(dersiteTypes);
				break;
			case DERSITE:
				enemyTypes.addAll(prospitianTypes);
		}
	}
	public void addEnemy(EntityType<?> enemyType)
	{
		if(canAttack(enemyType) && !enemyTypes.contains(enemyType))
		{
			enemyTypes.add(enemyType);
			this.setEnemies();
			this.setCombatTask();
		}
	}
	public void setAllies() 
	{
		switch(this.getKingdom())
		{
			case PROSPITIAN:
				allyTypes.addAll(prospitianTypes);
				break;
			case DERSITE:
				allyTypes.addAll(dersiteTypes);
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
	
	NearestAttackableTargetWithHeightGoal entityAINearestAttackableTargetWithHeight()
	{
		return new NearestAttackableTargetWithHeightGoal(this, LivingEntity.class, 256.0F, 0, true, false, attackEntitySelector);
	}
	public abstract EnumEntityKingdom getKingdom();

}
