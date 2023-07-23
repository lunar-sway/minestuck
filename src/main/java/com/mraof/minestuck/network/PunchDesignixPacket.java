package com.mraof.minestuck.network;

import com.mraof.minestuck.blockentity.machine.PunchDesignixBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;

public class PunchDesignixPacket implements PlayToServerPacket
{
	private final BlockPos pos;
	private final String captcha;
	
	public PunchDesignixPacket(BlockPos pos, String captcha)
	{
		this.pos = pos;
		this.captcha = captcha;
	}
	
	@Override
	public void encode(FriendlyByteBuf buffer)
	{
		buffer.writeBlockPos(pos);
		buffer.writeUtf(captcha, 8);
	}
	
	public static PunchDesignixPacket decode(FriendlyByteBuf buffer)
	{
		BlockPos pos = buffer.readBlockPos();
		String captcha = buffer.readUtf(8);
		
		return new PunchDesignixPacket(pos, captcha);
	}
	
	@Override
	public void execute(ServerPlayer player)
	{
		if(player.getCommandSenderWorld().isAreaLoaded(pos, 0))
		{
			if(player.level().getBlockEntity(pos) instanceof PunchDesignixBlockEntity punchDesignix)
			{
				if(Math.sqrt(player.distanceToSqr(pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5)) <= 8)
				{
					punchDesignix.setCaptcha(captcha);
					punchDesignix.punchCard(player);
				}
			}
		}
	}
}