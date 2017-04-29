package com.mraof.minestuck.block;

import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;

/**
 * A class for creating blocks with a sound type in the constructor, because the sound setter is protected.
 */
public class BlockCustom extends Block
{
	
	public int flammability, fireSpread;
	
	public BlockCustom(Material material, MapColor mapColor, SoundType sound)
	{
		super(material, mapColor);
		setSoundType(sound);
	}
	
	public BlockCustom(Material material, SoundType sound)
	{
		super(material);
		setSoundType(sound);
	}
	
	@Override
	public int getFlammability(IBlockAccess world, BlockPos pos, EnumFacing face)
	{
		return flammability;
	}
	
	@Override
	public int getFireSpreadSpeed(IBlockAccess world, BlockPos pos, EnumFacing face)
	{
		return fireSpread;
	}
	
	public BlockCustom setFireInfo(int flammability, int fireSpread)
	{
		this.flammability = flammability;
		this.fireSpread = fireSpread;
		return this;
	}
}