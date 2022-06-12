package com.mraof.minestuck.network;

import com.mraof.minestuck.tileentity.redstone.RemoteObserverTileEntity;
import net.minecraft.block.BlockState;
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
	private final int observingRange;
	
	public RemoteObserverPacket(RemoteObserverTileEntity.ActiveType activeType, int observingRange, BlockPos tileBlockPos, @Nullable EntityType<?> entityType)
	{
		this.activeType = activeType;
		this.observingRange = observingRange;
		this.tileBlockPos = tileBlockPos;
		this.entityType = entityType;
	}
	
	@Override
	public void encode(PacketBuffer buffer)
	{
		buffer.writeEnum(activeType);
		buffer.writeInt(observingRange);
		buffer.writeBlockPos(tileBlockPos);
		if(entityType != null)
		{
			buffer.writeUtf(EntityType.getKey(entityType).toString());
		} else
			buffer.writeUtf(EntityType.getKey(EntityType.PLAYER).toString());
		
	}
	
	public static RemoteObserverPacket decode(PacketBuffer buffer)
	{
		RemoteObserverTileEntity.ActiveType activeType = buffer.readEnum(RemoteObserverTileEntity.ActiveType.class);
		int observingRange = buffer.readInt();
		BlockPos tileBlockPos = buffer.readBlockPos();
		Optional<EntityType<?>> attemptedEntityType = EntityType.byString(buffer.readUtf());
		
		return new RemoteObserverPacket(activeType, observingRange, tileBlockPos, attemptedEntityType.orElse(null));
	}
	
	@Override
	public void execute(ServerPlayerEntity player)
	{
		if(player.level.isAreaLoaded(tileBlockPos, 0))
		{
			TileEntity te = player.level.getBlockEntity(tileBlockPos);
			if(te instanceof RemoteObserverTileEntity)
			{
				if(Math.sqrt(player.distanceToSqr(tileBlockPos.getX() + 0.5, tileBlockPos.getY() + 0.5, tileBlockPos.getZ() + 0.5)) <= 8)
				{
					RemoteObserverTileEntity observerTE = ((RemoteObserverTileEntity) te);
					observerTE.setActiveType(activeType);
					if(entityType != null)
						observerTE.setCurrentEntityType(entityType);
					observerTE.setObservingRange(observingRange);
					//Imitates the structure block to ensure that changes are sent client-side
					observerTE.setChanged();
					BlockState state = player.level.getBlockState(tileBlockPos);
					player.level.sendBlockUpdated(tileBlockPos, state, state, 3);
				}
			}
		}
	}
}