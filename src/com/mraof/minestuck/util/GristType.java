package com.mraof.minestuck.util;

import net.minecraft.util.text.translation.I18n;

public enum GristType {
	Amber("amber",0.5F), 
	Amethyst("amethyst",0.3F), 
	Artifact("artifact",0.1F),
	Build("build",0.0F),
	Caulk("caulk",0.5F),
	Chalk("chalk",0.5F),
	Cobalt("cobalt",0.4F),
	Diamond("diamond",0.2F),
	Garnet("garnet",0.3F),
	Gold("gold",0.2F), 
	Iodine("iodine",0.5F), 
	Marble("marble",0.4F), 
	Mercury("mercury",0.4F), 
	Quartz("quartz",0.4F), 
	Ruby("ruby",0.3F), 
	Rust("rust",0.3F), 
	Shale("shale",0.5F), 
	Sulfur("sulfur",0.4F), 
	Tar("tar",0.5F), 
	Uranium("uranium",0.2F), 
	Zillium("zillium",0.0F);
	
	final String name;
	final float rarity;
	
	public static final int allGrists = values().length;
	
	GristType(String name, float rarity) {
		this.name = name;
		this.rarity = rarity;
	}
	
	public String getDisplayName()
	{
		return I18n.translateToLocal("grist."+name);
	}
	
	/**
	 * Returns the grist's full unlocalized name.
	 * @return
	 */
	public String getName() {
		return name;
	}
	
	/**
	 * Returns the grist's rarity. Is a number from 0.0 to 1.0.
	 * @return
	 */
	public float getRarity() {
		return rarity;
	}
	
	/**
	 * Returns the power level of a underling of a grist's type. Don't call this with grists like Zillium or Build.
	 */
	public float getPower() {
		return 1/rarity;
	}
	
	public static GristType getTypeFromString(String string) 
	{
		for(GristType current : GristType.values())
			if(current.getName().equals(string))
				return current;
		return null;
	}
	
	public static String[] getNames()
	{
		String[] list = new String[values().length];
		for(int i = 0; i < list.length; i++)
			list[i] = values()[i].name;
		return list;
	}
}