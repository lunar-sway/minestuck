package com.mraof.minestuck.blockentity;

import com.mraof.minestuck.advancements.MSCriteriaTriggers;
import com.mraof.minestuck.util.ColorHandler;
import com.mraof.minestuck.util.Teleport;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.Connection;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

@MethodsReturnNonnullByDefault
public class ReturnNodeBlockEntity extends OnCollisionTeleporterBlockEntity<ServerPlayer>
{
	//Only used client-side
	public int color;
	
	public ReturnNodeBlockEntity(BlockPos pos, BlockState state)
	{
		super(MSBlockEntityTypes.RETURN_NODE.get(), pos, state, ServerPlayer.class);
	}
	
	@Override
	protected AABB getTeleportField()
	{
		return new AABB(worldPosition.getX() - 1, worldPosition.getY() + 7D / 16, worldPosition.getZ() - 1, worldPosition.getX() + 1, worldPosition.getY() + 9D / 16, worldPosition.getZ() + 1);
	}
	
	@Override
	protected void teleport(ServerPlayer player)
	{
		if(level instanceof ServerLevel serverLevel)
		{
			BlockPos spawnPos = serverLevel.getSharedSpawnPos();
			// "level.getHeightmapPos()" will default to y = 0 if the chunk isn't loaded,
			// so we get the height from the chunk directly to get an accurate height.
			int spawnHeight = level.getChunk(spawnPos).getHeight(Heightmap.Types.MOTION_BLOCKING, spawnPos.getX(), spawnPos.getZ());
			BlockPos pos = new BlockPos(spawnPos.getX(), spawnHeight, spawnPos.getZ());
			
			Teleport.teleportEntity(player, serverLevel, pos.getX() + 0.5, pos.getY() + 1, pos.getZ() + 0.5);
			player.setPortalCooldown();
			player.setDeltaMovement(Vec3.ZERO);
			player.fallDistance = 0;
			MSCriteriaTriggers.RETURN_NODE.get().trigger(player);
		}
	}
	
	@Override
	public CompoundTag getUpdateTag()
	{
		CompoundTag nbt = super.getUpdateTag();
		nbt.putInt("color", ColorHandler.getColorForDimension((ServerLevel) level));
		return nbt;
	}
	
	@Override
	public Packet<ClientGamePacketListener> getUpdatePacket()
	{
		return ClientboundBlockEntityDataPacket.create(this);
	}
	
	@Override
	public void handleUpdateTag(CompoundTag tag)
	{
		this.color = tag.getInt("color");
	}
	
	@Override
	public void onDataPacket(Connection net, ClientboundBlockEntityDataPacket pkt)
	{
		handleUpdateTag(pkt.getTag());
	}
	
}