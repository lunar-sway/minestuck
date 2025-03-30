package com.mraof.minestuck.api.alchemy.recipe.combination;

import com.mojang.serialization.Codec;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.util.StringRepresentable;

import java.util.Locale;

@MethodsReturnNonnullByDefault
public enum CombinationMode implements StringRepresentable
{
	AND,
	OR;
	
	public static final Codec<CombinationMode> CODEC = StringRepresentable.fromEnumWithMapping(CombinationMode::values, CombinationMode::mapSymbols);
	
	@Override
	public String getSerializedName()
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
	
	private static String mapSymbols(String str)
	{
		if(str.equals("&&"))
			return AND.getSerializedName();
		else if(str.equals("||"))
			return OR.getSerializedName();
		else
			return str.toLowerCase(Locale.ROOT);
	}
}