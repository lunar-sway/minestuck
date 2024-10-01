package com.mraof.minestuck.entity.consort;

import com.mojang.logging.LogUtils;
import com.mraof.minestuck.MinestuckConfig;
import com.mraof.minestuck.advancements.MSCriteriaTriggers;
import com.mraof.minestuck.entity.AnimatedPathfinderMob;
import com.mraof.minestuck.entity.ai.AnimatedPanicGoal;
import com.mraof.minestuck.entity.animation.MobAnimation;
import com.mraof.minestuck.entity.dialogue.DialogueComponent;
import com.mraof.minestuck.entity.dialogue.DialogueEntity;
import com.mraof.minestuck.entity.dialogue.RandomlySelectableDialogue;
import com.mraof.minestuck.inventory.ConsortMerchantInventory;
import com.mraof.minestuck.inventory.ConsortMerchantMenu;
import com.mraof.minestuck.player.IdentifierHandler;
import com.mraof.minestuck.player.PlayerData;
import com.mraof.minestuck.player.PlayerIdentifier;
import com.mraof.minestuck.world.MSDimensions;
import net.minecraft.ChatFormatting;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.nbt.Tag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.ServerLevelAccessor;
import net.neoforged.neoforge.common.util.FakePlayer;
import org.slf4j.Logger;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animation.AnimationState;
import software.bernie.geckolib.core.animation.*;
import software.bernie.geckolib.core.object.PlayState;
import software.bernie.geckolib.util.GeckoLibUtil;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class ConsortEntity extends AnimatedPathfinderMob implements MenuProvider, GeoEntity, DialogueEntity
{
	private static final Logger LOGGER = LogUtils.getLogger();
	
	private static final MobAnimation TALK_PROPERTIES = new MobAnimation(MobAnimation.Action.TALK, 80, true, false); //TODO adjust as needed - 4 secs for now
	private static final MobAnimation PANIC_PROPERTIES = new MobAnimation(MobAnimation.Action.PANIC, MobAnimation.LOOPING_ANIMATION, false, false);
	private static final RawAnimation IDLE_ANIMATION = RawAnimation.begin().thenLoop("idle");
	private static final RawAnimation WALK_ANIMATION = RawAnimation.begin().thenLoop("walk");
	private static final RawAnimation ARMS_WALKING_ANIMATION = RawAnimation.begin().thenLoop("walkarms");
	private static final RawAnimation JUMP_ANIMATION = RawAnimation.begin().then("jump", Animation.LoopType.PLAY_ONCE);
	private static final RawAnimation PANIC_ANIMATION = RawAnimation.begin().thenLoop("panicrun");
	private static final RawAnimation TALK_ANIMATION = RawAnimation.begin().thenLoop("talk");
	private static final RawAnimation DIE_ANIMATION = RawAnimation.begin().then("die", Animation.LoopType.PLAY_ONCE);
	
	private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);
	private final DialogueComponent dialogueComponent = new DialogueComponent(this);
	
	private final EnumConsort consortType;
	private int ticksUntilDialogueReset = 0;
	private final Set<PlayerIdentifier> talkRepPlayerList = new HashSet<>();
	public EnumConsort.MerchantType merchantType = EnumConsort.MerchantType.NONE;
	ResourceKey<Level> homeDimension;
	private boolean visitedSkaia;
	public ConsortMerchantInventory stocks;
	private int eventTimer = -1;    //TODO use the interface mentioned in the todo above to implement consort explosion instead
	
	public ConsortEntity(EnumConsort consortType, EntityType<? extends ConsortEntity> type, Level level)
	{
		super(type, level);
		this.consortType = consortType;
		this.xpReward = 1;
	}
	
	
	public static AttributeSupplier.Builder consortAttributes()
	{
		return Mob.createMobAttributes().add(Attributes.MAX_HEALTH, 10).add(Attributes.MOVEMENT_SPEED, 0.35);
	}
	
	@Override
	protected void registerGoals()
	{
		goalSelector.addGoal(0, new FloatGoal(this));
		goalSelector.addGoal(1, new AnimatedPanicGoal(this, 1.4D, PANIC_PROPERTIES));
		goalSelector.addGoal(4, new MoveTowardsRestrictionGoal(this, 1F));
		goalSelector.addGoal(6, new LookAtPlayerGoal(this, Player.class, 8.0F));
		goalSelector.addGoal(7, new RandomLookAroundGoal(this));
		goalSelector.addGoal(4, new AvoidEntityGoal<>(this, Player.class, 16F, 1.0D, 1.4D, this::shouldFleeFrom));
	}
	
	@Override
	public AnimatableInstanceCache getAnimatableInstanceCache()
	{
		return this.cache;
	}
	
	private boolean shouldFleeFrom(LivingEntity entity)
	{
		return entity instanceof ServerPlayer player && EntitySelector.NO_CREATIVE_OR_SPECTATOR.test(entity)
				&& ConsortReputation.get(player).getConsortReputation(homeDimension) <= -1000;
	}
	
	protected void applyAdditionalAITasks()
	{
		if(!this.hasRestriction() || getRestrictRadius() > 1)
			goalSelector.addGoal(5, new WaterAvoidingRandomStrollGoal(this, 0.5F));
	}
	
	@Override
	public boolean isWithinRestriction()
	{
		return homeDimension != this.level().dimension() || super.isWithinRestriction();
	}
	
	@Override
	protected InteractionResult mobInteract(Player player, InteractionHand hand)
	{
		if(!this.isAlive() || player.isShiftKeyDown())
			return InteractionResult.PASS;
		
		if(eventTimer >= 0)
			return InteractionResult.FAIL;
		
		if(!(player instanceof ServerPlayer serverPlayer))
			return InteractionResult.SUCCESS;
		
		if(this.dialogueComponent.hasAnyOngoingDialogue())	//todo do we want this? feel free to remove it if not
			return InteractionResult.FAIL;
		
		Optional<PlayerData> playerData = PlayerData.get(serverPlayer);
		if(playerData.isEmpty() || ConsortReputation.get(playerData.get()).getConsortReputation(homeDimension) <= -1000)
			return InteractionResult.FAIL;
		
		handleConsortRepFromTalking(serverPlayer);
		setCurrentAnimation(TALK_PROPERTIES);
		
		
		if(!this.dialogueComponent.hasActiveDialogue())
		{
			this.dialogueComponent.resetDialogue();
			RandomlySelectableDialogue.instance(this.merchantType.dialogueCategory())
					.pickRandomForEntity(this).ifPresent(this.dialogueComponent::setDialogue);
		}
		
		this.dialogueComponent.tryStartDialogue(serverPlayer);
		
		this.dialogueComponent.getStartingDialogue().ifPresent(dialogueId -> {
			MSCriteriaTriggers.CONSORT_TALK.get().trigger(serverPlayer, dialogueId.toString(), this);
			if(ticksUntilDialogueReset == 0)
				ticksUntilDialogueReset = 24000 + level().random.nextInt(24000);
		});
		
		return InteractionResult.SUCCESS;
	}
	
	private void clearDialogueData()
	{
		ticksUntilDialogueReset = 0;
		stocks = null;
		this.dialogueComponent.resetDialogue();
		talkRepPlayerList.clear();
	}
	
	private void handleConsortRepFromTalking(ServerPlayer player)
	{
		PlayerIdentifier identifier = IdentifierHandler.encode(player);
		if(!talkRepPlayerList.contains(identifier))
		{
			ConsortReputation.get(player).addConsortReputation(1, homeDimension);
			talkRepPlayerList.add(identifier);
		}
	}
	
	public void setExplosionTimer()
	{
		//Start a timer of one second: 20 ticks.
		//Consorts explode when the timer hits zero.
		if(eventTimer == -1)
			eventTimer = 20;
	}
	
	@Override
	public boolean hurt(DamageSource source, float amount)
	{
		boolean wasHurt = super.hurt(source, amount);
		
		if(wasHurt)
			this.dialogueComponent.closeAllCurrentDialogue();
		
		return wasHurt;
	}
	
	@Override
	public void aiStep()
	{
		super.aiStep();
		if(level().isClientSide)
			return;
		
		if(ticksUntilDialogueReset > 0)
		{
			ticksUntilDialogueReset -= MinestuckConfig.SERVER.dialogueRenewalSpeed.get();
			if(ticksUntilDialogueReset <= 0)
				clearDialogueData();
		}
		
		this.dialogueComponent.closeDialogueForMovingPlayers();
		
		if(MSDimensions.isSkaia(level().dimension()))
			visitedSkaia = true;
		
		if(eventTimer > 0)
			eventTimer--;
		else if(eventTimer == 0)
			explode();
	}
	
	private void explode()
	{
		if(!this.level().isClientSide)
		{
			this.dead = true;
			float explosionRadius = 2.0f;
			this.level().explode(this, this.getX(), this.getY(), this.getZ(), explosionRadius, Level.ExplosionInteraction.MOB);
			this.discard();
		}
	}
	
	@Override
	public void addAdditionalSaveData(CompoundTag compound)
	{
		super.addAdditionalSaveData(compound);
		
		compound.put("dialogue", dialogueComponent.write());
		
		compound.putInt("dialogue_reset_ticks", ticksUntilDialogueReset);
		
		ListTag list = new ListTag();
		for(PlayerIdentifier id : talkRepPlayerList)
			list.add(id.saveToNBT(new CompoundTag(), "id"));
		compound.put("talkRepList", list);
		
		compound.putInt("Type", merchantType.ordinal());
		ResourceLocation.CODEC.encodeStart(NbtOps.INSTANCE, homeDimension.location()).resultOrPartial(LOGGER::error)
				.ifPresent(tag -> compound.put("HomeDim", tag));
		
		if(merchantType != EnumConsort.MerchantType.NONE && stocks != null)
			compound.put("Stock", stocks.writeToNBT());
		
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
		
		compound.putBoolean("Skaia", visitedSkaia);
	}
	
	@Override
	public void readAdditionalSaveData(CompoundTag compound)
	{
		super.readAdditionalSaveData(compound);
		
		dialogueComponent.read(compound.getCompound("dialogue"));
		
		ticksUntilDialogueReset = compound.getInt("dialogue_reset_ticks");
		
		talkRepPlayerList.clear();
		ListTag list = compound.getList("talkRepList", Tag.TAG_COMPOUND);
		for(int i = 0; i < list.size(); i++)
			talkRepPlayerList.add(IdentifierHandler.loadOrThrow(list.getCompound(i), "id"));
		
		merchantType = EnumConsort.MerchantType.values()[Mth.clamp(compound.getInt("Type"), 0, EnumConsort.MerchantType.values().length - 1)];
		
		if(compound.contains("HomeDim", Tag.TAG_STRING))
			homeDimension = Level.RESOURCE_KEY_CODEC.parse(NbtOps.INSTANCE, compound.get("HomeDim")).resultOrPartial(LOGGER::error).orElse(null);
		if(homeDimension == null)
			homeDimension = this.level().dimension();
		
		if(merchantType != EnumConsort.MerchantType.NONE && compound.contains("Stock", Tag.TAG_LIST))
		{
			stocks = new ConsortMerchantInventory(this, compound.getList("Stock", Tag.TAG_COMPOUND));
		}
		
		if(compound.contains("HomePos", Tag.TAG_COMPOUND))
		{
			CompoundTag nbt = compound.getCompound("HomePos");
			BlockPos pos = new BlockPos(nbt.getInt("HomeX"), nbt.getInt("HomeY"), nbt.getInt("HomeZ"));
			restrictTo(pos, nbt.getInt("MaxHomeDistance"));
		}
		
		visitedSkaia = compound.getBoolean("Skaia");
		
		applyAdditionalAITasks();
	}
	
	@Nullable
	@Override
	public SpawnGroupData finalizeSpawn(ServerLevelAccessor worldIn, DifficultyInstance difficultyIn, MobSpawnType reason, @Nullable SpawnGroupData spawnDataIn, @Nullable CompoundTag dataTag)
	{
		if(merchantType == EnumConsort.MerchantType.NONE && this.random.nextInt(30) == 0)
		{
			merchantType = EnumConsort.MerchantType.SHADY;
			if(hasRestriction())
				restrictTo(getRestrictCenter(), (int) (getRestrictRadius() * 0.4F));
		}
		
		homeDimension = level().dimension();
		visitedSkaia = random.nextFloat() < 0.1F;
		
		applyAdditionalAITasks();
		
		return super.finalizeSpawn(worldIn, difficultyIn, reason, spawnDataIn, dataTag);
	}
	
	@Override
	public boolean skipAttackInteraction(Entity entityIn)
	{
		if(!(entityIn instanceof FakePlayer) && entityIn instanceof ServerPlayer player)
			ConsortReputation.get(player).addConsortReputation(-5, homeDimension);
		return super.skipAttackInteraction(entityIn);
	}
	
	@Override
	public void die(DamageSource cause)
	{
		LivingEntity livingEntity = this.getKillCredit();
		if(livingEntity instanceof ServerPlayer player && (!(player instanceof FakePlayer)))
			ConsortReputation.get(player).addConsortReputation(-100, homeDimension);
		super.die(cause);
	}
	
	@Override
	public boolean removeWhenFarAway(double distanceToClosestPlayer)
	{
		return false;
	}
	
	public EnumConsort getConsortType()
	{
		return consortType;
	}
	
	@Nullable
	@Override
	public AbstractContainerMenu createMenu(int windowId, Inventory playerInventory, Player player)
	{
		if(this.stocks != null)
			return new ConsortMerchantMenu(windowId, playerInventory, stocks, getConsortType(), merchantType, stocks.createPricesFor((ServerPlayer) player));
		else return null;
	}
	
	public void writeShopMenuBuffer(FriendlyByteBuf buffer)
	{
		ConsortMerchantMenu.write(buffer, this);
	}
	
	@Nullable
	@Override
	protected SoundEvent getAmbientSound()
	{
		return consortType.getAmbientSound();
	}
	
	@Nullable
	@Override
	protected SoundEvent getHurtSound(DamageSource damageSourceIn)
	{
		return consortType.getHurtSound();
	}
	
	@Nullable
	@Override
	protected SoundEvent getDeathSound()
	{
		return consortType.getDeathSound();
	}
	
	public ResourceKey<Level> getHomeDimension()
	{
		return homeDimension;
	}
	
	public boolean visitedSkaia()
	{
		return this.visitedSkaia;
	}
	
	public static boolean canConsortSpawnOn(EntityType<ConsortEntity> entityType, LevelAccessor world, MobSpawnType reason, BlockPos pos, RandomSource random)
	{
		return true;
	}
	
	@Override
	public DialogueComponent getDialogueComponent()
	{
		return this.dialogueComponent;
	}
	
	@Override
	public ChatFormatting getChatColor()
	{
		return getConsortType().getColor();
	}
	
	@Override
	public String getSpriteType()
	{
		//TODO consider adding support for vendor sprites here
		return getConsortType().getName();
	}
	
	@Override
	public void registerControllers(AnimatableManager.ControllerRegistrar controllers)
	{
		controllers.add(new AnimationController<>(this, "idleAnimation", ConsortEntity::idleAnimation));
		controllers.add(new AnimationController<>(this, "walkAnimation", ConsortEntity::walkAnimation));
		controllers.add(new AnimationController<>(this, "armsAnimation", ConsortEntity::armsAnimation));
		controllers.add(new AnimationController<>(this, "deathAnimation", ConsortEntity::deathAnimation));
		controllers.add(new AnimationController<>(this, "actionAnimation", ConsortEntity::actionAnimation));
	}
	
	private static PlayState idleAnimation(AnimationState<ConsortEntity> state)
	{
		if(state.isMoving() || state.getAnimatable().getCurrentAction() != MobAnimation.Action.IDLE)
		{
			return PlayState.STOP;
		}
		
		state.getController().setAnimation(IDLE_ANIMATION);
		return PlayState.CONTINUE;
	}
	
	private static PlayState walkAnimation(AnimationState<ConsortEntity> state)
	{
		MobAnimation.Action action = state.getAnimatable().getCurrentAction();
		
		if(!state.isMoving())
		{
			return PlayState.STOP;
		}
		
		if(action == MobAnimation.Action.PANIC)
		{
			//TODO add a system for the panic animation intended to precede this
			state.getController().setAnimation(PANIC_ANIMATION);
			return PlayState.CONTINUE;
		} else if(action != MobAnimation.Action.IDLE)
		{
			return PlayState.STOP;
		} else if(state.getAnimatable().jumping)
		{
			state.getController().setAnimation(JUMP_ANIMATION);
			return PlayState.CONTINUE;
		} else
		{
			state.getController().setAnimation(WALK_ANIMATION);
			return PlayState.CONTINUE;
		}
	}
	
	private static PlayState armsAnimation(AnimationState<ConsortEntity> state)
	{
		if(!state.isMoving() || state.getAnimatable().getCurrentAction() != MobAnimation.Action.IDLE)
		{
			return PlayState.STOP;
		}
		
		state.getController().setAnimation(ARMS_WALKING_ANIMATION);
		return PlayState.CONTINUE;
	}
	
	private static PlayState deathAnimation(AnimationState<ConsortEntity> state)
	{
		if(state.getAnimatable().dead)
		{
			state.getController().setAnimation(DIE_ANIMATION);
			return PlayState.CONTINUE;
		}
		return PlayState.STOP;
	}
	
	private static PlayState actionAnimation(AnimationState<ConsortEntity> state)
	{
		MobAnimation.Action action = state.getAnimatable().getCurrentAction();
		if(action == MobAnimation.Action.TALK)
		{
			state.getController().setAnimation(TALK_ANIMATION);
			return PlayState.CONTINUE;
		}
		
		return PlayState.STOP;
	}
}

