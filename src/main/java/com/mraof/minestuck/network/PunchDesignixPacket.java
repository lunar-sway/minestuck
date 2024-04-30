package com.mraof.minestuck.network;

import com.mraof.minestuck.Minestuck;
import com.mraof.minestuck.blockentity.machine.PunchDesignixBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;

public record PunchDesignixPacket(BlockPos pos, String captcha) implements MSPacket.PlayToServer
{
	public static final ResourceLocation ID = Minestuck.id("punch_designix");
	
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
	
	public static PunchDesignixPacket read(FriendlyByteBuf buffer)
	{
		BlockPos pos = buffer.readBlockPos();
		String captcha = buffer.readUtf(8);
		
		return new PunchDesignixPacket(pos, captcha);
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