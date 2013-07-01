package com.mraof.minestuck.block;

import java.util.List;
import java.util.Random;

import com.mraof.minestuck.CommonProxy;
import com.mraof.minestuck.Minestuck;
import com.mraof.minestuck.tileentity.TileEntityGatePortal;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.network.packet.Packet9Respawn;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;

public class BlockGatePortal extends BlockContainer
{
	int destinationDimension;
	public BlockGatePortal(int id, Material material) 
	{
		super(id, material);
		setUnlocalizedName("gatePortal");
		setCreativeTab(CreativeTabs.tabMisc);
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
			TileEntityGatePortal portal = (TileEntityGatePortal) par1World.getBlockTileEntity(x, y, z);
			if(par5Entity.dimension != portal.destinationDimension)
			{
				portal.teleportEntity(par5Entity);
			}
			else
				par1World.setBlockToAir(x, y, z);
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
		return par5 != 0 ? false : super.shouldSideBeRendered(par1IBlockAccess, par2, par3, par4, par5);
	}
	@Override
	public boolean isOpaqueCube()
	{
		return false;
	}
	//    public String getTextureFile () 
	//	{
	//		return CommonProxy.BLOCKS_PNG;
	//	}

	@Override
	public TileEntity createTileEntity(World world, int metadata) 
	{
		TileEntityGatePortal tileEntity = (TileEntityGatePortal) this.createNewTileEntity(world);
		tileEntity.destinationDimension = this.destinationDimension;
		return tileEntity;
	}
	@Override
	public TileEntity createNewTileEntity(World var1) 
	{
		return new TileEntityGatePortal();
	}

	/**
	 * If this block doesn't render as an ordinary block it will return False (examples: signs, buttons, stairs, etc)
	 */
	public boolean renderAsNormalBlock()
	{
		return false;
	}

	/**
	 * Returns the quantity of items to drop on block destruction.
	 */
	public int quantityDropped(Random par1Random)
	{
		return 0;
	}


	@SideOnly(Side.CLIENT)

	/**
	 * A randomly called display update to be able to add particles or other items for display
	 */
	public void randomDisplayTick(World par1World, int par2, int par3, int par4, Random par5Random)
	{

	}

	/**
	 * The type of render function that is called for this block
	 */
	public int getRenderType()
	{
		return -1;
	}

	/**
	 * Called whenever the block is added into the world. Args: world, x, y, z
	 */

	@SideOnly(Side.CLIENT)

	/**
	 * only called by clickMiddleMouseButton , and passed to inventory.setCurrentItem (along with isCreative)
	 */
	public int idPicked(World par1World, int par2, int par3, int par4)
	{
		return 0;
	}
	//this keeps portals that lead to the same world from existing
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
	public void setDestinationDimension(World world, int x, int y, int z, int destinationDimension) 
	{
		((TileEntityGatePortal) world.getBlockTileEntity(x, y, z)).destinationDimension = destinationDimension;
	}
	@SideOnly(Side.CLIENT)

	/**
	 * When this method is called, your block should register all the icons it needs with the given IconRegister. This
	 * is the only chance you get to register icons.
	 */
	public void registerIcons(IconRegister par1IconRegister)
	{
		this.blockIcon = par1IconRegister.registerIcon("Minestuck:GatePortal");
	}
}
