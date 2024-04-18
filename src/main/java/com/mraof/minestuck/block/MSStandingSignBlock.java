package com.mraof.minestuck.block;

import com.mraof.minestuck.blockentity.MSBlockEntityTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.StandingSignBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.SignBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.WoodType;

/**
 * A version of {@link StandingSignBlock} for minestuck signs.
 * To add new sign blocks, they need to use a new block entity type, which is why this class exists.
 */
public class MSStandingSignBlock extends StandingSignBlock
{
	public MSStandingSignBlock(WoodType type, Properties properties)
	{
		super(type, properties);
	}
	
	@Override
	public BlockEntity newBlockEntity(BlockPos pos, BlockState state)
	{
		return new SignBlockEntity(MSBlockEntityTypes.SIGN.get(), pos, state);
	}
}
