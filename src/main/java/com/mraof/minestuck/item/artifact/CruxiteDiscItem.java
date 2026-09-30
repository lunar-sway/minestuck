package com.mraof.minestuck.item.artifact;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.JukeboxBlockEntity;

public class CruxiteDiscItem extends CruxiteArtifactItem
{
	public CruxiteDiscItem(Properties properties)
	{
		super(properties);
	}
	
	@Override
	public InteractionResult useOn(UseOnContext context)
	{
		BlockPos pos = context.getClickedPos();
		Level level = context.getLevel();
		Player player = context.getPlayer();
		if(level.getBlockEntity(pos) instanceof JukeboxBlockEntity && player instanceof ServerPlayer serverPlayer)
		{
			context.getItemInHand().shrink(1);
			onArtifactActivated(serverPlayer);
			return InteractionResult.CONSUME;
		}
		return InteractionResult.sidedSuccess(level.isClientSide);
	}
}
