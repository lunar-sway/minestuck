package com.mraof.minestuck.network;

import com.mraof.minestuck.Minestuck;
import com.mraof.minestuck.entity.LotusFlowerEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;

public record LotusFlowerPacket(int entityID, LotusFlowerEntity.Animation animation) implements MSPacket.PlayToClient
{
	public static final ResourceLocation ID = Minestuck.id("lotus_flower");
	
	public static LotusFlowerPacket createPacket(LotusFlowerEntity entity, LotusFlowerEntity.Animation animation)
	{
		return new LotusFlowerPacket(entity.getId(), animation);
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
		buffer.writeInt(animation.ordinal());
	}
	
	public static LotusFlowerPacket read(FriendlyByteBuf buffer)
	{
		int entityID = buffer.readInt(); //readInt spits out the values you gave to the PacketBuffer in encode in that order
		LotusFlowerEntity.Animation animation = LotusFlowerEntity.Animation.values()[buffer.readInt()];
		
		return new LotusFlowerPacket(entityID, animation);
	}
	
	@Override
	public void execute()
	{
		Entity entity = Minecraft.getInstance().level.getEntity(entityID);
		if(entity instanceof LotusFlowerEntity)
		{
			((LotusFlowerEntity) entity).setAnimationFromPacket(animation);
		}
	}
}
