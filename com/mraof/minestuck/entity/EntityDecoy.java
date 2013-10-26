package com.mraof.minestuck.entity;

import com.mraof.minestuck.util.Debug;
import com.mraof.minestuck.util.EditHandler;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.renderer.ThreadDownloadImageData;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.packet.Packet8UpdateHealth;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ChatMessageComponent;
import net.minecraft.util.ChunkCoordinates;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EntityDamageSource;
import net.minecraft.util.FoodStats;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.EnumGameType;
import net.minecraft.world.World;

public class EntityDecoy extends EntityLivingBase {
	
	final static int USERNAME_OBJECT_ID = 22;
	
	boolean markedForDespawn;
	boolean init;
	FoodStats foodStats = new FoodStats();
	
	public EnumGameType gameType;
	public String username;
	ResourceLocation locationSkin;
	ResourceLocation locationCape;
	ThreadDownloadImageData downloadImageSkin;
	ThreadDownloadImageData downloadImageCape;
	double originX, originY, originZ;
	
	public EntityDecoy(World world){
		super(world);
	}
	
	public EntityDecoy(World world, EntityPlayerMP player) {
		super(world);
		this.boundingBox.setBB(player.boundingBox);
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
//		this.inventory.copyInventory(player.inventory);
		this.getHeldItem();
		this.gameType = player.theItemInWorldManager.getGameType();
		this.setHealth(player.getHealth());
		username = player.username;
//		this.capabilities.isFlying = player.capabilities.isFlying;
		this.foodStats.setFoodLevel(player.getFoodStats().getFoodLevel());
		this.foodStats.setFoodSaturationLevel(player.getFoodStats().getSaturationLevel());
		this.dataWatcher.updateObject(USERNAME_OBJECT_ID, username);
		this.dataWatcher.updateObject(USERNAME_OBJECT_ID+1, this.rotationYawHead);	//Due to rotationYawHead didn't update correctly
	}
	
	@Override
	protected void entityInit() {
		super.entityInit();
		this.dataWatcher.addObject(USERNAME_OBJECT_ID, "");
		this.dataWatcher.addObject(USERNAME_OBJECT_ID+1, 0F);
	}
	
	protected void setupCustomSkin() {	//Just copied from the AbstractClientPlayer, but with a non-final username string.
		if (this.worldObj.isRemote && username != null && !username.isEmpty()){
			locationSkin = AbstractClientPlayer.getLocationSkin(username);
			locationCape = AbstractClientPlayer.getLocationCape(username);
			downloadImageSkin = AbstractClientPlayer.getDownloadImageSkin(locationSkin, username);
			downloadImageCape = AbstractClientPlayer.getDownloadImageCape(locationCape, username);
		}
	}
	
	public ThreadDownloadImageData getTextureSkin() {
		return downloadImageSkin;
	}
	
	public ThreadDownloadImageData getTextureCape() {
		return downloadImageCape;
	}
	
	public ResourceLocation getLocationSkin() {
		if(locationSkin == null)
			return AbstractClientPlayer.locationStevePng;
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
//		foodStats.onUpdate(this);
		if(worldObj.isRemote && !init ){
			username = this.dataWatcher.getWatchableObjectString(USERNAME_OBJECT_ID);
			this.rotationYawHead = this.dataWatcher.getWatchableObjectFloat(USERNAME_OBJECT_ID+1);
			prevRotationYawHead = this.rotationYawHead;
			this.rotationYaw = rotationYawHead;	//I don't know how much of this that is necessary
			prevRotationYaw = rotationYaw;
			renderYawOffset = rotationYaw;
			setupCustomSkin();
			init = true;
		}
		rotationYaw = prevRotationYaw;
		rotationYawHead = prevRotationYawHead;
		rotationPitch = prevRotationPitch;
		
		if(!worldObj.isRemote) {
			if(this.locationChanged())
				EditHandler.reset(null, 0, this, MinecraftServer.getServer().getConfigurationManager().getPlayerForUsername(this.username));
		}
	}
	
	public boolean locationChanged() {
		return originX > posX+1 || originX < posX-1 ||
				originY > posY+1 || originY < posY-1 ||
				originZ > posZ+1 || originZ < posZ-1;
	}
	
	@Override
	public void readFromNBT(NBTTagCompound nbt) {
		this.markedForDespawn = true;
	}
	
	@Override
	public boolean attackEntityFrom(DamageSource damageSource, float par2) {
		if (!worldObj.isRemote && (!gameType.equals(EnumGameType.CREATIVE) || damageSource.canHarmInCreative()))
			EditHandler.reset(damageSource, par2, this, MinecraftServer.getServer().getConfigurationManager().getPlayerForUsername(this.username));
		return true;
	}
	
	@Override
	public boolean getAlwaysRenderNameTagForRender() {
		return username != null;
	}
	
	@Override
	public String getEntityName() {
		return username != null?username:"DECOY";
	}
	
	@Override
	public ItemStack getHeldItem() {
		return null;
	}

	@Override
	public ItemStack getCurrentItemOrArmor(int i) {
		return null;
	}

	@Override
	public void setCurrentItemOrArmor(int i, ItemStack itemstack) {}

	@Override
	public ItemStack[] getLastActiveItems() {
		return new ItemStack[0];
	}
	
}