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
 * specifically for implementing fluid blocks that change fluid fog color on movement
 */
public class FlowingWaterColorsBlock extends LiquidBlock implements IMSFog
{
	private final float fogDensity;
	
	public FlowingWaterColorsBlock(Supplier<FlowingFluid> fluid, Properties properties)
	{
		this(fluid, 0, properties);
	}
	
	public FlowingWaterColorsBlock(Supplier<? extends FlowingFluid> fluid, float fogDensity, Properties properties)
	{
		super(fluid, properties);
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
		Vec3 newColor = new Vec3(0.0, 20.0, 30.0);
		newColor = newColor.yRot((float) (entity.getX() / 2.0));
		newColor = newColor.xRot((float) (entity.getZ() / 2.0));
		newColor = newColor.yRot((float) (entity.getY()));
		newColor = newColor.normalize();
		newColor = new Vec3(newColor.x % 1.0, newColor.y % 1.0, newColor.z % 1.0);
		
		return newColor;
	}
}
