package com.mraof.minestuck.network.block;

import com.mraof.minestuck.Minestuck;
import com.mraof.minestuck.block.redstone.StatStorerBlock;
import com.mraof.minestuck.blockentity.redstone.StatStorerBlockEntity;
import com.mraof.minestuck.network.MSPacket;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;

public record StatStorerSettingsPacket(StatStorerBlockEntity.ActiveType activeType, int divideValueBy, BlockPos beBlockPos) implements MSPacket.PlayToServer
{
	public static final ResourceLocation ID = Minestuck.id("stat_storer_settings");
	
	@Override
	public ResourceLocation id()
	{
		return ID;
	}
	
	@Override
	public void write(FriendlyByteBuf buffer)
	{
		buffer.writeEnum(activeType);
		buffer.writeInt(divideValueBy);
		buffer.writeBlockPos(beBlockPos);
	}
	
	public static StatStorerSettingsPacket read(FriendlyByteBuf buffer)
	{
		StatStorerBlockEntity.ActiveType activeType = buffer.readEnum(StatStorerBlockEntity.ActiveType.class);
		int divideValueBy = buffer.readInt();
		BlockPos beBlockPos = buffer.readBlockPos();
		
		return new StatStorerSettingsPacket(activeType, divideValueBy, beBlockPos);
	}
	
	@Override
	public void execute(ServerPlayer player)
	{
		if(!StatStorerBlock.canInteract(player))
			return;
		
		MSPacket.getAccessibleBlockEntity(player, this.beBlockPos, StatStorerBlockEntity.class)
				.ifPresent(statStorer -> statStorer.handleSettingsPacket(this));
	}
}
