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
	private GlobalPos destination = GlobalPos.of(MSDimensions.skaiaDimension, new BlockPos(0, -1, 0));
	
	public SkaiaPortalTileEntity()
	{
		super(MSTileEntityTypes.SKAIA_PORTAL.get());
	}
	
	@Override
	public void setLevelAndPosition(World worldIn, BlockPos pos)
	{
		super.setLevelAndPosition(worldIn, pos);
		if(!worldIn.isClientSide && destination.dimension() == worldIn.dimension())
			destination = GlobalPos.of(worldIn.dimension() == MSDimensions.skaiaDimension ? World.OVERWORLD : MSDimensions.skaiaDimension, destination.pos());
	}
	
	@Override
	public void load(BlockState state, CompoundNBT nbt)
	{
		super.load(state, nbt);
		if(nbt.contains("dest", Constants.NBT.TAG_COMPOUND))
			destination = GlobalPos.CODEC.parse(NBTDynamicOps.INSTANCE, nbt.get("dest")).resultOrPartial(Debug::error).orElse(destination);
	}
	
	@Override
	public CompoundNBT save(CompoundNBT compound)
	{
		super.save(compound);
		GlobalPos.CODEC.encodeStart(NBTDynamicOps.INSTANCE, destination).resultOrPartial(Debug::error)
				.ifPresent(tag -> compound.put("dest", tag));
		
		return compound;
	}
	
	public void teleportEntity(Entity entity)
	{
		MinecraftServer server = entity.getServer();
		if(server == null || level == null)
			return;
		
		if(destination.dimension() != this.level.dimension())
		{
			if(destination.pos().getY() < 0)
			{
				ServerWorld world = server.getLevel(destination.dimension());
				if(world == null)
					return;
				//TODO gets world height on a chunk that doesn't exist
				// However doesn't matter a lot since the position isn't used yet
				destination = GlobalPos.of(destination.dimension(), world.getHeightmapPos(Heightmap.Type.WORLD_SURFACE, entity.blockPosition()).above(5));
			}
			entity = Teleport.teleportEntity(entity, server.getLevel(destination.dimension()), worldPosition.getX() + 0.5, worldPosition.getY(), worldPosition.getZ() + 0.5);
			if(entity != null)
				placeDestPlatform(entity.level);
		}
		if(entity != null)
				entity.setPortalCooldown();
	}
	
	private void placeDestPlatform(World world)
	{
		double x = worldPosition.getX();
		double y = worldPosition.getY();
		double z = worldPosition.getZ();
		Block[] blocks = {MSBlocks.BLACK_CHESS_DIRT, MSBlocks.LIGHT_GRAY_CHESS_DIRT, MSBlocks.WHITE_CHESS_DIRT, MSBlocks.DARK_GRAY_CHESS_DIRT};
		for(int blockX = (int) x - 2; blockX < x + 2; blockX++)
		{
			for(int blockZ = (int) z - 2; blockZ < z + 2; blockZ++)
			{
				world.setBlock(new BlockPos(blockX, (int) y - 1, blockZ), blocks[(blockX + blockZ) & 3].defaultBlockState(), Constants.BlockFlags.DEFAULT);
				for(int blockY = (int) y; blockY < y + 6; blockY++)
					world.removeBlock(new BlockPos(blockX, blockY, blockZ), false);
			}
		}
	}
	
}
