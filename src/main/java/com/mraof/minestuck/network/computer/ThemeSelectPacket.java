package com.mraof.minestuck.network.computer;

import com.mraof.minestuck.blockentity.ComputerBlockEntity;
import com.mraof.minestuck.computer.Theme;
import com.mraof.minestuck.network.PlayToServerPacket;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;

public class ThemeSelectPacket implements PlayToServerPacket
{
	private final BlockPos pos;
	private final int themeId;
	
	public ThemeSelectPacket(BlockPos pos, int themeId)
	{
		this.pos = pos;
		this.themeId = themeId;
	}
	
	public static ThemeSelectPacket create(ComputerBlockEntity be, Theme theme)
	{
		return new ThemeSelectPacket(be.getBlockPos(), theme.ordinal());
	}
	
	@Override
	public void execute(ServerPlayer player)
	{
		ComputerBlockEntity.forNetworkIfPresent(player, pos, computer -> computer.setTheme(Theme.values()[themeId]));
	}
	
	@Override
	public void encode(FriendlyByteBuf buffer)
	{
		buffer.writeBlockPos(pos);
		buffer.writeInt(themeId);
	}
	
	public static ThemeSelectPacket decode(FriendlyByteBuf buffer)
	{
		BlockPos computer = buffer.readBlockPos();
		int themeId = buffer.readInt();
		
		return new ThemeSelectPacket(computer, themeId);
	}
	
}
