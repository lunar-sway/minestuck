package com.mraof.minestuck.tileentity;

import com.mraof.minestuck.block.MinestuckBlocks;
import com.mraof.minestuck.util.Debug;
import com.mraof.minestuck.util.Location;
import com.mraof.minestuck.util.Teleport;

import com.mraof.minestuck.world.MinestuckDimensionHandler;
import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.WorldServer;
import net.minecraft.world.dimension.DimensionType;
import net.minecraft.world.gen.Heightmap;
import net.minecraftforge.common.DimensionManager;

public class TileEntitySkaiaPortal extends TileEntity implements Teleport.ITeleporter
{
	public Location destination = new Location();
	
	public TileEntitySkaiaPortal()
	{
		super(MinestuckTiles.SKAIA_PORTAL);
	}
	
	@Override
	public void setPos(BlockPos posIn)
	{
		super.setPos(posIn);
	}
	
	@Override
	public void read(NBTTagCompound compound)
	{
		super.read(compound);
		destination.pos = new BlockPos(compound.getInt("destX"), compound.getInt("destY"), compound.getInt("destZ"));
		destination.dim = DimensionType.byName(ResourceLocation.makeResourceLocation(compound.getString("destDim")));
		if(destination.dim == null)
			destination.dim = MinestuckDimensionHandler.skaia;
	}
	
	@Override
	public NBTTagCompound write(NBTTagCompound compound)
	{
		super.write(compound);
		ResourceLocation dimName = DimensionType.func_212678_a(this.destination.dim);
		
		if(dimName != null)
			compound.setString("destDim", dimName.toString());
		else Debug.warnf("Couldn't get dimension name for dimension %s!", destination.dim);
		compound.setInt("destX", destination.pos.getX());
		compound.setInt("destY", destination.pos.getY());
		compound.setInt("destZ", destination.pos.getZ());
		return compound;
	}
	
	public void teleportEntity(Entity entity)
	{
		if(destination.dim != this.world.getDimension().getType())
		{
			if(destination.pos.getY() < 0)
			{
				WorldServer world = DimensionManager.getWorld(entity.getServer(), destination.dim, true, true);
				if(world == null)
					return;
				destination.pos = world.getHeight(Heightmap.Type.WORLD_SURFACE, new BlockPos(entity)).up(5);
			}
			if(!Teleport.teleportEntity(entity, this.destination.dim, this, destination.pos))
				return;
		}
		entity.timeUntilPortal = entity.getPortalCooldown();
	}
	
	@Override
	public boolean prepareDestination(BlockPos pos, Entity entity, WorldServer worldserver)
	{
		entity.setPosition(pos.getX(), pos.getY(), pos.getZ());
		return true;
	}

	@Override
	public void finalizeDestination(Entity entity, WorldServer worldserver, WorldServer worldserver1)
	{
		double x = entity.posX;
		double y = entity.posY;
		double z = entity.posZ;
		Block[] blocks = {MinestuckBlocks.BLACK_CHESS_DIRT, MinestuckBlocks.LIGHT_GRAY_CHESS_DIRT, MinestuckBlocks.WHITE_CHESS_DIRT, MinestuckBlocks.DARK_GRAY_CHESS_DIRT};
		for(int blockX = (int) x - 2; blockX < x + 2; blockX++)
		{
			for(int blockZ = (int) z - 2; blockZ < z + 2; blockZ++)
			{
				worldserver1.setBlockState(new BlockPos(blockX, (int) y - 1, blockZ), blocks[(blockX + blockZ) & 3].getDefaultState(), 3);
				for(int blockY = (int) y; blockY < y + 6; blockY++)
					if(worldserver1.isBlockFullCube(new BlockPos(blockX, blockY, blockZ)))
						worldserver1.removeBlock(new BlockPos(blockX, blockY, blockZ));
			}
		}
	}
	
}
