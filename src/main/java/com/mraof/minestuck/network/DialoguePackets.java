package com.mraof.minestuck.network;

import com.mraof.minestuck.Minestuck;
import com.mraof.minestuck.client.gui.MSScreenFactories;
import com.mraof.minestuck.entity.dialogue.Dialogue;
import com.mraof.minestuck.entity.dialogue.DialogueComponent;
import com.mraof.minestuck.util.MSAttachments;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.neoforge.network.handling.IPayloadContext;

import java.util.Optional;

public final class DialoguePackets
{
	public record OpenScreen(int dialogueId, Dialogue.DialogueData dialogueData) implements MSPacket.PlayToClient
	{
		public static final Type<OpenScreen> ID = new Type<>(Minestuck.id("dialogue/open_screen"));
		public static final StreamCodec<RegistryFriendlyByteBuf, OpenScreen> STREAM_CODEC = StreamCodec.composite(
				ByteBufCodecs.INT,
				OpenScreen::dialogueId,
				Dialogue.DialogueData.STREAM_CODEC,
				OpenScreen::dialogueData,
				OpenScreen::new
		);
		
		@Override
		public Type<? extends CustomPacketPayload> type()
		{
			return ID;
		}
		
		@Override
		public void execute(IPayloadContext context)
		{
			MSScreenFactories.displayDialogueScreen(this.dialogueId, this.dialogueData);
		}
	}
	
	public record CloseScreen() implements MSPacket.PlayToClient
	{
		
		public static final Type<CloseScreen> ID = new Type<>(Minestuck.id("dialogue/close_screen"));
		public static final StreamCodec<FriendlyByteBuf, CloseScreen> STREAM_CODEC = StreamCodec.unit(new CloseScreen());
		
		@Override
		public Type<? extends CustomPacketPayload> type()
		{
			return ID;
		}
		
		@Override
		public void execute(IPayloadContext context)
		{
			MSScreenFactories.closeDialogueScreen();
		}
	}
	
	public record OnCloseScreen(int dialogueId) implements MSPacket.PlayToServer
	{
		
		public static final Type<OnCloseScreen> ID = new Type<>(Minestuck.id("dialogue/on_close_screen"));
		
		public static final StreamCodec<FriendlyByteBuf, OnCloseScreen> STREAM_CODEC = StreamCodec.composite(
				ByteBufCodecs.INT,
				OnCloseScreen::dialogueId,
				OnCloseScreen::new
		);
		
		@Override
		public Type<? extends CustomPacketPayload> type()
		{
			return ID;
		}
		
		@Override
		public void execute(IPayloadContext context, ServerPlayer player)
		{
			player.getData(MSAttachments.CURRENT_DIALOGUE)
					.validateAndGetComponent(player.level(), this.dialogueId)
					.ifPresent(component -> component.clearOngoingDialogue(player));
		}
	}
	
	public record TriggerResponse(int responseIndex, int dialogueId) implements MSPacket.PlayToServer
	{
		
		public static final Type<TriggerResponse> ID = new Type<>(Minestuck.id("dialogue/trigger_response"));
		public static final StreamCodec<FriendlyByteBuf, TriggerResponse> STREAM_CODEC = StreamCodec.composite(
				ByteBufCodecs.INT,
				TriggerResponse::responseIndex,
				ByteBufCodecs.INT,
				TriggerResponse::dialogueId,
				TriggerResponse::new
		);
		
		@Override
		public Type<? extends CustomPacketPayload> type()
		{
			return ID;
		}
		
		@Override
		public void execute(IPayloadContext context, ServerPlayer player)
		{
			player.getData(MSAttachments.CURRENT_DIALOGUE)
					.validateAndGetComponent(player.level(), this.dialogueId)
					.ifPresent(component -> findAndTriggerResponse(player, component));
		}
		
		private void findAndTriggerResponse(ServerPlayer player, DialogueComponent component)
		{
			Optional<Dialogue.Node> optionalNode = component.validateAndGetCurrentNode(player);
			component.clearOngoingDialogue(player);
			optionalNode.flatMap(node -> node.getResponseIfValid(this.responseIndex))
					.ifPresent(response -> response.trigger(component, player));
		}
	}
}
