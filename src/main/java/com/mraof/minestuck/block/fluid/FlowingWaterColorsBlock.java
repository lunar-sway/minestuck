package com.mraof.minestuck.block.fluid;

import com.mraof.minestuck.fluid.IMSFog;
import net.minecraft.block.BlockState;
import net.minecraft.block.FlowingFluidBlock;
import net.minecraft.entity.Entity;
import net.minecraft.fluid.FlowingFluid;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.IWorldReader;

import java.util.function.Supplier;

/**
 * specifically for implementing fluid blocks that change fluid fog color on movement
 */
public class FlowingWaterColorsBlock extends FlowingFluidBlock implements IMSFog
{
	private float fogDensity;
	
	public FlowingWaterColorsBlock(Supplier<FlowingFluid> fluid, Properties properties)
	{
		this(fluid, 0 ,properties);
	}
	
	public FlowingWaterColorsBlock(Supplier<? extends FlowingFluid> fluid, float fogDensity, Properties properties)
	{
		super(fluid, properties);
		this.fogDensity = fogDensity;
	}
	
	@Override
	public Vec3d getFogColor(BlockState state, IWorldReader world, BlockPos pos, Entity entity, Vec3d originalColor, float partialTicks)
	{
		Vec3d newColor = new Vec3d(0.0, 20.0, 30.0);
		newColor = newColor.rotateYaw((float) (entity.getPosX() / 2.0));
		newColor = newColor.rotatePitch((float) (entity.getPosZ() / 2.0));
		newColor = newColor.rotateYaw((float) (entity.getPosY()));
		newColor = newColor.normalize();
		newColor = new Vec3d(newColor.x % 1.0, newColor.y % 1.0, newColor.z % 1.0);
		
		return newColor;
	}
	
	@Override
	public float getMSFogDensity()
	{
		return fogDensity;
	}
	
	@Override
	public Vec3d getMSFogColor(BlockState state, IWorldReader world, BlockPos pos, Entity entity, Vec3d originalColor, float partialTicks)
	{
		return this.getFogColor(state, world, pos, entity, originalColor, partialTicks);
	}
}
