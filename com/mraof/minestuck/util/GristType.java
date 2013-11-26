package com.mraof.minestuck.util;

import net.minecraft.util.StatCollector;

public enum GristType {
	Amber("Amber",0.5F), 
	Amethyst("Amethyst",0.3F), 
	Artifact("Artifact",0.0F),
	Build("Build",0.0F),
	Caulk("Caulk",0.5F),
	Chalk("Chalk",0.5F),
	Cobalt("Cobalt",0.3F),
	Diamond("Diamond",0.1F),
	Garnet("Garnet",0.3F),
	Gold("Gold",0.1F), 
	Iodine("Iodine",0.5F), 
	Marble("Marble",0.5F), 
	Mercury("Mercury",0.5F), 
	Quartz("Quartz",0.4F), 
	Ruby("Ruby",0.3F), 
	Rust("Rust",0.3F), 
	Shale("Shale",0.5F), 
	Sulfur("Sulfur",0.4F), 
	Tar("Tar",0.5F), 
	Uranium("Uranium",0.1F), 
	Zillium("Zillium",0.0F);
	
	final String name;
	final float rarity;
	
	public static final int allGrists = 21;
	
	GristType(String name, float rarity) {
		this.name = name;
		this.rarity = rarity;
	}
	
	public String getDisplayName() {
		return StatCollector.translateToLocal("grist."+name);
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
	
	public String getAltName() {
		String s = "grist."+name+".altName";
		if(!StatCollector.func_94522_b(s) || StatCollector.translateToLocal(s).isEmpty())
			return getDisplayName();
		else return StatCollector.translateToLocal(s);
	}
	
	public static GristType getTypeFromString(String string) 
	{
		for(GristType current : GristType.values())
			if(current.getName().equals(string))
				return current;
		return null;
	}
}
