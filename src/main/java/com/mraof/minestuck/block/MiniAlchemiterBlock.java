package com.mraof.minestuck.block;

import com.mraof.minestuck.tileentity.MiniAlchemiterTileEntity;
import com.mraof.minestuck.util.IdentifierHandler;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.NetworkHooks;

import javax.annotation.Nullable;
import java.util.Map;

public class MiniAlchemiterBlock extends MachineProcessBlock
{
	public static final Map<Direction, VoxelShape> SHAPE = MSBlockShapes.SMALL_ALCHEMITER.createRotatedShapes();
	//public static final Map<Direction, VoxelShape> COLLISION_SHAPE;
	
	static
	{
		//COLLISION_SHAPE = createRotatedShapes(0, 2, 0, 4.5, 16, 2);
		//COLLISION_SHAPE.replaceAll((enumFacing, shape) -> VoxelShapes.or(shape, SHAPE.ge));
	}
	public MiniAlchemiterBlock(Properties properties)
	{
		super(properties);
	}
	
	@Override
	public VoxelShape getShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context)
	{
		return SHAPE.get(state.get(FACING));
	}
	
	@Override
	public VoxelShape getCollisionShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context)
	{
		return SHAPE.get(state.get(FACING));
	}
	
	@Override
	public boolean onBlockActivated(BlockState state, World worldIn, BlockPos pos, PlayerEntity player, Hand handIn, BlockRayTraceResult hit)
	{
		TileEntity tileEntity = worldIn.getTileEntity(pos);
		if(!(tileEntity instanceof MiniAlchemiterTileEntity) || player.isSneaking())
			return false;
		
		if(!worldIn.isRemote)
		{
			MiniAlchemiterTileEntity alchemiter = (MiniAlchemiterTileEntity) tileEntity;
			alchemiter.owner = IdentifierHandler.encode(player);
			NetworkHooks.openGui((ServerPlayerEntity) player, alchemiter, pos);
		}
		return true;
	}
	
	@Override
	public boolean hasComparatorInputOverride(BlockState state)
	{
		return true;
	}
	
	// Will provide a redstone signal through a comparator with the output level corresponding to how many items can be alchemized with the player's current grist cache.
	// If no item can be alchemized, it will provide no signal to the comparator.
	@Override
	public int getComparatorInputOverride(BlockState blockState, World worldIn, BlockPos pos)
	{
		TileEntity tileEntity = worldIn.getTileEntity(pos);
		if(tileEntity instanceof MiniAlchemiterTileEntity)
			return ((MiniAlchemiterTileEntity) tileEntity).comparatorValue();
		return 0;
	}
	
	@Override
	public boolean canConnectRedstone(BlockState state, IBlockReader world, BlockPos pos, @Nullable Direction side)
	{
		return side != null;
	}
	
	@Override
	public boolean hasTileEntity(BlockState state)
	{
		return true;
	}
	
	@Nullable
	@Override
	public TileEntity createTileEntity(BlockState state, IBlockReader world)
	{
		return new MiniAlchemiterTileEntity();
	}
}