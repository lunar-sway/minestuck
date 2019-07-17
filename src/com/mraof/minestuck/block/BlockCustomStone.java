package com.mraof.minestuck.block;

import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;

public class BlockCustomStone extends BlockCustom
{
	
	public int flammability, fireSpread;
	
	public BlockCustomStone(MapColor mapColor)
	{
		super(Material.ROCK, mapColor, SoundType.STONE);
		setHardness(1.0F);
		setHarvestLevel("pickaxe", 0);
	}
}