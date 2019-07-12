package com.mraof.minestuck.block;

import com.mraof.minestuck.tileentity.TileEntityUraniumCooker;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.NetworkHooks;

import javax.annotation.Nullable;

public class UraniumCookerBlock extends MachineProcessBlock
{
	public static final VoxelShape SHAPE = Block.makeCuboidShape(4, 0, 4, 12, 6, 12);
	
	public UraniumCookerBlock(Properties properties)
	{
		super(properties);
	}
	
	@Override
	public boolean onBlockActivated(BlockState state, World worldIn, BlockPos pos, PlayerEntity player, Hand handIn, BlockRayTraceResult hit)
	{
		TileEntity tileEntity = worldIn.getTileEntity(pos);
		
		if (!(tileEntity instanceof TileEntityUraniumCooker) || player.isSneaking())
			return false;
		
		if(!worldIn.isRemote)
		{
			NetworkHooks.openGui((ServerPlayerEntity) player, (TileEntityUraniumCooker) tileEntity, pos);
		}
		return true;
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
		return new TileEntityUraniumCooker();
	}
	
	@Override
	public VoxelShape getShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context)
	{
		return SHAPE;
	}
}