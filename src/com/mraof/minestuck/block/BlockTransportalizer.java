package com.mraof.minestuck.block;

import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import com.mraof.minestuck.Minestuck;
import com.mraof.minestuck.client.gui.GuiHandler;
import com.mraof.minestuck.tileentity.TileEntityTransportalizer;

public class BlockTransportalizer extends BlockContainer
{
	protected static final AxisAlignedBB TRANSPORTALIZER_AABB = new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1.0D, 1/2D, 1.0D);
	
	public BlockTransportalizer()
	{
		super(Material.IRON);
		this.setCreativeTab(Minestuck.tabMinestuck);
		this.setUnlocalizedName("transportalizer");
		this.setHardness(3.0F);
		setHarvestLevel("pickaxe", 0);
	}
	
	@Override
	public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos)
	{
		return TRANSPORTALIZER_AABB;
	}
	
	@Override
	public TileEntity createNewTileEntity(World world, int metadata)
	{
		TileEntityTransportalizer tileEntity = new TileEntityTransportalizer();
		return tileEntity;
	}
	
	@Override
	public void onEntityCollidedWithBlock(World world, BlockPos pos, IBlockState state, Entity entity)
	{
		if (!world.isRemote && entity.getRidingEntity() == null && entity.getPassengers().isEmpty() && !world.isRemote && entity.timeUntilPortal == 0)
		{
			((TileEntityTransportalizer) world.getTileEntity(pos)).teleport(entity);
		}
	}
	
	@Override
	public boolean isOpaqueCube(IBlockState state)
	{
		return false;
	}
	
	@Override
	public EnumBlockRenderType getRenderType(IBlockState state)
	{
		return EnumBlockRenderType.MODEL;
	}
	
	@Override
	public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, ItemStack heldItem, EnumFacing side, float hitX, float hitY, float hitZ)
	{
		TileEntityTransportalizer tileEntity = (TileEntityTransportalizer) world.getTileEntity(pos);

		if (tileEntity == null || player.isSneaking())
		{
			return false;
		}

		if(world.isRemote)
			player.openGui(Minestuck.instance, GuiHandler.GuiId.TRANSPORTALIZER.ordinal(), world, pos.getX(), pos.getY(), pos.getZ());

		return true;
	}
	
}
