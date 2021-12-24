package com.mraof.minestuck.block;

import com.mraof.minestuck.entity.underling.UnderlingEntity;
import com.mraof.minestuck.util.CustomVoxelShape;
import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;

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
			entityIn.causeFallDamage(fallDistance, 1.5F); //causeFallDamage was onLivingFall
		} else if(entityIn instanceof LivingEntity)
		{
			entityIn.causeFallDamage(fallDistance, 3);
		}
		
		super.fallOn(worldIn, pos, entityIn, fallDistance);
	}
	
	/*@Override
	public void onFallenUpon(World worldIn, BlockPos pos, Entity entityIn, float fallDistance)
	{
		if(entityIn instanceof UnderlingEntity)
		{
			entityIn.onLivingFall(fallDistance, 1.5F);
		} else if(entityIn instanceof LivingEntity)
		{
			entityIn.onLivingFall(fallDistance, 3);
		}
		
		super.onFallenUpon(worldIn, pos, entityIn, fallDistance);
	}*/
	
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
	
	/*@Override
	public void onEntityCollision(BlockState state, World worldIn, BlockPos pos, Entity entityIn)
	{
		if(entityIn instanceof LivingEntity && entityIn.fallDistance < 1)
		{
			if(!worldIn.isRemote && (entityIn.lastTickPosX != entityIn.getPosX() || entityIn.lastTickPosZ != entityIn.getPosZ()))
			{
				double distanceX = Math.abs(entityIn.getPosX() - entityIn.lastTickPosX);
				double distanceZ = Math.abs(entityIn.getPosZ() - entityIn.lastTickPosZ);
				
				if(entityIn instanceof UnderlingEntity)
				{
					entityIn.setMotionMultiplier(state, new Vec3d(0.1F, 0.9, 0.1F));
					if(distanceX >= (double) 0.003F || distanceZ >= (double) 0.003F)
					{
						entityIn.attackEntityFrom(DamageSource.GENERIC, 0.25F);
					}
				} else
				{
					entityIn.setMotionMultiplier(state, new Vec3d(0.3F, 0.9, 0.3F));
					if(distanceX >= (double) 0.003F || distanceZ >= (double) 0.003F)
					{
						entityIn.attackEntityFrom(DamageSource.GENERIC, 1.0F);
					}
				}
			}
		}
	}*/
}
