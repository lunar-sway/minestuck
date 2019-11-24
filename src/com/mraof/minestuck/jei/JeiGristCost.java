package com.mraof.minestuck.jei;

import com.mraof.minestuck.item.crafting.alchemy.GristSet;

import java.util.Objects;

public abstract class JeiGristCost
{
	public abstract Type getType();
	
	public abstract GristSet getGristSet();
	
	public abstract long getWildcardAmount();
	
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
		public long getWildcardAmount()
		{
			throw new UnsupportedOperationException();
		}
	}
	
	public static class Wildcard extends JeiGristCost
	{
		private final long wildcard;
		
		public Wildcard(long wildcard)
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
		public long getWildcardAmount()
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