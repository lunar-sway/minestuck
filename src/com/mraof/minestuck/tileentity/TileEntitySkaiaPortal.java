package com.mraof.minestuck.tileentity;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.WorldServer;

import com.mraof.minestuck.block.BlockChessTile;
import com.mraof.minestuck.block.MinestuckBlocks;
import com.mraof.minestuck.util.ITeleporter;
import com.mraof.minestuck.util.Location;
import com.mraof.minestuck.util.Teleport;

public class TileEntitySkaiaPortal extends TileEntity implements ITeleporter
{
	public Location destination;
	
	@Override
	public void setPos(BlockPos posIn)
	{
		super.setPos(posIn);
		if(destination.pos.getY() < 0)
			destination.pos = posIn;
	}
	
	@Override
	public void readFromNBT(NBTTagCompound nbt) 
	{
		super.readFromNBT(nbt);
		destination = new Location(nbt.getInteger("DestinationX"), nbt.getInteger("DestinationY"), nbt.getInteger("DestinationZ"), nbt.getInteger("Destination"));
		
		if(destination.pos.getY() == 0)
		{
			destination.pos = this.pos;
		}
	}
	@Override
	public void writeToNBT(NBTTagCompound par1nbtTagCompound) 
	{
		super.writeToNBT(par1nbtTagCompound);
		par1nbtTagCompound.setInteger("Destination", this.destination.dim);
		par1nbtTagCompound.setInteger("DestinationX", destination.pos.getX());
		par1nbtTagCompound.setInteger("DestinationY", destination.pos.getY());
		par1nbtTagCompound.setInteger("DestinationZ", destination.pos.getZ());
	}
	public void teleportEntity(Entity entity)
	{
		entity.timeUntilPortal = entity.getPortalCooldown();
		if(destination.dim != this.worldObj.provider.getDimension())
			Teleport.teleportEntity(entity, this.destination.dim, this, false);
		if(entity instanceof EntityPlayerMP)
			((EntityPlayerMP)entity).playerNetServerHandler.setPlayerLocation(destination.pos.getX(), destination.pos.getY(), destination.pos.getZ(), entity.rotationYaw, entity.rotationPitch);
		else
			entity.setPosition(destination.pos.getX(), destination.pos.getY(), destination.pos.getZ());
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
				worldserver1.setBlockState(new BlockPos(blockX, (int) y - 1, blockZ), MinestuckBlocks.chessTile.getDefaultState().withProperty(BlockChessTile.BLOCK_TYPE, BlockChessTile.BlockType.values()[(blockX + blockZ) & 3]), 3);
				for(int blockY = (int) y; blockY < y + 6; blockY++)
					if(worldserver1.isBlockNormalCube(new BlockPos(blockX, blockY, blockZ), true))
						worldserver1.setBlockToAir(new BlockPos(blockX, blockY, blockZ));
			}
		}
	}
	
}
