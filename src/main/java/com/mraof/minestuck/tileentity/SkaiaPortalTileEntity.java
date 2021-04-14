package com.mraof.minestuck.tileentity;

import com.mraof.minestuck.block.MSBlocks;
import com.mraof.minestuck.util.Debug;
import com.mraof.minestuck.util.Teleport;
import com.mraof.minestuck.world.MSDimensions;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.NBTDynamicOps;
import net.minecraft.server.MinecraftServer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.GlobalPos;
import net.minecraft.world.World;
import net.minecraft.world.gen.Heightmap;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.common.util.Constants;

public class SkaiaPortalTileEntity extends TileEntity //implements ITeleporter
{
	private GlobalPos destination = GlobalPos.getPosition(MSDimensions.skaiaDimension, new BlockPos(0, -1, 0));
	
	public SkaiaPortalTileEntity()
	{
		super(MSTileEntityTypes.SKAIA_PORTAL.get());
	}
	
	@Override
	public void setWorldAndPos(World worldIn, BlockPos pos)
	{
		super.setWorldAndPos(worldIn, pos);
		if(!worldIn.isRemote && destination.getDimension() == worldIn.getDimensionKey())
			destination = GlobalPos.getPosition(worldIn.getDimensionKey() == MSDimensions.skaiaDimension ? World.OVERWORLD : MSDimensions.skaiaDimension, destination.getPos());
	}
	
	@Override
	public void read(BlockState state, CompoundNBT nbt)
	{
		super.read(state, nbt);
		if(nbt.contains("dest", Constants.NBT.TAG_COMPOUND))
			destination = GlobalPos.CODEC.parse(NBTDynamicOps.INSTANCE, nbt.get("dest")).resultOrPartial(Debug::error).orElse(destination);
	}
	
	@Override
	public CompoundNBT write(CompoundNBT compound)
	{
		super.write(compound);
		GlobalPos.CODEC.encodeStart(NBTDynamicOps.INSTANCE, destination).resultOrPartial(Debug::error)
				.ifPresent(tag -> compound.put("dest", tag));
		
		return compound;
	}
	
	public void teleportEntity(Entity entity)
	{
		MinecraftServer server = entity.getServer();
		if(server == null || world == null)
			return;
		
		if(destination.getDimension() != this.world.getDimensionKey())
		{
			if(destination.getPos().getY() < 0)
			{
				ServerWorld world = server.getWorld(destination.getDimension());
				if(world == null)
					return;
				//TODO gets world height on a chunk that doesn't exist
				// However doesn't matter a lot since the position isn't used yet
				destination = GlobalPos.getPosition(destination.getDimension(), world.getHeight(Heightmap.Type.WORLD_SURFACE, entity.getPosition()).up(5));
			}
			entity = Teleport.teleportEntity(entity, server.getWorld(destination.getDimension()), pos.getX() + 0.5, pos.getY(), pos.getZ() + 0.5);
			if(entity != null)
				placeDestPlatform(entity.world);
		}
		if(entity != null)
				entity.setPortalCooldown();
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
				world.setBlockState(new BlockPos(blockX, (int) y - 1, blockZ), blocks[(blockX + blockZ) & 3].getDefaultState(), Constants.BlockFlags.DEFAULT);
				for(int blockY = (int) y; blockY < y + 6; blockY++)
					world.removeBlock(new BlockPos(blockX, blockY, blockZ), false);
			}
		}
	}
	
}
