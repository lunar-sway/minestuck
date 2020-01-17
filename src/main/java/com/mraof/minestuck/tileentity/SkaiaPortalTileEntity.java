package com.mraof.minestuck.tileentity;

import com.mojang.datafixers.Dynamic;
import com.mraof.minestuck.block.MSBlocks;
import com.mraof.minestuck.util.Teleport;
import com.mraof.minestuck.world.MSDimensions;
import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.NBTDynamicOps;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.GlobalPos;
import net.minecraft.world.World;
import net.minecraft.world.dimension.DimensionType;
import net.minecraft.world.gen.Heightmap;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.common.DimensionManager;

public class SkaiaPortalTileEntity extends TileEntity //implements ITeleporter
{
	public GlobalPos destination = GlobalPos.of(DimensionType.OVERWORLD, new BlockPos(0, -1, 0));
	
	public SkaiaPortalTileEntity()
	{
		super(MSTileEntityTypes.SKAIA_PORTAL);
	}
	
	@Override
	public void setWorld(World worldIn)
	{
		super.setWorld(worldIn);
		if(destination.getDimension() == worldIn.getDimension().getType())
			destination = GlobalPos.of(worldIn.getDimension().getType() == MSDimensions.skaiaDimension ? DimensionType.OVERWORLD : MSDimensions.skaiaDimension, destination.getPos());
	}
	
	@Override
	public void read(CompoundNBT compound)
	{
		super.read(compound);
		destination = GlobalPos.deserialize(new Dynamic<>(NBTDynamicOps.INSTANCE, compound.getCompound("dest")));
	}
	
	@Override
	public CompoundNBT write(CompoundNBT compound)
	{
		super.write(compound);
		compound.put("dest", destination.serialize(NBTDynamicOps.INSTANCE));
		
		return compound;
	}
	
	public void teleportEntity(Entity entity)
	{
		if(destination.getDimension() != this.world.getDimension().getType())
		{
			if(destination.getPos().getY() < 0)
			{
				ServerWorld world = DimensionManager.getWorld(entity.getServer(), destination.getDimension(), true, true);
				if(world == null)
					return;
				//TODO gets world height on a chunk that doesn't exist
				// However doesn't matter a lot since the position isn't used yet
				destination = GlobalPos.of(destination.getDimension(), world.getHeight(Heightmap.Type.WORLD_SURFACE, new BlockPos(entity)).up(5));
			}
			entity = Teleport.teleportEntity(entity, DimensionManager.getWorld(entity.getServer(), destination.getDimension(), true, true), pos.getX() + 0.5, pos.getY(), pos.getZ() + 0.5);
			if(entity != null)
				placeDestPlatform(entity.world);
		}
		if(entity != null)
			entity.timeUntilPortal = entity.getPortalCooldown();
	}
	
	private void placeDestPlatform(World world)
	{
		double x = pos.getX();
		double y = pos.getY();
		double z = pos.getZ();
		Block[] blocks = {MSBlocks.BLACK_CHESS_DIRT, MSBlocks.LIGHT_GRAY_CHESS_DIRT, MSBlocks.WHITE_CHESS_DIRT, MSBlocks.DARK_GRAY_CHESS_DIRT};
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
