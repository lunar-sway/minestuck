package com.mraof.minestuck.block;

import com.mojang.serialization.MapCodec;
import com.mraof.minestuck.util.MSTags;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.item.FallingBlockEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.FallingBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;

/**
 * Can be right clicked on one of its horizontal faces in order to push it in that direction, it will only destroy replaceables in its path.
 * It can be affected by gravity and can be lifted upwards by an item magnet
 */
public class PushableBlock extends FallingBlock
{
	public final Maneuverability maneuverability;
	
	public enum Maneuverability
	{
		PUSH,
		PULL,
		PUSH_AND_PULL //direction is determined by shift clicking, with shift clicking resulting in a pull
	}
	
	protected PushableBlock(Properties properties, Maneuverability maneuverability)
	{
		super(properties);
		this.maneuverability = maneuverability;
	}
	
	@Override
	protected MapCodec<PushableBlock> codec()
	{
		return null; //todo
	}
	
	@Override
	@SuppressWarnings("deprecation")
	public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit)
	{
		boolean willPush = ((!player.isShiftKeyDown() && maneuverability == Maneuverability.PUSH_AND_PULL) || maneuverability == Maneuverability.PUSH)||
				!((player.isShiftKeyDown() && maneuverability == Maneuverability.PUSH_AND_PULL) || maneuverability == Maneuverability.PULL);
		Direction direction = willPush ? hit.getDirection().getOpposite() : hit.getDirection();
		if((direction.getAxis() == Direction.Axis.X || direction.getAxis() == Direction.Axis.Z) && isReplaceable(level.getBlockState(pos.relative(direction))))
		{
			if(!level.isClientSide)
			{
				level.playSound(null, pos, SoundEvents.GRINDSTONE_USE, SoundSource.BLOCKS, 0.6F, 1.3F);
				level.removeBlock(pos, false);
				level.destroyBlock(pos.relative(direction), true);
				level.setBlock(pos.relative(direction), state, Block.UPDATE_ALL);
			}
			
			return InteractionResult.sidedSuccess(level.isClientSide);
		}
		
		return InteractionResult.PASS;
	}
	
	@Override
	protected int getDelayAfterPlace()
	{
		return 0;
	}
	
	@Override
	public void onLand(Level level, BlockPos pos, BlockState fallingBlockState, BlockState replacedState, FallingBlockEntity fallingBlockEntity)
	{
		level.playSound(null, pos, SoundEvents.GILDED_BLACKSTONE_STEP, SoundSource.BLOCKS, 0.5F, 0.3F);
	}
	
	public static boolean isReplaceable(BlockState state)
	{
		return isFree(state) || state.is(MSTags.Blocks.PUSHABLE_BLOCK_REPLACEABLE);
	}
}