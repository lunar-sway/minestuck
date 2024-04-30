package com.mraof.minestuck.network.block;

import com.mraof.minestuck.Minestuck;
import com.mraof.minestuck.block.redstone.BlockTeleporterBlock;
import com.mraof.minestuck.blockentity.redstone.BlockTeleporterBlockEntity;
import com.mraof.minestuck.network.MSPacket;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.block.state.BlockState;

public record BlockTeleporterPacket(BlockPos offsetPos, BlockPos beBlockPos) implements MSPacket.PlayToServer
{
	public static final ResourceLocation ID = Minestuck.id("block_teleporter");
	
	@Override
	public ResourceLocation id()
	{
		return ID;
	}
	
	@Override
	public void write(FriendlyByteBuf buffer)
	{
		buffer.writeBlockPos(offsetPos);
		buffer.writeBlockPos(beBlockPos);
	}
	
	public static BlockTeleporterPacket read(FriendlyByteBuf buffer)
	{
		BlockPos offsetPos = buffer.readBlockPos();
		BlockPos beBlockPos = buffer.readBlockPos();
		
		return new BlockTeleporterPacket(offsetPos, beBlockPos);
	}
	
	@SuppressWarnings("resource")
	@Override
	public void execute(ServerPlayer player)
	{
		if(!BlockTeleporterBlock.canInteract(player))
			return;
		
		MSPacket.getAccessibleBlockEntity(player, this.beBlockPos, BlockTeleporterBlockEntity.class).ifPresent(blockTeleporter ->
		{
			blockTeleporter.setTeleportOffset(offsetPos);
			//Imitates the structure block to ensure that changes are sent client-side
			blockTeleporter.setChanged();
			BlockState state = player.level().getBlockState(beBlockPos);
			player.level().sendBlockUpdated(beBlockPos, state, state, 3);
		});
	}
}