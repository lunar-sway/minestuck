package com.mraof.minestuck.util;

import net.minecraft.client.resources.I18n;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.EnumSet;
import java.util.Random;

/**
 * This class represents the 14 title-classes that exists in sburb,
 * (including lord and muse, but they are by default not generated in the <code>getRandomClass()</code> method,
 * thought they can be included by setting the parameter <code>includeSpecial</code> to <code>true</code>)
 * The <code>toString()</code> method is overridden and returns a lower-cased version of the title-class name.
 * @author kirderf1
 */
public enum EnumClass
{
	BARD,HEIR,KNIGHT,MAGE,MAID,PAGE,PRINCE,ROGUE,SEER,SYLPH,THIEF,WITCH,
	LORD,MUSE;	//Special title-classes
	
	/**
	 * This method generates one of the 12 default title-classes that is not found in the <code>unavailableClasses</code> set.
	 * @param unavailableClasses An <code>EnumSet&#60;EnumClass&#62;</code> that includes all forbidden title-classes,
	 *                              typically all title-classes that is already being used by other players.
	 * @param rand Random used to select a title-class.
	 * @return null if <code>unavailableClasses</code> has been filled, else a random <code>EnumClass</code> that isn't in <code>unavailableClasses</code>.
	 */
	public static EnumClass getRandomClass(@Nullable EnumSet<EnumClass> unavailableClasses, @Nonnull Random rand)
	{
		return getRandomClass(unavailableClasses, rand, false);
	}
	
	/**
	 * This method generates one of the 12 default title-classes that is not found in the <code>unavailableClasses</code> set.
	 * If you don't want the two special title-classes to be forbidden, use <code>true</code> as the <code>includeSpecial</code> parameter.
	 * @param unavailableClasses An <code>EnumSet&#60;EnumClass&#62;</code> that includes all forbidden title-classes,
	 *                              typically all title-classes that is already being used by other players.
	 * @param rand Random used to select a title-class.
	 * @param includeSpecial If it should include the two special title-classes.
	 * @return null if <code>unavailableClasses</code> has been filled, else a random <code>EnumClass</code> that isn't in <code>unavailableClasses</code>.
	 */
	public static EnumClass getRandomClass(@Nullable EnumSet<EnumClass> unavailableClasses, @Nonnull Random rand, boolean includeSpecial)
	{
		if(unavailableClasses == null)
			unavailableClasses = EnumSet.noneOf(EnumClass.class);
		if(!includeSpecial)	//Prevent generation of the special "master" classes unless includeSpecial is true
		{
			unavailableClasses.add(LORD);
			unavailableClasses.add(MUSE);
		}
		
		EnumSet<EnumClass> classes = EnumSet.complementOf(unavailableClasses);	//TODO Does it make more sense for the parameter to instead be a set of available classes?
		if(classes.isEmpty())
			return null;	//No class available to generate
		
		int classInt = rand.nextInt(classes.size());
		for(EnumClass c : classes)	//Go through each available title-class until the index generated is reached
		{
			if(classInt == 0)
				return c;
			classInt--;
		}
		
		return null;
	}
	
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
	
	/**
	 * Takes the enum name for this title-class and returns a lowercase version.
	 * Aside from regular use of the method, it is useful for producing
	 * the unlocalized name of the title-class using <code>"title." + titleClass.toString()</code>
	 * @return the name of this title-class
	 */
	@Override
	public String toString()
	{
		return this.name().toLowerCase();
	}
	
	/**
	 * Translates and returns the proper name of this title-class. Should only be used client-side.
	 * For usage in messages sent to a player from a server, use <code>asTextComponent()</code>.
	 * For debugging purposes, use <code>toString()</code> instead.
	 * @return a translated string of the name.
	 */
	@OnlyIn(Dist.CLIENT)
	public String getDisplayName()
	{
		return I18n.format("title." + this.toString());
	}
	
	/**
	 * Creates a text component for this title-class that will be translated client-side.
	 * Used for messages from the mod that for example will be sent trough chat.
	 * @return a text component that will translate into the name of the title-class
	 */
	public ITextComponent asTextComponent()
	{
		return new TextComponentTranslation("title." + this.toString());
	}
}
