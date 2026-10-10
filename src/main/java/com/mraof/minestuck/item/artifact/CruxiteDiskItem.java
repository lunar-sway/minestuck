package com.mraof.minestuck.item.artifact;

import com.mraof.minestuck.blockentity.ComputerBlockEntity;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;

public class CruxiteDiskItem extends CruxiteArtifactItem
{
	public CruxiteDiskItem(Properties properties)
	{
		super(properties);
	}
	
	@Override
	public InteractionResult onItemUseFirst(ItemStack stack, UseOnContext context)
	{
		BlockPos pos = context.getClickedPos();
		Level level = context.getLevel();
		Player player = context.getPlayer();
		if(level.getBlockEntity(pos) instanceof ComputerBlockEntity && player instanceof ServerPlayer serverPlayer)
		{
			context.getItemInHand().shrink(1);
			onArtifactActivated(serverPlayer);
			return InteractionResult.CONSUME;
		}
		return InteractionResult.sidedSuccess(level.isClientSide);
	}
}
