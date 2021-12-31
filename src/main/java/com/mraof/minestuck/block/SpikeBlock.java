package com.mraof.minestuck.block;

import com.mraof.minestuck.entity.underling.UnderlingEntity;
import com.mraof.minestuck.util.CustomVoxelShape;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.MobEntity;
import net.minecraft.pathfinding.PathNodeType;
import net.minecraft.state.StateContainer;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;

import javax.annotation.Nullable;

public class SpikeBlock extends DecorBlock
{
	public SpikeBlock(Properties properties, CustomVoxelShape shape)
	{
		super(properties, shape);
	}
	
	@Override
	public void fallOn(World worldIn, BlockPos pos, Entity entityIn, float fallDistance)
	{
		if(entityIn instanceof UnderlingEntity)
		{
			entityIn.causeFallDamage(fallDistance, 1.5F); //damage reduced for underlings
		} else if(entityIn instanceof LivingEntity)
		{
			entityIn.causeFallDamage(fallDistance, 3);
		}
		
		super.fallOn(worldIn, pos, entityIn, fallDistance);
	}
	
	/**
	 * Damages relevant entities as they move through the block
	 */
	@Override
	public void entityInside(BlockState state, World worldIn, BlockPos pos, Entity entityIn)
	{
		if(entityIn instanceof LivingEntity && entityIn.fallDistance < 1)
		{
			if(!worldIn.isClientSide && (entityIn.xOld != entityIn.getX() || entityIn.zOld != entityIn.getZ()))
			{
				double distanceX = Math.abs(entityIn.getX() - entityIn.xOld);
				double distanceZ = Math.abs(entityIn.getZ() - entityIn.zOld);
				
				if(entityIn instanceof UnderlingEntity)
				{
					entityIn.makeStuckInBlock(state, new Vector3d(0.1F, 0.9, 0.1F));
					if(distanceX >= (double) 0.003F || distanceZ >= (double) 0.003F)
					{
						entityIn.hurt(DamageSource.GENERIC, 0.25F);
					}
				} else
				{
					entityIn.makeStuckInBlock(state, new Vector3d(0.3F, 0.9, 0.3F));
					if(distanceX >= (double) 0.003F || distanceZ >= (double) 0.003F)
					{
						entityIn.hurt(DamageSource.GENERIC, 1.0F);
					}
				}
			}
		}
	}
	
	/**
	 * Helps entities avoid these blocks if possible
	 */
	@Nullable
	@Override
	public PathNodeType getAiPathNodeType(BlockState state, IBlockReader world, BlockPos pos, @Nullable MobEntity entity)
	{
		return PathNodeType.DAMAGE_OTHER;
	}
	
	@Override
	protected void createBlockStateDefinition(StateContainer.Builder<Block, BlockState> builder)
	{
		super.createBlockStateDefinition(builder);
	}
}
