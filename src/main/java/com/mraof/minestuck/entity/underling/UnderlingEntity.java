package com.mraof.minestuck.entity.underling;

import com.mraof.minestuck.entity.EntityListFilter;
import com.mraof.minestuck.entity.MinestuckEntity;
import com.mraof.minestuck.entity.ai.HurtByTargetAlliedGoal;
import com.mraof.minestuck.entity.item.GristEntity;
import com.mraof.minestuck.entity.item.VitalityGelEntity;
import com.mraof.minestuck.item.crafting.alchemy.GristAmount;
import com.mraof.minestuck.item.crafting.alchemy.GristSet;
import com.mraof.minestuck.item.crafting.alchemy.GristType;
import com.mraof.minestuck.item.crafting.alchemy.GristTypes;
import com.mraof.minestuck.player.Echeladder;
import com.mraof.minestuck.player.IdentifierHandler;
import com.mraof.minestuck.player.PlayerIdentifier;
import com.mraof.minestuck.skaianet.UnderlingController;
import com.mraof.minestuck.util.Debug;
import com.mraof.minestuck.util.MSTags;
import com.mraof.minestuck.world.storage.PlayerSavedData;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.IAttribute;
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
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.Difficulty;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;
import net.minecraftforge.common.util.Constants;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.*;

public abstract class UnderlingEntity extends MinestuckEntity implements IMob
{
	public static final UUID GRIST_MODIFIER_ID = UUID.fromString("08B6DEFC-E3F4-11EA-87D0-0242AC130003");
	private static final DataParameter<String> GRIST_TYPE = EntityDataManager.createKey(UnderlingEntity.class, DataSerializers.STRING);
	protected EntityListFilter attackEntitySelector;	//TODO this filter isn't being saved. F1X PLZ
	protected boolean fromSpawner;
	public boolean dropCandy;
	private int consortRep;
	
	private static final float maxSharedProgress = 2;	//The multiplier for the maximum amount progress that can be gathered from each enemy with the group fight bonus
	
	protected Map<PlayerIdentifier, Double> damageMap = new HashMap<>();	//Map that stores how much damage each player did to this to this underling. Null is used for environmental or other non-player damage
	
	public UnderlingEntity(EntityType<? extends UnderlingEntity> type, World world, int consortRep)
	{
		super(type, world);
		this.consortRep = consortRep;
	}
	
