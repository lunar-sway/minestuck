package com.mraof.minestuck.entity;

import com.mraof.minestuck.util.Debug;
import com.mraof.minestuck.util.EditHandler;

import net.minecraft.client.entity.EntityOtherPlayerMP;
import net.minecraft.client.renderer.ThreadDownloadImageData;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ChatMessageComponent;
import net.minecraft.util.ChunkCoordinates;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EntityDamageSource;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.EnumGameType;
import net.minecraft.world.World;

public class EntityDecoy extends EntityOtherPlayerMP{
	
	final static int USERNAME_OBJECT_ID = 22;
	
	public EnumGameType gameType;
	public String username;
	ResourceLocation locationSkin;
	ResourceLocation locationCape;
	ThreadDownloadImageData downloadImageSkin;
	ThreadDownloadImageData downloadImageCape;
	
	public EntityDecoy(World world){
		super(world, "DECOY");
	}
	
	public EntityDecoy(World par1World, EntityPlayerMP player) {
		super(par1World, player.username);
		this.posX = player.posX;
		this.chunkCoordX = player.chunkCoordX;
		this.posY = player.posY;
		this.chunkCoordY = player.chunkCoordY;
		this.posZ = player.posZ;
		this.chunkCoordZ = player.chunkCoordZ;
		this.rotationPitch = player.rotationPitch;
		this.rotationYaw = player.rotationYaw;
		this.rotationYawHead = player.rotationYawHead;
		this.gameType = player.theItemInWorldManager.getGameType();
		this.setHealth(player.getHealth());
		this.username = player.username;
		this.dataWatcher.updateObject(this.USERNAME_OBJECT_ID, username);
	}
	
	@Override
	protected void entityInit() {
		super.entityInit();
		this.dataWatcher.addObject(this.USERNAME_OBJECT_ID, "");
	}
	
	@Override
	protected void setupCustomSkin() {	//Just copied from the AbstractClientPlayer, but with a non-final username string.
		if (this.worldObj.isRemote && username != null && !username.isEmpty()){
			locationSkin = getLocationSkin(username);
			locationCape = getLocationCape(username);
			downloadImageSkin = getDownloadImageSkin(locationSkin, username);
			downloadImageCape = getDownloadImageCape(locationCape, username);
		}
	}
	
	@Override
	public ThreadDownloadImageData getTextureSkin() {
		return downloadImageSkin;
	}
	
	@Override
	public ThreadDownloadImageData getTextureCape() {
		return downloadImageCape;
	}
	
	@Override
	public ResourceLocation getLocationSkin() {
		if(locationSkin == null)
			return this.locationStevePng;
		else return locationSkin;
	}
	
	@Override
	public ResourceLocation getLocationCape() {
		return locationCape;
	}
	
	@Override
	public void onUpdate() {
		super.onUpdate();
		if(worldObj.isRemote && username == null) {
			username = dataWatcher.getWatchableObjectString(USERNAME_OBJECT_ID);
			if(username != null) {
				setupCustomSkin();
			}
		}
		else if(!worldObj.isRemote && this.velocityChanged)
			EditHandler.reset(null, 0, this, MinecraftServer.getServer().getConfigurationManager().getPlayerForUsername(this.username));
	}
	
	@Override
	public void sendChatToPlayer(ChatMessageComponent chatmessagecomponent) {}
	
	@Override
	public boolean canCommandSenderUseCommand(int i, String s) {return false;}

	@Override
	public ChunkCoordinates getPlayerCoordinates() {
		return new ChunkCoordinates(this.chunkCoordX,this.chunkCoordY,this.chunkCoordZ);
	}
	
	@Override
	public void readFromNBT(NBTTagCompound nbt) {
		this.setDead();
	}
	
	@Override
	public String getDisplayName() {
		return username == null?"DECOY":username;
	}
	
	@Override
	public boolean attackEntityFrom(DamageSource damageSource, float par2) {
		if (!worldObj.isRemote && (!gameType.equals(EnumGameType.CREATIVE) || damageSource.equals(DamageSource.outOfWorld)))
			EditHandler.reset(damageSource, par2, this, MinecraftServer.getServer().getConfigurationManager().getPlayerForUsername(this.username));
		return true;
	}
}
