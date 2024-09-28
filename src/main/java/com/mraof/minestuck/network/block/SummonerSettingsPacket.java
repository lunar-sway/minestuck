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

import javax.annotation.Nullable;
import java.util.Optional;

public record SummonerSettingsPacket(boolean isUntriggerable, int summonRange, @Nullable EntityType<?> entityType, BlockPos beBlockPos) implements MSPacket.PlayToServer
{
	public static final ResourceLocation ID = Minestuck.id("summoner_settings");
	
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
	
	public static SummonerSettingsPacket read(FriendlyByteBuf buffer)
	{
		boolean isUntriggerable = buffer.readBoolean();
		int summonRange = buffer.readInt();
		BlockPos beBlockPos = buffer.readBlockPos();
		Optional<EntityType<?>> attemptedEntityType = EntityType.byString(buffer.readUtf());
		
		return new SummonerSettingsPacket(isUntriggerable, summonRange, attemptedEntityType.orElse(null), beBlockPos);
	}
	
	@Override
	public void execute(ServerPlayer player)
	{
		if(!SummonerBlock.canInteract(player))
			return;
		
		MSPacket.getAccessibleBlockEntity(player, this.beBlockPos, SummonerBlockEntity.class)
				.ifPresent(summoner -> summoner.handleSettingsPacket(this));
	}
}
