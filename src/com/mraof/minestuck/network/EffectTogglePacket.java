package com.mraof.minestuck.network;

import java.util.EnumSet;

import com.mraof.minestuck.util.IdentifierHandler;
import com.mraof.minestuck.util.MinestuckPlayerData;
import com.mraof.minestuck.util.IdentifierHandler.PlayerIdentifier;

import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.relauncher.Side;

public class EffectTogglePacket extends MinestuckPacket
{

	@Override
	public MinestuckPacket generatePacket(Object... data) {return this;}

	@Override
	public MinestuckPacket consumePacket(ByteBuf data) {return this;}

	@Override
	public void execute(EntityPlayer player) 
	{
		PlayerIdentifier id = IdentifierHandler.encode(player);
		MinestuckPlayerData.setEffectToggle(id, !MinestuckPlayerData.getEffectToggle(id));
	}

	@Override
	public EnumSet<Side> getSenderSide() {return EnumSet.of(Side.CLIENT);}

}
