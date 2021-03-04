package com.mraof.minestuck.entity.consort;

import com.mraof.minestuck.MinestuckConfig;
import com.mraof.minestuck.advancements.MSCriteriaTriggers;
import com.mraof.minestuck.entity.SimpleTexturedEntity;
import com.mraof.minestuck.inventory.ConsortMerchantContainer;
import com.mraof.minestuck.inventory.ConsortMerchantInventory;
import com.mraof.minestuck.player.IdentifierHandler;
import com.mraof.minestuck.player.PlayerIdentifier;
import com.mraof.minestuck.util.MSNBTUtil;
import com.mraof.minestuck.world.MSDimensions;
import com.mraof.minestuck.world.storage.PlayerSavedData;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.goal.*;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.IContainerProvider;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EntityPredicates;
import net.minecraft.util.Hand;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.Explosion;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;
import net.minecraft.world.dimension.DimensionType;
import net.minecraftforge.common.util.Constants;

import javax.annotation.Nullable;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

public class ConsortEntity extends SimpleTexturedEntity implements IContainerProvider
{
	
	private final EnumConsort consortType;
	
	private boolean hasHadMessage = false;
	ConsortDialogue.DialogueWrapper message;
	int messageTicksLeft;
	private CompoundNBT messageData;
	private final Set<PlayerIdentifier> talkRepPlayerList = new HashSet<>();
	public EnumConsort.MerchantType merchantType = EnumConsort.MerchantType.NONE;
	DimensionType homeDimension;
	boolean visitedSkaia;
	MessageType.DelayMessage updatingMessage; //TODO Change to an interface/array if more message components need tick updates
	public ConsortMerchantInventory stocks;
	private int eventTimer = -1;	//TODO use the interface mentioned in the todo above to implement consort explosion instead
	
	public ConsortEntity(EnumConsort consortType, EntityType<? extends ConsortEntity> type, World world)
	{
		super(type, world);
		this.consortType = consortType;
		this.experienceValue = 1;
	}
	
