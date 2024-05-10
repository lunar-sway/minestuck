package com.mraof.minestuck.network;

import com.mraof.minestuck.Minestuck;
import com.mraof.minestuck.network.computer.*;
import com.mraof.minestuck.network.data.*;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlerEvent;

@Mod.EventBusSubscriber(modid = Minestuck.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class MSPayloads
{
	@SubscribeEvent
	private static void regsiter(RegisterPayloadHandlerEvent event)
	{
		event.registrar(Minestuck.MOD_ID)
				.versioned("1")
				.play(DataCheckerPermissionPacket.ID, DataCheckerPermissionPacket::read, MSPacket.PlayToClient::handler)
				.play(EcheladderDataPacket.ID, EcheladderDataPacket::read, MSPacket.PlayToClient::handler)
				.play(ModusDataPacket.ID, ModusDataPacket::read, MSPacket.PlayToClient::handler)
				.play(BoondollarDataPacket.ID, BoondollarDataPacket::read, MSPacket.PlayToClient::handler)
				.play(GristCachePacket.ID, GristCachePacket::read, MSPacket.PlayToClient::handler)
				.play(EditmodeCacheLimitPacket.ID, EditmodeCacheLimitPacket::read, MSPacket.PlayToClient::handler)
				.play(TitleDataPacket.ID, TitleDataPacket::read, MSPacket.PlayToClient::handler)
				.play(LandTypesDataPacket.ID, LandTypesDataPacket::read, MSPacket.PlayToClient::handler)
				
				.play(PlayerColorPacket.OpenSelection.ID, PlayerColorPacket.OpenSelection::read, MSPacket.PlayToClient::handler)
				.play(PlayerColorPacket.Data.ID, PlayerColorPacket.Data::read, MSPacket.PlayToClient::handler)
				.play(PlayerColorPacket.SelectIndex.ID, PlayerColorPacket.SelectIndex::read, MSPacket.PlayToServer::handler)
				.play(PlayerColorPacket.SelectRGB.ID, PlayerColorPacket.SelectRGB::read, MSPacket.PlayToServer::handler)
				
				.play(CaptchaDeckPacket.TriggerModusButton.ID, CaptchaDeckPacket.TriggerModusButton::read, MSPacket.PlayToServer::handler)
				.play(CaptchaDeckPacket.CaptchalogueHeldItem.ID, CaptchaDeckPacket.CaptchalogueHeldItem::read, MSPacket.PlayToServer::handler)
				.play(CaptchaDeckPacket.CaptchalogueInventorySlot.ID, CaptchaDeckPacket.CaptchalogueInventorySlot::read, MSPacket.PlayToServer::handler)
				.play(CaptchaDeckPacket.GetItem.ID, CaptchaDeckPacket.GetItem::read, MSPacket.PlayToServer::handler)
				.play(CaptchaDeckPacket.SetModusParameter.ID, CaptchaDeckPacket.SetModusParameter::read, MSPacket.PlayToServer::handler)
				.play(TitleSelectPacket.OpenScreen.ID, TitleSelectPacket.OpenScreen::read, MSPacket.PlayToClient::handler)
				.play(TitleSelectPacket.PickTitle.ID, TitleSelectPacket.PickTitle::read, MSPacket.PlayToServer::handler)
				
				.play(DialoguePackets.OpenScreen.ID, DialoguePackets.OpenScreen::read, MSPacket.PlayToClient::handler)
				.play(DialoguePackets.CloseScreen.ID, DialoguePackets.CloseScreen::read, MSPacket.PlayToClient::handler)
				.play(DialoguePackets.OnCloseScreen.ID, DialoguePackets.OnCloseScreen::read, MSPacket.PlayToServer::handler)
				.play(DialoguePackets.TriggerResponse.ID, DialoguePackets.TriggerResponse::read, MSPacket.PlayToServer::handler)
				
				.play(ConnectToSburbServerPacket.ID, ConnectToSburbServerPacket::read, MSPacket.PlayToServer::handler)
				.play(OpenSburbServerPacket.ID, OpenSburbServerPacket::read, MSPacket.PlayToServer::handler)
				.play(ResumeSburbConnectionPacket.AsClient.ID, ResumeSburbConnectionPacket.AsClient::read, MSPacket.PlayToServer::handler)
				.play(ResumeSburbConnectionPacket.AsServer.ID, ResumeSburbConnectionPacket.AsServer::read, MSPacket.PlayToServer::handler)
				.play(CloseSburbConnectionPacket.AsClient.ID, CloseSburbConnectionPacket.AsClient::read, MSPacket.PlayToServer::handler)
				.play(CloseSburbConnectionPacket.AsServer.ID, CloseSburbConnectionPacket.AsServer::read, MSPacket.PlayToServer::handler)
				.play(CloseRemoteSburbConnectionPacket.ID, CloseRemoteSburbConnectionPacket::read, MSPacket.PlayToServer::handler)
				.play(ClearMessagePacket.ID, ClearMessagePacket::read, MSPacket.PlayToServer::handler)
				
				.play(SkaianetInfoPacket.Data.ID, SkaianetInfoPacket.Data::read, MSPacket.PlayToClient::handler)
				.play(SkaianetInfoPacket.HasEntered.ID, SkaianetInfoPacket.HasEntered::read, MSPacket.PlayToClient::handler)
				.play(SkaianetInfoPacket.Request.ID, SkaianetInfoPacket.Request::read, MSPacket.PlayToServer::handler)
				.play(SkaianetInfoPacket.LandChains.ID, SkaianetInfoPacket.LandChains::read, MSPacket.PlayToClient::handler)
				
				.play(BurnDiskPacket.ID, BurnDiskPacket::read, MSPacket.PlayToServer::handler)
				.play(ThemeSelectPacket.ID, ThemeSelectPacket::read, MSPacket.PlayToServer::handler)
				.play(DataCheckerPacket.Request.ID, DataCheckerPacket.Request::read, MSPacket.PlayToServer::handler)
				.play(DataCheckerPacket.Data.ID, DataCheckerPacket.Data::read, MSPacket.PlayToClient::handler)
				
				.play(MiscContainerPacket.ID, MiscContainerPacket::read, MSPacket.PlayToServer::handler)
				
				.play(ClientEditPacket.Exit.ID, ClientEditPacket.Exit::read, MSPacket.PlayToServer::handler)
				.play(ClientEditPacket.Activate.ID, ClientEditPacket.Activate::read, MSPacket.PlayToServer::handler)
				.play(ServerEditPacket.Activate.ID, ServerEditPacket.Activate::read, MSPacket.PlayToClient::handler)
				.play(ServerEditPacket.UpdateDeployList.ID, ServerEditPacket.UpdateDeployList::read, MSPacket.PlayToClient::handler)
				.play(ServerEditPacket.Exit.ID, ServerEditPacket.Exit::read, MSPacket.PlayToClient::handler)
				
				.play(EditmodeDragPacket.Fill.ID, EditmodeDragPacket.Fill::read, MSPacket.PlayToServer::handler)
				.play(EditmodeDragPacket.Destroy.ID, EditmodeDragPacket.Destroy::read, MSPacket.PlayToServer::handler)
				.play(EditmodeDragPacket.Cursor.ID, EditmodeDragPacket.Cursor::read, MSPacket.PlayToServer::handler)
				.play(EditmodeDragPacket.Reset.ID, EditmodeDragPacket.Reset::read, MSPacket.PlayToServer::handler)
				
				.play(EditmodeLocationsPacket.ID, EditmodeLocationsPacket::read, MSPacket.PlayToClient::handler)
				.play(EditmodeInventoryPacket.Update.ID, EditmodeInventoryPacket.Update::read, MSPacket.PlayToClient::handler)
				.play(EditmodeInventoryPacket.Scroll.ID, EditmodeInventoryPacket.Scroll::read, MSPacket.PlayToServer::handler)
				.play(EditmodeTeleportPacket.ID, EditmodeTeleportPacket::read, MSPacket.PlayToServer::handler)
				
				.play(MachinePacket.SetRunning.ID, MachinePacket.SetRunning::read, MSPacket.PlayToServer::handler)
				.play(MachinePacket.SetLooping.ID, MachinePacket.SetLooping::read, MSPacket.PlayToServer::handler)
				
				.play(AlchemiterPacket.ID, AlchemiterPacket::read, MSPacket.PlayToServer::handler)
				.play(PunchDesignixPacket.ID, PunchDesignixPacket::read, MSPacket.PlayToServer::handler)
				.play(GristWildcardPacket.ID, GristWildcardPacket::read, MSPacket.PlayToServer::handler)
				.play(SendificatorPacket.ID, SendificatorPacket::read, MSPacket.PlayToServer::handler)
				
				.play(TransportalizerPacket.Id.ID, TransportalizerPacket.Id::read, MSPacket.PlayToServer::handler)
				.play(TransportalizerPacket.DestId.ID, TransportalizerPacket.DestId::read, MSPacket.PlayToServer::handler)
				
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
				
				.play(AtheneumPacket.Scroll.ID, AtheneumPacket.Scroll::read, MSPacket.PlayToServer::handler)
				.play(AtheneumPacket.Update.ID, AtheneumPacket.Update::read, MSPacket.PlayToClient::handler)
				
				.play(EntryEffectPackets.Effect.ID, EntryEffectPackets.Effect::read, MSPacket.PlayToClient::handler)
				.play(EntryEffectPackets.Clear.ID, EntryEffectPackets.Clear::read, MSPacket.PlayToClient::handler)
		;
	}
}
