package com.mraof.minestuck.entity.underling;

import com.mraof.minestuck.MinestuckConfig;
import com.mraof.minestuck.entity.EntityBigPart;
import com.mraof.minestuck.entity.IBigEntity;
import com.mraof.minestuck.entity.PartGroup;
import com.mraof.minestuck.entity.ai.CustomMeleeAttackGoal;
import com.mraof.minestuck.alchemy.GristHelper;
import com.mraof.minestuck.alchemy.GristSet;
import com.mraof.minestuck.alchemy.GristType;
import com.mraof.minestuck.player.Echeladder;
import com.mraof.minestuck.util.MSSoundEvents;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.tags.TagKey;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

public class GiclopsEntity extends UnderlingEntity implements IBigEntity
{
	private PartGroup partGroup;
	
	public GiclopsEntity(EntityType<? extends GiclopsEntity> type, Level level)
	{
		super(type, level, 7);
		
		this.maxUpStep = 2;
		partGroup = new PartGroup(this);
		partGroup.addBox(-4, 2, -1.5, 8, 8, 5);
		partGroup.addBox(-5, 0, -0.5, 3, 2, 3);
		partGroup.addBox(1, 0, -0.5, 3, 2, 3);
		partGroup.createEntities(level);
	}
	
	public static AttributeSupplier.Builder giclopsAttributes()
	{
		return UnderlingEntity.underlingAttributes().add(Attributes.MAX_HEALTH, 210)
				.add(Attributes.KNOCKBACK_RESISTANCE, 0.9).add(Attributes.MOVEMENT_SPEED, 0.23)
				.add(Attributes.ATTACK_DAMAGE, 10);
	}
	
	@Override
	protected void registerGoals()
	{
		super.registerGoals();
		this.goalSelector.addGoal(3, new CustomMeleeAttackGoal(this, 1.0F, false, 50, 1.1F));
	}
	
	@Override
	protected SoundEvent getAmbientSound()
	{
		return MSSoundEvents.ENTITY_GICLOPS_AMBIENT.get();
	}
	
	@Override
	protected SoundEvent getHurtSound(DamageSource damageSourceIn)
	{
		return MSSoundEvents.ENTITY_GICLOPS_HURT.get();
	}
	
	@Override
	protected SoundEvent getDeathSound()
	{
		return MSSoundEvents.ENTITY_GICLOPS_DEATH.get();
	}
	
	@Override
	public GristSet getGristSpoils()
	{
		return GristHelper.generateUnderlingGristDrops(this, damageMap, 10);
	}
	
	@Override
	protected int getVitalityGel()
	{
		return random.nextInt(4) + 5;
	}
	
	@Override
	protected void onGristTypeUpdated(GristType type)
	{
		super.onGristTypeUpdated(type);
		applyGristModifier(Attributes.MAX_HEALTH, 46 * type.getPower(), AttributeModifier.Operation.ADDITION);
		applyGristModifier(Attributes.ATTACK_DAMAGE, 4.5 * type.getPower(), AttributeModifier.Operation.ADDITION);
		this.xpReward = (int) (7 * type.getPower() + 5);
	}
	
	@Override
	public void baseTick()
	{
		super.baseTick();
		partGroup.updatePositions();
		if(!level.isClientSide && MinestuckConfig.SERVER.disableGiclops.get())
			this.discard();
	}
	
	@Override
	public void absMoveTo(double par1, double par3, double par5, float par7, float par8)
	{
		super.absMoveTo(par1, par3, par5, par7, par8);
		partGroup.updatePositions();
	}
	
	@Override
	protected void doPush(Entity entity)
	{
		if(!(entity instanceof EntityBigPart))
			super.doPush(entity);
	}
	
	@Override
	public void push(Entity entityIn)
	{
		if(!entityIn.noPhysics)
		{
			partGroup.applyCollision(entityIn);
		}
	}
	
	@Override
	public void die(DamageSource cause)
	{
		super.die(cause);
		Entity entity = cause.getEntity();
		if(this.dead && !this.level.isClientSide)
		{
			computePlayerProgress((int) (200 + 3 * getGristType().getPower())); //still give xp up to top rung
			firstKillBonus(entity, (byte) (Echeladder.UNDERLING_BONUS_OFFSET + 4));
		}
	}
	
	//Reduced lag is worth not taking damage for being inside a wall
	@Override
	public boolean isInWall()
	{
		return false;
	}
	
	//Only pay attention to the top for water
	
	@Override
	public boolean updateFluidHeightAndDoFluidPushing(TagKey<Fluid> fluidTag, double fluidFactor)
	{
		AABB realBox = this.getBoundingBox();
		this.setBoundingBox(new AABB(realBox.minX, realBox.maxY - 1, realBox.minZ, realBox.maxX, realBox.maxY, realBox.maxZ));
		boolean result = super.updateFluidHeightAndDoFluidPushing(fluidTag, fluidFactor);
		this.setBoundingBox(realBox);
		return result;
	}
	
	
	@Override
	public void move(MoverType typeIn, Vec3 pos)    //TODO probably doesn't work as originally intended anymore. What was this meant to do?
	{
		AABB realBox = this.getBoundingBox();
		double minX = pos.x > 0 ? realBox.maxX - pos.x : realBox.minX;
		/*				y > 0 ? realBox.maxY - y : realBox.minY,*/
		double minY = realBox.minY;
		double minZ = pos.z > 0 ? realBox.maxZ - pos.z : realBox.minZ;
		double maxX = pos.x < 0 ? realBox.minX - pos.x : realBox.maxX;
		double maxY = pos.y < 0 ? realBox.minY - pos.y : realBox.maxY;
		double maxZ = pos.z < 0 ? realBox.minZ - pos.z : realBox.maxZ;
		this.setBoundingBox(new AABB(minX, minY, minZ, maxX, maxY, maxZ));
		super.move(typeIn, pos);
		AABB changedBox = this.getBoundingBox();
		this.setPos(this.getX() + changedBox.minX - minX, this.getY() + changedBox.minY - minY, this.getZ() + changedBox.minZ - minZ);
	}
	
	@Override
	public PartGroup getGroup()
	{
		return partGroup;
	}
	
	/**
	 * Will get destroyed next tick.
	 */
	@Override
	public void remove(RemovalReason reason)
	{
		super.remove(reason);
		partGroup.updatePositions();
	}
	
	@Override
	public boolean isPickable()
	{
		return true;
	}
}