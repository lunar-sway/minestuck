package com.mraof.minestuck.block.fluid;

import com.mraof.minestuck.fluid.IMSFog;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.LiquidBlock;
import net.minecraft.world.level.material.FlowingFluid;
import net.minecraft.world.phys.Vec3;

import java.util.function.Supplier;

/**
 * This class allows for implementing most fluid blocks in the game
 */
public class FlowingModFluidBlock extends LiquidBlock implements IMSFog
{
	protected final Vec3 fogColor;
	protected final float fogDensity;
	
	public FlowingModFluidBlock(Supplier<? extends FlowingFluid> fluid, Vec3 fogColor, float fogDensity, Properties properties)
	{
		super(fluid, properties);
		this.fogColor = fogColor;
		this.fogDensity = fogDensity;
	}
	
	@Override
	public float getMSFogDensity()
	{
		return fogDensity;
	}
	
	@Override
	public Vec3 getMSFogColor(LevelReader level, BlockPos pos, Entity entity, Vec3 originalColor, double partialTicks)
	{
		return fogColor;
	}
}