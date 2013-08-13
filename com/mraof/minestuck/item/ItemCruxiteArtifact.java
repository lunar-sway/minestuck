package com.mraof.minestuck.item;

import java.util.Iterator;

import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.packet.Packet41EntityEffect;
import net.minecraft.network.packet.Packet9Respawn;
import net.minecraft.potion.PotionEffect;
import net.minecraft.server.MinecraftServer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraft.world.WorldProvider;
import net.minecraft.world.WorldServer;

import com.mraof.minestuck.Minestuck;

import cpw.mods.fml.common.registry.GameRegistry;

public class ItemCruxiteArtifact extends ItemFood 
{
	int destinationDimension = Minestuck.landDimensionIdStart;
	public ItemCruxiteArtifact(int par1, int par2, boolean par3) 
	{
		super(par1, par2, par3);
		this.setCreativeTab(Minestuck.tabMinestuck);
	}
	@Override
    public ItemStack onItemRightClick(ItemStack par1ItemStack, World par2World, EntityPlayer par3EntityPlayer)
    {
        par3EntityPlayer.setItemInUse(par1ItemStack, this.getMaxItemUseDuration(par1ItemStack));

        return par1ItemStack;
    }
	@Override
	protected void onFoodEaten(ItemStack par1ItemStack, World par2World, EntityPlayer par3EntityPlayer) {
//		super.onFoodEaten(par1ItemStack, par2World, par3EntityPlayer);
		if(!par2World.isRemote && par3EntityPlayer.worldObj.provider.dimensionId != this.destinationDimension)
		{
			teleportEntity(par3EntityPlayer);
		}
	}
	public void teleportEntity(Entity entity)
	{
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
                this.makeDestination(entity, worldserver, worldserver1);
            }


        entity.setWorld(worldserver1);
	}
	public void makeDestination(Entity entity, WorldServer worldserver0, WorldServer worldserver1)
	{
		int x = (int) entity.posX;
		int y = (int) entity.posY;
		int z = (int) entity.posZ;
		int width = 16;
		for(int blockX = x - width; blockX <= x + width; blockX++)
		{
			for(int blockZ = z - width; blockZ <= z + width; blockZ++)
			{
//				worldserver.setBlock(blockX, (int) y - 1, blockZ, Minestuck.chessTile.blockID, (blockX + blockZ) & 3, 3);
				double radius = Math.sqrt(((blockX - x) * (blockX - x) + (blockZ - z) * (blockZ - z)) / 2);
				for(int blockY = y - (int) (Math.sqrt(width*width - radius*radius)); blockY < y + width && blockY < 256; blockY++)
				{
					int blockId = worldserver0.getBlockId(blockX, blockY, blockZ);
					int metadata = worldserver0.getBlockMetadata(blockX, blockY, blockZ);
					TileEntity te = worldserver0.getBlockTileEntity(blockX, blockY, blockZ);
					worldserver1.setBlock(blockX, blockY, blockZ, blockId, metadata, 3);
					if((te) != null)
					{
						TileEntity te1 = null;
						try {
							te1 = te.getClass().newInstance();
						} catch (Exception e) {e.printStackTrace();	}
						NBTTagCompound nbt = new NBTTagCompound();
						te.writeToNBT(nbt);
						te1.readFromNBT(nbt);
						te1.yCoord++;//prevents TileEntity from being invalidated
						worldserver1.setBlockTileEntity(blockX, blockY, blockZ, te1);
					};
					worldserver0.setBlockToAir(blockX, blockY, blockZ);
				}
					
					
			}
		}
	}
	@Override
	public void registerIcons(IconRegister iconRegister) 
	{
		this.itemIcon = iconRegister.registerIcon("minestuck:CruxiteApple");
	}
	
}
