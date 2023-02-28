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
	private final int cacheLimit;
	private final GristSet gristCache;
	
	public GristToastPacket(GristSet gristValue, GristHelper.EnumSource source, boolean increase, int cacheLimit, GristSet gristCache)
	{
		this.gristValue = gristValue;
		this.source = source;
		this.increase = increase;
		this.cacheLimit = cacheLimit;
		this.gristCache = gristCache;
	}
	
	@Override
	public void encode(FriendlyByteBuf buffer)
	{
		gristValue.write(buffer);
		buffer.writeEnum(source);
		buffer.writeBoolean(increase);
		buffer.writeInt(cacheLimit);
		gristCache.write(buffer);
	}
	
	public static GristToastPacket decode(FriendlyByteBuf buffer)
	{
		GristSet gristValue = GristSet.read(buffer);
		GristHelper.EnumSource source = buffer.readEnum(GristHelper.EnumSource.class);
		boolean increase = buffer.readBoolean();
		int cacheLimit = buffer.readInt();
		GristSet gristCache = GristSet.read(buffer);
		return new GristToastPacket(gristValue, source, increase, cacheLimit, gristCache);
	}
	
	@Override
	public void execute()
	{
		GristSet gristValue = this.gristValue;
		GristHelper.EnumSource source = this.source;
		boolean increase = this.increase;
		int cacheLimit = this.cacheLimit;
		GristSet gristCache = this.gristCache;
		GristToast.sendGristMessage(gristValue, source, increase, cacheLimit, gristCache);
	}
}
