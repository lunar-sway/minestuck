package com.mraof.minestuck.fluid;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.phys.Vec3;

/**
 * This should be implemented if a block/fluid needs to add a fog overlay
 */
public interface IMSFog
{
	float getMSFogDensity();
	Vec3 getMSFogColor(LevelReader level, BlockPos pos, Entity entity, Vec3 originalColor, double partialTicks);
}

