package com.mraof.minestuck.network.block;

import com.mraof.minestuck.Minestuck;
import com.mraof.minestuck.block.redstone.RemoteObserverBlock;
import com.mraof.minestuck.blockentity.redstone.RemoteObserverBlockEntity;
import com.mraof.minestuck.network.MSPacket;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.block.state.BlockState;

import javax.annotation.Nullable;
import java.util.Optional;

public record RemoteObserverPacket(RemoteObserverBlockEntity.ActiveType activeType, int observingRange, BlockPos beBlockPos, @Nullable EntityType<?> entityType) implements MSPacket.PlayToServer
{
	public static final ResourceLocation ID = Minestuck.id("remote_observer");
	
	@Override
	public ResourceLocation id()
	{
		return ID;
	}
	
	@Override
	public void write(FriendlyByteBuf buffer)
	{
		buffer.writeEnum(activeType);
		buffer.writeInt(observingRange);
		buffer.writeBlockPos(beBlockPos);
		if(entityType != null)
		{
			buffer.writeUtf(EntityType.getKey(entityType).toString());
		} else
			buffer.writeUtf(EntityType.getKey(EntityType.PLAYER).toString());
		
	}
	
	public static RemoteObserverPacket read(FriendlyByteBuf buffer)
	{
		RemoteObserverBlockEntity.ActiveType activeType = buffer.readEnum(RemoteObserverBlockEntity.ActiveType.class);
		int observingRange = buffer.readInt();
		BlockPos beBlockPos = buffer.readBlockPos();
		Optional<EntityType<?>> attemptedEntityType = EntityType.byString(buffer.readUtf());
		
		return new RemoteObserverPacket(activeType, observingRange, beBlockPos, attemptedEntityType.orElse(null));
	}
	
	@Override
	public void execute(ServerPlayer player)
	{
		if(!RemoteObserverBlock.canInteract(player))
			return;
		
		MSPacket.getAccessibleBlockEntity(player, this.beBlockPos, RemoteObserverBlockEntity.class).ifPresent(observerBE ->
		{
			observerBE.setActiveType(activeType);
			if(entityType != null)
				observerBE.setCurrentEntityType(entityType);
			observerBE.setObservingRange(observingRange);
			//Imitates the structure block to ensure that changes are sent client-side
			observerBE.setChanged();
			BlockState state = player.level().getBlockState(beBlockPos);
			player.level().sendBlockUpdated(beBlockPos, state, state, 3);
		});
	}
}