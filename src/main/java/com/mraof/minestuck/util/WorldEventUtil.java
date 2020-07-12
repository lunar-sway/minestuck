package com.mraof.minestuck.util;

import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorld;
import net.minecraftforge.common.util.Constants;

public class WorldEventUtil
{
	public static void dispenserEffect(IWorld world, BlockPos pos, Direction direction, boolean success)
	{
		world.playEvent(success ? Constants.WorldEvents.DISPENSER_DISPENSE_SOUND : Constants.WorldEvents.DISPENSER_FAIL_SOUND, pos, 0);
		if(success)
		{
			int i = direction.getXOffset() + 1 + (direction.getZOffset() + 1) * 3;
			world.playEvent(Constants.WorldEvents.DISPENSER_SMOKE, pos, i);
		}
	}
}