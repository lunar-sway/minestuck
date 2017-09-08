package com.mraof.minestuck.util;

import net.minecraft.client.resources.I18n;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

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
	
	public static EnumAspect getAspectFromInt(int i)
	{
		return EnumAspect.values()[i];
	}
	
	public static int getIntFromAspect(EnumAspect e)
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