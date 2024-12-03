package com.mraof.minestuck.entity;

import com.mojang.datafixers.util.Pair;
import com.mraof.minestuck.Minestuck;
import com.mraof.minestuck.item.MSItems;
import com.mraof.minestuck.item.components.FrogTraitsComponent;
import com.mraof.minestuck.item.components.MSItemComponents;
import com.mraof.minestuck.util.MSSoundEvents;
import com.mraof.minestuck.util.MSTags;
import net.minecraft.core.BlockPos;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializer;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.control.JumpControl;
import net.minecraft.world.entity.ai.control.MoveControl;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.pathfinder.Path;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.network.codec.NeoForgeStreamCodecs;
import org.jetbrains.annotations.Nullable;

import java.util.Random;

public class FrogEntity extends PathfinderMob
{
	private static final double BASE_HEALTH = 5;
	private static final double BASE_SPEED = 0.3D;
	
	private int jumpTicks;
	private int jumpDuration;
	private boolean wasOnGround;
	private int currentMoveTypeDuration;
	private static final EntityDataAccessor<Integer> SKIN_COLOR = SynchedEntityData.defineId(FrogEntity.class, EntityDataSerializers.INT);
	private static final EntityDataAccessor<Integer> EYE_COLOR = SynchedEntityData.defineId(FrogEntity.class, EntityDataSerializers.INT);
	private static final EntityDataAccessor<Integer> BELLY_COLOR = SynchedEntityData.defineId(FrogEntity.class, EntityDataSerializers.INT);
	private static final EntityDataAccessor<EyeTypes> EYE_TYPE = SynchedEntityData.defineId(FrogEntity.class, EntityDataSerializer.forValueType(NeoForgeStreamCodecs.enumCodec(EyeTypes.class)));
	private static final EntityDataAccessor<BellyTypes> BELLY_TYPE = SynchedEntityData.defineId(FrogEntity.class, EntityDataSerializer.forValueType(NeoForgeStreamCodecs.enumCodec(BellyTypes.class)));
	private static final EntityDataAccessor<FrogVariants> VARIANT = SynchedEntityData.defineId(FrogEntity.class, EntityDataSerializer.forValueType(NeoForgeStreamCodecs.enumCodec(FrogVariants.class)));
	
	private static final ResourceLocation GENETIC_SIZE_ATTRIBUTE_KEY = Minestuck.id("genetic_size");
	
	public FrogEntity(Level level)
	{
		this(MSEntityTypes.FROG.get(), level);
	}
	
	public FrogEntity(EntityType<? extends FrogEntity> type, Level level)
	{
		super(type, level);
		this.jumpControl = new JumpHelperController(this);
		this.moveControl = new MoveHelperController(this);
		this.setMovementSpeed(0.0D);
	}
	
	@Override
	protected void defineSynchedData(SynchedEntityData.Builder builder)
	{
		super.defineSynchedData(builder);
		builder.define(VARIANT, FrogVariants.DEFAULT);
		builder.define(SKIN_COLOR, random(34277));
		builder.define(EYE_COLOR, random(15967496));
		builder.define(BELLY_COLOR, random(28350));
		builder.define(EYE_TYPE, EyeTypes.LIGHT);
		builder.define(BELLY_TYPE, BellyTypes.SOLID);
	}
	
	@Nullable
	@Override
	public SpawnGroupData finalizeSpawn(ServerLevelAccessor pLevel, DifficultyInstance pDifficulty, MobSpawnType pSpawnType, @Nullable SpawnGroupData pSpawnGroupData)
	{
		return super.finalizeSpawn(pLevel, pDifficulty, pSpawnType, pSpawnGroupData);
	}
	
