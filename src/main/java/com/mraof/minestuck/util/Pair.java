package com.mraof.minestuck.util;

/**
 * Container for a pair of objects. If this implementation already exists in the standard libraries,
 * switch usages of this class to that one instead
 * @param <A> the first type
 * @param <B> the second type
 */
public class Pair<A, B>
{
	public A object1;
	public B object2;
	
	public Pair(A a, B b)
	{
		object1 = a;
		object2 = b;
	}
}
