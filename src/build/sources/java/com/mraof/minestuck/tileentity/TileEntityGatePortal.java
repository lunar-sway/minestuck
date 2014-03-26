package com.mraof.minestuck.tileentity;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.WorldServer;

import com.mraof.minestuck.Minestuck;
import com.mraof.minestuck.util.ITeleporter;
import com.mraof.minestuck.util.Teleport;

public class TileEntityGatePortal extends TileEntity implements ITeleporter
{
	public int destinationDimension;
	public int destinationX, destinationY, destinationZ;
	@Override
	public void readFromNBT(NBTTagCompound par1nbtTagCompound) 
	{
		super.readFromNBT(par1nbtTagCompound);
		this.destinationDimension = par1nbtTagCompound.getInteger("Destination");
		this.destinationX = par1nbtTagCompound.getInteger("DestinationX");
		this.destinationY = par1nbtTagCompound.getInteger("DestinationY");
		this.destinationZ = par1nbtTagCompound.getInteger("DestinationZ");

		if(destinationY == 0)
		{
			this.destinationX = this.xCoord;
			this.destinationY = this.yCoord;
			this.destinationZ = this.zCoord;
		}
	}
	@Override
	public void writeToNBT(NBTTagCompound par1nbtTagCompound) 
	{
		super.writeToNBT(par1nbtTagCompound);
		par1nbtTagCompound.setInteger("Destination", this.destinationDimension);
		par1nbtTagCompound.setInteger("DestinationX", destinationX);
		par1nbtTagCompound.setInteger("DestinationY", destinationY);
		par1nbtTagCompound.setInteger("DestinationZ", destinationZ);
	}
	public void teleportEntity(Entity entity)
	{
		entity.timeUntilPortal = entity.getPortalCooldown();
		if(destinationDimension != this.worldObj.provider.dimensionId)
			Teleport.teleportEntity(entity, this.destinationDimension, this);
		if(entity instanceof EntityPlayerMP)
			((EntityPlayerMP)entity).playerNetServerHandler.setPlayerLocation(destinationX, destinationY, destinationZ, entity.rotationYaw, entity.rotationPitch);
		else
			entity.setPosition(destinationX, destinationY, destinationZ);
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
				worldserver1.setBlock(blockX, (int) y - 1, blockZ, Minestuck.chessTile, (blockX + blockZ) & 3, 3);
				for(int blockY = (int) y; blockY < y + 6; blockY++)
					if(worldserver1.isBlockNormalCubeDefault(blockX, blockY, blockZ, true))
						worldserver1.setBlockToAir(blockX, blockY, blockZ);
					
					
			}
		}
	}

}
