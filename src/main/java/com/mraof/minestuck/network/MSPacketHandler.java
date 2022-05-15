package com.mraof.minestuck.network;

import com.mraof.minestuck.Minestuck;
import com.mraof.minestuck.network.computer.*;
import com.mraof.minestuck.network.data.*;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.network.NetworkEvent;
import net.minecraftforge.fml.network.NetworkRegistry;
import net.minecraftforge.fml.network.PacketDistributor;
import net.minecraftforge.fml.network.simple.SimpleChannel;

import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.function.Supplier;

public class MSPacketHandler
{
	private static final String PROTOCOL_VERSION = "1";
	public static final SimpleChannel INSTANCE = NetworkRegistry.newSimpleChannel(new ResourceLocation(Minestuck.MOD_ID, "main"),
			() -> PROTOCOL_VERSION, PROTOCOL_VERSION::equals, PROTOCOL_VERSION::equals);
	
	public static void setupChannel()
	{
		nextIndex = 0;
		
		registerMessage(DataCheckerPermissionPacket.class, DataCheckerPermissionPacket::decode);
		registerMessage(EcheladderDataPacket.class, EcheladderDataPacket::decode);
		registerMessage(ColorDataPacket.class, ColorDataPacket::decode);
		registerMessage(ModusDataPacket.class, ModusDataPacket::decode);
		registerMessage(BoondollarDataPacket.class, BoondollarDataPacket::decode);
		registerMessage(ConsortReputationDataPacket.class, ConsortReputationDataPacket::decode);
		registerMessage(GristCachePacket.class, GristCachePacket::decode);
		registerMessage(TitleDataPacket.class, TitleDataPacket::decode);
		registerMessage(LandTypesDataPacket.class, LandTypesDataPacket::decode);
		
		registerMessage(CaptchaDeckPacket.class, CaptchaDeckPacket::decode);
		registerMessage(ColorSelectPacket.class, ColorSelectPacket::decode);
		registerMessage(TitleSelectPacket.class, TitleSelectPacket::decode);
		registerMessage(ConnectToSburbServerPacket.class, ConnectToSburbServerPacket::decode);
		registerMessage(OpenSburbServerPacket.class, OpenSburbServerPacket::decode);
		registerMessage(ResumeSburbConnectionPacket.class, ResumeSburbConnectionPacket::decode);
		registerMessage(CloseSburbConnectionPacket.class, CloseSburbConnectionPacket::decode);
		registerMessage(CloseRemoteSburbConnectionPacket.class, CloseRemoteSburbConnectionPacket::decode);
		registerMessage(ClearMessagePacket.class, ClearMessagePacket::decode);
		registerMessage(SkaianetInfoPacket.class, SkaianetInfoPacket::decode);
		registerMessage(DataCheckerPacket.class, DataCheckerPacket::decode);
		registerMessage(ClientEditPacket.class, ClientEditPacket::decode);
		registerMessage(ServerEditPacket.class, ServerEditPacket::decode);
		registerMessage(MiscContainerPacket.class, MiscContainerPacket::decode);
		registerMessage(EditmodeInventoryPacket.class, EditmodeInventoryPacket::decode);
		registerMessage(GoButtonPacket.class, GoButtonPacket::decode);
		registerMessage(AlchemiterPacket.class, AlchemiterPacket::decode);
		registerMessage(GristWildcardPacket.class, GristWildcardPacket::decode);
		registerMessage(TransportalizerPacket.class, TransportalizerPacket::decode);
		registerMessage(AreaEffectPacket.class, AreaEffectPacket::decode);
		registerMessage(WirelessRedstoneTransmitterPacket.class, WirelessRedstoneTransmitterPacket::decode);
		registerMessage(StatStorerPacket.class, StatStorerPacket::decode);
		registerMessage(RemoteObserverPacket.class, RemoteObserverPacket::decode);
		registerMessage(EffectTogglePacket.class, EffectTogglePacket::decode);
		registerMessage(StoneTabletPacket.class, StoneTabletPacket::decode);
		registerMessage(MagicEffectPacket.class, MagicEffectPacket::decode);
		registerMessage(LotusFlowerPacket.class, LotusFlowerPacket::decode);
		registerMessage(StopCreativeShockEffectPacket.class, StopCreativeShockEffectPacket::decode);
	}
	
	private static int nextIndex;
	private static <MSG extends StandardPacket> void registerMessage(Class<MSG> messageType, Function<PacketBuffer, MSG> decoder)
	{
		registerMessage(messageType, StandardPacket::encode, decoder, StandardPacket::consume);
	}
	
	private static <MSG> void registerMessage(Class<MSG> messageType, BiConsumer<MSG, PacketBuffer> encoder, Function<PacketBuffer, MSG> decoder, BiConsumer<MSG, Supplier<NetworkEvent.Context>> messageConsumer)
	{
		INSTANCE.registerMessage(nextIndex++, messageType, encoder, decoder, messageConsumer);
	}
	
	public static <MSG> void sendToPlayer(MSG message, ServerPlayerEntity player)
	{
		INSTANCE.send(PacketDistributor.PLAYER.with(() -> player), message);
	}
	
	public static <MSG> void sendToAll(MSG message)
	{
		INSTANCE.send(PacketDistributor.ALL.noArg(), message);
	}
	
	public static <MSG> void sendToNear(MSG message, PacketDistributor.TargetPoint point)
	{
		INSTANCE.send(PacketDistributor.NEAR.with(() -> point), message);
	}
	
	public static <MSG> void sendToServer(MSG message)
	{
		INSTANCE.sendToServer(message);
	}
	
	public static <MSG> void sendToTracking(MSG message, Entity entity)
	{
		INSTANCE.send(PacketDistributor.TRACKING_ENTITY.with(() -> entity), message);
	}
}