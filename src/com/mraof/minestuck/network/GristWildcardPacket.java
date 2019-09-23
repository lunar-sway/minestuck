package com.mraof.minestuck.network;

import com.mraof.minestuck.tileentity.AlchemiterTileEntity;
import com.mraof.minestuck.tileentity.MiniAlchemiterTileEntity;
import com.mraof.minestuck.util.Debug;
import com.mraof.minestuck.item.crafting.alchemy.GristType;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.network.NetworkDirection;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.Objects;
import java.util.function.Supplier;

public class GristWildcardPacket
{
	
	private final GristType gristType;
	private final BlockPos pos;
	
	public GristWildcardPacket(BlockPos pos, GristType gristType)
	{
		this.gristType = Objects.requireNonNull(gristType);
		this.pos = Objects.requireNonNull(pos);
	}
	
	public void encode(PacketBuffer buffer)
	{
		buffer.writeRegistryId(gristType);
		buffer.writeBlockPos(pos);
	}
	
	public static GristWildcardPacket decode(PacketBuffer buffer)
	{
		GristType gristType = buffer.readRegistryIdSafe(GristType.class);
		BlockPos pos = buffer.readBlockPos();
		
		return new GristWildcardPacket(pos, gristType);
	}
	
	public void consume(Supplier<NetworkEvent.Context> ctx)
	{
		if(ctx.get().getDirection() == NetworkDirection.PLAY_TO_SERVER)
			ctx.get().enqueueWork(() -> this.execute(ctx.get().getSender()));
		
		ctx.get().setPacketHandled(true);
	}
	
	public void execute(ServerPlayerEntity player)
	{
		if(player != null && player.getEntityWorld().isAreaLoaded(pos, 0))
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