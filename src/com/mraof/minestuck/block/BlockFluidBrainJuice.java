package com.mraof.minestuck.block;

import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.fluids.BlockFluidClassic;
import net.minecraftforge.fluids.Fluid;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class BlockFluidBrainJuice extends BlockFluidClassic 
{
	@SideOnly(Side.CLIENT)
	protected IIcon stillIcon;
	@SideOnly(Side.CLIENT)
	protected IIcon flowingIcon;

	public BlockFluidBrainJuice(Fluid fluid, Material material) 
	{
		super(fluid, material);
		
		setBlockName("brainJuice");
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
		stillIcon = par1IconRegister.registerIcon("minestuck:BrainJuiceStill");
		flowingIcon = par1IconRegister.registerIcon("minestuck:BrainJuiceFlowing");
	}
	
	@Override
	public boolean canDisplace(IBlockAccess world, int x, int y, int z) 
	{
		return world.getBlock(x,  y,  z).getMaterial().isLiquid() || super.canDisplace(world, x, y, z);
	}
}
