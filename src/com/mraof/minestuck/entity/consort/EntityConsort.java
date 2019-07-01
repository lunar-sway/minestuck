package com.mraof.minestuck.entity.consort;

import java.util.Iterator;

import com.mraof.minestuck.advancements.MinestuckCriteriaTriggers;
import com.mraof.minestuck.client.gui.GuiHandler;
import com.mraof.minestuck.entity.EntityMinestuck;
import com.mraof.minestuck.entity.consort.MessageType.SingleMessage;
import com.mraof.minestuck.inventory.ContainerConsortMerchant;
import com.mraof.minestuck.inventory.InventoryConsortMerchant;
import com.mraof.minestuck.world.MinestuckDimensionHandler;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.EnumCreatureType;
import net.minecraft.entity.IEntityLivingData;
import net.minecraft.entity.ai.*;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.IInteractionObject;
import net.minecraft.world.World;
import net.minecraft.world.dimension.DimensionType;

import javax.annotation.Nullable;

public abstract class EntityConsort extends EntityMinestuck implements IInteractionObject
{
	
	ConsortDialogue.DialogueWrapper message;
	int messageTicksLeft;
	NBTTagCompound messageData;
	public EnumConsort.MerchantType merchantType = EnumConsort.MerchantType.NONE;
	DimensionType homeDimension;
	boolean visitedSkaia;
	MessageType.DelayMessage updatingMessage; //Change to an interface/array if more message components need tick updates
	public InventoryConsortMerchant stocks;
	private int eventTimer = -1;
	private float explosionRadius = 2.0f;
	static private SingleMessage explosionMessage = new SingleMessage("immortalityHerb.3");
	
	public EntityConsort(EntityType<?> type, World world)
	{
		super(type, world);
		setSize(0.6F, 1.5F);
		this.experienceValue = 1;
	}
	
	@Override
	protected void initEntityAI()
	{
		tasks.addTask(0, new EntityAISwimming(this));
		tasks.addTask(1, new EntityAIPanic(this, 1.0D));
		tasks.addTask(4, new EntityAIMoveTowardsRestriction(this, 0.6F));
		tasks.addTask(6, new EntityAIWatchClosest(this, EntityPlayer.class, 8.0F));
		tasks.addTask(7, new EntityAILookIdle(this));
	}
	
	protected void applyAdditionalAITasks()
	{
		if(!hasHome() || getMaximumHomeDistance() > 1)
			tasks.addTask(5, new EntityAIWander(this, 0.5F));
	}
	
	@Override
	protected float getMaximumHealth()
	{
		return 10;
	}
	
	@Override
	protected boolean processInteract(EntityPlayer player, EnumHand hand)
	{
		if(this.isAlive() && !player.isSneaking() && eventTimer < 0)
		{
			if(!world.isRemote && player instanceof EntityPlayerMP)
			{
				EntityPlayerMP playerMP = (EntityPlayerMP) player;
				if(message == null)
				{
					message = ConsortDialogue.getRandomMessage(this, playerMP);
					messageTicksLeft = 24000 + world.rand.nextInt(24000);
					messageData = new NBTTagCompound();
				}
				ITextComponent text = message.getMessage(this, playerMP);	//TODO Make sure to catch any issues here
				if (text != null)
				{
					player.sendMessage(text);
					onSendMessage(playerMP, text, this);
				}
				MinestuckCriteriaTriggers.CONSORT_TALK.trigger((EntityPlayerMP) player, message.getString(), this);
			}
			
			return true;
		} else
			return super.processInteract(player, hand);
	}
	
