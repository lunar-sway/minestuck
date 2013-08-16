package com.mraof.minestuck.tileentity;

import java.util.Iterator;

import com.mraof.minestuck.Minestuck;
import com.mraof.minestuck.util.ITeleporter;
import com.mraof.minestuck.util.Teleport;

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

public class TileEntityGatePortal extends TileEntity implements ITeleporter
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
		Teleport.teleportEntity(entity, this.destinationDimension, this);
	}
	public void makeDestination(Entity entity, WorldServer worldserver, WorldServer worldserver1)
	{
		double x = entity.posX;
		double y = entity.posY;
		double z = entity.posZ;
		for(int blockX = (int) x - 2; blockX < x + 2; blockX++)
		{
			for(int blockZ = (int) z - 2; blockZ < z + 2; blockZ++)
			{
				worldserver1.setBlock(blockX, (int) y - 1, blockZ, Minestuck.chessTile.blockID, (blockX + blockZ) & 3, 3);
				for(int blockY = (int) y; blockY < y + 6; blockY++)
					worldserver1.setBlockToAir(blockX, blockY, blockZ);
					
					
			}
		}
	}

}
