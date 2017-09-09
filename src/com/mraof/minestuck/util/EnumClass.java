package com.mraof.minestuck.util;

import net.minecraft.client.resources.I18n;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

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
		if(!includeSpecial)
		{
			unavailableClasses.add(LORD);
			unavailableClasses.add(MUSE);
		}
		
		if(unavailableClasses.size() == 14)
			return null;	//No class available to generate
		int classInt = rand.nextInt(14 - unavailableClasses.size());
		EnumClass[] list = values();
		for(EnumClass c : list)
			if(!unavailableClasses.contains(c))
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
	 * @return The title-class
	 */
	public static EnumClass getClassFromInt(int i)
	{
		return EnumClass.values()[i];
	}
	
	public static int getIntFromClass(EnumClass e)
	{
		return e.ordinal();
	}
	
	@Override
	public String toString()
	{
		return this.name().toLowerCase();
	}
	
	@SideOnly(Side.CLIENT)
	public String getDisplayName()
	{
		return I18n.format("title." + this.toString());
	}
	
	public ITextComponent asTextComponent()
	{
		return new TextComponentTranslation("title." + this.toString());
	}
}
