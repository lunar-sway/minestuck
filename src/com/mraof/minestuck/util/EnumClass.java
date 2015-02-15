package com.mraof.minestuck.util;

import java.util.EnumSet;
import java.util.Random;

import net.minecraft.util.StatCollector;

/**
 * This class represents the 14 classes that exists in sburb,
 * (including lord and muse, but they are by default not generated in the <code>getRandomClass()</code> method,
 * thought they can be included by setting the parameter <code>includeSpecial</code> to <code>true</code>)
 * The <code>toString()</code> method is overridden and returns a better cased version of the class name.
 * @author kirderf1
 */
public enum EnumClass {
	BARD,HEIR,KNIGHT,MAGE,MAID,PAGE,PRINCE,ROGUE,SEER,SYLPH,THIEF,WITCH,
	LORD,MUSE;	//Non-randomized classes
	
	public static EnumClass getRandomClass(EnumSet<EnumClass> unavailableClasses, Random rand)
	{
		return getRandomClass(unavailableClasses, rand, false);
	}
	
	/**
	 * This method generates one of the 12 default classes that is not specified in the <code>unavailableClasses</code> array.
	 * If you want to enable special classes, use <code>true</code> as the <code>includeSpecial</code> parameter.
	 * @param unavailableClasses An <code>EnumSet&#60;EnumClass&#62;</code> that includes the classes that it won't choose from.
	 * Compatible with the value null.
	 * @param includeSpecial If it should include the two special classes.
	 * @return  null if <code>unavailableClasses</code> contains 12 or more classes (14 or more is <code>includeSpecial</code> is set to <code>true</code>.)
	 * or an <code>EnumClass</code> of the chosen class.
	 */
	public static EnumClass getRandomClass(EnumSet<EnumClass> unavailableClasses, Random rand, boolean includeSpecial)
	{
		if(unavailableClasses == null)
			unavailableClasses = EnumSet.noneOf(EnumClass.class);
		if(!includeSpecial){
			unavailableClasses.add(LORD);
			unavailableClasses.add(MUSE);
		}
		if(!(unavailableClasses.size() < 12) || includeSpecial && !(unavailableClasses.size() < 14))
			return null;	//No class available to generate
		int classInt = rand.nextInt(12 - unavailableClasses.size());
		EnumClass[] list = values();
		for(int i = 0; i < list.length; i++)
			if(!unavailableClasses.contains(list[i]))
			{
				classInt--;
				if(classInt == -1)
					return list[i];
			}
		
		return null;
	}
	
	@Override
	public String toString() {
		String s = this.name();
		s = s.toLowerCase();
		return s.replaceFirst(""+s.charAt(0), ""+Character.toUpperCase(s.charAt(0)));
	}
	
	public String getDisplayName() {
		return StatCollector.translateToLocal("title."+this.toString());
	}
	
}
