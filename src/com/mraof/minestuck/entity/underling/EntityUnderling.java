package com.mraof.minestuck.entity.underling;

import io.netty.buffer.ByteBuf;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.IEntityLivingData;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAILookIdle;
import net.minecraft.entity.ai.EntityAISwimming;
import net.minecraft.entity.ai.EntityAIWander;
import net.minecraft.entity.ai.EntityAIWatchClosest;
import net.minecraft.entity.monster.IMob;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.DamageSource;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.translation.I18n;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.EnumDifficulty;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.IEntityAdditionalSpawnData;

import com.mraof.minestuck.entity.EntityListFilter;
import com.mraof.minestuck.entity.EntityMinestuck;
import com.mraof.minestuck.entity.ai.EntityAIHurtByTargetAllied;
import com.mraof.minestuck.entity.ai.EntityAINearestAttackableTargetWithHeight;
import com.mraof.minestuck.entity.item.EntityGrist;
import com.mraof.minestuck.entity.item.EntityVitalityGel;
import com.mraof.minestuck.network.skaianet.SburbHandler;
import com.mraof.minestuck.util.Echeladder;
import com.mraof.minestuck.util.GristAmount;
import com.mraof.minestuck.util.GristSet;
import com.mraof.minestuck.util.GristType;

public abstract class EntityUnderling extends EntityMinestuck implements IEntityAdditionalSpawnData, IMob
{

	protected List<Class<? extends EntityLivingBase>> enemyClasses;
	@SuppressWarnings("unchecked")
	protected static EntityListFilter underlingSelector = new EntityListFilter(Arrays.<Class<? extends EntityLivingBase>>asList(EntityImp.class, EntityOgre.class, EntityBasilisk.class, EntityGiclops.class));
	protected EntityListFilter attackEntitySelector;
	//The type of the underling
	protected GristType type;
	//Name of underling, used in getting the texture and actually naming it
	public String underlingName;
	
	private static final float maxSharedProgress = 2;	//The multiplier for the maximum amount progress that can be gathered from each enemy with the group fight bonus
	
	protected Map<EntityPlayerMP, Double> damageMap = new HashMap<EntityPlayerMP, Double>();	//Map that stores how much damage each player did to this to this underling. Null is used for environmental or other non-player damage
	
	//random used in randomly choosing a type of creature
	protected static Random randStatic = new Random();
	
	public EntityUnderling(World par1World, String underlingName) 
	{
		super(par1World);
		
		this.underlingName = underlingName;
		enemyClasses = new ArrayList<Class<? extends EntityLivingBase>>();
		setEnemies();

		this.tasks.addTask(1, new EntityAISwimming(this));
		this.targetTasks.addTask(1, new EntityAIHurtByTargetAllied(this, underlingSelector));
		this.targetTasks.addTask(2, new EntityAINearestAttackableTargetWithHeight(this, EntityPlayer.class, 128.0F, 2, true, false));
		this.targetTasks.addTask(2, this.entityAINearestAttackableTargetWithHeight());
		this.tasks.addTask(5, new EntityAIWander(this, this.getWanderSpeed()));
		this.tasks.addTask(6, new EntityAILookIdle(this));
		this.tasks.addTask(6, new EntityAIWatchClosest(this, EntityPlayer.class, 8.0F));

		if (par1World != null && !par1World.isRemote)
		{
			this.setCombatTask();
		}

	}
	
	@Override
	protected void applyEntityAttributes()
	{
		super.applyEntityAttributes();
		this.getAttributeMap().registerAttribute(SharedMonsterAttributes.ATTACK_DAMAGE);
		
		this.getEntityAttribute(SharedMonsterAttributes.KNOCKBACK_RESISTANCE).setBaseValue((double)(this.getKnockbackResistance()));
		this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(this.getWanderSpeed());
	}
	
