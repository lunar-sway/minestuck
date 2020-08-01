package com.mraof.minestuck.entity.consort;

import com.mraof.minestuck.advancements.MSCriteriaTriggers;
import com.mraof.minestuck.entity.MinestuckEntity;
import com.mraof.minestuck.entity.consort.MessageType.SingleMessage;
import com.mraof.minestuck.inventory.ConsortMerchantContainer;
import com.mraof.minestuck.inventory.ConsortMerchantInventory;
import com.mraof.minestuck.util.MSNBTUtil;
import com.mraof.minestuck.world.MSDimensions;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ILivingEntityData;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.ai.goal.*;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.IContainerProvider;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.Hand;
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
import java.util.Iterator;

public abstract class ConsortEntity extends MinestuckEntity implements IContainerProvider
{	//I'd get rid of the seemingly pointless subclasses, but as of writing, entity renderers are registered to entity classes instead of entity types.
	
	ConsortDialogue.DialogueWrapper message;
	int messageTicksLeft;
	CompoundNBT messageData;
	public EnumConsort.MerchantType merchantType = EnumConsort.MerchantType.NONE;
	DimensionType homeDimension;
	boolean visitedSkaia;
	MessageType.DelayMessage updatingMessage; //Change to an interface/array if more message components need tick updates
	public ConsortMerchantInventory stocks;
	private int eventTimer = -1;
	private float explosionRadius = 2.0f;
	static private SingleMessage explosionMessage = new SingleMessage("immortalityHerb.3");
	
	public ConsortEntity(EntityType<? extends ConsortEntity> type, World world)
	{
		super(type, world);
		this.experienceValue = 1;
	}
	
	@Override
	protected void registerGoals()
	{
		goalSelector.addGoal(0, new SwimGoal(this));
		goalSelector.addGoal(1, new PanicGoal(this, 1.0D));
		goalSelector.addGoal(4, new MoveTowardsRestrictionGoal(this, 0.6F));
		goalSelector.addGoal(6, new LookAtGoal(this, PlayerEntity.class, 8.0F));
		goalSelector.addGoal(7, new LookRandomlyGoal(this));
	}
	
	protected void applyAdditionalAITasks()
	{
		if(!this.detachHome() || getMaximumHomeDistance() > 1)
			goalSelector.addGoal(5, new WaterAvoidingRandomWalkingGoal(this, 0.5F));
	}
	
	@Override
	protected float getMaximumHealth()
	{
		return 10;
	}
	
	@Override
	protected boolean processInteract(PlayerEntity player, Hand hand)
	{
		if(this.isAlive() && !player.isSneaking() && eventTimer < 0)
		{
			if(!world.isRemote && player instanceof ServerPlayerEntity)
			{
				ServerPlayerEntity serverPlayer = (ServerPlayerEntity) player;
				if(message == null)
				{
					message = ConsortDialogue.getRandomMessage(this, serverPlayer);
					messageTicksLeft = 24000 + world.rand.nextInt(24000);
					messageData = new CompoundNBT();
				}
				ITextComponent text = message.getMessage(this, serverPlayer);	//TODO Make sure to catch any issues here
				if (text != null)
				{
					player.sendMessage(text);
					onSendMessage(serverPlayer, text, this);
				}
				MSCriteriaTriggers.CONSORT_TALK.trigger(serverPlayer, message.getString(), this);
			}
			
			return true;
		} else
			return super.processInteract(player, hand);
	}
	
	public void onSendMessage(ServerPlayerEntity player, ITextComponent text, ConsortEntity consortEntity)
	{
		Iterator<ITextComponent> i = text.iterator();
		String explosionMessage = ConsortEntity.explosionMessage.getMessageForTesting(this, player).getUnformattedComponentText();
		
		//This block triggers when the consort from Flora Lands eats the "immortality" herb.
		if(text.getUnformattedComponentText().equals(explosionMessage))
		{
			//Start a timer of one second: 20 ticks.
			//Consorts explode when the timer hits zero.
			eventTimer = 20;
		}
	}
	
	@Override
	public void livingTick()
	{
		super.livingTick();
		if(world.isRemote)
			return;
		
		if(messageTicksLeft > 0)
			messageTicksLeft--;
		else if(messageTicksLeft == 0)
		{
			message = null;
			messageData = null;
			updatingMessage = null;
			stocks = null;
		}
		
		if(updatingMessage != null)
		{
			updatingMessage.onTickUpdate(this);
		}
		
		if(MSDimensions.isSkaia(dimension))
			visitedSkaia = true;
		
		if(eventTimer > 0)
		{
			eventTimer--;
		}
		else if(eventTimer == 0)
		{
			explode();
		}
	}
	
	private void explode()
	{
		if (!this.world.isRemote)
		{
			boolean flag = net.minecraftforge.event.ForgeEventFactory.getMobGriefingEvent(this.world, this);
			this.dead = true;
			this.world.createExplosion(this, this.getPosX(), this.getPosY(), this.getPosZ(), this.explosionRadius, flag ? Explosion.Mode.DESTROY : Explosion.Mode.NONE);
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
			compound.putInt("MessageTicks", messageTicksLeft);
			compound.put("MessageData", messageData);
		}
		
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
			if(compound.contains("MessageTicks", Constants.NBT.TAG_ANY_NUMERIC))
				messageTicksLeft = compound.getInt("MessageTicks");
			else messageTicksLeft = 24000;	//Used to make summoning with a specific message slightly easier
			messageData = compound.getCompound("MessageData");
		}
		
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
	public boolean canDespawn(double distanceToClosestPlayer)
	{
		return false;
	}
	
	public abstract EnumConsort getConsortType();
	
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
			return new ConsortMerchantContainer(windowId, playerInventory, stocks, getConsortType(), merchantType, stocks.getPrices());
		else return null;
	}
	
	protected void writeShopContainerBuffer(PacketBuffer buffer)
	{
		ConsortMerchantContainer.write(buffer, this, stocks.getPrices());
	}
}