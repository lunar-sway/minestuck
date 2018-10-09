	package com.mraof.minestuck.block;

import com.mraof.minestuck.item.TabMinestuck;

import net.minecraft.block.Block;
import net.minecraft.block.BlockBush;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.EnumPlantType;
import net.minecraftforge.common.IPlantable;

public class BlockPetrifiedPoppy extends BlockBush {
	public BlockPetrifiedPoppy()
	{
		super(Material.ROCK);
		setCreativeTab(TabMinestuck.instance);
		setUnlocalizedName("petrifiedPoppy");
		setSoundType(SoundType.STONE);
	}

	@Override
	public boolean canPlaceBlockAt(World worldIn, BlockPos pos) {
		IBlockState soil = worldIn.getBlockState(pos.down());
		return canSustainBush(soil);
	}
	
	@Override
    protected boolean canSustainBush(IBlockState state)
    {
        return state.getBlock() == Blocks.STONE || state.getBlock() == Blocks.GRAVEL || state.getBlock() == Blocks.COBBLESTONE;
    }

	@Override
	public MapColor getMapColor(IBlockState state, IBlockAccess worldIn, BlockPos pos)
	{
		return MapColor.GRAY;
	}
}
