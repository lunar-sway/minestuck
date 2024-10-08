package com.mraof.minestuck.network.block;

import com.mraof.minestuck.Minestuck;
import com.mraof.minestuck.block.redstone.BlockTeleporterBlock;
import com.mraof.minestuck.blockentity.redstone.BlockTeleporterBlockEntity;
import com.mraof.minestuck.network.MSPacket;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;

public record BlockTeleporterSettingsPacket(BlockPos offsetPos, BlockPos beBlockPos) implements MSPacket.PlayToServer
{
	public static final ResourceLocation ID = Minestuck.id("block_teleporter_settings");
	
	@Override
	public ResourceLocation id()
	{
		return ID;
	}
	
	@Override
	public void write(FriendlyByteBuf buffer)
	{
		buffer.writeBlockPos(offsetPos);
		buffer.writeBlockPos(beBlockPos);
	}
	
	public static BlockTeleporterSettingsPacket read(FriendlyByteBuf buffer)
	{
		BlockPos offsetPos = buffer.readBlockPos();
		BlockPos beBlockPos = buffer.readBlockPos();
		
		return new BlockTeleporterSettingsPacket(offsetPos, beBlockPos);
	}
	
	@Override
	public void execute(ServerPlayer player)
	{
		if(!BlockTeleporterBlock.canInteract(player))
			return;
		
		MSPacket.getAccessibleBlockEntity(player, this.beBlockPos, BlockTeleporterBlockEntity.class)
				.ifPresent(blockTeleporter -> blockTeleporter.handleSettingsPacket(this));
	}
}
