package com.mraof.minestuck.network;

import com.mojang.datafixers.util.Pair;
import com.mraof.minestuck.computer.editmode.EditData;
import com.mraof.minestuck.computer.editmode.EditmodeLocations;
import com.mraof.minestuck.world.storage.MSExtraData;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;

public class EditmodeTeleportPacket implements PlayToServerPacket
{
	private final BlockPos pos;
	
	public EditmodeTeleportPacket(BlockPos pos)
	{
		this.pos = pos;
	}
	
	@Override
	public void encode(FriendlyByteBuf buffer)
	{
		buffer.writeBlockPos(pos);
	}
	
	public static EditmodeTeleportPacket decode(FriendlyByteBuf buffer)
	{
		BlockPos pos = buffer.readBlockPos();
		return new EditmodeTeleportPacket(pos);
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
		//locations.getLocations().get(player.level().dimension()).stream().findFirst().get().getFirst()
		//TODO requires the dimensions to be the same, will need to be changed if the player will be allowed to teleport to other dimensions
		for(Pair<BlockPos, EditmodeLocations.Source> valuePair : locations.getLocations().get(player.level().dimension()).stream().toList())
		{
			if(valuePair.getFirst().equals(pos))
				player.teleportTo(pos.getX() + 0.5D, pos.getY() + 1.0D, pos.getZ() + 0.5D);
		}
		
	}
}