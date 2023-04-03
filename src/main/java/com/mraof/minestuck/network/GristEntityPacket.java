package com.mraof.minestuck.network;

import com.mraof.minestuck.entity.item.GristEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.Entity;

import java.util.Objects;

public class GristEntityPacket implements PlayToClientPacket
{
	private final int entityID;
	private final GristEntity.Animation animation;
	
	public static GristEntityPacket createPacket(GristEntity entity, GristEntity.Animation animation)
	{
		return new GristEntityPacket(entity.getId(), animation);
	}
	
	private GristEntityPacket(int entityID, GristEntity.Animation animation)
	{
		this.entityID = entityID;
		this.animation = animation;
		
	}
	
	@Override
	public void encode(FriendlyByteBuf buffer)
	{
		buffer.writeInt(entityID);
		buffer.writeInt(animation.ordinal());
	}
	
	public static GristEntityPacket decode(FriendlyByteBuf buffer)
	{
		int entityID = buffer.readInt();
		GristEntity.Animation animation = GristEntity.Animation.values()[buffer.readInt()];
		
		return new GristEntityPacket(entityID, animation);
	}
	
	@Override
	public void execute()
	{
		Entity entity = Objects.requireNonNull(Minecraft.getInstance().level).getEntity(entityID);
		if(entity instanceof GristEntity gristEntity)
		{
			gristEntity.setAnimationFromPacket(animation);
		}
	}
	
}
