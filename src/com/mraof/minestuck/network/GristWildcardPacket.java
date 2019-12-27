package com.mraof.minestuck.network;

import com.mraof.minestuck.item.crafting.alchemy.GristType;
import com.mraof.minestuck.tileentity.AlchemiterTileEntity;
import com.mraof.minestuck.tileentity.MiniAlchemiterTileEntity;
import com.mraof.minestuck.util.Debug;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;

import java.util.Objects;

public class GristWildcardPacket implements PlayToServerPacket
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
	
	@Override
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