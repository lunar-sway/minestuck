package com.mraof.minestuck.network.data;

import com.mraof.minestuck.computer.editmode.ClientEditHandler;
import com.mraof.minestuck.computer.editmode.EditmodeLocations;
import com.mraof.minestuck.network.PlayToClientPacket;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.Level;

import javax.annotation.Nullable;
import java.util.Objects;
import java.util.Optional;

public record EditmodeLocationsPacket(@Nullable ResourceKey<Level> land, EditmodeLocations locations) implements PlayToClientPacket
{
	@Override
	public void encode(FriendlyByteBuf buffer)
	{
		buffer.writeOptional(Optional.ofNullable(this.land), FriendlyByteBuf::writeResourceKey);
		
		buffer.writeNbt(this.locations.write());
	}
	
	public static EditmodeLocationsPacket decode(FriendlyByteBuf buffer)
	{
		ResourceKey<Level> land = buffer.readOptional(buffer_ -> buffer_.readResourceKey(Registries.DIMENSION)).orElse(null);
		
		CompoundTag tag = Objects.requireNonNull(buffer.readNbt());
		EditmodeLocations locations = EditmodeLocations.read(tag);
		return new EditmodeLocationsPacket(land, locations);
	}
	
	@Override
	public void execute()
	{
		ClientEditHandler.onLocationsPacket(this);
	}
}
