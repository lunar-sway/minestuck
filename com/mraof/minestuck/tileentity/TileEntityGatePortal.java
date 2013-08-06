package com.mraof.minestuck.tileentity;

import java.util.Iterator;

import com.mraof.minestuck.Minestuck;

import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.packet.Packet41EntityEffect;
import net.minecraft.network.packet.Packet9Respawn;
import net.minecraft.potion.PotionEffect;
import net.minecraft.server.MinecraftServer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ChunkCoordinates;
import net.minecraft.util.MathHelper;
import net.minecraft.world.WorldProvider;
import net.minecraft.world.WorldServer;

public class TileEntityGatePortal extends TileEntity
{
	public int destinationDimension;
	@Override
	public void readFromNBT(NBTTagCompound par1nbtTagCompound) 
	{
		//do not load invalid GatePortals
		super.readFromNBT(par1nbtTagCompound);
		this.destinationDimension = par1nbtTagCompound.getInteger("Destination");
	}
	@Override
	public void writeToNBT(NBTTagCompound par1nbtTagCompound) 
	{
		super.writeToNBT(par1nbtTagCompound);
		par1nbtTagCompound.setInteger("Destination", this.destinationDimension);
	}
	public void teleportEntity(Entity entity)
	{
//		entity.travelToDimension(this.destinationDimension);
		//		/* //Yes I commented out a comment
		if(entity instanceof EntityPlayerMP)
		{
			EntityPlayerMP par1EntityPlayerMP = (EntityPlayerMP) entity;
			int j = par1EntityPlayerMP.dimension;
			WorldServer worldserver = par1EntityPlayerMP.mcServer.worldServerForDimension(par1EntityPlayerMP.dimension);
			par1EntityPlayerMP.dimension = this.destinationDimension;
			WorldServer worldserver1 = par1EntityPlayerMP.mcServer.worldServerForDimension(par1EntityPlayerMP.dimension);
			Packet9Respawn respawnPacket = new Packet9Respawn(par1EntityPlayerMP.dimension, (byte)par1EntityPlayerMP.worldObj.difficultySetting, worldserver1.getWorldInfo().getTerrainType(), worldserver1.getHeight(), par1EntityPlayerMP.theItemInWorldManager.getGameType());
			par1EntityPlayerMP.playerNetServerHandler.sendPacketToPlayer(respawnPacket);
			worldserver.removePlayerEntityDangerously(par1EntityPlayerMP);
			par1EntityPlayerMP.isDead = false;
			this.transferEntityToWorld(par1EntityPlayerMP, j, worldserver, worldserver1);
//	        this.func_72375_a(par1EntityPlayerMP, worldserver);
	        WorldServer worldserver2 = par1EntityPlayerMP.getServerForPlayer();
	        worldserver.getPlayerManager().removePlayer(par1EntityPlayerMP);
	        worldserver2.getPlayerManager().addPlayer(par1EntityPlayerMP);
	        worldserver2.theChunkProviderServer.loadChunk((int)par1EntityPlayerMP.posX >> 4, (int)par1EntityPlayerMP.posZ >> 4);
//	        worldserver2.spawnEntityInWorld(par1EntityPlayerMP);
	        
//			par1EntityPlayerMP.mcServer.getConfigurationManager().func_72375_a(par1EntityPlayerMP, worldserver);
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
			WorldServer worldserver1 = minecraftserver.worldServerForDimension(this.destinationDimension);
			entity.dimension = this.destinationDimension;
			entity.worldObj.removeEntity(entity);
			entity.isDead = false;
			this.transferEntityToWorld(entity, j, worldserver, worldserver1);
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
		//		 */
		//TODO Load for the player, until then the old way is better
		entity.timeUntilPortal = entity.getPortalCooldown();
	}
	public void transferEntityToWorld(Entity entity, int dimension, WorldServer worldserver, WorldServer worldserver1)
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
                this.makeDestination(entity, worldserver1);
            }


        entity.setWorld(worldserver1);
	}
	public void makeDestination(Entity entity, WorldServer worldserver)
	{
		double x = entity.posX;
		double y = entity.posY;
		double z = entity.posZ;
		for(int blockX = (int) x - 2; blockX < x + 2; blockX++)
		{
			for(int blockZ = (int) z - 2; blockZ < z + 2; blockZ++)
			{
				worldserver.setBlock(blockX, (int) y - 1, blockZ, Minestuck.chessTile.blockID, (blockX + blockZ) & 3, 3);
				for(int blockY = (int) y; blockY < y + 6; blockY++)
					worldserver.setBlockToAir(blockX, blockY, blockZ);
					
					
			}
		}
	}

}
