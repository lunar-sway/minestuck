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
		buffer.writeEnumValue(activeType);
		buffer.writeBlockPos(tileBlockPos);
		buffer.writeString(EntityType.getKey(entityType).toString());
	}
	
	public static RemoteObserverPacket decode(PacketBuffer buffer)
	{
		RemoteObserverTileEntity.ActiveType activeType = buffer.readEnumValue(RemoteObserverTileEntity.ActiveType.class);
		BlockPos tileBlockPos = buffer.readBlockPos();
		Optional<EntityType<?>> attemptedEntityType = EntityType.byKey(buffer.readString());
		
		return new RemoteObserverPacket(activeType, tileBlockPos, attemptedEntityType.orElse(null));
	}
	
	@Override
	public void execute(ServerPlayerEntity player)
	{
		TileEntity te = player.world.getTileEntity(tileBlockPos);
		if(te instanceof RemoteObserverTileEntity)
		{
			((RemoteObserverTileEntity) te).setActiveType(activeType);
			if(entityType != null)
				((RemoteObserverTileEntity) te).setCurrentEntityType(entityType);
		}
	}
}