package com.mraof.minestuck.entity.underling;

import com.mraof.minestuck.alchemy.*;
import com.mraof.minestuck.entity.EntityListFilter;
import com.mraof.minestuck.entity.ai.HurtByTargetAlliedGoal;
import com.mraof.minestuck.entity.item.GristEntity;
import com.mraof.minestuck.entity.item.VitalityGelEntity;
import com.mraof.minestuck.player.Echeladder;
import com.mraof.minestuck.player.IdentifierHandler;
import com.mraof.minestuck.player.PlayerIdentifier;
import com.mraof.minestuck.skaianet.UnderlingController;
import com.mraof.minestuck.util.MSTags;
import com.mraof.minestuck.player.PlayerSavedData;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.Difficulty;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.monster.Enemy;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraftforge.common.util.FakePlayer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.*;

public abstract class UnderlingEntity extends PathfinderMob implements Enemy
{
	private static final Logger LOGGER = LogManager.getLogger();
	
	public static final UUID GRIST_MODIFIER_ID = UUID.fromString("08B6DEFC-E3F4-11EA-87D0-0242AC130003");
	private static final EntityDataAccessor<String> GRIST_TYPE = SynchedEntityData.defineId(UnderlingEntity.class, EntityDataSerializers.STRING);
	protected final EntityListFilter attackEntitySelector = new EntityListFilter(new ArrayList<>());    //TODO this filter isn't being saved. F1X PLZ
	protected boolean fromSpawner;
	public boolean dropCandy;
	private int consortRep;
	
	private static final float maxSharedProgress = 2;    //The multiplier for the maximum amount progress that can be gathered from each enemy with the group fight bonus
	
	protected Map<PlayerIdentifier, Double> damageMap = new HashMap<>();    //Map that stores how much damage each player did to this to this underling. Null is used for environmental or other non-player damage
	
	public UnderlingEntity(EntityType<? extends UnderlingEntity> type, Level level, int consortRep)
	{
		super(type, level);
		this.consortRep = consortRep;
		
		attackEntitySelector.entityList.add(EntityType.PLAYER);
	}
	
