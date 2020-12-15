package com.mraof.minestuck.entity;

import com.mraof.minestuck.computer.editmode.ServerEditHandler;
import com.mraof.minestuck.util.Debug;
import net.minecraft.entity.EntitySize;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.Pose;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.IPacket;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.DamageSource;
import net.minecraft.util.FoodStats;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Util;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.world.GameType;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.common.util.FakePlayer;
import net.minecraftforge.fml.common.registry.IEntityAdditionalSpawnData;
import net.minecraftforge.fml.network.NetworkHooks;

import java.util.UUID;

public class DecoyEntity extends MobEntity implements IEntityAdditionalSpawnData
{
	
	public boolean isFlying;
	public GameType gameType;
	public String username;
	private UUID playerId;
	private FoodStats foodStats;
	private CompoundNBT foodStatsNBT;
	public CompoundNBT capabilities = new CompoundNBT();
	
	public boolean markedForDespawn;
	private double originX, originY, originZ;
	private DecoyPlayer player;
	
	public PlayerInventory inventory;
	
	public DecoyEntity(World world)
	{
		super(MSEntityTypes.PLAYER_DECOY, world);
		inventory = new PlayerInventory(null);
		if(!world.isRemote)	//If not spawned the way it should
			markedForDespawn = true;
	}
	
	public DecoyEntity(ServerWorld world, ServerPlayerEntity player)
	{
		super(MSEntityTypes.PLAYER_DECOY, world);
		this.setBoundingBox(player.getBoundingBox());
		this.player = new DecoyPlayer(world, this, player);
		for(String key : player.getPersistentData().keySet())
			this.player.getPersistentData().put(key, player.getPersistentData().get(key).copy());
		this.setPosition(player.getPosX(), player.getPosY(), player.getPosZ());
		originX = this.getPosX();
		this.chunkCoordX = player.chunkCoordX;
		originY = this.getPosY();
		this.chunkCoordY = player.chunkCoordY;
		originZ = this.getPosZ();
		this.chunkCoordZ = player.chunkCoordZ;
		this.rotationPitch = player.rotationPitch;
		this.rotationYaw = player.rotationYaw;
		this.rotationYawHead = player.rotationYawHead;
		this.renderYawOffset = player.renderYawOffset;
		this.gameType = player.interactionManager.getGameType();
		initInventory(player);
		this.getAttribute(Attributes.MAX_HEALTH).copyValuesFromInstance(player.getAttribute(Attributes.MAX_HEALTH));
		this.player.getAttribute(Attributes.MAX_HEALTH).copyValuesFromInstance(player.getAttribute(Attributes.MAX_HEALTH));
		this.setHealth(player.getHealth());
		username = player.getGameProfile().getName();
		playerId = player.getUniqueID();
		isFlying = player.abilities.isFlying;
		player.abilities.write(this.capabilities);
		foodStatsNBT = new CompoundNBT();
		player.getFoodStats().write(foodStatsNBT);
		initFoodStats(player);
	}
	
	@Override
	public EntitySize getSize(Pose poseIn)
	{
		return EntityType.PLAYER.getSize();
	}
	
	private void initInventory(ServerPlayerEntity player)
	{
		inventory = this.player.inventory;
		
		inventory.copyInventory(player.inventory);
	}
	
	private void initFoodStats(ServerPlayerEntity sourcePlayer)
	{
		try
		{
			try
			{
				foodStats = new FoodStats();
			} catch(NoSuchMethodError e)
			{
				Debug.info("Custom constructor detected for FoodStats. Trying with player as parameter...");
				try
				{
					foodStats = FoodStats.class.getConstructor(PlayerEntity.class).newInstance(player);
				}
				catch(NoSuchMethodException ex)
				{
					throw new NoSuchMethodException("Found no known constructor for net.minecraft.util.FoodStats.");
				}
			}
			foodStats.read(foodStatsNBT);	//Exact copy of food stack
		} catch(Exception e)
		{
			foodStats = null;
			Debug.logger.error("Couldn't initiate food stats for player decoy. Proceeding to not simulate food stats.", e);
			sourcePlayer.sendMessage(new StringTextComponent("An issue came up while creating the decoy. More info in the server logs."), Util.DUMMY_UUID);
		}
	}
	
	public CompoundNBT getFoodStatsNBT()
	{
		if(foodStats != null)
		{
			CompoundNBT nbt = new CompoundNBT();
			foodStats.write(nbt);
			return nbt;
		} else return foodStatsNBT;
	}
	
