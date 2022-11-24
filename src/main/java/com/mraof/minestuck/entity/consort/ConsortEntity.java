package com.mraof.minestuck.entity.consort;

import com.mojang.logging.LogUtils;
import com.mraof.minestuck.MinestuckConfig;
import com.mraof.minestuck.advancements.MSCriteriaTriggers;
import com.mraof.minestuck.entity.AnimatedPathfinderMob;
import com.mraof.minestuck.entity.ai.AnimatedPanicGoal;
import com.mraof.minestuck.entity.animation.MSMobAnimation;
import com.mraof.minestuck.inventory.ConsortMerchantMenu;
import com.mraof.minestuck.inventory.ConsortMerchantInventory;
import com.mraof.minestuck.player.IdentifierHandler;
import com.mraof.minestuck.player.PlayerIdentifier;
import com.mraof.minestuck.util.AnimationControllerUtil;
import com.mraof.minestuck.world.MSDimensions;
import com.mraof.minestuck.player.PlayerSavedData;
import net.minecraft.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.nbt.Tag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.util.Mth;
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
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraftforge.common.util.FakePlayer;
import org.slf4j.Logger;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.builder.AnimationBuilder;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;

import javax.annotation.Nullable;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

public class ConsortEntity extends AnimatedPathfinderMob implements MenuProvider, IAnimatable
{
	private static final Logger LOGGER = LogUtils.getLogger();
	
	public static final MSMobAnimation TALK_ANIMATION = new MSMobAnimation(MSMobAnimation.Actions.TALK, 80, true); //TODO adjust as needed - 4 secs for now
	public static final MSMobAnimation PANIC_ANIMATION = new MSMobAnimation(MSMobAnimation.Actions.PANIC, MSMobAnimation.LOOPING_ANIMATION, false);
	
