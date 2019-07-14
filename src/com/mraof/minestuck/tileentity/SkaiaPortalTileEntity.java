package com.mraof.minestuck.tileentity;

import com.mraof.minestuck.block.MinestuckBlocks;
import com.mraof.minestuck.util.Debug;
import com.mraof.minestuck.util.Location;

import com.mraof.minestuck.world.MinestuckDimensionHandler;
import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.ServerWorld;
import net.minecraft.world.World;
import net.minecraft.world.dimension.DimensionType;
import net.minecraft.world.gen.Heightmap;
import net.minecraftforge.common.DimensionManager;

public class SkaiaPortalTileEntity extends TileEntity //implements ITeleporter
{
	public Location destination = new Location();
	
	public SkaiaPortalTileEntity()
	{
		super(ModTileEntityTypes.SKAIA_PORTAL);
	}
	
	@Override
	public void setWorld(World worldIn)
	{
		super.setWorld(worldIn);
		if(destination.dim == worldIn.getDimension().getType())
			destination.dim = worldIn.getDimension().getType() == MinestuckDimensionHandler.skaia ? DimensionType.OVERWORLD : MinestuckDimensionHandler.skaia;
	}
	
	@Override
	public void read(CompoundNBT compound)
	{
		super.read(compound);
		destination.pos = new BlockPos(compound.getInt("destX"), compound.getInt("destY"), compound.getInt("destZ"));
		destination.dim = DimensionType.byName(ResourceLocation.tryCreate(compound.getString("destDim")));
		if(destination.dim == null)
			destination.dim = MinestuckDimensionHandler.skaia;
	}
	
	@Override
	public CompoundNBT write(CompoundNBT compound)
	{
		super.write(compound);
		ResourceLocation dimName = this.destination.dim.getRegistryName();
		
		if(dimName != null)
			compound.putString("destDim", dimName.toString());
		else Debug.warnf("Couldn't get dimension name for dimension %s!", destination.dim);
		compound.putInt("destX", destination.pos.getX());
		compound.putInt("destY", destination.pos.getY());
		compound.putInt("destZ", destination.pos.getZ());
		return compound;
	}
	
	public void teleportEntity(Entity entity)
	{
		if(destination.dim != this.world.getDimension().getType() && destination.dim != null)
		{
			if(destination.pos.getY() < 0)
			{
				ServerWorld world = DimensionManager.getWorld(entity.getServer(), destination.dim, true, true);
				if(world == null)
					return;
				destination.pos = world.getHeight(Heightmap.Type.WORLD_SURFACE, new BlockPos(entity)).up(5);
			}
			entity = entity.changeDimension(destination.dim);//, this);
		}
		if(entity != null)
			entity.timeUntilPortal = entity.getPortalCooldown();
	}
	
	//@Override
	public void placeEntity(World world, Entity entity, float yaw)
	{
		double x = pos.getX();
		double y = pos.getY();
		double z = pos.getZ();
		entity.setPosition(x, y, z);
		Block[] blocks = {MinestuckBlocks.BLACK_CHESS_DIRT, MinestuckBlocks.LIGHT_GRAY_CHESS_DIRT, MinestuckBlocks.WHITE_CHESS_DIRT, MinestuckBlocks.DARK_GRAY_CHESS_DIRT};
		for(int blockX = (int) x - 2; blockX < x + 2; blockX++)
		{
			for(int blockZ = (int) z - 2; blockZ < z + 2; blockZ++)
			{
				world.setBlockState(new BlockPos(blockX, (int) y - 1, blockZ), blocks[(blockX + blockZ) & 3].getDefaultState(), 3);
				for(int blockY = (int) y; blockY < y + 6; blockY++)
					world.removeBlock(new BlockPos(blockX, blockY, blockZ), false);
			}
		}
	}
	
}
