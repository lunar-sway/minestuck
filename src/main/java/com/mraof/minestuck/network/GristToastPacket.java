package com.mraof.minestuck.network;

import com.mraof.minestuck.alchemy.GristHelper;
import com.mraof.minestuck.alchemy.GristSet;
import com.mraof.minestuck.client.ClientProxy;
import com.mraof.minestuck.client.gui.toasts.GristToast;
import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Player;

public class GristToastPacket implements PlayToClientPacket
{
	private final GristSet gristValue;
	private final GristToast.EnumSource source;
	private final boolean increase;
	
	public GristToastPacket(GristSet gristValue, GristToast.EnumSource source, boolean increase)
	{
		this.gristValue = gristValue;
		this.source = source;
		this.increase = increase;
	}
	
	@Override
	public void encode(FriendlyByteBuf buffer)
	{
		gristValue.write(buffer);
		buffer.writeEnum(source);
		buffer.writeBoolean(increase);
	}
	
	public static GristToastPacket decode(FriendlyByteBuf buffer)
	{
		GristSet gristValue = GristSet.read(buffer);
		GristToast.EnumSource source = buffer.readEnum(GristToast.EnumSource.class);
		boolean increase = buffer.readBoolean();
		return new GristToastPacket(gristValue, source, increase);
	}
	
	@Override
	public void execute()
	{
		Player playerEntity = ClientProxy.getClientPlayer();
		
		if(playerEntity != null)
		{
			GristSet gristValue = this.gristValue;
			GristToast.EnumSource source = this.source;
			boolean increase = this.increase;
			GristHelper.sendGristMessage(playerEntity, gristValue, source, increase);
		}
	}
}
