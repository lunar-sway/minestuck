package com.mraof.minestuck.entity.underling;

import com.mraof.minestuck.MinestuckConfig;
import com.mraof.minestuck.entity.EntityBigPart;
import com.mraof.minestuck.entity.IBigEntity;
import com.mraof.minestuck.entity.PartGroup;
import com.mraof.minestuck.entity.ai.CustomMeleeAttackGoal;
import com.mraof.minestuck.item.crafting.alchemy.GristHelper;
import com.mraof.minestuck.item.crafting.alchemy.GristSet;
import com.mraof.minestuck.item.crafting.alchemy.GristType;
import com.mraof.minestuck.player.Echeladder;
import com.mraof.minestuck.util.MSSoundEvents;
import com.mraof.minestuck.world.storage.PlayerSavedData;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.MoverType;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.AttributeModifierMap;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.fluid.Fluid;
import net.minecraft.tags.ITag;
import net.minecraft.util.DamageSource;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;

public class GiclopsEntity extends UnderlingEntity implements IBigEntity
{
	private PartGroup partGroup;

	public GiclopsEntity(EntityType<? extends GiclopsEntity> type, World world)
	{
		super(type, world);
		
		this.stepHeight = 2;
		partGroup = new PartGroup(this);
		partGroup.addBox(-4, 2, -1.5, 8, 8, 5);
		partGroup.addBox(-5, 0, -0.5, 3, 2, 3);
		partGroup.addBox(1, 0, -0.5, 3, 2, 3);
		partGroup.createEntities(world);
	}
	
	public static AttributeModifierMap.MutableAttribute giclopsAttributes()
	{
		return UnderlingEntity.underlingAttributes().createMutableAttribute(Attributes.MAX_HEALTH, 210)
				.createMutableAttribute(Attributes.KNOCKBACK_RESISTANCE, 0.9).createMutableAttribute(Attributes.MOVEMENT_SPEED, 0.23)
				.createMutableAttribute(Attributes.ATTACK_DAMAGE, 10);
	}
	
	@Override
	protected void registerGoals()
	{
		super.registerGoals();
		this.goalSelector.addGoal(3, new CustomMeleeAttackGoal(this, 1.0F, false, 50, 1.1F));
	}
	
	protected SoundEvent getAmbientSound()
	{
		return MSSoundEvents.ENTITY_GICLOPS_AMBIENT;
	}
	
	protected SoundEvent getHurtSound(DamageSource damageSourceIn)
	{
		return MSSoundEvents.ENTITY_GICLOPS_HURT;
	}
	
	protected SoundEvent getDeathSound()
	{
		return MSSoundEvents.ENTITY_GICLOPS_DEATH;
	}
	
	@Override
	public GristSet getGristSpoils()
	{
		return GristHelper.generateUnderlingGristDrops(this, damageMap, 10);
	}

	@Override
	protected int getVitalityGel()
	{
		return rand.nextInt(4) + 5;
	}
	
	@Override
	protected void onGristTypeUpdated(GristType type)
	{
		super.onGristTypeUpdated(type);
		applyGristModifier(Attributes.MAX_HEALTH, 46 * type.getPower(), AttributeModifier.Operation.ADDITION);
		applyGristModifier(Attributes.ATTACK_DAMAGE, 4.5 * type.getPower(), AttributeModifier.Operation.ADDITION);
		this.experienceValue = (int) (7 * type.getPower() + 5);
	}
	
	@Override
	public void baseTick()
	{
		super.baseTick();
		partGroup.updatePositions();
		if(!world.isRemote && MinestuckConfig.SERVER.disableGiclops.get())
			this.remove();
	}
	
	@Override
	public void setPositionAndRotation(double par1, double par3, double par5, float par7, float par8) 
	{
		super.setPositionAndRotation(par1, par3, par5, par7, par8);
		partGroup.updatePositions();
	}
	
	@Override
	protected void collideWithEntity(Entity par1Entity)
	{
		if(!(par1Entity instanceof EntityBigPart))
			super.collideWithEntity(par1Entity);
	}

	@Override
	public void applyEntityCollision(Entity entityIn)
	{
	    if(!entityIn.noClip)
		{
			partGroup.applyCollision(entityIn);
		}
	}

	@Override
	public void onDeath(DamageSource cause)
	{
		super.onDeath(cause);
		Entity entity = cause.getTrueSource();
		if(this.dead && !this.world.isRemote)
		{
			computePlayerProgress((int) (500* getGristType().getPower() + 1000));
			if(entity instanceof ServerPlayerEntity)
			{
				Echeladder ladder = PlayerSavedData.getData((ServerPlayerEntity) entity).getEcheladder();
				ladder.checkBonus((byte) (Echeladder.UNDERLING_BONUS_OFFSET + 4));
			}
		}
	}
	
	//Reduced lag is worth not taking damage for being inside a wall
	@Override
	public boolean isEntityInsideOpaqueBlock()
	{
		return false;
	}

	//Only pay attention to the top for water
	
	@Override
	public boolean handleFluidAcceleration(ITag<Fluid> fluidTag, double fluidFactor)
	{
		AxisAlignedBB realBox = this.getBoundingBox();
		this.setBoundingBox(new AxisAlignedBB(realBox.minX, realBox.maxY - 1, realBox.minZ, realBox.maxX, realBox.maxY, realBox.maxZ));
		boolean result = super.handleFluidAcceleration(fluidTag, fluidFactor);
		this.setBoundingBox(realBox);
		return result;
	}
	
	
	@Override
	public void move(MoverType typeIn, Vector3d pos)
	{
		AxisAlignedBB realBox = this.getBoundingBox();
		double minX = pos.x > 0 ? realBox.maxX - pos.x : realBox.minX;
/*				y > 0 ? realBox.maxY - y : realBox.minY,*/
		double minY = realBox.minY;
		double minZ = pos.z > 0 ? realBox.maxZ - pos.z : realBox.minZ;
		double maxX = pos.x < 0 ? realBox.minX - pos.x : realBox.maxX;
		double maxY = pos.y < 0 ? realBox.minY - pos.y : realBox.maxY;
		double maxZ = pos.z < 0 ? realBox.minZ - pos.z : realBox.maxZ;
		this.setBoundingBox(new AxisAlignedBB(minX, minY, minZ, maxX, maxY, maxZ));
		super.move(typeIn, pos);
		AxisAlignedBB changedBox = this.getBoundingBox();
		this.setBoundingBox(realBox.offset(changedBox.minX - minX, changedBox.minY - minY, changedBox.minZ - minZ));
		this.resetPositionToBB();
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
	public void remove()
	{
		super.remove();
		partGroup.updatePositions();
	}

	@Override
	public boolean canBeCollidedWith()
	{
		return true;
	}
}