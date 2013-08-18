package com.mraof.minestuck.util;

import java.util.Iterator;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.network.packet.Packet41EntityEffect;
import net.minecraft.network.packet.Packet9Respawn;
import net.minecraft.potion.PotionEffect;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.WorldProvider;
import net.minecraft.world.WorldServer;
import cpw.mods.fml.common.registry.GameRegistry;

public class Teleport 
{
	public static void teleportEntity(Entity entity, int destinationDimension, ITeleporter teleporter)
	{
		if(entity instanceof EntityPlayerMP)
		{
			EntityPlayerMP par1EntityPlayerMP = (EntityPlayerMP) entity;
			int j = par1EntityPlayerMP.dimension;
			WorldServer worldserver = par1EntityPlayerMP.mcServer.worldServerForDimension(par1EntityPlayerMP.dimension);
			par1EntityPlayerMP.dimension = destinationDimension;
			WorldServer worldserver1 = par1EntityPlayerMP.mcServer.worldServerForDimension(par1EntityPlayerMP.dimension);
			Packet9Respawn respawnPacket = new Packet9Respawn(par1EntityPlayerMP.dimension, (byte)par1EntityPlayerMP.worldObj.difficultySetting, worldserver1.getWorldInfo().getTerrainType(), worldserver1.getHeight(), par1EntityPlayerMP.theItemInWorldManager.getGameType());
			par1EntityPlayerMP.playerNetServerHandler.sendPacketToPlayer(respawnPacket);
			worldserver.removePlayerEntityDangerously(par1EntityPlayerMP);
			par1EntityPlayerMP.isDead = false;
			transferEntityToWorld(par1EntityPlayerMP, j, worldserver, worldserver1, teleporter);
	        WorldServer worldserver2 = par1EntityPlayerMP.getServerForPlayer();
	        worldserver.getPlayerManager().removePlayer(par1EntityPlayerMP);
	        worldserver2.getPlayerManager().addPlayer(par1EntityPlayerMP);
	        worldserver2.theChunkProviderServer.loadChunk((int)par1EntityPlayerMP.posX >> 4, (int)par1EntityPlayerMP.posZ >> 4);
	        
			par1EntityPlayerMP.playerNetServerHandler.setPlayerLocation(par1EntityPlayerMP.posX, par1EntityPlayerMP.posY, par1EntityPlayerMP.posZ, par1EntityPlayerMP.rotationYaw, par1EntityPlayerMP.rotationPitch);
			par1EntityPlayerMP.theItemInWorldManager.setWorld(worldserver1);
			par1EntityPlayerMP.mcServer.getConfigurationManager().updateTimeAndWeatherForPlayer(par1EntityPlayerMP, worldserver1);
			par1EntityPlayerMP.mcServer.getConfigurationManager().syncPlayerInventory(par1EntityPlayerMP);
			Iterator iterator = par1EntityPlayerMP.getActivePotionEffects().iterator();

			while (iterator.hasNext())
			{
				PotionEffect potioneffect = (PotionEffect)iterator.next();
				par1EntityPlayerMP.playerNetServerHandler.sendPacketToPlayer(new Packet41EntityEffect(par1EntityPlayerMP.entityId, potioneffect));
			}

			GameRegistry.onPlayerChangedDimension(par1EntityPlayerMP);
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
				newEntity.copyDataFrom(entity, true);
				worldserver1.spawnEntityInWorld(newEntity);
			}
			entity.isDead = true;
			worldserver.resetUpdateEntityTick();
			worldserver1.resetUpdateEntityTick();
		}
		entity.timeUntilPortal = entity.getPortalCooldown();
	}
	public static void transferEntityToWorld(Entity entity, int dimension, WorldServer worldserver, WorldServer worldserver1, ITeleporter teleporter)
	{
        WorldProvider pOld = worldserver.provider;
        WorldProvider pNew = worldserver1.provider;
        double moveFactor = pOld.getMovementFactor() / pNew.getMovementFactor();
        double d0 = entity.posX * moveFactor;
        double d1 = entity.posZ * moveFactor;
        double d3 = entity.posX;
        double d4 = entity.posY;
        double d5 = entity.posZ;
        float f = entity.rotationYaw;

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
