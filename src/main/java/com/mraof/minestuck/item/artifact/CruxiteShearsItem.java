package com.mraof.minestuck.item.artifact;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.common.IShearable;

/**
 * Activates when shearing an entity or using to cut plants
 */
public class CruxiteShearsItem extends CruxiteArtifactItem
{
	public CruxiteShearsItem(Properties properties)
	{
		super(properties);
	}
	
	@Override
	public InteractionResult interactLivingEntity(ItemStack stack, Player player, LivingEntity entity, InteractionHand usedHand)
	{
		if(entity instanceof IShearable)
		{
			stack.shrink(1);
			if(player instanceof ServerPlayer serverPlayer)
				onArtifactActivated(serverPlayer);
			return InteractionResult.CONSUME;
		}
		return InteractionResult.PASS;
	}
	
	@Override
	public boolean mineBlock(ItemStack stack, Level level, BlockState state, BlockPos pos, LivingEntity miningEntity)
	{
		// What do you mean there is no mineable with shears?
		if(state.is(BlockTags.SWORD_EFFICIENT))
		{
			stack.shrink(1);
			if(miningEntity instanceof ServerPlayer serverPlayer)
				onArtifactActivated(serverPlayer);
		}
		return super.mineBlock(stack, level, state, pos, miningEntity);
	}
}
