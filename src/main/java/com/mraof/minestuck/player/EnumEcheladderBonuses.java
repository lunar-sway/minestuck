package com.mraof.minestuck.player;

public enum EnumEcheladderBonuses
{
	IMP(10),
	OGRE(120),
	BASILISK(450),
	LICH(1200),
	GICLOPS(2500),
	ALCHEMY_1(30),
	ALCHEMY_2(400),
	ALCHEMY_3(3000);
	private final int bonus;
	
	EnumEcheladderBonuses(int bonus)
	{
		this.bonus = bonus;
	}
	
	public int getBonus()
	{
		return bonus;
	}
	
	@Override
	public String toString()
	{
		return this.name().toLowerCase();
	}
	
	public static EnumEcheladderBonuses fromString(String string)
	{
		for(EnumEcheladderBonuses c : values())
		{
			if(c.toString().equalsIgnoreCase(string))
				return c;
		}
		return null;
	}
}
