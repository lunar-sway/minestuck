package com.mraof.minestuck.network;

import com.mraof.minestuck.item.crafting.alchemy.GristSet;
import com.mraof.minestuck.item.crafting.alchemy.GristType;
import com.mraof.minestuck.world.storage.PlayerSavedData;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkDirection;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.Map;
import java.util.function.Supplier;

public class GristCachePacket
{
	public final GristSet gristCache;
	public final boolean isEditmode;
	
	public GristCachePacket(GristSet gristCache, boolean isEditmode)
	{
		this.gristCache = gristCache;
		this.isEditmode = isEditmode;
	}
	
	public void encode(PacketBuffer buffer)
	{
		gristCache.write(buffer);
		buffer.writeBoolean(isEditmode);
	}
	
	public static GristCachePacket decode(PacketBuffer buffer)
	{
		GristSet gristCache = GristSet.read(buffer);
		boolean isEditmode = buffer.readBoolean();
		return new GristCachePacket(gristCache, isEditmode);
	}
	
	public void consume(Supplier<NetworkEvent.Context> ctx)
	{
		if(ctx.get().getDirection() == NetworkDirection.PLAY_TO_CLIENT)
			ctx.get().enqueueWork(this::execute);
		
		ctx.get().setPacketHandled(true);
	}
	
	private void execute()
	{
		PlayerSavedData.onPacketRecived(this);
	}
}