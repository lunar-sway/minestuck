package com.mraof.minestuck.block;

import java.util.List;
import java.util.Random;

import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.DimensionManager;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import com.mraof.minestuck.Minestuck;
import com.mraof.minestuck.tileentity.TileEntityGatePortal;
import com.mraof.minestuck.util.Location;

public class BlockGatePortal extends BlockContainer
{
	int destinationDimension;
	public BlockGatePortal(Material material) 
	{
		super(material);
		
		setUnlocalizedName("gatePortal");
		this.setCreativeTab(Minestuck.tabMinestuck);
		destinationDimension = Minestuck.skaiaDimensionId;
	}
	
	@Override
	public void setBlockBoundsBasedOnState(IBlockAccess access, BlockPos pos)
	{
		float var5 = 0.0625F;
		this.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, var5, 1.0F);
	}
	
	@Override
	public void onEntityCollidedWithBlock(World world, BlockPos pos, Entity entity)
	{
		if (entity.ridingEntity == null && entity.riddenByEntity == null && !world.isRemote && entity.timeUntilPortal == 0)
		{
			TileEntityGatePortal portal = (TileEntityGatePortal) world.getTileEntity(pos);
				portal.teleportEntity(entity);
		}
	}
	
	@SideOnly(Side.CLIENT)
	/**
	 * Returns true if the given side of this block type should be rendered, if the adjacent block is at the given
	 * coordinates.  Args: blockAccess, x, y, z, side
	 */
	@Override
	public boolean shouldSideBeRendered(IBlockAccess worldIn, BlockPos pos, EnumFacing side)
	{
		return side.getAxis().isHorizontal() ? false : super.shouldSideBeRendered(worldIn, pos, side);
	}
	
	@Override
	public boolean isOpaqueCube() {
		return false;
	}
	
	@Override
	public TileEntity createNewTileEntity(World world, int metadata)
	{
		TileEntityGatePortal tileEntity = (TileEntityGatePortal) this.createNewTileEntity(world);
		tileEntity.destination = new Location();
		tileEntity.destination.dim = this.destinationDimension;
		return tileEntity;
	}
	
	public TileEntity createNewTileEntity(World var1)
	{
		return new TileEntityGatePortal();
	}
	
	@Override
	public boolean isFullCube()
	{
		return false;
	}
	
	@Override
	public void addCollisionBoxesToList(World worldIn, BlockPos pos, IBlockState state, AxisAlignedBB mask, @SuppressWarnings("rawtypes") List list, Entity collidingEntity)
	{}
	
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
	public Item getItem(World worldIn, BlockPos pos)
	{
		return null;
	}
	
	/**
	 * Called whenever the block is added into the world. Args: world, x, y, z
	 */
	//this keeps portals that lead to the same world from existing
	@Override
	public void onBlockAdded(World worldIn, BlockPos pos, IBlockState state)
	{
		if (worldIn.provider.getDimensionId() == destinationDimension)
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
//	public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumFacing side, float hitX, float hitY, float hitZ)
//	{
//		if (worldIn.isRemote)
//		{
//			return true;
//		}
//		int newDimension = ((TileEntityGatePortal) worldIn.getTileEntity(pos)).destination.dim + 1;
//		if(worldIn.provider.getDimensionId() != newDimension && DimensionManager.isDimensionRegistered(newDimension))
//		{
//			this.destinationDimension = newDimension;
//			((TileEntityGatePortal) worldIn.getTileEntity(pos)).destination.dim = newDimension;
//		}
//		
//		return true;
//	}
	
	public void setDestinationDimension(World world, int x, int y, int z, int destinationDimension) 
	{
		((TileEntityGatePortal) world.getTileEntity(new BlockPos(x, y, z))).destination.dim = destinationDimension;
	}
	
}
