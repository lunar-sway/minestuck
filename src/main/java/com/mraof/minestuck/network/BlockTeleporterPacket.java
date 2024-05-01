package com.mraof.minestuck.network;

import com.mraof.minestuck.Minestuck;
import com.mraof.minestuck.blockentity.redstone.BlockTeleporterBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.block.state.BlockState;

public class BlockTeleporterPacket implements MSPacket.PlayToServer
{
	public static final ResourceLocation ID = Minestuck.id("block_teleporter");
	
	private final BlockPos offsetPos;
	private final BlockPos beBlockPos;
	
	public BlockTeleporterPacket(BlockPos offsetPos, BlockPos beBlockPos)
	{
		this.offsetPos = offsetPos;
		this.beBlockPos = beBlockPos;
	}
	
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
		if(player.level().isAreaLoaded(beBlockPos, 0))
		{
			if(player.level().getBlockEntity(beBlockPos) instanceof BlockTeleporterBlockEntity blockTeleporter)
			{
				if(Math.sqrt(player.distanceToSqr(beBlockPos.getX() + 0.5, beBlockPos.getY() + 0.5, beBlockPos.getZ() + 0.5)) <= 8)
				{
					blockTeleporter.setTeleportOffset(offsetPos);
					//Imitates the structure block to ensure that changes are sent client-side
					blockTeleporter.setChanged();
					BlockState state = player.level().getBlockState(beBlockPos);
					player.level().sendBlockUpdated(beBlockPos, state, state, 3);
				}
			}
		}
	}
}