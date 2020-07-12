package com.mraof.minestuck.item.crafting.alchemy;

public enum CombinationMode
{
	AND("and"),
	OR("or");
	
	final String name;
	
	CombinationMode(String name)
	{
		this.name = name;
	}
	
	public String asString()
	{
		return name;
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