package com.mraof.minestuck.network.block;

import com.mraof.minestuck.Minestuck;
import com.mraof.minestuck.blockentity.redstone.StatStorerBlockEntity;
import com.mraof.minestuck.network.MSPacket;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;

public record StatStorerPacket(StatStorerBlockEntity.ActiveType activeType, BlockPos beBlockPos, int divideValueBy) implements MSPacket.PlayToServer
{
	public static final ResourceLocation ID = Minestuck.id("stat_storer");
	
	@Override
	public ResourceLocation id()
	{
		return ID;
	}
	
	@Override
	public void write(FriendlyByteBuf buffer)
	{
		buffer.writeEnum(activeType);
		buffer.writeBlockPos(beBlockPos);
		buffer.writeInt(divideValueBy);
	}
	
	public static StatStorerPacket read(FriendlyByteBuf buffer)
	{
		StatStorerBlockEntity.ActiveType activeType = buffer.readEnum(StatStorerBlockEntity.ActiveType.class);
		BlockPos beBlockPos = buffer.readBlockPos();
		int divideValueBy = buffer.readInt();
		
		return new StatStorerPacket(activeType, beBlockPos, divideValueBy);
	}
	
	@Override
	public void execute(ServerPlayer player)
	{
		MSPacket.getAccessibleBlockEntity(player, this.beBlockPos, StatStorerBlockEntity.class).ifPresent(statStorer ->
		{
			int largestDivideValueBy = Math.max(1, divideValueBy); //should not be able to enter 0 or negative number range
			statStorer.setActiveTypeAndDivideValue(activeType, largestDivideValueBy);
		});
	}
}
