package com.mraof.minestuck.entity;

import java.lang.reflect.Constructor;
import java.util.Set;

import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.renderer.ThreadDownloadImageData;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.DamageSource;
import net.minecraft.util.FoodStats;
import net.minecraft.util.IChatComponent;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraft.world.WorldSettings.GameType;

import com.mojang.authlib.GameProfile;
import com.mraof.minestuck.editmode.ServerEditHandler;
import com.mraof.minestuck.util.Debug;

public class EntityDecoy extends EntityLiving {
	
	final static int DATAWATCHER_ID_START = 22;
	
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
	
	public EntityDecoy(World world){
		super(world);
		player = new DecoyPlayer(world, this);
		inventory = new InventoryPlayer(player);
		if(!world.isRemote)	//If not spawned the way it should
			markedForDespawn = true;
	}
	
	public EntityDecoy(World world, EntityPlayerMP player)
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
		this.gameType = player.theItemInWorldManager.getGameType();
		initInventory(player);
		this.setHealth(player.getHealth());
		username = player.getCommandSenderName();
		isFlying = player.capabilities.isFlying;
		player.capabilities.writeCapabilitiesToNBT(this.capabilities);
		NBTTagCompound nbt = new NBTTagCompound();
		player.getFoodStats().writeNBT(nbt);
		initFoodStats();
		foodStats.readNBT(nbt);	//Exact copy of food stack
		dataWatcher.updateObject(DATAWATCHER_ID_START, username);
		dataWatcher.updateObject(DATAWATCHER_ID_START+1, this.rotationYawHead);	//Due to rotationYawHead didn't update correctly
		dataWatcher.updateObject(DATAWATCHER_ID_START+2, (byte) (isFlying?1:0));
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
			Debug.print("Custom constructor detected for FoodStats. Trying with player as parameter...");
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
	protected void entityInit() {
		super.entityInit();
		this.dataWatcher.addObject(DATAWATCHER_ID_START, "");
		this.dataWatcher.addObject(DATAWATCHER_ID_START+1, 0F);
		dataWatcher.addObject(DATAWATCHER_ID_START+2, (byte)0);
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
			username = this.dataWatcher.getWatchableObjectString(DATAWATCHER_ID_START);
			this.rotationYawHead = this.dataWatcher.getWatchableObjectFloat(DATAWATCHER_ID_START+1);
			prevRotationYawHead = rotationYawHead;
			this.rotationYaw = rotationYawHead;	//I don't know how much of this that is necessary
			prevRotationYaw = rotationYaw;
			renderYawOffset = rotationYaw;
			isFlying = dataWatcher.getWatchableObjectByte(DATAWATCHER_ID_START+2) != 0;
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
				ServerEditHandler.reset(null, 0, ServerEditHandler.getData(this));
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
	public String getCommandSenderName() 
	{
		return username != null ? username : "DECOY";
	}
	
	@Override
	public ItemStack getHeldItem() {
		return getEquipmentInSlot(0);
	}
	
	@Override
	public ItemStack getEquipmentInSlot(int i) {
		if(i == 0)
			return inventory.mainInventory[inventory.currentItem];
		else return inventory.armorInventory[i-1];
	}

	@Override
	public void setCurrentItemOrArmor(int i, ItemStack itemstack) {
		if(i == 0)
			inventory.mainInventory[inventory.currentItem] = itemstack;
		else inventory.armorInventory[i-1] = itemstack;
	}
	
	@Override
	public void setHealth(float par1) {
		if(player != null)
			player.setHealth(par1);
		super.setHealth(par1);
	}
	
	@Override
	public ItemStack[] getInventory()
	{
		return inventory.armorInventory;
	}
	
	@Override
	protected boolean canDespawn() {
		return false;
	}
	
	private static class DecoyPlayer extends EntityPlayer {	//Never spawned into the world. Only used for the InventoryPlayer and FoodStats.
			//TODO Check if you can use an implementation of net.minecraftforge.common.util.FakePlayer instead
		EntityDecoy decoy;
		
		DecoyPlayer(World par1World, EntityDecoy decoy) {
			super(par1World, new GameProfile(null, "Decoy"));
			this.decoy = decoy;
			this.setHealth(decoy.getHealth());
		}
		
		@Override
		public boolean canCommandSenderUseCommand(int i, String s)
		{return false;}
		
		@Override
		public void addChatMessage(IChatComponent var1)
		{}
		
		@Override
		public void heal(float par1)
		{
			decoy.heal(par1);
		}
		
		@Override
		public boolean isSpectator()
		{
			return false;
		}
		
	}
	
}
