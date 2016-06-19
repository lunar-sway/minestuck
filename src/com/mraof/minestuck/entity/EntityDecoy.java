package com.mraof.minestuck.entity;

import java.lang.reflect.Constructor;
import java.util.Arrays;
import java.util.Set;

import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.renderer.ThreadDownloadImageData;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.DamageSource;
import net.minecraft.util.FoodStats;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraft.world.WorldSettings.GameType;
import net.minecraftforge.common.util.FakePlayer;

import com.mojang.authlib.GameProfile;
import com.mraof.minestuck.editmode.ServerEditHandler;
import com.mraof.minestuck.util.Debug;

public class EntityDecoy extends EntityLiving {
	
	private static final DataParameter<String> USERNAME = EntityDataManager.createKey(EntityDecoy.class, DataSerializers.STRING);
	private static final DataParameter<Float> ROTATION_YAW_HEAD = EntityDataManager.createKey(EntityDecoy.class, DataSerializers.FLOAT);
	private static final DataParameter<Boolean> FLYING = EntityDataManager.createKey(EntityDecoy.class, DataSerializers.BOOLEAN);
	
	public boolean isFlying;
	public GameType gameType;
	public String username;
	public FoodStats foodStats;
	public NBTTagCompound capabilities = new NBTTagCompound();
	
	public boolean markedForDespawn;
	boolean init;
	double originX, originY, originZ;
	DecoyPlayer player;
	
	ResourceLocation locationSkin;
	ResourceLocation locationCape;
	ThreadDownloadImageData downloadImageSkin;
	ThreadDownloadImageData downloadImageCape;
	public InventoryPlayer inventory;
	
	public EntityDecoy(World world)
	{
		super(world);
		inventory = new InventoryPlayer(null);
		if(!world.isRemote)	//If not spawned the way it should
			markedForDespawn = true;
	}
	
	public EntityDecoy(WorldServer world, EntityPlayerMP player)
	{
		super(world);
		this.setEntityBoundingBox(player.getEntityBoundingBox());
		height = player.height;
		this.player = new DecoyPlayer(world, this);
		for(String key : (Set<String>) player.getEntityData().getKeySet())
			this.player.getEntityData().setTag(key, player.getEntityData().getTag(key).copy());
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
		this.setHealth(player.getHealth());
		username = player.getName();
		isFlying = player.capabilities.isFlying;
		player.capabilities.writeCapabilitiesToNBT(this.capabilities);
		NBTTagCompound nbt = new NBTTagCompound();
		player.getFoodStats().writeNBT(nbt);
		initFoodStats();
		foodStats.readNBT(nbt);	//Exact copy of food stack
		dataManager.set(USERNAME, username);
		dataManager.set(ROTATION_YAW_HEAD, this.rotationYawHead);	//Due to rotationYawHead didn't update correctly
		dataManager.set(FLYING, isFlying);
	}
	
	private void initInventory(EntityPlayerMP player)
	{
		inventory = new InventoryPlayer(this.player);
		this.player.inventory = inventory;
		if(player.inventory.getClass() != InventoryPlayer.class)	//Custom inventory class
		{
			Class<? extends InventoryPlayer> c = player.inventory.getClass();
			try
			{
				Constructor<? extends InventoryPlayer> constructor = c.getConstructor(EntityPlayer.class);
				inventory = constructor.newInstance(this.player);
				this.player.inventory = inventory;
			} catch(Exception e)
			{
				throw new IllegalStateException("The custom inventory class \""+c.getName()+"\" is not supported.");
			}
		}
		
		inventory.copyInventory(player.inventory);
	}
	
	private void initFoodStats()
	{
		try
		{
			foodStats = new FoodStats();
		}
		catch(NoSuchMethodError e)
		{
			Debug.info("Custom constructor detected for FoodStats. Trying with player as parameter...");
			try
			{
				foodStats = FoodStats.class.getConstructor(EntityPlayer.class).newInstance(player);
			}
			catch(NoSuchMethodException ex)
			{
				throw new NoSuchMethodError("Found no known constructor for net.minecraft.util.FoodStats.");
			}
			catch(Exception ex)
			{
				throw new RuntimeException(ex);	//No idea what sort of exception that should go here
			}
		}
	}
	
	@Override
	protected void entityInit()
	{
		super.entityInit();
		dataManager.register(USERNAME, "");
		dataManager.register(ROTATION_YAW_HEAD, 0F);
		dataManager.register(FLYING, false);
	}
	
	protected void setupCustomSkin() {
		if (this.worldObj.isRemote && username != null && !username.isEmpty()){
			locationSkin = AbstractClientPlayer.getLocationSkin(username);
			//locationCape = AbstractClientPlayer.getLocationCape(username);
			downloadImageSkin = AbstractClientPlayer.getDownloadImageSkin(locationSkin, username);
			//downloadImageCape = AbstractClientPlayer.getDownloadImageCape(locationCape, username);
		}
	}
	
	public ThreadDownloadImageData getTextureSkin() {
		return downloadImageSkin;
	}
	
	public ThreadDownloadImageData getTextureCape() {
		return downloadImageCape;
	}
	
	public ResourceLocation getLocationSkin() {
//		if(locationSkin == null)
//			return AbstractClientPlayer.locationStevePng;
		return locationSkin;
	}
	
	public ResourceLocation getLocationCape() {
		return locationCape;
	}
	
	@Override
	public void onUpdate() {
		if(markedForDespawn){
			this.setDead();
			return;
		}
		super.onUpdate();
		if(worldObj.isRemote && !init ){
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
		
		if(!worldObj.isRemote)
		{
			foodStats.onUpdate(player);
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
		if (!worldObj.isRemote && (!gameType.equals(GameType.CREATIVE) || damageSource.canHarmInCreative()))
			ServerEditHandler.reset(damageSource, par2, ServerEditHandler.getData(this));
		return true;
	}
	
	@Override
	public boolean getAlwaysRenderNameTagForRender() {
		return username != null;
	}
	
	@Override
	public String getName() 
	{
		return username != null ? username : "DECOY";
	}
	
	@Override
	public ItemStack getItemStackFromSlot(EntityEquipmentSlot slotIn)
	{
		if(slotIn == EntityEquipmentSlot.MAINHAND)
			return inventory.getCurrentItem();
		else if(slotIn == EntityEquipmentSlot.OFFHAND)
			return inventory.offHandInventory[0];
		else return inventory.armorItemInSlot(slotIn.getIndex());
	}
	
	@Override
	public void setItemStackToSlot(EntityEquipmentSlot slotIn, ItemStack stack)
	{
		if(slotIn == EntityEquipmentSlot.MAINHAND)
			inventory.setInventorySlotContents(inventory.currentItem, stack);
		else if(slotIn == EntityEquipmentSlot.OFFHAND)
			inventory.offHandInventory[0] = stack;
		else inventory.armorInventory[slotIn.getIndex()] = stack;	//Couldn't find a good method to replace this
	}
	
	@Override
	public void setHealth(float par1) {
		if(player != null)
			player.setHealth(par1);
		super.setHealth(par1);
	}
	
	@Override
	public Iterable<ItemStack> getArmorInventoryList()
	{
		return Arrays.asList(inventory.armorInventory);
	}
	
	@Override
	protected boolean canDespawn() {
		return false;
	}
	
	private static class DecoyPlayer extends FakePlayer	//Never spawned into the world. Only used for the InventoryPlayer and FoodStats.
	{
		
		EntityDecoy decoy;
		
		DecoyPlayer(WorldServer par1World, EntityDecoy decoy)
		{
			super(par1World, new GameProfile(null, "Decoy"));
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