package com.mraof.minestuck.network;

import com.mraof.minestuck.Minestuck;
import com.mraof.minestuck.client.gui.MSScreenFactories;
import com.mraof.minestuck.skaianet.LandSelectionHook;
import com.mraof.minestuck.world.lands.terrain.TerrainLandType;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.neoforge.network.handling.IPayloadContext;

import java.util.List;
import java.util.Optional;

public final class LandSelectPackets
{
	public record OpenScreen(List<TerrainLandType> terrainTypes) implements MSPacket.PlayToClient
	{
		
		public static final Type<OpenScreen> ID = new Type<>(Minestuck.id("land_select/open_screen"));
		public static final StreamCodec<RegistryFriendlyByteBuf, OpenScreen> STREAM_CODEC = TerrainLandType.STREAM_CODEC.apply(ByteBufCodecs.list()).map(OpenScreen::new, OpenScreen::terrainTypes);
		
		@Override
		public Type<? extends CustomPacketPayload> type()
		{
			return ID;
		}
		
		@Override
		public void execute(IPayloadContext context)
		{
			MSScreenFactories.displayLandSelectScreen(this.terrainTypes);
		}
	}
	
	public record PickLand(Optional<TerrainLandType> terrainType) implements MSPacket.PlayToServer
	{
		
		public static final Type<PickLand> ID = new Type<>(Minestuck.id("land_select/pick"));
		public static final StreamCodec<RegistryFriendlyByteBuf, PickLand> STREAM_CODEC = ByteBufCodecs.optional(TerrainLandType.STREAM_CODEC).map(PickLand::new, PickLand::terrainType);
		
		public static PickLand random()
		{
			return new PickLand(Optional.empty());
		}
		
		public static PickLand pick(TerrainLandType terrainType)
		{
			return new PickLand(Optional.of(terrainType));
		}
		
		@Override
		public Type<? extends CustomPacketPayload> type()
		{
			return ID;
		}
		
		@Override
		public void execute(IPayloadContext context, ServerPlayer player)
		{
			LandSelectionHook.handleLandSelection(player, this.terrainType.orElse(null));
		}
	}
}
