package com.mraof.minestuck.network;

import com.mraof.minestuck.Minestuck;
import com.mraof.minestuck.network.computer.*;
import com.mraof.minestuck.network.data.*;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraftforge.network.NetworkEvent;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.PacketDistributor;
import net.minecraftforge.network.simple.SimpleChannel;

import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.function.Supplier;

public class MSPacketHandler
{
	private static final String PROTOCOL_VERSION = "2";
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
		registerMessage(EditmodeCacheLimitPacket.class, EditmodeCacheLimitPacket::decode);
		registerMessage(TitleDataPacket.class, TitleDataPacket::decode);
		registerMessage(LandTypesDataPacket.class, LandTypesDataPacket::decode);
		
		registerMessage(CaptchaDeckPacket.class, CaptchaDeckPacket::decode);
		registerMessage(ColorSelectPacket.class, ColorSelectPacket::decode);
		registerMessage(RGBColorSelectPacket.class, RGBColorSelectPacket::decode);
		registerMessage(TitleSelectPacket.class, TitleSelectPacket::decode);
		registerMessage(ConnectToSburbServerPacket.class, ConnectToSburbServerPacket::decode);
		registerMessage(OpenSburbServerPacket.class, OpenSburbServerPacket::decode);
		registerMessage(ResumeSburbConnectionPacket.class, ResumeSburbConnectionPacket::decode);
		registerMessage(CloseSburbConnectionPacket.class, CloseSburbConnectionPacket::decode);
		registerMessage(CloseRemoteSburbConnectionPacket.class, CloseRemoteSburbConnectionPacket::decode);
		registerMessage(ClearMessagePacket.class, ClearMessagePacket::decode);
		registerMessage(SkaianetInfoPacket.class, SkaianetInfoPacket::decode);
		registerMessage(BurnDiskPacket.class, BurnDiskPacket::decode);
		registerMessage(ThemeSelectPacket.class, ThemeSelectPacket::decode);
		registerMessage(DataCheckerPacket.class, DataCheckerPacket::decode);
		registerMessage(ClientEditPacket.class, ClientEditPacket::decode);
		registerMessage(ServerEditPacket.class, ServerEditPacket::decode);
		registerMessage(EditmodeLocationsPacket.class, EditmodeLocationsPacket::decode);
		registerMessage(MiscContainerPacket.class, MiscContainerPacket::decode);
		registerMessage(EditmodeDragPacket.Fill.class, EditmodeDragPacket.Fill::decode);
		registerMessage(EditmodeDragPacket.Destroy.class, EditmodeDragPacket.Destroy::decode);
		registerMessage(EditmodeDragPacket.Cursor.class, EditmodeDragPacket.Cursor::decode);
		registerMessage(EditmodeDragPacket.Reset.class, EditmodeDragPacket.Reset::decode);
		registerMessage(EditmodeInventoryPacket.class, EditmodeInventoryPacket::decode);
		registerMessage(EditmodeTeleportPacket.class, EditmodeTeleportPacket::decode);
		registerMessage(MachinePacket.SetRunning.class, MachinePacket.SetRunning::decode);
		registerMessage(MachinePacket.SetLooping.class, MachinePacket.SetLooping::decode);
		registerMessage(AlchemiterPacket.class, AlchemiterPacket::decode);
		registerMessage(PunchDesignixPacket.class, PunchDesignixPacket::decode);
		registerMessage(GristWildcardPacket.class, GristWildcardPacket::decode);
		registerMessage(SendificatorPacket.class, SendificatorPacket::decode);
		registerMessage(TransportalizerPacket.class, TransportalizerPacket::decode);
		registerMessage(AreaEffectPacket.class, AreaEffectPacket::decode);
		registerMessage(WirelessRedstoneTransmitterPacket.class, WirelessRedstoneTransmitterPacket::decode);
		registerMessage(StatStorerPacket.class, StatStorerPacket::decode);
		registerMessage(RemoteObserverPacket.class, RemoteObserverPacket::decode);
		registerMessage(SummonerPacket.class, SummonerPacket::decode);
		registerMessage(StructureCorePacket.class, StructureCorePacket::decode);
		registerMessage(EffectTogglePacket.class, EffectTogglePacket::decode);
		registerMessage(StoneTabletPacket.class, StoneTabletPacket::decode);
		registerMessage(AnthvilPacket.class, AnthvilPacket::decode);
		registerMessage(MagicRangedEffectPacket.class, MagicRangedEffectPacket::decode);
		registerMessage(MagicAOEEffectPacket.class, MagicAOEEffectPacket::decode);
		registerMessage(LotusFlowerPacket.class, LotusFlowerPacket::decode);
		registerMessage(ServerCursorPacket.class, ServerCursorPacket::decode);
		registerMessage(MusicPlayerPacket.class, MusicPlayerPacket::decode);
		registerMessage(GristRejectAnimationPacket.class, GristRejectAnimationPacket::decode);
		registerMessage(StopCreativeShockEffectPacket.class, StopCreativeShockEffectPacket::decode);
		registerMessage(ClientMovementPacket.class, ClientMovementPacket::decode);
		registerMessage(GristToastPacket.class, GristToastPacket::decode);
		registerMessage(AtheneumPacket.Scroll.class, AtheneumPacket.Scroll::decode);
		registerMessage(AtheneumPacket.Update.class, AtheneumPacket.Update::decode);
		registerMessage(EntryEffectPackets.Effect.class, EntryEffectPackets.Effect::decode);
		registerMessage(EntryEffectPackets.Clear.class, EntryEffectPackets.Clear::decode);
	}
	
	private static int nextIndex;
	private static <MSG extends StandardPacket> void registerMessage(Class<MSG> messageType, Function<FriendlyByteBuf, MSG> decoder)
	{
		registerMessage(messageType, StandardPacket::encode, decoder, StandardPacket::consume);
	}
	
	private static <MSG> void registerMessage(Class<MSG> messageType, BiConsumer<MSG, FriendlyByteBuf> encoder, Function<FriendlyByteBuf, MSG> decoder, BiConsumer<MSG, Supplier<NetworkEvent.Context>> messageConsumer)
	{
		INSTANCE.registerMessage(nextIndex++, messageType, encoder, decoder, messageConsumer);
	}
	
	public static <MSG> void sendToPlayer(MSG message, ServerPlayer player)
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
	
	public static <MSG> void sendToTrackingAndSelf(MSG message, Entity entity)
	{
		INSTANCE.send(PacketDistributor.TRACKING_ENTITY_AND_SELF.with(() -> entity), message);
	}
}