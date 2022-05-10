package com.mraof.minestuck.util;

import com.mraof.minestuck.client.gui.StoneTabletScreen;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.Style;
import org.apache.commons.lang3.mutable.MutableInt;

public class StoneTabletUtils
{
	/**
	 * Returns the width of text
	 */
	public static int getTextWidth(FontRenderer font, String text)
	{
		return font.width(font.isBidirectional() ? font.bidirectionalShaping(text) : text);
	}
	
	/**
	 * Calculates the position of the given character index in the text
	 * Might be appropriate to clean up usage of this function similarly to what vanilla did
	 */
	public static Point createPointer(FontRenderer font, String pageText, int index)
	{
		Point point = new Point();
		
		font.getSplitter().splitLines(pageText, StoneTabletScreen.TEXT_WIDTH, Style.EMPTY, true, (style, start, end) -> {
			if(index >= end)
				point.y += font.lineHeight;
			if(index >= start)
				point.x = getTextWidth(font, pageText.substring(start, Math.min(index, end)));
		});
		
		return point;
	}
	
	public static void adjustPointerAForBidi(FontRenderer font, Point pointer)
	{
		if (font.isBidirectional())
			pointer.x = StoneTabletScreen.TEXT_WIDTH - pointer.x;
	}
	
	public static void pointerToRelative(Point pointer, int width)
	{
		pointer.x = pointer.x - (width - StoneTabletScreen.GUI_WIDTH) / 2 - StoneTabletScreen.TEXT_OFFSET_X;
		pointer.y = pointer.y - StoneTabletScreen.TEXT_OFFSET_Y;
	}
	
	public static void pointerToPrecise(Point pointer, int width)
	{
		pointer.x = pointer.x + (width - StoneTabletScreen.GUI_WIDTH) / 2 + StoneTabletScreen.TEXT_OFFSET_X;
		pointer.y = pointer.y + StoneTabletScreen.TEXT_OFFSET_Y;
	}
	
	
	public static int getSelectionX(FontRenderer font, String text, int pointerX)
	{
		if (pointerX < 0)
			return 0;
		else {
			float f1 = 0.0F;
			boolean flag = false;
			String s = text + " ";
			
			for(int i = 0; i < s.length(); ++i)
			{
				char c0 = s.charAt(i);
				float f2 = font.width(String.valueOf(c0));
				if (c0 == 167 && i < s.length() - 1)
				{
					++i;
					c0 = s.charAt(i);
					if (c0 != 'l' && c0 != 'L')
					{
						if (c0 == 'r' || c0 == 'R')
							flag = false;
					} else
						flag = true;
					
					f2 = 0.0F;
				}
				
				float f = f1;
				f1 += f2;
				if (flag && f2 > 0.0F)
					++f1;
				
				if ((float)pointerX >= f && (float)pointerX < f1)
					return i;
			}
			
			if ((float)pointerX >= f1)
				return s.length() - 1;
			else return -1;
		}
	}
	
	/**
	 * Get a char index from the given pointer.
	 * Reverse of createPointer
	 */
	public static int getSelectionIndex(FontRenderer font, String text, Point pointer)
	{
		int maxY = 16 * font.lineHeight;
		if(pointer.y >= 0 && pointer.y < maxY)
		{
			MutableInt lineY = new MutableInt();
			MutableInt index = new MutableInt(text.length());
			
			font.getSplitter().splitLines(text, StoneTabletScreen.TEXT_WIDTH, Style.EMPTY, true, (style, start, end) -> {
				int nextLineY = lineY.intValue() + font.lineHeight;
				if(pointer.y >= lineY.intValue() && pointer.y < nextLineY)
				{
					int lineIdx = getSelectionX(font, text.substring(start, end), pointer.x);
					index.setValue(lineIdx < 0 ? -1 : start + lineIdx);
				}
				lineY.setValue(nextLineY);
			});
			
			return index.intValue();
		} else return -1;
	}
	
	public static int getSelectionWidth(FontRenderer font, String pageText, int selectionEnd)
	{
		return font.width(String.valueOf(pageText.charAt(MathHelper.clamp(selectionEnd, 0, pageText.length() - 1))));
	}
	
	public static class Point
	{
		public int x;
		public int y;
		
		public Point() {}
		public Point(int xIn, int yIn)
		{
			this.x = xIn;
			this.y = yIn;
		}
	}
}
