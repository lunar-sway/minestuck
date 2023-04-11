package com.mraof.minestuck.network;

import com.mraof.minestuck.entity.item.GristEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.Entity;

import java.util.Objects;

public class GristRejectAnimationPacket implements PlayToClientPacket
{
	private final int entityID;
	
	public static GristRejectAnimationPacket createPacket(GristEntity entity)
	{
		return new GristRejectAnimationPacket(entity.getId());
	}
	
	private GristRejectAnimationPacket(int entityID)
	{
		this.entityID = entityID;
		
	}
	
	@Override
	public void encode(FriendlyByteBuf buffer)
	{
		buffer.writeInt(entityID);
	}
	
	public static GristRejectAnimationPacket decode(FriendlyByteBuf buffer)
	{
		int entityID = buffer.readInt();
		
		return new GristRejectAnimationPacket(entityID);
	}
	
	@Override
	public void execute()
	{
		Entity entity = Objects.requireNonNull(Minecraft.getInstance().level).getEntity(entityID);
		if(entity instanceof GristEntity gristEntity)
		{
			gristEntity.setAnimationFromPacket();
		}
	}
	
}
