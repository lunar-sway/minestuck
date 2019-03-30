package com.mraof.minestuck.block;

import net.minecraft.block.Block;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;

import java.util.Random;


public class BlockMobSpawner extends Block
{
	public BlockMobSpawner(Properties properties)
	{
		super(properties);
	}
	
	@Override
	public void randomTick(IBlockState state, World worldIn, BlockPos pos, Random random)
	{
		if (worldIn.isAirBlock(pos.down()))
		{
			worldIn.setBlockState(pos, Blocks.AIR.getDefaultState());
		}
		else
		{
	  //      EntityRabbit entity = new EntityRabbit(worldIn);
	  //      entity.setPosition(pos.getX() + .5, pos.getY(), pos.getZ() + .5);
	  //      entity.onInitialSpawn(null, null);
	  //      worldIn.spawnEntity(entity);
		}
	}
	
	@Override
	public EnumBlockRenderType getRenderType(IBlockState state)
	{
		return EnumBlockRenderType.INVISIBLE;
	}
	
	@Override
	public VoxelShape getShape(IBlockState state, IBlockReader worldIn, BlockPos pos)
	{
		return VoxelShapes.empty();
	}
	
	@Override
	public boolean isFullCube(IBlockState state)
	{
		return false;
	}
	
	@Override
	public boolean isAir(IBlockState state)
	{
		return true;
	}
	
	@Override
	public boolean isReplaceable(IBlockState state, BlockItemUseContext useContext)
	{
		return true;
	}
	
	@Override
	public BlockFaceShape getBlockFaceShape(IBlockReader worldIn, IBlockState state, BlockPos pos, EnumFacing face)
	{
		return BlockFaceShape.UNDEFINED;
	}
}