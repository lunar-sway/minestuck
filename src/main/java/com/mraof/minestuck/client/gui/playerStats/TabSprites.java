package com.mraof.minestuck.client.gui.playerStats;

public class TabSprites
{
	public enum TabPosition
	{
		LEFT, MIDDLE, RIGHT
	}
	
	public record TabSprite(int u, int v)
	{
	}
	
	public record TabSpritePool(TabSprite left, TabSprite middle, TabSprite right, TabSprite leftActive,
	                            TabSprite middleActive, TabSprite rightActive)
	{
		public TabSprite get(TabPosition position, boolean active)
		{
			return switch(position)
			{
				case LEFT -> active ? leftActive : left;
				case MIDDLE -> active ? middleActive : middle;
				case RIGHT -> active ? rightActive : right;
			};
		}
	}
}