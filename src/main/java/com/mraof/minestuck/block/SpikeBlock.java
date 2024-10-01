package com.mraof.minestuck.block;

import com.mraof.minestuck.util.MSDamageSources;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.pathfinder.BlockPathTypes;
import net.minecraft.world.phys.Vec3;

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
	public void fallOn(Level level, BlockState state, BlockPos pos, Entity entityIn, float fallDistance)
	{
		entityIn.causeFallDamage(fallDistance, 3, level.damageSources().fall());
	}
	
	/**
	 * Damages relevant entities as they move through the block
	 */
	@SuppressWarnings("deprecation")
	@Override
	public void entityInside(BlockState state, Level level, BlockPos pos, Entity entityIn)
	{
		if(entityIn instanceof LivingEntity && entityIn.fallDistance < 1)
		{
			if(!level.isClientSide && (entityIn.xOld != entityIn.getX() || entityIn.zOld != entityIn.getZ()))
			{
				double distanceX = Math.abs(entityIn.getX() - entityIn.xOld);
				double distanceZ = Math.abs(entityIn.getZ() - entityIn.zOld);
				
				entityIn.makeStuckInBlock(state, new Vec3(0.3F, 0.9, 0.3F));
				if(distanceX >= 0.003 || distanceZ >= 0.003)
				{
					entityIn.hurt(MSDamageSources.spike(level.registryAccess()), 1.0F);
				}
			}
		}
	}
	
	/**
	 * Helps entities avoid these blocks if possible
	 */
	@Nullable
	@Override
	public BlockPathTypes getBlockPathType(BlockState state, BlockGetter level, BlockPos pos, @Nullable Mob entity)
	{
		return BlockPathTypes.DAMAGE_OTHER;
	}
}
