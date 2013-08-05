package com.mraof.minestuck.util;

public class Title {
	
	private int heroClass;
	private int heroAspect;

	public Title(int heroClass, int heroAspect) {
		this.heroClass = heroClass;
		this.heroAspect = heroAspect;
	}
	
	public int getHeroClass() {
		return this.heroClass;
	}
	
	public int getHeroAspect() {
		return this.heroAspect;
	}
	
	public String getTitleName() {
		return TitleHelper.classes[heroClass] + " of " + TitleHelper.aspects[heroAspect];
	}

}