	@Override
	protected InteractionResult mobInteract(Player player, InteractionHand hand)
	{
		ItemStack itemstack = player.getItemInHand(hand);
		
		if(player.distanceToSqr(this) < 9.0D && !this.level().isClientSide)
		{
			if(itemstack.getItem() == MSItems.BUG_NET.get())
			{
				itemstack.hurtAndBreak(1, player, hand == InteractionHand.MAIN_HAND ? EquipmentSlot.MAINHAND : EquipmentSlot.OFFHAND);
				ItemStack frogItem = new ItemStack(MSItems.FROG.get());
				
				frogItem.set(MSItemComponents.FROG_TRAITS, FrogTraitsComponent.fromFrogEntity(this));
				if(this.hasCustomName())
					frogItem.set(DataComponents.CUSTOM_NAME, this.getCustomName());
				
				spawnAtLocation(frogItem, 0);
				this.discard();
			} else if(itemstack.getItem() == MSItems.GOLDEN_GRASSHOPPER.get() && this.getFrogVariant() != FrogVariants.GOLDEN)
			{
				if(!player.isCreative()) itemstack.shrink(1);
				
				this.level().addParticle(ParticleTypes.EXPLOSION, this.getX(), this.getY() + (double) (this.getBbHeight() / 2.0F), this.getZ(), 0.0D, 0.0D, 0.0D);
				this.playSound(SoundEvents.ANVIL_HIT, this.getSoundVolume(), ((this.random.nextFloat() - this.random.nextFloat()) * 0.2F + 1.0F) * 0.8F);
				this.setFrogVariant(FrogVariants.GOLDEN);
			}
		}
		return super.mobInteract(player, hand);
	}
	
	public static int maxTypes()
	{
		return 6;
	}
	
	public static int maxEyes()
	{
		return 2;
	}
	
	public static int maxBelly()
	{
		return 3;
	}
	
	public enum FrogVariants implements StringRepresentable
	{
		DEFAULT("default"),
		TOTALLY_NORMAL("totally_normal"),
		RUBY_CONTRABAND("ruby_contraband"),
		GENESIS("genesis"),
		NULL("null"),
		GOLDEN("golden"),
		SUSAN("susan");
		
		private final String name;
		
		FrogVariants(String name)
		{
			this.name = name;
		}
		
		@Override
		public String getSerializedName()
		{
			return name;
		}
	}
	
	public enum EyeTypes implements StringRepresentable
	{
		LIGHT("light"),
		DARK("dark"),
		BLANK("blank");
		
		private final String name;
		
		EyeTypes(String name)
		{
			this.name = name;
		}
		
		@Override
		public String getSerializedName()
		{
			return name;
		}
	}
	
	public enum BellyTypes implements StringRepresentable
	{
		NONE("none"),
		SOLID("solid"),
		SPOTTED("spotted"),
		STRIPED("striped");
		
		private final String name;
		
		BellyTypes(String name)
		{
			this.name = name;
		}
		
		@Override
		public String getSerializedName()
		{
			return name;
		}
	}
	
	public static AttributeSupplier.Builder frogAttributes()
	{
		return Mob.createMobAttributes().add(Attributes.MAX_HEALTH, BASE_HEALTH);
	}
	
	//Entity AI
	
	@Override
	protected void registerGoals()
	{
		
		this.goalSelector.addGoal(1, new FloatGoal(this));
		this.goalSelector.addGoal(1, new PanicGoal(this, 2.2D));
		this.goalSelector.addGoal(3, new TemptGoal(this, 1.0D, Ingredient.of(MSTags.Items.BUGS), false));
		this.goalSelector.addGoal(6, new WaterAvoidingRandomStrollGoal(this, 0.6D));
		this.goalSelector.addGoal(11, new LookAtPlayerGoal(this, Player.class, 10.0F));
		
	}
	
	@Override
	protected float getJumpPower()
	{
		if(!this.horizontalCollision && (!this.moveControl.hasWanted() || this.moveControl.getWantedY() <= this.getY() + 0.5D))
		{
			Path path = this.navigation.getPath();
			
			if(path != null && path.getNextNodeIndex() < path.getNodeCount())
			{
				Vec3 vec3d = path.getNextEntityPos(this);
				
				if(vec3d.y > this.getY() + 0.5D)
				{
					return 0.5F;
				}
			}
			
			return this.moveControl.getSpeedModifier() <= 0.6D ? 0.2F : 0.3F;
		} else
		{
			return 0.5F;
		}
	}
	
