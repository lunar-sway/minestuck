package com.mraof.minestuck.block;

import net.minecraft.block.material.Material;
import net.minecraft.util.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fluids.BlockFluidClassic;
import net.minecraftforge.fluids.Fluid;

public class BlockFluidBrainJuice extends BlockFluidClassic 
{
//	@SideOnly(Side.CLIENT)
//	protected IIcon stillIcon;
//	@SideOnly(Side.CLIENT)
//	protected IIcon flowingIcon;

	public BlockFluidBrainJuice(Fluid fluid, Material material) 
	{
		super(fluid, material);
		
		setUnlocalizedName("brainJuice");
		this.setTickRandomly(true);
	}

//	@Override
//	public IIcon getIcon(int side, int meta) 
//	{
//		return (side == 0 || side == 1) ? stillIcon : flowingIcon;
//	}

//	@SideOnly(Side.CLIENT)
//	@Override
//	public void registerBlockIcons(IIconRegister par1IconRegister) 
//	{
//		stillIcon = par1IconRegister.registerIcon("minestuck:BrainJuiceStill");
//		flowingIcon = par1IconRegister.registerIcon("minestuck:BrainJuiceFlowing");
//	}
	
	@Override
	public boolean canDisplace(IBlockAccess world, BlockPos pos)
	{
		return world.getBlockState(pos).getBlock().getMaterial().isLiquid() || super.canDisplace(world, pos);
	}
	
	//Used to fix a bug in the fml code where the block isn't set to the liquid when flowing into another block.
	@Override
	public boolean displaceIfPossible(World world, BlockPos pos)
	{
		if(super.displaceIfPossible(world, pos))
		{
			world.setBlockState(pos, this.getDefaultState());
			return true;
		} else return false;
	}
}
