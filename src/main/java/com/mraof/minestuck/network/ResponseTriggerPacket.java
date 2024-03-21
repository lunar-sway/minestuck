package com.mraof.minestuck.network;

import com.mraof.minestuck.entity.dialogue.Dialogue;
import com.mraof.minestuck.entity.dialogue.DialogueComponent;
import com.mraof.minestuck.util.MSCapabilities;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;

import java.util.Optional;

public record ResponseTriggerPacket(int responseIndex, int dialogueId) implements MSPacket.PlayToServer
{
	
	@Override
	public void encode(FriendlyByteBuf buffer)
	{
		buffer.writeInt(this.responseIndex);
		buffer.writeInt(this.dialogueId);
	}
	
	public static ResponseTriggerPacket decode(FriendlyByteBuf buffer)
	{
		int responseIndex = buffer.readInt();
		int dialogueId = buffer.readInt();
		
		return new ResponseTriggerPacket(responseIndex, dialogueId);
	}
	
	@Override
	public void execute(ServerPlayer player)
	{
		player.getCapability(MSCapabilities.CURRENT_DIALOGUE)
				.orElseThrow(IllegalStateException::new)
				.validateAndGetActiveComponent(player, this.dialogueId)
				.ifPresent(component -> findAndTriggerResponse(player, component));
	}
	
	private void findAndTriggerResponse(ServerPlayer player, DialogueComponent component)
	{
		Optional<Dialogue.Node> optionalNode = component.validateAndGetCurrentNode(player);
		component.clearCurrentNode(player);
		optionalNode.flatMap(node -> node.getResponseIfValid(this.responseIndex))
				.ifPresent(response -> response.trigger(component, player));
	}
}