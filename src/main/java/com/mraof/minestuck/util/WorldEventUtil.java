package com.mraof.minestuck.util;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.LevelEvent;

public class WorldEventUtil
{
	public static void dispenserEffect(LevelAccessor level, BlockPos pos, Direction direction, boolean success)
	{
		level.levelEvent(success ? LevelEvent.SOUND_DISPENSER_DISPENSE : LevelEvent.SOUND_DISPENSER_FAIL, pos, 0);
		if(success)
		{
			int i = direction.getStepX() + 1 + (direction.getStepZ() + 1) * 3;
			level.levelEvent(LevelEvent.PARTICLES_SHOOT_SMOKE, pos, i);
		}
	}
}