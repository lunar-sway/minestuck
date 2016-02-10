package com.mraof.minestuck.entity.underling;

import net.minecraft.entity.Entity;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.potion.Potion;
import net.minecraft.util.DamageSource;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeHooks;

import com.mraof.minestuck.entity.IEntityMultiPart;
import com.mraof.minestuck.entity.ai.EntityAIAttackOnCollideWithRate;
import com.mraof.minestuck.util.Echeladder;
import com.mraof.minestuck.util.GristHelper;
import com.mraof.minestuck.util.GristSet;
import com.mraof.minestuck.util.GristType;
import com.mraof.minestuck.util.MinestuckAchievementHandler;
import com.mraof.minestuck.util.MinestuckPlayerData;

public class EntityGiclops extends EntityUnderling implements IEntityMultiPart
{
	private EntityAIAttackOnCollideWithRate entityAIAttackOnCollideWithRate;
	
	EntityUnderlingPart topPart;

	public EntityGiclops(World world)
	{
		super(world, "Giclops");

		setSize(8.0F, 12.0F);
		this.stepHeight = 2;
		topPart = new EntityUnderlingPart(this, 0, 6.0F, 7.0F);
		world.spawnEntityInWorld(topPart);
	}

	@Override
	public GristSet getGristSpoils()
	{
		return GristHelper.getRandomDrop(type, 8);
	}

	@Override
	protected void setCombatTask() 
	{
		if(entityAIAttackOnCollideWithRate == null)
			entityAIAttackOnCollideWithRate = new EntityAIAttackOnCollideWithRate(this, .3F, 50, false);
		entityAIAttackOnCollideWithRate.setDistanceMultiplier(1.1F);
		this.tasks.removeTask(this.entityAIAttackOnCollideWithRate);
		this.tasks.addTask(4, entityAIAttackOnCollideWithRate);
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
	protected void applyGristType(GristType type, boolean fullHeal)
	{
		super.applyGristType(type, fullHeal);
		this.experienceValue = (int) (7 * type.getPower() + 5);
	}
	
	@Override
	protected void applyEntityAttributes()
	{
		super.applyEntityAttributes();
		this.getEntityAttribute(SharedMonsterAttributes.followRange).setBaseValue(32.0D);
		
	}
	
	@Override
	protected void jump() {
		this.motionY = 0.41999998688697815D;

        if (this.isPotionActive(Potion.jump))
        {
            this.motionY += (double)((float)(this.getActivePotionEffect(Potion.jump).getAmplifier() + 1) * 0.1F);
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
		return type != null ? 45*type.getPower() + 200 : 1;
	}
	
	@Override
	public void onEntityUpdate() 
	{
		super.onEntityUpdate();
		this.updatePartPositions();
	}
	
	@Override
	public void setPositionAndRotation(double par1, double par3, double par5, float par7, float par8) 
	{
		super.setPositionAndRotation(par1, par3, par5, par7, par8);
		this.updatePartPositions();
	}
	
	@Override
	public Entity[] getParts() 
	{
		return new Entity[] {topPart};
	}
	
	@Override
	public World getWorld() 
	{
		return this.worldObj;
	}
	
	@Override
	protected void collideWithEntity(Entity par1Entity) 
	{
		if(par1Entity != topPart)
			super.collideWithEntity(par1Entity);
	}
	
	@Override
	public boolean attackEntityFromPart(Entity entityPart, DamageSource source, float damage) 
	{
		return this.attackEntityFrom(source, damage);
	}
	
	@Override
	public void updatePartPositions()
	{
		float f1 = this.prevRotationYaw + (this.rotationYaw - this.prevRotationYaw);
		double topPartPosX = (this.posX + -Math.sin(f1 / 180.0 * Math.PI) * 2);
		double topPartPosZ = (this.posZ + Math.cos(f1 / 180.0 * Math.PI) * 2);
		
		if(topPart != null)
			topPart.setPositionAndRotation(topPartPosX, this.posY + 6, topPartPosZ, this.rotationYaw, this.rotationPitch);
	}

	@Override
	public void addPart(Entity entityPart, int id) 
	{
		this.topPart = (EntityUnderlingPart) entityPart;
	}

	@Override
	public void onPartDeath(Entity entityPart, int id) 
	{

	}
	
	@Override
	public void onDeath(DamageSource cause)
	{
		super.onDeath(cause);
		Entity entity = cause.getEntity();
		if(this.dead && !this.worldObj.isRemote)
		{
			computePlayerProgress((int) (500*type.getPower() + 1000));
			if(entity != null && entity instanceof EntityPlayerMP)
			{
				((EntityPlayerMP) entity).triggerAchievement(MinestuckAchievementHandler.killGiclops);
				Echeladder ladder = MinestuckPlayerData.getData((EntityPlayerMP) entity).echeladder;
				ladder.checkBonus((byte) (Echeladder.UNDERLING_BONUS_OFFSET + 3));
			}
		}
	}
}