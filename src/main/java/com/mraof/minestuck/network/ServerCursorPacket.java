package com.mraof.minestuck.network;

import com.mraof.minestuck.entity.ServerCursorEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.Entity;

public class ServerCursorPacket implements PlayToClientPacket
{
	private final int entityID;
	private final ServerCursorEntity.AnimationType animation;
	
	public static ServerCursorPacket createPacket(ServerCursorEntity entity, ServerCursorEntity.AnimationType animation)
	{
		return new ServerCursorPacket(entity.getId(), animation);
	}
	
	private ServerCursorPacket(int entityID, ServerCursorEntity.AnimationType animation)
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
	
	public static ServerCursorPacket decode(FriendlyByteBuf buffer)
	{
		int entityID = buffer.readInt(); //readInt spits out the values you gave to the PacketBuffer in encode in that order
		ServerCursorEntity.AnimationType animation = ServerCursorEntity.AnimationType.values()[buffer.readInt()];
		
		return new ServerCursorPacket(entityID, animation);
	}
	
	@Override
	public void execute()
	{
		Entity entity = Minecraft.getInstance().level.getEntity(entityID);
		if(entity instanceof ServerCursorEntity cursorEntity)
		{
			cursorEntity.setAnimationFromPacket(animation);
		}
	}
}

