package com.mraof.minestuck.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;

import java.util.Random;


public class MobSpawnerBlock extends Block
{
	public MobSpawnerBlock(Properties properties)
	{
		super(properties);
	}
	
	@Override
	public void randomTick(BlockState state, World worldIn, BlockPos pos, Random random)
	{
		if (worldIn.isAirBlock(pos.down()))
		{
			worldIn.removeBlock(pos, false);
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
	public BlockRenderType getRenderType(BlockState state)
	{
		return BlockRenderType.INVISIBLE;
	}
	
	@Override
	public VoxelShape getShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context)
	{
		return VoxelShapes.empty();
	}
	
	
	@Override
	public boolean isAir(BlockState state)
	{
		return true;
	}
	
	@Override
	public boolean isReplaceable(BlockState state, BlockItemUseContext useContext)
	{
		return true;
	}
}