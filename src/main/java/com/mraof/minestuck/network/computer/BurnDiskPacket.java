package com.mraof.minestuck.network.computer;

import com.mraof.minestuck.Minestuck;
import com.mraof.minestuck.blockentity.ComputerBlockEntity;
import com.mraof.minestuck.network.MSPacket;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;

public record BurnDiskPacket(BlockPos computerPos, int programId) implements MSPacket.PlayToServer
{
	public static final ResourceLocation ID = Minestuck.id("burn_disk");
	
	public static BurnDiskPacket create(ComputerBlockEntity be, int programId)
	{
		return new BurnDiskPacket(be.getBlockPos(), programId);
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
		buffer.writeInt(programId);
	}
	
	public static BurnDiskPacket read(FriendlyByteBuf buffer)
	{
		BlockPos bePos = buffer.readBlockPos();
		int programId = buffer.readInt();
		return new BurnDiskPacket(bePos, programId);
	}
	
	@Override
	public void execute(ServerPlayer player)
	{
		ComputerBlockEntity.getAccessibleComputer(player, computerPos)
				.ifPresent(computer -> computer.burnDisk(programId));
	}
}
