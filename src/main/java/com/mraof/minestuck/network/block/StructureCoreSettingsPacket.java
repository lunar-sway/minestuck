package com.mraof.minestuck.network.block;

import com.mraof.minestuck.Minestuck;
import com.mraof.minestuck.block.redstone.StructureCoreBlock;
import com.mraof.minestuck.blockentity.redstone.StructureCoreBlockEntity;
import com.mraof.minestuck.network.MSPacket;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;

public record StructureCoreSettingsPacket(StructureCoreBlockEntity.ActionType actionType, int shutdownRange, BlockPos beBlockPos) implements MSPacket.PlayToServer
{
	public static final ResourceLocation ID = Minestuck.id("structure_core_settings");
	
	@Override
	public ResourceLocation id()
	{
		return ID;
	}
	
	@Override
	public void write(FriendlyByteBuf buffer)
	{
		buffer.writeEnum(actionType);
		buffer.writeInt(shutdownRange);
		buffer.writeBlockPos(beBlockPos);
	}
	
	public static StructureCoreSettingsPacket read(FriendlyByteBuf buffer)
	{
		StructureCoreBlockEntity.ActionType actionType = buffer.readEnum(StructureCoreBlockEntity.ActionType.class);
		int summonRange = buffer.readInt();
		BlockPos beBlockPos = buffer.readBlockPos();
		
		return new StructureCoreSettingsPacket(actionType, summonRange, beBlockPos);
	}
	
	@Override
	public void execute(ServerPlayer player)
	{
		if(!StructureCoreBlock.canInteract(player))
			return;
		
		MSPacket.getAccessibleBlockEntity(player, this.beBlockPos, StructureCoreBlockEntity.class)
				.ifPresent(structureCore -> structureCore.handleSettingsPacket(this));
	}
}
