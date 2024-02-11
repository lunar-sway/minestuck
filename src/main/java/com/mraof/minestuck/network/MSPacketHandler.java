package com.mraof.minestuck.network;

import com.mraof.minestuck.Minestuck;
import com.mraof.minestuck.network.computer.*;
import com.mraof.minestuck.network.data.*;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.PacketDistributor;
import net.minecraftforge.network.simple.SimpleChannel;

import java.util.Optional;
import java.util.function.Function;

public class MSPacketHandler
{
	private static final String PROTOCOL_VERSION = "3";
	public static final SimpleChannel INSTANCE = NetworkRegistry.newSimpleChannel(new ResourceLocation(Minestuck.MOD_ID, "main"),
			() -> PROTOCOL_VERSION, PROTOCOL_VERSION::equals, PROTOCOL_VERSION::equals);
	
	public static void setupChannel()
	{
		nextIndex = 0;
		
		registerToClientMessage(DataCheckerPermissionPacket.class, DataCheckerPermissionPacket::decode);
		registerToClientMessage(EcheladderDataPacket.class, EcheladderDataPacket::decode);
		registerToClientMessage(ColorDataPacket.class, ColorDataPacket::decode);
		registerToClientMessage(ModusDataPacket.class, ModusDataPacket::decode);
		registerToClientMessage(BoondollarDataPacket.class, BoondollarDataPacket::decode);
		registerToClientMessage(ConsortReputationDataPacket.class, ConsortReputationDataPacket::decode);
		registerToClientMessage(GristCachePacket.class, GristCachePacket::decode);
		registerToClientMessage(EditmodeCacheLimitPacket.class, EditmodeCacheLimitPacket::decode);
		registerToClientMessage(TitleDataPacket.class, TitleDataPacket::decode);
		registerToClientMessage(LandTypesDataPacket.class, LandTypesDataPacket::decode);
		
		registerToServerMessage(CaptchaDeckPacket.class, CaptchaDeckPacket::decode);
		registerToServerMessage(ColorSelectPacket.class, ColorSelectPacket::decode);
		registerToServerMessage(RGBColorSelectPacket.class, RGBColorSelectPacket::decode);
		registerToBothMessage(TitleSelectPacket.class, TitleSelectPacket::decode);
		registerToServerMessage(DialoguePacket.class, DialoguePacket::decode);
		registerToServerMessage(ConnectToSburbServerPacket.class, ConnectToSburbServerPacket::decode);
		registerToServerMessage(OpenSburbServerPacket.class, OpenSburbServerPacket::decode);
		registerToServerMessage(ResumeSburbConnectionPacket.class, ResumeSburbConnectionPacket::decode);
		registerToServerMessage(CloseSburbConnectionPacket.class, CloseSburbConnectionPacket::decode);
		registerToServerMessage(CloseRemoteSburbConnectionPacket.class, CloseRemoteSburbConnectionPacket::decode);
		registerToServerMessage(ClearMessagePacket.class, ClearMessagePacket::decode);
		registerToBothMessage(SkaianetInfoPacket.class, SkaianetInfoPacket::decode);
		registerToServerMessage(BurnDiskPacket.class, BurnDiskPacket::decode);
		registerToServerMessage(ThemeSelectPacket.class, ThemeSelectPacket::decode);
		registerToBothMessage(DataCheckerPacket.class, DataCheckerPacket::decode);
		registerToServerMessage(ClientEditPacket.class, ClientEditPacket::decode);
		registerToClientMessage(ServerEditPacket.Activate.class, ServerEditPacket.Activate::decode);
		registerToClientMessage(ServerEditPacket.UpdateDeployList.class, ServerEditPacket.UpdateDeployList::decode);
		registerToClientMessage(ServerEditPacket.Exit.class, ServerEditPacket.Exit::decode);
		registerToClientMessage(EditmodeLocationsPacket.class, EditmodeLocationsPacket::decode);
		registerToServerMessage(MiscContainerPacket.class, MiscContainerPacket::decode);
		registerToServerMessage(EditmodeDragPacket.Fill.class, EditmodeDragPacket.Fill::decode);
		registerToServerMessage(EditmodeDragPacket.Destroy.class, EditmodeDragPacket.Destroy::decode);
		registerToServerMessage(EditmodeDragPacket.Cursor.class, EditmodeDragPacket.Cursor::decode);
		registerToServerMessage(EditmodeDragPacket.Reset.class, EditmodeDragPacket.Reset::decode);
		registerToBothMessage(EditmodeInventoryPacket.class, EditmodeInventoryPacket::decode);
		registerToServerMessage(EditmodeTeleportPacket.class, EditmodeTeleportPacket::decode);
		registerToServerMessage(MachinePacket.SetRunning.class, MachinePacket.SetRunning::decode);
		registerToServerMessage(MachinePacket.SetLooping.class, MachinePacket.SetLooping::decode);
		registerToServerMessage(AlchemiterPacket.class, AlchemiterPacket::decode);
		registerToServerMessage(PunchDesignixPacket.class, PunchDesignixPacket::decode);
		registerToServerMessage(GristWildcardPacket.class, GristWildcardPacket::decode);
		registerToServerMessage(SendificatorPacket.class, SendificatorPacket::decode);
		registerToServerMessage(TransportalizerPacket.class, TransportalizerPacket::decode);
		registerToServerMessage(AreaEffectPacket.class, AreaEffectPacket::decode);
		registerToServerMessage(WirelessRedstoneTransmitterPacket.class, WirelessRedstoneTransmitterPacket::decode);
		registerToServerMessage(StatStorerPacket.class, StatStorerPacket::decode);
		registerToServerMessage(RemoteObserverPacket.class, RemoteObserverPacket::decode);
		registerToServerMessage(SummonerPacket.class, SummonerPacket::decode);
		registerToServerMessage(StructureCorePacket.class, StructureCorePacket::decode);
		registerToServerMessage(EffectTogglePacket.class, EffectTogglePacket::decode);
		registerToServerMessage(StoneTabletPacket.class, StoneTabletPacket::decode);
		registerToServerMessage(AnthvilPacket.class, AnthvilPacket::decode);
		registerToClientMessage(MagicRangedEffectPacket.class, MagicRangedEffectPacket::decode);
		registerToClientMessage(MagicAOEEffectPacket.class, MagicAOEEffectPacket::decode);
		registerToClientMessage(LotusFlowerPacket.class, LotusFlowerPacket::decode);
		registerToClientMessage(ServerCursorPacket.class, ServerCursorPacket::decode);
		registerToClientMessage(MusicPlayerPacket.class, MusicPlayerPacket::decode);
		registerToClientMessage(GristRejectAnimationPacket.class, GristRejectAnimationPacket::decode);
		registerToClientMessage(StopCreativeShockEffectPacket.class, StopCreativeShockEffectPacket::decode);
		registerToClientMessage(ClientMovementPacket.class, ClientMovementPacket::decode);
		registerToClientMessage(GristToastPacket.class, GristToastPacket::decode);
		registerToServerMessage(AtheneumPacket.Scroll.class, AtheneumPacket.Scroll::decode);
		registerToClientMessage(AtheneumPacket.Update.class, AtheneumPacket.Update::decode);
		registerToClientMessage(EntryEffectPackets.Effect.class, EntryEffectPackets.Effect::decode);
		registerToClientMessage(EntryEffectPackets.Clear.class, EntryEffectPackets.Clear::decode);
	}
	