	private final AnimationFactory factory = new AnimationFactory(this);
	private final EnumConsort consortType;
	private boolean hasHadMessage = false;
	ConsortDialogue.DialogueWrapper message;
	int messageTicksLeft;
	private CompoundTag messageData;
	private final Set<PlayerIdentifier> talkRepPlayerList = new HashSet<>();
	public EnumConsort.MerchantType merchantType = EnumConsort.MerchantType.NONE;
	ResourceKey<Level> homeDimension;
	boolean visitedSkaia;
	MessageType.DelayMessage updatingMessage; //TODO Change to an interface/array if more message components need tick updates
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
		goalSelector.addGoal(1, new AnimatedPanicGoal(this, 1.4D));
		goalSelector.addGoal(4, new MoveTowardsRestrictionGoal(this, 1F));
		goalSelector.addGoal(6, new LookAtPlayerGoal(this, Player.class, 8.0F));
		goalSelector.addGoal(7, new RandomLookAroundGoal(this));
		goalSelector.addGoal(4, new AvoidEntityGoal<>(this, Player.class, 16F, 1.0D, 1.4D, this::shouldFleeFrom));
	}
	
	@Override
	public AnimationFactory getFactory()
	{
		return this.factory;
	}
	
	private boolean shouldFleeFrom(LivingEntity entity)
	{
		return entity instanceof ServerPlayer && EntitySelector.NO_CREATIVE_OR_SPECTATOR.test(entity) && PlayerSavedData.getData((ServerPlayer) entity).getConsortReputation(homeDimension) <= -1000;
	}
	
	protected void applyAdditionalAITasks()
	{
		if(!this.hasRestriction() || getRestrictRadius() > 1)
			goalSelector.addGoal(5, new WaterAvoidingRandomStrollGoal(this, 0.5F));
	}
	
	@Override
	public boolean isWithinRestriction()
	{
		return homeDimension != this.level.dimension() || super.isWithinRestriction();
	}
	
	@Override
	protected InteractionResult mobInteract(Player player, InteractionHand hand)
	{
		if(this.isAlive() && !player.isShiftKeyDown() && eventTimer < 0)
		{
			if(!level.isClientSide && player instanceof ServerPlayer serverPlayer && PlayerSavedData.getData(serverPlayer).getConsortReputation(homeDimension) > -1000)
			{
				if(message == null)
				{
					message = ConsortDialogue.getRandomMessage(this, hasHadMessage);
					hasHadMessage = true;
				}
				
				checkMessageData();
				
				try
				{
					Component text = message.getMessage(this, serverPlayer);
					if(text != null)
						player.sendMessage(text, Util.NIL_UUID);
					handleConsortRepFromTalking(serverPlayer);
					setCurrentAnimation(TALK_ANIMATION);
					MSCriteriaTriggers.CONSORT_TALK.trigger(serverPlayer, message.getString(), this);
				} catch(Exception e)
				{
					LOGGER.error("Got exception when getting dialogue message for consort for player {}.", serverPlayer.getGameProfile().getName(), e);
				}
			}
			
			return InteractionResult.SUCCESS;
		} else
			return super.mobInteract(player, hand);
	}
	
	private void checkMessageData()
	{
		if(messageData == null)
		{
			messageData = new CompoundTag();
			messageTicksLeft = 24000 + level.random.nextInt(24000);
		}
	}
	
	private void clearDialogueData()
	{
		messageData = null;
		updatingMessage = null;
		stocks = null;
		talkRepPlayerList.clear();
	}
	
	private void handleConsortRepFromTalking(ServerPlayer player)
	{
		PlayerIdentifier identifier = IdentifierHandler.encode(player);
		if(!talkRepPlayerList.contains(identifier))
		{
			PlayerSavedData.getData(player).addConsortReputation(1, homeDimension);
			talkRepPlayerList.add(identifier);
		}
	}
	
	protected void setExplosionTimer()
	{
		//Start a timer of one second: 20 ticks.
		//Consorts explode when the timer hits zero.
		if(eventTimer == -1)
			eventTimer = 20;
	}
	
	@Override
	public void aiStep()
	{
		super.aiStep();
		if(level.isClientSide)
			return;
		
		if(messageTicksLeft > 0)
			messageTicksLeft -= MinestuckConfig.SERVER.dialogueRenewalSpeed.get();
		else if(messageData != null)
		{
			clearDialogueData();
			if(message != null && !message.isLockedToConsort())
				message = null;
		}
		
		if(updatingMessage != null)
			updatingMessage.onTickUpdate(this);
		
		if(MSDimensions.isSkaia(level.dimension()))
			visitedSkaia = true;
		
		if(eventTimer > 0)
			eventTimer--;
		else if(eventTimer == 0)
			explode();
	}
	
	private void explode()
	{
		if(!this.level.isClientSide)
		{
			boolean flag = net.minecraftforge.event.ForgeEventFactory.getMobGriefingEvent(this.level, this);
			this.dead = true;
			float explosionRadius = 2.0f;
			this.level.explode(this, this.getX(), this.getY(), this.getZ(), explosionRadius, flag ? Explosion.BlockInteraction.DESTROY : Explosion.BlockInteraction.NONE);
			this.discard();
		}
	}
	
	@Override
	public void addAdditionalSaveData(CompoundTag compound)
	{
		super.addAdditionalSaveData(compound);
		
		if(message != null)
		{
			compound.putString("Dialogue", message.getString());
			if(messageData != null)
			{
				compound.putInt("MessageTicks", messageTicksLeft);
				compound.put("MessageData", messageData);
				ListTag list = new ListTag();
				for(PlayerIdentifier id : talkRepPlayerList)
					list.add(id.saveToNBT(new CompoundTag(), "id"));
				compound.put("talkRepList", list);
			}
		}
		compound.putBoolean("HasHadMessage", hasHadMessage);
		
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
		
		if(compound.contains("Dialogue", Tag.TAG_STRING))
		{
			message = ConsortDialogue.getMessageFromString(compound.getString("Dialogue"));
			if(compound.contains("MessageData", Tag.TAG_COMPOUND))
			{
				messageData = compound.getCompound("MessageData");
				messageTicksLeft = compound.getInt("MessageTicks");
				
				talkRepPlayerList.clear();
				ListTag list = compound.getList("talkRepList", Tag.TAG_COMPOUND);
				for(int i = 0; i < list.size(); i++)
					talkRepPlayerList.add(IdentifierHandler.load(list.getCompound(i), "id"));
			}
			
			hasHadMessage = true;
		} else hasHadMessage = compound.getBoolean("HasHadMessage");
		
		merchantType = EnumConsort.MerchantType.values()[Mth.clamp(compound.getInt("Type"), 0, EnumConsort.MerchantType.values().length - 1)];
		
		if(compound.contains("HomeDim", Tag.TAG_STRING))
			homeDimension = Level.RESOURCE_KEY_CODEC.parse(NbtOps.INSTANCE, compound.get("HomeDim")).resultOrPartial(LOGGER::error).orElse(null);
		if(homeDimension == null)
			homeDimension = this.level.dimension();
		
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
		
		homeDimension = level.dimension();
		visitedSkaia = random.nextFloat() < 0.1F;
		
		applyAdditionalAITasks();
		
		return super.finalizeSpawn(worldIn, difficultyIn, reason, spawnDataIn, dataTag);
	}
	
	@Override
	public boolean skipAttackInteraction(Entity entityIn)
	{
		if(entityIn instanceof ServerPlayer player)
			PlayerSavedData.getData(player).addConsortReputation(-5, homeDimension);
		return super.skipAttackInteraction(entityIn);
	}
	
	@Override
	public void die(DamageSource cause)
	{
		LivingEntity livingEntity = this.getKillCredit();
		if(livingEntity instanceof ServerPlayer player && (!(player instanceof FakePlayer)))
			PlayerSavedData.getData(player).addConsortReputation(-100, homeDimension);
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
	
	public void commandReply(ServerPlayer player, String chain)
	{
		if(this.isAlive() && !level.isClientSide && message != null)
		{
			Component text = message.getFromChain(this, player, chain);
			if(text != null)
				player.sendMessage(text, Util.NIL_UUID);
		}
	}
	
	public CompoundTag getMessageTag()
	{
		return messageData;
	}
	
	public CompoundTag getMessageTagForPlayer(Player player)
	{
		if(!messageData.contains(player.getStringUUID(), Tag.TAG_COMPOUND))
			messageData.put(player.getStringUUID(), new CompoundTag());
		return messageData.getCompound(player.getStringUUID());
	}
	
	@Nullable
	@Override
	public AbstractContainerMenu createMenu(int windowId, Inventory playerInventory, Player player)
	{
		if(this.stocks != null)
			return new ConsortMerchantMenu(windowId, playerInventory, stocks, getConsortType(), merchantType, stocks.createPricesFor((ServerPlayer) player));
		else return null;
	}
	
	protected void writeShopMenuBuffer(FriendlyByteBuf buffer)
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
	
	public static boolean canConsortSpawnOn(EntityType<ConsortEntity> entityType, LevelAccessor world, MobSpawnType reason, BlockPos pos, Random random)
	{
		return true;
	}
	
	@Override
	public MSMobAnimation getPanicAnimation()
	{
		return PANIC_ANIMATION;
	}
	
	@Override
	public void registerControllers(AnimationData data)
	{
		data.addAnimationController(AnimationControllerUtil.createAnimation(this, "idleAnimation", 1, ConsortEntity::idleAnimation));
		data.addAnimationController(AnimationControllerUtil.createAnimation(this, "walkAnimation", 1, ConsortEntity::walkAnimation));
		data.addAnimationController(AnimationControllerUtil.createAnimation(this, "armsAnimation", 1, ConsortEntity::armsAnimation));
		data.addAnimationController(AnimationControllerUtil.createAnimation(this, "deathAnimation", 1, ConsortEntity::deathAnimation));
		data.addAnimationController(AnimationControllerUtil.createAnimation(this, "actionAnimation", 1, ConsortEntity::actionAnimation));
	}
	
	private static PlayState idleAnimation(AnimationEvent<ConsortEntity> event)
	{
		if(event.isMoving() || event.getAnimatable().getCurrentAction() != MSMobAnimation.Actions.IDLE)
		{
			return PlayState.STOP;
		}
		
		event.getController().setAnimation(new AnimationBuilder().addAnimation("idle", true));
		return PlayState.CONTINUE;
	}
	
	private static PlayState walkAnimation(AnimationEvent<ConsortEntity> event)
	{
		if(!event.isMoving() || event.getAnimatable().getCurrentAction() != MSMobAnimation.Actions.IDLE)
		{
			return PlayState.STOP;
		}
		
		event.getController().setAnimation(new AnimationBuilder().addAnimation("walk", true));
		return PlayState.CONTINUE;
	}
	
	private static PlayState armsAnimation(AnimationEvent<ConsortEntity> event)
	{
		if(!event.isMoving() || event.getAnimatable().getCurrentAction() != MSMobAnimation.Actions.IDLE)
		{
			return PlayState.STOP;
		}
		
		event.getController().setAnimation(new AnimationBuilder().addAnimation("walkarms", true));
		return PlayState.CONTINUE;
	}
	
	private static PlayState deathAnimation(AnimationEvent<ConsortEntity> event)
	{
		if(event.getAnimatable().dead)
		{
			event.getController().setAnimation(new AnimationBuilder().addAnimation("die", false));
			return PlayState.CONTINUE;
		}
		return PlayState.STOP;
	}
	
	private static PlayState actionAnimation(AnimationEvent<ConsortEntity> event)
	{
		MSMobAnimation.Actions action = event.getAnimatable().getCurrentAction();
		if(action == MSMobAnimation.Actions.TALK)
		{
			event.getController().setAnimation(new AnimationBuilder().addAnimation("talk", true));
			return PlayState.CONTINUE;
		}
		if(action == MSMobAnimation.Actions.PANIC)
		{
			event.getController().setAnimation(new AnimationBuilder().addAnimation("panic", false).addAnimation("panicrun", true));
			return PlayState.CONTINUE;
		}
		return PlayState.STOP;
	}
}