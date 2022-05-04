package com.mraof.minestuck.entity;

import com.mraof.minestuck.item.MSItems;
import com.mraof.minestuck.util.MSSoundEvents;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.attributes.AttributeModifierMap;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.ai.controller.JumpController;
import net.minecraft.entity.ai.controller.MovementController;
import net.minecraft.entity.ai.goal.*;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.pathfinding.Path;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;

import java.util.Random;

public class FrogEntity extends CreatureEntity
{
	private static final double BASE_HEALTH = 5;
	private static final double BASE_SPEED = 0.3D;
	
	private int jumpTicks;
	private int jumpDuration;
	private boolean wasOnGround;
	private int currentMoveTypeDuration;
	private static final DataParameter<Float> FROG_SIZE = EntityDataManager.defineId(FrogEntity.class, DataSerializers.FLOAT);
	private static final DataParameter<Integer> SKIN_COLOR = EntityDataManager.defineId(FrogEntity.class, DataSerializers.INT);
	private static final DataParameter<Integer> EYE_COLOR = EntityDataManager.defineId(FrogEntity.class, DataSerializers.INT);
	private static final DataParameter<Integer> BELLY_COLOR = EntityDataManager.defineId(FrogEntity.class, DataSerializers.INT);
	private static final DataParameter<Integer> EYE_TYPE = EntityDataManager.defineId(FrogEntity.class, DataSerializers.INT);
	private static final DataParameter<Integer> BELLY_TYPE = EntityDataManager.defineId(FrogEntity.class, DataSerializers.INT);
	private static final DataParameter<Integer> TYPE = EntityDataManager.defineId(FrogEntity.class, DataSerializers.INT);
	
	public FrogEntity(World world)
	{
		this(MSEntityTypes.FROG, world);
	}
	
	public FrogEntity(EntityType<? extends FrogEntity> type, World world)
	{
		super(type, world);
		this.jumpControl = new JumpHelperController(this);
		this.moveControl = new MoveHelperController(this);
		this.setMovementSpeed(0.0D);
	}
	
	@Override
	protected void defineSynchedData()
	{
		super.defineSynchedData();
		this.entityData.define(TYPE, getRandomFrogType());
		this.entityData.define(FROG_SIZE, randomFloat(1)+0.6F);
		this.entityData.define(SKIN_COLOR, random(16777215));
		this.entityData.define(EYE_COLOR, random(16777215));
		this.entityData.define(BELLY_COLOR, random(16777215));
		this.entityData.define(EYE_TYPE, random(maxEyes()));
		this.entityData.define(BELLY_TYPE, random(maxBelly()));
	}
	
	@Override
	protected ActionResultType mobInteract(PlayerEntity player, Hand hand)
	{
		ItemStack itemstack = player.getItemInHand(hand);
		
		if(player.distanceToSqr(this) < 9.0D && !this.level.isClientSide)
		{
			if(itemstack.getItem() == MSItems.BUG_NET)
			{
				itemstack.hurtAndBreak(1, player, (entityPlayer) -> entityPlayer.broadcastBreakEvent(hand));
				ItemStack frogItem = new ItemStack(MSItems.FROG);
				
				frogItem.setTag(getFrogData());
				if(this.hasCustomName())frogItem.setHoverName(this.getCustomName());
				
				spawnAtLocation(frogItem, 0);
				this.remove();
			}
			else if(itemstack.getItem() == MSItems.GOLDEN_GRASSHOPPER && this.getFrogType() != 5)
			{
				if(!player.isCreative())itemstack.shrink(1);
				
				this.level.addParticle(ParticleTypes.EXPLOSION, this.getX(), this.getY() + (double)(this.getBbHeight() / 2.0F), this.getZ(), 0.0D, 0.0D, 0.0D);
				this.playSound(SoundEvents.ANVIL_HIT, this.getSoundVolume(), ((this.random.nextFloat() - this.random.nextFloat()) * 0.2F + 1.0F) * 0.8F);
				this.setType(5);
			}
		}
		return super.mobInteract(player, hand);
	}
	
