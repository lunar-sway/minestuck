package com.mraof.minestuck.util;

import net.minecraft.client.resources.I18n;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;

import java.util.EnumSet;
import java.util.Random;

/**
 * An aspect version of <code>EnumClass</code> that works pretty much the same way as the <code>EnumClass</code> except
 * that it doesn't have any special types.
 * The <code>toString()</code> method is overridden and returns a better cased version of the aspect name.
 * @author kirderf1
 */
public enum EnumAspect
{
	BLOOD,BREATH,DOOM,HEART,HOPE,LIFE,LIGHT,MIND,RAGE,SPACE,TIME,VOID;
	
	/**
	 * This method generates one of the 12 aspects that is not specified in the
	 * <code>unavailableAspects</code> array. Beware that this method is not compatible with duplicates in the array.
	 * @param unavailableAspects An <code>EnumSet&#60;EnumAspect&#62;</code> that includes the aspects that it won't choose from.
	 * Compatible with the value null.
	 * @return null if <code>unavailableAspects</code> contains 12 or more aspects or
	 * an <code>EnumAspect</code> of the chosen aspect.
	 */
	public static EnumAspect getRandomAspect(EnumSet<EnumAspect> unavailableAspects, Random rand)
	{
		if(unavailableAspects == null)
			unavailableAspects = EnumSet.noneOf(EnumAspect.class);
		if(unavailableAspects.size() == 12)
			return null;	//No aspect available to generate
		int aspectInt = rand.nextInt(12 - unavailableAspects.size());
		EnumAspect[] list = values();
		for(EnumAspect aspect : list)
			if(!unavailableAspects.contains(aspect))
			{
				if(aspectInt == 0)
					return aspect;
				aspectInt--;
			}
		return null;
	}
	
	/**
	 * Used to get a title-aspect based on index. Used when reading a title-aspect from nbt.
	 * The index value is matched with the return of <code>getIntFromAspect</code>
	 * @param i The index number.
	 * @return the title-aspect
	 */
	public static EnumAspect getAspectFromInt(int i)
	{
		if(i < 0 || i >= EnumAspect.values().length)
			return null;
		return EnumAspect.values()[i];
	}
	
	/**
	 * Gives the index for the aspect. Used when writing a title-aspect to nbt.
	 * When reading a title-aspect from nbt, <code>getAspectFromInt</code> should be used to return the index to a title-aspect.
	 * @param e The title-aspect to convert.
	 * @return an index number that matches this title-aspect
	 */
	public static int getIntFromAspect(EnumAspect e)
	{
		return e.ordinal();
	}
	
	/**
	 * Takes the enum name for this title-aspect and returns a lowercase version.
	 * Aside from regular use of the method, it is useful for producing
	 * the unlocalized name of the title-aspect using <code>"title." + titleAspect.toString()</code>
	 * @return the name of this title-aspect
	 */
	@Override
	public String toString()
	{
		return this.name().toLowerCase();
	}
	
	/**
	 * Translates and returns the proper name of this title-aspect. Should only be used client-side.
	 * For usage in messages sent to a player from a server, use <code>asTextComponent()</code>.
	 * For debugging purposes, use <code>toString()</code> instead.
	 * @return a translated string of the name.
	 * @deprecated use {@link #asTextComponent()} instead
	 */
	@Deprecated
	public String getDisplayName()
	{
		return I18n.format(getTranslationKey());
	}
	
	/**
	 * Creates a text component for this title-aspect that will be translated client-side.
	 * Used for messages from the mod that for example will be sent trough chat.
	 * @return a text component that will translate into the name of the title-aspect
	 */
	public ITextComponent asTextComponent()
	{
		return new TranslationTextComponent(getTranslationKey());
	}
	
	public String getTranslationKey()
	{
		return "title.aspect." + this.toString();
	}
}