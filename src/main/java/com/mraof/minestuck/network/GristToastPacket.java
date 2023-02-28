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
	private final GristHelper.EnumSource source;
	private final boolean increase;
	private final int cache_limit;
	private final GristSet gristTotal;
	
	public GristToastPacket(GristSet gristValue, GristHelper.EnumSource source, boolean increase, int cache_limit, GristSet gristTotal)
	{
		this.gristValue = gristValue;
		this.source = source;
		this.increase = increase;
		this.cache_limit = cache_limit;
		this.gristTotal = gristTotal;
	}
	
	@Override
	public void encode(FriendlyByteBuf buffer)
	{
		gristValue.write(buffer);
		buffer.writeEnum(source);
		buffer.writeBoolean(increase);
		buffer.writeInt(cache_limit);
		gristTotal.write(buffer);
	}
	
	public static GristToastPacket decode(FriendlyByteBuf buffer)
	{
		GristSet gristValue = GristSet.read(buffer);
		GristHelper.EnumSource source = buffer.readEnum(GristHelper.EnumSource.class);
		boolean increase = buffer.readBoolean();
		int cache_limit = buffer.readInt();
		GristSet gristTotal = GristSet.read(buffer);
		return new GristToastPacket(gristValue, source, increase, cache_limit, gristTotal);
	}
	
	@Override
	public void execute()
	{
		GristSet gristValue = this.gristValue;
		GristHelper.EnumSource source = this.source;
		boolean increase = this.increase;
		int cache_limit = this.cache_limit;
		GristSet gristTotal = this.gristTotal;
		GristToast.sendGristMessage(gristValue, source, increase, cache_limit, gristTotal);
	}
}
