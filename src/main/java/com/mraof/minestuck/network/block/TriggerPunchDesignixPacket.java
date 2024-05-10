package com.mraof.minestuck.network.block;

import com.mraof.minestuck.Minestuck;
import com.mraof.minestuck.blockentity.machine.PunchDesignixBlockEntity;
import com.mraof.minestuck.network.MSPacket;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;

public record TriggerPunchDesignixPacket(String captcha, BlockPos pos) implements MSPacket.PlayToServer
{
	public static final ResourceLocation ID = Minestuck.id("trigger_punch_designix");
	
	@Override
	public ResourceLocation id()
	{
		return ID;
	}
	
	@Override
	public void write(FriendlyByteBuf buffer)
	{
		buffer.writeBlockPos(pos);
		buffer.writeUtf(captcha, 8);
	}
	
	public static TriggerPunchDesignixPacket read(FriendlyByteBuf buffer)
	{
		BlockPos pos = buffer.readBlockPos();
		String captcha = buffer.readUtf(8);
		
		return new TriggerPunchDesignixPacket(captcha, pos);
	}
	
	@Override
	public void execute(ServerPlayer player)
	{
		MSPacket.getAccessibleBlockEntity(player, this.pos, PunchDesignixBlockEntity.class).ifPresent(punchDesignix ->
		{
			punchDesignix.setCaptcha(captcha);
			punchDesignix.punchCard(player);
		});
	}
}
