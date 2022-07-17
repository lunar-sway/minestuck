package com.mraof.minestuck.network;

import com.mraof.minestuck.block.redstone.SummonerBlock;
import com.mraof.minestuck.entity.MSEntityTypes;
import com.mraof.minestuck.tileentity.redstone.SummonerTileEntity;
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
	private final BlockPos tileBlockPos;
	private final EntityType<?> entityType;
	
	public SummonerPacket(boolean isUntriggerable, int summonRange, BlockPos tileBlockPos, @Nullable EntityType<?> entityType)
	{
		this.isUntriggerable = isUntriggerable;
		this.summonRange = summonRange;
		this.tileBlockPos = tileBlockPos;
		this.entityType = entityType;
	}
	
	@Override
	public void encode(FriendlyByteBuf buffer)
	{
		buffer.writeBoolean(isUntriggerable);
		buffer.writeInt(summonRange);
		buffer.writeBlockPos(tileBlockPos);
		if(entityType != null)
		{
			buffer.writeUtf(EntityType.getKey(entityType).toString());
		} else
			buffer.writeUtf(EntityType.getKey(MSEntityTypes.IMP).toString());
		
	}
	
	public static SummonerPacket decode(FriendlyByteBuf buffer)
	{
		boolean isUntriggerable = buffer.readBoolean();
		int summonRange = buffer.readInt();
		BlockPos tileBlockPos = buffer.readBlockPos();
		Optional<EntityType<?>> attemptedEntityType = EntityType.byString(buffer.readUtf());
		
		return new SummonerPacket(isUntriggerable, summonRange, tileBlockPos, attemptedEntityType.orElse(null));
	}
	
	@Override
	public void execute(ServerPlayer player)
	{
		if(player.level.isAreaLoaded(tileBlockPos, 0))
		{
			if(player.level.getBlockEntity(tileBlockPos) instanceof SummonerTileEntity summoner)
			{
				if(Math.sqrt(player.distanceToSqr(tileBlockPos.getX() + 0.5, tileBlockPos.getY() + 0.5, tileBlockPos.getZ() + 0.5)) <= 8)
				{
					if(entityType != null)
						summoner.setSummonedEntity(entityType);
					summoner.setSummonRange(summonRange);
					//Imitates the structure block to ensure that changes are sent client-side
					summoner.setChanged();
					player.level.setBlock(tileBlockPos, summoner.getBlockState().setValue(SummonerBlock.UNTRIGGERABLE, isUntriggerable), Block.UPDATE_ALL);
					BlockState state = player.level.getBlockState(tileBlockPos);
					player.level.sendBlockUpdated(tileBlockPos, state, state, 3);
				}
			}
		}
	}
}