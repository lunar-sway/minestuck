package com.mraof.minestuck.block.plant.aspect;

import com.mraof.minestuck.world.gen.feature.tree.FrostTree;
import com.mraof.minestuck.world.gen.feature.tree.aspect.TimeAspectTree;
import net.minecraft.core.BlockPos;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.SaplingBlock;
import net.minecraft.world.level.block.state.BlockState;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
public class TimeAspectSaplingBlock extends SaplingBlock
{
	public TimeAspectSaplingBlock(Properties pProperties)
	{
		super(new TimeAspectTree(), pProperties);
	}

}
