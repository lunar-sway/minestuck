package com.mraof.minestuck.util;

import java.lang.reflect.Field;
import java.util.Iterator;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.play.server.SPacketEntityEffect;
import net.minecraft.network.play.server.SPacketPlayerAbilities;
import net.minecraft.network.play.server.SPacketRespawn;
import net.minecraft.potion.PotionEffect;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.management.PlayerList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.WorldServer;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.common.util.FakePlayer;
import net.minecraftforge.fml.common.FMLCommonHandler;

public class Teleport
{
	public static boolean teleportEntity(Entity entity, int destinationDimension, ITeleporter teleporter, BlockPos dest)
	{
		return teleportEntity(entity, destinationDimension, teleporter, dest.getX(), dest.getY(), dest.getZ());
	}
	
	public static boolean teleportEntity(Entity entity, int destinationDimension, ITeleporter teleporter)
	{
		return teleportEntity(entity, destinationDimension, teleporter, entity.posX, entity.posY, entity.posZ);
	}
	
	public static boolean teleportEntity(Entity entity, int destinationDimension, ITeleporter teleporter, double x, double y, double z)
	{
		if(!ForgeHooks.onTravelToDimension(entity, destinationDimension))
			return false;
		MinecraftServer mcServer = entity.getServer();
		int prevDimension = entity.dimension;
		WorldServer worldFrom = mcServer.worldServerForDimension(prevDimension);
		WorldServer worldDest = mcServer.worldServerForDimension(destinationDimension);
		
		if(entity instanceof EntityPlayerMP)
		{
			PlayerList playerList = mcServer.getPlayerList();
			EntityPlayerMP player = (EntityPlayerMP) entity;
			try
			{
				setPortalInvincibilityWithReflection(player);
			} catch(Exception e)
			{
				Debug.warn("Failed to set portal invincibility through reflection. Portal problems might occur. Problem: "+e.getMessage());
			}
			player.dimension = destinationDimension;
			
			SPacketRespawn respawnPacket = new SPacketRespawn(player.dimension, player.worldObj.getDifficulty(), worldDest.getWorldInfo().getTerrainType(), player.interactionManager.getGameType());
			player.connection.sendPacket(respawnPacket);
			playerList.updatePermissionLevel(player);
			worldFrom.removeEntityDangerously(player);
			player.isDead = false;
			
			player.setPosition(x, y, z);
			if(teleporter != null)
				teleporter.makeDestination(player, worldFrom, worldDest);
			worldDest.spawnEntityInWorld(player);
			worldDest.updateEntityWithOptionalForce(entity, false);
			player.setWorld(worldDest);
			
			playerList.preparePlayer(player, worldFrom);
			
			player.connection.setPlayerLocation(player.posX, player.posY, player.posZ, player.rotationYaw, player.rotationPitch);
			player.interactionManager.setWorld(worldDest);
			player.connection.sendPacket(new SPacketPlayerAbilities(player.capabilities));
			
			mcServer.getPlayerList().updateTimeAndWeatherForPlayer(player, worldDest);
			mcServer.getPlayerList().syncPlayerInventory(player);
			Iterator<?> iterator = player.getActivePotionEffects().iterator();
			
			while (iterator.hasNext())
			{
				PotionEffect potioneffect = (PotionEffect)iterator.next();
				player.connection.sendPacket(new SPacketEntityEffect(player.getEntityId(), potioneffect));
			}
			
			FMLCommonHandler.instance().firePlayerChangedDimensionEvent(player, prevDimension, destinationDimension);
			
			return true;
		}
		else if (!entity.worldObj.isRemote && !entity.isDead)
		{
			Entity newEntity = EntityList.createEntityByName(EntityList.getEntityString(entity), worldDest);
			if(newEntity == null)
				return false;
			
			entity.dimension = destinationDimension;
			
			entity.worldObj.removeEntity(entity);
			entity.isDead = false;
			
			entity.setPosition(x, y, z);
			if(teleporter != null)
				teleporter.makeDestination(entity, worldFrom, worldDest);
			worldDest.updateEntityWithOptionalForce(entity, false);
			
			NBTTagCompound nbt = new NBTTagCompound();
			entity.writeToNBT(nbt);
			nbt.removeTag("Dimension");
			newEntity.readFromNBT(nbt);
			newEntity.timeUntilPortal = entity.timeUntilPortal;
			
			boolean flag = newEntity.forceSpawn;
			newEntity.forceSpawn = true;
			worldDest.spawnEntityInWorld(newEntity);
			newEntity.forceSpawn = flag;
			worldDest.updateEntityWithOptionalForce(newEntity, false);
			
			entity.isDead = true;
			worldFrom.resetUpdateEntityTick();
			worldDest.resetUpdateEntityTick();
			
			return true;
		}
		return false;
	}
	
	private static Field portalInvincibilityField = null;
	
	private static void setPortalInvincibilityWithReflection(EntityPlayerMP player) throws Exception
	{
		if(portalInvincibilityField == null)
		fieldSearch: {
			FakePlayer fake = new FakePlayer(player.getServerWorld(), player.getGameProfile());
			try
			{	//For dev environments with patch similar to 
				if(checkFieldForBoolean(EntityPlayerMP.class.getDeclaredField("invulnerableDimensionChange"), fake))
				{
					portalInvincibilityField = EntityPlayerMP.class.getDeclaredField("invulnerableDimensionChange");
					break fieldSearch;
				}
			} catch(NoSuchFieldException e) {}
			
			try
			{	//For obfuscated code
				if(checkFieldForBoolean(EntityPlayerMP.class.getDeclaredField("field_184851_cj"), fake))
				{
					portalInvincibilityField = EntityPlayerMP.class.getDeclaredField("field_184851_cj");
					break fieldSearch;
				}
			} catch(NoSuchFieldException e) {}
			
			Debug.warn("Couldn't find portal invincibility field by the normal names. Searching all fields based on type...");
			
			for(Field field : EntityPlayerMP.class.getDeclaredFields())	//There should only be one boolean field which is by default false
				if(checkFieldForBoolean(field, fake))
				{
					if(portalInvincibilityField != null)
					{
						portalInvincibilityField = null;
						throw new NoSuchFieldException("Found more than one field. Can't determine which one that is the portal cooldown.");
					}
					portalInvincibilityField = field;
				}
			
			if(portalInvincibilityField == null)
				throw new NoSuchFieldException("Couldn't find any fields that fits the portal cooldown field");
			else Debug.warn("Found possible field: "+ portalInvincibilityField.getName());
		}
		portalInvincibilityField.setAccessible(true);
		portalInvincibilityField.setBoolean(player, true);
		portalInvincibilityField.setAccessible(false);
	}
	
	private static boolean checkFieldForBoolean(Field field, FakePlayer player) throws Exception
	{
		field.setAccessible(true);
		boolean b = field.getType().equals(boolean.class) && !field.getBoolean(player);	//Field is false by default
		field.setAccessible(false);
		return b;
	}
}
