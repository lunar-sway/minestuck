package com.mraof.minestuck.network;

import com.mraof.minestuck.Minestuck;
import com.mraof.minestuck.entity.ServerCursorEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;

public record ServerCursorPacket(int entityID, ServerCursorEntity.AnimationType animation) implements MSPacket.PlayToClient
{
	public static final ResourceLocation ID = Minestuck.id("server_cursor");
	
	public static ServerCursorPacket createPacket(ServerCursorEntity entity, ServerCursorEntity.AnimationType animation)
	{
		return new ServerCursorPacket(entity.getId(), animation);
	}
	
	@Override
	public ResourceLocation id()
	{
		return ID;
	}
	
	@Override
	public void write(FriendlyByteBuf buffer)
	{
		buffer.writeInt(entityID);
		buffer.writeEnum(animation);
	}
	
	public static ServerCursorPacket read(FriendlyByteBuf buffer)
	{
		int entityID = buffer.readInt();
		ServerCursorEntity.AnimationType animation = buffer.readEnum(ServerCursorEntity.AnimationType.class);
		
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

