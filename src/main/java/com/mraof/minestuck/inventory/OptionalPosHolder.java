package com.mraof.minestuck.inventory;

import net.minecraft.util.IIntArray;
import net.minecraft.util.IntArray;
import net.minecraft.util.math.BlockPos;

import javax.annotation.Nullable;
import java.util.Optional;
import java.util.function.Supplier;

/**
 * A helper object for transferring an optional block position through a {@link IIntArray}.
 * Because numbers that are sent using an {@link IIntArray} are converted to a short at a certain point,
 * each coordinate is split into two numbers that each should fit in a short.
 * This workaround is only here to be able to use the synchronization feature built into containers,
 * but this could be replaced by writing a custom synchronization procedure.
 */
public class OptionalPosHolder
{
	private final IIntArray intArray;
	
	public static OptionalPosHolder dummy(@Nullable BlockPos pos)
	{
		IIntArray intArray = new IntArray(7);
		if (pos != null)
		{
			intArray.set(0, 1);
			intArray.set(1, pos.getX() >> 16);
			intArray.set(2, pos.getX() & 0xFFFF);
			intArray.set(3, pos.getY() >> 16);
			intArray.set(4, pos.getY() & 0xFFFF);
			intArray.set(5, pos.getZ() >> 16);
			intArray.set(6, pos.getZ() & 0xFFFF);
		}
		return new OptionalPosHolder(intArray);
	}
	
	public static OptionalPosHolder forPos(Supplier<Optional<BlockPos>> getter)
	{
		return new OptionalPosHolder(new IIntArray()
		{
			@Override
			public int get(int index)
			{
				Optional<BlockPos> optionalPos = getter.get();
				return optionalPos.map(pos -> {
					switch(index)
					{
						case 0: return 1;
						case 1: return pos.getX() >> 16;
						case 2: return pos.getX() & 0xFFFF;
						case 3: return pos.getY() >> 16;
						case 4: return pos.getY() & 0xFFFF;
						case 5: return pos.getZ() >> 16;
						case 6: return pos.getZ() & 0xFFFF;
						default: return 0;
					}
				}).orElse(0);
			}
			
			@Override
			public void set(int index, int value)
			{
				throw new UnsupportedOperationException();	// Takes extra effort to do right, and it's unlikely to see use.
			}
			
			@Override
			public int getCount()
			{
				return 7;
			}
		});
	}
	
	private OptionalPosHolder(IIntArray intArray)
	{
		this.intArray = intArray;
	}
	
	public Optional<BlockPos> getBlockPos()
	{
		if(intArray.get(0) == 0)
			return Optional.empty();
		
		int x = intArray.get(1) << 16 | intArray.get(2);
		int y = intArray.get(3) << 16 | intArray.get(4);
		int z = intArray.get(5) << 16 | intArray.get(6);
		
		return Optional.of(new BlockPos(x, y, z));
	}
	
	public IIntArray getIntArray()
	{
		return intArray;
	}
}
