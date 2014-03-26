//package com.mraof.minestuck.entity;
//
//import net.minecraft.client.entity.AbstractClientPlayer;
//import net.minecraft.client.renderer.ThreadDownloadImageData;
//import net.minecraft.entity.EntityLiving;
//import net.minecraft.entity.player.EntityPlayer;
//import net.minecraft.entity.player.EntityPlayerMP;
//import net.minecraft.entity.player.InventoryPlayer;
//import net.minecraft.item.ItemStack;
//import net.minecraft.nbt.NBTTagCompound;
//import net.minecraft.util.ChunkCoordinates;
//import net.minecraft.util.DamageSource;
//import net.minecraft.util.FoodStats;
//import net.minecraft.util.IChatComponent;
//import net.minecraft.util.ResourceLocation;
//import net.minecraft.world.World;
//import net.minecraft.world.WorldSettings.GameType;
//
//import com.mojang.authlib.GameProfile;
//import com.mraof.minestuck.editmode.ServerEditHandler;
//
//public class EntityDecoy extends EntityLiving {
//	
//	final static int DATAWATCHER_ID_START = 22;
//	
//	public boolean isFlying;
//	public GameType gameType;
//	public String username;
//	public FoodStats foodStats = new FoodStats();
//	public NBTTagCompound capabilities = new NBTTagCompound();
//	
//	public boolean markedForDespawn;
//	boolean init;
//	double originX, originY, originZ;
//	DecoyPlayer player;
//	
//	ResourceLocation locationSkin;
//	ResourceLocation locationCape;
//	ThreadDownloadImageData downloadImageSkin;
//	ThreadDownloadImageData downloadImageCape;
//	public InventoryPlayer inventory;
//	
//	public EntityDecoy(World world){
//		super(world);
//		player = new DecoyPlayer(world, this);
//		inventory = new InventoryPlayer(player);
//		if(!world.isRemote)	//If not spawned the way it should
//			markedForDespawn = true;
//	}
//	
//	public EntityDecoy(World world, EntityPlayerMP player) {
//		super(world);
//		this.boundingBox.setBB(player.boundingBox);
//		height = player.height;
//		this.player = new DecoyPlayer(world, this);
//		this.posX = player.posX;
//		originX = posX;
//		this.chunkCoordX = player.chunkCoordX;
//		this.posY = player.posY;
//		originY = posY;
//		this.chunkCoordY = player.chunkCoordY;
//		this.posZ = player.posZ;
//		originZ = posZ;
//		this.chunkCoordZ = player.chunkCoordZ;
//		this.rotationPitch = player.rotationPitch;
//		this.rotationYaw = player.rotationYaw;
//		this.rotationYawHead = player.rotationYawHead;
//		this.renderYawOffset = player.renderYawOffset;
//		inventory = new InventoryPlayer(this.player);
//		this.inventory.copyInventory(player.inventory);
//		this.getHeldItem();
//		this.gameType = player.theItemInWorldManager.getGameType();
//		this.setHealth(player.getHealth());
//		username = player.getCommandSenderName();
//		isFlying = player.capabilities.isFlying;
//		player.capabilities.writeCapabilitiesToNBT(this.capabilities);
//		NBTTagCompound nbt = new NBTTagCompound();
//		player.getFoodStats().writeNBT(nbt);
//		foodStats.readNBT(nbt);	//Exact copy of food stack
//		dataWatcher.updateObject(DATAWATCHER_ID_START, username);
//		dataWatcher.updateObject(DATAWATCHER_ID_START+1, this.rotationYawHead);	//Due to rotationYawHead didn't update correctly
//		dataWatcher.updateObject(DATAWATCHER_ID_START+2, (byte) (isFlying?1:0));
//	}
//	
//	@Override
//	protected void entityInit() {
//		super.entityInit();
//		this.dataWatcher.addObject(DATAWATCHER_ID_START, "");
//		this.dataWatcher.addObject(DATAWATCHER_ID_START+1, 0F);
//		dataWatcher.addObject(DATAWATCHER_ID_START+2, (byte)0);
//	}
//	
//	protected void setupCustomSkin() {	//Just copied from the AbstractClientPlayer, but with a non-final username string.
//		if (this.worldObj.isRemote && username != null && !username.isEmpty()){
//			locationSkin = AbstractClientPlayer.getLocationSkin(username);
//			locationCape = AbstractClientPlayer.getLocationCape(username);
//			downloadImageSkin = AbstractClientPlayer.getDownloadImageSkin(locationSkin, username);
//			downloadImageCape = AbstractClientPlayer.getDownloadImageCape(locationCape, username);
//		}
//	}
//	
//	public ThreadDownloadImageData getTextureSkin() {
//		return downloadImageSkin;
//	}
//	
//	public ThreadDownloadImageData getTextureCape() {
//		return downloadImageCape;
//	}
//	
//	public ResourceLocation getLocationSkin() {
//		if(locationSkin == null)
//			return AbstractClientPlayer.locationStevePng;
//		return locationSkin;
//	}
//	
//	public ResourceLocation getLocationCape() {
//		return locationCape;
//	}
//	
//	@Override
//	public void onUpdate() {
//		if(markedForDespawn){
//			this.setDead();
//			return;
//		}
//		super.onUpdate();
//		foodStats.onUpdate(player);
//		if(worldObj.isRemote && !init ){
//			username = this.dataWatcher.getWatchableObjectString(DATAWATCHER_ID_START);
//			this.rotationYawHead = this.dataWatcher.getWatchableObjectFloat(DATAWATCHER_ID_START+1);
//			prevRotationYawHead = rotationYawHead;
//			this.rotationYaw = rotationYawHead;	//I don't know how much of this that is necessary
//			prevRotationYaw = rotationYaw;
//			renderYawOffset = rotationYaw;
//			isFlying = dataWatcher.getWatchableObjectByte(DATAWATCHER_ID_START+2) != 0;
//			setupCustomSkin();
//			init = true;
//		}
//		rotationYawHead = prevRotationYawHead;	//Neutralize the effect of the LookHelper
//		rotationYaw = prevRotationYaw;
//		rotationPitch = prevRotationPitch;
//		
//		if(isFlying)
//			posY = prevPosY;
//		
//		if(!worldObj.isRemote) {
//			if(this.locationChanged())
//				ServerEditHandler.reset(null, 0, ServerEditHandler.getData(this));
//		}
//	}
//	
//	public boolean locationChanged() {
//		return originX >= posX+1 || originX <= posX-1 ||
//				originY >= posY+1 || originY <= posY-1 ||
//				originZ >= posZ+1 || originZ <= posZ-1;
//	}
//	
//	@Override
//	public boolean attackEntityFrom(DamageSource damageSource, float par2) {
//		if (!worldObj.isRemote && (!gameType.equals(GameType.CREATIVE) || damageSource.canHarmInCreative()))
//			ServerEditHandler.reset(damageSource, par2, ServerEditHandler.getData(this));
//		return true;
//	}
//	
//	@Override
//	public boolean getAlwaysRenderNameTagForRender() {
//		return username != null;
//	}
//	
//	@Override
//	public String getCustomNameTag() 
//	{
//		return username != null ? username : "DECOY";
//	}
//	
//	@Override
//	public ItemStack getHeldItem() {
//		return getEquipmentInSlot(0);
//	}
//	
//	@Override
//	public ItemStack getEquipmentInSlot(int i) {
//		if(i == 0)
//			return inventory.mainInventory[inventory.currentItem];
//		else return inventory.armorInventory[i-1];
//	}
//
//	@Override
//	public void setCurrentItemOrArmor(int i, ItemStack itemstack) {
//		if(i == 0)
//			inventory.mainInventory[inventory.currentItem] = itemstack;
//		else inventory.armorInventory[i-1] = itemstack;
//	}
//	
//	@Override
//	public void setHealth(float par1) {
//		if(player != null)
//			player.setHealth(par1);
//		super.setHealth(par1);
//	}
//	
//	@Override
//	public ItemStack[] getLastActiveItems() {
//		return inventory.armorInventory;
//	}
//	
//	@Override
//	protected boolean canDespawn() {
//		return false;
//	}
//	
//	private static class DecoyPlayer extends EntityPlayer {	//Never spawned into the world. Only used for the InventoryPlayer and FoodStats.
//		
//		EntityDecoy decoy;
//		
//		DecoyPlayer(World par1World, EntityDecoy decoy) {
//			super(par1World, new GameProfile("Decoy", "Decoy"));
//			this.decoy = decoy;
//			this.setHealth(decoy.getHealth());
//		}
//		
//		@Override
//		public boolean canCommandSenderUseCommand(int i, String s) {return false;}
//		
//		@Override
//		public void addChatMessage(IChatComponent var1) {}
//		
//		@Override
//		public ChunkCoordinates getPlayerCoordinates() {return null;}
//		
//		@Override
//		public void heal(float par1) {
//			decoy.heal(par1);
//		}
//		
//	}
//	
//}