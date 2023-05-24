package com.mraof.minestuck.network.computer;

import com.mraof.minestuck.blockentity.ComputerBlockEntity;
import com.mraof.minestuck.network.PlayToServerPacket;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;

public class BurnDiskPacket implements PlayToServerPacket
{
	private final BlockPos bePos;
	private final int programId;
	
	public BurnDiskPacket(BlockPos pos, int programId)
	{
		this.bePos = pos;
		this.programId = programId;
	}
	
	public static BurnDiskPacket create(ComputerBlockEntity be, int programId)
	{
		return new BurnDiskPacket(be.getBlockPos(), programId);
	}
	
	@Override
	public void encode(FriendlyByteBuf buffer)
	{
		buffer.writeBlockPos(bePos);
		buffer.writeInt(programId);
	}
	
	public static BurnDiskPacket decode(FriendlyByteBuf buffer)
	{
		BlockPos bePos = buffer.readBlockPos();
		int programId = buffer.readInt();
		return new BurnDiskPacket(bePos, programId);
	}
	
	@Override
	public void execute(ServerPlayer player)
	{
		ComputerBlockEntity.forNetworkIfPresent(player, bePos,
				computer -> computer.burnDisk(programId));
	}
}