	@Override
	public void jumpFromGround()
	{
		super.jumpFromGround();
		double d0 = this.moveControl.getSpeedModifier();
		
		if(d0 > 0.0D)
		{
			double d1 = this.getDeltaMovement().horizontalDistanceSqr();
			
			if(d1 < 0.01D)
			{
				this.moveRelative(0.1F, new Vec3(0.0F, 0.0F, 1.0F));
			}
		}
		
		if(!this.level().isClientSide)
		{
			this.level().broadcastEntityEvent(this, (byte) 1);
		}
	}
	
	public void handleEntityEvent(byte id)
	{
		if(id == 1)
		{
			this.jumpDuration = 10;
			this.jumpTicks = 0;
		} else
		{
			super.handleEntityEvent(id);
		}
	}
	
	
	public float setJumpCompletion(float p_175521_1_)
	{
		return this.jumpDuration == 0 ? 0.0F : ((float) this.jumpTicks + p_175521_1_) / (float) this.jumpDuration;
	}
	
	public void setMovementSpeed(double newSpeed)
	{
		this.getNavigation().setSpeedModifier(newSpeed);
		this.moveControl.setWantedPosition(this.moveControl.getWantedX(), this.moveControl.getWantedY(), this.moveControl.getWantedZ(), newSpeed);
	}
	
	public void setJumping(boolean jumping)
	{
		super.setJumping(jumping);
		
		if(jumping)
		{
			this.playSound(this.getJumpSound(), this.getSoundVolume(), ((this.random.nextFloat() - this.random.nextFloat()) * 0.2F + 1.0F) * 0.8F);
		}
	}
	
	public void startJumping()
	{
		this.setJumping(true);
		this.jumpDuration = 10;
		this.jumpTicks = 0;
	}
	
	public void customServerAiStep()
	{
		if(this.currentMoveTypeDuration > 0)
		{
			--this.currentMoveTypeDuration;
		}
		
		if(this.onGround())
		{
			if(!this.wasOnGround)
			{
				this.setJumping(false);
				this.checkLandingDelay();
			}
			
			
			JumpHelperController jumpHelper = (JumpHelperController) this.jumpControl;
			
			if(!jumpHelper.getIsJumping())
			{
				if(this.moveControl.hasWanted() && this.currentMoveTypeDuration == 0)
				{
					Path path = this.navigation.getPath();
					Vec3 vec3d = new Vec3(this.moveControl.getWantedX(), this.moveControl.getWantedY(), this.moveControl.getWantedZ());
					
					if(path != null && path.getNextNodeIndex() < path.getNodeCount())
					{
						vec3d = path.getNextEntityPos(this);
					}
					
					this.calculateRotationYaw(vec3d.x, vec3d.z);
					this.startJumping();
				}
			} else if(!jumpHelper.canJump())
			{
				this.enableJumpControl();
			}
		}
		
		this.wasOnGround = this.onGround();
	}
	
	private void calculateRotationYaw(double x, double z)
	{
		this.setYRot((float) (Mth.atan2(z - this.getZ(), x - this.getX()) * (180D / Math.PI)) - 90.0F);
	}
	
	private void enableJumpControl()
	{
		((JumpHelperController) this.jumpControl).setCanJump(true);
	}
	
	private void disableJumpControl()
	{
		((JumpHelperController) this.jumpControl).setCanJump(false);
	}
	
	private void updateMoveTypeDuration()
	{
		if(this.moveControl.getSpeedModifier() < 2.2D)
		{
			this.currentMoveTypeDuration = 10;
		} else
		{
			this.currentMoveTypeDuration = 1;
		}
	}
	
	private void checkLandingDelay()
	{
		this.updateMoveTypeDuration();
		this.disableJumpControl();
	}
	
