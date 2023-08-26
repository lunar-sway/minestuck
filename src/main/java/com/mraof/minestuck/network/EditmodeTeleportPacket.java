package com.mraof.minestuck.network;

import com.mraof.minestuck.computer.editmode.EditData;
import com.mraof.minestuck.computer.editmode.EditmodeLocations;
import com.mraof.minestuck.world.storage.MSExtraData;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.Level;

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
		
		if(editData != null)
		{
			EditmodeLocations locations = editData.getConnection().getClientEditmodeLocations();
			
			if(locations != null)
			{
				BlockPos teleportPos = EditmodeLocations.getClosestPosInDimension(locations.getLocations(), player);
				
				if(teleportPos != null)
				{
					player.teleportTo(teleportPos.getX(), teleportPos.getY(), teleportPos.getZ());
				}
			}
		}
	}
}