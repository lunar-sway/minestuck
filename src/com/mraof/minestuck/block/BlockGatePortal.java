package com.mraof.minestuck.block;

import java.util.List;
import java.util.Random;

import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.Entity;
import net.minecraft.item.Item;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import com.mraof.minestuck.Minestuck;
import com.mraof.minestuck.tileentity.TileEntityGatePortal;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class BlockGatePortal extends BlockContainer
{
	int destinationDimension;
	public BlockGatePortal(Material material) 
	{
		super(material);
		
		setBlockName("gatePortal");
		this.setCreativeTab(Minestuck.tabMinestuck);
		destinationDimension = 2;
	}
	@Override
	public void setBlockBoundsBasedOnState(IBlockAccess par1IBlockAccess, int par2, int par3, int par4)
	{
		float var5 = 0.0625F;
		this.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, var5, 1.0F);
	}

	@Override
	public void onEntityCollidedWithBlock(World par1World, int x, int y, int z, Entity par5Entity) 
	{
		if (par5Entity.ridingEntity == null && par5Entity.riddenByEntity == null && !par1World.isRemote && par5Entity.timeUntilPortal == 0)
		{
			TileEntityGatePortal portal = (TileEntityGatePortal) par1World.getTileEntity(x, y, z);
				portal.teleportEntity(par5Entity);
		}
	}
	
	@SideOnly(Side.CLIENT)
	/**
	 * Returns true if the given side of this block type should be rendered, if the adjacent block is at the given
	 * coordinates.  Args: blockAccess, x, y, z, side
	 */
	@Override
	public boolean shouldSideBeRendered(IBlockAccess par1IBlockAccess, int par2, int par3, int par4, int par5)
	{
		return par5 > 1 ? false : super.shouldSideBeRendered(par1IBlockAccess, par2, par3, par4, par5);
	}
	
	@Override
	public boolean isOpaqueCube() {
		return false;
	}
	
	@Override
	public TileEntity createNewTileEntity(World world, int metadata) {
		return createTileEntity(world, metadata);
	}
	
	@Override
	public TileEntity createTileEntity(World world, int metadata) 
	{
		TileEntityGatePortal tileEntity = (TileEntityGatePortal) this.createNewTileEntity(world);
		tileEntity.destinationDimension = this.destinationDimension;
		return tileEntity;
	}
	
	public TileEntity createNewTileEntity(World var1)
	{
		return new TileEntityGatePortal();
	}

	/**
	 * If this block doesn't render as an ordinary block it will return False (examples: signs, buttons, stairs, etc)
	 */
	@Override
	public boolean renderAsNormalBlock()
	{
		return false;
	}

	public void addCollisionBoxesToList(World par1World, int par2, int par3, int par4, AxisAlignedBB par5AxisAlignedBB, List par6List, Entity par7Entity) {}
	
	/**
	 * Returns the quantity of items to drop on block destruction.
	 */
	@Override
	public int quantityDropped(Random par1Random)
	{
		return 0;
	}


	/**
	 * The type of render function that is called for this block
	 */
	@Override
	public int getRenderType()
	{
		return -1;
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	/**
	 * only called by clickMiddleMouseButton , and passed to inventory.setCurrentItem (along with isCreative)
	 */
	public Item getItem(World par1World, int par2, int par3, int par4)
	{
		return null;
	}
	
	/**
	 * Called whenever the block is added into the world. Args: world, x, y, z
	 */
	//this keeps portals that lead to the same world from existing
	@Override
	public void onBlockAdded(World par1World, int par2, int par3, int par4)
	{
		if (par1World.provider.dimensionId == destinationDimension)
		{
			if(this.destinationDimension != 0)
				this.destinationDimension = 0;
			else
				this.destinationDimension = Minestuck.skaiaDimensionId;
		}
	}
//	/**
//	 * Called upon block activation (right click on the block.)
//	 */
//	@Override
//	public boolean onBlockActivated(World par1World, int x, int y, int z, EntityPlayer par5EntityPlayer, int par6, float par7, float par8, float par9)
//	{
//		if (par1World.isRemote)
//		{
//			return true;
//		}
//		int newDimension = ((TileEntityGatePortal) par1World.getBlockTileEntity(x, y, z)).destinationDimension + 1;
//		if(par1World.provider.dimensionId != newDimension && DimensionManager.isDimensionRegistered(newDimension))
//		{
//			this.destinationDimension = newDimension;
//			((TileEntityGatePortal) par1World.getBlockTileEntity(x, y, z)).destinationDimension = newDimension;
//		}
//
//		return true;
//	}//
	
	public void setDestinationDimension(World world, int x, int y, int z, int destinationDimension) 
	{
		((TileEntityGatePortal) world.getTileEntity(x, y, z)).destinationDimension = destinationDimension;
	}
	@Override
	@SideOnly(Side.CLIENT)

	/**
	 * When this method is called, your block should register all the icons it needs with the given IconRegister. This
	 * is the only chance you get to register icons.
	 */
	public void registerBlockIcons(IIconRegister par1IconRegister)
	{
		this.blockIcon = par1IconRegister.registerIcon("minestuck:GatePortal");
	}
}
