package com.mraof.minestuck.computer.editmode;

import com.mraof.minestuck.network.editmode.EditmodeDragPackets;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.state.BlockState;

import java.util.ArrayList;
import java.util.List;

/**
 * Client-only snapshot of the selection's block states. Used for preview/transition rendering;
 */
public final class ClientSelectionCache
{
	public record Entry(BlockPos localOffset, BlockState state) {}
	
	private static List<Entry> entries = List.of();
	private static int sizeX = 1, sizeY = 1, sizeZ = 1;
	
	public static void capture(Level level, BlockPos min, BlockPos max)
	{
		List<Entry> list = new ArrayList<>();
		for(BlockPos pos : BlockPos.betweenClosed(min, max))
		{
			BlockState state = level.getBlockState(pos);
			if(!state.isAir())
				list.add(new Entry(pos.subtract(min), state));
		}
		entries = list;
		sizeX = max.getX() - min.getX() + 1;
		sizeY = max.getY() - min.getY() + 1;
		sizeZ = max.getZ() - min.getZ() + 1;
	}
	
	public static void clear()
	{
		entries = List.of();
		sizeX = 1;
		sizeY = 1;
		sizeZ = 1;
	}
	
	public static void applyRotationAsBase(Rotation rotation)
	{
		if(rotation == Rotation.NONE || entries.isEmpty())
			return;
		
		List<Entry> rotated = new ArrayList<>(entries.size());
		for(Entry e : entries)
		{
			BlockPos newOffset = EditmodeDragPackets.rotateOffset(e.localOffset(), sizeX, sizeZ, rotation);
			BlockState newState = e.state().rotate(rotation);
			rotated.add(new Entry(newOffset, newState));
		}
		
		boolean swapXZ = rotation == Rotation.CLOCKWISE_90 || rotation == Rotation.COUNTERCLOCKWISE_90;
		int newSizeX = swapXZ ? sizeZ : sizeX;
		int newSizeZ = swapXZ ? sizeX : sizeZ;
		
		entries = rotated;
		sizeX = newSizeX;
		sizeZ = newSizeZ;
	}
	
	public static List<Entry> getEntries() { return entries; }
	public static int getSizeX() { return sizeX; }
	public static int getSizeY() { return sizeY; }
	public static int getSizeZ() { return sizeZ; }
}