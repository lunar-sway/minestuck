package com.mraof.minestuck.player;

import com.mojang.serialization.Codec;
import net.minecraft.network.chat.Component;
import net.minecraft.util.StringRepresentable;

import java.util.Locale;
import java.util.stream.Stream;

/**
 * This class represents the 14 title-classes that exists in sburb,
 * (including lord and muse, but they are by default not generated in the <code>getRandomClass()</code> method,
 * thought they can be included by setting the parameter <code>includeSpecial</code> to <code>true</code>)
 * The <code>toString()</code> method is overridden and returns a lower-cased version of the title-class name.
 * @author kirderf1
 */
public enum EnumClass implements StringRepresentable
{
	BARD(true), HEIR(true), KNIGHT(true),
	MAGE(true), MAID(true), PAGE(true),
	PRINCE(true), ROGUE(true), SEER(true),
	SYLPH(true), THIEF(true), WITCH(true),
	LORD(false), MUSE(false);	//Special title-classes
	
	private final boolean usedInGeneration;
	
	EnumClass(boolean usedInGeneration)
	{
		this.usedInGeneration = usedInGeneration;
	}
	
	public static final Codec<EnumClass> CODEC = StringRepresentable.fromEnum(EnumClass::values);
	
	/**
	 * Used to get a title-class based on index. Used when reading a title-class from nbt.
	 * The index value is matched with the return of <code>getIntFromClass</code>
	 * @param i The index number.
	 * @return the title-class
	 */
	public static EnumClass getClassFromInt(int i)
	{
		if(i < 0 || i >= EnumClass.values().length)
			return null;
		return EnumClass.values()[i];
	}
	
	/**
	 * Gives the index for the class. Used when writing a title-class to nbt.
	 * When reading a title-class from nbt, <code>getClassFromInt</code> should be used to return the index to a title-class.
	 * @param e The title-class to convert.
	 * @return an index number that matches this title-class
	 */
	public static int getIntFromClass(EnumClass e)
	{
		return e.ordinal();
	}
	
	public static EnumClass fromString(String string)
	{
		for(EnumClass c : values())
		{
			if(c.getSerializedName().equalsIgnoreCase(string))
				return c;
		}
		return null;
	}
	
	/**
	 * Takes the enum name for this title-class and returns a lowercase version.
	 * @return the name of this title-class
	 */
	@Override
	public String getSerializedName()
	{
		return this.name().toLowerCase(Locale.ROOT);
	}
	
	@Override
	public String toString()
	{
		return this.getSerializedName();
	}
	
	/**
	 * Creates a text component for this title-class that will be translated client-side.
	 * Used for messages from the mod that for example will be sent trough chat.
	 * @return a text component that will translate into the name of the title-class
	 */
	public Component asTextComponent()
	{
		return Component.translatable(getTranslationKey());
	}
	
	public String getTranslationKey()
	{
		return "title.class." + this.getSerializedName();
	}
	
	public boolean isUsedInGeneration()
	{
		return usedInGeneration;
	}
	
	public static Stream<EnumClass> valuesStream()
	{
		return Stream.of(values()).filter(EnumClass::isUsedInGeneration);
	}
}