	@Override
	public void writeSpawnData(PacketBuffer buffer)
	{
		buffer.writeString(username, 16);
		buffer.writeUniqueId(playerId);
		buffer.writeFloat(rotationYawHead);
		buffer.writeBoolean(isFlying);
	}
	
	@Override
	public void readSpawnData(PacketBuffer additionalData)
	{
		username = additionalData.readString(16);
		playerId = additionalData.readUniqueId();
		rotationYawHead = additionalData.readFloat();
		isFlying = additionalData.readBoolean();
		prevRotationYawHead = rotationYawHead;
		this.rotationYaw = rotationYawHead;	//I don't know how much of this that is necessary
		prevRotationYaw = rotationYaw;
		renderYawOffset = rotationYaw;
	}
	
	@Override
	public IPacket<?> createSpawnPacket()
	{
		return NetworkHooks.getEntitySpawningPacket(this);
	}
	
	public UUID getPlayerID()
	{
		return playerId;
	}
	
	@Override
	public void tick()
	{
		if(markedForDespawn)
		{
			this.remove();
			return;
		}
		super.tick();
		
		rotationYawHead = prevRotationYawHead;	//Neutralize the effect of the LookHelper
		rotationYaw = prevRotationYaw;
		rotationPitch = prevRotationPitch;
		
		if(isFlying)
			this.setPosition(this.getPosX(), prevPosY, this.getPosZ());
		
		if(!world.isRemote)
		{
			if(foodStats != null)
				foodStats.tick(player);
			if(this.locationChanged())
				ServerEditHandler.reset(ServerEditHandler.getData(this));
		}
	}
	
	public boolean locationChanged() {
		return originX >= this.getPosX()+1 || originX <= this.getPosX()-1 ||
				originY >= this.getPosY()+1 || originY <= this.getPosY()-1 ||
				originZ >= this.getPosZ()+1 || originZ <= this.getPosZ()-1;
	}
	
	@Override
	public boolean attackEntityFrom(DamageSource damageSource, float par2) {
		if (!world.isRemote && (!gameType.equals(GameType.CREATIVE) || damageSource.canHarmInCreative()))
			ServerEditHandler.reset(damageSource, par2, ServerEditHandler.getData(this));
		return true;
	}
	
	@Override
	public boolean getAlwaysRenderNameTagForRender() {
		return username != null;
	}
	
	@Override
	public ITextComponent getName()
	{
		return new StringTextComponent(username != null ? username : "DECOY");
	}
	
	@Override
	public ItemStack getItemStackFromSlot(EquipmentSlotType slotIn)
	{
		if(slotIn == EquipmentSlotType.MAINHAND)
			return inventory.getCurrentItem();
		else if(slotIn == EquipmentSlotType.OFFHAND)
			return inventory.offHandInventory.get(0);
		else return inventory.armorInventory.get(slotIn.getIndex());
	}
	
	@Override
	public void setItemStackToSlot(EquipmentSlotType slotIn, ItemStack stack)
	{
		if(slotIn == EquipmentSlotType.MAINHAND)
			inventory.setInventorySlotContents(inventory.currentItem, stack);
		else if(slotIn == EquipmentSlotType.OFFHAND)
			inventory.offHandInventory.set(0, stack);
		else inventory.armorInventory.set(slotIn.getIndex(), stack);
	}
	
	@Override
	public void setHealth(float par1)
	{
		if(player != null)
			player.setHealth(par1);
		super.setHealth(par1);
	}
	
	@Override
	public Iterable<ItemStack> getArmorInventoryList()
	{
		return inventory.armorInventory;
	}
	
	@Override
	public boolean canDespawn(double distanceToClosestPlayer)
	{
		return false;
	}
	
	private static class DecoyPlayer extends FakePlayer	//Never spawned into the world. Only used for the InventoryPlayer and FoodStats.
	{
		
		DecoyEntity decoy;
		
		DecoyPlayer(ServerWorld par1World, DecoyEntity decoy, ServerPlayerEntity player)
		{
			super(par1World, player.getGameProfile());
			player.getServer().getPlayerList().getPlayerAdvancements(player);
			//Fixes annoying NullPointerException when unlocking advancement, caused by just creating the fake player
			this.decoy = decoy;
			this.setHealth(decoy.getHealth());
		}
		
		@Override
		public void heal(float par1)
		{
			decoy.heal(par1);
		}
	}
}