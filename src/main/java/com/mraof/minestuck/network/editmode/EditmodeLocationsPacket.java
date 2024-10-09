package com.mraof.minestuck.network.editmode;

import com.mraof.minestuck.Minestuck;
import com.mraof.minestuck.computer.editmode.ClientEditmodeData;
import com.mraof.minestuck.computer.editmode.EditData;
import com.mraof.minestuck.computer.editmode.EditmodeLocations;
import com.mraof.minestuck.network.MSPacket;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.network.PacketDistributor;
import net.neoforged.neoforge.network.handling.IPayloadContext;

import javax.annotation.Nullable;
import java.util.Objects;
import java.util.Optional;

public record EditmodeLocationsPacket(@Nullable ResourceKey<Level> land, EditmodeLocations locations) implements MSPacket.PlayToClient
{
	
		public static final Type<EditmodeLocationsPacket> ID = new Type<>(Minestuck.id("editmode_locations"));
	public static final StreamCodec<RegistryFriendlyByteBuf, EditmodeLocationsPacket> STREAM_CODEC = StreamCodec.composite(
			ResourceKey.streamCodec(Registries.DIMENSION),
			EditmodeLocationsPacket::land,
			EditmodeLocations.STREAM_CODEC,
			EditmodeLocationsPacket::locations,
			EditmodeLocationsPacket::new
	);
	
	public static void send(EditData data)
	{
		PacketDistributor.sendToPlayer(data.getEditor(), new EditmodeLocationsPacket(data.sburbData().getLandDimensionIfEntered(), data.locations()));
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
