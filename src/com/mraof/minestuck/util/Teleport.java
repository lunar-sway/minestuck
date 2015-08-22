package com.mraof.minestuck.util;

import java.util.Iterator;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.network.play.server.S07PacketRespawn;
import net.minecraft.network.play.server.S1DPacketEntityEffect;
import net.minecraft.potion.PotionEffect;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.WorldProvider;
import net.minecraft.world.WorldServer;
import net.minecraftforge.fml.common.FMLCommonHandler;

public class Teleport	//TODO Add method that takes a Location as parameter that also moves the entity to the desired destination
{
	public static void teleportEntity(Entity entity, int destinationDimension, ITeleporter teleporter)
	{
		if(entity instanceof EntityPlayerMP)
		{
			Debug.print("Teleporting player to dimension "+destinationDimension);
			EntityPlayerMP player = (EntityPlayerMP) entity;
			int j = player.dimension;
			WorldServer worldserver = player.mcServer.worldServerForDimension(player.dimension);
			player.dimension = destinationDimension;
			WorldServer worldserver1 = player.mcServer.worldServerForDimension(player.dimension);
			S07PacketRespawn respawnPacket = new S07PacketRespawn(player.dimension, player.worldObj.getDifficulty(), worldserver1.getWorldInfo().getTerrainType(), player.theItemInWorldManager.getGameType());
			player.playerNetServerHandler.sendPacket(respawnPacket);			

			worldserver.removePlayerEntityDangerously(player);
			player.isDead = false;
			transferEntityToWorld(player, j, worldserver, worldserver1, teleporter);
			WorldServer worldserver2 = player.getServerForPlayer();
			worldserver.getPlayerManager().removePlayer(player);
			worldserver2.getPlayerManager().addPlayer(player);
			worldserver2.theChunkProviderServer.loadChunk((int)player.posX >> 4, (int)player.posZ >> 4);
			
			player.playerNetServerHandler.setPlayerLocation(player.posX, player.posY, player.posZ, player.rotationYaw, player.rotationPitch);
			player.theItemInWorldManager.setWorld(worldserver1);
			player.mcServer.getConfigurationManager().updateTimeAndWeatherForPlayer(player, worldserver1);
			player.mcServer.getConfigurationManager().syncPlayerInventory(player);
			Iterator<?> iterator = player.getActivePotionEffects().iterator();

			while (iterator.hasNext())
			{
				PotionEffect potioneffect = (PotionEffect)iterator.next();
				player.playerNetServerHandler.sendPacket(new S1DPacketEntityEffect(player.getEntityId(), potioneffect));
			}
			
			FMLCommonHandler.instance().firePlayerChangedDimensionEvent(player, j, destinationDimension);
		}
		else if (!entity.worldObj.isRemote && !entity.isDead)
		{
			MinecraftServer minecraftserver = MinecraftServer.getServer();
			int j = entity.dimension;
			WorldServer worldserver = minecraftserver.worldServerForDimension(j);
			WorldServer worldserver1 = minecraftserver.worldServerForDimension(destinationDimension);
			entity.dimension = destinationDimension;
			entity.worldObj.removeEntity(entity);
			entity.isDead = false;
			transferEntityToWorld(entity, j, worldserver, worldserver1, teleporter);
			Entity newEntity = EntityList.createEntityByName(EntityList.getEntityString(entity), worldserver1);

			if (newEntity != null)
			{
				newEntity.copyDataFromOld(entity);
				worldserver1.spawnEntityInWorld(newEntity);
			}
			entity.isDead = true;
			worldserver.resetUpdateEntityTick();
			worldserver1.resetUpdateEntityTick();
		}
		//entity.timeUntilPortal = entity.getPortalCooldown();
	}
	public static void transferEntityToWorld(Entity entity, int dimension, WorldServer worldserver, WorldServer worldserver1, ITeleporter teleporter)
	{
		WorldProvider pOld = worldserver.provider;
		WorldProvider pNew = worldserver1.provider;
		double moveFactor = pOld.getMovementFactor() / pNew.getMovementFactor();
		double d0 = entity.posX * moveFactor;
		double d1 = entity.posZ * moveFactor;

		if (entity.isEntityAlive())
		{
			worldserver1.spawnEntityInWorld(entity);
			entity.setLocationAndAngles(d0, entity.posY, d1, entity.rotationYaw, entity.rotationPitch);
			worldserver1.updateEntityWithOptionalForce(entity, false);
			teleporter.makeDestination(entity, worldserver, worldserver1);
		}


		entity.setWorld(worldserver1);
	}
}
