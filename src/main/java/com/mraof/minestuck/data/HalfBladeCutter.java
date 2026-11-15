package com.mraof.minestuck.data;

import java.awt.image.BufferedImage;

/**
 * Turns the texture of a sword into the texture of its broken half, by removing the upper part of the blade.
 */
public final class HalfBladeCutter
{
	
	//the size that the cut is designed for
	public static final int SIZE = 16;
	
	//a pixel stays if y - x >= CUT_OFFSET + JAG[x % JAG.length]
	private static final int CUT_OFFSET = -4;
	private static final int[] JAG = {0, 0, -1};
	
	private HalfBladeCutter()
	{
	}
	
	/**
	 * @return true if {@link #cut(BufferedImage)} is able to handle a texture with this size
	 */
	public static boolean isSupportedSize(int width, int height)
	{
		return width == height && width >= SIZE && width % SIZE == 0;
	}
	
	public static BufferedImage cut(BufferedImage source)
	{
		int width = source.getWidth(), height = source.getHeight();
		if(!isSupportedSize(width, height))
			throw new IllegalArgumentException("Only square textures with a size that is a multiple of " + SIZE + " can be cut automatically, but got " + width + "x" + height);
		
		int scale = width / SIZE;
		BufferedImage result = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
		boolean visible = false;
		for(int y = 0; y < height; y++)
		{
			for(int x = 0; x < width; x++)
			{
				int jag = JAG[(x / scale) % JAG.length];
				if(y - x < (CUT_OFFSET + jag) * scale) continue;
				
				int argb = source.getRGB(x, y);
				result.setRGB(x, y, argb);
				visible |= (argb >>> 24) != 0;
			}
		}
		
		if(!visible) throw new IllegalArgumentException("The cut removes the whole texture");
		return result;
	}
}
