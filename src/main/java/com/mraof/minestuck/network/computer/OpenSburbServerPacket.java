package com.mraof.minestuck.network.computer;

import com.mraof.minestuck.Minestuck;
import com.mraof.minestuck.blockentity.ComputerBlockEntity;
import com.mraof.minestuck.network.MSPacket;
import com.mraof.minestuck.skaianet.ComputerInteractions;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;

public record OpenSburbServerPacket(BlockPos computerPos) implements MSPacket.PlayToServer
{
	public static final ResourceLocation ID = Minestuck.id("open_sburb_server");
	
	public static OpenSburbServerPacket create(ComputerBlockEntity be)
	{
		return new OpenSburbServerPacket(be.getBlockPos());
	}
	
	@Override
	public ResourceLocation id()
	{
		return ID;
	}
	
	@Override
	public void write(FriendlyByteBuf buffer)
	{
		buffer.writeBlockPos(computerPos);
	}
	
	public static OpenSburbServerPacket read(FriendlyByteBuf buffer)
	{
		BlockPos computer = buffer.readBlockPos();
		return new OpenSburbServerPacket(computer);
	}
	
	@Override
	public void execute(ServerPlayer player)
	{
		ComputerBlockEntity.getAccessibleComputer(player, computerPos)
				.ifPresent(computer -> ComputerInteractions.get(player.server).openServer(computer));
	}
}