	protected CompoundNBT getFrogData()
	{
		CompoundNBT compound = new CompoundNBT();
		
		compound.putInt("Type", this.getFrogType());
		compound.putFloat("Size", this.getFrogSize()+0.4f);
		compound.putInt("SkinColor", this.getSkinColor());
		compound.putInt("EyeColor", this.getEyeColor());
		compound.putInt("BellyColor", this.getBellyColor());
		compound.putInt("EyeType", this.getEyeType());
		compound.putInt("BellyType", this.getBellyType());
		
		return compound;
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
	
	@Override
	public EntitySize getDimensions(Pose poseIn)
	{
		return super.getDimensions(poseIn).scale((this.getFrogType() == 6) ? 0.6F :this.getFrogSize());
	}
	
	public static AttributeModifierMap.MutableAttribute frogAttributes()
	{
		return MobEntity.createMobAttributes().add(Attributes.MAX_HEALTH, BASE_HEALTH);
	}
	
	//Entity AI
	
	@Override
	protected void registerGoals()
	{
		
		this.goalSelector.addGoal(1, new SwimGoal(this));
		this.goalSelector.addGoal(1, new PanicGoal(this, 2.2D));
		this.goalSelector.addGoal(3, new TemptGoal(this, 1.0D, Ingredient.of(MSItems.CONE_OF_FLIES, MSItems.BUG_ON_A_STICK, MSItems.GRASSHOPPER, MSItems.JAR_OF_BUGS), false));	//TODO use bug item tag
		this.goalSelector.addGoal(6, new WaterAvoidingRandomWalkingGoal(this, 0.6D));
		this.goalSelector.addGoal(11, new LookAtGoal(this, PlayerEntity.class, 10.0F));
		
	}
	
	@Override
	protected float getJumpPower()
	{
		if (!this.horizontalCollision && (!this.moveControl.hasWanted() || this.moveControl.getWantedY() <= this.getY() + 0.5D))
		{
			Path path = this.navigation.getPath();

			if (path != null && path.getNextNodeIndex() < path.getNodeCount())
			{
				Vector3d vec3d = path.getNextEntityPos(this);

				if (vec3d.y > this.getY() + 0.5D)
				{
					return 0.5F;
				}
			}

			return this.moveControl.getSpeedModifier() <= 0.6D ? 0.2F : 0.3F;
		}
		else
		{
			return 0.5F;
		}
	}

	@Override
	protected void jumpFromGround()
	{
		super.jumpFromGround();
		double d0 = this.moveControl.getSpeedModifier();

		if (d0 > 0.0D)
		{
			double d1 = getHorizontalDistanceSqr(this.getDeltaMovement());

			if (d1 < 0.01D)
			{
				this.moveRelative(0.1F, new Vector3d(0.0F, 0.0F, 1.0F));
			}
		}

		if (!this.level.isClientSide)
		{
			this.level.broadcastEntityEvent(this, (byte)1);
		}
	}
	
	public void handleEntityEvent(byte id)
	{
		if (id == 1)
		{
			this.jumpDuration = 10;
			this.jumpTicks = 0;
		}
		else
		{
			super.handleEntityEvent(id);
		}
	}
	

	public float setJumpCompletion(float p_175521_1_)
	{
		return this.jumpDuration == 0 ? 0.0F : ((float)this.jumpTicks + p_175521_1_) / (float)this.jumpDuration;
	}

	public void setMovementSpeed(double newSpeed)
	{
		this.getNavigation().setSpeedModifier(newSpeed);
		this.moveControl.setWantedPosition(this.moveControl.getWantedX(), this.moveControl.getWantedY(), this.moveControl.getWantedZ(), newSpeed);
	}

	public void setJumping(boolean jumping)
	{
		super.setJumping(jumping);

		if (jumping)
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
		if (this.currentMoveTypeDuration > 0)
		{
			--this.currentMoveTypeDuration;
		}

		if (this.onGround)
		{
			if (!this.wasOnGround)
			{
				this.setJumping(false);
				this.checkLandingDelay();
			}


			JumpHelperController jumpHelper = (JumpHelperController)this.jumpControl;

			if (!jumpHelper.getIsJumping())
			{
				if (this.moveControl.hasWanted() && this.currentMoveTypeDuration == 0)
				{
					Path path = this.navigation.getPath();
					Vector3d vec3d = new Vector3d(this.moveControl.getWantedX(), this.moveControl.getWantedY(), this.moveControl.getWantedZ());

					if (path != null && path.getNextNodeIndex() < path.getNodeCount())
					{
						vec3d = path.getNextEntityPos(this);
					}

					this.calculateRotationYaw(vec3d.x, vec3d.z);
					this.startJumping();
				}
			}
			else if (!jumpHelper.canJump())
			{
				this.enableJumpControl();
			}
		}

		this.wasOnGround = this.onGround;
	}
	
	private void calculateRotationYaw(double x, double z)
	{
		this.yRot = (float)(MathHelper.atan2(z - this.getZ(), x - this.getX()) * (180D / Math.PI)) - 90.0F;
	}

	private void enableJumpControl()
	{
		((JumpHelperController)this.jumpControl).setCanJump(true);
	}

	private void disableJumpControl()
	{
		((JumpHelperController) this.jumpControl).setCanJump(false);
	}

	private void updateMoveTypeDuration()
	{
		if (this.moveControl.getSpeedModifier() < 2.2D)
		{
			this.currentMoveTypeDuration = 10;
		}
		else
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

		if (this.jumpTicks != this.jumpDuration)
		{
			++this.jumpTicks;
		}
		else if (this.jumpDuration != 0)
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
		return MSSoundEvents.ENTITY_FROG_AMBIENT;
	}
	
	@Override
	protected SoundEvent getHurtSound(DamageSource damageSourceIn)
	{
		return MSSoundEvents.ENTITY_FROG_HURT;
	}
	
	@Override
	protected SoundEvent getDeathSound()
	{
		return MSSoundEvents.ENTITY_FROG_DEATH;
	}
	
	protected SoundEvent getJumpSound()
	{
		return SoundEvents.RABBIT_JUMP;
	}
	
	@Override
	protected float getVoicePitch()
	{
		return (this.random.nextFloat() - this.random.nextFloat()) / (this.getFrogSize()+0.4f) * 0.2F + 1.0F;
	}
	
	//NBT
	@Override
	public void addAdditionalSaveData(CompoundNBT compound)
	{
		super.addAdditionalSaveData(compound);
		compound.putInt("Type", this.getFrogType());
		if(getFrogType() != 6) compound.putFloat("Size", this.getFrogSize()+0.4f);
		else compound.putFloat("Size", 0.6f);
		compound.putInt("SkinColor", this.getSkinColor());
		compound.putInt("EyeColor", this.getEyeColor());
		compound.putInt("BellyColor", this.getBellyColor());
		compound.putInt("EyeType", this.getEyeType());
		compound.putInt("BellyType", this.getBellyType());
		compound.putBoolean("WasOnGround", this.wasOnGround);
	}
	
	@Override
	public void readAdditionalSaveData(CompoundNBT compound)
	{
		super.readAdditionalSaveData(compound);
		
		if(compound.contains("Type")) setType(compound.getInt("Type"));
		else setType(getRandomFrogType());
		
		if(compound.contains("Size") && getFrogType() != 6)
		{
			float i = compound.getFloat("Size");
			if (i <= 0.2f) i = 0.2f;
			this.setFrogSize(i-0.4f, false);
		}
		else this.setFrogSize(0.6f, false);
		if(compound.contains("SkinColor"))
		{
			if (compound.getInt("SkinColor") == 0)
				this.setSkinColor(0);
			
			else this.setSkinColor(compound.getInt("SkinColor"));
		}
		else this.setSkinColor(random(16777215));

		if (compound.contains("EyeColor"))
		{
			if (compound.getInt("EyeColor") == 0)
				this.setEyeColor(0);
			
			else this.setEyeColor(compound.getInt("EyeColor"));
		}
		else this.setEyeColor(random(16777215));
		
		if (compound.contains("EyeType"))
		{
			if (compound.getInt("EyeType") == 0)
				this.setEyeType(0);
			
			else this.setEyeType(compound.getInt("EyeType"));
		}
		else this.setEyeType(random(2));
		

		if (compound.contains("BellyColor"))
		{
			if (compound.getInt("BellyColor") == 0)
				this.setBellyColor(0);
			
			else this.setBellyColor(compound.getInt("BellyColor"));
		}
		else this.setBellyColor(random(16777215));
		
		if (compound.contains("BellyType"))
		{
			if (compound.getInt("BellyType") == 0)
				this.setBellyType(0);
			
			else this.setBellyType(compound.getInt("BellyType"));
		}
		else this.setBellyType(random(3));
		
		
		this.wasOnGround = compound.getBoolean("WasOnGround");
	}
	
	@Override
	public void onSyncedDataUpdated(DataParameter<?> key)
	{
		refreshDimensions();
		if (FROG_SIZE.equals(key))
		{
			this.yRot = this.yHeadRot;
			this.yBodyRot = this.yHeadRot;

			if (this.isInWater() && this.random.nextInt(20) == 0)
			{
				this.doWaterSplashEffect();
			}
		}

		super.onSyncedDataUpdated(key);
	}
	

	private void setSkinColor(int i) 
	{
		this.entityData.set(SKIN_COLOR, i);
	}

	public int getSkinColor() 
	{
		return this.entityData.get(SKIN_COLOR);
	}
	
	private void setEyeColor(int i)
	{
		this.entityData.set(EYE_COLOR, i);
	}
	
	public int getEyeColor() 
	{
		return this.entityData.get(EYE_COLOR);
	}

	private void setBellyColor(int i)
	{
		this.entityData.set(BELLY_COLOR, i);
	}
	
	public int getBellyColor() 
	{
		return this.entityData.get(BELLY_COLOR);
	}
	

	private void setEyeType(int i)
	{
		this.entityData.set(EYE_TYPE, i);
	}
	
	public int getEyeType() 
	{
		return this.entityData.get(EYE_TYPE);
	}

	private void setBellyType(int i)
	{
		this.entityData.set(BELLY_TYPE, i);
	}
	
	public int getBellyType() 
	{
		return this.entityData.get(BELLY_TYPE);
	}
	

	protected void setFrogSize(float size, boolean p_70799_2_)
	{
		if(this.entityData.get(TYPE) == 6) this.entityData.set(FROG_SIZE, 0.6f);
		else this.entityData.set(FROG_SIZE, size);
		this.setPos(this.getX(), this.getY(), this.getZ());
		this.getAttribute(Attributes.MAX_HEALTH).setBaseValue((double)(BASE_HEALTH * size));
		this.getAttribute(Attributes.MOVEMENT_SPEED).setBaseValue(BASE_SPEED * size);

		if (p_70799_2_)
		{
			this.setHealth(this.getMaxHealth());
		}

		this.xpReward = (int)size;
	}
	
	public float getFrogSize()
	{
		return this.entityData.get(FROG_SIZE);
	}
	
	private void setType(int i)
	{
		this.entityData.set(TYPE, i);
	}
	
	public int getFrogType()
	{
		return this.entityData.get(TYPE);
	}
	
	
	public int random(int max)
	{
		Random rand = new Random();
		return rand.nextInt(max+1);
	}
	
	public float randomFloat(int max)
	{
		Random rand = new Random();
		return (float)(rand.nextInt(max*10))/10;
	}
	
	public int getRandomFrogType(int chance1, int chance2, int chance3)
	{
		Random rand = new Random();
		int newType;
		
		if(rand.nextInt(chance1) == 1) 
		{
			newType = 1;
		}
		else if(rand.nextInt(chance2) == 1) 
		{
			newType = 2;
		}
		else if(rand.nextInt(chance3) == 1) 
		{
			newType = 6;
		}
		else newType = 0;
		
		return newType;
	}
	
	public int getRandomFrogType()
	{
		return getRandomFrogType(20, 50, 500);
	}
	
	public static class JumpHelperController extends JumpController
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
			if (this.jump)
			{
				this.frog.startJumping();
				this.jump = false;
			}
		}
	}

	static class MoveHelperController extends MovementController
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
			if(this.frog.onGround && !this.frog.jumping && !((JumpHelperController) this.frog.jumpControl).getIsJumping())
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
	
	public static boolean canFrogSpawnOn(EntityType<FrogEntity> entityType, IWorld world, SpawnReason reason, BlockPos pos, Random random)
	{
		return true;
	}
}