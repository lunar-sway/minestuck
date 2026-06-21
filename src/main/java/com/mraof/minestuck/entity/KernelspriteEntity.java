package com.mraof.minestuck.entity;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.Dynamic;
import com.mraof.minestuck.Minestuck;
import com.mraof.minestuck.entity.ai.brain.MSActivity;
import com.mraof.minestuck.entity.ai.brain.MSMemoryModuleType;
import com.mraof.minestuck.entity.ai.brain.MSPoiTypes;
import com.mraof.minestuck.entity.ai.brain.MSSensorType;
import com.mraof.minestuck.entity.dialogue.DialogueComponent;
import com.mraof.minestuck.entity.dialogue.DialogueEntity;
import com.mraof.minestuck.player.IdentifierHandler;
import com.mraof.minestuck.player.PlayerData;
import com.mraof.minestuck.player.PlayerIdentifier;
import com.mraof.minestuck.util.MSAttachments;
import com.mraof.minestuck.util.MSSoundEvents;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.GlobalPos;
import net.minecraft.core.Holder;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.util.Mth;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.Brain;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.behavior.*;
import net.minecraft.world.entity.ai.control.MoveControl;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.memory.MemoryStatus;
import net.minecraft.world.entity.ai.sensing.Sensor;
import net.minecraft.world.entity.ai.sensing.SensorType;
import net.minecraft.world.entity.ai.village.poi.PoiType;
import net.minecraft.world.entity.animal.armadillo.Armadillo;
import net.minecraft.world.entity.animal.armadillo.ArmadilloAi;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.schedule.Activity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;

import javax.annotation.Nullable;
import java.util.EnumSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.BiPredicate;

@EventBusSubscriber(modid = Minestuck.MOD_ID, bus = EventBusSubscriber.Bus.GAME)
public class KernelspriteEntity extends PathfinderMob implements DialogueEntity
{
	private static final EntityDataAccessor<Integer> COLOR = SynchedEntityData.defineId(KernelspriteEntity.class, EntityDataSerializers.INT);
	
	private final DialogueComponent dialogueComponent = new DialogueComponent(this);
	private final RandomMoveGoal randomMoveGoal = new RandomMoveGoal(this);
	private final StayPutGoal stayPutGoal = new StayPutGoal();
	
	@Nullable
	private BlockPos boundOrigin;
	private int wanderRadius = 20;
	private PlayerIdentifier owner;
	
	private static final ImmutableList<MemoryModuleType<?>> MEMORY_TYPES = ImmutableList.of(MSMemoryModuleType.MACHINE_LOCATIONS.get());
	private static final ImmutableList<SensorType<? extends Sensor<? super KernelspriteEntity>>> SENSOR_TYPES = ImmutableList.of(MSSensorType.MACHINE_SENSOR.get());
	public static final Map<MemoryModuleType<List<GlobalPos>>, BiPredicate<KernelspriteEntity, Holder<PoiType>>> POI_MEMORIES = ImmutableMap.of(
			MSMemoryModuleType.MACHINE_LOCATIONS.get(),
			(entity, holder) -> holder.is(MSPoiTypes.ALCHEMITER_KEY)
	);
	
	public KernelspriteEntity(EntityType<? extends PathfinderMob> entityType, Level level)
	{
		super(entityType, level);
		setInvulnerable(true);
		setPersistenceRequired();
		
		this.moveControl = new KernelspriteMoveControl(this);
		setStayPutGoal(true);
		
		dialogueComponent.setDialogue(Minestuck.id("individual/kernelsprite/start"), true);
	}
	
	public static AttributeSupplier.Builder kernelspriteAttributes()
	{
		return Mob.createMobAttributes().add(Attributes.MAX_HEALTH, 80);
	}
	
	@Override
	protected MovementEmission getMovementEmission()
	{
		return Entity.MovementEmission.NONE;
	}
	
	@Override
	public boolean canBeLeashed()
	{
		return super.canBeLeashed();
	}
	
	@Override
	public void move(MoverType type, Vec3 pos)
	{
		super.move(type, pos);
		this.checkInsideBlocks();
	}
	
	@Override
	public void tick()
	{
		//using Vex as base
		noPhysics = true;
		super.tick();
		noPhysics = false;
		setNoGravity(true);
	}
	
	@Override
	protected void defineSynchedData(SynchedEntityData.Builder builder)
	{
		super.defineSynchedData(builder);
		builder.define(COLOR, ChatFormatting.BLACK.getColor());
	}
	
