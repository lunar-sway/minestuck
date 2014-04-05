package com.mraof.minestuck.block;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;

import com.mraof.minestuck.Minestuck;
import com.mraof.minestuck.tileentity.TileEntityComputer;
import com.mraof.minestuck.util.ComputerProgram;
import com.mraof.minestuck.util.UsernameHandler;

public class BlockComputerOff extends Block {

	private IIcon frontIcon;
	private IIcon sideIcon;

	public BlockComputerOff()
	{
		super(Material.rock);
		setBlockName("computer");
		setHardness(4.0F);
		this.setCreativeTab(Minestuck.tabMinestuck);
		
	}
	
	@Override
	public IIcon getIcon(int par1, int par2)
	{
		if (par2 == 0 && par1 == 3) {return this.frontIcon;}
		return par1 != par2 ? this.sideIcon : this.frontIcon;
	}

	private void setDefaultDirection(World par1World, int par2, int par3, int par4)
	{
		if (!par1World.isRemote)
		{
			Block block = par1World.getBlock(par2, par3, par4 - 1);
			Block block1 = par1World.getBlock(par2, par3, par4 + 1);
			Block block2 = par1World.getBlock(par2 - 1, par3, par4);
			Block block3 = par1World.getBlock(par2 + 1, par3, par4);
			byte b0 = 3;

			if (block.func_149730_j() && !block1.func_149730_j())
			{
				b0 = 3;
			}

			if (block1.func_149730_j() && !block.func_149730_j())
            {
                b0 = 2;
            }

            if (block2.func_149730_j() && !block3.func_149730_j())
            {
                b0 = 5;
            }

            if (block3.func_149730_j() && !block2.func_149730_j())
            {
                b0 = 4;
            }

			par1World.setBlockMetadataWithNotify(par2, par3, par4, b0, 2);
		}
	}
	
	@Override
	public void registerBlockIcons(IIconRegister par1IconRegister)
	{
		this.frontIcon = par1IconRegister.registerIcon("minestuck:ComputerFrontOff");
		this.sideIcon =  par1IconRegister.registerIcon("minestuck:PhernaliaFrame");
	}
	
	@Override
	public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int par6, float par7, float par8, float par9) {
		if(world.isRemote || player.isSneaking() || player.getHeldItem() != null && ComputerProgram.getProgramID(player.getHeldItem()) == -2)
			return false;
		
		world.setBlock(x, y, z, Minestuck.blockComputerOn, world.getBlockMetadata(x, y, z), 2);
		
		TileEntityComputer te = (TileEntityComputer) world.getTileEntity(x, y, z);
		te.owner = UsernameHandler.encode(player.getCommandSenderName());
		Minestuck.blockComputerOn.onBlockActivated(world, x, y, z, player, par6, par7, par8, par9);
		
		return true;
	}
	
	@Override
	public void onBlockPlacedBy(World par1World, int par2, int par3, int par4, EntityLivingBase par5EntityLivingBase, ItemStack par6ItemStack)
	{
		int l = MathHelper.floor_double((double)(par5EntityLivingBase.rotationYaw * 4.0F / 360.0F) + 0.5D) & 3;

		if (l == 0)
		{
			par1World.setBlockMetadataWithNotify(par2, par3, par4, 2, 2);
		}

		if (l == 1)
		{
			par1World.setBlockMetadataWithNotify(par2, par3, par4, 5, 2);
		}

		if (l == 2)
		{
			par1World.setBlockMetadataWithNotify(par2, par3, par4, 3, 2);
		}

		if (l == 3)
		{
			par1World.setBlockMetadataWithNotify(par2, par3, par4, 4, 2);
		}
	}

	@Override
	public void onBlockAdded(World par1World, int par2, int par3, int par4)
	{
		super.onBlockAdded(par1World, par2, par3, par4);
		if(par1World.getBlockMetadata(par2, par3, par4) == 0)
			this.setDefaultDirection(par1World, par2, par3, par4);
	}

}
