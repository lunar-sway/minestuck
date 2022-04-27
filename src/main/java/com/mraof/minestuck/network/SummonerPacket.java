package com.mraof.minestuck.network;

import com.mraof.minestuck.block.redstone.SummonerBlock;
import com.mraof.minestuck.entity.MSEntityTypes;
import com.mraof.minestuck.tileentity.redstone.SummonerTileEntity;
import net.minecraft.block.BlockState;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.common.util.Constants;

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
	public void encode(PacketBuffer buffer)
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
	
	public static SummonerPacket decode(PacketBuffer buffer)
	{
		boolean isUntriggerable = buffer.readBoolean();
		int summonRange = buffer.readInt();
		BlockPos tileBlockPos = buffer.readBlockPos();
		Optional<EntityType<?>> attemptedEntityType = EntityType.byString(buffer.readUtf());
		
		return new SummonerPacket(isUntriggerable, summonRange, tileBlockPos, attemptedEntityType.orElse(null));
	}
	
	@Override
	public void execute(ServerPlayerEntity player)
	{
		if(player.level.isAreaLoaded(tileBlockPos, 0))
		{
			TileEntity te = player.level.getBlockEntity(tileBlockPos);
			if(te instanceof SummonerTileEntity)
			{
				if(Math.sqrt(player.distanceToSqr(tileBlockPos.getX() + 0.5, tileBlockPos.getY() + 0.5, tileBlockPos.getZ() + 0.5)) <= 8)
				{
					if(entityType != null)
						((SummonerTileEntity) te).setSummonedEntity(entityType, null);
					((SummonerTileEntity) te).setSummonRange(summonRange);
					//Imitates the structure block to ensure that changes are sent client-side
					te.setChanged();
					player.level.setBlock(tileBlockPos, te.getBlockState().setValue(SummonerBlock.UNTRIGGERABLE, isUntriggerable), Constants.BlockFlags.DEFAULT);
					BlockState state = player.level.getBlockState(tileBlockPos);
					player.level.sendBlockUpdated(tileBlockPos, state, state, 3);
				}
			}
		}
	}
}