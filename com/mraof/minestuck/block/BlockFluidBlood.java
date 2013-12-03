package com.mraof.minestuck.block;

import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.util.Icon;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fluids.BlockFluidClassic;
import net.minecraftforge.fluids.Fluid;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class BlockFluidBlood extends BlockFluidClassic 
{
	@SideOnly(Side.CLIENT)
	protected Icon stillIcon;
	@SideOnly(Side.CLIENT)
	protected Icon flowingIcon;

	public BlockFluidBlood(int id, Fluid fluid, Material material) 
	{
		super(id, fluid, material);
		this.setTickRandomly(true);
		setUnlocalizedName("blood");
	}

	@Override
	public Icon getIcon(int side, int meta) 
	{
		return (side == 0 || side == 1) ? stillIcon : flowingIcon;
	}

	@SideOnly(Side.CLIENT)
	@Override
	public void registerIcons(IconRegister par1IconRegister) 
	{
		stillIcon = par1IconRegister.registerIcon("minestuck:BloodStill");
		flowingIcon = par1IconRegister.registerIcon("minestuck:BloodFlowing");
	}
	
	@Override
	public boolean canDisplace(IBlockAccess world, int x, int y, int z) 
	{
		return world.getBlockMaterial(x,  y,  z).isLiquid() || super.canDisplace(world, x, y, z);
	}

	@Override
	public boolean displaceIfPossible(World world, int x, int y, int z) 
	{
		return super.displaceIfPossible(world, x, y, z);
	}
}
