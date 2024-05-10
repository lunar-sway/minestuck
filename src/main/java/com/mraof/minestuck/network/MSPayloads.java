package com.mraof.minestuck.network;

import com.mraof.minestuck.Minestuck;
import com.mraof.minestuck.network.block.*;
import com.mraof.minestuck.network.computer.*;
import com.mraof.minestuck.network.editmode.*;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlerEvent;

@Mod.EventBusSubscriber(modid = Minestuck.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public final class MSPayloads
{
	@SubscribeEvent
	private static void register(RegisterPayloadHandlerEvent event)
	{
		event.registrar(Minestuck.MOD_ID)
				.versioned("1")
				.play(EcheladderDataPacket.ID, EcheladderDataPacket::read, MSPacket.PlayToClient::handler)
				.play(BoondollarDataPacket.ID, BoondollarDataPacket::read, MSPacket.PlayToClient::handler)
				.play(GristCachePacket.ID, GristCachePacket::read, MSPacket.PlayToClient::handler)
				.play(EditmodeCacheLimitPacket.ID, EditmodeCacheLimitPacket::read, MSPacket.PlayToClient::handler)
				.play(TitleDataPacket.ID, TitleDataPacket::read, MSPacket.PlayToClient::handler)
				.play(LandTypesDataPacket.ID, LandTypesDataPacket::read, MSPacket.PlayToClient::handler)
				
				.play(PlayerColorPackets.OpenSelection.ID, PlayerColorPackets.OpenSelection::read, MSPacket.PlayToClient::handler)
				.play(PlayerColorPackets.Data.ID, PlayerColorPackets.Data::read, MSPacket.PlayToClient::handler)
				.play(PlayerColorPackets.SelectIndex.ID, PlayerColorPackets.SelectIndex::read, MSPacket.PlayToServer::handler)
				.play(PlayerColorPackets.SelectRGB.ID, PlayerColorPackets.SelectRGB::read, MSPacket.PlayToServer::handler)
				
				.play(CaptchaDeckPackets.ModusData.ID, CaptchaDeckPackets.ModusData::read, MSPacket.PlayToClient::handler)
				.play(CaptchaDeckPackets.TriggerModusButton.ID, CaptchaDeckPackets.TriggerModusButton::read, MSPacket.PlayToServer::handler)
				.play(CaptchaDeckPackets.CaptchalogueHeldItem.ID, CaptchaDeckPackets.CaptchalogueHeldItem::read, MSPacket.PlayToServer::handler)
				.play(CaptchaDeckPackets.CaptchalogueInventorySlot.ID, CaptchaDeckPackets.CaptchalogueInventorySlot::read, MSPacket.PlayToServer::handler)
				.play(CaptchaDeckPackets.GetItem.ID, CaptchaDeckPackets.GetItem::read, MSPacket.PlayToServer::handler)
				.play(CaptchaDeckPackets.SetModusParameter.ID, CaptchaDeckPackets.SetModusParameter::read, MSPacket.PlayToServer::handler)
				.play(TitleSelectPackets.OpenScreen.ID, TitleSelectPackets.OpenScreen::read, MSPacket.PlayToClient::handler)
				.play(TitleSelectPackets.PickTitle.ID, TitleSelectPackets.PickTitle::read, MSPacket.PlayToServer::handler)
				
				.play(DialoguePackets.OpenScreen.ID, DialoguePackets.OpenScreen::read, MSPacket.PlayToClient::handler)
				.play(DialoguePackets.CloseScreen.ID, DialoguePackets.CloseScreen::read, MSPacket.PlayToClient::handler)
				.play(DialoguePackets.OnCloseScreen.ID, DialoguePackets.OnCloseScreen::read, MSPacket.PlayToServer::handler)
				.play(DialoguePackets.TriggerResponse.ID, DialoguePackets.TriggerResponse::read, MSPacket.PlayToServer::handler)
				
				.play(ConnectToSburbServerPacket.ID, ConnectToSburbServerPacket::read, MSPacket.PlayToServer::handler)
				.play(OpenSburbServerPacket.ID, OpenSburbServerPacket::read, MSPacket.PlayToServer::handler)
				.play(ResumeSburbConnectionPackets.AsClient.ID, ResumeSburbConnectionPackets.AsClient::read, MSPacket.PlayToServer::handler)
				.play(ResumeSburbConnectionPackets.AsServer.ID, ResumeSburbConnectionPackets.AsServer::read, MSPacket.PlayToServer::handler)
				.play(CloseSburbConnectionPackets.AsClient.ID, CloseSburbConnectionPackets.AsClient::read, MSPacket.PlayToServer::handler)
				.play(CloseSburbConnectionPackets.AsServer.ID, CloseSburbConnectionPackets.AsServer::read, MSPacket.PlayToServer::handler)
				.play(CloseRemoteSburbConnectionPacket.ID, CloseRemoteSburbConnectionPacket::read, MSPacket.PlayToServer::handler)
				.play(ClearMessagePacket.ID, ClearMessagePacket::read, MSPacket.PlayToServer::handler)
				
				.play(SkaianetInfoPackets.Data.ID, SkaianetInfoPackets.Data::read, MSPacket.PlayToClient::handler)
				.play(SkaianetInfoPackets.HasEntered.ID, SkaianetInfoPackets.HasEntered::read, MSPacket.PlayToClient::handler)
				.play(SkaianetInfoPackets.Request.ID, SkaianetInfoPackets.Request::read, MSPacket.PlayToServer::handler)
				.play(SkaianetInfoPackets.LandChains.ID, SkaianetInfoPackets.LandChains::read, MSPacket.PlayToClient::handler)
				
				.play(BurnDiskPacket.ID, BurnDiskPacket::read, MSPacket.PlayToServer::handler)
				.play(ThemeSelectPacket.ID, ThemeSelectPacket::read, MSPacket.PlayToServer::handler)
				.play(DataCheckerPackets.Request.ID, DataCheckerPackets.Request::read, MSPacket.PlayToServer::handler)
				.play(DataCheckerPackets.Data.ID, DataCheckerPackets.Data::read, MSPacket.PlayToClient::handler)
				.play(DataCheckerPackets.Permission.ID, DataCheckerPackets.Permission::read, MSPacket.PlayToClient::handler)
				
				.play(MiscContainerPacket.ID, MiscContainerPacket::read, MSPacket.PlayToServer::handler)
				
				.play(ClientEditPackets.Exit.ID, ClientEditPackets.Exit::read, MSPacket.PlayToServer::handler)
				.play(ClientEditPackets.Activate.ID, ClientEditPackets.Activate::read, MSPacket.PlayToServer::handler)
				.play(ServerEditPackets.Activate.ID, ServerEditPackets.Activate::read, MSPacket.PlayToClient::handler)
				.play(ServerEditPackets.UpdateDeployList.ID, ServerEditPackets.UpdateDeployList::read, MSPacket.PlayToClient::handler)
				.play(ServerEditPackets.Exit.ID, ServerEditPackets.Exit::read, MSPacket.PlayToClient::handler)
				
				.play(EditmodeDragPackets.Fill.ID, EditmodeDragPackets.Fill::read, MSPacket.PlayToServer::handler)
				.play(EditmodeDragPackets.Destroy.ID, EditmodeDragPackets.Destroy::read, MSPacket.PlayToServer::handler)
				.play(EditmodeDragPackets.Cursor.ID, EditmodeDragPackets.Cursor::read, MSPacket.PlayToServer::handler)
				.play(EditmodeDragPackets.Reset.ID, EditmodeDragPackets.Reset::read, MSPacket.PlayToServer::handler)
				
				.play(EditmodeLocationsPacket.ID, EditmodeLocationsPacket::read, MSPacket.PlayToClient::handler)
				.play(EditmodeInventoryPackets.Update.ID, EditmodeInventoryPackets.Update::read, MSPacket.PlayToClient::handler)
				.play(EditmodeInventoryPackets.Scroll.ID, EditmodeInventoryPackets.Scroll::read, MSPacket.PlayToServer::handler)
				.play(EditmodeTeleportPacket.ID, EditmodeTeleportPacket::read, MSPacket.PlayToServer::handler)
				
				.play(MachinePackets.SetRunning.ID, MachinePackets.SetRunning::read, MSPacket.PlayToServer::handler)
				.play(MachinePackets.SetLooping.ID, MachinePackets.SetLooping::read, MSPacket.PlayToServer::handler)
				
				.play(AlchemiterPacket.ID, AlchemiterPacket::read, MSPacket.PlayToServer::handler)
				.play(PunchDesignixPacket.ID, PunchDesignixPacket::read, MSPacket.PlayToServer::handler)
				.play(GristWildcardPacket.ID, GristWildcardPacket::read, MSPacket.PlayToServer::handler)
				.play(SendificatorPacket.ID, SendificatorPacket::read, MSPacket.PlayToServer::handler)
				
				.play(TransportalizerPackets.Id.ID, TransportalizerPackets.Id::read, MSPacket.PlayToServer::handler)
				.play(TransportalizerPackets.DestId.ID, TransportalizerPackets.DestId::read, MSPacket.PlayToServer::handler)
				
				.play(AreaEffectPacket.ID, AreaEffectPacket::read, MSPacket.PlayToServer::handler)
				.play(WirelessRedstoneTransmitterPacket.ID, WirelessRedstoneTransmitterPacket::read, MSPacket.PlayToServer::handler)
				.play(StatStorerPacket.ID, StatStorerPacket::read, MSPacket.PlayToServer::handler)
				.play(RemoteObserverPacket.ID, RemoteObserverPacket::read, MSPacket.PlayToServer::handler)
				.play(SummonerPacket.ID, SummonerPacket::read, MSPacket.PlayToServer::handler)
				.play(StructureCorePacket.ID, StructureCorePacket::read, MSPacket.PlayToServer::handler)
				.play(BlockTeleporterPacket.ID, BlockTeleporterPacket::read, MSPacket.PlayToServer::handler)
				
				.play(EffectTogglePacket.ID, EffectTogglePacket::read, MSPacket.PlayToServer::handler)
				.play(StoneTabletPacket.ID, StoneTabletPacket::read, MSPacket.PlayToServer::handler)
				.play(AnthvilPacket.ID, AnthvilPacket::read, MSPacket.PlayToServer::handler)
				
				.play(MagicRangedEffectPacket.ID, MagicRangedEffectPacket::read, MSPacket.PlayToClient::handler)
				.play(MagicAOEEffectPacket.ID, MagicAOEEffectPacket::read, MSPacket.PlayToClient::handler)
				
				.play(LotusFlowerPacket.ID, LotusFlowerPacket::read, MSPacket.PlayToClient::handler)
				.play(ServerCursorPacket.ID, ServerCursorPacket::read, MSPacket.PlayToClient::handler)
				.play(MusicPlayerPacket.ID, MusicPlayerPacket::read, MSPacket.PlayToClient::handler)
				.play(GristRejectAnimationPacket.ID, GristRejectAnimationPacket::read, MSPacket.PlayToClient::handler)
				
				.play(StopCreativeShockEffectPacket.ID, StopCreativeShockEffectPacket::read, MSPacket.PlayToClient::handler)
				.play(ClientMovementPacket.ID, ClientMovementPacket::read, MSPacket.PlayToClient::handler)
				.play(GristToastPacket.ID, GristToastPacket::read, MSPacket.PlayToClient::handler)
				
				.play(AtheneumPackets.Scroll.ID, AtheneumPackets.Scroll::read, MSPacket.PlayToServer::handler)
				.play(AtheneumPackets.Update.ID, AtheneumPackets.Update::read, MSPacket.PlayToClient::handler)
				
				.play(EntryEffectPackets.Effect.ID, EntryEffectPackets.Effect::read, MSPacket.PlayToClient::handler)
				.play(EntryEffectPackets.Clear.ID, EntryEffectPackets.Clear::read, MSPacket.PlayToClient::handler)
		;
	}
}
