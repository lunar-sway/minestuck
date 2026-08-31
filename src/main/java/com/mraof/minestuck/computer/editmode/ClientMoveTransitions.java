package com.mraof.minestuck.computer.editmode;

import net.minecraft.core.BlockPos;

import java.util.ArrayList;
import java.util.List;

public final class ClientMoveTransitions
{
	public record Transition(BlockPos from, BlockPos to, long startTime) {}
	
	private static final long DURATION_MS = 180;
	private static final List<Transition> active = new ArrayList<>();
	
	public static void start(List<BlockPos> fromPositions, List<BlockPos> toPositions)
	{
		long now = System.currentTimeMillis();
		for(int i = 0; i < fromPositions.size(); i++)
			active.add(new Transition(fromPositions.get(i), toPositions.get(i), now));
	}
	
	public static boolean hasActive()
	{
		purgeExpired();
		return !active.isEmpty();
	}
	
	private static void purgeExpired()
	{
		long now = System.currentTimeMillis();
		active.removeIf(t -> now - t.startTime() > DURATION_MS);
	}
	
	public static List<double[]> getInterpolatedPositions() // [x, y, z] per active transition
	{
		purgeExpired();
		long now = System.currentTimeMillis();
		List<double[]> result = new ArrayList<>();
		for(Transition t : active)
		{
			double progress = Math.min(1.0, (now - t.startTime()) / (double) DURATION_MS);
			double ease = 1 - Math.pow(1 - progress, 3); // ease-out cubic
			result.add(new double[]{
					t.from().getX() + (t.to().getX() - t.from().getX()) * ease,
					t.from().getY() + (t.to().getY() - t.from().getY()) * ease,
					t.from().getZ() + (t.to().getZ() - t.from().getZ()) * ease
			});
		}
		return result;
	}
}