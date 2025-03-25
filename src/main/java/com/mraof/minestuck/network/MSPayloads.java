package com.mraof.minestuck.network;

import com.mraof.minestuck.Minestuck;
import com.mraof.minestuck.network.block.*;
import com.mraof.minestuck.network.computer.*;
import com.mraof.minestuck.network.editmode.*;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.phys.Vec3;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;

@EventBusSubscriber(modid = Minestuck.MOD_ID, bus = EventBusSubscriber.Bus.MOD)
public final class MSPayloads
{
	public static final StreamCodec<FriendlyByteBuf, Vec3> VEC3_STREAM_CODEC = StreamCodec.composite(
			ByteBufCodecs.DOUBLE,
			Vec3::x,
			ByteBufCodecs.DOUBLE,
			Vec3::y,
			ByteBufCodecs.DOUBLE,
			Vec3::z,
			Vec3::new
	);
	
	@SubscribeEvent
	private static void register(RegisterPayloadHandlersEvent event)
	{
		PayloadRegistrar registrar = event.registrar(Minestuck.MOD_ID).versioned("2");
		
		//Player Data Packets
		registerPlayToClient(registrar, EcheladderDataPacket.ID, EcheladderDataPacket.STREAM_CODEC);
		registerPlayToClient(registrar, BoondollarDataPacket.ID, BoondollarDataPacket.STREAM_CODEC);
		registerPlayToClient(registrar, GristCachePacket.ID, GristCachePacket.STREAM_CODEC);
		registerPlayToClient(registrar, EditmodeCacheLimitPacket.ID, EditmodeCacheLimitPacket.STREAM_CODEC);
		registerPlayToClient(registrar, TitleDataPacket.ID, TitleDataPacket.STREAM_CODEC);
		registerPlayToClient(registrar, LandTypesDataPacket.ID, LandTypesDataPacket.STREAM_CODEC);
		
		//Color Selector Packets
		registerPlayToClient(registrar, PlayerColorPackets.OpenSelection.ID,  PlayerColorPackets.OpenSelection.STREAM_CODEC);
		registerPlayToClient(registrar, PlayerColorPackets.Data.ID,  PlayerColorPackets.Data.STREAM_CODEC);
		registerPlayToServer(registrar, PlayerColorPackets.SelectIndex.ID,  PlayerColorPackets.SelectIndex.STREAM_CODEC);
		registerPlayToServer(registrar, PlayerColorPackets.SelectRGB.ID,  PlayerColorPackets.SelectRGB.STREAM_CODEC);
		
		//Captchalogue Deck Packets
		registerPlayToClient(registrar, CaptchaDeckPackets.ModusData.ID, CaptchaDeckPackets.ModusData.STREAM_CODEC);
		registerPlayToServer(registrar, CaptchaDeckPackets.TriggerModusButton.ID, CaptchaDeckPackets.TriggerModusButton.STREAM_CODEC);
		registerPlayToServer(registrar, CaptchaDeckPackets.CaptchalogueHeldItem.ID, CaptchaDeckPackets.CaptchalogueHeldItem.STREAM_CODEC);
		registerPlayToServer(registrar, CaptchaDeckPackets.CaptchalogueInventorySlot.ID, CaptchaDeckPackets.CaptchalogueInventorySlot.STREAM_CODEC);
		registerPlayToServer(registrar, CaptchaDeckPackets.GetItem.ID, CaptchaDeckPackets.GetItem.STREAM_CODEC);
		registerPlayToServer(registrar, CaptchaDeckPackets.SetModusParameter.ID, CaptchaDeckPackets.SetModusParameter.STREAM_CODEC);
		
		//Classpect Selection Screen Packets
		registerPlayToClient(registrar, TitleSelectPackets.OpenScreen.ID, TitleSelectPackets.OpenScreen.STREAM_CODEC);
		registerPlayToServer(registrar, TitleSelectPackets.PickTitle.ID, TitleSelectPackets.PickTitle.STREAM_CODEC);
		
		//Dialogue Packets
		registerPlayToClient(registrar, DialoguePackets.OpenScreen.ID, DialoguePackets.OpenScreen.STREAM_CODEC);
		registerPlayToClient(registrar, DialoguePackets.CloseScreen.ID, DialoguePackets.CloseScreen.STREAM_CODEC);
		registerPlayToServer(registrar, DialoguePackets.OnCloseScreen.ID, DialoguePackets.OnCloseScreen.STREAM_CODEC);
		registerPlayToServer(registrar, DialoguePackets.TriggerResponse.ID, DialoguePackets.TriggerResponse.STREAM_CODEC);
		
		//SBURB Connection Packets
		registerPlayToServer(registrar, ConnectToSburbServerPacket.ID, ConnectToSburbServerPacket.STREAM_CODEC);
		registerPlayToServer(registrar, OpenSburbServerPacket.ID, OpenSburbServerPacket.STREAM_CODEC);
		registerPlayToServer(registrar, ResumeSburbConnectionPackets.AsClient.ID, ResumeSburbConnectionPackets.AsClient.STREAM_CODEC);
		registerPlayToServer(registrar, ResumeSburbConnectionPackets.AsServer.ID, ResumeSburbConnectionPackets.AsServer.STREAM_CODEC);
		registerPlayToServer(registrar, CloseSburbConnectionPackets.AsClient.ID, CloseSburbConnectionPackets.AsClient.STREAM_CODEC);
		registerPlayToServer(registrar, CloseSburbConnectionPackets.AsServer.ID, CloseSburbConnectionPackets.AsServer.STREAM_CODEC);
		registerPlayToServer(registrar, CloseRemoteSburbConnectionPacket.ID, CloseRemoteSburbConnectionPacket.STREAM_CODEC);
		registerPlayToServer(registrar, ClearMessagePacket.ID, ClearMessagePacket.STREAM_CODEC);
		
		//Skaianet Info Packets
		registerPlayToClient(registrar, SkaianetInfoPackets.Data.ID, SkaianetInfoPackets.Data.STREAM_CODEC);
		registerPlayToClient(registrar, SkaianetInfoPackets.HasEntered.ID, SkaianetInfoPackets.HasEntered.STREAM_CODEC);
		registerPlayToClient(registrar, SkaianetInfoPackets.LandChains.ID, SkaianetInfoPackets.LandChains.STREAM_CODEC);
		registerPlayToServer(registrar, SkaianetInfoPackets.Request.ID, SkaianetInfoPackets.Request.STREAM_CODEC);
		
		//Base Computer Functionality Packets
		registerPlayToServer(registrar, BurnDiskPacket.ID, BurnDiskPacket.STREAM_CODEC);
		registerPlayToServer(registrar, ThemeSelectPacket.ID, ThemeSelectPacket.STREAM_CODEC);
		registerPlayToServer(registrar, EjectDiskPacket.ID, EjectDiskPacket.STREAM_CODEC);
		
		//Data Checker Packets
		registerPlayToServer(registrar, DataCheckerPackets.Request.ID, DataCheckerPackets.Request.STREAM_CODEC);
		registerPlayToClient(registrar, DataCheckerPackets.Data.ID, DataCheckerPackets.Data.STREAM_CODEC);
		registerPlayToClient(registrar, DataCheckerPackets.Permission.ID, DataCheckerPackets.Permission.STREAM_CODEC);
		
		//Edit Mode Packets
		registerPlayToServer(registrar, ClientEditPackets.Activate.ID, ClientEditPackets.Activate.STREAM_CODEC);
		registerPlayToServer(registrar, ClientEditPackets.Exit.ID, ClientEditPackets.Exit.STREAM_CODEC);
		registerPlayToClient(registrar, ServerEditPackets.Activate.ID, ServerEditPackets.Activate.STREAM_CODEC);
		registerPlayToClient(registrar, ServerEditPackets.UpdateDeployList.ID, ServerEditPackets.UpdateDeployList.STREAM_CODEC);
		registerPlayToClient(registrar, ServerEditPackets.Exit.ID, ServerEditPackets.Exit.STREAM_CODEC);
		
		registerPlayToClient(registrar, EditmodeLocationsPacket.ID, EditmodeLocationsPacket.STREAM_CODEC);
		registerPlayToClient(registrar, EditmodeInventoryPackets.Update.ID, EditmodeInventoryPackets.Update.STREAM_CODEC);
		registerPlayToServer(registrar, EditmodeInventoryPackets.Scroll.ID, EditmodeInventoryPackets.Scroll.STREAM_CODEC);
		registerPlayToServer(registrar, EditmodeTeleportPacket.ID, EditmodeTeleportPacket.STREAM_CODEC);
		
		//Edit Mode Drag and Drop Packets
		registerPlayToServer(registrar, EditmodeDragPackets.Fill.ID, EditmodeDragPackets.Fill.STREAM_CODEC);
		registerPlayToServer(registrar, EditmodeDragPackets.Destroy.ID, EditmodeDragPackets.Destroy.STREAM_CODEC);
		registerPlayToServer(registrar, EditmodeDragPackets.Cursor.ID, EditmodeDragPackets.Cursor.STREAM_CODEC);
		registerPlayToServer(registrar, EditmodeDragPackets.Reset.ID, EditmodeDragPackets.Reset.STREAM_CODEC);
		
		//Edit Mode Atheneum Packets
		registerPlayToServer(registrar, AtheneumPackets.Scroll.ID, AtheneumPackets.Scroll.STREAM_CODEC);
		registerPlayToClient(registrar, AtheneumPackets.Update.ID, AtheneumPackets.Update.STREAM_CODEC);
		
		//Alchemy Machines Packets
		registerPlayToServer(registrar, MachinePackets.SetRunning.ID, MachinePackets.SetRunning.STREAM_CODEC);
		registerPlayToServer(registrar, MachinePackets.SetLooping.ID, MachinePackets.SetLooping.STREAM_CODEC);
		
		registerPlayToServer(registrar, TriggerAlchemiterPacket.ID, TriggerAlchemiterPacket.STREAM_CODEC);
		registerPlayToServer(registrar, SetWildcardGristPacket.ID, SetWildcardGristPacket.STREAM_CODEC);
		registerPlayToServer(registrar, TriggerPunchDesignixPacket.ID, TriggerPunchDesignixPacket.STREAM_CODEC);
		
		//Transportalizer Packets
		registerPlayToServer(registrar, TransportalizerPackets.SetId.ID, TransportalizerPackets.SetId.STREAM_CODEC);
		registerPlayToServer(registrar, TransportalizerPackets.SetDestId.ID, TransportalizerPackets.SetDestId.STREAM_CODEC);
		
		//Dungeon Blocks Packets
		registerPlayToServer(registrar, AreaEffectSettingsPacket.ID, AreaEffectSettingsPacket.STREAM_CODEC);
		registerPlayToServer(registrar, WirelessRedstoneTransmitterSettingsPacket.ID, WirelessRedstoneTransmitterSettingsPacket.STREAM_CODEC);
		registerPlayToServer(registrar, StatStorerSettingsPacket.ID, StatStorerSettingsPacket.STREAM_CODEC);
		registerPlayToServer(registrar, RemoteObserverSettingsPacket.ID, RemoteObserverSettingsPacket.STREAM_CODEC);
		registerPlayToServer(registrar, SummonerSettingsPacket.ID, SummonerSettingsPacket.STREAM_CODEC);
		registerPlayToServer(registrar, StructureCoreSettingsPacket.ID, StructureCoreSettingsPacket.STREAM_CODEC);
		registerPlayToServer(registrar, BlockTeleporterSettingsPacket.ID, BlockTeleporterSettingsPacket.STREAM_CODEC);
		
		//Miscellaneous Machines Packets
		registerPlayToServer(registrar, TriggerAnthvilPacket.ID, TriggerAnthvilPacket.STREAM_CODEC);
		registerPlayToServer(registrar, SetSendificatorDestinationPacket.ID, SetSendificatorDestinationPacket.STREAM_CODEC);
		
		//Item Packets
		registerPlayToServer(registrar, CarveStoneTabletPacket.ID, CarveStoneTabletPacket.STREAM_CODEC);
		registerPlayToClient(registrar, MusicPlayerPacket.ID, MusicPlayerPacket.STREAM_CODEC);
		
		//Animation and Effect Packets
		registerPlayToClient(registrar, MagicRangedEffectPacket.ID, MagicRangedEffectPacket.STREAM_CODEC);
		registerPlayToClient(registrar, MagicAOEEffectPacket.ID, MagicAOEEffectPacket.STREAM_CODEC);
		registerPlayToClient(registrar, LotusFlowerAnimationPacket.ID, LotusFlowerAnimationPacket.STREAM_CODEC);
		registerPlayToClient(registrar, ServerCursorAnimationPacket.ID, ServerCursorAnimationPacket.STREAM_CODEC);
		registerPlayToClient(registrar, GristRejectAnimationPacket.ID, GristRejectAnimationPacket.STREAM_CODEC);
		registerPlayToClient(registrar, GristToastPacket.ID, GristToastPacket.STREAM_CODEC);
		registerPlayToClient(registrar, EntryEffectPackets.Effect.ID, EntryEffectPackets.Effect.STREAM_CODEC);
		registerPlayToClient(registrar, EntryEffectPackets.Clear.ID, EntryEffectPackets.Clear.STREAM_CODEC);
		
		//Miscellaneous Packets
		registerPlayToServer(registrar, ToggleAspectEffectsPacket.ID, ToggleAspectEffectsPacket.STREAM_CODEC);
		registerPlayToServer(registrar, MiscContainerPacket.ID, MiscContainerPacket.STREAM_CODEC);
		registerPlayToClient(registrar, StopCreativeShockEffectPacket.ID, StopCreativeShockEffectPacket.STREAM_CODEC);
		registerPlayToClient(registrar, PushPlayerPacket.ID, PushPlayerPacket.STREAM_CODEC);
	}
	
	private static <T extends MSPacket.PlayToServer> void registerPlayToServer(PayloadRegistrar registrar, CustomPacketPayload.Type<T> type, StreamCodec<? super RegistryFriendlyByteBuf, T> codec)
	{
		registrar.playToServer(type, codec, (t, context) -> t.execute(context, (ServerPlayer) context.player()));
	}
	
	private static <T extends MSPacket.PlayToClient> void registerPlayToClient(PayloadRegistrar registrar, CustomPacketPayload.Type<T> type, StreamCodec<? super RegistryFriendlyByteBuf, T> codec)
	{
		registrar.playToClient(type, codec, MSPacket.PlayToClient::execute);
	}
}
