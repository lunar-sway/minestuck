package com.mraof.minestuck.network;

import com.mraof.minestuck.tileentity.redstone.RemoteObserverTileEntity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;

import javax.annotation.Nullable;
import java.util.Optional;

public class RemoteObserverPacket implements PlayToServerPacket
{
	private final RemoteObserverTileEntity.ActiveType activeType;
	private final BlockPos tileBlockPos;
	private final EntityType<?> entityType;
	
	public RemoteObserverPacket(RemoteObserverTileEntity.ActiveType activeType, BlockPos tileBlockPos, @Nullable EntityType<?> entityType)
	{
		this.activeType = activeType;
		this.tileBlockPos = tileBlockPos;
		this.entityType = entityType;
	}
	
	@Override
	public void encode(PacketBuffer buffer)
	{
		buffer.writeEnum(activeType);
		buffer.writeBlockPos(tileBlockPos);
		if(entityType != null)
		{
			buffer.writeUtf(EntityType.getKey(entityType).toString());
		}
		else
			buffer.writeUtf(EntityType.getKey(EntityType.PLAYER).toString());
			
	}
	
	public static RemoteObserverPacket decode(PacketBuffer buffer)
	{
		RemoteObserverTileEntity.ActiveType activeType = buffer.readEnum(RemoteObserverTileEntity.ActiveType.class);
		BlockPos tileBlockPos = buffer.readBlockPos();
		Optional<EntityType<?>> attemptedEntityType = EntityType.byString(buffer.readUtf());
		
		return new RemoteObserverPacket(activeType, tileBlockPos, attemptedEntityType.orElse(null));
	}
	
	@Override
	public void execute(ServerPlayerEntity player)
	{
		TileEntity te = player.level.getBlockEntity(tileBlockPos);
		if(te instanceof RemoteObserverTileEntity)
		{
			((RemoteObserverTileEntity) te).setActiveType(activeType);
			if(entityType != null)
				((RemoteObserverTileEntity) te).setCurrentEntityType(entityType);
		}
	}
}