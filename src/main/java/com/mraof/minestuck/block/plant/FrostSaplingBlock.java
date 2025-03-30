package com.mraof.minestuck.block.plant;

import com.mraof.minestuck.Minestuck;
import com.mraof.minestuck.world.gen.feature.MSCFeatures;
import net.minecraft.core.BlockPos;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.SaplingBlock;
import net.minecraft.world.level.block.grower.TreeGrower;
import net.minecraft.world.level.block.state.BlockState;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.Optional;

@ParametersAreNonnullByDefault
public class FrostSaplingBlock extends SaplingBlock
{
	public FrostSaplingBlock(Properties pProperties)
	{
		super(new TreeGrower(Minestuck.id("frost").toString(), Optional.empty(), Optional.of(MSCFeatures.FROST_TREE), Optional.empty()), pProperties);
	}
	
	@Override
	protected boolean mayPlaceOn(BlockState state, BlockGetter level, BlockPos pos)
	{
		return state.is(BlockTags.SNOW) || super.mayPlaceOn(state, level, pos);
	}
}
