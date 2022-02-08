package com.mraof.minestuck.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.state.IntegerProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraftforge.common.util.Constants;

import java.util.Random;

/**
 * If the block is stepped on by a player and there is nothing directly underneath it to support it, it crumbles
 */
public class FragileBlock extends Block
{
	protected FragileBlock(Properties properties)
	{
		super(properties);
	}
	
	@Override
	@SuppressWarnings("deprecation")
	public VoxelShape getShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context)
	{
		return Block.box(0, 0, 0, 16, 15, 16);
	}
	
	@Override
	public void entityInside(BlockState stateIn, World worldIn, BlockPos pos, Entity entityIn)
	{
		if(entityIn instanceof PlayerEntity)
		{
			//PlayerEntity steppingPlayer = (PlayerEntity) entityIn;
			BlockState blockStateBelow = worldIn.getBlockState(pos.below());
			
			if(blockStateBelow.isAir())
			{
				worldIn.destroyBlock(pos, false);
			}
		}
	}
}