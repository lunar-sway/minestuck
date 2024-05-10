package com.mraof.minestuck.network;

import com.mraof.minestuck.Minestuck;
import com.mraof.minestuck.client.gui.MSScreenFactories;
import com.mraof.minestuck.entity.dialogue.Dialogue;
import com.mraof.minestuck.entity.dialogue.DialogueComponent;
import com.mraof.minestuck.util.MSAttachments;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;

import java.util.Optional;

public final class DialoguePackets
{
	public record OpenScreen(int dialogueId, Dialogue.DialogueData dialogueData) implements MSPacket.PlayToClient
	{
		public static final ResourceLocation ID = Minestuck.id("dialogue/open_screen");
		
		@Override
		public ResourceLocation id()
		{
			return ID;
		}
		
		@Override
		public void write(FriendlyByteBuf buffer)
		{
			buffer.writeInt(this.dialogueId);
			this.dialogueData.write(buffer);
		}
		
		public static OpenScreen read(FriendlyByteBuf buffer)
		{
			int dialogueId = buffer.readInt();
			var dialogueData = Dialogue.DialogueData.read(buffer);
			
			return new OpenScreen(dialogueId, dialogueData);
		}
		
		@Override
		public void execute()
		{
			MSScreenFactories.displayDialogueScreen(this.dialogueId, this.dialogueData);
		}
	}
	
	public record CloseScreen() implements MSPacket.PlayToClient
	{
		public static final ResourceLocation ID = Minestuck.id("dialogue/close_screen");
		
		@Override
		public ResourceLocation id()
		{
			return ID;
		}
		
		@Override
		public void write(FriendlyByteBuf buffer)
		{
		}
		
		public static CloseScreen read(FriendlyByteBuf ignored)
		{
			return new CloseScreen();
		}
		
		@Override
		public void execute()
		{
			MSScreenFactories.closeDialogueScreen();
		}
	}
	
	public record OnCloseScreen(int dialogueId) implements MSPacket.PlayToServer
	{
		public static final ResourceLocation ID = Minestuck.id("dialogue/on_close_screen");
		
		@Override
		public ResourceLocation id()
		{
			return ID;
		}
		
		@Override
		public void write(FriendlyByteBuf buffer)
		{
			buffer.writeInt(this.dialogueId);
		}
		
		public static OnCloseScreen read(FriendlyByteBuf buffer)
		{
			int dialogueId = buffer.readInt();
			
			return new OnCloseScreen(dialogueId);
		}
		
		@Override
		public void execute(ServerPlayer player)
		{
			player.getData(MSAttachments.CURRENT_DIALOGUE)
					.validateAndGetComponent(player.level(), this.dialogueId)
					.ifPresent(component -> component.clearOngoingDialogue(player));
		}
	}
	
	public record TriggerResponse(int responseIndex, int dialogueId) implements MSPacket.PlayToServer
	{
		public static final ResourceLocation ID = Minestuck.id("dialogue/trigger_response");
		
		@Override
		public ResourceLocation id()
		{
			return ID;
		}
		
		@Override
		public void write(FriendlyByteBuf buffer)
		{
			buffer.writeInt(this.responseIndex);
			buffer.writeInt(this.dialogueId);
		}
		
		public static TriggerResponse read(FriendlyByteBuf buffer)
		{
			int responseIndex = buffer.readInt();
			int dialogueId = buffer.readInt();
			
			return new TriggerResponse(responseIndex, dialogueId);
		}
		
		@Override
		public void execute(ServerPlayer player)
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
