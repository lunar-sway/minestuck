package com.mraof.minestuck.block;

import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.item.DyeColor;

public class BlockCollections
{
	public static Block coloredWool(DyeColor color)
	{
		switch(color)
		{
			case WHITE: return Blocks.WHITE_WOOL;
			case ORANGE: return Blocks.ORANGE_WOOL;
			case MAGENTA: return Blocks.MAGENTA_WOOL;
			case LIGHT_BLUE: return Blocks.LIGHT_BLUE_WOOL;
			case YELLOW: return Blocks.YELLOW_WOOL;
			case LIME: return Blocks.LIME_WOOL;
			case PINK: return Blocks.PINK_WOOL;
			case GRAY: return Blocks.GRAY_WOOL;
			case LIGHT_GRAY: return Blocks.LIGHT_GRAY_WOOL;
			case CYAN: return Blocks.CYAN_WOOL;
			case PURPLE: return Blocks.PURPLE_WOOL;
			case BLUE: return Blocks.BLUE_WOOL;
			case BROWN: return Blocks.BROWN_WOOL;
			case GREEN: return Blocks.GREEN_WOOL;
			case RED: return Blocks.RED_WOOL;
			case BLACK: return Blocks.BLACK_WOOL;
			default: throw new IllegalArgumentException("Got unexpected color: "+color);
		}
	}
	
	public static Block coloredTerracotta(DyeColor color)
	{
		switch(color)
		{
			case WHITE: return Blocks.WHITE_TERRACOTTA;
			case ORANGE: return Blocks.ORANGE_TERRACOTTA;
			case MAGENTA: return Blocks.MAGENTA_TERRACOTTA;
			case LIGHT_BLUE: return Blocks.LIGHT_BLUE_TERRACOTTA;
			case YELLOW: return Blocks.YELLOW_TERRACOTTA;
			case LIME: return Blocks.LIME_TERRACOTTA;
			case PINK: return Blocks.PINK_TERRACOTTA;
			case GRAY: return Blocks.GRAY_TERRACOTTA;
			case LIGHT_GRAY: return Blocks.LIGHT_GRAY_TERRACOTTA;
			case CYAN: return Blocks.CYAN_TERRACOTTA;
			case PURPLE: return Blocks.PURPLE_TERRACOTTA;
			case BLUE: return Blocks.BLUE_TERRACOTTA;
			case BROWN: return Blocks.BROWN_TERRACOTTA;
			case GREEN: return Blocks.GREEN_TERRACOTTA;
			case RED: return Blocks.RED_TERRACOTTA;
			case BLACK: return Blocks.BLACK_TERRACOTTA;
			default: throw new IllegalArgumentException("Got unexpected color: "+color);
		}
	}
	
	public static Block coloredGlass(DyeColor color)
	{
		switch(color)
		{
			case WHITE: return Blocks.WHITE_STAINED_GLASS;
			case ORANGE: return Blocks.ORANGE_STAINED_GLASS;
			case MAGENTA: return Blocks.MAGENTA_STAINED_GLASS;
			case LIGHT_BLUE: return Blocks.LIGHT_BLUE_STAINED_GLASS;
			case YELLOW: return Blocks.YELLOW_STAINED_GLASS;
			case LIME: return Blocks.LIME_STAINED_GLASS;
			case PINK: return Blocks.PINK_STAINED_GLASS;
			case GRAY: return Blocks.GRAY_STAINED_GLASS;
			case LIGHT_GRAY: return Blocks.LIGHT_GRAY_STAINED_GLASS;
			case CYAN: return Blocks.CYAN_STAINED_GLASS;
			case PURPLE: return Blocks.PURPLE_STAINED_GLASS;
			case BLUE: return Blocks.BLUE_STAINED_GLASS;
			case BROWN: return Blocks.BROWN_STAINED_GLASS;
			case GREEN: return Blocks.GREEN_STAINED_GLASS;
			case RED: return Blocks.RED_STAINED_GLASS;
			case BLACK: return Blocks.BLACK_STAINED_GLASS;
			default: throw new IllegalArgumentException("Got unexpected color: "+color);
		}
	}
	
