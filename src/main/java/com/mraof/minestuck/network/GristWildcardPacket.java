package com.mraof.minestuck.network;

import com.mraof.minestuck.alchemy.GristType;
import com.mraof.minestuck.blockentity.machine.GristWildcardHolder;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Objects;

public class GristWildcardPacket implements PlayToServerPacket
{
	private static final Logger LOGGER = LogManager.getLogger();
	
	private final GristType gristType;
	private final BlockPos pos;
	
	public GristWildcardPacket(BlockPos pos, GristType gristType)
	{
		this.gristType = Objects.requireNonNull(gristType);
		this.pos = Objects.requireNonNull(pos);
	}
	
	@Override
	public void encode(FriendlyByteBuf buffer)
	{
		buffer.writeRegistryId(gristType);
		buffer.writeBlockPos(pos);
	}
	
	public static GristWildcardPacket decode(FriendlyByteBuf buffer)
	{
		GristType gristType = buffer.readRegistryIdSafe(GristType.class);
		BlockPos pos = buffer.readBlockPos();
		
		return new GristWildcardPacket(pos, gristType);
	}
	
	@Override
	public void execute(ServerPlayer player)
	{
		if(player != null && player.getCommandSenderWorld().isAreaLoaded(pos, 0))
		{
			if(player.getCommandSenderWorld().getBlockEntity(pos) instanceof GristWildcardHolder blockEntity)
				blockEntity.setWildcardGrist(gristType);
			else
				LOGGER.warn("No block entity found at {} for packet sent by player {}!", pos, player.getName());
		}
	}
}