package com.mraof.minestuck.entity.consort;

import com.mraof.minestuck.MinestuckConfig;
import com.mraof.minestuck.advancements.MSCriteriaTriggers;
import com.mraof.minestuck.entity.AnimatedCreatureEntity;
import com.mraof.minestuck.entity.ai.AnimatedPanicGoal;
import com.mraof.minestuck.inventory.ConsortMerchantContainer;
import com.mraof.minestuck.inventory.ConsortMerchantInventory;
import com.mraof.minestuck.player.IdentifierHandler;
import com.mraof.minestuck.player.PlayerIdentifier;
import com.mraof.minestuck.world.MSDimensions;
import com.mraof.minestuck.world.storage.PlayerSavedData;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.attributes.AttributeModifierMap;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.ai.goal.*;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.IContainerProvider;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.nbt.NBTDynamicOps;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.world.*;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.common.util.FakePlayer;
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

public class ConsortEntity extends AnimatedCreatureEntity implements IContainerProvider, IAnimatable
{
	private final AnimationFactory factory = new AnimationFactory(this);
	private final EnumConsort consortType;
	private boolean hasHadMessage = false;
	ConsortDialogue.DialogueWrapper message;
	int messageTicksLeft;
	private CompoundNBT messageData;
	private final Set<PlayerIdentifier> talkRepPlayerList = new HashSet<>();
	public EnumConsort.MerchantType merchantType = EnumConsort.MerchantType.NONE;
	RegistryKey<World> homeDimension;
	boolean visitedSkaia;
	MessageType.DelayMessage updatingMessage; //TODO Change to an interface/array if more message components need tick updates
	public ConsortMerchantInventory stocks;
	private int eventTimer = -1;    //TODO use the interface mentioned in the todo above to implement consort explosion instead
	
	public ConsortEntity(EnumConsort consortType, EntityType<? extends ConsortEntity> type, World world)
	{
		super(type, world);
		this.consortType = consortType;
		this.xpReward = 1;
	}
	
	
	public static AttributeModifierMap.MutableAttribute consortAttributes()
	{
		return MobEntity.createMobAttributes().add(Attributes.MAX_HEALTH, 10).add(Attributes.MOVEMENT_SPEED, 0.35);
	}
	
	@Override
	protected void registerGoals()
	{
		goalSelector.addGoal(0, new SwimGoal(this));
		goalSelector.addGoal(1, new AnimatedPanicGoal(this, 1.4D));
		goalSelector.addGoal(4, new MoveTowardsRestrictionGoal(this, 1F));
		goalSelector.addGoal(6, new LookAtGoal(this, PlayerEntity.class, 8.0F));
		goalSelector.addGoal(7, new LookRandomlyGoal(this));
		goalSelector.addGoal(4, new AvoidEntityGoal<>(this, PlayerEntity.class, 16F, 1.0D, 1.4D, this::shouldFleeFrom));
	}
	
	@Override
	public AnimationFactory getFactory()
	{
		return this.factory;
	}
	
	private boolean shouldFleeFrom(LivingEntity entity)
	{
		return entity instanceof ServerPlayerEntity && EntityPredicates.NO_CREATIVE_OR_SPECTATOR.test(entity) && PlayerSavedData.getData((ServerPlayerEntity) entity).getConsortReputation(homeDimension) <= -1000;
	}
	
	protected void applyAdditionalAITasks()
	{
		if(!this.hasRestriction() || getRestrictRadius() > 1)
			goalSelector.addGoal(5, new WaterAvoidingRandomWalkingGoal(this, 0.5F));
	}
	
	@Override
	public boolean isWithinRestriction()
	{
		return homeDimension != this.level.dimension() || super.isWithinRestriction();
	}
	
