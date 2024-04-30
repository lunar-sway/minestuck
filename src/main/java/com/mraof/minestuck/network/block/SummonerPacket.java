package com.mraof.minestuck.network.block;

import com.mraof.minestuck.Minestuck;
import com.mraof.minestuck.block.redstone.SummonerBlock;
import com.mraof.minestuck.blockentity.redstone.SummonerBlockEntity;
import com.mraof.minestuck.entity.MSEntityTypes;
import com.mraof.minestuck.network.MSPacket;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;

import javax.annotation.Nullable;
import java.util.Optional;

public record SummonerPacket(boolean isUntriggerable, int summonRange, BlockPos beBlockPos, @Nullable EntityType<?> entityType) implements MSPacket.PlayToServer
{
	public static final ResourceLocation ID = Minestuck.id("summoner");
	
	@Override
	public ResourceLocation id()
	{
		return ID;
	}
	
	@Override
	public void write(FriendlyByteBuf buffer)
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
	
	public static SummonerPacket read(FriendlyByteBuf buffer)
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
		MSPacket.getAccessibleBlockEntity(player, this.beBlockPos, SummonerBlockEntity.class).ifPresent(summoner ->
		{
			if(entityType != null)
				summoner.setSummonedEntity(entityType);
			summoner.setSummonRange(summonRange);
			//Imitates the structure block to ensure that changes are sent client-side
			summoner.setChanged();
			player.level().setBlock(beBlockPos, summoner.getBlockState().setValue(SummonerBlock.UNTRIGGERABLE, isUntriggerable), Block.UPDATE_ALL);
			BlockState state = player.level().getBlockState(beBlockPos);
			player.level().sendBlockUpdated(beBlockPos, state, state, 3);
		});
	}
}
