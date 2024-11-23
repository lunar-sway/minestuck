package com.mraof.minestuck.network.editmode;

import com.mraof.minestuck.Minestuck;
import com.mraof.minestuck.computer.editmode.ClientEditmodeData;
import com.mraof.minestuck.computer.editmode.EditData;
import com.mraof.minestuck.computer.editmode.EditmodeLocations;
import com.mraof.minestuck.network.MSPacket;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.network.PacketDistributor;
import net.neoforged.neoforge.network.handling.IPayloadContext;

import java.util.Optional;

public record EditmodeLocationsPacket(Optional<ResourceKey<Level>> land, EditmodeLocations locations) implements MSPacket.PlayToClient
{
	public static final Type<EditmodeLocationsPacket> ID = new Type<>(Minestuck.id("editmode_locations"));
	public static final StreamCodec<RegistryFriendlyByteBuf, EditmodeLocationsPacket> STREAM_CODEC = StreamCodec.composite(
			ByteBufCodecs.optional(ResourceKey.streamCodec(Registries.DIMENSION)),
			EditmodeLocationsPacket::land,
			EditmodeLocations.STREAM_CODEC,
			EditmodeLocationsPacket::locations,
			EditmodeLocationsPacket::new
	);
	
	public static void send(EditData data)
	{
		PacketDistributor.sendToPlayer(data.getEditor(), new EditmodeLocationsPacket(Optional.ofNullable(data.sburbData().getLandDimensionIfEntered()), data.locations()));
	}
	
	@Override
	public Type<? extends CustomPacketPayload> type()
	{
		return ID;
	}
	
	@Override
	public void execute(IPayloadContext context)
	{
		ClientEditmodeData.onLocationsPacket(this);
	}
}