	@Override
	public void aiStep()
	{
		super.aiStep();
		
		if(this.jumpTicks != this.jumpDuration)
		{
			++this.jumpTicks;
		} else if(this.jumpDuration != 0)
		{
			this.jumpTicks = 0;
			this.jumpDuration = 0;
			this.setJumping(false);
		}
	}
	
	//Frog Sounds
	@Override
	protected SoundEvent getAmbientSound()
	{
		return MSSoundEvents.ENTITY_FROG_AMBIENT.get();
	}
	
	@Override
	protected SoundEvent getHurtSound(DamageSource damageSourceIn)
	{
		return MSSoundEvents.ENTITY_FROG_HURT.get();
	}
	
	@Override
	protected SoundEvent getDeathSound()
	{
		return MSSoundEvents.ENTITY_FROG_DEATH.get();
	}
	
	protected SoundEvent getJumpSound()
	{
		return SoundEvents.RABBIT_JUMP;
	}
	
	@Override
	public float getVoicePitch()
	{
		return (this.random.nextFloat() - this.random.nextFloat()) / (this.getScale() + 0.4f) * 0.2F + 1.0F;
	}
	
	@Override
	protected EntityDimensions getDefaultDimensions(Pose pPose)
	{
		return super.getDefaultDimensions(pPose);
	}
	
	//NBT
	@Override
	public void addAdditionalSaveData(CompoundTag compound)
	{
		super.addAdditionalSaveData(compound);
		compound.putString("Variant", this.getFrogVariant().getSerializedName());
		compound.putInt("SkinColor", this.getSkinColor());
		compound.putInt("EyeColor", this.getEyeColor());
		compound.putInt("BellyColor", this.getBellyColor());
		compound.putString("EyeType", this.getEyeType().getSerializedName());
		compound.putString("BellyType", this.getBellyType().getSerializedName());
		compound.putBoolean("WasOnGround", this.wasOnGround);
	}
	
	@Override
	public void readAdditionalSaveData(CompoundTag compound)
	{
		super.readAdditionalSaveData(compound);
		
		if(compound.contains("Variant", Tag.TAG_STRING))
			setFrogVariant(FrogVariants.valueOf(compound.getString("Type")));
		if(compound.contains("EyeType", Tag.TAG_STRING))
			setEyeType(EyeTypes.valueOf(compound.getString("EyeType")));
		if(compound.contains("BellyType", Tag.TAG_STRING))
			setBellyType(BellyTypes.valueOf(compound.getString("BellyType")));
		
		if(compound.contains("SkinColor", Tag.TAG_INT))
			this.setSkinColor(Math.clamp(compound.getInt("SkinColor"), 0, 0xFFFFFF));
		if(compound.contains("EyeColor", Tag.TAG_INT))
			this.setEyeColor(Math.clamp(compound.getInt("EyeColor"), 0, 0xFFFFFF));
		if(compound.contains("BellyColor", Tag.TAG_INT))
			this.setBellyColor(Math.clamp(compound.getInt("BellyColor"), 0, 0xFFFFFF));
		
		this.wasOnGround = compound.getBoolean("WasOnGround");
	}
	
	public void setSkinColor(int i)
	{
		this.entityData.set(SKIN_COLOR, i);
	}
	
	public int getSkinColor()
	{
		return this.entityData.get(SKIN_COLOR);
	}
	
	public void setEyeColor(int i)
	{
		this.entityData.set(EYE_COLOR, i);
	}
	
	public int getEyeColor()
	{
		return this.entityData.get(EYE_COLOR);
	}
	
	public void setBellyColor(int i)
	{
		this.entityData.set(BELLY_COLOR, i);
	}
	
	public int getBellyColor()
	{
		return this.entityData.get(BELLY_COLOR);
	}
	
	
	public void setEyeType(EyeTypes i)
	{
		this.entityData.set(EYE_TYPE, i);
	}
	
	public EyeTypes getEyeType()
	{
		return this.entityData.get(EYE_TYPE);
	}
	
