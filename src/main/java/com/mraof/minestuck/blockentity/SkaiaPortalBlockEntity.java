package com.mraof.minestuck.blockentity;

import com.mraof.minestuck.block.SkaiaBlocks;
import com.mraof.minestuck.util.Teleport;
import com.mraof.minestuck.world.MSDimensions;
import net.minecraft.core.BlockPos;
import net.minecraft.core.GlobalPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.nbt.Tag;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.Heightmap;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class SkaiaPortalBlockEntity extends BlockEntity //implements ITeleporter
{
	private static final Logger LOGGER = LogManager.getLogger();
	
	private GlobalPos destination = GlobalPos.of(MSDimensions.SKAIA, new BlockPos(0, -1, 0));
	
	public SkaiaPortalBlockEntity(BlockPos pos, BlockState state)
	{
		super(MSBlockEntityTypes.SKAIA_PORTAL.get(), pos, state);
	}
	
	@Override
	public void setLevel(Level level)
	{
		super.setLevel(level);
		if(!level.isClientSide && destination.dimension() == level.dimension())
			destination = GlobalPos.of(level.dimension() == MSDimensions.SKAIA ? Level.OVERWORLD : MSDimensions.SKAIA, destination.pos());
	}
	
	@Override
	public void load(CompoundTag nbt)
	{
		super.load(nbt);
		if(nbt.contains("dest", Tag.TAG_COMPOUND))
			destination = GlobalPos.CODEC.parse(NbtOps.INSTANCE, nbt.get("dest")).resultOrPartial(LOGGER::error).orElse(destination);
	}
	
	@Override
	public void saveAdditional(CompoundTag compound)
	{
		super.saveAdditional(compound);
		GlobalPos.CODEC.encodeStart(NbtOps.INSTANCE, destination).resultOrPartial(LOGGER::error)
				.ifPresent(tag -> compound.put("dest", tag));
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
				ServerLevel world = server.getLevel(destination.dimension());
				if(world == null)
					return;
				//TODO gets world height on a chunk that doesn't exist
				// However doesn't matter a lot since the position isn't used yet
				destination = GlobalPos.of(destination.dimension(), world.getHeightmapPos(Heightmap.Types.WORLD_SURFACE, entity.blockPosition()).above(5));
			}
			entity = Teleport.teleportEntity(entity, server.getLevel(destination.dimension()), worldPosition.getX() + 0.5, worldPosition.getY(), worldPosition.getZ() + 0.5);
			if(entity != null)
				placeDestPlatform(entity.level());
		}
		if(entity != null)
				entity.setPortalCooldown();
	}
	
	private void placeDestPlatform(Level level)
	{
		double x = worldPosition.getX();
		double y = worldPosition.getY();
		double z = worldPosition.getZ();
		Block[] blocks = {SkaiaBlocks.BLACK_CHESS_DIRT.asBlock(), SkaiaBlocks.LIGHT_GRAY_CHESS_DIRT.asBlock(), SkaiaBlocks.WHITE_CHESS_DIRT.asBlock(), SkaiaBlocks.DARK_GRAY_CHESS_DIRT.asBlock()};
		for(int blockX = (int) x - 2; blockX < x + 2; blockX++)
		{
			for(int blockZ = (int) z - 2; blockZ < z + 2; blockZ++)
			{
				level.setBlock(new BlockPos(blockX, (int) y - 1, blockZ), blocks[(blockX + blockZ) & 3].defaultBlockState(), Block.UPDATE_ALL);
				for(int blockY = (int) y; blockY < y + 6; blockY++)
					level.removeBlock(new BlockPos(blockX, blockY, blockZ), false);
			}
		}
	}
	
}
