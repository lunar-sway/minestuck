package com.mraof.minestuck.util;

public enum GristType {
	Amber("Amber",0.5F,0), 
	Amethyst("Amethyst",0.3F,0), 
	Artifact("Artifact",0.0F,0),
	Build("Build",0.0F,0),
	Caulk("Caulk",0.5F,0),
	Chalk("Chalk",0.5F,0),
	Cobalt("Cobalt",0.3F,0),
	Diamond("Diamond",0.1F,0),
	Garnet("Garnet",0.3F,0),
	Gold("Gold",0.1F,0), 
	Iodine("Iodine",0.5F,0), 
	Marble("Marble",0.5F,0), 
	Mercury("Mercury",0.5F,0), 
	Quartz("Quartz",0.4F,0), 
	Ruby("Ruby",0.3F,0), 
	Rust("Rust",0.3F,0), 
	Shale("Shale",0.5F,0), 
	Sulfur("Sulfur",0.4F,0), 
	Tar("Tar",0.5F,0), 
	Uranium("Uranium",0.1F,0), 
	Zillium("Zillium",0.0F,0);
	
	final String name;
	final float rarity;
	final int color;
	
	public static final int allGrists = 21;
	
	GristType(String name, float rarity, int color) {
		this.name = name;
		this.rarity = rarity;
		this.color = color;
	}
	
	/**
	 * Returns the grist's full name.
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
	 * Returns the grist's primary color. Used for coloring entity sprites.
	 * @return
	 */
	public int getColor() {
		return color;
	}
	
	/**
	 * Returns the power level of a underling of a grist's type. Don't call this with grists like Zillium or Build.
	 */
	public float getPower() {
		return 1/rarity;
	}
	
	public String getAltName() {
		if (this == GristType.Shale) {
			return "Crude";
		} else {
			return this.getName();
		}
	}
	
	public static GristType getTypeFromString(String string) 
	{
		for(GristType current : GristType.values())
			if(current.getName().equals(string))
				return current;
		return null;
	}
}
