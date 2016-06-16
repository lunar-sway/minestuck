package com.mraof.minestuck.block;

import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;

/**
 * A class for creating blocks with a sound type in the constructor, because the sound setter is protected.
 */
public class BlockSound extends Block
{

	public BlockSound(Material material, MapColor mapColor, SoundType sound)
	{
		super(material, mapColor);
		setStepSound(sound);
	}
	
	public BlockSound(Material material, SoundType sound)
	{
		super(material);
		setStepSound(sound);
	}
	
}