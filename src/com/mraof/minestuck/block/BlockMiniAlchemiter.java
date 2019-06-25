package com.mraof.minestuck.block;

import com.mraof.minestuck.tileentity.TileEntityMiniAlchemiter;
import com.mraof.minestuck.util.IdentifierHandler;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.NetworkHooks;

import javax.annotation.Nullable;
import java.util.Map;

public class BlockMiniAlchemiter extends BlockMachineProcess
{
	public static final VoxelShape SHAPE = Block.makeCuboidShape(0, 0, 0, 16, 8, 16);
	public static final Map<EnumFacing, VoxelShape> COLLISION_SHAPE;
	
	static
	{
		COLLISION_SHAPE = createRotatedShapes(0, 2, 0, 4.5, 16, 2);
		COLLISION_SHAPE.replaceAll((enumFacing, shape) -> VoxelShapes.or(shape, SHAPE));
	}
	public BlockMiniAlchemiter(Properties properties)
	{
		super(properties);
	}
	
	@Override
	public VoxelShape getShape(IBlockState state, IBlockReader worldIn, BlockPos pos)
	{
		return SHAPE;
	}
	
	@Override
	public VoxelShape getCollisionShape(IBlockState state, IBlockReader worldIn, BlockPos pos)
	{
		return COLLISION_SHAPE.get(state.get(FACING));
	}
	
	@Override
	public boolean onBlockActivated(IBlockState state, World worldIn, BlockPos pos, EntityPlayer player, EnumHand hand, EnumFacing side, float hitX, float hitY, float hitZ)
	{
		TileEntity tileEntity = worldIn.getTileEntity(pos);
		if(!(tileEntity instanceof TileEntityMiniAlchemiter) || player.isSneaking())
			return false;
		
		if(!worldIn.isRemote)
		{
			TileEntityMiniAlchemiter alchemiter = (TileEntityMiniAlchemiter) tileEntity;
			alchemiter.owner = IdentifierHandler.encode(player);
			NetworkHooks.openGui((EntityPlayerMP) player, alchemiter, pos);
		}
		return true;
	}
	
	@Override
	public boolean hasComparatorInputOverride(IBlockState state)
	{
		return true;
	}
	
	// Will provide a redstone signal through a comparator with the output level corresponding to how many items can be alchemized with the player's current grist cache.
	// If no item can be alchemized, it will provide no signal to the comparator.
	@Override
	public int getComparatorInputOverride(IBlockState blockState, World worldIn, BlockPos pos)
	{
		TileEntity tileEntity = worldIn.getTileEntity(pos);
		if(tileEntity instanceof TileEntityMiniAlchemiter)
			return ((TileEntityMiniAlchemiter) tileEntity).comparatorValue();
		return 0;
	}
	
	@Override
	public boolean canConnectRedstone(IBlockState state, IBlockReader world, BlockPos pos, @Nullable EnumFacing side)
	{
		return side != null;
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
		return new TileEntityMiniAlchemiter();
	}
}