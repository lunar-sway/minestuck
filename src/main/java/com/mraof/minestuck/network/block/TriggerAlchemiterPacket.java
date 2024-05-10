package com.mraof.minestuck.network.block;

import com.mraof.minestuck.Minestuck;
import com.mraof.minestuck.blockentity.machine.AlchemiterBlockEntity;
import com.mraof.minestuck.network.MSPacket;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;

public record TriggerAlchemiterPacket(int quantity, BlockPos pos) implements MSPacket.PlayToServer
{
	public static final ResourceLocation ID = Minestuck.id("trigger_alchemiter");
	
	@Override
	public ResourceLocation id()
	{
		return ID;
	}
	
	@Override
	public void write(FriendlyByteBuf buffer)
	{
		buffer.writeBlockPos(pos);
		buffer.writeInt(quantity);
	}
	
	public static TriggerAlchemiterPacket read(FriendlyByteBuf buffer)
	{
		BlockPos pos = buffer.readBlockPos();
		int quantity = buffer.readInt();
		
		return new TriggerAlchemiterPacket(quantity, pos);
	}
	
	@Override
	public void execute(ServerPlayer player)
	{
		MSPacket.getAccessibleBlockEntity(player, this.pos, AlchemiterBlockEntity.class)
				.ifPresent(alchemiter -> alchemiter.processContents(quantity, player));
	}
}
