package com.mraof.minestuck.util;

import net.minecraft.client.gui.FontRenderer;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.TextFormatting;

public class StoneTabletUtils
{
	/**
	 * Returns the width of text
	 */
	public static int getTextWidth(FontRenderer font, String text)
	{
		return font.width(font.isBidirectional() ? font.bidirectionalShaping(text) : text);
	}
	
	//I'm not exactly sure about what these do, so some function names might be inaccurate
	
	public static Point createPointer(FontRenderer font, String pageText, int sectionEnd)
	{
		Point point = new Point();
		int i = 0;
		int j = 0;
		
		for(String s = pageText; !s.isEmpty(); j = i)
		{
			int k = sizeStringToWidth(font, s, 114);
			if (s.length() <= k)
			{
				String s3 = s.substring(0, Math.min(Math.max(sectionEnd - j, 0), s.length()));
				point.x = point.x + getTextWidth(font, s3);
				break;
			}
			
			String s1 = s.substring(0, k);
			char c0 = s.charAt(k);
			boolean flag = c0 == ' ' || c0 == '\n';
			s = TextFormatting.stripFormatting(s1) + s.substring(k + (flag ? 1 : 0));
			i += s1.length() + (flag ? 1 : 0);
			if (i - 1 >= sectionEnd)
			{
				String s2 = s1.substring(0, Math.min(Math.max(sectionEnd - j, 0), s1.length()));
				point.x = point.x + getTextWidth(font, s2);
				break;
			}
			
			point.y = point.y + 9;
		}
		
		return point;
	}
	
	public static void adjustPointerAForBidi(FontRenderer font, Point pointer)
	{
		if (font.isBidirectional())
			pointer.x = 114 - pointer.x;
	}
	
	public static void adjustPointerA(Point pointer, int width)
	{
		pointer.x = pointer.x - (width - 192) / 2 - 36;
		pointer.y = pointer.y - 32;
	}
	
	public static void adjustPointerB(Point pointer, int width)
	{
		pointer.x = pointer.x + (width - 192) / 2 + 36;
		pointer.y = pointer.y + 32;
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
	
	public static int getSelectionIndex(FontRenderer font, String text, Point pointer)
	{
		int i = 16 * 9;
		if (pointer.y > i)
			return -1;
		else
		{
			int j = Integer.MIN_VALUE;
			int k = 9;
			int l = 0;
			
			for(String s = text; !s.isEmpty() && j < i; k += 9)
			{
				int i1 = sizeStringToWidth(font, s, 114);
				if (i1 < s.length())
				{
					String s1 = s.substring(0, i1);
					if (pointer.y >= j && pointer.y < k)
					{
						int k1 = getSelectionX(font, s1, pointer.x);
						return k1 < 0 ? -1 : l + k1;
					}
					
					char c0 = s.charAt(i1);
					boolean flag = c0 == ' ' || c0 == '\n';
					s = TextFormatting.stripFormatting(s1) + s.substring(i1 + (flag ? 1 : 0));
					l += s1.length() + (flag ? 1 : 0);
				} else if (pointer.y >= j && pointer.y < k)
				{
					int j1 = getSelectionX(font, s, pointer.x);
					return j1 < 0 ? -1 : l + j1;
				}
				
				j = k;
			}
			
			return text.length();
		}
	}
	
	public static int getSelectionWidth(FontRenderer font, String pageText, int selectionEnd)
	{
		return (int)font.width(String.valueOf(pageText.charAt(MathHelper.clamp(selectionEnd, 0, pageText.length() - 1))));
	}
	
	
	public static int sizeStringToWidth(FontRenderer font, String text, int wrapWidth)
	{
		return font.wordWrapHeight(text, wrapWidth);	//TODO was called sizeStringToWidth(). This was the only function with matching types. Check if this is the right replacement
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