	@Override
	protected void registerAttributes()
	{
		super.registerAttributes();
		getAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(10D);
		getAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.25);
	}
	
	@Override
	protected void registerGoals()
	{
		goalSelector.addGoal(0, new SwimGoal(this));
		goalSelector.addGoal(1, new PanicGoal(this, 1.0D));
		goalSelector.addGoal(4, new MoveTowardsRestrictionGoal(this, 0.6F));
		goalSelector.addGoal(6, new LookAtGoal(this, PlayerEntity.class, 8.0F));
		goalSelector.addGoal(7, new LookRandomlyGoal(this));
		goalSelector.addGoal(4, new AvoidEntityGoal<>(this, PlayerEntity.class, 16F, 1.0D, 1.4D, this::shouldFleeFrom));
	}
	
	private boolean shouldFleeFrom(LivingEntity entity)
	{
		return entity instanceof ServerPlayerEntity && EntityPredicates.CAN_AI_TARGET.test(entity) && PlayerSavedData.getData((ServerPlayerEntity) entity).getConsortReputation(homeDimension) <= -1000;
	}
	
	protected void applyAdditionalAITasks()
	{
		if(!this.detachHome() || getMaximumHomeDistance() > 1)
			goalSelector.addGoal(5, new WaterAvoidingRandomWalkingGoal(this, 0.5F));
	}
	
	@Override
	public boolean isWithinHomeDistanceCurrentPosition()
	{
		return homeDimension != this.dimension || super.isWithinHomeDistanceCurrentPosition();
	}
	
	@Override
	protected boolean processInteract(PlayerEntity player, Hand hand)
	{
		if(this.isAlive() && !player.isSneaking() && eventTimer < 0)
		{
			if(!world.isRemote && player instanceof ServerPlayerEntity && PlayerSavedData.getData((ServerPlayerEntity) player).getConsortReputation(homeDimension) > -1000)
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
						player.sendMessage(text);
					handleConsortRepFromTalking(serverPlayer);
					MSCriteriaTriggers.CONSORT_TALK.trigger(serverPlayer, message.getString(), this);
				} catch(Exception e)
				{
					LOGGER.error("Got exception when getting dialogue message for consort for player {}.", serverPlayer.getGameProfile().getName(), e);
				}
			}
			
			return true;
		} else
			return super.processInteract(player, hand);
	}
	
	private void checkMessageData()
	{
		if(messageData == null)
		{
			messageData = new CompoundNBT();
			messageTicksLeft = 24000 + world.rand.nextInt(24000);
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
	public void livingTick()
	{
		super.livingTick();
		if(world.isRemote)
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
		
		if(MSDimensions.isSkaia(dimension))
			visitedSkaia = true;
		
		if(eventTimer > 0)
			eventTimer--;
		else if(eventTimer == 0)
			explode();
	}
	
	private void explode()
	{
		if (!this.world.isRemote)
		{
			boolean flag = net.minecraftforge.event.ForgeEventFactory.getMobGriefingEvent(this.world, this);
			this.dead = true;
			float explosionRadius = 2.0f;
			this.world.createExplosion(this, this.getPosX(), this.getPosY(), this.getPosZ(), explosionRadius, flag ? Explosion.Mode.DESTROY : Explosion.Mode.NONE);
			this.remove();
		}
	}
	
	@Override
	public void writeAdditional(CompoundNBT compound)
	{
		super.writeAdditional(compound);
		
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
		MSNBTUtil.tryWriteDimensionType(compound, "HomeDim", homeDimension);
		
		if(merchantType != EnumConsort.MerchantType.NONE && stocks != null)
			compound.put("Stock", stocks.writeToNBT());
		
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
		
		compound.putBoolean("Skaia", visitedSkaia);
	}
	
	@Override
	public void readAdditional(CompoundNBT compound)
	{
		super.readAdditional(compound);
		
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
			homeDimension = MSNBTUtil.tryReadDimensionType(compound, "HomeDim");
		if(homeDimension == null)
			homeDimension = this.world.getDimension().getType();
		
		if(merchantType != EnumConsort.MerchantType.NONE && compound.contains("Stock", Constants.NBT.TAG_LIST))
		{
			stocks = new ConsortMerchantInventory(this, compound.getList("Stock", Constants.NBT.TAG_COMPOUND));
		}
		
		if(compound.contains("HomePos", Constants.NBT.TAG_COMPOUND))
		{
			CompoundNBT nbt = compound.getCompound("HomePos");
			BlockPos pos = new BlockPos(nbt.getInt("HomeX"), nbt.getInt("HomeY"), nbt.getInt("HomeZ"));
			setHomePosAndDistance(pos, nbt.getInt("MaxHomeDistance"));
		}
		
		visitedSkaia = compound.getBoolean("Skaia");
		
		applyAdditionalAITasks();
	}
	
	@Nullable
	@Override
	public ILivingEntityData onInitialSpawn(IWorld worldIn, DifficultyInstance difficultyIn, SpawnReason reason, @Nullable ILivingEntityData spawnDataIn, @Nullable CompoundNBT dataTag)
	{
		if(merchantType == EnumConsort.MerchantType.NONE && this.rand.nextInt(30) == 0)
		{
			merchantType = EnumConsort.MerchantType.SHADY;
			if(detachHome())
				setHomePosAndDistance(getHomePosition(), (int) (getMaximumHomeDistance()*0.4F));
		}
		
		homeDimension = world.getDimension().getType();
		visitedSkaia = rand.nextFloat() < 0.1F;
		
		applyAdditionalAITasks();
		
		return super.onInitialSpawn(worldIn, difficultyIn, reason, spawnDataIn, dataTag);
	}
	
	@Override
	public boolean hitByEntity(Entity entityIn)
	{
		if(entityIn instanceof ServerPlayerEntity)
			PlayerSavedData.getData((ServerPlayerEntity) entityIn).addConsortReputation(-5, homeDimension);
		return super.hitByEntity(entityIn);
	}
	
	@Override
	public void onDeath(DamageSource cause)
	{
		LivingEntity livingEntity = this.getAttackingEntity();
		if(livingEntity instanceof ServerPlayerEntity)
			PlayerSavedData.getData((ServerPlayerEntity) livingEntity).addConsortReputation(-100, homeDimension);
		super.onDeath(cause);
	}
	
	@Override
	public boolean canDespawn(double distanceToClosestPlayer)
	{
		return false;
	}
	
	public EnumConsort getConsortType()
	{
		return consortType;
	}
	
	public void commandReply(ServerPlayerEntity player, String chain)
	{
		if(this.isAlive() && !world.isRemote && message != null)
		{
			ITextComponent text = message.getFromChain(this, player, chain);
			if(text != null)
				player.sendMessage(text);
		}
	}
	
	public CompoundNBT getMessageTag()
	{
		return messageData;
	}
	
	public CompoundNBT getMessageTagForPlayer(PlayerEntity player)
	{
		if(!messageData.contains(player.getCachedUniqueIdString(), Constants.NBT.TAG_COMPOUND))
			messageData.put(player.getCachedUniqueIdString(), new CompoundNBT());
		return messageData.getCompound(player.getCachedUniqueIdString());
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
	
	public DimensionType getHomeDimension()
	{
		return homeDimension;
	}
	
	public static boolean canConsortSpawnOn(EntityType<ConsortEntity> entityType, IWorld world, SpawnReason reason, BlockPos pos, Random random)
	{
		return true;
	}
}