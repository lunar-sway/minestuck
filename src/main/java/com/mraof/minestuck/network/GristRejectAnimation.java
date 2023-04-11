package com.mraof.minestuck.network;

import com.mraof.minestuck.entity.item.GristEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.Entity;

import java.util.Objects;

public class GristRejectAnimation implements PlayToClientPacket
{
	private final int entityID;
	
	public static GristRejectAnimation createPacket(GristEntity entity)
	{
		return new GristRejectAnimation(entity.getId());
	}
	
	private GristRejectAnimation(int entityID)
	{
		this.entityID = entityID;
		
	}
	
	@Override
	public void encode(FriendlyByteBuf buffer)
	{
		buffer.writeInt(entityID);
	}
	
	public static GristRejectAnimation decode(FriendlyByteBuf buffer)
	{
		int entityID = buffer.readInt();
		
		return new GristRejectAnimation(entityID);
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
