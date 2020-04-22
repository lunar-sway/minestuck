package com.mraof.minestuck.fluid;

import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.IWorldReader;

/**
 * This should be implemented if a block/fluid needs to add a fog overlay
 */
public interface IMSFog
{
	float getMSFogDensity();
	Vec3d getMSFogColor(BlockState state, IWorldReader world, BlockPos pos, Entity entity, Vec3d originalColor, float partialTicks);
}

