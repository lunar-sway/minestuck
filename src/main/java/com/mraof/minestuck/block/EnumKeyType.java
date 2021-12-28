package com.mraof.minestuck.block;

import net.minecraft.util.IStringSerializable;

public enum EnumKeyType implements IStringSerializable
{
	none,
	tier_1_key,
	tier_2_key;
	
	public String getNameNoSpaces()
	{
		return name().replace('_', ' ');
	}
	
	public static EnumKeyType fromString(String str) //converts string back into enum, used primarily in Dungeon Interface block/tile entity
	{
		for(EnumKeyType type : EnumKeyType.values())
		{
			if(type.toString().equals(str))
				return type;
		}
		return null;
	}
	
	public static EnumKeyType fromInt(int ordinal) //converts int back into enum, used primarily in KeyItem
	{
		for(EnumKeyType type : EnumKeyType.values())
		{
			if(type.ordinal() == ordinal)
				return type;
		}
		return null;
	}
	
	@Override
	public String getSerializedName()
	{
		return name().toLowerCase();
	}
}