	@Override
	protected void registerGoals()
	{
		
		goalSelector.addGoal(1, new FloatGoal(this));
		goalSelector.addGoal(4, new MoveTowardsRestrictionGoal(this, 0.8D));
		goalSelector.addGoal(5, new RandomStrollGoal(this, 0.6D));
		goalSelector.addGoal(6, new LookAtPlayerGoal(this, Player.class, 8.0F));
		goalSelector.addGoal(7, new RandomLookAroundGoal(this));
		
		targetSelector.addGoal(1, new HurtByTargetAlliedGoal(this, entity -> entity.getType().is(MSTags.EntityTypes.UNDERLINGS)));
		targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, LivingEntity.class, 2, true, false, this::isAppropriateTarget));
	}
	
	protected boolean isAppropriateTarget(LivingEntity entity)
	{
		return attackEntitySelector.isEntityApplicable(entity);
	}
	
	public static AttributeSupplier.Builder underlingAttributes()
	{
		return Mob.createMobAttributes().add(Attributes.ATTACK_DAMAGE).add(Attributes.FOLLOW_RANGE, 32);
	}
	
	@Override
	public SoundSource getSoundSource()
	{
		return SoundSource.HOSTILE;
	}
	
	@Override
	public float getVoicePitch()
	{
		return getGristType() == GristTypes.ARTIFACT.get()
				? (this.random.nextFloat() - this.random.nextFloat()) * 0.2F + 0.7F
				: (this.random.nextFloat() - this.random.nextFloat()) * 0.2F + 1.0F;
	}
	
	@Override
	protected void defineSynchedData()
	{
		super.defineSynchedData();
		entityData.define(GRIST_TYPE, String.valueOf(GristTypes.ARTIFACT.get().getRegistryName()));
	}
	
	protected void applyGristType(GristType type)
	{
		if(!type.isUnderlingType())    //Utility grist type
			throw new IllegalArgumentException("Can't set underling grist type to " + type.getRegistryName());
		entityData.set(GRIST_TYPE, String.valueOf(type.getRegistryName()));
		
		onGristTypeUpdated(type);
		setHealth(getMaxHealth());
	}
	
	@Override
	public void onSyncedDataUpdated(EntityDataAccessor<?> parameter)
	{
		if(parameter == GRIST_TYPE)
			onGristTypeUpdated(getGristType());
	}
	
	protected void onGristTypeUpdated(GristType type)
	{
	}
	
	protected void applyGristModifier(Attribute attribute, double modifier, AttributeModifier.Operation operation)
	{
		getAttribute(attribute).removeModifier(GRIST_MODIFIER_ID);
		//Does not need to be saved because this bonus should already be applied when the grist type has been set
		getAttribute(attribute).addTransientModifier(new AttributeModifier(GRIST_MODIFIER_ID, "Grist Bonus", modifier, operation));
	}
	
	@Nonnull
	public GristType getGristType()
	{
		GristType type = GristTypes.getRegistry().getValue(ResourceLocation.tryParse(entityData.get(GRIST_TYPE)));
		
		if(type != null)
		{
			return type;
		} else
			LOGGER.warn("Unable to read underling grist type from string {}.", entityData.get(GRIST_TYPE));
		
		return GristTypes.ARTIFACT.get();
	}
	
	//used when getting how much grist should be dropped on death
	public abstract GristSet getGristSpoils();
	
	protected abstract int getVitalityGel();
	
	@Override
	public boolean doHurtTarget(Entity entityIn)
	{
		return entityIn.hurt(DamageSource.mobAttack(this), (float) this.getAttribute(Attributes.ATTACK_DAMAGE).getValue());
	}
	
	@Override
	protected void tickDeath()
	{
		super.tickDeath();
		if(this.deathTime == 20 && !this.level.isClientSide)
		{
			GristSet grist = this.getGristSpoils();
			if(grist == null)
				return;
			if(fromSpawner)
				grist.scale(0.5F, false);
			
			if(!dropCandy)
			{
				for(GristAmount gristAmount : grist.getAmounts())
				{
					if(gristAmount.getAmount() > 0)
						this.level.addFreshEntity(new GristEntity(level, randX(), this.getY(), randZ(), gristAmount));
				}
			} else
			{
				for(GristAmount gristType : grist.getAmounts())
				{
					int candy = (int) Math.min(64, (gristType.getAmount() + 2) / 4);
					long gristAmount = gristType.getAmount() - candy * 2;
					ItemStack candyItem = gristType.getType().getCandyItem();
					candyItem.setCount(candy);
					if(candy > 0)
						this.level.addFreshEntity(new ItemEntity(level, randX(), this.getY(), randZ(), candyItem));
					if(gristAmount > 0)
						this.level.addFreshEntity(new GristEntity(level, randX(), this.getY(), randZ(), new GristAmount(gristType.getType(), gristAmount)));
				}
			}
			
			if(this.random.nextInt(4) == 0)
				this.level.addFreshEntity(new VitalityGelEntity(level, randX(), this.getY(), randZ(), this.getVitalityGel()));
		}
	}
	
	@Override
	public void die(DamageSource cause)
	{
		LivingEntity entity = this.getKillCredit();
		if(entity instanceof ServerPlayer player && (!(player instanceof FakePlayer)))
			PlayerSavedData.getData(player).addConsortReputation(consortRep, level.dimension());
		
		super.die(cause);
	}
	
	private double randX()
	{
		return this.getX() + this.random.nextDouble() * this.getBbWidth() - this.getBbWidth() / 2;
	}
	
	private double randZ()
	{
		return this.getZ() + this.random.nextDouble() * this.getBbWidth() - this.getBbWidth() / 2;
	}
	
	@Override
	public Component getName()
	{
		if(getCustomName() == null)
			return new TranslatableComponent(getType().getDescriptionId() + ".type", getGristType().getDisplayName());
		else return super.getName();
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
	
	public void addEnemy(EntityType<?> enemyType)
	{
		if(!attackEntitySelector.entityList.contains(enemyType) && !enemyType.is(MSTags.EntityTypes.UNDERLINGS))
		{
			attackEntitySelector.entityList.add(enemyType);
		}
	}
	
	@Override
	public void addAdditionalSaveData(CompoundTag compound)
	{
		super.addAdditionalSaveData(compound);
		getGristType().write(compound, "Type");
		compound.putBoolean("Spawned", fromSpawner);
		if(hasRestriction())
		{
			CompoundTag nbt = new CompoundTag();
			BlockPos home = getRestrictCenter();
			nbt.putInt("HomeX", home.getX());
			nbt.putInt("HomeY", home.getY());
			nbt.putInt("HomeZ", home.getZ());
			nbt.putInt("MaxHomeDistance", (int) getRestrictRadius());
			compound.put("HomePos", nbt);
		}
	}
	
	@Override
	public void readAdditionalSaveData(CompoundTag compound)
	{
		//Note: grist type should be read and applied before reading health due to the modifiers to max health
		if(compound.contains("Type", Tag.TAG_STRING))
			applyGristType(GristType.read(compound, "Type", GristTypes.ARTIFACT));
		else applyGristType(GristHelper.getPrimaryGrist(this.getRandom()));
		
		super.readAdditionalSaveData(compound);
		
		fromSpawner = compound.getBoolean("Spawned");
		
		if(compound.contains("HomePos", Tag.TAG_COMPOUND))
		{
			CompoundTag nbt = compound.getCompound("HomePos");
			BlockPos pos = new BlockPos(nbt.getInt("HomeX"), nbt.getInt("HomeY"), nbt.getInt("HomeZ"));
			restrictTo(pos, nbt.getInt("MaxHomeDistance"));
		}
	}
	
	public static boolean canSpawnOnAndNotPeaceful(EntityType<? extends Mob> type, LevelAccessor worldIn, MobSpawnType reason, BlockPos pos, Random randomIn)
	{
		return worldIn.getDifficulty() != Difficulty.PEACEFUL && checkMobSpawnRules(type, worldIn, reason, pos, randomIn);
	}
	
	@Nullable
	@Override
	public SpawnGroupData finalizeSpawn(ServerLevelAccessor worldIn, DifficultyInstance difficultyIn, MobSpawnType reason, @Nullable SpawnGroupData spawnDataIn, @Nullable CompoundTag dataTag)
	{
		if(!(spawnDataIn instanceof UnderlingData))
		{
			applyGristType(UnderlingController.getUnderlingType(this));
			spawnDataIn = new UnderlingData(getGristType());
		} else
		{
			applyGristType(((UnderlingData) spawnDataIn).type);
		}
		
		return super.finalizeSpawn(worldIn, difficultyIn, reason, spawnDataIn, dataTag);
	}
	
	public void onEntityDamaged(DamageSource source, float amount)
	{
		PlayerIdentifier playerId = null;
		if(source.getEntity() instanceof ServerPlayer player)
			playerId = IdentifierHandler.encode(player);
		if(damageMap.containsKey(playerId))
			damageMap.put(playerId, damageMap.get(playerId) + amount);
		else damageMap.put(playerId, (double) amount);
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
		
		int maxProgress = (int) (progress * maxSharedProgress);
		damageMap.remove(null);
		PlayerIdentifier[] playerList = damageMap.keySet().toArray(new PlayerIdentifier[0]);
		double[] modifiers = new double[playerList.length];
		double totalModifier = 0;
		
		for(int i = 0; i < playerList.length; i++)
		{
			double f = damageMap.get(playerList[i]) / totalDamage;
			modifiers[i] = 2 * f - f * f;
			totalModifier += modifiers[i];
		}
		
		if(playerList.length > 0)
			LOGGER.debug("{} players are splitting on {} progress from {}", playerList.length, progress, getType().getRegistryName());
		
		if(totalModifier > maxSharedProgress)
			for(int i = 0; i < playerList.length; i++)
				Echeladder.increaseProgress(playerList[i], level, (int) (maxProgress * modifiers[i] / totalModifier));
		else
			for(int i = 0; i < playerList.length; i++)
				Echeladder.increaseProgress(playerList[i], level, (int) (progress * modifiers[i]));
	}
	
	protected static void firstKillBonus(Entity killer, byte type)
	{
		if(killer instanceof ServerPlayer && (!(killer instanceof FakePlayer)))
		{
			Echeladder ladder = PlayerSavedData.getData((ServerPlayer) killer).getEcheladder();
			ladder.checkBonus(type);
		}
	}
	
	protected static class UnderlingData implements SpawnGroupData
	{
		public final GristType type;
		
		public UnderlingData(GristType type)
		{
			this.type = type;
		}
	}
	
}