	@Override
	protected ActionResultType mobInteract(PlayerEntity player, Hand hand)
	{
		if(this.isAlive() && !player.isShiftKeyDown() && eventTimer < 0)
		{
			if(!level.isClientSide && player instanceof ServerPlayerEntity && PlayerSavedData.getData((ServerPlayerEntity) player).getConsortReputation(homeDimension) > -1000)
			{
				ServerPlayerEntity serverPlayer = (ServerPlayerEntity) player;
				if(message == null)
				{
					message = ConsortDialogue.getRandomMessage(this, hasHadMessage);
					hasHadMessage = true;
				}
				
				checkMessageData();
				
				try
				{
					ITextComponent text = message.getMessage(this, serverPlayer);
					if(text != null)
						player.sendMessage(text, Util.NIL_UUID);
					handleConsortRepFromTalking(serverPlayer);
					setCurrentAction(Actions.TALK, 40); // TODO adjust as needed - 2 secs for now
					this.getNavigation().stop();
					MSCriteriaTriggers.CONSORT_TALK.trigger(serverPlayer, message.getString(), this);
				} catch(Exception e)
				{
					LOGGER.error("Got exception when getting dialogue message for consort for player {}.", serverPlayer.getGameProfile().getName(), e);
				}
			}
			
			return ActionResultType.SUCCESS;
		} else
			return super.mobInteract(player, hand);
	}
	
	private void checkMessageData()
	{
		if(messageData == null)
		{
			messageData = new CompoundNBT();
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
	
	private void handleConsortRepFromTalking(ServerPlayerEntity player)
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
			this.level.explode(this, this.getX(), this.getY(), this.getZ(), explosionRadius, flag ? Explosion.Mode.DESTROY : Explosion.Mode.NONE);
			this.remove();
		}
	}
	