	@Override
	protected InteractionResult mobInteract(Player player, InteractionHand hand)
	{
		boolean isBusy = this.goalSelector.getAvailableGoals().stream()
				.filter(wrappedGoal -> wrappedGoal != null && wrappedGoal.isRunning())
				.anyMatch(goal -> goal.getGoal() instanceof MoveToBlockGoal);
		
		if(!this.isAlive() || player.isShiftKeyDown() || isBusy)
			return InteractionResult.PASS;
		
		if(!(player instanceof ServerPlayer serverPlayer))
			return InteractionResult.SUCCESS;
		
		if(this.dialogueComponent.hasAnyOngoingDialogue())
			return InteractionResult.FAIL;
		
		this.dialogueComponent.tryStartDialogue(serverPlayer);
		setStayPutGoal(true);
		
		return InteractionResult.SUCCESS;
	}
	
	@Override
	protected void registerGoals()
	{
		super.registerGoals();
		this.goalSelector.addGoal(0, new FloatGoal(this));
		this.goalSelector.addGoal(7, new RandomLookAroundGoal(this));
	}
	
	@Override
	public Brain<KernelspriteEntity> getBrain()
	{
		return (Brain<KernelspriteEntity>) super.getBrain();
	}
	
	@Override
	protected Brain.Provider<KernelspriteEntity> brainProvider()
	{
		return Brain.provider(MEMORY_TYPES, SENSOR_TYPES);
	}
	
	@Override
	protected Brain<KernelspriteEntity> makeBrain(Dynamic<?> dynamic)
	{
		Brain<KernelspriteEntity> brain = this.brainProvider().makeBrain(dynamic);
		this.registerBrainGoals(brain);
		return brain;
	}
	
	public void refreshBrain(ServerLevel serverLevel)
	{
		Brain<KernelspriteEntity> brain = this.getBrain();
		brain.stopAll(serverLevel, this);
		this.brain = brain.copyWithoutBehaviors();
		this.registerBrainGoals(this.getBrain());
	}
	
	private void registerBrainGoals(Brain<KernelspriteEntity> brain)
	{
		initCoreActivity(brain);
		initIdleActivity(brain);
		brain.setCoreActivities(Set.of(Activity.CORE));
		brain.setDefaultActivity(Activity.IDLE);
		brain.useDefaultActivity();
		
		/*brain.addActivityWithConditions(
				Activity.WORK,
				VillagerGoalPackages.getWorkPackage(villagerprofession, 1F),
				ImmutableSet.of(Pair.of(MSMemoryModuleType.MACHINE_LOCATIONS, MemoryStatus.VALUE_PRESENT))
		);*/
	}
	
	private static void initCoreActivity(Brain<KernelspriteEntity> brain) {
		brain.addActivity(
				Activity.CORE,
				0,
				ImmutableList.of(
						new Swim(0.8F),
						new ArmadilloAi.ArmadilloPanic(2.0F),
						new LookAtTargetSink(45, 90),
						new MoveToTargetSink() {
							@Override
							protected boolean checkExtraStartConditions(ServerLevel p_316506_, Mob p_316710_) {
								if (p_316710_ instanceof Armadillo armadillo && armadillo.isScared()) {
									return false;
								}
								
								return super.checkExtraStartConditions(p_316506_, p_316710_);
							}
						},
						new CountDownCooldownTicks(MemoryModuleType.TEMPTATION_COOLDOWN_TICKS),
						new CountDownCooldownTicks(MemoryModuleType.GAZE_COOLDOWN_TICKS),
						ARMADILLO_ROLLING_OUT
				)
		);
	}
	
	private static void initIdleActivity(Brain<KernelspriteEntity> brain) {
		brain.addActivity(
				Activity.IDLE,
				ImmutableList.of(
						Pair.of(0, SetEntityLookTargetSometimes.create(EntityType.PLAYER, 6.0F, UniformInt.of(30, 60))),
						Pair.of(1, new AnimalMakeLove(EntityType.ARMADILLO, 1.0F, 1)),
						Pair.of(
								2,
								new RunOne<>(
										ImmutableList.of(
												Pair.of(new FollowTemptation(p_316818_ -> 1.25F, p_319682_ -> p_319682_.isBaby() ? 1.0 : 2.0), 1),
												Pair.of(BabyFollowAdult.create(ADULT_FOLLOW_RANGE, 1.25F), 1)
										)
								)
						),
						Pair.of(3, new RandomLookAround(UniformInt.of(150, 250), 30.0F, 0.0F, 0.0F)),
						Pair.of(
								4,
								new RunOne<>(
										ImmutableMap.of(MemoryModuleType.WALK_TARGET, MemoryStatus.VALUE_ABSENT),
										ImmutableList.of(
												Pair.of(RandomStroll.stroll(1.0F), 1), Pair.of(SetWalkTargetFromLookTarget.create(1.0F, 3), 1), Pair.of(new DoNothing(30, 60), 1)
										)
								)
						)
				)
		);
	}
	
