package com.mraof.minestuck.util;

import net.minecraft.client.resources.I18n;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class Title
{
	
	private EnumClass heroClass;
	private EnumAspect heroAspect;

	public Title(EnumClass heroClass, EnumAspect heroAspect)
	{
		this.heroClass = heroClass;
		this.heroAspect = heroAspect;
	}
	
	public EnumClass getHeroClass()
	{
		return this.heroClass;
	}
	
	public EnumAspect getHeroAspect()
	{
		return this.heroAspect;
	}
	
	@SideOnly(Side.CLIENT)
	public String getTitleName()
	{
		return I18n.format("title.format", heroClass.getDisplayName(), heroAspect.getDisplayName());
	}
	
	@Override
	public String toString()
	{
		return heroClass.toString() + " of " + heroAspect.toString();
	}
	
	public ITextComponent asTextComponent()
	{
		return new TextComponentTranslation("title.format", heroClass.asTextComponent(), heroAspect.asTextComponent());
	}
	
	@Override
	public boolean equals(Object obj)	//TODO override title.hashCode() too.
	{
		if(obj instanceof Title)
		{
			Title title = (Title) obj;
			return title.heroClass.equals(this.heroClass) && title.heroAspect.equals(this.heroAspect);
		}
		return false;
	}
}
