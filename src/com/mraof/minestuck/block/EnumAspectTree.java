package com.mraof.minestuck.block;

import net.minecraft.block.material.MaterialColor;
import net.minecraft.block.trees.AbstractTree;

/**
 * Even if we allow plugins to register their own title aspects, and we change EnumAspect to something other than an enum,
 * this class should remain unchanged as we only should register trees for our own aspects.
 */
public enum EnumAspectTree
{
	BLOOD(MaterialColor.WOOD, MaterialColor.WOOD, MaterialColor.WOOD, null),
	BREATH(MaterialColor.WOOD, MaterialColor.WOOD, MaterialColor.WOOD, null),
	DOOM(MaterialColor.WOOD, MaterialColor.WOOD, MaterialColor.WOOD, null),
	HEART(MaterialColor.WOOD, MaterialColor.WOOD, MaterialColor.WOOD, null),
	HOPE(MaterialColor.WOOD, MaterialColor.WOOD, MaterialColor.WOOD, null),
	LIFE(MaterialColor.WOOD, MaterialColor.WOOD, MaterialColor.WOOD, null),
	LIGHT(MaterialColor.WOOD, MaterialColor.WOOD, MaterialColor.WOOD, null),
	MIND(MaterialColor.WOOD, MaterialColor.WOOD, MaterialColor.WOOD, null),
	RAGE(MaterialColor.WOOD, MaterialColor.WOOD, MaterialColor.WOOD, null),
	SPACE(MaterialColor.WOOD, MaterialColor.WOOD, MaterialColor.WOOD, null),
	TIME(MaterialColor.WOOD, MaterialColor.WOOD, MaterialColor.WOOD, null),
	VOID(MaterialColor.WOOD, MaterialColor.WOOD, MaterialColor.WOOD, null);
	
	public final MaterialColor topColor, sideColor, plankColor;
	public final AbstractTree tree;
	
	EnumAspectTree(MaterialColor topColor, MaterialColor sideColor, MaterialColor plankColor, AbstractTree tree)
	{
		this.topColor = topColor;
		this.sideColor = sideColor;
		this.plankColor = plankColor;
		this.tree = tree;
	}
	
	public String getName()
	{
		return this.name().toLowerCase();
	}
}