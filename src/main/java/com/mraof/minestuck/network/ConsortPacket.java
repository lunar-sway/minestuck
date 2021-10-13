package com.mraof.minestuck.network;

import com.mraof.minestuck.entity.consort.ConsortEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.network.PacketBuffer;

public class ConsortPacket implements PlayToClientPacket
{
	private final int entityID;
	private final ConsortEntity.Animation animation;
	private final int animationTimer;
	
	public static ConsortPacket createPacket(ConsortEntity entity, ConsortEntity.Animation animation, int animationTimer)
	{
		return new ConsortPacket(entity.getEntityId(), animation, animationTimer);
	}
	
	private ConsortPacket(int entityID, ConsortEntity.Animation animation, int animationTimer)
	{
		this.entityID = entityID;
		this.animation = animation;
		this.animationTimer = animationTimer;
	}
	
	@Override
	public void encode(PacketBuffer buffer)
	{
		buffer.writeInt(entityID);
		buffer.writeInt(animation.ordinal());
		buffer.writeInt(animationTimer);
	}
	
	public static ConsortPacket decode(PacketBuffer buffer)
	{
		int entityID = buffer.readInt(); //readInt spits out the values you gave to the PacketBuffer in encode in that order
		ConsortEntity.Animation animation = ConsortEntity.Animation.values()[buffer.readInt()];
		int animationTimer = buffer.readInt();
		
		return new ConsortPacket(entityID, animation, animationTimer);
	}
	
	@Override
	public void execute()
	{
		Entity entity = Minecraft.getInstance().world.getEntityByID(entityID);
		if(entity instanceof ConsortEntity)
		{
			((ConsortEntity) entity).setAnimationFromPacket(animation, animationTimer);
		}
	}
}