	public static void updateActivity(Armadillo armadillo) {
		armadillo.getBrain().setActiveActivityToFirstValid(ImmutableList.of(Activity.IDLE));
	}
	
	public void setRandomMoveGoal(boolean add)
	{
		if(add)
			this.goalSelector.addGoal(8, this.randomMoveGoal);
		else
			this.goalSelector.removeGoal(this.randomMoveGoal);
	}
	
	/**
	 * Handle stay put. Adding the goal will temporarily disable the random move goal
	 */
	public void setStayPutGoal(boolean add)
	{
		if(add)
			this.goalSelector.addGoal(7, stayPutGoal);
		else
			this.goalSelector.removeGoal(this.stayPutGoal);
	}
	
	@Override
	protected SoundEvent getAmbientSound()
	{
		return MSSoundEvents.ENTITY_KERNELSPRITE_AMBIENT.get();
	}
	
	@Override
	public float getLightLevelDependentMagicValue()
	{
		return 1.0F;
	}
	
	@Override
	public void addAdditionalSaveData(CompoundTag compound)
	{
		super.addAdditionalSaveData(compound);
		compound.putInt("color", this.getColor());
		owner.saveToNBT(compound, "owner");
		compound.put("dialogue", dialogueComponent.write());
		
		if(this.boundOrigin != null)
		{
			compound.putInt("BoundX", this.boundOrigin.getX());
			compound.putInt("BoundY", this.boundOrigin.getY());
			compound.putInt("BoundZ", this.boundOrigin.getZ());
		}
		compound.putInt("wander_radius", this.getWanderRadius());
	}
	
	@Override
	public void readAdditionalSaveData(CompoundTag compound)
	{
		super.readAdditionalSaveData(compound);
		this.setColor(compound.getInt("color"));
		this.owner = IdentifierHandler.load(compound, "owner").result().orElse(null);
		dialogueComponent.read(compound.getCompound("dialogue"));
		
		if(compound.contains("BoundX"))
		{
			this.boundOrigin = new BlockPos(compound.getInt("BoundX"), compound.getInt("BoundY"), compound.getInt("BoundZ"));
		}
		this.wanderRadius = compound.getInt("wander_radius");
	}
	
	@Nullable
	public BlockPos getBoundOrigin()
	{
		return this.boundOrigin;
	}
	
	public void setBoundOrigin(@Nullable BlockPos boundOrigin)
	{
		this.boundOrigin = boundOrigin;
	}
	
	public int getWanderRadius()
	{
		return this.wanderRadius;
	}
	
	public void setWanderRadius(boolean longLeash)
	{
		this.wanderRadius = longLeash ? 20 : 5;
	}
	
	public PlayerIdentifier getOwner()
	{
		return owner;
	}
	
	public void setOwner(PlayerIdentifier owner)
	{
		this.owner = owner;
	}
	
	public void setColor(int i)
	{
		this.entityData.set(COLOR, i);
	}
	
	public int getColor()
	{
		return this.entityData.get(COLOR);
	}
	
	@Override
	public DialogueComponent getDialogueComponent()
	{
		return dialogueComponent;
	}
	
	@Override
	public ChatFormatting getChatColor()
	{
		return ChatFormatting.BLACK;
	}
	
	@Override
	public String getSpriteType()
	{
		return this.getType().builtInRegistryHolder().getKey().location().getPath();
	}
	
	/**
	 * Based on {@link net.minecraft.world.entity.monster.Vex.VexMoveControl}
	 */
	static class KernelspriteMoveControl extends MoveControl
	{
		public KernelspriteMoveControl(KernelspriteEntity kernelsprite)
		{
			super(kernelsprite);
		}
		
		@Override
		public void tick()
		{
			if(this.operation == MoveControl.Operation.MOVE_TO)
			{
				Vec3 vec3 = new Vec3(wantedX - mob.getX(), this.wantedY - mob.getY(), this.wantedZ - mob.getZ());
				double d0 = vec3.length();
				if(d0 < mob.getBoundingBox().getSize())
				{
					this.operation = MoveControl.Operation.WAIT;
					mob.setDeltaMovement(mob.getDeltaMovement().scale(0.09)); //slows down much faster than Vex
				} else
				{
					mob.setDeltaMovement(mob.getDeltaMovement().add(vec3.scale(this.speedModifier * 0.05 / d0)));
					if(mob.getTarget() == null)
					{
						Vec3 vec31 = mob.getDeltaMovement();
						mob.setYRot(-((float) Mth.atan2(vec31.x, vec31.z)) * (180.0F / (float) Math.PI));
					} else
					{
						double d2 = mob.getTarget().getX() - mob.getX();
						double d1 = mob.getTarget().getZ() - mob.getZ();
						mob.setYRot(-((float) Mth.atan2(d2, d1)) * (180.0F / (float) Math.PI));
					}
					mob.yBodyRot = mob.getYRot();
				}
			}
		}
	}
	
