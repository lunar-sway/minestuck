package com.mraof.minestuck.api.alchemy.recipe.combination;

import java.util.Locale;

public enum CombinationMode
{
	AND,
	OR;
	
	public String asString()
	{
		return this.name().toLowerCase(Locale.ROOT);
	}
	
	public boolean asBoolean()
	{
		return this == AND;
	}
	
	public static CombinationMode fromBoolean(boolean b)
	{
		return b ? AND : OR;
	}
	
	public static CombinationMode fromString(String str)
	{
		if(str.equals("&&") || str.equalsIgnoreCase("and"))
			return AND;
		else if(str.equals("||") || str.equalsIgnoreCase("or"))
			return OR;
		else throw new IllegalArgumentException(str+" is not a valid combination type");
	}
}