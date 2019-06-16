package com.mraof.minestuck.network;

import com.mraof.minestuck.tileentity.TileEntityTransportalizer;
import net.minecraft.entity.player.EntityPlayerMP;
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
	
	public void execute(EntityPlayerMP player)
	{
		if(player.getEntityWorld().isBlockLoaded(pos))
		{
			TileEntity te = player.world.getTileEntity(pos);
			if(te instanceof TileEntityTransportalizer)
			{
				((TileEntityTransportalizer) te).setDestId(destId);
			}
		}
	}
}