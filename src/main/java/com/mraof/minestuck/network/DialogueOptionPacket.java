package com.mraof.minestuck.network;

import com.mraof.minestuck.entity.consort.ConsortEntity;
import com.mraof.minestuck.entity.consort.MessageType;
import com.mraof.minestuck.world.storage.PlayerData;
import com.mraof.minestuck.world.storage.PlayerSavedData;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.network.PacketBuffer;

public class DialogueOptionPacket implements PlayToServerPacket
{
	private final int optionIndex;
	
	public DialogueOptionPacket(int optionIndex)
	{
		this.optionIndex = optionIndex;
	}
	
	@Override
	public void encode(PacketBuffer buffer)
	{
		buffer.writeInt(optionIndex);
	}
	
	public static DialogueOptionPacket decode(PacketBuffer buffer)
	{
		int optionIndex = buffer.readInt();
		return new DialogueOptionPacket(optionIndex);
	}
	
	@Override
	public void execute(ServerPlayerEntity player)
	{
		PlayerData playerData = PlayerSavedData.getData(player);
		if(playerData.getCurrentDialogueTarget() instanceof ConsortEntity)
		{
			ConsortEntity consort = ((ConsortEntity) playerData.getCurrentDialogueTarget());
			((MessageType.OptionMessage) playerData.getCurrentDialogue()).executeAction(optionIndex, player, consort);
		}
	}
}