	@Override
	protected void registerGoals()
	{
		attackEntitySelector = new EntityListFilter(new ArrayList<>());
		attackEntitySelector.entityList.add(EntityType.PLAYER);
		
		goalSelector.addGoal(1, new SwimGoal(this));
		goalSelector.addGoal(4, new MoveTowardsRestrictionGoal(this, 0.8D));
		goalSelector.addGoal(5, new RandomWalkingGoal(this, 0.6D));
		goalSelector.addGoal(6, new LookAtGoal(this, PlayerEntity.class, 8.0F));
		goalSelector.addGoal(7, new LookRandomlyGoal(this));
		
		targetSelector.addGoal(1, new HurtByTargetAlliedGoal(this, entity -> MSTags.EntityTypes.UNDERLINGS.contains(entity.getType())));
		targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, LivingEntity.class, 2, true, false, this::isAppropriateTarget));
	}
	
	protected boolean isAppropriateTarget(LivingEntity entity)
	{
		return attackEntitySelector.isEntityApplicable(entity);
	}
	
	@Override
	protected void registerAttributes()
	{
		super.registerAttributes();
		getAttributes().registerAttribute(SharedMonsterAttributes.ATTACK_DAMAGE);
		
		getAttribute(SharedMonsterAttributes.FOLLOW_RANGE).setBaseValue(32.0D);
	}
	
	@Override
	public SoundCategory getSoundCategory()
	{
		return SoundCategory.HOSTILE;
	}
	
	@Override
	protected void registerData()
	{
		super.registerData();
		dataManager.register(GRIST_TYPE, String.valueOf(GristTypes.ARTIFACT.get().getRegistryName()));
	}
	
	protected void applyGristType(GristType type)
	{
		if(!type.isUnderlingType())	//Utility grist type
			throw new IllegalArgumentException("Can't set underling grist type to "+type.getRegistryName());
		dataManager.set(GRIST_TYPE, String.valueOf(type.getRegistryName()));
		
		onGristTypeUpdated(type);
		setHealth(getMaxHealth());
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
	}
	
	protected void applyGristModifier(IAttribute attribute, double modifier, AttributeModifier.Operation operation)
	{
		getAttribute(attribute).removeModifier(GRIST_MODIFIER_ID);
		//Does not need to be saved because this bonus should already be applied when the grist type has been set
		getAttribute(attribute).applyModifier(new AttributeModifier(GRIST_MODIFIER_ID, "Grist Bonus", modifier, operation).setSaved(false));
	}
	
	@Nonnull
	public GristType getGristType()
	{
		GristType type = GristTypes.getRegistry().getValue(ResourceLocation.tryCreate(dataManager.get(GRIST_TYPE)));
		
		if(type != null)
		{
			return type;
		} else Debug.warnf("Unable to read underling grist type from string %s.", dataManager.get(GRIST_TYPE));
		
		return GristTypes.ARTIFACT.get();
	}
	
	//used when getting how much grist should be dropped on death
	public abstract GristSet getGristSpoils();
	
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
				for(GristAmount gristAmount : grist.getAmounts())
				{
					Random random = new Random();
					long gristLeft = gristAmount.getAmount();

					while(gristLeft > (gristAmount.getAmount() / (int) ((Math.random() * 10) + 1))) {
							if (gristLeft > 0) {
								this.world.addEntity(new GristEntity(world, randX(), this.getPosY(), randZ(), new GristAmount(gristAmount.getType(), (long) (gristLeft * 0.9))));
							}
							gristLeft = gristLeft - (long)((gristLeft*0.9)+1);
							System.out.println(gristLeft);
					}
					this.world.addEntity(new GristEntity(world, randX(), this.getPosY(), randZ(), new GristAmount(gristAmount.getType(), gristLeft)));
				}
			} else
			{
				for(GristAmount gristType : grist.getAmounts())
				{
					int candy = (int) Math.min(64, (gristType.getAmount() + 2)/4);
					long gristAmount = gristType.getAmount() - candy*2;
					ItemStack candyItem = gristType.getType().getCandyItem();
					candyItem.setCount(candy);
					if(candy > 0)
						this.world.addEntity(new ItemEntity(world, randX(), this.getPosY(), randZ(), candyItem));
					if(gristAmount > 0)
						this.world.addEntity(new GristEntity(world, randX(), this.getPosY(), randZ(),new GristAmount(gristType.getType(), gristAmount)));
				}
			}
			
			if(this.rand.nextInt(4) == 0)
				this.world.addEntity(new VitalityGelEntity(world, randX(), this.getPosY(), randZ(), this.getVitalityGel()));
		}
	}
	
	@Override
	public void onDeath(DamageSource cause)
	{
		LivingEntity entity = this.getAttackingEntity();
		if(entity instanceof ServerPlayerEntity)
			PlayerSavedData.getData((ServerPlayerEntity) entity).addConsortReputation(consortRep, dimension);
		
		super.onDeath(cause);
	}
	
	private double randX()
	{
		return this.getPosX() + this.rand.nextDouble() * this.getWidth() - this.getWidth() / 2;
	}
	
	private double randZ()
	{
		return this.getPosZ() + this.rand.nextDouble() * this.getWidth() - this.getWidth() / 2;
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
		getGristType().write(compound, "Type");
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
		//Note: grist type should be read and applied before reading health due to the modifiers to max health
		if(compound.contains("Type", Constants.NBT.TAG_STRING))
			applyGristType(GristType.read(compound, "Type", GristTypes.ARTIFACT));
		else applyGristType(UnderlingController.getUnderlingType(this));
		
		super.readAdditional(compound);
		
		fromSpawner = compound.getBoolean("Spawned");
		
		if(compound.contains("HomePos", Constants.NBT.TAG_COMPOUND))
		{
			CompoundNBT nbt = compound.getCompound("HomePos");
			BlockPos pos = new BlockPos(nbt.getInt("HomeX"), nbt.getInt("HomeY"), nbt.getInt("HomeZ"));
			setHomePosAndDistance(pos, nbt.getInt("MaxHomeDistance"));
		}
	}
	
	public static boolean canSpawnOnAndNotPeaceful(EntityType<? extends MobEntity> type, IWorld worldIn, SpawnReason reason, BlockPos pos, Random randomIn)
	{
		return worldIn.getDifficulty() != Difficulty.PEACEFUL && canSpawnOn(type, worldIn, reason, pos, randomIn);
	}
	
	@Nullable
	@Override
	public ILivingEntityData onInitialSpawn(IWorld worldIn, DifficultyInstance difficultyIn, SpawnReason reason, @Nullable ILivingEntityData spawnDataIn, @Nullable CompoundNBT dataTag)
	{
		if(!(spawnDataIn instanceof UnderlingData))
		{
			applyGristType(UnderlingController.getUnderlingType(this));
			spawnDataIn = new UnderlingData(getGristType());
		} else
		{
			applyGristType(((UnderlingData)spawnDataIn).type);
		}
		
		return super.onInitialSpawn(worldIn, difficultyIn, reason, spawnDataIn, dataTag);
	}
	
	public void onEntityDamaged(DamageSource source, float amount)
	{
		PlayerIdentifier player = null;
		if(source.getTrueSource() instanceof ServerPlayerEntity)
			player = IdentifierHandler.encode((ServerPlayerEntity) source.getTrueSource());
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
		PlayerIdentifier[] playerList = damageMap.keySet().toArray(new PlayerIdentifier[0]);
		double[] modifiers = new double[playerList.length];
		double totalModifier = 0;
		
		for(int i = 0; i < playerList.length; i++)
		{
			double f = damageMap.get(playerList[i])/totalDamage;
			modifiers[i] = 2*f - f*f;
			totalModifier += modifiers[i];
		}
		
		if(playerList.length > 0)
			Debug.debugf("%s players are splitting on %s progress from %s", playerList.length, progress, getType().getRegistryName());
		
		if(totalModifier > maxSharedProgress)
			for(int i = 0; i < playerList.length; i++)
				Echeladder.increaseProgress(playerList[i], world, (int) (maxProgress*modifiers[i]/totalModifier));
		else
			for(int i = 0; i < playerList.length; i++)
				Echeladder.increaseProgress(playerList[i], world, (int) (progress*modifiers[i]));
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
