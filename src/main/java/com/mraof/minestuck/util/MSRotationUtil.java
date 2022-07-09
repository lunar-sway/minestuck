package com.mraof.minestuck.util;

import net.minecraft.util.Direction;
import net.minecraft.util.Rotation;

public final class MSRotationUtil
{
	public static Rotation fromDirection(Direction direction)
	{
		return rotationBetween(Direction.NORTH, direction);
	}
	
	public static Rotation rotationBetween(Direction from, Direction to)
	{
		if (from.getAxis() == Direction.Axis.Y || to.getAxis() == Direction.Axis.Y)
			throw new IllegalArgumentException("Only horizontal directions are allowed");
			else if (from == to)
				return Rotation.NONE;
			else if (from.getClockWise() == to)
				return Rotation.CLOCKWISE_90;
		else if (from.getCounterClockWise() == to)
			return Rotation.COUNTERCLOCKWISE_90;
		else
			return Rotation.CLOCKWISE_180;
	}
}