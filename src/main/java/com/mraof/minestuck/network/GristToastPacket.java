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
	private final long cacheLimit;
	private final boolean isCacheOwner;
	
	public GristToastPacket(GristSet gristValue, GristHelper.EnumSource source, long cacheLimit, boolean isCacheOwner)
	{
		this.gristValue = gristValue;
		this.source = source;
		this.cacheLimit = cacheLimit;
		this.isCacheOwner = isCacheOwner;
	}
	
	@Override
	public void encode(FriendlyByteBuf buffer)
	{
		gristValue.write(buffer);
		buffer.writeEnum(source);
		buffer.writeLong(cacheLimit);
		buffer.writeBoolean(isCacheOwner);
	}
	
	public static GristToastPacket decode(FriendlyByteBuf buffer)
	{
		GristSet gristValue = GristSet.read(buffer);
		GristHelper.EnumSource source = buffer.readEnum(GristHelper.EnumSource.class);
		long cacheLimit = buffer.readLong();
		boolean isCacheOwner = buffer.readBoolean();
		return new GristToastPacket(gristValue, source, cacheLimit, isCacheOwner);
	}
	
	@Override
	public void execute()
	{
		GristSet gristValue = this.gristValue;
		GristHelper.EnumSource source = this.source;
		GristSet gristCache = ClientPlayerData.getGristCache(this.isCacheOwner);
		GristToast.sendGristMessage(gristValue, source, this.cacheLimit, gristCache);
	}
}
