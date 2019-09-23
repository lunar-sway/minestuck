package com.mraof.minestuck.jei;

import com.mraof.minestuck.item.crafting.alchemy.GristSet;

import java.util.Objects;

public abstract class JeiGristCost
{
	public abstract Type getType();
	
	public abstract GristSet getGristSet();
	
	public abstract int getWildcardAmount();
	
	public static class Set extends JeiGristCost
	{
		private final GristSet set;
		
		public Set(GristSet set)
		{
			this.set = Objects.requireNonNull(set);
		}
		
		@Override
		public Type getType()
		{
			return Type.GRIST_SET;
		}
		
		@Override
		public GristSet getGristSet()
		{
			return set;
		}
		
		@Override
		public int getWildcardAmount()
		{
			throw new UnsupportedOperationException();
		}
	}
	
	public static class Wildcard extends JeiGristCost
	{
		private final int wildcard;
		
		public Wildcard(int wildcard)
		{
			this.wildcard = wildcard;
		}
		
		@Override
		public Type getType()
		{
			return Type.WILDCARD;
		}
		
		@Override
		public GristSet getGristSet()
		{
			throw new UnsupportedOperationException();
		}
		
		@Override
		public int getWildcardAmount()
		{
			return wildcard;
		}
	}
	
	public enum Type
	{
		GRIST_SET,
		WILDCARD
	}
}