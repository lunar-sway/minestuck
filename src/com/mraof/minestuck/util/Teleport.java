package com.mraof.minestuck.util;

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
			player.dimension = destinationDimension;
			
			SPacketRespawn respawnPacket = new SPacketRespawn(player.dimension, player.worldObj.getDifficulty(), worldDest.getWorldInfo().getTerrainType(), player.interactionManager.getGameType());
			player.playerNetServerHandler.sendPacket(respawnPacket);
			playerList.updatePermissionLevel(player);
			worldFrom.removePlayerEntityDangerously(player);
			player.isDead = false;
			
			player.setPosition(x, y, z);
			if(teleporter != null)
				teleporter.makeDestination(player, worldFrom, worldDest);
			worldDest.spawnEntityInWorld(player);
			worldDest.updateEntityWithOptionalForce(entity, false);
			player.setWorld(worldDest);
			
			playerList.preparePlayer(player, worldFrom);
			
			player.playerNetServerHandler.setPlayerLocation(player.posX, player.posY, player.posZ, player.rotationYaw, player.rotationPitch);
			player.interactionManager.setWorld(worldDest);
			player.playerNetServerHandler.sendPacket(new SPacketPlayerAbilities(player.capabilities));
			
			mcServer.getPlayerList().updateTimeAndWeatherForPlayer(player, worldDest);
			mcServer.getPlayerList().syncPlayerInventory(player);
			Iterator<?> iterator = player.getActivePotionEffects().iterator();
			
			while (iterator.hasNext())
			{
				PotionEffect potioneffect = (PotionEffect)iterator.next();
				player.playerNetServerHandler.sendPacket(new SPacketEntityEffect(player.getEntityId(), potioneffect));
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
}
