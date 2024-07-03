package com.mraof.minestuck.network;

import com.mraof.minestuck.Minestuck;
import com.mraof.minestuck.entity.item.GristEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;

import java.util.Objects;

public record GristRejectAnimationPacket(int entityID) implements MSPacket.PlayToClient
{
	public static final ResourceLocation ID = Minestuck.id("grist_reject_animation");
	
	public static GristRejectAnimationPacket createPacket(GristEntity entity)
	{
		return new GristRejectAnimationPacket(entity.getId());
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
	}
	
	public static GristRejectAnimationPacket read(FriendlyByteBuf buffer)
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
