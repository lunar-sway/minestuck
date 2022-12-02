package com.mraof.minestuck.network;

import com.mraof.minestuck.alchemy.GristHelper;
import com.mraof.minestuck.alchemy.GristSet;
import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;

public class GristToastPacket implements PlayToClientPacket
{
	public final GristSet gristValue;
	public final String source;
	public final boolean increase;
	
	public GristToastPacket(GristSet gristValue, String source, boolean increase)
	{
		this.gristValue = gristValue;
		this.source = source;
		this.increase = increase;
	}
	
	@Override
	public void encode(FriendlyByteBuf buffer)
	{
		gristValue.write(buffer);
		buffer.writeUtf(source);
		buffer.writeBoolean(increase);
	}
	
	public static GristToastPacket decode(FriendlyByteBuf buffer)
	{
		GristSet gristValue = GristSet.read(buffer);
		String source = buffer.readUtf();
		boolean increase = buffer.readBoolean();
		return new GristToastPacket(gristValue, source, increase);
	}
	
	@Override
	public void execute()
	{
		GristSet gristValue = this.gristValue;
		String source = this.source;
		boolean increase = this.increase;
		GristHelper.sendGristMessage(Minecraft.getInstance().player, gristValue, source, increase);
	}
}
