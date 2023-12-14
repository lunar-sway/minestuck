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
		CompoundTag tag = new CompoundTag();
		tag.put("editmode_locations", this.locations.write());
		
		buffer.writeNbt(tag);
	}
	
	public static EditmodeLocationsPacket decode(FriendlyByteBuf buffer)
	{
		CompoundTag tag = Objects.requireNonNull(buffer.readNbt());
		EditmodeLocations locations = EditmodeLocations.read(tag.getList("editmode_locations", Tag.TAG_COMPOUND));
		return new EditmodeLocationsPacket(locations);
	}
	
	@Override
	public void execute()
	{
		ClientEditHandler.locations = this.locations;
	}
}
