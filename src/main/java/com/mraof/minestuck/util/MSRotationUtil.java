package com.mraof.minestuck.util;

import net.minecraft.util.Direction;
import net.minecraft.util.Rotation;

public final class MSRotationUtil
{
	public static Rotation fromDirection(Direction direction)
	{
		switch(direction)
		{
			case NORTH:
				return Rotation.NONE;
			case EAST:
				return Rotation.CLOCKWISE_90;
			case SOUTH:
				return Rotation.CLOCKWISE_180;
			case WEST:
				return Rotation.COUNTERCLOCKWISE_90;
			default:
				throw new IllegalArgumentException("Only horizontal directions are allowed");
		}
	}
}