	public void setBellyType(BellyTypes i)
	{
		this.entityData.set(BELLY_TYPE, i);
	}
	
	public BellyTypes getBellyType()
	{
		return this.entityData.get(BELLY_TYPE);
	}
	
	public void setFrogVariant(FrogVariants i)
	{
		this.entityData.set(VARIANT, i);
	}
	
	public FrogVariants getFrogVariant()
	{
		return this.entityData.get(VARIANT);
	}
	
	public void setFrogSize(double size, boolean regenHealth)
	{
		this.setPos(this.getX(), this.getY(), this.getZ());
		this.getAttribute(Attributes.SCALE).setBaseValue(size);
		this.getAttribute(Attributes.MAX_HEALTH).setBaseValue(BASE_HEALTH * size);
		this.getAttribute(Attributes.MOVEMENT_SPEED).setBaseValue(BASE_SPEED * size);
		
		if(regenHealth)
			this.setHealth(this.getMaxHealth());
		
		this.xpReward = (int) size;
	}
	
	public double getFrogSize()
	{
		return getAttribute(Attributes.SCALE).getBaseValue();
	}
	
	public int random(int max)
	{
		Random rand = new Random();
		return rand.nextInt(max + 1);
	}
	
	public float randomFloat(int max)
	{
		Random rand = new Random();
		return (float) (rand.nextInt(max * 10)) / 10;
	}
	
	public FrogVariants getRandomFrogVariant(Pair<FrogVariants, Float>... odds)
	{
		for(Pair<FrogVariants, Float> odd : odds)
		{
			if(random.nextFloat() <= odd.getSecond())
				return odd.getFirst();
		}
		
		return FrogVariants.DEFAULT;
	}
	
	public FrogVariants getRandomFrogVariant()
	{
		return getRandomFrogVariant(
				new Pair<>(FrogVariants.TOTALLY_NORMAL, 0.05f),
				new Pair<>(FrogVariants.RUBY_CONTRABAND, 0.02f),
				new Pair<>(FrogVariants.SUSAN, 0.002f)
		);
	}
	
	public static class JumpHelperController extends JumpControl
	{
		private final FrogEntity frog;
		private boolean canJump;
		
		public JumpHelperController(FrogEntity frog)
		{
			super(frog);
			this.frog = frog;
		}
		
		public boolean getIsJumping()
		{
			return this.jump;
		}
		
		public boolean canJump()
		{
			return this.canJump;
		}
		
		public void setCanJump(boolean canJumpIn)
		{
			this.canJump = canJumpIn;
		}
		
		@Override
		public void tick()
		{
			if(this.jump)
			{
				this.frog.startJumping();
				this.jump = false;
			}
		}
	}
	
	static class MoveHelperController extends MoveControl
	{
		private final FrogEntity frog;
		private double nextJumpSpeed;
		
		public MoveHelperController(FrogEntity frog)
		{
			super(frog);
			this.frog = frog;
		}
		
		@Override
		public void tick()
		{
			if(this.frog.onGround() && !this.frog.jumping && !((JumpHelperController) this.frog.jumpControl).getIsJumping())
			{
				this.frog.setMovementSpeed(0.0D);
			} else if(this.hasWanted())
			{
				this.frog.setMovementSpeed(this.nextJumpSpeed);
			}
			
			super.tick();
		}
		
		@Override
		public void setWantedPosition(double x, double y, double z, double speedIn)
		{
			if(this.frog.isInWater())
			{
				speedIn = 1.5D;
			}
			
			super.setWantedPosition(x, y, z, speedIn);
			
			if(speedIn > 0.0D)
			{
				this.nextJumpSpeed = speedIn;
			}
		}
	}
	
	@Override
	public boolean removeWhenFarAway(double distanceToClosestPlayer)
	{
		return false;
	}
	
	public static boolean canFrogSpawnOn(EntityType<FrogEntity> entityType, LevelAccessor world, MobSpawnType reason, BlockPos pos, RandomSource random)
	{
		return true;
	}
}
