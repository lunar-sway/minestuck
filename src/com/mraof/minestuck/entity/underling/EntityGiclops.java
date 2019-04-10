package com.mraof.minestuck.entity.underling;

import com.mraof.minestuck.Minestuck;
import com.mraof.minestuck.MinestuckConfig;
import com.mraof.minestuck.alchemy.GristHelper;
import com.mraof.minestuck.alchemy.GristSet;
import com.mraof.minestuck.alchemy.GristType;
import com.mraof.minestuck.entity.EntityBigPart;
import com.mraof.minestuck.entity.IBigEntity;
import com.mraof.minestuck.entity.PartGroup;
import com.mraof.minestuck.entity.ai.EntityAIAttackOnCollideWithRate;
import com.mraof.minestuck.util.*;
import net.minecraft.entity.Entity;
import net.minecraft.entity.MoverType;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.MobEffects;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.DamageSource;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeHooks;

import javax.annotation.Nullable;

public class EntityGiclops extends EntityUnderling implements IBigEntity
{
	private PartGroup partGroup;

	public EntityGiclops(World world)
	{
		super(world);

		setSize(8.0F, 12.0F);
		this.stepHeight = 2;
		partGroup = new PartGroup(this);
		partGroup.addBox(-4, 2, -1.5, 8, 8, 5);
		partGroup.addBox(-5, 0, -0.5, 3, 2, 3);
		partGroup.addBox(1, 0, -0.5, 3, 2, 3);
		partGroup.createEntities(world);
	}
	
	@Override
	protected String getUnderlingName()
	{
		return "giclops";
	}
	
	@Override
	protected void initEntityAI()
	{
		super.initEntityAI();
		EntityAIAttackOnCollideWithRate aiAttack = new EntityAIAttackOnCollideWithRate(this, .3F, 50, false);
		aiAttack.setDistanceMultiplier(1.1F);
		this.tasks.addTask(3, aiAttack);
	}
	
	protected SoundEvent getAmbientSound()
	{
		return MinestuckSoundHandler.soundGiclopsAmbient;
	}
	
	protected SoundEvent getHurtSound(DamageSource damageSourceIn)
	{
		return MinestuckSoundHandler.soundGiclopsHurt;
	}
	
	protected SoundEvent getDeathSound()
	{
		return MinestuckSoundHandler.soundGiclopsDeath;
	}
	
	@Override
	public GristSet getGristSpoils()
	{
		return GristHelper.getRandomDrop(type, 10);
	}

	@Override
	protected double getWanderSpeed() 
	{
		return 0.7;
	}
	
	@Override
	protected float getKnockbackResistance()
	{
		return 0.9F;
	}
	
	@Override
	protected double getAttackDamage()
	{
		return this.type.getPower()*4.5 + 10;
	}
	
	@Override
	protected int getVitalityGel()
	{
		return rand.nextInt(4) + 5;
	}
	
	@Override
	protected void applyGristType(GristType type, boolean fullHeal)
	{
		super.applyGristType(type, fullHeal);
		this.experienceValue = (int) (7 * type.getPower() + 5);
	}
	
	@Override
	protected void applyEntityAttributes()
	{
		super.applyEntityAttributes();
		this.getEntityAttribute(SharedMonsterAttributes.FOLLOW_RANGE).setBaseValue(32.0D);
	}
	
	@Override
	protected void jump()
	{
		this.motionY = 0.42D;

		PotionEffect jumpBoost = this.getActivePotionEffect(MobEffects.JUMP_BOOST);
		if (jumpBoost != null)
		{
			this.motionY += (double)((float)(jumpBoost.getAmplifier() + 1) * 0.1F);
		}

		if (this.isSprinting())
		{
			float f = this.rotationYaw * 0.017453292F;
			this.motionX -= (double)(MathHelper.sin(f) * 0.2F);
			this.motionZ += (double)(MathHelper.cos(f) * 0.2F);
		}
		
		this.isAirBorne = true;
		ForgeHooks.onLivingJump(this);
	}
	
	@Override
	protected float getMaximumHealth() 
	{
		return type != null ? 46*type.getPower() + 210 : 1;
	}
	
	@Override
	public void onEntityUpdate() 
	{
		super.onEntityUpdate();
		partGroup.updatePositions();
		if(!world.isRemote && MinestuckConfig.disableGiclops)
			this.setDead();
	}
	
	@Override
	public void setPositionAndRotation(double par1, double par3, double par5, float par7, float par8) 
	{
		super.setPositionAndRotation(par1, par3, par5, par7, par8);
		partGroup.updatePositions();
	}
	
	@Override
	public Entity[] getParts() 
	{
		Entity[] array = new Entity[partGroup.parts.size()];
		return partGroup.parts.toArray(array);
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
		if(this.dead && !this.world.isRemote && type != null)
		{
			computePlayerProgress((int) (500*type.getPower() + 1000));
			if(entity != null && entity instanceof EntityPlayerMP)
			{
				//((EntityPlayerMP) entity).addStat(MinestuckAchievementHandler.killGiclops);
				Echeladder ladder = MinestuckPlayerData.getData((EntityPlayerMP) entity).echeladder;
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
	public boolean handleWaterMovement()
	{
		AxisAlignedBB realBox = this.getEntityBoundingBox();
		this.setEntityBoundingBox(new AxisAlignedBB(realBox.minX, realBox.maxY - 1, realBox.minZ, realBox.maxX, realBox.maxY, realBox.maxZ));
		boolean result = super.handleWaterMovement();
		this.setEntityBoundingBox(realBox);
		return result;
	}
	
	@Override
	public void move(MoverType type, double x, double y, double z)
	{
		AxisAlignedBB realBox = this.getEntityBoundingBox();
		double minX = x > 0 ? realBox.maxX - x : realBox.minX;
/*				y > 0 ? realBox.maxY - y : realBox.minY,*/
		double minY = realBox.minY;
		double minZ = z > 0 ? realBox.maxZ - z : realBox.minZ;
		double maxX = x < 0 ? realBox.minX - x : realBox.maxX;
		double maxY = y < 0 ? realBox.minY - y : realBox.maxY;
		double maxZ = z < 0 ? realBox.minZ - z : realBox.maxZ;
		this.setEntityBoundingBox(new AxisAlignedBB(minX, minY, minZ, maxX, maxY, maxZ));
		super.move(type, x, y, z);
		AxisAlignedBB changedBox = this.getEntityBoundingBox();
		this.setEntityBoundingBox(realBox.offset(changedBox.minX - minX, changedBox.minY - minY, changedBox.minZ - minZ));
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
	public void setDead()
	{
		super.setDead();
		partGroup.updatePositions();
	}

	@Override
	public boolean canBeCollidedWith()
	{
		return true;
	}
}