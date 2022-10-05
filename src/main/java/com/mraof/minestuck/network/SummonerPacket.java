package com.mraof.minestuck.network;

import com.mraof.minestuck.block.redstone.SummonerBlock;
import com.mraof.minestuck.blockentity.redstone.SummonerBlockEntity;
import com.mraof.minestuck.entity.MSEntityTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;

import javax.annotation.Nullable;
import java.util.Optional;

public class SummonerPacket implements PlayToServerPacket
{
	private final boolean isUntriggerable;
	private final int summonRange;
	private final BlockPos beBlockPos;
	private final EntityType<?> entityType;
	
	public SummonerPacket(boolean isUntriggerable, int summonRange, BlockPos beBlockPos, @Nullable EntityType<?> entityType)
	{
		this.isUntriggerable = isUntriggerable;
		this.summonRange = summonRange;
		this.beBlockPos = beBlockPos;
		this.entityType = entityType;
	}
	
	@Override
	public void encode(FriendlyByteBuf buffer)
	{
		buffer.writeBoolean(isUntriggerable);
		buffer.writeInt(summonRange);
		buffer.writeBlockPos(beBlockPos);
		if(entityType != null)
		{
			buffer.writeUtf(EntityType.getKey(entityType).toString());
		} else
			buffer.writeUtf(EntityType.getKey(MSEntityTypes.IMP.get()).toString());
		
	}
	
	public static SummonerPacket decode(FriendlyByteBuf buffer)
	{
		boolean isUntriggerable = buffer.readBoolean();
		int summonRange = buffer.readInt();
		BlockPos beBlockPos = buffer.readBlockPos();
		Optional<EntityType<?>> attemptedEntityType = EntityType.byString(buffer.readUtf());
		
		return new SummonerPacket(isUntriggerable, summonRange, beBlockPos, attemptedEntityType.orElse(null));
	}
	
	@Override
	public void execute(ServerPlayer player)
	{
		if(player.level.isAreaLoaded(beBlockPos, 0))
		{
			if(player.level.getBlockEntity(beBlockPos) instanceof SummonerBlockEntity summoner)
			{
				if(Math.sqrt(player.distanceToSqr(beBlockPos.getX() + 0.5, beBlockPos.getY() + 0.5, beBlockPos.getZ() + 0.5)) <= 8)
				{
					if(entityType != null)
						summoner.setSummonedEntity(entityType);
					summoner.setSummonRange(summonRange);
					//Imitates the structure block to ensure that changes are sent client-side
					summoner.setChanged();
					player.level.setBlock(beBlockPos, summoner.getBlockState().setValue(SummonerBlock.UNTRIGGERABLE, isUntriggerable), Block.UPDATE_ALL);
					BlockState state = player.level.getBlockState(beBlockPos);
					player.level.sendBlockUpdated(beBlockPos, state, state, 3);
				}
			}
		}
	}
}