	public static Block coloredGlassPane(DyeColor color)
	{
		switch(color)
		{
			case WHITE: return Blocks.WHITE_STAINED_GLASS_PANE;
			case ORANGE: return Blocks.ORANGE_STAINED_GLASS_PANE;
			case MAGENTA: return Blocks.MAGENTA_STAINED_GLASS_PANE;
			case LIGHT_BLUE: return Blocks.LIGHT_BLUE_STAINED_GLASS_PANE;
			case YELLOW: return Blocks.YELLOW_STAINED_GLASS_PANE;
			case LIME: return Blocks.LIME_STAINED_GLASS_PANE;
			case PINK: return Blocks.PINK_STAINED_GLASS_PANE;
			case GRAY: return Blocks.GRAY_STAINED_GLASS_PANE;
			case LIGHT_GRAY: return Blocks.LIGHT_GRAY_STAINED_GLASS_PANE;
			case CYAN: return Blocks.CYAN_STAINED_GLASS_PANE;
			case PURPLE: return Blocks.PURPLE_STAINED_GLASS_PANE;
			case BLUE: return Blocks.BLUE_STAINED_GLASS_PANE;
			case BROWN: return Blocks.BROWN_STAINED_GLASS_PANE;
			case GREEN: return Blocks.GREEN_STAINED_GLASS_PANE;
			case RED: return Blocks.RED_STAINED_GLASS_PANE;
			case BLACK: return Blocks.BLACK_STAINED_GLASS_PANE;
			default: throw new IllegalArgumentException("Got unexpected color: "+color);
		}
	}
	
	public static Block coloredConcrete(DyeColor color)
	{
		switch(color)
		{
			case WHITE: return Blocks.WHITE_CONCRETE;
			case ORANGE: return Blocks.ORANGE_CONCRETE;
			case MAGENTA: return Blocks.MAGENTA_CONCRETE;
			case LIGHT_BLUE: return Blocks.LIGHT_BLUE_CONCRETE;
			case YELLOW: return Blocks.YELLOW_CONCRETE;
			case LIME: return Blocks.LIME_CONCRETE;
			case PINK: return Blocks.PINK_CONCRETE;
			case GRAY: return Blocks.GRAY_CONCRETE;
			case LIGHT_GRAY: return Blocks.LIGHT_GRAY_CONCRETE;
			case CYAN: return Blocks.CYAN_CONCRETE;
			case PURPLE: return Blocks.PURPLE_CONCRETE;
			case BLUE: return Blocks.BLUE_CONCRETE;
			case BROWN: return Blocks.BROWN_CONCRETE;
			case GREEN: return Blocks.GREEN_CONCRETE;
			case RED: return Blocks.RED_CONCRETE;
			case BLACK: return Blocks.BLACK_CONCRETE;
			default: throw new IllegalArgumentException("Got unexpected color: "+color);
		}
	}
	
	public static Block coloredConcretePowder(DyeColor color)
	{
		switch(color)
		{
			case WHITE: return Blocks.WHITE_CONCRETE_POWDER;
			case ORANGE: return Blocks.ORANGE_CONCRETE_POWDER;
			case MAGENTA: return Blocks.MAGENTA_CONCRETE_POWDER;
			case LIGHT_BLUE: return Blocks.LIGHT_BLUE_CONCRETE_POWDER;
			case YELLOW: return Blocks.YELLOW_CONCRETE_POWDER;
			case LIME: return Blocks.LIME_CONCRETE_POWDER;
			case PINK: return Blocks.PINK_CONCRETE_POWDER;
			case GRAY: return Blocks.GRAY_CONCRETE_POWDER;
			case LIGHT_GRAY: return Blocks.LIGHT_GRAY_CONCRETE_POWDER;
			case CYAN: return Blocks.CYAN_CONCRETE_POWDER;
			case PURPLE: return Blocks.PURPLE_CONCRETE_POWDER;
			case BLUE: return Blocks.BLUE_CONCRETE_POWDER;
			case BROWN: return Blocks.BROWN_CONCRETE_POWDER;
			case GREEN: return Blocks.GREEN_CONCRETE_POWDER;
			case RED: return Blocks.RED_CONCRETE_POWDER;
			case BLACK: return Blocks.BLACK_CONCRETE_POWDER;
			default: throw new IllegalArgumentException("Got unexpected color: "+color);
		}
	}
}