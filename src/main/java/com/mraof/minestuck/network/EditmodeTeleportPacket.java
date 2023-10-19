package com.mraof.minestuck.network;

import com.mraof.minestuck.computer.editmode.EditData;
import com.mraof.minestuck.computer.editmode.EditmodeLocations;
import com.mraof.minestuck.world.storage.MSExtraData;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;

public class EditmodeTeleportPacket implements PlayToServerPacket
{
	public EditmodeTeleportPacket()
	{
	}
	
	@Override
	public void encode(FriendlyByteBuf buffer)
	{
	}
	
	public static EditmodeTeleportPacket decode(FriendlyByteBuf buffer)
	{
		return new EditmodeTeleportPacket();
	}
	
	@Override
	public void execute(ServerPlayer player)
	{
		EditData editData = MSExtraData.get(player.serverLevel()).findEditData(data -> data.getEditor() == player);
		
		if(editData == null)
			return;
		
		EditmodeLocations locations = editData.getConnection().getClientEditmodeLocations();
		
		if(locations == null)
			return;
		
		BlockPos teleportPos = locations.getClosestPosInDimension(player);
		
		if(teleportPos != null)
		{
			player.teleportTo(teleportPos.getX() + 0.5D, teleportPos.getY() + 1.0D, teleportPos.getZ() + 0.5D);
		}
	}
}