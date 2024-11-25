package com.mraof.minestuck.network;

import com.google.common.collect.ImmutableMap;
import com.mraof.minestuck.Minestuck;
import com.mraof.minestuck.client.ClientDimensionData;
import com.mraof.minestuck.world.lands.LandTypePair;
import com.mraof.minestuck.world.lands.LandTypes;
import com.mraof.minestuck.world.lands.terrain.TerrainLandType;
import com.mraof.minestuck.world.lands.title.TitleLandType;
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.network.handling.IPayloadContext;

import java.util.HashMap;
import java.util.Map;

public record LandTypesDataPacket(Map<ResourceKey<Level>, LandTypePair> types) implements MSPacket.PlayToClient
{
	public static final Type<LandTypesDataPacket> ID = new Type<>(Minestuck.id("land_types_data"));
	private static final StreamCodec<RegistryFriendlyByteBuf, Map<ResourceKey<Level>, LandTypePair>> MAP_STREAM_CODEC = ByteBufCodecs.map(HashMap::new, ResourceKey.streamCodec(Registries.DIMENSION), LandTypePair.STREAM_CODEC);
	public static final StreamCodec<RegistryFriendlyByteBuf, LandTypesDataPacket> STREAM_CODEC = MAP_STREAM_CODEC.map(LandTypesDataPacket::new, LandTypesDataPacket::types);
	
	@Override
	public Type<? extends CustomPacketPayload> type()
	{
		return ID;
	}
	
	@Override
	public void execute(IPayloadContext context)
	{
		ClientDimensionData.receivePacket(this);
	}
}