	@Override
	public void addAdditionalSaveData(CompoundNBT compound)
	{
		super.addAdditionalSaveData(compound);
		
		if(message != null)
		{
			compound.putString("Dialogue", message.getString());
			if(messageData != null)
			{
				compound.putInt("MessageTicks", messageTicksLeft);
				compound.put("MessageData", messageData);
				ListNBT list = new ListNBT();
				for(PlayerIdentifier id : talkRepPlayerList)
					list.add(id.saveToNBT(new CompoundNBT(), "id"));
				compound.put("talkRepList", list);
			}
		}
		compound.putBoolean("HasHadMessage", hasHadMessage);
		
		compound.putInt("Type", merchantType.ordinal());
		ResourceLocation.CODEC.encodeStart(NBTDynamicOps.INSTANCE, homeDimension.location()).resultOrPartial(LOGGER::error)
				.ifPresent(tag -> compound.put("HomeDim", tag));
		
		if(merchantType != EnumConsort.MerchantType.NONE && stocks != null)
			compound.put("Stock", stocks.writeToNBT());
		
		if(hasRestriction())
		{
			CompoundNBT nbt = new CompoundNBT();
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
	public void readAdditionalSaveData(CompoundNBT compound)
	{
		super.readAdditionalSaveData(compound);
		
		if(compound.contains("Dialogue", Constants.NBT.TAG_STRING))
		{
			message = ConsortDialogue.getMessageFromString(compound.getString("Dialogue"));
			if(compound.contains("MessageData", Constants.NBT.TAG_COMPOUND))
			{
				messageData = compound.getCompound("MessageData");
				messageTicksLeft = compound.getInt("MessageTicks");
				
				talkRepPlayerList.clear();
				ListNBT list = compound.getList("talkRepList", Constants.NBT.TAG_COMPOUND);
				for(int i = 0; i < list.size(); i++)
					talkRepPlayerList.add(IdentifierHandler.load(list.getCompound(i), "id"));
			}
			
			hasHadMessage = true;
		} else hasHadMessage = compound.getBoolean("HasHadMessage");
		
		merchantType = EnumConsort.MerchantType.values()[MathHelper.clamp(compound.getInt("Type"), 0, EnumConsort.MerchantType.values().length - 1)];
		
		if(compound.contains("HomeDim", Constants.NBT.TAG_STRING))
			homeDimension = World.RESOURCE_KEY_CODEC.parse(NBTDynamicOps.INSTANCE, compound.get("HomeDim")).resultOrPartial(LOGGER::error).orElse(null);
		if(homeDimension == null)
			homeDimension = this.level.dimension();
		
		if(merchantType != EnumConsort.MerchantType.NONE && compound.contains("Stock", Constants.NBT.TAG_LIST))
		{
			stocks = new ConsortMerchantInventory(this, compound.getList("Stock", Constants.NBT.TAG_COMPOUND));
		}
		
		if(compound.contains("HomePos", Constants.NBT.TAG_COMPOUND))
		{
			CompoundNBT nbt = compound.getCompound("HomePos");
			BlockPos pos = new BlockPos(nbt.getInt("HomeX"), nbt.getInt("HomeY"), nbt.getInt("HomeZ"));
			restrictTo(pos, nbt.getInt("MaxHomeDistance"));
		}
		
		visitedSkaia = compound.getBoolean("Skaia");
		
		applyAdditionalAITasks();
	}
	
	@Nullable
	@Override
	public ILivingEntityData finalizeSpawn(IServerWorld worldIn, DifficultyInstance difficultyIn, SpawnReason reason, @Nullable ILivingEntityData spawnDataIn, @Nullable CompoundNBT dataTag)
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
		if(entityIn instanceof ServerPlayerEntity)
			PlayerSavedData.getData((ServerPlayerEntity) entityIn).addConsortReputation(-5, homeDimension);
		return super.skipAttackInteraction(entityIn);
	}
	
	@Override
	public void die(DamageSource cause)
	{
		LivingEntity livingEntity = this.getKillCredit();
		if(livingEntity instanceof ServerPlayerEntity && (!(livingEntity instanceof FakePlayer)))
			PlayerSavedData.getData((ServerPlayerEntity) livingEntity).addConsortReputation(-100, homeDimension);
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
	
	public void commandReply(ServerPlayerEntity player, String chain)
	{
		if(this.isAlive() && !level.isClientSide && message != null)
		{
			ITextComponent text = message.getFromChain(this, player, chain);
			if(text != null)
				player.sendMessage(text, Util.NIL_UUID);
		}
	}
	
	public CompoundNBT getMessageTag()
	{
		return messageData;
	}
	
	public CompoundNBT getMessageTagForPlayer(PlayerEntity player)
	{
		if(!messageData.contains(player.getStringUUID(), Constants.NBT.TAG_COMPOUND))
			messageData.put(player.getStringUUID(), new CompoundNBT());
		return messageData.getCompound(player.getStringUUID());
	}
	
	@Nullable
	@Override
	public Container createMenu(int windowId, PlayerInventory playerInventory, PlayerEntity player)
	{
		if(this.stocks != null)
			return new ConsortMerchantContainer(windowId, playerInventory, stocks, getConsortType(), merchantType, stocks.createPricesFor((ServerPlayerEntity) player));
		else return null;
	}
	
	protected void writeShopContainerBuffer(PacketBuffer buffer)
	{
		ConsortMerchantContainer.write(buffer, this);
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
	
	public RegistryKey<World> getHomeDimension()
	{
		return homeDimension;
	}
	
	public static boolean canConsortSpawnOn(EntityType<ConsortEntity> entityType, IWorld world, SpawnReason reason, BlockPos pos, Random random)
	{
		return true;
	}
	
	@Override
	public void registerControllers(AnimationData data)
	{
		data.addAnimationController(createAnimation(this, "walkAnimation", 1, ConsortEntity::walkAnimation));
		data.addAnimationController(createAnimation(this, "armsAnimation", 1, ConsortEntity::armsAnimation));
		data.addAnimationController(createAnimation(this, "deathAnimation", 1, ConsortEntity::deathAnimation));
		data.addAnimationController(createAnimation(this, "actionAnimation", 1, ConsortEntity::actionAnimation));
	}
	
	private static PlayState walkAnimation(AnimationEvent<ConsortEntity> event)
	{
		if(!event.isMoving() || event.getAnimatable().getCurrentAction() != Actions.NONE)
		{
			return PlayState.STOP;
		}
		
		event.getController().setAnimation(new AnimationBuilder().addAnimation("walk", true));
		return PlayState.CONTINUE;
	}
	
	private static PlayState armsAnimation(AnimationEvent<ConsortEntity> event)
	{
		if(!event.isMoving() || event.getAnimatable().getCurrentAction() != Actions.NONE)
		{
			if(event.getAnimatable().getConsortType() == EnumConsort.TURTLE)
			{ // eeeeeeeeeeehhh maybe just fix the turtle anims instead
				event.getController().setAnimation(new AnimationBuilder().addAnimation("armfix", true));
				return PlayState.CONTINUE;
			}
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
		Actions action = event.getAnimatable().getCurrentAction();
		if(action == Actions.TALK)
		{
			event.getController().setAnimation(new AnimationBuilder().addAnimation("talk", true));
			return PlayState.CONTINUE;
		}
		if(action == Actions.PANIC)
		{
			event.getController().setAnimation(new AnimationBuilder().addAnimation("panic", false).addAnimation("panicrun", true));
			return PlayState.CONTINUE;
		}
		return PlayState.STOP;
	}
}