	public void onSendMessage(EntityPlayerMP player, ITextComponent text, EntityConsort entityConsort)
	{
		Iterator<ITextComponent> i = text.iterator();
		String explosionMessage = EntityConsort.explosionMessage.getMessageForTesting(this, player).getUnformattedComponentText();
		
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
		
		if(MinestuckDimensionHandler.isSkaia(dimension))
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
			this.world.createExplosion(this, this.posX, this.posY, this.posZ, this.explosionRadius, flag);
			this.remove();
		}
	}
	
	@Override
	public void writeAdditional(NBTTagCompound compound)
	{
		super.writeAdditional(compound);
		
		if(message != null)
		{
			compound.putString("Dialogue", message.getString());
			compound.putInt("MessageTicks", messageTicksLeft);
			compound.put("MessageData", messageData);
		}
		
		compound.putInt("Type", merchantType.ordinal());
		compound.putString("HomeDim", homeDimension.getRegistryName().toString());
		
		if(merchantType != EnumConsort.MerchantType.NONE && stocks != null)
			compound.put("Stock", stocks.writeToNBT());
		
		if(hasHome())
		{
			NBTTagCompound nbt = new NBTTagCompound();
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
	public void readAdditional(NBTTagCompound compound)
	{
		super.readAdditional(compound);
		
		if(compound.contains("Dialogue", 8))
		{
			message = ConsortDialogue.getMessageFromString(compound.getString("Dialogue"));
			if(compound.contains("MessageTicks", 99))
				messageTicksLeft = compound.getInt("MessageTicks");
			else messageTicksLeft = 24000;	//Used to make summoning with a specific message slightly easier
			messageData = compound.getCompound("MessageData");
		}
		
		merchantType = EnumConsort.MerchantType.values()[MathHelper.clamp(compound.getInt("Type"), 0, EnumConsort.MerchantType.values().length - 1)];
		
		if(compound.contains("HomeDim", 99))
			homeDimension = DimensionType.byName(new ResourceLocation(compound.getString("HomeDim")));
		else homeDimension = this.world.getDimension().getType();
		
		if(merchantType != EnumConsort.MerchantType.NONE && compound.contains("Stock", 9))
		{
			stocks = new InventoryConsortMerchant(this, compound.getList("Stock", 10));
		}
		
		if(compound.contains("HomePos", 10))
		{
			NBTTagCompound nbt = compound.getCompound("HomePos");
			BlockPos pos = new BlockPos(nbt.getInt("HomeX"), nbt.getInt("HomeY"), nbt.getInt("HomeZ"));
			setHomePosAndDistance(pos, nbt.getInt("MaxHomeDistance"));
		}
		
		visitedSkaia = compound.getBoolean("Skaia");
		
		applyAdditionalAITasks();
	}
	
	@Nullable
	@Override
	public IEntityLivingData onInitialSpawn(DifficultyInstance difficulty, @Nullable IEntityLivingData entityLivingData, @Nullable NBTTagCompound itemNbt)
	{
		if(merchantType == EnumConsort.MerchantType.NONE && this.rand.nextInt(30) == 0)
		{
			merchantType = EnumConsort.MerchantType.SHADY;
			if(hasHome())
				setHomePosAndDistance(getHomePosition(), (int) (getMaximumHomeDistance()*0.4F));
		}
		
		homeDimension = world.getDimension().getType();
		visitedSkaia = rand.nextFloat() < 0.1F;
		
		applyAdditionalAITasks();
		
		return super.onInitialSpawn(difficulty, entityLivingData, itemNbt);
	}
	
	@Override
	public boolean canDespawn()
	{
		return false;
	}
	
	@Override
	public boolean isCreatureType(EnumCreatureType type, boolean forSpawnCount)
	{
		if(forSpawnCount && this.isNoDespawnRequired())
			return false;
		return type.equals(EnumCreatureType.CREATURE);
	}
	
	public abstract EnumConsort getConsortType();
	
	public void commandReply(EntityPlayerMP player, String chain)
	{
		if(this.isAlive() && !world.isRemote && message != null)
		{
			ITextComponent text = message.getFromChain(this, player, chain);
			if(text != null)
				player.sendMessage(text);
		}
	}
	
	public NBTTagCompound getMessageTag()
	{
		return messageData;
	}
	
	public NBTTagCompound getMessageTagForPlayer(EntityPlayer player)
	{
		if(!messageData.contains(player.getCachedUniqueIdString(), 10))
			messageData.put(player.getCachedUniqueIdString(), new NBTTagCompound());
		return messageData.getCompound(player.getCachedUniqueIdString());
	}
	
	@Override
	public Container createContainer(InventoryPlayer playerInventory, EntityPlayer playerIn)
	{
		if(this.stocks != null)
			return new ContainerConsortMerchant(playerIn, stocks);
		else return null;
	}
	
	@Override
	public String getGuiID()
	{
		return GuiHandler.CONSORT_MERCHANT_ID.toString();
	}
}