	class StayPutGoal extends Goal
	{
		private int timer = -1;
		
		@Override
		public boolean canUse()
		{
			return timer > 0 || timer == -1;
		}
		
		@Override
		public void start()
		{
			super.start();
			timer = 600;
			KernelspriteEntity.this.setRandomMoveGoal(false);
		}
		
		@Override
		public void stop()
		{
			super.stop();
			timer = -1;
			KernelspriteEntity.this.setStayPutGoal(false);
			KernelspriteEntity.this.setRandomMoveGoal(true);
		}
		
		@Override
		public void tick()
		{
			super.tick();
			timer--;
			
			if(timer == 0 || timer % 100 != 0)
				return;
			
			lookForNonSuffocating();
		}
		
		private void lookForNonSuffocating()
		{
			Level level = KernelspriteEntity.this.level();
			BlockPos mobPos = KernelspriteEntity.this.blockPosition();
			if(!level.getBlockState(mobPos).blocksMotion())
				return;
			
			for(BlockPos iteratePos : BlockPos.betweenClosed(mobPos.offset(1, 1, 1), mobPos.offset(-1, -1, -1)))
			{
				if(KernelspriteEntity.this.isWithinRestriction(iteratePos) && !level.getBlockState(iteratePos).blocksMotion())
				{
					KernelspriteEntity.this.moveControl.setWantedPosition((double) iteratePos.getX() + 0.5, (double) iteratePos.getY() + 0.5, (double) iteratePos.getZ() + 0.5, 0.25);
					break;
				}
			}
		}
	}
	
	/**
	 * Built directly off {@link net.minecraft.world.entity.monster.Vex.VexRandomMoveGoal}
	 */
	static class RandomMoveGoal extends Goal
	{
		private final KernelspriteEntity entity;
		
		public RandomMoveGoal(KernelspriteEntity entity)
		{
			this.entity = entity;
			this.setFlags(EnumSet.of(Goal.Flag.MOVE));
		}
		
		@Override
		public boolean canUse()
		{
			return !entity.getMoveControl().hasWanted() && entity.random.nextInt(reducedTickDelay(30)) == 0;
		}
		
		@Override
		public boolean canContinueToUse()
		{
			return super.canContinueToUse() &&
					entity.goalSelector.getAvailableGoals().stream().filter(WrappedGoal::isRunning).noneMatch(goal -> goal.getGoal() instanceof MoveToBlockGoal);
		}
		
		@Override
		public void tick()
		{
			BlockPos blockpos = entity.getBoundOrigin();
			if(blockpos == null)
			{
				blockpos = entity.blockPosition();
			}
			
			for(int i = 0; i < 3; i++)
			{
				int radius = entity.getWanderRadius();
				int radiusOffset = radius - (radius / 2);
				BlockPos blockpos1 = blockpos.offset(
						entity.random.nextInt(radius) - radiusOffset,
						(int) (entity.random.nextInt((int) (radius * 0.6)) - (radiusOffset * 0.6)),
						entity.random.nextInt(radius) - radiusOffset);
				if(entity.level().isEmptyBlock(blockpos1))
				{
					entity.moveControl.setWantedPosition((double) blockpos1.getX() + 0.5, (double) blockpos1.getY() + 0.5, (double) blockpos1.getZ() + 0.5, 0.25);
					if(entity.getTarget() == null)
						entity.getLookControl().setLookAt((double) blockpos1.getX() + 0.5, (double) blockpos1.getY() + 0.5, (double) blockpos1.getZ() + 0.5, 180.0F, 20.0F);
					break;
				}
			}
		}
	}
	
	@SubscribeEvent
	private static void onPlayerLoggedIn(PlayerEvent.PlayerLoggedInEvent event)
	{
		ServerPlayer player = (ServerPlayer) event.getEntity();
		PlayerData playerData = PlayerData.get(player).orElseThrow();
		
		if(playerData.getExistingData(MSAttachments.HAS_KERNELSPRITE).isEmpty())
			playerData.setData(MSAttachments.HAS_KERNELSPRITE, false);
	}
}
