package com.mraof.minestuck.network;

import com.mraof.minestuck.tileentity.TransportalizerTileEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.network.NetworkDirection;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

public class TransportalizerPacket
{
	BlockPos pos;
	String destId;
	
	public TransportalizerPacket(BlockPos pos, String destId)
	{
		this.pos = pos;
		this.destId = destId;
	}
	
	public void encode(PacketBuffer buffer)
	{
		buffer.writeBlockPos(pos);
		buffer.writeString(destId, 4);
	}
	
	public static TransportalizerPacket decode(PacketBuffer buffer)
	{
		BlockPos pos = buffer.readBlockPos();
		String destId = buffer.readString(4);
		
		return new TransportalizerPacket(pos, destId);
	}
	
	public void consume(Supplier<NetworkEvent.Context> ctx)
	{
		if(ctx.get().getDirection() == NetworkDirection.PLAY_TO_SERVER)
			ctx.get().enqueueWork(() -> this.execute(ctx.get().getSender()));
		
		ctx.get().setPacketHandled(true);
	}
	
	public void execute(ServerPlayerEntity player)
	{
		if(player.getEntityWorld().isAreaLoaded(pos, 0))
		{
			TileEntity te = player.world.getTileEntity(pos);
			if(te instanceof TransportalizerTileEntity)
			{
				((TransportalizerTileEntity) te).setDestId(destId);
			}
		}
	}
}