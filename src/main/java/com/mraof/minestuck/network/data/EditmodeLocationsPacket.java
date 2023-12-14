package com.mraof.minestuck.network.data;

import com.mraof.minestuck.computer.editmode.ClientEditHandler;
import com.mraof.minestuck.computer.editmode.EditmodeLocations;
import com.mraof.minestuck.network.PlayToClientPacket;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.FriendlyByteBuf;

import java.util.Objects;

public record EditmodeLocationsPacket(EditmodeLocations locations) implements PlayToClientPacket
{
	@Override
	public void encode(FriendlyByteBuf buffer)
	{
		buffer.writeNbt(this.locations.write());
	}
	
	public static EditmodeLocationsPacket decode(FriendlyByteBuf buffer)
	{
		CompoundTag tag = Objects.requireNonNull(buffer.readNbt());
		EditmodeLocations locations = EditmodeLocations.read(tag);
		return new EditmodeLocationsPacket(locations);
	}
	
	@Override
	public void execute()
	{
		ClientEditHandler.locations = this.locations;
	}
}
