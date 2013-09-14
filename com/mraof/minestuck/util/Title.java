package com.mraof.minestuck.util;

public class Title {
	
	private EnumClass heroClass;
	private EnumAspect heroAspect;

	public Title(EnumClass heroClass, EnumAspect heroAspect) {
		this.heroClass = heroClass;
		this.heroAspect = heroAspect;
	}
	
	public EnumClass getHeroClass() {
		return this.heroClass;
	}
	
	public EnumAspect getHeroAspect() {
		return this.heroAspect;
	}
	
	public String getTitleName() {
		return this.toString();
	}
	
	@Override
	public String toString() {
		return heroClass.toString() + " of " + heroAspect.toString();
	}
	
}
