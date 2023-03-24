package com.mraof.minestuck.network;

import com.mraof.minestuck.alchemy.GristHelper;
import com.mraof.minestuck.alchemy.GristSet;
import com.mraof.minestuck.client.gui.toasts.GristToast;
import com.mraof.minestuck.player.ClientPlayerData;
import net.minecraft.network.FriendlyByteBuf;

public class GristToastPacket implements PlayToClientPacket
{
	private final GristSet gristValue;
	private final GristHelper.EnumSource source;
	private final boolean increase;
	private final int cacheLimit;
	private final boolean isCacheOwner;
	
	public GristToastPacket(GristSet gristValue, GristHelper.EnumSource source, boolean increase, int cacheLimit, boolean isCacheOwner)
	{
		this.gristValue = gristValue;
		this.source = source;
		this.increase = increase;
		this.cacheLimit = cacheLimit;
		this.isCacheOwner = isCacheOwner;
	}
	
	@Override
	public void encode(FriendlyByteBuf buffer)
	{
		gristValue.write(buffer);
		buffer.writeEnum(source);
		buffer.writeBoolean(increase);
		buffer.writeInt(cacheLimit);
		buffer.writeBoolean(isCacheOwner);
	}
	
	public static GristToastPacket decode(FriendlyByteBuf buffer)
	{
		GristSet gristValue = GristSet.read(buffer);
		GristHelper.EnumSource source = buffer.readEnum(GristHelper.EnumSource.class);
		boolean increase = buffer.readBoolean();
		int cacheLimit = buffer.readInt();
		boolean isCacheOwner = buffer.readBoolean();
		return new GristToastPacket(gristValue, source, increase, cacheLimit, isCacheOwner);
	}
	
	@Override
	public void execute()
	{
		GristSet gristValue = this.gristValue;
		GristHelper.EnumSource source = this.source;
		boolean increase = this.increase;
		int cacheLimit = this.cacheLimit;
		GristSet gristCache = ClientPlayerData.getGristCache(this.isCacheOwner);
		GristToast.sendGristMessage(gristValue, source, increase, cacheLimit, gristCache);
	}
}
