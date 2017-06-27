package com.mraof.minestuck.entity.consort;

import net.minecraft.entity.IEntityLivingData;
import net.minecraft.entity.ai.EntityAILookIdle;
import net.minecraft.entity.ai.EntityAIWander;
import net.minecraft.entity.ai.EntityAIWatchClosest;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.EnumSkyBlock;
import net.minecraft.world.World;

import com.mraof.minestuck.entity.EntityMinestuck;

public abstract class EntityConsort extends EntityMinestuck
{
	
	ConsortDialogue.ConditionedMessage message;
	int messageTicksLeft;
	NBTTagCompound messageData;
	EnumConsort.MerchantType merchantType = EnumConsort.MerchantType.NONE;
	int homeDimension;
	
	public EntityConsort(World world)
	{
		super(world);
		setSize(0.6F, 1.5F);
		this.experienceValue = 1;
		this.tasks.addTask(5, new EntityAIWander(this, 0.6F));
		this.tasks.addTask(6, new EntityAILookIdle(this));
		this.tasks.addTask(6, new EntityAIWatchClosest(this, EntityPlayer.class, 8.0F));
	}
	
	@Override
	protected float getMaximumHealth()
	{
		return 10;
	}
	
	@Override
	protected boolean processInteract(EntityPlayer player, EnumHand hand, ItemStack stack)
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
				ITextComponent text = message.getMessage(this, player);
				if(text != null)
					player.sendMessage(text);
			}
			
			return true;
		} else
			return super.processInteract(player, hand, stack);
	}
	
	@Override
	public void onLivingUpdate()
	{
		super.onLivingUpdate();
		if(messageTicksLeft > 0)
			messageTicksLeft--;
		else
		{
			message = null;
			messageData = null;
		}
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
		
		compound.setInteger("merchant", merchantType.ordinal());
		compound.setInteger("homeDim", homeDimension);
	}
	
	@Override
	public void readEntityFromNBT(NBTTagCompound compound)
	{
		super.readEntityFromNBT(compound);
		
		if(compound.hasKey("dialogue", 8))
		{
			message = ConsortDialogue.getMessageFromString(compound.getString("dialogue"));
			messageTicksLeft = compound.getInteger("messageTicks");
			messageData = compound.getCompoundTag("messageData");
		}
		
		merchantType = EnumConsort.MerchantType.values()[MathHelper.clamp(compound.getInteger("merchant"), 0, EnumConsort.MerchantType.values().length - 1)];
		
		if(compound.hasKey("homeDim", 99))
			homeDimension = compound.getInteger("homeDim");
		else homeDimension = this.world.provider.getDimension();
	}
	
	@Override
	public IEntityLivingData onInitialSpawn(DifficultyInstance difficulty, IEntityLivingData livingdata)
	{
		if(this.rand.nextInt(30) == 0)
			merchantType = EnumConsort.MerchantType.SHADY;
		
		homeDimension = world.provider.getDimension();
		
		return super.onInitialSpawn(difficulty, livingdata);
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