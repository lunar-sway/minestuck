package com.mraof.minestuck.block;


import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import com.mraof.minestuck.Minestuck;

public class BlockLargeMachine extends Block {
	public int Xlength;
	public int Zwidth;
	public int Yheight;
	public BlockLargeMachine(int length, int width, int height) 
	{
		super(Material.ROCK);
		this.setHardness(2);
		this.setCreativeTab(Minestuck.tabMinestuck);
		this.Xlength=length;
		this.Yheight=width;
		this.Zwidth=height;	
	}	
	//keeps the blocks from dropping something
	@Override
	public List<ItemStack> getDrops(IBlockAccess world,BlockPos pos,IBlockState state,int fortune)
	{
		return null;	
	}
	//make sure that the machine will fit where it's trying to be placed
    public boolean canPlaceBlockAt(World worldIn, BlockPos pos)
    {
    	for(int x = 0; x<Xlength; x++){
    		for (int y=0;y<Yheight;y++){
    			for (int z=0; z<Zwidth;z++){
    				if (!super.canPlaceBlockAt(worldIn, pos.add(x, y, z))){
    					return false;
    				}
    			}
    		}
    	}
    	return true;
    	
    }

	@Override
	public IBlockState getStateFromMeta(int meta) 
	{
	    return getDefaultState();
	}

	@Override
	public int getMetaFromState(IBlockState state) {
	    return 1;
	}
}
