package com.mraof.minestuck.block;

import com.mraof.minestuck.util.CustomVoxelShape;
import com.mraof.minestuck.util.MSDamageSources;
import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.MobEntity;
import net.minecraft.pathfinding.PathNodeType;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;

import javax.annotation.Nullable;

/**
 * Increases damage from falls for living entities landing on it. Also injures living entities that move through it, in a manner similar to sweet berry bushes
 */
public class SpikeBlock extends CustomShapeBlock
{
	public SpikeBlock(Properties properties, CustomVoxelShape shape)
	{
		super(properties, shape);
	}
	
	@Override
	public void fallOn(World worldIn, BlockPos pos, Entity entityIn, float fallDistance)
	{
		entityIn.causeFallDamage(fallDistance, 3);
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
				
				entityIn.makeStuckInBlock(state, new Vector3d(0.3F, 0.9, 0.3F));
				if(distanceX >= 0.003 || distanceZ >= 0.003)
				{
					entityIn.hurt(MSDamageSources.SPIKE, 1.0F);
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
}
