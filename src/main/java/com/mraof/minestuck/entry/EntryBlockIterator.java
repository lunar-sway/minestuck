package com.mraof.minestuck.entry;

import com.google.common.collect.AbstractIterator;
import net.minecraft.core.BlockPos;

import javax.annotation.Nullable;

public final class EntryBlockIterator
{
	public static Iterable<BlockPos.MutableBlockPos> get(int centerX, int centerY, int centerZ, int range)
	{
		return () -> new AbstractIterator<>()
		{
			private final BlockPos.MutableBlockPos pos = new BlockPos.MutableBlockPos();
			private int xOffset;
			private int zOffset;
			private int yOffset;
			{
				updateXOffset(-range);
			}
			
			@Nullable
			@Override
			protected BlockPos.MutableBlockPos computeNext()
			{
				if(xOffset > range)
					return endOfData();
				
				pos.set(centerX + xOffset, centerY + yOffset, centerZ + zOffset);
				
				updateOffsets();
				
				return pos;
			}
			
			private void updateOffsets()
			{
				if(yOffset <= yReachAtXZ(range, xOffset, zOffset))
					yOffset++;
				else if(zOffset <= zReachAtX(range, xOffset))
					updateZOffset(zOffset + 1);
				else
					updateXOffset(xOffset + 1);
			}
			
			private void updateXOffset(int x)
			{
				xOffset = x;
				if(xOffset <= range)
					updateZOffset(-zReachAtX(range, xOffset));
			}
			
			private void updateZOffset(int z)
			{
				zOffset = z;
				yOffset = -yReachAtXZ(range, xOffset, zOffset);
			}
		};
	}
	
	public static Iterable<BlockPos.MutableBlockPos> getHorizontal(int centerX, int centerY, int centerZ, int range)
	{
		return () -> new AbstractIterator<>()
		{
			private final BlockPos.MutableBlockPos pos = new BlockPos.MutableBlockPos().setY(centerY);
			private int xOffset;
			private int zOffset;
			{
				updateXOffset(-range);
			}
			
			@Nullable
			@Override
			protected BlockPos.MutableBlockPos computeNext()
			{
				if(xOffset > range)
					return endOfData();
				
				pos.setX(centerX + xOffset);
				pos.setZ(centerZ + zOffset);
				
				updateOffsets();
				
				return pos;
			}
			
			private void updateOffsets()
			{
				if(zOffset <= zReachAtX(range, xOffset))
					zOffset++;
				else
					updateXOffset(xOffset + 1);
			}
			
			private void updateXOffset(int x)
			{
				xOffset = x;
				if(xOffset <= range)
					zOffset = -zReachAtX(range, xOffset);
			}
			
		};
	}
	
	private static int zReachAtX(int range, int xOffset)
	{
		return (int) Math.sqrt((range + 0.5) * (range + 0.5) - xOffset * xOffset);
	}
	
	private static int yReachAtXZ(int range, int xOffset, int zOffset)
	{
		return (int) Math.sqrt((range + 0.5) * (range + 0.5) - ((xOffset * xOffset + zOffset * zOffset) / 2F));
	}
	
	public static int yReach(BlockPos pos, int range, int centerX, int centerZ)
	{
		return yReachAtXZ(range, pos.getX() - centerX, pos.getZ() - centerZ);
	}
}
