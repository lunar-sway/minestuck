package com.mraof.minestuck.network.editmode;

import com.mraof.minestuck.Minestuck;
import com.mraof.minestuck.computer.editmode.ClientEditmodeData;
import com.mraof.minestuck.computer.editmode.EditData;
import com.mraof.minestuck.computer.editmode.EditmodeLocations;
import com.mraof.minestuck.network.MSPacket;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.network.PacketDistributor;

import javax.annotation.Nullable;
import java.util.Objects;
import java.util.Optional;

public record EditmodeLocationsPacket(@Nullable ResourceKey<Level> land, EditmodeLocations locations) implements MSPacket.PlayToClient
{
	public static final ResourceLocation ID = Minestuck.id("editmode_locations");
	
	public static void send(EditData data)
	{
		PacketDistributor.PLAYER.with(data.getEditor())
				.send(new EditmodeLocationsPacket(data.sburbData().getLandDimensionIfEntered(), data.locations()));
	}
	
	@Override
	public ResourceLocation id()
	{
		return ID;
	}
	
	@Override
	public void write(FriendlyByteBuf buffer)
	{
		buffer.writeOptional(Optional.ofNullable(this.land), FriendlyByteBuf::writeResourceKey);
		
		buffer.writeNbt(this.locations.serializeNBT());
	}
	
	public static EditmodeLocationsPacket read(FriendlyByteBuf buffer)
	{
		ResourceKey<Level> land = buffer.readOptional(buffer_ -> buffer_.readResourceKey(Registries.DIMENSION)).orElse(null);
		
		CompoundTag tag = Objects.requireNonNull(buffer.readNbt());
		EditmodeLocations locations = EditmodeLocations.read(tag);
		return new EditmodeLocationsPacket(land, locations);
	}
	
	@Override
	public void execute()
	{
		ClientEditmodeData.onLocationsPacket(this);
	}
}
