package com.mraof.minestuck.entity.underling;

import com.mraof.minestuck.item.crafting.alchemy.GristAmount;
import com.mraof.minestuck.item.crafting.alchemy.GristSet;
import com.mraof.minestuck.item.crafting.alchemy.GristType;
import com.mraof.minestuck.entity.EntityListFilter;
import com.mraof.minestuck.entity.MinestuckEntity;
import com.mraof.minestuck.entity.ai.HurtByTargetAlliedGoal;
import com.mraof.minestuck.entity.ai.NearestAttackableTargetWithHeightGoal;
import com.mraof.minestuck.entity.item.GristEntity;
import com.mraof.minestuck.entity.item.VitalityGelEntity;
import com.mraof.minestuck.network.skaianet.SburbHandler;
import com.mraof.minestuck.util.*;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.goal.*;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.monster.IMob;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.DamageSource;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.Difficulty;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.*;

public abstract class UnderlingEntity extends MinestuckEntity implements IMob
{
	private static final DataParameter<String> GRIST_TYPE = EntityDataManager.createKey(UnderlingEntity.class, DataSerializers.STRING);
	protected EntityListFilter attackEntitySelector;	//TODO this filter isn't being saved. F1X PLZ
	protected boolean fromSpawner;
	public boolean dropCandy;
	
	private static final float maxSharedProgress = 2;	//The multiplier for the maximum amount progress that can be gathered from each enemy with the group fight bonus
	
	protected Map<ServerPlayerEntity, Double> damageMap = new HashMap<>();	//Map that stores how much damage each player did to this to this underling. Null is used for environmental or other non-player damage
	
	public UnderlingEntity(EntityType<? extends UnderlingEntity> type, World world)
	{
		super(type, world);
	}
	
	@Override
	protected void registerGoals()
	{
		attackEntitySelector = new EntityListFilter(new ArrayList<>());
		attackEntitySelector.entityList.add(EntityType.PLAYER);
		
		goalSelector.addGoal(1, new SwimGoal(this));
		goalSelector.addGoal(4, new MoveTowardsRestrictionGoal(this, getWanderSpeed()));
		goalSelector.addGoal(5, new RandomWalkingGoal(this, getWanderSpeed()));
		goalSelector.addGoal(6, new LookAtGoal(this, PlayerEntity.class, 8.0F));
		goalSelector.addGoal(7, new LookRandomlyGoal(this));
		
		targetSelector.addGoal(1, new HurtByTargetAlliedGoal(this, entity -> MSTags.EntityTypes.UNDERLINGS.contains(entity.getType())));
		targetSelector.addGoal(2, new NearestAttackableTargetWithHeightGoal(this, LivingEntity.class, 128.0F, 2, true, false, attackEntitySelector));
	}
	
	@Override
	protected void registerAttributes()
	{
		super.registerAttributes();
		this.getAttributes().registerAttribute(SharedMonsterAttributes.ATTACK_DAMAGE);
		
		this.getAttribute(SharedMonsterAttributes.KNOCKBACK_RESISTANCE).setBaseValue(this.getKnockbackResistance());
		this.getAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(this.getWanderSpeed());
	}
	
	@Override
	protected void registerData()
	{
		super.registerData();
		dataManager.register(GRIST_TYPE, String.valueOf(GristType.ARTIFACT.getRegistryName()));
	}
	
	protected void applyGristType(GristType type, boolean fullHeal)
	{
		if(type.getRarity() == 0)	//Utility grist type
			throw new IllegalArgumentException("Can't set underling grist type to "+type.getRegistryName());
		dataManager.set(GRIST_TYPE, String.valueOf(type.getRegistryName()));
		
		onGristTypeUpdated(type);
		if(fullHeal)
			this.setHealth(this.getMaxHealth());
	}
	
	@Override
	public void notifyDataManagerChange(DataParameter<?> parameter)
	{
		if(parameter == GRIST_TYPE)
			onGristTypeUpdated(getGristType());
	}
	
