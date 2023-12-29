package com.mraof.minestuck.world.biome;

import com.mojang.serialization.Codec;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.util.StringRepresentable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

@MethodsReturnNonnullByDefault
public enum LandBiomeType implements StringRepresentable
{
	NORMAL, ROUGH, OCEAN;
	
	public static final Codec<LandBiomeType> CODEC = StringRepresentable.fromEnum(LandBiomeType::values);
	
	@Override
	public String getSerializedName()
	{
		return this.name().toLowerCase(Locale.ROOT);
	}
	
	public static LandBiomeType[] any()
	{
		return values();
	}
	
	public static LandBiomeType[] anyExcept(LandBiomeType... excepted)
	{
		List<LandBiomeType> types = new ArrayList<>(Arrays.asList(LandBiomeType.values()));
		types.removeAll(Arrays.asList(excepted));
		return types.toArray(new LandBiomeType[0]);
	}
}