	private static <MSG extends MSPacket.PlayToBoth> void registerToBothMessage(Class<MSG> messageType, Function<FriendlyByteBuf, MSG> decoder)
	{
		registerMessage(messageType, decoder, Optional.empty());
	}
	
	private static <MSG extends MSPacket.PlayToClient> void registerToClientMessage(Class<MSG> messageType, Function<FriendlyByteBuf, MSG> decoder)
	{
		registerMessage(messageType, decoder, Optional.of(NetworkDirection.PLAY_TO_CLIENT));
	}
	
	private static <MSG extends MSPacket.PlayToServer> void registerToServerMessage(Class<MSG> messageType, Function<FriendlyByteBuf, MSG> decoder)
	{
		registerMessage(messageType, decoder, Optional.of(NetworkDirection.PLAY_TO_SERVER));
	}
	
	private static int nextIndex;
	
	@SuppressWarnings("OptionalUsedAsFieldOrParameterType")
	private static <MSG extends MSPacket> void registerMessage(Class<MSG> messageType, Function<FriendlyByteBuf, MSG> decoder,
															   Optional<NetworkDirection> networkDirection)
	{
		INSTANCE.registerMessage(nextIndex++, messageType, MSPacket::encode, decoder, MSPacket::consume, networkDirection);
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