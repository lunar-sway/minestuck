package com.mraof.minestuck.tileentity;

import com.mraof.minestuck.block.BlockChessTile;
import com.mraof.minestuck.block.MinestuckBlocks;
import com.mraof.minestuck.util.Location;
import com.mraof.minestuck.util.Teleport;
import net.minecraft.entity.Entity;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.WorldServer;

public class TileEntitySkaiaPortal extends TileEntity implements Teleport.ITeleporter
{
	public Location destination = new Location();
	
	@Override
	public void setPos(BlockPos posIn)
	{
		super.setPos(posIn);
	}
	
	@Override
	public void readFromNBT(NBTTagCompound nbt) 
	{
		super.readFromNBT(nbt);
		destination.pos = new BlockPos(nbt.getInteger("destX"), nbt.getInteger("destY"), nbt.getInteger("destZ"));
		destination.dim = nbt.getInteger("destDim");
	}
	
	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound tagCompound) 
	{
		super.writeToNBT(tagCompound);
		tagCompound.setInteger("destDim", this.destination.dim);
		tagCompound.setInteger("destX", destination.pos.getX());
		tagCompound.setInteger("destY", destination.pos.getY());
		tagCompound.setInteger("destZ", destination.pos.getZ());
		return tagCompound;
	}
	
	public void teleportEntity(Entity entity)
	{
		if(destination.dim != this.world.provider.getDimension())
		{
			if(destination.pos.getY() < 0)
			{
				WorldServer world = entity.getServer().getWorld(destination.dim);
				if(world == null)
					return;
				destination.pos = world.getTopSolidOrLiquidBlock(new BlockPos(entity)).up(5);
			}
			if(!Teleport.teleportEntity(entity, this.destination.dim, this, destination.pos))
				return;
		}
		entity.timeUntilPortal = entity.getPortalCooldown();
	}
	
	@Override
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
