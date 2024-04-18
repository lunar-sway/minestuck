package com.mraof.minestuck.network;

import com.mraof.minestuck.Minestuck;
import com.mraof.minestuck.api.alchemy.GristType;
import com.mraof.minestuck.api.alchemy.GristTypes;
import com.mraof.minestuck.blockentity.machine.GristWildcardHolder;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Objects;

public class GristWildcardPacket implements MSPacket.PlayToServer
{
	public static final ResourceLocation ID = Minestuck.id("grist_wildcard");
	
	private static final Logger LOGGER = LogManager.getLogger();
	
	private final GristType gristType;
	private final BlockPos pos;
	
	public GristWildcardPacket(BlockPos pos, GristType gristType)
	{
		this.gristType = Objects.requireNonNull(gristType);
		this.pos = Objects.requireNonNull(pos);
	}
	
	@Override
	public ResourceLocation id()
	{
		return ID;
	}
	
	@Override
	public void write(FriendlyByteBuf buffer)
	{
		buffer.writeId(GristTypes.REGISTRY, gristType);
		buffer.writeBlockPos(pos);
	}
	
	public static GristWildcardPacket read(FriendlyByteBuf buffer)
	{
		GristType gristType = buffer.readById(GristTypes.REGISTRY);
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