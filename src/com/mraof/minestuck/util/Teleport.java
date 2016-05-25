package com.mraof.minestuck.util;

import java.util.Iterator;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.play.server.SPacketEntityEffect;
import net.minecraft.network.play.server.SPacketRespawn;
import net.minecraft.potion.PotionEffect;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.WorldProvider;
import net.minecraft.world.WorldServer;
import net.minecraftforge.fml.common.FMLCommonHandler;

public class Teleport	//TODO Add method that takes a Location as parameter that also moves the entity to the desired destination
{
	public static void teleportEntity(Entity entity, int destinationDimension, ITeleporter teleporter, boolean movementFactor)
	{
		if(entity instanceof EntityPlayerMP)
		{
			Debug.debug("Teleporting player to dimension "+destinationDimension);
			EntityPlayerMP player = (EntityPlayerMP) entity;
			int j = player.dimension;
			WorldServer worldserver = player.mcServer.worldServerForDimension(player.dimension);
			player.dimension = destinationDimension;
			WorldServer worldserver1 = player.mcServer.worldServerForDimension(player.dimension);
			SPacketRespawn respawnPacket = new SPacketRespawn(player.dimension, player.worldObj.getDifficulty(), worldserver1.getWorldInfo().getTerrainType(), player.interactionManager.getGameType());
			player.playerNetServerHandler.sendPacket(respawnPacket);			

			worldserver.removePlayerEntityDangerously(player);
			player.isDead = false;
			transferEntityToWorld(player, j, worldserver, worldserver1, teleporter, movementFactor);
			WorldServer worldserver2 = player.getServerForPlayer();
			worldserver.getPlayerChunkManager().removePlayer(player);
			worldserver2.getPlayerChunkManager().addPlayer(player);
			worldserver2.getChunkProvider().loadChunk((int)player.posX >> 4, (int)player.posZ >> 4);
			
			player.playerNetServerHandler.setPlayerLocation(player.posX, player.posY, player.posZ, player.rotationYaw, player.rotationPitch);
			player.interactionManager.setWorld(worldserver1);
			player.mcServer.getPlayerList().updateTimeAndWeatherForPlayer(player, worldserver1);
			player.mcServer.getPlayerList().syncPlayerInventory(player);
			Iterator<?> iterator = player.getActivePotionEffects().iterator();

			while (iterator.hasNext())
			{
				PotionEffect potioneffect = (PotionEffect)iterator.next();
				player.playerNetServerHandler.sendPacket(new SPacketEntityEffect(player.getEntityId(), potioneffect));
			}
			
			FMLCommonHandler.instance().firePlayerChangedDimensionEvent(player, j, destinationDimension);
		}
		else if (!entity.worldObj.isRemote && !entity.isDead)
		{
			MinecraftServer minecraftserver = entity.getServer();
			int j = entity.dimension;
			WorldServer worldserver = minecraftserver.worldServerForDimension(j);
			WorldServer worldserver1 = minecraftserver.worldServerForDimension(destinationDimension);
			entity.dimension = destinationDimension;
			entity.worldObj.removeEntity(entity);
			entity.isDead = false;
			transferEntityToWorld(entity, j, worldserver, worldserver1, teleporter, movementFactor);
			Entity newEntity = EntityList.createEntityByName(EntityList.getEntityString(entity), worldserver1);
			
			if (newEntity != null)
			{
				//newEntity.copyDataFromOld(entity);
				NBTTagCompound nbttagcompound = new NBTTagCompound();
				entity.writeToNBT(nbttagcompound);
				nbttagcompound.removeTag("Dimension");
				newEntity.readFromNBT(nbttagcompound);
				newEntity.timeUntilPortal = entity.timeUntilPortal;
				worldserver1.spawnEntityInWorld(newEntity);
			}
			entity.isDead = true;
			worldserver.resetUpdateEntityTick();
			worldserver1.resetUpdateEntityTick();
		}
		//entity.timeUntilPortal = entity.getPortalCooldown();
	}
	public static void transferEntityToWorld(Entity entity, int dimension, WorldServer worldserver, WorldServer worldserver1, ITeleporter teleporter, boolean movementFactor)
	{
		WorldProvider pOld = worldserver.provider;
		WorldProvider pNew = worldserver1.provider;
		double moveFactor = movementFactor ? pOld.getMovementFactor() / pNew.getMovementFactor() : 1;
		double d0 = entity.posX * moveFactor;
		double d1 = entity.posZ * moveFactor;
		
		if (entity.isEntityAlive())
		{
			worldserver1.spawnEntityInWorld(entity);
			entity.setLocationAndAngles(d0, entity.posY, d1, entity.rotationYaw, entity.rotationPitch);
			worldserver1.updateEntityWithOptionalForce(entity, false);
			if(teleporter != null)
				teleporter.makeDestination(entity, worldserver, worldserver1);
		}
		
		entity.setWorld(worldserver1);
	}
}