	protected void onGristTypeUpdated(GristType type)
	{
		clearTexture();
		this.getAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(this.getMaximumHealth());
		this.getAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).setBaseValue(this.getAttackDamage());
	}
	
	@Nonnull
	public GristType getGristType()
	{
		GristType type = GristType.REGISTRY.getValue(ResourceLocation.tryCreate(dataManager.get(GRIST_TYPE)));
		
		if(type != null)
		{
			return type;
		} else Debug.warnf("Unable to read underling grist type from string %s.", dataManager.get(GRIST_TYPE));
		
		return GristType.ARTIFACT;
	}
	
	//used when getting how much grist should be dropped on death
	public abstract GristSet getGristSpoils();
	
	protected abstract float getKnockbackResistance();
	
	protected abstract double getWanderSpeed();
	
	protected abstract double getAttackDamage();
	
	protected abstract int getVitalityGel();
	
	@Override
	public boolean attackEntityAsMob(Entity entityIn)
	{
		return entityIn.attackEntityFrom(DamageSource.causeMobDamage(this), (float) this.getAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).getValue());
	}
	
	@Override
	protected void onDeathUpdate()
	{
		super.onDeathUpdate();
		if(this.deathTime == 20 && !this.world.isRemote)
		{
			GristSet grist = this.getGristSpoils();
			if(grist == null)
				return;
			if(fromSpawner)
				grist.scale(0.5F, false);
			
			if(!dropCandy)
			{
				for(GristAmount gristType : grist.getArray())
					this.world.addEntity(new GristEntity(world, randX(), this.posY, randZ(), gristType));
			} else
			{
				for(GristAmount gristType : grist.getArray())
				{
					int candy = (int) Math.min(64, (gristType.getAmount() + 2)/4);
					long gristAmount = gristType.getAmount() - candy*2;
					ItemStack candyItem = gristType.getType().getCandyItem();
					candyItem.setCount(candy);
					if(candy > 0)
						this.world.addEntity(new ItemEntity(world, randX(), this.posY, randZ(), candyItem));
					if(gristAmount > 0)
						this.world.addEntity(new GristEntity(world, randX(), this.posY, randZ(),new GristAmount(gristType.getType(), gristAmount)));
				}
			}
			
			if(this.rand.nextInt(4) == 0)
				this.world.addEntity(new VitalityGelEntity(world, randX(), this.posY, randZ(), this.getVitalityGel()));
		}
	}
	
	private double randX()
	{
		return this.posX + this.rand.nextDouble() * this.getWidth() - this.getWidth() / 2;
	}
	
	private double randZ()
	{
		return this.posZ + this.rand.nextDouble() * this.getWidth() - this.getWidth() / 2;
	}
	
	@Override
	protected ResourceLocation createTexture()
	{
		ResourceLocation underlingName = Objects.requireNonNull(getType().getRegistryName(), () -> "Getting texture for entity without a registry name! "+this);
		ResourceLocation gristName = getGristType().getEffectiveName();
		
		return new ResourceLocation(underlingName.getNamespace(), String.format("textures/entity/underlings/%s/%s_%s.png", gristName.getNamespace(), gristName.getPath(), underlingName.getPath()));
	}
	
	@Override
	public ITextComponent getName()
	{
		if(getCustomName() == null)
			return new TranslationTextComponent(getType().getTranslationKey() + ".type", getGristType().getDisplayName());
		else return super.getName();
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

	public void addEnemy(EntityType<?> enemyType)
	{
		if(!attackEntitySelector.entityList.contains(enemyType) && !MSTags.EntityTypes.UNDERLINGS.contains(enemyType))
		{
			attackEntitySelector.entityList.add(enemyType);
		}
	}
	
	@Override
	public void writeAdditional(CompoundNBT compound)
	{
		super.writeAdditional(compound);
		compound.putString("Type", getGristType().getRegistryName().toString());
		compound.putBoolean("Spawned", fromSpawner);
		if(detachHome())
		{
			CompoundNBT nbt = new CompoundNBT();
			BlockPos home = getHomePosition();
			nbt.putInt("HomeX", home.getX());
			nbt.putInt("HomeY", home.getY());
			nbt.putInt("HomeZ", home.getZ());
			nbt.putInt("MaxHomeDistance", (int) getMaximumHomeDistance());
			compound.put("HomePos", nbt);
		}
	}
	
	@Override
	public void readAdditional(CompoundNBT compound)
	{
		if(compound.contains("Type", 8))
			applyGristType(GristType.getTypeFromString(compound.getString("Type")), false);
		else applyGristType(SburbHandler.getUnderlingType(this), true);
		super.readAdditional(compound);
		
		fromSpawner = compound.getBoolean("Spawned");
		
		if(compound.contains("HomePos", 10))
		{
			CompoundNBT nbt = compound.getCompound("HomePos");
			BlockPos pos = new BlockPos(nbt.getInt("HomeX"), nbt.getInt("HomeY"), nbt.getInt("homeZ"));
			setHomePosAndDistance(pos, nbt.getInt("MaxHomeDistance"));
		}
	}
	
	@Override
	public boolean canSpawn(IWorld worldIn, SpawnReason spawnReasonIn)
	{
		return this.world.getDifficulty() != Difficulty.PEACEFUL && super.canSpawn(worldIn, spawnReasonIn);
	}
	
	@Nullable
	@Override
	public ILivingEntityData onInitialSpawn(IWorld worldIn, DifficultyInstance difficultyIn, SpawnReason reason, @Nullable ILivingEntityData spawnDataIn, @Nullable CompoundNBT dataTag)
	{
		if(!(spawnDataIn instanceof UnderlingData))
		{
			applyGristType(SburbHandler.getUnderlingType(this), true);
			spawnDataIn = new UnderlingData(getGristType());
		} else
		{
			applyGristType(((UnderlingData)spawnDataIn).type, true);
		}
		
		return super.onInitialSpawn(worldIn, difficultyIn, reason, spawnDataIn, dataTag);
	}
	
	public void onEntityDamaged(DamageSource source, float amount)
	{
		ServerPlayerEntity player = null;
		if(source.getTrueSource() instanceof ServerPlayerEntity)
			player = (ServerPlayerEntity) source.getTrueSource();
		if(damageMap.containsKey(player))
			damageMap.put(player, damageMap.get(player) + amount);
		else damageMap.put(player, (double) amount);
	}
	
	@Override
	public void baseTick()
	{
		super.baseTick();
		if(this.getHealth() > 0.0F)
			dropCandy = false;
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
		ServerPlayerEntity[] playerList = damageMap.keySet().toArray(new ServerPlayerEntity[0]);
		double[] modifiers = new double[playerList.length];
		double totalModifier = 0;
		
		for(int i = 0; i < playerList.length; i++)
		{
			double f = damageMap.get(playerList[i])/totalDamage;
			modifiers[i] = 2*f - f*f;
			totalModifier += modifiers[i];
		}
		
		Debug.infof("%s players are splitting on %s progress from %s", playerList.length, progress, getType().getRegistryName());
		if(totalModifier > maxSharedProgress)
			for(int i = 0; i < playerList.length; i++)
				Echeladder.increaseProgress(playerList[i], (int) (maxProgress*modifiers[i]/totalModifier));
		else
			for(int i = 0; i < playerList.length; i++)
				Echeladder.increaseProgress(playerList[i], (int) (progress*modifiers[i]));
	}
	
	protected static class UnderlingData implements ILivingEntityData
	{
		public final GristType type;
		public UnderlingData(GristType type)
		{
			this.type = type;
		}
	}
	
}
