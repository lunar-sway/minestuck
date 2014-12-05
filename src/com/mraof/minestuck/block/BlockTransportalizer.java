package com.mraof.minestuck.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import com.mraof.minestuck.Minestuck;
import com.mraof.minestuck.client.gui.GuiHandler;
import com.mraof.minestuck.tileentity.TileEntityTransportalizer;
import com.mraof.minestuck.util.Location;

public class BlockTransportalizer extends BlockContainer
{
	public BlockTransportalizer()
	{
		super(Material.iron);
		this.setCreativeTab(Minestuck.tabMinestuck);
		this.setBlockBounds(0F, 0F, 0F, 1F, 0.5F, 1F);
		this.setUnlocalizedName("transportalizer");
	}
	
	@Override
	public TileEntity createNewTileEntity(World world, int metadata)
	{
		TileEntityTransportalizer tileEntity = new TileEntityTransportalizer();
		return tileEntity;
	}
	
	@Override
	public void onEntityCollidedWithBlock(World world, BlockPos pos, Entity entity)
	{
		if (!world.isRemote && entity.ridingEntity == null && entity.riddenByEntity == null && !world.isRemote && entity.timeUntilPortal == 0)
		{
			((TileEntityTransportalizer) world.getTileEntity(pos)).teleport(entity);
		}
	}
	
	@Override
	public boolean isOpaqueCube() 
	{
		return false;
	}

	@Override
	public int getRenderType()
	{
		return -1;
	}
	
	@Override
	public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumFacing side, float hitX, float hitY, float hitZ)
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
