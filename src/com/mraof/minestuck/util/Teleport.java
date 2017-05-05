package com.mraof.minestuck.util;

import java.lang.reflect.Field;
import java.util.Iterator;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.item.*;
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
		return teleportEntity(entity, destinationDimension, teleporter, dest.getX() + 0.5, dest.getY(), dest.getZ() + 0.5);
	}
	
	public static boolean teleportEntity(Entity entity, int destinationDimension, ITeleporter teleporter)
	{
		return teleportEntity(entity, destinationDimension, teleporter, entity.posX, entity.posY, entity.posZ);
	}
	
	public static boolean teleportEntity(Entity entity, int destinationDimension, ITeleporter teleporter, double x, double y, double z)
	{
		if(destinationDimension == entity.dimension)
			return localTeleport(entity, teleporter, x, y, z);
		
		if(entity.world.isRemote)
			throw new IllegalArgumentException("Shouldn't do teleporting with a clientside entity.");
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
			
			SPacketRespawn respawnPacket = new SPacketRespawn(player.dimension, player.world.getDifficulty(), worldDest.getWorldInfo().getTerrainType(), player.interactionManager.getGameType());
			player.connection.sendPacket(respawnPacket);
			playerList.updatePermissionLevel(player);
			worldFrom.removeEntityDangerously(player);
			player.isDead = false;
			
			player.setPosition(x, y, z);
			if(teleporter != null)
				teleporter.makeDestination(player, worldFrom, worldDest);
			worldDest.spawnEntity(player);
			worldDest.updateEntityWithOptionalForce(entity, false);
			player.setWorld(worldDest);
			
			playerList.preparePlayer(player, worldFrom);
			
			player.connection.setPlayerLocation(player.posX, player.posY, player.posZ, player.rotationYaw, player.rotationPitch);
			player.interactionManager.setWorld(worldDest);
			player.connection.sendPacket(new SPacketPlayerAbilities(player.capabilities));
			
			playerList.updateTimeAndWeatherForPlayer(player, worldDest);
			playerList.syncPlayerInventory(player);
			Iterator<?> iterator = player.getActivePotionEffects().iterator();
			
			while (iterator.hasNext())
			{
				PotionEffect potioneffect = (PotionEffect)iterator.next();
				player.connection.sendPacket(new SPacketEntityEffect(player.getEntityId(), potioneffect));
			}
			
			FMLCommonHandler.instance().firePlayerChangedDimensionEvent(player, prevDimension, destinationDimension);
			
			return true;
		}
		else if (!entity.isDead)
		{
			Entity newEntity = EntityList.createEntityByName(EntityList.getEntityString(entity), worldDest);
			if(newEntity == null)
				return false;

			NBTTagCompound nbt = new NBTTagCompound();
			entity.dimension = destinationDimension;
			entity.writeToNBT(nbt);
			
			if(teleporter != null)
				teleporter.makeDestination(entity, worldFrom, worldDest);
			worldDest.updateEntityWithOptionalForce(entity, false);
			
			nbt.removeTag("Dimension");
			newEntity.readFromNBT(nbt);
			newEntity.timeUntilPortal = entity.timeUntilPortal;
			newEntity.setPosition(x, y, z);
						
			boolean flag = newEntity.forceSpawn;
			newEntity.forceSpawn = true;
			worldDest.spawnEntity(newEntity);
			newEntity.forceSpawn = flag;
			worldDest.updateEntityWithOptionalForce(newEntity, false);
			
			entity.world.removeEntity(entity);
			entity.isDead = true;
			worldFrom.resetUpdateEntityTick();
			worldDest.resetUpdateEntityTick();
			
			return true;
		}
		return false;
	}
	
	public static boolean localTeleport(Entity entity, ITeleporter teleporter, double x, double y, double z)
	{
		if(entity.world.isRemote)
			throw new IllegalArgumentException("Shouldn't do teleporting with a clientside entity.");
		if(!ForgeHooks.onTravelToDimension(entity, entity.dimension))
			return false;
		
		if(entity instanceof EntityPlayerMP)
		{
			WorldServer world = (WorldServer) entity.world;
			PlayerList playerList = entity.getServer().getPlayerList();
			EntityPlayerMP player = (EntityPlayerMP) entity;
			try
			{
				setPortalInvincibilityWithReflection(player);
			} catch(Exception e)
			{
				Debug.warn("Failed to set portal invincibility through reflection. Portal problems might occur. Problem: "+e.getMessage());
			}
			
//			SPacketRespawn respawnPacket = new SPacketRespawn(player.dimension, world.getDifficulty(), world.getWorldInfo().getTerrainType(), player.interactionManager.getGameType());
//			player.connection.sendPacket(respawnPacket);
//			playerList.updatePermissionLevel(player);
			
			player.setPosition(x, y, z);
			if(teleporter != null)
				teleporter.makeDestination(player, world, world);
//			world.updateEntityWithOptionalForce(entity, false);
			
//			playerList.preparePlayer(player, world);
			
			player.connection.setPlayerLocation(player.posX, player.posY, player.posZ, player.rotationYaw, player.rotationPitch);
//			player.connection.sendPacket(new SPacketPlayerAbilities(player.capabilities));
			
			
			return true;
		} else if(!entity.isDead)
		{
			entity.setPosition(x, y, z);
			if(teleporter != null)
				teleporter.makeDestination(entity, (WorldServer) entity.world, (WorldServer) entity.world);
			
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
	
	public static interface ITeleporter
	{
		void makeDestination(Entity entity, WorldServer worldserver, WorldServer worldserver1);
	}
	
}