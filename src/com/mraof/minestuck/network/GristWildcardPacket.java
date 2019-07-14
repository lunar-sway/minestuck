package com.mraof.minestuck.network;

import com.mraof.minestuck.tileentity.AlchemiterTileEntity;
import com.mraof.minestuck.tileentity.MiniAlchemiterTileEntity;
import com.mraof.minestuck.util.Debug;
import com.mraof.minestuck.alchemy.GristType;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.network.PacketBuffer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.network.NetworkDirection;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

public class GristWildcardPacket
{
	
	private final BlockPos pos;
	private final GristType gristType;
	
	public GristWildcardPacket(BlockPos pos, GristType gristType)
	{
		this.pos = pos;
		this.gristType = gristType;
	}
	
	public void encode(PacketBuffer buffer)
	{
		buffer.writeBlockPos(pos);
		buffer.writeInt(GristType.REGISTRY.getID(gristType));
	}
	
	public static GristWildcardPacket decode(PacketBuffer buffer)
	{
		BlockPos pos = buffer.readBlockPos();
		GristType gristType = GristType.REGISTRY.getValue(buffer.readInt());
		
		return new GristWildcardPacket(pos, gristType);
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
			TileEntity te = player.getEntityWorld().getTileEntity(pos);
			if(te instanceof MiniAlchemiterTileEntity)
			{
				((MiniAlchemiterTileEntity) te).setWildcardGrist(gristType);
			} else if(te instanceof AlchemiterTileEntity)
			{
				((AlchemiterTileEntity) te).setWildcardGrist(gristType);
			} else
			{
				Debug.warnf("No tile entity found at %s for packet sent by player %s!", pos, player.getName());
			}
		}
	}
}