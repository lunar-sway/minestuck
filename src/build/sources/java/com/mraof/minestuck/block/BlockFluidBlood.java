package com.mraof.minestuck.block;

import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fluids.BlockFluidClassic;
import net.minecraftforge.fluids.Fluid;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class BlockFluidBlood extends BlockFluidClassic 
{
	@SideOnly(Side.CLIENT)
	protected IIcon stillIcon;
	@SideOnly(Side.CLIENT)
	protected IIcon flowingIcon;

	public BlockFluidBlood(Fluid fluid, Material material) 
	{
		super(fluid, material);
		
		setBlockName("blood");
		this.setTickRandomly(true);
	}

	@Override
	public IIcon getIcon(int side, int meta) 
	{
		return (side == 0 || side == 1) ? stillIcon : flowingIcon;
	}

	@SideOnly(Side.CLIENT)
	@Override
	public void registerBlockIcons(IIconRegister par1IconRegister) 
	{
		stillIcon = par1IconRegister.registerIcon("minestuck:BloodStill");
		flowingIcon = par1IconRegister.registerIcon("minestuck:BloodFlowing");
	}
	
	@Override
	public boolean canDisplace(IBlockAccess world, int x, int y, int z) 
	{
		return world.getBlock(x,  y,  z).getMaterial().isLiquid() || super.canDisplace(world, x, y, z);
	}

	@Override
	public boolean displaceIfPossible(World world, int x, int y, int z) 
	{
		return super.displaceIfPossible(world, x, y, z);
	}
}
