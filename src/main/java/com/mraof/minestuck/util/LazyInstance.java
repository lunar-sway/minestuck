package com.mraof.minestuck.util;

import java.util.function.Supplier;

public class LazyInstance<T>
{
	private final Supplier<T> supplier;
	private T item = null;
	
	public LazyInstance(Supplier<T> supplier)
	{
		this.supplier = supplier;
	}
	
	public T get()
	{
		if(item == null)
			item = supplier.get();
		return item;
	}
	
	public void invalidate()
	{
		item = null;
	}
}