package com.mraof.minestuck.network.editmode;

import com.mraof.minestuck.Minestuck;
import com.mraof.minestuck.computer.editmode.EditData;
import com.mraof.minestuck.computer.editmode.EditmodeLocations;
import com.mraof.minestuck.network.MSPacket;
import com.mraof.minestuck.world.storage.MSExtraData;
import io.netty.buffer.ByteBuf;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public record EditmodeTeleportPacket(BlockPos pos) implements MSPacket.PlayToServer
{
	
	public static final Type<EditmodeTeleportPacket> ID = new Type<>(Minestuck.id("editmode_teleport"));
	public static final StreamCodec<ByteBuf, EditmodeTeleportPacket> STREAM_CODEC = BlockPos.STREAM_CODEC.map(EditmodeTeleportPacket::new, EditmodeTeleportPacket::pos);
	
	@Override
	public Type<? extends CustomPacketPayload> type()
	{
		return ID;
	}
	
	@SuppressWarnings("resource")
	@Override
	public void execute(IPayloadContext context, ServerPlayer player)
	{
		EditData editData = MSExtraData.get(player.serverLevel()).findEditData(data -> data.getEditor() == player);
		
		if(editData == null)
			return;
		
		if(EditmodeLocations.checkIsValidSourcePos(editData, player.level().dimension(), this.pos))
			player.teleportTo(pos.getX() + 0.5D, pos.getY() + 1.0D, pos.getZ() + 0.5D);
	}
}
