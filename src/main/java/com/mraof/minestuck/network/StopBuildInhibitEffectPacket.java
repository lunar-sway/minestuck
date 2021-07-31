package com.mraof.minestuck.network;

import com.mraof.minestuck.entity.LotusFlowerEntity;
import com.mraof.minestuck.world.storage.ClientPlayerData;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ServerData;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.SidedProvider;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.EnumSet;
import java.util.function.Supplier;

public class StopBuildInhibitEffectPacket implements PlayToServerPacket
{
	private final int entityID;
	
	public static StopBuildInhibitEffectPacket createPacket(LotusFlowerEntity entity, LotusFlowerEntity.Animation animation)
	{
		return new StopBuildInhibitEffectPacket(entity.getEntityId());
	}
	
	public StopBuildInhibitEffectPacket(int entityID)
	{
		this.entityID = entityID;
	}
	
	@Override
	public void encode(PacketBuffer buffer)
	{
		buffer.writeInt(entityID);
	}
	
	public static StopBuildInhibitEffectPacket decode(PacketBuffer buffer)
	{
		int entityID = buffer.readInt(); //readInt spits out the values you gave to the PacketBuffer in encode in that order
		
		return new StopBuildInhibitEffectPacket(entityID);
	}
	
	@Override
	public void consume(Supplier<NetworkEvent.Context> ctx)
	{
		PlayToServerPacket.super.consume(ctx);
	}
	
	@Override
	public void execute(ServerPlayerEntity player)
	{
		Entity entity = Minecraft.getInstance().world.getEntityByID(entityID);
		if(entity instanceof PlayerEntity)
		{
			PlayerEntity playerEntity = (PlayerEntity) entity;
			if(!playerEntity.isCreative())
				playerEntity.abilities.allowEdit = !((ServerPlayerEntity) playerEntity).interactionManager.getGameType().hasLimitedInteractions();
		}
	}
	
	/*public EnumSet<Side> getSenderSide()
	{
		return EnumSet.of(SidedProvider.SERVER);
	}*/
}
