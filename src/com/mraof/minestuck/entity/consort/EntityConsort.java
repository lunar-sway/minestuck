package com.mraof.minestuck.entity.consort;

import com.mraof.minestuck.advancements.MinestuckCriteriaTriggers;
import com.mraof.minestuck.entity.EntityMinestuck;
import com.mraof.minestuck.inventory.InventoryConsortMerchant;
import com.mraof.minestuck.world.MinestuckDimensionHandler;
import net.minecraft.entity.EnumCreatureType;
import net.minecraft.entity.IEntityLivingData;
import net.minecraft.entity.ai.*;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.EnumSkyBlock;
import net.minecraft.world.World;

public abstract class EntityConsort extends EntityMinestuck
{
	
	ConsortDialogue.DialogueWrapper message;
	int messageTicksLeft;
	NBTTagCompound messageData;
	public EnumConsort.MerchantType merchantType = EnumConsort.MerchantType.NONE;
	int homeDimension;
	boolean visitedSkaia;
	MessageType.DelayMessage updatingMessage; //Change to an interface/array if more message components need tick updates
	public InventoryConsortMerchant stocks;
	
	public EntityConsort(World world)
	{
		super(world);
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
		if(this.isEntityAlive() && !player.isSneaking())
		{
			if(!world.isRemote)
			{
				if(message == null)
				{
					message = ConsortDialogue.getRandomMessage(this, player);
					messageTicksLeft = 24000 + world.rand.nextInt(24000);
					messageData = new NBTTagCompound();
				}
				ITextComponent text = message.getMessage(this, player);    //TODO Make sure to catch any issues here
				if (text != null)
					player.sendMessage(text);
				MinestuckCriteriaTriggers.CONSORT_TALK.trigger((EntityPlayerMP) player, message.getString(), this);
			}
			
			return true;
		} else
			return super.processInteract(player, hand);
	}
	
	@Override
	public void onLivingUpdate()
	{
		super.onLivingUpdate();
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
	}
	
	@Override
	public boolean getCanSpawnHere()
	{
		return this.isValidLightLevel() && super.getCanSpawnHere();
	}
	
	protected boolean isValidLightLevel()
	{
		int i = MathHelper.floor(this.posX);
		int j = MathHelper.floor(this.getEntityBoundingBox().minY);
		int k = MathHelper.floor(this.posZ);
		BlockPos pos = new BlockPos(i, j, k);
		
		if(this.world.getLightFor(EnumSkyBlock.SKY, pos) < this.rand.nextInt(8))
		{
			return false;
		} else
		{
			int l = this.world.getLightFromNeighbors(pos);
			
			if(this.world.isThundering())
			{
				int i1 = this.world.getSkylightSubtracted();
				this.world.setSkylightSubtracted(10);
				l = this.world.getLightFromNeighbors(pos);
				this.world.setSkylightSubtracted(i1);
			}
			
			return l >= this.rand.nextInt(8);
		}
	}
	
	@Override
	public void writeEntityToNBT(NBTTagCompound compound)
	{
		super.writeEntityToNBT(compound);
		
		if(message != null)
		{
			compound.setString("dialogue", message.getString());
			compound.setInteger("messageTicks", messageTicksLeft);
			compound.setTag("messageData", messageData);
		}
		
		compound.setInteger("type", merchantType.ordinal());
		compound.setInteger("homeDim", homeDimension);
		
		if(merchantType != EnumConsort.MerchantType.NONE && stocks != null)
			compound.setTag("stock", stocks.writeToNBT());
		
		if(hasHome())
		{
			NBTTagCompound nbt = new NBTTagCompound();
			BlockPos home = getHomePosition();
			nbt.setInteger("homeX", home.getX());
			nbt.setInteger("homeY", home.getY());
			nbt.setInteger("homeZ", home.getZ());
			nbt.setInteger("maxHomeDistance", (int) getMaximumHomeDistance());
			compound.setTag("homePos", nbt);
		}
		
		compound.setBoolean("skaia", visitedSkaia);
	}
	
	@Override
	public void readEntityFromNBT(NBTTagCompound compound)
	{
		super.readEntityFromNBT(compound);
		
		if(compound.hasKey("dialogue", 8))
		{
			message = ConsortDialogue.getMessageFromString(compound.getString("dialogue"));
			if(compound.hasKey("messageTicks", 99))
				messageTicksLeft = compound.getInteger("messageTicks");
			else messageTicksLeft = 24000;	//Used to make summoning with a specific message slightly easier
			messageData = compound.getCompoundTag("messageData");
		}
		
		merchantType = EnumConsort.MerchantType.values()[MathHelper.clamp(compound.getInteger("type"), 0, EnumConsort.MerchantType.values().length - 1)];
		
		if(compound.hasKey("homeDim", 99))
			homeDimension = compound.getInteger("homeDim");
		else homeDimension = this.world.provider.getDimension();
		
		if(merchantType != EnumConsort.MerchantType.NONE && compound.hasKey("stock", 9))
		{
			stocks = new InventoryConsortMerchant(this, compound.getTagList("stock", 10));
		}
		
		if(compound.hasKey("homePos", 10))
		{
			NBTTagCompound nbt = compound.getCompoundTag("homePos");
			BlockPos pos = new BlockPos(nbt.getInteger("homeX"), nbt.getInteger("homeY"), nbt.getInteger("homeZ"));
			setHomePosAndDistance(pos, nbt.getInteger("maxHomeDistance"));
		}
		
		visitedSkaia = compound.getBoolean("skaia");
		
		applyAdditionalAITasks();
	}
	
	@Override
	public IEntityLivingData onInitialSpawn(DifficultyInstance difficulty, IEntityLivingData livingdata)
	{
		if(merchantType == EnumConsort.MerchantType.NONE && this.rand.nextInt(30) == 0)
		{
			merchantType = EnumConsort.MerchantType.SHADY;
			if(hasHome())
				setHomePosAndDistance(getHomePosition(), (int) (getMaximumHomeDistance()*0.4F));
		}
		
		homeDimension = world.provider.getDimension();
		visitedSkaia = rand.nextFloat() < 0.1F;
		
		applyAdditionalAITasks();
		
		return super.onInitialSpawn(difficulty, livingdata);
	}
	
	@Override
	protected boolean canDespawn()
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
	
	public void commandReply(EntityPlayer player, String chain)
	{
		if(this.isEntityAlive() && !world.isRemote && message != null)
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
		if(!messageData.hasKey(player.getCachedUniqueIdString(), 10))
			messageData.setTag(player.getCachedUniqueIdString(), new NBTTagCompound());
		return messageData.getCompoundTag(player.getCachedUniqueIdString());
	}
}