package com.mraof.minestuck.block;

import com.mraof.minestuck.item.TabMinestuck;

import net.minecraft.block.BlockBush;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class BlockBloomingCactus extends BlockBush {
	public BlockBloomingCactus() 
	{
	super(Material.PLANTS);
	setCreativeTab(TabMinestuck.instance);
	setUnlocalizedName("bloomingCactus");
	}
	
	@Override
	public boolean canPlaceBlockAt(World worldIn, BlockPos pos) {
		IBlockState soil = worldIn.getBlockState(pos.down());
		return canSustainBush(soil);
	}
	
	public boolean canSustainBush(IBlockState state) {
		return state.getBlock() == Blocks.SAND;
	}
}
