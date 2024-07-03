package com.mraof.minestuck.network.block;

import com.mraof.minestuck.Minestuck;
import com.mraof.minestuck.api.alchemy.GristType;
import com.mraof.minestuck.api.alchemy.GristTypes;
import com.mraof.minestuck.blockentity.machine.GristWildcardHolder;
import com.mraof.minestuck.network.MSPacket;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;

public record SetWildcardGristPacket(BlockPos pos, GristType gristType) implements MSPacket.PlayToServer
{
	public static final ResourceLocation ID = Minestuck.id("set_wildcard_grist");
	
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
	
	public static SetWildcardGristPacket read(FriendlyByteBuf buffer)
	{
		GristType gristType = buffer.readById(GristTypes.REGISTRY);
		BlockPos pos = buffer.readBlockPos();
		
		return new SetWildcardGristPacket(pos, gristType);
	}
	
	@Override
	public void execute(ServerPlayer player)
	{
		MSPacket.getAccessibleBlockEntity(player, this.pos, GristWildcardHolder.class)
				.ifPresent(blockEntity -> blockEntity.setWildcardGrist(gristType));
	}
}
