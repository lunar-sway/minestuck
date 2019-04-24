package com.mraof.minestuck.block;

import java.util.List;
import java.util.Map;

import javax.annotation.Nullable;

import com.mraof.minestuck.tileentity.TileEntityHolopad;

import net.minecraft.block.Block;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.Mirror;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;

public class BlockHolopad extends BlockMachine
{
	public static final Map<EnumFacing, VoxelShape> SHAPE = createRotatedShapes(2, 0, 1, 14, 6, 13);
	public static final Map<EnumFacing, VoxelShape> COLLISION_SHAPE;
	protected static final AxisAlignedBB HOLOPAD_TOP_AABB = new AxisAlignedBB(3/16F, 6/16F, 2.6/16F, 13/16F, 7/16F, 12.6/16F);
	protected static final AxisAlignedBB HOLOPAD_CARDSLOT_AABB = new AxisAlignedBB(4/16F, 0F, 13.8/16F, 12/16F, 10.1/16F, 15.94/16F);
	
	public static final BooleanProperty HAS_CARD = MinestuckProperties.HAS_CARD;
	
	static
	{
		VoxelShape topShape = Block.makeCuboidShape(3, 6, 3, 13, 7, 13);
		COLLISION_SHAPE = createRotatedShapes(4, 0, 14, 12, 10, 16);
		COLLISION_SHAPE.replaceAll((enumFacing, shape) -> VoxelShapes.or(shape, topShape));
	}
	
	public BlockHolopad(Properties builder)
	{
		super(builder);
	}
	
	@Override
	public boolean hasTileEntity(IBlockState state)
	{
		return true;
	}
	
	@Nullable
	@Override
	public TileEntity createTileEntity(IBlockState state, IBlockReader world)
	{
		return new TileEntityHolopad();
	}
	
	@Override
	public boolean onBlockActivated(IBlockState state, World worldIn, BlockPos pos, EntityPlayer player, EnumHand hand, EnumFacing side, float hitX, float hitY, float hitZ)
	{
		if(player.isSneaking()) return false;
		if(worldIn.isRemote)
			return true;
		TileEntity te = worldIn.getTileEntity(pos);
		
		if(te instanceof TileEntityHolopad)
			((TileEntityHolopad) te).onRightClick(player);
		return true;
	}
	
	@Override
	public void onBlockHarvested(World worldIn, BlockPos pos, IBlockState state, EntityPlayer player) 
	{
		TileEntityHolopad te = (TileEntityHolopad) worldIn.getTileEntity(pos);
		
		if(te != null && !worldIn.isRemote)
		{
			te.dropItem(true, worldIn, pos, te.getCard());
			te.destroyHologram(pos);
		}
		
		super.onBlockHarvested(worldIn, pos, state, player);
	}
	
	@Override
	protected void fillStateContainer(StateContainer.Builder<Block, IBlockState> builder)
	{
		super.fillStateContainer(builder);
		builder.add(HAS_CARD);
	}
	
	@Override
	public VoxelShape getShape(IBlockState state, IBlockReader worldIn, BlockPos pos)
	{
		return SHAPE.get(state.get(FACING));
	}
	
	@Override
	public VoxelShape getCollisionShape(IBlockState state, IBlockReader worldIn, BlockPos pos)
	{
		return COLLISION_SHAPE.get(state.get(FACING));
	}
	
	@Override
	public BlockRenderLayer getRenderLayer()
	{
		return BlockRenderLayer.CUTOUT_MIPPED;
	}
}