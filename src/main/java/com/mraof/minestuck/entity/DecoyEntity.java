package com.mraof.minestuck.entity;

import com.mraof.minestuck.computer.editmode.ServerEditHandler;
import com.mraof.minestuck.util.Debug;
import net.minecraft.client.entity.player.AbstractClientPlayerEntity;
import net.minecraft.client.renderer.texture.DownloadingTexture;
import net.minecraft.entity.*;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.IPacket;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.DamageSource;
import net.minecraft.util.FoodStats;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.world.GameType;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.common.util.FakePlayer;
import net.minecraftforge.fml.network.NetworkHooks;

public class DecoyEntity extends MobEntity
{
	
	private static final DataParameter<String> USERNAME = EntityDataManager.createKey(DecoyEntity.class, DataSerializers.STRING);
	private static final DataParameter<Float> ROTATION_YAW_HEAD = EntityDataManager.createKey(DecoyEntity.class, DataSerializers.FLOAT);
	private static final DataParameter<Boolean> FLYING = EntityDataManager.createKey(DecoyEntity.class, DataSerializers.BOOLEAN);
	
	public boolean isFlying;
	public GameType gameType;
	public String username;
	private FoodStats foodStats;
	private CompoundNBT foodStatsNBT;
	public CompoundNBT capabilities = new CompoundNBT();
	
	public boolean markedForDespawn;
	boolean init;
	double originX, originY, originZ;
	DecoyPlayer player;
	
	ResourceLocation locationSkin;
	ResourceLocation locationCape;
	DownloadingTexture downloadImageSkin;
	DownloadingTexture downloadImageCape;
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
		this.posX = player.posX;
		originX = posX;
		this.chunkCoordX = player.chunkCoordX;
		this.posY = player.posY;
		originY = posY;
		this.chunkCoordY = player.chunkCoordY;
		this.posZ = player.posZ;
		originZ = posZ;
		this.chunkCoordZ = player.chunkCoordZ;
		this.rotationPitch = player.rotationPitch;
		this.rotationYaw = player.rotationYaw;
		this.rotationYawHead = player.rotationYawHead;
		this.renderYawOffset = player.renderYawOffset;
		this.gameType = player.interactionManager.getGameType();
		initInventory(player);
		player.getAttribute(SharedMonsterAttributes.MAX_HEALTH).getModifiers().forEach(attributeModifier ->
				this.getAttribute(SharedMonsterAttributes.MAX_HEALTH).applyModifier(attributeModifier));
		player.getAttribute(SharedMonsterAttributes.MAX_HEALTH).getModifiers().forEach(attributeModifier ->
				this.player.getAttribute(SharedMonsterAttributes.MAX_HEALTH).applyModifier(attributeModifier));
		this.setHealth(player.getHealth());
		username = player.getName().getFormattedText();
		isFlying = player.abilities.isFlying;
		player.abilities.write(this.capabilities);
		foodStatsNBT = new CompoundNBT();
		player.getFoodStats().write(foodStatsNBT);
		initFoodStats(player);
		dataManager.set(USERNAME, username.toString());
		dataManager.set(ROTATION_YAW_HEAD, this.rotationYawHead);	//Due to rotationYawHead didn't update correctly
		dataManager.set(FLYING, isFlying);
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
			sourcePlayer.sendMessage(new StringTextComponent("An issue came up while creating the decoy. More info in the server logs."));
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
	protected void registerData()
	{
		super.registerData();
		dataManager.register(USERNAME, "");
		dataManager.register(ROTATION_YAW_HEAD, 0F);
		dataManager.register(FLYING, false);
	}
	
	protected void setupCustomSkin()
	{
		if (this.world.isRemote && username != null && !username.isEmpty()){
			locationSkin = AbstractClientPlayerEntity.getLocationSkin(username);
			//locationCape = AbstractClientPlayer.getLocationCape(username);
			downloadImageSkin = AbstractClientPlayerEntity.getDownloadImageSkin(locationSkin, username);
			//downloadImageCape = AbstractClientPlayer.getDownloadImageCape(locationCape, username);
		}
	}
	
	@Override
	public IPacket<?> createSpawnPacket()
	{
		return NetworkHooks.getEntitySpawningPacket(this);
	}
	
	public DownloadingTexture getTextureSkin()
	{
		return downloadImageSkin;
	}
	
	public DownloadingTexture getTextureCape()
	{
		return downloadImageCape;
	}
	
	public ResourceLocation getLocationSkin()
	{
//		if(locationSkin == null)
//			return AbstractClientPlayer.locationStevePng;
		return locationSkin;
	}
	
	public ResourceLocation getLocationCape()
	{
		return locationCape;
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
		if(world.isRemote && !init ){
			username = dataManager.get(USERNAME);
			this.rotationYawHead = dataManager.get(ROTATION_YAW_HEAD);
			prevRotationYawHead = rotationYawHead;
			this.rotationYaw = rotationYawHead;	//I don't know how much of this that is necessary
			prevRotationYaw = rotationYaw;
			renderYawOffset = rotationYaw;
			isFlying = dataManager.get(FLYING);
			setupCustomSkin();
			init = true;
		}
		rotationYawHead = prevRotationYawHead;	//Neutralize the effect of the LookHelper
		rotationYaw = prevRotationYaw;
		rotationPitch = prevRotationPitch;
		
		if(isFlying)
			posY = prevPosY;
		
		if(!world.isRemote)
		{
			if(foodStats != null)
				foodStats.tick(player);
			if(this.locationChanged())
				ServerEditHandler.reset(ServerEditHandler.getData(this));
		}
	}
	
	public boolean locationChanged() {
		return originX >= posX+1 || originX <= posX-1 ||
				originY >= posY+1 || originY <= posY-1 ||
				originZ >= posZ+1 || originZ <= posZ-1;
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