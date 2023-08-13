package com.mraof.minestuck.network;

import com.google.common.collect.Multimap;
import com.mojang.datafixers.util.Pair;
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
		//TODO "player" refers to the player in editmode but the UUID needs to be that of their client
		EditmodeLocations editmodeLocations = MSExtraData.get(player.level()).getEditmodeLocations(player.getUUID());
		Multimap<ResourceKey<Level>, Pair<BlockPos, EditmodeLocations.Source>> locations = EditmodeLocations.addTestingLocations(player.level().dimension(), editmodeLocations.getLocations());
		
		BlockPos teleportPos = EditmodeLocations.getClosestPosInDimension(locations, player);
		
		if(teleportPos != null)
		{
			player.teleportTo(teleportPos.getX(), teleportPos.getY(), teleportPos.getZ());
		}
	}
}