	protected void applyGristType(GristType type, boolean fullHeal)
	{
		this.type = type;
		this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(this.getMaximumHealth());
		this.getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).setBaseValue(this.getAttackDamage());
		if(fullHeal)
			this.setHealth(this.getMaxHealth());
	}
	
	//used when getting how much grist should be dropped on death
	public abstract GristSet getGristSpoils();

	protected abstract void setCombatTask();
	
	protected abstract float getMaximumHealth();
	
	protected abstract float getKnockbackResistance();
	
	protected abstract double getWanderSpeed();
	
	protected abstract double getAttackDamage();
	
	protected abstract int getVitalityGel();
	
	@Override
	public boolean attackEntityAsMob(Entity entityIn)
	{
		boolean flag = entityIn.attackEntityFrom(DamageSource.causeMobDamage(this), (float) this.getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).getAttributeValue());
		return flag;
	}
	
	@Override
	protected void onDeathUpdate() 
	{
		super.onDeathUpdate();
		if(this.deathTime == 20 && !this.worldObj.isRemote)
		{
			for(GristAmount gristType : this.getGristSpoils().getArray())
				this.worldObj.spawnEntityInWorld(new EntityGrist(worldObj, this.posX + this.rand.nextDouble() * this.width - this.width / 2, this.posY, this.posZ + this.rand.nextDouble() * this.width - this.width / 2, gristType));
			if(this.rand.nextInt(4) == 0)
				this.worldObj.spawnEntityInWorld(new EntityVitalityGel(worldObj, this.posX + this.rand.nextDouble() * this.width - this.width / 2, this.posY, this.posZ + this.rand.nextDouble() * this.width - this.width / 2, this.getVitalityGel()));
		}
	}
	@Override
	public String getTexture() 
	{
		if(type == null)
			return "textures/mobs/" + GristType.Shale.getName() + '_' + underlingName + ".png";
		return "textures/mobs/" + type.getName() + '_' + underlingName + ".png";
	}
	
	@Override
	public String getName() 
	{
		if(type != null)
			return I18n.translateToLocalFormatted("entity.minestuck." + underlingName + ".type", type.getDisplayName());
		else return I18n.translateToFallback("entity.minestuck." + underlingName + ".name");
	}
	
	@Override
	public void setAttackTarget(EntityLivingBase par1EntityLivingBase) 
	{
		super.setAttackTarget(par1EntityLivingBase);
		if(par1EntityLivingBase != null)
		{
			this.addEnemy(par1EntityLivingBase.getClass());
		}
	}
	public void setEnemies()
	{
		attackEntitySelector = new EntityListFilter(enemyClasses);
	}

	public void addEnemy(Class<? extends EntityLivingBase> enemyClass)
	{
		if(!enemyClasses.contains(enemyClass))
		{
			enemyClasses.add(enemyClass);
			this.setEnemies();
			this.setCombatTask();
		}
	}
	EntityAINearestAttackableTargetWithHeight entityAINearestAttackableTargetWithHeight()
	{
		return new EntityAINearestAttackableTargetWithHeight(this, EntityLivingBase.class, 128.0F, 2, true, false, attackEntitySelector);
	}
	
	@Override
	public void writeEntityToNBT(NBTTagCompound tagCompound) 
	{
		super.writeEntityToNBT(tagCompound);
		tagCompound.setString("Type", this.type.getName());
		if(hasHome())
		{
			NBTTagCompound nbt = new NBTTagCompound();
			BlockPos home = getHomePosition();
			nbt.setInteger("homeX", home.getX());
			nbt.setInteger("homeY", home.getY());
			nbt.setInteger("homeZ", home.getZ());
			nbt.setInteger("maxHomeDistance", (int) getMaximumHomeDistance());
			tagCompound.setTag("homePos", nbt);
		}
	}
	@Override
	public void readEntityFromNBT(NBTTagCompound tagCompound) 
	{
		if(tagCompound.hasKey("Type", 8))
			applyGristType(GristType.getTypeFromString(tagCompound.getString("Type")), false);
		else applyGristType(SburbHandler.getUnderlingType(this), true);
		super.readEntityFromNBT(tagCompound);
		
		if(tagCompound.hasKey("homePos", 10))
		{
			NBTTagCompound nbt = tagCompound.getCompoundTag("homePos");
			BlockPos pos = new BlockPos(nbt.getInteger("homeX"), nbt.getInteger("homeY"), nbt.getInteger("homeZ"));
			setHomePosAndDistance(pos, nbt.getInteger("maxHomeDistance"));
		}
	}
	
	@Override
	public boolean getCanSpawnHere()
	{
		return this.worldObj.getDifficulty() != EnumDifficulty.PEACEFUL && super.getCanSpawnHere();
	}
	
	@Override
	public void writeSpawnData(ByteBuf buffer)
	{
		buffer.writeInt(type.ordinal());
	}
	
	@Override
	public void readSpawnData(ByteBuf additionalData)
	{
		applyGristType(GristType.values()[additionalData.readInt()], false);
		this.textureResource = new ResourceLocation("minestuck", this.getTexture());
	}
	
	@Override
	public IEntityLivingData onInitialSpawn(DifficultyInstance difficulty, IEntityLivingData livingData)
	{
		
		if(!(livingData instanceof UnderlingData))
		{
			if(this.type == null)
				applyGristType(SburbHandler.getUnderlingType(this), true);
			livingData = new UnderlingData(this.type);
		} else
		{
			applyGristType(((UnderlingData)livingData).type, true);
		}
		
		return super.onInitialSpawn(difficulty, livingData);
	}
	
	@Override
	protected boolean canDespawn()
	{
		return !this.hasHome();
	}
	
	@Override
	protected void damageEntity(DamageSource damageSrc, float damageAmount)
	{
		if (!this.isEntityInvulnerable(damageSrc))
		{
			damageAmount = net.minecraftforge.common.ForgeHooks.onLivingHurt(this, damageSrc, damageAmount);
			if (damageAmount <= 0) return;
			damageAmount = this.applyArmorCalculations(damageSrc, damageAmount);
			damageAmount = this.applyPotionDamageCalculations(damageSrc, damageAmount);
			float f1 = damageAmount;
			damageAmount = Math.max(damageAmount - this.getAbsorptionAmount(), 0.0F);
			this.setAbsorptionAmount(this.getAbsorptionAmount() - (f1 - damageAmount));
			
			EntityPlayerMP player = null;
			if(damageSrc.getEntity() instanceof EntityPlayerMP)
				player = (EntityPlayerMP) damageSrc.getEntity();
			if(damageMap.containsKey(player))
					damageMap.put(player, damageMap.get(player) + f1);
			else damageMap.put(player, (double) f1);
			
			if (damageAmount != 0.0F)
			{
				float f2 = this.getHealth();
				this.setHealth(f2 - damageAmount);
				this.getCombatTracker().trackDamage(damageSrc, f2, damageAmount);
				this.setAbsorptionAmount(this.getAbsorptionAmount() - damageAmount);
			}
		}
	}
	
	protected void computePlayerProgress(int progress)
	{
		double totalDamage = 0;
		for(Double i : damageMap.values())
			totalDamage += i;
		if(totalDamage < this.getMaxHealth())
			totalDamage = this.getMaxHealth();
		
		int maxProgress = (int) (progress*maxSharedProgress);
		damageMap.remove(null);
		EntityPlayerMP[] playerList = damageMap.keySet().toArray(new EntityPlayerMP[damageMap.size()]);
		double[] modifiers = new double[playerList.length];
		double totalModifier = 0;
		
		for(int i = 0; i < playerList.length; i++)
		{
			double f = damageMap.get(playerList[i])/totalDamage;
			modifiers[i] = 2*f - f*f;
			totalModifier += modifiers[i];
		}
		
		if(totalModifier > maxSharedProgress)
			for(int i = 0; i < playerList.length; i++)
				Echeladder.increaseProgress(playerList[i], (int) (maxProgress*modifiers[i]/totalModifier));
		else
			for(int i = 0; i < playerList.length; i++)
				Echeladder.increaseProgress(playerList[i], (int) (progress*modifiers[i]));
	}
	
	protected class UnderlingData implements IEntityLivingData
	{
		public final GristType type;
		public UnderlingData(GristType type)
		{
			this.type = type;
		}
	}
	
}
