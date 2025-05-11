package com.mraof.minestuck.blockentity;

import com.mraof.minestuck.block.SkaiaBlocks;
import com.mraof.minestuck.util.Teleport;
import com.mraof.minestuck.world.MSDimensions;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public final class SkaiaPortalBlockEntity extends BlockEntity //implements ITeleporter
{
	private static final Logger LOGGER = LogManager.getLogger();
	
	public SkaiaPortalBlockEntity(BlockPos pos, BlockState state)
	{
		super(MSBlockEntityTypes.SKAIA_PORTAL.get(), pos, state);
	}
	
	public void teleportEntity(Entity entity)
	{
		MinecraftServer server = entity.getServer();
		if(server == null || level == null)
			return;
		
		Entity newEntity = Teleport.teleportEntity(entity, server.getLevel(this.destinationDimension()),
				worldPosition.getX() + 0.5, worldPosition.getY(), worldPosition.getZ() + 0.5);
		if(newEntity != null)
		{
			placeDestPlatform(newEntity.level());
			newEntity.setPortalCooldown();
		}
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
	
	private ResourceKey<Level> destinationDimension()
	{
		return level != null && level.dimension() == MSDimensions.SKAIA ? Level.OVERWORLD : MSDimensions.SKAIA;
	}
}
